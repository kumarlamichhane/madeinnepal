package utils

import play.api.Logger
import play.api.libs.json._
import reactivemongo.bson.BSONDateTime

import scala.util.parsing.json.JSONObject

/**
 * Created by xplorer on 11/27/14.
 */
object CommonUtils {

  val log = Logger(this.getClass)
  
  object EnumUtils {

    def enumReads[E <: Enumeration](enum: E): Reads[E#Value] = new Reads[E#Value] {
      def reads(json: JsValue): JsResult[E#Value] = json match {
        case JsString(s) => {
          try {
            JsSuccess(enum.withName(s))
          } catch {
            case _: NoSuchElementException => JsError(s"Enumeration expected of type: '${enum.getClass}', but it does not appear to contain the value: '$s'")
          }
        }
        case _ => JsError("String value expected")
      }
    }

    def enumWrites[E <: Enumeration]: Writes[E#Value] = new Writes[E#Value] {
      def writes(v: E#Value): JsValue = JsString(v.toString)
    }

    def enumFormat[E <: Enumeration](enum: E): Format[E#Value] = {
      Format(enumReads(enum), enumWrites)
    }
  }

  object ServiceUtils{
    //todo add created date
   Logger.info("....writing meta info....")
    def metaInfoWriter(userId: String): JsObject = Json.obj("createdBy"->userId,"createdDate"->"")

  }

}

