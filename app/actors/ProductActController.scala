package actors

import akka.actor.{Props, ActorRef, ActorSystem}
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import akka.pattern.ask
import scala.concurrent.duration._
import scala.concurrent.Await
import akka.util.Timeout
/**
 * Created by xplorer on 2/13/15.
 */
object ProductActController extends Controller {

  implicit val timeout = Timeout(10 seconds)
  val system = ActorSystem("my-system")
  val productActor: ActorRef = system.actorOf(Props[ProductActor], "ProductActor")

  def saveProduct = Action.async(parse.json) {
    request => {
      val userId = "" //request.session.get("userId").get
      val product = request.body.validate[domains.Product].get
      //tell/! fire and forget
      //productActor ! Insert(product, userId)
      
      //ask/? fire and accept the response
      val res = productActor.ask(Insert(product, userId)).mapTo[domains.Product]
      //val result = Await.result(res, timeout.duration)

      res.map(s=>Ok(Json.toJson(s)))
    }
  }
}
