package factories

import daos.BaseDao
import domains._
import domains.{Host, Contact}
import play.modules.reactivemongo.ReactiveMongoPlugin
import reactivemongo.api.gridfs.GridFS
import play.api.Play.current
import security.User

/**
 * Created by xplorer on 9/18/14.
 */
object DaoFactory {


  val productDao = new BaseDao[Product]("products")
  val cartDao = new BaseDao[Cart]("carts")
  val customerDao = new BaseDao[Customer]("customers")
  val customerOrderDao = new BaseDao[CustomerOrder]("customer.orders")
  

  val contactDao = new BaseDao[Contact]("contacts")
  //val mailOptionDao = new BaseDao[MailOption]("mailoptions")
  val hostDao = new BaseDao[Host]("hosts")
  val fileDao = new BaseDao[UploadFile]("files")
  val gridFS =   new GridFS(ReactiveMongoPlugin.db, "documents")
  val userDao = new BaseDao[User]("users")
  val sessionDao = new BaseDao[User]("sessions")

}
