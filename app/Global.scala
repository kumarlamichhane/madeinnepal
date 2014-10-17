
import play.api._
import play.api.http.Status
import play.api.libs.json.{Json, JsValue}
import play.api.mvc._
import play.api.mvc.Results._


import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("call4blood server has started.")

  }

  override def onError(request: RequestHeader, ex: Throwable) = {
    Future.successful(InternalServerError)
  }

  override def onHandlerNotFound(request: RequestHeader) = {
    Future.successful(NotFound)
  }

}