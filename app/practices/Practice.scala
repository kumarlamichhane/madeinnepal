package practices

import play.api.mvc.{Action, EssentialAction, Controller}

/**
 * Created by xplorer on 11/15/14.
 */
object Practice extends Controller{

  def checkRequestHeader : EssentialAction = Action{
    request=>

    Ok(request.contentType
      +" \n  body: " +
      request.body
      +" \n  headers: " +
      request.headers
      +" , id: \n" +
      request.id
      +" \n method: " +
      request.method
      +" , \n path: " +
      request.path
      +" , \n remoteAddress: " +
      request.remoteAddress
      +" , \n uri: " + request.uri)
  }

}
