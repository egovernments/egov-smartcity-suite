<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@	taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>


<html>

<head>
	<title>Search Application</title>
	<sj:head jqueryui="true" jquerytheme="redmond" loadAtOnce="true" />

<script type="text/javascript">

jQuery.noConflict();
jQuery(document).ready(function() {
jQuery('.commontopbluebg').children().remove();
});

function validateEmail()
{	
	dom.get("searchRecords_error").style.display='none';
	var x=document.getElementById("email").value;
	if(x!=""){
		var atpos=x.indexOf("@");
		var dotpos=x.lastIndexOf(".");
		if (atpos<1 || dotpos<atpos+2 || dotpos+2>=x.length)
		{
			dom.get("searchRecords_error").style.display = '';
			document.getElementById("searchRecords_error").innerHTML = '<s:text name="invalid.email.address" />';
			document.getElementById("email").value="";
		  return false;
		}
	}
	return true;
}

function validateMobileNumber()
{

	var text = document.getElementById('phoneNo').value;
	
	if(text!=''){
		
		if(text.length!=10)
		{	
			document.getElementById("phoneNo").value="";
			dom.get("searchRecords_error").style.display = '';
			document.getElementById("searchRecords_error").innerHTML = '<s:text name="invalid.mobileno.length" />';
			return false;
	
		}
	validatePhoneNumber();
	}
	return true;
}

function validatePhoneNumber(){
	var text =document.getElementById('phoneNo').value;
	if(text!=""){
		
	var msg;
	msg='<s:text name="invalid.mobileno" />';
	if(isNaN(text))
	{
		dom.get("searchRecords_error").style.display = '';
		document.getElementById("searchRecords_error").innerHTML = msg;
		document.getElementById("email").value="";
		return false;
	}
	if(text<=0)
	{
		dom.get("searchRecords_error").style.display = '';
		document.getElementById("searchRecords_error").innerHTML = msg;
		document.getElementById("email").value="";
		return false;
	}
	if(text.replace(".","~").search("~")!=-1)
	{
		dom.get("searchRecords_error").style.display = '';
		document.getElementById("searchRecords_error").innerHTML = '<s:text name="period.notallowed" />';
		document.getElementById("email").value='';
		return false;
	}
	if(text.replace("+","~").search("~")!=-1)
	{
		dom.get("searchRecords_error").style.display = '';
		document.getElementById("searchRecords_error").innerHTML = '<s:text name="plus.notallowed" />';
		document.getElementById("email").value='';
		return false;
	}
	}
	return true;
}


function gotoPage(obj){

	var currRow=getRow(obj); 
	    dom.get("searchRecords_error").style.display = '';
		document.getElementById("searchRecords_error").innerHTML = 'Please Wait....This will take a while..';
	 	var objectParam;
	 	var value=obj.value
		objectParam = getControlInBranch(currRow,'regId');
 			if(value=="View Application"){
 			   
				document.location.href="${pageContext.request.contextPath}/citizen/citizenSearch!viewApplication.action?registrationId="+objectParam.value;
					
			}
			
			else if(value=="View Letter To Party"){
			   
				document.location.href="${pageContext.request.contextPath}/citizen/citizenSearch!showLettertoParty.action?registrationId="+objectParam.value;
					
			}
			
			else if(value=="View InspectionSchedule"){
			
				document.location.href="${pageContext.request.contextPath}/citizen/citizenSearch!showInspectionSchedule.action?registrationId="+objectParam.value;
					
			}else if(value=="View Fees Receipt"){
				
				document.location.href="${pageContext.request.contextPath}/citizen/citizenSearch!showCollectedFeeReceipts.action?registrationId="+objectParam.value;
			
			}else if(value=="Print Challan"){
					
				  document.location.href="${pageContext.request.contextPath}/citizen/citizenSearch!feePaymentPdf.action?registrationId="+objectParam.value;
				
			}
}
function  viewAutoDcrPlan(path){
	 document.location.href=path;
	 }

