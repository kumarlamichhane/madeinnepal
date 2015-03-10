package controllers

import factories.ServiceFactory._


/**
 * Created by xplorer on 2/10/15.
 */
object OrderApi extends BaseApi{

  def findAllOrders = getAll(customerOrderService)
  
  def getOrderById(id: String) = getById(id)(customerOrderService)
  
}
