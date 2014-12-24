<%@ include file="/includes/taglibs.jsp" %>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>eGov- Property Tax-Zone Wise Demand Results</title>
</head>

<body>
<form method="post" action="#" >
<div class="formmainbox"><div class="formheading"></div>
<div class="headingbg"><bean:message key="zoneWiseResults"/></div>
    <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="tablebottom">
  
      <c:choose>
      <c:when test = "${fn:length(links)<1}">
       <tr>
      <th colspan="6">
        <div class="headingsmallbgnew" align="center"><font color=red><b><bean:message key="noData"/></b></font></div> 
		    </th>
    </tr>
     </c:when>
     <c:otherwise>
     
     <tr >
     <th colspan="6" >
      <div class="headingsmallbgnew" align="center"><font color=red> <bean:message key="totalRecords"/><span class="bold"><c:out value = "${fn:length(links)-1}"/></span>
      </font></div>
      </th>
    </tr>
    <tr>
    
    <display:table name="links" id="linksTable" requestURI="/report/zoneWiseDemand.action" export="true"  class="tablebottom" style="width:100%">
	<display:column property="zoneNumber" title="Zone No"  sortable="true" headerClass="bluebgheadtd" class="blueborderfortd" style="width:10%;text-align:center"/>
	<display:column property="totalArrearsDemand(Rs.)"  title="Total Arrear Demand(Rs.)"    headerClass="bluebgheadtd" class="blueborderfortd" style="width:30%;text-align:right" />	
	<display:column property="totalCurrentDemand(Rs.)"  title="Total Current Demand(Rs.)"  headerClass="bluebgheadtd" class="blueborderfortd" style="width:30%;text-align:right" />
	<display:column property="totalDemand(Rs.)"  title="Total Demand(Rs.)"  headerClass="bluebgheadtd" class="blueborderfortd" style="width:30%;text-align:right"/>
		
	<display:setProperty name="paging.banner.item" value="Record"/>
	<display:setProperty name="paging.banner.items_name" value="Records"/>
	<display:setProperty name="export.pdf" value="true"/>
    </display:table>
    </tr>
  </c:otherwise>
  </c:choose>
  </table>
</div>
</form>
</body>
</html>
