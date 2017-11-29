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

<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/includes/taglibs.jsp" %>


<html>
<head>
	<title><s:text name="chPropAdd" /></title>	
</head>
	<body onload=" refreshParentInbox();">
	<s:form name="deactivate" >
		<s:token />
		<table border="0" width="100%" cellpadding="0" cellspacing="0">
			<div class="formmainbox">
			  <div class="formheading"></div>
					<div class="headingbg"><s:text name="chPropAddAck"/></div>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="bluebox">
							&nbsp;&nbsp;&nbsp;
						</td>
					</tr>
					<tr>
				        <td colspan="5" style="background-color: #FDF7F0;font-size: 13px;" align="center">
				        	<span class="bold"> <s:property value="ackMessage"/>  </span>			        					        					        		
				       </td>
					</tr>
					<tr>
						<td class="bluebox">
							&nbsp;&nbsp;&nbsp;
						</td>
					</tr>
					<tr style="background-color:#E4E4E4; padding-top: 5px; padding-bottom: 5px" height="40px">
				        <td colspan="5" align="center">
				        	<input type="button" class="button" value="Close" onclick="window.close()">
				       </td>
					</tr>
					</table>
			</div>
		</table>
	</s:form>
	</body>
</html>
