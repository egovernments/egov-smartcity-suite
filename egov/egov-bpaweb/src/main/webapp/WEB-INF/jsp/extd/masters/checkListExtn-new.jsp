<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>

		

<head>		
		
<title>
	<s:if test="%{mode=='view'}">
  		<s:text name="View Check List Type "/>
	</s:if>
	<s:elseif test="%{mode=='edit'}">
  		<s:text name="Modify Check List Type "/>
	</s:elseif>
	  <s:else>
	  <s:text 
	  name="Check List Master"/>
	  </s:else>
</title>
</head>
 <style>
    label {
        display: inline-block;
        width: 5em;
    }
    </style>
     <style>
    #dialog label, #dialog input { display:block; }
    #dialog label { margin-top: 0.5em; }
    #dialog input, #dialog textarea { width: 95%; }
    #tabs { margin-top: 1em; }
    #tabs li .ui-icon-close { float: left; margin: 0.4em 0.2em 0 0; cursor: pointer; }
    #add_tab { cursor: pointer; }
}
    </style>
</head>
 <link rel="stylesheet" type="text/css" href="<c:url value='/common/css/jquery/jquery-ui-1.8.22.custom.css'/>" />
   <script type="text/javascript" src="<c:url value='/common/js/jquery/jquery-1.7.2.min.js'/>"></script>
   <script type="text/javascript" src="<c:url value='/common/js/jquery/jquery-ui-1.8.22.custom.min.js'/>"></script>
 <script type="text/javascript">
 jQuery.noConflict();
 var index=0;

 //This method is to validate form  
 
 function showerrormsg(obj,msg){
	// alert("showmethod");
 dom.get("Bpaservice_error").style.display = '';
 document.getElementById("Bpaservice_error").innerHTML =msg;
 jQuery(obj).css("border", "1px solid red");		
 return false;
 }

 function validateForm()
 {   
	// hideFieldError();
	//alert("validatefor");
	 dom.get("Bpaservice_error").style.display='none';
	 if( jQuery('#checklistType').val()=="-1"){
		   showerrormsg(jQuery('#checklistType'),"Pleas Select ChecklistType");
		  
		   }

	 if( jQuery('#serviceType').val()=="-1"){
		   showerrormsg(jQuery('#serviceType'),"Pleas Select ServiceType");
		  
		   }
	   

		
 }
 
 
 function addRow()
 {     //alert('addrow');
 		if(validateCheckListDetail())
 		{
 			//alert('addrowin');
	    	var tableObj=document.getElementById('checkdetails');
			var tbody=tableObj.tBodies[0];
			var lastRow = tableObj.rows.length;
			var rowObj = tableObj.rows[1].cloneNode(true);
			tbody.appendChild(rowObj);
			var rowno = parseInt(tableObj.rows.length)-2;
			document.forms[0].srlNo[lastRow-1].value=tableObj.rows.length - 1;
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		
            document.forms[0].code[lastRow-1].value="";
			document.forms[0].description[lastRow-1].value="";
			document.forms[0].isMandatory[lastRow-1].value="";
			document.forms[0].isActive[lastRow-1].checked=true;		
			document.forms[0].checkListDetailsId[lastRow-1].value="";
		    document.forms[0].srlNo[lastRow-1].setAttribute("name","checkListDetailsList["+index+"].srlNo");
			document.forms[0].code[lastRow-1].setAttribute("name","checkListDetailsList["+index+"].code");
			document.forms[0].description[lastRow-1].setAttribute("name","checkListDetailsList["+index+"].description");
           document.forms[0].isMandatory[lastRow-1].setAttribute("name","checkListDetailsList["+index+"].isMandatory");
           document.forms[0].isActive[lastRow-1].setAttribute("name","checkListDetailsList["+index+"].isActive");
			document.forms[0].checkListDetailsId[lastRow-1].setAttribute("name","checkListDetailsList["+index+"].id");
			index++;
		}
  }
  
 
 //This method is to remove rows from shopdetail table 
  function removeRow(obj)
  { 
    	var tb1=document.getElementById('checkdetails');
       var lastRow = (tb1.rows.length)-1;
       var curRow=getRow(obj).rowIndex;
     //  alert("curRow---> "+curRow +" ,lastRow---> "+lastRow)
       dom.get("Bpaservice_error").style.display='none';
       if(lastRow ==1)
     	{

       //	alert("row 1");
    		 dom.get("Bpaservice_error").style.display='none';
    		 document.getElementById("Bpaservice_error").innerHTML='This row can not be deleted';
  			 dom.get("Bpaservice_error").style.display='';
     	     return false;
       }
     	else
     	{
         	//alert("deleted row");
      		var updateserialnumber=curRow;
			for(updateserialnumber;updateserialnumber<tb1.rows.length-1;updateserialnumber++)
			{  
				//alert("inside delete row");
				if(document.forms[0].srlNo[updateserialnumber]!=null)
					document.forms[0].srlNo[updateserialnumber].value=updateserialnumber;
			}
			
			tb1.deleteRow(curRow);
			
	      	return true;
     }
  }

  // This method is to validate Line level items
  
  function validateCheckListDetail()
 {    
     var tableObj=document.getElementById('checkdetails');
     var lastRow = tableObj.rows.length;
     var Code,Description;
     var i;
     if(lastRow>2)
     {
    //alert("row=2");
     	for(i=0;i<lastRow-1;i++)
     	{   
        // alert("hi for condition");
          	 Code=document.forms[0].code[i].value;
          	Description=document.forms[0].description[i].value;
         	mendatory=document.forms[0].isMandatory[i].value;
          active=document.forms[0].isActive[i].value;
           	 if(!validateFeesLines(Code,Description,i+1))
           	   return false;
    		}
    		return true;
    	}
    	else
    	{
    		Code=document.getElementById('code').value;
    	    Description=document.getElementById('description').value;
            Amount=document.getElementById('isMandatory').value;
             active=document.getElementById('isActive').value;
    	    if(!validateFeesLines(Code,Description,1))
    	       return false;
    	     else
    	       return true;
    	    
    	}
   }
   
   function validateFeesLines(Code,Description,row)
   {   
	 // alert("validatelines");
	   //Code=document.forms[0].code[i].value;
     	//Description=document.forms[0].description[i].value;
         dom.get("Bpaservice_error").style.display='none';
          if(Code=="" || Description==""  )
        {
              //alert("enterrow values");
                 document.getElementById("Bpaservice_error").innerHTML='';
    				document.getElementById("Bpaservice_error").innerHTML='Checklist Details is Required for row:'+" "+row;
    				dom.get("Bpaservice_error").style.display='';
     			return false;
        }
         
          return true;
   }
 function uniqueCheckcode()
 {  
	// alert("uniqueCheckcode");
     var tableObj=document.getElementById('checkdetails');
     var lastRow = tableObj.rows.length;
     var code1,code2;
     var i,j;
     if(lastRow>2)
     {
   	for(i=0;i<lastRow-2;i++)
   	{
   		code1=document.forms[0].code[i].value;
        	 for(j=i+1;j<lastRow-1;j++)
        	 {
        		 code2=document.forms[0].code[j].value;
         	    if(code1==code2)
         	    { 
         	    	// alert("uniqueCheckcoderoopa");
         	        dom.get("Bpaservice_error").style.display='none';
         	        document.getElementById("Bpaservice_error").innerHTML='CheckList code should be unique';
         	        dom.get("Bpaservice_error").style.display='';
         	   		return false;
         	   	}
         	  }
  		}
  		
  	}
  	return true;
 }

