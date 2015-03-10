package services

import controllers.Application
import domains.{CustomerOrder, Cart, Customer}
import factories.DaoFactory._
import play.api.Logger
import play.api.libs.json.{Writes, JsValue, Json, JsObject}
import factories.ServiceFactory._
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
/**
 * Created by xplorer on 1/12/15.
 */
object CustomerService extends BaseService[Customer](customerDao) {


  def checkOut(t: JsValue): Future[JsValue] = {

    //todo create the bill and return it
    Logger.info(s"inside customer service with $t")
    val customer = t.as[Customer]
    val email = customer.email.get
    Logger.info(s"customer wants 2 checkout $customer")
    val cart = CartService.cart1
    val cartJs = Json.toJson[Cart](cart)(implicitly[Writes[Cart]]).as[JsObject]
    //todo cannot checkout an empty cart===done
    if (cart.cartItems.isEmpty) {
      Logger.info("cart is empty need no checkout")
      Future(Json.toJson("cart is empty"))
    } else {
      CartService.persistCart(cart)
      val cartID = cart._id
      CartService.removeCart(cartID)
      for {
        customerID <- CustomerService.insert(customer)("")
        order = CustomerOrder(None, None, customerID._1, cartID)
        orderID <- customerOrderService.insert(order)(customerID._1)
        billJSON = buildBill(orderID._1, t.as[JsObject], cartJs)
       // _=Logger.info(s"sending mail 2 $email")
       // _=Application.sendMail(email, buildMail(billJSON))
      } yield billJSON
    }

  }
  
  def buildBill(orderId: String, customer: JsObject, cart: JsObject): JsObject =
    Json.obj(
      "_id"->orderId,
      "customer"->customer,
      "cart"->cart
    )
  
  //todo make a html bill template
  def buildMail(bill: JsObject): String= {
    val billId = bill.\("_id").toString()
    val customerName = bill \ "customer" \"name" .toString()
    val html=  s"Dear, $customerName, ur  bill id or confirmation number is $billId Thanks 4 shopping with us"
    html
  }
  
//    val cartID = Await.result(CartService.createCart(cart).map(res=> res._1.stringify),0 nanos)
//    val customer = t.\("customer").as[Customer]
//    val customerID = Await.result(CustomerService.insert(customer)("").map(res => res._1), 0 nanos)
//    val id = java.util.UUID.randomUUID().toString
//    val customerOrder = CustomerOrder(Some(id),None,customerID,cartID,id)
//    customerOrderService.insert(customerOrder)("").map(res=> (res._1,res._2.asInstanceOf[JsObject]))

  
  
}
