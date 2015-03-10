//package actors
//
//import akka.actor._
//import com.typesafe.plugin._
//import play.libs.Akka
//
///**
// * Created by xplorer on 2/9/15.
// */
//
//case class Mail(subject: String,
//                    recipient: String,
//                    sender: String,
//                    message: String
//                     )
//
//class EmailActor(name: String) extends Actor with ActorLogging {
//
//  import EmailActor.SendEmail
//
//  def receive = {
//    case SendEmail() => {
//      log.info("Sending Email")
//      //MailerAPI mailer = Play.application().plugin(MailerPlugin.class).email();
//      val mail = use[MailerPlugin].email
//      mail.setSubject("")
//      mail.setRecipient("kumarlamichhane13@gmail.com")
//      mail.setFrom("kumarlamichhane13@gmail.com")
//      //mail.addAttachment("favicon.png", new File(current.classloader.getResource("public/images/favicon.png").getPath))
//      //val data: Array[Byte] = "data".getBytes
//      //mail.addAttachment("data.txt", data, "text/plain", "A simple file", EmailAttachment.INLINE)
//      mail.sendHtml("")
//      log.info("Email sent!")
//    }
//  }
//
//}
//
//object EmailActor{
//  case class SendEmail()
//
//  def props(name: String): Props = Props(classOf[EmailActor], name)
//
//  val instance:ActorRef = Akka.system.actorOf(EmailActor.props("Mailer"))
//
//
//}