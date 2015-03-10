package controllers

import domains.MailOption
import factories.DaoFactory._
import factories.ServiceFactory._
import models.{Contact, Host}
import play.api.Logger
import play.api.libs.json.{JsValue, Reads, Json, JsObject}
import play.api.mvc.Action
import services.{ContactService, BaseService, HostService}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.api.mvc._

/**
 * Created by xplorer on 10/13/14.
 */
object HostApi extends BaseApi{

  val hostReader: Reads[Host] = implicitly[Reads[Host]]
  val hostService = new BaseService[Host](hostDao)
  //val contactService = new BaseService[Contact](contactDao)

  def createHost: EssentialAction= Action.async(parse.json){
    request=>{
      val userId = ""
      Json.fromJson(request.body)(hostReader).map{
        // request.body.validate[T].map{
        t => hostService.insert(t)(userId).map{
          val requiredBloodGroup = t.bloodGroup
          val hostLocation = t.location
          Logger.info(s"Requested bloodGroup: $requiredBloodGroup")

          //val contactsWithRequiredBloodGroup: Future[Seq[JsObject]] = ContactService.findByBloodGroup(requiredBloodGroup)
          //    contactsWithRequiredBloodGroup.map{
          //            contact=> contact.map{
          //              c=> val email = c.\("email").toString.replaceAll("\"","")
          //                Logger.info(s"email found : $email")
          //                  val mailBody = t.name+" needs " + bloodGroup + "please call him at " + t.phoneNumber
          //                Application.sendMail(email,mailBody)
          //                Logger.info(s" mail sent to  : $email")
          //            }
          //          }

          val contactsAll:Future[Seq[JsObject]] = contactService.findAll(Json.obj())
          contactsAll.map{
            contact => contact.map{
              c=>
                val contact = c.as[Contact]
                val mailOpt: Boolean = contact.mailOption.get
                val contactBloodGroup: String = contact.bloodGroup.get
                val contactAddress: String = contact.address.get
                val contactEmail: String = contact.email.get

                val mailBody = t.name+" needs " + requiredBloodGroup + " if u want 2 help, call him at " + t.phoneNumber

                mailOpt match {
                  case true => if(contactBloodGroup==requiredBloodGroup && contactAddress==hostLocation) Application.sendMail(contactEmail,mailBody)
                  case false => Logger.info(s"$contactEmail does not want 2 receive emails")
                }
            }
          }

          lastError =>
            Logger.info(s"Successfully inserted with LastError: $lastError")
            Ok(Json.toJson("created"))
        }
      }
    }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def findAllHosts = getAll(hostService)

  def findHostById(id: String) = getById(id)(hostService)

  def findHostsByBloodGroup(bloodGroup: String) = Action.async{
    val hostByGroup: Future[Seq[JsObject]] = HostService.findByBloodGroup(bloodGroup)
    hostByGroup.map{
      case Nil => NotFound
      case hosts: Seq[JsObject]=> Ok(Json.toJson(hosts))
    }
  }

  def findHostsByAddress(address: String)= Action.async{
    val hostByAddress: Future[Seq[JsObject]] = HostService.findByAddress(address)
    hostByAddress.map{
      case Nil => NotFound
      case hosts: Seq[JsObject]=>Ok(Json.toJson(hosts))
    }
  }

  def cleanStr(jsVal: JsValue): String ={
    jsVal.toString.replaceAll("\"","")
  }

}
