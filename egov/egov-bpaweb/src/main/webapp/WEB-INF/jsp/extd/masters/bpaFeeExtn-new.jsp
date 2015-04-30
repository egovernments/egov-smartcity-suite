#-------------------------------------------------------------------------------
# #-------------------------------------------------------------------------------
# # <!-- #-------------------------------------------------------------------------------
# # # eGov suite of products aim to improve the internal efficiency,transparency, 
# # #    accountability and the service delivery of the government  organizations.
# # # 
# # #     Copyright (C) <2015>  eGovernments Foundation
# # # 
# # #     The updated version of eGov suite of products as by eGovernments Foundation 
# # #     is available at http://www.egovernments.org
# # # 
# # #     This program is free software: you can redistribute it and/or modify
# # #     it under the terms of the GNU General Public License as published by
# # #     the Free Software Foundation, either version 3 of the License, or
# # #     any later version.
# # # 
# # #     This program is distributed in the hope that it will be useful,
# # #     but WITHOUT ANY WARRANTY; without even the implied warranty of
# # #     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# # #     GNU General Public License for more details.
# # # 
# # #     You should have received a copy of the GNU General Public License
# # #     along with this program. If not, see http://www.gnu.org/licenses/ or 
# # #     http://www.gnu.org/licenses/gpl.html .
# # # 
# # #     In addition to the terms of the GPL license to be adhered to in using this
# # #     program, the following additional terms are to be complied with:
# # # 
# # # 	1) All versions of this program, verbatim or modified must carry this 
# # # 	   Legal Notice.
# # # 
# # # 	2) Any misrepresentation of the origin of the material is prohibited. It 
# # # 	   is required that all modified versions of this material be marked in 
# # # 	   reasonable ways as different from the original version.
# # # 
# # # 	3) This license does not grant any rights to any user of the program 
# # # 	   with regards to rights under trademark law for use of the trade names 
# # # 	   or trademarks of eGovernments Foundation.
# # # 
# # #   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
# # #------------------------------------------------------------------------------- -->
# #-------------------------------------------------------------------------------
#-------------------------------------------------------------------------------
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>

		

	
		
<title>
	<s:if test="%{mode=='view'}">
  		<s:text name="View Fees Type "/>
	</s:if>
	<s:elseif test="%{mode=='edit'}">
  		<s:text name="Modify Fees Type "/>
	</s:elseif>
	  <s:else>
	  <s:text 
	  name="Fees Master"/>
	  </s:else>
</title>
<head>	
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
    #fundDiv { width:100%; }
    
}
    </style>
</head>
 <link rel="stylesheet" type="text/css" href="<c:url value='/common/css/jquery/jquery-ui-1.8.22.custom.css'/>" />
   <script type="text/javascript" src="<c:url value='/common/js/jquery/jquery-1.7.2.min.js'/>"></script>
   <script type="text/javascript" src="<c:url value='/common/js/jquery/jquery-ui-1.8.22.custom.min.js'/>"></script>
   <script type="text/javascript" src="<c:url value='/common/js/combobox.js'/>"></script>
 <script type="text/javascript">

 var index=0;
 
 jQuery.noConflict();
 jQuery(document).ready(function() {
	 jQuery("#fund").combobox();
	 jQuery("#function").combobox();
	 jQuery("#glcode").combobox();
	    jQuery('.ui-autocomplete-input').css('width', '80%');
	        
		
	}

	);

function onChangeOfisFixedAmount()
{
	 var fixAmt=jQuery("#isFixedAmount").val();
		//alert("serviceTypeValue "+fee);
		if(fixAmt != "true"){
			jQuery('#divFixedPrice').hide();
			jQuery('#feesdetailsid').show();
			document.getElementById("fixedPrice").value="";
		}		
		else{
			jQuery('#divFixedPrice').show();
			jQuery('#feesdetailsid').hide();
			clearRows();
		}
		
	}

