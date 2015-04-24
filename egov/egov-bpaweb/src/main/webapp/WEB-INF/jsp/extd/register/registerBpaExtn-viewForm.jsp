<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>
<script>
function gotoPage(obj){
	
	var flag=obj;
	var regid=jQuery("#id").val();

		//print Docket
 if(flag=="officialDocketView"){
	 window.open("${pageContext.request.contextPath}/extd/inspection/inspectionExtn!printDocketSheet.action?registrationId="+regid+"","simple","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		
 }
//official Document Histry
else if(flag=="officialDocHistoryView" ){
	window.open("${pageContext.request.contextPath}/extd/register/registerBpaExtn!printDocumentHistory.action?registrationId="+regid+"&printMode=OfficialPrint","simple","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
	}
	//surveyor Doc Histy
else if(flag=="surveyorDocView"  )
	{
	window.open("${pageContext.request.contextPath}/extd/register/registerBpaExtn!printDocumentHistory.action?registrationId="+regid+"&printMode=surveyorPrint","simple","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
	
	 }
else if(flag=="surveyorInspectionView")
	{
	window.open("${pageContext.request.contextPath}/extd/register/registerBpaExtn!printInspectionDetails.action?registrationId="+regid+"&printMode=surveyorPrint","simple","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
	}
else if(flag=="officialInspectionView")
{
window.open("${pageContext.request.contextPath}/extd/register/registerBpaExtn!printInspectionDetails.action?registrationId="+regid+"&printMode=OfficialPrint","simple","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
}

}
</script>
<div>
<s:hidden name="idTemp" id="idTemp"  value="%{id}"/>

<table width="100%" border="0" cellspacing="0" cellpadding="0">

 <tr>
	
	<td class="bluebox" width="13%">&nbsp;</td>
		<s:if test="%{baNum!=null}">
		   <td class="bluebox"   id="searchFieldText" width="13%"><s:text name="buildingApprovalOrderDate" /> : </td>
		   <td class="bluebox"  style="font-weight:bold;font-size:13px"><s:date name="baOrderDate" format="dd/MM/yyyy" /> </td>    
		 <td style="display: none;">  
		 <sj:datepicker value="%{baOrderDate}" id="baOrderDate" name="baOrderDate" displayFormat="dd/mm/yy" showOn="focus" disabled="true" /> 
		   </td>
		 </s:if>
         <s:else>       
	        <td class="bluebox">&nbsp;</td>
			<td class="bluebox">&nbsp;</td>
		</s:else>
            <td class="bluebox" >&nbsp;</td>
        <s:if test="%{baOrderDate!=null}">	     
	        <td class="bluebox" width="20%"  ><s:text name="buildingApprovalNum" /> </td>
			<td class="bluebox" width="26%"  style="font-weight:bold;font-size:13px" ><s:property  value="%{baNum}" /></td>
		  </s:if>
         <s:else>       
	        <td class="bluebox">&nbsp;</td>
			<td class="bluebox">&nbsp;</td>
			<td class="bluebox">&nbsp;</td>
		</s:else>
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox">&nbsp;</td>							
	</tr>

	<tr>
		<td class="greybox" width="13%">&nbsp;</td>
			
        <s:if test="%{planPermitApprovalNum!=null}">
	        <td class="greybox" id="searchFieldText"    swidth="13%"><s:text name="planPermitApprovalNumber" /> : </td>
			<td class="greybox" width="26%"     style="font-weight:bold;font-size:13px" ><s:property    value="%{planPermitApprovalNum}"/></td>
		 </s:if>
         <s:else>       
	        <td class="greybox">&nbsp;</td>
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
		<td class="bluebox"   id="searchFieldText" width="13%"><s:text name="applDate" />:</td>
		<td class="bluebox"  style="font-weight:bold;font-size:13px"> 
		<s:date name="planSubmissionDate" format="dd/MM/yyyy" /></td>
		<td style="display: none;">   <sj:datepicker value="%{planSubmissionDate}" id="planSubmissionDate" name="planSubmissionDate" readonly="true" displayFormat="dd/mm/yy" showOn="focus"/>
		     
		</td>
        <td class="bluebox" width=""></td>
        <td class="bluebox"   id="searchFieldText" width="20%"><s:text name="serviceType" /> : </td>
		<td class="bluebox" width="26%" style="font-weight:bold;font-size:13px">
			<s:property value="%{serviceType.code}"  /><s:property  value="%{serviceType.description}"/></td>
		<td style="display: none;"> 
			<s:select headerKey="-1"
				headerValue="----choose-----" name="serviceType"
				id="serviceTypeId" listKey="id" listValue="code+'-'+description"
				list="dropdownData.serviceTypeList" value="%{serviceType.id}"
				cssClass="selectnew" onchange="onChangeOfServiceType();"/> 
				<s:hidden id="isCmdaServicetype" name="isCmdaServicetype" value="%{isCmdaServicetype}" ></s:hidden>
				<s:hidden id="isAutoDcrMandatory" name="isAutoDcrMandatory" value="%{isAutoDcrMandatory}" ></s:hidden>
				<s:hidden id="isPropertyMandatory" name="isPropertyMandatory" value="%{isPropertyMandatory}" ></s:hidden>	</td>
			 <td class="bluebox">&nbsp;</td>	
			 <td class="bluebox">&nbsp;</td>
		<td class="bluebox">&nbsp;</td>						
	</tr>
  <tr>
        <td class="greybox" width="13%">&nbsp;</td>
		<td class="greybox" id="searchFieldText"   width="13%"><s:text name="applMode" /> : </td>
		<td class="greybox" style="font-weight:bold;font-size:13px"width="26%"> <s:property value="%{appMode}"/></td>
		<td style="display: none;"> 
		<s:select  id="appMode" name="appMode" value="%{appMode}" 
		list="dropdownData.applicationModeList" listKey="code" listValue="code" headerKey="-1" 
         headerValue="----choose-----"  /> </td>
		
		 </td>
          <td class="greybox" width="">&nbsp;</td>
		<td class="greybox" id="searchFieldText"   width="20%"><s:text name="applType" /> : </td>
		<td class="greybox" wiidth="26%"  style="font-weight:bold;font-size:13px" ><s:property   value="%{appType}"/>
		</td>
		<td style="display: none;">  <s:select list="{'New','Revised'}" id="appType"
         name="appType" value="%{appType}" headerKey="-1" 
         headerValue="----choose-----"  /></td>
		
         <td class="greybox">&nbsp;</td>
		<td class="greybox">&nbsp;</td>	
            <td class="greybox">&nbsp;</td>
              
	  <tr>
	
        <s:if test="%{planSubmissionNum!=null}">
         <td class="bluebox" width="13%">&nbsp;</td>
	        <td class="bluebox" id="searchFieldText"   width="13%"><s:text name="planSubmissionNum" /> </td>
			<td class="bluebox" width="26%"  style="font-weight:bold;font-size:13px" ><s:property  value="%{planSubmissionNum}"/></td>
		<td style="display: none;"> 
			<s:textfield size="33%" name="planSubmissionNum" value="%{planSubmissionNum}" id="planSubmissionNum" readonly="true" /> </td>
        <td class="bluebox">&nbsp;</td>
       <td class="bluebox">&nbsp;</td>
       <td class="bluebox">&nbsp;</td>
		<td class="bluebox">&nbsp;</td>	
        </s:if>
        <s:else>  
         <td class="bluebox" width="15%">&nbsp;</td>
        <td class="bluebox">&nbsp;</td>
       <td class="bluebox">&nbsp;</td>
       <td class="bluebox">&nbsp;</td>
       <td class="bluebox">&nbsp;</td>
       <td class="bluebox">&nbsp;</td>
		</s:else>
		 <td class="bluebox">&nbsp;</td>
		 <td class="bluebox">&nbsp;</td>
    </tr>
    
     
	 <s:if test="%{initialPlanSubmissionNum!=null}">
		  <tr>
	      <td class="bluebox" width="13%">&nbsp;</td>
	        <td class="bluebox" id="searchFieldText"    width="13%"><s:text name="oldPlanSubmissionNum" /> </td>
			<td class="bluebox" width="26%"  style="font-weight:bold;font-size:13px" ><s:property  value="%{initialPlanSubmissionNum}"/></td>
		    <td class="bluebox">&nbsp;</td>
       <td class="bluebox">&nbsp;</td>
       <td class="bluebox">&nbsp;</td>
		<td class="bluebox">&nbsp;</td>	
		
		
       		</tr>	
		   </s:if>

	
    <table width="100%" border="0" cellspacing="0" cellpadding="0">  
	
	
	 <%-- 	<tr>								
		<egov:loadBoundaryData adminboundaryid="${adminboundaryid.id}"
		locboundaryid="${locboundaryid.id}" adminBndryVarId="adminboundaryid" locBndryVarId="locboundaryid" />
		<s:hidden  id= "adminboundaryid" name="adminboundaryid" value="%{adminboundaryid.id}" />
		<s:hidden  id= "locboundaryid"  name="locboundaryid" value="%{locboundaryid.id}" />	
	</tr> --%>
	
	<tr>								
		
			<td class="greybox" width="13%">&nbsp;</td>
		<td class="greybox" width="13%"><s:text name="Region" /> : </td>
		<td class="greybox" style="font-weight:bold;font-size:13px"><s:property value="%{adminboundaryid.parent.parent.name}"/>
		</td>
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><s:text name="Zone" /> : </td>
		<td class="greybox" style="font-weight:bold;font-size:13px"><s:property value="%{adminboundaryid.parent.name}"/>
		</td>
		</tr>
		<tr>
		<td class="bluebox" width="13%">&nbsp;</td>
		<td class="bluebox"width="13%"><s:text name="Ward" /> :</td>
		<td class="bluebox" style="font-weight:bold;font-size:13px"><s:property value="%{adminboundaryid.parent.name}"/>
		</td>
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox" >
		<s:text name="Area" /> : </td>
		<td class="bluebox" style="font-weight:bold;font-size:13px"><s:property value="%{locboundaryid.parent.parent.name}"/>
		</td>
		</tr>
		<tr>
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><s:text name="Location" /> : </td>
		<td class="greybox" style="font-weight:bold;font-size:13px"><s:property value="%{locboundaryid.parent.name}"/>
		</td>
		
		<td class="greybox">&nbsp;</td>
		<td class="greybox" ><s:text name="Street" /> :</td>
		<td class="greybox" style="font-weight:bold;font-size:13px"><s:property value="%{locboundaryid.name}"/>
		</td>
		<s:hidden  id= "adminboundaryid" name="adminboundaryid" value="%{adminboundaryid.id}" />
		<s:hidden  id= "locboundaryid"  name="locboundaryid" value="%{locboundaryid.id}" />	
		
		
	</tr>    
    <tr>
		<td class="bluebox" width="13%">&nbsp;</td>
		<td class="bluebox" id="searchFieldText"   width="13%"><s:text name="propertyId" /> :</td>
		<td class="bluebox" width="26%"  style="font-weight:bold;font-size:13px" ><s:property  value="%{propertyid}"/></td>
		<td style="display: none;"> 
			<s:textfield id="propertyid" name="propertyid" value="%{propertyid}" readonly="true" />
			</td>
				<td class="bluebox" >&nbsp;</td>
			<td class="bluebox" id="searchFieldText"   ><s:text name="autoDcrNum" /> :</td>
	<td class="bluebox" width="20%"  style="font-weight:bold;font-size:13px" ><s:property  value="%{autoDcrNum}"/></td>
		<td style="display: none;"> 
	<s:textfield id="autoDcrNum" name="autoDcrNum" value="%{autoDcrNum}" readonly="true" /></td>
			
			<td class="bluebox" >&nbsp;</td>
			</tr>
			
      <!--  <s:if test="%{appType!=null && appType=='Revised'}"> 
       <tr id="existingApplDtls0">
		<td class="greybox" width="13%">&nbsp;</td>
        <td class="greybox" width="13%"><s:text name="existingPPANum.txt" /> :</td>
        <td class="greybox" ><s:property value="%{existingPPANum}"/></td>
		<td style="display: none;"> 
        <s:textfield name="existingPPANum" id="existingPPANum" value="%{existingPPANum}"   /></td>
      	<td class="greybox">&nbsp;</td>
        <td class="greybox"><s:text name="existingBANum.txt" /> :</td>
        <td class="greybox"><s:property value="%{existingBANum}" /></td>
		<td style="display: none;"> 
        <s:textfield name="existingBANum" id="existingBANum" value="%{existingBANum}"  /></td>
   </tr>
   <!--</s:if> -->
     </table>   
	<div id="ownerDtls">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr> 
		<td colspan="6"><div class="headingbg"><span class="bold"><s:text name="Applicant Details"/></span></div></td>
	</tr>
	
	<tr>
		<td class="bluebox" width="13%">&nbsp;</td>
		<td class="bluebox" id="searchFieldText" width="13%" ><s:text name="applicantName" /> : </td>
		<td class="bluebox"  style="font-weight:bold;font-size:13px" ><s:property  value="%{owner.firstName}"/>
		</td>
		<td style="display: none;"> <s:textfield id="ownerFirstname" name="owner.firstName" value="%{owner.firstName}" onblur="return validateName(this);" />
		</td>
		<td class="bluebox" width="13%">&nbsp;</td>
		<td class="bluebox"   id="searchFieldText" ><s:text name="spouseOrFatheName" /> : </span></td>
		<td class="bluebox"  style="font-weight:bold;font-size:13px" width="20%"><s:property  value="%{owner.fatherName}"/>
		</td>
		<td style="display: none;"> <s:textfield id="spouseOrFatheName" name="owner.fatherName" value="%{owner.fatherName}" onblur="return validateName(this);" />
		</td>
		<s:hidden id="owner" name="owner" value="%{owner.citizenID}" /> 
	</tr>
	<tr>
		<td class="greybox" width="13%" >&nbsp;</td> 
		<td class="greybox" width="13%"  id="searchFieldText"><s:text name="applicantAddress1" /> : </td>
		<td class="greybox"  style="font-weight:bold;font-size:13px" ><s:property  value="%{applicantrAddress.streetAddress1}"/>
		</td>
		<td style="display: none;"> <s:textarea cols="20" rows="2" id="applicantAddress1" name="applicantrAddress.streetAddress1" value="%{applicantrAddress.streetAddress1}" maxlength='512' onblur="filterAddress(this);"/>
		</td>
		<td class="greybox" width="13%">&nbsp;</td>
		<td class="greybox"   id="searchFieldText"><s:text name="applicantAddress2" /> : </td>
		<td class="greybox"  style="font-weight:bold;font-size:13px"><s:property  value="%{applicantrAddress.streetAddress2}"/>
		</td>
		<td style="display: none;"> <s:textarea  cols="20" rows="2" id="applicantAddress2" name="applicantrAddress.streetAddress2" value="%{applicantrAddress.streetAddress2}" maxlength='512' onblur="filterAddress(this);"/>
		</td>
		<s:hidden id="applicantrAddress.id" name="applicantrAddress.id"  />
	</tr>
	<tr>
		<td class="bluebox" width="13%">&nbsp;</td> 
		<td class="bluebox"  width="13%"><s:text name="phoneNum" /> :</td>
        <td class="bluebox"  style="font-weight:bold;font-size:13px" ><s:property  value="%{owner.homePhone}"/>
        </td>
		<td style="display: none;"> <s:textfield id="contactNo" name="owner.homePhone" value="%{owner.homePhone}" maxlength="10" onblur="return validateContactNumber(this);" />
	</td>
        <td class="bluebox"  width="26%" >&nbsp;</td>
		<td class="bluebox"   ><s:text name="mobileNum" /> :</td>
        <td class="bluebox"  style="font-weight:bold;font-size:13px" width="26%"><s:property  value="%{mobileNumber}"/>
        </td>
		<td style="display: none;"> <s:textfield id="mobileNo" name="mobileNumber" value="%{mobileNumber}" maxlength="15" onblur="return validateMobileNumber(this);"/>

       	<span class="mandatory" style="font-weight:bold;font-size:13px" >**<s:text name="SMS is sent to this" /> </span>
        </td>

	</tr>
	<tr>
	<td class="greybox"width="13%">&nbsp;</td> 
		<td class="greybox"  width="13%"><s:text name="email" /> :</td>
        <td class="greybox"  style="font-weight:bold;font-size:13px" ><s:property  value="%{emailId}"/>
        </td>
		<td style="display: none;"> <s:textfield id="email" name="emailId" value="%{emailId}" onblur="return validateEmail();" />

       <span class="mandatory" style="font-weight:bold;font-size:13px" >**<s:text name="mail is sent to this" /> </span>
        </td>
        <td class="greybox">&nbsp;</td> 
        <td class="greybox">&nbsp;</td> 
<td class="greybox">&nbsp;</td> 
	</tr>
	</table>
	</div>

	  
	<div id="plotAddress">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		
<tr>
		<td colspan="6"><div class="headingbg"><span class="bold"><s:text name="Site Address"/></span></div></td>
	</tr>
	
 	<tr>
    
		<td class="greybox" width="13%">&nbsp;</td>
		<td class="greybox" width="13%"   ><s:text name="doorNum" /> :</td>
	    <td class="greybox"   style="font-weight:bold;font-size:13px" ><s:property  value="%{siteAddress.plotDoorNumber}"/>
	    </td>
		<td style="display: none;"> <s:textfield id="plotDoorNum" name="siteAddress.plotDoorNumber" value="%{siteAddress.plotDoorNumber}" maxlength="32" />
		</td>
	    <td class="greybox">&nbsp;</td>
	    <td class="greybox"   ><s:text name="blockNum" /> : </td>
	    <td class="greybox" style="font-weight:bold;font-size:13px" width="20%">
      	<s:property value="%{siteAddress.plotBlockNumber}"/>
      	</td>
		<td style="display: none;"> <s:textfield id="plotBlockNum" name="siteAddress.plotBlockNumber" value="%{siteAddress.plotBlockNumber}" maxlength="510" />
		</td>	 
		<s:hidden id="siteAddress.id" name="siteAddress.id"  />
		
   	</tr>
   
 	<tr>
		<td class="bluebox"width="13%">&nbsp;</td>
       	<td class="bluebox"   width="13%">&nbsp;<s:text name="landmark" /> :</td>
      	<td class="bluebox" style="font-weight:bold;font-size:13px">
		<s:property value="%{siteAddress.plotLandmark}"/></td>
		<td style="display: none;"> 
		<s:textfield id="plotLandmark" name="siteAddress.plotLandmark" value="%{siteAddress.plotLandmark}"maxlength="126" />
		</td>
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox"  ><s:text name="plotNum" /> : </td>
      	<td class="bluebox" width="20%"style="font-weight:bold;font-size:13px">
		<s:property value="%{siteAddress.plotNumber}"/></td>
		<td style="display: none;"> 
		<s:textfield id="sitePlotNum" name="siteAddress.plotNumber" value="%{siteAddress.plotNumber}" maxlength="32" />
		</td>
		
	</tr>
             
    <tr>
      	<td class="greybox" width="13%">&nbsp;</td>
      	<td class="greybox"   width="13%"><s:text name="surveyNum" /> : </td>
      	<td class="greybox"style="font-weight:bold;font-size:13px"> <s:property value="%{siteAddress.plotSurveyNumber}"/> </td> 
		<td style="display: none;"> 
		<s:textfield id="sitePlotSurveyNum" name="siteAddress.plotSurveyNumber" value="%{siteAddress.plotSurveyNumber}" maxlength="250" />
		</td>
		<td class="greybox">&nbsp;</td>
		<td class="greybox"   ><s:text name="Reg.villageName" /> : </td>
		<td class="greybox" width="20%"style="font-weight:bold;font-size:13px">
		<s:property value="%{siteAddress.villageName.name}"/></td>
		<td style="display: none;"> 
		<s:select name="siteAddress.villageName.id" id="villageName" value="%{siteAddress.villageName.id}" 
				list="dropdownData.villageNameList" listKey="id" listValue="name" headerKey="-1" headerValue="----choose---" />
		</td>        
		<tr>
		
  		<td class="bluebox" width="13%" >&nbsp;</td>
		<td class="bluebox"   width="13%"><s:text name="townOrCityName" /> : </span></td>
        <td class="bluebox" style="font-weight:bold;font-size:13px">
		<s:property value="%{siteAddress.cityTown}"/></td>
		<td style="display: none;"> 
		<s:textfield name="siteAddress.cityTown" value="%{siteAddress.cityTown}" id="cityTown"  readonly="true"/>
		&nbsp;</td>
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox"   ><s:text name="stateName" /> : </td>
        <td class="bluebox" width="20%" style="font-weight:bold;font-size:13px">
		<s:property value="%{siteAddress.indianState.name}"/></td>
		<td style="display: none;"> 
		<s:select headerKey="-1" headerValue="----choose-----" name="boundaryStateId" id="boundaryStateId" listKey="id" listValue="name" list="dropdownData.bndryStateList"
				cssClass="selectnew" value="%{boundaryStateId}"/> 
		</td>
		
	</tr>
	<tr>
	<td class="greybox" width="13%">&nbsp;</td>
		<td class="greybox"   width="13%"><s:text name="pincode" /> : </span></td>
        <td class="greybox" style="font-weight:bold;font-size:13px">
	<s:property value="%{siteAddress.pincode}"/></td>
		<td style="display: none;"> 
	<s:textfield id="pincode" name="siteAddress.pincode" value="%{siteAddress.pincode}" maxlength="6" onblur="return validatePincodeValue(this);" />

	</td>
        <td class="greybox" width="20%">&nbsp;</td>
        <td class="greybox">&nbsp;</td>
       
	</tr>
             
	<tr id="buildingCategory">
		<td class="bluebox" width="13%">&nbsp;</td>
        <td class="bluebox"   width="13%"><s:text name="existingBuildingCategory" /> :</td>
        <td class="bluebox" style="font-weight:bold;font-size:13px">
		 <s:property value="%{regnDetails.existingBldgCatg.code}"/></td>
		<td style="display: none;"> 
		 <s:select headerKey="-1"
				headerValue="-----Choose-----" name="existingbuildingCategoryId" id="existingbuildingCategoryId"
				listKey="id" listValue="code" list="dropdownData.existingBuildingCategoryList"
				cssClass="selectnew" 
				value="%{existingbuildingCategoryId}" />
		</td>
		<td class="bluebox">&nbsp;</td>
		 <td class="bluebox"   ><s:text name="proposedBuildingCategory" /> :</td>
         <td class="bluebox" width="20%" style="font-weight:bold;font-size:13px">
		<s:property value="%{regnDetails.proposedBldgCatg.code}"/></td>
		<td style="display: none;"> 
		<s:select headerKey="-1"
				headerValue="-----Choose-----" name="proposedbuildingCategoryId" id="proposedbuildingCategoryId"
				listKey="id" listValue="code" list="dropdownData.proposedBuildingCategoryList"
				cssClass="selectnew" 
				value="%{proposedbuildingCategoryId}" />
</td>
	</tr> 
	</table>
	</div>

		      
	<div id="plotDtls">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
		<td colspan="6"><div class="headingbg"><span class="bold"><s:text name="Site Details"/></span></div></td>
	</tr>
  	<tr>
  		<td class="greybox" width="13%">&nbsp;</td>
        <td class="greybox"   width="13%"><s:text name="plotAreaSqft" /> : </td>
        <td class="greybox"   style="font-weight:bold;font-size:13px" ><s:property  value="%{regnDetails.sitalAreasqft}"/></td>
		<td style="display: none;"> 
        <s:textfield id="plotAreaInSqft" name="regnDetails.sitalAreasqft" value="%{regnDetails.sitalAreasqft}" maxlength="9" onblur="convertSQFTtoSQMT()" />
       <td class="greybox" width="23%">&nbsp;</td>
		<td class="greybox"  ><s:text name="plotAreaSqmt" /> : </td>
        <td class="greybox"   style="font-weight:bold;font-size:13px" ><s:property  value="%{regnDetails.sitalAreasqmt}"/></td>
		<td style="display: none;"> 
        <s:textfield id="plotAreaInSqmt" name="regnDetails.sitalAreasqmt" value="%{regnDetails.sitalAreasqmt}" maxlength="9"  readonly="true"  />
     <s:hidden id="regnDetails.id" name="regnDetails.id" />
	</tr> 
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
		<td class="bluebox"   width="13%"><s:text name="cmdaProposalNum" /> :</td>
        <td class="bluebox"   style="font-weight:bold;font-size:13px" ><s:property  value="%{cmdaNum}"/></td>
		<td style="display: none;">  
        <s:textfield id="cmdaProposalNumber" name="cmdaNum" value="%{cmdaNum}"/>
   	</td>
        <td class="bluebox" width="23%"> &nbsp;</td>
        <td class="bluebox"   ><s:text name="cmdaRefDate" /> :</td>
        <td class="bluebox" style="font-weight:bold;font-size:13px">
         <s:date name="cmdaRefDate" format="dd/MM/yyyy" />  </td>
		<td style="display: none;"> 
          <sj:datepicker value="%{cmdaRefDate}" id="cmdaRefDate" name="cmdaRefDate" displayFormat="dd/mm/yy" showOn="focus" readonly="true"/>
     </td>
	
	</tr> 
	</table>
	</div>
	
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="bluebox" width="13%">&nbsp;</td>
						<td class="bluebox"    width="13%"><s:text name="remarks" /> :</td>
						<td class="bluebox" style="font-weight:bold;font-size:13px">
						<s:property value="%{regnDetails.remarks}"/></td>
		<td style="display: none;"> 
						<s:textarea cols="50" rows="2"
								id="regnDetails.remarks" name="regnDetails.remarks"
								value="%{regnDetails.remarks}" onblur="filterAddress(this);"/>
					</td>
						
						<td class="bluebox">&nbsp;</td>
						
						<td class="bluebox" width="20%">&nbsp;</td>
						<td class="bluebox">&nbsp;</td>
						
					</tr></table>
				
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
			<td colspan="10"><div class="headingbg"><span class="bold"><s:text name="Surveyor Details"/></span></div></td>
		</tr>
		 <tr style="display: none;">
    <td class="greybox"> <s:hidden  id="Surveyor" name="Surveyor" value="%{Surveyor}" />
		 </td>
   </tr>
					<tr>
							<td class="greybox" width="13%">&nbsp;</td>
							<td class="greybox"    width="13%"><s:text name="Surveyor Name" />
							<td class="greybox" style="font-weight:bold;font-size:13px">
							<s:property value="%{surveyorNameLocal}"/></td>
		<td style="display: none;"> 
							<s:textfield
									id="surveyorNameLocal" name="surveyorNameLocal"
									value="%{surveyorNameLocal}" readonly="true" />
							</td>
							<td class="greybox"></td>
							<td class="greybox"   ><s:text name="Surveyor Code" />:
								</td>
							<td class="greybox" style="font-weight:bold;font-size:13px">
							<s:property value="%{surveyorCode}"/></td>
		<td style="display: none;"> 
							<s:textfield
									id="surveyorCode" name="surveyorCode" value="%{surveyorCode}" readonly="true"
									 /></td>
									 
									 <td class="greybox"    ><s:text
									name="Surveyor Class" />: </td>
							<td class="greybox"   style="font-weight:bold;font-size:13px" ><s:property  value="%{surveyorClass}"/>
							</td>
		<td style="display: none;"> <s:textfield
									id="surveyorClass" name="surveyorClass"
									value="%{surveyorClass}" readonly="true" /></td>
							
						</tr>
						

					</table>	
					<table width="100%" border="0" cellspacing="0" cellpadding="0" id="admissionTbl">
		<tr>
		
			<td colspan="6"   ><div class="headingbg"><span class="bold"><s:text name="admissionFee"/></span></div></td>
		</tr>
		<tr>
			<td class="bluebox" width="13%">&nbsp;</td>
			<td class="bluebox" width="13%"><s:text name="admissionfeeAmount" /> :</td>
			<td class="bluebox"  style="font-weight:bold;font-size:13px" ><s:property  value="%{admissionfeeAmount}"/></td>
		<td style="display: none;"> 
			<s:textfield id="admissionfeeAmount" name="admissionfeeAmount" value="%{admissionfeeAmount}"  readonly="true"  /></td>
			<td class="bluebox">&nbsp;</td>
			<td class="bluebox">&nbsp;</td>
			<td class="bluebox">&nbsp;</td>
		</tr>   
	</table>
    </div>   
   <table width="100%" border="0" cellspacing="0" cellpadding="0" id="admissionTbl">
   <tr>
   </tr>
    <s:if test="%{ enableDocketSheetForView && existingSiteInspectionDetails.size!=0   && !isUserMappedToSurveyorRole()}">
     
    	<tr><td class="bluebox" width="13%">&nbsp;</td>
						 		 <td class="bluebox" width="15%"><s:text name="View Surveyor Inspection Details"/>
						 		<td class="bluebox">
						 		<input type="button" name="surveyorInspectionView" id="surveyorInspectionView" class="button" value="View/Print" onclick=" gotoPage('surveyorInspectionView');"/></td>
						 		<td class="bluebox" width="13%">&nbsp;</td>
								</tr> 
								  <tr><td class="greybox" width="13%">&nbsp;</td>
     <td class="greybox" width="13%">&nbsp;</td>
     <td class="greybox" width="13%">&nbsp;</td>
     <td class="greybox" width="13%">&nbsp;</td></tr>
								
 	<tr><td class="bluebox" width="13%">&nbsp;</td>
     <td class="bluebox" width="15%"><s:text name="View Surveyor Document History Details"/>
 	<td  class="bluebox" >
 	<input type="button" name="surveyorDocView" id="surveyorDocView" class="button" value="View/Print" onclick=" gotoPage('surveyorDocView');"/></td>
			
						 		<td class="bluebox" width="13%">&nbsp;</td>
						 		</tr>
						 		
						  <tr><td class="greybox" width="13%">&nbsp;</td>
     <td class="greybox" width="13%">&nbsp;</td>
     <td class="greybox" width="13%">&nbsp;</td>
     <td class="greybox" width="13%">&nbsp;</td></tr>
						 		 <tr> 
						 		 <td class="bluebox" width="13%">&nbsp;</td>
						 		  <td class="bluebox" width="15%"><s:text name="View AE/AEE Inspection Details"/>
						 		<td class="bluebox">
						 		<input type="button" name="officialInspectionView" id="officialInspectionView" class="button" value="View/Print" onclick=" gotoPage('officialInspectionView');"/></td>
						 		<td class="bluebox" width="13%">&nbsp;</td>
						 		</tr>
						 		 <tr><td class="greybox" width="13%">&nbsp;</td>
     <td class="greybox" width="13%">&nbsp;</td>
     <td class="greybox" width="13%">&nbsp;</td>
     <td class="greybox" width="13%">&nbsp;</td></tr>
						 		<tr>
						 		<td class="bluebox" width="13%">&nbsp;</td>
						 	 <td class="bluebox" width="15%"><s:text name="View AE/AEE Document History Details"/>
						 		<td class="bluebox">
						 		<input type="button" name="officialDocHistoryView" id="officialDocHistoryView" class="button" value="View/Print" onclick=" gotoPage('officialDocHistoryView');"/></td>
								<td class="bluebox" width="13%">&nbsp;</td>
								</tr>
								 <tr><td class="greybox" width="13%">&nbsp;</td>
     <td class="greybox" width="13%">&nbsp;</td>
     <td class="greybox" width="13%">&nbsp;</td>
     <td class="greybox" width="13%">&nbsp;</td></tr> 
								<tr><td class="bluebox" width="13%">&nbsp;</td>
								 <td class="bluebox" width="15%"><s:text name="View AE/AEE Docket Sheet Details"/>
						 		<td class="bluebox">
						 		
						 		<input type="button" name="officialDocketView" id="officialDocketView" class="button" value="View/Print" onclick=" gotoPage('officialDocketView');"/></td>
			<td class="bluebox" width="13%">&nbsp;</td></tr>
						 		
	
   </s:if>
    
    </table>
     
  
    <s:if test="%{mode!='view' && registration.id!=null && (registration.currentState.value==@org.egov.bpa.constants.BpaConstants@FORWADREDTOSESTATE
    || registration.currentState.value==@org.egov.bpa.constants.BpaConstants@FORWARDEDTORDCSTATE)}">
			 <div id="regViewOfficialPdfActionsForm">
		         <%@ include file="registerBpaExtn-viewOfficialPdfActions.jsp"%>   
		     </div>
     </s:if> 
    <s:if test="%{approvedWorkflowState == 'Approved'}">
 	<s:if test="%{ !enableDocketSheetForView}">
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
	</s:if>
	</s:if>
	</table>
	
  
			
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
	else if(type=='Create Letter To CMDA'){
		createLetterToCMDA();
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

function createLetterToCMDA(){ 
	var regid=jQuery("#id").val();	
	document.location.href="${pageContext.request.contextPath}/extd/register/registerBpaExtn!letterToCMDAForm.action?registrationId="+regid;
	jQuery("input[name=actionsbutton]").click();
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