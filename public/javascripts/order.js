/**
 * Created by xplorer on 2/10/15.
 */


function getAllOrders(){

    $.ajax({
        type: 'GET',
        url: "http://localhost:8888/orders",
        success: function(orders) {
            $.each(orders,function(idx,order) {
                $('#orders').append(
                    '<label class="order" id="'+order._id+'">orderID: '+order._id+'</label></br>'

                )
            })
        },
        error:function(msg){
            alert("error getting orders")

        }
    });

}
$(document).ready(function() {
    $("#orders").on("click", ".order", function () {
        $('#orders').empty()
        var orderId = $(this).attr("id")
        getOrderById(orderId)
    })
})

function getOrderById(orderId){
    $.ajax({
        type: 'GET',
        url: "http://localhost:8888/orders/"+orderId,
        success: function(order) {
                getCustomerById(order.customerID)
                renderCart(order.cartID.toString())

        },
        error:function(msg){
            alert("error getting orders")

        }
    });

}

function getCustomerById(customerId){
    $.ajax({
        type: 'GET',
        url: "http://localhost:8888/customers/"+customerId,
        success: function(customer) {
            $("#customer_id").text(" customer id: " +customer._id)
            $("#customer_name").text(" Customer Name: " +customer.name)
            $("#customer_phone").text("Customer phone: " +customer.phone)
        },
        error:function(msg){
            alert("error getting customer")

        }
    });

}

function renderCart(cId){

    $.ajax({
        type: 'GET',
        url: "http://localhost:8888/carts/"+cId,
        success: function(cart) {
            $('#cart_items').empty()
            var cartId = cart._id
            var count = cart.count
            var totalAmount = cart.totalAmount
            var items = cart.cartItems
            $('#no-of-items').text(count)
            $('#total-amount').text("Rs "+totalAmount)
            $('#cart_id').text(cartId)
            $.each(items, function(idx,item){

                $('#total_amount').val=totalAmount

                $('#cart_items').append(
                    '<tr><td class="product-in-table">'+
                    '<label class="img-responsive" value="'+item.productID+'" alt="">'+

                    '<label id="namei'+item.productID+'">'+item.productName+'</label></td>'+
                        //<span>Sed aliquam tincidunt tempus</span>
                    '<td id="pricei'+item.productID +'" value="">'+item.price+'</td>'+
                    '<td>'+item.count+
                    '</td>'+
                    '<td class="shop-black">Rs. '+parseFloat(item.price * item.count)+'</td>'+

                    '</tr>'
                )
            })
        },
        error:function(msg){
            alert("error getting cart items")
        }
    });
}
