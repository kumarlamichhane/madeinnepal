package controllers

import domains.{CartItem, Cart, Product}
import play.api.Logger
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.{Controller, Action}
import factories.ServiceFactory._
import reactivemongo.bson.BSONObjectID
import services.CartService
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by xplorer on 1/12/15.
 */
object CartApi extends BaseApi{

  def clearSession = Action{
    request=>
    val cartID = request.session.get("cartID").getOrElse("None")
    Logger.info(s" cartID in session $cartID") 
      //CartService.clearCart(cartID)
    Ok(Json.toJson("cleared session")).withNewSession
    
  }
  
  def addToCart = Action.async(parse.json) {
    request =>{
      val cartID = request.session.get("cartID").getOrElse("None")
      Logger.info(s" cartID in session $cartID")
      val bod = request.body.toString()
      Logger.info(s" ordered product  $bod")
      val cartItem = request.body.as[CartItem]
      if(cartID=="None"){
        Logger.info("no cart in session creating new cart")
        val cartID = BSONObjectID.generate.stringify
        CartService.createCart(cartID,cartItem).map(_=>Ok(Json.toJson(cartID.toString)).withSession("cartID"->cartID))
      }else{
        Logger.info(s"cart with id: $cartID exists in session")
        CartService.updateCart(cartID,cartItem).map(_=>Ok(Json.toJson(cartID.toString)))
        
      }
    }
  }
  
  
  def getCart(id: String) = Action.async{
    Future(Ok(Json.toJson(CartService.getCart(id))))
  }
  

  def clearCart(id: String) = Action.async{
    CartService.clearCart(id)
    Future(Ok(Json.toJson(id)))

  }
  
  def removeItem(id: String,productID: String)= Action{
    Logger.info(s"product $productID need 2 b removed from cart $id")
    CartService.removeItem(id,productID)
    Ok(Json.toJson(id))
  }
  
  def deleteCart(id: String) = delete(id)(cartService)
  
  def getCartById(id: String) = getById(id)(cartService)
}
