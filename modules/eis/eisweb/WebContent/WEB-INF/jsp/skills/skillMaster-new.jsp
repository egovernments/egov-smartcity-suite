<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<html>
<head>
<title><s:text name="skillMaster.title" /></title>
<script language="JavaScript" type="text/javascript">

function alphaNumeric(obj){
	if(obj.value!=""){
		var num=obj.value;
		var objRegExp  = /^([a-zA-Z0-9]+)$/i;
		if(!objRegExp.test(num)){
			showError('Please Enter Valid Skill');
			obj.value="";
			obj.focus();
			}
		else
		{
		showError('');
		}
}
	}

function validateNameUniqueCheck(obj){
	var sname=obj.value;
	if(sname != null || sname != ""){
	var mode = document.getElementById('mode').value;
	<s:if test="%{mode !='edit'}">
	populatenameUniqueCheck({name:sname,mode:mode});
	</s:if>
	 <s:if test="%{mode =='edit'}">
	  var skillId = document.getElementById('id').value;
	  populatenameUniqueCheck({name:sname,mode:mode,id:skillId});
	 </s:if>
	}
	else {
		return false;
		}
	
}

function viewSkills(){
	<s:if test="%{mode=='view'}">
	{
	document.getElementById("name").disabled  = true;
	document.getElementById("fromDate").disabled =true;
	document.getElementById("toDate").disabled  = true;
	}
	</s:if>
}

function showError(msg)
{
	document.getElementById("skill_error").style.display='none';
	if(document.getElementById("fieldError")!=null)
		document.getElementById("fieldError").style.display='none';
	dom.get("skill_error").style.display = '';
	document.getElementById("skill_error").innerHTML = msg;
}

function checkMandatory(){
	if(document.getElementById("name").value == null || document.getElementById("name").value == ""){
		showError('Please Enter Skill Name');
		return false;
		}
	if(document.getElementById("fromDate").value == ""){
		showError('Please Enter Valid Fromdate');
		return false;
		}
	if(document.getElementById("toDate").value == ""){
		showError('Please Enter Valid Todate');
		return false;
		}
	return true;
}
function compareDates(){

	if(document.getElementById("toDate").value != ""){
		var toDate=document.getElementById("toDate").value;
		var fromDate=document.getElementById("fromDate").value;
		
		if(compareDate(toDate,fromDate) == 1 || 
				compareDate(toDate,fromDate) == 0){
			showError('EndDate should be greater than FromDate');
			return false;
			}
		else{
			showError('');
			}
		}
}
</script>
</head>
<body onload="viewSkills()">
	<div class="mandatory" id="skill_error" style="display: none;"></div>

	<div class="mandatory" style="display: none" id="nameUniqueCheck">
		<s:text name="skill.name.exists" />
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
												<s:text name="skillMaster.headinglbl" />
											</s:if>
											<s:if test="%{mode=='edit'}">
												<s:text name="Modify Skill" />
											</s:if>
											<s:if test="%{mode=='view'}">
												<s:text name="View Skill" />
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
											name="skillname.lbl" />&nbsp;</td>
									<td class="whitebox2wk"><s:textfield name="name" id="name"
											cssClass="selectwk"
											onblur="alphaNumeric(this);validateNameUniqueCheck(this);" />
										<egov:uniquecheck id="nameUniqueCheck" name="nameUniqueCheck"
											fieldtoreset="name" fields="['Value']"
											url='skills/skillMaster!nameUniqueCheck.action' /></td>
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
								</tr>

								<tr>
									<td class="whiteboxwk" align="center"><span
										class="mandatory">*</span> <s:text name="todate.lbl" />&nbsp;</td>
									<td class="whitebox2wk"><s:date name='%{toDate}'
											format='dd/MM/yyyy' var="todate" /> <s:textfield
											name="toDate" id="toDate" value="%{todate}"
											cssClass="selectwk"
											onkeyup="DateFormat(this,this.value,event,false,'3')"
											onblur="validateDateFormat(this);compareDates();" /></td>
								</tr>
								<tr>
									<td colspan="4" class="shadowwk"></td>
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
												onclick="return checkMandatory();" />
											<s:submit method="update" value="VIEW/MODIFY"
												cssClass="buttonfinal" />
										</s:if> <s:if test="%{mode =='edit'}">
											<s:submit method="saveOrUpdate" value="MODIFY"
												cssClass="buttonfinal" onclick="return checkMandatory();" />

										</s:if> <s:submit method="" onclick="window.close();" value="CLOSE"
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