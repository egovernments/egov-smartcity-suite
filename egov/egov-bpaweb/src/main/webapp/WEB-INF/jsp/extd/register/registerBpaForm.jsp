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

	<tr>
		<td class="bluebox" width="13%">&nbsp;</td>
		<s:if test="%{baNum!=null}">
		    <td class="bluebox"  ><s:text name="buildingApprovalOrderDate" /> : </td>
			<td class="bluebox" style="font-weight:bold;font-size:13px" width="26%">
			<s:date name="baOrderDate" format="dd/MM/yyyy" />
						
							</td>
				<td style="display: none;"> 
			<sj:datepicker value="%{baOrderDate}" id="baOrderDate" name="baOrderDate" displayFormat="dd/mm/yy" showOn="focus" disabled="true" /></td>
	     </s:if>
         <s:else>       
	        <td class="bluebox">&nbsp;</td>
			<td class="bluebox">&nbsp;</td>
		</s:else>
        <td class="bluebox" >&nbsp;</td>
        <s:if test="%{baOrderDate!=null}">	     
	        <td class="bluebox" ><s:text name="buildingApprovalNum" /> : </td>
			<td class="bluebox" style="font-weight:bold;font-size:13px">
						<s:property value="%{baNum}"/> </td>
				<td style="display: none;">
			<s:textfield size="33%" name="baNum" value="%{baNum}" id="baNum" readonly="true" /></td>
         </s:if>
         <s:else>       
	        <td class="bluebox">&nbsp;</td>
			<td class="bluebox">&nbsp;</td>
		</s:else>
		<td class="bluebox">&nbsp;</td>
				  <td class="bluebox">&nbsp;</td>							
	</tr>

	<tr>
		<td class="greybox" width="13%">&nbsp;</td>
		
        <s:if test="%{planPermitApprovalNum!=null}">
	        <td class="greybox" ><s:text name="planPermitApprovalNumber" /> : </td>
			<td class="greybox" style="font-weight:bold;font-size:13px">	<s:property value="%{planPermitApprovalNum}"/> 
						</td>
				<td style="display: none;"> 
			<s:textfield size="20%" name="planPermitApprovalNum" value="%{planPermitApprovalNum}" id="planPermitApprovalNum" readonly="true" />
			</td>
        </s:if>
         <s:else>       
	        <td class="greybox">&nbsp;</td>
			<td class="greybox">&nbsp;</td>
		</s:else>
 	   			 <td class="greybox">&nbsp;</td>
				  <td class="greybox">&nbsp;</td>
				  <td class="greybox">&nbsp;</td>
				  <td class="greybox">&nbsp;</td>
				   <td class="greybox">&nbsp;</td>		
	</tr>

	<tr>
		<td class="bluebox" width="13%">&nbsp;</td>
		<td class="bluebox"  ><s:text name="applDate" />:</td>
		<td class="bluebox" style="font-weight:bold;font-size:13px">   	
		<s:date name="planSubmissionDate" format="dd/MM/yyyy" />
					
				</td>
				<td style="display: none;">			
        <sj:datepicker value="%{planSubmissionDate}" id="planSubmissionDate" name="planSubmissionDate" readonly="true" displayFormat="dd/mm/yy" showOn="focus"/>
		</td>
        <td class="bluebox" >&nbsp;</td>
        <td class="bluebox"  ><s:text name="serviceType" /> : </td>
		<td class="bluebox" style="font-weight:bold;font-size:13px">
		
						<s:property value="serviceType.code+'-'+serviceType.description"/> </td>
			<td style="display: none;"> 		
		 <s:select headerKey="-1"
				headerValue="----choose-----" name="serviceType"
				id="serviceTypeId" listKey="id" listValue="code+'-'+description"
				list="dropdownData.serviceTypeList" value="%{serviceType.id}"
				cssClass="selectnew" onchange="onChangeOfServiceType();"/> </td>
				<s:hidden id="isCmdaServicetype" name="isCmdaServicetype" value="%{isCmdaServicetype}" ></s:hidden>
				<s:hidden id="isAutoDcrMandatory" name="isAutoDcrMandatory" value="%{isAutoDcrMandatory}" ></s:hidden>
				<s:hidden id="isPropertyMandatory" name="isPropertyMandatory" value="%{isPropertyMandatory}" ></s:hidden>	
				  <td class="bluebox">&nbsp;</td>
				  <td class="bluebox">&nbsp;</td>						
	</tr>
   <tr >
        <td class="greybox" width="13%">&nbsp;</td>
		<td class="greybox"  ><s:text name="applMode" /> : </td>
		<td class="greybox" style="font-weight:bold;font-size:13px"> 
		
							<s:property value="%{appMode}"/>
							</td>
				<td style="display: none;">
		<s:select  id="appMode" name="appMode" value="%{appMode}" 
		list="dropdownData.applicationModeList" listKey="code" listValue="code" headerKey="-1" 
         headerValue="----choose-----"  />
		 </td>
          <td class="greybox" >&nbsp;</td>
		<td class="greybox"   width="13%"><s:text name="applType" /> :</td>
		<td class="greybox" style="font-weight:bold;font-size:13px">
							<s:property value="%{appType}"/>
							</td>
				<td style="display: none;">
		<s:select list="{'New','Revised'}" id="appType"
         name="appType" value="%{appType}" headerKey="-1" 
         headerValue="----choose-----"  />
          </td>
            <td class="greybox">&nbsp;</td>
             <td class="greybox">&nbsp;</td>
    </tr>
	  <tr>
	 
        <s:if test="%{planSubmissionNum!=null}">
         <td class="bluebox" width="13%">&nbsp;</td>
	        <td class="bluebox"   ><s:text name="planSubmissionNum" /> : </td>
			<td class="bluebox"style="font-weight:bold;font-size:13px">		<s:property value="%{planSubmissionNum}"/>
							</td>
				<td style="display: none;">
			<s:textfield size="33%" name="planSubmissionNum" value="%{planSubmissionNum}" id="planSubmissionNum" readonly="true" />
			</td>
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

	<!--  <div id="existingApplDtls">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<%@ include file="../common/existingApplicationExtnDetailsForm.jsp"%>
	</table>
	</div>-->
	
    <table width="100%" border="0" cellspacing="0" cellpadding="0">  
	<tr>
		<td class="greybox" width="13%">&nbsp;</td>
		<td class="greybox" width="13%"   ><s:text name="propertyId" /> :</td>
		<td class="greybox" width="25%" style="font-weight:bold;font-size:13px">
		
							<s:property value="%{propertyid}"/>
						</td>
				<td style="display: none;"> 
			<s:textfield id="propertyid" name="propertyid" value="%{propertyid}" readonly="true" />
			<s:if test="%{mode!='view'}"> 
					<a id="propertyAtag" href="javascript:openSearchScreen();">Search Property</a>
			</s:if>
			<s:else>&nbsp;</s:else>
		</td>
		<td class="greybox">&nbsp;</td>
		<td class="greybox" width="20%"   ><s:text name="autoDcrNum" /> :</td>
		<td class="greybox" style="font-weight:bold;font-size:13px">
		<s:property value="%{autoDcrNum}"/>
		</td>
				<td style="display: none;"> <s:textfield id="autoDcrNum" name="autoDcrNum" value="%{autoDcrNum}" readonly="true" />
		<s:if test="%{mode!='view'}"> 
		<a id="autodcrAtag1" href="#"><img src="${pageContext.request.contextPath}/images/searchicon.gif" onclick="openSearchAutoDcr();"></a>
     	<a id="autodcrAtag2" href="#"><img src="${pageContext.request.contextPath}/images/refresh.gif" onclick="ResetAutoDcr()"></a>
		</s:if>
	
		</td>
	<td class="greybox">&nbsp;</td>
	</tr>
	<tr>
	 <td class="bluebox" width="13%">&nbsp;</td>
	       
        <td class="bluebox">&nbsp;</td>
       <td class="bluebox">&nbsp;</td>
       <td class="bluebox">&nbsp;</td>
       <td class="bluebox">&nbsp;</td>
        
        <td class="bluebox">&nbsp;</td>
       <td class="bluebox">&nbsp;</td>
       <td class="bluebox">&nbsp;</td>
       <td class="bluebox">&nbsp;</td>
       <td class="bluebox">&nbsp;</td>
		 <td class="bluebox">&nbsp;</td>
    </tr>
	<tr>								
		<egov:loadBoundaryData adminboundaryid="${adminboundaryid.id}"
		locboundaryid="${locboundaryid.id}" adminBndryVarId="adminboundaryid" locBndryVarId="locboundaryid" />
		<s:hidden  id= "adminboundaryid" name="adminboundaryid" value="%{adminboundaryid.id}" />
		<s:hidden  id= "locboundaryid"  name="locboundaryid" value="%{locboundaryid.id}" />	
	</tr>       
    </table> 
       
	<div id="ownerDtls">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<%@ include file="../common/ownerDetailsExtnForm.jsp"%>
	</table>
	</div>

	  
	<div id="plotAddress">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<%@ include file="../common/plotAddressExtn.jsp"%>
	</table>
	</div>

		      
	<div id="plotDtls">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<%@ include file="../common/plotDetailsExtn.jsp"%>
	</table>
	</div>

	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td colspan="6"><div class="headingbg"><span class="bold"><s:text name="Other Information"/></span></div></td>
		</tr>
	</table>
	
	<div id="cmdaDtls">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
		<td class="bluebox" width="13%">&nbsp;</td>
		<td class="bluebox" width="13%"   ><s:text name="cmdaProposalNum" /> :<span class="mandatory" >*</span></td>
        <td class="bluebox" width="26%" style="font-weight:bold;font-size:13px"><s:property value="%{cmdaNum}"/></td>
        <td style="display: none;"> <s:textfield id="cmdaProposalNumber" name="cmdaNum" value="%{cmdaNum}"/></td>
        <td class="bluebox">&nbsp;</td>
        <td class="bluebox" width="20%"   ><s:text name="cmdaRefDate" /> :<span class="mandatory" >*</span></td>
        <td class="bluebox" style="font-weight:bold;font-size:13px">
        <s:date name="cmdaRefDate" format="dd/MM/yyyy" />
						
							</td>
				<td style="display: none;"> 
      <sj:datepicker value="%{cmdaRefDate}" id="cmdaRefDate" name="cmdaRefDate" displayFormat="dd/mm/yy" showOn="focus" readonly="true"/></td>
	
	</tr> 
	</table>
    </div>   
      
    <div id="surveyorDiv">
      
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
					 <tr style="display: none;">
    <td class="greybox"> <s:hidden  id="surveyor" name="surveyor" value="%{surveyor}" />
		 </td>
   </tr>
					<tr>
						<td class="bluebox" width="13%">&nbsp;</td>
							<td class="bluebox"    width="13%"><s:text
									name="Surveyor Class" />: </td>
							<td class="bluebox" width="26%"style="font-weight:bold;font-size:13px">
							
							<s:property value="surveyorClass"/>
						</td>
				<td style="display: none;"> 
							<s:textfield
									id="surveyorClass" name="surveyorClass"
									value="%{surveyorClass}" readonly="true" />
								</td>
							<td class="bluebox">&nbsp;</td>
							<td class="bluebox">&nbsp;</td>
							<td class="bluebox">&nbsp;</td>
							<td class="bluebox">&nbsp;</td>

						</tr>
					<tr>
							<td class="greybox" width="13%">&nbsp;</td>
							<td class="greybox" width="13%"   ><s:text name="Surveyor Name" />:
								</td>
							<td class="greybox" width="26%" style="font-weight:bold;font-size:13px">
							
							<s:property value="%{surveyorNameLocal}"/></td>
				<td style="display: none;"> 
							<s:textfield
									id="surveyorNameLocal" name="surveyorNameLocal"
									value="%{surveyorNameLocal}" readonly="true" /></td>
							<td class="greybox" width="13%"   ><s:text name="Surveyor Code" />:
								</td>
							<td class="greybox" width="20%" style="font-weight:bold;font-size:13px">
							
			<s:property value="%{surveyorCode}" /></td>
				<td style="display: none;"> <s:textfield
									id="surveyorCode" name="surveyorCode" value="%{surveyorCode}" readonly="true"
									 /></td>
						</tr>
						

					</table>
				</div>
				  <table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="greybox" width="13%">&nbsp;</td>
						<td class="greybox"   width="13%"><s:text name="remarks" /> :</td>
						<td class="greybox" style="font-weight:bold;font-size:13px">
						
						<s:property value="%{regnDetails.remarks}"/>
							</td>
				<td style="display: none;"> 
								<s:textarea cols="50" rows="2"
								id="regnDetails.remarks" name="regnDetails.remarks"
								value="%{regnDetails.remarks}" onblur="filterAddress(this);"/>
								
								</td>
						<td class="greybox">&nbsp;</td>
						<td class="greybox">&nbsp;</td>
						<td class="greybox">&nbsp;</td>
						<td class="greybox">&nbsp;</td>
					</tr>
				</table>

