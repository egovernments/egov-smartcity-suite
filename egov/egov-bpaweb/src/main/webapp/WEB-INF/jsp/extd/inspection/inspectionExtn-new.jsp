<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>
<%@ include file="/includes/taglibs.jsp" %>

<html>
<title>
	<s:text name="Site Inspection Details"/>
</title>	

<head>

<sj:head jqueryui="true" jquerytheme="redmond" loadAtOnce="true"/>
<script type="text/javascript">

jQuery.noConflict();

jQuery(document).ready(function(){ 

	if(jQuery('#autoGenInspectionDates').val()=="YES" && jQuery('#ismodify').val() == "NO"){
		var sisdates=jQuery('#siteInspectionScheduledates').val();
		var alldates=sisdates.split(",");
		var enabledDays=new Array();
	
		for (var j = 0; j < alldates.length; j++) {
			  enabledDays[j]=alldates[j];
		}
	
		function enableAllTheseDays(date) {
		    var m = date.getMonth(), d = date.getDate(), y = date.getFullYear();
		    for (i = 0; i < enabledDays.length; i++) {
		        if(jQuery.inArray(y + '-' + (m+1) + '-' + d,enabledDays) != -1) {
		            return [true];
		        }
		    }
		    return [false];
		}
		jQuery('#inspectionDate').datepicker({
		        dateFormat: 'dd/mm/yy',
		        beforeShowDay: enableAllTheseDays
		});
		
	}else{
		jQuery('#inspectionDate').datepicker({ dateFormat: 'dd/mm/yy'});
	 	jQuery('#inspectionDate').datepicker('getDate');   
	}
	jQuery('#inspectionDate').datepicker( "option", "dateFormat", "dd/mm/yy" );	
jQuery('#regdetails').find('input,select,textarea').attr('disabled','true');	  
if(jQuery('#mode').val()=='view'){
var insdate='<s:property value="InspectionDateString"/>';
var inspectionnum=jQuery("#inspectionNum").val();


		if( jQuery('#fromreg').val()=="true"){
		
           // window.opener.callinspectiondetails('<s:property value="registrationId"/>');
              }  
  
 jQuery('#inspectionDate').attr('disabled','true');	 
 jQuery('#postponementReason').attr('disabled','true');
  refreshInbox();    
  }

  jQuery("#close").removeAttr('disabled'); 
});



function validation(){
if( jQuery('#inspectionDate').val()==""){
alert("Inspection Date is mandatory")
return false;

}


var inspectionDate=document.getElementById('inspectionDate').value;	
var applicationDate=document.getElementById('applicationDate').value;	
var verifyDate=document.getElementById('verifyDate').value;


if(inspectionDate!=null && inspectionDate!="" && inspectionDate!=undefined ){

	 if( applicationDate!=null && applicationDate!="" && applicationDate!=undefined ){

		if(compareDate(applicationDate,inspectionDate) == -1 )
		{
			alert("Inspection Date should be greater than Registration Date :"+jQuery('#applicationDate').val())
		   	return false;
		}
	 }
	 if( verifyDate!=null && verifyDate!="" && verifyDate!=undefined ){	
		if(compareDate(verifyDate,inspectionDate) == -1 )
		{
			alert("Inspection Date should be greater than "+jQuery('#verifyDate').val())
		   	return false;
		}
	
	}
}

enableAllFields();
return true;
}

function enableAllFields(){
for(var i=0;i<document.forms[0].length;i++)
			{
				document.forms[0].elements[i].disabled =false;
			}

}

</script>
</head>
<body onload="">

<div class="errorstyle" id="shop_error" style="display:none;" >
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

