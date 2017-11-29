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
		<title><s:text name='advancerequisition.search.title' /></title>
	</head>
<script src="<egov:url path='resources/js/works.js?${app_release_no}'/>"></script> 

<script type="text/javascript">
function validateAndSubmit(){
	   if(dom.get('estimateNumberSearch').value == "" && dom.get('advanceRequisitionFromDate').value == "" && dom.get('advanceRequisitionToDate').value == ""
		   && dom.get('arfStatus').value == -1 && dom.get('contractorId').value == -1 && dom.get('drawingOfficerId').value == -1 
		   && dom.get('executingDepartmentId').value == -1 && dom.get('workOrderNumberSearch').value=="") {
	   		dom.get("searchContractorAdvance_error").innerHTML='<s:text name="contractoradvance.validation.select.one.search.creteria"/>'
			dom.get("searchContractorAdvance_error").style.display='';
        	return false;
	  }
	   else {
			dom.get("searchContractorAdvance_error").innerHTML='';
			dom.get("searchContractorAdvance_error").style.display="none";
		}
}

var estimateNumberSearchSelectionHandler = function(sType, arguments){ 
	var oData = arguments[2];
};

var workOrderNumberSearchSelectionHandler = function(sType, arguments){ 
	var oData = arguments[2];
};

function goToPage(obj){
	var currRow=getRow(obj);
	var id = getControlInBranch(currRow,'arfId');
	var arfStateId=getControlInBranch(currRow,'arfStateId');
   if(dom.get('searchActions')[1]!=null && obj.value==dom.get('searchActions')[1].value){	
		var url = '${pageContext.request.contextPath}/contractoradvance/contractorAdvanceRequisition!edit.action?sourcepage=search&id='+id.value;
		window.open(url,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(dom.get('searchActions')[2]!=null && obj.value==dom.get('searchActions')[2].value){
		window.open("${pageContext.request.contextPath}/estimate/abstractEstimate!workflowHistory.action?stateValue="+
				arfStateId.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
}

</script>

<body>
	<div class="errorstyle" id="searchContractorAdvance_error" style="display: none;"></div>
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
	<s:form name="searchContractorAdvanceRequisition" id="searchContractorAdvanceRequisition" theme="simple" onsubmit="return validateAndSubmit(); ">		
		<div class="formmainbox">
			<div class="insidecontent">
				<div id="printContent" class="rbroundbox2">
					<div class="rbtop2"><div></div></div>
					<div class="rbcontent2">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr><td>&nbsp;</td></tr>
							<tr>
								<td>
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td width="100%" colspan="4" class="headingwk">
												<div class="arrowiconwk">
													<img src="/egworks/resources/erp2/images/arrow.gif" />
												</div>
												<div class="headplacer">
													<s:text name="search.criteria" />
												</div>
											</td>
										</tr>
										
										<tr>	
											<td class="whiteboxwk"><s:text name="advancerequisition.search.arfstatus" />:</td>
											<td class="whitebox2wk">
												<s:select headerKey="-1" headerValue="%{getText('list.default.select')}"
													name="arfStatus" id="arfStatus" cssClass="selectwk" list="dropdownData.statusList" listKey="id"
													listValue='description' value="%{arfStatus}" />
											</td>	
											<td class="whiteboxwk"><s:text name="contractoradvance.estimatenumber" />:</td>
											<td class="whitebox2wk" >
				        						<div class="yui-skin-sam">
				        							<div id="estimateNumberSearch_autocomplete">
			                							<div>
				        									<s:textfield id="estimateNumberSearch" name="estimateNumber" value="%{estimateNumber}" cssClass="selectwk" />
				        								</div>
				        								<span id="estimateNumberSearchResults"></span>
				        							</div>	
				        						</div>
				        						<egov:autocomplete name="estimateNumberSearch" width="20" field="estimateNumberSearch" url="ajaxContractorAdvance!searchEstimateNumberFromARF.action?" queryQuestionMark="false" results="estimateNumberSearchResults" handler="estimateNumberSearchSelectionHandler" queryLength="3"/>
					         				</td>										
										</tr>									
									
										<tr>
											<td class="greyboxwk"><s:text name="advancerequisition.search.fromdate" />:</td>
											<td class="greybox2wk">											
												<s:date name="advanceRequisitionFromDate" var="advanceRequisitionFromDateFormat" format="dd/MM/yyyy" />
												<s:textfield name="advanceRequisitionFromDate" id="advanceRequisitionFromDate" cssClass="selectwk" value="%{advanceRequisitionFromDateFormat}"
													onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')" />
												<a	href="javascript:show_calendar('forms[0].advanceRequisitionFromDate',null,null,'DD/MM/YYYY');"
													onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"> 
													<img src="/egworks/resources/erp2/images/calendar.png"
														alt="Calendar" width="16" height="16" border="0" align="absmiddle" />
												</a>
											</td>
											<td class="greyboxwk"><s:text name="advancerequisition.search.todate" />:</td>
											<td class="greybox2wk">
												<s:date name="advanceRequisitionToDate" var="advanceRequisitionToDateFormat" format="dd/MM/yyyy" />
												<s:textfield name="advanceRequisitionToDate" id="advanceRequisitionToDate" value="%{advanceRequisitionToDateFormat}" cssClass="selectwk"
													onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')" />
												<a href="javascript:show_calendar('forms[0].advanceRequisitionToDate',null,null,'DD/MM/YYYY');"
													onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"> <img
														src="/egworks/resources/erp2/images/calendar.png"
														alt="Calendar" width="16" height="16" border="0" align="absmiddle" />
												</a>
											</td>
										</tr>
										<tr>
											<td class="whiteboxwk"><s:text name="contractoradvance.contractor" />:</td>
											<td class="whitebox2wk">
												<s:select id="contractorId" name="contractorId" cssClass="selectwk"
													list="%{contractorsInARF}"  headerKey="-1"
													headerValue="--- Select ---" value="%{contractorId}" />
											</td>
											<td class="whiteboxwk"><s:text name="contractoradvance.workordernumber" />:</td>
											<td class="whitebox2wk" > 
				        						<div class="yui-skin-sam">
				        							<div id="workOrderNumberSearch_autocomplete">
			                							<div> 
				        									<s:textfield id="workOrderNumberSearch" name="workOrderNumber" value="%{workOrderNumber}" cssClass="selectwk" />
				        								</div>
				        								<span id="workOrderNumberSearchResults"></span>
				        							</div>		
				        						</div>
				        						<egov:autocomplete name="workOrderNumberSearch" width="20" field="workOrderNumberSearch" url="ajaxContractorAdvance!searchWorkOrderNumberFromARF.action?" queryQuestionMark="false" results="workOrderNumberSearchResults" handler="workOrderNumberSearchSelectionHandler" queryLength="3" />
					         				</td>
										</tr>
										
										<tr>
											<td class="greyboxwk"> <s:text name="contractoradvance.executingdepartment" /></td>
											<td class="greybox2wk" >
												<s:select headerKey="-1" headerValue="%{getText('list.default.select')}" name="executingDepartmentId" 
													id="executingDepartmentId" cssClass="selectwk" list="dropdownData.executingDepartmentList" listKey="id"
													listValue='deptName' value="%{executingDepartmentId}" />
											</td>
											<td class="greyboxwk"><s:text name="advancerequisition.search.drawingofficer" />:</td>
											<td class="greybox2wk">
												<s:select headerKey="-1" headerValue="%{getText('list.default.select')}" name="drawingOfficerId" 
													id="drawingOfficerId" cssClass="selectwk" list="dropdownData.drawingOfficerList" listKey="id"
													listValue='code+" - "+name' value="%{drawingOfficerId}" />  
											</td>
										</tr>
										</tr>
									<tr>
										
				         			</tr>
									<tr>
											<td colspan="4" class="shadowwk"></td>
										</tr>
										
										<tr>
											<td colspan="4">
												<div  class="buttonholderwk" align="center">
													<s:submit  cssClass="buttonadd" value="SEARCH"
														id="saveButton" name="button"  onclick="return validateAndSubmit();" method="searchList" />
													<input type="button" class="buttonfinal" value="CLOSE"
														id="closeButton" name="button" onclick="window.close();" />
										</td>
										</tr>
										<%@ include file='searchContractorAdvanceRequisition-result.jsp'%>
									</table>
							</td>
							</tr>
							
						</table>
						
					</div>
					<!-- end of rbroundbox2 -->
				</div>
				<!-- end of insidecontent -->
			</div>
			<!-- end of formmainbox -->
	</s:form>
</body>
</html>
