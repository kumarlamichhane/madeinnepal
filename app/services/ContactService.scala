package services

import domains.Contact
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

  def findByBloodGroup(bloodGroup: String): Future[Seq[JsObject]] = {
    val query = Json.obj("bloodGroup"-> bloodGroup)
    contactDao.readAll(query).map(_.map( js => transformMongoFields(js)))
  }

  def findByAddressAndBloodGroup(address: String, bloodGroup: String): Future[Seq[JsObject]] = {
    val query = Json.obj("address"-> address) ++ Json.obj("bloodGroup"->bloodGroup)
    contactDao.readAll(query).map(_.map(js=> transformMongoFields(js)))
  }

}
