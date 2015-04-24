<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>
<%@ include file="/includes/taglibs.jsp" %>

<html>
<title>
	<s:text name="Approval Information"/>
</title>	

<head>
<sj:head jqueryui="true" jquerytheme="redmond" loadAtOnce="true"/>
<script type="text/javascript">

jQuery.noConflict();



function validation(){
	var count=0;
	 var fixAmt=jQuery("#isForwardToCmda").val();
	 var todaysDate=getTodayDate();
		var applicationDate=document.getElementById('applicationDate').value;
		var commApprovalDate=document.getElementById("commApprovalDate").value;
		var tempcmdaapprovalDate=document.getElementById("dateOfForward").value;
		
	  	if(compareDate(applicationDate,commApprovalDate) == -1 )
		{
		   	alert("Approval Date should be greater than Application Date "+applicationDate);
		   	return false;
		}
	  	if(compareDate(commApprovalDate,todaysDate) == -1)
		{						  	 	
			alert("Approval Date should not be greater than Todays Date ");
			return false;
		}
	  if(tempcmdaapprovalDate!=null && compareDate(commApprovalDate,tempcmdaapprovalDate) == -1 )
		{
		   	alert("CMDA Approval Dat should be greater than Approval Date "+applicationDate);
		   	return false;
		}
	  	if(tempcmdaapprovalDate!=null && compareDate(tempcmdaapprovalDate,todaysDate) == -1)
		{						  	 	
			alert("CMDA Approval Date should not be greater than Todays Date ");
			return false;
		}
	
	 if( jQuery('#approvalType').val()=="" ||  jQuery('#approvalType').val()==-1){
	   showerrormsg(jQuery('#approvalType'),"Approval Type is mandatory");
	  count++;
	   }
	   if(count!=0)
			return false;
	   if( jQuery('#usageFrom').val()=="" ||  jQuery('#usageFrom').val()==-1){
		   showerrormsg(jQuery('#usageFrom'),"Change Of Usage From is mandatory");
		  count++;
		   }
		   if(count!=0)
				return false;
		   if( jQuery('#usageTo').val()=="" ||  jQuery('#usageTo').val()==-1){
			   showerrormsg(jQuery('#usageTo'),"Change of UsageTo is mandatory");
			  count++;
			   }
			   if(count!=0)
				return false;
			   if( jQuery('#isForwardToCmda').val()=="" ||  jQuery('#isForwardToCmda').val()==-1){
				   showerrormsg(jQuery('#isForwardToCmda'),"Please Select IsForward To CMDA");
				  count++;
				   }
				   if(count!=0)
					return false;
	 if( jQuery('#commApprovalDate').val()==""){
	   showerrormsg(jQuery('#commApprovalDate'),"Approval Date is mandatory");
	  count++;
	   }
	   if(count!=0)
			return false;
		if(fixAmt == "true"){
	   if( jQuery('#dateOfForward').val()==""){
		   showerrormsg(jQuery('#dateOfForward'),"Date Of Forward is mandatory");
		  count++;
		   }
		}
		   if(count!=0)
				return false;

		   if(jQuery('#usageFrom').val()==jQuery('#usageTo').val())
			{
			showerrormsg(jQuery('#usageFrom'),"Both the usage of Change of use From and To should not be the same");
			  count++;
			}
			   if(count!=0)
					return false;
			
		
}

function onChangeOfisForwardToCmda()
{
	var mode = document.getElementById("mode").value;
	
	
		
	 var fixAmt=jQuery("#isForwardToCmda").val();
		//alert("serviceTypeValue "+fee);
		if(mode=="view" || fixAmt != "true"){
			jQuery('#divcmdaDate').hide();
			document.getElementById("dateOfForward").value="";
		
			
		}		
		else{
			jQuery('#divcmdaDate').show();
			
		}
		
	}

function showerrormsg(obj,msg){
	dom.get("approval_error").style.display = '';
	document.getElementById("approval_error").innerHTML =msg;
	jQuery(obj).css("border", "1px solid red");		
	return false;
	}


function enableAllFields(){
for(var i=0;i<document.forms[0].length;i++)
			{
				document.forms[0].elements[i].disabled =false;
			}

}
function bodyOnLoad() {
	
	 var fixAmt=jQuery("#isForwardToCmda").val();
	var mode = document.getElementById("mode").value;
	
		if(mode=="view") {
			refreshInbox();
			  for(var i=0;i<document.forms[0].length;i++)
	    		{  
		    	
		    	document.forms[0].elements[i].disabled =true;
		    	 document.getElementById("close").disabled=false;
	    	
			}
		}
		if(mode=="modify" &&  fixAmt != "true"){
			jQuery('#divcmdaDate').hide();
		}
		if(mode=="view" &&  fixAmt != "true"){
			jQuery('#divcmdaDate').hide();
		}
}
</script>
</head>
<body onload="bodyOnLoad();">

<div class="errorstyle" id="approval_error" style="display:none;" >
</div>

