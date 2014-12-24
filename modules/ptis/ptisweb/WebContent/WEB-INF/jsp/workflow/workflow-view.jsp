<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp" %>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title><s:text name='NewProp.title'/></title>
<sx:head/>
<script type="text/javascript">
	function loadOnStartUp() {
   		setCorrCheckBox();
   		setPropCreatedDate();
	}
 function setCorrCheckBox(){
    
     <s:if test="%{isAddressCheck()}">
			document.getElementById("chkIsCorrIsDiff").checked=true;
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
        	<%@ include file="../create/createPropertyView.jsp"%>  
        	
        	</tr>
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
