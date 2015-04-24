<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@	taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>

<html>

<head>
	<title>Search Application</title>
	
	<sj:head jqueryui="true" jquerytheme="redmond" loadAtOnce="true"/>
	

<script type="text/javascript">

jQuery.noConflict();

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

function validateMobileNumber(obj)
{

	var text = obj.value;
	if(text!=''){
		
		if(text.length!=10)
		{		
			obj.value="";
			dom.get("searchRecords_error").style.display = '';
			document.getElementById("searchRecords_error").innerHTML = '<s:text name="invalid.mobileno.length" />';
			return false;
	
		}
	validatePhoneNumber(obj,'mobile');
	}
	return true;
}

function validatePhoneNumber(obj,mode){
	var text = obj.value;
	if(text!=""){
		
	var msg;
	if(mode=='mobile')
		msg='<s:text name="invalid.mobileno" />';
	else
		msg='<s:text name="invalid.teleno" />';
	if(isNaN(text))
	{
		dom.get("searchRecords_error").style.display = '';
		document.getElementById("searchRecords_error").innerHTML = msg;
		obj.value="";
		return false;
	}
	if(text<=0)
	{
		dom.get("searchRecords_error").style.display = '';
		document.getElementById("searchRecords_error").innerHTML = msg;
		obj.value="";
		return false;
	}
	if(text.replace(".","~").search("~")!=-1)
	{
		dom.get("searchRecords_error").style.display = '';
		document.getElementById("searchRecords_error").innerHTML = '<s:text name="period.notallowed" />';
		obj.value='';
		return false;
	}
	if(text.replace("+","~").search("~")!=-1)
	{
		dom.get("searchRecords_error").style.display = '';
		document.getElementById("searchRecords_error").innerHTML = '<s:text name="plus.notallowed" />';
		obj.value='';
		return false;
	}
	}
	return true;
}


function dateValidate(){
	
	var todaysDate=getTodayDate();
	//alert("validate "+todaysDate);
	var fromdate=document.getElementById('applicationFromDate').value;
	var todate=document.getElementById('applicationToDate').value;
	
	if(fromdate!=null && fromdate!="" && fromdate!=undefined && todate!=null && todate!="" && todate!=undefined ){
		if(compareDate(fromdate,todaysDate) == -1)
		{						  	 	
			 dom.get("searchRecords_error").style.display = '';
			 document.getElementById("searchRecords_error").innerHTML = '<s:text name="fromDate.todaysDate.validate" />';			  					 
			 document.getElementById('applicationFromDate').value=""; 
			 document.getElementById('applicationFromDate').focus();
			 return false;
		}
		if(compareDate(todate,todaysDate) == -1)
		{		
			 
			 dom.get("searchRecords_error").style.display = '';
			 document.getElementById("searchRecords_error").innerHTML = '<s:text name="toDate.todaysDate.validate" />';			  
								 
			  document.getElementById('applicationToDate').value="";  
			  document.getElementById('applicationToDate').focus();
			  return false;
		}
		if(compareDate(fromdate,todate) == -1)
		{		
				
			 dom.get("searchRecords_error").style.display = '';
			 document.getElementById("searchRecords_error").innerHTML = '<s:text name="fromDate.toDate.validate" />';			  
			 document.getElementById('applicationFromDate').value="";
			 document.getElementById('applicationToDate').value="";    
			 document.getElementById('applicationFromDate').focus();
			 return false;
		}
		
   }
/*   if(fromdate==null || fromdate=="" || fromdate==undefined){
		if(todate!=null && todate!="" && todate!=undefined){
		  		
			dom.get("searchRecords_error").style.display = '';
			document.getElementById("searchRecords_error").innerHTML = '<s:text name="select.fromDate" />';
		 	document.getElementById('applicationFromDate').focus();
			return false;
		}
	}
	if(todate==null || todate=="" || todate==undefined){
		if(fromdate!=null && fromdate!="" && fromdate!=undefined){
		
		dom.get("searchRecords_error").style.display = '';
		document.getElementById("searchRecords_error").innerHTML = '<s:text name="select.todate" />';
		 document.getElementById('applicationToDate').focus();
		return false;
		}
	}*/
   return true;
}

