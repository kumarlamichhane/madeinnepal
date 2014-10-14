package services

import models.Host
import factories.DaoFactory._
import play.api.libs.json.{Json, JsObject}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

/**
 * Created by xplorer on 10/13/14.
 */
object HostService extends BaseService[Host](hostDao){

  def findByBloodGroup(bloodGroup: String): Future[Seq[JsObject]] = {
    val query = Json.obj("bloodGroup" -> bloodGroup)
    hostDao.readAll(query).map( _.map( js => transformMongoFields(js)))

  }

}
