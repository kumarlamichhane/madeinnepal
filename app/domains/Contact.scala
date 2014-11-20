package models

import play.api.libs.json.Json


/**
 * Created by xplorer on 8/21/14.
 */
case class Contact(firstName: String,
                   lastName: String,
                   phoneNumber: String,
                   bloodGroup: String,
                   address: String,
                   emailAddress:String)


object Contact{
  implicit  val format = Json.format[Contact]
}