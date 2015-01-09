
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
 <head> 
 <link rel="stylesheet" type="text/css" href="<c:url value='/css/workflow.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/jquery/jquery.multiselect.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/jquery/jquery-ui-1.8.22.custom.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/jquery/jquery.multiselect.filter.css'/>" />
<script type="text/javascript" src="<c:url value='/javascript/jquery/jquery-1.7.2.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/javascript/jquery/jquery-ui-1.8.22.custom.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/javascript/jquery/jquery.multiselect.min.js'/>"></script>
<jsp:include page='/WEB-INF/jsp/workflow/workFlowMatrix.jsp'/> 
  <s:if test="%{mode!='modify'}">
 <title>Create WorkFlow</title> 
</s:if>
<s:else>
 <title>Modify WorkFlow</title>
</s:else>
<script type="text/javascript">


jQuery.noConflict();

jQuery(document).ready(function(){
enableAmount();
	var mode = jQuery("#mode").val();
		
	
	
	jQuery("#department").multiselect();

		if(mode=='edit'||mode=='view'||mode=='modify'){

		jQuery("#department").multiselect("widget").find(':checkbox').attr('disabled','true');	
	    jQuery('#headerinfo').find('input,select').attr('disabled','true');	    
	    jQuery('#cal1').each(function(index) {
   			jQuery(this).hide();
			});
		jQuery('#cal2').each(function(index) {
   		jQuery(this).hide();
			});
		jQuery('#Proceed').hide();
		jQuery('#wfdetails').show();
		enableMultiSelectsforLinedetails();	
		
       
	}  else if(mode==""||mode==null){
	jQuery('#wfdetails').hide();	
	}
	
	
	jQuery('#objectType').change(function()
   {
   
   if(jQuery(this).attr('value')!="-1"){
 
  
	var i=0;
	var objectTypeid=jQuery('#objectType').val();
	var mySelect =jQuery('#additionalRule');	   
	
	jQuery('#additionalRule').empty()
	mySelect.append(jQuery('<option></option>').val("-1").html("-----Choose------"))
	var jqxhr =jQuery.ajax({  
        url: "<%=request.getContextPath()%>/workflow/workFlowMatrix!getAdditionalRuleforObjecttype.action",
        data: {           
            objectTypeid:objectTypeid
        }
    })
    .success (function(data) { 
   
    for(i=0;i<data.ResultSet.Result.length;i++){
        
   		mySelect.append(jQuery('<option></option>').val(data.ResultSet.Result[i].Id).html(data.ResultSet.Result[i].Description));
   	}
    
    
    })
    .error   (function(){ alert("Unable to Load AdditionalRule"); })
    .complete(function(){ })
    ;
  }else{
	  jQuery('#additionalRule').empty()
jQuery('#additionalRule').append(jQuery('<option></option>').val("-1").html("-----Choose------"))
  }

    });
	
	
	});

