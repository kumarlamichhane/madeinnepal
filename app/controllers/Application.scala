  package controllers

  import play.modules.reactivemongo.MongoController
  import play.api.mvc._
  import play.api._
  import com.typesafe.plugin._
  import domains._
  import play.api.libs.iteratee.{Iteratee, Enumerator}
  import play.api.libs.json.{Reads,Json}
  import reactivemongo.api.gridfs.{ReadFile, DefaultFileToSave}
  import reactivemongo.bson.{BSONObjectID, BSONValue, BSONDocument}
  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.Future
  import factories.ServiceFactory._
  import factories.DaoFactory._
  import play.api.Play.current

  object Application extends Controller with MongoController{


    def index = Action{
      Ok("...hello call4blood...")
    }

    def uploadFile(contactId: String,reference: String) = Action.async(parse.multipartFormData) { request =>
      val refCheck = request.body.files(0).ref
      Logger.info(s"ref ????: $refCheck")
      request.body.file("picture").map { picture =>
        import java.io.File
        val filename = picture.filename
        val contentType = picture.contentType

        Logger.info(s"content-type: $contentType")
        Logger.info(s"filename: $filename")
        //createFolder(contactId)
        picture.ref.moveTo(new java.io.File(s"./public/images/"+contactId+".jpg"))

        //File(name: String, mimeType: String, description: String, path: String, reference: Reference)
        val file : UploadFile = UploadFile(contactId, " " ," ", " ",reference)
        fileService.insert(file)
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


    def downloadFile(fileId: String)=Action{

      Ok.sendFile(new java.io.File("./public/images/"+fileId+".jpg"))

  //     val fileContent:  Enumerator[Array[Byte]] = Enumerator.fromFile(file)
  //    SimpleResult(
  //      header = ResponseHeader(200),
  //      body = fileContent
  //    )
    }

  //  def uploadStream(contactId: String) = Action.async(parse.file(to = new File("./public/images/"+contactId+".jpg"))) {
  //    request=> Future{Ok}
  //  }


    def sendMail(to: String,body: String) = {
      val mail = use[MailerPlugin].email
      mail.setSubject("call 4 blood")
      mail.addRecipient(to)
      mail.addFrom("kumarlamichhane13@gmail.com")
      mail.send(body)
      Future{Ok(Json.obj("mail sent to: " -> to))}
    }

    def createFolder(contactId: String):Boolean = {
      val folder = new java.io.File("./public/images"+ "/"+contactId)
      try {
        folder.mkdirs()
        true
      }catch  {
        case _: Throwable =>
          false
      }
    }


  }