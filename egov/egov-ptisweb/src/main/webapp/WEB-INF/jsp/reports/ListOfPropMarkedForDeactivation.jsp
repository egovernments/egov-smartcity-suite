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

<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>Report - List Of Properties Marked For Deactivation</title>
 	<link href="<cdn:url value='/resources/css/propertytax.css'/>" rel="stylesheet" type="text/css" />
	<link href="<cdn:url value='/resources/css/commonegov.css'/>" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<cdn:url value='/resources/javascript/propertyTax.js'/>"></script>
  </head>
  <%
String searchValue=(String)session.getAttribute("SearchValue");
String msgStr="Sorry, No property matches the selected criteria";
%>
  <body>
   <form method="post" action="#">
<div class="formmainbox">
  <c:if test = "${fn:length(links)>0}">
    <div class="formheading"></div>
  	<div class="headingbg"><bean:message key="listOfPropMarkDeactive" /></div>
  </c:if>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" align="center">
	<tr>
	<th colspan="7">
	<div class="headingsmallbgnew">
	<div class="searchvalue1">Search Value: <%=searchValue%></div>
	</div>
	</th>
	</tr>
	
	<tr>
	<th colspan="7">
      <div class="headingsmallbgnew">
      <c:if test = "${fn:length(links) < 1}">
        <div align="center"><font color=red><b><%=msgStr%></b></font></div> </br>
     </c:if>
     </div>
    </th>
	</tr>
	<c:if test = "${fn:length(links) > 0}">
    <tr>
    <display:table name="links" class="tablebottom" style="width:100%" requestURI="/property/beforeDeactivateProp.do">
	<display:column property="billNumber" title="Bill Number"  sortable="true" headerClass="bluebgheadtd" class="blueborderfortd" style="width:10%;text-align:center"/>
	<display:column property="typeOfBuilding"  title="Type Of Building" headerClass="bluebgheadtd" class="blueborderfortd" style="width:17%;text-align:center" />	
	<display:column property="ownerName"  title="Owner Name"  headerClass="bluebgheadtd" class="blueborderfortd" style="width:35%;text-align:left" />
	<display:column property="propertyAddress"  title="Property Address"  headerClass="bluebgheadtd" class="blueborderfortd" style="width:40%;text-align:left"/>
	<display:column property="reasonForDeactivation"  title="Reason For Deactivation"  headerClass="bluebgheadtd" class="blueborderfortd" style="width:10%;text-align:center"/>
	<display:column property="dateOfOperation"  title="Date Of Operation"  headerClass="bluebgheadtd" class="blueborderfortd" style="width:13%;text-align:center" />
	<display:column property="userName" title="User Who Marked The Property For Deactivation" headerClass="bluebgheadtd" class="blueborderfortd" style="width:20%;text-align:center"/>
    </display:table>
    </tr>
	</c:if>
</table>
</div>

<div class="buttonbottom" align="center">
<input type="button" name="button2" id="button2" value="Close" class="button" onclick="window.close();"/>
 <input class="button" type="button"  name="button" value="List Again" tabindex="67" onclick="window.location='/ptis/reports/propMarkedForDeactive.do';" />
</div>
    

</form>
  </body>
</html>
