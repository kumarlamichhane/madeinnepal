package services


import domains.{CartItem, Cart, Product}
import factories.DaoFactory._
import factories.ServiceFactory._
import play.Logger
import play.api.libs.json._
import reactivemongo.bson.BSONObjectID
import scala.collection.mutable.ListBuffer
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by xplorer on 1/12/15.
 */

object CartService extends BaseService[Cart](cartDao) {

  //todo save & update the instance of cart in Application but not in db === done

  //todo for all user create a separate cart1 which should b inside carts array and accessed accordingly=== done

  //todo cartItems cannot b global=== done
  
  //todo clearCart should not delete carts.cart but remove carts.cart.cartItems=== done
  
    var carts: ListBuffer[Cart] = ListBuffer[Cart]()
    var cartItems: ListBuffer[CartItem] = ListBuffer[CartItem]()
    var cart1 = Cart("",cartItems,Some(0),0)

  
  def buildCart(id: String,cartItems: ListBuffer[CartItem], noOfItem: Int, totalAmount: Double): Cart = {
    cart1 = Cart(id, cartItems, Some(noOfItem), totalAmount)
    carts.+=:(cart1)
    cart1
  }

  def createCart(id: String,newItem: CartItem): Future[Cart] = {
    Logger.info(s"creating cart id: $id")
    //val bid = BSONObjectID.parse(id)
    val t: JsObject = Json.toJson(newItem).as[JsObject]
    //val productID = newItem.productID.get
    val noOfItems = newItem.count
    //for{
     // optProduct <- productService.findById(productID)
      //product <- Future(optProduct.get.as[Product])
    val sellingPrice =  newItem.price
    val  totalAmount = noOfItems*sellingPrice
    val cartItems: ListBuffer[CartItem] = ListBuffer[CartItem](newItem)
      
   Future(buildCart(id,cartItems,noOfItems,totalAmount))
     // _=Logger.info(s"build a cart : $cart1")
      //_=persistCart(cart: Cart)
     //_=cart1.count=Some(newItem.count)
     //_=cart1.totalAmount=newItem.price
      
    //}yield cart

  }

  
  def updateCart(id: String, newItem: CartItem): Future[String] = {
    
    Logger.info(s"needs 2 update cart: $cart1")
    Logger.info(s"available carts: $carts")
    val cart = carts.find(c=>c._id==id).get

    Logger.info(s"needs to update cart with id: $id cart is: $cart1")
    val items = cart.cartItems
    
    val isOldItem = items.toSet.exists(i=>i.productID==newItem.productID)
    if(isOldItem){
      Logger.info(s"isOldItem : $isOldItem")
      val oldItem = items.toSet.find(i=>i.productID==newItem.productID).get
      Logger.info(s"item already available in the cart $oldItem")
      oldItem.count=newItem.count
      
    }else{
      Logger.info(s"isOldItem : $isOldItem")
      cart.cartItems.+=:(newItem)
      Logger.info(s"adding new Item in the cart $cart")

    }

    var c = 0
    var st = 0.0
    var tp = 0.0
    for(item <- items){
      st = item.price*item.count
      tp+=st
      c+=item.count
    }
    
    val totalNoOfItems = c
    val totalAmount = tp

    cart.count=Some(totalNoOfItems)
    cart.totalAmount=totalAmount
    
    Future(s"updated cart with Id: $id")
  }

  def removeItem(id: String,productID: String) = {
    val cart = carts.find(c=>c._id==id).get
    val items = cart.cartItems
    val item = items.toSet.find(i=>i.productID.get==productID).get
    val decrementCount = item.count
    val decrementPrice = item.price
    Logger.info(s"removing item $item")
    cart.cartItems.-=(item)
    cart.count=Some(cart.count.get-decrementCount)
    cart.totalAmount=cart.totalAmount-(decrementCount*decrementPrice)
    Logger.info(s"cart after removing $cart")
    //todo calculate total no of pros and totalAmt of the cart== done
    
  }

  
  //get cart from application
  def getCart(id: String) = Json.toJson(carts.find(c=>c._id==id).get)
  
//  def updateProductCount(bid: BSONObjectID, proID: String, newCount: Int) ={
//    Logger.info(s"updating count of product $proID")
//    //todo update noOfItem & totalAmount
////    val update = Json.obj("cartItems"->Json.obj("$each"->JsArray(jsCartItem::Nil),"$position"->pos))
//    cartDao.updateCountOfCartItem(bid,proID,newCount)
//
//  }

  //persists cart in database
  def persistCart(cart: Cart): Future[(String,Option[Cart])] = {
    Logger.info(s"persisting new cart $cart")
    val t: JsObject = writer.writes(cart).as[JsObject]
    cartDao.create(t).map{
      case (id,jso)=> (id.stringify,jso.asOpt[Cart])
    }
  }

  //clear cart from application
  def clearCart(id: String) = {
    //val emptyCartItems: ListBuffer[CartItem] = ListBuffer[CartItem]()
    Logger.info(s"clearing cart $id")
    val cart = carts.find(c=>c._id==id).get
    cart.cartItems.clear()
    cart.totalAmount=0.0
    cart.count=Some(0)

  }
  
  //remove cart from application
  def removeCart(id: String) = {
    val cart = carts.find(c=>c._id==id).get
    carts-cart
    
  }
  
}
