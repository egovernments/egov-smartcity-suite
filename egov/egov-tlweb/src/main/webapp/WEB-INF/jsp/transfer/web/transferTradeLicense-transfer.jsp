<!-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#     accountability and the service delivery of the government  organizations.
#  
#      Copyright (C) <2015>  eGovernments Foundation
#  
#      The updated version of eGov suite of products as by eGovernments Foundation 
#      is available at http://www.egovernments.org
#  
#      This program is free software: you can redistribute it and/or modify
#      it under the terms of the GNU General Public License as published by
#      the Free Software Foundation, either version 3 of the License, or
#      any later version.
#  
#      This program is distributed in the hope that it will be useful,
#      but WITHOUT ANY WARRANTY; without even the implied warranty of
#      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#      GNU General Public License for more details.
#  
#      You should have received a copy of the GNU General Public License
#      along with this program. If not, see http://www.gnu.org/licenses/ or 
#      http://www.gnu.org/licenses/gpl.html .
#  
#      In addition to the terms of the GPL license to be adhered to in using this
#      program, the following additional terms are to be complied with:
#  
#  	1) All versions of this program, verbatim or modified must carry this 
#  	   Legal Notice.
#  
#  	2) Any misrepresentation of the origin of the material is prohibited. It 
#  	   is required that all modified versions of this material be marked in 
#  	   reasonable ways as different from the original version.
#  
#  	3) This license does not grant any rights to any user of the program 
#  	   with regards to rights under trademark law for use of the trade names 
#  	   or trademarks of eGovernments Foundation.
#  
#    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------->
<%@ include file="/includes/taglibs.jsp"%>
<html>
<head>
<title><s:text name="page.title.transfertrade" /></title>
<sx:head />
<script>
		  	function validateForm(obj) {
			    if (validateApprover(obj) == false) {
			      return false;
			    } else {
			      clearWaterMark();
			      return true;
			    }
			  }
		
			function checkLength(obj,val){
				if(obj.value.length>val) {
					alert('Max '+val+' digits allowed')
					obj.value = obj.value.substring(0,val);
				}
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
															value="tradeName.name" /> <s:hidden name="license" /></td>
												</tr>
												<c:choose>
													<c:when test="${trclass=='greybox'}">
														<c:set var="trclass" value="bluebox" />
													</c:when>
													<c:when test="${trclass=='bluebox'}">
														<c:set var="trclass" value="greybox" />
													</c:when>
												</c:choose>
												<c:choose>
													<c:when test="${hotelGrade!=null && hotelGrade!=''}">
														<td class="<c:out value="${trclass}"/>">&nbsp;</td>
														<td class="<c:out value="${trclass}"/>"><s:text
																name="license.hotel.grade" /></td>
														<td class="<c:out value="${trclass}"/>" colspan="3">
															<s:property value="hotelGrade" />
														</td>
													</c:when>
												</c:choose>

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
															id="applicationDate" value="%{applicationDate}"
															onfocus="waterMarkTextIn('applicationDate','dd/mm/yyyy');"
															onblur="waterMarkTextOut('applicationDate','dd/mm/yyyy');lessThanOrEqualToCurrentDate(this)"
															maxlength="10" size="10"
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
													<td class="<c:out value="${trclass}"/>" width="5%">
														&nbsp;</td>
													<td class="<c:out value="${trclass}"/>"><s:text
															name="licensee.applicantname" /> <span class="mandatory">*</span>
													</td>
													<td class="<c:out value="${trclass}"/>"><s:textfield
															name="licenseTransfer.oldApplicantName"
															id="applicantName" value="%{licensee.applicantName}"
															maxlength="100" /></td>
													<td class="<c:out value="${trclass}"/>"><s:text
															name="license.establishmentname" /> <span
														class="mandatory">*</span></td>
													<td class="<c:out value="${trclass}"/>"><s:textfield
															name="licenseTransfer.oldNameOfEstablishment"
															id="nameOfEstablishment" maxlength="100"
															value="%{nameOfEstablishment}" /></td>
												</tr>
												<c:choose>
													<c:when test="${trclass=='greybox'}">
														<c:set var="trclass" value="bluebox" />
													</c:when>
													<c:when test="${trclass=='bluebox'}">
														<c:set var="trclass" value="greybox" />
													</c:when>
												</c:choose>
												<s:if test="%{licensee.boundary.name.contains('Zone')}">
													<tr>
														<td class="<c:out value="${trclass}"/>" width="5%">&nbsp;</td>
														<td class="<c:out value="${trclass}"/>" align="right"><s:text
																name="license.zone" /><span class="mandatory">*</span>
														</td>
														<td class="<c:out value="${trclass}"/>" align="left"><s:select
																headerKey=""
																headerValue="%{getText('license.default.select')}"
																name="licenseeZoneId" id="licenseeZoneId"
																list="licenseZoneList" listKey="id" listValue='name'
																value="licensee.boundary.id"
																onChange="setupLicenseeAjaxDivision(this);" /> <egov:ajaxdropdown
																id="populateLicenseeDivision" fields="['Text','Value']"
																dropdownId='licenseedivision'
																url='web/commonAjax!populateDivisions.action' /></td>
														<td class="<c:out value="${trclass}"/>"><s:text
																name="license.division" /></td>
														<td class="<c:out value="${trclass}"/>"><s:select
																headerKey=""
																headerValue="%{getText('license.default.select')}"
																disabled="%{sDisabled}" name="licenseTransfer.boundary"
																id="licenseedivision"
																list="dropdownData.divisionListLicensee" listKey="id"
																listValue='name' value="%{licensee.boundary.id}"
																onChange="setupAjaxArea(this);" /> <egov:ajaxdropdown
																id="populateLicenseeArea" fields="['Text','Value']"
																dropdownId='licenseeArea'
																url='web/commonAjax!populateAreas.action' /></td>
													</tr>
												</s:if>
												<s:else>
													<tr>
														<td class="<c:out value="${trclass}"/>" width="5%">&nbsp;</td>
														<td class="<c:out value="${trclass}"/>" align="right"><s:text
																name="license.zone" /><span class="mandatory">*</span>
														</td>
														<td class="<c:out value="${trclass}"/>" align="left"><s:select
																headerKey=""
																headerValue="%{getText('license.default.select')}"
																name="licenseeZoneId" id="licenseeZoneId"
																list="dropdownData.zoneList" listKey="id"
																listValue='name'
																onChange="setupLicenseeAjaxDivision(this);" /> <egov:ajaxdropdown
																id="populateLicenseeDivision" fields="['Text','Value']"
																dropdownId='licenseedivision'
																url='web/commonAjax!populateDivisions.action' /></td>
														<td class="<c:out value="${trclass}"/>"><s:text
																name="license.division" /></td>
														<td class="<c:out value="${trclass}"/>"><s:select
																headerKey=""
																headerValue="%{getText('license.default.select')}"
																disabled="%{sDisabled}" name="licenseTransfer.boundary"
																id="licenseedivision"
																list="dropdownData.divisionListLicensee" listKey="id"
																listValue='name' value="%{licensee.boundary.id}"
																onChange="setupAjaxArea(this);" /> <egov:ajaxdropdown
																id="populateLicenseeArea" fields="['Text','Value']"
																dropdownId='licenseeArea'
																url='web/commonAjax!populateAreas.action' /></td>
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
															name="licenseTransfer.oldAddress.houseNo"
															value="%{licensee.address.houseNo}" maxlength="10" /></td>
													<td class="<c:out value="${trclass}"/>"><s:text
															name='license.housenumber.old' /></td>
													<td class="<c:out value="${trclass}"/>">
														<s:textfield name="licenseTransfer.oldAddress.streetAddress2" maxlength="10" value="%{licensee.address.streetAddress2}"/></td>
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
													<td class="<c:out value="${trclass}"/> width="5%">
													<td class="<c:out value="${trclass}"/>"><s:text
															name='license.remainingaddress' /></td>
													<td class="<c:out value="${trclass}"/>" colspan="3"><s:textarea
															name="licenseTransfer.oldAddress.streetAddress1"
															value="%{licensee.address.streetAddress1}" rows="3"
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
															onBlur="checkLength(this,6)"
															value="%{licensee.address.pinCode}" maxlength="6" /></td>
													<td class="<c:out value="${trclass}"/>"><s:text
															name='license.address.phonenumber' /></td>
													<td class="<c:out value="${trclass}"/>"><s:textfield
															name="licenseTransfer.oldPhoneNumber"
															value="%{phoneNumber}"
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
															value="%{licensee.phoneNumber}"
															onKeyPress="return numbersonly(this, event)"
															maxlength="15" /></td>
													<td class="<c:out value="${trclass}"/>"><s:text
															name='licensee.mobilephone' /></td>
													<td class="<c:out value="${trclass}"/>"><s:textfield
															name="licenseTransfer.oldMobileNumber"
															value="%{licensee.mobilePhoneNumber}"
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
															value="%{licensee.emailId}"
															onBlur="validateEmail(this);checkLength(this,50)"
															maxlength="50" /></td>
													<td class="<c:out value="${trclass}"/>"><s:text
															name='licensee.uid' /></td>
													<td class="<c:out value="${trclass}"/>"><s:textfield
															name="licenseTransfer.oldUid" value="%{licensee.uid}"
															onBlur="checkLength(this,12)" maxlength="12" /></td>
												</tr>
												<tr>
													<td colspan="5"><%@ include
															file='../../common/tradelicenseworkflow.jsp'%>
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
															id="Approve" method="create"
															onclick="return validateForm(this);" /></td>
															</s:if>
													<td><s:submit cssClass="buttonsubmit" value="Forward"
															id="Forward" method="create"
															onclick="return validateForm(this);" /></td>
													<td><s:submit type="submit" cssClass="buttonsubmit"
															value="Save" id="Save" method="create"
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
