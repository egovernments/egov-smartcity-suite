<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<html>
	<head>
		<title><s:text name="Revoke Suspension" /></title>
		<sx:head />
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
									<s:form action="revokeSuspension" theme="css_xhtml" name="revokeSuspensionForm" validate="true">
									<s:token/>
										<s:hidden name="licenseId" />
										<c:set var="trclass" value="greybox" />
										<s:push value="model">
											<table width="99%" border="0" cellspacing="0" cellpadding="0">
												<s:push value="license">
												<%@ include file='../../web/objection/viewLicense.jsp'%>
												</s:push>
												<%@ include file='../../common/licenseobjectionview.jsp'%>
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
														<span class="mandatory">*</span>:
													</td>
													<td class="<c:out value="${trclass}"/>">
														<s:textfield name="revokeDate" id="revokeDate" onfocus="waterMarkTextIn('revokeDate','dd/mm/yyyy');" onblur="validateDateFormat(this);lessThanOrEqualToCurrentDate(this);" maxlength="10" size="10" value="" tabindex="1" onkeyup="DateFormat(this,this.value,event,false,'3')" />
														<a href="javascript:show_calendar('forms[0].revokeDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"> <img src="${pageContext.request.contextPath}/images/calendaricon.gif" alt="Date" width="18" height="18" border="0" align="absmiddle" id="calenderImgId" tabindex="1" /> </a>
													</td>
													<td class="<c:out value="${trclass}"/>">
														<s:text name="license.revoke.remarks" />
														<span class="mandatory">*</span>:
													</td>
													<td class="<c:out value="${trclass}"/>">
														<s:textarea name="revokeRemarks" rows="5" cols="40" id="revokeRemarks" tabindex="1" />
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