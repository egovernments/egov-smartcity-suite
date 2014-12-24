<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<html>
<head>
<title><s:text name="skillMasterSearch.title" /></title>
<script language="JavaScript" type="text/javascript">
	function checkOnSubmit() {
		if (document.getElementById('skillName').value == 0) {
			showError("Please select value for the skill");
			return false;
		} else {
			showError('');
		}

	}

	function showError(msg) {
		document.getElementById("skill_error").style.display = 'none';
		if (document.getElementById("fieldError") != null)
			document.getElementById("fieldError").style.display = 'none';
		dom.get("skill_error").style.display = '';
		document.getElementById("skill_error").innerHTML = msg;
	}
</script>
</head>

<body>
	<div class="formmainbox">
		<div class="insidecontent">
			<div class="rbroundbox2">
				<s:form theme="simple" align="center">
					<div class="mandatory" id="skill_error" style="display: none;"></div>
					<s:if test="%{hasErrors()}">
						<div align="center" class="errorcss" id="fieldError">
							<s:actionerror cssClass="mandatory" />
							<s:fielderror cssClass="mandatory" />
						</div>
					</s:if>
					<table width="95%" cellpadding="0" cellspacing="0" border="0"
						align="center">
						<tbody>
							<tr>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td class="headingwk">
									<div class="arrowiconwk">
										<img
											src="${pageContext.request.contextPath}/common/image/arrow.gif" />
									</div>
									<div class="headplacer">
										<s:text name="skillMaster.headinglbl" />
									</div>
								</td>
							</tr>
						</tbody>
					</table>
					<s:push value="model">
						<table width="95%" cellpadding="0" cellspacing="0" border="0"
							align="center">
							<tbody>

								<tr>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td class="whiteboxwk"><span class="mandatory">*</span> <s:text
											name="skillname.lbl" />&nbsp;</td>
									<td class="whitebox2wk"><s:select name="name"
											id="skillName" headerValue="---------choose---------"
											headerKey="0" list="dropdownData.skillsList" listValue="name"
											listKey="name" /></td>
								</tr>
							</tbody>
						</table>
					</s:push>
					<table align=center>
						<tbody>
							<tr>
								<td>&nbsp;</td>
							</tr>

							<tr>
								<td colspan="4" align="center"><s:submit
										method="viewSkills" value="VIEW" cssClass="buttonfinal"
										onclick="return checkOnSubmit();" /> <s:submit
										method="modifySkills" value="MODIFY" cssClass="buttonfinal"
										onclick="return checkOnSubmit();" /> <s:submit method=""
										onclick="window.close();" value="CLOSE" cssClass="buttonfinal" /></td>
							</tr>
						</tbody>
					</table>
				</s:form>
			</div>
		</div>
	</div>

</body>
</html>