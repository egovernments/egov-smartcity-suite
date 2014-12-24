<%@ include file="/includes/taglibs.jsp" %>
<html>
<head>
<title>
	<s:text name="billNoMasterView"/>
</title>
<script type="text/javascript">
function onClickBack()
{
	window.location="${pageContext.request.contextPath}/billNumber/billNumberMaster!newForm.action";	
}
</script>

</head>
<body>
	<s:form name="billNumberMasterViewForm" theme="simple">
	<table width="100%" border="0"  cellpadding="0" cellspacing="0">
			<s:if test="%{!billNumberBeans.isEmpty()}">
				<tr>
					<td class="tablesubheadwk" width="30%">&nbsp;</td>
					<td class="tablesubheadwk" width="20%"><s:text name="billNo"/></td>
					<td class="tablesubheadwk" width="20%"><s:text name="approverPosition"/></td>
					<td class="tablesubheadwk">&nbsp;</td>
				</tr>
				<s:iterator status="billStatus" value="billNumberBeans">
					<tr id="billNumberInfo">
						<td class="whitebox2wk" width="30%">&nbsp;</td>
						<td class="whitebox2wk" width="20%" style="font-size:12px">
							<s:property value="%{billNumberBeans[#billStatus.index].billNumber}"/>
						</td>
						<td class="whitebox2wk" width="20%" style="font-size:12px">
							<s:property value="%{billNumberBeans[#billStatus.index].positionName}" />
						</td>
						<td class="whitebox2wk" width="30%">&nbsp;</td>					
					</tr>
					</s:iterator>
				<tr>
				    <td class="greyboxwk" width="30%">&nbsp;</td>
					<td class="greyboxwk"  width="20%">
						<input type=button value="Back" class="buttonfinal" onCLick="history.back()">
						<s:submit name="close" value="Close" cssClass="buttonfinal" onclick="window.close();"></s:submit>
					</td>
					<td class="greyboxwk" colspan="2" width="30%">&nbsp;</td>
				</tr>
			</s:if>
			<s:else>
				<span class="Bold""><center></span><s:text name="NoRecFound"></s:text></center></span>
			</s:else>
	</table>
	</s:form>
</body>
</html>