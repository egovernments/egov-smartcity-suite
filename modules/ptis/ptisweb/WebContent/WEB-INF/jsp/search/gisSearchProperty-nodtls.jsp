<%@ include file="/includes/taglibs.jsp"%>

<html>
	<head>
	<title><s:text name="gisSearchResult.title" /></title>
	<script type="text/javascript">
	function clearSearch(mgSession,mode)
		{
		var gisVer='<s:property value="%{gisVersion}"/>';
		var gisCity='<s:property value="%{gisCity}"/>';
		var gisUrl=gisVer+gisCity;
		parent.parent.formFrame.Submit(gisUrl+"/clearfindresults.jsp?SESSION="+mgSession+"&DomainName="+gisCity+"&mode="+mode,null,"scriptFrame");
		}
	</script>
</head>
<body>
<div class="formmainbox">
			<table width="320px" border="0">
			<s:form name="viewform" theme="simple">
			<tr>
							<td align="center">
								<span class="mandatory"> <s:text name="noRecFound"></s:text></span>
							</td>
						</tr>
						<input type="button" value="Search Again" class="button"
						onclick="clearSearch('${SESSION}','${mode}');" />
						
			</s:form>
			</table>
			</div>
			
</body>
</html>