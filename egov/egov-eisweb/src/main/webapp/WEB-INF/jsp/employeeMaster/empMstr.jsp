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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<c:catch var ="catchException"> 
<script type="text/javascript">
//js for employee personal details

var errorDiv = document.getElementById("empMstr_errors");

function checkNumericForCode(obj)
{
	if(obj.value!="")
	{
		obj.value = obj.value.toUpperCase();
		var num=obj.value;
		var objRegExp  = /^[a-zA-Z0-9_-]+$/;
		if(!objRegExp.test(num))
		{
			showError('<s:text name="alertProperCode"/>');//alertProperCode = Please fill the proper code number. Only numbers, alphabets, -, _ are allowed 
			obj.value="";
			obj.focus();
			return false;
		}
	}

	 
	return true;
}

function mandatoryDeathFiled()
{
	var selectedStatus = document.getElementById("statusId").options[document.getElementById("statusId").selectedIndex].text;
	if(selectedStatus.toUpperCase()=='DECEASED')
	{
		document.getElementById("deathDateLbl").style.display="";
		document.getElementById("deathDate").style.display="";
		document.getElementById("isEmpActive").checked=false;
	}
	else
	{
		document.getElementById("deathDateLbl").style.display="none";
		document.getElementById("deathDate").style.display="none";
		document.getElementById("deathDateId").value="";
		document.getElementById("isEmpActive").checked=true;
	}
}

function checkEmpCodeForUniqueness()
{
	var empcode = document.getElementById("employeeCodeId").value;
	var empId = document.getElementById("id").value;
	if(empcode != "" && empcode!=null)
	{		
		populateempCodeUnique({empCodeUniqueCheck:empcode,empId:empId});
	}
}

function checkPanNoForUniqueness()
{
	var panno = document.getElementById("panNumberId").value;
	var empId = document.getElementById("id").value;
	if(panno != "" && panno!=null)
	{		
		populatepanNumberUnique({panNoUniqueCheck:panno,empId:empId});
	}
}


function setBlur(obj,defaultval)
{
	if(obj.value == ""){
		document.getElementById(obj.id).value=defaultval;
		document.getElementById(obj.id).style.color='';
	}
}

function setFocus(obj,defaultval)
{
	if(obj.value==defaultval ){
		document.getElementById(obj.id).value="";
		document.getElementById(obj.id).style.color='black';
		}
}

function checkAlphaNumeric(obj)
{
	if(obj.value!="")
	{
		var num=obj.value;
		var objRegExp  = /^[a-zA-Z  .]+$/;
		if(!objRegExp.test(num))
		{
			showError('<s:text name="alertEnterValNme"/>');//alertEnterValNme=please enter a  Valid name 
			obj.value="";
			obj.focus();
			return false;
		}
	}
	 
	return true;
}

function DataTrimStr(obj)
{
	if(obj.value != " "|| obj.value != null)
	{
		var str = obj.value;
		while(str.charAt(0) == (" ") )
		{
			str = str.substring(1);
		}
		while(str.charAt(str.length-1) == " " )
		{
			str = str.substring(0,str.length-1);
		}
	obj.value=str;
	}
}

function checkMaxLengthName(obj)
{
	if(obj.value !="")
	{
		var strNmae = obj.value;
		if(strNmae.length > 256)
		{
			if(obj.name == 'propertyNoper')
			{
				showError( '<s:text name="alertPermanentAddLength"/>' );//alertPermanentAddLength = Permanent Address Exceeded The Permissible Length 
			}
			else if(obj.name =='streetNameper')
			{
				showError( '<s:text name="alertCorresAddLength"/>' );//alertCorresAddLength = Correspondence Address Exceeded The Permissible Length 
			}
			else
			{
				showError( obj.name+' <s:text name="alertExceededLength"/>' );//alertExceededLength = Exceeded The Permissible Length  
			}
			obj.focus();
			obj.value="";
			return false;
		}
	}
	 
	return true;
}

