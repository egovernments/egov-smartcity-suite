<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>
<%@ include file="/includes/taglibs.jsp" %>

<html>
<title>
	<s:text name="Registration FeeDetails"/>
</title>	

<head>
<sj:head jqueryui="true" jquerytheme="redmond" loadAtOnce="true" />
<script type="text/javascript">

jQuery.noConflict();

jQuery(document).ready(function(){

 jQuery("[id=feeamount]").each(function(index){
  var feeval= jQuery(this).attr("value");
  if(feeval==""){
  jQuery(this).val(0);
  }
  });
 calculateTotal();
 
 
if( jQuery('#mode').val()=="view"){


for(var i=0;i<document.forms[0].length;i++)
			{
				document.forms[0].elements[i].disabled =true;
			}
			jQuery("#close").removeAttr('disabled');
			
			
			if( jQuery('#fromreg').val()=="true"){
            // window.opener.callfeedetails();
              }  
              
          
			}

if( jQuery('#mode').val()=="readOnlyMode"){


	for(var i=0;i<document.forms[0].length;i++)
				{
					document.forms[0].elements[i].readOnly =true;
				}
				jQuery("#close").removeAttr('disabled');
				
		}

});



function checkNumbers(){

 jQuery("[id=numbers]").each(function(index) {	

	var values=(jQuery(this).find('input').attr("value"));
	if(values!=null&&values!=""){
   		checkUptoTwoDecimalPlace(values,"shop_error",jQuery(this).find('input').attr("id"),jQuery(this).find('input'));
   		}
   		});
}

function validation(){



if(!mandatorycheckvalidation()){
    return false;
   }
  
   	return true;
}



function mandatorycheckvalidation(){

var count=0;
 jQuery("[id=mandatorycheck]").each(function(index) {	
	var values=(jQuery(this).find('input').attr("value"));
	if(values=='true'){	
   		if(jQuery(this).find('input:last').attr('value')==""||jQuery(this).find('input:last').attr('value')==0){
   		showerrormsg(jQuery(this).find('input:last'),"Please enter the Fee Amount");
   		count++;
   		
   		  }
   		} 		
   		});	
   		
   		if(count==0)
   		return true;
   		else
   		return false
   		}
   		
function showerrormsg(obj,msg){
dom.get("shop_error").style.display = '';
document.getElementById("shop_error").innerHTML =msg;
jQuery(obj).css("border", "1px solid red");		

}



function calculateTotal(){
 var coctotal=0;
  var cmdatotal=0;
   var mwgwftotal=0;
   var grandtotal=0;
resetborder();
 jQuery("[id=cocfeeamount]").each(function(index){
  var feeval= jQuery(this).attr("value");
  if(feeval!=""){
  checkNumbers(feeval);
  coctotal=new Number(coctotal)+new Number(feeval);

  }
 });	
 jQuery("#coctotal").val(coctotal);
 
  jQuery("[id=cmdafeeamount]").each(function(index){
  var feeval= jQuery(this).attr("value");
  if(feeval!=""){ 
  checkNumbers(feeval);
  cmdatotal=new Number(cmdatotal)+new Number(feeval);
  
  }
 });	
 jQuery("#cmdatotal").val(cmdatotal);
 
 
  jQuery("[id=mwgwffeeamount]").each(function(index){
  var feeval= jQuery(this).attr("value");
  if(feeval!=""){
  checkNumbers(feeval);
  mwgwftotal=new Number(mwgwftotal)+new Number(feeval);
   
  } 
 });	
 jQuery("#mwgwftotal").val(mwgwftotal);
 
 
 
  jQuery("#grandtotal").val(mwgwftotal+cmdatotal+coctotal);
 
}

