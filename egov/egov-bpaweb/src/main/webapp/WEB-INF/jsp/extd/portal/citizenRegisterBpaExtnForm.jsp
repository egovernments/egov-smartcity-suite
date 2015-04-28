#-------------------------------------------------------------------------------
# <!-- #-------------------------------------------------------------------------------
# # eGov suite of products aim to improve the internal efficiency,transparency, 
# #    accountability and the service delivery of the government  organizations.
# # 
# #     Copyright (C) <2015>  eGovernments Foundation
# # 
# #     The updated version of eGov suite of products as by eGovernments Foundation 
# #     is available at http://www.egovernments.org
# # 
# #     This program is free software: you can redistribute it and/or modify
# #     it under the terms of the GNU General Public License as published by
# #     the Free Software Foundation, either version 3 of the License, or
# #     any later version.
# # 
# #     This program is distributed in the hope that it will be useful,
# #     but WITHOUT ANY WARRANTY; without even the implied warranty of
# #     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# #     GNU General Public License for more details.
# # 
# #     You should have received a copy of the GNU General Public License
# #     along with this program. If not, see http://www.gnu.org/licenses/ or 
# #     http://www.gnu.org/licenses/gpl.html .
# # 
# #     In addition to the terms of the GPL license to be adhered to in using this
# #     program, the following additional terms are to be complied with:
# # 
# # 	1) All versions of this program, verbatim or modified must carry this 
# # 	   Legal Notice.
# # 
# # 	2) Any misrepresentation of the origin of the material is prohibited. It 
# # 	   is required that all modified versions of this material be marked in 
# # 	   reasonable ways as different from the original version.
# # 
# # 	3) This license does not grant any rights to any user of the program 
# # 	   with regards to rights under trademark law for use of the trade names 
# # 	   or trademarks of eGovernments Foundation.
# # 
# #   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
# #------------------------------------------------------------------------------- -->
#-------------------------------------------------------------------------------
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>
<div>
<s:hidden name="idTemp" id="idTemp"  value="%{id}"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<s:if test="%{baNum!=null}">
		<tr>
			<td class="bluebox" width="30%">&nbsp;</td>
			<td class="bluebox"><s:text name="buildingApprovalOrderDate" /> : <span class="mandatory" >*</span></td>
			<td class="bluebox"><sj:datepicker value="%{baOrderDate}" id="baOrderDate" name="baOrderDate" displayFormat="dd/mm/yy" showOn="focus" disabled="true" /> 
		    <td class="bluebox" width="20%">&nbsp;</td>
	        <s:if test="%{baOrderDate!=null}">	     
		        <td class="bluebox"><s:text name="buildingApprovalNum" /> : <span class="mandatory" >*</span></td>
				<td class="bluebox" width="26%"><s:textfield size="33%" name="baNum" value="%{baNum}" id="baNum" readonly="true" /></td>
	         </s:if>
	         <s:else>       
		        <td class="bluebox" width="13%">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
			</s:else>
			<td class="bluebox">&nbsp;</td>
			<td class="bluebox">&nbsp;</td>							
		</tr>
		</s:if>
		<tr>
			<td class="greybox" width="13%">&nbsp;</td>
			 <s:if test="%{planPermitApprovalNum!=null}">
		        <td class="greybox"><s:text name="planPermitApprovalNumber" /> : <span class="mandatory" >*</span></td>
				<td class="greybox" width="26%"><s:textfield size="33%" name="planPermitApprovalNum" value="%{planPermitApprovalNum}" id="planPermitApprovalNum" readonly="true" /></td>
	        </s:if>
	         <s:else>       
		        <td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
				 <td class="greybox">&nbsp;</td>
			</s:else>
 	   			 <td class="greybox">&nbsp;</td>
				 <td class="greybox">&nbsp;</td>		
		</tr>
		
		<tr>
			<td class="bluebox" width="13%"></td>
			<td class="bluebox"><s:text name="applDate" />:</td>
			<td class="bluebox" width="26%"><s:date name="planSubmissionDate" format="dd/MM/yyyy"/></td>
			<td class="bluebox" style="display: none;">
			<s:date format="dd/MM/yyyy" name="planSubmissionDate" var="TempDate1"/>
				<s:textfield name="planSubmissionDate" id="planSubmissionDate"  maxlength="20" value="%{TempDate1}" readonly="true"  />
			</td>
	        <td class="bluebox" ></td>
	        <td class="bluebox" ><s:text name="serviceType" /> : </td>
			<td class="bluebox" width="26%"><s:property value="serviceType.code+'-'+serviceType.description"/> 
				<s:select headerKey="-1" cssStyle="display:none"
					headerValue="----choose-----" name="serviceType"
					id="serviceTypeId" listKey="id" listValue="code+'-'+description"
					list="dropdownData.serviceTypeList" value="%{serviceType.id}"
					cssClass="selectnew" onchange="onChangeOfServiceType();"/>   
			</td>
			<s:hidden id="isCmdaServicetype" name="isCmdaServicetype" value="%{isCmdaServicetype}" ></s:hidden>
			<s:hidden id="isAutoDcrMandatory" name="isAutoDcrMandatory" value="%{isAutoDcrMandatory}" ></s:hidden>
			<s:hidden id="isPropertyMandatory" name="isPropertyMandatory" value="%{isPropertyMandatory}" ></s:hidden>	
			<s:hidden id="isDocUplaodMendatory" name="isDocUplaodMendatory" value="%{isDocUplaodMendatory}" ></s:hidden>
			<td class="bluebox">&nbsp;</td>
			<td class="bluebox">&nbsp;</td>						
		</tr>
	
			   	
		<tr style="display: none;">
	        <td class="greybox" width="40%">&nbsp;</td>
			<td class="greybox" width="13%"><s:text name="applMode" /> : <span class="mandatory" >*</span></td>
			<td class="greybox"> <s:select  id="appMode" name="appMode" value="%{appMode}" 
			list="dropdownData.applicationModeList" listKey="code" listValue="code" headerKey="-1" 
	         headerValue="----choose-----"  />
			 </td>
	          <td class="greybox" width="20%">&nbsp;</td>
			<td class="greybox" width="13%"><s:text name="applType" /> : <span class="mandatory" >*</span></td>
			<td class="greybox"><s:select list="{'New'}" id="appType"
	         name="appType" value="%{appType}" headerKey="-1" 
	         headerValue="----choose-----"  />
	          </td>
	            <td class="greybox">&nbsp;</td>
	             <td class="greybox">&nbsp;</td>
		</tr>	
		 <s:if test="%{initialPlanSubmissionNum!=null}">
		  <tr>
	           	<td class="bluebox" width="13%">&nbsp;</td>
		        <td class="bluebox" ><s:text name="oldPlanSubmissionNum" /> : </td>
				<td class="bluebox" width="26%">
				<s:property value="initialPlanSubmissionNum"/> 
				</td>
	        	<td class="bluebox">&nbsp;</td>
	       		<td class="bluebox">&nbsp;</td>
	      		<td class="bluebox">&nbsp;</td>
       			<td class="bluebox">&nbsp;</td><td class="bluebox">&nbsp;</td>
       		</tr>	
		   </s:if>
		 <tr>
	        <s:if test="%{planSubmissionNum!=null}">
	         	<td class="bluebox" width="13%">&nbsp;</td>
		        <td class="bluebox" ><s:text name="planSubmissionNum" /> : <span class="mandatory" >*</span></td>
				<td class="bluebox" width="26%"><s:textfield size="33%" name="planSubmissionNum" value="%{planSubmissionNum}" id="planSubmissionNum" readonly="true" /></td>
	        	<td class="bluebox">&nbsp;</td>
	       		<td class="bluebox">&nbsp;</td>
	      		<td class="bluebox">&nbsp;</td>
       			<td class="bluebox">&nbsp;</td>
	        </s:if>
	        <s:else>  
	         	<td class="bluebox" width="13%">&nbsp;</td>
	        	<td class="bluebox">&nbsp;</td>
	       		<td class="bluebox">&nbsp;</td>
	       		<td class="bluebox">&nbsp;</td>
	       		<td class="bluebox">&nbsp;</td>
	       		<td class="bluebox">&nbsp;</td>
			</s:else>
			 	<td class="bluebox">&nbsp;</td>
			 	<td class="bluebox">&nbsp;</td>
    	</tr>
    </table>

	<div id="existingApplDtls">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<%@ include file="../common/existingApplicationExtnDetailsForm.jsp"%>
		</table>
	</div>
	       
	<div id="ownerDtls">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<%@ include file="../common/ownerDetailsExtnCitizenForm.jsp"%>
	</table>
	</div>

	  
	<div id="plotAddress"> 
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<%@ include file="../common/plotAddressExtnCitizen.jsp"%>
	</table>
	</div>

		      
	<div id="plotDtls">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<%@ include file="../common/plotDetailsExtn.jsp"%>
	</table>
	</div>
	<s:if test="%{userRole!='PORTALUSERSURVEYOR' || (userRole=='PORTALUSERSURVEYOR' && regServiceTypeCode!=null &&  regServiceTypeCode=='07')}">     
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td colspan="6"><div class="headingbg"><span class="bold"><s:text name="Other Information"/></span></div></td>
		</tr>
	</table>
	</s:if>

	<div id="cmdaDtls">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<%@ include file="../common/cmdaExtnDetails.jsp"%>
	</table>
    </div>   
     
   		 <div id="surveyorDiv">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="greybox" width="13%">&nbsp;</td>
							<td class="greybox" ><s:text name="Surveyor Code" /> : <span class="mandatory" >*</span></td>
							<td class="greybox" ><s:textfield
									id="surveyorCode" name="surveyorCode" value="%{surveyorCode}" onblur="splitValue(this,null,null);"/>
							<span class="bold">(OR)</span></td>
						<td class="greybox" >&nbsp;</td>
							<td class="greybox" ><s:text name="Surveyor Name" /> : <span class="mandatory" >*</span>
								</td>
							<td class="greybox" ><s:textfield
									id="surveyorNameLocal" name="surveyorNameLocal"
									value="%{surveyorNameLocal}" onblur="splitValue(null,this,null);" /><span class="bold">(OR)</span></td> 
							<td class="greybox" >&nbsp;</td>
							<td class="greybox"><s:text
									name="Surveyor Mobile No" /> : <span class="mandatory" >*</span></td>
							<td class="greybox" ><s:textfield
									id="surveyorMobNo" name="surveyorMobNo"
									value="%{surveyorMobNo}" onblur="splitValue(null,null,this);" /></td>
						</tr>
						<s:hidden name="Surveyor" id="Surveyor" value="%{Surveyor}" />
					</table>
					</div>
				
