<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Employee Probation Report</title>
<script type="text/javascript">
function datemandatory(obj) {
	
		if (""== document.getElementById("probationDate").value) {
			document.getElementById("emp_error").style.visibility="visible";
			showError('  ' + '<s:text name="date.is.mandatory"/>');
			document.getElementById("probationDate").focus();
			return false;
		}
		document.getElementById("emp_error").style.visibility="hidden";
	}


	function showError(msg)
	{
		document.getElementById("emp_error").style.display = '';	
		dom.get("emp_error").innerHTML = msg;
		window.scroll(0,0);
		return false;
	} 
</script>
</head>
<body>
	<div class="errorcss" id="emp_error" style="display: none;" ></div>
		<div class="errorstyle" id="report_error" style="display: none;"></div>
		</div>
		<s:if test="%{hasErrors()}">
			<div class="errorMessage" id="fieldError">
				<s:actionerror cssClass="errorMessage" />
				<s:fielderror cssClass="errorMessage" />
			</div>
		</s:if>

		<s:if test="%{hasActionMessages()}">
			<div class="errorMessage">
				<s:actionmessage />
			</div>
		</s:if>
		<div class="formmainbox">
			<div class="insidecontent">
				<div class="rbroundbox2">
					<s:form theme="simple">
						<center>
							<table width="100%" cellpadding="0" cellspacing="0" border="0">
								<tbody>
									<tr>
										<td colspan="20" class="headingwk"><div
												class="arrowiconwk">
												<img src="../common/image/arrow.gif" />
											</div>
											<p>
											<div class="headplacer">
												<s:text name="search.emp.under.prob" />
											</div>
											</p></td>
										<td></td>
									</tr>
								</tbody>
							</table>
						</center>

						<table border="0" width="95%" cellpadding="0" cellspacing="0"
							align="center">

							<tbody>
								<tr>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td class="whiteboxwk" width="20%"></td>
									<td class="whiteboxwk"><span class="mandatory">*</span> <s:text
											name="as.of.date" />:</td>
									<td class="whitebox2wk" width="20%"><s:date
											name="probationDate"
											var="probationDate" format="dd/MM/yyyy" />
										<s:textfield id="probationDate"
											name="probationDate"
											value="%{probationDateValue}"
											onkeyup="DateFormat(this,this.value,event,false,'3')"
											onblur="datemandatory();validateDateFormat(this);" /> &nbsp;
										&nbsp; &nbsp;&nbsp; <a name="dateFromAnchor"
										id="dateFromAnchor"
										href="javascript:show_calendar('forms[0].probationdate');"
										onmouseover="window.status='Date Picker';return true;"
										onmouseout="window.status='';return true;"> <img
											src="<%=request.getContextPath()%>/common/image/calendar.png"
											border="0" height="10%">
									</a></td>
									<td class="whiteboxwk"><s:text name="deptmnt" />:</td>
									<td class="whitebox2wk"><s:select id="departmentId"
											name="departmentId" value="%{departmentId}" headerKey="0"
											headerValue="--Choose--" cssClass="selectwk"
											list="dropdownData.departmentlist" listKey="id"
											listValue="deptName" /></td>
									<td class="whiteboxwk" width="20%"></td>
								</tr>
<tr>
<td class="greyboxwk"> </td>
<td class="greyboxwk"> </td><td class="greyboxwk"> </td>
<td class="greyboxwk"> </td><td class="greyboxwk"> </td>
 <td class="greyboxwk"><div align="right" class="mandatory">* Mandatory Fields</div></td></tr>

							</tbody>
						</table>
						<br />

	
				
						<table align="center">
							<tbody>
								<tr>
									<td><s:submit id="submit" cssClass="buttonfinal"
											onclick="return datemandatory();" name="submit" value="Submit"
											method="getEmpsUnderProbation" /></td>
									<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
									<td><input type="button" name="closeBut" id="closeBut"
										value="Close" onclick="window.close();" class="buttonfinal" />
									</td>
								</tr>
							</tbody>
						</table>
						<br/>
					</s:form>
				
				</div>
			</div><s:if test="searchResult!=null">
				
				<br />
				<display:table name="searchResult" class="its" uid="currentRowObject" style="color:blue;">
				<display:setProperty name='css.tr.even' value='data_even'/>
        <display:setProperty name='css.tr.odd' value='data_odd'/>
					<display:column style="width:2%" title="Sl No">
						<s:property value="%{#attr.currentRowObject_rowNum}" />
					</display:column>
					<display:column title="Employee Code">
						<c:if test="${currentRowObject[0]!= null}">
							<c:out value="${currentRowObject[0]}" />
						</c:if>
						<c:if test="${currentRowObject[0]== null}">
							<c:out value="-" />
						</c:if>
					</display:column>

					<display:column title="Employee Name">
						<c:if test="${currentRowObject[1]!= null}">
							<c:out value="${currentRowObject[1]}" />
						</c:if>
						<c:if test="${currentRowObject[1]== null}">
							<c:out value="-" />
						</c:if>
					</display:column>

					<display:column title="Department">
						<c:if test="${currentRowObject[2]!=null }">
							<c:out value="${currentRowObject[2]}" />
						</c:if>
						<c:if test="${currentRowObject[2]== null}">
							<c:out value="-" />
						</c:if>
					</display:column>

					<display:column title="Probation From Date">
						<c:if test="${currentRowObject[3]!=null }">
							<fmt:formatDate value="${currentRowObject[3]}"
								pattern="dd/MM/yyyy" var="fromdatevar" />
							<c:out value="${fromdatevar}" />
						</c:if>
						<c:if test="${currentRowObject[3]== null}">
							<c:out value="-" />
						</c:if>
					</display:column>

					<display:column title="Probation To Date">
						<c:if test="${currentRowObject[4]!=null }">
							<fmt:formatDate value="${currentRowObject[4]}"
								pattern="dd/MM/yyyy" var="todatevar" />
							<c:out value="${todatevar}" />
						</c:if>
						<c:if test="${currentRowObject[4]== null}">
							<c:out value="-" />
						</c:if>
					</display:column>
				</display:table>
		</s:if>
		</div>

		
</body>
</html>