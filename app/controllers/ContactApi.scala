package controllers


import play.api.Logger
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

  def createContact = post(contactService)

  def findAllContacts = getAll(contactService)

  def findContactById(id: String) = getById(id)(contactService)

  def updateContact(id: String) = put(id)(contactService)

  def updateContactField(id: String) = putPartial(id)(contactService)

  def findContactsByAddress(address: String) = Action.async {

    val contactsByAddress: Future[Seq[JsObject]] = ContactService.findByAddress(address)
    contactsByAddress.map {
      contacts => //val count =contacts.size.toString
        Ok(Json.toJson(contacts))
    }
  }

  def findContactsByBloodGroup(bloodGroup: String) = Action.async {

    val contactsByBloodGroup: Future[Seq[JsObject]] = ContactService.findByBloodGroup(bloodGroup)
    contactsByBloodGroup.map {
      case Nil => NotFound
      case contacts: Seq[JsObject] =>
        Ok(Json.toJson(contacts))
    }
  }

  def findContactByPhoneNumber(phoneNumber: String) = Action.async {

    val query: JsObject = Json.obj("phoneNumber" -> phoneNumber)
    val contactsByPhoneNumber: Future[Option[JsObject]] = contactService.findOne(query)
    contactsByPhoneNumber.map {
      case None => NotFound
      case Some(t) => Ok(Json.toJson(t))
    }
  }

  def findContactByAddressAndBloodGroup(address: String, bloodGroup: String) = Action.async{
    val contactsFromAddressWithBloodGroup: Future[Seq[JsObject]] = ContactService.findByAddressAndBloodGroup(address,bloodGroup)
    contactsFromAddressWithBloodGroup.map{
      case Nil => NotFound
      case contacts: Seq[JsObject] => Ok(Json.toJson(contacts))
    }
  }

}