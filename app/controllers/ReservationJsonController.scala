/*
package controllers

import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scalikejdbc._
import models._

object ReservationJsonController {
  // ReservationRowをJSONに変換するためのWrites
  implicit val userRowWritesWrites = (
    (__ \ "id"  ).write[Int] and
      (__ \ "name").write[String]
    )(unlift(Reservation.unapply))


  // 予約情報を受け取るためのケースクラス
  case class ReservationForm(id: Option[Long], name: String)

  // JSONをReservationFormにへんかんするためのReadsを定義
  implicit val reservationFormFormat = (
    (__ \ "id"  ).readNullable[Long] and
      (__ \ "name").read[String]
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
      Ok(Json.obj("reservation" -> reservations))
    }
  }

  /**
    * 予約登録
    */
  def create = Action(parse.json) { implicit request =>
    request.body.validate[ReservationForm].map { form =>
      // OKの場合は予約を登録
      DB.localTx { implicit session =>
        Reservation.create(form.name)
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
          Reservation.save(reservation.copy(name = form.name, companyId = form.companyId))
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
  def remove(id: Long) = Action { implicit request =>
    DB.localTx { implicit session =>
      // 予約を削除
      Reservation.find(id).foreach { reservation =>
        Reservation.destroy(reservation)
      }
      Ok(Json.obj("result" -> "success"))
    }
  }
}

*/