<div>
      <s:if test="%{!isUserMappedToSurveyorRole() && regServiceTypeCode!=null && (regServiceTypeCode=='01' || regServiceTypeCode=='03' || regServiceTypeCode=='06')}"> 
      	<table width="100%" border="0" cellspacing="0" cellpadding="0" id="admissionTbl" style="display: none;" >
      </s:if>
      <s:else>
      	 <table width="100%" border="0" cellspacing="0" cellpadding="0" id="admissionTbl" >
      </s:else>
		<tr>
			<td colspan="6"><div class="headingbg"><span class="bold"><s:text name="admissionFee"/></span></div></td>
		</tr>
		<tr>
			<td class="bluebox" width="13%">&nbsp;</td>
			<td class="bluebox" width="13%"><s:text name="admissionfeeAmount" /> :</td>
			<td class="bluebox"><s:textfield id="admissionfeeAmount" name="admissionfeeAmount" value="%{admissionfeeAmount}"  readonly="true"  /></td>
			<td class="bluebox">&nbsp;</td>
			<td class="bluebox">&nbsp;</td>
			<td class="bluebox">&nbsp;</td>
		</tr>   
	</table>
	
	<s:if test="%{!isUserMappedToSurveyorRole()}"> 
	<div>
		<sj:dialog 
	    	id="rules" 
	    	autoOpen="false" 
	    	modal="true" 
	    	title="Terms & Conditions"
	    	openTopics="openRemoteDialog"
	    	height="300"
	    	width="500"
	    	requiredposition="left"
	    	zindex=""
	    	dialogClass="formmainbox"
	    	showEffect="slide" 
	    	hideEffect="slide" 
	    	onOpenTopics="openTopicDialog" cssStyle="BACKGROUND-COLOR: #ffffff"
	    	onCompleteTopics="dialogopentopic" 
	    	loadingText="Please Wait ...."
	    	errorText="Permission Denied"
	    />
	    <br></br>
	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	       		<tr>
	       			<s:url id="conditionsLink" value="/extd/portal/citizenRegisterBpaExtn!showTermsandConditions.action" escapeAmp="false">
				        </s:url> 
					
					<td class="greybox"><div  class="subheadsmallnew" style="background-color: #fffff0; border-bottom:1px #cccccc;">
						<s:checkbox name="termsCondition" id="termsCondition" value="%{termsCondition}"/>
						<sj:a  onClickTopics="openRemoteDialog" href="%{conditionsLink}" button="true" buttonIcon="ui-icon-newwin"><s:text name="citizen.termsCondition.lbl" /></sj:a></div></td>
					</div></td>
				</tr>
			</table>
	</div> 
	       
	</s:if>
	
