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

var servictypeid=<s:property value='%{registration.serviceType.code}'/>


jQuery.subscribe('openTopicDialog', function(event,ui) {
	      
		});
    


 //var tabs = jQuery('div[id^="tab"]').tabs();
 
  


if( jQuery('#mode').val()=="view"){
for(var i=0;i<document.forms[0].length;i++)
			{
				document.forms[0].elements[i].disabled =true;
			}
			
			var inspectionnum=jQuery("#inspectionNum").val();
		if( jQuery('#fromreg').val()=="true"){
           //  window.opener.callinspectionmeasurementdetails('<s:property value="registrationId"/>');
              }  
              
       refreshInbox();       
}

   jQuery.subscribe('beforeDiv', function(event,data) {		
    });
    
    
 jQuery.subscribe('completeDiv', function(event,data) { 	
 	disable();
    });


});





function disable(){
if( jQuery('#mode').val()=="view"){
for(var i=0;i<document.forms[0].length;i++)
			{
				document.forms[0].elements[i].disabled =true;
			}
jQuery("#close").removeAttr('disabled');
}

}



function validationmethod(){
var count=0;


if(jQuery('#serviceType').val()!="2"){
 if( jQuery('#remarks').val()==""){
   showerrormsg(jQuery('#remarks'),"Inspection Remarks is mandatory");
  count++;
   }

   }
   if(count!=0)
		return false;


 if( jQuery('#inspectionDate').val()==""){
   showerrormsg(jQuery('#inspectionDate'),"Inspection Date is mandatory");
  count++;
   }
   if(count!=0)
		return false;

   
   	var todaysDate=getTodayDate();
	var inspectionDate=document.getElementById('inspectionDate').value;	
	var applicationDate=document.getElementById('applicationDate').value;	
	var lastInspectionDate=document.getElementById('lastInspectionDate').value;
	var surroundedByNrth=jQuery('#surroundedByNorth').val();
	var surroundedBySuth=jQuery('#surroundedBySouth').val();	
   
 // if(new Date(jQuery('#inspectionDate').val()).getTime() < new Date(jQuery('#applicationDate').val()).getTime() ){
 	if(inspectionDate!=null && inspectionDate!="" && inspectionDate!=undefined &&
 		 	 applicationDate!=null && applicationDate!="" && applicationDate!=undefined ){

 		if(compareDate(inspectionDate,todaysDate) == -1)
		{	
		    if(lastInspectionDate==""){					  	 	
 			showerrormsg(jQuery('#inspectionDate'),"Inspection Date should not be greater than Todays Date ")
			count++;
			}
		}
 		
	 	if(compareDate(applicationDate,inspectionDate) == -1 )
		{
		   	showerrormsg(jQuery('#inspectionDate'),"Inspection Date should be greater than Registration Date :"+jQuery('#applicationDate').val())
			count++;
		}
	}
 	
 	if(surroundedByNrth!=undefined && (surroundedByNrth==null || surroundedByNrth=="-1")){
 		showerrormsg(jQuery('#surroundedByNorth'),"surrounded By North is mandatory")
		return false;
	}
 	if(surroundedBySuth!=undefined &&  (surroundedBySuth==null || surroundedBySuth=="-1")){
 		showerrormsg(jQuery('#surroundedBySouth'),"surrounded By South is mandatory")
		return false; 
	}
	if(inspectionDate!=null && inspectionDate!="" && inspectionDate!=undefined 
			&& lastInspectionDate!=null && lastInspectionDate!="" && lastInspectionDate!=undefined ){		
		if(jQuery('#lastInspectionDate').val()!=""){
			//if(new Date(jQuery('#inspectionDate').val()).getTime() <= new Date(jQuery('#lastInspectionDate').val()).getTime() ){
			 if(compareDate(lastInspectionDate,inspectionDate) == -1){
			   showerrormsg(jQuery('#inspectionDate'),"Inspection Date should be greater than previous Inspection Date :"+jQuery('#lastInspectionDate').val())
			count++;
			}
		}
	}



if(count!=0)
		return false;
		
    jQuery("[id=numbers]").each(function(index) {	
	var values=(jQuery(this).find('input').attr("value"));
	if(values!=null&&values!=""){
   		var check=checkUptoTwoDecimalPlace(values,"shop_error",jQuery(this).find('input').attr("id"),jQuery(this).find('input'));
   		
   		if(check==false){
   		count++;
   		}
   		}
   		});
   		
   		if(count!=0)
		return false;
   	jQuery("[id=wholenumbers]").each(function(index) {	
	var values=(jQuery(this).find('input').attr("value"));
	if(values!=null&&values!=""){
   		var check=checkSpecialCharacters(values,"shop_error",jQuery(this).find('input').attr("id"),jQuery(this).find('input'));
   			if(check==false){
   		count++;
   		}
   			var check=checkNumbers(values,"shop_error",jQuery(this).find('input').attr("id"),jQuery(this).find('input'))
   			if(check==false){
   		count++;
   		}
   		
   		}
   		});	
   		
   		if(count!=0)
		return false;
   	jQuery('input[type=text], textarea').each(function(index) {	
	var values=(jQuery(this).attr("value"));
	if(values!=null&&values!=""){	
	if(values.length>256){
   		showerrormsg(jQuery(this),"Size cannot be greater than 256 characters");
   		 count++;
   		 }
   		}
   		});		
   		
   		if(count!=0)
		return false;
		
   jQuery("[id=mandatorycheck]").each(function(index) {	
  
	var values=(jQuery(this).find('input').attr("value"));
	if(values=='true'){
   		if(jQuery(this).find(':checkbox').prop('checked')){
   		}else{ 		
   		
   		jQuery(this).find(':checkbox').css('outline-color', 'red');
		jQuery(this).find(':checkbox').css('outline-style', 'solid');
		jQuery(this).find(':checkbox').css('outline-width', 'thin');
		showerrormsgwithoutcolor("Please select the other details")
	    count++;
   		}
   		}
   		});		
   		
   		if(count!=0)
		return false;


    jQuery("[id=mandatoryfields]").each(function(index) {	
  
	var values=(jQuery(this).next('td').find('input').attr("value"));
	if(values==""){
	showerrormsg(jQuery(this).next('td').find('input'),"Please enter all mandatory fields");
	 count++;
	}
   		});			


if(count==0){
return true;

}else return false;

return false; 
}


