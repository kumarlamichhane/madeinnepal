package factories

import models.{Host, Contact}
import services.BaseService
import DaoFactory._
/**
 * Created by xplorer on 9/18/14.
 */
object ServiceFactory {

  val contactService = new BaseService[Contact](contactDao)
  val hostService = new BaseService[Host](hostDao)
}
