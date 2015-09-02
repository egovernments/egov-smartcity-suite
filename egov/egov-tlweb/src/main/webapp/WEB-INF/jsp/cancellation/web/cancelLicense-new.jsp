#-------------------------------------------------------------------------------
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
#-------------------------------------------------------------------------------
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<html>
	<head>
		<sx:head />
		<title><s:text name="page.title.cancellicense" /></title>

		<style type="text/css">
div.errorstyle {
	font-size: 10px;
	font-weight: bold;
	color: #F00;
	text-align: left;
	margin-bottom: 10px;
	padding: 10px;
	background-color: #FFFFEE;
	font-family: Arial, Helvetica, sans-serif;
	border: 1px solid #EEEE00;
}
</style>
		<script type="text/javascript">
	function validateDataBeforeSubmit() {
		var canclReasonObj = document.getElementById("reasonForCancellation");
		var canclReasonIndex = canclReasonObj.selectedIndex;
		var canclReasonValue = canclReasonObj.options[canclReasonIndex].value;
		if (canclReasonValue == " " || canclReasonValue == "-1"
				|| canclReasonValue == "0") {
			dom.get("cancelLicense_error").style.display = '';
			document.getElementById("cancelLicense_error").innerHTML = '<s:text name="tradelicense.LicenseCancelInfo.reasonForCancellation.none" />';
			return false;
		}
		if (document.getElementById("cancelInforemarks").value == '') {
			dom.get("cancelLicense_error").style.display = '';
			document.getElementById("cancelLicense_error").innerHTML = '<s:text name="tradelicense.LicenseCancelInfo.cancelInforemarks.none" />';
			return false;
		}

		return true;
	}

	function waterMarkTextIn(styleId, value) {
		var txt = document.getElementById(styleId).value;
		if (txt == value) {
			document.getElementById(styleId).value = '';
			document.getElementById(styleId).style.color = '';
		}
	}

	function waterMarkTextOut(styleId, value) {
		var txt = document.getElementById(styleId).value;
		if (txt == '') {
			document.getElementById(styleId).value = value;
			document.getElementById(styleId).style.color = 'DarkGray';
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
										<s:text name="page.title.cancellicense" />
									</div>
									<table>
										<tr>
											<td align="left" style="color: #FF0000">
												<s:actionerror cssStyle="color: #FF0000" />
											</td>
										</tr>
									</table>
									<div id="cancelLicense_error" class="errorstyle" style="display: none;">
									</div>
									<s:form action="cancelLicense" theme="css_xhtml" name="cancelLicenseForm" validate="true">
										<s:token />
										<s:push value="model">
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
												<tr>
													<td colspan="17" class="headingwk">
														<div class="arrowiconwk">
															<img src="${pageContext.request.contextPath}/images/arrow.gif" height="20"/>
														</div>
														<div class="headplacer">
															Cancellation Details
														</div>
													</td>
												</tr>
												<tr>
													<td class="greybox" width="5%"></td>
													<td class="greybox">
														<span class="mandatory">*</span>
														<s:text name="license.LicenseCancelInfo.reasonForCancellation" />
													</td>
													<td class="greybox bold" colspan="3">
														<s:select headerKey="" headerValue="%{getText('license.default.select')}" disabled="%{sDisabled}" name="reasonForCancellation" id="reasonForCancellation" list="reasonMap" />
													</td>
													<td class="greybox" width="5%"></td>
													<td class="greybox">
														<span class="mandatory">*</span>
														<s:text name="license.license.refernceno" />
													</td>
													<td class="greybox bold" colspan="3">
														<s:textfield value="%{refNo}" name="refernceno" size="20" tabindex="2" id="refernceno" disabled="%{sDisabled}" />
													</td>
												</tr>
												<tr>
													<td class="bluebox" width="5%"></td>
													<td class="bluebox">
														<s:text name="license.license.referencedate" />
													</td>
													<td class="bluebox bold" colspan="3">
														<s:date name="refDate" id="commDateId2" format="dd/MM/yyyy" />
														<s:textfield disabled="%{sDisabled}" name="commdateApp" id="commdateApp" onfocus="waterMarkTextIn('commdateApp','dd/mm/yyyy');" onblur="validateDateFormat(this);lessThanOrEqualToCurrentDate(this);" maxlength="10" size="10" value="%{commDateId2}" tabindex="3" onkeyup="DateFormat(this,this.value,event,false,'3')" />
														<a href="javascript:show_calendar('forms[0].commdateApp',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"> <img src="${pageContext.request.contextPath}/images/calendaricon.gif" alt="Date" width="18" height="18" border="0" align="absmiddle" id="calenderImgId" /> </a>
													</td>
													<td class="bluebox" width="5%"></td>
													<td class="bluebox">
														<s:text name="license.license.Remarks" />
													</td>
													<td class="bluebox bold" colspan="3">
														<s:textarea tabindex="4" name="cancelInforemarks" rows="3" cols="33" id="cancelInforemarks" disabled="%{sDisabled}" value="" />
													</td>

												</tr>
											</table>
											<s:hidden name="licenseId" />
											<div class="mandatory" style="font-size: 11px;" align="left">
												* Mandatory Fields
											</div>
											<div class="buttonbottom">
												<table>
													<tr class="buttonbottom" id="buttondiv" style="align: middle">
														<td>
															<s:submit name="button32" type="submit" cssClass="buttonsubmit" id="button32" method="confirmCancellation" value="Submit" />
														</td>
														<td>
															<input name="button2" type="button" class="button" id="button" onclick="window.close()" value="Close" />
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
