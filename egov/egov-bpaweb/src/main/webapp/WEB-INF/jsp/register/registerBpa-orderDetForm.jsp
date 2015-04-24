<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  
  <sj:head jqueryui="true" jquerytheme="redmond" loadAtOnce="true" />
  <script type="text/javascript">
  OnLoadOrdDet();
  
  	function validateOrdDet(action) {
  		var todaysDate=getTodayDate();
  		var applicationDate=document.getElementById('applicationDate').value;
  		var validateDate=document.getElementById('validatedate').value;
  			

  		if(action == "Add Challan Sent Date") {
  	  		var challanSentDate=document.getElementById("challanSentDate").value;
	  	  	if(document.getElementById("challanSentDate").value==""){
				   alert("Please select the Date when Challan Notice is Sent to Citizen");
				   return false;
			}
	  	  	if(compareDate(applicationDate,challanSentDate) == -1 )
			{
			   	alert("Challan Sent Date should be greater than Application Date "+applicationDate);
			   	return false;
			}
	  	  	if(compareDate(challanSentDate,todaysDate) == -1)
			{						  	 	
	 			alert("Challan Sent Date should not be greater than Todays Date ");
	 			return false;
			}
  	  	}
  	  	if(action == "Update Signature") {
  	  	 
	  	  	if(document.getElementById("signDetailsDate").value==""){
				   alert("Please select the Applicant Signature Date");
				   return false;
			}

	  	  if((validateDate!=null && validateDate!="" && validateDate!=undefined ) && compareDate(validateDate,document.getElementById("signDetailsDate").value) == -1 )
			{
			   	alert("Signature Date should be greater than challan notice sent date "+validateDate);
			   	return false;
			} 	

	  	  	if(compareDate(applicationDate,document.getElementById("signDetailsDate").value) == -1 )
			{
			   	alert("Signature Date should be greater than Application Date "+applicationDate);
			   	return false;
			}

	  	  	if(compareDate(document.getElementById("signDetailsDate").value,todaysDate) == -1)
			{						  	 	
	 			alert("Signature Date should not be greater than Todays Date ");
	 			return false;
			}

  	  }

	  	if(action == "Order Prepared") {
	  	  	if(document.getElementById("orderDetailsDate").value==""){
				   alert("Please select the Order Preparation Date");
				   return false;
			}
	  	  if(compareDate(applicationDate,document.getElementById("orderDetailsDate").value) == -1 )
			{
			   	alert("Order Details Date should be greater than Application Date "+applicationDate);
			   	return false;
			}

	  	if((validateDate!=null && validateDate!="" && validateDate!=undefined ) && compareDate(validateDate,document.getElementById("orderDetailsDate").value) == -1 )
		{
		   	alert("Order Details Date should greater than Applicant Signature updated date "+validateDate);
		   	return false;
		} 	
		
	  	  	if(compareDate(document.getElementById("orderDetailsDate").value,todaysDate) == -1)
			{						  	 	
	 			alert("Order Details Date should not be greater than Todays Date ");
	 			return false;
			}
		} 

	  	if(action == "Order Issued") {
			//alert("document.getElementById(orderDetailsDate)==="+document.getElementById("orderDetailsDate"));
		  //	alert("document.getElementById(orderIssueDetDate)==="+document.getElementById("orderIssueDetDate"));
	  	  	if(document.getElementById("orderIssueDetDate").value==""){
				   alert("Please select the Order Issue Date");
				   return false;
				   }
	  	 // 	var ordCheckDate=checkFdateTdate(document.getElementById("orderDetailsDate").value,document.getElementById("orderIssueDetDate").value);
	  	  	//alert(ordCheckDate);
		/*	if(ordCheckDate==false)
			{
				alert('Order Issue Date should be greater than or equal to Order Preparation Date');
				document.getElementById("orderIssueDetDate").focus();
				return false;
			} */
			
			if((validateDate!=null && validateDate!="" && validateDate!=undefined ) && compareDate(validateDate,document.getElementById("orderIssueDetDate").value) == -1 )
			{
			   	alert("Order Issue Date should be greater than or equal to Order Preparation Date "+validateDate);
			   	return false; 
			} 	
			
			if(compareDate(applicationDate,document.getElementById("orderIssueDetDate").value) == -1 )
			{
			   	alert("Order Issue Date should be greater than Application Date "+applicationDate);
			   	return false;
			}
	  	  	if(compareDate(document.getElementById("orderIssueDetDate").value,todaysDate) == -1)
			{						  	 	
	 			alert("Order Issue Date should not be greater than Todays Date ");
	 			return false;
			}
		}

  		if(action == "Unconsidered Order Prepared") {
  			if(document.getElementById("rejectOrdPrepDetDate").value==""){
				   alert("Please select the Unconsideration Order Preparation Date");
				   return false;
			}
  			if(compareDate(applicationDate,document.getElementById("rejectOrdPrepDetDate").value) == -1 )
			{
			   	alert("Unconsideration order prepared Date should be greater than Application Date "+applicationDate);
			   	return false;
			}
	  	  	if(compareDate(document.getElementById("rejectOrdPrepDetDate").value,todaysDate) == -1)
			{						  	 	
	 			alert("Unconsideration order prepared Date should not be greater than Todays Date ");
	 			return false;
			}
  	  	}

  		if(action == "Unconsidered Order Issued") {
  			if(document.getElementById("rejectOrdIssDetDate").value==""){
				   alert("Please select the Unconsideration Order Issue Date");
				   return false;
			}
  			if(compareDate(applicationDate,document.getElementById("rejectOrdIssDetDate").value) == -1 )
			{
			   	alert("Unconsideration order Issued Date should be greater than Application Date "+applicationDate);
			   	return false;
			}
	  	  	if(compareDate(document.getElementById("rejectOrdIssDetDate").value,todaysDate) == -1)
			{						  	 	
	 			alert("Unconsideration order Issued Date should not be greater than Todays Date ");
	 			return false;
			}
  	  	}
  		
  		//closeParentWindow();
  		return true;
  	  	}

  	function closeParentWindow() {
		window.opener.close();
		return true;
  	  	}

  	function OnLoadOrdDet() {
  		var mode=document.getElementById("mode").value;
  		var signDate = document.getElementById("signDetailsDate").value;
  	  	var ordPrepDate = document.getElementById("orderDetailsDate").value;
  	  	var ordIssDate = document.getElementById("orderIssueDetDate").value;
  	  	var rejectOrdPrepDate = document.getElementById("rejectOrdPrepDetDate").value;
  	  	var rejectOrdIssDate = document.getElementById("rejectOrdIssDetDate").value;
  	  	var action = document.getElementById("reqdAction").value;
  	   // alert("document.getElementById(validateDate)==="+document.getElementById("validatedate").value);
  		if(mode=='edit') 
  	  	{
  			if(action == "Add Challan Sent Date") {
  				document.getElementById("challanDet").style.display='';
  				document.getElementById("signDet").style.display='none';
  				document.getElementById("ordPrepDet").style.display='none';
  				document.getElementById("ordIssDet").style.display='none';
  				document.getElementById("rejOrdDet").style.display='none';
  				document.getElementById("rejIssDet").style.display='none';
  	  	  	  	}
	  	  	  	
  			if(action == "Update Signature") {
  				document.getElementById("challanDet").style.display='none';
  				document.getElementById("signDet").style.display='';
  				document.getElementById("ordPrepDet").style.display='none';
  				document.getElementById("ordIssDet").style.display='none';
  				document.getElementById("rejOrdDet").style.display='none';
  				document.getElementById("rejIssDet").style.display='none';
  	  	  	  	}

  		  	if(action == "Order Prepared") {
  		  		document.getElementById("challanDet").style.display='none';
  				document.getElementById("signDet").style.display='none';				
  				document.getElementById("ordPrepDet").style.display='';
  				document.getElementById("ordIssDet").style.display='none';
  				document.getElementById("rejOrdDet").style.display='none';
  				document.getElementById("rejIssDet").style.display='none';
  			  	} 

  		  	if(action == "Order Issued") {
  				document.getElementById("challanDet").style.display='none';
  				document.getElementById("signDet").style.display='none';
  				document.getElementById("ordPrepDet").style.display='none';
  				document.getElementById("ordIssDet").style.display='';
  				document.getElementById("rejOrdDet").style.display='none';
  				document.getElementById("rejIssDet").style.display='none';
  			  	}

  	  		if(action == "Unconsidered Order Prepared") {
  				document.getElementById("challanDet").style.display='none';
  				document.getElementById("signDet").style.display='none';
  				document.getElementById("ordPrepDet").style.display='none';
  				document.getElementById("ordIssDet").style.display='none';
  				document.getElementById("rejOrdDet").style.display='';
  				document.getElementById("rejIssDet").style.display='none';
  	  	  		}

  	  		if(action == "Unconsidered Order Issued") {
  				document.getElementById("challanDet").style.display='none';
  				document.getElementById("signDet").style.display='none';
  				document.getElementById("ordPrepDet").style.display='none';
  				document.getElementById("ordIssDet").style.display='none';
  				document.getElementById("rejOrdDet").style.display='none';
  				document.getElementById("rejIssDet").style.display='';
  	  	  		}
  	  		 }
	  		 return true;
  	  	}
  </script>
  <body onload="OnLoadOrdDet();">
  <div align="left">
  	<s:actionerror/>
  </div>
	<div class="errorstyle" id="bpa_error_area" style="display: none;"></div>
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

