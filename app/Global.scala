
import common.{AddDefaultResponseHeader}
import factories.DaoFactory._
import play.api._
import play.api.http.Status
import play.api.libs.json.{JsObject, Json, JsValue}
import play.api.mvc._
import play.api.mvc.Results._
import security.{Group, User}


import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("nepalikapas server has started.")
    userDao.readOne(Json.obj()).map{
      case None=> {
        val superAdmin = User(emailAddress = "kumarlamichhane13@gmail.com",username =  "kumar",password="1234",status=true,group= Some(Group.SuperAdmin), phoneNumber =  "9841389377")
        userDao.create(Json.toJson(superAdmin).as[JsObject])
      }
      case Some(user) => Logger.info("none")
    }

  }

//  override def doFilter(next: EssentialAction): EssentialAction = {
//    Filters(super.doFilter(next), AddDefaultResponseHeader)
//  }

  override def onError(request: RequestHeader, ex: Throwable) = {
    Future.successful(InternalServerError)
  }

  override def onHandlerNotFound(request: RequestHeader) = {
    Future.successful(NotFound)
  }

}