//function check from date of birth
function checkfromDateofBirth(obj)
{
	if (obj.value != null || obj.value != "")
	{
		var date = obj.value;
		var year = date.substr(6,4);
		if(document.empMaster.dateOfFirstAppointment.value!=null && document.empMaster.dateOfFirstAppointment.value!="")
		{
			var appoDate = document.empMaster.dateOfFirstAppointment.value;
			var appoYear= appoDate.substr(6,4);
			var diffYr = eval(appoYear) - (year);
			if(diffYr < 16)
			{
				showError('<s:text name="alertApp15GtBd"/>');//alertApp15GtBd=First Appointment Date Should Be 16 years greater than Birth Date 
				obj.focus();
				obj.value="";
				return false;
			}
		}
	}
	 
	return true;
}

function checkDateOfBirth(obj)
{
	if (obj.value != null || obj.value != "")
	{
		var dte = obj.value;
		var year = dte.substr(6,4);
		var today = new Date();
		var curyear = today.getFullYear();
		var validYear = eval(curyear) - 16;
		if(year>=validYear)
		{
			showError('<s:text name="alertProperDOB"/>');//alertProperDOB=Employee cannot be less than 16 years of age.Please enter the proper date of birth
			obj.focus();
			obj.value="";
			return false;
		}
	}

	 
	return true;
}

function checkDobWithDeceased(){
	var dateofbirth = document.getElementById("dateOfBirthId");
	var deathdate = document.getElementById("deathDateId");
	if ((dateofbirth.value != null && dateofbirth.value != "") && (deathdate.value != null && deathdate.value != "")){
		if (compareDate(deathdate.value,dateofbirth.value) == 1){
			showError('<s:text name="alertDeathDate"/>');//alertDeathDate = Death Date cannot be before date of birth
			dateofbirth.value="";
			deathdate.value="";
			deathdate.focus();
			return false;
		}
	}
	 
	return true;
}

function checkIdentAlphaNumeric(obj)
{
	if(obj.value!="")
	{
		var num=obj.value;
		var objRegExp  = /^[a-zA-Z0-9- () _ @  & "" ]+$/;
		if(!objRegExp.test(num))
		{
			showError('<s:text name="alertValidIdenMark1"/>');//alertValidIdenMark1=please enter Valid IdentificationMarks 
			obj.value="";
			obj.focus();
			return false;
		}
	}

	 
	return true;
}

function checkPanAlphaNumeric(obj)
{
	if(obj.value!="")
	{
		var num=obj.value;
		var objRegExp  = /^[a-zA-Z0-9]+$/;
		if(!objRegExp.test(num))
		{
			showError('<s:text name="alertEnterValPanNum"/>');//alertEnterValPanNum=please enter Valid PanNumber 
			obj.value="";
			obj.focus();
			return false;
		}
	}

	 
	return true;
}

function calLength(obj)
{
	if(obj.value !="")
	{
		var strTemp = obj.value;
		if(strTemp.length !== 10)
		{
			showError( '<s:text name="alertTenpanNum"/>');//alertTenpanNum= PanNumber must be 10 characters 
			obj.focus();
			obj.value="";
			return false;
		}
		obj.value=strTemp.toUpperCase();
	}

	 
	return true;
}

function checkEmail(obj) {

    var email = obj.value;
    var filter = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

    if (!(filter.test(email))) 
    {
	    showError('<s:text name="alert.email"/>');//alert.email = Please provide a valid email address
	    obj.focus;
	    return false;
 	}

     
 	return true;
}

function checkPhoneNumber(obj)  
{  
  var phoneno = /^\d{10}$/;  
  if(!(obj.value.match(phoneno)))  
  {  
  	showError('<s:text name="alert.phno"/>');  //alert.phno = Please provide a valid phone number
  	obj.focus();
    return false;  
  }  

   
  return true;
}

function checkDOBForDOA(obj)//checkDateofAppontment
{
	if(document.empMaster.dateOfFirstAppointment.value !="")
	{
		if(document.empMaster.dateOfBirth.value =="")
		{
			showError('<s:text name="alertfilldob"/>');//alertfilldob=fill the date of birth
			document.empMaster.dateOfBirth.focus();
			document.empMaster.dateOfFirstAppointment.value = "";
			return false;
		}
	}

	if(!checkfromDateofBirth(document.getElementById("dateOfBirthId")))
		return false;

	return true;
}

