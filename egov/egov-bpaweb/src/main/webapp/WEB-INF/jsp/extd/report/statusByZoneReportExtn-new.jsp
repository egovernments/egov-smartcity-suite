<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@	taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>

<%@ include file="/includes/taglibs.jsp" %>
<html>

<head>
<title>Application Status Report By Zone</title>
	
	<sj:head jqueryui="true" jquerytheme="redmond" loadAtOnce="true"/>
	

<script type="text/javascript">
jQuery.noConflict(); 

function dateValidate(){
	
	var todaysDate=getTodayDate();
//alert("validate "+todaysDate);
	var fromdate=document.getElementById('applicationFromDate').value;
	var todate=document.getElementById('applicationToDate').value;
	
	if((fromdate!=null && fromdate!="" && fromdate!=undefined )||( todate!=null && todate!="" && todate!=undefined) ){
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
}

function  getWardByZone(zonecode,statecode)
{
	//alert("code" +zonecode);
	
	document.searchForm.action =('${pageContext.request.contextPath}/extd/report/statusByZoneReportExtn!getWardByZone.action?zonecode='+zonecode+'&statecode='+statecode);	
    document.searchForm.submit();           
    //return true;
	}

function resetValues(){
	//alert("reset");
	document.getElementById('serviceType').value="-1";
   // document.getElementById('egwStatus').value="-1";
    document.getElementById('statusSearch').value="-1" ;
 	document.getElementById('adminboundaryid').value="-1" ;
 	document.getElementById('appMode').value="-1" ; 
 	/*document.getElementById('Zone').value="-1";
 	document.getElementById('Ward').value="-1";
 	document.getElementById('locboundaryid').value="" ;
 	document.getElementById('Area').value="-1";
 	document.getElementById('Locality').value="-1";
 	document.getElementById('Street').value="-1";*/
  	document.getElementById('applicationFromDate').value="" ;
 	document.getElementById('applicationToDate').value="" ;
document.getElementById("searchdetails").style.display='none';
}
function closeWindow()
{
  window.close();
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
<s:form action="statusByZoneReportExtn" name="searchForm" theme="simple">
 


<div class="formheading"/></div>
		<table width="100%" border="0" cellspacing="0" cellpadding="2">
		
				<tr>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="Application Fromdate"/> </td>
				<td class="bluebox"><sj:datepicker value="%{applicationFromDate}" id="applicationFromDate" name="applicationFromDate" displayFormat="dd/mm/yy" showOn="focus"/></td>					
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="Application Todate"/> </td>
				<td class="bluebox"><sj:datepicker value="%{applicationToDate}" id="applicationToDate" name="applicationToDate" displayFormat="dd/mm/yy" showOn="focus"/></td>
				
			</tr>
				
			<tr>
				<td class="greybox">&nbsp;</td>
				<td class="greybox"><s:text name="Zone.lbl"/> </td>
				<td width="21%" class="greybox">
					<s:select id="adminboundaryid" name="adminboundaryid"  
						list="dropdownData.adminboundaryList" headerKey="-1" headerValue="---choose----"
						listKey="id" listValue="name"  value="%{adminboundaryid}" />
				</td>	
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp; </td>
				<td class="greybox" >&nbsp;</td>
						
			
				 
			</tr>
		
			
			<tr>
			<td class="bluebox">&nbsp;</td>
			<td class="bluebox"><s:text name=" Service Type "/> </td>
				<td width="21%" class="bluebox">
					<s:select id="serviceType" name="serviceType"  
						list="dropdownData.serviceTypeList" headerKey="-1" headerValue="---choose----"
						listKey="id" listValue="code+'-'+description"  value="%{serviceType}" />
				 </td>	<td class="bluebox">&nbsp;</td>
			<td class="bluebox"><s:text name="Status.lbl"/> </td>
				<td class="bluebox" ><s:select id="statusSearch" name="egwStatus"  
						list="dropdownData.statusList" listKey="id" listValue="code" 
						headerKey="-1" headerValue="------choose--------"
						value="%{egwStatus}" />	</td>
						
			
				 
			</tr>
				<tr>
			<td class="greybox" width="13%">&nbsp;</td>
		<td class="greybox" width="13%"><s:text name="applMode" /> </td>
		<td class="greybox"> <s:select  id="appMode" name="appMode" value="%{appMode}" 
		list="dropdownData.applicationModeList" listKey="code" listValue="code" headerKey="-1" headerValue="-----choose----"/>
		 </td>
          <td class="greybox" width="13%">&nbsp;</td>
            <td class="greybox" width="13%">&nbsp;</td>
              <td class="greybox" width="13%">&nbsp;</td>
          </tr>
			</table>
			<div class="buttonbottom" align="center">
			<table>
				<tr>
					<td><s:submit cssClass="buttonsubmit" id="search" name="search" value="Search" method="reportResult"  onclick="return dateValidate();" /></td>
				 <td><input type="button" id="reset" name="reset" class="button" value="Reset"  onclick="return resetValues();" /></td>
			  		<td><input type="button" name="close" id="close" class="button" value="Close" onclick="window.close();"/></td>
			  	</tr>
	        </table>
	   </div>
	<div id="tableData">
	   <div class="infostyle" id="searchRecords_error" style="display:none;"></div> 
<s:if test="%{searchMode=='result'}">
	    <div id="searchdetails" >
	  
<table width="100%" class="its" border="0" cellspacing="0" cellpadding="2">	
	<tr>						   
<div align="center">
<th class="bluebgheadtd" width="10%"><s:text name="Status.lbll" />
</th>   
</div>
<div align="center">
<th class="bluebgheadtd" colspan="80%" ><s:text name="Zone.lbl" />
</th>   
</div>
</tr>
<tr>
<div align="center">
<th class="bluebgheadtd" width="10%">
</div></th>
<s:iterator value="appstatusmap" status="headermapstatus">
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


  <s:iterator value="appstatusmap" status="outerstatus">
  
 <tr>
     <td class="blueborderfortd">
  <s:property value="key"  /> 
  <s:set var="count" value="key"/>
  
 </td>
    <s:iterator value="value">
 <td class="blueborderfortd">
 <div align="center">
 <s:if test="%{value>0 && key!='total'}">
<a href="#" onclick="getWardByZone('<s:property value="key"/>','<s:property value="#count"/>')"></s:if>


       <s:property value="value"/>
</a>
 </div>
     </td>
  </s:iterator>
</tr>
 
</s:iterator>


  </table>
 </div>
 </s:if>
 </div> 
   </s:form>
	   </body>
	   </html>
	   
	   
