package security

import controllers.Application
import controllers.Application._
import factories.ServiceFactory._
import play.api.mvc._
import play.api._
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.Action
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import factories.DaoFactory._
import scala.concurrent.duration._


/**
 * Created by xplorer on 11/20/14.
 */
object LoginController extends Controller {

  //todo create UserWrapped request
  //case class AuthenticatedRequest (val userId: String, request: Request[AnyContent]) extends WrappedRequest(request)
  
  object ActionSuperAdmin extends ActionBuilder[Request] {
    
    def invokeBlock[A](request: Request[A], block: Request[A] => Future[SimpleResult]) = {
      
      val userIdInSession = request.session.get("userId").get.toString
      val futureUser: Future[Option[JsObject]] = userService.findById(userIdInSession)
      val userGroup = futureUser.map{
        case None => Ok(Json.toJson("not present"))
        case Some(u) => {
          val userObj = u.as[User]
          Logger.info(s"found user $u")
          val group = userObj.group.get.toString
          group
        }
      }
      Logger.info(s"user group $userGroup")

      val group = Await.result(userGroup, 10 seconds)

      group match {
        case "SuperAdmin" => block(request)
        case _ => Future(Ok(Json.toJson("unauthorized")))
      }

    }
  }

  def admin = Action{
    Ok(views.html.admin(" "))
  }

  def adminHome = Action{
      Ok(views.html.adminhome(" "))
  }

  def getUserInSession = Action.async{
    request=>
      val userId = request.session.get("userId").get.toString
      val futureUser = UserService.findById(userId)
      val user = Await.result(futureUser,10 seconds)
      Logger.info(s"user in session: $user")
      val userJsObj = user.get
    Future(Ok(Json.toJson(userJsObj)))
  }

  def login: EssentialAction = Action.async(parse.json){
    implicit request => {
      val userFromRequest = request.body.as[LoginEvent]
      val userNameFromRequest = userFromRequest.username
      Logger.info(s"username from req: $userNameFromRequest")
      val passwordFromRequest = userFromRequest.password
      Logger.info(s"password from req: $passwordFromRequest")

      val userFromDB: Future[Option[JsObject]] = UserService.findUserByName(userNameFromRequest)
      userFromDB map {
        case None => NotFound
        case Some(t) => {

          val user = t.as[User]
          Logger.info(s"username form db $user")
          val id = user._id.get.toString
          val pass = user.password
          val status = user.status

          if(pass==passwordFromRequest) {
            Logger.info("password matched")

            if(status.equals(true)){
              Logger.info(s"status: $status " )
              Ok(Json.toJson("success")).withSession("userId"-> id)

            }else{
              Logger.info(s"status: $status " )
              Ok(Json.toJson("change")).withSession("userId"-> id)

            }
          //  val session = createCall4BloodSession(t, authToken)
          //  sessionDao.create(session.doc)

          }
          else {
            Logger.info(s"password incorrect")
            BadRequest(Json.toJson(" Incorrect Password "))
          }
        }
      }
    }
  }

  def logout: EssentialAction = Action.async{
    Future(Ok(Json.toJson("logged out")).withNewSession)
  }


  def adminPasswordChange = Action{
    request=>
      val userId = request.session.get("userId").get.toString
      val futureUser = UserService.findById(userId)
      val user = Await.result(futureUser,10 seconds)
      val userName = user.get.as[User].username

    Ok(views.html.passwordchange(userName))
  }

  private def createCall4BloodSession(user: JsObject, authToken: String): Call4BloodSession = {

    Call4BloodSession(Json.obj(
      "user" -> (user \ "_id" \ "$oid").as[String],
      "authToken" -> authToken
    ))
  }

}
