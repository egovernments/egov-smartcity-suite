
function getFormData($form) {
	var unindexed_array = $form.serializeArray();
	var indexed_array = {};

	$.map(unindexed_array, function(n, i) {
		indexed_array[n['name']] = n['value'];
	});

	return indexed_array;
}

function callAjaxSearch() {
	doLoadingMask();
	var fd=jQuery('#mrform').serialize();
	jQuery.ajax({
		url: "/EGF/brs/manualReconciliation-ajaxSearch.action",
		type: "POST",
		data: fd,
		//dataType: "text",
		success: function (response) {
			//	console.log("success"+response );
			jQuery('#resultDiv').html(response);
			jQuery('#reconcileDiv').show();
			jQuery(".datepicker").datepicker({
				format: "dd/mm/yyyy",
				autoclose:true
			});  

		undoLoadingMask();  
		}, 
		error: function (response) {
			console.log("failed");

			bootbox.alert("Failed to search Details");
			undoLoadingMask();  
		}
	});
	
}


function validateReconcile()
{
	
	var toDate=document.getElementById("toDate").value;
	var len=jQuery('#resultTable tr').length;
    var row="line ";
    var rows;
    var numOfrows='';
    var value=false;
    for(i=0;i<=len-2;i++)
				{
			if(document.getElementById('reconDates'+i).value > toDate)
			{
				var a=i+1;
				rows=row.concat(a);
				numOfrows=numOfrows+rows+',';
				value=true;
			}
			
				}
			
		if(value==true)
		{
			
			bootbox.alert("Reconciliation Date should be less than or equal to Bank statement To Date : "+numOfrows.replace(/\,$/, ''));
			return false;
		}
				

    
	if( !validateReconDate() )
	{
		bootbox.alert("Add atleast one Reconciliation Date");
		return false;
	}
	
	

	doLoadingMask();
	var fd=jQuery('#mrform').serialize();
	jQuery.ajax({
		url: "/EGF/brs/manualReconciliation-update.action",
		type: "POST",
		data: fd, 
		//dataType: "text",
		success: function (response) {
			//	console.log("success"+response );
			undoLoadingMask();    
			jQuery('#resultDiv').html(response);
			//bootbox.alert("Passed to Reconcile Details");
			
 

		}, 
		error: function (response) {
			console.log("failed");
			undoLoadingMask();  
			bootbox.alert("Failed to Reconcile Details");
			
		}
	});
	 
}



function showBalance()
{
	//alert("returned  "+validateReconDate())


	doLoadingMask();
	var fd=jQuery('#mrform').serialize();
	jQuery.ajax({
		url: "/EGF/brs/manualReconciliation-ajaxBalance.action",  
		type: "POST",
		data: fd, 
		//dataType: "text",
		success: function (response) {
			//	console.log("success"+response );
			undoLoadingMask();    
			jQuery('#balanceDiv').html(response);
			//bootbox.alert("Passed to Reconcile Details");
			
 

		}, 
		error: function (response) {
			console.log("failed");
			undoLoadingMask();  
			bootbox.alert("Failed to Show balance Details");
			
		}
	});
	 
}

function validateReconDate()
{
	//alert("Validating Reconciliation Date"+jQuery('#resultTable tr').length);
	var len=jQuery('#resultTable tr').length;

	for(i=0;i<=len-2;i++)
	{
		//alert("'"+document.getElementById('reconDates'+i).value+"'");

		//alert("Compare----"+ document.getElementById('reconDates'+i).value!='');

		
		if(document.getElementById('reconDates'+i).value!='')
			return true;	
	}

	return false;
}


