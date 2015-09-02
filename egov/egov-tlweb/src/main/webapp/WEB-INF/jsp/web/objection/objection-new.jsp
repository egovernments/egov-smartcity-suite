<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<html>
	<head>
		<title><s:text name="page.title.objectlicense" /></title>
		<sx:head />
		<script>
		   function validateForm(obj) {
		    	return validateApprover(obj);
	  	   }
    	</script>
	</head>
	<body onload="resetForm()">
		<table align="center" width="100%">
			<tbody>
				<tr>
					<td>
						<div align="center">
							<center>
								<div class="formmainbox">
									<div class="headingbg">
										<s:text name="page.title.objectlicense" />
									</div>
									<s:form action="objection" theme="css_xhtml" name="objectionLicenseForm" validate="true">
									<s:token/>
										<s:hidden name="licenseId" />
										<c:set var="trclass" value="greybox" />
										<s:push value="model">

											<table width="99%" border="0" cellspacing="0" cellpadding="0">
												<s:push value="license">
													<%@ include file='viewLicense.jsp'%>
												</s:push>
												<tr>
													<td colspan="5" class="headingwk">
														<div class="arrowiconwk">
															<img src="${pageContext.request.contextPath}/images/arrow.gif" height="20" />
														</div>
														<div class="headplacer">
															<s:text name="license.objection.details" />
														</div>
													</td>
												</tr>
												<tr>
													<td class="bluebox">
														&nbsp;
													</td>
													<td class="bluebox">
														<s:text name="license.objection.raisedby" />
														<span class="mandatory">*</span>:
													</td>
													<td class="bluebox">
														<s:textfield name="name" id="objectionName" tabindex="1" value="" />
													</td>
													<td class="bluebox">
														<s:text name="license.objection.address" />
														<span class="mandatory">*</span>:
													</td>
													<td class="bluebox">
														<s:textarea name="address" rows="3" cols="33" id="objectionAddress" value="" tabindex="1" />
													</td>
												</tr>
												<tr>
													<td class="greybox">
														&nbsp;
													</td>
													<td class="greybox">
														<s:text name="license.objection.receivedon" />
														<span class="mandatory">*</span>:
													</td>
													<td class="greybox">
														<s:textfield name="objectionDate" id="objectionDate" onfocus="waterMarkTextIn('objectionDate','dd/mm/yyyy');" onblur="validateDateFormat(this);lessThanOrEqualToCurrentDate(this);" maxlength="10" size="10" value="" tabindex="1" onkeyup="DateFormat(this,this.value,event,false,'3')" />
														<a href="javascript:show_calendar('forms[0].objectionDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"> <img src="${pageContext.request.contextPath}/images/calendaricon.gif" alt="Date" width="18" height="18" border="0" align="absmiddle" id="calenderImgId" tabindex="1" /> </a>
													</td>
													<td class="greybox">
														<s:text name="license.objection.reason" />
														<span class="mandatory">*</span>:
													</td>
													<td class="greybox">
														<s:select headerKey="" headerValue="%{getText('license.default.select')}" name="reason" value="" id="objectionReason" list="objectionReasons" tabindex="1" />
													</td>
												</tr>
												<tr>
													<td class="bluebox">
														&nbsp;
													</td>
													<td class="bluebox">
														<s:text name="license.objection.details" />
														<span class="mandatory">*</span>:
													</td>
													<td class="bluebox">
														<s:textarea name="details" rows="3" cols="33" id="objectionDetails" value="" tabindex="1" />
													</td>
													<td class="bluebox">
														<s:text name="license.uploaddocument" />
														:
													</td>
													<td class="bluebox">
														<s:hidden name="docNumber" id="objection.docNumber" />
														<input type="button" class="button" value="Upload Document" id="docUploadButton" onclick="showDocumentManagerForDoc('objection.docNumber');updateCurrentDocId('objection.docNumber')" tabindex="1" />
													</td>
												</tr>
												<tr style="display: none">
													<td colspan="5">
														<c:set value="bluebox" var="trclass" />
														<%@ include file='../../common/tradelicenseworkflow.jsp'%>
													</td>
												</tr>
											</table>
											<div class="mandatory" style="font-size: 11px;" align="left">
												* Mandatory Fields
											</div>

											<div class="buttonbottom">
												<table>
													<tr class="buttonbottom" id="buttondiv" style="align: middle">
														<td>
															<s:submit name="submit" type="submit" cssClass="buttonsubmit" id="button2" method="create" value="Save" onclick="return validateForm(this);" tabindex="1" />
														</td>
														<td>
															<input name="close" type="button" class="button" id="button" onclick="window.close()" value="Close" tabindex="1" />
														</td>
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