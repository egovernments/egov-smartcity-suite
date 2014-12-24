<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>
<html>
<title>
<s:if test="%{mode=='view'}">
	<s:text name="View WorkFlowDetails"/>
</s:if>
<s:elseif test="%{mode=='modify'}">
	<s:text name="Modify WorkFlowDetails"/>
</s:elseif> 
 <s:else>
 <s:text name="Create WorkFlowDetails"/>
 </s:else>

</title>	

<head>
 <link rel="stylesheet" type="text/css" href="<c:url value='/css/workflow.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/jquery/jquery.multiselect.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/jquery/jquery-ui-1.8.22.custom.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/jquery/jquery.multiselect.filter.css'/>" />
 <style>
        body { font-size: 62.5%; }
        label, input { display:block; }
        input.text { margin-bottom:12px; width:95%; padding: .4em; }
        fieldset { padding:0; border:0; margin-top:25px; }
        h1 { font-size: 1.2em; margin: .6em 0; }
        div#users-contain { width: 350px; margin: 20px 0; }
        div#users-contain table { margin: 1em 0; border-collapse: collapse; width: 100%; }
        div#users-contain table td, div#users-contain table th { border: 1px solid #eee; padding: .6em 10px; text-align: left; }
        .ui-dialog .ui-state-error { padding: .3em; }
        .validateTips { border: 1px solid transparent; padding: 0.3em; }
    </style>
<script type="text/javascript" src="<c:url value='/javascript/jquery/jquery-1.7.2.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/javascript/jquery/jquery-ui-1.8.22.custom.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/javascript/jquery/jquery.multiselect.min.js'/>"></script>
<script type="text/javascript">

jQuery.noConflict();

jQuery(document).ready(function(){

 jQuery("#actionlist").multiselect({
   noneSelectedText: 'Actions Selected',
   header:'To delete,check and press delete button'
});

 jQuery("#statelist").multiselect({
  noneSelectedText: 'States Selected',
   header:'To delete,check and press delete button'
});

 jQuery("#buttonlist").multiselect({
  noneSelectedText: 'Buttons Selected',
   header:'To delete,check and press delete button'
});

 jQuery('select[id*="list"]').each(function(index) {
	
   		jQuery(this).multiselect("uncheckAll");
   		});

if(jQuery("#mode").val()=="view"){

 jQuery('select[id*="list"]').each(function(index) {
	
   		jQuery(this).multiselect("uncheckAll");
   		jQuery(this).multiselect("widget").find(':checkbox').each(function(index){
   		jQuery(this).hide();
   		});
			});
	jQuery('button[id^="create-button"]').each(function(index) {
   		jQuery(this).hide();
			});		
		jQuery('button[id^="delete-button"]').each(function(index) {
   		jQuery(this).hide();
			});			
			 jQuery('#headerinfo').find('input,select').attr('disabled','true');	 
}

if(jQuery("#mode").val()=="modify"){
jQuery("#objecttypeid").attr('disabled','true');
}

 var name = jQuery( "#name" );
 jQuery( "#dialog-form" ).dialog({
            autoOpen: false,
            height: 200,
            width: 200,
            modal: true,
            buttons: {
                "Add": function() { 
             
                   var selid= jQuery("#tempid").val();
                jQuery("#"+selid).multiselect("destroy");
                             var valuesadded=name.val();
                            if(jQuery.trim(valuesadded)!="")
                      jQuery("#"+selid).append("<option value="+name.val()+">"+name.val()+"</option>")
                    
                        jQuery(this).dialog( "close" );                 
                },
                Cancel: function() {
                    jQuery( this ).dialog( "close");
                }
            },
            close: function() {
            var selid= jQuery("#tempid").val();
                name.val( "" ).removeClass( "ui-state-error" );                
               
                if(selid=='buttonList'){
                 jQuery("#"+selid).multiselect({
  					noneSelectedText: 'Buttons Selected',
  				 header:'To delete,check and press delete button'
				});
                 }else if(selid=='statelist'){
                 jQuery("#"+selid).multiselect({
  					noneSelectedText: 'States Selected',
  				 header:'To delete,check and press delete button'
				});
                 }else{
                  jQuery("#"+selid).multiselect({
  					noneSelectedText: 'Actions Selected',
  				 header:'To delete,check and press delete button'
				});
                 }
                 jQuery("#"+selid).multiselect("refresh");
                jQuery("#tempid").val("");
                
            }
        });
        
        
 jQuery('button[id^="create-button"]').each(function(index) {
  jQuery(this).button().click(function() {
  var selectedid= jQuery(this).closest('td').prev().find('select').attr("id");
                jQuery("#tempid").val(selectedid);	
                jQuery( "#dialog-form" ).dialog( "open" );
                return false;
 });     
 }); 
 

 jQuery('button[id^="delete-button"]').each(function(index) {
            jQuery(this).button().click(function() {
           var selectid= jQuery(this).closest('td').prev().find('select').attr("id");
                     
                 jQuery("#"+selectid).multiselect("destroy");
                 jQuery("#"+selectid+" option:selected").remove();
                 
                 if(selectid=='buttonList'){
                 jQuery("#"+selectid).multiselect({
  					noneSelectedText: 'Buttons Selected',
  				 header:'To delete,check and press delete button'
				});
                 }else if(selectid=='statelist'){
                 jQuery("#"+selectid).multiselect({
  					noneSelectedText: 'States Selected',
  				 header:'To delete,check and press delete button'
				});
                 }else{
                  jQuery("#"+selectid).multiselect({
  					noneSelectedText: 'Actions Selected',
  				 header:'To delete,check and press delete button'
				});
                 }
                 
                
                return false;
            }); 
            }); 
            
            <s:if test="%{hasErrors()}">
			uncheckallcheckboxes();
			</s:if>      

});



