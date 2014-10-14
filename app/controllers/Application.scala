package controllers

import play.api.Logger
import play.api.libs.json.{JsValue, JsObject, Json}
import play.api.mvc._
import play.libs.Json
import reactivemongo.api.gridfs.GridFS
import services.BaseService
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Application extends Controller{


  def index = Action{
    Ok("...hello call4blood...")
  }

  def upload = Action.async(parse.multipartFormData) { request =>
    request.body.file("picture").map { picture =>
      import java.io.File
      val filename = picture.filename
      val contentType = picture.contentType
      picture.ref.moveTo(new File(s"./public/images/$filename"))
      Future {
        Ok("File uploaded")
      }
    }.getOrElse {
      Future {
        Redirect(routes.Application.index).flashing(
          "error" -> "Missing file"
        )
      }
    }
  }



}