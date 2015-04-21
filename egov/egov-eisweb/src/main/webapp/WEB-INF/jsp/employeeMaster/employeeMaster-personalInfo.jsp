<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
  <c:catch var ="catchException"> 
<script type="text/javascript">

$(function() {
    $( "#dateOfBirthId" ).datepicker({ dateFormat: "dd/mm/yy" ,changeYear: true,changeMonth: true,yearRange:"1900:c"});
    $( "#deathDateId" ).datepicker({ dateFormat: "dd/mm/yy" ,changeYear: true,changeMonth: true,yearRange:"1900:c"});
    $( "#dateOfFirstAppointmentId" ).datepicker({ dateFormat: "dd/mm/yy" ,changeYear: true,changeMonth: true});
    $( "#dateOfjoinId" ).datepicker({ dateFormat: "dd/mm/yy" ,changeYear: true,changeMonth: true});
    $( "#retirementDateId" ).datepicker({ dateFormat: "dd/mm/yy" ,changeYear: true,changeMonth: true});
  });



var userSelectionHandler = function(sType, arguments) {
	var oData = arguments[2];
	document.getElementById('userMasterId').value = oData[0];
	document.getElementById('userId').value = oData[1];
	
}





</script>
<table width="100%" cellpadding="0" cellspacing="0" border="0">
		<tbody><tr><td>&nbsp;</td></tr>
			<tr>
				<td  class="headingwk">
					<div class="arrowiconwk">
						<img src="${pageContext.request.contextPath}/common/image/arrow.gif" />
					</div>
					<div class="headplacer">
						<s:text name="emp.personal.details" />
					</div>
				</td>
				<td></td>
			</tr>
		</tbody>
