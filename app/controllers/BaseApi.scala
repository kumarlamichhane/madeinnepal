package controllers

import play.api._
import play.api.libs.json.{JsObject, Json}
import play.api.mvc._
import play.modules.reactivemongo.MongoController
import services.BaseService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Created by xplorer on 8/23/14.
 */
trait BaseApi extends Controller with MongoController{

  //post, get, put, delete
  def post[T](service: BaseService[T]): EssentialAction= Action.async(parse.json){
    request=>{

      Json.fromJson(request.body)(service.reader).map{
     // request.body.validate[T].map{
        t => service.insert(t).map{
          lastError =>
          Logger.debug(s"Successfully inserted with LastError: $lastError")
          Created
        }
      }
    }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def getAll[T](service: BaseService[T]): EssentialAction = Action.async{

    val query = Json.obj()
    val list = service.findAll(query)
    list.map{
      case Nil=>NotFound
      case l:Seq[JsObject] => Ok(Json.toJson(l))
    }
  }

  def getById[T](id: String)(service: BaseService[T]): EssentialAction = Action.async{

    val doc = service.findById(id)
    doc.map{
      case None => NotFound
      case Some(t)=> Ok(Json.toJson(t))
    }
  }

  def put[T](id: String)(service: BaseService[T]): EssentialAction = Action.async(parse.json) {
    request =>{

      Json.fromJson(request.body)(service.reader).map{
     // request.body.validate[T].map {
        //t => service.update(id, t).map(_=>Ok(Json.toJson(t)(service.writer)))
        t => service.update(id, t).map(_=> Ok(Json.toJson("updated ")))

      }
    }.getOrElse(Future.successful(BadRequest("invalid json")))                 //??
  }




}