function onLoadFixedAmountDetails()
{
	 var fixAmt=jQuery("#isFixedAmount").val();
	 var fee=jQuery("#feeType").val();

	 if(fee != "AdmissionFee"){
		 jQuery('#feesdetailsid').hide();	
			jQuery('#divFixedAmount').hide();
			jQuery('#divFixedPrice').hide();
			document.getElementById("isFixedAmount").value="-1";
			jQuery('#divFeeGroup').show();
			jQuery('#divFeeGroupdtl').show();
	 }else
		 {
		 if(fixAmt != "true"){
				jQuery('#divFixedPrice').hide();
				jQuery('#feesdetailsid').show();
			
			}		
			else{
				jQuery('#divFixedPrice').show();
				jQuery('#feesdetailsid').hide();
			
			}
		 	jQuery('#divFeeGroup').hide();
			jQuery('#divFeeGroupdtl').hide();
		 }
		}
 
 function  show_province(){

	 //alert("jquery");
	 var fee=jQuery("#feeType").val();
		//alert("serviceTypeValue "+fee);
		if(fee != "AdmissionFee"){
		//alert("sanctionfee");
			jQuery('#feesdetailsid').hide();	
			jQuery('#divFixedAmount').hide();
			jQuery('#divFixedPrice').hide();
			jQuery('#divFeeGroup').show();
			jQuery('#divFeeGroupdtl').show();
			
			document.getElementById("isFixedAmount").value="-1";
			
			 
		}		
		else{
	//alert("admissionfee");
			jQuery('#feesdetailsid').show();
			jQuery('#divFixedAmount').show();	
			jQuery('#divFixedPrice').show();
			jQuery('#divFeeGroup').hide();
			jQuery('#divFeeGroupdtl').hide();
		}
}

 
 function showerrormsg(obj,msg){
	// alert("showmethod");
 dom.get("Bpaservice_error").style.display = '';
 document.getElementById("Bpaservice_error").innerHTML =msg;
 jQuery(obj).css("border", "1px solid red");		
 return false;
 }
    
 function validateForm()
 {   
	 hideFieldError();
	 var planfee=jQuery("input[name='isPlanningPermitFee']:checked").val();
		alert("validatefor" +planfee);
	 dom.get("Bpaservice_error").style.display='none';
	 if( jQuery('#feeType').val()=="-1"){
		   showerrormsg(jQuery('#feeType'),"Please Enter FeesType");
		   return false;
		     }
			 
	if( jQuery('#feeCode').val()==""){
		   showerrormsg(jQuery('#feeCode'),"Please Enter FeesCode");
		   return false;
	} 
     if(jQuery('#feeDescription').val()==""){
  		
    	 showerrormsg(jQuery('#feeDescription'),"Please Enter Fees Description");
    	 return false;
	}
	   if(jQuery('#fund').val()=="-1"){
		   showerrormsg(jQuery('#fund'),"Please Select Fund");
		   return false;
	} 
	   if(jQuery('#function').val()=="-1"){
		   showerrormsg(jQuery('#function'),"Please Select Function");
		   return false;
  } 
	   if(jQuery('#glcode').val()=="-1")
	   {
		   showerrormsg(jQuery('#glcode'),"Please Select Budget Head");
		   return false;
      } 

	   if( jQuery("#feeType").val()=="AdmissionFee")
		{	 
		   if(jQuery('#isFixedAmount').val()=="-1")
		   {
			   showerrormsg(jQuery('#isFixedAmount'),"Please Select IsFixed Amount");
			   return false;     
	    	} 
	    
			   if(jQuery('#isFixedAmount').val()=="true")
			   {
					   if(jQuery('#fixedPrice').val()==""){
					   		   showerrormsg(jQuery('#fixedPrice'),"Please enter fees Amount");
					   		   return false;
					   }
			       
		    		}
			   if(jQuery('#isFixedAmount').val()=="false")
			   {
				  if(!validateFeesDetail())
					  { return false;
					  }
			   }
		}	
		
		 if( jQuery("#feeType").val()!="AdmissionFee")
		{	 
		if(jQuery('#feeGroup').val()=="-1"){
		   showerrormsg(jQuery('#feeGroup'),"Please Select Fee Group");
		   return false;
  } 
      }
return true;
 }      


 function closeWindow()
 {
   window.close();
   }

 function validateCodeUniqueCheck(obj)
 {

  if(!document.getElementById("feeCode").value=="" && document.getElementById("feeCode").value!=null)
 {
   
   var feecode=document.getElementById("feeCode").value;
   populatecodeUniqueCheck({feecode:feecode});

 }
  else
 {
  
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
 function validateIsNan(obj)
 {
	 dom.get("Bpaservice_error").style.display='none';
	    if(obj!=null && obj.value!=null && isNaN(obj.value))
	    {
	    	dom.get("Bpaservice_error").style.display='';
			document.getElementById("Bpaservice_error").innerHTML='Pleas Enter only Numbers'
			 dom.get(obj).value = ""; 
		 return false;
	    }
	   return true; 
	 }
 function validateNumeric(obj)
 {      
 	if(!checkForLength()){
 		return false;
 	}
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
 	
 	 document.getElementById("feeCode").disabled=false;
 	 var FeeCode=document.getElementById('feeCode');
 	 //alert("length"+ codea.value.length);
 	  if(FeeCode.value.length==3)
 		  {
 		 dom.get("Bpaservice_error").style.display='none';
 		  return true;
 	      }
 	  else
 		  {
		if(FeeCode.value!=""){
 		  dom.get("Bpaservice_error").style.display='';
 		  document.getElementById("Bpaservice_error").innerHTML='Pleas Enter only three Digit Numbers'
 			  FeeCode.value=""; 
 		  return false;
 		   
		}
     	  return false;
 	  }
 }



 //This is to add Multiple rows to a shopdetail table

 function addRow()
  {     //alert('addrow');
  		if(validateFeesDetail())
  		{
  			//alert('addrowin');
 	    	var tableObj=document.getElementById('feesdetails');
 			var tbody=tableObj.tBodies[0];
 			var lastRow = tableObj.rows.length;
 			var rowObj = tableObj.rows[1].cloneNode(true);
 			tbody.appendChild(rowObj);
 			var rowno = parseInt(tableObj.rows.length)-2;
 			document.forms[0].srlNo[lastRow-1].value=tableObj.rows.length - 1;								
 			document.forms[0].fromAreasqmt[lastRow-1].value="";
 			document.forms[0].toAreasqmt[lastRow-1].value="";
 			document.forms[0].amount[lastRow-1].value="";
            document.forms[0].feeDetailId[lastRow-1].value="";
 		    document.forms[0].srlNo[lastRow-1].setAttribute("name","feedetailsList["+index+"].srlNo");
 			document.forms[0].fromAreasqmt[lastRow-1].setAttribute("name","feedetailsList["+index+"].fromAreasqmt");
 			document.forms[0].toAreasqmt[lastRow-1].setAttribute("name","feedetailsList["+index+"].toAreasqmt");
            document.forms[0].amount[lastRow-1].setAttribute("name","feedetailsList["+index+"].amount");
 		document.forms[0].feeDetailId[lastRow-1].setAttribute("name","feedetailsList["+index+"].id");
 			index++;
 		}
   }
   
  
  //This method is to remove rows from shopdetail table 
   function removeRow(obj)
   { 
	   // alert("hello");
     	var tb1=document.getElementById("feesdetails");
        var lastRow = (tb1.rows.length)-1;
        var curRow=getRow(obj).rowIndex;
        dom.get("Bpaservice_error").style.display='none';
        if(lastRow ==1)
      	{
     		 dom.get("Bpaservice_error").style.display='none';
     		 document.getElementById("Bpaservice_error").innerHTML='This row can not be deleted';
   			 dom.get("Bpaservice_error").style.display='';
      	     return false;
        }
      	else
      	{
       		var updateserialnumber=curRow;
 			for(updateserialnumber;updateserialnumber<tb1.rows.length-1;updateserialnumber++)
 			{
 				if(document.forms[0].srlNo[updateserialnumber]!=null)
 					document.forms[0].srlNo[updateserialnumber].value=updateserialnumber;
 			}
 			tb1.deleteRow(curRow);
 	      	return true;
      }
   }

   // This method is to validate Line level items
   
   function validateFeesDetail()
  {    
      var tableObj=document.getElementById('feesdetails');
      var lastRow = tableObj.rows.length;
      var FromAreasqmt,ToAreasqmt,Amount;
      var i;
      if(lastRow>2)
      {
     	// alert("row=2");
      	for(i=0;i<lastRow-1;i++)
      	{   
          	//alert("hi for condition");
           	 FromAreasqmt=document.forms[0].fromAreasqmt[i].value;
            	 ToAreasqmt=document.forms[0].toAreasqmt[i].value;
                   Amount=document.forms[0].amount[i].value;
            	 if(!validateFeesLines(FromAreasqmt,ToAreasqmt,Amount,i+1))
            	   return false;
     		}
     		return true;
     	}
     	else
     	{
     	    FromAreasqmt=document.getElementById('fromAreasqmt').value;
     	    ToAreasqmt=document.getElementById('toAreasqmt').value;
               Amount=document.getElementById('amount').value;
     	    if(!validateFeesLines(FromAreasqmt,ToAreasqmt,Amount,1))
     	       return false;
     	     else
     	       return true;
     	    
     	}
    }
    
    function validateFeesLines(FromAreasqmt,ToAreasqmt,Amount,row)
    {
          dom.get("Bpaservice_error").style.display='none';
           if(FromAreasqmt==""||ToAreasqmt==""||Amount=="")
         {
              // alert("FromAreasqmt");
                  document.getElementById("Bpaservice_error").innerHTML='';
     				document.getElementById("Bpaservice_error").innerHTML='Fees Details is Required for row:'+" "+row;
     				dom.get("Bpaservice_error").style.display='';
      			return false;
         }else if(FromAreasqmt!="" && ToAreasqmt!="")
             {
               if(FromAreasqmt>=ToAreasqmt)
                   {
            	   document.getElementById("Bpaservice_error").innerHTML='';
    				document.getElementById("Bpaservice_error").innerHTML='Sq Mt. From should be less than Sq Mt.to  for the row:'+" "+row;
    				dom.get("Bpaservice_error").style.display='';
     			return false;
                   }

              }
          
           return true;
    }
    function clearRows()
    {
   	var table = document.getElementById('feesdetails');
       var rowslength = table.rows.length;
       while(rowslength>2) {
           table.deleteRow(rowslength-1);
           rowslength--;
       }
      document.getElementById('fromAreasqmt').value=""; 
      document.getElementById('toAreasqmt').value="";
      document.getElementById('amount').value="";
      document.getElementById('feeDetailId').value="";
    }  

    function validateQty(obj)
    {
      
    	 if(obj!=null && obj.value!=null && isNaN(obj.value))
  	    {
  	    	dom.get("Bpaservice_error").style.display='';
  			document.getElementById("Bpaservice_error").innerHTML='Pleas Enter only Numbers'
  			 dom.get(obj).value = ""; 
   		 return false;
  	    }
    }

   
    function hideFieldError()
    {
  		if (dom.get("fieldError") != null)
  			dom.get("fieldError").style.display = 'none';
  		document.getElementById('codeUniqueCheck').style.display = 'none';
  		
   }
    function bodyOnLoad()
    {	
    	 //document.getElementById("isActive").checked=true;	
	       var mode=document.getElementById("mode").value;
	       index=document.getElementById('feesdetails').rows.length-1;
	        onLoadFixedAmountDetails();
	       if(mode!=null && mode=='edit')
		   {
	    	 //  show_province();
		   document.getElementById("feeCode").disabled=true;
		   document.getElementById("feeDescription").disabled=true;
		   document.getElementById("feeType").disabled=true;
		   document.getElementById("serviceType").disabled=true;
		   jQuery('#glcodeDiv').find('input,select,button').attr('disabled','true');	 
		   
	   }
	       if(mode!=null && mode=='view')
           {  // show_province();
	    	   for(var i=0;i<document.forms[0].length;i++)
	    		{  
	    		 
		    	document.forms[0].elements[i].disabled =true;
		    	 document.getElementById("close").disabled=false;
	    		  document.getElementById("Back").disabled=false;
	    		  
	    		}
	    		
           	}  
	      // clearRows();                              
       }
    function enableFields(){
    	for(var i=0;i<document.forms[0].length;i++)
    	{
    		document.forms[0].elements[i].disabled =false;
    	}
    }
     
      	  
    
   
  </script> 


<body onload="bodyOnLoad();" >
	
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
	<div class="errorstyle" id="Bpaservice_error" style="display:none;"></div>
        <s:form action="bpaFeeExtn" name="bpaFeeActionForm" theme="simple" onsubmit="enableFields()">
        <s:token />
		<s:push value="model">
		
		<div class="formheading"/></div>
		<s:hidden id="modifiedDate" name="modifiedDate" value="%{modifiedDate}" />
		<s:hidden id="modifiedBy" name="modifiedBy" value="%{modifiedBy.id}" />
		<s:hidden id="createdBy" name="createdBy" value="%{createdBy.id}" />
		<s:hidden name="id" id="id" value="%{id}"  />
		<s:hidden name="idTemp" id="idTemp" value="%{idTemp}"  />
		
		<s:hidden id="mode" name="mode" value="%{mode}" />	
		
		<div class="mandatory" style="display: none"
				id="codeUniqueCheck">
				<s:text name="fees.code.exists"/>
		</div>
	
	   	<table width="100%" border="0" cellspacing="0" cellpadding="2">
	   	
	   
         					<tr>             
                            <td width="20%" class="greybox"></td>
                              <td  class="greybox"></td>
						         <td class="greybox"><s:text name="Fees Type"/><span class="mandatory">*</span>:</td>
				             <td class="greybox"><s:select name="feeType" id="feeType"  headerValue="----Choose---" headerKey="-1" 
				                 list="#{'Sanction Fees':'Sanction Fees' ,'AdmissionFee':'AdmissionFee'}"  onchange="show_province();" Class="bluebox">
		                             </s:select></td>
		                            <td  class="greybox" width="10%"></td>
		                             <td class="greybox"><s:text name="Service Type"/> <span class="mandatory">*</span></td>
							<td class="greybox"><s:select  id="serviceType" name="serviceType.id"  value="%{serviceType.id}" list="dropdownData.servicetypeList" listKey="id" listValue="code+'-'+description"  headerKey="-1" headerValue="----Choose----" maxsize = "256"  /> 	
		                         </td> <td width="20%"class="greybox"></td>
		                         </td> <td width="60%"class="greybox"></td>
				               </tr>
		                       <tr>
		                    <td width="20%" class="bluebox"></td>
		                     <td class="bluebox"></td>
				           <td class="bluebox"><s:text name="FeesCode"/><span class="mandatory">*</span>:</td>
				        
				           <td class="bluebox"> <s:textfield name="feeCode" id="feeCode"  value="%{feeCode}" onblur="validateCodeUniqueCheck(this);validateNumeric(this);"/>
				           <egov:uniquecheck id="codeUniqueCheck" fieldtoreset="feeCode" fields="['Value']" url='masters/bpaFee!codeUniqueCheck.action'			
                                Class="bluebox"    /></td>  
                                 <td class="bluebox" width="10%"></td>
				            <td class="bluebox" ><s:text name="Fees Description"/><span class="mandatory">*</span>:</td>
				             
				           <td class="bluebox"><s:textfield name="feeDescription" size="50" maxLength="64" id="feeDescription" value="%{feeDescription}" Class="greybox" onblur="checkNotSpecial(this)"/></td>
				             <td width="20%"class="bluebox"></td>
				              <td   width="60%" class="bluebox"></td>
				         </tr>
				         <tr> 
		                    <td width="20%" class="greybox"></td>
                                 <td class="greybox" width="10%"></td>
                                  <td class="greybox"><s:text name="Is Mandatory"/>:</td>
						       <td  class="greybox"><s:checkbox name="isMandatory" id="isMandatory" value="%{isMandatory}" />
				                 </td>
				                  <td class="greybox" width="10%"></td>
				            <td class="greybox" ><s:text name="Fees Description Local"/></td>
				             
				           <td class="greybox"><s:textfield name="feeDescriptionLocal" size="50" maxLength="64" id="feeDescriptionLocal" value="%{feeDescriptionLocal}" Class="greybox"/></td>
				                <td width="20%"class="greybox"></td>
				                 <td   width="60%" class="greybox"></td>
				         </tr>
				         <tr>
				            <td width="20%" class="bluebox"/>
				              <td  class="bluebox"></td>
				                <td class=bluebox><s:text name="Fee Fund"/>&nbsp;<span class="mandatory">*</span>:</td>
				             
						           <td  class="bluebox" width="50%">
				    <div id="fundDiv" >
				        <s:select  id="fund" name="fund" value="%{fund.id}" list="dropdownData.fundList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----" maxsize = "256" size="60" /> 	
		                          </div> </td>
		                          <td  class="bluebox" width="10%"></td>
				                 <td   width="40%" class="bluebox"><s:text name="Permit Type Fee"/></td>
				                 <td   width="20%" class="bluebox"><s:radio id="isPlanningPermitFee" name="isPlanningPermitFee" value="%{isPlanningPermitFee}" list="#{'false':'Building Permit','true':'Plan Permit'}"></s:radio></td>
				                  <td width="20%"class="bluebox"></td>
				               </tr>
					      <tr>
					          <td   width="5%" class="greybox"></td>
				         <td class="greybox"></td>
				         <td class="greybox" ><s:text name="Fees Function"/><span class="mandatory">*</span>:</td>
				             
				              <td class="greybox" width="50%">
				             <s:select  id="function" name="function" value="%{function.id}" list="dropdownData.functionList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----" maxsize = "256" size="60" /> 	
		                           </td>
				                 <td  class="greybox" width="10%"/>
                                          
		                              
		                               <td colspan="1" id="divFixedAmount" class="greybox"><s:text name="Is Fixed Amount(Y/N)" />&nbsp;<span class="mandatory">*</span>:
                                              <s:select name="isFixedAmount" id="isFixedAmount"  value="%{isFixedAmount}" headerValue="----Choose---" headerKey="-1" 
				                 list="#{'true':'YES','false':'NO' }" Class="greybox" onchange="onChangeOfisFixedAmount();" >
		                                 </s:select></td> 
		                                 
		                                  <td id="divFeeGroup" class="greybox"><s:text name="Fee Group" />&nbsp;<span class="mandatory">*</span>:
                                         </td><td id="divFeeGroupdtl" class="greybox"> <s:select name="feeGroup" id="feeGroup"  value="%{feeGroup}" headerValue="----Choose---" headerKey="-1" 
				                        list="dropdownData.feeGroupList"  Class="greybox"  >
		                                 </s:select></td>   
		                      
		                         <td id="divFixedPrice" class="greybox"> Fees Amount (in Rs.) <span class="mandatory">*</span>
		                         <s:textfield name="fixedPrice" size="6" maxLength="6" id="fixedPrice" value="%{fixedPrice}" Class="greybox" onblur="validateIsNan(this)" />
		                         </td>
				                <td width="20%"class="greybox"></td>
                            </tr>
				             <tr>
				              <td  class="bluebox" width="20%"></td>
				               <td  class="bluebox"></td>
					            <td class="bluebox" ><s:text name="Budget Head"/><span class="mandatory">*</span>:</td>
				              
				           <td class="bluebox" width="50%">
				           <div id="glcodeDiv">
				       			 <s:select  id="glcode" name="glcode.id" value="%{glcode.id}" list="dropdownData.glcodeList" listKey="id"  listValue='glcode+"/"+name' headerKey="-1" headerValue="----Choose----" maxsize = "256"  size="60" /> 	
		                        </div>  </td>  <td  class="bluebox" width="10%"></td>
						         <s:if test="%{mode =='edit'}">
						         
						           <td class="bluebox"><s:text name="Is Active"/><span class="mandatory">*</span>:</td>
						       <td  class="bluebox"><s:checkbox name="isActive" id="isActive" value="%{isActive}" />
				                 </td>
				                    </s:if>       
				                 <td   width="40%" class="bluebox"></td>
				                 <td   width="20%" class="bluebox"></td>
				                  <td   width="20%" class="bluebox"></td>
				                   <td width="20%"class="bluebox"></td>
				              
				        
				             </tr>
					       </table> 
					       <div id="feesdetailsid" >
					        <table id="feesdetails" width="60%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" align="center">
		                 	<div class="blueshadow"></div>
	                    	<h1 class="subhead" ><s:text name="Fees Section"/></h1>
	 						<div align="center">
	     					 <tr>
								<th  class="bluebgheadtd" width="3%"><div align="center">Sl No</div></th>
							    <th  class="bluebgheadtd" width="8%"><div align="center">Sq.Mtr From <span class="mandatory">*</span></div></th>
							    <th  class="bluebgheadtd" width="8%"><div align="center"> Sq.Mtr To  <span class="mandatory">*</span></div></th>
					            <th  class="bluebgheadtd" width="8%"><div align="center">Fees Amount(In Rs.)<span class="mandatory">*</span>:</div></th>
					             <s:if test="%{mode!='view'}">
							    <th  class="bluebgheadtd" width="2%"><div align="center"> Add</div></th>
								<th  class="bluebgheadtd" width="2%"><div align="center"> Delete</div></th>
								</s:if>
						  </tr>
		  				<s:iterator value="feedetailsList" status="row_status">
							    <tr>
							  	<td  class="blueborderfortd"><s:textfield name="feedetailsList[%{#row_status.index}].srlNo" id="srlNo" readonly="true"  cssClass="tablerow" value="%{#row_status.count}" cssStyle="text-align:center"/></td>
							    <td class="blueborderfortd">
							    	<div align="center">
							    		<s:textfield name="feedetailsList[%{#row_status.index}].fromAreasqmt" id="fromAreasqmt" maxlength="64" cssClass="tablerow"   size="64"  cssStyle="text-align:right" onblur="validateQty(this)"/>
							    	</div>
							    </td>
							    <td  class="blueborderfortd"><s:textfield name="feedetailsList[%{#row_status.index}].toAreasqmt" id="toAreasqmt" maxlength="8" cssClass="tablerow"   cssStyle="text-align:right"  onblur="validateQty(this)"/></td>
					             <td  class="blueborderfortd"><s:textfield name="feedetailsList[%{#row_status.index}].amount" id="amount" maxlength="8" cssClass="tablerow"   cssStyle="text-align:right" onblur="validateQty(this)"/></td>
					
					 <s:if test="%{mode!='view'}">
							   	 <td  class="blueborderfortd"><div align="center"><a href="#"><img src="${pageContext.request.contextPath}/images/addrow.gif" height="16"  width="16" border="2" alt="Add" onclick="addRow()"></a></div></td>
							   		<td  class="blueborderfortd"><div align="center"><a href="#"><img src="${pageContext.request.contextPath}/images/removerow.gif" height="16"  width="16" border="2" alt="Delete" onclick="removeRow(this);" ></a></div></td>
							    		</s:if>	
							    			 <s:hidden id="feeDetailId" name="feedetailsList[%{#row_status.index}].id" />
							    			  </tr>
							    
							   </s:iterator>
	  					</table>  
	  					</div >
	  					
	                   
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