function gotoPage(obj){
	//alert("obj.value = "+obj.value);
	
	var currRow=getRow(obj); 
	 	var objectParam;

	 	objectParam = getControlInBranch(currRow,'registrationId');
 	
	var url = "${pageContext.request.contextPath}/common/ajaxCommon!ajaxLoadActionsForSearch.action?arguments="+obj.value+"&registrationId="+objectParam.value;
	var req = initiateRequest();
	req.open("GET", url, false);
	req.send(null);
	if (req.readyState == 4)
	{
		if (req.status == 200)
		{
			 var responseString =req.responseText;  			     			
			 FLAG=responseString; 	
			
			if(FLAG=="viewApplication"){
				document.location.href="${pageContext.request.contextPath}/register/registerBpa!viewForm.action?registrationId="+objectParam.value;
					
			}
			else if(FLAG=="viewAdmissionReceipt"){
				alert("viewAdmissionReceipt");
			}
			else if(FLAG=="viewBPO"){
				document.location.href="${pageContext.request.contextPath}/report/bpaReport!printReport.action?registrationId="+objectParam.value+"&printMode=BuildingPermit";
			}
			else if(FLAG=="viewChallanNotice"){
				alert("viewChallanNotice");
			}
			else if(FLAG=="viewChallanReceipt"){
				alert("viewChallanReceipt");
			}
			else if(FLAG=="viewInspectionDetails"){
				alert("viewInspectionDetails");
			}
			else if(FLAG=="viewPPO"){
				document.location.href="${pageContext.request.contextPath}/report/bpaReport!printReport.action?registrationId="+objectParam.value+"&printMode=PlanPermit";
			}
			else if(FLAG=="viewRejectionOrder"){
				document.location.href="${pageContext.request.contextPath}/report/bpaReport!printReport.action?registrationId="+objectParam.value+"&printMode=RejectionNotice";
			}
			else if(FLAG=="viewWorkflow"){
				alert("viewWorkflow");
			}else if(FLAG=="collectFee"){

				  document.location.href="${pageContext.request.contextPath}/bill/bpaFeeCollection!viewUnitCollectTax.action?registrationId="+objectParam.value;
				
			}
			else if(FLAG=="addInspectionDetails"){

				  document.location.href="${pageContext.request.contextPath}/inspection/inspection!newForm.action?registrationId="+objectParam.value;
				
			}
			else if(FLAG=="addSiteInspection"){

				  document.location.href="${pageContext.request.contextPath}/inspection/inspection!inspectionForm.action?registrationId="+objectParam.value;
				
			}
			else if(FLAG=="addLPReply") {
				document.location.href="${pageContext.request.contextPath}/lettertoparty/lpReply!newForm.action?registrationId="+objectParam.value;	
			}
			else if(FLAG=="Letter To CMDA Reply")
				{
				document.location.href="${pageContext.request.contextPath}/lettertoparty/lpReplyCmda!newForm.action?registrationId="+objectParam.value;	
				
				}
			else if(FLAG=="viewLetterToParty"){

				  document.location.href="${pageContext.request.contextPath}/register/registerBpa!viewLetterToPartyForm.action?registrationId="+objectParam.value;
				
			}	
			else if(FLAG=="viewFeeCollection"){

				  document.location.href="${pageContext.request.contextPath}/register/registerBpa!feePaymentPdf.action?registrationId="+objectParam.value;
				
			}else if(FLAG=="printExternalFee"){

				  document.location.href="${pageContext.request.contextPath}/register/registerBpa!printExternalFeeDetails.action?registrationId="+objectParam.value;
				
			}
			else if(FLAG=="amountIsZero"){

				alert("No Amount to Be Paid/Collected.");		
			}	
			else if(FLAG=="viewOI"){

				  document.location.href="${pageContext.request.contextPath}/register/registerBpa!orderForm.action?registrationId="+objectParam.value+"&reqdAction=Order Issued";
						
			}
			else if(FLAG=="addRevisedFee"){

				  document.location.href="${pageContext.request.contextPath}/fee/revisedFee!newForm.action?registrationId="+objectParam.value;
						
			}
			
		}
	}	
}


