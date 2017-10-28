package controllers

import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scalikejdbc._
import models._
import org.joda.time.DateTime;

object EventJsonController {
  // EventRowをJSONに変換するためのWrites
  implicit val eventRowWritesWrites = (
    (__ \ "id"  ).write[Int] and
      (__ \ "title").write[String] and
      (__ \ "description").write[String] and
      (__ \ "author").write[Int] and
      (__ \ "place").write[String] and
      (__ \ "participants").write[Int] and
      (__ \ "max_participants").write[Int] and
      (__ \ "published_at").write[DateTime] and
      (__ \ "created_at").write[DateTime]
    )(unlift(Event.unapply))

  // イベント情報を受け取るためのケースクラス
  case class EventForm(
                        id: Option[Int],
                        title: String,
                        description: String,
                        author: Int,
                        place: String,
                        participants: Int,
                        max_participants: Int,
                        published_at: DateTime,
                        created_at: DateTime
                      )

  // JSONをEventFormに変換するためのReadsを定義
  implicit val eventFormFormat = (
    (__ \ "id"  ).readNullable[Int] and
      (__ \ "title").read[String] and
      (__ \ "description").read[String] and
      (__ \ "author").read[Int] and
      (__ \ "place").read[String] and
      (__ \ "participants").read[Int] and
      (__ \ "max_participants").read[Int] and
      (__ \ "published_at").read[DateTime] and
      (__ \ "created_at").read[DateTime]
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
      Ok(Json.obj {
        "contents" -> events
      }).as("application/json; charset=utf-8")
    }
  }

  /**
    * イベント登録
    */
  def create = Action(parse.json) { implicit request =>
    request.body.validate[EventForm].map { form =>
      // OKの場合はイベントを登録
      DB.localTx { implicit session =>
        Event.create(
          form.title,
          form.description,
          form.author,
          form.place,
          form.participants,
          form.max_participants,
          form.published_at,
          form.created_at)
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
          Event.save(event.copy(title           = form.title))
          Event.save(event.copy(description     = form.description))
          Event.save(event.copy(author          = form.author))
          Event.save(event.copy(place           = form.place))
          Event.save(event.copy(participants    = form.participants))
          Event.save(event.copy(maxParticipants = form.max_participants))
          Event.save(event.copy(publishedAt     = form.published_at))
          Event.save(event.copy(createdAt       = form.created_at))
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
  def remove(id: Int) = Action { implicit request =>
    DB.localTx { implicit session =>
      // イベントを削除
      Event.find(id).foreach { event =>
        Event.destroy(event)
      }
      Ok(Json.obj("result" -> "success"))
    }
  }
}