<div>

	
    
      
      <table width="100%" border="0" cellspacing="0" cellpadding="0" id="admissionTbl">
		<tr>
		
			<td colspan="6"><div class="headingbg"><span class="bold"><s:text name="admissionFee"/></span></div></td>
		</tr>
		<tr>
			<td class="bluebox" width="13%">&nbsp;</td>
			<td class="bluebox"    width="13%"><s:text name="admissionfeeAmount" /> :</td>
			<td class="bluebox" style="font-weight:bold;font-size:13px">
			
			<s:property value="%{admissionfeeAmount}" />
			</td>
				<td style="display: none;" > 
			<s:textfield id="admissionfeeAmount" name="admissionfeeAmount" value="%{admissionfeeAmount}"  readonly="true"  />
		
			</td>
			<td class="bluebox">&nbsp;</td>
			<td class="bluebox">&nbsp;</td>
			<td class="bluebox">&nbsp;</td>
		</tr>   
	</table>
	<s:if test="%{mode!=null}">
	<sj:dialog 
    	id="ordDet" 
    	autoOpen="false" 
    	modal="true" 
    	title="Order Details"
    	openTopics="openOrdDetailsDialog"
    	height="500"
    	width="700"
    	dialogClass="formmainbox"
    	showEffect="slide" 
    	hideEffect="slide" 
    	onOpenTopics="openTopicDialog" cssStyle="BACKGROUND-COLOR: #ffffff"
    	onCompleteTopics="dialogopentopic" 
    	loadingText="Please Wait...."  
    	errorText="Permission denied."
    />
    
	<table width="100%" border="0" cellspacing="0" cellpadding="0" id="ordTbl">
	
		<tr>
			   <s:url id="orderlink" value="/extd/register/registerBpaExtn!showOrderDetails.action" escapeAmp="false">
			       <s:param name="registrationId" value="%{id}"></s:param>	
			   </s:url> 
			  	<td  class="greybox">
			  		<div align="center"> 
			  			<sj:a  onClickTopics="openOrdDetailsDialog" href="%{orderlink}" button="true" buttonIcon="ui-icon-newwin">
			  				View Order Details
			  			</sj:a>
			  		</div>
			  	</td>
		</tr>
	</table>
	</s:if>
	<s:if test="%{mode!=null}">
		<sj:dialog 
    	id="viewReceipt" 
    	autoOpen="false" 
    	modal="true" 
    	title="View Receipt Collection Details"
    	openTopics="openViewReceiptDialog"
    	height="500"
    	width="700"
    	dialogClass="formmainbox"
    	showEffect="slide" 
    	hideEffect="slide" 
    	loadingText="Please Wait ...."
    	errorText="Permission denied."
    	onOpenTopics="openTopicDialog" cssStyle="BACKGROUND-COLOR: #ffffff"
    	onCompleteTopics="dialogopentopic"  >
     <img id="indicator" src="${pageContext.request.contextPath}/images/loading.gif" alt="Loading..."/>
     </sj:dialog> 
	 
	<table width="100%" border="0" cellspacing="0" cellpadding="0" id="receiptTbl">
	
		<tr>
			   <s:url id="receiptlink" value="/extd/register/registerBpaExtn!getCollectedReceipts.action" escapeAmp="false">
			       <s:param name="registrationId" value="%{id}"></s:param>	
			   </s:url> 
			  	<td  class="bluebox">
			  		<div align="center"> 
			  			<sj:a  onClickTopics="openViewReceiptDialog" href="%{receiptlink}" button="true" buttonIcon="ui-icon-newwin">
			  				View Receipt Collection Details
			  				
			  			</sj:a>
			  		</div>
			  	</td>
		</tr>
	</table>
	</s:if>
