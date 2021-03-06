package controllers

import play.api._
import play.api.libs.json.{Reads, Writes, JsObject, Json}
import play.api.mvc._
import security.LoginController.ActionSuperAdmin
import services.BaseService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Created by xplorer on 8/23/14.
 */
trait BaseApi extends Controller{

  val idWriter= Writes[String]{id => Json.obj("_id"->id)}
  
  //post, get, put, delete
  def post[T](service: BaseService[T]): EssentialAction= Action.async(parse.json){
    request=>{
      val userId = request.session.get("userId").getOrElse("None")
      Logger.info(s"inside base api post")
      Json.fromJson(request.body)(service.reader).map{
     // request.body.validate[T].map{
          t=> service.insert(t)(userId).map{ res => Created(Json.toJson(res._1)(idWriter))
        }
      }
    }.getOrElse(Future.successful(BadRequest("invalid json")))
  }


  def getAll[T](service: BaseService[T]): EssentialAction = Action.async{
    request =>

      val queryStr: Map[String, Seq[String]] = request.queryString
      Logger.info(queryStr.toString())
      var query : JsObject = Json.obj()
      for((key,value) <- queryStr - "limit" - "skip"){
        query = query+(key,Json.toJson(value.head))
      }

      val list = service.findAll(query)
      list.map{
        case Nil=>NotFound(Json.toJson("no data"))
        case l:Seq[JsObject] => Ok(Json.toJson(l))
      }
  }
  val queryReader: Reads[JsObject] = implicitly[Reads[JsObject]]
  //todo find by a query
  def findAll[T](service: BaseService[T]): EssentialAction = Action.async{
  request =>

    val queryStr: Map[String, Seq[String]] = request.queryString
    Logger.info(queryStr.toString())
    var query : JsObject = Json.obj()
    for((key,value) <- queryStr - "limit" - "skip"){
      query = query+(key,Json.toJson(value.head))
    }
    //todo implement skip and limit
    val limit = queryStr.get("limit").getOrElse(0).asInstanceOf[Int]
    val skip = queryStr.get("skip").getOrElse(0).asInstanceOf[Int]

    val list = service.findAllByQuery(query,limit,skip)
    list.map{
      case Nil=>NotFound(Json.toJson("no data"))
      case l:Seq[JsObject] => Ok(Json.toJson(l))
    }
  }


  def getCount[T](service: BaseService[T]): EssentialAction = Action.async{

    val query = Json.obj()
    val list = service.findAll(query)
    list.map{
      case Nil=>NotFound(Json.toJson(0))
      case l:Seq[JsObject] => val count = l.size; Ok(Json.toJson(count))
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
        t => service.update(id, t).map(id => Ok(Json.toJson(id)(idWriter)))

      }
    }.getOrElse(Future.successful(BadRequest("invalid json")))                 //??
  }


  def putPartial[T](id: String)(service: BaseService[T]): EssentialAction = Action.async(parse.json){
    request=>{
      val updateJsObj = request.body.as[JsObject]
      service.updatePartial(id,updateJsObj).map(_=>Ok(Json.toJson(id)))
    }
  }

  
  def delete[T](id: String)(service: BaseService[T]): EssentialAction = Action.async{
    request =>

      service.remove(id).map(id => Ok(Json.toJson(id)))

  }

}
