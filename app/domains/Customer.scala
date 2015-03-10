package domains

import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

/**
 * Created by xplorer on 1/12/15.
 */

case class Location(longitude: Double, latitude: Double)

object Location{
  implicit val format = Json.format[Location]
  
}

case class Address(location: Option[Location],
                   city: String,
                   street: String,
                   houseNumber: String)


object Address{
  implicit val format = Json.format[Address]

}

case class Customer(_id: Option[String]= Option(BSONObjectID.generate.stringify),
                     name: String,
                     phone: String,
                     email: Option[String],
                     address:Option[Address]
                     )

object Customer{
  implicit val format = Json.format[Customer]
  
}

