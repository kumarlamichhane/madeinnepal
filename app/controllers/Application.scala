package controllers

import com.typesafe.plugin._
import play.api.Play.current
import domains.Mail
import play.api.libs.json.{Reads,Json}
import play.api.mvc._
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

  val mailReader: Reads[Mail] = implicitly[Reads[Mail]]
  def mail = Action.async(parse.json){
    request=>{
      Json.fromJson(request.body)(mailReader).map{
        mailData => {
          val mail = use[MailerPlugin].email
          mail.setSubject("Email sent using Scala")
          mail.addRecipient(mailData.email)
          mail.addFrom(mailData.email)
          mail.send("Hello World")
          Future{Ok("mail sent 2 " + mailData.email)}
        }
      }
    }.getOrElse(Future.successful(BadRequest("invalid json")))

  }


}