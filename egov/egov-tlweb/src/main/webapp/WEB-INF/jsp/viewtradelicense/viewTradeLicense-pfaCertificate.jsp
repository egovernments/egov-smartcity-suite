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
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<html>
	<head>
		<title>Trade License Certificate</title>
		<script>
		function printDiv(divName) {
		    var printContents = document.getElementById(divName).innerHTML;
		    var originalContents = document.body.innerHTML;
		    document.body.innerHTML = printContents;
		    window.print();
		    document.body.innerHTML = originalContents;
		}
		</script>
	</head>
	<body onload="refreshInbox()">
		<div id="content">
			<center>
				<s:form name="certificateform" action="viewTradeLicense" theme="simple">
				<s:token/>
					<s:push value="model">
					<div id="main">
						<table width="100%" border="0" cellpadding="3" cellspacing="3" style="margin-left: 14px; margin-right: 14px; font-size: 13px">
							<tr>
								<td colspan="4" align="center">
									<img src="/egi/images/<%-- <%=logoName%> --%>" width="91" height="90" />
								</td>
							</tr>
							<tr>
								<td colspan="4" align="center" style="font-size: 15px; font-weight: bolder;">
									<%-- <%=cityName%> --%>
									<br />
									<br/>
									<s:text name="license.pfacertificate.publicHealthDepartment" />
									<br />
									<%-- <s:if test="%{#parameters.duplicate}">
										<s:text name="license.certificate.watermark.duplicate" />
									</s:if> --%>
									<br />

								</td>
							</tr>
							<tr>
								<td>
									<s:text name="license.pfacertificate.title" />
								</td>
							</tr>
							<tr>
								<td>
									<table>
										<tr>
											<td>
												<s:text name="license.license.number" />
											</td>
											<td>
												:
											</td>
											<td>
												<b><s:property value="licenseNumber" />&nbsp;</b>
											</td>

										</tr>

										<tr>
											<td>
												<s:text name="licence.pfacertificate.applicantName" />
											</td>
											<td>
												:
											</td>
											<td>
												<b><s:property value="licensee.applicantName" /> </b>
											</td>
										</tr>
										<tr>
											<td>
												<s:text name="licensee.pfacertificate.residentialaddress" />
											</td>
											<td>
												:
											</td>
											<td>
												<b><s:property value="licensee.address" /> </b>
											</td>

										</tr>
										<tr>
											<td>
												<s:text name="license.establishmentname" />
											</td>
											<td>
												:
											</td>
											<td>
												<b><s:property value="nameOfEstablishment" />
												</b>
											</td>
										</tr>
										<tr>
											<td>
												<s:text name="licensee.pfacertificate.completeaddress" />
											</td>
											<td>
												:
											</td>
											<td>
												<b><s:property value="address" />
												</b>
											</td>
										</tr>
									</table>
								</td>
							</tr>

							<tr>
								<td colspan="4" align="center" style="font-size: 15px; font-weight: bolder;">
									<br />
									<s:text name="license.pfacertificate.license" />
								</td>
							</tr>
							<tr>
								<td colspan="4" align="center" style="font-size: 15px; font-weight: bolder;">
									<s:text name="license.pfacertificate.formB" />
								</td>
							</tr>
							<tr>
								<td align="center">
									<s:text name="license.pfacertificate.seerule" />
								</td>
							</tr>
							<tr>
								<td>
									<s:text name="license.pfacertificate.shrismt" />
									&nbsp;
									<b><s:property value="licensee.applicantName" /> </b>
									<s:text name="license.pfacertificate.residingat" />
									&nbsp;
									<b> <s:property value="licensee.address" /> </b>
									<s:text name="license.pfacertificate.ishereby" />
									<b>&nbsp;<s:property value="tradeName.name" />
									</b> &nbsp;
									<s:text name="license.pfacertificate.herein" />
									&nbsp;
									<b><s:property value="nameOfEstablishment" />
									</b>
									<s:text name="license.pfacertificate.situatedin" />
									<b><s:property value="address" /></b>
								</td>
							</tr>
							<tr>
								<td align="justify">
									<s:text name="license.pfacertificate.content1" />
									<b>&nbsp;<s:property value="tradeName.name" />
									</b>&nbsp;
									<s:text name="license.pfacertificate.content2" />
								</td>
							</tr>
							<tr>
								<td align="right">
									<br />
									<br />
									<s:text name="license.pfacertificate.healthOfficer" />
								</td>
							</tr>
							<tr>
								<td align="right">
									<s:text name="license.pfacertificate.licensingauthority" />
								</td>
							</tr>
						</table>
					</div>	
						<s:hidden name="model.id" />
						<s:hidden name="workflowBean.actionName" id="workflowBean.actionName" />
						<div align="center" id="printDiv">
							<table width="30%" align="center">
								<tr>
									<td>
										<input type="button" id="print" value="Print" onclick="printDiv('main')" />
									</td>
									<td>
										<input type="button" id="close" value="Close" onclick="javascript:window.close();" />
									</td>
								</tr>
							</table>
						</div>
					</s:push>
				</s:form>
			</center>
		</div>
	</body>
</html>
