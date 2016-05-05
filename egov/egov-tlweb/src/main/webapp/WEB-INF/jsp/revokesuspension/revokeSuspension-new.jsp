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

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<html>
	<head>
		<title><s:text name="Revoke Suspension" /></title>
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
										<s:text name="Revoke Suspension" />
									</div>
									<s:form action="revokeSuspension" theme="simple" name="revokeSuspensionForm" validate="true">
									<s:token/>
										<s:hidden name="licenseId" />
										<c:set var="trclass" value="greybox" />
										<s:push value="model">
											<table width="99%" border="0" cellspacing="0" cellpadding="0">
												<s:push value="license">
												<%@ include file='../objection/viewLicense.jsp'%>
												</s:push>
												<%@ include file='../common/licenseobjectionview.jsp'%>
												<tr>
													<td colspan="5" class="headingwk">
														<div class="arrowiconwk">
															<img src="${pageContext.request.contextPath}/images/arrow.gif" height="20" />
														</div>
														<div class="headplacer">
															<s:text name="Revoke Suspension Details" />
														</div>
													</td>
												</tr>
												<tr>
													<td class="<c:out value="${trclass}"/>">
														&nbsp;
													</td>
													<td class="<c:out value="${trclass}"/>">
														<s:text name="Revoke Suspension Date" />
														<span class="mandatory1">*</span>:
													</td>
													<td class="<c:out value="${trclass}"/>">
														<s:textfield name="revokeDate" id="revokeDate" onfocus="waterMarkTextIn('revokeDate','dd/mm/yyyy');" onblur="validateDateFormat(this);lessThanOrEqualToCurrentDate(this);" maxlength="10" size="10" value="" tabindex="1" onkeyup="DateFormat(this,this.value,event,false,'3')" />
														<a href="javascript:show_calendar('forms[0].revokeDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"> <img src="${pageContext.request.contextPath}/images/calendaricon.gif" alt="Date" width="18" height="18" border="0" align="absmiddle" id="calenderImgId" tabindex="1" /> </a>
													</td>
													<td class="<c:out value="${trclass}"/>">
														<s:text name="license.revoke.remarks" />
														<span class="mandatory1">*</span>:
													</td>
													<td class="<c:out value="${trclass}"/>">
														<s:textarea name="revokeRemarks" rows="5" cols="40" id="revokeRemarks" tabindex="1" />
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
															<s:submit name="submit" type="submit" cssClass="buttonsubmit" id="button1" method="confirmRevokeSuspension" value="Save" onclick="return validateForm(this);" tabindex="1" />
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
