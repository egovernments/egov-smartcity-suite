<%@ include file="/includes/taglibs.jsp"%>
<html>
	<head>
		<title><s:text name="page.title.edittrade" /></title>
		<script type="text/javascript" src="../javascript/license/tradeLicense.js"></script>
		<script>
		    function validateForm(obj) {
		    	if (validateApprover(obj) == false) {
		      		return false;
		    	} else {
		        	return true;
		    	}
		  	}
		  	function  enableRentPaid(obj) {
		if(obj.value=="Rental") {
			document.getElementById("rentpaid").disabled=false;
		} else {
			document.getElementById("rentpaid").value="";
			document.getElementById("rentpaid").disabled=true;
		}
	}
	function checkLength(obj,val){
				if(obj.value.length>val) {
					alert('Max '+val+' digits allowed')
					obj.value = obj.value.substring(0,val);
				}
			}	
	
			function formatCurrency(obj) {
       			if(obj.value=="") {
        			return;
        		} else {
            		obj.value=(parseFloat(obj.value)).toFixed(2);
       			}
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
												<s:fielderror />
												<s:actionmessage />
											</td>
										</tr>
									</table>
									<s:push value="model">
										<s:form action="editTradeLicense" theme="css_xhtml" name="registrationForm" validate="true">
										<s:token/>
											<s:hidden name="id" id="id" />
											<s:hidden name="docNumber" id="docNumber" />
											<table border="0" cellpadding="0" cellspacing="0" width="100%" id="maintbl">
												<tbody>
													<tr>
														<td colspan="5" class="headingwk">
															<div class="arrowiconwk">
																<img src="${pageContext.request.contextPath}/images/arrow.gif" height="20" />
															</div>
															<div class="headplacer">
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
															<span class="mandatory">*</span>
														</td>
														<td>
															<s:select headerKey="" headerValue="%{getText('license.default.select')}" name="licenseeZoneId" id="licenseeZoneId" list="licenseZoneList" listKey="id" listValue='name' onChange="setupLicenseeAjaxDivision(this);" value="licensee.boundary.id" />
															<egov:ajaxdropdown id="populateLicenseeDivision" fields="['Text','Value']" dropdownId='licenseedivision' url='common/commonAjax!populateDivisions.action' />
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
															<span class="mandatory">*</span>
														</td>
														<td>
															<s:select headerKey="" headerValue="%{getText('license.default.select')}" name="licenseeZoneId" id="licenseeZoneId" list="dropdownData.zoneList" listKey="id" listValue='name' onChange="setupLicenseeAjaxDivision(this);" value="licensee.boundary.parent.id" />
															<egov:ajaxdropdown id="populateLicenseeDivision" fields="['Text','Value']" dropdownId='licenseedivision' url='common/commonAjax!populateDivisions.action' />
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
															<span class="mandatory">*</span>
														</td>
														<td>
															<s:textfield name="address.houseNo" maxlength="10"/>
														</td>
														<td>
															<s:text name='license.housenumber.old' />
														</td>
														<td>
															<s:textfield name="address.streetAddress2" maxlength="10"/>
														</td>
													</tr>
													<tr>
														<td>
															&nbsp;
														</td>
		<td><s:text name="license.remainingaddress" />
		</td>
	<td>
		<s:property value="address.streetAddress1" />
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
															<span class="mandatory">*</span>
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
															<div class="arrowiconwk">
																<img src="${pageContext.request.contextPath}/images/arrow.gif" height="20" />
															</div>
															<div class="headplacer">
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
															<s:text name="license.zone" /><span class="mandatory">*</span>
														</td>
														<td align="left">
															<s:select headerKey="" headerValue="%{getText('license.default.select')}" name="licenseZoneId" id="licenseZoneId" list="licenseZoneList" listKey="id" listValue='name' onChange="setupAjaxDivision(this);" value="boundary.id" />
															<egov:ajaxdropdown id="populateDivision" fields="['Text','Value']" dropdownId='division' url='common/commonAjax!populateDivisions.action' />
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
															<s:text name="license.zone" /><span class="mandatory">*</span>
														</td>
														<td align="left">
															<s:select headerKey="" headerValue="%{getText('license.default.select')}" name="licenseZoneId" id="licenseZoneId" list="dropdownData.zoneList" listKey="id" listValue='name' onChange="setupAjaxDivision(this);" value="boundary.parent.id" />
															<egov:ajaxdropdown id="populateDivision" fields="['Text','Value']" dropdownId='division' url='common/commonAjax!populateDivisions.action' />
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
															<span class="mandatory">*</span>
														</td>
														<td>
															<s:textfield name="licensee.address.houseNo" maxlength="10"/>
														</td>
														<td>
															<s:text name='license.housenumber.old' />
														</td>
														<td>
															<s:textfield name="licensee.address.streetAddress2"  maxlength="10"/>
														</td>
													</tr>
													<tr>
														<td>
															&nbsp;
														</td>
		<td><s:text name="licensee.remainingaddress" />
	</td>
	<td>
		<s:property value="licensee.address.streetAddress1" />
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
															<div class="arrowiconwk">
																<img src="${pageContext.request.contextPath}/images/arrow.gif" height="20" />
															</div>
															<div class="headplacer">
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
															<span class="mandatory">*</span>
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
																<div class="arrowiconwk">
																	<img src="${pageContext.request.contextPath}/images/arrow.gif" height="20" />
																</div>
																<div class="headplacer">
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
													<s:if test="%{#attr.isOldLicense == false}">
													    <tr>
													    	<td colspan="5">
																<c:set value="bluebox" var="trclass" />
																<%@ include file='../common/tradelicenseworkflow.jsp'%>
															</td>
														</tr>
													</s:if>
												</tbody>
											</table>
											<div class="mandatory" style="font-size: 11px;" align="left">
												* Mandatory Fields
											</div>
											<div>
												<table>
													<tr>
														<td align="center" colspan="4">
														<br/>
														<input type="button" class="button" value="Upload Document" id="docUploadButton" onclick="showDocumentManagerForDoc('docNumber');updateCurrentDocId('docNumber')" tabindex="1" />
														<br/>
														<br/>
														</td>
													</tr>
													<tr class="buttonbottom" id="buttondiv" style="align: middle">
														<s:if test="%{#attr.isOldLicense == false}">
														 <s:if test="%{roleName.contains('TLAPPROVER')}">
															<td>
																<s:submit type="submit" cssClass="buttonsubmit" value="Approve" id="Approve" method="edit" onclick="return validateForm(this);" />
															</td>
															</s:if>
															<td>
																<s:submit type="submit" cssClass="buttonsubmit" value="Forward" id="Forward" method="edit" onclick="return validateForm(this);" />
															</td>
															<td>
																<s:submit type="submit" cssClass="buttonsubmit" value="Reject" id="Reject" method="edit" onclick="return validateForm(this);" />
															</td>
														</s:if>
														<s:else>
															<td colspan="3">
																<s:hidden name="isOldLicense" value="%{isOldLicense}" />
																<s:submit type="submit" cssClass="buttonsubmit" value="Save" id="Save" method="edit" onclick="return validateForm(this);" />
															</td>
														</s:else>
														<td>
															<input type="button" value="Close" id="closeButton" onclick="javascript: window.close();" class="button" />
														</td>
													</tr>
												</table>
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
