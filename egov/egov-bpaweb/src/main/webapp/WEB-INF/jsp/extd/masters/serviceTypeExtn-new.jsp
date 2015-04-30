<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>

<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>
<head>
		
		
<title>
	<s:if test="%{mode=='view'}">
  		<s:text name="View Service Type"/>
	</s:if>
	<s:elseif test="%{mode=='edit'}">
  		<s:text name="Modify Service Type"/>
	</s:elseif>
	  <s:else>
	  <s:text 
	  name="Service Type Master"/>
	  </s:else>
</title>
</head>
<script type="text/javascript">



  function validateForm()
  {   

	var code=document.getElementById('code').value;
	if(document.getElementById('code').value==""){

		 dom.get("Bpaservice_error").style.display='';
			document.getElementById("Bpaservice_error").innerHTML='pleas enter service type code'
   		//alert("pleas enter service type code");
    	return false;
	} 
      if(document.getElementById('description').value==""){
   		
    	  dom.get("Bpaservice_error").style.display='';
			document.getElementById("Bpaservice_error").innerHTML='pleas enter service type description'
				//alert("pleas enter service type description");
    	return false;
	}
      if(document.getElementById('serviceNumberPrefix').value==""){

    	  dom.get("Bpaservice_error").style.display='';
			document.getElementById("Bpaservice_error").innerHTML='pleas select serviceNumberPrefix'
	   		//alert("pleas select serviceNumberPrefix");
	    	return false;	
	 }
	  
	   if(document.getElementById('isCmdaType').value=="-1"){

		   dom.get("Bpaservice_error").style.display='';
			document.getElementById("Bpaservice_error").innerHTML='pleas select application form type'
   		//alert("pleas select application form type");
    	return false;
	} 

	if(document.getElementById('IsInspectionFeeRequired').value=="-1"){
		 dom.get("Bpaservice_error").style.display='';
			document.getElementById("Bpaservice_error").innerHTML='pleas select InspectionFee'
   		//alert("pleas Select InspectionFee");
    	return false;
	} 
    if(document.getElementById('isPtisNumberRequired').value=="-1"){

    	 dom.get("Bpaservice_error").style.display='';
			document.getElementById("Bpaservice_error").innerHTML='pleas select ptisNumber'
   		//alert("pleas select ptisNumber");
    	return false;
	} 	
   
	 if(document.getElementById('ISAutoDcrNumberRequired').value=="-1"){

		 dom.get("Bpaservice_error").style.display='';
			document.getElementById("Bpaservice_error").innerHTML='pleas select AutoDcrIntegrated'
   		//alert("pleas select AutoDcrIntegrated");
    	return false;
	} 
	 if(document.getElementById('isScrutinyFeeRequired').value=="-1"){
	   		
		 dom.get("Bpaservice_error").style.display='';
			document.getElementById("Bpaservice_error").innerHTML='pleas select AdmissionFee'
	   	//	alert("pleas select AdmissionFee ");
	    	return false;
		} 	 

  }


    function closeWindow()
   {
     window.close();
     }


   function validateNumeric(obj)
   {      
	if(!checkForLength()){
		return false;
	}
	    if(obj!=null && obj.value!=null && isNaN(obj.value))
	    {    
	    	 dom.get("Bpaservice_error").style.display='';
				document.getElementById("Bpaservice_error").innerHTML='pleas enter only two digit numbers'
	   		// alert('pleas enter only numbers');
    		 dom.get(obj).value = ""; 
    		 return false;
	    }
	
    }

    function validateCodeUniqueCheck(obj)
    {
	
     if(!document.getElementById("code").value=="" && document.getElementById("code").value!=null)
	{
	  
      var servicecode=document.getElementById("code").value;
      populatecodeUniqueCheck({servicecode:servicecode});
  
	}
     else
	{
     // alert('Service Code all ready available');
     return false;
	}
  
  }
     
	function checkForLength()  
	{
		
		 document.getElementById("code").disabled=false;
		 var codea=document.getElementById('code');
		 //alert("length"+ codea.value.length);
		  if(codea.value.length==2)
			  {
			  return true;
		      }
		  else
			  {
			  dom.get("Bpaservice_error").style.display='';
				document.getElementById("Bpaservice_error").innerHTML='pleas enter only two digit numbers'
			 // alert('pleas enter only two digit numbers');
			  codea.value=""; 
			  codea.focus(); 
	    	 return false;
		  }
	}

	
	function checkNumberForLength()  
	{
		
		// document.getElementById("serviceNumberPrefix").disabled=false;
		 var codea=document.getElementById('serviceNumberPrefix');
		// alert("length"+ codea.value.length);
		  if(codea.value.length<=8)
			  {
			//  alert("number is less than 8");
			  return true;
		      }
		  else
			  {
			 // alert("number is grater than 8");
			  dom.get("Bpaservice_error").style.display='';
				document.getElementById("Bpaservice_error").innerHTML='pleas enter service Prefix Number is less than or equal to 8 characters'
			 // alert('pleas enter only two digit numbers');
			  codea.value=""; 
			  codea.focus(); 
	    	 return false;
		  }
	}

	
    //move to common jsp
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


	
	  function numberformat(button)
	  { 
	    alert('ServicePrefixNumber should be this in Format "099-COC1"');
	  }

	
        function bodyOnLoad()
        {	
		       var mode=document.getElementById("mode").value;
		       if(mode!=null && mode=='edit')
			   {
			   document.getElementById("code").disabled=true;
			   }
		       if(mode!=null && mode=='view')
	           { 
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

<body onload="bodyOnLoad();" >

 
    <s:if test="%{hasErrors()}">
		<div class="errorcss" id="fieldError">
			<s:actionerror cssClass="errorcss"/>
			<s:fielderror cssClass="errorcss"/>
		</div>
	</s:if>
<s:if test="%{hasActionMessages()}">
		<div class="errorcss">
			<s:actionmessage />
		</div>
	</s:if>
	<div class="errorstyle" id="Bpaservice_error" style="display:none;"></div>
        <s:form action="serviceTypeExtn" name="serviceTypeForm" theme="simple" onsubmit="enableFields()">
        <s:token />
			<s:push value="model">
			<div class="formheading"/></div>
			<s:hidden id="modifiedDate" name="modifiedDate" value="%{modifiedDate}" />
		<s:hidden id="modifiedBy" name="modifiedBy" value="%{modifiedBy.id}" />
		<s:hidden id="createdBy" name="createdBy" value="%{createdBy.id}" />
		<s:hidden id="mode" name="mode" value="%{mode}" />
		<s:hidden id="id" name="id" value="%{id}" />
		<s:hidden name="idTemp" id="idTemp" />
			                   
	 <table width="100%" border="0" cellspacing="0" cellpadding="2">
	
     
		<div class="mandatory" style="display: none"
				id="codeUniqueCheck">
				<s:text name="service.code.exists" />
			</div>
       
					<tr>      
					             <td   width="12%" class="bluebox"></td>
					             <td   width="12%" class="bluebox"></td>
						         <td width="10%" class="bluebox"><s:text name="Service Type Code" /><span class="mandatory">*</span>:</td>
						        <td  class="bluebox"><s:textfield name="code" id="code" onblur="validateNumeric(this);validateCodeUniqueCheck(this);"/>
						       <egov:uniquecheck id="codeUniqueCheck" fieldtoreset="code" fields="['Value']" url='masters/serviceType!codeUniqueCheck.action'/>			
                                               </td> <td class="bluebox"/>
                                               <td   width="12%" class="bluebox"></td>
                                               <td class="bluebox"><s:text name="Service Type Description" /><span class="mandatory">*</span>:</td>
						        <td  class="bluebox"><s:textfield name="description" id="description" size="65" maxLength="256" onblur="checkNotSpecial(this)"
								Class="bluebox" /></td>
					              <td   width="60%" class="bluebox"></td> 
					              </tr>
					              <tr>
					              <td width="12%" class="greybox">&nbsp;</td>
					              <td   width="12%" class="greybox"></td>
                                               <td class="greybox"><s:text name="Service Type Description Local" /></td>
						        <td  class="greybox"><s:textfield name="descriptionLocal" id="descriptionLocal" maxLength="256" Class="bluebox" /></td>
					              <td class="greybox"/><td class="greybox"/><td class="greybox"/><td class="greybox"/><td class="greybox"/> 
					              </tr>
					                <tr>
		                                 <td width="12%" class="bluebox">&nbsp;</td>
		                                 <td width="12%" class="bluebox">&nbsp;</td>
					       
						        <td width="10%" class="bluebox"><s:text name="Service Number Prefix" /><span class="mandatory">*</span>:</td>
						        <td  class="bluebox"><s:textfield name="serviceNumberPrefix" id="serviceNumberPrefix" onblur="checkNotSpecial(this);checkNumberForLength()"
								/></td>  
								<td id="addlink" class="bluebox">
									<div class="buttonholderrnew">
										<input type="button" value="Help"
										class="buttoncreatenewcase"  OnClick="return numberformat(this);" />
									</div>
								</td>
								  <td  width="12%" class="bluebox">&nbsp;</td>
					         <td  class="bluebox"><s:text name="Admission Fee Required(Y/N)" /><span class="mandatory">*</span>:</td>
                                              <td class="bluebox">  <s:select name="isScrutinyFeeRequired" id="isScrutinyFeeRequired" headerValue="----Choose----" headerKey="-1" 
				                 list="#{'true':'YES','false':'NO' }" Class="greybox">
		                                 </s:select></td>
		                                 <td   width="60%" class="bluebox"></td>
                                        </tr>
                                        <tr>     
                                      <td   width="12%" class="greybox"></td>
					             <td   width="12%" class="greybox"></td>
					         
                                             <td width="10%" class="greybox"><s:text name="Inspection Fee Required(Y/N)" /><span class="mandatory">*</span>:</td>
                                            <td class="greybox">  <s:select name="IsInspectionFeeRequired" id="IsInspectionFeeRequired" headerValue="----Choose----" headerKey="-1" 
				                 list="#{'true':'YES','false':'NO' }" Class="greybox">
		                                 </s:select></td>
		                                 <td   width="12%" class="greybox"></td>
		                             
					           <td    class="greybox"></td>               <td width="24%" class="greybox"><s:text name="Ptis Integrated Required(Y/N)" /><span class="mandatory">*</span>:</td>
                                          <td  class="greybox">     <s:select name="isPtisNumberRequired" id="isPtisNumberRequired" headerValue="---Choose----"headerKey="-1"
				               list="#{'true':'YES','false':'NO' }" Class="greybox">
		                                 </s:select></td>
		                                  <td   width="60%" class="greybox"></td>
		                                </tr>
                                        <tr>
                                        <td  width="12%" class="bluebox">&nbsp;</td>
					                      <td width="12%" class="bluebox">&nbsp;</td>
                                               <td width="10%"  class="bluebox"><s:text name="AutoDcr Integrated Required(Y/N)" /><span class="mandatory">*</span>:</td>
                                              <td class="bluebox"> <s:select name="ISAutoDcrNumberRequired" id="ISAutoDcrNumberRequired" headerValue="----Choose---" headerKey="-1" 
				                list="#{'true':'YES','false':'NO' }" Class="bluebox"> 
		                                 </s:select></td>
		                                <td class="bluebox"/>
		                                  <td  width="12%" class="bluebox">&nbsp;</td>
                                <td class="bluebox"><s:text name="Application Form(Citizen/Cmda)" /><span class="mandatory">*</span>:</td>
                                          <td  class="bluebox">     <s:select name="isCmdaType" id="isCmdaType" headerValue="---Choose-----"headerKey="-1"
				               list="#{'true':'CMDA','false':'CITIZEN' }" Class="bluebox">
		                                 </s:select></td>
		                                  <td   width="60%" class="bluebox"></td>
                                        </tr>
		                                </table>
               <div id="actionbuttons" align="center" class="buttonbottom"> 
             
					
				
		  <td colspan="4" align="center">
		  
		
		  <s:if test="%{mode !='edit' && mode!='view'}">
            <s:submit type="submit" cssClass="buttonsubmit"  method="create" value="Save"  onclick="return validateForm();"   />		  
		  </s:if>
		  
		  <s:if test="%{mode =='edit'}">
            <s:submit type="submit" cssClass="buttonsubmit"  method="create" value="Modify" onclick="return validateForm();"    />          		
          </s:if> 
         
          <s:submit type="submit" cssClass="buttonsubmit" value="Back" id="Back" name="Back" method="searchList" />
            
		   <input type="button" name="close" id="close" class="button" value="Close" onclick=" window.close();" ></td>
	    </tr>
					      
				     
				</div>
			</s:push>
		</s:form>
</body>
</html>