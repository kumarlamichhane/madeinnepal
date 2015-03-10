package actors

import akka.actor._
import play.api.Logger
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import factories.ServiceFactory._
import scala.concurrent.duration._
import akka.util.Timeout

//case class Reply(msg: String)

case class Insert(product: domains.Product, userId: String)

class ProductActor extends Actor with ActorLogging{

  implicit val timeout = Timeout(10 seconds)
  
  def receive ={
    case Insert(product,userId)=>
     Logger.info(s"saving product: $product")
      val res = productService.insert(product)(userId).map(r=>r._2)
      val result = Await.result(res, timeout.duration)
     sender ! result
      
  }
}

//class ReplyActor extends Actor{
//
//  def receive ={
//    case Reply(msg) => Logger.info(s" reply: $msg")
//
//  }
//}

//object SaveMessage extends App {
//
//  val system = ActorSystem("my-system")
//  val me: ActorRef = system.actorOf(Props[MessageActor],"MessageActor")
// // val rep = system.actorOf(Props[ReplyActor],"ReplyActor")
// // me.tell(Message("hello"),rep)
//
//  system.shutdown()
//
//}