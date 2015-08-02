/**
 * Created by xplorer on 1/31/15.
 */


var cartItem ={}



$(document).ready(function()
{

    $("#products").on("click", ".add2cart",function () {
        var proId = $(this).attr("id")
        var count = $('#count'+proId).val()
        var price = $('#price'+proId).text()
        var name = $('#name'+proId).text()
        add2cart(proId,name,count,price)
    })

});



function orderProducts(pID,name,count,price)
{
    var count = parseInt(count)
    cartItem = JSON.stringify({
        "productID": pID,
        "productName":name,
        "price":parseFloat(price),
        "count": count
    })

    console.log(cartItem)
}

function add2cart(pID,name,count,price)
{
    orderProducts(pID,name,count,price)
    $.ajax({
        type: 'POST',
        url: "http://localhost:8888/addtocart",
        dataType: "json",
        contentType:"application/json",
        data: cartItem,
        success:function(cartId) {
            $('#cart_items').empty()
            getCartItems2(cartId)

        }
        ,
        error:function(msg){
            alert(msg)
        }
    });

    //$('#cart_items').append(" ")

}

function getCartItems2(cId){

    $.ajax({
        type: 'GET',
        url: "http://localhost:8888/cart/"+cId,
        success: function(cart) {
            $('#cart_items').empty()
            var cartId = cart._id
            var count = cart.count
            var totalAmount = cart.totalAmount
            var items = cart.cartItems
           // $('.cart_price').text("Total Amount: "+totalAmount)
            $('#no-of-items').text(count)
           // $('.cart_quantity').text("No Of Items: "+count)
            $('#total-amount').text("Rs "+totalAmount)
            $('#cart_id').text(cartId)
            $.each(items, function(idx,item){

                $('#cart_items').append(
                    '<tr><td class="product-in-table">'+
                    '<img class="img-responsive" src="/assets/images/'+item.productID+'.jpg" alt="">'+

                    '<label id="namei'+item.productID+'">'+item.productName+'</label></td>'+
                        //<span>Sed aliquam tincidunt tempus</span>
                    '<td id="pricei'+item.productID +'" value="">'+item.price+'</td>'+
                    '<td><button type="button" class="sub" name="'+item.productID +'" id="sub'+item.productID +'">-</button>'+
                    '<input type="text" class="quantity-field" name="'+item.productID +'" value="'+item.count +'" id="counti'+ item.productID+'"/>'+
                    '<button type="button" class="add" name="'+item.productID +'" id="add'+item.productID +'">+</button></td>'+
                    '<td class="shop-black">Rs. '+parseFloat(item.price * item.count)+'</td>'+
                    '<td><button type="button" class="remove_item" id = "'+item.productID+'">X</button>'+
                    '<button type="button" class="update_item" id = "'+item.productID+'">Update</button></td>'+
                    '</tr>'
                )
            })
        },
        error:function(msg){
            alert("error getting cart items")
        }
    });
}

$(document).ready(function()
{
    //$("#cart_items").on("keyup", ".quantity-field",function () {
    //        var proId = $(this).attr("name").toString()
    //        var count = $('#counti' + proId).val()
    //        var price = $('#pricei' + proId).text()
    //        var name = $('#namei' + proId).text()
    //        //count = $('#counti' + proId).val()
    //        //count++
    //        add2cart(proId, name, count, price)
    //        getCartItems2($('#cart_id').text())
    //    }
    //)

    $("#cart_items").on("click", ".add",function () {
            var proId = $(this).attr("name").toString()
            var count = $('#counti' + proId).val()
            var price = $('#pricei' + proId).text()
            var name = $('#namei' + proId).text()
            count = $('#counti' + proId).val()
            count++
            add2cart(proId, name, count, price)
            getCartItems2($('#cart_id').text())
        }
    )

    $("#cart_items").on("click", ".sub",function () {
        var proId = $(this).attr("name").toString()
        var price = $('#pricei'+proId).text()
        var name = $('#namei'+proId).text()

        var count
       if($('#counti'+proId).val()!="1"){
            count = $('#counti'+proId).val()
            count--
            add2cart(proId,name,count,price)
            getCartItems2($('#cart_id').text())
        }else{
            alert("count cant b less than 1")
        }
    })
});

//update
$(document).ready(function()
{
    $("#cart_items").on("click", ".update_item",function () {
        var proId = $(this).attr("id")
        var count = $('#counti'+proId).val()
        var price = $('#pricei'+proId).text()
        var name = $('#namei'+proId).text()
        add2cart(proId,name,count,price)
        getCartItems2($('#cart_id').text())
    })

});



function checkout(){

    if($('#no-of-items').text()==0){
        alert("need no checkout")
    }else {
        //window.location.href = "/page/bill"
        $.ajax({
            type: 'POST',
            url: "http://localhost:8888/checkout",
            dataType: "json",
            contentType: "application/json",
            data: customerJSON(),
            success: function(order){

                window.location.replace("/page/bill/"+order._id)


            },
            error: function (msg) {
                alert("error checkout")
            }
        })
    }
}


function customerJSON(){

    //var location= {}
    //
    //var address = JSON.stringify({
    //    "city":$('#city').val(),
    //    "street":$('#street').val(),
    //    "houseNo":$('#houseNo').val()
    //})
    

    if($('#name').val()!="" && $('#phone').val()!="" && $('#email').val()!="") {
        return JSON.stringify({
            "name": $('#name').val(),
            "phone": $('#phone').val(),
            "email": $('#email').val()
            //"address":address
        })
    }else{
        alert("enter valid informations")
        
    }
}

function clearCart(cartId){
    console.log("clearing cart: "+cartId)
    if(cartId!="") {
        $.ajax({
            type: 'GET',
            url: "http://localhost:8888/clearCart/" + cartId,
            success: function (msg) {
                $('#cart_items').empty()
                getCartItems2(cartId)
            },
            error: function (msg) {
                alert(msg)

            }
        });
    }else{
        alert("no cart 2 clear")
        
    }
}

function removeItem(cartId,productID){
    $.ajax({
        type: 'GET',
        url: "http://localhost:8888/cart/"+cartId+"/"+productID,
        success: function(msg) {
            //  alert(msg)
            $('#cart_items').empty()
            getCartItems2(cartId)
        },
        error:function(msg){
            alert(msg)

        }
    });
}

$(document).ready(function()
{
    $("#cart_items").on("click", ".remove_item", function () {
        //  alert("productId ")
        var cartId = $('#cart_id').text()
        var productId = $(this).attr('id')
        removeItem(cartId,productId)
    })
});

$(document).ready(function()
{
    $("#cart_top").on("click", ".clear_cart", function () {
        var cartId = $('#cart_id').text()
        if(cartId=="None"){
            alert("no cart 2 clear")
        }else {
            clearCart(cartId)
        }
    })
});