</table>
<div id="personalInfo">
<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tbody>	
					<!-- Employee Status , Code and isActive-->
					<tr>
							<td class="whiteboxwk" width="16%"><span class="mandatory">*</span><s:text name="emp.status" /></td>
							<td class="whitebox2wk"><s:select
									headerValue="-------choose-------" headerKey="0"
									list="dropdownData.statusList" listKey="id"
									listValue="code" id="statusId"
									name="StatusMaster"  value="%{StatusMaster.id}" cssClass="selectwk" onchange="mandatoryDeathFiled();"/>
							</td>
							<s:if test="%{mode=='Create'}">
								<s:if test="%{empCodeAutogen!=true}">
									<td class="whiteboxwk" width="16%"><span class="mandatory">*</span><s:text name="emp.code" /></td>
									<td class="whitebox2wk" colspan="3"><s:textfield name="employeeCode" value="%{employeeCode}" id="employeeCodeId" maxlength="9" onblur="checkNumericForCode(this);checkEmpCodeForUniqueness();"/>
									<egovtags:uniquecheck id="empCodeUnique" fieldtoreset="employeeCodeId" fields="['Value']" url='employeeMaster/employeeMaster!checkEmpCodeForUniqueness.action' />
									</td>
								</s:if>
							</s:if>	
							<s:else>
								<td class="whiteboxwk" width="16%"><span class="mandatory">*</span><s:text name="emp.code" /></td>
								<td class="whitebox2wk" colspan="3"><s:textfield name="employeeCode" value="%{employeeCode}" id="employeeCodeId" maxlength="9" onblur="checkNumericForCode(this);checkEmpCodeForUniqueness();"/>
								<egovtags:uniquecheck id="empCodeUnique" fieldtoreset="employeeCodeId" fields="['Value']" url='employeeMaster/employeeMaster!checkEmpCodeForUniqueness.action' />
								 <s:checkbox name="isEmpActive" id="isEmpActive" value="%{isEmpActive}" /><s:text name="emp.active"/> 
								</td>
							</s:else>
							
					</tr>
					<!-- Employee Type and Group -->
					<tr>
							<td class="greyboxwk"><span class="mandatory">*</span><s:text name="emp.type"/></td>
							<td class="greybox2wk" >
										<s:select
									headerValue="-------choose-------" headerKey="0"
									list="dropdownData.empTypeMstrList" listKey="id"
									listValue="name" id="empTypeId"
									name="employeeTypeMaster"  value="%{employeeTypeMaster.id}" cssClass="selectwk" />
							</td>
							<td class="greyboxwk" width="16%"><span class="mandatory">*</span><s:text name="emp.group" /></td>
							<td class="greybox2wk"><s:select
									headerValue="-------choose-------" headerKey="0"
									list="dropdownData.empGroupMstrList" listKey="id"
									listValue="name" id="empGroupId"
									name="groupCatMstr" value="%{groupCatMstr.id}" cssClass="selectwk">
								</s:select>
							</td>
					</tr>
					<!-- Employee and Fathers/Husband's Name -->
					<tr>
							<td class="whiteboxwk"><span class="mandatory">*</span><s:text name="emp.name" /></td>
							<td class="whitebox2wk">
								<s:textfield name="employeeFirstName" id="employeeFirstName" value="%{employeeFirstName!=null?employeeFirstName:'First Name'}"
									onblur="setBlur(this,'First Name');checkAlphaNumeric(this);DataTrimStr(this);checkMaxLengthName(this);"
									cssClass="selectwk grey" onfocus="setFocus(this,'First Name')"/>
								<s:textfield name="employeeMiddleName" id="employeeMiddleName" value="%{employeeMiddleName!=null?employeeMiddleName:'Middle Name'}"
									onblur="setBlur(this,'Middle Name');checkAlphaNumeric(this);DataTrimStr(this);checkMaxLengthName(this);"
									cssClass="selectwk grey" onfocus="setFocus(this,'Middle Name')"/>
								<s:textfield name="employeeLastName" id="employeeLastName" value="%{employeeLastName!=null?employeeLastName:'Last Name'}"
									onblur="setBlur(this,'Last Name');checkAlphaNumeric(this);DataTrimStr(this);checkMaxLengthName(this);"
									cssClass="selectwk grey" onfocus="setFocus(this,'Last Name')"/>	
							</td>
					</tr>	
					<tr>
							<td class="greyboxwk"><s:text name="father.husband.name" /></td>
							<td class="greybox2wk" colspan="3">
								<s:textfield name="fatherHusbandFirstName" id="fatherHusbandFirstName" value="%{fatherHusbandFirstName!=null?fatherHusbandFirstName:'First Name'}"
									onblur="setBlur(this,'First Name');checkAlphaNumeric(this);DataTrimStr(this);checkMaxLengthName(this);"
									cssClass="selectwk grey" onfocus="setFocus(this,'First Name')"/>
								<s:textfield name="fatherHusbandMiddleName" id="fatherHusbandMiddleName" value="%{fatherHusbandMiddleName!=null?fatherHusbandMiddleName:'Middle Name'}"
									onblur="setBlur(this,'Middle Name');checkAlphaNumeric(this);DataTrimStr(this);checkMaxLengthName(this);"
									cssClass="selectwk grey" onfocus="setFocus(this,'Middle Name')"/>
								<s:textfield name="fatherHusbandLastName" id="fatherHusbandLastName" value="%{fatherHusbandLastName!=null?fatherHusbandLastName:'Last Name'}"
									onblur="setBlur(this,'Last Name');checkAlphaNumeric(this);DataTrimStr(this);checkMaxLengthName(this);"
									cssClass="selectwk grey" onfocus="setFocus(this,'Last Name')"/>	
							</td>
					</tr>		
					<!-- Employee Date of Birth and Gender -->
					<tr>
							<td class="whiteboxwk"><span class="mandatory">*</span><s:text name="emp.dob" /></td>
							<td class="whitebox2wk"/>
								<s:date id="dateOfBirth"  name='dateOfBirth' format='dd/MM/yyyy' var="birthDate"/>
								<s:textfield  id="dateOfBirthId"  name="dateOfBirth" value="%{birthDate}" 
									onblur = "validateDateFormat(this);checkDateOfBirth(this);checkfromDateofBirth(this);checkDobWithDeceased();"
								    size="10" onkeyup="DateFormat(this,this.value,event,false,'3') " cssClass="selectwk"/>
							</td>	
							<td class="whiteboxwk"><span class="mandatory">*</span><s:text name="emp.gender" /></td>
							<td class="whitebox2wk" ><s:select
											headerValue="-------choose-------" headerKey="0"
											list="#{'M':'Male','F':'Female'}" 
											id="genderId" name="gender"  value="%{gender}" cssClass="selectwk" />
							</td>
					</tr>
					<!-- Employee Blood Group and Mother Tongue -->
					<tr>
							<td class="greyboxwk"><s:text name="emp.bloodgroup" /></td>
							<td class="greybox2wk" width="20%">
										<s:select
									headerValue="-------choose-------" headerKey="0"
									list="dropdownData.bloodGroupList" listKey="id"
									listValue="name" id="bloodGroupMstrId"
									name="bloodGroupMstr"  value="%{bloodGroupMstr.id}" cssClass="selectwk" />
							</td>
							<td class="greyboxwk"><s:text name="emp.mother.tongue" /></td>
							<td class="greybox2wk" >
										<s:select
									headerValue="-------choose-------" headerKey="0"
									list="dropdownData.langKnownMasterList" listKey="id"
									listValue="name" id="languagesKnownMstrId"
									name="languagesKnownMstr"  value="%{languagesKnownMstr.id}" cssClass="selectwk" />
							</td>
					</tr>		
					<!-- Employee Religion and Community -->
					<tr>
							<td class="whiteboxwk"><s:text name="emp.religion" /></td>
							<td class="whitebox2wk" >
										<s:select
									headerValue="-------choose-------" headerKey="0"
									list="dropdownData.religionMasterList" listKey="id"
									listValue="name" id="religionMstrId"
									name="religionMstr"  value="%{religionMstr.id}" cssClass="selectwk" />
							</td>
							<td class="whiteboxwk"><s:text name="emp.community" /></td>
							<td class="whitebox2wk" >
										<s:select
									headerValue="-------choose-------" headerKey="0"
									list="dropdownData.commMasterList" listKey="id"
									listValue="name" id="communityMstrId"
									name="communityMstr"  value="%{communityMstr.id}" cssClass="selectwk" />
							</td>
					</tr>	
					<!-- Employee Physical and Medical Status -->
					<tr>
							<td class="greyboxwk"><span class="mandatory">*</span><s:text name="is.physically.handicapped"/></td>
							<td class="greybox2wk">
									<s:radio name="isHandicapped" value="%{isHandicapped==null?'0':isHandicapped}"
									    list="#{'1':'Yes','0':'No' }" id="isHandicappedId"/>
							</td>
							<td class="greyboxwk"><s:text name="is.med.report.available"/>
							<td class="greybox2wk">
									<s:radio name="isMedReportAvailable" value="%{isMedReportAvailable==null?'0':isMedReportAvailable}"
									    list="#{'1':'Yes','0':'No' }" id="isMedReportAvailableId"/>
							</td>
					</tr>
					<!-- Employee Identification marks -->
					<tr>
							<td class="whiteboxwk"><s:text name="idmark.1"/></td>
							<td class="whitebox2wk">
									<s:textarea name="identificationMarks1" value="%{identificationMarks1}" id="identificationMarks1Id"
										style="width: 220px; height: 50px;"/>
							</td>
							<td class="whiteboxwk"><s:text name="idmark.2"/></td>
							<td class="whitebox2wk">
									<s:textarea name="identificationMarks2" value="%{identificationMarks2}" id="identificationMarks2Id"
										style="width: 220px; height: 50px;"/>
							</td>
					</tr>	
					<!-- Employee Permanent and Correspondence address -->	
					<tr>
							<td class="greyboxwk"><span class="mandatory">*</span><s:text name="emp.per.address"/>
							<td class="greybox2wk">
									<s:textarea name="permanentAddress" value="%{permanentAddress}" 
										id="permanentAddId" style="width: 220px; height: 50px;"/>
							</td>
							<td class="greyboxwk"><s:text name="emp.pre.address"/></td>
							<td class="greybox2wk">
									<s:textarea name="correspondenceAddress" value="%{correspondenceAddress}" 
										id="presentAddId" style="width: 220px; height: 50px;"/>
							</td>
					</tr>		
					<!-- Employee email and phone number -->
					<tr>
							<td class="whiteboxwk"><s:text name="emp.email"/></td>
							<td class="whitebox2wk">
									<s:textfield name="email" value="%{email}" id="emailId" onblur="checkEmail(this);"/>
							</td>	
							<td class="whiteboxwk"><s:text name="emp.phonenum"/></td>
							<td class="whitebox2wk">
									<s:textfield name="phoneNum" value="%{phoneNum}" id="phoneNumId" onblur="checkPhoneNumber(this);"/>
							</td>
								
					</tr>
					<!-- Employee Languages Known and Local Languages Qualified -->	
					<tr>
							<td class="greyboxwk"><s:text name="emp.lan.known" /></td>
							<td class="greybox2wk" >
								<s:select name="langKnownList" list="dropdownData.langKnownMasterList" listKey="id" 
									listValue="name" multiple="true" size="5" value="%{langKnownList}"/>
							</td>
							<td class="greyboxwk"><s:text name="emp.local.lang"/></td>
							<td class="greybox2wk" >
										<s:select
									headerValue="-------choose-------" headerKey="0"
									list="dropdownData.langQualiMasterList" listKey="id"
									listValue="name" id="langQulMstrId"
									name="langQulMstr"  value="%{langQulMstr.id}" cssClass="selectwk" />
							</td>
					</tr>	
					<!-- Employee PAN number and Date of Death -->
					<tr>
							<td class="whiteboxwk"><s:text name="emp.pan.num"/></td>
							<td class="whitebox2wk">
									<s:textfield name="panNumber" value="%{panNumber}" id="panNumberId" onblur="checkPanNoForUniqueness();"/>
									<egovtags:uniquecheck id="panNumberUnique" fieldtoreset="panNumberId" fields="['Value']" url='employeeMaster/employeeMaster!checkPanNoForUniqueness.action' />
							</td>
							<td class="whiteboxwk" id="deathDateLbl" style="display:none"><span class="mandatory">*</span><s:text name="emp.death.date"/></td>
							<td class="whitebox2wk" id="deathDate" style="display:none">
								<s:date id="deathDate"  name='deathDate' format='dd/MM/yyyy' var="dateOfDeath"/>
								<s:textfield  id="deathDateId"  name="deathDate" value="%{dateOfDeath}" 
									onblur = "validateDateFormat(this);checkDeathDate(this);checkDobWithDeceased();" onfocus="javascript:vDateType='3'"
								    size="10" onkeyup="DateFormat(this,this.value,event,false,'3') " cssClass="selectwk"/>
							</td>	    
					</tr>	
					<!-- Employee Date of Appointment and Date of Joining -->
					<tr>
							<td class="greyboxwk"><span class="mandatory">*</span><s:text name="date.of.appoint"/></td>
							<td class="greybox2wk">
									<s:date id="dateOfFirstAppointment"  name='dateOfFirstAppointment' format='dd/MM/yyyy' var="appointmentDate"/>
									<s:textfield  id="dateOfFirstAppointmentId"  name="dateOfFirstAppointment" value="%{appointmentDate}" 
									onblur = "validateDateFormat(this);checkDOBForDOA(this);CompfaDate(this);populateJoinDate(this);" 
									onfocus="setFocus(this,'dd/mm/yyyy');javascript:vDateType='3'" 
								    size="10" onkeyup="DateFormat(this,this.value,event,false,'3') " cssClass="selectwk"/>
							</td>	
							<td class="greyboxwk"><span class="mandatory">*</span><s:text name="date.of.join"/></td>
							<td class="greybox2wk">
									<s:date id="dateOfjoin"  name='dateOfjoin' format='dd/MM/yyyy' var="joiningDate"/>
									<s:textfield  id="dateOfjoinId"  name="dateOfjoin" value="%{joiningDate}" 
									onblur = "validateDateFormat(this);CompfaDate(this);" 
									onfocus="setFocus(this,'dd/mm/yyyy');javascript:vDateType='3'" 
								    size="10" onkeyup="DateFormat(this,this.value,event,false,'3') " cssClass="selectwk"/>
							</td>
								
					</tr>
					<!-- Employee Retirement Age and Retirement Date -->
					<tr>
							<td class="whiteboxwk"><s:text name="emp.retire.age"/></td>
							<td class="whitebox2wk">
									<s:textfield name="retirementAge" value="%{retirementAge}" id="retirementAgeId"
									 onblur="checkNumericForAge(this);populateRetDate(this);checkRetireAgeDiff(this);"/>
							</td>
							<td class="whiteboxwk"><s:text name="emp.retirement.date"/></td>
							<td class="whitebox2wk">
								<s:date id="retirementDate"  name='retirementDate' format='dd/MM/yyyy' var="dateOfRetirement"/>
								<s:textfield  id="retirementDateId"  name="retirementDate" value="%{dateOfRetirement}" 
									onblur = "validateDateFormat(this); checkRetire(this);" onfocus="javascript:vDateType='3'"
									onchange="populateAge(this)" onkeyup="DateFormat(this,this.value,event,false,'3') " 
									cssClass="selectwk"/>
							</td>	    
					</tr>	
					
										
			</tbody>