<s:form name="RegisterBpaForm" action="registerBpa" theme="simple" >

  <div class="formmainbox">
  			
  			<s:hidden id="registrationId" name="registrationId" value="%{registrationId}"/>
     		<s:hidden  id="signDetails"  name="signDetails" value="%{signDetails.id}" />	
     		<s:hidden  id="orderDetails"  name="orderDetails" value="%{orderDetails.id}" />	
     		<s:hidden  id="orderIssueDet"  name="orderIssueDet" value="%{orderIssueDet.id}" />	
     		<s:hidden  id="rejectOrdPrepDet"  name="rejectOrdPrepDet" value="%{rejectOrdPrepDet.id}" />	
     		<s:hidden  id="rejectOrdIssDet"  name="rejectOrdIssDet" value="%{rejectOrdIssDet.id}" />	
     		<s:hidden  id="challanDetails"  name="challanDetails" value="%{challanDetails.id}" />	
     		<s:hidden  id="reqdAction"  name="reqdAction" value="%{reqdAction}" />
     		<s:hidden  id="mode"  name="mode" value="%{mode}" />	 
   			<s:date name="applicationdate" var="cdFormat" format="dd/MM/yyyy"/>
   			<s:hidden id="applicationDate" name="applicationDate" value="%{cdFormat}" />
   			
   			<s:date name="validateDate" var="validdateFormat" format="dd/MM/yyyy"/>
   			<s:hidden id="validatedate" name="validatedate" value="%{validdateFormat}" />
   			
   			 
 <div align="center" id="orddetdiv">
 	<table width="100%" border="0" cellspacing="0" cellpadding="0" id="orderTbl">
 		<tr>
 			<td>
 			<table width="100%" border="0" cellspacing="0" cellpadding="0" id="challanDet" >
					<tr>
						<td colspan="6"><div class="headingbg"><span class="bold"><s:text name="ChallanNoticeSentDateDtl"/></span></div></td>
					</tr>
					<tr>
						<td class="bluebox" width="13%">&nbsp;</td>
						<td class="bluebox" width="13%"><s:text name="ChallanSentDate" /> :</td>
						<td class="bluebox">
							<sj:datepicker id="challanSentDate" name="challanDetails.statusdate" displayFormat="dd/mm/yy" 
								showOn="focus" value="%{challanDetails.statusdate}"/>
						</td>
						<td class="bluebox"><s:text name="ChallanSentDateRemarks" /> :</td>
						<td class="bluebox">
							<s:textarea id="challanDetailsremarks" name="challanDetails.remarks" cols="20" rows="2" 
								value="%{challanDetails.remarks}"/>
						</td>
						<td class="bluebox">&nbsp;</td>
					</tr> 
				</table>
 				<table width="100%" border="0" cellspacing="0" cellpadding="0" id="signDet" >
					<tr>
						<td colspan="6"><div class="headingbg"><span class="bold"><s:text name="signatureDtl"/></span></div></td>
					</tr>
					<tr>
						<td class="bluebox" width="13%">&nbsp;</td>
						<td class="bluebox" width="13%"><s:text name="signatureDate" /> :</td>
						<td class="bluebox">
							<sj:datepicker id="signDetailsDate" name="signDetails.statusdate" displayFormat="dd/mm/yy" 
								showOn="focus" value="%{signDetails.statusdate}"/>
						</td>
						<td class="bluebox"><s:text name="signatureRemarks" /> :</td>
						<td class="bluebox">
							<s:textarea id="signDetailsremarks" name="signDetails.remarks" cols="20" rows="2" 
								value="%{signDetails.remarks}"/>
						</td>
						<td class="bluebox">&nbsp;</td>
					</tr> 
				</table>
				<table width="100%" border="0" cellspacing="0" cellpadding="0" id="ordPrepDet">  
					<tr>
						<td colspan="6"><div class="headingbg"><span class="bold"><s:text name="ordPrepDtl"/></span></div></td>
					</tr>
					<tr>
						<td class="bluebox" width="13%">&nbsp;</td>
						<td class="bluebox" width="13%"><s:text name="orderDate" /> :</td>
						<td class="bluebox">
							<sj:datepicker value="%{orderDetails.statusdate}" id="orderDetailsDate" name="orderDetails.statusdate" 
								displayFormat="dd/mm/yy" showOn="focus"/>
						</td>
						<td class="bluebox"><s:text name="orderRemarks" /> :</td>
						<td class="bluebox">
							<s:textarea id="orderDetailsremarks" name="orderDetails.remarks" value="%{orderDetails.remarks}" 
								cols="20" rows="2"/>
						</td>
						<td class="bluebox">&nbsp;</td>
					</tr>
				</table>
				<table width="100%" border="0" cellspacing="0" cellpadding="0" id="ordIssDet">   
					<tr>
						<td colspan="6"><div class="headingbg"><span class="bold"><s:text name="ordIssueDtl"/></span></div></td>
					</tr>
					<tr>
						<td class="bluebox" width="13%">&nbsp;</td>
						<td class="bluebox" width="13%"><s:text name="orderIssDate" /> :</td>
						<td class="bluebox">
							<sj:datepicker value="%{orderIssueDet.statusdate}" id="orderIssueDetDate" name="orderIssueDet.statusdate" displayFormat="dd/mm/yy" showOn="focus"/>
						</td>
						<td class="bluebox"><s:text name="orderIssRemarks" /> :</td>
						<td class="bluebox">
							<s:textarea id="orderIssueDet.remarks" name="orderIssueDet.remarks" value="%{orderIssueDet.remarks}" cols="20" rows="2"/>
						</td>
						<td class="bluebox">&nbsp;</td>
					</tr>
				</table>
				<table width="100%" border="0" cellspacing="0" cellpadding="0" id="rejOrdDet">
					<tr>
						<td colspan="6"><div class="headingbg"><span class="bold"><s:text name="rejectOrdDetails"/></span></div></td>
					</tr>
					<tr>
						<td class="bluebox" width="13%">&nbsp;</td>
						<td class="bluebox" width="13%"><s:text name="rejectPrepDate" /> :</td>
						<td class="bluebox">
							<sj:datepicker value="%{rejectOrdPrepDet.statusdate}" id="rejectOrdPrepDetDate" name="rejectOrdPrepDet.statusdate" displayFormat="dd/mm/yy" showOn="focus"/>
						</td>
						<td class="bluebox"><s:text name="rejectPrepRemarks" /> :</td>
						<td class="bluebox">
							<s:textarea id="rejectOrdPrepDet.remarks" name="rejectOrdPrepDet.remarks" value="%{rejectOrdPrepDet.remarks}" cols="20" rows="2"/>
						</td>
						<td class="bluebox">&nbsp;</td>
					</tr>
				</table>
				<table width="100%" border="0" cellspacing="0" cellpadding="0" id="rejIssDet">   
					<tr>
						<td colspan="6"><div class="headingbg"><span class="bold"><s:text name="rejectOrdIssDet"/></span></div></td>
					</tr>
					<tr>
						<td class="bluebox" width="13%">&nbsp;</td>
						<td class="bluebox" width="13%"><s:text name="rejectIssDate" /> :</td>
						<td class="bluebox">
							<sj:datepicker value="%{rejectOrdIssDet.statusdate}" id="rejectOrdIssDetDate" name="rejectOrdIssDet.statusdate" displayFormat="dd/mm/yy" showOn="focus"/>
						</td>
						<td class="bluebox"><s:text name="rejectIssRemarks" /> :</td>
						<td class="bluebox">
							<s:textarea id="rejectOrdIssDet.remarks" name="rejectOrdIssDet.remarks" value="%{rejectOrdIssDet.remarks}" cols="20" rows="2"/>
						</td>
						<td class="bluebox">&nbsp;</td>
					</tr>
				</table>
			</td></tr>   
		</table>
 </div>
 <div class="buttonbottom" align="center">
		<table>
			<s:if test="%{mode!='view'}">
		  		<td>
		  			<s:submit type="submit" cssClass="buttonsubmit" value="%{reqdAction}" 
		  				id="%{reqdAction}" name="%{reqdAction}" method="updateOrders" 
		  				onclick="return validateOrdDet('%{reqdAction}');"/>
		  		</td>
	  		</s:if>
	  	</table>
  </div>
  </div>

  </s:form>
 </body>
 </html>
 
