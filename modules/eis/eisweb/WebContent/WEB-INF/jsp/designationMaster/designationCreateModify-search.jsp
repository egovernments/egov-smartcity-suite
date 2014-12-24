<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>
<head>
<title><s:text name="Designation" /></title>

<script type="text/javascript">
function showError(msg)
{
	document.getElementById("designationCreateModify_error").style.display='none';
	if(document.getElementById("fieldError")!=null)
		document.getElementById("fieldError").style.display='none';
	dom.get("designationCreateModify_error").style.display = '';
	document.getElementById("designationCreateModify_error").innerHTML = msg;
}

function validateForm()
{
	if (document.getElementById('designationId').value == 0)
	{				
		showError("Please select a designation.");
		return false;
	}
return true;


	}

</script>
</head>
<body>
	<div class="formmainbox">
		<div class="insidecontent">
			<div class="rbroundbox2">
				<s:form theme="simple">
					<div class="rbcontent2">
					<div class="mandatory" id="designationCreateModify_error"
				style="display: none;"></div>
				<s:if test="%{hasErrors()}">
				<div class="errorcss" id="fieldError">
					<s:actionerror cssClass="mandatory" />
					<s:fielderror cssClass="mandatory" />
				</div></s:if>
						<table align="center" width="100%" cellpadding="0" cellspacing="0"
							border="0">
							<tbody>
								<tr>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td class="whiteboxwk"><span class="mandatory">*</span>Choose
										Designation:
									<td>
									<td class="whiteboxbox2wk"><s:select cssClass="greybox2wk"
											name="designationId" id="designationId"
											headerValue="-------choose-------" headerKey="0"
											list="designations" listKey="designationId"
											listValue="designationName" value="%{designationId}" /></td>
								</tr>
							</tbody>
						</table>

						<table width="100%" cellpadding="0" cellspacing="0" border="0">
							<tbody>
								<tr>
									<td align="right">
										<div class="mandatory">* Mandatory Fields</div>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					<table align="center">
						<tbody>
							<tr>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<s:if test="%{mode!='view'}">
									<td class="whiteboxwk" align="center"><s:submit
									onclick="return validateForm();"		cssClass="buttonfinal" method="modifyDesignation"
											value="Modify" /></td>
								</s:if>
								<s:if test="%{mode=='view'}">
									<td class="whiteboxwk" align="center"><s:submit
									onclick="return validateForm();"		cssClass="buttonfinal" method="viewDesignation" value="VIEW">
										</s:submit></td>
								</s:if>
							</tr>
						</tbody>
					</table>
				</s:form>
			</div>
		</div>
	</div>
</body>
</html>