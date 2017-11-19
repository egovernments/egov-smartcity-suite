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
<%@include file="/includes/taglibs.jsp"%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<html>
	<head>
		<title><s:text name="deactivate.prop"/></title>
		<script type="text/javascript" src="/javascript/calendar.js">	
		</script>
		<script type="text/javascript" src="/javascript/dateValidation.js">	
		</script>		
		<script type="text/javascript">
			jQuery.noConflict();
			jQuery("#loadingMask").remove();
		</script>
	</head>
	<body>
		<div align="left" class="mandatory">
			<s:actionerror />
		</div>
		<div class="errorstyle" id="property_error_area" style="display:none;"></div>
		<s:form method="post" theme="simple" name="deactivatePropertyForm"
			validate="true">
			<s:push value="model">
			<s:token />
			<div class="formmainbox">
			<center>
				<table border="0" width="100%" cellpadding="0" cellspacing="0">
					<tr>
						<td class="headingbg" width="25%">							
							<div class="formheading"></div>
							<div class="headingbg">&nbsp;</div>
						</td>
						<td class="headingbg" colspan="2">							
							<div class="formheading"></div>
							<div class="headingbg">							
							<s:text name="deactivate.prop"></s:text>											
							</div>
						</td>
						<td class="headingbg" width="25%">							
							<div class="formheading"></div>
							<div class="headingbg">&nbsp;</div>
						</td>
					</tr>
					<tr>
						<td class="greybox" width="25%">
							&nbsp;
						</td>
						<td class="greybox" colspan="2">
							&nbsp;&nbsp;&nbsp;
						</td>
						<td class="greybox" width="25%">
							&nbsp;
						</td>		
					</tr>
					<tr>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>
						<td class="bluebox" >
							<s:text name="prop.Id" />
							:
						</td>

						<td class="bluebox">
							<b><s:property value="%{basicProp.upicNo}" /> </b>
						</td>
						<s:hidden name="propertyId" value="%{basicProp.upicNo}"></s:hidden>
						<s:hidden name="ownerName" value="%{ownerName}"></s:hidden>
						<s:hidden name="address" value="%{address}"></s:hidden>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>			
					</tr>

					<tr>
						<td class="greybox" width="25%">
							&nbsp;
						</td>
						<td class="greybox" >
							<s:text name="HouseNo" />
							:
						</td>

						<td class="greybox">
							<b><s:property value="%{basicProp.address.houseNo}"
									default="N/A" /> </b>
						</td>
						<td class="greybox" width="25%">
							&nbsp;
						</td>			
					</tr>
					<tr>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>
						<td class="bluebox" >
							<s:text name="OwnerName" />
							:
						</td>

						<td class="bluebox">
							<b><s:property value="%{ownerName}" default="N/A" /> </b>
						</td>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>			
					</tr>
					<tr>
						<td class="greybox" width="25%">
							&nbsp;
						</td>
						<td class="greybox"  style="vertical-align: top">
							<s:text name="PropertyAddress" />
							:
						</td>

						<td class="greybox">
							<b><s:property value="%{address}" default="N/A" /> </b>
						</td>
						<td class="greybox" width="25%">
							&nbsp;
						</td>			
					</tr>
					<tr>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>
						<td class="bluebox" >
							<s:text name="PropertyType" />
							:
						</td>

						<td class="bluebox">
							<b><s:property
									value="%{basicProp.property.propertyDetail.propertyTypeMaster.type}"
									default="N/A" /> </b>
						</td>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>			
					</tr>
					<tr>
						<td class="greybox" width="25%">
							&nbsp;
						</td>
						<td class="greybox" >
							<s:text name="deactRsn" />
							<span class="mandatory">*</span> :
						</td>

						<td class="greybox">
							<b><s:select name="reason" list="dropdownData.Reason"
									headerKey="none" listKey="code" listValue="mutationName"
									headerValue="----Choose----" cssStyle="width:150px"></s:select>
							</b>
						</td>
						<td class="greybox" width="25%">
							&nbsp;
						</td>			
					</tr>
					<tr>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>
						<td class="bluebox" >
							<s:text name="OrdNo" />
							:
						</td>
						<td class="bluebox">
							<s:textfield name="referenceNo"></s:textfield>
						</td>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>			
					</tr>
					<tr>
						<td class="greybox" width="25%">
							&nbsp;
						</td>
						<td class="greybox" >
							<s:text name="OrdDate" />
							:
						</td>

						<td class="greybox" style="padding-right: 0; margin-right: 0">
							<s:date name="referenceDate" format="dd/MM/yyyy"
								var="refDate" />
							<s:textfield id="rDate" name="referenceDate"
								value="%{refDate}" size="10"></s:textfield>
								<a 
								href="javascript:show_calendar('deactivatePropertyForm.rDate')"><img
									src="../image/calendaricon.gif" style="border: none" /> </a>
						</td>

						<td class="greybox" width="25%">
							&nbsp;
						</td>
									
					</tr>
					<tr>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>
						<td class="bluebox"  style="vertical-align: top">
							<s:text name="comment" />
							<span class="mandatory">*</span>:
						</td>
						<td class="bluebox">
							<b><s:textarea name="remarks" cols="18"></s:textarea> </b>
						</td>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>			
					</tr>
					<tr>
		<td class="greybox2">&nbsp;</td>
		<td class="greybox">Upload Document</td>
		<td class="greybox"><input type="button" class="button"
			value="Upload Document" id="docUploadButton"
			onclick="showDocumentManager();" /> <s:hidden name="docNumber"
				id="docNumber" /></td>
		<td class="greybox" colspan="2">&nbsp;</td>
	</tr>
					<tr>
						<td class="greybox" width="25%">
							&nbsp;
						</td>
						<td class="greybox" colspan="2">
							&nbsp;&nbsp;&nbsp;
						</td>
						<td class="greybox" width="25%">
							&nbsp;
						</td>			
							
					</tr>
					<tr>
        				<%@ include file="../workflow/property-workflow.jsp" %>  
       			 	</tr>
       			 	<s:hidden name="modelId" id="modelId" value="%{modelId}"/>
				</table>
				<div id="loadingMask" style="display:none"><p align="center"><img src="/ptis/resources/erp2/images/bar_loader.gif"> <span id="message"><p style="color: red">Please wait....</p></span></p></div>
				<div class="buttonbottom" align="center">					
					<s:submit cssClass="buttonsubmit" value="Approve" id="Deactivate:Save" theme="simple" method="save" onclick="setWorkFlowInfo(this);doLoadingMask();"/>																				
					<s:submit cssClass="buttonsubmit" value="Forward" id="Deactivate:Forward" theme="simple" method="forward" onclick="setWorkFlowInfo(this);doLoadingMask();"/>
					<input type="button" class="button" value="Close" onclick="return confirmClose();">
				</div>								
			</center>
			</div>
			</s:push>
		</s:form>
		<script type="text/javascript">
	function showDocumentManager() {
			var docNum = document.getElementById("docNumber").value;
			var url;
			if (docNum == null || docNum == '' || docNum == 'To be assigned') {
				url = "/egi/docmgmt/basicDocumentManager.action?moduleName=ptis";
			} else {
				url = "/egi/docmgmt/basicDocumentManager!editDocument.action?docNumber="
						+ docNum + "&moduleName=ptis";
			}
			window.open(url, 'docupload', 'width=1000,height=400');
		}
		</script>
	</body>
</html>
