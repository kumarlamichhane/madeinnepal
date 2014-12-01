package models

import play.api.libs.json.Json

/**
 * Created by xplorer on 8/21/14.
 */
case class Host(name: String,
                location: String,
                phoneNumber: String,
                bloodGroup: String)


  object Host{
    implicit  val format = Json.format[Host]

  }

