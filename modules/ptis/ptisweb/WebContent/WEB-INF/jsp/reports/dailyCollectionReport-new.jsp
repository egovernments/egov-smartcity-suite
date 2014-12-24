<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>
			<s:text name='ptis.collectionReport.title' />
		</title>
		<script type="text/javascript">
		function checkBeforeSubmit() {
			var fromDate = document.getElementById("fromDate").value;
			var toDate = document.getElementById("toDate").value;
			var userId = document.getElementById("userId").value;

			if (fromDate == null || fromDate == "" || fromDate == 'DD/MM/YYYY') {
				alert('From Date is mandatory');
				return false;
			}
			
			if (toDate == null || toDate == "" || toDate == 'DD/MM/YYYY') {
				alert('To Date is mandatory');
				return false;
			}

			if (userId == null || userId == "" || userId == "-1") {
				alert('Select Operator');
				return false;
			}
			
			if ((fromDate != null && fromDate != "") && (toDate != null && toDate != "")) {
				if (validateFromAndToDate(fromDate,toDate))	{
					return true;
				} else {
					return false;
				}
			} else {
				return true;
			}
			
		}
		</script>
	</head>

	<body>
		<s:form name="dailyCollectionReportSearch" action="dailyCollectionReport!generateReport.action" theme="simple">
			<s:actionerror/>
			<div class="formmainbox">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<div class="formheading"></div>
					<tr>
						<td width="100%" colspan="6" class="headingbg">												
							<div class="headingbg">					
								<s:text name="ptis.collectionReport.title" />									
							</div>									
						</td>
					</tr>
					<tr>
						<td colspan="6" class="greybox">&nbsp;</td>
					</tr>
					<tr>
						<td class="bluebox" >&nbsp;</td>
						<td class="bluebox" >
							<s:text name="fromDate"/><span class="mandatory">*</span>
						</td>
						<s:date name="fromDate" var="cdFormat" format="dd/MM/yyyy" />
						<td class="bluebox">
							<s:textfield id="fromDate"
							name="fromDate" value="%{cdFormat}"
							onkeyup="DateFormat(this,this.value,event,false,'3')"
							onblur="validateDateFormat(this);" />
							<a href="javascript:show_calendar('forms[0].fromDate');"
							onmouseover="window.status='Date Picker';return true;"
							onmouseout="window.status='';return true;">
							<img src="${pageContext.request.contextPath}/images/calendaricon.gif"
							alt="Date" width="18" height="18" border="0" align="absmiddle" /></a>
							<div class="highlight2" style="width: 80px">DD/MM/YYYY</div>
						</td>
						<td class="bluebox">
							<div style="text-align:center">
								<s:text name="toDate" /><span class="mandatory">*</span>
							</div>
						</td>
						<s:date name="toDate" var="cdFormat1" format="dd/MM/yyyy" />
						<td class="bluebox">
							<s:textfield id="toDate" name="toDate" value="%{cdFormat1}"
							onkeyup="DateFormat(this,this.value,event,false,'3')"
							onblur="validateDateFormat(this);" />
							<a href="javascript:show_calendar('forms[0].toDate');"
							onmouseover="window.status='Date Picker';return true;"
							onmouseout="window.status='';return true;">
							<img src="${pageContext.request.contextPath}/images/calendaricon.gif"
							alt="Date" width="18" height="18" border="0" align="absmiddle" /></a>
								<div class="highlight2" style="width: 80px">DD/MM/YYYY</div>
						</td>
						<td class="bluebox">&nbsp;</td>
					</tr>
					<tr>
						<td class="greybox">&nbsp;</td>
						<td class="greybox">
							<s:text name="operator"/><span class="mandatory">*</span>
						</td>
						<td class="greybox">
							<s:select list="dropdownData.userList" listKey="id" listValue="userName" cssClass="selectnew"
								headerKey="-1" headerValue="%{getText('default.select')}" name="userId" value="%{userId}" id="userId"/>
						</td>
						<td class="greybox">
							&nbsp;
						</td>
						<td colspan="2" class="greybox">&nbsp;</td>
					</tr>
			</table>
			<tr>
        		<font size="2"><div align="left" class="mandatory"><s:text name="mandtryFlds"/></div></font>
        	</tr>
		</div>
		<div class="buttonbottom" align="center">
			<tr>
				<td>
					<s:submit name="submit" value="generate Report" id="search" method="generateReport" 
						cssClass="buttonsubmit" onclick="return checkBeforeSubmit();"></s:submit>
					<input name="buttonClose" type="button" class="button"
						id="buttonClose" value="Close" onclick="window.close()" />
						&nbsp;
				</td>
			</tr>
		</div>
		</s:form>
		<s:if test="%{!searchForm}">
			<div>
				<s:text name="noRecsFound"></s:text>	
			</div>		
		</s:if>
	</body>
</html>