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


<html>
	<head>
		<title><s:text name="chPropAdd" /></title>
		<script type="text/javascript">
			jQuery.noConflict();
			jQuery("#loadingMask").remove();
		</script>
	</head>
	<body>
		<div align="left">
  			<s:actionerror/>
  		</div>
		<div class="errorstyle" id="property_error_area"
			style="display: none;"></div>
		<s:form method="post" action="../modify/changePropertyAddress.action"
			theme="simple" name="modifyPropertyForm">
			<s:push value="model">
			<s:token />
			<div class="formmainbox">
			<div class="formheading"></div>
			<div class="headingbg"> <s:text name="chPropAdd"/> </div>
				<table border="0" width="100%" cellpadding="0" cellspacing="0">
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
						<td class="bluebox" width="25%">
							<s:text name="prop.Id" />
							:
						</td>
						<td class="bluebox" width="25%">
							<b><s:property value="%{basicProperty.upicNo}" /> </b>
						</td>
						<s:hidden name="indexNumber" value="%{basicProperty.upicNo}"></s:hidden>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="greybox" width="25%">
							&nbsp;
						</td>
						<td class="greybox">
							<s:text name="HouseNo" />
							:
						</td>
						<td class="greybox">
							<b><s:property value="%{basicProperty.address.houseNo}"
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
						<td class="bluebox">
							<s:text name="Zone" />
							:
						</td>
						<td class="bluebox">
							<b><s:property value="%{basicProperty.propertyID.zone.name}"
									default="N/A" />
							</b>
						</td>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="greybox" width="25%">
							&nbsp;
						</td>
						<td class="greybox">
							<s:text name="Ward" />
							:
						</td>
						<td class="greybox">
							<b><s:property value="%{basicProperty.propertyID.ward.name}"
									default="N/A" />
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
						<td class="bluebox">
							<s:text name="Area" />
							:
						</td>
						<td class="bluebox">
							<b><s:property value="%{basicProperty.propertyID.area.name}"
									default="N/A" />
							</b>
						</td>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>
					</tr>
					<tr>		
						<td class="greybox" width="25%">
							&nbsp;
						</td>				
						<td class="greybox" style="text-align: left" colspan="2">
							<b><s:text name="addressChange" />
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
						<td class="bluebox">
							<s:text name="Address" />
							<span class="mandatory">*</span> :
						</td>
						<td class="bluebox">
							<s:textfield name="streetAddress1" maxlength="512"
								onblur="validateAddress(this);" />
						</td>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>
					</tr>

					<tr>
						<td class="greybox" width="25%">
							&nbsp;
						</td>
						<td class="greybox">
							<s:text name="HouseNo" />
							<span class="mandatory">*</span> :
						</td>
						<td class="greybox">
							<s:textfield name="houseNo" maxlength="50"
								onblur="validatePlotNo(this,'Plot No/House No');"></s:textfield>
						</td>
						<td class="greybox" width="25%">
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>
						<td class="bluebox">
							<s:text name="OldNo" />
							:
						</td>
						<td class="bluebox">
							<s:textfield name="doorNumOld" maxlength="50"
								onblur="validatePlotNo(this,'Plot No/House No');"></s:textfield>
						</td>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>
					</tr>

					<tr>
						<td class="greybox" width="25%">
							&nbsp;
						</td>
						<td class="greybox">
							<s:text name="PinCode" />
							:
						</td>
						<td class="greybox">
							<s:textfield name="pinCode" maxlength="6"
								onblur="validNumber(this);checkZero(this);"></s:textfield>
						</td>
						<td class="greybox" width="25%">
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>
						<td class="bluebox">
							<s:text name="MobileNumber" />
							:
						</td>
						<td class="bluebox">
							<s:textfield name="mobileNo" maxlength="10"
								onblur="validNumber(this);checkZero(this,'Mobile Number');"></s:textfield>
						</td>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>
					</tr>
					<tr>
					<td class="greybox" width="8%">&nbsp;</td>
				    <td class="greybox" width="8%"><s:text name="address.khasraNumber"/> : </td>
				    <td class="greybox"><s:textfield id="khasraNumber" name="extraField1" value="%{extraField1}"  maxlength="128" /></td>
				    <td class="greybox" colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td class="bluebox" width="8%">&nbsp;</td>
					<td class="bluebox" width="10%"><s:text name="address.Mauza"/> : </td>
				    <td class="bluebox"><s:textfield  id="mauza"  name="extraField2" value="%{extraField2}" maxlength="128" /></td>
				    <td class="bluebox" colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td class="greybox" width="8%">&nbsp;</td>
				    <td class="greybox" width="8%"><s:text name="address.citySurveyNumber"/> : </td>
				    <td class="greybox"><s:textfield id="citySurveyNumber" name="extraField3" value="%{extraField3}" maxlength="128" /></td>
				    <td class="greybox" colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td class="bluebox" width="8%">&nbsp;</td>
				    <td class="bluebox" width="10%"><s:text name="address.sheetNumber"/> : </td>
				    <td class="bluebox"><s:textfield id="sheetNumber" name="extraField4" value="%{extraField4}" maxlength="128" /></td>
				    <td class="bluebox" colspan="2">&nbsp;</td>
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
	
					<tr><%@ include file="../workflow/property-workflow.jsp"%></tr>
					<s:hidden name="modelId" id="modelId" value="%{modelId}" />
				</table>
				<div id="loadingMask" style="display:none"><p align="center"><img src="/ptis/resources/erp2/images/bar_loader.gif"> <span id="message"><p style="color: red">Please wait....</p></span></p></div>
				<div class="buttonbottom" align="center">					
				    <s:submit cssClass="buttonsubmit" name="action" value="Approve" theme="simple" method="save" id="ChangeAddress:Save" onclick="setWorkFlowInfo(this);doLoadingMask();"/>					
					<s:submit cssClass="buttonsubmit" name="action" value="Forward" theme="simple" method="forward" id="ChangeAddress:Forward" onclick="setWorkFlowInfo(this);doLoadingMask();"/>
					<input type="button" class="button" value="Close" onclick="return confirmClose();">
				</div>
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

