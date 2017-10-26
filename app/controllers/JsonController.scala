package controllers

import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scalikejdbc._
import models._


object JsonController {
  // UsersRowをJSONに変換するためのWrites
  implicit val usersRowWritesFormat = new Writes[Users]{
    def writes(user: Users): JsValue = {
      Json.obj(
        "id"        -> user.id,
        "name"      -> user.name,
        "companyId" -> user.companyId
      )
    }
  }

  // ユーザ情報を受け取るためのケースクラス
  case class UserForm(id: Option[Long], name: String, companyId: Option[Int])

  // JSONをUserFormにへんかんするためのReadsを定義
  implicit val userFormFormat = (
    (__ \ "id"  ).readNullable[Long] and
      (__ \ "name").read[String]     and
      (__ \ "companyId").readNullable[Int]
  )(UserForm)

}

class JsonController extends Controller {

  import JsonController._
  /**
    * 一覧表示
    */
  def list = Action { implicit request =>
    val u = Users.syntax("u")

    DB.readOnly { implicit session =>
      // ユーザのリストを取得
      val users = withSQL {
        select.from(Users as u).orderBy(u.id.asc)
      }.map(Users(u.resultName)).list.apply()

      // ユーザの一覧をJSONで返す
      Ok(Json.obj("users" -> users))
    }
  }

  /**
    * ユーザ登録
    */
  def create = Action(parse.json) { implicit request =>
    request.body.validate[UserForm].map { form =>
      // OKの場合はユーザを登録
      DB.localTx { implicit session =>
        Users.create(form.name, form.companyId)
        Ok(Json.obj("result" -> "success"))
      }
    }.recoverTotal { e =>
      // NGの場合はバリデーションエラーを返す
      BadRequest(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
    }
  }

  /**
    * ユーザ更新
    */
  def update = Action(parse.json) { implicit request =>
    request.body.validate[UserForm].map { form =>
      // OKの場合はユーザ情報を更新
      DB.localTx { implicit session =>
        Users.find(form.id.get).foreach { user =>
          Users.save(user.copy(name = form.name, companyId = form.companyId))
        }
        Ok(Json.obj("result" -> "success"))
      }
    }.recoverTotal { e =>
      // NGの場合はバリデーションエラーを返す
      BadRequest(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
    }
  }

  /**
    * ユーザ削除
    */
  def remove(id: Long) = Action { implicit request =>
    DB.localTx { implicit session =>
      // ユーザを削除
      Users.find(id).foreach { user =>
        Users.destroy(user)
      }
      Ok(Json.obj("result" -> "success"))
    }
  }
}