function enableMultiSelectsforLinedetails(){

	
jQuery('select[id^="designation"]').each(function(index) {
jQuery(this).multiselect({
         open: function(){
          
            
            jQuery('select[id^="designation"]').each(function(index) {
            
            jQuery(this).multiselect('disable');
            });
             jQuery(this).multiselect('enable');
            
   }, beforeclose: function(){
     jQuery('select[id^="designation"]').each(function(index) {
            
            jQuery(this).multiselect('enable');
            });
       }   		
   		  }
   		);

if(document.getElementById('mode').value=='view'){
  
		jQuery(this).multiselect("widget").find(':checkbox').attr('disabled','true');	
}
}); 



jQuery('select[id^="rejectDesignation"]').each(function(index) {
	jQuery(this).multiselect({
	         open: function(){
	          
	            
	            jQuery('select[id^="rejectDesignation"]').each(function(index) {
	            
	            jQuery(this).multiselect('disable');
	            });
	             jQuery(this).multiselect('enable');
	            
	   }, beforeclose: function(){
	     jQuery('select[id^="rejectDesignation"]').each(function(index) {
	            
	            jQuery(this).multiselect('enable');
	            });
	       }   		
	   		  }
	   		);

	if(document.getElementById('mode').value=='view'){
	  
			jQuery(this).multiselect("widget").find(':checkbox').attr('disabled','true');	
	}
	}); 
jQuery('select[id^="buttons"]').each(function(index) {
	jQuery(this).multiselect({
	         open: function(){
	          
	            
	            jQuery('select[id^="buttons"]').each(function(index) {
	            
	            jQuery(this).multiselect('disable');
	            });
	             jQuery(this).multiselect('enable');
	            
	   }, beforeclose: function(){
	     jQuery('select[id^="buttons"]').each(function(index) {
	            
	            jQuery(this).multiselect('enable');
	            });
	       }   		
	   		  }
	   		);
	if(document.getElementById('mode').value=='view'){
           
			jQuery(this).multiselect("widget").find(':checkbox').attr('disabled','true');	
	}
	}); 


jQuery('select[id^="rejectButtons"]').each(function(index) {
	jQuery(this).multiselect({
	         open: function(){
	          
	            
	            jQuery('select[id^="rejectButtons"]').each(function(index) {
	            
	            jQuery(this).multiselect('disable');
	            });
	             jQuery(this).multiselect('enable');
	            
	   }, beforeclose: function(){
	     jQuery('select[id^="rejectButtons"]').each(function(index) {
	            
	            jQuery(this).multiselect('enable');
	            });
	       }   		
	   		  }
	   		);
	if(document.getElementById('mode').value=='view'){
           
			jQuery(this).multiselect("widget").find(':checkbox').attr('disabled','true');	
	}
	}); 


}
function init(){

	if(document.getElementById("mode").value=='view'){
		disableAllFields();
		enableMultiSelectsforLinedetails();	
	}
}


function validate(){
  if(validateForm()){
	  
  enableAllFields();
  return true
  }else

return false;      
}


function enableAllFields(){
for(var i=0;i<document.forms[0].length;i++)
			{
				document.forms[0].elements[i].disabled =false;
			}

}

function disableAllFields(){
	
	 jQuery('#headerinfo').find('input,select').attr('disabled','true');	 
	 jQuery('#wfdetails').find('input').attr('disabled','true');	    
	 jQuery('select[id^="state"]').each(function(index) {
		 jQuery(this).attr("disabled", "disabled");
	 });
	 jQuery('select[id^="status"]').each(function(index) {
		 jQuery(this).attr("disabled", "disabled");
	 });
	 jQuery('select[id^="action"]').each(function(index) {
		 jQuery(this).attr("disabled", "disabled");
	 });
	 jQuery('select[id^="rejectAction"]').each(function(index) {
		 jQuery(this).attr("disabled", "disabled");
	 });
	 jQuery('select[id^="rejectState"]').each(function(index) {
		 jQuery(this).attr("disabled", "disabled");
	 });
	 jQuery('select[id^="rejectStatus"]').each(function(index) {
		 jQuery(this).attr("disabled", "disabled");
	 });
	}

function enableAmount(){
if(document.getElementById('amountRule').checked)
{
document.getElementById('fromAmount').disabled=false;
document.getElementById('toAmount').disabled=false;
}else{
document.getElementById('fromAmount').value="";
document.getElementById('toAmount').value="";
document.getElementById('fromAmount').disabled=true;
document.getElementById('toAmount').disabled=true;
}

}
</script>
</head>
<body onload="init();">

<s:if test="%{hasActionErrors()}">
        <div class="errorstyle">
        
         <s:actionerror/>
                 </div>
</s:if>		
<s:if test="%{hasActionMessages()}">
        <div class="errorstyle">
        
         <s:actionmessage/>
                 </div>
