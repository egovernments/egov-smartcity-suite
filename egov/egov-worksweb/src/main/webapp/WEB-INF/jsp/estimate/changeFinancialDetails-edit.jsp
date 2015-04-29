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
	function noBack() {
		window.history.forward(); 
	}
	function setupSchemes(elem){
		clearMessage('changeFD_error');
	    var fundElem = document.getElementById('fund');
		var fundId = fundElem.options[elem.selectedIndex].value;
		var id=elem.options[elem.selectedIndex].value;
	    populatescheme({fundId:id});
	    populatesubScheme({schemeId:id});
	}
	function validateCancel() {
		var msg='<s:text name="changeFDHeader.cancel.confirm"/>';
		var changeFD=''; 
		if(!confirmCancel(msg,changeFD)) {
			return false;
		}
		else {
			return true;
		}
	}
	
	function validateForWF(text){
		if(!validate()){
			  return false;
		}
		if(document.getElementById("actionName").value=='Cancel'){
			if(!validateCancel()){
				return false;
			}
		}
		if(text!='Approve' && text!='Reject' && text!='Save'){
			if(!validateWorkFlowApprover(text))
				return false;
		}
		<s:if test="%{sourcepage == 'inbox' && model.id!=null && model.egwStatus.code=='REJECTED'}">
			var flag = checkForSubSchemeResetting();
			if(!flag)
				return false;	
		</s:if>
		enableFields();
		return true;  
	}

	function checkForSubSchemeResetting()
	{
		var length=eval('<s:property value="estimateSearchResults.size()" />')+1;
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
	
	function viewEstimate(estId) {
		window.open("${pageContext.request.contextPath}/estimate/abstractEstimate!edit.action?id="+estId+"&sourcepage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}
	
	function enableSelectEstimates() {
		var length=eval('<s:property value="estimateSearchResults.size()" />')+1;
		var i;
		for(i=1;i<length;i++) {
			var id='isEstimateSelected'+i.toString();
			dom.get(id).disabled=false;
		}
	}
	
	function disableSelectEstimates() {
		var length=eval('<s:property value="estimateSearchResults.size()" />')+1;
		var i;
		for(i=1;i<length;i++) {
			var id='isEstimateSelected'+i.toString();
			dom.get(id).disabled=true;
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
	
		<s:if test="%{isDescChange}">
			if(desc==""){
				document.getElementById("changeFD_error").innerHTML='<s:text name="changeFDHeader.null.work.description" />'; 
		        document.getElementById("changeFD_error").style.display='';
		        window.scroll(0,0);
				return false;
			}
			else{
				document.getElementById("changeFD_error").innerHTML='';
				document.getElementById("changeFD_error").style.display="none";
				setHiddenValues();
				return true;
			}
		</s:if>
		<s:else>
			<s:if test="%{isSchemeWorks}">
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
			</s:if>
			<s:if test="%{isDepWorks}">
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
				setHiddenValues();
				return true;
			</s:if>
			<s:if test="%{isBdgtHeadChange}">
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
			</s:if>
		</s:else>
		document.getElementById("changeFD_error").innerHTML='';
		document.getElementById("changeFD_error").style.display="none";
		setHiddenValues();
		return true;	
	}

	function setHiddenValues()
	{
		var descObj = dom.get("workDesc");
		var fundObj = dom.get("fund");
		var funcObj = dom.get("function");
		var bgtGrpObj  = dom.get("budgetGroup");
		var depositCOAObj = dom.get("depositCOA");
		var depositCodeObj = dom.get("depositCode");
		var schemeObj = dom.get("scheme");
		var subSchemeObj = dom.get("subScheme");

		if( descObj!=null && descObj.value=='')
			dom.get("isWorkDescNull").value=true;
		if( fundObj!=null && fundObj.value==-1)
			dom.get("isFundNull").value=true;
		if( funcObj!=null && funcObj.value==-1)
			dom.get("isFunctionNull").value=true;
		if( bgtGrpObj!=null && bgtGrpObj .value==-1)
			dom.get("isBudgetGroupNull").value=true;
		if( depositCOAObj!=null && depositCOAObj.value==-1)
			dom.get("isDepositCOANull").value=true;
		if( depositCodeObj!=null && depositCodeObj.value=='')
			dom.get("isDepositCodeNull").value=true;
		if( schemeObj!=null && schemeObj.value==-1)
			dom.get("isSchemeNull").value=true;
		if( subSchemeObj!=null && subSchemeObj.value==-1)
			dom.get("isSubSchemeNull").value=true;

	}

	function validateSubmit(){
		clearMessage('changeFD_error');
		document.getElementById("changeFD_error").style.display='none';
		var manyEstimatesSelected=0;
		var length=eval('<s:property value="estimateSearchResults.size()" />')+1;
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
		if(manyEstimatesSelected>1)
		{
			<s:if test="%{isDepWorks==true || isSchemeWorks==true || isDescChange==true}">
				document.getElementById("changeFD_error").style.display='';
				document.getElementById("changeFD_error").innerHTML='<s:text name="changeFDHeader.select.one.estimate" />';
				window.scroll(0,0);
				return false;
			</s:if>
		}	
		return true;
	}

	function disableFD(){
		var checkAllForDesc = document.forms[0].checkedAll.checked;

		if(document.forms[0].isDescChange.checked){
			document.getElementById('newDescField').style.display='block';
			document.getElementById('FinDetailsTable').style.visibility='hidden';
		}
		else{
			document.getElementById('newDescField').style.display='none';
			document.getElementById('FinDetailsTable').style.visibility='visible';

		}
	}
	function selectAllEstimates(obj) {
		var length=eval('<s:property value="estimateSearchResults.size()" />')+1;
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
	function setupBudgetGroups(elem){
		var id=elem.options[elem.selectedIndex].value;
	       	makeJSONCall(["Text","Value"],'${pageContext.request.contextPath}/estimate/ajaxFinancialDetail!loadBudgetGroups.action',
	    	{functionId:id},budgetGroupDropdownSuccessHandler,budgetLoadFailureHandler) ;
	}
</script>
		
	<body onload="defaultApproverDept();disableFD();noBack();" onpageshow="if(event.persisted) noBack();" onunload="" >
	<c:catch>
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
	<s:hidden name="actionName" id="actionName"/>
	<s:hidden name="sourcepage" id="sourcepage"/>
	<s:hidden id="additionalRuleValue" name="additionalRuleValue" value="%{additionalRuleValue}" />
	<s:hidden id="loggedInUserDept" name="loggedInUserDept" value="%{loggedInUserDept}" />
	<s:hidden id="newWorkDesc" name="newWorkDesc" value="%{newWorkDesc}" />
	<s:hidden id="isWorkDescNull" name="isWorkDescNull" />
	<s:hidden id="isFundNull" name="isFundNull"  />
	<s:hidden id="isFunctionNull" name="isFunctionNull" />
	<s:hidden id="isBudgetGroupNull" name="isBudgetGroupNull" />
	<s:hidden id="isDepositCOANull" name="isDepositCOANull" />
	<s:hidden id="isDepositCodeNull" name="isDepositCodeNull" />
	<s:hidden id="isSchemeNull" name="isSchemeNull" />
	<s:hidden id="isSubSchemeNull" name="isSubSchemeNull"  />
	
	
		<div class="formmainbox">
		<div class="insidecontent">
		<div id="printContent" class="rbroundbox2">
		<div class="rbtop2"><div></div></div>
		<div class="rbcontent2">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td></td>
				</tr>
				<s:if test="%{estimateSearchResults.size() >= 1}">
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
							<display:table name="estimateSearchResults" uid="currentRow"
								cellpadding="0" cellspacing="0" requestURI=""
								style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
								
								<display:column headerClass="pagetableth"
														class="pagetabletd" 
														title='<input type="checkbox" id="checkedAll" name="checkedAll" onclick="selectAllEstimates(this)" />'
														style="width:3%;text-align:center">
											
													<input type='hidden'
															name='changeFinDetDetail[<s:property value='#attr.currentRow_rowNum' />].abstractEstimate.id'
															value='<s:property value="#attr.currentRow[0].id" />' />
															
														<s:if test="%{model!=null && model.id!=null && model.status.code!='REJECTED'}" >
															<input type='checkbox' id='isEstimateSelected<s:property value='#attr.currentRow_rowNum' />'  
																name='changeFinDetDetail[<s:property value='#attr.currentRow_rowNum' />].isChecked' 
																onclick='populateIsChecked(this);' value='true' checked='true' readonly='true' disabled='true'/>
														</s:if>
														<s:elseif test="%{model!=null && model.id!=null && (model.status.code=='NEW' || model.status.code=='REJECTED')}" >
															<input type='checkbox' id='isEstimateSelected<s:property value='#attr.currentRow_rowNum' />'    
																name='changeFinDetDetail[<s:property value='#attr.currentRow_rowNum' />].isChecked' 
																onclick='populateIsChecked(this);' value='true' checked='true'/>
														</s:elseif>
														
									 					<s:else>	
														<input type='checkbox' id='isEstimateSelected<s:property value='#attr.currentRow_rowNum' />'   
																name='changeFinDetDetail[<s:property value='#attr.currentRow_rowNum' />].isChecked' 
																onclick='populateIsChecked(this);' value='false' />
														</s:else>
															
													</display:column>
													
													<display:column title="Sl.No"
														titleKey='estimate.search.slno' headerClass="pagetableth"
														class="pagetabletd" style="width:2%;text-align:right">
														<s:property
															value="#attr.currentRow_rowNum" />
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
														style="width:10%;text-align:left" >
														<s:property value="#attr.currentRow[1].function.name" />
													</display:column>
													
													<s:if test="%{isBdgtHeadChange}">
													<display:column headerClass="pagetableth"
														class="pagetabletd" title="Budget Head"
														titleKey='estimate.search.name'
														style="width:26%;text-align:left" >
														<s:property value="#attr.currentRow[1].budgetGroup.name" />	
													</display:column>
													</s:if>
													
													<s:if test="%{isDepWorks}">
													<display:column headerClass="pagetableth"
														class="pagetabletd" title="Deposit COA,<br> Deposit Code"
														titleKey='estimate.search.name'
														style="width:37%;text-align:left" >
														<s:property value="#attr.currentRow[1].coa.glcode" /> , <s:property value="#attr.currentRow[0].depositCode.code" /> 	
													</display:column>
													</s:if>
													<s:if test="%{isSchemeWorks}">
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
				
			</table>
			<br />
				<table>
					<tr>
						<td class="whiteboxwk">
		   					<s:checkbox name="isDescChange" id="isDescChange" onClick="disableFD();" />
							<s:hidden id="isChecked" name="isChecked" value="%{isDescChange}"/>
			   			</td>
						<td class="whitebox2wk" colspan="2" >
							<s:text name='change.fd.desc.only'/>
						</td>
						<td class="whitebox2wk" id="newDescField" colspan="2" style="display: none;">
							<s:textarea name="newWorkDesc" cols="50" rows="3" cssClass="selectwk" id="workDesc" value="%{model.changeFinDetails[0].workDesc}"/>
																	 
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
						<s:if test="%{isBdgtHeadChange}">
							<td class="whiteboxwk">
								<s:text name='estimate.financial.budgethead'/> :				
							</td>
							<td class="whitebox2wk" colspan="3">
								<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="budgetGroup" id="budgetGroup" cssClass="selectwk" list="dropdownData.budgetGroupList" listKey="id" listValue="name" value="%{budgetGroup.id}" /></td>
						</s:if>
						<s:if test="%{isDepWorks}">
							<td class="whiteboxwk">
								<s:text name='change.fd.depositCOA'/>				
							</td>
							<td class="whitebox2wk" width="20%">
   								<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" 
								name="depositCOA" id="depositCOA" cssClass="selectwk" list="dropdownData.allDepositCodeList"
								listKey="id" listValue='glcode  + " : " + name' value="%{depositCOA.id}"/>						
   													
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
						<s:if test="%{isSchemeWorks}">
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
			<br />
			<div id="manual_workflow">
					         			<c:set var="approverHeadCSS" value="headingwk" scope="request" />
					         			<c:set var="approverCSS" value="bluebox" scope="request" />
					         			<s:hidden name="departmentName" id="departmentName" value="%{departmentName}"/>
										<%@ include file="/commons/commonWorkflow.jsp"%>
			</div>
									
									<div class="buttonholderwk" id="buttons">
										<br />
										<s:if	test="%{(sourcepage=='inbox' || hasErrors())}">
												<s:iterator value="%{getValidActions()}" var="p">
													 <s:submit type="submit" cssClass="buttonfinal" value="%{p}" id="%{p}" name="%{p}" method="save" onclick="document.changeFinancialDetailsForm.actionName.value='%{p}';return validateForWF('%{p}');"/>
												</s:iterator>
										</s:if>	
											<input type="button" class="buttonfinal" value="CLOSE"
												id="wfcloseButton" 
												onclick="window.close();" />
										<br />
									</div>
									<br />
		</div>
	</div>
	</div>
	</div>	
	</s:push>								
</s:form>
<script type="text/javascript">
<s:if test="%{sourcepage == 'inbox' && model.id!=null && model.egwStatus.code!='REJECTED'}">
	toggleFields(true,['approverDepartment','approverDesignation','approverPositionId','approverComments']);
	enabledivChilderns("buttons");
	disableSelectEstimates();
</s:if>
<s:if test="%{estimateSearchResults.size() != 0}">
	dom.get("checkedAll").checked=true;
	selectAllEstimates(dom.get("checkedAll"));	
</s:if>
<s:if test="%{sourcepage == 'inbox' && estimateSearchResults.size() != 0 && model.id!=null && model.status.code=='REJECTED'}">
	toggleFields(false,[]);
	enabledivChilderns("buttons");
	enableSelectEstimates();
</s:if>
var descObj= document.getElementById("isDescChange");
if(descObj)
	descObj.disabled=true;
</script>
</c:catch>
</body>

</html>