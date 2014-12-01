package security

import controllers.BaseApi
import factories.ServiceFactory._
import play.api.libs.json.Json
import play.api.mvc.Action
import scala.concurrent.ExecutionContext.Implicits.global
import play.api._

/**
 * Created by xplorer on 11/26/14.
 */
object UserApi extends BaseApi{

  def createUser = post(userService)

  def findAllUsers = getAll(userService)

  def findUserById(id: String) = getById(id)(userService)

  def findUserByUsername(username: String) = Action.async{

        val user = UserService.findUserByName(username)
        user map {
          case None => NotFound
          case Some(user) => Ok(Json.toJson(user))
        }
  }

}
