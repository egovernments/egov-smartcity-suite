
<%@ include file="/includes/taglibs.jsp"%>
<html>
	<head>
		<title><s:text name="page.title.transfertrade" /></title>
		<script>
		  function validateForm(obj) {
		    return validateApprover(obj);
		  }
	  	</script>
	</head>
	<body>
		<table align="center" width="100%">
			<tbody>
				<tr>
					<td>
						<div align="center">
						<center>
							<div class="formmainbox">
								<div class="headingbg">
									<s:text name="page.title.transfertrade" />
								</div>
								<table>
									<tr>
										<td align="left" style="color: #FF0000"><s:actionerror
												cssStyle="color: #FF0000" /> <s:fielderror /> <s:actionmessage />
										</td>
									</tr>
								</table>
								<s:form action="transferTradeLicense" theme="css_xhtml"
									name="registrationForm" validate="true">
									<s:token />
									<s:push value="model">
										<c:set var="trclass" value="greybox" />
										<table border="0" cellpadding="0" cellspacing="0" width="100%">
											<tbody>
												<c:choose>
													<c:when test="${trclass=='greybox'}">
														<c:set var="trclass" value="bluebox" />
													</c:when>
													<c:when test="${trclass=='bluebox'}">
														<c:set var="trclass" value="greybox" />
													</c:when>
												</c:choose>
												<tr>
													<td colspan="5" class="headingwk">
														<div class="arrowiconwk">
															<img
																src="${pageContext.request.contextPath}/images/arrow.gif"
																height="20" />
														</div>
														<div class="headplacer">
															<s:text name='license.tranfer.title' />
														</div>
													</td>
												</tr>
												<tr>
													<td class="<c:out value="${trclass}"/>" width="5%">
														&nbsp;</td>
													<td class="<c:out value="${trclass}"/>"><s:text
															name="license.licensenumber" /></td>
													<td class="<c:out value="${trclass}"/>"><s:property
															value="licenseNumber" /></td>
													<td class="<c:out value="${trclass}"/>"><s:text
															name="license.tradename" /></td>
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
													<td class="<c:out value="${trclass}"/>" width="5%">
														&nbsp;</td>
													<td class="<c:out value="${trclass}"/>"><s:text
															name="licensee.current.applicantname" /></td>
													<td class="<c:out value="${trclass}"/>"><s:property
															value="licensee.applicantName" /></td>
													<td class="<c:out value="${trclass}"/>"><s:text
															name="license.applicationdate" /> <span
														class="mandatory">*</span></td>
													<s:date name='licenseTransfer.oldApplicationDate'
														id="VTL.applicationDate" format='dd/MM/yyyy' />
													<td class="<c:out value="${trclass}"/>"><s:textfield
															name="licenseTransfer.oldApplicationDate"
															id="applicationDate"
															onfocus="waterMarkTextIn('applicationDate','dd/mm/yyyy');"
															onblur="waterMarkTextOut('applicationDate','dd/mm/yyyy');"
															maxlength="10" size="10" value="%{VTL.applicationDate}"
															onkeyup="DateFormat(this,this.value,event,false,'3')" />
														<a
														href="javascript:show_calendar('forms[0].applicationDate',null,null,'DD/MM/YYYY');"
														onmouseover="window.status='Date Picker';return true;"
														onmouseout="window.status='';return true;"> <img
															src="${pageContext.request.contextPath}/images/calendaricon.gif"
															alt="Date" width="18" height="18" border="0"
															align="absmiddle" id="calenderImgId" /></td>
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
													<td class="<c:out value="${trclass}"/>" width="5%"><s:hidden
															name="licenseTransfer.id" /> <s:hidden
															name="licenseTransfer.oldApplicationNumber" /> &nbsp;</td>
													<td class="<c:out value="${trclass}"/>"><s:text
															name="licensee.applicantname" /> <span class="mandatory">*</span>
													</td>
													<td class="<c:out value="${trclass}"/>"><s:textfield
															name="licenseTransfer.oldApplicantName"
															id="applicantName" maxlength="100" /></td>
													<td class="<c:out value="${trclass}"/>"><s:text
															name="license.establishmentname" /> <span
														class="mandatory">*</span></td>
													<td class="<c:out value="${trclass}"/>"><s:textfield
															name="licenseTransfer.oldNameOfEstablishment"
															id="nameOfEstablishment" maxlength="100" /></td>
												</tr>
												<c:choose>
													<c:when test="${trclass=='greybox'}">
														<c:set var="trclass" value="bluebox" />
													</c:when>
													<c:when test="${trclass=='bluebox'}">
														<c:set var="trclass" value="greybox" />
													</c:when>
												</c:choose>
												<s:if
													test="%{licenseTransfer.boundary.name.contains('Zone')}">
													<tr>
														<td class="<c:out value="${trclass}"/>" width="5%">
															&nbsp;</td>
														<td class="<c:out value="${trclass}"/>" align="right">
															<s:text name="license.zone" /> <span class="mandatory">*</span>
														</td>
														<td class="<c:out value="${trclass}"/>" align="left">
															<s:select headerKey=""
																headerValue="%{getText('license.default.select')}"
																name="licenseeZoneId" id="licenseeZoneId"
																list="licenseZoneList" listKey="id" listValue='name'
																onChange="setupLicenseeAjaxDivision(this);"
																value="licenseTransfer.boundary.id" /> <egov:ajaxdropdown
																id="populateLicenseeDivision" fields="['Text','Value']"
																dropdownId='licenseedivision'
																url='common/commonAjax!populateDivisions.action' />
														</td>
														<td class="<c:out value="${trclass}"/>"><s:text
																name="license.division" /> <span class="mandatory">*</span>
														</td>
														<td class="<c:out value="${trclass}"/>"><s:select
																headerKey=""
																headerValue="%{getText('license.default.select')}"
																disabled="%{sDisabled}" name="licenseTransfer.boundary"
																id="licenseedivision"
																list="dropdownData.divisionListLicensee" listKey="id"
																listValue='name' value="licenseTransfer.boundary.id" />
														</td>
													</tr>
												</s:if>
												<s:else>
													<tr>
														<td class="<c:out value="${trclass}"/>" width="5%">
															&nbsp;</td>
														<td class="<c:out value="${trclass}"/>" align="right">
															<s:text name="license.zone" /> <span class="mandatory">*</span>
														</td>
														<td class="<c:out value="${trclass}"/>" align="left">
															<s:select headerKey=""
																headerValue="%{getText('license.default.select')}"
																name="licenseeZoneId" id="licenseeZoneId"
																list="dropdownData.zoneList" listKey="id"
																listValue='name'
																onChange="setupLicenseeAjaxDivision(this);"
																value="licenseTransfer.boundary.parent.id" /> <egov:ajaxdropdown
																id="populateLicenseeDivision" fields="['Text','Value']"
																dropdownId='licenseedivision'
																url='common/commonAjax!populateDivisions.action' />
														</td>
														<td class="<c:out value="${trclass}"/>"><s:text
																name="license.division" /> <span class="mandatory">*</span>
														</td>
														<td class="<c:out value="${trclass}"/>"><s:select
																headerKey=""
																headerValue="%{getText('license.default.select')}"
																disabled="%{sDisabled}" name="licenseTransfer.boundary"
																id="licenseedivision"
																list="dropdownData.divisionListLicensee" listKey="id"
																listValue='name' value="licenseTransfer.boundary.id" />
														</td>
													</tr>
												</s:else>
												<c:choose>
													<c:when test="${trclass=='greybox'}">
														<c:set var="trclass" value="bluebox" />
													</c:when>
													<c:when test="${trclass=='bluebox'}">
														<c:set var="trclass" value="greybox" />
													</c:when>
												</c:choose>
												<tr>
													<td class="<c:out value="${trclass}"/>" width="5%">
														&nbsp;</td>
													<td class="<c:out value="${trclass}"/>"><s:text
															name='license.housenumber' /> <span class="mandatory">*</span>
													</td>
													<td class="<c:out value="${trclass}"/>"><s:textfield
															name="licenseTransfer.oldAddress.houseNo" maxlength="10" />
													</td>
													<td class="<c:out value="${trclass}"/>"><s:text
															name='license.housenumber.old' /></td>
													<td class="<c:out value="${trclass}"/>"><s:textfield
															name="licenseTransfer.oldAddress.streetAddress2"
															maxlength="10" /></td>
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
													<td class="<c:out value="${trclass}"/> width="5%"></td>
													<td class="<c:out value="${trclass}"/>"><s:text
															name='license.remainingaddress' /></td>
													<td class="<c:out value="${trclass}"/>" colspan="3"><s:textarea
															name="licenseTransfer.oldAddress.streetAddress1" rows="3"
															cols="40" maxlength="500" /></td>
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
													<td class="<c:out value="${trclass}"/>" width="5%">
														&nbsp;</td>
													<td class="<c:out value="${trclass}"/>"><s:text
															name='license.pincode' /></td>
													<td class="<c:out value="${trclass}"/>"><s:textfield
															name="licenseTransfer.oldAddress.pinCode"
															onKeyPress="return numbersonly(this, event)"
															maxlength="6" /></td>
													<td class="<c:out value="${trclass}"/>"><s:text
															name='license.address.phonenumber' /></td>
													<td class="<c:out value="${trclass}"/>"><s:textfield
															name="licenseTransfer.oldPhoneNumber"
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
													<td class="<c:out value="${trclass}"/>" width="5%">
														&nbsp;</td>
													<td class="<c:out value="${trclass}"/>"><s:text
															name='licensee.homephone' /></td>
													<td class="<c:out value="${trclass}"/>"><s:textfield
															name="licenseTransfer.oldHomePhoneNumber"
															onKeyPress="return numbersonly(this, event)"
															maxlength="15" /></td>
													<td class="<c:out value="${trclass}"/>"><s:text
															name='licensee.mobilephone' /></td>
													<td class="<c:out value="${trclass}"/>"><s:textfield
															name="licenseTransfer.oldMobileNumber"
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
													<td class="<c:out value="${trclass}"/> width="5%"></td>

													<td class="<c:out value="${trclass}"/>"><s:text
															name='licensee.emailId' /></td>
													<td class="<c:out value="${trclass}"/>"><s:textfield
															name="licenseTransfer.oldEmailId"
															onBlur="validateEmail(this);checkLength(this,50)"
															maxlength="50" /></td>
													<td class="<c:out value="${trclass}"/>"><s:text
															name='licensee.uid' /></td>
													<td class="<c:out value="${trclass}"/>"><s:textfield
															name="licenseTransfer.oldUid"
															onBlur="checkLength(this,12)" maxlength="12" /></td>
												</tr>
												<tr>
													<td colspan="5"><%@ include
															file='../common/tradelicenseworkflow.jsp'%>
													</td>
												</tr>
											</tbody>
										</table>
										<div class="mandatory" style="font-size: 11px;" align="left">
											* Mandatory Fields</div>
										<div>
											<table>
												<tr class="buttonbottom" id="buttondiv"
													style="align: middle">
													<s:if test="%{roleName.contains('TLAPPROVER')}">
													<td><s:submit cssClass="buttonsubmit" value="Approve"
															id="Approve" method="edit"
															onclick="return validateForm(this);" /></td>
															</s:if>
													<td><s:submit cssClass="buttonsubmit" value="Forward"
															id="Forward" method="edit"
															onclick="return validateForm(this);" /></td>
													<td><s:submit cssClass="buttonsubmit" value="Reject"
															id="Reject" method="edit"
															onclick="return validateForm(this);" /></td>
													<td><input type="button" value="Close"
														id="closeButton" onclick="javascript: window.close();"
														class="button" /></td>
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
</body>
</html>