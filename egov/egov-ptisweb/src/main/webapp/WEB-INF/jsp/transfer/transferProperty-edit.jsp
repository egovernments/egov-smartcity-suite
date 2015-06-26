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
		<script type="text/javascript">
		jQuery.noConflict();
		jQuery("#loadingMask").remove();
	function loadOnStartUp() {
		document.getElementById("corrAddress1").className = "hiddentext";
		document.getElementById("corrAddress2").className = "hiddentext";
		document.getElementById("corrPinCode").className = "hiddentext";
		document.getElementById("saleDtls").className = "hiddentext";
		document.getElementById("crtOrderNum").className = "hiddentext";
		document.getElementById("corrAddress1").readOnly = true;
		document.getElementById("corrAddress2").readOnly = true;
		document.getElementById("corrPinCode").readOnly = true;
		document.getElementById("saleDtls").readOnly = true;
		document.getElementById("crtOrderNum").readOnly = true;
		enableCorresAddr();
		enableBlock();
		var applDate = document.getElementById("noticeDate").value;
		var deedDate = document.getElementById("deedDate").value;
		var mutDate = document.getElementById("mutationDate").value;

		if (applDate == "" || applDate == "DD/MM/YYYY" || applDate == undefined) {
			waterMarkInitialize('noticeDate', 'DD/MM/YYYY');
		}
		if (deedDate == "" || deedDate == "DD/MM/YYYY" || deedDate == undefined) {
			waterMarkInitialize('deedDate', 'DD/MM/YYYY');
		}
		if (mutDate == "" || mutDate == "DD/MM/YYYY" || mutDate == undefined) {
			waterMarkInitialize('mutationDate', 'DD/MM/YYYY');
		}

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
			<s:form  id="transferform" name="transferform" theme="simple" enctype="multipart/form-data">
				<s:push value="model">
				<s:hidden name="model.id"/>
				<s:hidden name="modelId" id="modelId" value="%{modelId}" />
				<s:hidden id="indexNumber" name="indexNumber" value="%{indexNumber}"></s:hidden>
				<s:hidden id="oldOwnerName" name="oldOwnerName" value="%{oldOwnerName}"></s:hidden>
				<s:hidden id="propAddress" name="propAddress" value="%{propAddress}"></s:hidden>
				<s:token/>
				<div class="headingbg">
					<s:text name="transferProperty" />
				</div>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="bluebox2" style="width:10%">
							&nbsp;
						</td>
						<td class="bluebox">
							<s:text name="prop.Id"></s:text>
						</td>
						<td class="bluebox">
							<s:property value="indexNumber" />
						</td>
						<td class="bluebox" colspan="2">
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="greybox2">
							&nbsp;
						</td>
						<td class="greybox">
							<s:text name="assesseeName"></s:text>
						</td>
						<td class="greybox">
							<s:property value="oldOwnerName" />
						</td>
						<td class="greybox" colspan="2">
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="bluebox2">
							&nbsp;
						</td>
						<td class="bluebox">
							<s:text name="PropertyAddress"></s:text>
						</td>
						<td class="bluebox">
							<s:property value="propAddress" />
						</td>
						<td class="bluebox" colspan="2">
							&nbsp;
						</td>
					</tr>
					<tr>
						<td colspan="6">
							<div class="headingsmallbg">
								<s:text name="transferDtls" />
							</div>
						</td>
					</tr>
					<tr>
						<td class="bluebox2">
							&nbsp;
						</td>
						<td class="bluebox">
							<s:text name="application.date"></s:text>
							<span class="mandatory">*</span> :
						</td>
						<td class="bluebox">
							<s:date name="noticeDate" var="applDate" format="dd/MM/yyyy" />
							<s:textfield name="noticeDate" id="noticeDate" maxlength="10"
								value="%{applDate}"
								onkeyup="DateFormat(this,this.value,event,false,'3')"
								onfocus="waterMarkTextIn('noticeDate','DD/MM/YYYY');"
								onblur="validateDateFormat(this);waterMarkTextOut('noticeDate','DD/MM/YYYY');" readonly="true" />
						</td>
						<td class="bluebox">
							<s:text name="applicant.name"/>
							<span class="mandatory">*</span> :
						</td>
						<td class="bluebox">
							<s:textfield name="applicantName" value="%{applicantName}" id="applicantName" readonly="true"/>
						</td>
					</tr>
					<tr>
						<td class="greybox2">
							&nbsp;
						</td>
						<td class="greybox">
							<s:text name="transferreason"></s:text>
							<span class="mandatory">*</span> :
						</td>
						<td class="greybox">
							<s:select name="propMutationMstr" id="transRsnId"
								list="dropdownData.MutationReason" listKey="id"
								listValue="mutationName" headerKey="-1"
								headerValue="%{getText('default.select')}"
								value="%{propMutationMstr.id}"
								onchange="enableSaleDtls(this);" />
						</td>
						<td class="greybox">
							<s:text name="saleDetls" />
							<span class="mandatory">*</span> :
						</td>
						<td class="greybox">
							<s:textarea cols="50" rows="2" name="extraField3" id="saleDtls"
								onchange="return validateMaxLength(this);"
								onblur="trim(this,this.value);" value="%{extraField3}"></s:textarea>
						</td>
					</tr>
					<tr id="mutationRsnRow">
						<td class="bluebox2" colspan="1">&nbsp;</td>
						<td class="bluebox" colspan="1">
							<s:text name="othertransreason"></s:text>
							<span class="mandatory">*</span>
						</td>
						<td class="bluebox">
							<s:textfield id="mutationRsn" name="extraField4" value="%{extraField4}" size="40" maxlength="128"></s:textfield>
						</td>
						<td class="bluebox" colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td class="bluebox2">
							&nbsp;
						</td>
						<td class="bluebox">
							<s:text name="subregoffName" />
							<span class="mandatory">*</span> :
						</td>
						<td class="bluebox">
							<s:textfield name="extraField2" id="subRegName"
								value="%{extraField2}" maxlength="256"/>
						</td>
						<td class="bluebox">
							<s:text name="crtOrderNum" />
							<span class="mandatory">*</span> :
						</td>
						<td class="bluebox">
							<s:textfield name="mutationNo" id="crtOrderNum"
								value="%{mutationNo}" maxlength="60"/>
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
							<s:textfield name="deedNo" id="docNum" value="%{deedNo}" maxlength="64"/>
						</td>
						<td class="greybox">
							<s:text name="docDate" /> :
						</td>
						<td class="greybox">
							<s:date name="deedDate" var="docDate" format="dd/MM/yyyy" />
							<s:textfield name="deedDate" id="deedDate" maxlength="10"
								value="%{docDate}" cssClass="datepicker"
								onkeyup="DateFormat(this,this.value,event,false,'3')"
								onfocus="waterMarkTextIn('deedDate','DD/MM/YYYY');"
								onblur="validateDateFormat(this);waterMarkTextOut('deedDate','DD/MM/YYYY');" />
						</td>
					</tr>
					<tr>
						<td class="bluebox2">
							&nbsp;
						</td>
						<td class="bluebox">
							Upload Document
							
						</td>
						<td class="bluebox">
							<ul>
							<c:forEach items="${property.basicProperty.propertyDocsSet}" var="propDocs">
								<li><a target="_blank" href="/egi/downloadfile?fileStoreId=${propDocs.supportDoc.fileStoreId}&moduleName=PTIS">${propDocs.supportDoc.fileName}</a></li>
							</c:forEach>
							</ul>
							<input type="file" name="upload" class="button" width="500px" value="Upload Document"/>
						</td>
						<td class="bluebox" colspan="2">
							&nbsp;
						</td>
					</tr>
					<tr>
						<td colspan="6">
							<div class="headingsmallbg">
								<s:text name="feeDtls" />
							</div>
						</td>
					</tr>
					<tr>
						<td class="bluebox2">&nbsp;</td>
						<td class="bluebox">
							<s:text name="mutationFee"></s:text>
							<span class="mandatory">*</span> :
						</td>
						<td class="bluebox">
							<s:textfield name="mutationFee" id="mutationFee" value="%{mutationFee}" maxlength="12" readonly="true"></s:textfield>
						</td>
						<td class="bluebox">
							<s:text name="otherFee"/> :
						</td>
						<td class="bluebox">
							<s:textfield name="otherFee" id="otherFee" value="%{otherFee}" maxlength="12" readonly="true"></s:textfield>
						</td>
					</tr>
					<tr>
						<td class="greybox2">&nbsp;</td>
						<td class="bluebox">
							<s:text name="marketValue"></s:text>
							<span class="mandatory">*</span> :
						</td>
						<td class="bluebox">
							<s:textfield name="marketValue" id="marketValue" value="%{marketValue}" maxlength="12" readonly="true"></s:textfield>
						</td>
						<td class="greybox">
							<s:text name="mutationDate"></s:text>
							<span class="mandatory">*</span> :
						</td>
						<td class="greybox">
							<s:date name="mutationDate" var="mutDate" format="dd/MM/yyyy" />
							<s:textfield name="mutationDate" id="mutationDate" value="%{mutDate}" maxlength="10"
								onkeyup="DateFormat(this,this.value,event,false,'3')"
								onfocus="waterMarkTextIn('mutationDate','DD/MM/YYYY');"
								onblur="validateDateFormat(this);waterMarkTextOut('mutationDate','DD/MM/YYYY');" cssClass="datepicker"/>
						</td>
						<td class="greybox" colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td colspan="6">
							<div class="headingsmallbg">
								<s:text name="ownerDtls" />
							</div>
						</td>
					</tr>
					<tr>
						<td class="bluebox" colspan="6">
							<div id="OwnerNameDiv">
								<%@ include file="../common/OwnerNameForm.jsp"%>
							</div>
						</td>
					</tr>
				</table>
				 <%@ include file="../common/CorrAddressForm.jsp"%>
       			 <%@ include file="../workflow/property-workflow.jsp" %>  
					<div class="buttonbottom">
						<input type="button" value="Forward" id="Mutation:Forward" name="forward" class="buttonsubmit submitbtn"/>
						<input type="button" value="Approve" id="Mutation:Approve" name="approve" class="buttonsubmit submitbtn"/>
						<input type="button" value="Reject" id="Mutation:Reject" name="reject" class="buttonsubmit submitbtn"/>
						<input type="button" value="Close" class="button"  onclick="return confirmClose();" />
					</div>
				</s:push>
			</s:form>
			<div align="left" class="mandatory" style="font-size: 11px">
				* Mandatory Fields
			</div>
		</div>
		<script type="text/javascript">
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
				if(selectedValue=="OTHERS") { 
					document.getElementById("mutationRsnRow").style.display="";
				} else {
					document.getElementById("mutationRsnRow").style.display="none";
				}
			}
		}
		function showDocumentManager(){
			var docNum= document.getElementById("docNumber").value;
  			var url;
  			if(docNum==null||docNum==''||docNum=='To be assigned'){
       			 url="/egi/docmgmt/basicDocumentManager.action?moduleName=ptis";
  			} else {
       			 url = "/egi/docmgmt/basicDocumentManager!editDocument.action?docNumber="+docNum+"&moduleName=ptis";
 		 	}
     		window.open(url,'docupload','width=1000,height=400');
		}

		/**
		 * this resetting of date fields is performed because, the watermark(String) 
		 * is used in jsp and data type of the property is Date. So the default error messages are 
		 * displayed along with  
		 */
		function resetDateFields() {
			var applDate = document.getElementById("noticeDate").value;
			var deedDate = document.getElementById("deedDate").value;
			var mutDate = document.getElementById("mutationDate").value;
			if (applDate == "DD/MM/YYYY") {
				document.getElementById("noticeDate").value = "";
			}
			if (deedDate == "DD/MM/YYYY") {
				document.getElementById("deedDate").value = "";
			}
			if (mutDate == "DD/MM/YYYY") {
				document.getElementById("mutationDate").value = "";
			}
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

		jQuery(".submitbtn").click(function(){
			setWorkFlowInfo(this);
			resetDateFields();
			//doLoadingMask();
			if(jQuery(this).attr("name") == 'approve') {
				jQuery("#transferform").attr("action","property-approve.action");
			} else if (jQuery(this).attr("name") == 'forward') {
				jQuery("#transferform").attr("action","property-forward.action");
			} else if (jQuery(this).attr("name") == 'reject') {
				jQuery("#transferform").attr("action","property-reject.action");
			}
			jQuery("#transferform").submit();
			
		});
</script>
<div id="loadingMask" style="display:none">
<p align="center"><img src="/egi/images/bar_loader.gif"> <span id="message"><p style="color: red">Please wait....</p></span></p>
</div>
</body>
</html>
