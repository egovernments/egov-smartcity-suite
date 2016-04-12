$(document).ready(function(){
	$( "#pipesizeid" ).focusout(function() {
	    textValue =  $.trim($(this).val());
	    if(textValue ==0 || textValue =='' ){
	       $.trim($(this).val('')); //to set it blank
	    } else {
	       return true;
	    }
	});
	$('#pipesizeid').blur(function(){
		textValue =  $.trim($(this).val());
		 if(textValue !=0 || textValue !='' ){
			var pipeSizeInInch = textValue * 0.039370;
			result = parseFloat(pipeSizeInInch).toFixed(3);
			$("#pipesizeInInch").val(result);
		 }
	});
	$("#pipesizeInInch").attr('disabled','disabled');
	
	$('#statusdiv').hide();
	var activeDiv = $('#reqAttr').val();
	if (activeDiv =='false')
		{
		$('#statusdiv').hide();
	     $('#addnewid').hide();
		}
	
	else
		{
		$('#statusdiv').show();
		 $('#addnewid').show();
		}
	
	$("#resetid").click(function(){
		$("#propertypipesizeform")[0].reset();
		})
	
 });

$('#listid').click(function() {
	window.open("/wtms/masters/propertyPipeSizeMaster/list", "_self");
 });

$('#addnewid').click(function() {
	window.open("/wtms/masters/propertyPipeSizeMaster/", "_self");
});

function addNew()
{
	window.open("/wtms/masters/propertyPipeSizeMaster/", "_self");
}

function edit(propertyPipeSize)
{
	
	window.open("/wtms/masters/propertyPipeSizeMaster/"+propertyPipeSize, "_self");
	
}

