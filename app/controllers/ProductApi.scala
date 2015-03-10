package controllers

import factories.ServiceFactory._
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.Action
import services._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by xplorer on 1/3/15.
 */
object ProductApi extends BaseApi {

  def createProduct = post(productService)

  def updateProduct(id: String)= put(id)(productService)
  
  def findAllProducts = getAll(productService)
  
  def findProductByID(id: String) = getById(id)(productService)
  
  def deleteProduct(id: String) = delete(id)(productService)
  
  def findProductByCategory(category: String) = Action.async{
    val productWithCategory = ProductService.findProductByCategory(category)
    productWithCategory.map{
      case Nil => NotFound
      case p: Seq[JsObject] => Ok(Json.toJson(p))
      
    }
  }
  
  
}
