package factories

import domains._
import models.{Host, Contact}
import security.User
import services.BaseService
import DaoFactory._
/**
 * Created by xplorer on 9/18/14.
 */
object ServiceFactory {


  val productService = new BaseService[Product](productDao)
  val cartService = new BaseService[Cart](cartDao)
  val customerService = new BaseService[Customer](customerDao)
  val customerOrderService = new BaseService[CustomerOrder](customerOrderDao)
  
  
  
  val contactService = new BaseService[Contact](contactDao)
  //val mailOptionService = new BaseService[MailOption](mailOptionDao)
  val hostService = new BaseService[Host](hostDao)
  val fileService = new BaseService[UploadFile](fileDao)
  val userService = new BaseService[User](userDao)

}
