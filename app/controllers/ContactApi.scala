package controllers


import play.api.mvc._
import play.modules.reactivemongo.{ReactiveMongoPlugin, MongoController}
import play.api.libs.json._
import reactivemongo.bson.{BSONValue, BSONDocument,BSONObjectID}
import reactivemongo.api.gridfs.{GridFS, ReadFile, DefaultFileToSave}
import services.ContactService
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import factories.ServiceFactory._

/**
 * Created by xplorer on 9/18/14.
 */
object ContactApi extends BaseApi {

  def create = post(contactService)

  def findAll = getAll(contactService)

  def findById(id: String) = getById(id)(contactService)

  def update(id: String) = put(id)(contactService)

  def findByAddress(address: String) = Action.async {

    val contactsByAddress: Future[Seq[JsObject]] = ContactService.findByAddress(address)
    contactsByAddress.map {
      contacts => //val count =contacts.size.toString
        Ok(Json.toJson(contacts))
    }
  }

  def findByBloodGroup(bloodGroup: String) = Action.async {

    val query: JsObject = Json.obj("bloodGroup" -> bloodGroup)
    val contactsByBloodGroup: Future[Seq[JsObject]] = contactService.findAll(query)
    contactsByBloodGroup.map {
      case Nil => NotFound
      case contacts: Seq[JsObject] => val count= contacts.size.toString
        val jsCount= Json.obj("count"->count)
        val jsContacts = Json.obj("contacts"->contacts)
        Ok(jsContacts++jsCount)
    }
  }

  def findByPhoneNumber(phoneNumber: String) = Action.async {

    val query: JsObject = Json.obj("phoneNumber" -> phoneNumber)
    val contactsByPhoneNumber: Future[Option[JsObject]] = contactService.findOne(query)
    contactsByPhoneNumber.map {
      case None => NotFound
      case Some(t) => Ok(Json.toJson(t))
    }
  }

  def count(address: String) = Action.async {
    val count: Future[String] = ContactService.countContacts(address)
    count.map(count=>Ok(Json.obj("count"->count)))
  }

}