function validateNumeric(obj)
{      
	   if(obj!=null && obj.value!=null && isNaN(obj.value))
	    {
	    	dom.get("Bpaservice_error").style.display='';
			document.getElementById("Bpaservice_error").innerHTML='Pleas Enter only Numbers'
			 dom.get(obj).value = ""; 
 		 return false;
	    }
	
 }
 function checkForLength()  
 {
 	
 	 document.getElementById("code").disabled=false;
 	 var checkCode=document.getElementById('code');
 	//alert("length"+ checkCode.value.length);
 	  if(checkCode.value.length<=3){
 		 //alert("length"+ checkCode.value.length);
 		  
 		  return true;
 	      }
 	  else
 		  {
 		// alert("length"+ checkCode.value.length);
 		  dom.get("Bpaservice_error").style.display='';
 		  document.getElementById("Bpaservice_error").innerHTML='Pleas Enter only Three Digit Numbers';
 			 checkCode.value=""; 
 		 checkCode.focus(); 
     	  return false;
 	  }
 }
 
 function checkNotSpecial(obj)
 {

 	var iChars = "!@$%^*+=[']`~';{}|\":<>?#_";
 	for (var i = 0; i < obj.value.length; i++)
 	{
 		if ((iChars.indexOf(obj.value.charAt(i)) != -1)||(obj.value.charAt(0)==" "))
 	{
     dom.get("Bpaservice_error").style.display='';
 	document.getElementById("Bpaservice_error").innerHTML='Special characters are not allowed'
 	obj.value="";
 	obj.focus();
 	return false;
 	}
    }
 	return true;
 }

 function bodyOnLoad()
 {	      

	 //alert("hi");
	 document.getElementById("isActive").checked=true;	
	       var mode=document.getElementById("mode").value;
	       index=document.getElementById('checkdetails').rows.length-1;
	      
	       if(mode!=null && mode=='edit')
		   {  
			// alert("edit");
			   document.getElementById("checklistType").disabled=true;
		       document.getElementById("serviceType").disabled=true;
		   }
	       if(mode!=null && mode=='view')
        {   

	          //alert("view");
	    	   for(var i=0;i<document.forms[0].length;i++)
	    		{  
	    		  document.forms[0].elements[i].disabled =true;
	    		  document.getElementById("close").disabled=false;
	    		  document.getElementById("Back").disabled=false;
	    			
	    		}
	    		
        	}  
	                              
    }
 function enableFields(){
 	for(var i=0;i<document.forms[0].length;i++)
 	{
 		document.forms[0].elements[i].disabled =false;
 	}
 }
  </script> 


