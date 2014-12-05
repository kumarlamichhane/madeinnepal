/**
 * Created by xplorer on 12/4/14.
 */




function login() {

    $.ajax({
        type: 'POST',
        url: "http://localhost:9000/login",
        dataType: "json",
        contentType:"application/json",
        data: loginForm(),
        success:function(msg){
            window.location.href = "/admin/home"
        },
        error:function(msg){
            window.location.href = "/admin"
        }
    });
}
function loginForm() {
    return JSON.stringify({
        "username": $('#username').val(),
        "password": $('#password').val()


    });
}


function createUser(){

    $.ajax({
        type: 'POST',
        url: "http://localhost:9000/users",
        dataType: "json",
        contentType:"application/json",
        data: userForm(),
        success:function(msg){
            alert(msg)
        },
        error:function(msg){
            alert(msg)
        }
    });
}


function userForm() {

    return JSON.stringify({
        "username": $('#userName').val(),
        "phoneNumber":$('#userPhoneNumber').val(),
        "emailAddress": $('#userEmailAddress').val(),
        "address": $('#userAddress').val(),
        "password":"",
        "status": false,
        "group":$('#userGroup').val()


    });
}