<%@ include file="/includes/taglibs.jsp"%>
<html>
	<title><s:text name="egsEduCessCollReport"/></title>
	<body>
	<div align="left">
  		<s:actionerror/>
  	</div>
	<div class="errorstyle" id="property_error_area" style="display:none;"></div>
	<div class="formmainbox">
		<table border="0" cellspacing="0" cellpadding="0" width="100%">
			<s:form action="/reports/egsEduCessCollectionReport!generateReport.action" name="egsEduCessCollectionReportForm" theme="simple">
				<div class="formheading"></div>
				<tr>
					<td width="100%" colspan="3" class="headingbg">
						<div class="headingbg">
							<s:text name="egsEduCessCollReport" />
						</div>
					</td>
				</tr>
				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox">
						<table align="center">
							<td class="bluebox"><s:text name="day" /></td>
							<td class="bluebox">
									<s:date name="day" format="dd/MM/yyyy" var="d"/>
									<s:textfield name="day" id="day" maxlength="10" size="10" value="%{d}"/>
									<a href="javascript:show_calendar('egsEduCessCollectionReportForm.day',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;
									<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>
							</td>
						</table>
					</td>
					<td class="bluebox">&nbsp;</td>
				</tr>
				<tr>
					<td class="greybox">&nbsp;</td>
					<td class="greybox">
						<table align="center">
							<td class="greybox"><s:text name="month" /></td>
							<td class="greybox"><s:select list="monthsMap" listKey="key" listValue="value"
								headerKey="-1" headerValue="--Choose--" id="month" name="month"
								value="%{month}" />
							</td>
						</table>
					</td>
					<td class="greybox">&nbsp;</td>
				</tr>
				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox">
						<table align="center">
							<td  class="bluebox"><s:text name="year" /></td>
							<td  class="bluebox"><s:select list="dropdownData.yearsList" listKey="description"
								listValue="description" headerKey="-1" headerValue="--Choose--"
								id="year" name="year" value="%{year}" />
							</td>
						</table>
					</td>
					<td class="bluebox">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="3">
						<div class="buttonbottom" align="center">
							<s:submit name="search" value="Get Report" cssClass="buttonSubmit"/>
							<input type="button" name="close" value="Close" class="button" onclick="return confirmClose();"/>
						</div>
					</td>
				</tr>
			</s:form>
		</table>
	</div>
	<s:if test="%{!recordsExist}">
		<div>
			<s:text name="noRecsFound"/>
		</div>
	</s:if>
	</body>
</html>