function compareFromDateAndToDate()
{
	if(document.getElementById("assignmentToDateId").value!="")
	{	
		if(compareDate(document.getElementById("assignmentToDateId").value,document.getElementById("assignmentFromDateId").value) == 1||compareDate(document.getElementById("assignmentToDateId").value,document.getElementById("assignmentFromDateId").value)==0)
		{
			showError('<s:text name="alertToGTFromDt"/>');//alertToGTFromDt=To date should be greater than from Date
			document.getElementById("assignmentToDateId").focus();
			document.getElementById("assignmentToDateId").value="";
			return false;
		}
	}
	 
	return true;
}

//compare first appointment date 
function CompfaDate(obj)
{
	if(document.empMaster.dateOfFirstAppointment.value!=="")
	{
		if(compareDate(document.empMaster.dateOfFirstAppointment.value,document.empMaster.dateOfBirth.value) == 1 ||compareDate(document.empMaster.dateOfFirstAppointment.value,document.empMaster.dateOfBirth.value) == 0)
		{
				showError('<s:text name="alertFstGtBirthDy"/>');//alertFstGtBirthDy=First Appointment Date must be greater than birthdate 
				document.empMaster.dateOfFirstAppointment.focus();
				document.empMaster.dateOfFirstAppointment.value="";
				return false;
		}
	}
	 
	return true;
}

function checkDateofAppontment(obj)
{
	return checkfromDateofBirth(obj);
}

function populateJoinDate(obj)
{
	var DOJ = document.getElementById("dateOfjoinId");
	if(DOJ.value==null || DOJ.value=="")
	{	
		DOJ.value=obj.value;
		DOJ.style.color='black';
	}	
}

function checkNumericForAge(obj)
{
	if(obj.value!="")
	{
		var num=obj.value;
		var objRegExp  = /^[0-9]+$/;
		if(!objRegExp.test(num))
		{
			showError('<s:text name="alertAge"/>');//alertAge = Please fill the proper Age 
			obj.value="";
			obj.focus();
			return false;
		}
	}

	 
	return true;
}

function populateRetDate(obj)
{
	var age = obj.value;
	var dateOfBirth =document.getElementById("dateOfBirthId").value;
	if(age!=null && age!="" )
	{
		if(dateOfBirth!=null && dateOfBirth!="")
		{
			if(document.empMaster.dateOfFirstAppointment.value =="")
			{
					showError('<s:text name="alertDoApp"/>');//alertDoApp=fill date of appointment 
					document.empMaster.dateOfFirstAppointment.focus();
					obj.value="";
					return false;
			}
			else
			{
				if(obj.value!=null && obj.value!="")
				{
				var dobYear =dateOfBirth.substr(6,4);
				var sumOfDate = parseInt(dobYear) + parseInt(age);
				document.getElementById("retirementDateId").value = dateOfBirth.substr(0,5)+'/'+sumOfDate;
				}
			}
		}
		else
		{
			showError('<s:text name="alertEnterDob"/>');//alertEnterDob=Pls Fill in the employee Date Of Birth 
			document.empMaster.dateOfBirth.focus();
			obj.value = "";
			return false;
		}
	}

	 
	return true;

}

function checkRetireAgeDiff(obj)
{
	if (obj.value != null || obj.value != "")
	{
		var date = obj.value;
		var year = date.substr(6,4);
		var appoDate = document.empMaster.dateOfBirth.value;
		var appoYear= appoDate.substr(6,4);
		var diffYr = eval(year) - (appoYear);
		if(diffYr < 50)
		{
			showError('<s:text name="alertRetirementDateLess"/>');//alertRetirementDateLess = Retirement age should not be less than 50 
			obj.focus();
			obj.value="";
			document.getElementById("retirementAgeId").value="";
			return false;
	
		}
	}

	 
	return true;
}