<body  onload="bodyOnLoad();">
	
  <s:if test="%{hasActionMessages()}">
			<div class="errorstyle">
			<s:actionmessage />
		</div>
	</s:if>
	
<s:if test="%{hasErrors()}">
		<div class="errorstyle" id="fieldError">
			<s:actionerror />
			<s:fielderror />
		</div>
</s:if>
<div class="mandatory" style="display: none"
				id="checklistType">
				<s:text name="fees.checklistservicetype.exists"/>
		</div>

	<div class="errorstyle" id="Bpaservice_error" style="display:none;"></div>
        <s:form action="checkListExtn" name="checkListActionForm" theme="simple" onsubmit="enableFields()">
        <s:token />
		<s:push value="model">
		
		
		<div class="formheading"/></div>
			<s:hidden id="modifiedDate" name="modifiedDate" value="%{modifiedDate}" />
		<s:hidden id="modifiedBy" name="modifiedBy" value="%{modifiedBy.id}" />
		<s:hidden id="createdBy" name="createdBy" value="%{createdBy.id}" />
		<s:hidden id="id" name="id" value="%{id}" />
	
	<s:hidden id="mode" name="mode" value="%{mode}" />	
		
		
				         
				             <table width="100%" border="0" cellspacing="0" cellpadding="2">
	   	
	   
         					<tr>             
                            <td width="20%" class="bluebox"></td>
                            <td width="20%"class="bluebox"></td>
						         <td class="bluebox" width="10%"><s:text name="Check List Type"/><span class="mandatory">*</span>:</td>
						         <td  class="bluebox"></td>
				               <td width="12%" class="bluebox">
				             <s:select  id="checklistType" name="checklistType" value="%{checklistType}" list="dropdownData.checkIdList" listKey="code" listValue="code" headerKey="-1" headerValue="-----choose----"/>
				             </td>
		       
		                             <td  class="bluebox"></td>
		                             <td class="bluebox"/><td class="bluebox">
		                             <s:text name="Service Type"/><span class="mandatory">*</span>:</td>
		                            <td  class="bluebox"></td>
						         <td width="12%" class="bluebox">
								 <s:select  id="serviceType" name="serviceType.id" value="%{serviceType.id}" list="dropdownData.servicetypeList" listKey="id" listValue="code+'-'+description" headerKey="-1" headerValue="----Choose----" maxsize = "256"  /> 	
		                         </td> <td width="20%"class="bluebox"></td>
		                         <td width="20%"class="bluebox"></td>
		                         </tr>
		                         </table>
					      <table id="checkdetails" width="60%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" align="center">
		                 	<div class="blueshadow"></div>
		                 
	                    	<h1 class="subhead" ><s:text name="Check List Details"/></h1>
	                   
	 						<div align="center">
	     					 <tr>
								<th  class="bluebgheadtd" width="6%"><div align="center">Sl No</div></th>
							    <th  class="bluebgheadtd" width="8%"><div align="center">Code<span class="mandatory">*</span>:</div></th>
							    <th  class="bluebgheadtd" width="40%"><div align="center">Description<span class="mandatory">*</span>:</div></th>
					            <th  class="bluebgheadtd" width="8%"><div align="center">Is Mandatory</div></th>
					            <th  class="bluebgheadtd" width="8%"><div align="centre">Is Active</div></th>
					             <s:if test="%{mode!='view'}">
							    <th  class="bluebgheadtd" width="2%"><div align="center"> Add</div></th>
								<th  class="bluebgheadtd" width="2%"><div align="center"> Delete</div></th>
								</s:if>
						  </tr>
		  			 
							   <s:iterator value="checkListDetailsList" status="row_status">
							    <tr>
							  	<td  class="blueborderfortd"><s:textfield name="checkListDetailsList[%{#row_status.index}].srlNo" id="srlNo" readonly="true"  cssClass="tablerow" value="%{#row_status.count}" cssStyle="text-align:center"/></td>
							    <td class="blueborderfortd">
							    
							    		<s:textfield name="checkListDetailsList[%{#row_status.index}].code" id="code" maxlength="64" cssClass="tablerow"   size="64"  cssStyle="text-align:center" onblur="checkForLength();uniqueCheckcode();validateNumeric(this)"/>
							    	
							    	 
							    <td  class="blueborderfortd" width="100%"><s:textfield name="checkListDetailsList[%{#row_status.index}].description" id="description" maxlength="512" size="512" cssClass="tablerow"   cssStyle="text-align:left"  onblur="checkNotSpecial(this)" />
							    
							    </td>
					             <td  class="blueborderfortd"><s:checkbox name="checkListDetailsList[%{#row_status.index}].isMandatory" id="isMandatory"  />
				                 </td>
				                  <td  class="blueborderfortd"><s:checkbox name="checkListDetailsList[%{#row_status.index}].isActive" id="isActive"  />
				                 </td>    
				                 <s:if test="%{mode!='view'}">
		    	<td  class="blueborderfortd"><div align="center"><a href="#"><img src="${pageContext.request.contextPath}/images/addrow.gif" height="16"  width="16" border="2" alt="Add" onclick="addRow();"></a></div></td>
		   		<td  class="blueborderfortd"><div align="center"><a href="#"><img src="${pageContext.request.contextPath}/images/removerow.gif" height="16"  width="16" border="2" alt="Delete" onclick="removeRow(this);"></a></div></td>
		    </s:if>
		    <s:hidden id="checkListDetailsId" name="checkListDetailsList[%{#row_status.index}].id" />
		    </tr>
		    </s:iterator>
	  					</table>
	  					
	           <div id="actionbuttons" align="center" class="buttonbottom"> 
	            <td colspan="4" align="center">
          <s:if test="%{mode !='edit' && mode!='view'}">
            <s:submit type="submit" cssClass="buttonsubmit"  method="create" value="Save" onclick="return validateForm();"   />		  
		  </s:if>
		  
		  <s:if test="%{mode =='edit'}">
            <s:submit type="submit" cssClass="buttonsubmit"  method="create" value="Modify" onclick="return validateForm();"    />          		
          </s:if> 
         
          <s:submit type="submit" cssClass="buttonsubmit" value="Back" id="Back" name="Back" method="search" />
            
		   
            
		   <input type="button" name="close" id="close" class="button" value="Close" onclick=" window.close();" ></td>
	    </div>
					   
					   
					   
</s:push>
</s:form>
</body>
</html>
