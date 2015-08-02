package domains

import play.api.data.format.Formats._
import play.api.data._
import play.api.data.Forms._
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
                    featured: Option[Boolean] = Some(false),
                    gender: Option[String])

object Product{

  implicit val format = Json.format[Product]

  case class InsertProduct(product: Product)
  case class ChangeCostPrice(_id: String, costPrice: Double)
  case class ChangeSellingPrice(_id: String, sellingPrice: Double)
  case class Feature(_id: String, featured: Boolean=true)

  val productForm: Form[Product] = Form(
    mapping(
      "_id" -> optional(text),
      "metaInfo" -> optional(mapping(
        "createdBy" -> optional(text),
        "createdDate" -> optional(jodaDate)
      )(MetaInfo.apply)(MetaInfo.unapply)),
    "name"->optional(text),
      "category"->optional(text),
      "count"->optional(number),
      "costPrice"->optional(of[Double]),
      "sellingPrice"->optional(of[Double]),
      "description"->optional(text),
      "featured"->optional(boolean),
      "gender"->optional(text)
    )(Product.apply)(Product.unapply)
  )
}

