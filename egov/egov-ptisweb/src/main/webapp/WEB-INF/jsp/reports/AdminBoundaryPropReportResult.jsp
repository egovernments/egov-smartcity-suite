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

