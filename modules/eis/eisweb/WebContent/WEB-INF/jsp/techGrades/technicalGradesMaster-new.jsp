<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="techGrades.title" /></title>
<script language="JavaScript" type="text/javascript">
	function checkOnsubmit() {
		if (document.getElementById("gradeName").value == null
				|| document.getElementById("gradeName").value == "") {
			alert('<s:text name="alertGradeName"/>');
			return false;
		}
		if(document.getElementById('skillId').value == 0) {
			alert('<s:text name="alertSkillName"/>');
			return false;
		}
		if (document.getElementById("fromDate").value == "") {
			alert('<s:text name="alertFromDate"/>');
			return false;
		}
		if (document.getElementById("toDate").value == "") {
			alert('<s:text name="alertToDate"/>');
			return false;
		}
		
		return true;
	}

	function compareDates() {

		if (document.getElementById("toDate").value != "") {
			var toDate = document.getElementById("toDate").value;
			var fromDate = document.getElementById("fromDate").value;

			if (compareDate(toDate, fromDate) == 1
					|| compareDate(toDate, fromDate) == 0) {
				alert('<s:text name="alertCompareDates"/>');
				document.getElementById("toDate").value= null;
				return false;
			} 
		}
	}

	function alphaNumeric(obj) {
		if (obj.value != "") {
			var num = obj.value;
			var objRegExp = /^([a-zA-Z0-9]+)$/i;
			if (!objRegExp.test(num)) {
				alert('<s:text name="alertValidGrade"/>');
				obj.value = "";
				obj.focus();
			} 
		}
	}


function readOnly(){
	<s:if test="%{mode=='view'}">
	{
	document.getElementById("gradeName").disabled  = true;
	document.getElementById("fromDate").disabled =true;
	document.getElementById("toDate").disabled  = true;
   document.getElementById("skillId").disabled  = true;
	
	}
	</s:if>
}

function validateGradeNameUnique(){
	var gname=document.getElementById("gradeName").value;
	if(gname != null || gname != ""){
		var mode=document.getElementById("mode").value;
		<s:if test="%{mode =='save'}">
		populategradeNameUniqueCheck({mode:mode,gradeName:gname});
		</s:if>
		<s:if test="%{mode != 'save'}">
		var gradeId=document.getElementById("id").value;
		populategradeNameUniqueCheck({mode:mode,gradeName:gname,id:gradeId});
		</s:if>
		}
	else
		{
		return false;
		}
}
</script>
</head>
<body onload="readOnly();">
	<div class="mandatory" style="display: none" id="gradeNameUniqueCheck">
		<s:text name="techGrade.name.exists" />
	</div>

	<s:if test="%{hasActionMessages()}">
		<div align="center" class="errorcss">
			<s:actionmessage />
		</div>
	</s:if>

	<s:if test="%{hasErrors()}">
		<div class="errorcss" id="fieldError">
			<s:actionerror cssClass="mandatory" />
			<s:fielderror cssClass="mandatory" />
		</div>
	</s:if>

	<div class="formmainbox">
		<div class="insidecontent">
			<div class="rbroundbox2">
				<s:form theme="simple" align="center">
					<s:push value="model">
						<s:token />
						<s:hidden id="id" name="id" value="%{id}" />
						<s:hidden id="mode" name="mode" value="%{mode}" />
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
											<s:if test="%{mode=='save'}">
												<s:text name="techGrades.headinglabel" />
											</s:if>
											<s:if test="%{mode=='edit'}">
												<s:text name="techGrades.modifylabel" />
											</s:if>
											<s:if test="%{mode=='view'}">
												<s:text name="techGrades.viewlabel" />
											</s:if>
										</div>
									</td>
								</tr>
							</tbody>
						</table>

						<table width="95%" cellpadding="0" cellspacing="0" border="0"
							align="center">
							<tbody>

								<tr>
									<td>&nbsp;</td>
								</tr>

								<tr>
									<td class="whiteboxwk"><span class="mandatory">*</span> <s:text
											name="grade.lbl" /> &nbsp;</td>
									<td class="whitebox2wk"><s:textfield name="gradeName"
											id="gradeName" cssClass="selectwk" value="%{gradeName}"
											onblur="alphaNumeric(this);validateGradeNameUnique();">
											<egov:uniquecheck id="gradeNameUniqueCheck"
												name="gradeNameUniqueCheck" fieldtoreset="gradeName"
												fields="['Value']"
												url='techGrades/technicalGradesMaster!gradeNameUniqueCheck.action' />
										</s:textfield></td>

									<td class="whiteboxwk"><span class="mandatory">*</span> <s:text
											name="skill.lbl" />&nbsp;</td>
									<td class="whitebox2wk"><s:select name="skillId"
											cssClass="selectwk" id="skillId"
											headerValue="      ---------choose---------   " headerKey="0"
											list="dropdownData.skillsList" listValue="name" listKey="id"
											value="%{skillId}" /></td>
								</tr>
								<tr>
									<td class="greyboxwk"><span class="mandatory">*</span> <s:text
											name="fromdate.lbl" />&nbsp;</td>
									<td class="greybox2wk"><s:date name='%{fromDate}'
											format='dd/MM/yyyy' var="fromdate" /> <s:textfield
											name="fromDate" id="fromDate" value="%{fromdate}"
											cssClass="selectwk"
											onkeyup="DateFormat(this,this.value,event,false,'3')"
											onblur="validateDateFormat(this);compareDates();" /></td>

									<td class="greyboxwk" align="center"><span
										class="mandatory">*</span> <s:text name="todate.lbl" />&nbsp;</td>
									<td class="greybox2wk"><s:date name='%{toDate}'
											format='dd/MM/yyyy' var="todate" /> <s:textfield
											name="toDate" id="toDate" value="%{todate}"
											cssClass="selectwk"
											onkeyup="DateFormat(this,this.value,event,false,'3')"
											onblur="validateDateFormat(this);compareDates();" /></td>
								</tr>
								<tr>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td colspan="4"></td>
								</tr>
								<tr>
									<td colspan="4"><div align="right" class="mandatory">*
											Mandatory Fields</div></td>
								</tr>
							</tbody>
						</table>
						<table align="center">
							<tbody>
								<tr>
									<td>&nbsp;</td>
								</tr>

								<tr>
									<td><s:if test="%{mode =='save'}">
											<s:submit method="save" value="CREATE" cssClass="buttonfinal"
												onclick="return checkOnsubmit();" />
											<s:submit method="update" value="VIEW/MODIFY"
												cssClass="buttonfinal" />
										</s:if> <s:if test="%{mode =='edit'}">
											<s:submit method="saveOrUpdate" value="MODIFY"
												cssClass="buttonfinal" onclick="return checkOnsubmit();" />
										</s:if> <s:submit onclick="window.close();" value="CLOSE"
											cssClass="buttonfinal" /></td>
								</tr>
								<tr></tr>
								<tr></tr>
							</tbody>
						</table>
					</s:push>
				</s:form>
			</div>
		</div>
	</div>
</body>
</html>