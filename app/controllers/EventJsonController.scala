/*
package controllers

import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scalikejdbc._
import models._

object EventJsonController {
  // EventRowをJSONに変換するためのWrites
  implicit val eventRowWritesWrites = (
    (__ \ "id"  ).write[Int] and
      (__ \ "name").write[String]
    )(unlift(Event.unapply))


  // イベント情報を受け取るためのケースクラス
  case class EventForm(id: Option[Long], name: String)

  // JSONをEventFormにへんかんするためのReadsを定義
  implicit val eventFormFormat = (
    (__ \ "id"  ).readNullable[Long] and
      (__ \ "name").read[String]
    )(EventForm)
}

class EventJsonController extends Controller {

  import EventJsonController._
  /**
    * 一覧表示
    */
  def list = Action { implicit request =>
    val u = Event.syntax("u")

    DB.readOnly { implicit session =>
      // イベントのリストを取得
      val events = withSQL {
        select.from(Event as u).orderBy(u.id.asc)
      }.map(Event(u.resultName)).list.apply()

      // イベントの一覧をJSONで返す
      Ok(Json.obj("event" -> events))
    }
  }

  /**
    * イベント登録
    */
  def create = Action(parse.json) { implicit request =>
    request.body.validate[EventForm].map { form =>
      // OKの場合はイベントを登録
      DB.localTx { implicit session =>
        Event.create(form.name)
        Ok(Json.obj("result" -> "success"))
      }
    }.recoverTotal { e =>
      // NGの場合はバリデーションエラーを返す
      BadRequest(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
    }
  }

  /**
    * イベント更新
    */
  def update = Action(parse.json) { implicit request =>
    request.body.validate[EventForm].map { form =>
      // OKの場合はイベント情報を更新
      DB.localTx { implicit session =>
        Event.find(form.id.get).foreach { event =>
          Event.save(event.copy(name = form.name, companyId = form.companyId))
        }
        Ok(Json.obj("result" -> "success"))
      }
    }.recoverTotal { e =>
      // NGの場合はバリデーションエラーを返す
      BadRequest(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
    }
  }

  /**
    * イベント削除
    */
  def remove(id: Long) = Action { implicit request =>
    DB.localTx { implicit session =>
      // イベントを削除
      Event.find(id).foreach { event =>
        Event.destroy(event)
      }
      Ok(Json.obj("result" -> "success"))
    }
  }
}

*/