function validation(){

  hideFieldError();
  dom.get("shop_error").style.display='none';

  if(!validationmethod()){
  return false;
  }else {
  
return true;
 
}
}


function showerrormsg(obj,msg){
dom.get("shop_error").style.display = '';
document.getElementById("shop_error").innerHTML =msg;
jQuery(obj).css("border", "1px solid red");		
return false;
}


function showerrormsgwithoutcolor(msg){
dom.get("shop_error").style.display = '';
document.getElementById("shop_error").innerHTML =msg;
return false;
}

function hideFieldError()
  {
			if (dom.get("fieldError") != null)
			dom.get("fieldError").style.display = 'none';
			
			jQuery("[id=datedetails]").each(function(index) {
			jQuery(this).find('input').css("border", "");
			});
			
		jQuery("[id=numbers]").each(function(index) {
		jQuery(this).find('input').css("border", "");
		});
		
		  jQuery("[id=mandatorycheck]").each(function(index) {	
   		jQuery(this).find(':checkbox').css('outline-color', '');
		jQuery(this).find(':checkbox').css('outline-style', '');
		jQuery(this).find(':checkbox').css('outline-width', '');
   		
   		});	
   		
   		  jQuery("[id=mandatoryfields]").each(function(index) {	
   		  jQuery(this).next('td').find('input').css("border", "");
   		  });	
 }
 
 function enableFields(){
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
			<s:fielderror/>
		</div>
</s:if>

<s:if test="%{hasActionMessages()}">
		<div class="errorstyle">
				<s:actionmessage />
		</div>
</s:if>

<s:form action="inspection" theme="simple" onkeypress="return disableEnterKey(event);" onsubmit="enableFields();">
<s:token/>
<s:push value="model">
<s:hidden id="createdBy" name="createdBy" value="%{createdBy.id}"/>
<s:hidden id="modifiedBy" name="modifiedBy" value="%{modifiedBy.id}"/>
<s:hidden id="createdDate" name="createdDate" value="%{createdDate}"/>
<s:hidden id="registration" name="registration" value="%{registration.id}"/>
<s:hidden id="id" name="id" value="%{id}"/>
<s:hidden id="inspectionDetails" name="inspectionDetails" value="%{inspectionDetails.id}"/>
<s:hidden id="inspectedBy" name="inspectedBy" value="%{inspectedBy.id}"/>
<s:hidden id="mode" name="mode" value="%{mode}"/>
<s:hidden id="fromreg" name="fromreg" value="%{fromreg}"/>
<s:hidden id="lastInspectionDate" name="lastInspectionDate" value="%{lastInspectionDate}"/>
<s:hidden id="serviceTypeCode" name="serviceTypeCode" value="%{serviceTypeCode}"/>


<div align="center"> 

<div align="center" id="tab0"> 
 <div id="datedetails" class="formmainbox">
  <h1 class="subhead" ><s:text name="Inspection details"/></h1>

  <div id="header" class="formmainbox">
	    <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
	    	<tr>
	 			<td class="greybox" width="20%">&nbsp;</td>
				<td class="greybox" width="13%"><s:text name="inspectionlbl.applicationdate"/></td> 
	   			<td class="greybox"> <sj:datepicker value="%{registration.planSubmissionDate}" id="applicationDate" name="registration.planSubmissionDate" displayFormat="dd/mm/yy" disabled="true" showOn="focus"/></td>   			
				<td class="greybox" ><s:text name="inspectionlbl.sevicetype"/></td>
				<td class="greybox" ><s:select id="serviceType" name="serviceType" list="dropdownData.servicetypeList" listKey="id" listValue="code" disabled="true"  value="registration.serviceType.id" headerKey="-1" headerValue="----Choose------"/></td>
	       
          </tr>
	        
	         
	   	 	<tr>
	 			<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="13%"><s:text name="inspectionlbl.siteinspecteddate"/><span class="mandatory">*</span></td> 
	   			<td class="bluebox"> <sj:datepicker   value="%{inspectionDate}" id="inspectionDate" name="inspectionDate" displayFormat="dd/mm/yy" showOn="focus" /></td>   			
				<td class="bluebox" ><s:text name="inspectionlbl.siteinspectedby"/></td>
				<td class="bluebox" ><s:textfield  id="inspectedbyname" name="inspectedBy.userName" value="%{inspectedBy.userName}" /></td>
				
	         </tr>  
	       
          	<tr>
	 			<td class="greybox" width="20%">&nbsp;</td>
				<td class="greybox" width="13%"><s:text name="inspectionlbl.siteinspectedremarks"/></td>
	   			<td class="greybox"><s:textarea  id="remarks" name="inspectionDetails.remarks" value="%{inspectionDetails.remarks}" cols="20" rows="3"/></td>   			
				<td class="greybox" ><s:text name="Inspection Number"/></td>
				<td class="greybox" ><s:textfield  id="inspectionNum" name="inspectionNum" value="%{inspectionNum}" disabled="true"/></td>
			
	         </tr>  
	      </table>
	 </div>
	 
	</div>
	  </div> 
	   <s:url id="ajax" value="/inspection/inspection!showPlanDetails.action"  escapeAmp="false">
	   <s:param name="serviceTypeId" value="%{registration.serviceType.id}"></s:param>	 
	     <s:param name="inspectionId" value="%{inspectionId}"></s:param>	         
	   </s:url>
   
       <sj:div href="%{ajax}" indicator="indicator"  cssClass="formmainbox" id="tab1"  dataType="html" onCompleteTopics="completeDiv">
        <img id="indicator" src="<egov:url path='/images/loading.gif'/>" alt="Loading..." style="display:none"/>
      </sj:div> 
        
      <s:url id="consajax" value="/inspection/inspection!showConstructionDetails.action"  escapeAmp="false">
	   <s:param name="serviceTypeId" value="%{registration.serviceType.id}"></s:param>	   
	   <s:param name="inspectionId" value="%{inspectionId}"></s:param>	   
	   </s:url>
       <sj:div href="%{consajax}" indicator="indicator"  cssClass="formmainbox" id="tab2" dataType="html" onCompleteTopics="completeDiv">
        <img id="indicator" src="<egov:url path='/images/loading.gif'/>" alt="Loading..." style="display:none"/>
      </sj:div> 
    
     <s:url id="checklistajax" value="/inspection/inspection!showCheckList.action"  escapeAmp="false">
	  <s:param name="serviceTypeId" value="%{registration.serviceType.id}"></s:param>	
	   <s:param name="inspectionId" value="%{inspectionId}"></s:param>	      
	   </s:url>
       <sj:div href="%{checklistajax}" indicator="indicator"  cssClass="formmainbox" id="tab3"  dataType="html" onCompleteTopics="completedchecklistDiv">
        <img id="indicator" src="<egov:url path='/images/loading.gif'/>" alt="Loading..." style="display:none"/>
    </sj:div>
 
    <s:url id="plotdetajax" value="/inspection/inspection!showPlotDetails.action"  escapeAmp="false">
	  <s:param name="serviceTypeId" value="%{registration.serviceType.id}"></s:param>	
	   <s:param name="inspectionId" value="%{inspectionId}"></s:param>	      
	   </s:url>
       <sj:div href="%{plotdetajax}" indicator="indicator"  cssClass="formmainbox" id="tab4"  dataType="html" onCompleteTopics="completeDiv">
        <img id="indicator" src="<egov:url path='/images/loading.gif'/>" alt="Loading..." style="display:none"/>
    </sj:div>
    
	 
	
  <div class="buttonbottom" align="center">
		<table>
		<tr>
		 <s:if test="%{mode!='view'}">
		  	<td><s:submit  cssClass="buttonsubmit" id="save" name="save" value="Save" method="createSiteInspection" onclick="return validation()"/></td>	
	  			</s:if>	         
	  		 <td><input type="button" name="close" id="close" class="button"  value="Close" onclick="window.close();"/>
	  		</td>
	  	</tr>
        </table>
   </div>	
	
	
</div>

</s:push>	
	
</s:form>
</body>
</html>