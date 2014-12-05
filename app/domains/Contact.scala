package models

import domains.MailOption
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

/**
 * Created by xplorer on 8/21/14.
 */
case class Contact(_id : Option[String] = Option(BSONObjectID.generate.stringify),
                    name: Option[String],
                   phoneNumber: Option[String],
                   bloodGroup: Option[String],
                   address: Option[String],
                   email:Option[String],
                   mailOption:Option[Boolean]
                   )



object Contact{

  implicit  val format = Json.format[Contact]

}

