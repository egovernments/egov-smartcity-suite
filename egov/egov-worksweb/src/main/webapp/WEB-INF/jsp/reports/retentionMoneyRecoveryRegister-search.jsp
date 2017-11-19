<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  ~
  --%>

<%@ include file="/includes/taglibs.jsp" %>
<html>
	<head>	
		<title><s:text name='retentionMoneyRecoveryRegister.page.title' />
		</title>
	</head>
<script type="text/javascript">

var estimateNumberSearchSelectionHandler = function(sType, arguments){ 
	var oData = arguments[2];
};

var projectCodeSearchSelectionHandler = function(sType, arguments){ 
	var oData = arguments[2];
};

var contractorCodeNameSearchSelectionHandler = function(sType, arguments){ 
	var oData = arguments[2];
};
 
function validateInput(){	
	if( dom.get('estimateNumberSearch').value=="" && dom.get('projectCodeSearch').value=="" && dom.get('billDateFrom').value=="" 
		&& dom.get('billDateTo').value==""  && dom.get('contractorCodeNameSearch').value=="" 
		&& (dom.get('budgetHeads').value=="" || dom.get('budgetHeads').value==-1) 
		&& (dom.get('depositCOA').value=="" || dom.get('depositCOA').value==-1) && dom.get('billDepartment').value==-1 
		&& dom.get('retentionMoneyAmountFrom').value==""&& dom.get('retentionMoneyAmountTo').value=="" && dom.get('billType').value=="" && dom.get("retentionMoneyRefPeriod").value==-1
				) {
				dom.get("retentionMoneyRecoveryRegister_error").innerHTML='<s:text name="retentionMoneyRecoveryRegister.validation.select.one.search.creteria"/>'
				dom.get("retentionMoneyRecoveryRegister_error").style.display='';
	        	return false;
	}
	else {
		dom.get("retentionMoneyRecoveryRegister_error").innerHTML='';
		dom.get("retentionMoneyRecoveryRegister_error").style.display="none";
	}
	
	retentionMoneyAmountFrom =  dom.get("retentionMoneyAmountFrom").value;
	retentionMoneyAmountTo =  dom.get("retentionMoneyAmountTo").value;
	billDateFrom = dom.get("billDateFrom").value;
	billDateTo = dom.get("billDateTo").value;
	
	if((retentionMoneyAmountFrom!='' && retentionMoneyAmountTo=='') || (retentionMoneyAmountFrom=='' && retentionMoneyAmountTo!='')) {
		dom.get("retentionMoneyRecoveryRegister_error").innerHTML='<s:text name="retentionMoneyRecoveryRegister.enter.retentionmoneyamount.amount"/>'; 
        dom.get("retentionMoneyRecoveryRegister_error").style.display='';
		return false;
	}
	if((retentionMoneyAmountFrom!='' && retentionMoneyAmountTo!='') && (parseFloat(retentionMoneyAmountFrom)>parseFloat(retentionMoneyAmountTo))) {
		dom.get("retentionMoneyRecoveryRegister_error").innerHTML='<s:text name="retentionMoneyRecoveryRegister.retentionmoneyamountfrom.greater.retentionmoneyamountto"/>'; 
        dom.get("retentionMoneyRecoveryRegister_error").style.display='';
		return false;
	}
	if((billDateFrom!='' && billDateTo=='') || (billDateFrom=='' && billDateTo!='')) {
		dom.get("retentionMoneyRecoveryRegister_error").innerHTML='<s:text name="retentionMoneyRecoveryRegister.enter.billdatefrom.billdateto"/>'; 
        dom.get("retentionMoneyRecoveryRegister_error").style.display='';
		return false;
	}	 
	dom.get("retentionMoneyRecoveryRegister_error").style.display="none";
	hideResult();
	formHeaderForListBox(); 
	return true;
}

function formHeaderForListBox() {
	var  allbudgetHeadVal=dom.get("budgetHeads");
	if(allbudgetHeadVal)
		allbudgetHeadVal=allbudgetHeadVal.value;
	else
		allbudgetHeadVal='';
	var  alldepositCodesVal=dom.get("depositCOA");
	if(alldepositCodesVal)
		alldepositCodesVal=alldepositCodesVal.value;
	else
		alldepositCodesVal='';
	
	if(allbudgetHeadVal!='' && allbudgetHeadVal!=-1) {
		var budgetHeadText="";
		var x=document.getElementById("budgetHeads");
		  for (var i = 0; i < x.options.length; i++) {
			 if(x.options[i].selected ==true){
			     if(budgetHeadText=='')
			    	 budgetHeadText=x.options[i].text;
			     else
			    	 budgetHeadText=budgetHeadText+", "+x.options[i].text; 
		     }
		}
	}
	if(alldepositCodesVal!='' && alldepositCodesVal!=-1 ) {
		var depositCOAsText="";
		var x=document.getElementById("depositCOA");
		  for (var i = 0; i < x.options.length; i++) {
			  if(x.options[i].selected ==true){
				     if(depositCOAsText=='')
				    	 depositCOAsText=x.options[i].text;
				     else
				    	 depositCOAsText=depositCOAsText+", "+x.options[i].text; 
			     }
		}
	}
	dom.get("subHeaderBudgetHeads").value = budgetHeadText;
	dom.get("subHeaderDepositCOA").value = depositCOAsText;
}

