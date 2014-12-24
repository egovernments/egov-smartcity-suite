String.prototype.trim = function () {
    return this.replace(/^\s*/, "").replace(/\s*$/, "");
}
function amtAfterReval(){
	var assetValueAsOnDate = document.getElementById("assetValueAsOnDate").value;
	var revalAmt = document.getElementById("revalAmt").value;
	var typeOfChange = document.getElementById("typeOfChange").value;

	if(typeOfChange !=-1 && revalAmt.trim() !="" ){
		assetValueAsOnDate = assetValueAsOnDate.trim()==""?0:assetValueAsOnDate;
		revalAmt = revalAmt.trim()==""?0:revalAmt;
		valAfterReval = typeOfChange=="Increase"?parseFloat(assetValueAsOnDate)+parseFloat(revalAmt):parseFloat(assetValueAsOnDate)-parseFloat(revalAmt);
		document.getElementById("valAfterReval").value = valAfterReval.toFixed(2);
	}
	
}
	function onRevalDateChange(assetid){
		document.getElementById("save").disabled=false;
		document.getElementById('lblError1').innerHTML = "";
		var date = new Date();
		var revaldate = document.getElementById("revalDate").value;
		 var dt = revaldate.substring(0,2);
   		 var mon = revaldate.substring(3,5);
    		 var yr = revaldate.substring(6,10); 
		 var date1 = new Date(yr, mon-1, dt); 
		 if(date1 > date){
			document.getElementById('lblError1').innerHTML = "Revaluation date should be on or before current date";
			document.getElementById("save").disabled=true;
		 }else{
			var url = '../assetmaster/ajaxAsset!getAssetValueToDate.action?revaldate='+revaldate+"&assetid="+assetid;
			YAHOO.util.Connect.asyncRequest('POST', url, postType, null);
		}
			
		
	}
	var postType = {
success: function(o) {
		var assetVal= o.responseText;
		 document.getElementById("assetValueAsOnDate").value = assetVal;
		amtAfterReval();
    },
    failure: function(o) {
    	alert('failure');
    }
}