/*
package models

import scalikejdbc._
import java.time.{OffsetDateTime}

case class Event(
  id: Int,
  title: String,
  description: String,
  author: Int,
  place: String,
  participants: Int,
  maxParticipants: Int,
  publishedAt: OffsetDateTime,
  createdAt: OffsetDateTime) {

  def save()(implicit session: DBSession = Event.autoSession): Event = Event.save(this)(session)

  def destroy()(implicit session: DBSession = Event.autoSession): Int = Event.destroy(this)(session)

}


object Event extends SQLSyntaxSupport[Event] {

  override val schemaName = Some("PUBLIC")

  override val tableName = "event"

  override val columns = Seq("id", "title", "description", "author", "place", "participants", "max_participants", "published_at", "created_at")

  def apply(e: SyntaxProvider[Event])(rs: WrappedResultSet): Event = apply(e.resultName)(rs)
  def apply(e: ResultName[Event])(rs: WrappedResultSet): Event = new Event(
    id = rs.get(e.id),
    title = rs.get(e.title),
    description = rs.get(e.description),
    author = rs.get(e.author),
    place = rs.get(e.place),
    participants = rs.get(e.participants),
    maxParticipants = rs.get(e.maxParticipants),
    publishedAt = rs.get(e.publishedAt),
    createdAt = rs.get(e.createdAt)
  )

  val e = Event.syntax("e")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[Event] = {
    withSQL {
      select.from(Event as e).where.eq(e.id, id)
    }.map(Event(e.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Event] = {
    withSQL(select.from(Event as e)).map(Event(e.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Event as e)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Event] = {
    withSQL {
      select.from(Event as e).where.append(where)
    }.map(Event(e.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Event] = {
    withSQL {
      select.from(Event as e).where.append(where)
    }.map(Event(e.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Event as e).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    title: String,
    description: String,
    author: Int,
    place: String,
    participants: Int,
    maxParticipants: Int,
    publishedAt: OffsetDateTime,
    createdAt: OffsetDateTime)(implicit session: DBSession = autoSession): Event = {
    val generatedKey = withSQL {
      insert.into(Event).namedValues(
        column.title -> title,
        column.description -> description,
        column.author -> author,
        column.place -> place,
        column.participants -> participants,
        column.maxParticipants -> maxParticipants,
        column.publishedAt -> publishedAt,
        column.createdAt -> createdAt
      )
    }.updateAndReturnGeneratedKey.apply()

    Event(
      id = generatedKey.toInt,
      title = title,
      description = description,
      author = author,
      place = place,
      participants = participants,
      maxParticipants = maxParticipants,
      publishedAt = publishedAt,
      createdAt = createdAt)
  }

  def batchInsert(entities: Seq[Event])(implicit session: DBSession = autoSession): List[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'title -> entity.title,
        'description -> entity.description,
        'author -> entity.author,
        'place -> entity.place,
        'participants -> entity.participants,
        'maxParticipants -> entity.maxParticipants,
        'publishedAt -> entity.publishedAt,
        'createdAt -> entity.createdAt))
        SQL("""insert into event(
        title,
        description,
        author,
        place,
        participants,
        max_participants,
        published_at,
        created_at
      ) values (
        {title},
        {description},
        {author},
        {place},
        {participants},
        {maxParticipants},
        {publishedAt},
        {createdAt}
      )""").batchByName(params: _*).apply[List]()
    }

  def save(entity: Event)(implicit session: DBSession = autoSession): Event = {
    withSQL {
      update(Event).set(
        column.id -> entity.id,
        column.title -> entity.title,
        column.description -> entity.description,
        column.author -> entity.author,
        column.place -> entity.place,
        column.participants -> entity.participants,
        column.maxParticipants -> entity.maxParticipants,
        column.publishedAt -> entity.publishedAt,
        column.createdAt -> entity.createdAt
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Event)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(Event).where.eq(column.id, entity.id) }.update.apply()
  }

}
*/