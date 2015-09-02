<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%@ include file="/includes/taglibs.jsp"%>

<html>
	<head>
		<title><s:text name="page.title.cancellicense" />
		</title>
	</head>
	<script>
		function closethis() {
			if (confirm("Do you want to close this window ?")) {
				window.close();
			}
		}
		
		function printthis() {
			if (confirm("Do you want to print this screen ?")) {
				var html="<html>";
				html+= document.getElementById('content').innerHTML;
				html+="</html>";
				
				var printWin = window.open('','','left=0,top=0,width=1,height=1,toolbar=0,scrollbars=0,status=0');
				printWin.document.write(html);
				printWin.document.close();
				printWin.focus();
				printWin.print();
				printWin.close();
			}		
		}
	</script>
	<body onload="refreshInbox();">
		<div id="content">
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
										<s:form action="cancelLicense" theme="simple" name="cancelLicenseForm">
											<s:push value="model">
												<c:set var="trclass" value="greybox" />
												<table width="100%">
													<%@ include file='../../common/view.jsp'%>
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
																<img src="${pageContext.request.contextPath}/images/arrow.gif" height="20"/>
															</div>
															<div class="headplacer">
																<s:text name='license.title.cancellationdetails' />
															</div>
														</td>
													</tr>
													<tr>
														<td class="<c:out value="${trclass}"/>" width="5%">
															&nbsp;
														</td>
														<td class="<c:out value="${trclass}"/>">
															<b><s:text name="license.LicenseCancelInfo.reasonForCancellation" />
															</b>
														</td>
														<td class="<c:out value="${trclass}"/>">
															<s:property value="%{reasonMap.get(reasonForCancellation)}" />
														</td>
														<td class="<c:out value="${trclass}"/>">
															<b><s:text name="license.license.refernceno" />
															</b>
														</td>
														<td class="<c:out value="${trclass}"/>">
															<s:text name="%{refernceno}" />
														</td>														
													</tr>
													<tr>
														<td class="<c:out value="${trclass}"/>" width="5%">
															&nbsp;
														</td>
														<s:date name='commdateApp' id="refdate" format='dd/MM/yyyy' />
														<td class="<c:out value="${trclass}"/>">
															<b><s:text name="license.license.referencedate" />
															</b>
														</td>
														<td class="<c:out value="${trclass}"/>">
															<s:property value="refdate" />

														</td>
														<td class="<c:out value="${trclass}"/>">
															<b><s:text name="license.license.Remarks" />
															</b>
														</td>
														<td class="<c:out value="${trclass}"/>">
															<s:property value="cancelInforemarks" />
														</td>														
													</tr>
												</table>
											</s:push>
										</s:form>
									</div>
								</center>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div align="center" class="buttonbottom" id="buttondiv">
			<table>
				<tr>
					<td>
						<input name="button1" type="button" class="button" id="button" onClick="printthis()" value="Print" />
					</td>
					<td>
						<input name="button2" type="button" class="button" id="button" onclick="closethis()" value="Close" />
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>