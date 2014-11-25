package security

/**
 * Created by xplorer on 11/25/14.
 */
case class User(username: String, password: String) {

  def checkPassword(password: String): Boolean = password==this.password

}

object User