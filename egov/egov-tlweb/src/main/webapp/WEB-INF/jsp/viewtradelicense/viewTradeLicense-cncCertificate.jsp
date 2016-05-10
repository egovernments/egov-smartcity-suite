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
	function printLicense() {
		document.getElementById('printDiv').style.display = 'none';
		window.print();
	}

	/* function submitandclose() {
		var printcomplete = confirm(
				"Are you sure License Certificate print completed?", "YES",
				"NO");
		if (printcomplete) {
			document.getElementById('workflowBean.actionName').value = 'generatedcertificate';
			return true;
		} else {
			return false;
		}
	} */
</script>
	</head>
	<body>
		<div id="content">
			<center>
				<s:form name="certificateform" action="viewTradeLicense" theme="simple">
				<s:token/>
					<s:push value="model">
						<table width="100%" border="0" cellpadding="5" cellspacing="5" style="margin-left: 30px; font-size: 14px">
							<tr>
								<td colspan="4" align="center" style="font-size: 15px; font-weight: bolder;">
									<br />
            	                    <s:text name="tradelicense.certificate.title" />
									<s:if test="%{#parameters.duplicate}">
										<s:text name="license.certificate.watermark.duplicate" />
									</s:if>
									<br />
								</td>
								
							</tr>
							<tr>
								<td width="30%">
									<s:text name="license.applicationnumber" />
									:
								</td>
								<td width="40%">
									<b><s:property value="applicationNumber" />&nbsp;</b>
								</td>
							</tr>
							<tr>
								<td width="30%">
									<s:text name="license.license.number" />
									:
								</td>
								<td width="40%">
									<b><s:property value="licenseNumber" />&nbsp;</b>
								</td>
							</tr>
						<s:if test="%{boundary.parent.name.equalsIgnoreCase(@org.egov.tl.utils.Constants@CITY_NAME)}">
							<tr>
								<td><s:text name="license.zone" /> :</td>
								<td colspan="3"><b><s:property value="boundary.name" />&nbsp;</b>
								</td>
							</tr>
						</s:if>
						<s:else>
							<tr>
								<td width="40%"><s:text name="license.zone" /> :</td>
								<td width="40%"><b><s:property
											value="boundary.parent.name" />&nbsp;</b></td>
							</tr>
							<tr>
								<td><s:text name="license.division" /> :</td>
								<td colspan="3"><b><s:property value="boundary.name" />&nbsp;</b>
								</td>
							</tr>
						</s:else>
							<tr>
								<td>
									<s:text name="license.date" />
									:
								</td>
								<td colspan="3">
									<b> <s:date name='commencementDate' id="formattedDateOfCreation" format='dd-MMM-yyyy' /> <s:property value="%{formattedDateOfCreation}" />&nbsp;</b>
								</td>
							</tr>
							<tr>
								<td>
									Shri/ Smt:
								</td>
								<td colspan="3">
									<b><s:property value="licensee.applicantName" /> </b>
								</td>
							</tr>

							<tr>
								<td>
									S/o, D/o, W/o:
								</td>
								<td colspan="3">
									<b><s:property value="licensee.fatherOrSpouseName" /> </b>&nbsp;&nbsp;&nbsp;
								</td>
							</tr>
							<tr>
								<td colspan="6">
									<span style="width: 90%; color: black">
										<p>
											<s:text name="license.certificate.having.paid" />
											&nbsp;
											<b><s:property value="getPayableAmountInWords()" /> </b>&nbsp;
											<s:text name="license.certificate.hereby.licensed" />
											&nbsp;
											<b><s:property value="tradeName.name" /> </b>&nbsp;
											<s:text name="license.certificate.with.horse.power" />
											&nbsp;
											<b><s:property value="totalHP" /> </b>&nbsp;
											<s:text name="license.certificate.at.house.no" />
											&nbsp;
											<b><s:property value="address" /> </b>&nbsp;
										</p> &nbsp;&nbsp;</span>
								</td>
							</tr>
							<tr>
								<td colspan="5">
									<s:text name="license.certificate.for.official.year.ending" />
									<s:date name='dateOfExpiry' id="formattedDateOfExpiry" format='dd-MMM-yyyy' />
									<b> <s:property value="%{formattedDateOfExpiry}" /> </b>&nbsp;
								</td>
							</tr>
							<tr>
								<td colspan="4">
									<s:text name="license.certificate.ddno" />
									:
								</td>
							</tr>
							<tr>
								<td colspan="4">
									<s:text name="license.certificate.dddate" />
									:
								</td>
							</tr>
							<tr>
								<td colspan="4">
									<s:text name="license.certificate.ddamt" />
									:
								</td>
							</tr>
							<tr>
								<td colspan="4">
									<s:text name="license.certificate.drawon" />
									:
								</td>
							</tr>
							<tr>
								<td colspan="4">
									<table width="50%" border="1" cellspacing="0" cellpadding="0" style="font-size: 15px">
										<tr>
											<th width="50%">
												<s:text name='license.feename' />
											</th>
											<th width="50%">
												<s:text name="license.fee.amount" />
											</th>
										</tr>
										<s:hidden name="model.id" />
										<s:hidden name="workflowBean.actionName" id="workflowBean.actionName" />
											<s:iterator var="demandDetails" value="getCurrentDemand().egDemandDetails">
												<tr>
													<td style="padding-left: 12px">
														<s:property value="#demandDetails.egDemandReason.egDemandReasonMaster.reasonMaster" />
													</td>
													<td align="right" style="text-align: right; padding-right: 2px">
														<s:if test="#demandDetails.egDemandReason.egDemandReasonMaster.reasonMaster=='Deduction'">
															<s:property value="#demandDetails.amtRebate" />
														</s:if>
														<s:else>
															<s:property value="#demandDetails.amount" />
														</s:else>
													</td>
												</tr>
											</s:iterator>
										<tr>
											<td>
												<b> <s:text name="license.total.fee.amount" /> </b>
											</td>
											<td align="right" style="text-align: right; padding-right: 2px">
												<b> <s:text name="format.number">
														<s:param value="getAapplicableDemand(getCurrentDemand())" />
													</s:text> </b>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<br />
						<br />
						<table width="100%" border="0" cellpadding="5" cellspacing="5" style="margin-left: 15px; font-size: 10px">
							<tr>
								<td>
									<b><s:text name="license.inspector" /> </b>
								</td>

								<td align="right" colspan="2">
									<b><s:text name="license.zonalofficer" /> </b>
								</td>
							</tr>
						</table>

						<div align="center" id="printDiv">
							<table width="30%" align="center">
								<tr>
									<td>
										<input type="button" id="print" value="Print" onclick="return printLicense()" />
									</td>
									<%-- <s:if test="%{!#parameters.duplicate}">
										<td>
											<s:submit value="Print Complete" id="printcmplt" method="approve" onclick="return submitandclose();" />
										</td>
									</s:if> --%>
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
