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

<%@ taglib prefix="s" uri="/WEB-INF/taglib/struts-tags.tld" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp"%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<html>
	<head>
		<title><s:text name="BigBuildingRec.title"/></title>
		<script type="text/javascript">
		</script>
	</head>
	<body>
		<div align="left">
  			<s:actionerror/>
  		</div>
		<s:form action="bigBuildingRecoveryReport" theme="simple" validate="true">
		<div class="formmainbox">
			<div class="formheading"></div>
			<div class="headingbg"><s:text name="BigBuildingRec.title"/></div>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="greybox">&nbsp;</td>
					<td class="greybox"><s:text name="fromDate"/><span class="mandatory1">*</span> :</td>
					<td class="greybox">
						<s:textfield name="fromDate" id="fromDate" maxlength="10"
							onkeyup="DateFormat(this,this.value,event,false,'3')" size="10" onblur="validateDateFormat(this);"/>
					</td>
					<td class="greybox">&nbsp;</td>
				</tr>
				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox"><s:text name="toDate"/><span class="mandatory1">*</span> :</td>
					<td class="bluebox">
						<s:textfield name="toDate" id="toDate" maxlength="10"
							onkeyup="DateFormat(this,this.value,event,false,'3')" size="10" onblur="validateDateFormat(this);"/>
					</td>
					<td class="bluebox" colspan="2">&nbsp;</td>
				</tr>
	</table>
		<tr>
        	<font size="2"><div align="left" class="mandatory"><s:text name="mandtryFlds"/></div></font>
        </tr>
	</div>
	<div class="buttonbottom" align="center">
		<tr>
		 <td><s:submit name="button32" type="submit" cssClass="buttonsubmit" id="button32" value="Search" method="generateBigBldgRecStmt"/></td>
		 <td><input type="button" name="button2" id="button2" value="Close" class="button" onclick="window.close();"/></td>
		</tr>
	</div>
	</s:form>
			
	</body>
</html>
