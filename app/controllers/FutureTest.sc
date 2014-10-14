
import akka.actor.Status.Success

import scala.concurrent.Future._
import scala.concurrent.Future
import play.api._
import play.api.mvc.Controller
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

object Test extends Controller{

  def main (args: Array[String]) {
    val futurePI = futurePIValue
    println(futurePI)
  }

  val futurePIValue: Future[Double] = computePIAsynchronously
  def computePIAsynchronously: Future[Double] = Future{
    3.142857
  }

  futurePIValue.onComplete{
    case Success(result) => println(result)

  }

}