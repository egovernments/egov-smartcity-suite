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
<title><s:text name="estimate.budgetfolio.header" /></title>
<script src="<egov:url path='resources/js/works.js?${app_release_no}'/>"></script>

<script type="text/javascript">
	function checkDate(obj){
		if(!validateDateFormat(obj)) {
	    	dom.get('errorreportDate').style.display='none';
			dom.get("budgetFolio_error").style.display='block';
			document.getElementById("budgetFolio_error").innerHTML='Please enter valid Date';
			document.budgetFolioForm.reportDate.focus();
	    	return;
		}
		else {
			dom.get("budgetFolio_error").style.display='none';
			dom.get("budgetFolio_error").style.display='none';
		}
		return true;
	}
	function getReportDate() {
		var reportDate=document.budgetFolioForm.reportDate;	
		if(reportDate.value=='') {
			reportDate.value=getCurrentDate();
		}
	}	
	
	budgetLoadFailureHandler=function(){
	   showMessage('budgetFolio_error','Unable to load budget head information');
	}
	
	
	function setupBudgetGroups(elem){
		var id=elem.options[elem.selectedIndex].value;
	    populatebudgetGroup({functionId:id});
	}
	
	function setUp(element){
	enableFields();
	document.getElementById('loadingMask').remove();
	var obj=document.getElementById("userDepartment");
	document.getElementById("deptName").value=obj.options[obj.selectedIndex].text;
	 document.getElementById("option").value="searchPdf";
	 return true;
	}
	
	function enableFields(){
		for(i=0;i<document.budgetFolioForm.elements.length;i++){
	        document.budgetFolioForm.elements[i].disabled=false;
		}
	} 	

	function validate(){
	 enableFields();
	 document.getElementById("isEnableSelect").value='<s:property value="%{isEnableSelect}"/>';
	 document.getElementById("option").value="searchdetails";
	 if(document.getElementById("reportDate").value==""){
	 	 dom.get("budgetFolio_error").style.display='';
		document.getElementById("budgetFolio_error").innerHTML="Please enter the Date";
	 	return false;
	 }
	 if(!checkDate(document.getElementById("reportDate"))){	
	  return false;
	 }
	 else
	 {
	  return true;
	 }
	  return true;
	}
	
	function setMandatoryFields(){
		 var dept='<s:property value="%{mandatoryFields['department']}"/>';
		 var fund='<s:property value="%{mandatoryFields['fund']}"/>';
		 if(dept=='M'){
		 	dom.get("deptDisp").style.display='';
		 }
		 if(fund=='M'){
		 	dom.get("fundDisp").style.display='';
		 }
	}
	function setAsOnDate(elem){
		dom.get("reportDate").value = "";
		var dropdownId = elem.value;
		var currFinYear = dom.get("currentFinancialYearId").value;

		var repDate = document.getElementById("reportDate");

		if(dropdownId==currFinYear){
			getReportDate();
		}

		var leng = dropdownId.length;
		var finYRStr = dom.get("finYearRangeStr").value;
		var index=-1;

		if(dropdownId!=-1)
		{
			index = finYRStr.search("id:"+dropdownId+"--");
			if(index!=-1)
			{
				dom.get("reportDate").value=finYRStr.substr(index+leng+17,10);
			}	
		}	
	}	

	
</script>

