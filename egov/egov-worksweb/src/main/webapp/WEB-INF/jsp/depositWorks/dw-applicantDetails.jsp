<script>
function validateMobileNumber(obj)
{
	var text = obj.value;
	if(text=='')
		return;
	if(text.length!=10)
	{
		alert('<s:text name="depositworks.roadcut.invalid.mobileno.length" />');
		obj.value="";
	}
	validatePhoneNumber(obj,'mobile');
}
function validatePhoneNumber(obj,mode){
	var text = obj.value;
	if(text=="")
		return;
	var msg;
	if(mode=='mobile')
		msg='<s:text name="depositworks.roadcut.invalid.mobileno" />';
	else
		msg='<s:text name="depositworks.roadcut.invalid.teleno" />';
	if(isNaN(text))
	{
		alert(msg);
		obj.value="";
		return;
	}
	if(text<=0)
	{
		alert(msg);
		obj.value='';
		return;
	}
	if(text.replace(".","~").search("~")!=-1)
	{
		alert('<s:text name="depositworks.roadcut.period.notallowed" />');
		obj.value='';
		return;
	}
	if(text.replace("+","~").search("~")!=-1)
	{
		alert('<s:text name="depositworks.roadcut.plus.notallowed" />');
		obj.value='';
		return;
	}
}	
function validatePinCode(obj)
{
	var text = obj.value;
	if(text=="")
		return;
	if(text.length!=6)
	{
		alert('<s:text name="depositworks.roadcut.invalid.pincode.length" />');
		obj.value="";
		return;
	}
	if(isNaN(text))
	{
		alert('<s:text name="depositworks.roadcut.invalid.pincode" />');
		obj.value="";
		return;
	}
	if(text<=0)
	{
		alert('<s:text name="depositworks.roadcut.invalid.pincode" />');
		obj.value='';
		return;
	}
	if(text.replace(".","~").search("~")!=-1)
	{
		alert('<s:text name="depositworks.roadcut.period.notallowed" />');
		obj.value='';
		return;
	}
	if(text.replace("+","~").search("~")!=-1)
	{
		alert('<s:text name="depositworks.roadcut.plus.notallowed" />');
		obj.value='';
		return;
	}
}
function showIndvidualOrOrg(obj)  
{     
	
	var bpaCheckboxStatus = document.getElementById("depositWorksCategoryBPA").checked;
	document.getElementById("mirrorApplicantOrgznServDeptTR").style.display='none';
	if(obj.value=='true')     
	{
		 jQuery(document).ready( jQuery('#depositWorksCategoryREPAIRSANDMAINTENANCE').hide());
		 jQuery(document).ready( jQuery('label[for="depositWorksCategoryREPAIRSANDMAINTENANCE"]').hide());
		 jQuery(document).ready( jQuery('#depositWorksCategoryEMERGENCYCUT').hide());
		 jQuery(document).ready( jQuery('label[for="depositWorksCategoryEMERGENCYCUT"]').hide());
		document.getElementById('organizationElement').style.display='none';
		document.getElementById('organizationLabel').style.display='none';
		if(bpaCheckboxStatus)
		{
			document.getElementById("newOrgznServiceDeptTR").style.display='';
			document.getElementById("serviceDeptTR").style.display='none';
		}
		else
		{
			document.getElementById("newOrgznServiceDeptTR").style.display='none';
			document.getElementById("serviceDeptTR").style.display='none';	
		}			
		document.getElementById('individualElement').style.display='';
		document.getElementById('individualLabel').style.display='';
	}
	else
	{
		 jQuery(document).ready( jQuery('#depositWorksCategoryREPAIRSANDMAINTENANCE').show());
		 jQuery(document).ready( jQuery('label[for="depositWorksCategoryREPAIRSANDMAINTENANCE"]').show());
		 jQuery(document).ready( jQuery('#depositWorksCategoryEMERGENCYCUT').show());
		 jQuery(document).ready( jQuery('label[for="depositWorksCategoryEMERGENCYCUT"]').show());
		document.getElementById('organizationElement').style.display='';
		document.getElementById('organizationLabel').style.display='';
		if(bpaCheckboxStatus)  
		{
			document.getElementById("serviceDeptTR").style.display='none';
			document.getElementById("newOrgznServiceDeptTR").style.display='none';
			document.getElementById("mirrorApplicantOrgznServDeptTR").style.display='';			
		}
		else
		{
			document.getElementById("serviceDeptTR").style.display='';
			document.getElementById("newOrgznServiceDeptTR").style.display='none';			
		}		
		document.getElementById('individualElement').style.display='none';
		document.getElementById('individualLabel').style.display='none';	
	}			
}
function changeServiceDept(obj)
{
	document.getElementById("serviceDept").value=obj.value;
}
function setupServicedept(elem){
	var orgId = elem.value;
   	populateservicedept({organizationId:orgId});
}
function setTypeOfCut(elem,fromOnChange)
{
	var typeOfCutObj = document.getElementById("typeOfCut");
	var orgznId='';
	var typeOfCutId='';
	var elemOrgznId;
	if(elem==null|| elem=='')
		return;
	typeOfCutObj.disabled=false;
	if(fromOnChange)
		elemOrgznId  = elem.value;
	else
		elemOrgznId  = elem;
	<s:iterator id="typesOfRoadCutList" value="dropdownData.typesOfRoadCut" status="typeOfCut_Status">
		orgznId="<s:property value='organization.id'/>";
		typeOfCutId="<s:property value='id'/>";
		if(orgznId==eval(elemOrgznId))
		{
			typeOfCutObj.value=eval(typeOfCutId);
			showOrHideSchemeDetails(typeOfCutObj);
			typeOfCutObj.disabled=true;		
		}	
	</s:iterator>
}
function mirrorOrgzn(obj)
{
	var mirror = document.getElementById("mirrorApplicantOrgzn");
	if(mirror)
		mirror.value=obj.value;
	//In case of resetting
	var mirrorSD = document.getElementById("mirrorApplicantServDept");
	if(mirrorSD)
		mirrorSD.value=-1;
}
function mirrorServDept(obj)
{
	var mirror = document.getElementById("mirrorApplicantServDept");
	if(mirror)
		mirror.value=obj.value;
}
</script>
<table width="100%" cellspacing="0" cellpadding="0" border="0" id="applicantDtl">
	<tr>
		<td colspan="4" class="headingwk">
			<div class="arrowiconwk">
				<img src="${pageContext.request.contextPath}/image/arrow.gif" />
			</div>
			<div class="headplacer"><s:text name="depositworks.applicant.details.title" /></div>
		</td>
	</tr>
	
	<s:hidden name="applicationRequest.applicant.userName" id="userName" value="%{applicationRequest.applicant.userName}"/>
	<s:hidden name="applicationRequest.applicant.pwd" id="pwd" value="%{applicationRequest.applicant.pwd}"/>
	<tr>
		<td class="greyboxwk">&nbsp;</td>
		<td class="greybox2wk"><s:radio id="individualOrOrgRadio" name="isIndividual" onchange="showIndvidualOrOrg(this);" list="#{true:'Individual',false:'Organization'}" value="%{isIndividual}" /></td>
		<td class="greyboxwk">&nbsp;</td>
		<td class="greybox2wk">&nbsp;</td>
	</tr>
	<tr>
		<td class="whiteboxwk" id="individualLabel">
			<span class="mandatory">*</span><s:text name="depositworks.applicant.name" />:
		</td>
		<td class="whitebox2wk" id="individualElement">
			<s:textfield name="applicationRequest.applicant.name" id="citizenName"/>
		</td>
		<td class="whiteboxwk" id="organizationLabel" style="display: none" >
			<span class="mandatory">*</span><s:text name="depositworks.applicant.organization" />:
		</td>
		<td class="whitebox2wk" id="organizationElement" style="display: none">
			<s:select id="organizationTypes" name="organizationId" class="input" onchange="setupServicedept(this);setTypeOfCut(this,true);mirrorOrgzn(this);"
				list="dropdownData.organizationList" headerKey="-1" headerValue="--- Select ---"
				listKey="id" listValue="name" value="%{applicationRequest.applicant.organization.id}" />
			<egov:ajaxdropdown id="servicedeptDropdown" fields="['Text','Value']" dropdownId='servicedept' url='depositWorks/ajaxDepositWorks!getServiceDepartments.action' selectedValue="%{applicationRequest.applicant.organization.id}"/>
			&nbsp;&nbsp;&nbsp;<span class="mandatory">*</span><s:text name="depositworks.applicant.service.dept" />:&nbsp;&nbsp;&nbsp;
			<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="servicedept" id="servicedept" onchange="changeServiceDept(this);mirrorServDept(this);" cssClass="selectwk" list="dropdownData.serviceDeptList" listKey="id" listValue="name" value="%{applicationRequest.applicant.serviceDepartment.id}"  />	
		</td>
		<td class="whiteboxwk">
			<span class="mandatory">*</span><s:text name="depositworks.applicant.email">:</s:text>
		</td>
		<td class="whitebox2wk">
			<s:textfield name="applicationRequest.applicant.email" id="citizenEMail" />
		</td>
	</tr>
	<tr>
		<td class="greyboxwk">
			<span class="mandatory">*</span><s:text name="depositworks.applicant.mobile" />:
		</td>
		<td class="greybox2wk">
			<s:textfield name="applicationRequest.applicant.mobileNumber" maxlength="10" id="citizenMobileNo" 
				onblur="validateMobileNumber(this)" />
		</td>
		<td class="greyboxwk">
			<s:text name="depositworks.applicant.alternatenumber" />:
		</td>
		<td class="greybox2wk">
			<s:textfield name="applicationRequest.applicant.alternateNumber" id="citizenAlternateNo"
				onblur="validatePhoneNumber(this,'phone')" />
		</td>
	</tr>
	<tr>
		<td class="whiteboxwk">
			<span class="mandatory">*</span><s:text name="depositworks.applicant.houseno" />:
		</td>
		<td class="whitebox2wk">
			<s:textfield name="applicationRequest.applicant.address.houseNo" id="citizenHouseNo" />
		</td>
		<td class="whiteboxwk"></td>
		<td class="whitebox2wk"></td>
	</tr>
	<tr>
		<td class="greyboxwk">
			<span class="mandatory">*</span><s:text name="depositworks.applicant.street1" />:
		</td>
		<td class="greybox2wk">
			<s:textfield name="applicationRequest.applicant.address.streetAddress1" size="35" 
				id="citizenAdr1" />
		</td>
		<td class="greyboxwk"></td>
		<td class="greybox2wk"></td>
	</tr>
	<tr>
		<td class="whiteboxwk">
			<s:text name="depositworks.applicant.street2" />:
		</td>
		<td class="whitebox2wk">
			<s:textfield name="applicationRequest.applicant.address.streetAddress2" size="35" 
				id="citizenAdr2" />
		</td>
		<td class="whiteboxwk"></td>
		<td class="whitebox2wk"></td>
	</tr>
	<tr>
		<td class="greyboxwk">
			<span class="mandatory">*</span><s:text name="depositworks.locality" />:
		</td>
		<td class="greybox2wk">
			<s:textfield name="applicationRequest.applicant.address.locality" id="citizenLocality" />
		</td>
		<td class="greyboxwk"></td>
		<td class="greybox2wk"></td>
	</tr>
	<tr>
		<td class="whiteboxwk">
			<span class="mandatory">*</span><s:text name="depositworks.applicant.pincode" />:
		</td>
		<td class="whitebox2wk">
			<s:textfield name="applicationRequest.applicant.address.pinCode" maxlength="6" id="citizenPincode" onblur="validatePinCode(this)" />
		</td>
		<td class="whiteboxwk"></td>
		<td class="whitebox2wk"></td>
	</tr>