</div>		
</td></tr></table>
</div>	

 
<script>


function callfeedetails(){

//jQuery('#reloadFeeLink').click();
//jQuery('#reloadactionsLink').click();
window.location.reload();

}


function callinspectionmeasurementdetails(){

//jQuery('#reloadLink').click();
//jQuery('#reloadactionsLink').click();
 window.location.reload();

}

function callinspectiondetails(fixeddate){
 //jQuery('#reloadactionsLink').click();
 //jQuery("#inspectionFixedDate").val(fixeddate);
 window.location.reload();

}

function callLetterToPartyDetails(){

	//jQuery('#reloadLink').click();
	//jQuery('#reloadactionsLink').click();
	 window.location.reload();

	}

function openaction(obj){
	
	alert(""+ obj);
	var type=obj;
	if(type=='Add SiteInspection'||type=='Add New SiteInspection'){
	addInspection();
	}
	else if(type=='Add InspectionDate'){
	
	addInspectiondate();
	}
	
	else if(type=='Modify InspectionDate'){
	addInspectiondate();
	}
	
	else if(type=='Add FeeDetails'){
	addFeeDetails();
	}
	
	else if(type=='Modify FeeDetails'){
	modifyFeeDetails();
	}
	else if(type=='Add/Modify Approval Information'){
		addApproveInfo();
		}
	else if(type=='Create Letter To Party'){
		createLetterToParty();
	}
	
	else if(type=='Reject Unconsidered'){
		createRejectionForm();
		}
	else if(type=='Update Signature') {
		createOrdDetForm(type);
	}
	else if(type=='Order Prepared') {
		createOrdDetForm(type);
	}
	/*else if(type=='Order Issued') {
		createOrdDetForm(type);
	}*/
	else if(type=='Unconsidered Order Prepared') {
		createOrdDetForm(type);
	}
	else if(type=='Unconsidered Order Issued') {
		createOrdDetForm(type);
	}
	else if(type=='Add Challan Sent Date') {
		createOrdDetForm(type);
	}
	else if(type=='Print Building Permit') {
		printPermit('BuildingPermit');
	}
	else if(type=='Print Plan Permit') {
		printPermit('PlanPermit');
	}
	else if(type=='Print Rejection Notice') {
		printRejection();
	}
	else if(type=='Print Unconsidered Notice') {
		printRejection();
	}
	else if(type=='Building Measurement Update') {
		updateBldgMeasurement();
	}
	else if(type=='Capture DD details') {
	
		captureDDdetails();
	}
}

