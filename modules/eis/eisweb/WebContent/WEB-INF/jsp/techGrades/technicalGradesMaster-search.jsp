<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="techGrades.title" /></title>
<script language="JavaScript" type="text/javascript">
	function checkOnSubmit() {
		if (document.getElementById('gradeName').value == 0) {
			alert('<s:text name="alertSearchGrade"/>');
			return false;
		} 
	}


</script>
</head>
<body>
	<div class="formmainbox">
		<div class="insidecontent">
			<div class="rbroundbox2">
				<div class="mandatory" id="techGrade_error" style="display: none;"></div>

				<s:if test="%{hasErrors()}">
					<div class="errorcss" id="fieldError">
						<s:actionerror cssClass="mandatory" />
						<s:fielderror cssClass="mandatory" />
					</div>
				</s:if>


				<s:form theme="simple" align="center">

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
										<s:text name="techGrades.headinglabel" />
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
											name="grade.lbl" />&nbsp;</td>
									<td class="whitebox2wk"><s:select name="gradeName"
											id="gradeName" cssClass="selectwk"
											headerValue="---------choose---------" headerKey="0"
											list="dropdownData.techGradesList" listValue="gradeName"
											listKey="gradeName" /></td>
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
										method="viewTechGrades" value="VIEW" cssClass="buttonfinal"
										onclick="return checkOnSubmit();" /> <s:submit
										method="modifyTechGrades" value="MODIFY"
										cssClass="buttonfinal" onclick="return checkOnSubmit();" /> <s:submit
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
