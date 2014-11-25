package domains

import play.api.libs.json.Json

/**
 * Created by xplorer on 11/18/14.
 */
case class UploadFile(name: String, mimeType: String, description: String, path: String, reference: String)

object UploadFile{
  implicit val format = Json.format[UploadFile]
}