function resetValues(){
	document.getElementById('serviceTypeSearch').value="-1";
	document.getElementById('psnSearch').value="" ;
	document.getElementById('autoDCRNo').value="" ; 
	document.getElementById('propertyIdSearch').value="" ; 
	document.getElementById('ownerSearch').value=""  ; 
	document.getElementById('fatherNameSearch').value=""  ;
 	document.getElementById('phoneNo').value="" ; 
 	document.getElementById('emailAddressSearch').value="" ; 
 	document.getElementById('adminboundaryid').value="" ;
 	document.getElementById('Zone').value="-1";
 	document.getElementById('Ward').value="-1";
 	document.getElementById('locboundaryid').value="" ;
 	document.getElementById('Area').value="-1";
 	document.getElementById('Locality').value="-1";
 	document.getElementById('Street').value="-1";
 	//document.getElementById('doorno').value="" ;
 	document.getElementById('surveyorNameSearch').value="" ;
 	document.getElementById('cmdaNumSearch').value="" ;
 	document.getElementById('applicationFromDate').value="" ;
 	document.getElementById('applicationToDate').value="" ;
 	//document.getElementById('challanFromDate').value="" ;
 //	document.getElementById('challanToDate').value="" ;
 //	document.getElementById('challanNo').value="" ;
 	document.getElementById('baNumSearch').value="" ;
 	document.getElementById('statusSearch').value="-1" ;
 	document.getElementById('appMode').value="-1" ; 
 //	document.getElementById('user').value="" ;
 	document.getElementById("tableData").style.display='none';
}


