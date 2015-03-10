package services

import factories.DaoFactory._
import domains._
import play.api.libs.json.{Json, JsObject}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Created by xplorer on 2/26/15.
 */
object ProductService extends BaseService[Product](productDao){

  def findProductByCategory(category: String): Future[Seq[JsObject]] = {
    val query = Json.obj("category"->category)
    productDao.readAll(query).map( _.map( js => transformMongoFields(js)))
    
  } 
  
  def findFeaturedProducts: Future[Seq[JsObject]] ={
    val query = Json.obj("featured"->true)
    productDao.readAll(query).map( _.map( js => transformMongoFields(js)))
    
  }
  
}
