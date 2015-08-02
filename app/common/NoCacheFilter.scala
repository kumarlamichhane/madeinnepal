//package filter
//
//import play.api.http.HeaderNames._
//import play.api.libs.concurrent.Execution.Implicits._
//import play.api.mvc._
//
//import scala.concurrent.Future
//
//
//object NoCacheFilter extends Filter {
//
//  def apply(f: (RequestHeader) => Future[Result])(rh: RequestHeader) = {
//
//    //add cache directives
//    val result = f(rh)
//    // TODO: Find a better way to identify the pages in future
//    result.map(_.withHeaders(
//      (CACHE_CONTROL, "no-cache, no-store, must-revalidate"),
//      (PRAGMA, "no-cache"),
//      (EXPIRES, "0")))
//
//  }
//
//}
//
