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
		<sx:head />
		<title>Record Inspection/Response</title>
		<script>
 		function validateForm(obj) {
		    if (validateApprover(obj) == false) {
		      return false;
		    } else {
		     	if (document.getElementById('typeResponse').checked || document.getElementById('typeInspection').checked ) {
	      			if (document.getElementById('activityDate').value == '') {
	      				alert ('Please enter the Inspection / Response Date');
	      				return false;
	      			}
		      	}
		      	return true;
		    }
		}
		function showGradeName(obj) {
			if(obj.value === '1') { 
				document.getElementById('gradeName').style.display = '';
				document.getElementById('gradeNameLbl').style.display = '';
			} else {
				document.getElementById('gradeName').style.display = 'none';
				document.getElementById('gradeNameLbl').style.display = 'none';
			}
		}
		
		function openPreliminaryNotice(){
			window.open('${pageContext.request.contextPath}/objection/objection-preNotice.action?model.id=<s:property value="model.id"/>','_self','resizable=yes,scrollbars=yes,height=700,width=900,status=yes');
		}
		
		function openShowCauseNotice(){
			window.open('${pageContext.request.contextPath}/objection/objection-scNotice.action?model.id=<s:property value="model.id"/>','_self','resizable=yes,scrollbars=yes,height=700,width=900,status=yes');
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
										<s:text name="page.title.object.license.response" />
									</div>
									<s:form action="objection" theme="css_xhtml" name="objectionLicenseForm" validate="true">
									<s:token/>
										<c:set var="trclass" value="greybox" />
										<s:push value="model">
											<table width="99%" border="0" cellspacing="0" cellpadding="0">
												<s:push value="license">
													<%@ include file='viewLicense.jsp'%>
												</s:push>
												<%@ include file='viewObjection.jsp'%>
												<%@ include file='inspectiondetails.jsp'%>
												<%@ include file='responsedetails.jsp'%>
												<tr>
													<td colspan="5" class="headingwk">
														<div class="arrowiconwk">
															<img src="${pageContext.request.contextPath}/images/arrow.gif" height="20" />
														</div>
														<div class="headplacer">
															<s:text name="page.title.objection.responsedetail" />
														</div>
													</td>
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
													<td class="<c:out value="${trclass}"/>"></td>
													<td class="<c:out value="${trclass}" />">
														<s:text name="objection.recordtype" />
														<span class="mandatory1">*</span>
													</td>
													<td class="<c:out value="${trclass}" />">
														<s:radio name="activities[%{size}].type" list="activityTypeList" id="type" />
													</td>
													<td class="<c:out value="${trclass}"/>"></td>
													<td class="<c:out value="${trclass}"/>"></td>
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
													<td class="<c:out value="${trclass}"/>">
														&nbsp;
													</td>
													<td class="<c:out value="${trclass}"/>">
														<s:text name="objection.activity.date" />
														<span class="mandatory1">*</span>:
													</td>
													<td class="<c:out value="${trclass}"/>">
														<s:textfield name="activities[%{size}].activityDate" id="activityDate" onfocus="waterMarkTextIn('activityDate','dd/mm/yyyy');" onblur="validateDateFormat(this);lessThanOrEqualToCurrentDate(this);" maxlength="10" size="10" value="" tabindex="1" onkeyup="DateFormat(this,this.value,event,false,'3')" />
														<a href="javascript:show_calendar('forms[0].activityDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"> <img src="${pageContext.request.contextPath}/images/calendaricon.gif" alt="Date" width="18" height="18" border="0" align="absmiddle" id="calenderImgId" tabindex="1" /> </a>
													</td>
													<td class="<c:out value="${trclass}"/>">
														<s:text name="license.uploaddocument" />
													</td>
													<td class="<c:out value="${trclass}"/>">
														<input type="button" class="button" value="Upload Document" id="docUploadButton" onclick="updateCurrentDocId('activities[${size}].docNumber');showDocumentManagerForDoc('activities[${size}].docNumber')" tabindex="1" />
														<s:hidden name="activities[%{size}].docNumber" id="activities[%{size}].docNumber" />
													</td>
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
													<td class="<c:out value="${trclass}"/>">
														&nbsp;
													</td>
													<td class="<c:out value="${trclass}"/>">
														<s:text name="objection.activitydetails" />
													</td>
													<td class="<c:out value="${trclass}"/>" colspan="3">
														<s:textarea name="activities[%{size}].details" rows="3" cols="100" id="" value="" tabindex="1" />
													</td>
												</tr>
												<s:if test="true">
													<c:choose>
														<c:when test="${trclass=='greybox'}">
															<c:set var="trclass" value="bluebox" />
														</c:when>
														<c:when test="${trclass=='bluebox'}">
															<c:set var="trclass" value="greybox" />
														</c:when>
													</c:choose>

													<tr>
														<td class="<c:out value="${trclass}"/>">
															&nbsp;
														</td>
														<td class="<c:out value="${trclass}"/>">
															<s:text name="license.objection.response.reassign" />
														</td>
														<td class="<c:out value="${trclass}"/>">
															<s:radio name="objection.reassign" list="#{'1':'Yes','2':'No'}" value="2" onclick="showGradeName(this)" />
														</td>
														<td class="<c:out value="${trclass}"/>">
															<label id="gradeNameLbl" style="display: none">
																<s:text name="license.gradename" />
															</label>
														</td>
														<td class="greybox">
															<s:select id="gradeName" style="display:none" name="gradename" headerKey="-1" headerValue="Select Grade" list="#{'Hospital':'Hospital', 'Nursing Home':'Nursing Home','Dispensary':'Dispensary'}" value="selectedGrade" required="true" />
														</td>
													</tr>
												</s:if>

												<tr>
													<td colspan="5">
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
													<s:if test="%{roleName.contains('TLAPPROVER')}">
														<td>
															<s:submit name="submit" type="submit" cssClass="buttonsubmit" id="button1" method="approve" value="Approve" onclick="return validateForm(this);" tabindex="1" />
														</td>
														</s:if>
														<td>
															<s:submit name="submit" type="submit" cssClass="buttonsubmit" id="button1" method="approve" value="Reject" onclick="return validateForm(this);" tabindex="1" />
														</td>
														<td>
															<s:submit name="submit" type="submit" cssClass="buttonsubmit" id="button1" method="approve" value="Forward" onclick="return validateForm(this);" tabindex="1" />
														</td>
														<td>
															<s:submit name="submit" type="submit" cssClass="buttonsubmit" id="button2" method="approve" value="Save" onclick="return validateForm(this);" tabindex="1" />
														</td>
														<s:if test="%{model.currentState.value=='Object Licenses:PIDone'}">
															<td>
																<input name="preNotice" type="button" class="button" id="button" value="Preliminary Notice" onclick="openPreliminaryNotice();" />
															</td>
														</s:if>
														<s:if test="%{(model.currentState.value=='Object Licenses:PN Issued' && model.currentState.value!='NEW' && model.currentState.value!='Object Licenses:PIDone') || 
														(model.currentState.value=='Object Licenses:Forwarded' && model.currentState.previous.value=='Object Licenses:SCN Issued') || 
														(model.currentState.previous.value=='Object Licenses:PN Issued' && model.currentState.value=='Object Licenses:Forwarded')}">
															<td>
																<input name="showCauseNotice" type="button" class="button" id="button" value="Show Cause Notice" onclick="openShowCauseNotice();" />
															</td>
														</s:if>
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