function resetborder(){
dom.get("shop_error").style.display = 'none';
jQuery("[id=numbers]").each(function(index) {
		jQuery(this).find('input').css("border", "");
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

<s:form action="feeDetailsExtn" theme="simple" onkeypress="return disableEnterKey(event);" >
<s:token/>
<s:hidden id="registrationId" name="registrationId" value="%{registrationId}"/>
<s:hidden id="mode" name="mode" value="%{mode}"/>
<s:hidden id="fromreg" name="fromreg" value="%{fromreg}"/>

<div align="center"> 
 <div id="regfeeetails" class="formmainbox">
 <div align="center">
 <table id="feehead" width="80%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
   <tr>
	 			
			   <td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="13%"><s:text name="Challan Number"/></td> 
	   			<td class="bluebox"> <s:textfield readonly="true" value="%{registrationObj.registrationFeeChallanNumber}" id="challanNumber" name="challanNumber"  disabled="true" /></td>   						
				<td class="bluebox" >&nbsp;</td>
				<td class="bluebox" >&nbsp;</td>
	       
          </tr>
          </table>
          </div>
     <div>
	<div align="center" id="feelistdiv">
	<s:if test="%{COCFeeList.size!=0}">
	<h1 class="subhead" ><s:text name="COC Fees"/></h1>
	  
		   <table id="feelists" width="80%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
	     
	       <tr>
			<th  class="bluebgheadtd" width="3%"><div align="center">Sl No</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">Description</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">Amount</div></th>
		    </tr>
		    
		  <s:iterator value="COCFeeList" status="row_status">
		    <tr>
		  	<td  class="blueborderfortd"><div align="center"><s:text name="#row_status.count" /></div></td>
		    <td class="blueborderfortd"><div align="center"><s:property value="%{feeDescription}"/>
		    <s:if test="{COCFeeList[%{#row_status.index}].isMandatory=='true'}"><span class="mandatory">*</span></s:if></div>
		    <s:hidden  value="COCFeeList[%{#row_status.index}].isMandatory"/></td>
		    
		    
		    <td class="blueborderfortd" id="mandatorycheck"><s:hidden name="COCFeeList[%{#row_status.index}].isMandatory"/><div align="center" id="numbers">
		    <s:textfield id="cocfeeamount" name="COCFeeList[%{#row_status.index}].feeAmount"  onblur="calculateTotal()"/></div></td>		  			  		    
		    <s:hidden  name="COCFeeList[%{#row_status.index}].id"/>
		      <s:hidden  name="COCFeeList[%{#row_status.index}].feeType"/>
		        <s:hidden  name="COCFeeList[%{#row_status.index}].feeCode"/>
		        <s:hidden  name="COCFeeList[%{#row_status.index}].demandDetailId"/>
		         <s:hidden  name="COCFeeList[%{#row_status.index}].feeDescription"/>
		            <s:hidden  name="COCFeeList[%{#row_status.index}].feeGroup"/>
		            <s:hidden  name="COCFeeList[%{#row_status.index}].glcode.id"/>
		    
		    </tr>
		    
		    
		    </s:iterator>
		     <tr>
			  <td class="blueborderfortd"><div align="center"></div></td>				
			  <td class="blueborderfortd"><div align="center"><s:text name="Total" /></div></td>	
			   <td class="blueborderfortd"><div align="center"><s:textfield id="coctotal" name="total" value="" disabled="true"/></div></td>	
		    </tr>
		      <s:if test="%{CMDAFeeList.size==0 && MWGWFFeeList.size==0}">
		       <tr>
			  <td class="blueborderfortd"><div align="center"></div></td>				
			  <td class="blueborderfortd"><div align="center"><b><s:text name="Grand Total" /></b></div></td>	
			   <td class="blueborderfortd"><div align="center"><s:textfield id="grandtotal" name="total" value="" disabled="true"/></div></td>	
		    </tr>
		      </s:if>
		    </table>
		    </s:if>
		    <s:if test="%{CMDAFeeList.size!=0}">
	
	  <h1 class="subhead" ><s:text name="CMDA Fees"/></h1>
		   <table id="feelists" width="80%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
	     
	       <tr>
			<th  class="bluebgheadtd" width="3%"><div align="center">Sl No</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">Description</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">Amount</div></th>
		    </tr>
		    <s:iterator value="CMDAFeeList" status="row_status">
		    <tr>
		  	<td  class="blueborderfortd"><div align="center"><s:text name="#row_status.count" /></div></td>
		    <td class="blueborderfortd"><div align="center"><s:property value="%{feeDescription}"/>
		    <s:if test="{CMDAFeeList[%{#row_status.index}].isMandatory=='true'}"><span class="mandatory">*</span></s:if></div>
		    <s:hidden  value="CMDAFeeList[%{#row_status.index}].isMandatory"/></td>
		    
		    
		    <td class="blueborderfortd" id="mandatorycheck"><s:hidden name="CMDAFeeList[%{#row_status.index}].isMandatory"/><div align="center" id="numbers">
		    <s:textfield id="cmdafeeamount" name="CMDAFeeList[%{#row_status.index}].feeAmount"  onblur="calculateTotal()"/></div></td>		  			  		    
		    <s:hidden  name="CMDAFeeList[%{#row_status.index}].id"/>
		      <s:hidden  name="CMDAFeeList[%{#row_status.index}].feeType"/>
		        <s:hidden  name="CMDAFeeList[%{#row_status.index}].feeCode"/>
		        <s:hidden  name="CMDAFeeList[%{#row_status.index}].demandDetailId"/>
		         <s:hidden  name="CMDAFeeList[%{#row_status.index}].feeDescription"/>
		            <s:hidden  name="CMDAFeeList[%{#row_status.index}].feeGroup"/>
		            <s:hidden  name="CMDAFeeList[%{#row_status.index}].glcode.id"/>
		    
		    </tr>
		    
		    </s:iterator>
		     <tr>
			  <td class="blueborderfortd"><div align="center"></div></td>				
			  <td class="blueborderfortd"><div align="center"><s:text name="Total" /></div></td>	
			   <td class="blueborderfortd"><div align="center"><s:textfield id="cmdatotal" name="total" value="" disabled="true"/></div></td>	
		    </tr>
		    <s:if test="%{MWGWFFeeList.size==0}">
		       <tr>
			  <td class="blueborderfortd"><div align="center"></div></td>				
			  <td class="blueborderfortd"><div align="center"><b><s:text name="Grand Total" /></b></div></td>	
			   <td class="blueborderfortd"><div align="center"><s:textfield id="grandtotal" name="total" value="" disabled="true"/></div></td>	
		    </tr>
		      </s:if>
		    </table>
		    </s:if>
		     <s:if test="%{MWGWFFeeList.size!=0}">
	<h1 class="subhead" ><s:text name="M.W.G.W.F Fees"/></h1>
	  
		   <table id="feelists" width="80%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
	     
	       <tr>
			<th  class="bluebgheadtd" width="3%"><div align="center">Sl No</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">Description</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">Amount</div></th>
		    </tr>
		    <s:iterator value="MWGWFFeeList" status="row_status">
		    <tr>
		  	<td  class="blueborderfortd" ><div align="center"><s:text name="#row_status.count" /></div></td>
		    <td class="blueborderfortd"><div align="center"><s:property value="%{feeDescription}"/>
		    <s:if test="{MWGWFFeeList[%{#row_status.index}].isMandatory=='true'}"><span class="mandatory">*</span></s:if></div>
		    <s:hidden  value="MWGWFFeeList[%{#row_status.index}].isMandatory"/></td>
		    
		    
		    <td class="blueborderfortd" id="mandatorycheck" ><s:hidden name="MWGWFFeeList[%{#row_status.index}].isMandatory"/><div align="center" id="numbers">
		    <s:textfield id="mwgwffeeamount" name="MWGWFFeeList[%{#row_status.index}].feeAmount"  onblur="calculateTotal()"/></div></td>		  			  		    
		    <s:hidden  name="MWGWFFeeList[%{#row_status.index}].id"/>
		      <s:hidden  name="MWGWFFeeList[%{#row_status.index}].feeType"/>
		        <s:hidden  name="MWGWFFeeList[%{#row_status.index}].feeCode"/>
		        <s:hidden  name="MWGWFFeeList[%{#row_status.index}].demandDetailId"/>
		         <s:hidden  name="MWGWFFeeList[%{#row_status.index}].feeDescription"/>
		     <s:hidden  name="MWGWFFeeList[%{#row_status.index}].feeGroup"/>
		     <s:hidden  name="MWGWFFeeList[%{#row_status.index}].glcode.id"/>
		    </tr>
		    </s:iterator>
		  
		     <tr>
			  <td class="blueborderfortd"><div align="center"></div></td>				
			  <td class="blueborderfortd"><div align="center"><s:text name="Total" /></div></td>	
			   <td class="blueborderfortd"><div align="center"><s:textfield id="mwgwftotal" name="total" value="" disabled="true"/></div></td>	
		    </tr>
		 
		       <tr>
			  <td class="blueborderfortd"><div align="center"></div></td>				
			  <td class="blueborderfortd"><div align="center"><b><s:text name="Grand Total" /></b></div></td>	
			   <td class="blueborderfortd"><div align="center"><s:textfield id="grandtotal" name="total" value="" disabled="true"/></div></td>	
		    </tr>
		    
	  </table>	
	
		 
	  </s:if>
	  
	  
	   <div align="center" class="formmainbox">
	  <h1 class="subhead" ><s:text name="Remarks"/></h1>
	 
	  <table id="feelistremarks" width="37%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
	   <tr>		 
			   <td class="blueborderfortd"><div align="center"><s:textarea cols="100" rows="10" id="feeRemarks" name="feeRemarks" value="%{feeRemarks}" /></div></td>	
		    </tr>
	  </table>
	   </div>
    </div>
	</div> 
	<s:if test="%{santionFeeList.size!=0}">
<div class="buttonbottom" align="center">
		<table>
		<tr>
		 <s:if test="%{mode!='view'}">
		 
		  	<td><s:submit  cssClass="buttonsubmit" id="save" name="save" value="Save" method="create" onclick="return validation();"/></td>	
	  			</s:if>	         
	  		 <td><input type="button" name="close" id="close" class="button"  value="Close" onclick="window.close();"/>
	  		</td>
	  	</tr>
        </table>
   </div>	
   	</s:if>
   	<s:else>
    <div> No Feedetails found for the service type.Please enter the fee details in the master screen and try again.</div>
     <input type="button" name="close" id="close" class="button"  value="Close" onclick="window.close();"/>
   	</s:else>
</div>
	








	
</s:form>
</body>
</html>



