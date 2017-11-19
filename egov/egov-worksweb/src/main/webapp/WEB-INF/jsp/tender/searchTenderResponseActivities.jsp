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
<script src="<egov:url path='resources/js/works.js?${app_release_no}'/>"></script>
<script>

function validateAndSubmit() {
	document.searchTenderResponseActivitiesForm.action='${pageContext.request.contextPath}/tender/searchTenderResponseActivities!search.action';
 	document.searchTenderResponseActivitiesForm.submit();
}

function checkAll(obj){ 
	var len=document.forms[0].selectedActivity.length;
	if(obj.checked){
		if(len>0){
			for (i = 0; i < len; i++)
				document.forms[0].selectedActivity[i].checked = true;
		}else document.forms[0].selectedActivity.checked = true;
	}
	else{
		if(len>0){
			for (i = 0; i < len; i++)
				document.forms[0].selectedActivity[i].checked = false;
		}else document.forms[0].selectedActivity.checked = false;
	}
}

function returnBackToParent() { 
	var value = new Array();
	var wind=window.dialogArguments;
	var len=document.forms[0].selectedActivity.length; 
	var j=0;
	if(len >0){
		for (i = 0; i < len; i++){
			if(document.forms[0].selectedActivity[i].checked){
				value[j] = document.forms[0].tenderResponseActivityId[i].value+'~'+document.forms[0].assignedQuantity[i].value;
				j++;
			}
		}
	}
	else{
		if(document.forms[0].selectedActivity.checked){
			value[j] = dom.get('tenderResponseActivityId').value+'~'+dom.get('assignedQuantity').value;
		}
	}
	if(value.length>0)
	{
		var wind;
		var data = new Array();
		wind=window.dialogArguments;
		if(wind==undefined){
			wind=window.opener;
			data=value;
			window.opener.update(data);
		}
		else{
			wind=window.dialogArguments;
			wind.result=value;
		}
		window.close();
	}
	else{
		dom.get("search_error").innerHTML='Please Select any one of the Activities'; 
        dom.get("search_error").style.display='';
		return false;
	 }
	 dom.get("search_error").style.display='none';
	 dom.get("search_error").innerHTML='';
	return true;
}

</script>

<html>
<head>
    <title><s:text name="workOrder.title.activity.search" /></title>
</head>
<body>
<s:if test="%{hasErrors()}">
	<div class="errorstyle">
		<s:actionerror />
		<s:fielderror />
	</div>
</s:if>
<s:if test="%{hasActionMessages()}">
	<div class="messagestyle">
		<s:actionmessage theme="simple" />
	</div>