</div>		
</td></tr></table>
</div>	

 
<script>
function onChangeOfServiceType(){
	if( jQuery('#mode').val()!='edit'){

		if( jQuery('#mode').val()!='noEdit'){
			
		if( jQuery('#mode').val()!='view' && jQuery('#mode').val()!='editForCitizen'){	
			jQuery("#checklists").children().remove(); 
			getData();
		}
		}
	}
	if( jQuery('#mode').val()=='edit'){	 
		
	 jQuery('#showjsp').hide();
	 jQuery('#hidejsp').show();
	}
	callAdmissionFeeAmount();
	getMandatoryFieldsForServiceType();
	hideShowBuildingCategory();
	hideShowCMDAfields();
	
}

function enableBoundaryData() {
	document.getElementById("Zone").disabled=false;
	document.getElementById("Ward").disabled=false;
	document.getElementById("Area").disabled=false;
	document.getElementById("Locality").disabled=false;
	document.getElementById("Street").disabled=false;
	document.getElementById("ownerFirstname").disabled=false;
}
function getMandatoryFieldsForServiceType(){

	var servicetypeid=jQuery("#serviceTypeId").val();
	jQuery.ajax({
	    url:"<%=request.getContextPath()%>/extd/common/ajaxExtnCommon!ajaxGetMandatoryFieldsForServiceType.action",
	     data: { serviceTypeId:servicetypeid }, 
	      type: "POST",
	     	dataType: "html",
	   	 success: function(data){
	   	 	var flags = data.split("-");
		   	var cmdaFlag = flags[0];
		   	var autoDcrFlag = flags[1];
			var propertyFlag=flags[2];
			var docUpload=flags[3];
		   	document.getElementById('isCmdaServicetype').value=cmdaFlag;
		 	document.getElementById('isAutoDcrMandatory').value=autoDcrFlag;
		 	document.getElementById('isPropertyMandatory').value=propertyFlag;
		 	document.getElementById('isDocUplaodMendatory').value=docUpload;		    
	    }
	});
}

