/**
 * Created by xplorer on 12/4/14.
 */

function logout(){
    $.ajax({
        type: 'GET',
        url: "http://localhost:8888/logout",
        success:
            function(msg) {

                window.location.href = "/admin"
            },
        error:function(msg){

            window.location.href = "/admin"
        }
    });
}


function getUserInSession(){
    $.ajax({
        type: 'GET',
        url: "http://localhost:8888/users/me",
        success: function(msg) {
            console.log(msg)
            $('#user-name-label').append('<li>' + msg.username +'\t(' +msg.group + ')</li>');
            hideUserForm(msg.group)
            
        },
        error:function(msg){
            alert(msg)

        }
    });
}

function login() {

    $.ajax({
        type: 'POST',
        url: "http://localhost:8888/login",
        dataType: "json",
        contentType:"application/json",
        data: loginForm(),
        success:
            function(msg){

            var msgStr = msg.toString()
            if(msgStr=="success") {

                window.location.href = "/admin/page/home"
            }

            else if(msgStr=="change") {

                window.location.href = "/admin/page/changepassword"
            }
            else {

                window.location.href = "/admin/page/login"
            }
      },
        error:function(msg){
            alert("login error")
            window.location.href = "/admin/page/login"
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
        url: "http://localhost:8888/users",
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

function changePassword(){

    $.ajax({
        type: 'PUT',
        url: "http://localhost:8888/users/password",
        dataType: "json",
        contentType:"application/json",
        data: passwordForm(),
        success:function(msg){
            alert(msg)
            window.location.href = "/admin/home"
        },
        error:function(msg){
            alert(msg)
        }
    });
}

function passwordForm() {

    var newPassword = $('#newPassword').val()
    var confirmPassword = $('#confirmPassword').val()
    if(newPassword==confirmPassword) {
        return JSON.stringify({
            "password": newPassword
        });
    }else{
        alert("passwords do not match")
    }

}

function hideUserForm(group){

    if(group!="SuperAdmin"){
        $('#userdiv').hide();
    }
}

function getUserCount(){

    $.ajax({
        type: 'GET',
        url: "http://localhost:8888/users/count/user",
        success:
            function(msg) {
                $('#count_user').append('<li>' + msg + '</li>');
            },
        error:function(msg){


        }
    });
    
}