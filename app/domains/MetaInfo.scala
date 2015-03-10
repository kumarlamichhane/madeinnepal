package domains

import org.joda.time.DateTime
import play.api.libs.json.Json
import security.{Group, User}

/**
 * Created by xplorer on 11/27/14.
 */
trait MetaInfoAware{
    def meta: Option[MetaInfo]
}

case class MetaInfo(createdBy: Option[String] = None, createdDate: Option[DateTime])

object MetaInfo{
  implicit val format = Json.format[MetaInfo]
}