<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@	taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>

<%@ include file="/includes/taglibs.jsp" %>
<html>

<head>
<title>Report For DD Details Against Fee Group For Official </title>
	
<sj:head jqueryui="true" jquerytheme="redmond" loadAtOnce="true" />

<link rel="stylesheet" type="text/css"
	href="<c:url value='/common/css/jquery/jquery.multiselect.css'/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value='/common/css/jquery/jquery.multiselect.filter.css'/>" />
<script type="text/javascript"
	src="<c:url value='/common/js/jquery/jquery.multiselect.min.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/common/js/jquery/jquery.multiselect.filter.js'/>"></script>


<script type="text/javascript">
	jQuery.noConflict();
	jQuery(document).ready(function() {

	jQuery("#serviceTypeList").multiselect();
	jQuery("#locboundaryid").hide();
	jQuery("#AreaArea").hide();
	jQuery("#Area").hide();
	jQuery('#Area').parent('td').prev('td').hide();
	jQuery("#StreetStreet").hide();
	jQuery("#LocalityLocality").hide();
	jQuery("#Ward").removeAttr("onchange");

	});
function dateValidate(){
	
	var todaysDate=getTodayDate();
//alert("validate "+todaysDate);
	var fromdate=document.getElementById('orderissuedFromDate').value;
	var todate=document.getElementById('orderissuedToDate').value;

	var wardnum=document.getElementById('Ward').value;
	var zonenum=document.getElementById('Zone').value;
	if(wardnum!=null && wardnum!=-1){
		
		document.getElementById('adminboundaryid').value=wardnum;
	}
	else if(wardnum==-1)
		{
		document.getElementById('adminboundaryid').value=zonenum;
		}
	if((fromdate!=null && fromdate!="" && fromdate!=undefined) ||( todate!=null && todate!="" && todate!=undefined )){
		if(compareDate(fromdate,todaysDate) == -1)
		{						  	 	
			 dom.get("searchRecords_error").style.display = '';
			 document.getElementById("searchRecords_error").innerHTML = '<s:text name="fromDate.todaysDate.validate" />';			  					 
			 document.getElementById('orderissuedFromDate').value=""; 
			 document.getElementById('orderissuedFromDate').focus();
			 return false;
		}
		if(compareDate(todate,todaysDate) == -1)
		{		
			 
			 dom.get("searchRecords_error").style.display = '';
			 document.getElementById("searchRecords_error").innerHTML = '<s:text name="toDate.todaysDate.validate" />';			  
								 
			  document.getElementById('orderissuedToDate').value="";  
			  document.getElementById('orderissuedToDate').focus();
			  return false;
		}
		if(compareDate(fromdate,todate) == -1)
		{		
				
			 dom.get("searchRecords_error").style.display = '';
			 document.getElementById("searchRecords_error").innerHTML = '<s:text name="fromDate.toDate.validate" />';			  
			 document.getElementById('orderissuedFromDate').value="";
			 document.getElementById('orderissuedToDate').value="";    
			 document.getElementById('orderissuedFromDate').focus();
			 return false;
		}
		
   }
	if(!checkAllfields()){
		return false;
	}
}
function resetValues(){
	document.getElementById('serviceTypeList').value = "-1";
	jQuery('#serviceTypeList').multiselect('uncheckAll');
   document.getElementById('adminboundaryid').value="" ;
 	document.getElementById('Zone').value="-1";
 	document.getElementById('Region').value="-1";
 	document.getElementById('Ward').value="-1";
 	document.getElementById('locboundaryid').value="" ;
 	document.getElementById('Area').value="-1";
 	document.getElementById('feesGroup').value="-1";
 	document.getElementById('Locality').value="-1";
 	document.getElementById('Street').value="-1";
  	document.getElementById('orderissuedFromDate').value="" ;
 	document.getElementById('orderissuedToDate').value="" ;
 	document.getElementById("searchdetails").style.display='none';
 	jQuery("#printid").hide();
}
function closeWindow()
{
  window.close();
  }
function checkAllfields(){
	  if(document.getElementById('adminboundaryid').value ==-1 && document.getElementById('orderissuedFromDate').value=="" && document.getElementById('orderissuedToDate').value=="" &&
				 document.getElementById("serviceTypeList").value ==""  && document.getElementById('feesGroup').value==-1)
		{                     
			dom.get("searchRecords_error").style.display = '';
			document.getElementById("searchRecords_error").innerHTML = 'At Least one information has to be provided';	
			return false;         
			}
		return true;
	   	
		
} 



function printDiv(divName) 
{ 
	  jQuery("#breadcrumb").hide();
		jQuery(".commontopyellowbg").hide();
		jQuery(".commontopbluebg").hide();
		 jQuery("#searchscreen").hide();
		jQuery("#printid").hide();
		 window.print();
		jQuery("#breadcrumb").show();
		jQuery(".commontopyellowbg").show();
		jQuery(".commontopbluebg").show();
		 jQuery("#searchscreen").show();
		 jQuery("#printid").show();

  	} 
