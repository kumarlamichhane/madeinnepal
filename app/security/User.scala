package security


import utils.CommonUtils._
import play.api.Logger
import play.api.libs.json.{Writes, Reads, JsObject, Json}
import play.api.mvc.{Controller, Action}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Created by xplorer on 11/25/14.
 */
case  class LoginEvent(username: String, password: String)

object LoginEvent{
  implicit val format = Json.format[LoginEvent]
}

case class User( _id: Option[String], username: String, password: String, group: Option[Group.Group])

object User{
  implicit val format = Json.format[User]
 // val superAdmin = new User("kumar" , "1234" , Group.Super_Admin)

}

object Group extends Enumeration{
  type Group = Value
  val SuperAdmin, Admin, Other = Value

  implicit val readGroup: Reads[Group] = EnumUtils.enumReads(Group)
  implicit val writeGroup: Writes[Group] = EnumUtils.enumWrites

}