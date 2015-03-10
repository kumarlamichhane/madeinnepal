package domains

import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

import scala.collection.mutable.ListBuffer

/**
 * Created by xplorer on 1/12/15.
 */

//case class ProductDTO(productID: String, productName: String, sellingPrice: Double)
//
//object ProductDTO{
//  implicit val format = Json.format[ProductDTO]
//
//}

case class CartItem(productID: Option[String], productName: String, price: Double, var count: Int)

object CartItem{

  implicit val format = Json.format[CartItem]
}

case class Cart(_id: String = BSONObjectID.generate.stringify,
                var cartItems: ListBuffer[CartItem] ,
                var count: Option[Int]= Some(0),
                var totalAmount: Double = 0.0
                 )

object Cart{
  
  implicit val format = Json.format[Cart]
  
}