function validate(){


if(jQuery('#objecttypeid').val()=="-1"){
	alert("Please select objectType");
return false;
}

if(jQuery('#status').val()=="-1"){
	alert("Please select status");
return false;
}



checkallcheckboxes();


			
if(jQuery('#statelist').val()==null||jQuery('#statelist').val()==""){
	alert("Please select state");
	uncheckallcheckboxes();
return false;
}

if(jQuery('#actionlist').val()==null||jQuery('#actionlist').val()==""){
	alert("Please select action");
	uncheckallcheckboxes();
return false;
}

if(jQuery('#buttonlist').val()==null||jQuery('#buttonlist').val()==""){
	alert("Please select buttons");
	uncheckallcheckboxes();
return false;
}

		 jQuery('#headerinfo').find('input,select').removeAttr('disabled');
	 	
	return true;

}



function checkallcheckboxes(){
 jQuery('select[id*="list"]').each(function(index) {	
   		jQuery(this).multiselect("checkAll");

			});

}


function uncheckallcheckboxes(){
 jQuery('select[id*="list"]').each(function(index) {	
   		jQuery(this).multiselect("uncheckAll");
  
			});

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
<s:token/>
<s:push value="model">
  <s:hidden id="id" name="id" value="%{id}"/>
    <s:hidden id="tempid" />
 <s:hidden id="mode" name="mode" value="%{mode}"/>


<div class="formheading"/></div>
  
 	<div id="dialog-form" title="Add">
 	
 	
    <fieldset>
        <label for="name">Add Values</label>
        <input type="text" name="name" id="name" class="text ui-widget-content ui-corner-all" />
      
    </fieldset>
    
 	
 	</div>
	<div class="formmainbox" id="headerinfo">

	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	   	 <tr>
					<td class="greybox" width="25%">&nbsp;</td>
				
					<td class="greybox" width="20%"><s:text name="Object Type"/><span class="mandatory">*</span></td> 
					<td class="greybox" width="10%"><span style="display:none;" class="mandatory"></span><s:select id="objecttypeid" name="objecttypeid"  list="dropdownData.objectTypeList" listKey="id" listValue="displayName" headerKey="-1" headerValue="-----Choose-----" value="%{objecttypeid.id}" /></td>    
					<td class="greybox">&nbsp;</td>
					<td class="greybox">&nbsp;</td>		
					<td class="greybox">&nbsp;</td>   
		</tr>
				
	 
	    
	
	<tr>
					<td class="greybox" width="25%">&nbsp;</td>				
					<td class="greybox" width="20%"><s:text name="Additional Rule"/></td> 
					<td class="greybox" width="10%"><s:textfield id="additionalRule" name="additionalRule" value="%{additionalRule}" /> </td>				
					<td class="greybox">&nbsp;</td>
					<td class="greybox">&nbsp;</td>	
					<td class="greybox">&nbsp;</td>		
					   
		</tr>
		
		<tr>
					<td class="greybox" width="25%">&nbsp;</td>
				
					<td class="greybox" width="20%"><s:text name="Object Status"/><span class="mandatory">*</span></td> 
					<td class="greybox" width="10%"><s:select id="status" name="status"  list="dropdownData.statusList" headerKey="-1" headerValue="-----Choose-----" value="status" /></td>    
					<td class="greybox">&nbsp;</td>
					<td class="greybox">&nbsp;</td>		
					<td class="greybox">&nbsp;</td>   
		</tr>
		
		<tr>
					<td class="greybox" width="25%">&nbsp;</td>				
					<td class="greybox"  width="20%"><s:text name="State"/><span class="mandatory">*</span></td> 
					<td class="greybox" width="10%"><s:select  id="statelist" name="stateList"  list="stateList" multiple="true"  /></td>					
					<td id="abc" class="greybox"><button id="create-button" >Add</button>	<button id="delete-button">Delete</button></td>	
					<td class="greybox">&nbsp;</td>	
					<td class="greybox">&nbsp;</td>	
		</tr>
		
		
		<tr>
					<td class="greybox" width="25%">&nbsp;</td>				
					<td class="greybox" width="20%"><s:text name="Actions"/><span class="mandatory">*</span></td> 
					<td class="greybox" width="10%"><s:select  id="actionlist" name="actionList"  list="actionList" multiple="true"  /></td>					
					<td class="greybox"><button id="create-button" >Add</button>	<button id="delete-button">Delete</button></td>	
					<td class="greybox">&nbsp;</td>	
					<td class="greybox">&nbsp;</td>	
		</tr>
		
		
		
		
		
		<tr>
					<td class="greybox" width="25%">&nbsp;</td>				
					<td class="greybox" width="20%"><s:text name="Button"/><span class="mandatory">*</span></td> 
					<td class="greybox" width="10%"><s:select  id="buttonlist" name="buttonList"  list="buttonList" multiple="true"  /></td>					
					<td class="greybox"><button id="create-button" >Add</button>	<button id="delete-button">Delete</button></td>	
					<td class="greybox">&nbsp;</td>	
					<td class="greybox">&nbsp;</td>	
		</tr>
		
		
	</table>
	</div>
	 <div class="buttonbottom" align="center" id="workFlowButtons">
		<table>
		<tr>      
	  
	  			
	  	       <s:if test="%{mode!='view'}">
	  			<td><s:submit  cssClass="buttonsubmit" id="Create" name="Create" value="Create "  method="create"  onclick="return validate();"/></td>
	  	</s:if>
	  
	  	 <td><input type="button" name="close" id="close" class="button"  value="Close" onclick="window.close();"/></td>
	  
	  		
	  		
	  	</tr>
        </table>
   </div>
  
  
  
   
	
	
	
</s:push>	
	
</s:form>
</body>
</html>