package domains

import play.api.libs.json.Json

/**
 * Created by xplorer on 10/14/14.
 */
case class Mail(email: String)

object Mail{

  implicit val format = Json.format[Mail]
}