</table>		

<table width="100%" cellpadding="0" cellspacing="0" border="0">
		<tbody><tr><td>&nbsp;</td></tr>
			<tr>
				<td  class="headingwk">
					<div class="arrowiconwk">
						<img src="${pageContext.request.contextPath}/common/image/arrow.gif" />
					</div>
					<div class="headplacer">
						<s:text name="emp.user.details" />
					</div>
				</td>
				<td></td>
			</tr>
		</tbody>
</table>
<table width="100%" cellpadding="0" cellspacing="0" border="0" >
			<tbody>
					<!-- Is Employee Username Active and Username -->
					<tr>
							<td class="whiteboxwk"  width="16%"><s:text name="is.emp.user.active"/></td>
							<td class="whitebox2wk"  width="16%">
									<s:radio name="userMaster.isActive" value="%{userMaster==null?0:userMaster.isActive}"
									    list="#{'1':'Yes','0':'No' }" id="userMasterRadioId" />									    
							</td>
							<td class="whiteboxwk" width="25%"><s:text name="emp.user.name"/>
							<td class="whitebox2wk" valign="top" align="left" >
								<s:hidden id="userId" name="userId" value="%{userMaster.id}"/>
									<div class="yui-skin-sam">
										<div class="yui-skin-sam">
											<div id="userSearch_autocomplete" class="yui-ac">
												<s:textfield name="userMaster.userName" value="%{userMaster.userName}" id="userMasterId"
										 		onblur="checkUserLength(this);" size="20"/>
										 		<div id="userSearchResults"></div>
										 	</div>
										 </div>
									</div>
									<egovtags:autocomplete name="userMasterId" field="userMasterId"
													url="${pageContext.request.contextPath}/common/employeeSearch!getAllUnmappedUsers.action" 
													queryQuestionMark="true" results="userSearchResults"
													handler="userSelectionHandler"
													/>
										<span class='warning' id="impropercodeSelectionWarning"></span> 		
							</td>
					</tr>	
			</tbody>
</table>			
</div>
</c:catch>
<c:if test = "${catchException!=null}">
The error  is : ${catchException}<br>
</c:if>						
