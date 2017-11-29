/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
	function viewEstimatesDrillDown(reportSource, contextPath,deptName,estStatus) {
		if(reportSource=="2")
		{
			if(!validate())
				return;
			generateSubHeader('2');
		}
		else
			generateSubHeader('1');	
		dom.get("departmentName").value=deptName;
		var budgetHeadParams="";
		budgetHeadParams = getBudgetHeadsParams(reportSource); 
		var depositCodesParams = getDepositCodesParams(reportSource);
		var parameter=generateURLParams('viewEstimatesDrillDown',estStatus,budgetHeadParams,deptName,reportSource);
		if(estStatus=='tenderYetToBeCalledEstimate') {
			window.open(contextPath+"/reports/workProgressAbstract2!showTenderYetToBeCalledEstDrillDown.action?"+parameter+depositCodesParams,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
		}
		else if(estStatus=='tenderYetToBeFinalisedEstimate') {
			window.open(contextPath+"/reports/workProgressAbstract2!showTenderYetToBeFinalizedEstDrillDown.action?"+parameter+depositCodesParams,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
		}
		else if(estStatus=='adminSancEstValue') {
			window.open(contextPath+"/reports/workProgressAbstract2!showAdminSancEstValueDrillDown.action?"+parameter+depositCodesParams,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
		}
		else if(estStatus=='spilloverWorksEstimate') {
			window.open(contextPath+"/reports/workProgressAbstract2!showSpillOverEstimateDrillDown.action?"+parameter+depositCodesParams,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
		}
		else if(estStatus=='currentYearEstimates'|| estStatus=='spillOverEstimates'){
			window.open(contextPath+"/reports/workProgressAbstract3!showAdminSancEstimatesDrillDown.action?"+parameter+depositCodesParams,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
		}
		else {
			window.open(contextPath+"/reports/workProgressAbstract!showEstimatesForDrillDown.action?"+parameter+depositCodesParams,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
		}
	}
	
	function viewTenderFinalizedDrillDown(reportSource, contextPath,deptName, type) {
		if(reportSource=="2")
		{
			if(!validate())
				return;
			generateSubHeader('2');
		}
		else
			generateSubHeader('1');		
		dom.get("departmentName").value=deptName;
		var budgetHeadParams="";
		budgetHeadParams = getBudgetHeadsParams(reportSource); 
		var depositCodesParams = getDepositCodesParams(reportSource);
		var parameter=generateURLParams('viewTenderFinalizedDrillDown',type,budgetHeadParams,deptName,reportSource);
		window.open(contextPath+"/reports/workProgressAbstract!showTenderFinalizedForDrillDown.action?"+parameter+depositCodesParams,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}
	
	function showWPDetails(reportSource, contextPath,deptName, wpDrillDownType){
		if(reportSource=="2")
		{
			if(!validate())
				return;
			generateSubHeader('2');
		}
		else
			generateSubHeader('1');
		dom.get("departmentName").value=deptName;
		var budgetHeadParams="";
		budgetHeadParams = getBudgetHeadsParams(reportSource); 
		var depositCodesParams = getDepositCodesParams(reportSource);
		var parameter=generateURLParams('showWPDetails',wpDrillDownType,budgetHeadParams,deptName,reportSource);
		if(wpDrillDownType=='tenderYetToBeFinalisedWP') {
			window.open(contextPath+"/reports/workProgressAbstract2!showTenderYetToBeFinalizeWPDrillDown.action?"+parameter+depositCodesParams,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
		}
		else {
			window.open(contextPath+"/reports/workProgressAbstract!showWPDetails.action?"+parameter+depositCodesParams,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
		}
	}
	
	function showEstDetails(reportSource, contextPath,deptName){
		if(reportSource=="2")
		{
			if(!validate())
				return;
			generateSubHeader('2');
		}
		else
			generateSubHeader('1');
		dom.get("departmentName").value=deptName;
		var budgetHeadParams="";
		budgetHeadParams = getBudgetHeadsParams(reportSource); 
		var depositCodesParams = getDepositCodesParams(reportSource);
		var parameter=generateURLParams('showEstDetails','',budgetHeadParams,deptName,reportSource);
		window.open(contextPath+"/reports/workProgressAbstract!showWPEstDetails.action?"+parameter+depositCodesParams,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}

	function showWODetails(reportSource, contextPath,deptName,type){
		if(reportSource=="2")
		{
			if(!validate())
				return;
			generateSubHeader('2');
		}
		else
			generateSubHeader('1');
		dom.get("departmentName").value=deptName;
		var budgetHeadParams="";
		budgetHeadParams = getBudgetHeadsParams(reportSource); 
		var depositCodesParams = getDepositCodesParams(reportSource);
		var parameter=generateURLParams('showWODetails',type,budgetHeadParams,deptName,reportSource);
		window.open(contextPath+"/reports/workProgressAbstract!showWODetails.action?"+parameter+depositCodesParams,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}

	function showWOEstDetails(reportSource, contextPath,deptName,type){
		if(reportSource=="2")
		{
			if(!validate())
				return;
			generateSubHeader('2');
		}
		else
			generateSubHeader('1');
		dom.get("departmentName").value=deptName;
		var budgetHeadParams="";
		budgetHeadParams = getBudgetHeadsParams(reportSource); 
		var depositCodesParams = getDepositCodesParams(reportSource);
		var parameter=generateURLParams('showWOEstDetails',type,budgetHeadParams,deptName,reportSource);
		window.open(contextPath+"/reports/workProgressAbstract!showWOEstimateDetails.action?"+parameter+depositCodesParams,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}

	function ViewWorkValueDrillDown(reportSource, contextPath,deptName, workValueDrillType) {
		if(reportSource=="2")
		{
			if(!validate())
				return;
			generateSubHeader('2');
		}
		else
			generateSubHeader('1');
		dom.get("departmentName").value=deptName;
		var budgetHeadParams="";
		budgetHeadParams = getBudgetHeadsParams(reportSource); 
		var depositCodesParams = getDepositCodesParams(reportSource);
		var parameter=generateURLParams('ViewWorkValueDrillDown',workValueDrillType,budgetHeadParams,deptName,reportSource);
		window.open(contextPath+"/reports/workProgressAbstract!showWorkValueForDrillDown.action?"+parameter+depositCodesParams,'',
				'height=650,width=700,scrollbars=yes,left=0,top=0,status=yes');
	}

	function showMilestoneDetails(reportSource, contextPath,deptName,type) {
		if(reportSource=="2")
		{
			if(!validate())
				return;
			generateSubHeader('2');
		}
		else
			generateSubHeader('1');
		dom.get("departmentName").value=deptName;
		var budgetHeadParams="";
		budgetHeadParams = getBudgetHeadsParams(reportSource); 
		var depositCodesParams = getDepositCodesParams(reportSource);
		var parameter=generateURLParams('showMilestoneDetails',type,budgetHeadParams,deptName,reportSource);
		window.open(contextPath+"/reports/workProgressAbstract!showMilestoneDetails.action?"+parameter+depositCodesParams,'',
				'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}
	
	function viewSpillOverWorkValue(reportSource, contextPath,deptName) {
		if(reportSource=="2")
		{
			if(!validate())
				return;
			generateSubHeader('2');
		}
		else
			generateSubHeader('1');
		dom.get("departmentName").value=deptName;
		var budgetHeadParams="";
		budgetHeadParams = getBudgetHeadsParams(reportSource); 
		var depositCodesParams = getDepositCodesParams(reportSource);
		var parameter=generateURLParams('viewSpillOverWorkValue','',budgetHeadParams,deptName,reportSource);
		window.open(contextPath+"/reports/workProgressAbstract2!showSpillOverWorkValue.action?"+parameter+depositCodesParams,'',
				'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	
	function generateURLParams(callingFunc,param1,budgetHeadParams,deptName,reportSource)
	{
		var additionalParam = "";
		//Generate the subheader
		if(callingFunc =='viewEstimatesDrillDown')
			additionalParam='&estStatus='+param1;
		if(callingFunc =='viewTenderFinalizedDrillDown')
			additionalParam='&tenderFinalDrillType='+param1;
		if(callingFunc =='showWODetails')
			additionalParam='&woDrillDownType='+param1;
		if(callingFunc =='showWOEstDetails')
			additionalParam='&woDrillDownType='+param1;
		if(callingFunc =='ViewWorkValueDrillDown')
			additionalParam='&workValueDrillType='+param1;
		if(callingFunc =='showMilestoneDetails')
			additionalParam='&milestoneDrillDownType='+param1;
		if(callingFunc =='showWPDetails') 
			additionalParam='&wpDrillDownType='+param1;
		if(callingFunc =='showWOYetToBeCreatedEstDetails')	
			additionalParam='&wpDrillDownType='+param1;
		var parameter='fromDate='+dom.get("fromDate").value+'&toDate='+dom.get("toDate").value+'&worksType='+dom.get("worksType").value+'&executingDepartment='+dom.get("executingDepartment").value;
			parameter=parameter+'&worksSubType='+dom.get("worksSubType").value+'&fund='+dom.get("fund").value+'&function='+dom.get("function").value;
			parameter=parameter+'&preparedBy='+dom.get("preparedBy").value+'&scheme='+dom.get("scheme").value+'&subScheme='+dom.get("subScheme").value;
			parameter=parameter+budgetHeadParams+'&coa='+dom.get("coa").value;
			parameter=parameter+'&departmentName='+deptName+additionalParam;
			parameter=parameter+'&natureOfWork='+dom.get("natureOfWork").value+'&subHeader='+dom.get("subHeader").value;
			parameter=parameter+'&reportSource='+reportSource;

		return parameter;
	}
	
	function generateSubHeader(reportSource)
	{
		var  fromDateVal =dom.get("fromDate").value;
		var  toDateVal=dom.get("toDate").value;
		var  executingDepartmentVal=dom.get("executingDepartment").value;
		var  natureOfWorkVal=dom.get("natureOfWork").value;
		var  worksTypeVal=dom.get("worksType").value;
		var  worksSubTypeVal=dom.get("worksSubType").value;
		var  fundVal=dom.get("fund").value;
		var  functionVal=dom.get("function").value;
		var  preparedByVal=dom.get("preparedBy").value;
		var  schemeVal=dom.get("scheme").value;
		var  subSchemeVal=dom.get("subScheme").value;
		var  budgetHeadInReport1=dom.get("budgetHeads");
		if(budgetHeadInReport1)
			budgetHeadInReport1=budgetHeadInReport1.value;
		else
			budgetHeadInReport1='';
		var  budgetHeadVal=dom.get("dropDownBudgetHeads");
		if(budgetHeadVal)
			budgetHeadVal=budgetHeadVal.value;
		else
			budgetHeadVal='';
		var  depositCodesVal=dom.get("dropDownDepositCodes");
		if(depositCodesVal)
			depositCodesVal=depositCodesVal.value;
		else
			depositCodesVal='';
		var  allbudgetHeadVal=dom.get("allBudgetHeads");
		if(allbudgetHeadVal)
			allbudgetHeadVal=allbudgetHeadVal.value;
		else
			allbudgetHeadVal='';
		var  alldepositCodesVal=dom.get("allDepositCodes");
		if(alldepositCodesVal)
			alldepositCodesVal=alldepositCodesVal.value;
		else
			alldepositCodesVal='';
		var  coaVal=dom.get("coa");
		if(coaVal)
			coaVal=coaVal.value;
		else
			coaVal=-1;
		var count = 0;
		var subHeaderTxt = "Report";
		var singleConditionHeader='';	
		if(fromDateVal!="")
		{
			count++;
			subHeaderTxt = "Report from " + fromDateVal +" to current date";
		}	
		if(toDateVal!="")
		{
			count++;
			subHeaderTxt = "Report as on " + toDateVal;
		}
		if(fromDateVal!="" && toDateVal!="")
		{
			count++;
			subHeaderTxt = "Report for date range " + fromDateVal + " - " +toDateVal ;
		}
		if(reportSource=="2")
		{
			var  finYearVal = dom.get("finYearId");
			if(finYearVal.value != -1)
			{
				count++;
				subHeaderTxt = "Report for Financial Year " + finYearVal.options[finYearVal.selectedIndex].text;
			}
		}
		if(executingDepartmentVal!=-1)
		{
			count++;
			var deptIndex = dom.get("executingDepartment").selectedIndex;
			var deptOptions= dom.get("executingDepartment").options;
			subHeaderTxt = subHeaderTxt + " for department " + deptOptions[deptIndex].text  ;
			singleConditionHeader = "Report for department "+ deptOptions[deptIndex].text;
		}
		if(natureOfWorkVal!=-1)
		{
			count++;
			var natureOfWorkIndex = dom.get("natureOfWork").selectedIndex;
			var natureOfWorkOptions= dom.get("natureOfWork").options;
			subHeaderTxt = subHeaderTxt + " with nature of work " + natureOfWorkOptions[natureOfWorkIndex].text;
			singleConditionHeader = "Report for nature of work "+ natureOfWorkOptions[natureOfWorkIndex].text;
		}
		if(worksTypeVal!=-1)
		{
			count++;
			var worksTypeIndex = dom.get("worksType").selectedIndex;
			var worksTypeOptions= dom.get("worksType").options;
			subHeaderTxt = subHeaderTxt + " with type of work " + worksTypeOptions[worksTypeIndex].text;
			singleConditionHeader = "Report for type of work "+ worksTypeOptions[worksTypeIndex].text;
		}
		if(worksSubTypeVal!=-1)
		{
			count++;
			var worksSubTypeIndex = dom.get("worksSubType").selectedIndex;
			var worksSubTypeOptions= dom.get("worksSubType").options;
			subHeaderTxt = subHeaderTxt + " with subtype of work " + worksSubTypeOptions[worksSubTypeIndex].text;
			singleConditionHeader = "Report for subtype of work "+ worksSubTypeOptions[worksSubTypeIndex].text;
		}
		if(fundVal!=-1)
		{
			count++;
			var fundIndex = dom.get("fund").selectedIndex;
			var fundOptions= dom.get("fund").options;
			subHeaderTxt = subHeaderTxt + " under " + fundOptions[fundIndex].text;
			singleConditionHeader = "Report for fund "+ fundOptions[fundIndex].text;
		}
		if(functionVal!=-1)
		{
			count++;
			var functionIndex = dom.get("function").selectedIndex;
			var functionOptions= dom.get("function").options;
			subHeaderTxt = subHeaderTxt + " for function " + functionOptions[functionIndex].text;
			singleConditionHeader = "Report for function "+ functionOptions[functionIndex].text;
		}
		if(preparedByVal!=-1)
		{
			count++;
			var preparedByIndex = dom.get("preparedBy").selectedIndex;
			var preparedByOptions= dom.get("preparedBy").options;
			subHeaderTxt = subHeaderTxt + " as prepared by " + preparedByOptions[preparedByIndex].text;
			singleConditionHeader = "Report as prepared by "+ preparedByOptions[preparedByIndex].text;
		}
		if(schemeVal!=-1)
		{
			count++;
			var schemeIndex = dom.get("scheme").selectedIndex;
			var schemeOptions= dom.get("scheme").options;
			subHeaderTxt = subHeaderTxt + " under scheme " + schemeOptions[schemeIndex].text;
			singleConditionHeader = "Report for scheme "+ schemeOptions[schemeIndex].text;
		}
		if(subSchemeVal!=-1)
		{
			count++;
			var subSchemeIndex = dom.get("subScheme").selectedIndex;
			var subSchemeOptions= dom.get("subScheme").options;
			subHeaderTxt = subHeaderTxt + " under subscheme " + subSchemeOptions[subSchemeIndex].text;
			singleConditionHeader = "Report for sub-scheme "+ subSchemeOptions[subSchemeIndex].text;
		}
		if(budgetHeadVal!='' && budgetHeadVal!=-1) {
			count++;
			var budgetHeadText="";
			var x=document.getElementById("dropDownBudgetHeads");
			  for (var i = 1; i < x.options.length; i++) {
				 if(budgetHeadVal=='All')
				 {
					 if(budgetHeadText=='')
				    	 budgetHeadText=x.options[i].text;
				     else
				    	 budgetHeadText=budgetHeadText+", "+x.options[i].text; 
				 }
				 else
				 {
					 if(x.options[i].selected ==true){
					     if(budgetHeadText=='')
					    	 budgetHeadText=x.options[i].text;
					     else
					    	 budgetHeadText=budgetHeadText+", "+x.options[i].text; 
				     }
				 }	 
			     
			}
			subHeaderTxt = subHeaderTxt + " with Budget Head(s) "+budgetHeadText;
			singleConditionHeader = "Report for Budget Head(s) "+budgetHeadText;
		}
		if(budgetHeadInReport1!='' && budgetHeadInReport1!=-1) {
			count++;
			var budgetHeadText="";
			var x=document.getElementById("budgetHeads");
			  for (var i = 1; i < x.options.length; i++) {
				 if(x.options[i].selected ==true){
				     if(budgetHeadText=='')
				    	 budgetHeadText=x.options[i].text;
				     else
				    	 budgetHeadText=budgetHeadText+", "+x.options[i].text; 
			     }
			}
			subHeaderTxt = subHeaderTxt + " with Budget Head(s) "+budgetHeadText;
			singleConditionHeader = "Report for Budget Head(s) "+budgetHeadText;
		}
		if(allbudgetHeadVal!='' && allbudgetHeadVal!=-1) {
			count++;
			var budgetHeadText="";
			var x=document.getElementById("allBudgetHeads");
			  for (var i = 0; i < x.options.length; i++) {
				 if(x.options[i].selected ==true){
				     if(budgetHeadText=='')
				    	 budgetHeadText=x.options[i].text;
				     else
				    	 budgetHeadText=budgetHeadText+", "+x.options[i].text; 
			     }
			}
			subHeaderTxt = subHeaderTxt + " with Budget Head(s) "+budgetHeadText;
			singleConditionHeader = "Report for Budget Head(s) "+budgetHeadText;
		}
		if(alldepositCodesVal!='' && alldepositCodesVal!=-1 ) {
			count++;
			var depositCodesText="";
			var x=document.getElementById("allDepositCodes");
			  for (var i = 0; i < x.options.length; i++) {
				  if(x.options[i].selected ==true){
					     if(depositCodesText=='')
					    	 depositCodesText=x.options[i].text;
					     else
					    	 depositCodesText=depositCodesText+", "+x.options[i].text; 
				     }
			}
			subHeaderTxt = subHeaderTxt + " with Deposit Code(s) "+depositCodesText;
			singleConditionHeader = "Report for Deposit Code(s) "+depositCodesText;
		}
		if(depositCodesVal!='' && depositCodesVal!=-1 ) {
			count++;
			var depositCodesText="";
			var x=document.getElementById("dropDownDepositCodes");
			  for (var i = 1; i < x.options.length; i++) {
				  if(depositCodesVal=='All')
				  {
					  if(depositCodesText=='')
					    	 depositCodesText=x.options[i].text;
					     else
					    	 depositCodesText=depositCodesText+", "+x.options[i].text; 
				  }
				  else
				  {
					  if(x.options[i].selected ==true){
						     if(depositCodesText=='')
						    	 depositCodesText=x.options[i].text;
						     else
						    	 depositCodesText=depositCodesText+", "+x.options[i].text; 
					     }
				  }	  
			     
			}
			subHeaderTxt = subHeaderTxt + " with Deposit Code(s) "+depositCodesText;
			singleConditionHeader = "Report for Deposit Code(s) "+depositCodesText;
		}
		if(coaVal != -1)
		{
			count++;
			var coaIndex = dom.get("coa").selectedIndex;
			var coaOptions= dom.get("coa").options;
			subHeaderTxt = subHeaderTxt + " with deposit coa " + coaOptions[coaIndex].text;
			singleConditionHeader = "Report for deposit coa "+ coaOptions[coaIndex].text;
		}
		if(count==0)
			dom.get("subHeader").value = "";
		if(count==1)
			dom.get("subHeader").value = singleConditionHeader;
		if(count>1)
			dom.get("subHeader").value = subHeaderTxt;
	}
	function setupSubTypes(elem){
		worksSubTypeId=elem.options[elem.selectedIndex].value;
    	populateworksSubType({category:worksSubTypeId});
	}	
	

	function setupSubSchemes(elem){
		var id=elem.options[elem.selectedIndex].value;
		populatesubScheme({schemeId:id});
	}
	
	function loadWaitingImage() {
		if(document.getElementById('loading')){
			document.getElementById('loading').style.display='block';
		}
	}

	function clearWaitingImage(){
		if(document.getElementById('loading')){
			document.getElementById('loading').style.display='none';
		}
	}
	function hideResult()
	{
		dom.get("resultRow").style.display='none';
	}
	
	function getBudgetHeadsParams(reportSource)
	{
		var budgetHeadParams="";
		var x=document.getElementById("dropDownBudgetHeads");
		if(x)
		{
			// When all is selected
			if(x.options[0].selected)
			{
				for (var i = 1; i < x.options.length; i++) {
				     if(budgetHeadParams=='')
				    	 budgetHeadParams="&budgetHeadsStr="+x.options[i].value;
				     else 
				    	 budgetHeadParams=budgetHeadParams+","+x.options[i].value; 
				}
			}
			else
			{
				for (var i = 1; i < x.options.length; i++) {
				     if(x.options[i].selected ==true){
					     if(budgetHeadParams=='') 
					    	 budgetHeadParams="&budgetHeadsStr="+x.options[i].value;
					     else 
					    	 budgetHeadParams=budgetHeadParams+","+x.options[i].value; 
				     }
				}
			}
		}	
		var originalReportBudgetHeadParams="";
		var originalRpt=document.getElementById("budgetHeads");
		if(originalRpt)
		{
			for (var i = 1; i < originalRpt.options.length; i++) {
			     if(originalRpt.options[i].selected ==true){
				     if(originalReportBudgetHeadParams=='') 
				    	 originalReportBudgetHeadParams="&budgetHeadsFirstReportStr="+originalRpt.options[i].value;
				     else 
				    	 originalReportBudgetHeadParams=originalReportBudgetHeadParams+","+originalRpt.options[i].value; 
			     }
			}
		}
		//For newly added budget head drop down bug 21720 . Only budgetHeads or allBudgetHeads can be present
		x=document.getElementById("allBudgetHeads");
		if(x)
		{
			for (var i = 0; i < x.options.length; i++) {
			     if(x.options[i].selected ==true){
				     if(budgetHeadParams=='') 
				    	 budgetHeadParams="&budgetHeadIdsStr="+x.options[i].value;
				     else 
				    	 budgetHeadParams=budgetHeadParams+","+x.options[i].value; 
			     }
			}
		}
		budgetHeadParams = originalReportBudgetHeadParams+ budgetHeadParams;
	   return budgetHeadParams;
	}
	function getDepositCodesParams(reportSource)
	{
		var depositCodeParams="";
		if(reportSource=='2')
		{
			var x=document.getElementById("dropDownDepositCodes");
			// When all is selected
			if(x.options[0].selected)
			{
				for (var i = 1; i < x.options.length; i++) {
				     if(depositCodeParams=='')
				    	 depositCodeParams="&depositCodesStr="+x.options[i].value;
				     else 
				    	 depositCodeParams=depositCodeParams+","+x.options[i].value; 
				}
			}
			else
			{
				for (var i = 1; i < x.options.length; i++) {
				     if(x.options[i].selected ==true){
					     if(depositCodeParams=='') 
					    	 depositCodeParams="&depositCodesStr="+x.options[i].value;
					     else 
					    	 depositCodeParams=depositCodeParams+","+x.options[i].value; 
				     }
				}
			}
			//For newly added budget head drop down bug 21720 . Only dropDownDepositCodes or allDepositCodes can be present
			x=document.getElementById("allDepositCodes");
			for (var i = 0; i < x.options.length; i++) {
			     if(x.options[i].selected ==true){
				     if(depositCodeParams=='') 
				    	 depositCodeParams="&depositCodeIdsStr="+x.options[i].value;
				     else 
				    	 depositCodeParams=depositCodeParams+","+x.options[i].value; 
			     }
			}
		}	
	   return depositCodeParams;
	}
	
	function openWorkPrgReport(id,estId){
		window.open("${pageContext.request.contextPath}/reports/workProgressRegister!searchDetails.action?woId="+id+
			"&sourcePage=deptWiseReport&estId="+estId,'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}
	
	function showWOYetToBeCreatedEstDetails(reportSource, contextPath,deptName,type) {
		if(reportSource=="2")
		{
			if(!validate())
				return;
			generateSubHeader('2');
		}
		else
			generateSubHeader('1');
		dom.get("departmentName").value=deptName;
		var budgetHeadParams="";
		budgetHeadParams = getBudgetHeadsParams(reportSource); 
		var depositCodesParams = getDepositCodesParams(reportSource);
		var parameter=generateURLParams('showWOYetToBeCreatedEstDetails',type,budgetHeadParams,deptName,reportSource);
		window.open(contextPath+"/reports/workProgressAbstract2!showWOYetToBeGivenEstDrillDown.action?"+parameter+depositCodesParams,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}
	// NOTE --------- This method can be used in the future to create post form submits --------------	
	function post_to_url() {
	    method =  "post"; // Set method to post by default if not specified.

	    // The rest of this code assumes you are not using a library.
	    // It can be made less wordy if you use one.
	    var form = document.createElement("form");
	    form.setAttribute("method", method);
	    form.setAttribute("action", "/egworks/reports/workProgressAbstract!search.action");

	    //for(var key in params) {
	      //  if(params.hasOwnProperty(key)) {
	            var hiddenField = document.createElement("input");
	            hiddenField.setAttribute("type", "hidden");
	            hiddenField.setAttribute("name", "fromDate");
	            hiddenField.setAttribute("value", "01/04/2013");

	            form.appendChild(hiddenField);
	            
	            hiddenField = document.createElement("input");
	            hiddenField.setAttribute("type", "hidden");
	            hiddenField.setAttribute("name", "executingDepartment");
	            hiddenField.setAttribute("value", 35);

	            form.appendChild(hiddenField);
	            
	            hiddenField = document.createElement("input");
	            hiddenField.setAttribute("type", "hidden");
	            hiddenField.setAttribute("name", "toDate");
	            hiddenField.setAttribute("value", "01/05/2013");

	            form.appendChild(hiddenField);
	        // }
	    //}

	    document.body.appendChild(form);
	    form.submit();
	}
