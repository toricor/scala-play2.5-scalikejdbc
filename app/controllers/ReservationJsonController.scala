package controllers

import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scalikejdbc._
import models._

object ReservationJsonController {
  // ReservationRowをJSONに変換するためのWrites
  implicit val reservationRowWritesWrites = (
    (__ \ "id"  ).write[Int] and
      (__ \ "user_id").write[Int] and
      (__ \ "event_id").write[Int]
    )(unlift(Reservation.unapply))

  // 予約情報を受け取るためのケースクラス
  case class ReservationForm(id: Option[Int], user_id: Int, event_id: Int)

  // JSONをReservationFormにへんかんするためのReadsを定義
  implicit val reservationFormFormat = (
    (__ \ "id"  ).readNullable[Int] and
      (__ \ "user_id").read[Int] and
      (__ \ "event_id").read[Int]
    )(ReservationForm)
}

class ReservationJsonController extends Controller {

  import ReservationJsonController._
  /**
    * 一覧表示
    */
  def list = Action { implicit request =>
    val u = Reservation.syntax("u")

    DB.readOnly { implicit session =>
      // 予約のリストを取得
      val reservations = withSQL {
        select.from(Reservation as u).orderBy(u.id.asc)
      }.map(Reservation(u.resultName)).list.apply()

      // 予約の一覧をJSONで返す
      Ok(Json.obj("contents" -> reservations))
    }
  }

  /**
    * 予約登録
    */
  def create = Action(parse.json) { implicit request =>
    request.body.validate[ReservationForm].map { form =>
      // OKの場合は予約を登録
      DB.localTx { implicit session =>
        Reservation.create(form.user_id, form.event_id)
        Ok(Json.obj("result" -> "success"))
      }
    }.recoverTotal { e =>
      // NGの場合はバリデーションエラーを返す
      BadRequest(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
    }
  }

  /**
    * 予約更新
    */
  def update = Action(parse.json) { implicit request =>
    request.body.validate[ReservationForm].map { form =>
      // OKの場合は予約情報を更新
      DB.localTx { implicit session =>
        Reservation.find(form.id.get).foreach { reservation =>
          Reservation.save(reservation.copy(userId = form.user_id, eventId = form.event_id))
        }
        Ok(Json.obj("result" -> "success"))
      }
    }.recoverTotal { e =>
      // NGの場合はバリデーションエラーを返す
      BadRequest(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
    }
  }

  /**
    * 予約削除
    */
  def remove(id: Int) = Action { implicit request =>
    DB.localTx { implicit session =>
      // 予約を削除
      Reservation.find(id).foreach { reservation =>
        Reservation.destroy(reservation)
      }
      Ok(Json.obj("result" -> "success"))
    }
  }
}