</script>
</head>
<body  >
<div class="errorstyle" id="searchRecords_error" style="display: none;"></div>
	<div class="errorstyle" style="display: none" ></div>
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />	
			<s:fielderror />
		</div>
	</s:if>
<s:form action="dDDetailsByFeesGroupReport" name="registerReportForm" theme="simple">


<div id="searchscreen">
<div class="formheading"/></div>
		<table width="100%" border="0" cellspacing="0" cellpadding="2">
		
				<tr>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="Order Issued Fromdate"/> </td>
				<td class="bluebox"><sj:datepicker value="%{orderissuedFromDate}" id="orderissuedFromDate" name="orderissuedFromDate" displayFormat="dd/mm/yy" showOn="focus"/></td>					
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="Order Issued Todate"/> </td>
				<td class="bluebox"><sj:datepicker value="%{orderissuedToDate}" id="orderissuedToDate" name="orderissuedToDate" displayFormat="dd/mm/yy" showOn="focus"/></td>
				
			</tr>
				
			<tr>								
				<egov:loadBoundaryData adminboundaryid="${adminboundaryid}"
						locboundaryid="${locboundaryid}" adminBndryVarId="adminboundaryid" locBndryVarId="locboundaryid" />
				<s:hidden id= "adminboundaryid" name="adminboundaryid" value="%{adminboundaryid}" />
				<s:hidden id= "locboundaryid"  name="locboundaryid" value="%{locboundaryid}" />	
			</tr>
		
			
			<tr>
			<td class="greybox">&nbsp;</td>
			<td class="greybox"><s:text name="Service Type"/> </td>
				<td width="21%" class="greybox">
					<s:select id="serviceTypeList"
							name="serviceTypeList" multiple="true"
							list="dropdownData.serviceTypeDropDownList" listKey="id"
							listValue="code+'-'+description" value="%{serviceTypeList}" />
				 </td>	<td class="greybox">&nbsp;</td>
				 
				 <td class="greybox"><s:text name="Fees Group "/> </td>
				<td width="21%" class="greybox">
					<s:select id="feesGroup"
						name="feesGroup" checkmark="true" list="dropdownData.feesGroupList"
						listKey="code" listValue="code" value="%{feesGroup}" headerKey="-1" 
         headerValue="----choose-----"  />
				 </td>	<td class="greybox">&nbsp;</td>
		
			</tr>
			<tr>
		
			</table>
			<div class="buttonbottom" align="center">
			<table>
				<tr>
					<td><s:submit cssClass="buttonsubmit" id="search" name="search" value="Search"  onclick="return dateValidate();"  method="searchResults"/></td>
				 <td><input type="button" id="reset" name="reset" class="button" value="Reset"  onclick="return resetValues();" /></td>
			  		<td><input type="button" name="close" id="close" class="button" value="Close" onclick="window.close();"/></td>
			  	</tr>
	        </table>
	   </div>
	   </div>
<div id="tableData">
		<s:if test="%{searchMode=='result'}">
				<div class="infostyle" id="search_error" style="display: none;"></div>
	<div id="searchdetails" >
	   
	   <table width="100%" class="its" border="0" cellspacing="0" cellpadding="2">	
<tr>						   
<div align="center">
<th class="bluebgheadtd" width="10%"><s:text name="Plan Submission Number" />
</th>   
</div>
<div align="center">
<th class="bluebgheadtd" colspan="80%" >Fees Description
</th>   
</div>
</tr>


<tr>						   
<div align="center">
<th class="bluebgheadtd" width="10%">
</th>
</div>
<s:iterator value="FeeDetailmap" status="headermapstatus">

   <s:if test="#headermapstatus.index ==0">
   
     
    <s:iterator value="value">
   
     <th class="bluebgheadtd" width="10%">
     <div align="center">
     <s:property value="key"/>
     </div></th>  
     
   </s:iterator>
   
   </s:if>
</s:iterator>

</tr>

 
  

 
  <s:iterator value="FeeDetailmap" status="outerstatus">
   <tr>
     <td class="blueborderfortd">
     
  <s:property value="key" />
   <s:set var="count" value="key"/>
 </td>
    <s:iterator value="value">
 <td class="blueborderfortd">
 <div align="center">
 

<s:property value="value"/>


 </div>
     </td>
  </s:iterator>

 
</s:iterator>
</tr>

  </table>
	   
	   </div>
	   </s:if>
	   </div>
	    <div id="printid" class="buttonbottom" align="center">
		<s:if test="%{searchMode=='result'}">
			<table>
			<tr>
			<td><input type="button" name="Print" id="Print" class="button"  value="Print"  onclick="printDiv('tableData')" />
			</td>
			</tr>
			</table>
			</s:if>
		</div>
</s:form>
</body>
</html>
