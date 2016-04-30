
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
	if(!validate())
		{
		return false;
		}
	
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

function validate() {
	if (document.getElementById("bankId").value == "") {
		bootbox.alert("Select Bank");
		return false;
	}
	if (document.getElementById("branchId").value == "") {
		bootbox.alert("Select Branch");
		return false;
	}
	if (document.getElementById("accountId").value == "") {
		bootbox.alert("Select Account");
		return false;
	}
	var toDateStr=document.getElementById("toDate").value;
	var fromDatestr=document.getElementById("fromDate").value;
	var reconDateStr =document.getElementById("reconciliationDate").value;
	if (reconDateStr == "") {
		bootbox.alert("Select Reconciliation Date");
		return false;
	}
	if (fromDatestr == "") {
		bootbox.alert("Select Bank Statement From Date"); 
		return false;
	}
	if (toDateStr == "") {
		bootbox.alert("Select Bank Statement To Date");
		return false;
	}
	if(fromDatestr>toDateStr)
		{
		bootbox.alert("Bank Statement From Date must be less than or equal to Bank Statement To Date");
		return false;
		}
	if(toDateStr!=null && reconDateStr!=null)
	{
	
	var toDateParts=	toDateStr.split("/");
	if(toDateParts.length!=3)
	{
	bootbox.alert("Enter date is 'DD/MM/YYYY' format only");
	return false;
	}
	var toDate=new Date(toDateParts[1]+"/"+toDateParts[0]+"/"+toDateParts[2]);
	var reconDateParts=	reconDateStr.split("/");
	
	if(reconDateParts.length!=3)
	{
	bootbox.alert("Enter date is 'DD/MM/YYYY' format only");
	return false;
	}
	var reconDate=new Date(reconDateParts[1]+"/"+reconDateParts[0]+"/"+reconDateParts[2]);
	//bootbox.alert(reconDate.toString('MM-dd-yyyy'));
	if(reconDate<toDate)
	{
	bootbox.alert("Reconciliation Date must be greater than or equal to Bank Statement To Date");
	return false;
	}
	}
    return true;
}
