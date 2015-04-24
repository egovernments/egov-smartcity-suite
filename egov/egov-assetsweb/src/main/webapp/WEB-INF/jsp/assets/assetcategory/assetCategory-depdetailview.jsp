<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<script>
</script>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
	<td class="headingwk">
		<div class="arrowiconwk">
			<img src="${pageContext.request.contextPath}/image/arrow.gif" />
		</div>
		<div class="headplacer">
			<s:text name='title.dep.details' />
		</div>
	</td>
</tr>
<tr>
<td>
<table width="50%" align="center" border="0" cellspacing="0" cellpadding="0">
	
	<tr>
		<td width="20%" class="tablesubheadwk">
			Sr No.
		</td>
		<td width="20%" class="tablesubheadwk">
			Year
		</td>
		<td width="20%" class="tablesubheadwk">
			Rate
		</td>
	</tr>
	<table align="center" width="50%" border="0" cellspacing="0" cellpadding="0">
				<s:iterator id="depIterator" value="depreciationMetaDatas"
					status="row_status">
					<tr>
						<td width="20%"">
							<s:property value="#row_status.count" />
							&nbsp;
						</td>
						<td width="20%">
							<s:property value="%{financialYear.finYearRange}" />
							&nbsp;
						</td>
						<td width="20%"">
							<s:property value="%{depreciationRate}" />
							&nbsp;
						</td>
					</tr>
				</s:iterator>
			</table>
	</table>
</td>
</tr>
</table>