function hideResult() {
	dom.get("resultRow").style.display='none';
}

function disableMasking(){
	document.getElementById('loadingMask').remove();
}

function validateAmts(obj) {
	var text = obj.value;
	if(text=='')
		return;
	var msg = '<s:text name="retentionMoneyRecoveryRegister.report.enter.valid.retentionmoney.amount" />';
	if(isNaN(text)) {
		alert(msg);
		obj.value="";
		return;
	}
	if(text<=0) {
		alert(msg);
		obj.value='';
		return;
	}
}
</script>
		
<body>
	<div class="errorstyle" id="retentionMoneyRecoveryRegister_error" style="display: none;"></div>
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
	<s:form name="retentionMoneyRecoveryRegisterForm" action="retentionMoneyRecoveryRegister"	theme="simple">			
	<s:hidden name="subHeaderBudgetHeads" id="subHeaderBudgetHeads" />
	<s:hidden name="subHeaderDepositCOA" id="subHeaderDepositCOA" />
	 
	
	<div class="formmainbox">
		<div class="insidecontent">
			<div id="printContent" class="rbroundbox2">
			<div class="rbtop2"><div></div></div>
			<div class="rbcontent2">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td>
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td colspan="4" class="headingwk">
										<div class="arrowiconwk">
											<img
												src="/egworks/resources/erp2/images/arrow.gif" />
										</div>
										<div class="headplacer">
											<s:text name="label.search" />
										</div>
									</td>
								</tr>
								<tr>												
									<td class="greyboxwk">
										<s:text name="retentionMoneyRecoveryRegister.label.estimatenumber" /> :
									</td>
									<td width="53%" class="greybox2wk">
										<div class="yui-skin-sam">
	        							<div id="estimateNumberSearch_autocomplete">
                							<div>
	        									<s:textfield id="estimateNumberSearch" name="estimateNumber" value="%{estimateNumber}" cssClass="selectwk" />
	        								</div>
	        								<span id="estimateNumberSearchResults"></span>
	        							</div>	
	        							</div>
	        							<egov:autocomplete name="estimateNumberSearch" width="20" 
	        								field="estimateNumberSearch" url="../reports/ajaxRetentionMoneyRecoveryRegister!searchEstimateNumber.action?" 
	        								queryQuestionMark="false" results="estimateNumberSearchResults" 
	        								handler="estimateNumberSearchSelectionHandler" queryLength="3"/>
									</td>
									<td class="greyboxwk">
										<s:text name="retentionMoneyRecoveryRegister.label.projectcode" /> :
									</td>
									<td width="53%" class="greybox2wk">
										<div class="yui-skin-sam">
	        							<div id="projectCodeSearch_autocomplete">
                							<div>
	        									<s:textfield id="projectCodeSearch" name="projectCode" value="%{projectCode}" cssClass="selectwk" />
	        								</div>
	        								<span id="projectCodeSearchResults"></span>
	        							</div>	
	        							</div>
	        							<egov:autocomplete name="projectCodeSearch" width="20" 
	        								field="projectCodeSearch" url="../reports/ajaxRetentionMoneyRecoveryRegister!searchProjectCode.action?" 
	        								queryQuestionMark="false" results="projectCodeSearchResults" 
	        								handler="projectCodeSearchSelectionHandler" queryLength="3"/>
									</td>
								</tr>
								
								<tr>
									<td class="whiteboxwk">
										<s:text name="retentionMoneyRecoveryRegister.label.billdatefrom" /> :
									</td>
									<td class="whitebox2wk">
										<s:date name="fromDate" var="fromDateFormat" format="dd/MM/yyyy" />
										<s:textfield name="billDateFrom" id="billDateFrom" cssClass="selectwk" value="%{fromDateFormat}"
											onfocus="javascript:vDateType='3';"	onkeyup="DateFormat(this,this.value,event,false,'3')" />
										<a href="javascript:show_calendar('forms[0].billDateFrom',null,null,'DD/MM/YYYY');"
											onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"> 
											<img src="/egworks/resources/erp2/images/calendar.png" alt="Calendar" width="16" height="16" border="0" align="absmiddle" />
										</a>
									</td>
									<td class="whiteboxwk">
										<s:text name="retentionMoneyRecoveryRegister.label.billdateto" /> :
									</td>
									<td class="whitebox2wk">
										<s:date name="toDate" var="toDateFormat" format="dd/MM/yyyy" />
										<s:textfield name="billDateTo" id="billDateTo" value="%{toDateFormat}" cssClass="selectwk" 
											onfocus="javascript:vDateType='3';"	onkeyup="DateFormat(this,this.value,event,false,'3')" />
										<a href="javascript:show_calendar('forms[0].billDateTo',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;"
											onmouseout="window.status='';return true;"> 
											<img src="/egworks/resources/erp2/images/calendar.png" alt="Calendar" width="16" height="16" border="0" align="absmiddle" />
										</a>
									</td>
								</tr>
								
								<tr>												
									<td class="greyboxwk">
										<s:text name="retentionMoneyRecoveryRegister.label.contractorcodeorname" /> :
									</td>
									<td width="53%" class="greybox2wk">
										<div class="yui-skin-sam">
	        							<div id="contractorCodeNameSearch_autocomplete">
                							<div>
	        									<s:textfield id="contractorCodeNameSearch" name="contractorCodeName" value="%{contractorCodeName}" cssClass="selectwk" />
	        								</div>
	        								<span id="contractorCodeNameSearchResults"></span>
	        							</div>	
	        							</div>
	        							<egov:autocomplete name="contractorCodeNameSearch" width="20" 
	        								field="contractorCodeNameSearch" url="../reports/ajaxRetentionMoneyRecoveryRegister!searchContractors.action?" 
	        								queryQuestionMark="false" results="contractorCodeNameSearchResults"  
	        								handler="contractorCodeNameSearchSelectionHandler" queryLength="3"/>
									</td>
									<td class="greyboxwk">
										<s:text name="retentionMoneyRecoveryRegister.label.billdepartment" /> :
									</td>
									<td width="53%" class="greybox2wk">
										<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}"
																name="billDepartment" id="billDepartment" cssClass="selectwk"
																list="dropdownData.billDepartmentList" listKey="id"	listValue="deptName" value="%{billDepartment}" />
									</td>
								</tr>
								
								<tr>
									<td class="whiteboxwk"><s:text name='retentionMoneyRecoveryRegister.label.depositcoa'/> : </td>
					                <td class="whitebox2wk" colspan="3">
										<s:select multiple="true" size="4"  name="depositCOA" id="depositCOA" cssClass="selectwk" list="dropdownData.depositCOAList"
											listKey="id" listValue='glcode  + " : " + name' value="%{depositCOA}" headerKey="-1" headerValue="%{getText('estimate.default.select')}" />
									</td>
								</tr>
								<tr>
									<td width="11%" class="greyboxwk">
										<s:text name='retentionMoneyRecoveryRegister.label.budgethead'/> :
									</td>
									<td class="greybox2wk" colspan="3">
										<s:select multiple="true" size="4" cssClass="selectwk" list="dropdownData.budgetHeadList" 
										listKey="id" listValue='name' 
										name="budgetHeads" id="budgetHeads" value="%{budgetHeads}" headerKey="-1" headerValue="%{getText('estimate.default.select')}" /> 
									</td>
								</tr>
								<tr>
									<td class="whiteboxwk">
										<s:text name="retentionMoneyRecoveryRegister.report.retentionmoney.amount.between" /> :
									</td>
									<td class="whitebox2wk" > 
										<s:textfield name="retentionMoneyAmountFrom" id="retentionMoneyAmountFrom" onblur="validateAmts(this)"  style="width:65px;text-align:right"/>&nbsp;--&nbsp;
										<s:textfield name="retentionMoneyAmountTo" id="retentionMoneyAmountTo" onblur="validateAmts(this)" style="width:65px;text-align:right" />
									</td>
									<td class="whiteboxwk">
										<s:text name="contractorBill.billType" />:
									</td>
									<td class="whitebox2wk">
										<s:select name="billType" id="billType" cssClass="selectwk" headerKey="" headerValue="%{getText('list.default.select')}"
											list="dropdownData.billTypeList" value="%{billType}"  />
									</td>
								</tr>
								<tr>
									<td width="11%" class="greyboxwk"><s:text name="retentionMoneyRecoveryRegister.label.DLP.period" />:</td>
									<td class="greybox2wk" colspan="3">
								          <s:select id="retentionMoneyRefPeriod" name="retentionMoneyRefPeriod" cssClass="selectwk" 
													list="#{'30':'One month', '60':'Two months', '90':'Three months', '180':'Six months', '365':'One Year'}" headerKey="-1" headerValue="--- Select ---"
													value="%{retentionMoneyRefPeriod}"/>
											 		
									</td>
									
								</tr>										
								<tr>
									<td colspan="4" class="shadowwk"></td>
								</tr>
								<tr>
									<td colspan="4">
										<div  class="buttonholderwk" align="center">
											<s:submit cssClass="buttonadd" value="SEARCH" id="saveButton" name="button" 
												onClick="return validateInput();" method="searchList"/>
											<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="button" onclick="window.close();"/>	 
										</div>
									</td>
								</tr>
															
								<table width="100%" border="0" cellspacing="0" cellpadding="0">									
									<tr><td colspan="4">&nbsp;</td></tr>
									<tr id="resultRow" >
										<td colspan="4">												
											<%@ include file='retentionMoneyRecoveryRegister-searchResults.jsp'%>
										</td>
									</tr>																							
								</table>								
							</table>
						</td>
					</tr>
				</table>
			</div>
			<div class="rbbot2"><div></div></div>
		</div>
	</div>
	</s:form>
</body>
</html>
