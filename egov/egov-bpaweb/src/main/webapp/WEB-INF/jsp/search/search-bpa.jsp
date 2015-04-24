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
var bpaData_DW="";

jQuery(document).ready(function() {

	jQuery("#revisedPlanNumber").autocomplete({
		source: function(request,response) {
				var url='${pageContext.request.contextPath}/search/search!populateRevisedPlanNumberList.action';
				//showWaiting();
				jQuery.getJSON(url,{revisedPlanNumberAutocomplete: request.term},function(data){
					response(data);		
					if(data==null || data==""){
						jQuery("#revisedPlanNumber").val('');
						//jQuery("#revisedPlanNumberId").val('');
					}		
					
		      })	
		},
	    minLength: 2,
	    select: function( event, ui ) {
		   jQuery("#revisedPlanNumber").val(ui.item.id);
		 }
	});
});

function bodyOnload(){
	jQuery('#Region').parent('td').prev('td').append('<span class="mandatory">*</span>');
	jQuery('#Zone').parent('td').prev('td').append('<span class="mandatory">*</span>');
	jQuery('#Ward').parent('td').prev('td').append('<span class="mandatory">*</span>');
}

function dateValidate(){
	
	var todaysDate=getTodayDate();
	var fromdate=document.getElementById('applicationFromDate').value;
	var todate=document.getElementById('applicationToDate').value;
	if ((fromdate != null && fromdate != "" && fromdate != undefined)||( todate != null && todate != "" && todate != undefined)){
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
		return true;
   }
}
function onSearchSubmit()
{
	var psnnum=document.getElementById('psnSearch').value ;
	var revpsnnum=document.getElementById('revisedPlanNumber').value ;
	var owner=document.getElementById('ownerSearch').value;
	var servicetype=document.getElementById('serviceTypeSearch').value ;
	var phonenum=document.getElementById('phoneNo').value;
	if((psnnum==""  ) && (servicetype==-1) && (owner=="" ) && ( phonenum=="") && (revpsnnum=="") )
	{
	return validateMandatoryFields();
	}

	}
