

import play.api.libs.json.{Json, JsObject}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
val contact1: JsObject = Json.obj("name"->"kumar","phone"->"984189377","email"->"kumarlamichhane@gmail.com",
  "mailOption"->Json.obj("all"->true, "address"-> true))

val contact2: JsObject = Json.obj("name"->"amit","phone"->"9803693318","email"->"kumarlamichhane@gmail.com")
val contacts: Future[Seq[JsObject]] = Future(List(contact1,contact2))
contact1.\("email").toString.replaceAll("\"","")
val email = contacts.map{
  contact=> contact.map{
    c=> val emails = c.\("email").toString
  }
}

contact1.\("mailOption")