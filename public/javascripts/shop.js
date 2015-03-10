///**
//* Created by xplorer on 1/25/15.
//*/
//
//var orderedProducts =[]
//
//var cart = {}
//
//var customer = {
//
//        "name": "Kumar Lamichhane",
//        "phone": "9841389377",
//        "email": "kumarlamichhane13@gmail.com",
//        "address": {
//            "location": {
//                "longitude": 123.12,
//                "latitude": 21.12
//            },
//            "city": "Kathmandu",
//            "street": "Mulpani-4",
//            "houseNumber": "13"
//        }
//
//}
//
//var corequest = {
//
//    "customer": customer,
//    "cart": cart
//
//}
//
//function checkoutRequest(){
//    createCart()
//    console.log(corequest)
//    return JSON.stringify(corequest)
//
//}
//
//function checkout(){
//
//    $.ajax({
//        type: 'POST',
//        url: "http://localhost:9000/checkout",
//        dataType: "json",
//        contentType:"application/json",
//        data: checkoutRequest(),
//        success:function(msg){
//            alert(msg)
//        },
//        error:function(msg){
//            alert(msg)
//        }
//    });
//
//}
//
//function customer(){
//
//
//}
//
//

function getProductsByCategory(category){
    $.ajax({
        type: 'GET',
        url: "http://localhost:9000/products/categories/"+category,
        success: function(products) {
            $('#products').empty()
            $.each(products, function(idx,product){
                var pID = (product._id)
                var qty = (product.count)
                var name = (product.name)
                var price = (product.sellingPrice)

                $('#products').append(

                    '<div id= "product" class="col-xs-6 col-md-3"> <a href="#" class="thumbnail">'
                    +  '<image src="/assets/images/'+pID+'.jpg"/></br>'
                    + '<label id = "name'+pID+'">'+name+'</label></br> '
                    +'<label id = "price'+pID+'">' + price + '</label></br>'
                    +'<input type="text" class = "qty" id="count'+pID+'" value="1"></br>'
                    + '<input class="add2cart btn-primary" type="button" id = "'+pID+'" value="add 2 Cart"></br>'
                    + '</a></div> ')

            })
        },
        error:function(msg){
            alert(msg)

        }
    });
    
}


function getAllProducts(){
    $.ajax({
        type: 'GET',
        url: "http://localhost:9000/products",
        success: function(products) {
            $.each(products, function(idx,product){
                var pID = (product._id)
                var qty = (product.count)
                var name = (product.name)
                var price = (product.sellingPrice)

                $('#products').append(
                
                    '<div id= "product" class="col-xs-6 col-md-3"> <a href="#" class="thumbnail">'
                    +  '<image src="/assets/images/'+pID+'.jpg"/></br>'
                    + '<label id = "name'+pID+'">'+name+'</label></br> '
                    +'<label id = "price'+pID+'">' + price + '</label></br>'
                    +'<input type="text" class = "qty" id="count'+pID+'" value="1"></br>'
                    + '<input class="add2cart btn-primary" type="button" id = "'+pID+'" value="add 2 Cart"></br>'
                    + '</a></div> ')

            })
        },
        error:function(msg){
            alert(msg)

        }
    });
}
//
////+ '<input type="button" id = "add2cart" value="add 2 Cart" onclick='+orderProducts(pID,qty)+'>'
//
//$(document).ready(function(){
//
//
//        $("#abc").on("click", ".test",function () {
//            var proId = $(this).attr("id")
//            var qty = $('#count'+proId).val()
//            var price = $('#price'+proId).text()
//            var name = $('#name'+proId).text()
//           orderProducts(proId,name,qty,price)
//        })
//
//});



//
////{"_id":"product1","name":"Jacket","sellingPrice":""}
//
//function orderProducts(pID,name,qty,price){
//
//    var count = parseInt(qty)
//    var price = parseInt(price)
//    console.log(orderedProducts.length)
//    orderedProducts.push({
//        "product": {
//            "productID":pID,
//            "name":name,
//            "sellingPrice":price
//        },
//        "count": count
//    })
//    //todo increase count inside json when add2cart is clicked
//    //if(orderedProducts.length==0){
//    //    orderedProducts.push({
//    //        "productID": pID,
//    //        "count": count
//    //    })
//    //}else {
//    //    $.each(orderedProducts, function (idx, orderedProduct) {
//    //        if (orderedProduct.productID == pID) {
//    //           orderedProduct.count=++count
//    //        //todo update orderedproduct
//    //        } else {
//    //            orderedProducts.push({
//    //                "productID": pID,
//    //                "count": count
//    //
//    //            })
//    //        }
//    //    })
//    //}
//    console.log(orderedProducts)
//}
//
//
//function createCart(){
//    var noOfItem  = 0
//    var totalPrice = 0
//    $.each(orderedProducts,function(idx,orderedProduct){
//      noOfItem =noOfItem + orderedProduct.count
//        console.log(parseInt(orderedProduct.count))
//        console.log(parseInt(orderedProduct.product.sellingPrice))
//      totalPrice = totalPrice+( parseInt(orderedProduct.count) * parseInt(orderedProduct.product.sellingPrice))
//
//    })
//    cart = JSON.stringify({
//        "orderedProducts":orderedProducts,
//        "noOfItem":noOfItem,
//        "totalPrice":totalPrice
//    })
//    console.log(cart)
//
//}
//
