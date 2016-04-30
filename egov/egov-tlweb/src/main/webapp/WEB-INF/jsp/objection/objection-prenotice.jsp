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
		<title>Record Inspection/Response</title>
		<script>
		 function validateForm(obj) {
		    return validateApprover(obj);
		 }
		   
		function refreshInbox() {
			if (opener && opener.top.document.getElementById('inboxframe')) {
				opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
			}
		}
					
		function printLicense() {
			if(document.getElementById("activityDate").value==''){
				bootbox.alert("Please enter Notice Date");return false;
			} else if(document.getElementById("expectedDateOfResponse").value=='') {
				bootbox.alert("Please enter Expected Date of Response");return false;
			} else {
				var url = 'objection-preliminaryNotice.action?model.id=<s:property value="model.id"/>';
				var form = document.createElement("form");
				form.setAttribute("method", "post");
				form.setAttribute("action", url);
				document.body.appendChild(form);
				var popupwin = window.open(url,"PrintNoticeLetter",'resizable=yes,scrollbars=yes,height=700,width=800,status=yes');
				popupwin.focus();
			}
						
		}
		
		function submitandclose(){ 
		    var printcomplete=confirm("Are you sure License print completed?","YES","NO");
		    if(printcomplete){
		      	document.getElementById('workflowBean.actionName').value='GeneratedPN';
		      	return true;
		    } else {
		      	return false;
		    }
		}
		</script>
	</head>
	<body onload="refreshInbox()">
		<table align="center" width="100%">
			<tbody>
				<tr>
					<td>
						<div align="center">
							<center>
								<div class="formmainbox">
									<div class="headingbg">
										<s:text name="Preliminary Notice" />
									</div>
									<s:form action="objection" theme="simple" name="preliminaryNoticeForm" validate="true">
									<s:token/>
										<s:hidden name="workflowBean.actionName" id="workflowBean.actionName" />
										<s:hidden name="activities[0].type" value="PreNotice" />
										<s:hidden name="model.id" />
										<c:set var="trclass" value="greybox" />
										<s:push value="model">
											<br />
											<table width="99%" border="0" cellspacing="0" cellpadding="0">

												<tr>
													<td colspan="5" class="headingwk">
														<div class="arrowiconwk">
															<img src="${pageContext.request.contextPath}/images/arrow.gif" height="20" />
														</div>
														<div class="headplacer">
															<s:text name="Notice Details" />
														</div>
													</td>
												</tr>
												<tr>
													<td class="<c:out value="${trclass}"/>" width="20%">
														&nbsp;
													</td>
													<td class="<c:out value="${trclass}"/>" width="15%">
														<s:text name="license.preliminarynotice.date" />
														<span class="mandatory1">*</span>:
													</td>
													<td class="<c:out value="${trclass}"/>">
														<s:textfield name="activities[0].activityDate" id="activityDate" onfocus="waterMarkTextIn('activityDate','dd/mm/yyyy');" onblur="waterMarkTextOut('activityDate','dd/mm/yyyy');lessThanOrEqualToCurrentDate(this);" maxlength="10" size="10" value="%{activityDate}" onkeyup="DateFormat(this,this.value,event,false,'3')" />
														<a href="javascript:show_calendar('forms[0].activityDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"> <img src="${pageContext.request.contextPath}/images/calendaricon.gif" alt="Date" width="18" height="18" border="0" align="absmiddle" id="calenderImgId" /> </a>
													</td>
													<td class="<c:out value="${trclass}"/>" width="20%">
														<s:text name="license.preliminarynotice.response.expecteddate" />
														<span class="mandatory1">*</span>:
													</td>
													<td class="<c:out value="${trclass}"/>">
														<s:textfield name="activities[0].expectedDateOfResponse" id="expectedDateOfResponse" onfocus="waterMarkTextIn('expectedDateOfResponse','dd/mm/yyyy');" onblur="waterMarkTextOut('expectedDateOfResponse','dd/mm/yyyy');" maxlength="10" size="10" value="%{expectedDateOfResponse}" onkeyup="DateFormat(this,this.value,event,false,'3')" />
														<a href="javascript:show_calendar('forms[0].expectedDateOfResponse',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"> <img src="${pageContext.request.contextPath}/images/calendaricon.gif" alt="Date" width="18" height="18" border="0" align="absmiddle" id="calenderImgId" /> </a>
													</td>
												</tr>
											</table>
											<div class="buttonbottom">
												<table>
													<tr>
														<td>
															<input type="button" id="print" value="Print" onclick="return printLicense()" />
														</td>
														<s:if test="%{!#parameters.duplicate}">
															<td>
																<s:submit name="submit" type="submit" value="Print Completed" id="printcmplt" method="approve" onclick="return submitandclose(); return validateForm(this);" />
															</td>
														</s:if>
														<td>
															<input type="button" id="close" value="Close" onclick="javascript:window.close();" />
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
