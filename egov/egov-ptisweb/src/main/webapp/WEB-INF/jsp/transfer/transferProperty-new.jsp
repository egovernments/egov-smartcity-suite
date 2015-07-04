<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<html>
	<head>
		<title><s:text name='transferProperty' /></title>
		<link href="<c:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>" rel="stylesheet" type="text/css" />
        <script src="<c:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
		<script type="text/javascript">
		jQuery.noConflict();
		jQuery("#loadingMask").remove();
	function loadOnStartUp() {
		document.getElementById("saleDtls").className = "hiddentext";
		document.getElementById("crtOrderNum").className = "hiddentext";
		document.getElementById("saleDtls").readOnly = true;
		document.getElementById("crtOrderNum").readOnly = true;
		enableBlock();
		var applDate = document.getElementById("noticeDate").value;
		var deedDate = document.getElementById("deedDate").value;

		if (applDate == "" || applDate == "DD/MM/YYYY" || applDate == undefined) {
			waterMarkInitialize('noticeDate', 'DD/MM/YYYY');
		}
		if (deedDate == "" || deedDate == "DD/MM/YYYY" || deedDate == undefined) {
			waterMarkInitialize('deedDate', 'DD/MM/YYYY');
		}

		try { 
			jQuery(".datepicker").datepicker({
				format: "dd/mm/yyyy"
			}); 
		}catch(e){
			console.warn("No Date Picker "+ e);
		}

		jQuery('.datepicker').on('changeDate', function(ev){
			jQuery(this).datepicker('hide');
		});
		
		
	}
