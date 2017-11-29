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

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title><s:text name="unit.rate.search.title"/></title>
<script type="text/javascript">
	function submitForm() {
		document.forms[0].action = 'unitRate-search.action';
		document.forms[0].submit;       
		return true;
	}

	function confirmClose(categoryId,bndryId) {
		/* var result = confirm("Do you really want to Deactivate selected record?"); */
		bootbox.dialog({ 
          message: "Do you really want to Deactivate selected record ?",
		  show: true,
		  backdrop: true,
		  closeButton: true,
		  animate: true,
		  className: "my-modal",
		  buttons: {
		    success: {   
		      label: "Yes",
		      className: "btn-primary",
		      callback: function() {
		    	  document.unitRateForm.action='${pageContext.request.contextPath}/admin/unitRate-deactivate.action?categoryId='+categoryId;
				  document.unitRateForm.submit(); 
			  }
		    },
		    "No": {
		      className: "btn-default",
		      callback: function() {
		    	  document.unitRateForm.action='${pageContext.request.contextPath}/admin/unitRate-search.action';
				  document.unitRateForm.submit(); 
			  }
		    }
		  }
		});
		
		
		/* 
	    if (result == true) {
	    	document.unitRateForm.action='${pageContext.request.contextPath}/admin/unitRate-deactivate.action?categoryId='+categoryId;
		    document.unitRateForm.submit(); 
		} else {
			document.unitRateForm.action='${pageContext.request.contextPath}/admin/unitRate-search.action';
			document.unitRateForm.submit(); 
		} */
	} 
	
</script>
</head>
<body>

	<s:form name="unitRateForm" action="unitRate" theme="simple">
		<s:if test="%{hasErrors()}">
		<div class="errorstyle" id="unitrate_error_area">
			<div class="errortext">
				<s:actionerror />
			</div>
		</div>
	  </s:if>
		<s:push value="model">
			<s:token />
			<s:hidden name="mode" id="mode" value="%{mode}" />
			<div class="formmainbox">
				<div class="headingbg">
					<s:text name="unit.rate.search.title" />
				</div>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="greybox" width="20%">&nbsp;</td>
						<td class="greybox" width="30%"><s:text name="unit.rate.zone" /><span
							class="mandatory1">*</span> :</td>
						<td class="greybox" width="30%"><s:select headerKey="-1"
								headerValue="%{getText('default.select')}" name="zoneId"
								id="zoneId" listKey="id" listValue="name"
								list="dropdownData.ZoneList" value="%{zoneId}"
								cssClass="selectnew" /></td>
						<td class="greybox" width="20%">&nbsp;</td>
					</tr>
					<tr>
						<td class="greybox" width="20%">&nbsp;</td>
						<td class="greybox" width="30%"><s:text
								name="unit.rate.usage" />
							:</td>
						<td class="greybox" width="30%"><s:select headerKey="-1"
								headerValue="%{getText('default.select')}" name="usageId"
								id="usageId" listKey="id" listValue="usageName"
								list="dropdownData.UsageList" value="%{usageId}"
								cssClass="selectnew" /></td>
						<td class="greybox" width="20%">&nbsp;</td>
					</tr>
					<tr>
						<td class="greybox" width="20%">&nbsp;</td>
						<td class="greybox" width="30%"><s:text
								name="unit.rate.structure.classification" /> :</td>
						<td class="greybox" width="30%"><s:select headerKey="-1"
								headerValue="%{getText('default.select')}"
								name="structureClassId" id="structureClassId" listKey="id"
								listValue="typeName"
								list="dropdownData.StructureClassificationList"
								value="%{structureClassId}" cssClass="selectnew" /></td>
						<td class="greybox" width="20%">&nbsp;</td>
					</tr>
				</table>
				<br /> <font size="2"><div align="right" class="mandatory1">
						&nbsp;&nbsp;
						<s:text name="mandtryFlds" />
					</div></font>

				<div class="buttonbottom" align="center">
					<s:submit value="Search" name="search" id='search'
						cssClass="buttonsubmit" method="create"
						onclick="return submitForm();" />
					<input type="button" name="button2" id="button2" value="Close"
						class="button" onclick="window.close();" />
				</div>


				<s:if test="%{bndryCatList != null && bndryCatList.size >0}">
					<tr>
						<display:table name="bndryCatList" id="linksTables" pagesize="10" export="true" requestURI="" class="tablebottom"
							style="width:100%" uid="currentRowObject">
							<display:caption>
							 Unit Rates
							</display:caption> 
							
							<display:column property="category.categoryName" title="Category Name" headerClass="bluebgheadtd"
								class="blueborderfortd" style="text-align:left" />
							<display:column property="bndry.name" title="Zone" headerClass="bluebgheadtd" class="blueborderfortd"
								style="text-align:left" />
							<display:column property="category.propUsage.usageName"	title="Property Usage" headerClass="bluebgheadtd"
								class="blueborderfortd" style="text-align:left" />
							<display:column property="category.structureClass.typeName"	title="Classification of Building" headerClass="bluebgheadtd"
								class="blueborderfortd" style="text-align:left" />
							<display:column property="category.categoryAmount"	title="Category Amount" headerClass="bluebgheadtd"
								class="blueborderfortd" style="text-align:left" />
							<display:column property="category.fromDate" format="{0,date,dd/MM/yyyy}" title="Effective From"
								headerClass="bluebgheadtd" class="blueborderfortd"
								style="text-align:left" />
							<s:if test="%{mode == 'edit'}">
							  <display:column title=""
									 media="html"
									headerClass="bluebgheadtd" class="blueborderfortd"
									style="text-align:center">
									<a href="../admin/unitRate-newForm.action?categoryId=${currentRowObject.category.id}&zoneId=${currentRowObject.bndry.id}&mode=edit"> Edit</a>
									</display:column> 
									<display:column title="" media="html"
									headerClass="bluebgheadtd" class="blueborderfortd"
									style="text-align:center">
									<a href="javascript:void(0);" onclick="confirmClose('${currentRowObject.category.id}','${currentRowObject.bndry.id}');">Deactivate</a>
									</display:column>
									</s:if>
							<s:if test="%{mode == 'view'}">
							 <display:column title=""
									 media="html"
									headerClass="bluebgheadtd" class="blueborderfortd"
									style="text-align:center">
									<a href="../admin/unitRate-view.action?categoryId=${currentRowObject.category.id}&zoneId=${currentRowObject.bndry.id}&mode=view"> View</a>
									</display:column> 
									</s:if>

							<display:setProperty name="export.pdf" value="true" />
							<display:setProperty name="export.rtf" value="false" />
							<display:setProperty name="export.xml" value="false" />
							<display:setProperty name="export.csv" value="true" />
							<display:setProperty name="export.excel" value="true" />
						</display:table>
					</tr>
				</s:if>
			</div>
		</s:push>
	</s:form>
	
</body>