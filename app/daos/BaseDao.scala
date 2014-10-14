package daos

import akka.actor.FSM.Failure
import play.api.{db, Logger}
import play.api.Play.current
import play.api.libs.iteratee.Enumerator
import play.api.libs.json._
import play.modules.reactivemongo.ReactiveMongoPlugin
import play.modules.reactivemongo.json.BSONFormats._
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.QueryOpts
import reactivemongo.bson.BSONObjectID
import reactivemongo.core.commands.LastError
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

}