package services



import models.Contact
import org.joda.time.format.ISODateTimeFormat
import play.api.libs.json.Reads._
import play.api.libs.json._
import daos.BaseDao
import reactivemongo.bson.BSONObjectID
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.matching.Regex

/**
 * Created by xplorer on 8/23/14.
 */

class BaseService[T: Format](dao: BaseDao[T]) {

  //insert,find,update,delete

  val reader: Reads[T] = implicitly[Reads[T]]
  val writer: Writes[T] = implicitly[Writes[T]]

  def insert(entity: T):  Future[Unit]={
    val t:JsObject = writer.writes(entity).as[JsObject]
    dao.create(t)
  }

  def findAll(t: JsObject): Future[Seq[JsObject]]={
    dao.readAll(t).map( _.map( js => transformMongoFields(js)))
  }

  def findOne(t: JsObject):Future[Option[JsObject]]={
    dao.readOne(t).map( _.map( js => transformMongoFields(js)))
  }

  def findById(id: String):Future[Option[JsObject]]={
    val bid: BSONObjectID= BSONObjectID.parse(id).get
    dao.readById(bid).map( _.map( js => transformMongoFields(js)))
  }

  def update(id: String,entity: T) : Future[Unit]={
    val bid: BSONObjectID= BSONObjectID.parse(id).get
    val t: JsObject = writer.writes(entity).as[JsObject]
    dao.update(bid,t)
  }

  protected def transformMongoFields(jsObject: JsObject): JsObject = {

    val jsObjectWithStringifiedId = (jsObject \ "_id") match {
      case s: JsString => jsObject
      case o: JsObject => jsObject.transform(writeId).get
      case _ => jsObject // no id present
    }
    val repl = Json.stringify(jsObjectWithStringifiedId)
    Json.parse(repl).as[JsObject]
  }

  val writeId = (__.json.update((__ \ '_id).json.copyFrom((__ \ '_id \'$oid).json.pick)))
}
