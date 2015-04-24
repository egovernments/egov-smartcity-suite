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
			<td class="bluebox"><s:textfield size="33%" name="baNum" value="%{baNum}" id="baNum" readonly="true" /></td>
         </s:if>
         <s:else>       
	        <td class="bluebox">&nbsp;</td>
			<td class="bluebox">&nbsp;</td>
		</s:else>
		<td class="bluebox">&nbsp;</td>
				  <td class="bluebox">&nbsp;</td>							
	</tr>
  </s:if>
	<tr>
		<td class="greybox" width="30%">&nbsp;</td>
		<td class="greybox">&nbsp;</td>
		<td class="greybox">&nbsp;</td>			
        <td class="greybox" width="13%">&nbsp;</td>
        <s:if test="%{planPermitApprovalNum!=null}">
	        <td class="greybox"><s:text name="planPermitApprovalNumber" /> : <span class="mandatory" >*</span></td>
			<td class="greybox"><s:textfield size="33%" name="planPermitApprovalNum" value="%{planPermitApprovalNum}" id="planPermitApprovalNum" readonly="true" /></td>
        </s:if>
         <s:else>       
	        <td class="greybox">&nbsp;</td>
			<td class="greybox">&nbsp;</td>
		</s:else>
 	   			 <td class="greybox">&nbsp;</td>
				  <td class="greybox">&nbsp;</td>		
	</tr>

	<tr>
		<td class="bluebox" width="30%">&nbsp;</td>
		<td class="bluebox" width="13%"><s:text name="applDate" />:<span class="mandatory" >*</span></td>
		<td class="bluebox" >      
        		<s:date format="dd/MM/yyyy" name="planSubmissionDate" var="TempDate1"/>
					<s:textfield name="planSubmissionDate" id="planSubmissionDate"  maxlength="20" value="%{TempDate1}" readonly="true"  />
		</td>
        <td class="bluebox" width="20%">&nbsp;</td>
        <td class="bluebox" ><s:text name="serviceType" /> : <span class="mandatory" >*</span></td>
		<td class="bluebox">
		 <s:select headerKey="-1"
				headerValue="----choose-----" name="serviceType"
				id="serviceTypeId" listKey="id" listValue="code+'-'+description"
				list="dropdownData.serviceTypeList" value="%{serviceType.id}"
				cssClass="selectnew" onchange="onChangeOfServiceType();"/>   </td>
				<s:hidden id="isCmdaServicetype" name="isCmdaServicetype" value="%{isCmdaServicetype}" ></s:hidden>
				<s:hidden id="isAutoDcrMandatory" name="isAutoDcrMandatory" value="%{isAutoDcrMandatory}" ></s:hidden>
				<s:hidden id="isPropertyMandatory" name="isPropertyMandatory" value="%{isPropertyMandatory}" ></s:hidden>	
				  <td class="bluebox">&nbsp;</td>
				  <td class="bluebox">&nbsp;</td>						
	</tr>
   <tr>
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
	  <tr>
	 
        <s:if test="%{planSubmissionNum!=null}">
         <td class="bluebox" width="40%">&nbsp;</td>
	        <td class="bluebox" width="30%"><s:text name="planSubmissionNum" /> : <span class="mandatory" >*</span></td>
			<td class="bluebox"><s:textfield size="33%" name="planSubmissionNum" value="%{planSubmissionNum}" id="planSubmissionNum" readonly="true" /></td>
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
		<%@ include file="../common/existingApplicationDetailsForm.jsp"%>
	</table>
	</div>
	
    <table width="100%" border="0" cellspacing="0" cellpadding="0">  
	<tr>
		<td class="bluebox" width="13%">&nbsp;</td>
		<td class="bluebox" width="13%"><s:text name="propertyId" /> :</td>
		<td class="bluebox" width="25%">
			<s:textfield id="propertyid" name="propertyid" value="%{propertyid}" readonly="true" />
			<s:if test="%{mode!='view'}"> 
					<a class="small-button" id="propertyAtag" href="javascript:openSearchScreen();">Search Property</a>
			</s:if>
			<s:else>&nbsp;</s:else>
		</td>
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox" width="20%"><s:text name="autoDcrNum" /> :</td>
		<td class="bluebox" ><s:textfield id="autoDcrNum" name="autoDcrNum" value="%{autoDcrNum}" readonly="true" />
		<s:if test="%{mode!='view'}"> 
		<a id="autodcrAtag1" href="#"><img src="${pageContext.request.contextPath}/images/searchicon.gif" onclick="openSearchAutoDcr();"></a>
     	<a id="autodcrAtag2" href="#"><img src="${pageContext.request.contextPath}/images/refresh.gif" onclick="ResetAutoDcr()"></a>
		</s:if>
	
		</td>

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
		<%@ include file="../common/ownerDetailsForm.jsp"%>
	</table>
	</div>

	  
	<div id="plotAddress">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<%@ include file="../common/plotAddress.jsp"%>
	</table>
	</div>

		      
	<div id="plotDtls">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<%@ include file="../common/plotDetails.jsp"%>
	</table>
	</div>

	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td colspan="6"><div class="headingbg"><span class="bold"><s:text name="Other Information"/></span></div></td>
		</tr>
	</table>
	
	<div id="cmdaDtls">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<%@ include file="../common/cmdaDetails.jsp"%>
	</table>
    </div>   
      
    <table width="100%" border="0" cellspacing="0" cellpadding="0">  
		<tr>
			<td class="greybox" width="13%">&nbsp;</td>
			<td class="greybox" width="13%"><s:text name="surveyorName" /> : <span class="mandatory" >*</span></td>
			<td class="greybox" width="27%">
				<div id="surveyorDiv"><s:select name="surveyorName" id="surveyorId" value="%{surveyorName.id}" 
				list="dropdownData.surveyorNameList" listKey="id" listValue="name" headerKey="-1" headerValue="--------Choose--------" /></div></td>
			<td class="greybox">&nbsp;</td>
			<td class="greybox" width="13%"><s:text name="remarks" /> :</td>
			<td class="greybox"><s:textarea cols="50" rows="2" id="regnDetails.remarks"  name="regnDetails.remarks" value="%{regnDetails.remarks}"/></td>
			
		</tr> 	
	</table>

<div>

	
    
      
      <table width="100%" border="0" cellspacing="0" cellpadding="0" id="admissionTbl">
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
	
</div>		
</td></tr></table>
</div>	

 
<script>
function onChangeOfServiceType(){
	if( jQuery('#mode').val()!='edit'){

		if( jQuery('#mode').val()!='view'){	
			jQuery("#checklists").children().remove(); 
			getData();
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
		
	    url:"${pageContext.request.contextPath}/register/registerBpa!existPPAnumberforRegistration.action",
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
	    url:"${pageContext.request.contextPath}/register/registerBpa!existBanumberCheckForAjax.action",
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
	    url:"<%=request.getContextPath()%>/register/registerBpa!showCheckList.action",
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
 	 	
		var url = "${pageContext.request.contextPath}/common/ajaxCommon!ajaxGetAdmissionFeeAmount.action?serviceTypeId="+serviceTypeId+'&sitalAreaInSqmt='+sitalAreaInSqmt;
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

