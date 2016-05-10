<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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
  --%>

<%@ taglib prefix="s" uri="/WEB-INF/taglib/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>

<html>
	<head>
		<script>
			function refreshInbox() {
				if (opener && opener.top.document.getElementById('inboxframe')) {
					opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
				}
			}
			function printLicense(){				
				window.print();
			}
		</script>
		<%-- <% 
			String cityUrl = (String) session.getAttribute("cityurl");
		   	CityWebsiteDAO cityWebsiteDAO = new CityWebsiteDAO();
			CityWebsite cityWebsite = cityWebsiteDAO.getCityWebSiteByURL(cityUrl);
			String cityName = cityWebsite.getCityName();
			String logoName = cityWebsite.getLogo();
		%> --%>
	</head>
	<body onload="refreshInbox();printLicense()">
		<center>
			<s:form name="scNoticeform" action="objection" theme="simple">
			<s:token/>
			<s:hidden name="workflowBean.actionName" id="workflowBean.actionName" />
					<s:hidden name="activities[0].type" value="preNotice" />
					<s:hidden name="model.id" />
				<s:push value="model">
					<table width="100%" border="0" cellpadding="5" cellspacing="5" style="margin-left: 25px font-size: 12px">
						<tr>
							<td colspan="4" align="center">
								<img src="/egi/images/<%-- <%=logoName%> --%>" width="91" height="90" />
							</td>
						</tr>
						<tr>
							<td colspan="4" align="center" style="font-size: 15px; font-weight: bolder;">
								<%-- <%=cityName%> --%>
								<br />
								<br />
							</td>
						</tr>
						<tr>
							<td colspan="4" align="center" style="font-size: 15px; font-weight: bolder;">
								<s:text name="Show Cause Notice on" />
								&nbsp;
								<b><s:property value="license.tradeName.licenseType.name" />
								</b>&nbsp;
								<br />
							</td>
						</tr>
						<s:if
							test="%{license.boundary.parent.name.equalsIgnoreCase(@org.egov.tl.utils.Constants@CITY_NAME)}">
							<tr>
								<td colspan="4">&nbsp;</td>
								<td><s:text name="license.zone" /> : &nbsp; <b><s:property
											value="license.boundary.name" /> </b></td>
							</tr>
						</s:if>
						<s:else>
							<tr>
								<td colspan="4">&nbsp;</td>
								<td><s:text name="license.division" /> :
									&nbsp; <b><s:property value="license.boundary.name" /> </b></td>
							</tr>
							<tr>
								<td colspan="4">&nbsp;</td>
								<td><s:text name="license.zone" /> : &nbsp; <b><s:property
											value="license.boundary.parent.name" /> </b></td>
							</tr>
						</s:else>
						<tr>
							<td colspan="4">
								&nbsp;
							</td>
							<td>
								<s:text name="Date" />
								: &nbsp;
								<b><%=new java.text.SimpleDateFormat("dd-MMM-yyyy").format(new java.util.Date())%></b>
							</td>
						</tr>

						<tr>
							<td colspan="4" style="font-size: 15px; font-weight: bolder;">

								<br />
								<s:text name="To" />
								,
							</td>
						</tr>
						<tr>
							<td colspan="4" style="font-size: 15px; font-weight: bolder;">
								<s:property value="name" />
							</td>
						</tr>
						<tr>
							<td colspan="4" style="font-size: 15px; font-weight: bolder;">
								<s:property value="address" />
							</td>
						</tr>
						</table>
						<table width="100%" border="0" cellpadding="5" cellspacing="5" style="margin-left: 25px font-size:12px font-size: 12px">
						<tr>
							<td colspan="4">
								<br />
								<br />
								<b><s:text name="Sub" />&nbsp;:&nbsp;<s:text name="Show Cause Notice of license for the objection Raised by" />&nbsp;<s:property value="name" />,&nbsp;<s:property value="number" />.</b>
							</td>
						</tr>
						<tr>
							<td colspan="4">
								<br />
							</td>
						</tr>
						</table>
                  <table width="100%" border="0" cellpadding="5" cellspacing="5" style="margin-left: 25px font-size: 12px">
						<tr>
							<td colspan="4">
							<s:text name="An objection raised on" />
								&nbsp;
								<b><s:property value="license.tradeName.licenseType.name" /> </b>&nbsp;
								<s:text name="issued to Shri/ Smt" />
								&nbsp;
								<s:property value="license.licensee.applicantName" />
								,&nbsp;
								<s:property value="license.licenseNumber" />
								<b> </b>&nbsp;
								<s:text name="a show cause notice has been issued for reasons stated below." />

							</td>
						</tr>
						<tr>
							<td colspan="4">
								<br />
							</td>
						</tr>
						</table>
      <table width="100%" border="0" cellpadding="5" cellspacing="5" style="margin-left: 25px font-size: 12px">
						<tr>
							<td colspan="4">
								<s:property value="details" />
							</td>
						</tr>

						<tr>
							<td colspan="4">
								&nbsp;
							</td>
							<td width="50%">
								<br />
								<b><%-- <%=cityName%> --%></b>
							</td>
						</tr>
					</table>
				</s:push>
			</s:form>
		</center>
	</body>
</html>
