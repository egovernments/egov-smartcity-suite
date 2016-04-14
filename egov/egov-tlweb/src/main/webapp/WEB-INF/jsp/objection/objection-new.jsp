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
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<html>
	<head>
		<title><s:text name="page.title.objectlicense" /></title>
		<script>
			function validateForm(obj) {
				if (validateForm_objection() == false) {
					return false;
			    } else {
			    	return onSubmit();
			    }
	  	   }

		   function onSubmit() {
       			document.forms[0].action = 'objection-create.action';
       			document.forms[0].submit;
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
									<s:form action="objection" theme="simple" name="objectionLicenseForm" validate="true">
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
														<span class="mandatory1">*</span>:
													</td>
													<td class="bluebox">
														<s:textfield name="name" id="objectionName" tabindex="1" value="" />
													</td>
													<td class="bluebox">
														<s:text name="license.objection.address" />
														<span class="mandatory1">*</span>:
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
														<span class="mandatory1">*</span>:
													</td>
													<td class="greybox">
														<s:textfield name="objectionDate" id="objectionDate" onfocus="waterMarkTextIn('objectionDate','dd/mm/yyyy');" onblur="validateDateFormat(this);lessThanOrEqualToCurrentDate(this);" maxlength="10" size="10" value="" tabindex="1" onkeyup="DateFormat(this,this.value,event,false,'3')" />
														<a href="javascript:show_calendar('forms[0].objectionDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"> <img src="${pageContext.request.contextPath}/images/calendaricon.gif" alt="Date" width="18" height="18" border="0" align="absmiddle" id="calenderImgId" tabindex="1" /> </a>
													</td>
													<td class="greybox">
														<s:text name="license.objection.reason" />
														<span class="mandatory1">*</span>:
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
														<span class="mandatory1">*</span>:
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
														<%@ include file='../common/commonWorkflowMatrix.jsp'%>
														<%@ include file='../common/commonWorkflowMatrix-button.jsp'%>
													</td>
												</tr>
											</table>
											<div class="mandatory1" style="font-size: 11px;" align="left">
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
												<%-- <%@ include file='../common/commonWorkflowMatrix.jsp'%>
												<%@ include file='../common/commonWorkflowMatrix-button.jsp'%> --%>
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
