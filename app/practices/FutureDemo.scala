package practices

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by nirajan on 11/21/14.
 */
object FutureDemo extends App {


  val f1 = Future{ "Hello" + "World" }
  val xLength = f1.map{
    x => x.length
  }
val k = xLength.flatMap(x=>Future(x))
  k


//  val f2 = f1 flatMap { x => Future(x.length) }
//  val f3 = f1 map {x => Future(x.length)}
//  f2.map(k => println("f2 : " +k))
//  f3.map(m => m map(l=>println(l)))
//  println("hello")
//  //val future3 = for(x <- f1; y <- f2) yield (x + y)



}
