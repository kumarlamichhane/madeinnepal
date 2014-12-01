package security

import play.api.libs.json.{Json, JsObject}
import services.BaseService
import factories.DaoFactory._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

/**
 * Created by xplorer on 11/26/14.
 */
object UserService extends BaseService[User](userDao) {

  def findUserByName(username: String): Future[Option[JsObject]] = {
    val query = Json.obj("username"->username)
    userDao.readOne(query).map( _.map( js => transformMongoFields(js)))
  }

}
