package practices

import play.api.libs.json.{Json, JsObject}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import practices.Contact._

object Practice{

  def toInt(s: String): Option[Int]={

    try{
      Some(s.toInt)
    }catch{
      case e: NumberFormatException => None
    }
}

  println(toInt("3").get)
  println(toInt("a").getOrElse("BOOM IS NOT A NUMERIC VALUE"))

  var three = toInt("2") match {
    case Some(int) if (int == 3 )=> println(int)
    case Some(int) if (int == 2) => println(int)
  }

}

object call4blood extends App{

  val contact1: JsObject = Json.obj("name"->"kumar","phone"->"984189377","mail"->"kumarlamichhane@gmail.com",
    "mailOption"->Json.obj("all"->true, "address"-> true))
 val contact2: JsObject = Json.obj("name"->"amit","phone"->"9803693318","mail"->"achaulagain123@gmail.com" ,
   "mailOption"->Json.obj("all"->true, "address"-> true))

  //println(contact1.\("mail").toString.replaceAll("\"",""))

 val futureContacts: Future[Seq[JsObject]] = Future(List(contact1,contact2))

 val futureContact: Future[Option[JsObject]] = Future(Option(contact1))


 val fContact= futureContact.map{
   contacts => println
 }


}

