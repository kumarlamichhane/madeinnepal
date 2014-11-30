
function webSocketTest(){
	  $.ajax({      
	        type: 'GET',
	        url: "ws://localhost:9000/testwebsocket",
	        dataType: "text",
	        contentType:"text/html",        
	        success:alert("hi")
	    })
}


function getContacts() {
 
    $.ajax({      
        type: 'GET',
        url: "http://localhost:9000/contacts",
        dataType: "json",
        contentType:"application/json",        
        success:renderList
    })
}

function createContact() {
 
    $.ajax({      
        type: 'POST',
        url: "http://localhost:9000/contacts",
        dataType: "json",
        contentType:"application/json",
        data: formToJSON(),        
        success:function(msg){
        	alert(msg);
        },
        error:function(msg){
        	alert(msg);
        }
    });
}

function updateContact() {
	 
    $.ajax({      
        type: 'PUT',
        url: "http://localhost:9000/contacts",
        dataType: "json",
        contentType:"application/json",
        data: formToJSON(),
        success:function(msg){
        	alert (msg);
        },
        error: function(msg){
        	alert (msg);
        }
    });
}

function deleteContact() {
	
    $.ajax({      
        type: 'DELETE',
        url: "http://localhost:9000/contacts",
        dataType: "json",
        contentType:"application/json",      
        data:formToJSON(),
        success:function(msg){
        	alert(msg);
        },
        error:function(msg){
        	alert(msg);
        }
    });
}

function findContactByPhone(){
	
	$.ajax({      
        type: 'GET',
        url: "http://localhost:9000/contacts/phonenumber/"+$('#phonenumber').val(),
        dataType: "json",
        contentType:"application/json",        
        success:renderContact
    });	
}


function renderList(contacts) {
	
    //console.log(contacts);
    $.each(contacts, function(idx, contact) {
        //console.log(idx + " " + obj.description);
        $('#contacts').append('<li>' + contact.name + '\t' +contact.bloodGroup +'\t'+contact.phoneNumber +'\t'+contact.address + '</li>');
    });
}

function renderContact(contact){
	console.log(contact)
	$('#contacts').append('<li>' + contact.name + '\t' +contact.bloodGroup +'\t'+contact.phoneNumber +'\t'+contact.address + '</li>');
}

function formToJSON() {
	
	var bloodGroup = document.getElementById("bloodGroup");
	var selected = bloodGroup.options[bloodGroup.selectedIndex].value;
	return JSON.stringify({
        "address": $('#address').val(),
        "name": $('#name').val(),
        "email": $('#email').val(),
        "phoneNumber": $('#phoneNumber').val(),
        "bloodGroup": selected
    });
      
}

function test(){
    alert(" hello ")
}