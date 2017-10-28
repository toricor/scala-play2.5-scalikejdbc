/*
package models

import scalikejdbc._

case class Reservation(
  id: Int,
  userId: Int,
  eventId: Int) {

  def save()(implicit session: DBSession = Reservation.autoSession): Reservation = Reservation.save(this)(session)

  def destroy()(implicit session: DBSession = Reservation.autoSession): Int = Reservation.destroy(this)(session)

}


object Reservation extends SQLSyntaxSupport[Reservation] {

  override val tableName = "reservation"

  override val columns = Seq("id", "user_id", "event_id")

  def apply(r: SyntaxProvider[Reservation])(rs: WrappedResultSet): Reservation = apply(r.resultName)(rs)
  def apply(r: ResultName[Reservation])(rs: WrappedResultSet): Reservation = new Reservation(
    id = rs.get(r.id),
    userId = rs.get(r.userId),
    eventId = rs.get(r.eventId)
  )

  val r = Reservation.syntax("r")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[Reservation] = {
    withSQL {
      select.from(Reservation as r).where.eq(r.id, id)
    }.map(Reservation(r.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Reservation] = {
    withSQL(select.from(Reservation as r)).map(Reservation(r.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Reservation as r)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Reservation] = {
    withSQL {
      select.from(Reservation as r).where.append(where)
    }.map(Reservation(r.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Reservation] = {
    withSQL {
      select.from(Reservation as r).where.append(where)
    }.map(Reservation(r.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Reservation as r).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    userId: Int,
    eventId: Int)(implicit session: DBSession = autoSession): Reservation = {
    val generatedKey = withSQL {
      insert.into(Reservation).namedValues(
        column.userId -> userId,
        column.eventId -> eventId
      )
    }.updateAndReturnGeneratedKey.apply()

    Reservation(
      id = generatedKey.toInt,
      userId = userId,
      eventId = eventId)
  }

  def batchInsert(entities: Seq[Reservation])(implicit session: DBSession = autoSession): List[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'userId -> entity.userId,
        'eventId -> entity.eventId))
        SQL("""insert into reservation(
        user_id,
        event_id
      ) values (
        {userId},
        {eventId}
      )""").batchByName(params: _*).apply[List]()
    }

  def save(entity: Reservation)(implicit session: DBSession = autoSession): Reservation = {
    withSQL {
      update(Reservation).set(
        column.id -> entity.id,
        column.userId -> entity.userId,
        column.eventId -> entity.eventId
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Reservation)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(Reservation).where.eq(column.id, entity.id) }.update.apply()
  }

}
*/