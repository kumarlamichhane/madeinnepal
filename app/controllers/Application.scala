  package controllers

  import play.api.libs.iteratee.{Iteratee, Enumerator}

  import scala.concurrent.duration._
  import java.io.{File, FileInputStream}

  import reactivemongo.api.gridfs.{ReadFile, DefaultFileToSave}
  import reactivemongo.api.gridfs.Implicits.DefaultReadFileReader
  import play.modules.reactivemongo.MongoController
  import play.api.mvc._
  import play.api._
  import com.typesafe.plugin._
  import domains._
  import play.api.libs.json.{JsValue, Reads, Json}
  import reactivemongo.bson.{BSONObjectID, BSONValue, BSONDocument}
  import play.api.libs.concurrent.Execution.Implicits.defaultContext
  import security.{User, UserService}
  import scala.concurrent.{Await, Future}
  import factories.ServiceFactory._
  import factories.DaoFactory._
  import play.api.Play.current

  object Application extends Controller with MongoController{



    def socket = WebSocket.using[String] { request =>

      // Log events to the console
      val in = Iteratee.foreach[String](println).map { _ =>
        println("Disconnected")
      }

      // Send a single 'Hello!' message
      val out = Enumerator("Hello!")

      (in, out)
    }


    //    def preflight = Action {
//      Ok("...").withHeaders(
//        "Access-Control-Allow-Origin" -> "*",
//        "Access-Control-Allow-Methods" -> "GET, OPTIONS, POST, DELETE, PUT",
//        "Access-Control-Max-Age" -> "3600",
//        "Access-Control-Allow-Headers" -> "Origin, Content-Type, Accept, Authorization",
//        "Access-Control-Allow-Credentials" -> "true"
//      )
//    }



    def singlePage(id: String) = Action{
      request=>
      if(request.session.get("cartID")!=None) {
        val cartId = request.session.get("cartID").get
        Logger.info(s"checking out cart with ID: $cartId")
        Ok(views.html.productdetail(cartId,id))
      }else{

        Ok(views.html.productdetail("None",id))
      }

    }
    
    def location = Action{
      Ok(views.html.location(" "))
    }

    def saveLocation= Action{
      request=>
        val host = request.remoteAddress
        val lat = request.getQueryString("lat")
        val long = request.getQueryString("long")
        Ok(Json.toJson(s" host: $host lat: $lat long: $long"))
    }
    
    def cart = Action{
      Ok(views.html.cart( ""))

    }

    def bill(id: String) = Action{
        Ok(views.html.bill(id))
    }
    
    def home = Action{
      request =>

      Ok(views.html.home(""))
    }

    def signUp = Action{
      Ok(views.html.signup(""))
    }
    
    def checkoutPage = Action{
      request=>
        if(request.session.get("cartID")!=None) {
          val cartId = request.session.get("cartID").get
          Logger.info(s"checking out cart with ID: $cartId")
          Ok(views.html.checkout(cartId))
        }else{

          Ok(views.html.checkout("None"))
        }

      
    }
  
    def productPage= Action{
      request=>
        if(request.session.get("userID")!=None) {
          val userId = request.session.get("userID").get
         // Ok(views.html.products(userId, Product.productForm))
          Ok(views.html.products(userId))
        }else{

          //Ok(views.html.products("None",Product.productForm))
          Ok(views.html.products("None"))
        }

      
    }
    
    def orderPage = Action{
      Ok(views.html.orders(" "))
      
    }

    def shop = Action{
      request=>
        if(request.session.get("cartID")!=None) {
          Logger.info(s"cartId in session directing 2 shop page")
          val cartId = request.session.get("cartID").get
          Ok(views.html.shop(cartId))
        }else{

          Ok(views.html.shop("None"))
        }

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
        fileService.insert(file)("")
        Future {
          Ok("File uploaded")
        }
      }.getOrElse {
        Future {
          Redirect(routes.Application.home).flashing(
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


    def sendmailtest(to: String, text: String) = Action{
      sendMail(to,text)
      Ok
    }

    def sendMail(to: String, body: String) = {
      val mail = use[MailerPlugin].email
      mail.setSubject("Made in Nepal")
      mail.addRecipient(to)
      mail.addFrom("kumarlamichhane13@gmail.com")
      //mail.addCc("","")
      mail.send(body)
      Logger.info(s" mail sent to  : $to")
      Future{Json.obj("mail sent to: " -> to)}
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
    // save the uploaded file as an attachment of the article with the given id
    def saveAttachment(id: String) = Action.async(gridFSBodyParser(gridFS)) { request =>
      // here is the future file!
      val futureFile = request.body.files.head.ref
      // when the upload is complete, we add the article id to the file entry (in order to find the attachments of the article)
      val futureUpdate = for {
        file <- futureFile
        // here, the file is completely uploaded, so it is time to update the article
        updateResult <- {
          gridFS.files. update(
            BSONDocument("_id" -> file.id),
            BSONDocument("$set" -> BSONDocument("contactID" -> id)))
        }
      } yield updateResult

      futureUpdate.map {
        case _ => Ok(" .....")
      }.recover {
        case e => InternalServerError(e.getMessage())
      }
    }

    def downloadAttachment(id: String) = Action.async { request =>
      // find the matching attachment, if any, and streams it to the client
      val file = gridFS.find(BSONDocument("_id" -> new BSONObjectID(id)))
      request.getQueryString("inline") match {
        case Some("true") => serve(gridFS, file, CONTENT_DISPOSITION_INLINE)
        case _            => serve(gridFS, file)
      }
    }

  }