<s:form action="inspectionExtn" theme="simple" onkeypress="return disableEnterKey(event);" >
<s:token/>
<s:push value="model">
<s:hidden id="createdBy" name="createdBy" value="%{createdBy.id}"/>
<s:hidden id="modifiedBy" name="modifiedBy" value="%{modifiedBy.id}"/>
<s:hidden id="createdDate" name="createdDate" value="%{createdDate}"/>
<s:hidden id="registration" name="registration" value="%{registration.id}"/>
<s:hidden id="registrationId" name="registrationId" value="%{registrationId}"/>
<s:hidden id="id" name="id" value="%{id}"/>
<s:hidden id="verifyDate" name="verifyDate" value="%{verifyDate}"/>
<s:hidden id="mode" name="mode" value="%{mode}"/>
<s:hidden id="inspectionNum" name="inspectionNum" value="%{inspectionNum}"/>
<s:hidden id="fromreg" name="fromreg" value="%{fromreg}"/>
<s:hidden id="autoGenInspectionDates" name="autoGenInspectionDates" value="%{autoGenInspectionDates}"/>
<s:hidden id="siteInspectionScheduledates" name="siteInspectionScheduledates" value="%{siteInspectionScheduledates}"/>
<s:hidden id="ismodify" name="ismodify" value="%{ismodify}"/>


<div align="center"> 


<div  id="regdetails" class="formmainbox">
<h1 class="subhead" ><s:text name="Registration Details"/></h1>
 <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
	   	 	<tr>
	 			<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="13%"><s:text name="inspectionlbl.applicationdate"/></td> 
	   			<td class="bluebox"> <sj:datepicker value="%{registration.planSubmissionDate}" id="applicationDate" name="registration.planSubmissionDate" displayFormat="dd/mm/yy" disabled="true" showOn="focus"/></td>   			
				<td class="bluebox" ><s:text name="inspectionlbl.sevicetype"/></td>
				<td class="bluebox" ><s:select id="serviceType" name="serviceType" list="dropdownData.servicetypeList" listKey="id" listValue="code"  value="registration.serviceType.id" headerKey="-1" headerValue="----Choose------"/></td>
	       
          </tr>
	         <tr>
	 			<td class="greybox" width="20%">&nbsp;</td>
				<td class="greybox" width="13%"><s:text name="inspectionlbl.applicantname"/></td> 
	   			<td class="greybox"><s:textfield id="applicantName" name="applicantName" value="%{registration.owner.firstName}" /></td>   			
				<td class="greybox" ><s:text name="inspectionlbl.applicantaddress"/></td>
				<td class="greybox" ><s:textarea id="applicantAddress" name="applicantAddress" value="%{registration.bpaOwnerAddress}" rows="3" cols="9" /></td>
	         </tr>
	         <tr>
	 			<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="13%"><s:text name="inspectionlbl.propertyid"/></td> 
	   			<td class="bluebox"><s:textfield id="propertyid" name="propertyid" value="%{registration.propertyid}" /></td>   			
				<td class="bluebox" ><s:text name="inspectionlbl.buildingctg"/></td>
				<td class="bluebox" ><s:select id="buildingcategory" name="buildingcategory" list="dropdownData.buildingCategoryList" listKey="id" listValue="code" headerKey="-1" headerValue="----Choose------" value="registration.regnDetails.proposedBldgCatg.id"/></td>
	         </tr>         
	      </table>
</div>
 <div id="datedetails" class="formmainbox">
  <h1 class="subhead" ><s:text name="inspectionlbl.datdtls"/></h1>
	    <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
	   	 	<tr>
	 			<td class="greybox" width="20%">&nbsp;</td>
				<td class="greybox" width="13%"><s:text name="inspectionlbl.inspectiondate"/><span class="mandatory">*</span></td> 
	   				<td class="greybox"> <s:textfield value="%{inspectionDate}" id="inspectionDate" name="inspectionDate" /></td>
	   			
				<td class="greybox" ><s:text name="inspectionlbl.reason"/></td>
				<td class="greybox" ><s:textarea id="postponementReason" name="postponementReason" value="%{postponementReason}"  cols="20" rows="3"/></td>
	         </tr>         
	      </table>
	
     
	</div> 
	</div>
	
<div class="buttonbottom" align="center">
		<table>
		<tr>
		 <s:if test="%{mode!='view'}">
		  	<td><s:submit  cssClass="buttonsubmit" id="save" name="save" value="Save" method="create" onclick="return validation()"/></td>	
	  			</s:if>	         
	  		 <td><input type="button" name="close" id="close" class="button"  value="Close" onclick="window.close();"/>
	  		</td>
	  	</tr>
        </table>
   </div>	
	
</s:push>	
	
</s:form>
</body>
</html>