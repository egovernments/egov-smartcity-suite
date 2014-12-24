<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>Report - List of Properties for Administration Boundary Property Report</title>
  </head>
  <body>
   <form method="post" action="#">

  <div class="formmainbox"><div class="formheading"></div>
  <div class="headingbg"><bean:message key="adminPropReportResult" /></div>

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" align="center">

	<c:choose>
	<c:when test = "${fn:length(links)<1}">
	<tr>
	<th colspan="7">
      <div class="headingsmallbgnew">
      
        <div align="center"><font color="red"><b><bean:message key="noPropMsg"/></b></font></div>
        </div> </br>
       </th>  
	</tr>
	</c:when>
	<c:otherwise>
    <tr>
    <display:table name="links" export="true" class="tablebottom" style="width:100%" requestURI="/reports/adminBndryPropReportResult.do">
    <display:column property="serialNo" title="Serial Number" headerClass="bluebgheadtd" class="blueborderfortd" style="width:10%;text-align:center"/>
	<display:column property="billNumber" title="Bill Number" headerClass="bluebgheadtd" class="blueborderfortd" style="width:10%;text-align:center"/>
	<display:column property="ownerName"  title="Owner Name" headerClass="bluebgheadtd" class="blueborderfortd" style="width:17%;text-align:left" />	
	<display:column property="propAddress"  title="Property Address"  headerClass="bluebgheadtd" class="blueborderfortd" style="width:20%;text-align:left" />
	<display:column property="propertyUsage"  title="Property Usage"  sortable="true" headerClass="bluebgheadtd" class="blueborderfortd" style="width:20%;text-align:center" />
	<display:column property="aggregateCurrentDemand" title="Current Demand"  sortable="true" headerClass="bluebgheadtd" class="blueborderfortd" style="width:10%;text-align:right" />
	<display:column property="aggregateArrearDemand" title="Arrear Demand"  sortable="true" headerClass="bluebgheadtd" class="blueborderfortd" style="width:13%;text-align:right" />
	<display:column property="totalDemand" title="Total Demand" headerClass="bluebgheadtd" class="blueborderfortd" style="width:20%;text-align:right" />
	
	<display:setProperty name="paging.banner.item" value="Record"/>
	<display:setProperty name="paging.banner.items_name" value="Records"/>
	<display:setProperty name="export.pdf" value="true"/>
    </display:table>
    </tr>
	</c:otherwise>
	</c:choose>
</table>
</div>
<div class="buttonbottom" align="center">
 <input class="button" type="button"  name="button" value="Search" tabindex="67" onclick="window.location='/ptis/reports/adminBndryPropReport.do';" />
</div>

</form>
  </body>
</html>