/*
 * if(trimAll(document.getElementById('regNo').value)=="" &&
			trimAll(document.getElementById('regYear').value)=="" && 
			trimAll(document.getElementById('fromDate').value)==""  && 
		 	trimAll(document.getElementById('toDate').value)=="" && 
		 	trimAll(document.getElementById('firstName').value)=="" && 
		 	trimAll(document.getElementById('midName').value)=="" && 
		 	trimAll(document.getElementById('lastName').value)=="" && 
		 	document.getElementById('sextype').value==-1 && 
		 	trimAll(document.getElementById('fatherName').value)=="" && 
		 	trimAll(document.getElementById('motherName').value)=="" && 
		 	trimAll(document.getElementById('pincode').value)=="" &&
		 	document.getElementById('registrationUnitId').value=="" &&
		 	placeOfEventValue==""){
		 		dom.get("searchRecords_error").style.display = '';
				document.getElementById("searchRecords_error").innerHTML = '<s:text name="select.atleast.one.value" />';			  					 		 		
				return false;
		}	
		
	
		return true;
	}
 */

 function viewRegisterBpa(registrationId){
		document.location.href="${pageContext.request.contextPath}/register/registerBpa!viewForm.action?registrationId="+registrationId;		
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

	<s:form action="search" theme="simple" name="searchForm">
	
		 <s:hidden name="searchMode" id="searchMode"/>
		 <s:hidden name="mode" id="mode" value="%{mode}" />

		
		<div class="headingbg"><span class="bold"><s:text name="applicationdetails.header.lbl"/></span></div>
		<table width="100%" border="0" cellspacing="0" cellpadding="2">
		
			<tr>
				<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="8%"><s:text name="planSubmissionNo.lbl"/></td>
				<td class="bluebox" ><s:textfield id="psnSearch" name="planSubmissionNum" value="%{planSubmissionNum}" /></td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox" width="8%"><s:text name="serviceType.lbl"/> </td>
				<td width="21%" class="bluebox">
					<s:select id="serviceTypeSearch" name="serviceType"  
						list="dropdownData.serviceTypeList" headerKey="-1" headerValue="---choose----"
						listKey="id" listValue="code+'-'+description"  value="%{serviceType.id}" />
				 </td>	
			</tr>
			
			<tr>
				<td class="greybox">&nbsp;</td>
				<td class="greybox"><s:text name="autoDCRNo.lbl"/> </td>
				<td class="greybox" ><s:textfield id="autoDCRNo" name="autoDCRNo" value="%{autoDCRNo}" /></td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox"><s:text name="propertyId.lbl"/> </td>
				<td class="greybox" ><s:textfield id="propertyIdSearch" name="propertyid" value="%{propertyid}" /></td>			
				
			</tr>
		
			<tr>
				<td class="bluebox" >&nbsp;</td>
				<td class="bluebox" ><s:text name="applicantName.lbl"/></td>
				<td class="bluebox" ><s:textfield id="ownerSearch" name="owner.firstName" value="%{owner.firstName}"/></td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox" ><s:text name="fatherHusbandName.lbl"/></td>
				<td class="bluebox" ><s:textfield id="fatherNameSearch" name="owner.fatherName" value="%{owner.fatherName}"/></td>
				
			</tr>
			
			<tr>
				<td class="greybox" width="20%">&nbsp;</td>				
				<td class="greybox" ><s:text name="emailid.lbl"/></td>
				<td class="greybox" ><s:textfield id="emailAddressSearch" name="owner.emailAddress" value="%{owner.emailAddress}" onblur="validateEmail();" /></td>
				<td class="greybox">&nbsp;</td>
					<td class="greybox" ><s:text name="mobileNo.lbl"/></td>
				<td class="greybox" ><s:textfield id="phoneNo" name="phoneNo" value="%{phoneNo}" onblur="validateMobileNumber(this);" /></td>
				

			</tr>
				<tr>
			<td class="bluebox" >&nbsp;</td>
		<td class="bluebox" ><s:text name="applMode" /> </td>
		<td class="bluebox"> <s:select  id="appMode" name="appMode" value="%{appMode}" 
		list="dropdownData.applicationModeList" listKey="code" listValue="code" headerKey="-1" headerValue="-----choose----"/>
		 </td>
          <td class="bluebox" width="13%">&nbsp;</td>
           <td class="bluebox" >&nbsp;</td>
            <td class="bluebox" >&nbsp;</td>
             
          </tr>
		</table>
			
		<div class="headingbg"><span class="bold"><s:text name="sitedetails.header.lbl"/></span></div>
		<table width="100%" border="0" cellspacing="0" cellpadding="2">
			
			<tr>								
				<egov:loadBoundaryData adminboundaryid="${adminboundaryid.id}"
						locboundaryid="${locboundaryid.id}" adminBndryVarId="adminboundaryid" locBndryVarId="locboundaryid" />
				<s:hidden  id= "adminboundaryid" name="adminboundaryid" value="%{adminboundaryid.id}" />
				<s:hidden  id= "locboundaryid"  name="locboundaryid" value="%{locboundaryid.id}" />	
			</tr>
			
			<!-- <tr>
				<td class="greybox">&nbsp;</td>
				<td class="greybox"><s:text name="doorno.lbl"/></td>
				<td class="greybox"><s:textfield id="doorno" name="doorno" value="%{doorno}"/></td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
			</tr> -->
			
			<tr>
				<td class="greybox" width="20%">&nbsp;</td>
				<td class="greybox" width="8%"><s:text name="surveyor.name.lbl"/></td>
				<td class="greybox" width="26%"><s:textfield id="surveyorNameSearch" name="surveyorName.name" value="%{surveyorName.name}"/></td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox" width="8%"><s:text name="CMDAProposalNo.lbl"/></td>
				<td class="greybox" ><s:textfield id="cmdaNumSearch" name="cmdaNum" value="%{cmdaNum}"/></td>				
			</tr>
			
			<tr>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="ApplicationFromdate.lbl"/> </td>
				<td class="bluebox"><sj:datepicker value="%{applicationFromDate}" id="applicationFromDate" name="applicationFromDate" displayFormat="dd/mm/yy" showOn="focus"/></td>					
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="ApplicationTodate.lbl"/> </td>
				<td class="bluebox"><sj:datepicker value="%{applicationToDate}" id="applicationToDate" name="applicationToDate" displayFormat="dd/mm/yy" showOn="focus"/></td>
				
			</tr>
			
		<!--   <tr>
				<td class="greybox">&nbsp;</td>
				<td class="greybox"><s:text name="challanFromdate.lbl"/> </td>
				<td class="greybox">
					<s:date format="dd/MM/yyyy" name="challanFromDate" var="TempDate1"/>
					<s:textfield name="challanFromDate" id="challanFromDate"  maxlength="20" value="%{TempDate1}"  onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);" />
					<a href="javascript:show_calendar('forms[0].challanFromDate');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true; "><img src="${pageContext.request.contextPath}/common/image/calendaricon.gif" border="0"/></a>
					<br/>
				</td>  
				<td class="greybox"><s:text name="challanTodate.lbl"/> </td>
				<td class="greybox">
				
					<s:date name="challanTodate" format="dd/MM/yyyy" var="dateTemp1"/>
					<s:textfield name="challanTodate" id="challanTodate" maxlength="20" value="%{dateTemp1}" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);" />
					<a href="javascript:show_calendar('forms[0].challanTodate');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true; "><img src="${pageContext.request.contextPath}/common/image/calendaricon.gif" border="0"/></a>
					</td>			
				<td class="greybox">&nbsp;</td>
			</tr>
		
			<tr>
				<td class="greybox" >&nbsp;</td>
				<td class="greybox" ><s:text name="challanNo.lbl"/></td>
				<td class="greybox" ><s:textfield id="challanNo" name="challanNo" value="%{challanNo}"  /></td>
				<td class="greybox" ><s:text name="current.user.lbl"/></td>
				<td class="greybox" ><s:textfield id="user" name="user" value="%{user}"  /></td>
				
			</tr>
			-->
			<tr>
				<td class="greybox" >&nbsp;</td>
				<td class="greybox" ><s:text name="file.status.lbl"/></td>
				<td class="greybox" ><s:select id="statusSearch" name="egwStatus.id"  
						list="dropdownData.statusList" listKey="id" listValue="code" 
						headerKey="-1" headerValue="------choose--------"
						value="%{egwStatus.id}"  />	</td></td>
						<td class="greybox">&nbsp;</td>
				<td class="greybox" ><s:text name="orderNo.lbl"/></td>
				<td class="greybox" ><s:textfield id="baNumSearch" name="baNum" value="%{baNum}"  /></td>
				
				
			</tr>
		
		</table>
		
		<div class="buttonbottom" align="center">
			<table>
				<tr>
					<td><s:submit cssClass="buttonsubmit" id="submit" name="submit" value="Search"  method="searchResults" onclick="return dateValidate();" /></td>
			  		 <td><input type="button" id="reset" name="reset" class="button" value="Reset"  onclick="return resetValues();" /></td>
			  		 <td><input type="button" name="close" id="close" class="button" value="Close" onclick="window.close();"/></td>
			  	</tr>
	        </table>
	   </div>	   
	   
	   <div id="tableData">
	   <div class="infostyle" id="search_error" style="display:none;"></div> 
		 <s:if test="%{searchMode=='result'}">
          		 <div id="displaytbl">	
          		     	 <display:table  name="searchResult" export="true" requestURI="" id="regListid"  class="its" uid="currentRowObject" >
          			 	<display:caption style="text-align: center; margin: 10px 0 10px 0;" class="headerbold" >Building Plan Applications</display:caption>
                        <div STYLE="display: table-header-group" align="center">
          			 	 <display:setProperty name="basic.show.header" value="true" />
          			 	 <display:setProperty name="export.xml" value="false" />
                		<display:setProperty name="export.pdf" value="true" />
                        <display:setProperty name="export.excel" value="false" />
                      	<display:setProperty name="export.csv" value="false" />
                      	<display:setProperty name="export.pdf.filename" value="SearchBpa.pdf" />
          			 	
          			 	  
 						 	<display:column title=" Sl No" style="text-align:center;"  >
 						 	 		<s:property value="#attr.currentRowObject_rowNum + (page-1)*pageSize"/>
 						 	</display:column>
						
							<display:column class="hidden" headerClass="hidden"  media="html">
 						 		<s:hidden id="registrationId" name="registrationId" value="%{#attr.currentRowObject.id}" />
							</display:column>
							
							<display:column title="Plan Submission Number " style="text-align:center;" >	
 						 	<a href="#" onclick="viewRegisterBpa('${currentRowObject.id}')">
 						 		 ${currentRowObject.planSubmissionNum}
 						 	</a>
 						 	</display:column>	
 						 	
 						 	<display:column  title="Plan Submission Date" style="width:6%;text-align:left" >
								<s:date name="#attr.currentRowObject.planSubmissionDate" format="dd/MM/yyyy" />
							</display:column>
 						 	
							<display:column title="Service Type" style="text-align:center;" property="serviceType.description" />								
						
							<display:column title="Applicant Name " style="text-align:center;" property="owner.firstName" />	 
							<display:column title="Mobile Number" style="text-align:center;" property="owner.mobilePhone" />
							<display:column title="Applicant Address " style="text-align:center;" property="bpaOwnerAddress" />	 								
							
							<display:column title="Current Owner" style="text-align:center;"  >
          			        	<s:property value="%{getUsertName(#attr.currentRowObject.state.owner.id)}" />
							</display:column>
							
							<display:column title="Last Transaction Date " style="text-align:center;"  >	
								<s:date name="#attr.currentRowObject.modifiedDate" format="dd/MM/yyyy" />
							</display:column>
							
							<display:column title="Status " style="text-align:center;" property="egwStatus.code" />	 
							
							<display:column title="Next Action " style="text-align:center;" property="state.nextAction" media="html"/>	 
								 
							<display:column title="Actions"  style="width:10%;text-align:left" media="html">					
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
  	     </div>
	
	   
	   <div>
	   
	   </div>
	  
	   
	  </s:form>
	  </body>
</html>