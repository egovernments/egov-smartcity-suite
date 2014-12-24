<%@ include file="/includes/taglibs.jsp"%>
<html>
<head>
<title><s:text name="license.search" /></title>
<link href="../../css/license/searchElectrical.css" rel="stylesheet"
	type="text/css"></link>
<script type="text/javascript">
function validateForm() {
	if(document.getElementById("applNumber").value == '' && document.getElementById("licenseNumber").value == '')
	{
		document.getElementById("error").innerHTML = '<ul><li><span class="errorMessage" style="color: #FF0000">Please Enter either Application Number or License Number or Both to Search License for OnLine Payment</li></ul>';
		return false;
	}
	return true;
}
function validateAppNumber(appNumberField) {
	var appNumberValue =appNumberField.value;	  
	 var regexPattern=/^[A-Z a-z]{2}\-[APLapl]{4}\d{10}$/;
	if(!regexPattern.test(appNumberValue)) {
	  alert('Please enter a valid Application Number');
	  appNumberField.value = "";
	  return false;
	}
}

function validateLicenseNumber(licenseNumberField) {
	var licenseNumberValue =licenseNumberField.value;	  
	 var regexPattern=/^[A-Z a-z]{3}\/\d{10}\/[A-Z a-z]{3}\-\d{4}$/;
	if(!regexPattern.test(licenseNumberValue)) {
	  alert('Please enter a valid License Number');
	  licenseNumberField.value = "";
	  return false;
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
									<s:text name="license.search" />
								</div>
									<s:form action="searchCitizenLicense" theme="simple"
										name="searchCitizenLicenseForm">
										<div id="error" style="color: #FF0000"></div>
										<table border="0" cellpadding="0" cellspacing="0" width="100%"
											align="center">
											<tbody>
												<tr>
												<td class="bluebox"></td>
												<td class="bluebox"><b> <s:text name="license.enterapplicationnumber"/></b></td>
												<td class="bluebox"><s:textfield name="applNumber" id="applNumber" onblur="validateAppNumber(this);"/></td>
												<td class="bluebox"><b><s:text name="license.enterlicensenumber"/></b></td>
												<td class="bluebox"><s:textfield name="licenseNumber" id="licenseNumber" onblur="validateLicenseNumber(this);"/></td>
											</tr>
											</tbody>
										</table>
										<div class="buttonbottom">
											<s:submit name="button32" onclick="return validateForm()"
												cssClass="buttonsubmit" id="button32" method="search"
												value="Search" />
											<s:reset name="button" cssClass="button" id="button"
												value="Reset" />
											<input name="button2" type="button" class="button"
												id="button" onclick="window.close()" value="Close" />
										</div>
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