function gotoNewPage(obj){

	var currRow=getRow(obj); 
	    dom.get("searchRecords_error").style.display = '';
		document.getElementById("searchRecords_error").innerHTML = 'Please Wait....This will take a while..';
	 	var objectParam;
	 	var value=obj.value
		objectParam = getControlInBranch(currRow,'regId');
 			if(value=="View Application"){
 			   
				document.location.href="${pageContext.request.contextPath}/citizen/citizenSearch!viewNewApplication.action?registrationId="+objectParam.value;
					
			}
 			else if(value=="View Plan"){
 				document.location.href="${pageContext.request.contextPath}/citizen/citizenSearch!showNewCollectedFeeReceipts.action?registrationId="+objectParam.value;
 				
 			}
			/*else if(value=="View Letter To Party"){
			   
				document.location.href="${pageContext.request.contextPath}/citizen/citizenSearch!showNewLettertoParty.action?registrationId="+objectParam.value;
					
			}
			
			/*else if(value=="View InspectionSchedule"){
			
				document.location.href="${pageContext.request.contextPath}/citizen/citizenSearch!showNewInspectionSchedule.action?registrationId="+objectParam.value;
					
			}*/else if(value=="View Fees Receipt"){
				
				document.location.href="${pageContext.request.contextPath}/citizen/citizenSearch!showNewCollectedFeeReceipts.action?registrationId="+objectParam.value;
			
			}else if(value=="Print Challan"){
					
				  document.location.href="${pageContext.request.contextPath}/citizen/citizenSearch!feeNewPaymentPdf.action?registrationId="+objectParam.value;
				
			}
}


function resetValues(){

	document.getElementById('securityKey').value="" ; 
	document.getElementById('psnSearch').value="" ; 
	document.getElementById('autoDCRNo').value="" ; 
 	document.getElementById('phoneNo').value="" ; 
 	document.getElementById('email').value="" ; 
 	document.getElementById('cmdaNumSearch').value="" ;
 	document.getElementById("tableData").style.display='none';
}

function validate(){


	if(document.getElementById('securityKey').value==""){
		dom.get("searchRecords_error").style.display = '';
		document.getElementById("searchRecords_error").innerHTML = 'Security Key is mandatory';
		return false;
		}
	
if(document.getElementById('psnSearch').value=="" &&
	document.getElementById('autoDCRNo').value=="" &&
 	document.getElementById('phoneNo').value=="" &&
 	document.getElementById('email').value=="" &&
 	document.getElementById('cmdaNumSearch').value=="" ){
 	dom.get("searchRecords_error").style.display = '';
		document.getElementById("searchRecords_error").innerHTML = 'Select atleast one field';
		return false;
 	
 	}



if(!validateMobileNumber()){
return false;
}

if(!validateEmail()){
return false;
}
}

 
</script>

</head>