</script>
	</head>
	<body onload="loadOnStartUp();">
		<div class="formmainbox">
			<s:if test="%{hasErrors()}">
				<div class="errorstyle" id="property_error_area">
					<div class="errortext">
						<s:actionerror />
					</div>
				</div>
			</s:if>
			<s:form action="save" name="transferform" theme="simple" enctype="multipart/form-data">
				<s:push value="model">
				<s:token/>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<div class="headingbg">
						<s:text name="transferortitle" />
					</div>
					<tr>
						<td class="bluebox2" style="width:5%;">
							&nbsp;
						</td>
						<td class="bluebox" style="width:20%">
							<s:text name="prop.Id"></s:text> :
						</td>
						<td class="bluebox">
							<span class="bold"><s:property value="propertyImpl.basicProperty.upicNo" default="N/A"/></span>
						</td>
						<td class="bluebox">
							&nbsp;
						</td>
						<td style="width:25%;">&nbsp;</td>
					</tr>
					<tr>
						<td class="bluebox2">
							&nbsp;
						</td>
						<td class="bluebox">
							<s:text name="PropertyAddress"></s:text> :
						</td>
						<td class="bluebox">
							<span class="bold"><s:property value="propertyImpl.basicProperty.address" /></span>
						</td>
						<td class="bluebox">
							<s:text name="Zone"></s:text> :
						</td>
						<td class="bluebox">
							<span class="bold"><s:property value="propertyImpl.basicProperty.propertyID.zone.name" /></span>
						</td>
					</tr>
					
					<tr>
						<td class="greybox2">
							&nbsp;
						</td>
						<td class="greybox">
							<s:text name="Ward" /> :
						</td>
						<td class="greybox">
							<span class="bold"><s:property value="propertyImpl.basicProperty.propertyID.ward.name" /></span>
						</td>
						<td class="greybox">
							<s:text name="block" /> :
						</td>
						<td class="greybox">
							<span class="bold"><s:property value="propertyImpl.basicProperty.propertyID.area.name" /></span>
						</td>
					</tr>
					
					<tr>
						<td class="greybox2">
							&nbsp;
						</td>
						<td class="greybox">
							<s:text name="currentpropertytax" /> :
						</td>
						<td class="greybox">
							<span class="bold">Rs. <s:property value="currentPropertyTax" /> /-</span>
						</td>
					</tr>
					<tr>
						<td colspan="5">
							<table class="tablebottom" id="" width="100%" border="0"
								cellpadding="0" cellspacing="0">
								<tbody>
									<tr>
										<th class="bluebgheadtd">Aadhaar No</th>
										<th class="bluebgheadtd">Owner Name</th>
										<th class="bluebgheadtd">Gender</th>
										<th class="bluebgheadtd">Mobile Number</th>
										<th class="bluebgheadtd">Email Address</th>

									</tr>
									<s:iterator value="propertyImpl.basicProperty.propertyOwnerInfo" status="status">
									<tr>
										<td class="blueborderfortd" align="center"><s:property value="owner.aadhaarNumber" /></td>
										<td class="blueborderfortd" align="center"><s:property value="owner.name" /></td>
										<td class="blueborderfortd" align="center"><s:property value="owner.gender" /></td>
										<td class="blueborderfortd" align="center"><s:property value="owner.mobileNumber" /></td>
										<td class="blueborderfortd" align="center"><s:property value="owner.emailId" /></td>
									</tr>
									</s:iterator>
								</tbody>
							  </table> 
						</td>
					</tr>
					<tr>
						<td colspan="5">
							<div class="headingsmallbg">
								<s:text name="transferDtls" />
							</div>
						</td>
					</tr>

					<tr>
						<td colspan="5">
							<div>
								<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" id="nameTable" >
								    <tr>
								    	<th class="bluebgheadtd">Aadhaar No</th>
										<th class="bluebgheadtd">Owner Name</th>
										<th class="bluebgheadtd">Gender</th>
										<th class="bluebgheadtd">Mobile Number(without +91)</th>
										<th class="bluebgheadtd">Email Address</th>
										<th class="bluebgheadtd">Add/Delete</th>
									</tr>
								      <tr id="nameRow" >
								        <td class="blueborderfortd" align="center">
										   <s:textfield name="newOwnerInfos[0].owner.aadhaarNumber" size="12" maxlength="12" value=""></s:textfield>
										</td>
								        <td class="blueborderfortd" align="center">
								        	<s:textfield name="newOwnerInfos[0].owner.name" maxlength="512" size="20" id="ownerName"  value="" 
								        		onblur="trim(this,this.value);checkSpecialCharForName(this);"/>
								        </td>
								        <td class="blueborderfortd" align="center">
								        	<s:select name="newOwnerInfos[0].owner.gender" list="@org.egov.infra.persistence.entity.enums.Gender@values()"></s:select>
								        </td>
								        <td class="blueborderfortd" align="center">
								        	<s:textfield name="newOwnerInfos[0].owner.mobileNumber" maxlength="10" size="20" id="mobileNumber"  value="" 
								        		onblur="validNumber(this);checkZero(this,'Mobile Number');"/>
								        </td>
								        <td class="blueborderfortd" align="center">
								        	<s:textfield name="newOwnerInfos[0].owner.emailId" maxlength="64" size="20" id="emailId"  value="" 
								        		onblur="trim(this,this.value);validateEmail(this);"/>
								        </td>
								        
								        <td class="blueborderfortd">
								        	<img id="addOwnerBtn" name="addOwnerBtn" src="${pageContext.request.contextPath}/resources/image/addrow.gif" onclick="javascript:addOwner(); return false;" alt="Add" width="18" height="18" border="0" />
								      		<img id="removeOwnerBtn" name="removeOwnerBtn" src="${pageContext.request.contextPath}/resources/image/removerow.gif" onclick="javascript:deleteOwner(this); return false;" alt="Remove" width="18" height="18" border="0" />
								        </td>
								     </tr>
								</table>
							</div>
							<br/>
						</td>
					</tr>

					<tr>
						<td class="greybox2">
							&nbsp;
						</td>
						<td class="greybox">
							<s:text name="transferreason"></s:text>
							<span class="mandatory1">*</span> :
						</td>
						<td class="greybox">
							<s:select name="basicProperty.propMutationSet[0].propMutationMstr" id="transRsnId"
								list="dropdownData.MutationReason" listKey="id"
								listValue="mutationName" headerKey="-1"
								headerValue="%{getText('default.select')}"
								value="%{propMutationMstr.id}"
								onchange="enableSaleDtls(this);" />
						</td>
						<td class="greybox">
							<s:text name="saleDetls" />
							<span class="mandatory1">*</span> :
						</td>
						<td class="greybox">
							<s:textarea cols="30" rows="2" name="basicProperty.propMutationSet[0].extraField3" id="saleDtls"
								onchange="return validateMaxLength(this);"
								onblur="trim(this,this.value);" value="%{extraField3}"></s:textarea>
						</td>
					</tr>
					
					<tr>
						<td class="greybox2">
							&nbsp;
						</td>
						<td class="greybox">
							<s:text name="docNum" /> :
						</td>
						<td class="greybox">
							<s:textfield name="basicProperty.propMutationSet[0].deedNo" id="docNum" value="%{basicProperty.propMutationSet[0].deedNo}" maxlength="64"/>
						</td>
						<td class="greybox">
							<s:text name="docDate" /> :
						</td>
						<td class="greybox">
							<s:date name="basicProperty.propMutationSet[0].deedDate" var="docDate" format="dd/MM/yyyy" />
							<s:textfield name="basicProperty.propMutationSet[0].deedDate" id="deedDate" maxlength="10"
								value="%{docDate}"
								onkeyup="DateFormat(this,this.value,event,false,'3')"
								onfocus="waterMarkTextIn('deedDate','DD/MM/YYYY');"
								onblur="validateDateFormat(this);waterMarkTextOut('deedDate','DD/MM/YYYY');"
								cssClass="datepicker" />
						</td>
					</tr>
					<tr>
						<td class="bluebox2">
							&nbsp;
						</td>
						<td class="bluebox">
							<s:text name="docValue" /> :
						</td>
						<td class="bluebox">
							<s:textfield name="txtdocval" id="txtdocval" maxlength="64"/>
						</td>
						<td class="bluebox">
							<s:text name="payablefee" /><span class="mandatory1">*</span> :
						</td>
						<td class="bluebox">
							<s:textfield name="txtfee" id="txtfee" />
						</td>
					</tr>
					
					<tr>
						<td colspan="5">
							<div class="headingsmallbg">
								<s:text name="docsectiontitle" /> 
							</div>
						</td>
					</tr>

                     <tr>
						<td colspan="5">
						<table class="tablebottom" id="nameTable" width="100%" border="0" cellpadding="0" cellspacing="0">
							<tbody>
								<tr>
									<th class="bluebgheadtd"><s:text name="doctable.docenclosed" /></th>
									<th class="bluebgheadtd"><s:text name="doctable.doctype" /></th>
									<th class="bluebgheadtd"><s:text name="doctable.docdate" /></th>
									<th class="bluebgheadtd"><s:text name="doctable.docdetails" /></th>
								</tr>
	
								<%-- <tr>
									<td class="blueborderfortd" align="center">
									  <s:checkbox name="docDetail[0].cbenclosed" id="docDetail[0].cbenclosed"/>
									</td>
									<td class="blueborderfortd" align="center">
									  <s:select name="docDetail[0].selectdoctype" id="docDetail[0].selectdoctype"
											list="#{'-1':'select', '1':'Document Type 1', '2':'Document Type 2'}"/>
									</td>
									<td class="blueborderfortd" align="center">
									  <s:textfield name="docDetail[0].txtdocdate" id="docDetail[0].txtdocdate" cssClass='datepicker' maxlength="10"/>
									</td>
									<td class="blueborderfortd" align="center">
										<textarea name="docDetail[0].tadocdetail" id="docDetail[0].tadocdetail" cols="40", rows="2"></textarea>
									</td>
	
								</tr>
	
	
								<tr>
									<td class="blueborderfortd" align="center">
									  <s:checkbox name="docDetail[1].cbenclosed" id="docDetail[1].cbenclosed"/>
									</td>
									<td class="blueborderfortd" align="center">
									  <s:select name="docDetail[1].selectdoctype" id="docDetail[1].selectdoctype"
											list="#{'-1':'select', '1':'Document Type 1', '2':'Document Type 2'}"/>
									</td>
									<td class="blueborderfortd" align="center">
									  <s:textfield name="docDetail[1].txtdocdate" id="docDetail[1].txtdocdate" cssClass='datepicker' maxlength="10"/>
									</td>
									<td class="blueborderfortd" align="center" style="width: 1--;">
										<textarea name="docDetail[1].tadocdetail" id="docDetail[1].tadocdetail" cols="40", rows="2"></textarea>
									</td>
	
								</tr>
								
								<tr>
									<td class="blueborderfortd" align="center">
									  <s:checkbox name="docDetail[2].cbenclosed" id="docDetail[2].cbenclosed"/>
									</td>
									<td class="blueborderfortd" align="center">
									  <s:select name="docDetail[2].selectdoctype" id="docDetail[2].selectdoctype"
											list="#{'-1':'select', '1':'Document Type 1', '2':'Document Type 2'}"/>
									</td>
									<td class="blueborderfortd" align="center">
									  <s:textfield name="docDetail[2].txtdocdate" id="docDetail[2].txtdocdate" cssClass='datepicker' maxlength="10"/>
									</td>
									<td class="blueborderfortd" align="center" style="width: 1--;">
										<textarea name="docDetail[2].tadocdetail" id="docDetail[2].tadocdetail" cols="40", rows="2"></textarea>
									</td>
	
								</tr>  --%>
	
	
							</tbody>
						</table>
					</td>
				 </tr>

				</table>
        		<%-- <%@ include file="../workflow/property-workflow.jsp" %>   --%>
       			 <div class="buttonbottom">
					<s:submit value="Save & Submit" id="Mutation:Forward" name="Transfer" cssClass="buttonsubmit" align="center" onclick="setWorkFlowInfo(this);resetDateFields();doLoadingMask();"></s:submit>
					<input type="reset" value="Cancel" class="button" align="center" />
					<input type="button" value="Close" class="button" align="center" onClick="return confirmClose();" />
				</div>
				</s:push>
			</s:form>
			<div align="left" class="mandatory1" style="font-size: 11px">
				* Mandatory Fields
			</div>
		</div>
		<script type="text/javascript">
		jQuery("#marketValue").blur(function(){
			var marketVal = parseInt(jQuery("#marketValue").val());
			jQuery("#mutationFee").val((1/100)*marketVal);
		});
		function enableSaleDtls(obj) {
			var selectedValue = obj.options[obj.selectedIndex].text;
			if(selectedValue=='<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@MUTATIONRS_SALES_DEED}" />') {
				document.getElementById("saleDtls").readOnly=false;
				document.getElementById("saleDtls").className="";
			}
			else {
				document.getElementById("saleDtls").value="";
				document.getElementById("saleDtls").className="hiddentext";
				document.getElementById("saleDtls").readOnly=true;
			}
			if(selectedValue=='<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@MUTATIONRS_COURT_ORDER}" />') {
				document.getElementById("crtOrderNum").readOnly=false;
				document.getElementById("crtOrderNum").className="";
			}
			else {
				document.getElementById("crtOrderNum").value="";
				document.getElementById("crtOrderNum").className="hiddentext";
				document.getElementById("crtOrderNum").readOnly=true;
			}
			if(selectedValue=='<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@MUTATIONRS_OTHERS}" />') { 
				document.getElementById("mutationRsnRow").style.display="";
			}
			else {
				document.getElementById("mutationRsnRow").style.display="none";
			}
		}
		function confirmClose(){
	 		var result = confirm("Do you want to close the window?");
	 		if(result==true){
	 			window.close();
	 			return true;
	 		}else{
	 			return false;
	 		}
	 	}		
		function enableBlock(){
	   		var obj=document.getElementById("transRsnId");
	 		if(obj!=null || obj!="undefined"){
	  			var selectedValue = obj.options[obj.selectedIndex].text;
	 			if(selectedValue=="SALE DEED") { 
					document.getElementById("saleDtls").readOnly=false;
					document.getElementById("saleDtls").className="";
				} else {
					document.getElementById("saleDtls").value="";
					document.getElementById("saleDtls").className="hiddentext";
					document.getElementById("saleDtls").readOnly=true;
				}
				if(selectedValue=="COURT ORDER") {
					document.getElementById("crtOrderNum").readOnly=false;
					document.getElementById("crtOrderNum").className="";
				} else {
					document.getElementById("crtOrderNum").value="";
					document.getElementById("crtOrderNum").className="hiddentext";
					document.getElementById("crtOrderNum").readOnly=true;
				}
			}
		}
		/**
		 * this resetting of date fields is performed because, the watermark(String) 
		 * is used in jsp and data type of the property is Date. So the default error messages are 
		 * displayed along with  
		 */
		function resetDateFields() {
			var applDate = document.getElementById("noticeDate").value;
			var deedDate = document.getElementById("deedDate").value;
			if (applDate == "DD/MM/YYYY") {
				document.getElementById("noticeDate").value = "";
			}
			if (deedDate == "DD/MM/YYYY") {
				document.getElementById("deedDate").value = "";
			}
		}
</script>
<div id="loadingMask" style="display:none"><p align="center"><img src="/egi/images/bar_loader.gif"> <span id="message"><p style="color: red">Please wait....</p></span></p></div>
</body>
</html>
