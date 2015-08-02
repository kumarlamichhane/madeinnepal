package security

import controllers._
import controllers.BaseApi
import factories.ServiceFactory._
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.Action
import security.LoginController.{ActionAdmin, ActionSuperAdmin}
import scala.concurrent.ExecutionContext.Implicits.global
import play.api._
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

/**
 * Created by xplorer on 11/26/14.
 */
object UserApi extends BaseApi{

  def createUser = ActionSuperAdmin.async(parse.json){
    request=>{
      Json.fromJson(request.body)(userService.reader).map{
        val userId = request.session.get("userId").get
        val passwordToken = java.util.UUID.randomUUID().toString
        t=>
          Logger.info(s".... inside  create user ....")
          val user = t.copy(password = passwordToken)
          val emailAddress = t.emailAddress.toString
          val username = t.username.toString
          Logger.info(s".... sending mail @ user .... $emailAddress")
       //   Application.sendMail(emailAddress,s"reset ur password, username: $username,  current password:$passwordToken")
          Logger.info(s".... creating user .... $user")
          userService.insert(user)(userId).map{
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

  def changePassword = ActionAdmin.async(parse.json){
    Logger.info(s"changing password")
    request => {
      val userId = request.session.get("userId").get.toString
      Logger.info(s"userId : $userId")
      val newPassword = request.body.\("password").toString().replaceAll("\"","")
      Logger.info(s"new password: $newPassword")
      val query =Json.obj("password"->newPassword)++Json.obj("status"->true)
      userService.updatePartial(userId,query).map(_=>Ok(Json.toJson("changed password")))

      Future(Ok(Json.toJson("password changed")))
    }

  }

  def userCount = getCount(userService)
  
}
