package security

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
/**
 * Created by xplorer on 12/2/14.
 */
case class Call4BloodSession(doc: JsObject) {

  def user = (doc \ "user").asOpt[String]
  def authToken = (doc \ "authToken").as[String]

}

object Call4BloodSession{

  implicit val sessionFormat = Json.format[Call4BloodSession]

  val validateCall4BloodSession: Reads[JsObject] = (
    ((__ \ "user").json.pickBranch) and
      (__ \ "authToken").json.pickBranch
    ).reduce
}