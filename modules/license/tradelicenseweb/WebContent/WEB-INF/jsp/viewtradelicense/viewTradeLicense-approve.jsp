<%@ include file="/includes/taglibs.jsp"%>
<html>
	<head>
		<title><s:text name="page.title.approve.trade" /></title>
		<script>
	  		function validateForm(obj) {
	    		if (validateApprover(obj) == false) {
	      			return false;
	    		} else {
	      			return true;
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
										<s:text name="page.title.approve.trade" />
									</div>
									<table>
										<tr>
											<td align="left" style="color: #FF0000">
												<s:actionerror cssStyle="color: #FF0000" />
												<s:fielderror />
												<s:actionmessage />
											</td>
										</tr>
									</table>
									<s:form action="viewTradeLicense" theme="css_xhtml" name="viewForm" validate="true">
									<s:token/>
										<s:push value="model">
											<s:hidden name="docNumber" id="docNumber" />
											<c:set var="trclass" value="greybox" />
											<table border="0" cellpadding="0" cellspacing="0" width="100%">
												<%@ include file='../common/view.jsp'%>
												<s:if test="%{motorInstalled}">
													<tr>
														<td colspan="5">
															<%@ include file='../common/motordetailsview.jsp'%>
														</td>
													</tr>
												</s:if>
												<tr>
													<td colspan="5">
														<%@ include file='../common/feedetailsview.jsp'%>
													</td>
												</tr>												
												<tr>
													<td colspan="5">
														<%@ include file='../common/tradelicenseworkflow.jsp'%>
													</td>
												</tr>
												<tr>
													<td colspan="5" align="center">
														<br/>
														<input type="button" class="button" value="Upload Document" id="docUploadButton" onclick="showDocumentManagerForDoc('docNumber');updateCurrentDocId('docNumber')" tabindex="1" />
														<br/>
														<br/>
													</td>
												</tr>												
											</table>
											<div>
												<table>
													<tr class="buttonbottom" id="buttondiv" style="align: middle">
													<s:if test="%{roleName.contains('TLAPPROVER')}">
														<td>
															<s:submit type="submit" cssClass="buttonsubmit" value="Approve" id="Approve" method="approve" onclick="return validateForm(this);" />
														</td>
														</s:if>
														<td>
															<s:submit type="submit" cssClass="buttonsubmit" value="Forward" id="Forward" method="approve" onclick="return validateForm(this);" />
														</td>
														<td>
															<s:submit type="submit" cssClass="buttonsubmit" value="Reject" id="Reject" method="approve" onclick="return validateForm(this);" />
														</td>
														<td>
															<input type="button" value="Close" id="closeButton" onclick="javascript:window.close();" class="button" />
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