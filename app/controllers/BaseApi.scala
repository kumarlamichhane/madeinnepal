package controllers

import play.api._
import play.api.libs.json.{JsObject, Json}
import play.api.mvc._
import security.LoginController.ActionSuperAdmin
import services.BaseService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Created by xplorer on 8/23/14.
 */
trait BaseApi extends Controller{

  //post, get, put, delete
  def post[T](service: BaseService[T]): EssentialAction= Action.async(parse.json){
    request=>{

      Json.fromJson(request.body)(service.reader).map{
     // request.body.validate[T].map{
          t=> service.insert(t).map{
              lastError =>
              Logger.debug(s"Successfully inserted with LastError: $lastError")
              Created(Json.toJson("created"))
        }
      }
    }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def getAll[T](service: BaseService[T]): EssentialAction = ActionSuperAdmin.async{

    val query = Json.obj()
    val list = service.findAll(query)
    list.map{
      case Nil=>NotFound(Json.toJson("no data"))
      case l:Seq[JsObject] => Ok(Json.toJson(l))
    }
  }

  def getById[T](id: String)(service: BaseService[T]): EssentialAction = Action.async{

    val doc = service.findById(id)
    doc.map{
      case None => NotFound(Json.toJson("no data"))
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


  def putPartial[T](id: String)(service: BaseService[T]): EssentialAction = Action.async(parse.json){
    request=>{
      val updateJsObj = request.body.as[JsObject]
      service.updatePartial(id,updateJsObj).map(_=>Ok(Json.toJson(id)))
    }
  }


}
