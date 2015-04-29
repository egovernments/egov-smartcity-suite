

	function validate() 
	{
		var finYearVal = dom.get("finYearId").value;
		var toDateVal = dom.get("toDate").value;
		var budgetHeadsDD = document.getElementById("dropDownBudgetHeads");
		var depositCodesDD = document.getElementById("dropDownDepositCodes");
		var allbudgetHeadsDD = document.getElementById("allBudgetHeads");
		var alldepositCodesDD = document.getElementById("allDepositCodes");
		var allSelected =false;
		var oneSelected = false;
		var oneSelectedDepCode = false;
		var oneSelectedAllBudgetHeads = false;
		var oneSelectedAllDepCode = false;
		if(finYearVal == -1)
		{
			document.getElementById("workProgress_error").style.display='';
		  	document.getElementById("workProgress_error").innerHTML='Please select financial year';
		  	window.scroll(0,0);
		  	return false;
		}
		if(toDateVal == "")
		{
			document.getElementById("workProgress_error").style.display='';
		  	document.getElementById("workProgress_error").innerHTML='Please enter the Estimate Admin Sanction To Date';
		  	window.scroll(0,0);
		  	return false;
		}
		if(!validateDate(toDateVal))
		{
			document.getElementById("workProgress_error").style.display='';
		  	document.getElementById("workProgress_error").innerHTML='Please enter the Estimate Admin Sanction To Date in dd/mm/yyyy format';
		  	window.scroll(0,0);
		  	return false;
		}
		if(compareDate(dom.get("fromDate").value,toDateVal)==-1)
		{
			document.getElementById("workProgress_error").style.display='';
		  	document.getElementById("workProgress_error").innerHTML='Estimate Admin Sanction From Date cannot be greater than Estimate Admin Sanction To Date';
		  	window.scroll(0,0);
		  	return false;
		}		
		for (var i = 0; i < budgetHeadsDD.options.length; i++) {
		     if(budgetHeadsDD.options[i].selected ==true){
			     oneSelected = true;
			     if(i==0)
			    	 allSelected = true;
			     if(allSelected &&  i!=0)
			     {
			    	 document.getElementById("workProgress_error").style.display='';
				  	 document.getElementById("workProgress_error").innerHTML='Please select either all or individual budget heads. Combination is not allowed.';
				  	 window.scroll(0,0);
				  	 return false;
			     }    
		     }
		}
		allSelected = false;
		for (var i = 0; i < depositCodesDD.options.length; i++) {
		     if(depositCodesDD.options[i].selected ==true){
		    	 oneSelectedDepCode = true;
			     if(i==0)
			    	 allSelected = true;
			     if(allSelected &&  i!=0)
			     {
			    	 document.getElementById("workProgress_error").style.display='';
				  	 document.getElementById("workProgress_error").innerHTML='Please select either all or individual desposit codes. Combination is not allowed.';
				  	 window.scroll(0,0);
				  	 return false;
			     }    
		     }
		}
		if(oneSelectedDepCode && oneSelected)
		{
			document.getElementById("workProgress_error").style.display='';
		  	 document.getElementById("workProgress_error").innerHTML='You can either select a budget head or a deposit code , but you cannot select both together';
		  	 window.scroll(0,0);
		  	 return false;	
		}
		for (var i = 0; i < allbudgetHeadsDD.options.length; i++) {
		     if(allbudgetHeadsDD.options[i].selected ==true){
		    	 oneSelectedAllBudgetHeads = true;
		     }
		}
		if((oneSelectedDepCode || oneSelected) && oneSelectedAllBudgetHeads)
		{
			document.getElementById("workProgress_error").style.display='';
		  	 document.getElementById("workProgress_error").innerHTML='You can either select a budget head or a deposit code , but you cannot select both together';
		  	 window.scroll(0,0);
		  	 return false;	
		}
		for (var i = 0; i < alldepositCodesDD.options.length; i++) {
		     if(alldepositCodesDD.options[i].selected ==true){
		    	 oneSelectedAllDepCode = true;
		     }
		}
		if((oneSelectedDepCode || oneSelected || oneSelectedAllBudgetHeads) && oneSelectedAllDepCode)
		{
			document.getElementById("workProgress_error").style.display='';
		  	 document.getElementById("workProgress_error").innerHTML='You can either select a budget head or a deposit code , but you cannot select both together';
		  	 window.scroll(0,0);
		  	 return false;	
		}
		if(!(oneSelectedDepCode || oneSelected || oneSelectedAllBudgetHeads || oneSelectedAllDepCode))
		{
			document.getElementById("workProgress_error").style.display='';
		  	 document.getElementById("workProgress_error").innerHTML='You can either select a budget head or a deposit code , but you cannot select both together';
		  	 window.scroll(0,0);
		  	 return false;	
		}
		document.getElementById("workProgress_error").style.display='none';
	  	document.getElementById("workProgress_error").innerHTML='';
	  	return true;
	}
	
	function setEstimateDateRange(elem){
		dom.get("fromDate").value = "";
		dom.get("toDate").value = "";
		var dropdownId = elem.value;
		var currFinYear = dom.get("currentFinancialYearId").value;
		var toDt = document.getElementById("toDate");
		
		if(dropdownId==currFinYear){
			toDt.readOnly = false;
		}
		else{
			toDt.readOnly = true;
		}
		var leng = dropdownId.length;
		var finYRStr = dom.get("finYearRangeStr").value;
		var index=-1;
		if(dropdownId!=-1)
		{
			index = finYRStr.search("id:"+dropdownId+"--");
			if(index!=-1)
			{
				dom.get("fromDate").value=finYRStr.substr(index+leng+5,10);
				dom.get("toDate").value=finYRStr.substr(index+leng+17,10);
			}	
		}	
	}


	function setDateOnLoadForCurrFinYear() {
		var toDt = document.getElementById("toDate");
		var finYearVal = dom.get("finYearId").value;
		var currFinYear = dom.get("currentFinancialYearId").value;

		if(finYearVal != -1 && toDt.value !=""){
			if(finYearVal==currFinYear){
				toDt.readOnly = false;
			}
			else{
				toDt.readOnly = true;
			}
		}
}