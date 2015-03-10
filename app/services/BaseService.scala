package services



import models.Contact
import org.joda.time.format.ISODateTimeFormat
import play.api.Logger
import play.api.libs.json.Reads._
import play.api.libs.json._
import daos.BaseDao
import reactivemongo.bson.BSONObjectID
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.matching.Regex
import utils.CommonUtils.ServiceUtils._
/**
 * Created by xplorer on 8/23/14.
 */

class BaseService[T: Format](dao: BaseDao[T]) {

  //insert,find,update,remove
  val log = Logger(this.getClass)
  val reader: Reads[T] = implicitly[Reads[T]]
  val writer: Writes[T] = implicitly[Writes[T]]

  def insert(domain: T)(userId: String):  Future[(String,T)]={
    Logger.info(s"inside base service with: $domain")
    val t:JsObject = writer.writes(domain).as[JsObject]
    val tWithMeta = t.deepMerge(metaInfoWriter(userId))
    Logger.info(s"inside base service with: $tWithMeta")
    dao.create(tWithMeta).map(t=>(t._1.stringify,transformMongoFields(t._2).as[T]))
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

  def update(id: String, domain: T) : Future[String]={
    Logger.info(s"inside service update $domain")
    val bid: BSONObjectID= BSONObjectID.parse(id).get
    val t: JsObject = writer.writes(domain).as[JsObject]
    dao.update(bid,t).map(_ => bid.stringify)
  }

  def updatePartial(id: String, jsObject: JsObject): Future[Unit] = {
    val bid: BSONObjectID= BSONObjectID.parse(id).get
    dao.updatePartial(bid,jsObject)
  }

  def remove(id: String): Future[String]={
    val bid: BSONObjectID= BSONObjectID.parse(id).get
    dao.delete(bid).map(_=> bid.stringify)
    
  }

//  def getAsObject(id: String): Future[Option[T]] = {
//
//    try {
//      val bid = BSONObjectID.parse(id).get
//      for {
//        jso <- findOne(Json.obj("_id" -> Json.toJsFieldJsValueWrapper(bid)))
//
//          } yield jso.get.asOpt[T]
//
//    } catch {
//      case e: Throwable => Future.failed(e)
//    }
//  }
  
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