<s:if test="%{hasErrors()}">
		<div class="errorstyle" id="fieldError">
			<s:actionerror />
			<s:fielderror />
		</div>
</s:if>

<s:if test="%{hasActionMessages()}">
		<div class="errorstyle">
				<s:actionmessage />
		</div>
</s:if>

<s:form action="approvalInformation" theme="simple" onkeypress="return disableEnterKey(event);" >

	<s:token />


<s:hidden id="registration" name="registration" value="%{registration.id}"/>
<s:hidden id="mode" name="mode" value="%{mode}" />
<s:hidden id="id" name="id" value="%{id}"/>
<s:date name="applicationdate" var="cdFormat" format="dd/MM/yyyy"/>
<s:hidden id="applicationDate" name="applicationDate" value="%{cdFormat}" />
	<s:hidden id="createdBy" name="createdBy" value="%{createdBy.id}"/>
	   		
<div align="center"> 


<div  id="regdetails" class="formmainbox">
<h1 class="subhead" ><s:text name="Registration Details"/></h1>
 <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
	   	 	<tr>
	   	 	<td class="bluebox" width="10%">&nbsp;</td>
		<td class="bluebox" width="13%"><s:text name="Note Approval Type" /> : <span class="mandatory" >*</span></td>
		<td class="bluebox"> <s:select name="approvalType" id="approvalType"  list="approvalTypeMap" listKey="key" listValue="value" headerKey="-1" headerValue="----Choose------" />
		 </td>
	 			<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="13%"><s:text name="Commissioner Approved Date"/><span class="mandatory">*</span>:</td> 
	   			<td class="bluebox"> <sj:datepicker value="%{commApprovalDate}" id="commApprovalDate" name="commApprovalDate" displayFormat="dd/mm/yy" disabled="false" showOn="focus"/></td>   			
				<td class="bluebox" width="10%">&nbsp;</td>
				 <td class="bluebox" width="10%">&nbsp;</td>
          </tr>
          <tr>
	       
		 <td class="greybox" width="10%">&nbsp;</td>
		<td class="greybox" width="13%"><s:text name="Change of use From" /> : <span class="mandatory" >*</span></td>
	<td class="greybox">	 <s:select name="usageFrom" id="usageFrom" value="%{usageFrom.id}" 
				list="dropdownData.usageList" listKey="id" listValue="name" headerKey="-1" headerValue="----choose---" />
				</td>
			 <td class="greybox" width="20%">&nbsp;</td>
		<td class="greybox" width="13%"><s:text name="Change of use To" /> : <span class="mandatory" >*</span></td>
		<td class="greybox" ><s:select name="usageTo" id="usageTo" value="%{usageTo.id}" 
				list="dropdownData.usageList" listKey="id" listValue="name" headerKey="-1" headerValue="----choose---" />
				</td>
	 	 </tr>
	 	 <tr>
				 
				  <td class="bluebox" width="20%">&nbsp;</td>
				     <td class="bluebox"><s:text name="Is Forwarded to CMDA(Y/N)" />&nbsp;<span class="mandatory">*</span>:</td>
				      <td class="bluebox"><s:select name="isForwardToCmda" id="isForwardToCmda"  value="%{isForwardToCmda}" headerValue="----Choose---" headerKey="-1" 
				                 list="#{'true':'YES','false':'NO' }" Class="bluebox" onchange="onChangeOfisForwardToCmda();" >
		               </s:select></td> 
		               <td class="bluebox" width="10%">&nbsp;</td>
		               <td class="bluebox" width="10%">&nbsp;</td>
		                <td class="bluebox" width="20%">&nbsp;</td>
		                  <td class="bluebox" width="10%">&nbsp;</td>
		                <td class="bluebox" width="20%">&nbsp;</td>
		               
				   </tr> 
	         <tr colspan="1" id="divcmdaDate" >
	 			<td class="greybox" width="20%">&nbsp;</td>
				
				<td class="greybox"><s:text name="Date of forwarded to CMDA"/><span class="mandatory">*</span>:</td>
	   			<td class="greybox"><sj:datepicker value="%{dateOfForward}" id="dateOfForward" name="dateOfForward" displayFormat="dd/mm/yy" disabled="false" showOn="focus"/></td>   	
	   			</td>		
				  <td class="greybox" width="20%">&nbsp;</td>
				  <td class="greybox" width="20%">&nbsp;</td>
				  <td class="greybox" width="20%">&nbsp;</td>
				  </tr>
				          
	      </table>
</div>
 
<div class="buttonbottom" align="center">
		<table>
		<tr>
		 <s:if test="%{mode!='view'}">
		  	<td><s:submit  cssClass="buttonsubmit" id="save" name="save" value="Save"  method="create"  onclick="return validation();"/></td>	
	  			</s:if>	         
	  		 <td><input type="button" name="close" id="close" class="button"  value="Close" onclick="window.close();"/>
	  		</td>
	  	</tr>
        </table>
   </div>	
	
	
</div>






	
	
</s:form>
</body>
</html>