package security

import controllers._
import controllers.BaseApi
import factories.ServiceFactory._
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.Action
import security.LoginController.ActionSuperAdmin
import scala.concurrent.ExecutionContext.Implicits.global
import play.api._

import scala.concurrent.Future

/**
 * Created by xplorer on 11/26/14.
 */
object UserApi extends BaseApi{

  def createUser = Action.async(parse.json){
    request=>{
      Json.fromJson(request.body)(userService.reader).map{
      val passwordToken = java.util.UUID.randomUUID().toString
      t=>
        Logger.info(s".... inside  create user ....")
        val user = t.copy(password = passwordToken)
        val emailAddress = t.emailAddress.toString
        val username = t.username.toString
       //  Logger.info(s".... sending mail @ user .... $emailAddress")
       // Application.sendMail(emailAddress,s"reset ur password, username: $username,  current password:$passwordToken")
        Logger.info(s".... creating user .... $user")
        userService.insert(user).map{
          Logger.info(s".... created user .... $user")
          output => Ok(Json.toJson("created"))
      }
    }
    }.getOrElse(Future.successful(BadRequest(Json.toJson("invalid json"))))
  }

  def findAllUsers = getAll(userService)

  def findUserById(id: String) = getById(id)(userService)

  def findUserByUsername(username: String) = Action.async{

        val user = UserService.findUserByName(username)
        user map {
          case None => NotFound
          case Some(user) => Ok(Json.toJson(user))
        }
  }

  def changePassword(id: String) = Action.async(parse.json){
    request =>{
      val password = request.body.as[User].password
      Json.fromJson(request.body)(userService.reader).map{
        user =>
          val newUser = user.copy(password=password, status=true)
          userService.insert(newUser).map(_=>Ok(Json.toJson("password changed")))
      }
    }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

}