function hideShowBuildingCategory(){
	 var serviceTypeValue= document.getElementById('serviceTypeId').options[document.getElementById('serviceTypeId').selectedIndex].text;
		//alert("serviceTypeValue "+serviceTypeValue);
		if(serviceTypeValue.indexOf("04")!=-1 ||serviceTypeValue.indexOf("05")!=-1  ){
			jQuery('#buildingCategory').hide();			
		}		
		else{
			jQuery('#buildingCategory').show();				
		}
}

function hideShowCMDAfields(){
	 var serviceTypeValue= document.getElementById('serviceTypeId').options[document.getElementById('serviceTypeId').selectedIndex].text;
		if(serviceTypeValue.indexOf("07")!=-1  ){
			jQuery('#cmdaDtls').show();			
		}		
		else{
			jQuery('#cmdaDtls').hide();				
		}
}
/*
 * To Call Ajax for Exist PPA Number Onchange validation
 */
function checkValidPpaNumber(){
	var plannumber=jQuery("#existingPPANum").val();
	
	jQuery.ajax({
		
	    url:"${pageContext.request.contextPath}/extd/register/registerBpaExtn!existPPAnumberforRegistration.action",
	    data: { existPpaNumber:plannumber }, 
	      type: "POST", 
	     dataType: "json",
	    success: function(data){
	    	
	if( data.ResultSet.Result[0].value=="success"){
		
	    jQuery("#existingPPANum").append(data);
	    document.getElementById("bpa_error_area").innerHTML ="";
	    dom.get("bpa_error_area").style.display = 'none';
	}
	else{
		document.getElementById('existingPPANum').value="";
		//alert(data.ResultSet.Result[0].message);
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML =data.ResultSet.Result[0].message;
		
		}
		
	       },
	       error: function (data) {
	    	 alert("Oops something went wrong , Please try again or contact system administrator")
	         }
	    
	    });  

	
}/*
 * To Call Ajax for Exist BA Number Onchange validation
 */
