package services

import models.Contact
import factories.DaoFactory._
import play.api.libs.json.{Reads, Json, JsObject}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Created by xplorer on 9/18/14.
 */
object ContactService extends BaseService[Contact](contactDao){

    def findByAddress(address: String): Future[Seq[JsObject]] = {
      val query = Json.obj("address" -> address)
      contactDao.readAll(query).map( _.map( js => transformMongoFields(js)))

    }

  def countContacts(address: String): Future[String] = {
    val contactWithAddress: Future[Seq[JsObject]] = findByAddress(address)
    contactWithAddress.map{contacts=>
      val count= contacts.size.toString
      count
    }



  }



}