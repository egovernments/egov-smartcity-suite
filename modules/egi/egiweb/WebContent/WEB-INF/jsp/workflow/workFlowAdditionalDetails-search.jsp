<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>
<html>
<title>

	<s:text name="WorkFlowDetails"/>

</title>	
<head>
 <link rel="stylesheet" type="text/css" href="<c:url value='/css/workflow.css'/>" />
 <script type="text/javascript" src="<c:url value='/javascript/jquery/jquery-1.7.2.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/javascript/jquery/jquery-ui-1.8.22.custom.min.js'/>"></script>
<script>
jQuery.noConflict();

jQuery(document).ready(function(){
jQuery('#objectType').change(function()
   {
   
   if(jQuery(this).attr('value')!="-1"){
 
  
	var i=0;
	var objectTypeid=jQuery('#objectType').val();
	var mySelect =jQuery('#additionalRule');	   
	
	jQuery('#additionalRule').empty()
	mySelect.append(jQuery('<option></option>').val("-1").html("-----Choose------"))
	var jqxhr =jQuery.ajax({  
        url: "<%=request.getContextPath()%>/workflow/workFlowAdditionalDetails!getAdditionalRulesforObject.action",
        data: {           
            objectType:objectTypeid
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


function validate(){
if(jQuery('#objectType').val()=="-1"){
	alert("Please select objectType");
return false;
}

return true;

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

<s:form action="workFlowAdditionalDetails" theme="simple" onkeypress="return disableEnterKey(event);" >



<div class="formheading"/></div>
  
 	
	<div class="formmainbox" id="headerinfo">
 <s:hidden id="mode" name="mode" value="%{mode}"/>
	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	   	 <tr>
					<td class="greybox" width="15%">&nbsp;</td>			
					<td class="greybox" ><s:text name="Object Type"/><span class="mandatory">*</span></td> 
					<td class="greybox"><span style="display:none;" class="mandatory"></span><s:select id="objectType" name="objectType"  list="dropdownData.objectTypeList" listKey="id" listValue="displayName" headerKey="-1" headerValue="-----Choose-----" /></td>    
					<td class="greybox" ><s:text name="Additional Rule"/></td> 
					<td class="greybox"><s:select id="additionalRule" list="dropdownData.additionalRuleList" name="additionalRules" multiple="false" headerKey="-1" headerValue="-------Choose-------" listKey="additionalRule"  listValue="additionalRule"  /></td>
					
					
					<td class="greybox">&nbsp;</td>		
					
		</tr>
	</table>
	
	 <div class="buttonbottom" align="center" id="workFlowButtons">
		<table>
		<tr>      
	  
	  			<td><s:submit  cssClass="buttonsubmit" id="Create" name="Create" value="Create "  method="buildWorkFlowDetails"  /></td>
	  	        <td><s:submit  cssClass="buttonsubmit" id="View" name="View" value="View "  method="view"  onclick="return validate()"/></td>
	  			<td><s:submit  cssClass="buttonsubmit" id="Modify" name="Modify" value="Modify "  method="modify" onclick="return validate()"/></td>
	  
	  		
	  	</tr>
        </table>
   </div>
	</div>
	
</s:form>
</body>
</html>