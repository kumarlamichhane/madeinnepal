package models

import play.api.libs.json.Json

/**
 * Created by xplorer on 8/21/14.
 */
case class Host(_id:Option[String],
                name: String,
                location: String,
                phoneNumber: String,
                bloodGroup: String)


  object Host{
    implicit  val format = Json.format[Host]

  }

object BloodGroup extends Enumeration{
  type BloodGroup = Value

}
