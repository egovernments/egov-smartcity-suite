
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib prefix="sx" uri="/WEB-INF/struts-dojo-tags.tld"%>
<html>
<head>
<title><s:text name="page.title.transfer" /></title>
<sx:head />
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
															name='license.housenumber' /> <span class="mandatory">*</span>
													</td>
													<td class="<c:out value="${trclass}"/>"><s:textfield
															name="licenseTransfer.oldAddress.houseNo" /></td>
													<td class="<c:out value="${trclass}"/>"><s:text
															name='license.housenumber.old' /></td>
													<td class="<c:out value="${trclass}"/>"
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