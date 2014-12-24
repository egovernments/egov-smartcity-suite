
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

 <title>Search WorkFlow</title>

<script type="text/javascript">


jQuery.noConflict();

jQuery(document).ready(function(){
	 jQuery(function() {
	        var icons = {
	            header: "ui-icon-circle-arrow-e",
	            activeHeader: "ui-icon-circle-arrow-s"
	        };
	        jQuery( "#accordion" ).accordion({
	            icons: icons,
	            heightStyle: "content",
	            active: false,
	            clearStyle: true,
	            collapsible:true
	        }).bind("accordionchange", function(event, ui) {
		      
		       
		           jQuery('#objectTypeView').val(ui.newHeader.find('#objectTypelabel').text())
		            jQuery('#fromDateView').val(ui.newHeader.find('#fromDatelabel').text())
		             jQuery('#toDateView').val(ui.newHeader.find('#toDatelabel').text())
		              jQuery('#additionalRuleView').val(ui.newHeader.find('#additionalRulelabel').text())
		               jQuery('#fromQtyView').val(ui.newHeader.find('#fromQtylabel').text())
		                jQuery('#toQtyView').val(ui.newHeader.find('#toQtylabel').text())
		                


		        //alert(ui.newHeader.text());
		        //alert(ui.oldContent.text());
		        //alert(ui.oldHeader.text());  
		        //alert(ui.newContent.text());
		       //console.dir(jQuery(this).find('#objectTypelabel'))		      
		      // console.dir(jQuery(this).find(':input'))		        
	        	//  console.dir(ui.newHeader); // jQuery, activated header
	        	 // console.log(ui.newHeader[0].val()); //this has the id attribute of the header that was clicked
	        	//  alert(ui.newHeader[0].val())
	        	 // doSomething(ui.newHeader[0].id); 
		                
		    
	        	}); 
	        jQuery( "#toggle" ).button().click(function() {
	            if ( jQuery( "#accordion" ).accordion( "option", "icons" ) ) {
	                jQuery( "#accordion" ).accordion( "option", "icons", null );
	            } else {
	                jQuery( "#accordion" ).accordion( "option", "icons", icons );
	            }
	        });
	    });

	var mode = jQuery("#mode").val();
	 if(mode=="search"){
	jQuery('#wfdetails').hide();	
	}else
	if(mode=="results")
	jQuery('#wfdetails').show();
	
	
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


function init(){

	
}





function enableAllFields(){
for(var i=0;i<document.forms[0].length;i++)
			{
				document.forms[0].elements[i].disabled =false;
			}

}



function deleteMatrix(){

	if(confirm('Deleting the workflow will make the records unusable if they are in between a workflow.Please complete the workflow or reject them. Do you still want to continue.'))
		{document.getElementById('searchAction').value='delete';
		 document.forms[0].action="<c:url value='/workflow/workFlowMatrix!deleteWorkFlowMatrix.action'/>";
		document.getElementById('searchForm').submit();
		}
}

function modifyMatrix(){
	{
	if(confirm('Modifying the workflow will make the records unusable if they are in between a workflow.Please complete the workflow or reject them. Do you still want to continue.'))
		document.getElementById('searchAction').value='modify';
	    document.forms[0].action="<c:url value='/workflow/workFlowMatrix!prepareModifyForm.action'/>";
		document.getElementById('searchForm').submit();
	}
}

function validate(){

if(document.getElementById('department').value=="-1"){
	alert("Department is Mandatory ,Please select a department");
	return false;
}
if(document.getElementById('objectType').value=="-1"){

	alert("ObjectType is mandatory,Please select a Object Type");
	return false;
}

if(document.getElementById('fromAmount').value!=null&&document.getElementById('fromAmount').value!=""){

	var fromamt=document.getElementById('fromAmount').value;
	if(isNaN(fromamt)){
	  alert("Please Enter a valid Number for from amount")
	  return false;
	}else{
		if(fromamt<0){
			alert("Please Enter a valid Number for from amount")
			return false;
		}else if(document.getElementById('toAmount').value!=null&&document.getElementById('toAmount').value!=""){
			
			var toamt=document.getElementById('toAmount').value;
			if(isNaN(toamt)){
			  alert("Please Enter a valid Number for to amount")
			  return false;
			}else{
				if(toamt<0){
					alert("Please Enter a valid Number for to amount")
					return false;
				}else{alert("")
					if(fromamt>toamt){
					alert("From amount should be lesser than to amount")
						return false;
				}
			}
			}
			}
			}
	
}



if(document.getElementById('toAmount').value!=null&&document.getElementById('toAmount').value!=""){

	var toamt=document.getElementById('toAmount').value;
	if(isNaN(toamt)){
	  alert("Please Enter a valid Number for to amount")
	  return false;
	}else{
		if(toamt<0){
			alert("Please Enter a valid Number for to amount")
			return false;
		}
	}

	}

enableAllFields();
return true;
	
}


</script>
</head>
<body onload="init();">

<s:if test="%{hasActionMessages()}">
        <div class="errorstyle">
        
         <s:actionmessage/>
                 </div>
</s:if>		

	<div class="formheading">
	
   <s:form action="workFlowMatrix" theme="simple" onsubmit="" id="searchForm">

 
	<s:hidden id="mode" name="mode" value="%{mode}"/>
	<s:hidden id="objectTypeView" name="objectTypeView" value="%{objectTypeView}"/>
	<s:hidden id="fromDateView" name="fromDateView" value="%{fromDateView}"/>
	<s:hidden id="toDateView" name="toDateView" value="%{toDateView}"/>
	<s:hidden id="fromQtyView" name="fromQtyView" value="%{fromQtyView}"/>
	<s:hidden id="toQtyView" name="toQtyView" value="%{toQtyView}"/>
	<s:hidden id="additionalRuleView" name="additionalRuleView" value="%{additionalRuleView}"/>
	<s:hidden id="searchAction" name="searchAction" value="%{searchAction}"/>

	<div class="formmainbox" id="headerinfo">

	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	   	 <tr>
					<td class="greybox" width="15%">&nbsp;</td>
				
					<td class="greybox" ><s:text name="Object Type"/><span class="mandatory">*</span></td> 
					 <td class="greybox"><s:select id="objectType" name="objectType"  list="dropdownData.objectTypeList" listKey="id" listValue="displayName" headerKey="-1" headerValue="-----Choose-----" value="objectType" /></td>    
					
					<td class="greybox" ><s:text name="Additional Rule"/></td> 
					<td class="greybox"><s:select id="additionalRule" list="dropdownData.additionalRuleList" name="additionalRule" multiple="false" headerKey="-1" headerValue="-------Choose-------" listKey="additionalRule"  listValue="additionalRule"  value="additionalRule" /></td>
					
					
					<td class="greybox">
						&nbsp;
					</td>   
				</tr>
				
				 <tr>
					
					<td class="greybox" width="15%">&nbsp;</td>		
					 <td class="greybox"><s:text name="From Date"/></td>	
					<td class="greybox"><s:date name="fromDate"  format="dd/MM/yyyy" var="fromdateTemp"/>
					<s:textfield name="fromDate" id="fromDate" value="%{fromdateTemp}" maxlength="20" onblur="validateDateFormat(this);"  onkeyup="DateFormat(this,this.value,event,false,'3')"  />
					<a href="javascript:show_calendar('forms[0].fromDate');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true; "><img id="cal1" src="${pageContext.request.contextPath}/images/calendaricon.gif" border="0" align="absmiddle" /></a>
					</td><%--
					 <td class="greybox"><s:text name="To Date"/></td>	
					<td class="greybox"><s:date name="toDate"  format="dd/MM/yyyy" var="todateTemp"/>
				    <s:textfield name="toDate" id="toDate" value="%{todateTemp}" maxlength="20" onblur="validateDateFormat(this);"  onkeyup="DateFormat(this,this.value,event,false,'3')"  />
					<a href="javascript:show_calendar('forms[0].toDate');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true; "><img id="cal2" src="${pageContext.request.contextPath}/images/calendaricon.gif" border="0"  align="absmiddle" /></a>
					</td>
					--%><td class="greybox">&nbsp;</td>
					<td class="greybox">&nbsp;</td>
					<td class="greybox">&nbsp;</td>
				</tr>
				
	         <tr>
					
					<td class="greybox" width="15%">&nbsp;</td>    
					<td class="greybox"><s:text name="Departments"/><span class="mandatory">*</span></td> 					
					 <td class="greybox"><s:select id="department" name="department"  list="dropdownData.departmentList" listKey="deptName" listValue="deptName"  value="department" headerKey="-1" headerValue="----Choose-----"/></td>			
					<td class="greybox">&nbsp;</td>
					<td class="greybox">&nbsp;</td>
					<td class="greybox">&nbsp;</td>
					
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
	  
	  			<td><s:submit  cssClass="buttonsubmit" id="Search" name="Search" value="Search" onclick="return validate()" method="viewMatrixResults" /></td>
	  	   
	  		
	  	</tr>
        </table>
   </div>
   
   <div id="wfdetails" align="center">
 
  <div id="accordion" align="center" style="width: 1100px; height: 1500px; ">
  <s:iterator value="viewList" status="row_status">
     <div>
         <div>
             <table style="width:1100px; ">
              
              <tr style="display:none;">
                <td>
                <s:textfield name="ids" id="id" value="345"></s:textfield>
                </td>
              
              </tr>
               <tr>
                    <td width="20%">&nbsp;</td>		
					<td width="20%" style="font-size:12px"><s:text name="Object Type"/>:<s:label style="display:none;" id="objectTypelabel" name="objectType"/><s:label   name="objectTypeDisplay"/></td>
					<td  width="20%" >&nbsp;</td>				
					<td  width="40%" style="font-size:12px"><s:text name="Additional Rule"/>:<s:label id="additionalRulelabel" name="additionalRule" /></td> 
					
                 </tr>
              
            
              
             <tr>
                    <td width="20%">&nbsp;</td>		
					<td width="20%" style="font-size:12px"><s:text name="From Date " />:<s:label id="fromDatelabel" name="fromDateString" /></td>
					<td width="20%"  >&nbsp;</td>		
					<td  width="40%" style="font-size:12px"><s:text name="To Date"/>:<s:label id="toDatelabel" name="toDateString" /></td> 
					
                 </tr>
              
              
             
              <tr>
                    <td width="20%">&nbsp;</td>		
					<td width="20%" style="font-size:12px"><s:text name="From Amount " />:<s:label id="fromQtylabel" name="fromQty" /></td>
					<td width="20%" >&nbsp;</td>		
					<td width="40%" style="font-size:12px"><s:text name="To Amount"/>:<s:label id="toQtylabel" name="toQty" /></td> 
					
                 </tr>
              
              
                 
                 
           
                 
             </table>
          </div>
     </div>
    <div>
        
         <div>
             <table style="width:1100px; border-style:solid;border-width:1px;">
             
               
                 
                 
              <tr id="insideheader">
                    <td >&nbsp;</td>			
					<td style="font-size:14px" align="center"><s:label>Approver Level</s:label></td> 
					<td>&nbsp;</td>		
					<td style="font-size:14px" align="center"><s:label>State</s:label></td> 					
					<td>&nbsp;</td>							
					<td style="font-size:14px" align="center"><s:label>Designation</s:label></td> 
					<td>&nbsp;</td>
					<td style="font-size:14px" align="center"><s:label>Action</s:label></td> 			
					<td>&nbsp;</td>
					<td style="font-size:14px" align="center"><s:label>Status</s:label></td> 
					<td>&nbsp;</td>
					<td style="font-size:14px" align="center"><s:label>Buttons</s:label></td> 
					<td>&nbsp;</td>
                 </tr>
              <tr id="insideheader">
                    <td>&nbsp;</td>		
					<td>&nbsp;</td>	
					<td>&nbsp;</td>		
					<td>&nbsp;</td>	
					<td>&nbsp;</td>		
					<td>&nbsp;</td>		
					<td>&nbsp;</td>
					<td>&nbsp;</td>			
					<td>&nbsp;</td>
					<td>&nbsp;</td>	
					<td>&nbsp;</td>	
					<td>&nbsp;</td>	
					<td>&nbsp;</td>	
					<td>&nbsp;</td>	
					<td>&nbsp;</td>										
					<td align="left" style="font-size:14px"><a href="javascript:modifyMatrix();"/>Modify</a>
					
					/ <a href="javascript:deleteMatrix();"/>Delete</a></td>
                 </tr>
                 
                  <s:iterator  value="matrixdetails"  status="row_status">
                  <tr >
                    <td >&nbsp;</td>		
					<td  style="font-size:12px" align="center"><s:property value="%{#row_status.count}"/></td> 
					<td>&nbsp;</td>		
					<td  style="font-size:12px" align="center"><s:property value="state"/></td> 
					<td>&nbsp;</td>		
					<td  style="font-size:12px" align="center"><s:property value="designation"/></td> 
					<td>&nbsp;</td>
					<td  style="font-size:12px" align="center"><s:property value="action"/></td> 
					<td>&nbsp;</td>		
					<td  style="font-size:12px" align="center"><s:property value="status"/></td> 
					<td>&nbsp;</td>			
					<td  style="font-size:12px" align="center"><s:property value="buttons"/></td> 
					<td>&nbsp;</td>
					
                 </tr>
                  
                 </s:iterator>
             </table>
          </div>
     </div>   
   </s:iterator>         
   </div>
 </div>
 
 
  </s:form>
  </div>
  
</body>
   
</html>
