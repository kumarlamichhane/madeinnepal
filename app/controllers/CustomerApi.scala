package controllers

import controllers.Application._
import factories.ServiceFactory._
import play.api.Logger
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.{Cookie, Session, Action}
import services.CustomerService
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Created by xplorer on 1/12/15.
 */
object CustomerApi extends BaseApi{

  def checkOut = Action.async(parse.json){
    request=>{
      
      Logger.info(s"inside Customer Api")
      CustomerService.checkOut(Json.toJson(request.body)).map(billJSON => Ok(Json.toJson(billJSON)).withNewSession)
      //CustomerService.checkOut(Json.toJson(request.body)).map(billJs => Ok(views.html.bill(billJs)).withNewSession)
    }
    
  }
  
  def getCustomerById(id: String) = getById(id)(customerService)
  
  
  def sessionTest(str: String) = Action.async{
    if(str=="true") {
      Future(Ok("").withSession("session" -> "123"))
    }else
      Future(Ok("").withCookies(Cookie("cookie","456")))
  }
  
  
}
