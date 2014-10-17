package controllers

import factories.ServiceFactory._
import models.Host
import play.api.libs.json.{Reads, Json, JsObject}
import play.api.mvc.Action
import services.HostService
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.api.mvc._

/**
 * Created by xplorer on 10/13/14.
 */
object HostApi extends BaseApi{

  val hostReader: Reads[Host] = implicitly[Reads[Host]]
  def create = Action.async(parse.json){
    request=>{
      Json.fromJson(request.body)(hostReader).map{
        t=>val host = HostService.createHost(t)
          
      }
    }

  }

  def findAll = getAll(hostService)

  def findById(id: String) = getById(id)(hostService)

  def findByBloodGroup(bloodGroup: String) = Action.async{
    val hostGroup: Future[Seq[JsObject]] = HostService.findByBloodGroup(bloodGroup)
    hostGroup.map{
      case Nil => NotFound
      case hosts: Seq[JsObject]=> Ok(Json.toJson(hosts))
    }
  }

}
