package domains

import play.api.libs.json.Json

/**
 * Created by xplorer on 11/20/14.
 */
case class LoginEvent(userName: String, password: String)


object LoginEvent{

  implicit val format = Json.format[LoginEvent]
}