function checkValidBaNumber(){
	var plannumber=jQuery("#existingBANum").val();
	
	jQuery.ajax({
	    url:"${pageContext.request.contextPath}/extd/register/registerBpaExtn!existBanumberCheckForAjax.action",
	    data: { existBaNumber:plannumber }, 
	      type: "POST",
	     dataType: "json",
	    success: function(data){
	    	 
	    	if( data.ResultSet.Result[0].value=="success"){
	    		 jQuery("#existingBANum").append(data);
	    	document.getElementById("bpa_error_area").innerHTML ="";
	    	dom.get("bpa_error_area").style.display = 'none';
	    	}
	    	else{
	    		document.getElementById('existingBANum').value="";
	    		dom.get("bpa_error_area").style.display = '';
	    		document.getElementById("bpa_error_area").innerHTML =data.ResultSet.Result[0].message;
	    		
	    	}
	      }
	     
	 	
	    });  
	
}
function getData(){
	
	var servicetypeid=jQuery("#serviceTypeId").val();
	jQuery.ajax({
	    url:"${pageContext.request.contextPath}/extd/register/registerBpaExtn!showCheckList.action",
	     data: { serviceTypeIdTemp:servicetypeid }, 
	      type: "POST",
	     dataType: "html",
	    success: function(data){
	    jQuery("#regnchecklist").children().remove();  
	    jQuery("#regnchecklist").append(data);
	       
	    }
	    });  
	
}


function callAdmissionFeeAmount(){
	document.getElementById('admissionfeeAmount').value="";
	var serviceTypeId=document.getElementById('serviceTypeId').value;
 	var sitalAreaInSqmt=document.getElementById('plotAreaInSqmt').value;

 	if(serviceTypeId!=null && serviceTypeId!="" && serviceTypeId!="-1" && sitalAreaInSqmt!=null && sitalAreaInSqmt!=""){
 	 	
		var url = "${pageContext.request.contextPath}/extd/common/ajaxExtnCommon!ajaxGetAdmissionFeeAmount.action?serviceTypeId="+serviceTypeId+'&sitalAreaInSqmt='+sitalAreaInSqmt;
		var req = initiateRequest();
		req.open("GET", url, false);
		req.send(null);
		if (req.readyState == 4)
		{
			if (req.status == 200)
			{
				 var responseString =req.responseText;  			     			
				 feeAmount=responseString; 
				 document.getElementById('admissionfeeAmount').value=feeAmount;
			}
		}	

 	}
}

jQuery('#appType').change(function(){
	if(jQuery(this).val()=="Revised"){
		jQuery('#existingApplDtls0').show();
		
	}
	else if(jQuery(this).val()=="New" || jQuery(this).val()==-1 ){
		document.getElementById('existingPPANum').value="" ;
		document.getElementById('existingBANum').value="" ;
		jQuery('#existingApplDtls0').hide();
	}
});

function hideExistingApplDetails(){
	 var appTypeeValue= document.getElementById('appType').options[document.getElementById('appType').selectedIndex].text;
		if(appTypeeValue=="Revised"  ){
			
			jQuery('#existingApplDtls0').show();
				
		}		
		else{
			document.getElementById('existingPPANum').value="" ;
			document.getElementById('existingBANum').value="" ;
			jQuery('#existingApplDtls0').hide();
		}
	
}

function validateArea(obj,labelname){

	return checkNumbers(obj.value,'bpa_error_area',labelname,obj);
}

</script> 

