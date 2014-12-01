package factories

import daos.BaseDao
import domains.{UploadFile}
import models.{Host, Contact}
import play.modules.reactivemongo.ReactiveMongoPlugin
import reactivemongo.api.gridfs.GridFS
import play.api.Play.current
import security.User

/**
 * Created by xplorer on 9/18/14.
 */
object DaoFactory {

  val contactDao = new BaseDao[Contact]("contacts")
  //val mailOptionDao = new BaseDao[MailOption]("mailoptions")
  val hostDao = new BaseDao[Host]("hosts")
  val fileDao = new BaseDao[UploadFile]("files")
  val gridFS =   new GridFS(ReactiveMongoPlugin.db, "documents")
  val userDao = new BaseDao[User]("users")

}