</s:if>
<s:form action="searchTenderResponseActivities" theme="simple"	name="searchTenderResponseActivitiesForm" >
<div class="errorstyle" id="search_error" style="display:none;"></div>
<div class="navibarshadowwk"></div>
	<div class="formmainbox">
	<div class="insidecontent">
		<div class="rbroundbox2">
		<div class="rbtop2"><div></div></div>
		<div class="rbcontent2">
		<s:hidden name="tenderRespId" id="tenderRespId"  value="%{tenderRespId}"/>
		<s:hidden name="selectedactivities" id="selectedactivities"  value="%{selectedactivities}"/>
		<table id="formTable" width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
					<table id="activitySearchTable" width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td colspan="4">&nbsp;</td>
						</tr>
							
						<tr>
							<td colspan="4" class="headingwk">
								<div class="arrowiconwk">
									<img src="/egworks/resources/erp2/images/arrow.gif" />
								</div>
								<div class="headplacer">
									<s:text name='title.search.criteria' />
								</div>
							</td>
						</tr>
						<tr>
							<td width="19%" class="greyboxwk">
								<s:text name='workOrder.activitySearch.activityType' />:
							</td>
							<td width="30%" class="greybox2wk" colspan="3">
								<s:select name="activityType" id="activityType" list="#{'--Select--':'--Select--','SOR':'SOR','Non SOR':'Non SOR','Both':'Both'}" value="%{activityType}" />									
							</td>
						</tr>
						<tr>
							<td width="19%" class="whiteboxwk">
								<s:text name='workOrder.activitySearch.sorCode' />:
							</td>
							<td width="30%" class="whitebox2wk">
								<s:textfield name="sorCode" cssClass="selectwk" id="sorCode" maxlength = "50" />
							</td>
							<td width="14%" class="whiteboxwk">
								<s:text name='workOrder.activitySearch.description' />:
							</td>
							<td width="37%" class="whitebox2wk">
								<s:textarea name="activityDesc" cols="25"  rows="2" cssClass="selectwk" id="activityDesc" />
							</td>
						</tr>
						<tr>
							<td width="19%" class="greyboxwk">
								<s:text name='workOrder.activitySearch.estNo' />:
							</td>
							<td width="30%" class="greybox2wk">
									<s:select id="estimateId" name="estimateId" headerKey="-1"  headerValue="%{getText('estimate.default.select')}"
									cssClass="selectwk" list="dropdownData.estimateList" value="%{estimateId}" listKey="id" listValue="estimateNumber"/>								
							</td>
							<td width="14%" class="greyboxwk">
								<s:text name='workOrder.activitySearch.estName' />:
							</td>
							<td width="37%" class="greybox2wk">
								<s:textarea name="estimateName" cols="25"  rows="2" cssClass="selectwk" id="estimateName" />
							</td>
						</tr>
							
						<tr>
							<td>
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="4">
								<div class="buttonholdersearch">
									<input type="submit" class="buttonadd" value="Search" id="searchButton" 
										name="Search"  onclick="return validateAndSubmit();" />
									<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="button"
										onclick="window.close();" />
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="4">
							<div>
								<table width="100%" border="0" cellspacing="0"
									cellpadding="0">
									<tr>
										<td colspan="7" class="headingwk">
											<div class="arrowiconwk">
												<img
													src="/egworks/resources/erp2/images/arrow.gif" />
											</div>
											<div class="headplacer">
												<s:text name="title.search.result" />
											</div>
										</td>
									</tr>
									<tr>
										<td>
											<display:table name="searchResult" pagesize="100" uid="currentRow"
												cellpadding="0" cellspacing="0" requestURI=""
												style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
												
												<display:column headerClass="pagetableth" class="pagetabletd"
													title="Sl.No" titleKey="workOrder.activitySearch.slNo"
													style="width:3%;text-align:right">
													<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
													<s:hidden name="tenderResponseActivityId" id="tenderResponseActivityId" value="%{#attr.currentRow.id}" />
													<s:hidden name="assignedQuantity" id="assignedQuantity" value="%{#attr.currentRow.assignedQty}" />
												</display:column>
												
												<display:column headerClass="pagetableth" class="pagetabletd"
													title='<input type="checkbox" id="checkedAll" name="checkedAll" onclick="checkAll(this)" />' 
													style="width:3%;text-align:center">
													<s:checkbox id="selectedActivity" name="selectedActivity" />
												</display:column>
											
												<display:column headerClass="pagetableth" class="pagetabletd"
														title="Description" titleKey="workOrder.activitySearch.description" style="width:40%;text-align:left">
													<s:if test="%{#attr.currentRow.activity.schedule}">
														<s:property value="#attr.currentRow.activity.schedule.summary" />
														<a href="#" class="hintanchor" onMouseover='showhint("<s:property value='%{#attr.currentRow.activity.schedule.description}' />", this, event, "300px")'><img src="/egworks/resources/erp2/images/help.gif" alt="Help" width="16" height="16" border="0" align="absmiddle"/></a>
													</s:if>	
													<s:else>
														<s:property value="#attr.currentRow.activity.nonSor.description" />
													</s:else>														
												</display:column>
													<display:column headerClass="pagetableth" class="pagetabletd"
														title="SOR Code" titleKey="workOrder.activitySearch.sorCode" style="width:12%;text-align:left">
														<s:if test="%{#attr.currentRow.activity.schedule}">
															<s:property value="#attr.currentRow.activity.schedule.code" />
														</s:if>	
												</display:column>
													
												<display:column headerClass="pagetableth" class="pagetabletd"
													title="Estimate Number" titleKey="workOrder.activitySearch.estNo" style="width:12%;text-align:left"
													property="activity.abstractEstimate.estimateNumber" />
													
												<display:column headerClass="pagetableth" class="pagetabletd"
													title="Estimate Quantity" titleKey="workOrder.activitySearch.estQty" style="width:12%;text-align:right"
													property="activity.quantity" />
													
												<display:column headerClass="pagetableth" class="pagetabletd"
													title="Quantity Aassigned" titleKey="workOrder.activitySearch.qtyAssigned" style="width:12%;text-align:right"
													property="assignedQty" />
													
												<display:column headerClass="pagetableth" class="pagetabletd"
													title="UOM" titleKey="workOrder.activitySearch.uom" style="width:12%;text-align:left"
													property="activity.uom.uom" />
												<display:column headerClass="pagetableth" class="pagetabletd"
													title="Negotiated Rate" titleKey="workOrder.activitySearch.negotiatedRate" style="width:12%;text-align:right"
													property="negotiatedRate" />
												<display:setProperty name="paging.banner.all_items_found" value=""></display:setProperty>
												<display:setProperty name="paging.banner.one_item_found" value=""></display:setProperty>
												<display:setProperty name="paging.banner.some_items_found" value=""></display:setProperty>
												
											</display:table>
											
											<s:if test="%{searchResult.fullListSize>0}">
												<div class="buttonholderwk">
													<input type="button" class="buttonfinal" value="ADD" id="button"
														name="button" onclick="returnBackToParent()" />
													<input type="button" class="buttonfinal" value="CLOSE"
														id="closeButton" name="closeButton" onclick="window.close();" />
												</div>
											</s:if>
											
										</td>
									</tr>								
								</table>
							</div>											
							</td>
						</tr>
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
</s:form>		
</body>
</html>

