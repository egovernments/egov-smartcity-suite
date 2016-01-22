jQuery.noConflict();
jQuery(document).ready(function(){
    
	jQuery('.add-attachment').click(function(){
       console.log('came');
       jQuery(this).parent().before('<div class="col-sm-3 add-margin"> <input type="file" class="form-control" required> </div>');
    });
    
	jQuery('.motorcheck').click(function(){
    	jQuery('.motorpart').toggle();
    });
    
});



function resetOnPropertyNumChange(){
	var propertyNo = jQuery("#propertyNo").val();
   	if(propertyNo!="" && propertyNo!=null){
		document.getElementById("address").disabled="true";
    	document.getElementById("boundary").disabled="true"; 
	} else {
        document.getElementById("address").disabled=false;
    	document.getElementById("boundary").disabled=false;  
    }
	document.getElementById("boundary").value='-1';
	document.getElementById("zoneName").value="";
	document.getElementById("wardName").value="";
	document.getElementById("address").value="";
}

function detailchange(){
	document.getElementById("detailChanged").value = 'true';
}
	
function checkLength(obj,val){
	if(obj.value.length>val) {
		bootbox.alert('Max '+val+' digits allowed')
		obj.value = obj.value.substring(0,val);
	}
}	

function formatCurrency(obj) {
		if(obj.value=="") {
		return;
	} else {
		obj.value=(parseFloat(obj.value)).toFixed(2);
		}
}