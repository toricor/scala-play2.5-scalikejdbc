package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._


class ReservationSpec extends Specification {

  "Reservation" should {

    val r = Reservation.syntax("r")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Reservation.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Reservation.findBy(sqls.eq(r.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Reservation.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Reservation.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Reservation.findAllBy(sqls.eq(r.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Reservation.countBy(sqls.eq(r.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Reservation.create(userId = 123, eventId = 123)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Reservation.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Reservation.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Reservation.findAll().head
      val deleted = Reservation.destroy(entity) == 1
      deleted should beTrue
      val shouldBeNone = Reservation.find(123)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = Reservation.findAll()
      entities.foreach(e => Reservation.destroy(e))
      val batchInserted = Reservation.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