function addFeeDetails(){
	
	var regid=jQuery("#id").val();	
	document.location.href="${pageContext.request.contextPath}/extd/fee/feeDetailsExtn!newForm.action?registrationId="+regid+"&fromreg=true","simple","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes";
	//window.open("${pageContext.request.contextPath}/fee/feeDetails!newForm.action?registrationId="+regid+"&fromreg=true","simple","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		//window.opener=self;
	//window.close();
		//jQuery("input[name=actionsbutton]").click();	
			}
			
			
	function addInspection(){
	
	var regid=jQuery("#id").val();	
	
	//window.open("${pageContext.request.contextPath}/inspection/inspection!inspectionForm.action?registrationId="+regid+"&fromreg=true","simple","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
	//window.opener=self;
	//window.close();
	document.location.href="${pageContext.request.contextPath}/extd/inspection/inspectionExtn!inspectionForm.action?registrationId="+regid+"&fromreg=true","simple","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes";
	//jQuery("input[name=actionsbutton]").click();
			}

	function addApproveInfo(){
		
		var regid=jQuery("#id").val();	
		document.location.href="${pageContext.request.contextPath}/extd/approve/approvalInformationExtn!newForm.action?registrationId="+regid+"&fromreg=true","simple","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes";
				}
	
function addInspectiondate(){

	var regid=jQuery("#id").val();
	document.location.href="${pageContext.request.contextPath}/extd/inspection/inspectionExtn!newForm.action?registrationId="+regid+"&fromreg=true","simple","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes";
	//window.open("${pageContext.request.contextPath}/inspection/inspection!newForm.action?registrationId="+regid+"&fromreg=true","simple","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
	//window.opener=self;
	//window.close();
	//jQuery("input[name=actionsbutton]").click();
	}
			
	
