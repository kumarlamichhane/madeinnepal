package models

import play.api.libs.json.Json


/**
 * Created by xplorer on 8/21/14.
 */
case class Contact(name: String,
                   phoneNumber: Option[String],
                   bloodGroup: String,
                   address: Option[String],
                   email:Option[String],
                   mailOption: Option[MailOption])



object Contact{
  implicit  val format = Json.format[Contact]
}


case class MailOption(bloodGroup: Option[Boolean], address: Option[Boolean] , all: Option[Boolean], none: Option[Boolean])