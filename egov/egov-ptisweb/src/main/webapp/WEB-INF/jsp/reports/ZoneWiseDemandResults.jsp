<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  ~
  --%>

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