function modifyFeeDetails(){

 
	var regid=jQuery("#id").val();	
	//window.open("${pageContext.request.contextPath}/fee/feeDetails!modify.action?registrationId="+regid+"&fromreg=true","simple","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
	document.location.href="${pageContext.request.contextPath}/extd/fee/feeDetailsExtn!modify.action?registrationId="+regid+"&fromreg=true","simple","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes";
	
	//window.opener=self;
	//window.close();
	//jQuery("input[name=actionsbutton]").click();
    
	
			}

function createLetterToParty(){
	alert();
	var regid=jQuery("#id").val();	
	document.location.href="${pageContext.request.contextPath}/extd/register/registerBpaExtn!letterToPartyForm.action?registrationId="+regid
	jQuery("input[name=actionsbutton]").click();
			}	
			
function createRejectionForm(){
	
	var regid=jQuery("#id").val();	
	document.location.href="${pageContext.request.contextPath}/extd/register/registerBpaExtn!rejectForm.action?registrationId="+regid;
	jQuery("input[name=actionsbutton]").click();
			}

function createOrdDetForm(reqdAction){
	
	var regid=jQuery("#id").val();	
	document.location.href="${pageContext.request.contextPath}/extd/register/registerBpaExtn!orderForm.action?registrationId="+regid+"&reqdAction="+reqdAction,"simple","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes";
	jQuery("input[name=actionsbutton]").click();
			}

