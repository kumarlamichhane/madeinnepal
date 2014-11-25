package daos

import play.api.Play.current

import play.api.libs.json._
import play.modules.reactivemongo.ReactiveMongoPlugin
import play.modules.reactivemongo.json.collection.JSONCollection
import play.modules.reactivemongo.json.BSONFormats._
import reactivemongo.bson.BSONObjectID
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by xplorer on 8/22/14.
 */
class BaseDao[T](collectionName: String) {

  //CRUD
  //def collection: JSONCollection = db.collection[JSONCollection]("contacts")

  def db = ReactiveMongoPlugin.db

  def collection: JSONCollection = db[JSONCollection](collectionName)

  def create(t: JsObject):  Future[Unit]={
    collection.insert(t).map(_=>())
  }

  def readAll(t: JsObject):Future[Seq[JsObject]]={
    collection.find(t).sort(Json.obj("firstName"->1)).cursor[JsObject].collect[List]()
  }

  def readOne(t: JsObject): Future[Option[JsObject]]={
    collection.find(t).cursor[JsObject].headOption
  }

  def readById(id: BSONObjectID) : Future[Option[JsObject]]={
    collection.find(Json.obj("_id"->id )).cursor[JsObject].headOption
  }

  def update(id: BSONObjectID,t: JsObject): Future[Unit]={
    collection.update(Json.obj("_id" -> id), Json.obj("$set" -> t)).map(_=>())
  }

  def updatePartial(id: BSONObjectID, update: JsObject): Future[Unit]={
    collection.update(Json.obj("_id" -> id), Json.obj("$set" -> update)).map( _ => ())
  }

}
