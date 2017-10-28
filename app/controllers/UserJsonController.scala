package controllers

import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scalikejdbc._
import models._


object UserJsonController {
  // UserRowをJSONに変換するためのWrites
  implicit val userRowWritesWrites = (
      (__ \ "id"  ).write[Int] and
        (__ \ "name").write[String]
      )(unlift(User.unapply))

  // ユーザ情報を受け取るためのケースクラス
  case class UserForm(id: Option[Int], name: String)

  // JSONをUserFormにへんかんするためのReadsを定義
  implicit val userFormFormat = (
    (__ \ "id"  ).readNullable[Int] and
      (__ \ "name").read[String]
  )(UserForm)
}

class UserJsonController extends Controller {

  import UserJsonController._
  /**
    * 一覧表示
    */
  def list = Action { implicit request =>
    val u = User.syntax("u")

    DB.readOnly { implicit session =>
      // ユーザのリストを取得
      val users = withSQL {
        select.from(User as u).orderBy(u.id.asc)
      }.map(User(u.resultName)).list.apply()

      // ユーザの一覧をJSONで返す
      Ok(Json.obj("contents" -> users)).as("application/json; charset=utf-8")
    }
  }

  /**
    * ユーザ登録
    */
  def create = Action(parse.json) { implicit request =>
    request.body.validate[UserForm].map { form =>
      // OKの場合はユーザを登録
      DB.localTx { implicit session =>
        User.create(form.name)
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
        User.find(form.id.get).foreach { user =>
          User.save(user.copy(name = form.name))
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
  def remove(id: Int) = Action { implicit request =>
    DB.localTx { implicit session =>
      // ユーザを削除
      User.find(id).foreach { user =>
        User.destroy(user)
      }
      Ok(Json.obj("result" -> "success"))
    }
  }
}