function validateMandatoryFields(){
	
	if(document.getElementById('adminboundaryid').value=="" ||document.getElementById('adminboundaryid').value==null || document.getElementById('adminboundaryid').value=="-1"){
		dom.get("searchRecords_error").style.display = '';
		document.getElementById("searchRecords_error").innerHTML = '<s:text name="adminboundaryid.required" />';
		return false;
	}
	if(document.getElementById('Region').value==null || document.getElementById('Region').value=="-1"){
		dom.get("searchRecords_error").style.display = '';
		document.getElementById("searchRecords_error").innerHTML = '<s:text name="Region.required" />';
		return false;
	}
	if(document.getElementById('Zone').value==null || document.getElementById('Zone').value=="-1"){
		dom.get("searchRecords_error").style.display = '';
		document.getElementById("searchRecords_error").innerHTML = '<s:text name="zone.required" />';
		return false;
	}
	if(document.getElementById('Ward').value==null || document.getElementById('Ward').value=="-1"){
		dom.get("searchRecords_error").style.display = '';
		document.getElementById("searchRecords_error").innerHTML = '<s:text name="ward.required" />';
		return false;
	}
	
		
	
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

function resetValues(){
	document.getElementById('serviceTypeSearch').value="-1";
 	document.getElementById('adminboundaryid').value="" ;
 	document.getElementById('Zone').value="-1";
 	document.getElementById('Region').value="-1";
 	document.getElementById('Ward').value="-1";
 	document.getElementById('locboundaryid').value="" ;
 	document.getElementById('Area').value="-1";
 	document.getElementById('Locality').value="-1";
 	document.getElementById('Street').value="-1";
 	document.getElementById('statusSearch').value="-1" ;
 	document.getElementById('psnSearch').value="" ;
 	document.getElementById('revisedPlanNumber').value ="";
 	document.getElementById('ownerSearch').value="" ;
 	document.getElementById('phoneNo').value="" ;
 	document.getElementById('applicationFromDate').value="" ;
 	document.getElementById('applicationToDate').value="" ;
 	document.getElementById('baNumSearch').value="" ; ;
 	document.getElementById("tableData").style.display='none';
}

 

	function returnBackToParent(bpaData_DW) {
		var wind;
		var data = new Array();
		row_id = $('rowid').value;
		wind=window.dialogArguments;
		if(wind==undefined){
			wind=window.opener;
			data = row_id + '`~`' + bpaData_DW;
			window.opener.update(data);
		}
		else{
			wind=window.dialogArguments;
			wind.result = row_id + '`~`' + bpaData_DW;
		}
		window.close();
	}

	function addToParent(){
		if(bpaData_DW==""){
			var errmsg='<s:text name="select.bpano.msg" />';
			alert(errmsg);
			return false;
		}
		returnBackToParent(bpaData_DW);
	}

	function setAllValues(obj){
		 bpaData_DW="";
		 var zoneId="";
		 var wardId="";
		 var areaId="";
		 var localityId="";
		 var streetId="";
		var bpano = obj.value;
		 var currRow=getRow(obj);
		  zoneId = getControlInBranch(currRow,'zoneId').value;
		
	 	wardId=getControlInBranch(currRow,'wardId').value; 
	 	areaId=getControlInBranch(currRow,'areaId').value; 
	 	localityId=getControlInBranch(currRow,'localityId').value; 
	 	streetId=getControlInBranch(currRow,'streetId').value; 
	 	revPPaNum=getControlInBranch(currRow,'revisedPlanForworks').value; 
	 	if(revPPaNum=="")
		 	{
			bpaData_DW=bpano + '`~`' + zoneId + '`~`' + wardId + '`~`' + areaId + '`~`' + localityId + '`~`' + streetId ;
	 		}
	 	else{
	 			bpaData_DW=bpano + '`~`' + zoneId + '`~`' + wardId + '`~`' + areaId + '`~`' + localityId + '`~`' + streetId + '`~`'  +revPPaNum;
			}
		
	}
 
 
</script>

</head>

<body onload="bodyOnload();">

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
		 <s:hidden name="rowId" id="rowid"/>
		 <s:hidden name="searchMode" id="searchMode"/>
		 <s:hidden name="mode" id="mode" value="%{mode}" />

		
		<div class="headingbg"><span class="bold"><s:text name="applicationdetails.header.lbl"/></span></div>
		<table width="100%" border="0" cellspacing="0" cellpadding="2">
			<tr>
				<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="8%"><s:text name="planSubmissionNo.lbl"/></td>
				<td class="bluebox" ><s:textfield id="psnSearch" name="planSubmissionNum" value="%{planSubmissionNum}" /></td>
				<td class="bluebox" width="8%"><s:text name="serviceType.lbl"/> </td>
				<td width="21%" class="bluebox">
					<s:select id="serviceTypeSearch" name="serviceType"  
						list="dropdownData.serviceTypeList" headerKey="-1" headerValue="---choose----"
						listKey="id" listValue="code+'-'+description"  value="%{serviceType.id}" />
				 </td>	
				
			</tr>
			<tr>
				<td class="greybox" width="20%">&nbsp;</td>
				<td class="greybox" width="8%"><s:text name="applicantName.lbl"/></td>
				<td class="greybox" ><s:textfield id="ownerSearch" name="owner.firstName" value="%{owner.firstName}"/></td>
				<td class="greybox" width="8%"><s:text name="mobileNo.lbl"/></td>
				<td width="21%" class="greybox"><s:textfield id="phoneNo" name="phoneNo" value="%{phoneNo}" onblur="validateMobileNumber(this);" /></td>
				
			</tr>
			<tr>
			<td class="bluebox" width="20%">&nbsp;</td>
			<td class="bluebox"><s:text name="Revised plan Number " />
					:</td>
				<td class="bluebox"><s:textfield name="revisedPlanNumber"
						id="revisedPlanNumber" value="%{revisedPlanNumber}" /> 
						
						<td class="bluebox" width="20%">&nbsp;</td>
						<td class="bluebox" width="20%">&nbsp;</td>
			</tr>
        </table>
	   </div>	
			
		<div class="headingbg"><span class="bold"><s:text name="sitedetails.header.lbl"/></span></div>
		<table width="100%" border="0" cellspacing="0" cellpadding="2">
			<tr>
				<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox"><s:text name="ApplicationFromdate.lbl"/> </td>
				<td class="bluebox"><sj:datepicker value="%{applicationFromDate}" id="applicationFromDate" name="applicationFromDate" displayFormat="dd/mm/yy" showOn="focus"/></td>					
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="ApplicationTodate.lbl"/> </td>
				<td class="bluebox"><sj:datepicker value="%{applicationToDate}" id="applicationToDate" name="applicationToDate" displayFormat="dd/mm/yy" showOn="focus"/></td>
				
			</tr>
			<tr>								
				<egov:loadBoundaryData adminboundaryid="${adminboundaryid.id}"
						locboundaryid="${locboundaryid.id}" adminBndryVarId="adminboundaryid" locBndryVarId="locboundaryid" />
				<s:hidden  id= "adminboundaryid" name="adminboundaryid" value="%{adminboundaryid.id}" />
				<s:hidden  id= "locboundaryid"  name="locboundaryid" value="%{locboundaryid.id}" />	
			</tr>
			
			<tr>
				<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" ><s:text name="file.status.lbl"/></td>
				<td class="bluebox" ><s:select id="statusSearch" name="egwStatus.id"  
						list="dropdownData.statusList" listKey="id" listValue="code" 
						value="%{egwStatus.id}"  />	</td></td>
							<td class="bluebox">&nbsp;</td>
				<td class="bluebox" ><s:text name="orderNo.lbl"/></td>
				<td class="bluebox" ><s:textfield id="baNumSearch" name="baNum" value="%{baNum}"  /></td>
				
			</tr>
		
		</table>
		</div>
		<div class="buttonbottom" align="center">
			<table>
				<tr>
					<td><s:submit cssClass="buttonsubmit" id="submit" name="submit" value="Search"  method="searchResults" onclick="return onSearchSubmit();" /></td>
			  		 <td><input type="button" id="reset" name="reset" class="button" value="Reset"  onclick="return resetValues();" /></td>
			  		 <td><input type="button" name="close" id="close" class="button" value="Close" onclick="window.close();"/></td>
			  	</tr>
	        </table>
	   </div>	   
	   
	   <div id="tableData">
	   <div class="infostyle" id="search_error" style="display:none;"></div> 
		 <s:if test="%{searchMode=='result'}">
          		 <div id="displaytbl">	
          		     	 <display:table  name="searchResult" export="false" requestURI="" id="regListid"  class="its" uid="currentRowObject" >
          			 	 <div STYLE="display: table-header-group" align="center">
          			 	  	 <display:column  headerClass="pagetableth" class="pagetabletd" title="Select" style="width:3%;text-align:center">
                                                               <input name="radio" type="radio" id="radio"  value="<s:property value='%{#attr.currentRowObject.planSubmissionNum}'/>" onClick="setAllValues(this);"  />
                                                    </display:column>
 						 	<display:column title=" Sl No" style="text-align:center;"  >
 						 	 		<s:property value="#attr.currentRowObject_rowNum + (page-1)*pageSize"/>
 						 	</display:column>
						
							<display:column class="hidden" headerClass="hidden"  media="html">
 						 		<s:hidden id="registrationId" name="registrationId" value="%{#attr.currentRowObject.id}" />
 						 		<s:hidden id="zoneId" name="zoneId" value="%{#attr.currentRowObject.adminboundaryid.parent.id}" />
 						 		<s:hidden id="wardId" name="wardId" value="%{#attr.currentRowObject.adminboundaryid.id}" />
 						 		<s:hidden id="areaId" name="areaId" value="%{#attr.currentRowObject.locboundaryid.parent.parent.id}" />
 						 		<s:hidden id="localityId" name="localityId" value="%{#attr.currentRowObject.locboundaryid.parent.id}" />
 						 		<s:hidden id="streetId" name="streetId" value="%{#attr.currentRowObject.locboundaryid.id}" />
 						 		<s:hidden id="revisedPlanForworks" name="revisedPlanForworks" value="%{#attr.currentRowObject.revisedPlanForworks}" />
 						 		
							</display:column>
							
							<display:column title="Plan Submission Number " style="text-align:center;" property="planSubmissionNum"  >	
 						 	</display:column>
 						 	<display:column title="Revised Plan Submission Number " style="text-align:center;" >	
 						 	 <s:property value="#attr.currentRowObject.revisedPlanForworks"/>
 						 	 </display:column>
 						 		
 						 	<display:column  title="Plan Submission Date" style="width:6%;text-align:left" >
								<s:date name="#attr.currentRowObject.planSubmissionDate" format="dd/MM/yyyy" />
							</display:column>
 						 	
							<display:column title="Service Type" style="text-align:center;" property="serviceType.description" />								
						
							<display:column title="Applicant Name " style="text-align:center;" property="owner.firstName" />	 
							
							<display:column title="Applicant Address " style="text-align:center;" property="bpaOwnerAddress" />	 								
						</div>
						</display:table>
						<div class="buttonbottom" align="center">
						<s:if test="%{mode=='DepositWorks' && searchResult.list.size>0}">			
						<table>
							<tr>
					  		  <td><input type="button" id="add" name="add" class="button" value="Add"  onclick="addToParent();" /></td>
					  		 <td><input type="button" id="reset" name="reset" class="button" value="Reset"  onclick="return resetValues();" /></td>
					  		 <td><input type="button" name="close" id="close" class="button" value="Close" onclick="window.close();"/></td>
			  				</tr>
	        			</table>
	        			</s:if>
	   					</div>	 
					</div>
  	   			</s:if>
  	     </div>
	   <div>
	   </div>
	  </s:form>
	  </body>
</html>