function printPermit(permitType){
	
	var regid=jQuery("#id").val();
	window.open("${pageContext.request.contextPath}/extd/report/bpaReportExtn!printReport.action?registrationId="+regid+"&printMode="+permitType,"simple","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
	jQuery("input[name=actionsbutton]").click();
			}	

function printRejection(){
	
	var regid=jQuery("#id").val();	
	window.open("${pageContext.request.contextPath}/extd/report/bpaReportExtn!printReport.action?registrationId="+regid+"&printMode=RejectionNotice","simple","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
	jQuery("input[name=actionsbutton]").click();
			}

function updateBldgMeasurement() {
	var regid=jQuery("#id").val();	
	document.location.href="${pageContext.request.contextPath}/extd/buildingdetails/buildingDetailsExtn!newForm.action?registrationId="+regid,"simple","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes";
	jQuery("input[name=actionsbutton]").click();
}

function captureDDdetails() {
   
	var regid=jQuery("#id").val();	
	//window.open("${pageContext.request.contextPath}/register/registerBpa!captureDDForm.action?registrationId="+regid,"simple","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
	document.location.href="${pageContext.request.contextPath}/extd/register/registerBpaExtn!captureDDForm.action?registrationId="+regid,"simple","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes";
	//window.opener=self;
	//window.close();
	//jQuery("input[name=actionsbutton]").click();
}

function onChangeOfServiceType(obj){
	jQuery("#checklists").children().remove(); 
	if( jQuery('#mode').val()=='edit'){	 
	 jQuery('#showjsp').hide();
	 jQuery('#hidejsp').show();
	}
	getData();
	callAdmissionFeeAmount();
	getMandatoryFieldsForServiceType();
	hideShowBuildingCategory();
	hideShowCMDAfields();
	//commented because these fields need to be editable 
	//enableBoundaryData();
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
	    url:"<%=request.getContextPath()%>/common/ajaxCommon!ajaxGetMandatoryFieldsForServiceType.action",
	     data: { serviceTypeId:servicetypeid }, 
	      type: "POST",
	     	dataType: "html",
	   	 success: function(data){
	   	 	var flags = data.split("-");
		   	var cmdaFlag = flags[0];
		   	var autoDcrFlag = flags[1];
			var propertyFlag=flags[2];
		   	document.getElementById('isCmdaServicetype').value=cmdaFlag;
		 	document.getElementById('isAutoDcrMandatory').value=autoDcrFlag;
		 	document.getElementById('isPropertyMandatory').value=propertyFlag;		    
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
	    url:"<%=request.getContextPath()%>/extd/register/registerBpaExtn!showCheckList.action",
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
		//jQuery('#existingApplDtls1').show();
	}
	else if(jQuery(this).val()=="New" || jQuery(this).val()==-1 ){
		document.getElementById('existingPPANum').value="" ;
		document.getElementById('existingBANum').value="" ;
		jQuery('#existingApplDtls0').hide();
	}
});

function hideExistingApplDetails(){
	 var appTypeeValue= document.getElementById('appType').options[document.getElementById('appType').selectedIndex].text;
		//alert("serviceTypeValue "+serviceTypeValue);
		if(appTypeeValue=="Revised"  ){
			
			jQuery('#existingApplDtls0').show();
			//jQuery('#existingApplDtls1').show();	
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


/*
jQuery("#surveyorId").combobox();
jQuery('.ui-autocomplete-input').css('width', '90%');
*/

</script> 
