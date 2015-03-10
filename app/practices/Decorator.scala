package practices

/**
 * Higher Order Function
 * Created by xplorer on 12/14/14.
 */
class Decorator(left: String, right: String) {

  def layout[A](x: A)= left + x.toString + right
  
}

object FunTest extends App{

  def apply(f: Int => String,v: Int) = f(v)

  val decorator = new Decorator("***","***")

  println(apply(decorator.layout,13))
}