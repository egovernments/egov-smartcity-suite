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
		<title><s:text name="license.certificate.title.reprint" /></title>
		<sx:head />
		<script>
			function setDuplicate(obj) {
				if (obj.value == 'duplicate') {
					document.getElementById('duplicateDiv').style.display = '';
					document.getElementById('duplicate').value = "true";
				} else {
					document.getElementById('duplicateDiv').style.display = 'none';
					document.getElementById('duplicate').value = "false";
				}
			}
			
			function validate() {
				if(document.getElementById('reprint').value=="-1"){
					bootbox.alert("Please provide the mandatory values");
					return false;
				}
				
				if (document.getElementById('duplicate').value == "true") {
					if (document.getElementById('receiptNumber').value.trim() == "" || document.getElementById('receiptDate').value.trim() == "") {
						bootbox.alert("Please provide the mandatory values");
						return false;
					}					
				}
				return true;
			
			}
			function generateCertificate()
			{
				var retType=true;
				retType=validate();
				if(retType==true)
				{
					document.forms[0].action = "${pageContext.request.contextPath}/viewtradelicense/viewTradeLicense-generateCertificate.action";
					document.forms[0].submit();
				}
			}
		</script>
	</head>
	<body>
		<table align="center" width="100%" border="0">
			<tbody>
				<tr>
					<td>
						<div align="center">
							<center>
								<div class="formmainbox">
									<div class="headingbg">
										<s:text name="license.certificate.title.reprint" />
									</div>
									<s:form action="viewTradeLicense" theme="simple" name="viewForm">
									<s:token/>
										<c:set var="trclass" value="bluebox" />
										<table width="100%" border="0" cellpadding="0" cellspacing="0">
											<tr>
												<td class="<c:out value="${trclass}"/>">
													&nbsp;
													<s:hidden name="model.id" value="%{model.id}" />
													<s:hidden name="duplicate" id="duplicate" value="false" />
												</td>
												<td class="<c:out value="${trclass}"/>" width="20%">
													<s:text name='license.certificate.reasonforreprint' />
													<span class="mandatory1">*</span>
												</td>
												<td class="<c:out value="${trclass}"/>" width="20%">
													<select name="reprint" id="reprint" onchange="setDuplicate(this)">
														<option value="-1">
															<s:text name='license.default.select' />
														</option>
														<option value="duplicate">
															Duplicate
														</option>
														<option value="modification">
															Modification
														</option>
														<option value="printissue">
															Print Issue
														</option>
													</select>
												</td>
												<td class="<c:out value="${trclass}"/>" colspan="2">
													&nbsp;
												</td>
											</tr>
											<c:choose>
												<c:when test="${trclass=='greybox'}">
													<c:set var="trclass" value="bluebox" />
												</c:when>
												<c:when test="${trclass=='bluebox'}">
													<c:set var="trclass" value="greybox" />
												</c:when>
											</c:choose>
											<tr style="display: none" id="duplicateDiv">
												<td class="<c:out value="${trclass}"/>">
													&nbsp;
												</td>
												<td class="<c:out value="${trclass}"/>">
													<s:text name="license.receiptnumber" />
													<span class="mandatory1">*</span>
												</td>
												<td class="<c:out value="${trclass}"/>">
													<s:textfield name="receiptNumber" id="receiptNumber" />
												</td>
												<td class="<c:out value="${trclass}"/>" width="20%" >
													<s:text name="license.receiptdate" />
													<span class="mandatory1">*</span>
												</td>
												<td class="<c:out value="${trclass}"/>" />
													<s:textfield name="receiptDate" id="receiptDate" onfocus="waterMarkTextIn('receiptDate','dd/mm/yyyy');" onblur="validateDateFormat(this);waterMarkTextOut('receiptDate','dd/mm/yyyy');lessThanOrEqualToCurrentDate(this);" maxlength="10" size="10" tabindex="4" onkeyup="DateFormat(this,this.value,event,false,'3')" />
													<a href="javascript:show_calendar('forms[0].receiptDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"> <img src="${pageContext.request.contextPath}/images/calendaricon.gif" alt="Date" width="18" height="18" border="0" align="absmiddle" id="calenderImgId" /> </a>
												</td>
											</tr>
										</table>
										<div align="center" class="buttonbottom">
											<table>
												<tr>
													<td>
														<input type="button" value="Print" class="button" onclick="return generateCertificate();" />
													</td>
													<td>
														<input name="button2" type="button" class="button" id="button" onclick="javascript:window.close();" value="Close" />
													</td>
												</tr>
											</table>
										</div>
									</s:form>
								</div>
							</center>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</body>
</html>
