<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>
<head>
<title><s:text name='ratecontract.assignment.header.label' /></title>
</head>
<script src="<egov:url path='js/works.js'/>" type="text/javascript"></script>

<script type="text/javascript">


</script>

<body>
	<div class="errorstyle" id="searchRateContract_error"
		style="display: none;"></div>
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
	<s:form name="rateContractAssignment" id="rateContractAssignment"
		theme="simple">
		<s:hidden name="rowId" id="rowid" />
		<div class="formmainbox">
			<div class="insidecontent">
				<div id="printContent" class="rbroundbox2">
					<div class="rbtop2">
						<div></div>
					</div>
					<div class="rbcontent2">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td>
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<%@ include file='rateContractAssignment-result.jsp'%>

									</table>
								</td>
							</tr>

						</table>

					</div>
					<div class="rbbot2">
						<div></div>
					</div>
					<!-- end of rbroundbox2 -->
				</div>
				<!-- end of insidecontent -->
			</div>
			<!-- end of formmainbox -->
		</div>
	</s:form>
</body>
</html>