package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._
import java.time.{OffsetDateTime}
import scalikejdbc.jsr310._


class EventSpec extends Specification {

  "Event" should {

    val e = Event.syntax("e")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Event.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Event.findBy(sqls.eq(e.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Event.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Event.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Event.findAllBy(sqls.eq(e.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Event.countBy(sqls.eq(e.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Event.create(title = "MyString", description = "MyString", author = 123, place = "MyString", participants = 123, maxParticipants = 123, publishedAt = null, createdAt = null)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Event.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Event.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Event.findAll().head
      val deleted = Event.destroy(entity) == 1
      deleted should beTrue
      val shouldBeNone = Event.find(123)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = Event.findAll()
      entities.foreach(e => Event.destroy(e))
      val batchInserted = Event.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
