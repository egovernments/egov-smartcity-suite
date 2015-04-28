#-------------------------------------------------------------------------------
# <!-- #-------------------------------------------------------------------------------
# # eGov suite of products aim to improve the internal efficiency,transparency, 
# #    accountability and the service delivery of the government  organizations.
# # 
# #     Copyright (C) <2015>  eGovernments Foundation
# # 
# #     The updated version of eGov suite of products as by eGovernments Foundation 
# #     is available at http://www.egovernments.org
# # 
# #     This program is free software: you can redistribute it and/or modify
# #     it under the terms of the GNU General Public License as published by
# #     the Free Software Foundation, either version 3 of the License, or
# #     any later version.
# # 
# #     This program is distributed in the hope that it will be useful,
# #     but WITHOUT ANY WARRANTY; without even the implied warranty of
# #     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# #     GNU General Public License for more details.
# # 
# #     You should have received a copy of the GNU General Public License
# #     along with this program. If not, see http://www.gnu.org/licenses/ or 
# #     http://www.gnu.org/licenses/gpl.html .
# # 
# #     In addition to the terms of the GPL license to be adhered to in using this
# #     program, the following additional terms are to be complied with:
# # 
# # 	1) All versions of this program, verbatim or modified must carry this 
# # 	   Legal Notice.
# # 
# # 	2) Any misrepresentation of the origin of the material is prohibited. It 
# # 	   is required that all modified versions of this material be marked in 
# # 	   reasonable ways as different from the original version.
# # 
# # 	3) This license does not grant any rights to any user of the program 
# # 	   with regards to rights under trademark law for use of the trade names 
# # 	   or trademarks of eGovernments Foundation.
# # 
# #   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
# #------------------------------------------------------------------------------- -->
#-------------------------------------------------------------------------------
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@	taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>

<%@ include file="/includes/taglibs.jsp" %>
<html>

<head>
<title>Application Status Report</title>
	
	<sj:head jqueryui="true" jquerytheme="redmond" loadAtOnce="true" />
	

<script type="text/javascript">
jQuery.noConflict();

function dateValidate(){
	
	var todaysDate=getTodayDate();
//alert("validate "+todaysDate);
	var fromdate=document.getElementById('applicationFromDate').value;
	var todate=document.getElementById('applicationToDate').value;
	
	if((fromdate!=null && fromdate!="" && fromdate!=undefined) ||( todate!=null && todate!="" && todate!=undefined )){
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

function  showDetail(servcode,statecode)
{
	if(servcode=='total')
		servcode=0;
	document.registerReportForm.action =('${pageContext.request.contextPath}/extd/report/registerReportExtn!showdetail.action?servcode='+servcode+'&statecode='+statecode);	

	document.registerReportForm.submit();           
//return true;
	}


function resetValues(){
	//alert("reset");
	document.getElementById('serviceType').value="-1";
   // document.getElementById('egwStatus').value="-1";
    document.getElementById('statusSearch').value="-1" ;
 	document.getElementById('adminboundaryid').value="" ;
 	document.getElementById('Zone').value="-1";
 	document.getElementById('Region').value="-1";
 	document.getElementById('Ward').value="-1";
 	document.getElementById('locboundaryid').value="" ;
 	document.getElementById('Area').value="-1";
 	document.getElementById('Locality').value="-1";
 	document.getElementById('Street').value="-1";
  	document.getElementById('applicationFromDate').value="" ;
 	document.getElementById('applicationToDate').value="" ;
 	document.getElementById('appMode').value="-1" ; 
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
<s:form action="registerReportExtn" name="registerReportForm" theme="simple">



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
				<egov:loadBoundaryData adminboundaryid="${adminboundaryid}"
						locboundaryid="${locboundaryid}" adminBndryVarId="adminboundaryid" locBndryVarId="locboundaryid" />
				<s:hidden id= "adminboundaryid" name="adminboundaryid" value="%{adminboundaryid}" />
				<s:hidden id= "locboundaryid"  name="locboundaryid" value="%{locboundaryid}" />	
			</tr>
		
			
			<tr>
			<td class="bluebox">&nbsp;</td>
			<td class="bluebox"><s:text name="Service Type"/> </td>
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
					<td><s:submit cssClass="buttonsubmit" id="search" name="search" value="Search"  method="searchResults" onclick="return dateValidate();" /></td>
				 <td><input type="button" id="reset" name="reset" class="button" value="Reset"  onclick="return resetValues();" /></td>
			  		<td><input type="button" name="close" id="close" class="button" value="Close" onclick="window.close();"/></td>
			  	</tr>
	        </table>
	   </div>
<div id="tableData">
	   <div class="infostyle" id="search_error" style="display:none;"></div> 
<s:if test="%{searchMode=='result'}"> 
<div id="searchdetails" >

<table width="100%" class="its" border="0" cellspacing="0" cellpadding="2">	
<tr>						   
<div align="center">
<th class="bluebgheadtd" width="10%"><s:text name="Status.lbll" />
</th>   
</div>
<div align="center">
<th class="bluebgheadtd" colspan="80%" >Service Type
</th>   
</div>
</tr>


<tr>						   
<div align="center">
<th class="bluebgheadtd" width="10%">
</th>
</div>
<s:iterator value="statusmap" status="headermapstatus">

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

  <s:iterator value="statusmap" status="outerstatus">
  
 <tr>
     <td class="blueborderfortd">
  <s:property value="key" />
   <s:set var="count" value="key"/>
 </td>
    <s:iterator value="value">
 <td class="blueborderfortd">
 <div align="center">
 <s:if test="%{key!='total'}">
<s:if test="%{value>0}">
<a href="#" onclick="showDetail('<s:property value="key"/>','<s:property value="#count"/>')"> </s:if>
<s:property value="value"/>
</a>
</s:if>
<s:else>
<s:if test="%{serviceType==-1 && value>0}">
<a href="#" onclick="showDetail('<s:property value="key"/>','<s:property value="#count"/>')"> </s:if>
<s:property value="value"/>
</a>
</s:else>
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
