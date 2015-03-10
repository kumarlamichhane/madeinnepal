package domains

import play.api.libs.json.Json
import reactivemongo.bson.{BSONObjectID, BSON}

/**
 * Created by xplorer on 1/3/15.
 */
case class Product(_id: Option[String]= Option(BSONObjectID.generate.stringify),
                    metaInfo : Option[MetaInfo],
                    name: Option[String],
                    category: Option[String],
                    count: Option[Int],
                    costPrice: Option[Double],
                    sellingPrice: Option[Double],
                    description: Option[String],
                    featured: Option[Boolean],
                    gender: Option[String])

object Product{
  implicit val format = Json.format[Product]
}

