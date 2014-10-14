package factories

import daos.BaseDao
import models.{Host, Contact}

/**
 * Created by xplorer on 9/18/14.
 */
object DaoFactory {

  val contactDao = new BaseDao[Contact]("contacts")
  val hostDao = new BaseDao[Host]("hosts")

}