function checkRetire(obj)
{
	if(document.empMaster.retirementDate.value !="")
	{
		if(document.empMaster.dateOfFirstAppointment.value =="")
		{
			showError('<s:text name="alertDoApp"/>');//alertDoApp=Fill date of appointment
			document.empMaster.dateOfFirstAppointment.focus();
			document.empMaster.retirementDate.value="";
			return false;
		}
		else
		{
			if(compareDate(document.empMaster.retirementDate.value,document.empMaster.dateOfFirstAppointment.value) == 1||compareDate(document.empMaster.retirementDate.value,document.empMaster.dateOfFirstAppointment.value)==0)
			{
				showError('<s:text name="alertDoRetrGtAppDate"/>');//alertDoRetrGtAppDate=Date of retirement should be greater than appointment Date
				document.empMaster.retirementDate.focus();
				document.empMaster.retirementDate.value="";
				return false;
			}
		}

	}

	 
	return true;
}

//js for user details tab

function checkUserName(obj)
{
	if(obj.value!="")
	{
		var num=obj.value;
		var objRegExp  = /^[a-zA-Z0-9-._]+$/;
		if(!objRegExp.test(num))
		{
			showError('<s:text name="alertpropUsernme"/>');//alertpropUsernme=Only alphabets numbers - _ and . allowed in username 
			obj.value="";
			obj.focus();
			return false;
		}
	}

	 
	return true;
}

function checkUserLength(obj)
{
	if(obj.value !="")
	{
		var strNmae = obj.value;
		if(strNmae.length > 32)
		{
			showError( obj.name+' <s:text name="alertExceededLength"/>' );//alertExceededLength = Exceeded The Permissible Length
			obj.focus();
			obj.value="";
			return false;
		}
	}

	 
	return true;

}

function validatePersonalInfo()
{
	var empStatus = document.getElementById("statusId");
	var isEmpCodeAutogen=document.getElementById("empCodeAutogen").value;
	var empCode;
	var empType = document.getElementById("empTypeId");
	var empGroup = document.getElementById("empGroupId");
	var empFirstName = document.getElementById("employeeFirstName");
	var empDob = document.getElementById("dateOfBirthId");
	var gender = document.getElementById("genderId");
	var empAdd = document.getElementById("permanentAddId");
	var DOA = document.getElementById("dateOfFirstAppointmentId");
	

	
	if(empStatus.options[empStatus.selectedIndex].value==0 
		|| empStatus.options[empStatus.selectedIndex].value==null)
	{
		showError('<s:text name="alertStatus"/>');//alertStatus = Please Select Status
		empStatus.focus();
		return false;
	}
	if(document.getElementById("mode").value=='Create')
	{
		if(!isEmpCodeAutogen && (document.getElementById("employeeCodeId").value=="" || document.getElementById("employeeCodeId").value==null))
		{
			showError('<s:text name="alert.enter.empcode"/>');//alert.enter.empcode=Please Enter Employee Code
			document.getElementById("employeeCode").focus();	
			return false;
		}	
	}
	else
	{
		empCode = document.getElementById("employeeCodeId");
		if(empCode.value=="" || empCode.value==null)
		{	
			showError('<s:text name="alert.enter.empcode"/>');//alert.enter.empcode=Please Enter Employee Code
			empCode.focus();	
			return false;
		}	
	}		
	if(empType.options[empType.selectedIndex].value==0 || empType.options[empType.selectedIndex].value==null)
	{
		showError('<s:text name="alertEnterEmpType"/>');//alertEnterEmpType=Please choose Employee Type
		empType.focus();
		return false;
	}		
	if(empGroup.options[empGroup.selectedIndex].value==0 || empGroup.options[empGroup.selectedIndex].value==null)
	{
		showError('<s:text name="alertEmpGroup"/>');//alertEmpGroup = Please choose the Employee Group
		empGroup.focus();
		return false;
	}	
	if(empFirstName.value=="First Name" || empFirstName.value==null)
	{
		showError('<s:text name="alertEnterFirstName"/>');//alertEnterFirstName=Please fill the firstname
		empFirstName.focus();
		return false;
	}	
	if(empDob.value=="" || empDob.value==null)
	{
		showError('<s:text name="alertEnterDob"/>');//alertEnterDob=Pls Fill in the employee Date Of Birth
		empDob.focus();
		return false;
	}
	if(gender.options[gender.selectedIndex].value==0 || gender.options[gender.selectedIndex].value==null)
	{
		showError('<s:text name="alert.select.gender"/>');//alert.select.gender = Please select the gender
		gender.focus();
		return false;
	}	
	if(empAdd.value=="" || empAdd.value==null)
	{
		showError('<s:text name="alertPermAddr"/>');//alertPermAddr=Please Fill The Permanent Address
		empAdd.focus();
		return false;
	}	
	if(DOA.value=="" || DOA.value==null)
	{
		showError('<s:text name="alertDoApp"/>');//alertDoApp=Fill date of appointment
		DOA.focus();
		return false;
	}	

	if(empStatus.options[empStatus.selectedIndex].text.toUpperCase()=='DECEASED')
	{
		var deathDate = document.getElementById("deathDateId");
		if(deathDate.value=='' || deathDate.value==null)
		{
			showError('<s:text name="alertEnterDeathDate"/>');//alertEnterDeathDate = Please enter the Death Date
			document.getElementById("isEmpActive").checked=false;
			deathDate.focus();
			return false;
		}	
	}	

	if(empStatus.options[empStatus.selectedIndex].text.toUpperCase()=='RETIRED')
	{
		var retirementDate = document.getElementById("retirementDateId");
		if(retirementDate.value=='' || retirementDate.value==null )
		{
			showError('<s:text name="alertRetiredDate"/>');//alertRetiredDate = Please enter the Retired Date
			retirementDate.focus();
			return false;
		}	
	}

	 
	return true;
}

