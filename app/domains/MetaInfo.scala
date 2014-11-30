package domains

import play.api.libs.json.Json
import security.{Group, User}

/**
 * Created by xplorer on 11/27/14.
 */
trait MetaInfoAware{
    def meta: Option[MetaInfo]
}

case class MetaInfo(owner: Option[String], permission: Option[Group.Group])

object MetaInfo{
  implicit val format = Json.format[MetaInfo]
}