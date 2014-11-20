
import play.api.libs.json.{Json, JsObject}
import play.api.libs.json.{Json, JsObject}
import play.api.mvc.Controller
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

object Test extends Controller {

  def main(args: Array[String]) {
    val futurePI = futurePIValue
    println(futurePI)
  }
  val futurePIValue: Future[Double] = Future {
    3.142857
  }

    futurePIValue.map{
      PI=> println(PI)
    }

  val PIValue: Double = 3.142857

















}