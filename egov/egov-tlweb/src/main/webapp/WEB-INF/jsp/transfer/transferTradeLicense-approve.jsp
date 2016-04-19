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
<title><s:text name="page.title.transfer" /></title>
<script>
		  	function validateForm(obj) {
		    	if (validateApprover(obj) == false) {
		      		return false;
		    	} else {
		      		enableAll();
		      		return true;
		    	}
		  	}
		 	
		 	function onLoadTask() {
		     	disableAll();
		    }
			
			function disableAll() {
				var frmIndex=0;
				for(var i=0;i<document.forms[frmIndex].length;i++) {
		    		for(var i=0;i<document.forms[0].length;i++) {
		    			if(document.forms[0].elements[i].value != 'Save' && document.forms[0].elements[i].value != 'Approve'
						    && document.forms[0].elements[i].value != 'Reject' && document.forms[0].elements[i].value != 'Cancel' && document.forms[0].elements[i].value != 'Forward'
						      && document.forms[0].elements[i].value != 'GenerateCertificate' && document.forms[0].elements[i].value != 'Close'
						      && document.forms[0].elements[i].name!='workflowBean.departmentId' && document.forms[0].elements[i].name!='workflowBean.designationId' &&
						      document.forms[0].elements[i].name!='workflowBean.approverUserId' && document.forms[0].elements[i].name!='workflowBean.comments')
						 {
						    document.forms[frmIndex].elements[i].disabled =true;
						 }           
		  			}	                       
				}
		   	}
			
			function enableAll() {
				var frmIndex=0;
				for(var i=0;i<document.forms[frmIndex].length;i++) {
		    		for(var i=0;i<document.forms[0].length;i++) {
		    	    	document.forms[frmIndex].elements[i].disabled =false;
		            }                      
				}
		    }
		  
		</script>
</head>
<body onload="onLoadTask();">
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
								<s:form action="transferTradeLicense" theme="simple"
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
												<tr>
													<td class="<c:out value="${trclass}"/>" width="5%">
														&nbsp;</td>
													<td class="<c:out value="${trclass}"/>"><s:text
															name="licensee.current.applicantname" /></td>
													<td class="<c:out value="${trclass}"/>"><s:property
															value="licensee.applicantName" /></td>
													<td class="<c:out value="${trclass}"/>"><s:text
															name="license.applicationdate" /></td>
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
													<td class="<c:out value="${trclass}"/>" width="5%">
														&nbsp;</td>
													<td class="<c:out value="${trclass}"/>"><s:text
															name="licensee.applicantname" /></td>
													<td class="<c:out value="${trclass}"/>"><s:textfield
															name="licenseTransfer.oldApplicantName"
															id="applicantName" /></td>
													<td class="<c:out value="${trclass}"/>"><s:text
															name="license.establishmentname" /></td>
													<td class="<c:out value="${trclass}"/>"><s:textfield
															name="licenseTransfer.oldNameOfEstablishment"
															id="nameOfEstablishment" /></td>
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
															<s:text name="license.zone" />
														</td>
														<td class="<c:out value="${trclass}"/>" align="left">
															<select id="zoneId">
																<option value="${licenseTransfer.boundary.id}">
																	${licenseTransfer.boundary.name}</option>
														</select>
														</td>
														<td class="<c:out value="${trclass}"/>"><s:text
																name="license.division" /></td>
														<td class="<c:out value="${trclass}"/>" align="left">
															<select name="licenseTransfer.boundary" id="zoneId">
																<option value="">--Select--</option>
														</select>
														</td>


													</tr>
												</s:if>
												<s:else>
													<tr>
														<td class="<c:out value="${trclass}"/>" width="5%">
															&nbsp;</td>
														<td class="<c:out value="${trclass}"/>" align="right">
															<s:text name="license.zone" />
														</td>
														<td class="<c:out value="${trclass}"/>" align="left">
															<select id="zoneId">
																<option value="${licenseTransfer.boundary.parent.id}">
																	${licenseTransfer.boundary.parent.name}</option>
														</select>
														</td>
														<td class="<c:out value="${trclass}"/>"><s:text
																name="license.division" /></td>
														<td class="<c:out value="${trclass}"/>"><select
															name="licenseTransfer.boundary" id="zoneId">
																<option value="${licenseTransfer.boundary.id}">
																	${licenseTransfer.boundary.name}</option>
														</select></td>

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
															name='license.housenumber' /> <span class="mandatory1">*</span>
													</td>
													<td class="<c:out value="${trclass}"/>"><s:textfield
															name="licenseTransfer.oldAddress.houseNo" /></td>
													<td class="<c:out value="${trclass}"/>"><s:text
															name='license.housenumber.old' /></td>
													<td class="<c:out value="${trclass}"/>">
														<s:textfield name="licenseTransfer.oldAddress.streetAddress2" /></td>
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
															cols="40" /></td>
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
															onKeyPress="return numbersonly(this, event)" /></td>
													<td class="<c:out value="${trclass}"/>"><s:text
															name='license.address.phonenumber' /></td>
													<td class="<c:out value="${trclass}"/>"><s:textfield
															name="licenseTransfer.oldPhoneNumber"
															onKeyPress="return numbersonly(this, event)" /></td>
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
															onBlur="checkLength(this,10)" /></td>
													<td class="<c:out value="${trclass}"/>"><s:text
															name='licensee.mobilephone' /></td>
													<td class="<c:out value="${trclass}"/>"><s:textfield
															name="licenseTransfer.oldMobileNumber"
															onKeyPress="return numbersonly(this, event)"
															onBlur="checkLength(this,10)" /></td>
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
															onBlur="validateEmail(this);checkLength(this,50)" /></td>
													<td class="<c:out value="${trclass}"/>"><s:text
															name='licensee.uid' /></td>
													<td class="<c:out value="${trclass}"/>"><s:textfield
															name="licenseTransfer.oldUid"
															onBlur="checkLength(this,12)" /></td>
												</tr>
												<tr>
													<td colspan="5">
														<%@ include file='../common/commonWorkflowMatrix.jsp'%>
														<%@ include file='../common/commonWorkflowMatrix-button.jsp'%>
													</td>
												</tr>
											</tbody>
										</table>
										<div class="mandatory1" style="font-size: 11px;" align="left">
											* Mandatory Fields</div>
										<div>
											<table>
												<tr class="buttonbottom" id="buttondiv"
													style="align: middle">
													<s:if test="%{roleName.contains('TLAPPROVER')}">
													<td><s:submit cssClass="buttonsubmit" value="Approve"
															id="Approve" method="approve"
															onclick="return validateForm(this);" /></td>
															</s:if>
													<td><s:submit cssClass="buttonsubmit" value="Forward"
															id="Forward" method="approve"
															onclick="return validateForm(this);" /></td>
													<td><s:submit cssClass="buttonsubmit" value="Reject"
															id="Reject" method="approve"
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
