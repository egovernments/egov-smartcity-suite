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
<%@ include file="/includes/taglibs.jsp" %>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title><s:text name='NewProp.title'/></title>
<script type="text/javascript">
	function loadOnStartUp() {
   		setCorrCheckBox();
   		setPropCreatedDate();
	}
 function setCorrCheckBox(){
      <s:if test="%{isAddressCheck()}">
			document.getElementById("corrAddressDiff").checked=true;
	</s:if>
   }
   function setPropCreatedDate(){
     	var propDate=document.getElementById("basicProperty.propCreateDate").value;
     	document.getElementById("propCreateDate").innerHTML = propDate;
	document.getElementById("propertyCreatedDateDiv").style.display="none";

   }
   
   function validateForm(obj) {
 	var validation = true;
    document.getElementById("property_error_area").innerHTML="";
 		
 	validation=validateApprover(obj);
 	
 	if(validation==false){
		dom.get("property_error_area").style.display="block";
		window.scroll(0,0);
	}
 	return validation;
 }
 
 function generatenotice(){
 				var upicNo=document.getElementById("propertyId").value;
			    document.workflowform.action="../notice/propertyTaxNotice!generateNotice.action?upicNo="+upicNo;
				document.workflowform.submit();
			  }
			  
			  function generatePrativrutta(){
			  	var upicNo=document.getElementById("propertyId").value;
			  	window.open("../notice/propertyTaxNotice!generateNotice.action?upicNo="+upicNo+"&noticeType=Prativrutta","","resizable=yes,scrollbars=yes,top=40, width=900, height=650");
			  }
   
</script>
</head>
  
    <body onload="loadOnStartUp();">
  <div align="left">
  	<s:actionerror/>
  </div>
  <s:if test="%{hasActionMessages()}">
    <div id="actionMessages" class="messagestyle" align="center">
    	<s:actionmessage theme="simple"/>
    </div>
    <div class="blankspace">&nbsp;</div>
</s:if>
	<!-- Area for error display -->
	<div class="errorstyle" id="property_error_area" style="display:none;"></div>
  <s:form name="workflowform" action="workflow" theme="simple" validate="true">
  <s:push value="model">
  <s:hidden id="propertyId" name="propertyId" value='%{propertyId}'/> 
  <s:hidden label="noticeType" id="noticeType" name="noticeType" value="%{extra_field2}" />
  <div class="formmainbox">
  <div class="formheading"></div>
  		<div class="headingbg">CreatePropertyHeader</div>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<!--  have to add check for each activity - ModifyProperty, ChangeAddress, Transfer Property etc -->
		<s:if test="%{actionName=='Create'}" >
		<tr>
        	<%@ include file="../common/createPropertyView.jsp"%>  
        	
        	</tr>
        <s:if test="%{!documentTypes.isEmpty()}">
			<tr>
			   <td colspan="5">
				<%@ include file="../common/DocumentUploadView.jsp"%>
				</td>
			</tr>
		</s:if>
		
        </s:if>
 		<tr>
        	<%@ include file="../workflow/property-workflow.jsp" %>  
        	
        	</tr>
		
         <tr><font size="2"><div align="left" class="mandatory"><s:text name="mandtryFlds"/></div></font></tr>
        <div class="buttonbottom" align="center">
		<tr>
		<s:if test="%{!model.state.value.endsWith('NOTICE_GENERATION_PENDING')}">
			<td><s:submit value="Save" name="Save" id='%{actionName}:Save'  cssClass="buttonsubmit" method="save"/></td>
			
				<td><s:submit value="Forward" name="Forward" id='%{actionName}:Forward'  cssClass="buttonsubmit" method="forward" onclick="return validateForm(this);"/></td>
		</s:if>	
		<s:if test="%{model.state.value.endsWith('APPROVAL_PENDING')}">
		    <td><s:submit value="Approve" name="Approve" id='%{actionName}:Approve'  cssClass="buttonsubmit" method="approve" /></td>
		</s:if>
		<s:if test="%{model.state.value.endsWith('NOTICE_GENERATION_PENDING')}">
		        <s:if test="%{extra_field3!='Yes'}" >
					<input type="button" name="GenerateNotice" id="GenerateNotice" value="Generate Notice" class="button" onclick="return generatenotice()" />
				</s:if>
				
				<s:if test="%{extra_field4!='Yes'}" >
				<input type="button" name="GeneratePrativrutta" id="GeneratePrativrutta" value="Generate Prativrutta" class="button" onclick="return generatePrativrutta()" />
				</s:if>

		</s:if>
		    	<td><input type="button" name="button2" id="button2" value="Close" class="button" onclick="window.close();"/></td>
		</tr>    
		</div>              
		</table>
	</div>
  </s:push>
  </s:form>
  </body>
</html>
