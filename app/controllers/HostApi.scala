package controllers

import factories.ServiceFactory._
import play.api.libs.json.{Json, JsObject}
import play.api.mvc.Action
import services.HostService
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.api.mvc._

/**
 * Created by xplorer on 10/13/14.
 */
object HostApi extends BaseApi{

  def create = post(hostService)

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
