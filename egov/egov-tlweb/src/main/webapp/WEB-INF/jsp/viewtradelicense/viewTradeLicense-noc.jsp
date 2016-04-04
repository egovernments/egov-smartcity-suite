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
<%@ include file="/includes/taglibs.jsp"%>
<html>
<head>
<title><s:text name="page.title.viewtrade" /></title>
<sx:head />
<script>
	function closethis() {
		if (confirm("Do you want to close this window ?")) {
			window.close();
		}
	}

	function validateForm(obj) {
		document.getElementById("error").innerHTML = '';
		if (document.getElementById("sandBuckets").value == '') {
			document.getElementById("error").innerHTML = '<ul><li><span class="errorMessage" style="color: #FF0000">Please enter No. Of Sand Buckets</li></ul>';
			return false;
		} else if (document.getElementById("waterBuckets").value == '') {
			document.getElementById("error").innerHTML = '<ul><li><span class="errorMessage" style="color: #FF0000">Please enter No. Of Water Buckets</li></ul>';
			return false;
		} else if (document.getElementById("dcpExtinguisher").value == '') {
			document.getElementById("error").innerHTML = '<ul><li><span class="errorMessage" style="color: #FF0000">Please enter No. Of DCP Extinguisher</li></ul>';
			return false;
		} else {
			return true;
		}
	}
</script>
</head>
<body>
	<div id="content">
		<table align="center" width="100%">
			<tbody>
				<tr>
					<td>
						<div align="center">
							<center>
								<div class="formmainbox">
									<div class="headingbg" id="headingdiv">
										<s:text name="page.title.viewtrade" />
									</div>
									<table>
										<tr>
											<td align="left" style="color: #FF0000"><s:actionerror
													cssStyle="color: #FF0000" /> <s:fielderror /> <s:actionmessage />
											</td>
										</tr>
									</table>
									<s:form action="viewTradeLicense" theme="css_xhtml"
										name="viewForm">
										<s:push value="model">
											<s:hidden name="id" id="id" />
											<s:hidden name="actionName" value="create" />
											<div id="error" style="color: #FF0000"></div>
											<c:set var="trclass" value="greybox" />
											<table width="100%">
												<c:choose>
													<c:when test="${trclass=='greybox'}">
														<c:set var="trclass" value="bluebox" />
													</c:when>
													<c:when test="${trclass=='bluebox'}">
														<c:set var="trclass" value="greybox" />
													</c:when>
												</c:choose>
												<tr>
													<td class="<c:out value="${trclass}"/>" width="5%">&nbsp;</td>
													<td class="<c:out value="${trclass}"/>"><b><s:text
																name='license.licensenumber' /> </b></td>
													<td class="<c:out value="${trclass}"/>"><s:property
															value="licenseNumber" /></td>
													<td class="<c:out value="${trclass}"/>"><b><s:text
																name="license.applicationnumber" /> </b></td>
													<td class="<c:out value="${trclass}"/>"><s:property
															value="applicationNumber" /></td>
												</tr>

												<c:choose>
													<c:when test="${trclass=='greybox'}">
														<c:set var="trclass" value="bluebox" />
													</c:when>
													<c:when test="${trclass=='bluebox'}">
														<c:set var="trclass" value="greybox" />
													</c:when>
												</c:choose>
												<tr>
													<td class="<c:out value="${trclass}"/>" width="5%">&nbsp;</td>
													<td class="<c:out value="${trclass}"/>"><b><s:text
																name="license.applicationdate" /> </b></td>
													<s:date name='applicationDate' id="VTL.applicationDate"
														format='dd/MM/yyyy' />
													<td class="<c:out value="${trclass}"/>"><s:property
															value="%{VTL.applicationDate}" /></td>
													<td class="<c:out value="${trclass}"/>"><b><s:text
																name="license.tradename" /> </b></td>
													<td class="<c:out value="${trclass}"/>"><s:property
															value="tradeName.name" /></td>
												</tr>
												<c:choose>
													<c:when test="${trclass=='greybox'}">
														<c:set var="trclass" value="bluebox" />
													</c:when>
													<c:when test="${trclass=='bluebox'}">
														<c:set var="trclass" value="greybox" />
													</c:when>
												</c:choose>
												<tr>
													<td class="<c:out value="${trclass}"/>" width="5%">&nbsp;</td>
													<td class="<c:out value="${trclass}"/>"><b><s:text
																name="licensee.applicantname" /> </b></td>
													<td class="<c:out value="${trclass}"/>"><s:property
															value="licensee.applicantName" /></td>
													<td class="<c:out value="${trclass}"/>"><b><s:text
																name="licensee.nationality" /> </b></td>
													<td class="<c:out value="${trclass}"/>"><s:property
															value="licensee.nationality" /></td>
												</tr>

												<c:choose>
													<c:when test="${trclass=='greybox'}">
														<c:set var="trclass" value="bluebox" />
													</c:when>
													<c:when test="${trclass=='bluebox'}">
														<c:set var="trclass" value="greybox" />
													</c:when>
												</c:choose>
												<tr>
													<td class="<c:out value="${trclass}"/>" width="5%">&nbsp;</td>
													<td class="<c:out value="${trclass}"/>"><b><s:text
																name="license.sand.buckets" /> </b><span class="mandatory1">*</span></td>
													<td class="<c:out value="${trclass}"/>"><s:textfield
															name="sandBuckets" id="sandBuckets"
															onKeyPress="return numbersonly(this, event)"
															maxlength="15" /></td>
													<td class="<c:out value="${trclass}"/>"><b><s:text
																name="license.water.buckets" /> </b><span class="mandatory1">*</span></td>
													<td class="<c:out value="${trclass}"/>"><s:textfield
															name="waterBuckets" id="waterBuckets"
															onKeyPress="return numbersonly(this, event)"
															maxlength="15" /></td>
												</tr>
												<c:choose>
													<c:when test="${trclass=='greybox'}">
														<c:set var="trclass" value="bluebox" />
													</c:when>
													<c:when test="${trclass=='bluebox'}">
														<c:set var="trclass" value="greybox" />
													</c:when>
												</c:choose>
												<tr>
													<td class="<c:out value="${trclass}"/>" width="5%">&nbsp;</td>
													<td class="<c:out value="${trclass}"/>"><b><s:text
																name="license.dcp.extinguisher" /> </b><span
														class="mandatory1">*</span></td>
													<td colspan="3" class="<c:out value="${trclass}"/>"><s:textfield
															name="dcpExtinguisher" id="dcpExtinguisher"
															onKeyPress="return numbersonly(this, event)"
															maxlength="15" /></td>
												</tr>
											</table>
											<div align="center" class="buttonbottom" id="buttondiv">
												<table>
													<tr>
														<td><s:submit type="submit" cssClass="buttonsubmit"
																value="Create NOC" id="createNoc" method="createNoc"
																align="center" onclick="return validateForm(this);" /></td>
														<td><input name="button2" type="button"
															class="button" onclick="closethis()" value="Close" /></td>
													</tr>
												</table>
											</div>
										</s:push>
									</s:form>
								</div>
							</center>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</body>
</html>