function checkDeathDate(obj)
{
	
	if (obj.value != "" )
	{
		if (!validateDate(obj.value))
		{

			showError('<s:text name="alertFutureDeathDate"/>');//alertFutureDeathDate = Death Date cannot be in the future
			obj.focus();
			obj.value="";
			return false;
		}
	}

	 
	return true;
}

// js for employee assignment details

function checkDOBforAssignment(obj)
{
	return checkDOBForDOA(obj);
}

function CompeffecDate(obj)
{
	if(compareDate(obj.value,document.empMaster.dateOfFirstAppointment.value) == 1)
	{
		showError('<s:text name="alertFromDtGtAppDt"/>'+' -'+document.empMaster.dateOfFirstAppointment.value);//alertFromDtGtAppDt=From date should be greater than appointment Date
		document.getElementById('assignmentFromDateId').focus();
		document.getElementById('assignmentFromDateId').value="";
		return false;
	}

	 
	return true;
}

function disableFields(obj)
{
	var el=document.getElementById(obj);
	var allIn=el.getElementsByTagName('input');

	var inp, count=0;
	while(inp=allIn[count++]) {
		if(inp!=undefined)
			inp.disabled=true;
	}
	count=0;
	var allsel=el.getElementsByTagName('select');
	while(inp=allsel[count++]) {
		if(inp!=undefined)
			inp.disabled=true
	}
	count=0;
	var allRadio=el.getElementsByTagName('radio');
	while(inp=allRadio[count++]) {
		if(inp!=undefined)
			inp.disabled=true;
	}
	count=0;
	var allTextArea=el.getElementsByTagName('textarea');
	var inp, count=0;
	while(inp=allTextArea[count++]) {
		if(inp!=undefined)
			inp.disabled=true;
	}

	return true;
}

function clearTopFields()
{
	var el=document.getElementById("topTable");
	var allIn=el.getElementsByTagName('input');

	var inp, count=0;
	while(inp=allIn[count++]) {
		inp.value='';
	}
	count=0;
	var allsel=el.getElementsByTagName('select');
	while(inp=allsel[count++]) {
		if(inp!=undefined)
			inp.value=0;
	}
	count=0;
	document.getElementById("isPrimaryIdY").checked=true;
	document.getElementById("hodRadioId0").checked=true;
	document.getElementById("isHodlbl").style.display="none";
	document.getElementById("hodDept").style.display="none";
	document.getElementById("hodId").colSpan="3";
	recordToModifyAssignment=null;
}

