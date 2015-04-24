<%@ include file="/includes/taglibs.jsp"%>
<head>
<title><s:text name="collectionSummaryReport.title" /></title>
</head>
<body>
	<s:form theme="simple" name="bankRemittanceReportForm"
		action="bankRemittanceReport!report.action">
		<div class="formmainbox">
			<div class="subheadnew">
				<s:text name="bankRemittanceReport.title" />
			</div>
			<div class="subheadsmallnew">
				<span class="subheadnew"><s:text
						name="collectionReport.criteria" /> </span>
			</div>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">

				<tr>
					<td width="35%" class="bluebox">
						&nbsp;
					</td>
					<td width="10%" class="bluebox">
						<s:text name="collectionReport.criteria.dept" />
					</td>
					<td width="25%" class="bluebox">
						<s:select name="deptId" id="dept" cssClass="selectwk"
							list="dropdownData.departmentList" listKey="id"
							listValue="deptName" value="%{deptId}" />
					</td>
					<td width="30%" class="bluebox">
						&nbsp;
					</td>
				</tr>

			</table>
		</div>
		<div class="buttonbottom">
			<label>
				<s:submit type="submit" cssClass="buttonsubmit" id="button"
					value="%{getText('collectionReport.create')}" />
			</label>&nbsp;
			<label>
				<s:reset type="submit" cssClass="button"
					value="%{getText('collectionReport.reset')}" />
			</label>&nbsp;
			<label>
				<input type="button" class="button" id="buttonClose"
					value="<s:text name='common.buttons.close'/>"
					onclick="window.close()" />
			</label>
		</div>
	</s:form>
</body>
</html>
