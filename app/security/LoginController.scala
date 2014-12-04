package security

import controllers.ContactApi._
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
        case _ => ???
      }

    }
  }



  //  object InterceptorSuperAdmin extends ActionBuilder[Request] {
//    type BlockType[A] = Request[A] => Future[SimpleResult]
//
//    def invokeBlock[A](request: Request[A], block: BlockType[A]) = {
//      val userIdInSession = request.session.get("userId").get.toString
//      val futureUser: Future[Option[JsObject]] = userService.findById(userIdInSession)
//      val userGroup = futureUser.map {
//        case None => Ok(Json.toJson(" user doesnot exist"))
//        case Some(u) => {
//          val userObj = u.as[User]
//          Logger.info(s"found user $u")
//          val group = userObj.group.get.toString
//          group
//        }
//      }
//      Logger.info(s"user group $userGroup")
//      userGroup.map {
//        case "SuperAdmin" => block(request)
//        case _ => Ok("Unauthorized")
//      }
//    }
//  }


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
          Logger.info(s" login user $t")
          val pass = t.as[User].password
          if(pass==passwordFromRequest) {
            Logger.info("password matched")
            val authToken = java.util.UUID.randomUUID.toString
            Logger.info(s"authZToken generated : $authToken")
          //  val session = createCall4BloodSession(t, authToken)
          //  sessionDao.create(session.doc)
            Ok("Success").withSession("userId"-> t.as[User]._id.get)
          }
          else
            NotFound(" wrong ")
        }
      }
    }
  }

  def logout: EssentialAction = Action.async{
    Future(Ok("logged out ").withNewSession)
  }

  private def createCall4BloodSession(user: JsObject, authToken: String): Call4BloodSession = {

    Call4BloodSession(Json.obj(
      "user" -> (user \ "_id" \ "$oid").as[String],
      "authToken" -> authToken
    ))
  }

}
