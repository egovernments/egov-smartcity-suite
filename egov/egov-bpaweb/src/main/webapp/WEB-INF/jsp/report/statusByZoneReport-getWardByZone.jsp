<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@	taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>

<%@ include file="/includes/taglibs.jsp" %>
<html>

<head>
<title>Application Status Report By Zone</title>
<script type="text/javascript">

function  showDetail(wardcode,statecode)
{
	//if(wardcode='Total')
	//	wardcode='0';
	document.searchForm.action =('${pageContext.request.contextPath}/report/statusByZoneReport!showdetail.action?wardcode='+wardcode+'&statecode='+statecode);	
 document.searchForm.submit();           
return true;
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
<s:form action="statusByZoneReport" name="searchForm" theme="simple">
<s:hidden name="serviceType" id="serviceType" value="%{serviceType}"/>
		 <s:hidden name="applicationFromDate" id="applicationFromDate" value="%{applicationFromDate}" />
		 <s:hidden name="egwStatus" id="egwStatus" value="%{egwStatus}"/>
		 <s:hidden name="applicationToDate" id="applicationToDate" value="%{applicationToDate}" />
		 <s:hidden name="adminboundaryid" id="adminboundaryid" value="%{adminboundaryid}"/>
		 <s:hidden name="appMode" id="appMode" value="%{appMode}"/>
		  <s:hidden name="zonecode" id="zonecode" value="%{zonecode}"/>
<div id="tableData" >
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
<th class="bluebgheadtd" colspan="80%" ><s:text name="Ward" />
</th>   
</div>
</tr>
<tr>
<div align="center">
<th class="bluebgheadtd" width="10%">
</div></th>
<s:iterator value="appstatusmap" status="headermapstatusward">
 <s:if test="#headermapstatusward.index ==0">
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
  <s:property value="key" /> 
   <s:set var="count" value="key"/>

  
 </td>
    <s:iterator value="value">
 <td class="blueborderfortd">
 <div align="center">
   <s:if test="%{value>0}">
<a href="#" onclick="showDetail('<s:property value="key"/>','<s:property value="#count"/>')"> </s:if>
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
	   
	   
