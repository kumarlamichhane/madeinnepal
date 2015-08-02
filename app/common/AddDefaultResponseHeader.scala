package common

import play.api.Logger
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object AddDefaultResponseHeader extends Filter {

  override def apply(f: (RequestHeader) => Future[SimpleResult])(rh: RequestHeader): Future[SimpleResult] ={
    val result = f(rh)
    Logger.info(s"${rh.method}")
    result.map(_.withHeaders(
            "Access-Control-Allow-Origin" -> "*",
            "Access-Control-Allow-Methods" -> "GET, OPTIONS, POST, DELETE, PUT",
            "Access-Control-Max-Age" -> "3600",
            "Access-Control-Allow-Headers" -> "Origin, Content-Type, Accept, Authorization",
            "Access-Control-Allow-Credentials" -> "true"
//            "Content-Type"->"application/json"

    ))
  }


}

