function createHost() {
 
    $.ajax({      
        type: 'POST',
        url: "http://localhost:9000/hosts",
        dataType: "json",
        contentType:"application/json",
        data: formHost(),        
        success:function(msg){
        	alert(msg);
        },
        error:function(msg){
        	alert(msg);
        }
    });
}

function formHost() {
	
	var bloodgroup = document.getElementById("bloodGroup");
	var selected = bloodgroup.options[bloodgroup.selectedIndex].value;
	return JSON.stringify({
        "location": $('#location').val(),
        "name": $('#name').val(),
        "phoneNumber": $('#phoneNumber').val(),
        "bloodGroup": selected
    });
      
}

