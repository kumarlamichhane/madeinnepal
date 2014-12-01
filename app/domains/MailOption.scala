package domains

import play.api.libs.json.Json

/**
 * Created by xplorer on 12/1/14.
 */
case class MailOption(optBloodGroup: Option[Boolean] = Some(false),
                      optAddressBloodGroup: Option[Boolean],
                      optAddress: Option[Boolean] = Some(false),
                      optAll: Option[Boolean] = Some(false),
                      optNone: Option[Boolean]= Some(false))

object MailOption{

  implicit  val format = Json.format[MailOption]

}
