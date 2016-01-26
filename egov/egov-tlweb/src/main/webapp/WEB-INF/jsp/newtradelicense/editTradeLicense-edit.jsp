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
<title><s:text name="page.title.edittrade" /></title>
<sx:head />
<script type="text/javascript"
	src="../javascript/license/tradeLicense.js"></script>
<script>
	function validateForm(obj) {
		if(validateForm_editTradeLicense()==false) {
			return false;
		} else { 
			document.forms[0].action = '/tl/newtradelicense/editTradeLicense-edit.action';
			document.forms[0].submit;
		}
	}
	function enableRentPaid(obj) {
		if (obj.value == "Rental") {
			document.getElementById("rentpaid").disabled = false;
		} else {
			document.getElementById("rentpaid").value = "";
			document.getElementById("rentpaid").disabled = true;
		}
	}
	function checkLength(obj, val) {
		if (obj.value.length > val) {
			bootbox.alert('Max ' + val + ' digits allowed')
			obj.value = obj.value.substring(0, val);
		}
	}

	function formatCurrency(obj) {
		if (obj.value == "") {
			return;
		} else {
			obj.value = (parseFloat(obj.value)).toFixed(2);
		}
	}

	function onSubmit() {
		return validateForm(this);
	}
	
</script>
</head>
<body onload="loadTableTdClass()">
		<table align="center" width="100%">
			<tbody>
				<tr>
					<td>
						<div align="center">
							<center>
								<div class="formmainbox">
									<div class="headingbg">
										<s:text name="page.title.edittrade" />
									</div>
									<table>
										<tr>
											<td align="left" style="color: #FF0000">
												<s:actionerror cssStyle="color: #FF0000" />
												<s:fielderror></s:fielderror>
												<s:actionmessage />
											</td>
										</tr>
									</table>
									<s:push value="model">
										<s:form action="editTradeLicense" theme="css_xhtml" enctype="multipart/form-data" name="editTradeLicense" validate="true">
										<s:token/>
											<s:hidden name="id" id="id" />
											<table border="0" cellpadding="0" cellspacing="0" width="100%" id="maintbl">
												<tbody>
													<tr>
														<td colspan="5" class="headingwk">
															<div class="subheadnew text-left">
																<s:text name='license.title.applicantiondetails' />
															</div>
														</td>
													</tr>
													<tr>
														<td>
														</td>
														<td>
															<s:text name="license.applicationnumber" />
														</td>
														<td>
															<s:textfield name="applicationNumber" id="applNumber" readonly="true" />
														</td>
														<td>
															<s:text name="license.applicationdate" />
														</td>
														<td>
															<s:date name="applicationDate" id="applicationDateFmtd" format="dd/MM/yyyy" />
															<s:textfield name="applicationDate" id="applicationDate" readonly="true" value="%{applicationDateFmtd}" />
														</td>
													</tr>

													<tr>
														<td>
															&nbsp;
														</td>
														<td>
															<s:text name="licensee.applicantname" />
														</td>
														<td>
															<s:textfield name="licensee.applicantName" id="applicantName" readonly="true" />
														</td>
														<td colspan="2">
															&nbsp;
														</td>
													</tr>
													<s:if test="%{licensee.boundary.name.contains('Zone')}">
													<tr>
														<td>
															&nbsp;
														</td>
														<td>
															<s:text name="license.zone" />
															<span class="mandatory1">*</span>
														</td>
														<td>
															<s:select headerKey="" headerValue="%{getText('license.default.select')}" name="licenseeZoneId" id="licenseeZoneId" list="licenseZoneList" listKey="id" listValue='name' onChange="setupLicenseeAjaxDivision(this);" value="licensee.boundary.id" />
															<egov:ajaxdropdown id="populateLicenseeDivision" fields="['Text','Value']" dropdownId='licenseedivision' url='domain/commonAjax-populateDivisions.action' />
														</td>
														<td>
															<s:text name="license.division" />
															
														</td>
														<td>
															<s:select headerKey="" headerValue="%{getText('license.default.select')}" disabled="%{sDisabled}" name="licensee.boundary" id="licenseedivision" list="dropdownData.divisionListLicensee" listKey="id" listValue='name' value="licensee.boundary.id" />

														</td>
													</tr>
													</s:if>
													<s:else>
													<tr>
														<td>
															&nbsp;
														</td>
														<td>
															<s:text name="license.zone" />
															<span class="mandatory1">*</span>
														</td>
														<td>
															<s:select headerKey="" headerValue="%{getText('license.default.select')}" name="licenseeZoneId" id="licenseeZoneId" list="dropdownData.zoneList" listKey="id" listValue='name' onChange="setupLicenseeAjaxDivision(this);" value="licensee.boundary.parent.id" />
															<egov:ajaxdropdown id="populateLicenseeDivision" fields="['Text','Value']" dropdownId='licenseedivision' url='domain/commonAjax-populateDivisions.action' />
														</td>
														<td>
															<s:text name="license.division" />
															
														</td>
														<td>
															<s:select headerKey="" headerValue="%{getText('license.default.select')}" disabled="%{sDisabled}" name="licensee.boundary" id="licenseedivision" list="dropdownData.divisionListLicensee" listKey="id" listValue='name' value="licensee.boundary.id" />

														</td>
													</tr>
													</s:else>
													<tr>
														<td>
															&nbsp;
														</td>
														<td>
															<s:text name='license.housenumber' />
															<span class="mandatory1">*</span>
														</td>
														<td>
															<s:textfield name="address.houseNoBldgApt" maxlength="10"/>
														</td>
														<td colspan="2">
															&nbsp;
														</td>
													</tr>
													<tr>
														<td>
															&nbsp;
														</td>
														<td><s:text name="license.remainingaddress" />
														</td>
														<td>
															<s:property value="address.streetRoadLine" default="N/A"/>
														</td>
														<td>
															<s:text name='license.pincode' />
														</td>
														<td>
															<s:textfield name="address.pinCode" onKeyPress="return numbersonly(this, event)" maxlength="6"/>
														</td>
														
														</tr>
													<tr>
														<td>
															&nbsp;
														</td>
														<td>
															<s:text name='license.address.phonenumber' />
														</td>
														<td>
															<s:textfield name="phoneNumber" onKeyPress="return numbersonly(this, event)" maxlength="15"/>
														</td>
														<td>
															<s:text name='license.ownbuilding' />
															<span class="mandatory1">*</span>
														</td>
														<td>
															<s:radio name="buildingType" list="buildingTypeList" id="buildingtype" onchange="enableRentPaid(this)" value="buildingType" />
														</td>
														
													</tr>
													<tr>
														<td>
														&nbsp;
														</td>
														<td>
															<s:text name="license.rentpaid" />
														</td>
														<td>
															<s:textfield name="rentPaid" size="20" id="rentpaid" disabled="true" onKeyPress="return numbersforamount(this, event)" onBlur="checkLength(this,6),formatCurrency(rentPaid)" />
														</td>
														<td>
															<s:text name='license.numberofrooms' />
														</td>
														<td>
															<s:textfield name="noOfRooms" id="license.noOfRooms" onKeyPress="return numbersonly(this, event)" maxlength="3"/>
														</td>
														
													</tr>
													
													<tr>
													<td>
														&nbsp;
														</td>
													<td>
															<s:text name="license.remarks" />
														</td>
														<td>
															<s:textarea name="remarks" rows="3" cols="25" maxlength="500" />
														</td>
														<td class="<c:out value="${trclass}"/>" colspan="2"></td>
														</tr>
													<tr>
														<td colspan="5" class="headingwk">
															<div class="subheadnew text-left">
																<s:text name='license.title.applicantdetails' />
															</div>
														</td>
													</tr>
													<tr style="display: none"></tr>
													<s:if test="%{boundary.name.contains('Zone')}">
													<tr>
														<td width="5%">
														</td>
														<td>
															<s:text name="license.zone" /><span class="mandatory1">*</span>
														</td>
														<td align="left">
															<s:select headerKey="" headerValue="%{getText('license.default.select')}" name="licenseZoneId" id="licenseZoneId" list="licenseZoneList" listKey="id" listValue='name' onChange="setupAjaxDivision(this);" value="boundary.id" />
															<egov:ajaxdropdown id="populateDivision" fields="['Text','Value']" dropdownId='division' url='domain/commonAjax-populateDivisions.action' />
														</td>
														<td>
															<s:text name="license.division" />
														</td>
														<td>
															<s:select headerKey="" headerValue="%{getText('license.default.select')}" disabled="%{sDisabled}" name="boundary" id="division" list="dropdownData.divisionListLicense" listKey="id" listValue='name' value="boundary.id" />

														</td>
													</tr>
													</s:if>
													<s:else>
													<tr>
														<td width="5%">
														</td>
														<td>
															<s:text name="license.zone" /><span class="mandatory1">*</span>
														</td>
														<td align="left">
															<s:select headerKey="" headerValue="%{getText('license.default.select')}" name="licenseZoneId" id="licenseZoneId" list="dropdownData.zoneList" listKey="id" listValue='name' onChange="setupAjaxDivision(this);" value="boundary.parent.id" />
															<egov:ajaxdropdown id="populateDivision" fields="['Text','Value']" dropdownId='division' url='domain/commonAjax-populateDivisions.action' />
														</td>
														<td>
															<s:text name="license.division" />
														</td>
														<td>
															<s:select headerKey="" headerValue="%{getText('license.default.select')}" disabled="%{sDisabled}" name="boundary" id="division" list="dropdownData.divisionListLicense" listKey="id" listValue='name' value="boundary.id" />

														</td>
													</tr>
													</s:else>
													<tr>
														<td>
															&nbsp;
														</td>
														<td>
															<s:text name='license.housenumber' />
															<span class="mandatory1">*</span>
														</td>
														<td>
															<s:textfield name="licensee.address.houseNoBldgApt" maxlength="10"/>
														</td colspan="2">
															&nbsp;														
														<td>
													</tr>
													<tr>
														<td>
															&nbsp;
														</td>
															<td><s:text name="licensee.remainingaddress" />
														</td>
														<td>
															<s:property value="licensee.address.streetRoadLine" default="N/A"/>
														</td>
														<td>
															<s:text name='license.pincode' />
														</td>
														<td>
															<s:textfield name="licensee.address.pinCode" onKeyPress="return numbersonly(this, event)" maxlength="6" />
														</td>
													</tr>
													<tr>
													<td>
															&nbsp;
														</td>
													<td>
															<s:text name='licensee.homephone' />
														</td>
														<td>
															<s:textfield name="licensee.phoneNumber" onKeyPress="return numbersonly(this, event)" maxlength="15"/>
														</td>
														<td class="<c:out value="${trclass}"/>" colspan="2"></td>
														</tr>
													<tr>
														<td  width="5%"></td>
														<td>
															<s:text name="licensee.mobilephone"/>
														</td>
														<td>
														<s:textfield name="licensee.mobilePhoneNumber" onKeyPress="return numbersonly(this, event)" maxlength="15"/>
														</td>
														<td>
														<s:text name="licensee.emailId"></s:text>
														</td>
														<td ><s:textfield    name="licensee.emailId" value="%{licensee.emailId}" onBlur="validateEmail(this);checkLength(this,50)" maxlength="50"/></td>
													</tr>
													
													<tr>	    
														<td width="5%"></td>
														<td><s:text name='licensee.uid' /></td>
														<td colspan="3"> <s:textfield name="licensee.uid"  value="%{licensee.uid}" onBlur="checkLength(this,12)" maxlength="12"/></td>
													</tr>
													<tr>
														<td colspan="5" class="headingwk">
															<div class="subheadnew text-left">
																<s:text name='license.title.tradedetail' />
															</div>
														</td>
													</tr>
													<tr style="display: none"></tr>
													<tr>
														<td>
															&nbsp;
														</td>
														<td>
															<s:text name="license.tradename" />
														</td>
														<td>
															<s:textfield name="tradeName.name" id="tradeName" readonly="true" />
														</td>
														<td>
															<s:text name="license.establishmentname" />
															<span class="mandatory1">*</span>
														</td>
														<td>
															<s:textfield name="nameOfEstablishment" id="establishmentName" onblur="trimAll(this.value);" maxlength="100"/>
														</td>
													</tr>
													<s:if test="%{#attr.hotelGrade != null}">
													<tr>
														<td>
															&nbsp;
														</td>
														<td>
															<s:text name="license.hotel.grade" />
														</td>
														<td colspan="3">
															<s:textfield name="hotelGrade" readonly="true" />
														</td>
													</tr>
													</s:if>
													<tr>
														<td>
															&nbsp;
														</td>
														<td>
															<s:text name="license.motor.installed" />
														</td>
														<td>
															<s:checkbox name="motorInstalled" id="motorInstalled" disabled="true" />
														</td>
														<td>
															&nbsp;
														</td>
														<td>
															&nbsp;
														</td>
													</tr>
													<s:if test="%{#attr.isOldLicense == false && motorInstalled}">
														<tr>
															<td colspan="5" class="headingwk">
																<div class="subheadnew text-left">
																	<s:text name='license.title.motordetail' />
																</div>
															</td>
														</tr>
														<tr style="display: none"></tr>
														<tr>
															<td colspan="5">
																<table cellspacing="0" border="1" width="35%" style="background-color: white">
																	<thead>
																		<tr>
																			<th style="font-size: 10px;">
																				<s:text name='license.horsepower' />
																			</th>
																			<th style="font-size: 10px;">
																				<s:text name='license.noofmotorinstalled' />
																			</th>
																		</tr>
																	</thead>
																	<tbody>
																		<c:forEach items="${installedMotorList}" var="motor" varStatus="status">
																			<tr>
																				<td style="text-align: center">
																					${motor.hp}
																				</td>
																				<td style="text-align: center">
																					${motor.noOfMachines}
																				</td>
																			</tr>
																		</c:forEach>
																	</tbody>
																</table>
															</td>
														</tr>
													</s:if>
													<tr>
													    <td colspan="5">
															<%@ include file="../common/documentUpload.jsp" %>
														</td>
													</tr>
													</div>
													<s:if test="%{#attr.isOldLicense == false}">
													    <tr>
													    	<td colspan="5">
																<c:set value="bluebox" var="trclass" />
																<%@ include file='../common/commonWorkflowMatrix.jsp'%>
																<%@ include file='../common/commonWorkflowMatrix-button.jsp'%>
															</td>
														</tr>
													</s:if>
												</tbody>
											</table>
											<div class="mandatory1" style="font-size: 11px;" align="left">
												* Mandatory Fields
											</div>
										</s:form>
									</s:push>
								</div>
							</center>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</body>
</html>
