package domains

import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

/**
 * Created by xplorer on 1/12/15.
 */
case class CustomerOrder(_id: Option[String]= Option(BSONObjectID.generate.stringify),
                          metaInfo: Option[MetaInfo],
                          customerID: String,
                          cartID: String
                          )


object CustomerOrder{
  implicit val format = Json.format[CustomerOrder]
  
}