</s:if>	

	<div class="formheading">
	
   <s:form action="workFlowMatrix" theme="simple" onsubmit="">
  <s:token/>  
  
	<s:hidden id="mode" name="mode" value="%{mode}"/>
	<s:hidden id="id" name="id" value="%{id}" />
	<s:hidden id="modifyToDate" name="modifyToDate" value="%{modifyToDate}"/>
	<s:hidden id="objectTypeView" name="objectTypeView" value="%{objectTypeView}"/>
	<s:hidden id="fromDateView" name="fromDateView" value="%{fromDateView}"/>
	<s:hidden id="toDateView" name="toDateView" value="%{toDateView}"/>
	<s:hidden id="fromQtyView" name="fromQtyView" value="%{fromQtyView}"/>
	<s:hidden id="toQtyView" name="toQtyView" value="%{toQtyView}"/>
	<s:hidden id="additionalRuleView" name="additionalRuleView" value="%{additionalRuleView}"/>
	<s:hidden id="legacyDate" name="legacyDate" value="%{legacyDate}"/>
	
	<div class="formmainbox" id="headerinfo">

	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	   	 <tr>
					<td class="greybox" width="15%">&nbsp;</td>
				
					<td class="greybox" ><s:text name="Object Type"/><span class="mandatory">*</span></td> 
					 <td class="greybox"><s:select id="objectType" name="objectType"  list="dropdownData.objectTypeList" listKey="id" listValue="displayName" headerKey="-1" headerValue="-----Choose-----" value="objectType" /></td>    
					
					<td class="greybox" ><s:text name="Additional Rule"/></td> 
					<td class="greybox"><s:select id="additionalRule" list="dropdownData.additionalRuleList" name="additionalRule" multiple="false" headerKey="-1" headerValue="-------Choose-------" listKey="additionalRule"  listValue="additionalRule"  value="additionalRuleSelected" /></td>
					
					
					<td class="greybox">
						&nbsp;
					</td>   
				</tr>
				
				 <tr>
					
					<td class="greybox" width="15%">&nbsp;</td>		
					 <td class="greybox"><s:text name="From Date"/><span class="mandatory">*</span></td>	
					<td class="greybox"><s:date name="fromDate"  format="dd/MM/yyyy" var="fromdateTemp" />
					<s:textfield name="fromDate" id="fromDate" value="%{fromdateTemp}" maxlength="20" onblur="validateDateFormat(this);"  onkeyup="DateFormat(this,this.value,event,false,'3')"  />
					<a href="javascript:show_calendar('forms[0].fromDate');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true; "><img id="cal1" src="${pageContext.request.contextPath}/images/calendaricon.gif" border="0" align="absmiddle" /></a>
					</td><%--
					 <td class="greybox"><s:text name="To Date"/></td>	
					<td class="greybox"><s:date name="toDate"  format="dd/MM/yyyy" var="todateTemp"/>
				    <s:textfield name="toDate" id="toDate" value="%{todateTemp}" maxlength="20" onblur="validateDateFormat(this);"  onkeyup="DateFormat(this,this.value,event,false,'3')"  />
					<a href="javascript:show_calendar('forms[0].toDate');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true; "><img id="cal2" src="${pageContext.request.contextPath}/images/calendaricon.gif" border="0"  align="absmiddle" /></a>
					</td>
					--%>
					<td class="greybox">&nbsp;</td>
					<td class="greybox">&nbsp;</td>
					<td class="greybox">&nbsp;</td>
					
				</tr>
				
	         <tr>
					
					<td class="greybox" width="15%">&nbsp;</td>    
					<td class="greybox"><s:text name="Departments"/><span class="mandatory">*</span></td> 					
					 <td class="greybox"><s:select id="department" name="department" multiple="true" list="dropdownData.departmentList" listKey="deptName" listValue="deptName"  value="departmentSelected" /></td>
					<td class="greybox"><s:text name="Amount Rule"/></td> 
					<td class="greybox"><s:checkbox id="amountRule"  name="amountRule"    value="%{amountRule}" onchange="enableAmount()"/></td>			
					<td class="greybox">
						&nbsp;
					</td>
				</tr>			  
	             
	             <tr>
					
					<td class="greybox" >&nbsp;</td>		
					<td class="greybox"><s:text name="From Amount"/></td> 
					<td class="greybox"><s:textfield id="fromAmount" name="fromAmount" value="%{fromAmount}" /> </td>
					<td class="greybox"><s:text name="To Amount"/></td> 
					<td class="greybox"><s:textfield id="toAmount" name="toAmount" value="%{toAmount}" /> </td>
					<td class="greybox">&nbsp;</td>
				</tr>
				
	 
	      </table>   
	</div>
	 <div class="buttonbottom" align="center" id="workFlowButtons">
		<table>
		<tr>      
	  
	  			<td><s:submit  cssClass="buttonsubmit" id="Proceed" name="Proceed" value="Proceed" onclick="return validate()" method="buildWorkflow" /></td>
	  	
	  		
	  	</tr>
        </table>
   </div>
   
   <div id="wfdetails">
  <div class="blueshadow"></div>
	<h1 class="subhead" ><s:text name="WorkFlow Details"/></h1>
	<div align="center">
		
		<table id="workflowDetailstbl" width="90%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
	      <tr>
			<th  class="bluebgheadtd" width="5%"><div align="center">Approver Level</div></th>
			 <th  class="bluebgheadtd" width="10%"><div align="center">Next State</div></th>	
		    <th  class="bluebgheadtd" width="10%"><div align="center">Next Designation</div></th>		   
		    <th  class="bluebgheadtd" width="20%"><div align="center">Next Action</div></th>		    
		    <th  class="bluebgheadtd" width="10%"><div align="center">Next Status</div></th>	
		    <th  class="bluebgheadtd" width="5%"><div align="center">Buttons</div></th>	
		    <s:if test="%{mode!='view'}">	    
		    <th  class="bluebgheadtd" width="5%"><div align="center"> Add</div></th>
		    <th  class="bluebgheadtd" width="5%"><div align="center"> Delete</div></th>
		    </s:if>
		 </tr>
		  <s:iterator value="workFlowMatrixDetails" status="row_status">
		    <tr>
		  	<td  class="blueborderfortd"><s:textfield name="workFlowMatrixDetails[%{#row_status.index}].approverNo" id="approverNo" readonly="true"  cssClass="tablerow" value="%{#row_status.count}" cssStyle="text-align:center"/></td>
		   
		    <td class="blueborderfortd"><div align="center">		    		
		    <s:select id="state" name="workFlowMatrixDetails[%{#row_status.index}].state"  list="dropdownData.stateList"  headerKey="-1" headerValue="----Choose----"  />
		    </div>
		    </td>	 
		       <td class="blueborderfortd"><div align="center">		    		
		    <s:select id="designation" name="workFlowMatrixDetails[%{#row_status.index}].designation"  list="dropdownData.designationList" listKey="designationName" multiple="true" listValue="designationDescription"  />
		    </div>
		    </td>  
		     <td  class="blueborderfortd" ><div align="center">
		    <s:select id="action" name="workFlowMatrixDetails[%{#row_status.index}].action"  list="dropdownData.actionList"  headerKey="-1" headerValue="----Choose----"  />
		    </div>
		    </td> 
		    <td class="blueborderfortd"><div align="center">		    		
		   <s:select id="status" name="workFlowMatrixDetails[%{#row_status.index}].status"  list="dropdownData.statusList"  headerKey="-1" headerValue="----Choose----"  />
		    </div>
		    </td>
		    <td class="blueborderfortd"><div align="center">		    		
		    <s:select id="buttons" name="workFlowMatrixDetails[%{#row_status.index}].buttons"  list="dropdownData.buttonList"   multiple="true" />
		    </div>
		    </td>
		    	 <s:if test="%{mode!='view'}">
		    	<td  class="blueborderfortd"><div align="center"><a href="#"><img src="${pageContext.request.contextPath}/images/addrow.gif" height="16"  width="16" border="2" alt="Add" onclick="addRow();"></a></div></td>
		   		<td  class="blueborderfortd"><div align="center"><a href="#"><img src="${pageContext.request.contextPath}/images/removerow.gif" height="16"  width="16" border="2" alt="Delete" onclick="removeRow(this);"></a></div></td>		  
		    </s:if>
		    	
		    </tr>
		    </s:iterator>
	  </table>
	</div>
	
	 
	 
	
   <div class="blueshadow"></div>
	<h1 class="subhead" ><s:text name="Rejection Details"/></h1>
	<div align="center">
   <table id="rejectiontable" width="80%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
	      <tr>
			 <th  class="bluebgheadtd" width="10%"><div align="center">Sl No</div></th>	
			 <th  class="bluebgheadtd" width="10%"><div align="center">Next State</div></th>	
		    <th  class="bluebgheadtd" width="10%"><div align="center">Next Designation</div></th>		   
		    <th  class="bluebgheadtd" width="20%"><div align="center">Next Action</div></th>		    
		    <th  class="bluebgheadtd" width="10%"><div align="center">Next Status</div></th>	
		    <th  class="bluebgheadtd" width="5%"><div align="center">Buttons</div></th>	
		     <s:if test="%{mode!='view'}">	    
		    <th  class="bluebgheadtd" width="5%"><div align="center"> Add</div></th>
		    <th  class="bluebgheadtd" width="5%"><div align="center"> Delete</div></th>
		    </s:if>
		 </tr>
		 <s:iterator value="workFlowMatrixRejectDetails" status="reject_status">
		     <tr>
		  	<td  class="blueborderfortd"><s:textfield name="workFlowMatrixRejectDetails[%{#reject_status.index}].rejectApproverNo" id="rejectApproverNo" readonly="true"  cssClass="tablerow" value="%{#reject_status.count}" cssStyle="text-align:center"/></td>
		   
		    <td class="blueborderfortd"><div align="center">		    		
		    <s:select id="rejectState" name="workFlowMatrixRejectDetails[%{#reject_status.index}].rejectState"  list="dropdownData.stateList"  headerKey="-1" headerValue="----Choose----"  />
		    </div>
		    </td>	 
		       <td class="blueborderfortd"><div align="center">		    		
		    <s:select id="rejectDesignation" name="workFlowMatrixRejectDetails[%{#reject_status.index}].rejectDesignation"  list="dropdownData.designationList" listKey="designationName" multiple="true" listValue="designationDescription"  />
		    </div>
		    </td>  
		     <td  class="blueborderfortd" ><div align="center">
		    <s:select id="rejectAction" name="workFlowMatrixRejectDetails[%{#reject_status.index}].rejectAction"  list="dropdownData.actionList"  headerKey="-1" headerValue="----Choose----"  />
		    </div>
		    </td> 
		    	<td class="blueborderfortd"><div align="center">		    		
		    <s:select id="rejectStatus" name="workFlowMatrixRejectDetails[%{#reject_status.index}].rejectStatus"  list="dropdownData.statusList"  headerKey="-1" headerValue="----Choose----"  />
		    </div>
		    </td>
		    <td class="blueborderfortd"><div align="center">		    		
		    <s:select id="rejectButtons" name="workFlowMatrixRejectDetails[%{#reject_status.index}].rejectButtons"  list="dropdownData.buttonList"   multiple="true" />
		    </div>
		    </td>
		    	 <s:if test="%{mode!='view'}">
		    	<td  class="blueborderfortd"><div align="center"><a href="#"><img src="${pageContext.request.contextPath}/images/addrow.gif" height="16"  width="16" border="2" alt="Add" onclick="addRowReject();"></a></div></td>
		   		<td  class="blueborderfortd"><div align="center"><a href="#"><img src="${pageContext.request.contextPath}/images/removerow.gif" height="16"  width="16" border="2" alt="Delete" onclick="removeRowReject(this);"></a></div></td>		  
		    </s:if>
		    	
		    </tr>
		   </s:iterator>
	  </table>
    </div>
	
	
	<div>
	
	 <table id="test" width="80%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
	 </table>
	</div>
	
	
	 <div class="buttonbottom" align="center" id="workFlowButtons">
		<table>
		<tr>      
	  <s:if test="%{mode!='view'}">
	  			<td><s:submit  cssClass="buttonsubmit" id="Create WorkFlow" name="Create WorkFlow" value="Create WorkFlow"  method="createWorkFlowforobjects"  onclick="return validate()"/></td>
	  	</s:if>
	  	<s:else>
	  	 <td><input type="button" name="close" id="close" class="button"  value="Close" onclick="window.close();"/></td>
	  	</s:else>
	  		
	  	</tr>
        </table>
   </div>
  </div>
   
   
   

   
  </s:form>
  </div>
  
 
</body>
   
</html>
