<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>

<html>  
<head>  
    <title><s:text name="download.template.estimate" /></title>  
</head>  
	<body>
		<table width="400" border="0" cellpadding="0" cellspacing="0" align="center">
			<tr >
				<td width="15%" class="tablesubheadwka">#</td>
				<td width="65%" class="tablesubheadwka"><s:text name="column.title.Name" /></td>
				<td width="20%" class="tablesubheadwka">&nbsp;</td>
			</tr>
			<tr>
				<td width="15%" class="whitebox3wka">1</td>
				<td width="65%" class="whitebox3wka">Enclosures forms for Estimates - BRR</td>
				<td width="20%" class="whitebox3wka">
					<a href="${pageContext.request.contextPath}/downloads/Enclosure_forms_for_Estimate_BRR.xls"><s:text name="download.template.estimate" /></a>
				</td>
			</tr>
			<tr>
				<td width="15%" class="whitebox3wka">2</td>
				<td width="65%" class="whitebox3wka">Enclosures forms for Estimates - Bridges</td>
				<td width="20%" class="whitebox3wka">
					<a href="${pageContext.request.contextPath}/downloads/Enclosure_forms_for_Estimate_Bridges.xls"><s:text name="download.template.estimate" /></a>
				</td>
			</tr>
			<tr>
				<td width="15%" class="whitebox3wka">3</td>
				<td width="65%" class="whitebox3wka">Enclosure forms for Estimates - Zones</td>
				<td width="20%" class="whitebox3wka">
					<a href="${pageContext.request.contextPath}/downloads/Enclosure_forms_for_Estimate_Zone.xls"><s:text name="download.template.estimate" /></a>
				</td>
			</tr>
			<tr>
				<td width="15%" class="whitebox3wka">4</td>
				<td width="65%" class="whitebox3wka">Enclosures forms for Estimates - SWD</td>
				<td width="20%" class="whitebox3wka">
					<a href="${pageContext.request.contextPath}/downloads/Enclosure_forms_for_Estimate_SWD.xls"><s:text name="download.template.estimate" /></a>
				</td>
			</tr>
			<tr>
				<td width="15%" class="whitebox3wka">5</td>
				<td width="65%" class="whitebox3wka">Enclosures forms for Estimates - Electrical</td>
				<td width="20%" class="whitebox3wka">
					<a href="${pageContext.request.contextPath}/downloads/Enclosure_forms_for_Estimate_Electrical.xls"><s:text name="download.template.estimate" /></a>
				</td>
			</tr>
			<tr>
				<td width="15%" class="whitebox3wka">6</td>
				<td width="65%" class="whitebox3wka">Enclosures forms for Estimates - Buildings</td>
				<td width="20%" class="whitebox3wka">
					<a href="${pageContext.request.contextPath}/downloads/Enclosure_forms_for_Estimate_Buildings.xls"><s:text name="download.template.estimate" /></a>
				</td>
			</tr>
			<!-- tr>
				<td width="15%" class="whitebox3wka">2</td>
				<td width="65%" class="whitebox3wka">Last Page for Estimate</td>
				<td width="20%" class="whitebox3wka">
					<a href="${pageContext.request.contextPath}/downloads/Last_Page_of_Estimate.xls"><s:text name="download.template.estimate" /></a>
				</td>
			</tr-->
		</table>
	</body>  
</html>