function vaidateTopFieldAssignment()
{
	var isPrimary=false;
	var assignmentId=null;
	assignmentId = document.getElementById("assignmentId").value;
	var radio=document.getElementsByName("isPrimary");
	if(radio[0].checked)
		isPrimary=true;
	var assignmentFromDate = document.getElementById("assignmentFromDateId");
	var assignmentToDate = document.getElementById("assignmentToDateId");
	var department = document.getElementById("deptId");
	var designation = document.getElementById("empDesig");
	var position = document.getElementById("position");
	var fund = document.getElementById("fundId");
	var empfunction = document.getElementById("functionId");
	var functionary = document.getElementById("functionaryId");
	var grade = document.getElementById("gradeId");

	if(assignmentFromDate.value=="" || assignmentFromDate.value==null)
	{
		showError('<s:text name="alertEnterFromDate"/>');//Please fill from Date
		assignmentFromDate.focus();
		return false;
	}
	if(assignmentToDate.value=="" || assignmentToDate.value==null)
	{
		showError('<s:text name="alertFillToDate"/>');//alertFillToDate=Please fill to Date 
		assignmentToDate.focus();
		return false;
	}
	if(compareDate(assignmentToDate.value,assignmentFromDate.value) == 1||compareDate(assignmentToDate.value,assignmentFromDate.value)==0)
	{
		showError('<s:text name="alertToGTFromDt"/>');//alertToGTFromDt=To date should be greater than from Date
		assignmentToDate.focus();
		assignmentToDate.value="";
		return false;
	}
	
	if(isPrimary && !validateAssigmentForOverlapping(assignmentFromDate,assignmentToDate,assignmentId))
		return false;
	if(department.options[department.selectedIndex].value==0 || department.options[department.selectedIndex].value==null)
	{
		showError('<s:text name="alertchooseMnDept"/>');//alertchooseMnDept=Please choose the main department
		department.focus();
		return false;
	}	
	if(designation.value=="" || designation.value==null)
	{
		showError('<s:text name="alertChooseDesg"/>');//alertChooseDesg=Please choose the designation
		designation.focus();
		return false;
	}
	if(position.value=="" || position.value==null)
	{
		showError('<s:text name="alertChooseposition"/>');//alertChooseposition=Please choose the position
		position.focus();
		return false;
	}
	if(fund.options[fund.selectedIndex].value==0 || fund.options[fund.selectedIndex].value==null)
	{
		showError('<s:text name="alertChooseFund"/>');//alertChooseFund=Please choose the fund
		fund.focus();
		return false;
	}
	if(empfunction.options[empfunction.selectedIndex].value==0 || empfunction.options[empfunction.selectedIndex].value==null)
	{
		showError('<s:text name="alertChooseFunction"/>');//alertChooseFunction=Please choose the function
		empfunction.focus();
		return false;
	}
	if(functionary.options[functionary.selectedIndex].value==0 || functionary.options[functionary.selectedIndex].value==null)
	{
		showError('<s:text name="alertChoosefuncary"/>');//alertChoosefuncary=Please choose the functionary
		functionary.focus();
		return false;
	}
	if(grade.options[grade.selectedIndex].value==0 || grade.options[grade.selectedIndex].value==null)
	{
		showError('<s:text name="alertChooseGradeName"/>');//alertChooseGradeName =Please choose the grade for the employee
		grade.focus();
		return false;
	}

	 
	return true;
}

