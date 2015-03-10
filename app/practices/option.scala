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
   contacts => contacts
 }


  val flat = futureContact.flatMap(contacts=>Future(contacts.get))



  val list = List(1,2,3,4,5)

  val doubleList = list.map {
    l => println(l * 2)
  }

  val fx =  (x: Int) => if(x>2) x else 0

  val listfx = list.map(s=> fx(s))




}

