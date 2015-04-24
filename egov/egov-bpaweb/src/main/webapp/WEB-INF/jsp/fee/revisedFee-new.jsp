<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>
<%@ include file="/includes/taglibs.jsp" %>

<html>
<title>
	<s:text name="Revised Fee Details"/>
</title>	

<head>
<sj:head jqueryui="true" jquerytheme="redmond"  loadAtOnce="true" />
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
		
         
			}
			
			
	 var dept='<s:property value="departmentForLoggedInUser"/>';
				 if(dept!=null){
				jQuery("#approverDepartment option[value!='<s:property value="departmentForLoggedInUser"/>']").each(function() {
				if(jQuery(this).val()!=-1)
				    jQuery(this).remove();
			});
				 }		
			

});



function checkNumbers(){

 jQuery("[id=numbers]").each(function(index) {	

	var values=(jQuery(this).find('input').attr("value"));
	if(values!=null&&values!=""){
   		checkUptoTwoDecimalPlace(values,"fee_error",jQuery(this).find('input').attr("id"),jQuery(this).find('input'));
   		}
   		});
}

function validateForm(obj){

document.getElementById('workFlowAction').value=obj;

if(!mandatorycheckvalidation()){
    return false;
   }
  
   if(obj=='Forward'||obj=='forward')	{
	if(document.getElementById('approverPositionId').value=="-1" ||document.getElementById('approverPositionId').value==""){
	     document.getElementById('fee_error').style.display='';
         document.getElementById('fee_error').innerHTML = "Please Select the Approver Details";
         return false;
		}
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
dom.get("fee_error").style.display = '';
document.getElementById("fee_error").innerHTML =msg;
jQuery(obj).css("border", "1px solid red");		

}

 function viewRegisterBpa(registrationId){
		document.location.href="${pageContext.request.contextPath}/register/registerBpa!viewForm.action?registrationId="+registrationId;		
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
dom.get("fee_error").style.display = 'none';
jQuery("[id=numbers]").each(function(index) {
		jQuery(this).find('input').css("border", "");
		});
		
}

  function loadDesignationFromMatrix()
  {
        var currentState=document.getElementById('currentState').value;        
        var amountRule=document.getElementById('amountRule').value;
        var additionalRule=document.getElementById('additionalRule').value;
        var pendingAction=document.getElementById('pendingActions').value;
        var dept="";
        loadDesignationByDeptAndType('RegistrationFee',dept,currentState,amountRule,additionalRule,pendingAction); 
  }

  function populateApprover()
	{
		getUsersByDesignationAndDept();
	}
</script>
</head>
<body onload="refreshInbox();">

<div class="errorstyle" id="fee_error" style="display:none;" >
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

<s:form action="revisedFee" theme="simple" onkeypress="return disableEnterKey(event);" >
<s:token/>
<s:push value="model">
<s:hidden id="registrationId" name="registrationId" value="%{registrationId}"/>
<s:hidden id="mode" name="mode" value="%{mode}"/>
<s:hidden id="fromreg" name="fromreg" value="%{fromreg}"/>
<s:hidden id="id" name="id" value="%{id}"/>
<s:hidden id="createdBy" name="createdBy" value="%{createdBy.id}"/>
<s:hidden id="modifiedBy" name="modifiedBy" value="%{modifiedBy.id}"/>
<s:hidden id="createdDate" name="createdDate" value="%{createdDate}"/>
<s:hidden id="modifiedDate" name="modifiedDate" value="%{modifiedDate}"/>
<s:hidden id="state" name="state" value="%{state.id}" />
<s:hidden id="egwStatus" name="egwStatus" value="%{egwStatus.id}"/>
<s:hidden id="registration" name="registration" value="%{registration.id}"/>
<s:hidden id="isRevised" name="isRevised" value="%{isRevised}"/>
<div align="center"> 
 <div id="regfeeetails1" class="formmainbox">
 
 <div align="center" id="regdetails">
 <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
        
	    	<tr style="padding-top: 5%">
	 			<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="13%"><s:text name="Revised Fee Date"/></td> 
	   			<td class="bluebox"> <sj:datepicker value="%{feeDate}" id="feeDate" name="feeDate" displayFormat="dd/mm/yy"  showOn="focus"/></td>   			
				<td class="bluebox" ><s:text name="Plan Submission Number"/></td>
				<td class="bluebox" > <a href="#" onclick="viewRegisterBpa('<s:property value="%{registration.id}"/>')" id="reloadFeeLink" button="true"  buttonIcon="ui-icon-newwin"><s:property value="%{registration.planSubmissionNum}"/></a></td>
	       
          </tr>
         
          <tr>
	 			
			   <td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="13%"><s:text name="Challan Number"/></td> 
	   			<td class="bluebox"> <s:textfield value="%{challanNumber}" id="challanNumber" name="challanNumber"  disabled="true" /></td>   						
				<td class="bluebox" >&nbsp;</td>
				<td class="bluebox" >&nbsp;</td>
	       
          </tr>
  </table>        
 </div>
 

 

	<div align="center" id="feelistdiv">
	<h1 class="subhead" ><s:text name="Revised Fee Details"/></h1>
	<s:if test="%{COCRegFeeList.size!=0}">
	
	  <h1 class="subhead" ><s:text name="COC Fees"/></h1>
		   <table id="feelists" width="80%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
	     
	       <tr>
			<th  class="bluebgheadtd" width="3%"><div align="center">Sl No</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">Description</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">Amount</div></th>
		    </tr>
		    
		  <s:iterator value="COCRegFeeList" status="row_status">
		    <tr>
		  	<td  class="blueborderfortd"><div align="center"><s:text name="#row_status.count" /></div></td>
		  	<script>
		  	
		  
		  	
		  	</script>
		    <td class="blueborderfortd"><div align="center"><s:property value="%{bpaFee.feeDescription}"/>
		    <s:if test="{COCRegFeeList[%{#row_status.index}].isMandatory=='true'}"><span class="mandatory">*</span></s:if></div>
		    <s:hidden  value="COCRegFeeList[%{#row_status.index}].bpaFee.isMandatory"/></td>
		    
		    
		    <td class="blueborderfortd" id="mandatorycheck"><s:hidden name="COCRegFeeList[%{#row_status.index}].bpaFee.isMandatory"/><div align="center" id="numbers">
		    <s:textfield id="cocfeeamount" name="COCRegFeeList[%{#row_status.index}].amount"  onblur="calculateTotal()"/></div></td>		  			  		    
		    <s:hidden  name="COCRegFeeList[%{#row_status.index}].id"/>
		     <s:hidden  name="COCRegFeeList[%{#row_status.index}].bpaFee.id"/>	    
		      <s:hidden  name="COCRegFeeList[%{#row_status.index}].bpaFee.feeDescription"/>	   
		      <s:hidden  name="COCRegFeeList[%{#row_status.index}].bpaFee.feeGroup"/>	 
		    </tr>
		    </s:iterator>
		     <tr>
			  <td class="blueborderfortd"><div align="center"></div></td>				
			  <td class="blueborderfortd"><div align="center"><s:text name="Total" /></div></td>	
			   <td class="blueborderfortd"><div align="center"><s:textfield id="coctotal" name="total" value="" disabled="true"/></div></td>	
		    </tr>
		   
		      <s:if test="%{CMDARegFeeList.size==0 && MWGWFRegFeeList.size==0}">
		       <tr>
			  <td class="blueborderfortd"><div align="center"></div></td>				
			  <td class="blueborderfortd"><div align="center"><b><s:text name="Grand Total" /></b></div></td>	
			   <td class="blueborderfortd"><div align="center"><s:textfield id="grandtotal" name="total" value="" disabled="true"/></div></td>	
		    </tr>
		      </s:if>
		   
		    
	  </table>	
	
	  </s:if>
	  
	  <s:if test="%{CMDARegFeeList.size!=0}">
	
	   <h1 class="subhead" ><s:text name="CMDA Fees"/></h1>
		   <table id="feelists" width="80%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
	     
	       <tr>
			<th  class="bluebgheadtd" width="3%"><div align="center">Sl No</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">Description</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">Amount</div></th>
		    </tr>
		    
		  <s:iterator value="CMDARegFeeList" status="row_status">
		    <tr>
		  	<td  class="blueborderfortd"><div align="center"><s:text name="#row_status.count" /></div></td>
		  	<script>
		  	
		  
		  	
		  	</script>
		    <td class="blueborderfortd"><div align="center"><s:property value="%{bpaFee.feeDescription}"/>
		    <s:if test="{CMDARegFeeList[%{#row_status.index}].isMandatory=='true'}"><span class="mandatory">*</span></s:if></div>
		    <s:hidden  value="CMDARegFeeList[%{#row_status.index}].bpaFee.isMandatory"/></td>
		    
		    
		    <td class="blueborderfortd" id="mandatorycheck"><s:hidden name="CMDARegFeeList[%{#row_status.index}].bpaFee.isMandatory"/><div align="center" id="numbers">
		    <s:textfield id="cmdafeeamount" name="CMDARegFeeList[%{#row_status.index}].amount"  onblur="calculateTotal()"/></div></td>		  			  		    
		    <s:hidden  name="CMDARegFeeList[%{#row_status.index}].id"/>
		     <s:hidden  name="CMDARegFeeList[%{#row_status.index}].bpaFee.id"/>	    
		      <s:hidden  name="CMDARegFeeList[%{#row_status.index}].bpaFee.feeDescription"/>	 
		       <s:hidden  name="CMDARegFeeList[%{#row_status.index}].bpaFee.feeGroup"/>	    
		    </tr>
		    </s:iterator>
		     <tr>
			  <td class="blueborderfortd"><div align="center"></div></td>				
			  <td class="blueborderfortd"><div align="center"><s:text name="Total" /></div></td>	
			   <td class="blueborderfortd"><div align="center"><s:textfield id="cmdatotal" name="total" value="" disabled="true"/></div></td>	
		    </tr>
		   
		     <s:if test="%{MWGWFRegFeeList.size==0}">
		       <tr>
			  <td class="blueborderfortd"><div align="center"></div></td>				
			  <td class="blueborderfortd"><div align="center"><b><s:text name="Grand Total" /></b></div></td>	
			   <td class="blueborderfortd"><div align="center"><s:textfield id="grandtotal" name="total" value="" disabled="true"/></div></td>	
		    </tr>
		      </s:if>
		   
		    
	  </table>	
	
	  </s:if>
	  
	  
	  <s:if test="%{MWGWFRegFeeList.size!=0}">
	<h1 class="subhead" ><s:text name="M.W.G.W.F Fees"/></h1>
	  
	  
		   <table id="feelists" width="80%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
	     
	       <tr>
			<th  class="bluebgheadtd" width="3%"><div align="center">Sl No</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">Description</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">Amount</div></th>
		    </tr>
		    
		  <s:iterator value="MWGWFRegFeeList" status="row_status">
		    <tr>
		  	<td  class="blueborderfortd"><div align="center"><s:text name="#row_status.count" /></div></td>
		  	<script>
		  	
		  
		  	
		  	</script>
		    <td class="blueborderfortd"><div align="center"><s:property value="%{bpaFee.feeDescription}"/>
		    <s:if test="{MWGWFRegFeeList[%{#row_status.index}].isMandatory=='true'}"><span class="mandatory">*</span></s:if></div>
		    <s:hidden  value="MWGWFRegFeeList[%{#row_status.index}].bpaFee.isMandatory"/></td>
		    
		    
		    <td class="blueborderfortd" id="mandatorycheck"><s:hidden name="MWGWFRegFeeList[%{#row_status.index}].bpaFee.isMandatory"/><div align="center" id="numbers">
		    <s:textfield id="mwgwffeeamount" name="MWGWFRegFeeList[%{#row_status.index}].amount"  onblur="calculateTotal()"/></div></td>		  			  		    
		    <s:hidden  name="MWGWFRegFeeList[%{#row_status.index}].id"/>
		     <s:hidden  name="MWGWFRegFeeList[%{#row_status.index}].bpaFee.id"/>	    
		      <s:hidden  name="MWGWFRegFeeList[%{#row_status.index}].bpaFee.feeDescription"/>	    
		       <s:hidden  name="MWGWFRegFeeList[%{#row_status.index}].bpaFee.feeGroup"/>	
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
	 <div id="measuredetailsreg" align="center"> 

 <s:if test="%{existingFeeDetails.size!=0}">
  <h1 class="subhead" ><s:text name="Existing Fee Details"/></h1>
	
	<sj:dialog 
    	id="measure" 
    	autoOpen="false" 
    	modal="true" 
    	title="Fee Details"
    	openTopics="openRemoteMeasurementDialog"
    	height="500"
    	width="700"
    	dialogClass="formmainbox"
    	showEffect="slide" 
    	hideEffect="slide" 
    	onOpenTopics="openTopicDialog" cssStyle="BACKGROUND-COLOR: #ffffff"
    	onCompleteTopics="dialogopentopic" 
    	loadingText="Please Wait ...."
    	errorText="Permission Denied"
    />

  <div id="header" class="formmainbox" align="center">
	    <table id="postponetable" width="80%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
	     
	      <tr>
			<th  class="bluebgheadtd" width="3%"><div align="center">Sl No</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">Date</div></th>	
		     <th  class="bluebgheadtd" width="8%"><div align="center">Challan Number</div></th>	
		     <th  class="bluebgheadtd" width="8%"><div align="center">Status</div></th>		   
		    <th  class="bluebgheadtd" width="8%"><div align="center"> Fee Details</div></th>
		  </tr>
		  <s:iterator value="existingFeeDetails" status="row_status">
		    <tr>
		  	<td  class="blueborderfortd"> <div align="center"><s:text name="#row_status.count" /></div></td>
		    <td  class="blueborderfortd"><div align="center"><s:property value="%{formattedFeeDate}" /></div></td>
		     <td  class="blueborderfortd"><div align="center"><s:property value="%{challanNumber}" /></div></td>
		     <td  class="blueborderfortd"><div align="center"><s:property value="%{egwStatus.code}" /></div></td>
		    <s:if test="%{legacyFee!='true'}">
		   <s:url id="measurementlink" value="/fee/revisedFee!showFeeDetails.action" escapeAmp="false">
		      <s:param name="registrationFeeId" value="%{id}"></s:param>	 
		       <s:param name="registrationId" value="%{registration.id}"></s:param>	
		      
		   </s:url> 
		   </s:if>
		   <s:else>
		     <s:url id="measurementlink" value="/fee/feeDetails!showFeeDetailsinRevisedFee.action" escapeAmp="false">
		      <s:param name="registrationFeeId" value="%{id}"></s:param>	 
		       <s:param name="registrationId" value="%{registration.id}"></s:param>	
		      
		   </s:url> 
		   
		   </s:else>
		  	<td  class="blueborderfortd"><div align="center"> <sj:a  onClickTopics="openRemoteMeasurementDialog" href="%{measurementlink}" button="true" buttonIcon="ui-icon-newwin">View</sj:a></div></td>		  
		      
		    </tr>
		    </s:iterator>
	  </table>
	 </div>
	 
</s:if>
</div>
    <s:if test="%{mode!='view'}"> 
		<div id="approverInfo">
	        <c:set var="approverHeadTDCSS" value="headingwk" scope="request" /> 
	        <c:set var="approverHeaderCss" value="headplacerlbl" scope="request"/>
	        <c:set var="headerImgCss" value="arrowiconwk" scope="request"/>
	        <c:set var="headerImgUrl" value="../common/image/arrow.gif" scope="request"/>
	        <c:set var="approverOddCSS" value="whiteboxwk" scope="request" />
	        <c:set var="approverOddTextCss" value="whitebox2wk" scope="request" />
	        <c:set var="approverEvenCSS" value="greyboxwk" scope="request" />
	        <c:set var="approverEvenTextCSS" value="greybox2wk" scope="request" />
	        
			<c:import url="/commons/commonWorkflow.jsp" context="/bpa" />
	</div>
	</s:if>	
	<div class="buttonbottom" align="center">
		<table>	
		
		<s:if test="%{canProceed!=false }">
		<s:if test="%{mode!='view' }">
	  			<td><s:submit  cssClass="buttonsubmit" id="save" name="save" value="Save"  method="save" onclick="return validateForm('save');" /></td> 
	  	<s:iterator value="%{getValidActions()}" var="p">
		<td><s:submit type="submit" cssClass="buttonsubmit" value="%{p}" id="%{p}" name="%{p}" method="save" onclick="return validateForm('%{p}')" /></td>
		</s:iterator>
	  		</s:if>	
	  	</s:if>
	  	<td><input type="button" name="close" id="close" class="button"  value="Close" onclick="window.close();"/></td>  		
	  	</table>	    			    	
	</div>  
</div>
	








</s:push>	
</s:form>
</body>
</html>