function validateAssigmentForOverlapping(fromDate,toDate,assignmentId)
{
	var records= assignmentDataTable.getRecordSet();
	var enteredFromDate = fromDate.value;
	var enteredToDate = toDate.value;
	var i,indexRow;
	var existingAssignId;
	var isExistingAssignmentPrimary,existingAssignmentFromDate,existingAssignmentToDate;
	for(i=0;i<records.getLength();i++)
	{
    	indexRow = records.getRecord(i).getId();
    	isExistingAssignmentPrimary = dom.get("isPrimary"+indexRow).value;
    	existingAssignId = dom.get("id"+indexRow).value;
    	if(isExistingAssignmentPrimary=='Y')
        {
            if(existingAssignId!=assignmentId)
            { 
	    		existingAssignmentFromDate = dom.get("fromDate"+indexRow).value;
	    		existingAssignmentToDate = dom.get("toDate"+indexRow).value;
	    		if(compareDate(existingAssignmentToDate,enteredFromDate)==-1 && (compareDate(existingAssignmentFromDate,enteredFromDate)==0 || compareDate(existingAssignmentFromDate,enteredFromDate)==1))
	        	{
	            	showError('<s:text name="alert.fromdate.overlap"/> : '+(i+1));
	            	fromDate.focus();
	            	return false;
	            }
	    		if(compareDate(existingAssignmentToDate,enteredToDate)==-1 && (compareDate(existingAssignmentFromDate,enteredToDate)==0 || compareDate(existingAssignmentFromDate,enteredToDate)==1))
	        	{
	            	showError('<s:text name="alert.todate.overlap"/> : '+(i+1));
	            	toDate.focus();
	            	return false;
	            }
            }   
            
        }	
	}	

	 
	return true;
}

function validateAssignmentForPrimary()
{

	var records= assignmentDataTable.getRecordSet();
	var recordId;
	var primaryAssignExists=false;

	for(var i=0;i<records.getLength();i++)
    {
    	recordId=records.getRecord(i).getId();
    	if(dom.get("isPrimary"+recordId).value=='Y')
    	{
    		primaryAssignExists = true;
    		break;
        }
    	showError('<s:text name="alertEmpPrimaryAssign"/>');//alertEmpPrimaryAssign = Employee should have a primary assignment'
    	return primaryAssignExists;
    }

	return primaryAssignExists;
}

function checkDeptDesig(obj)
{
	var isPrimary=false;
	var dept = document.getElementById("deptId");
	var desig = document.getElementById("empDesig");
	var radio=document.getElementsByName("isPrimary");
	var fromDate = document.getElementById("assignmentFromDateId");
	var toDate = document.getElementById("assignmentFromDateId");
	var posHiddenId = document.getElementById("positionId");
	var position = document.getElementById("position");
	var desigHiddenId = document.getElementById("empDesigId");
	if(radio[0].checked)
		isPrimary=true;
	if(isPrimary)
	{
		if(fromDate.value=="" || fromDate.value==null)
		{
			showError('<s:text name="alertFromDate"/>');//alertFromDate=Fill the from date
			fromDate.focus();
			obj.value="";
			posHiddenId.value="";
			position.value="";
			return false;
		}
		if(toDate.value=="" || toDate.value==null)
		{
			showError('<s:text name="alertFromDate"/>');//alertFromDate=Fill the from date
			toDate.focus();
			obj.value="";
			posHiddenId.value="";
			position.value="";
			return false;
		}
		
		if(dept.options[dept.selectedIndex].value==0)
		{
			showError('<s:text name="alertchooseMnDept"/>');//alertchooseMnDept=Please choose the main department
			dept.focus();
			obj.value="";
			posHiddenId.value="";
			position.value="";
			desig.value="";
			desigHiddenId.value="";
			return false;
		}	
		if(desig.value=="" || desig.value==null)
		{
			showError('<s:text name="alertChooseDesg"/>');//alertChooseDesg=Please choose the designation
			desig.focus();
			desigHiddenId.value="";
			obj.value="";
			posHiddenId.value="";
			position.value="";
			return false;
		}	
	}

	 
	return true;
}

function clearFields(name)
{

	var radio=document.getElementsByName("isPrimary");
	if(radio[0].checked)
	{	
		switch(name)
		{
			case "Department":
				document.getElementById("empDesigId").value="";
				document.getElementById("empDesig").value="";
				document.getElementById("positionId").value="";
				document.getElementById("position").value="";
	
			case "Designation":
				if(document.getElementById("empDesig").value=='')
				{	
					document.getElementById("positionId").value="";
					document.getElementById("position").value="";
					document.getElementById("empDesigId").value="";
				}		
		}
	}	

	return true;
	
}

</script>
</c:catch>
<c:if test = "${catchException!=null}">
The error  is : ${catchException}<br>
</c:if>	
