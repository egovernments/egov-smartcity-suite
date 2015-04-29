<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<script src="<egov:url path='js/works.js'/>"></script>
<script src="<egov:url path='js/helper.js'/>"></script>
<script src="<egov:url path='js/changeFinancialDetailsHelper.js'/>"></script>
<html>
	<head>	
		<title><s:text name='change.gd.page.title'/> </title>
	</head>
	<style>
#warning {
  display:none;
  color:blue;
}
</style>
<script type="text/javascript">
	window.history.forward(1);
	var LAST_INVALID_ESTIMATE;
	var LAST_INVALID_YEAR_END_ESTIMATE;
	function noBack() {
		window.history.forward(); 
	}
	function viewEstimate(estId) {
		window.open("${pageContext.request.contextPath}/estimate/abstractEstimate!edit.action?id="+estId+"&sourcepage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}
	function setupSchemes(elem){
		clearMessage('changeFD_error');
	    var fundElem = document.getElementById('fund');
		var fundId = fundElem.options[elem.selectedIndex].value;
		var id=elem.options[elem.selectedIndex].value;
		<s:if test="%{schemeSelected==true}">
		    populatescheme({fundId:id});
		    populatesubScheme({schemeId:id});
	    </s:if>
	}
	var warnings=new Array();
	warnings['improperDepositCodeSelection']='<s:text name="estimate.depositCode.warning.improperDepositCodeSelection"/>'
	
	function warn(type){
	    dom.get(type+"Warning").innerHTML=warnings[type]
	    dom.get(type+"Warning").style.display='';
	    YAHOO.lang.later(3000,null,function(){dom.get(type+"Warning").style.display='none';});
	}
	
	function setupPreparedByList(elem) {
		deptId=elem.options[elem.selectedIndex].value;
	    populatepreparedBy({executingDepartment:deptId});
	}
	
	function enableDepositWorkFields() {
			var depWorks = document.getElementById("depWorks");
			var bdgtHd = document.getElementById("budgetHeadId");
			if(depWorks.checked) {
				document.getElementById('depositCOAFields').style.display='';
				document.getElementById('depWorks').value=true;
			 	document.getElementById('depWorks').checked=true;
			 	dom.get('budgetHeadId').value=-1;
			 	document.getElementById('budgetHeadRow').style.display='none';
				bdgtHd.disabled=true;
			} else {
				document.getElementById('depositCOAFields').style.display='none';
				document.getElementById('budgetHeadRow').style.display='';
				document.getElementById('depWorks').value=false;
			 	document.getElementById('depWorks').checked=false;
				bdgtHd.disabled=false;
			}
	}
	
	function enableSchemeWorksFields() {
		var schemeWorks = document.getElementById("schemeWorks");
		if(schemeWorks.checked) {
			document.getElementById('schemeFields').style.display='';
			document.getElementById('schemeWorks').value=true;
		 	document.getElementById('schemeWorks').checked=true;
		} else {
			document.getElementById('schemeFields').style.display='none';
			document.getElementById('schemeWorks').value=false;
		 	document.getElementById('schemeWorks').checked=false;
		}
	}
	
	function validateSearch(){
		if(document.getElementById('userDepartment').value==-1)
		{
			document.getElementById("changeFD_error").innerHTML='Please select user department'; 
	        document.getElementById("changeFD_error").style.display='';
	        window.scroll(0,0);
			return false;
		}
		document.getElementById("changeFD_error").innerHTML='';
		document.getElementById("changeFD_error").style.display="none";
		enableFields();
		return true;
	}
	
	var estimateNumberSearchSelectionHandler = function(sType, arguments){ 
		var oData = arguments[2];
	};
	
	var newDepositCodeSearchSelectionHandler = function(sType, arguments){ 
		var oData = arguments[2];
	};
		
	function checkAll(obj){ 
		var len=document.forms[0].selectedEstimate.length;
		var dwEnabled = document.getElementById('depWorks').checked;
		var schemeEnabled = document.getElementById('schemeWorks').checked;
		
			if(obj.checked){
				if(dwEnabled!=true && schemeEnabled!=true){
					if(len>0){
						for (i = 0; i < len; i++)
							document.forms[0].selectedEstimate[i].checked = true;
					}else document.forms[0].selectedEstimate.checked = true;
				}
				else{
					alert("Multi select not allowed");
					document.forms[0].checkedAll.checked = false
				}
			}
			else{
				if(len>0){
					for (i = 0; i < len; i++)
						document.forms[0].selectedEstimate[i].checked = false;
				}else document.forms[0].selectedEstimate.checked = false;
			}
	}
	
	function disableFD(){
		var checkAllForDesc = document.forms[0].checkedAll.checked;
		var len=eval('<s:property value="searchResult.fullListSize" />')+1;
		var i;
		if(document.forms[0].isDescChange.checked){
			if(checkAllForDesc){
				alert(" You have to modify the work description of the estimate one by one , please select one estimate from the list and then proceed ");
				if(len>0){
					for(i=1;i<len;i++) {
						var id='isEstimateSelected'+i.toString();
						if(dom.get(id))
						{
							dom.get(id).checked=false;
							dom.get(id).value=false;
						}	
					}
					document.forms[0].checkedAll.checked = false;	
				}
		
			}
			document.getElementById('newDescField').style.display='block';
			document.getElementById('FinDetailsTable').style.visibility='hidden';
		}
		else{
			document.getElementById('newDescField').style.display='none';
			document.getElementById('FinDetailsTable').style.visibility='visible';
	
		}
	}
	
	budgetLoadFailureHandler=function(){
		   showMessage('changeFD_error','Unable to load budget head information');
	}
	
	function validate(){
		var fundId = document.getElementById('fund');
		var funcId = document.getElementById('function');
		var bdgtId =  document.getElementById('budgetGroup');
		var desc = document.getElementById('workDesc');
		var depCodeCoa = document.getElementById('depositCOA');
		var depCodeId = document.getElementById('depositCode');
		var schemeId = document.getElementById('scheme');
		var subSchemeId = document.getElementById('subScheme');
		var isDepositWorks = dom.get('dwSelected').value;
		var isSchemeWork = dom.get('schemeSelected').value;
		//Set the values properly -------
		if(fundId)
			fundId = fundId.value;
		else
			fundId=-1;
		if(funcId)
			funcId = funcId.value;
		else
			funcId=-1;		
		if(bdgtId)
			bdgtId = bdgtId.value;
		else
			bdgtId=-1;
		if(desc)
			desc = desc.value;
		else
			desc="";
		if(depCodeCoa)
			depCodeCoa = depCodeCoa.value;
		else
			depCodeCoa = -1;
		if(depCodeId)
			depCodeId = depCodeId.value;
		else
			depCodeId="";
		if(schemeId)
			schemeId = schemeId.value;
		else
			schemeId=-1;
		if(subSchemeId)
			subSchemeId = subSchemeId.value;
		else
			subSchemeId=-1;
		//End of setting values --------
		var workDescCheckBox = document.forms[0].isDescChange.checked;
		if(!validateSubmit()){
			  return false;
		}
		  
		if(workDescCheckBox){
				if(desc==""){
					document.getElementById("changeFD_error").innerHTML='<s:text name="changeFDHeader.null.work.description" />'; 
			        document.getElementById("changeFD_error").style.display='';
			        window.scroll(0,0);
					return false;
				}
				else{
					document.getElementById("changeFD_error").innerHTML='';
					document.getElementById("changeFD_error").style.display="none";
					return true;
				}
			}
			else
			{
				if(isSchemeWork=='true'){
					if(schemeId==-1 && subSchemeId!=-1){
						document.getElementById("changeFD_error").innerHTML='<s:text name="changeFDHeader.select.both.scheme.subshceme" />'; 
				        document.getElementById("changeFD_error").style.display='';
				        window.scroll(0,0);
						return false;
					}
					if(fundId!=-1 && schemeId==-1){
						document.getElementById("changeFD_error").innerHTML='<s:text name="changeFDHeader.select.both.fund.scheme" />'; 
				        document.getElementById("changeFD_error").style.display='';
				        window.scroll(0,0);
						return false;
					}
				}
				if(isDepositWorks=='true'){
					if(fundId==-1 && funcId==-1 && depCodeCoa==-1 && depCodeId==''  && schemeId==-1 && subSchemeId==-1 ){
						document.getElementById("changeFD_error").innerHTML='<s:text name="changeFDHeader.select.info" />'; 
				        document.getElementById("changeFD_error").style.display='';
				        window.scroll(0,0);
						return false;
					}
					if(fundId==-1 && depCodeId!=''){
						document.getElementById("changeFD_error").innerHTML='<s:text name="changeFDHeader.select.both.fund.depcode" />'; 
				        document.getElementById("changeFD_error").style.display='';
				        window.scroll(0,0);
						return false;
					}
					if(fundId!=-1 && depCodeId==''){
						document.getElementById("changeFD_error").innerHTML='<s:text name="changeFDHeader.select.both.fund.depcode" />'; 
				        document.getElementById("changeFD_error").style.display='';
				        window.scroll(0,0);
						return false;
					}
					document.getElementById("changeFD_error").innerHTML='';
					document.getElementById("changeFD_error").style.display="none";
					return true;
				}
				else
				{
					//Budget head change case
					if(fundId==-1 && funcId==-1 && bdgtId==-1 && schemeId==-1 && subSchemeId==-1 ){
						document.getElementById("changeFD_error").innerHTML='<s:text name="changeFDHeader.select.info" />'; 
				        document.getElementById("changeFD_error").style.display='';
				        window.scroll(0,0);
						return false;
					}
					if(funcId!=-1 && bdgtId==-1){
						document.getElementById("changeFD_error").innerHTML='<s:text name="changeFDHeader.select.both.function.budgethead" />'; 
				        document.getElementById("changeFD_error").style.display='';
				        window.scroll(0,0);
						return false;
					}
					if(funcId==-1 && bdgtId!=-1){
						document.getElementById("changeFD_error").innerHTML='<s:text name="changeFDHeader.select.both.function.budgethead" />'; 
				        document.getElementById("changeFD_error").style.display='';
				        window.scroll(0,0);
						return false;
					}
				}	
			}	
			document.getElementById("changeFD_error").innerHTML='';
			document.getElementById("changeFD_error").style.display="none";
			return true;
	}
	
	function selectAllEstimates(obj) {
		var length=eval('<s:property value="searchResult.fullListSize" />')+1;
		var i;
	   if(obj.checked){
		 	obj.value=true;
		 	obj.checked=true;
			for(i=1;i<length;i++) {
				var id='isEstimateSelected'+i.toString();
				if(dom.get(id))
				{
				dom.get(id).checked=true;
				dom.get(id).value=true;
				}	
			}
		}
		else if(!obj.checked){
		 	obj.value=false;
		 	obj.checked=false;
			for(i=1;i<length;i++) {
				var id='isEstimateSelected'+i.toString();
				if(dom.get(id))
				{
					dom.get(id).checked=false;
					dom.get(id).value=false;
				}
			}
		}
	}
	   
	var depositCodeSelectionEnforceHandler = function(sType, arguments) {
		warn('improperDepositCodeSelection');
	}
	
	function validateSubmit(){
		clearMessage('changeFD_error');
		document.getElementById("changeFD_error").style.display='none';
		var manyEstimatesSelected=0;
		var length=eval('<s:property value="searchResult.fullListSize" />')+1;
		var i;
		for(i=1;i<length;i++) {
			var id='isEstimateSelected'+i.toString();
			if(dom.get(id) && dom.get(id).checked ){
				manyEstimatesSelected++;
				if(manyEstimatesSelected>1)
					break;
			}
		}
		if(manyEstimatesSelected==0)
		{
			document.getElementById("changeFD_error").style.display='';
			document.getElementById("changeFD_error").innerHTML='<s:text name="changeFDHeader.select.estimate" />';
			window.scroll(0,0);
			return false;
		}
		if(manyEstimatesSelected>1 && (document.getElementById("depWorks").checked || document.getElementById("schemeWorks").checked 
				||  document.getElementById("isDescChange").checked ))
		{
			document.getElementById("changeFD_error").style.display='';
			document.getElementById("changeFD_error").innerHTML='<s:text name="changeFDHeader.select.one.estimate" />';
			window.scroll(0,0);
			return false;
		}	
		return true;
	}
	
	function validateForWF(text){
		if(!validate()){
			  return false;
		}
		if(text=='Forward'){
			if(!validateWorkFlowApprover(text))
				return false;
			if(!checkForSubSchemeResetting())
				return false;
		}
		enableFields();
		return true;  
	}

	function checkForSubSchemeResetting()
	{
		var length=eval('<s:property value="searchResult.fullListSize" />')+1;
		var i;
		var schObj = document.getElementById("scheme");
		var ssObj = document.getElementById("subScheme");
		if(schObj && schObj.value!=-1 && ssObj && ssObj.value==-1)
		{
			for(i=1;i<length;i++) {
				var id='isEstimateSelected'+i.toString();
				var subSchemeCode='hiddenSubScheme'+i.toString();
				if(dom.get(id) && dom.get(id).checked ){
					if(dom.get(subSchemeCode) && dom.get(subSchemeCode).value!='' ){
						var ans = confirm("The Sub Scheme "+dom.get(subSchemeCode).value+" was already linked to this estimate. Do you want to reset that scheme to empty?");
						if(!ans)
							return false;
					}	
				}
			}
		}
		return true;	
	}
	
	function setupSearchSchemes(obj)
	{
		clearMessage('changeFD_error');
	    var fundElem = document.getElementById('searchFund');
		var fundId = fundElem.options[elem.selectedIndex].value;
		var id=elem.options[elem.selectedIndex].value;
		populatesearchScheme({fundId:id});
	}
	function setupSearchSubSchemes(elem){
		var id=elem.options[elem.selectedIndex].value;
		populatesearchSubScheme({schemeId:id});
	}
	function setupBudgetGroups(elem){
		var id=elem.options[elem.selectedIndex].value;
	       	makeJSONCall(["Text","Value"],'${pageContext.request.contextPath}/estimate/ajaxFinancialDetail!loadBudgetGroups.action',
	    	{functionId:id},budgetGroupDropdownSuccessHandler,budgetLoadFailureHandler) ;
	}
	function disableCheckbox()
	{
		var status =  '<s:property value="pageLoadStatus" />';
		if(status == 'afterSearch')
		{
			document.getElementById("depWorks").disabled=true;
			document.getElementById("schemeWorks").disabled=true;
		}	
	}
	
	function validateSelection(obj,estId,estNo)
	{
		if(!document.forms[0].isDescChange.checked){
			getListOfREs(obj,estId,estNo);
		}	
	}
	
	function getListOfREs(obj,estId,estNo){
		if(obj.checked){ 
	    	makeJSONCall(["estimateNo"],'${pageContext.request.contextPath}/revisionEstimate/ajaxRevisionEstimate!getListOfREsForParent.action',{estimateId:estId},reLoadHandler,reLoadFailureHandler);
		}
		else
		{
			var errorMessage = document.getElementById("changeFD_error").innerHTML;
			if(estNo==LAST_INVALID_ESTIMATE && errorMessage!='' && errorMessage.indexOf(estNo) > -1)
				clearMessage('changeFD_error');
		}		
	}
	
	reLoadHandler = function(req,res){
		results=res.results;
		if(results != '') {
			if(results.length>=1)
			{
				var firstObj = results[0].estimateNo +'' ;
				var indexOfLastSlash = firstObj.lastIndexOf("/");
				if(indexOfLastSlash!=-1)
				{
					var errorMessage  = "";
				  	for(var i=0; i<results.length;i++) {
				  	  	if(results[i].estimateNo != '' && results[i].estimateNo != undefined){
					  	  	if(errorMessage=='')
					  	  		errorMessage = results[i].estimateNo;
					  	  	else  	
				  	  			errorMessage = errorMessage+", " +  results[i].estimateNo;
				  	  	}
			  		}
			  		if(errorMessage!='')
				  	{
			  			LAST_INVALID_ESTIMATE = firstObj.substr(0,indexOfLastSlash);
						dom.get("changeFD_error").style.display='';
						document.getElementById("changeFD_error").innerHTML='<s:text name="changeFDHeader.ajax.rev.estimates.present1" />'+ errorMessage+' <s:text name="changeFDHeader.ajax.rev.estimates.present2" />';
						window.scroll(0,0);
						return false;
					}	
				}	
			}	
		}
	}	

	reLoadFailureHandler= function(){
	    dom.get("changeFD_error").style.display='';
		document.getElementById("changeFD_error").innerHTML='<s:text name="changeFDHeader.ajax.rev.estimates.failure" />';
		window.scroll(0,0);
	}
	
</script>
		
	<body onload="enableDepositWorkFields();enableSchemeWorksFields();defaultApproverDept();disableCheckbox();noBack();" onpageshow="if(event.persisted) noBack();" onunload="">
	<div class="errorstyle" id="changeFD_error"	style="display: none;"></div>
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
	<s:form action="changeFinancialDetails" name="changeFinancialDetailsForm" id="changeFinancialDetailsForm" theme="simple"  >
	<s:token />
	<s:push value="model">

	<s:hidden name="id" value="%{id}" id="id" />
 	<s:hidden name="dwSelected" id="dwSelected" />
	<s:hidden name="schemeSelected" id="schemeSelected" />
	<s:hidden name="actionName" id="actionName"/>
	<s:hidden id="additionalRuleValue" name="additionalRuleValue" value="%{additionalRuleValue}" />
	<s:hidden name="sourcepage" value="%{sourcepage}" id="sourcepage" />
	<s:hidden id="loggedInUserDept" name="loggedInUserDept" value="%{loggedInUserDept}" />
	
		<div class="formmainbox">
		<div class="insidecontent">
		<div class="rbroundbox2">
		<div class="rbtop2"><div></div></div>
		<div class="rbcontent2">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr><td>&nbsp;</td></tr>
			<tr>
				<td>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td colspan="4" class="headingwk">
								<div class="arrowiconwk">
									<img
										src="${pageContext.request.contextPath}/image/arrow.gif" />
								</div>
								<div class="headplacer">
									<s:text name="page.subheader.search.estimate" />
								</div>
							</td>
						</tr>
												
						<tr>
							<td class="greyboxwk">
								<s:text name="workprogressabstract.fromdate" />
							</td>
							<td class="greybox2wk">
									<s:date name="fromDate" var="fromDateFormat"
										format="dd/MM/yyyy" />
									<s:textfield name="fromDate" id="fromDate"
										cssClass="selectwk" value="%{fromDateFormat}"
										onfocus="javascript:vDateType='3';"
										onkeyup="DateFormat(this,this.value,event,false,'3')" />
									<a
										href="javascript:show_calendar('forms[0].fromDate',null,null,'DD/MM/YYYY');"
										onmouseover="window.status='Date Picker';return true;"
										onmouseout="window.status='';return true;"> <img
											src="${pageContext.request.contextPath}/image/calendar.png"
											alt="Calendar" width="16" height="16" border="0"
											align="absmiddle" />
									</a>

							</td>
							
							<td  class="greyboxwk">
								<s:text name="workprogressabstract.todate" />
							</td>
							<td width="17%" class="greybox2wk">
								<s:date name="toDate" var="toDateFormat"
									format="dd/MM/yyyy" />
								<s:textfield name="toDate" id="toDate"
									value="%{toDateFormat}" cssClass="selectwk"
									onfocus="javascript:vDateType='3';"
									onkeyup="DateFormat(this,this.value,event,false,'3')" />
								<a
									href="javascript:show_calendar('forms[0].toDate',null,null,'DD/MM/YYYY');"
									onmouseover="window.status='Date Picker';return true;"
									onmouseout="window.status='';return true;"> <img
										src="${pageContext.request.contextPath}/image/calendar.png"
										alt="Calendar" width="16" height="16" border="0"
										align="absmiddle" />
								</a>
							</td>
						</tr>
						<tr>
							<td  class="whiteboxwk">
								<s:text name='estimate.user.department'/> <span class="mandatory">*</span> :
							</td>
							<td class="whitebox2wk" >
								<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="userDepartment" 
									id="userDepartment" cssClass="selectwk" list="dropdownData.userDepartmentList" listKey="id" 
									listValue="deptName" value="%{userDepartment}" />
							</td>
							<td  class="whiteboxwk">
								<s:text name='estimate.executing.department'/> :
							</td>
							<td  class="whitebox2wk" >
								<s:select headerKey="-1"
                                    headerValue="%{getText('estimate.default.select')}"
                                    name="executingDepartment" id="executingDepartment"
                                    cssClass="selectwk"
                                    list="dropdownData.executingDepartmentList" listKey="id"
                                    listValue="deptName" value="%{executingDepartment}"
                                    onChange="setupPreparedByList(this);" />
	                              <egov:ajaxdropdown id="preparedBy"
									fields="['Text','Value','Designation']"
									dropdownId='preparedBy' optionAttributes='Designation'
									url='estimate/ajaxEstimate!usersInExecutingDepartment.action' />
                              </td> 
                                           
						</tr>
						<tr>
							<td class="greyboxwk"><s:text name='estimate.preparedBy'/> :</td >
							<td class="greybox2wk" >
								<s:select headerKey="-1"
									headerValue="%{getText('default.dropdown.select')}"
									name="preparedBy" value="%{preparedBy}" id="preparedBy"
									cssClass="selectwk" list="dropdownData.preparedByList"
									listKey="id" listValue="employeeName" />
							</td>
							<td width="15%" class="greyboxwk">
								<s:text name="estimate.number" />:
							</td>
							<td class="greybox2wk">
								<div class="yui-skin-sam">
	       							<div id="estimateNumberSearch_autocomplete">
	              						<div>
	       									<s:textfield id="estimateNumberSearch" name="estimatenumber" 
	       										value="%{estimatenumber}" cssClass="selectwk" />
	       								</div>
	       								<span id="estimateNumberSearchResults"></span>
	       							</div>	
	       						</div>
	       						<egov:autocomplete name="estimateNumberSearch" width="20" 
	       							field="estimateNumberSearch" url="ajaxChangeFinancialDetails!searchEstNoAjax.action?" 
	       							queryQuestionMark="false" results="estimateNumberSearchResults" 
	       							handler="estimateNumberSearchSelectionHandler" queryLength="3"/>
							</td>
						</tr>
						<tr>	
							<td width="15%" class="whiteboxwk">
								<s:text name='workprogressabstract.fund'/> :				
							</td>
							<td width="53%" class="whitebox2wk">
								<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}"
									name="searchFund" id="searchFund" cssClass="selectwk" list="dropdownData.fundList" listKey="id" 
									listValue="name" value="%{searchFund}" onchange="setupSearchSchemes(this);"/>
								<egov:ajaxdropdown id="searchSchemeDropdown" fields="['Text','Value']" dropdownId='searchScheme' url='estimate/ajaxChangeFinancialDetails!loadSchemes.action' selectedValue="%{searchFund}"/>
							</td>
							<td width="11%" class="whiteboxwk">
								<s:text name='workprogressabstract.function'/> :
							</td>
							<td width="21%" class="whitebox2wk" colspan="3">
								<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}"
				 					name="searchFunction" id="searchFunction" cssClass="selectwk" list="dropdownData.functionList" 
				 					listKey="id" listValue="name" value="%{searchFunction}"   />
							</td>
						</tr>
						<tr>
							<td width="20%" class="greyboxwk">
								<s:text name='estimateabstract.estimate.status'/> :				
							</td>
							<td class="greybox2wk" width="30%">
									<s:textfield name="status" value="%{status}" id="status" cssClass="selectwk" readonly="true"/>
							</td>
							<td colspan="2" class="greyboxwk"></td>
						</tr>
						<tr id="budgetHeadRow">		
							<td width="20%" class="whiteboxwk">
								<s:text name='estimate.financial.budgethead'/> :				
							</td>
							<td class="whitebox2wk" colspan="3">
								<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}"
								 	cssClass="selectwk" list="dropdownData.allBudgetHeadList" 
									listKey="id" listValue='name' name="budgetHeadId" 
									id="budgetHeadId" value="%{budgetHeadId}" />
							</td>
						</tr>
						<tr >
							<td width="15%" class="greyboxwk">
								<s:text name='change.fd.depositWorks'/>
							</td>
							<td class="greybox2wk">
			   					<s:checkbox name="depWorks" id="depWorks" value="%{depWorks}" onClick="enableDepositWorkFields(this);" />
				   			</td>
				   			<td width="15%" class="greyboxwk">
								<s:text name='change.fd.schemeWork'/>
							</td>
							<td class="greybox2wk">
			   					<s:checkbox name="schemeWorks" id="schemeWorks" value="%{schemeWorks}" onClick="enableSchemeWorksFields(this);"/>
				   			</td>
						</tr>
						<tr id="depositCOAFields">
							<td width="15%" class="whiteboxwk">
								<s:text name='change.fd.depositCOA'/>
							</td>
							<td class="whitebox2wk" width="20%">
   								<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" 
									name="depositCOAId" id="depositCOAId" cssClass="selectwk" list="dropdownData.allDepositCodeList"
									listKey="id" listValue='glcode  + " : " + name' value="%{depositCOAId}"/>						
   							</td>
   							<td width="15%" class="whiteboxwk">
								<s:text name='change.fd.depositCode'/>
							</td>
							<td class="whitebox2wk">
								<div class="yui-skin-sam">
        							<div id="searchFilterDepositCode_autocomplete">
               							<div>
        									<s:textfield id="searchFilterDepositCode" name="depCode" 
        										value="%{depCode}" cssClass="selectwk" />
        								</div>
        								<span id="searchFilterDepositCodeResults"></span>
        							</div>	
        						</div>
        						<egov:autocomplete name="searchFilterDepositCode" width="20" 
        							field="searchFilterDepositCode" url="ajaxChangeFinancialDetails!searchDepositCodeAjax.action?" 
        							queryQuestionMark="false" results="searchFilterDepositCodeResults" 
        							handler="newDepositCodeSearchSelectionHandler" queryLength="3"/>
							</td>
						</tr>
						<tr id="schemeFields">
							<td width="15%" class="whiteboxwk">
								<s:text name='estimate.financial.scheme'/> : 
							</td>
							<td class="whitebox2wk" width="20%">
   								<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="searchScheme" id="searchScheme" cssClass="selectwk" list="dropdownData.searchSchemeList" listKey="id" listValue="name" value="%{searchScheme}"  onChange="setupSearchSubSchemes(this);"/>
								<egov:ajaxdropdown id="searchSubSchemeDropdown" fields="['Text','Value']" dropdownId='searchSubScheme' url='estimate/ajaxChangeFinancialDetails!loadSubSchemes.action' selectedValue="%{searchSubScheme.id}"/>							
   							</td>
   							<td width="15%" class="whiteboxwk">
								Sub <s:text name='estimate.financial.scheme'/> : 
							</td>
							<td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="searchSubScheme" id="searchSubScheme" cssClass="selectwk" 
								list="dropdownData.searchSubSchemeList" listKey="id" listValue="name" value="%{searchSubScheme}" />
							</td>
						</tr>
						<tr>
							<td colspan="4" class="shadowwk"></td>
						</tr>
						<tr>
							<td colspan="4">
								<div align="right" class="mandatory" style="font-size: 11px; padding-right: 20px;">
								* <s:text name="default.message.mandatory" />
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="4">
								<div class="buttonholdersearch">
								    <s:submit cssClass="buttonadd" value="SEARCH" id="saveButton" name="button" onclick="return validateSearch();" method="searchList"/> &nbsp;&nbsp;&nbsp;
								    <s:submit cssClass="buttonadd" value="CLEAR" id="clearBtn" name="clearBtn" method="search"/>&nbsp;&nbsp;&nbsp;
								    <input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="button" onclick="window.close();" />
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="4">
								<div id="loading" style="position:relative; left:40%; top:10%; padding:2px; z-index:20001; height:auto;width:500px;display: none;">
									<div class="loading-indicator" style="background:white;  color:#444; font:bold 13px tohoma,arial,helvetica; padding:10px; margin:0; height:auto;">
										<img src="${pageContext.request.contextPath}/images/loading.gif" width="32" height="32" style="margin-right:8px;vertical-align:top;"/> Processing...
									</div>
								</div>
							</td>
						</tr>
						<tr><td colspan="4">&nbsp;</td></tr>
				</table>
			</td>
		</tr>
		</table>
	</div>
	<div class="rbcontent2">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td></td>
				</tr>
				<s:if test="%{searchResult.fullListSize != 0}">
					<tr>
						<td>
							<table width="100%" border="0" cellspacing="0"
								cellpadding="0">
								<tr>
									<td class="headingwk" align="left">
										<div class="arrowiconwk">
											<img
												src="${pageContext.request.contextPath}/image/arrow.gif" />
										</div>
										<div class="headerplacer">
											<s:text name='search.result' />
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td>
							<display:table name="searchResult" uid="currentRow"
								cellpadding="0" cellspacing="0" requestURI=""
								style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">

								<display:column headerClass="pagetableth"
									class="pagetabletd" 
									title='<input type="checkbox" id="checkedAll" name="checkedAll" onclick="selectAllEstimates(this)" />'
									style="width:3%;text-align:center">
									<input type='hidden'
										name='changeFinDetDetail[<s:property value='#attr.currentRow_rowNum' />].abstractEstimate.id'
										value='<s:property value="#attr.currentRow[0].id" />' />
									
									<input type='checkbox' id='isEstimateSelected<s:property value='#attr.currentRow_rowNum' />'   
										name='changeFinDetDetail[<s:property value='#attr.currentRow_rowNum' />].isChecked' 
										onclick='populateIsChecked(this);validateSelection(this,<s:property  value="%{#attr.currentRow[0].id}" />,"<s:property  value="%{#attr.currentRow[0].estimateNumber}" />");' value='false' />
								</display:column>
								
								<display:column title="Sl.No"
									titleKey='estimate.search.slno' headerClass="pagetableth"
									class="pagetabletd" style="width:2%;text-align:right">
									<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
								</display:column>
								
								<display:column headerClass="pagetableth"
									class="pagetabletd" title="Estimate Number"
									style="width:8%;text-align:left">
									<a href="Javascript:viewEstimate('<s:property  value='%{#attr.currentRow[0].id}' />')">
										<s:property value="#attr.currentRow[0].estimateNumber" />
									</a>
								</display:column>
								
								<display:column headerClass="pagetableth"
									class="pagetabletd" title="Estimate Date"
									titleKey='estimate.search.estimateDate'
									style="width:8%;text-align:center">
									<s:date name="#attr.currentRow[0].estimateDate" format="dd/MM/yyyy" />
									<input type='hidden' name="estimateDate" id="estimateDate" value='<s:property value="#attr.currentRow[0].estimateDate" />' />
         						</display:column>
								
								<display:column headerClass="pagetableth"
									class="pagetabletd" title="User Department"
									titleKey='plannedEstimateReport.userDept'
									style="width:8%;text-align:left">
									<s:property value="#attr.currentRow[0].userDepartment.deptName" />
								</display:column>	
								
								<display:column headerClass="pagetableth"
									class="pagetabletd" title="Executing Department"
									titleKey='estimate.search.executingdept'
									style="width:8%;text-align:left">
									<s:property value="#attr.currentRow[0].executingDepartment.deptName" />
								</display:column>	
								
								<display:column headerClass="pagetableth"
									class="pagetabletd" title="Name"
									titleKey='estimate.search.name'
									style="width:20%;text-align:left">
									<s:property value="#attr.currentRow[0].name" />
								</display:column>
									
								<display:column headerClass="pagetableth"
									class="pagetabletd" title="Fund"
									titleKey='estimate.search.name'
									style="width:8%;text-align:left" >
									<s:property value="#attr.currentRow[1].fund.name" />
								</display:column>
								
								<display:column headerClass="pagetableth"
									class="pagetabletd" title="Function"
									titleKey='estimate.search.name'
									style="width:15%;text-align:left" >
									<s:property value="#attr.currentRow[1].function.name" />
								</display:column>
								
								<s:if test="%{dwSelected!=true}">
								<display:column headerClass="pagetableth"
									class="pagetabletd" title="Budget Head"
									titleKey='estimate.search.name'
									style="width:26%;text-align:left" >
									<s:property value="#attr.currentRow[1].budgetGroup.name" />	
								</display:column>
								</s:if>
								
								<s:if test="%{dwSelected==true}">
								<display:column headerClass="pagetableth"
									class="pagetabletd" title="Deposit COA,<br> Deposit Code"
									titleKey='estimate.search.name'
									style="width:37%;text-align:left" >
									<s:property value="#attr.currentRow[1].coa.glcode" /> , <s:property value="#attr.currentRow[0].depositCode.code" /> 	
								</display:column>
								</s:if>
								<s:if test="%{schemeSelected==true}">
								<display:column headerClass="pagetableth"
									class="pagetabletd" title="Scheme"
									titleKey='estimate.search.name'
									style="width:20%;text-align:left" >
									<s:property value="#attr.currentRow[1].scheme.name" />	
								</display:column>
								<display:column headerClass="pagetableth"
									class="pagetabletd" title="Sub Scheme"
									titleKey='estimate.search.name'
									style="width:20%;text-align:left" >
									<s:property value="#attr.currentRow[1].subScheme.name" />	
									<input type='hidden'
										id='hiddenSubScheme<s:property value='#attr.currentRow_rowNum' />'
										name='hiddenSubScheme<s:property value='#attr.currentRow_rowNum' />'
										value='<s:property value="#attr.currentRow[1].subScheme.name" />' />
								</display:column>
								</s:if>
								
								<display:column headerClass="pagetableth"
									class="pagetabletd" title="Project Code"
									titleKey='estimate.search.name'
									style="width:20%;text-align:left" >
									<s:property value="#attr.currentRow[0].projectCode.code" />
								</display:column>
												
								<display:column headerClass="pagetableth"
									class="pagetabletd" title="Total"
									titleKey='estimate.search.total'
									style="width:10%;text-align:right">
										<s:property value="%{#attr.currentRow[0].totalAmount.formattedString}" />
								</display:column>
																							
							</display:table>
						</td>
					</tr>
				</s:if>
				<s:elseif test="%{searchResult.fullListSize == 0}">
						<div>
							<table width="100%" border="0" cellpadding="0"
								cellspacing="0">
								<tr>
									<td align="center">
										<font color="red"><s:text name="label.no.records.found" /></font>
									</td>
								</tr>
							</table>
						</div>
				</s:elseif>
			</table>
			<br />
			<s:if test="%{searchResult.fullListSize != 0}">
				<table>
					<tr >
						<td class="whiteboxwk">
		   					<s:checkbox name="isDescChange" id="isDescChange" 	onClick="disableFD();" />
							<s:hidden id="isChecked" name="isChecked" value="%{isDescChange}"/>
			   			</td>
						<td class="whitebox2wk" colspan="2" >
							<s:text name='change.fd.desc.only'/>
						</td>
						<td class="whitebox2wk" id="newDescField" colspan="2" style="display: none;">
							<s:textarea name="newWorkDesc" cols="50" rows="3" cssClass="selectwk"
									 id="workDesc" value="%{newWorkDesc}"/>
						</td>
							<td class="whitebox2wk" colspan="2" />
					</tr>
					<table id="FinDetailsTable">
						<tr>
							<td class="whiteboxwk">
								<s:text name='workprogressabstract.fund'/> :
							</td>
							<td class="whitebox2wk">
								<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="fund" id="fund" cssClass="selectwk" list="dropdownData.fundList" listKey="id" listValue="name" value="%{fund.id}" onchange="setupSchemes(this);"/>
            									<egov:ajaxdropdown id="schemeDropdown" fields="['Text','Value']" dropdownId='scheme' url='estimate/ajaxChangeFinancialDetails!loadSchemes.action' selectedValue="%{fund.id}"/>
           								</td>
							<td class="whiteboxwk">
								<s:text name='workprogressabstract.function'/> :
							</td>
							<td width="53%" class="whitebox2wk">
								<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="function" id="function" cssClass="selectwk" list="dropdownData.functionList" listKey="id" listValue="name" value="%{function.id}" onChange="setupBudgetGroups(this);"/>
            								<egov:ajaxdropdown id="budgetGroupDropdown" fields="['Text','Value']" dropdownId='budgetGroup' url='ajaxFinancialDetail!loadBudgetGroups.action' selectedValue="%{scheme.id}"/></td>
						</tr>
						<tr>
						<s:if test="%{dwSelected!=true}">
							<td class="whiteboxwk">
								<s:text name='estimate.financial.budgethead'/> :				
							</td>
							<td class="whitebox2wk" colspan="3">
								<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="budgetGroup" id="budgetGroup" cssClass="selectwk" list="dropdownData.budgetGroupList" listKey="id" listValue="name" value="%{budgetGroup.id}" /></td>
						</s:if>
						<s:if test="%{dwSelected==true}">
							<td class="whiteboxwk">
								<s:text name='change.fd.depositCOA'/>				
							</td>
							<td class="whitebox2wk" width="20%">
   								<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" 
								name="depositCOA" id="depositCOA" cssClass="selectwk" list="dropdownData.allDepositCodeList"
								listKey="id" listValue='glcode  + " : " + name' value="%{depositCOA}"/>						
   													
   							</td>
			                <td class="whiteboxwk">
			                	<s:text name="change.fd.depositCode" />:
			                </td>
			                <td class="whitebox2wk">
				                <div class="yui-skin-sam">
					                <div id="depositCodeSearch_autocomplete">
						                <div><s:textfield id="depositCodeSearch" type="text" name="code" onkeypress="return validateFundSelection()" onblur="clearHiddenIdIfEmpty(this)" value="%{depositCode.code}" class="selectwk"  /><s:hidden id="depositCode" name="depositCode" value="%{depositCode.id}"/></div>
						                <span id="depositCodeSearchResults"></span>
					                </div>
				                </div>
				                <egov:autocomplete name="depositCodeSearch" width="20" field="depositCodeSearch" url="depositCodeSearch!searchDepositCodeAjax.action?" queryQuestionMark="false" results="depositCodeSearchResults" paramsFunction="depositCodeSearchParameters" handler="depositCodeSearchSelectionHandler" forceSelectionHandler="depositCodeSelectionEnforceHandler"/>
			                </td> 
						</s:if>	
						</tr>
						<s:if test="%{schemeSelected==true}">
							<tr>
								<td width="15%" class="whiteboxwk">
									<s:text name='estimate.financial.scheme'/> : 
								</td>
			
								<td class="whitebox2wk" width="20%">
	   								<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="scheme" id="scheme" cssClass="selectwk" list="dropdownData.schemeList" listKey="id" listValue="name" value="%{scheme.id}" onChange="setupSubSchemes(this);"/>
									<egov:ajaxdropdown id="subSchemeDropdown" fields="['Text','Value']" dropdownId='subScheme' url='estimate/ajaxChangeFinancialDetails!loadSubSchemes.action' selectedValue="%{scheme.id}"/>							
	   							</td>
	   							<td width="15%" class="whiteboxwk">
									Sub <s:text name='estimate.financial.scheme'/> : 
								</td>
								<td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="subScheme" id="subScheme" cssClass="selectwk" 
									list="dropdownData.subSchemeList" listKey="id" listValue="name" value="%{subScheme.id}" />
								</td>
							</tr>
						</s:if>	
					</table>	
				</table>
				<div id="manual_workflow">
         			<c:set var="approverHeadCSS" value="headingwk" scope="request" />
         			<c:set var="approverCSS" value="bluebox" scope="request" />
         			<s:hidden name="departmentName" id="departmentName" value="%{departmentName}"/>
					<%@ include file="/commons/commonWorkflow.jsp"%>
  				</div>
  				
				<div class="buttonholderwk">
					<br />
					<s:iterator value="%{getValidActions()}" var="p">
						 <s:submit type="submit" cssClass="buttonfinal" value="%{p}" id="%{p}" name="%{p}" method="create" onclick="document.changeFinancialDetailsForm.actionName.value='%{p}';return validateForWF('%{p}');"/>
					</s:iterator>
					<input type="button" class="buttonfinal" value="CLOSE"
						id="wfcloseButton" 
						onclick="window.close();" />
					<br />
				</div>
				<br />
			</s:if>
			<br />
		</div>
	</div>
	</div>
	</div>	
	</s:push>								
</s:form>
</body>
</html>