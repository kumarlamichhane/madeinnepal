package practices

import play.api.libs.json.Json


case class Contact(name: String, phone: String, mail: String)

object Contact{

  implicit val format = Json.format[Contact]
}