<body>

	<div class="errorstyle" id="searchRecords_error" style="display: none;"></div>
	<div class="errorstyle" style="display: none" ></div>
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />	
			<s:fielderror />
		</div>
	</s:if>
	<s:if test="%{hasActionMessages()}">
		<div class="errorstyle">
			<s:actionmessage />
		</div>
	</s:if>

	<s:form action="citizenSearch" theme="simple" name="searchForm">
		<div  align="center">
			<span  class="mandatory">*</span><b>Select atleast one field along with Security Key</b>
		</div>
		<table width="100%" border="0" cellspacing="0" cellpadding="2">
			
			<tr>
				<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" ><s:text name="planSubmissionNo.lbl"/></td>
				<td class="bluebox" width="25%"><s:textfield id="psnSearch" name="planSubmissionNum" value="%{planSubmissionNum}" /></td>
				<td class="bluebox" ><s:text name="mobileNo.lbl"/></td>
				<td class="bluebox" ><s:textfield id="phoneNo" name="phoneNo" value="%{phoneNo}" onblur="validateMobileNumber(this);" /></td>
				<td class="bluebox">&nbsp;</td>
			</tr>
			 
			<tr>
				<td class="greybox" width="20%">&nbsp;</td>				
				<td class="greybox" ><s:text name="emailid.lbl"/></td>
				<td class="greybox" ><s:textfield id="email" name="emailAddress" value="%{emailAddress}" onblur="validateEmail();" /></td>
				<td class="greybox"><s:text name="autoDCRNo.lbl"/> </td>
				<td class="greybox" ><s:textfield id="autoDCRNo" name="autoDCRNo" value="%{autoDCRNo}" /></td>
				<td class="greybox">&nbsp;</td>

			</tr>
			
				<tr>
				<td class="bluebox" width="20%">&nbsp;</td>	
				<td class="bluebox" ><s:text name="CMDAProposalNo.lbl"/></td>
				<td class="bluebox" ><s:textfield id="cmdaNumSearch" name="cmdaNum" value="%{cmdaNum}"/></td>
				<td class="bluebox"><s:text name="securityKey.lbl"/><span  class="mandatory">*</span></td>
				<td class="bluebox"><s:textfield id="securityKey" name="securityKey" value="%{securityKey}"/></td>
				<td  class="bluebox">
			  		
			  		<s:url id="keylink" value="/citizen/citizenSearch!sendSecurityKey.action" escapeAmp="false">	</s:url> 		
			  			<sj:a  onClickTopics="openSecKeyDialog" href="%{keylink}" button="true" buttonIcon="ui-icon-newwin">
			  				Forgot Security Key? Click Here
			  			</sj:a>
			  		
			  	</td>
			</tr>
			<sj:dialog 
			    	id="secKey" 
			    	autoOpen="false" 
			    	modal="true" 
			    	title="Send Security Key"
			    	openTopics="openSecKeyDialog"
			    	height="500"
			    	width="700"
			    	dialogClass="formmainbox"
			    	showEffect="slide" 
			    	hideEffect="slide" 
			    	onOpenTopics="openTopicDialog" cssStyle="BACKGROUND-COLOR: #ffffff"
			    	onCompleteTopics="dialogopentopic" 
			    	errorText="Permission Denied"
			    	loadingText="Please Wait...."
			    />
    
	
		</table>
		
		
			
		<div class="buttonbottom" align="center">
			<table>
				<tr>
					<td><s:submit cssClass="buttonsubmit" id="submit" name="submit" value="Search"  method="searchResult" onclick="return validate();" /></td>
			  		 <td><input type="button" id="reset" name="reset" class="button" value="Reset"  onclick="return resetValues();" /></td>
			  		 <td><input type="button" name="close" id="close" class="button" value="Close" onclick="window.close();"/></td>
			  	</tr>
	        </table>
	   </div>	   
	   
	   <div id="tableData">
	   <div class="infostyle" id="search_error" style="display:none;"></div> 
	    <s:if test="%{searchMode=='result'}">
	   <s:if test="%{oldregList.size!=0}">
	  
          		 <div id="displaytbl">	
          		     	 <display:table  name="oldregList" export="false" requestURI="" id="regListid"  class="its" uid="currentRowObject" >
          			 	 <div STYLE="display: table-header-group" align="center">
          			 	  
 						 	<display:column title=" Sl No" style="text-align:center;"  >
 						 	 		<s:property value="#attr.currentRowObject_rowNum"/>
 						 	</display:column>
						
							<display:column class="hidden" headerClass="hidden"  media="html">
 						 		<s:hidden id="regId" name="regId" value="%{#attr.currentRowObject.id}" />
							</display:column>
							
							<display:column title="Plan Submission Number " style="text-align:center;"  property="planSubmissionNum">	
 						 
 						 	</display:column>	
 						 	
 						 	<display:column  title="Plan Submission Date" style="width:6%;text-align:left" >
								<s:date name="#attr.currentRowObject.planSubmissionDate" format="dd/MM/yyyy" />
							</display:column>
 						 	
			
							<display:column title="Service Type" style="text-align:center;" property="serviceType.description" />								
						
							<display:column title="Applicant Name " style="text-align:center;" property="owner.firstName" />	 
							
							<display:column title="Applicant Address " style="text-align:center;" property="bpaOwnerAddress" />	 								
							
							<display:column title="Current Owner" style="text-align:center;" >
          			        	<s:property value="%{getUsertName(#attr.currentRowObject.state.owner.id)}" />
							</display:column>
							
							<display:column title="Status " style="text-align:center;" property="egwStatus.code" />	 
							
							 <display:column title="View AutoDcr Plan" style="text-align:center;" >
								<s:if test="%{autoDcrispresent}">
 						 	<a href="#" onclick="viewAutoDcrPlan('<s:property value="%{getOldAutoDcrPath(#attr.currentRowObject.id)}" />')">
 						 		View AutoDcr Plan
 						 	</a></s:if> 
 						 	</display:column>
								
							<display:column title="Actions"  style="width:10%;text-align:left">					
								<s:select theme="simple"
										list="#attr.currentRowObject.registerBpaSearchActions"
										name="actionDropdown" id="actionDropdown"
										headerValue="--- Select ---"
										headerKey="-1" onchange="gotoPage(this);">
								</s:select>						
							</display:column>	  
										 						 
						</div>
						</display:table>
					</div>
  	   			</s:if>
  	   			<s:else>
  	   			 <s:if test="%{newregList.size!=0}">
  	   			<div id="displaytbl">	
          		     	 <display:table  name="newregList" export="false" requestURI="" id="regListid"  class="its" uid="currentRowObject" >
          			 	 <div STYLE="display: table-header-group" align="center">
          			 	  
 						 	<display:column title=" Sl No" style="text-align:center;"  >
 						 	 		<s:property value="#attr.currentRowObject_rowNum "/>
 						 	</display:column>
						
							<display:column class="hidden" headerClass="hidden"  media="html">
 						 		<s:hidden id="regId" name="regId" value="%{#attr.currentRowObject.id}" />
							</display:column>
							
							<display:column title="Plan Submission Number " style="text-align:center;"  property="planSubmissionNum">	
 						 
 						 	</display:column>	
 						 	
 						 	<display:column  title="Plan Submission Date" style="width:6%;text-align:left" >
								<s:date name="#attr.currentRowObject.planSubmissionDate" format="dd/MM/yyyy" />
							</display:column>
 						 	
			
							<display:column title="Service Type" style="text-align:center;" property="serviceType.description" />								
						
							<display:column title="Applicant Name " style="text-align:center;" property="owner.firstName" />	 
							
							<display:column title="Applicant Address " style="text-align:center;" property="bpaOwnerAddress" />	 								
							
							<display:column title="Current Owner" style="text-align:center;" >
          			        	<s:property value="%{getUsertName(#attr.currentRowObject.state.owner.id)}" />
							</display:column>
							
							<display:column title="Status " style="text-align:center;" property="egwStatus.code" />	 
							
								<display:column title="View AutoDcr Plan" style="text-align:center;" >
							<s:if test="%{autoDcrispresent}">	
 						 	<a href="#" onclick="viewAutoDcrPlan('<s:property value="%{getAutoDcrPath(#attr.currentRowObject.id)}" />')">
 						 		View AutoDcr Plan
 						 	</a>
 						 	</s:if> 
 						 	</display:column>
								
							<display:column title="Actions"  style="width:10%;text-align:left">					
								<s:select theme="simple"
										list="#attr.currentRowObject.registerBpaSearchActions"
										name="actionDropdown" id="actionDropdown"
										headerValue="--- Select ---"
										headerKey="-1" onchange="gotoNewPage(this);">
								</s:select>						
							</display:column>	  
										 						 
						</div>
						</display:table>
					</div>
					</s:if>
					<s:else>
					<div id="show">
			<td>Nothing Found..</td>
			</div> 
					</s:else>
  	   			</s:else>
  	   			</s:if>
  	   			
  	     </div>
	
	   
	   <div>
	   
	   </div>
	  

	  </s:form>
	  </body>
</html>