</table>
<script>
var readOnly = "${applicationRequest.applicant.id != null}";
if (readOnly == "true"){
	var indObj =  document.getElementById('individualOrOrgRadiotrue');
	var orgObj =  document.getElementById('individualOrOrgRadiofalse');
	var orgDD =  document.getElementById('organizationTypes');
	var serviceDeptDD =  document.getElementById('servicedept');
	var citizenObj =  document.getElementById('citizenName');
	if(indObj!=null)
	{
		indObj.disabled = true;
	}
	if(orgObj!=null)
	{
		orgObj.disabled = true;
	}
	if(orgDD!=null)
	{
		orgDD.disabled = true;
	}
	if(serviceDeptDD!=null)
	{
		var modeObj = document.getElementById("mode");
		if(modeObj!=null && modeObj.value=='edit' && serviceDeptDD.value==-1)
			serviceDeptDD.disabled = false;
		else
			serviceDeptDD.disabled = true;
	}
	if(citizenObj!=null)
	{
		citizenObj.readOnly = true;
	}
	
	document.getElementById('citizenEMail').readOnly = true;
	document.getElementById('citizenMobileNo').readOnly = true;
	document.getElementById('citizenAlternateNo').readOnly = true;
	document.getElementById('citizenHouseNo').readOnly = true;
	document.getElementById('citizenAdr1').readOnly = true;
	document.getElementById('citizenAdr2').readOnly = true;
	document.getElementById('citizenLocality').readOnly = true;
	document.getElementById('citizenPincode').readOnly=true;
}

</script>