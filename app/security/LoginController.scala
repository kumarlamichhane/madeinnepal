package security

import play.api.mvc._
import play.api._
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.Action
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


/**
 * Created by xplorer on 11/20/14.
 */
object LoginController extends Controller {


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
          val pass = t.\("password").toString.replaceAll("\"", "")
          if(pass==passwordFromRequest) {
            //val authToken = java.util.UUID.randomUUID.toString
            Ok("right").withCookies(Cookie("user", userNameFromRequest, httpOnly = false))
          }
          else
            NotFound(" wrong ")
        }
      }
    }
  }

  def logout: EssentialAction = Action.async{
    Future(Ok("discarding cookie ").discardingCookies(DiscardingCookie("user")))
  }

}
