package models

import play.api.libs.json.Json

/**
 * Created by xplorer on 8/21/14.
 */
case class Host(_id:Option[String],
                firstName: String,
                lastName: String,
                address:String,
                phoneNumber: String,
                bloodGroup: String)


  object Host{
    implicit  val format = Json.format[Host]

  }
