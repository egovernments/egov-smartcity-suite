<%@ include file="/includes/taglibs.jsp"%>
<%@page import="org.egov.lib.admbndry.CityWebsiteDAO,org.egov.lib.admbndry.CityWebsite"%>
<html>
	<head>
		<title>Rejected Trade License</title>
		<script>
			function refreshInbox() {
				if (opener && opener.top.document.getElementById('inboxframe')) {
					opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
				}
			}
			
			function printLicense(){
				if( document.getElementById("rejectreason").value=='') {
					alert("Please enter reason for rejection");return false;
				} else {
					var url= "viewTradeLicense!certificateForRej.action?model.id="+<s:property value="model.id"/>;
					var form = document.createElement("form");
					form.setAttribute("method", "post");
					form.setAttribute("action", url);
					// setting form target to a window named 'formresult'
					form.setAttribute("target", "PrintRejectionLetter");
					var hiddenField = document.createElement("input");
					hiddenField.setAttribute("type","hidden");              
					hiddenField.setAttribute("name", "rejectreason");
					hiddenField.setAttribute("value", document.getElementById("rejectreason").value);
					form.appendChild(hiddenField);
					document.body.appendChild(form);
					var popupwin = window.open(url,"PrintRejectionLetter",'resizable=yes,scrollbars=yes,height=700,width=800,status=yes');
					form.submit();			
					popupwin.focus();
				}
			}
      		
      		function submitandclose() {
      			var printcomplete=confirm("Are you sure License Certificate print completed?","YES","NO");
			      if(printcomplete) {
			      	document.getElementById('workflowBean.actionName').value='PrintCompleted';
			      	return true;
			      } else {
			      	return false;
			      }
          	}
      
		</script>
		<% 
			String cityUrl = (String) session.getAttribute("cityurl");
		   	CityWebsiteDAO cityWebsiteDAO = new CityWebsiteDAO();
			CityWebsite cityWebsite = cityWebsiteDAO.getCityWebSiteByURL(cityUrl);
			String cityName = cityWebsite.getCityName();
			String logoName = cityWebsite.getLogo();
		%>
	</head>
	<body onload="refreshInbox()">
		<center>
			<s:form action="viewTradeLicense" theme="css_xhtml" name="viewForm">
			<s:token/>
				<s:hidden name="workflowBean.actionName" id="workflowBean.actionName" />
				<s:hidden name="model.id" />
				<s:push value="model">
					<c:set var="trclass" value="greybox" />
					
					<tbody>
						<tr>
							<td>
								<div align="center">
									<center>
										<div class="formmainbox">
											<div class="headingbg" id="headingdiv">
												<s:text name="page.title.rejecttrade" />
											</div>
											<table width="100%">
												<%@ include file='../../common/view.jsp'%>
												<tr id="duplicateDiv">
													<td class="<c:out value="${trclass}"/>">
														&nbsp;
													</td>
													<td class="<c:out value="${trclass}"/>">
														<s:text name="license.rejectreason" />
														<span class="mandatory">*</span>
													</td>
													<td class="<c:out value="${trclass}"/>" colspan="4">
														<s:textarea name="rejectreason" id="rejectreason" rows="3" cols="80" />
													</td>
												</tr>
											</table>
											<div align="center" class="buttonbottom" id="buttondiv">
												<table>
													<tr>
														<td>
															<input type="button" id="print" value="Print" onclick="return printLicense()" />
														</td>
														<td>
															<s:submit value="PrintCompleted" id="printcmplt" method="approve" onclick="return submitandclose();" />
														</td>
														<td>
															<input type="button" id="close" value="Close" onclick="javascript:window.close();" />
														</td>
													</tr>
												</table>
											</div>
										</div>
									</center>
								</div>
							</td>
						</tr>
					</tbody>					
				</s:push>
			</s:form>
		</center>
	</body>
</html>
