package daos

import play.api.Logger
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

  val log = Logger(this.getClass)
  def db = ReactiveMongoPlugin.db

  def collection: JSONCollection = db[JSONCollection](collectionName)

  //return should b Future[Option[T]] or ?
  def create(t: JsObject):  Future[(BSONObjectID,JsObject)]={
    
    def appendObjId(id: BSONObjectID): JsObject = t ++ Json.obj("_id" -> id)
    t \ "_id" match {
      case u: JsUndefined =>
        val id = BSONObjectID.generate
        val obj = appendObjId(id)
        Logger.info( s"...creating..inside BaseDao fo JsUndefined: $collectionName")
        collection.insert(obj) map (_ => (id, obj))
      case idStr: JsString =>
        val id = BSONObjectID.parse(idStr.as[String]).get
        val obj = appendObjId(id)
        Logger.info( s"...creating..inside BaseDao fo JsString: $collectionName")
        collection.insert(obj) map (_ => (id, obj))
      case id: JsObject =>
        Logger.info( s"...creating..inside BaseDao fo JsObject: $collectionName")
        collection.insert(t) map (_ => (id.as[BSONObjectID], t) )
      case t => throw new IllegalStateException(s"Invalid _id type $t")
    }
    
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

  def update(id: BSONObjectID,t: JsObject): Future[BSONObjectID]={
    Logger.info(s"inside dao update $t")
    collection.update(Json.obj("_id" -> id), Json.obj("$set" -> t)).map(_=>id)
  }

  def updatePartial(id: BSONObjectID, update: JsObject): Future[Unit]={
    Logger.info(s"partial update object: $update")
    collection.update(Json.obj("_id" -> id), Json.obj("$set" -> update)).map( _ => ())
  }

  def pushIntoArray(id: BSONObjectID, update: JsObject): Future[Unit] = {
    collection.update(Json.obj("_id" -> id),Json.obj("$push" -> update)).map( _ => ())
    
  }

  def delete(id: BSONObjectID): Future[BSONObjectID]={
    collection.remove(Json.obj("_id" -> id)).map(_ => id)

  }
  
//  //todo transport it to cartdao
//  def updateCountOfCartItem(id: BSONObjectID,proID: String,newCount: Int) = {
//    collection.update(Json.obj("_id"->id,"cartItems.productID"->proID),Json.obj("$set"->Json.obj("cartItems.$.count"->newCount))).map(_=>())
//    
//  }
  

  
}