<body onload="setMandatoryFields();">
		<s:if test="%{hasErrors()}">
       		 <div class="errorstyle">
          		<s:actionerror/>
          		<s:fielderror/>
        	</div>
    	</s:if>
   		<s:if test="%{hasActionMessages()}">
       		<div id="msgsDiv" class="messagestyle">
        		<s:actionmessage theme="simple"/>
        	</div>
    	</s:if>	
		<s:form  theme="simple" name="budgetFolioForm"  action="financialDetail">   
		<s:hidden name="finYearRangeStr" id="finYearRangeStr" />
		<s:hidden name="currentFinancialYearId" id="currentFinancialYearId" />
		<div class="errorstyle" id="budgetFolio_error" style="display: none;"></div>
			<div class="navibarshadowwk">
			</div>
			<div class="formmainbox">
			<div class="insidecontent">
			<div class="rbroundbox2">
			<div class="rbtop2"><div></div>
			</div>
			<div class="rbcontent2">
			  
			<table id="formTable" width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td> 
						<table id="budgetFolioHeader" width="100%" border="0"
							cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="4">&nbsp;<s:hidden name="financialYearStartDate" /></td>
								
							</tr>
							<tr>
								<td width="11%" class="whiteboxwk">
								<span class="mandatory" id="deptDisp" style="display:none;">*</span><s:text name='estimate.user.department'/> :
								</td>
								<td width="21%" class="whitebox2wk">
										<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="userDepartment" 
										id="userDepartment" cssClass="selectwk" list="dropdownData.userDepartmentList" listKey="id" 
										listValue="deptName" value="%{userDepartment}"/>
								</td>
								<td width="15%" class="whiteboxwk">
								<span class="mandatory">*</span><s:text name='budgetfolio.asondate'/> :	
								</td>
						 		<td width="53%" class="whitebox2wk">
										<s:date name="reportDate" var="estDateFormat" format="dd/MM/yyyy"/>
										<s:textfield name="reportDate" id="reportDate"	cssClass="selectwk" value="%{estDateFormat}" 
											onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')"/> 
										<a href="javascript:show_calendar('forms[0].reportDate',null,null,'DD/MM/YYYY');"
											onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;">
											<img src="/egworks/resources/erp2/images/calendar.png"
												alt="Calendar" width="16" height="16" border="0"
												align="absmiddle" />
										</a>
										<span id='errorreportDate' style="display:none;color:red;font-weight:bold">&nbsp;x</span>	
							</td>
							</tr>
							<tr>
								<td width="11%" class="greyboxwk">
									<span class="mandatory">*</span><s:text name='estimate.financial.function'/> :
								</td>
								<td width="21%" class="greybox2wk">
									<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}"
									 name="function" id="function" cssClass="selectwk" list="dropdownData.functionList" 
									 listKey="id" listValue="name" value="%{function.id}" disabled="%{isEnableSelect}"  onChange="setupBudgetGroups(this);"/>
									  <egov:ajaxdropdown id="budgetGroupDropdown" fields="['Text','Value']" dropdownId='budgetGroup' 
									  url='estimate/ajaxFinancialDetail!loadBudgetGroups.action' selectedValue="%{budgetGroup.id}"/>
									</td>
								<td width="15%" class="greyboxwk">
								<span class="mandatory" id="fundDisp" style="display:none;">*</span><s:text name='estimate.financial.fund'/> :				
								</td>
								<td width="53%" class="greybox2wk">
									<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" 
										name="fund" id="fund" cssClass="selectwk" list="dropdownData.fundList" listKey="id" 
										listValue="name" value="%{fund.id}" disabled="%{isEnableSelect}" />
								</td>
							</tr>
							<tr>
								<td width="11%" class="whiteboxwk">
									<span class="mandatory">*</span><s:text name='estimate.financial.budgethead'/> :
								</td>
								<td width="21%" class="whitebox2wk">
									<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" 
									name="budgetGroup" id="budgetGroup" cssClass="selectwk" list="dropdownData.budgetHeadList" 
									listKey="id" listValue="name" value="%{budgetGroup.id}" disabled="%{isEnableSelect}"  />
									<input type="hidden" name="option" id="option" />
									<input type="hidden" name="deptName" id="deptName" />
									<input type="hidden" name="isEnableSelect" id="isEnableSelect"/>
								</td>
								
								<td width="15%" class="whiteboxwk">
								<span class="mandatory">*</span><s:text name='budgetfolio.finYear.label'/> :
								</td>
								<td width="53%" class="whitebox2wk">
									<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="finYearId" 
									id="finYearId" cssClass="selectwk" list="dropdownData.finYearList" listKey="id" 
									listValue="finYearRange" value="%{finYearId}" onchange="setAsOnDate(this);"/>
									
								</td>
							</tr>
							<tr>
								<td colspan="4">
									<div class="buttonholdersearch">
									    <s:submit cssClass="buttonadd" value="SEARCH" id="saveButton" name="button" method="searchBudgetFolio"
									    onClick="return validate();"/>
									</div>
								</td>
							</tr>
						<tr><td colspan="4">&nbsp;</td></tr>
				  				<%@ include file="financialDetail-approvedBudgetFolioDetail.jsp"%>   
				 			<tr><td colspan="4">&nbsp;</td></tr>
				  	</table>			
					</td>
				</tr>
				<tr>
					<td colspan="4" class="shadowwk"></td>
				</tr>
			</table>	
			</div>
			<div class="rbbot2"><div></div></div>
			</div>
			</div>
			</div>
			<div class="buttonholderwk">
				<s:if test="%{approvedBudgetFolioDetails}">
				 <s:submit cssClass="buttonpdf" value="VIEW PDF" id="pdfButton" name="pdfButton" method="viewBudgetFolioPdf"
									    onClick="return setUp(this);"/>
					<%-- input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/financialDetail!viewBudgetFolioPdf.action?option=searchPdf');"
					 class="buttonpdf" value="VIEW PDF" id="pdfButton" name="pdfButton" / --%>
				</s:if>
				<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="button" onclick="window.close();" />			
			</div>
		</s:form>
	</body>
</html>
