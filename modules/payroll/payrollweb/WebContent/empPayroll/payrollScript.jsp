<%@ include file="/includes/taglibs.jsp" %>

<%@ page import="java.util.*,
org.egov.infstr.utils.*,
org.apache.log4j.Logger,
org.egov.pims.*,
org.egov.pims.dao.*,
org.egov.pims.service.*,
org.egov.pims.utils.*,
org.egov.pims.model.*,
org.egov.infstr.commons.*,
org.egov.payroll.utils.*,
org.egov.pims.commons.client.*,
org.egov.infstr.commons.dao.*,
org.egov.infstr.utils.*,
org.egov.payroll.services.payslip.*,
org.egov.lib.rjbac.dept.*,
org.egov.lib.address.model.*,
java.math.BigDecimal,
org.egov.payroll.model.*,
org.egov.lib.address.dao.*,
java.text.SimpleDateFormat,
java.util.StringTokenizer,
org.egov.pims.client.*"
%>

<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">


<%!
	public Map getGradeMapForSkill(Integer skillId)
	{
	
	Map gradeMap = new HashMap();
	if(skillId!=null&&!skillId.equals(new Integer(0)))
	{
	//try
	//{
	SkillMasterDAO skillMasterDAO = new SkillMasterDAO();
	SkillMaster skillMaster = (SkillMaster) skillMasterDAO.getSkillMaster(skillId.intValue());
	Set gradeSet = skillMaster.getSettechnicalGradesMaster();
	if(gradeSet!=null&&!gradeSet.isEmpty())
	{
	Iterator gradeitr = gradeSet.iterator();
	while(gradeitr.hasNext())
	{
	TechnicalGradesMaster 	technicalGradesMaster = (TechnicalGradesMaster)gradeitr.next();
	gradeMap.put(technicalGradesMaster.getId(),technicalGradesMaster.getGradeName());
	}
	}
	
	}
	return gradeMap;
	
	}
%>

<%
		Map deptMap = (Map)session.getAttribute("deptmap");
		session.setAttribute("deptMap",deptMap);
		

		Set deptSet = null;
		Set eduDetailsSet = null;

		Set teckQualSet = null;


		PersonalInformation egpimsPersonalInformation =null;
		String id ="";
		List paySet = null;
		if(request.getAttribute("employeeOb")!=null)
		{

		//id = request.getParameter("Id").trim();

		egpimsPersonalInformation = (PersonalInformation)request.getAttribute("employeeOb");

		}
		else
		{
		egpimsPersonalInformation = new PersonalInformation();

		}
		if(request.getParameter("Id")!=null)
		{

		id = request.getParameter("Id").trim();
		System.out.println("the id in the jsp ::::: " + id);
		paySet = PayrollManagersUtill.getPayRollService().getPayStructureByEmp(new Integer(id));
		//egpimsPersonalInformation = eisManager.getEmloyeeById(new Integer(id));

		}
		else
		{
		//egpimsPersonalInformation = new PersonalInformation();
		paySet =new ArrayList();
		}




		deptSet = egpimsPersonalInformation.getEgpimsDeptTests();


		eduDetailsSet = egpimsPersonalInformation.getEgpimsEduDetails();

		teckQualSet = egpimsPersonalInformation.getEgpimsTecnicalQualification();


		if(deptSet.isEmpty())
		deptSet.add(new DeptTests());
		if(eduDetailsSet.isEmpty())
		eduDetailsSet.add(new EduDetails());

		if(teckQualSet.isEmpty())
		teckQualSet.add(new TecnicalQualification());


		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");


%>


<script>

function setReadOnlyPayrollRelated() {

	document.payrollForm.statusMaster.disabled= true;
	<%
	String modeonloadView=((String)(session.getAttribute("viewMode"))).trim();
	if(modeonloadView.equalsIgnoreCase("view"))
	{
	%>
	
		document.payrollForm.mOrId.disabled= true;
		document.payrollForm.recruitmentTypeId.disabled= true;
		document.payrollForm.postingTypeId.disabled= true;
		document.payrollForm.category.disabled= true;
		document.payrollForm.gpf.disabled= true;
		document.payrollForm.payFixIn.disabled= true;
		document.payrollForm.paymentMethod.disabled= true;
		document.payrollForm.effDate.disabled=true;
		document.payrollForm.annualIncrDate.disabled=true;
		document.payrollForm.payScaleHeader.disabled= true;
		document.payrollForm.govtOrderNo.disabled= true;
		
		
		var pHeaderTable= document.getElementById("PayHeaderTable");
		var rows= pHeaderTable.rows.length;
		
		
		if(rows==3)
		{
			document.payrollForm.payScaleHeader.disabled= true;
			document.payrollForm.effDate.disabled= true;
			document.payrollForm.annualIncrDateShow.disabled= true;
			document.payrollForm.annualIncrDate.disabled= true;
			document.payrollForm.basicFrom.readOnly= true;
			document.payrollForm.basicto.readOnly= true;
			document.payrollForm.currMnthPay.disabled= true;
			document.payrollForm.currDailyPay.disabled= true;
			document.payrollForm.stagnantPay.disabled= true;
			
		}
		
		if(rows>3)
		{
			for(var i=0 ;i<rows-2;i++)
			{
				document.payrollForm.payScaleHeader[i].disabled= true;
				document.payrollForm.effDate[i].disabled= true;
				document.payrollForm.annualIncrDate[i].disabled= true;
				document.payrollForm.annualIncrDateShow[i].disabled= true;
				document.payrollForm.basicFrom.readOnly= true;
				document.payrollForm.basicto.readOnly= true;
	
				document.payrollForm.currMnthPay[i].disabled= true;
	            document.payrollForm.currDailyPay[i].disabled= true;
				document.payrollForm.stagnantPay[i].disabled= true;
			}
		}
		document.payrollForm.bank.disabled= true;
		document.payrollForm.branch.disabled= true;
		document.payrollForm.accountNumber.readOnly= true;
		
	<%
	}
	%>
}

function checkIncr2Date()
{
		
	var ptablermr =document.getElementById('PayHeaderTable');
	var len = ptablermr.rows.length;

	if(len==2)
	{
		var tempdt =document.payrollForm.annualIncrDateShow.value;
	
		document.payrollForm.annualIncrDate.value =tempdt;
	
		if(tempdt.length>5)
		{
			tempdt= tempdt.substr(0,5);
			document.payrollForm.annualIncrDateShow.value=tempdt;
		}
	}
	if(len>2)
	{

		var annual=document.getElementsByName("annualIncrDateShow");
	
		for(var j=0;j<len-1;j++)
		{
			var tempdt =document.payrollForm.annualIncrDateShow[j].value;
			document.payrollForm.annualIncrDateShow[j].value =tempdt;
		
			if(tempdt.length>5)
			{
				tempdt= tempdt.substr(0,5);
				document.payrollForm.annualIncrDateShow[j].value=tempdt;
			}
		}
	}
		

}

function validateTabChange(arg) {
	var check=true;
	if(document.getElementById("employeeTable").style.display!="none") {
	check=validatePayrollDetails(arg);
	if(check==true)
	check=validateRow();
	
	}
	return check;
}


function toRetire()
{
	if(document.payrollForm.dateOfRetirement!=null && document.payrollForm.dateOfRetirement.value!="" )
	{
		var tbl= document.getElementById("EOTable");
		var rows= tbl.rows.length;
		if(rows==2)
		{
			if(compareDate(document.payrollForm.dateOfRetirement.value,document.payrollForm.toDate.value) == 1)
			{
			  	alert("Todate  must be less than Retirement Date");
			  	document.payrollForm.toDate.focus();
			  	document.payrollForm.toDate.value="";
			 	return false;
			}
		}
	}
}


function validateRow()
{
}

function validatePayrollDetails(arg)
{
	if(document.payrollForm.mOrId.value == "0")
	{
		alert('<bean:message key="alertModeRecr"/>');
		document.payrollForm.mOrId.focus();
		return false;
	}

	if(document.payrollForm.recruitmentTypeId.value == "0")
	{
		alert('<bean:message key="alertRecruitmentType"/>');
		document.payrollForm.recruitmentTypeId.focus();
		return false;
	}

    if(document.getElementById("paymentMethod").value =="dbt")
    {
		if(document.payrollForm.bank.options.selectedIndex =='0')
		{
			alert('<bean:message key="alertChooseBnkNme"/>');
			document.payrollForm.bank.focus();
			return false;
	    }

		if(document.payrollForm.branch.options.selectedIndex=='0' )
		{
			alert('<bean:message key="alertChooseBrnchNme"/>');
			document.payrollForm.branch.focus();
			return false;
		}
		if(document.payrollForm.accountNumber.value=="")
		{
			alert('<bean:message key="alertChooseAccnum"/>');
			document.payrollForm.accountNumber.focus();
			return false;
		}
	}
	
	return true;

}

function populatebasic(obj)
{

	var val = obj.value;
	var row=getRow(obj);
	var basicto = getControlInBranch(row,"basicto");
	var baFrom =  getControlInBranch(row,"basicFrom");
	var effDate =  getControlInBranch(row,"effDate");
	
	<%
	
	PayRollService payRollManager =  PayrollManagersUtill.getPayRollService();
	List pList = payRollManager.getAllPayScaleHeaders();
	for(Iterator itr=pList.iterator(); itr.hasNext();)
	{
	PayScaleHeader payScaleHeader =(PayScaleHeader)itr.next();
	
	%>
	var PayHeaderTable =document.getElementById('PayHeaderTable');
	var len = PayHeaderTable.rows.length;
	
	
	if(obj.options[obj.selectedIndex].value == '<%=payScaleHeader.getId()%>')
	{
	
	
	baFrom.value = '<%=payScaleHeader.getAmountFrom()%>'   ;
	
	basicto.value = '<%=payScaleHeader.getAmountTo()%>'       ;
	
	}
	<%
	}
	%>

}

function CompeffDate(obj)
{
	var row = getRow(obj);
	var tbl= document.getElementById("PayHeaderTable");
	var len = tbl.rows.length;
	var i=0;
	var rows=row.rowIndex;
	var dateOfFA="<%=sdf.format(egpimsPersonalInformation.getDateOfFirstAppointment())%>";
	if(len >3)
	{
		for(var i=1;i<len-2;i++)
		{
			if(compareDate(document.payrollForm.effDate[i].value,dateOfFA) == 1)
			{
				alert('<bean:message key="alertEffGtFAppDate"/>');
				document.payrollForm.effDate[i].focus();
				document.payrollForm.effDate[i].value="";
				return false;
			}
		}
	}
	else
	{
		if(compareDate(document.payrollForm.effDate.value,dateOfFA) == 1)
		{
			alert('<bean:message key="alertEffGtFAppDate"/>');
			document.payrollForm.effDate.focus();
			document.payrollForm.effDate.value="";
			return false;
		}
	}
}

function setCheckBoxValue(obj){	
	var rowObj = getRow(obj);
	var table = document.getElementById("PayHeaderTable");
	if(obj.checked == true)
		getControlInBranch(table.rows[rowObj.rowIndex],'stagnantPayId').value = "Y";
	else
		getControlInBranch(table.rows[rowObj.rowIndex],'stagnantPayId').value = "N";
	
}

function checkBasic(obj)
{
	var val = obj.value;
	var row=getRow(obj);
	var payHead = getControlInBranch(row,"payScaleHeader");
	
	var http = initiateRequest();
	var url = "<%=request.getContextPath()%>/empPayroll/checkBasic.jsp?basic="+val+"&payHead="+payHead.value;
	http.open("GET", url, true);
	http.onreadystatechange = function()
	{
		if (http.readyState == 4)
		{
			if (http.status == 200)
			{
				var statusString =http.responseText.split("^");
				
				if(statusString[0]=="false")
				{
					var popup = statusString[1];
					if(popup!="")
					{
						alert(popup);
						if(row.rowIndex<=1)
						{
							document.forms[0].currBasicPay.value = "";
							document.forms[0].currBasicPay.focus();
						}
						else
						{
							obj.value = "";
							obj.focus();
						}
					
					}
				}
			}
		}
	};
	http.send(null);

}

function checkBasicfromPayscale(obj)
{
	var val = obj.value;
	var row=getRow(obj);
	var payHead = getControlInBranch(row,"payScaleHeader");
	
	if(row.rowIndex <=2)
	{
		var currentPay=document.forms[0].currBasicPay.value;
		var http = initiateRequest();
		var url = "<%=request.getContextPath()%>/empPayroll/checkBasic.jsp?basic="+currentPay+"&payHead="+val;
		
		http.open("GET", url, true);
		http.onreadystatechange = function()
		{
			if (http.readyState == 4)
			{
				if (http.status == 200)
				{
					var statusString =http.responseText.split("^");
	
					if(statusString[0]=="false")
					{
						var popup = statusString[1];
						if(popup!="")
						{
							alert(popup);
							document.forms[0].currBasicPay.value = "";
							document.forms[0].currBasicPay.focus();
						}
					}
				}
			}
		};
		http.send(null);
	}
	else
	{

   		var currentPay=document.payrollForm.currBasicPay[row.rowIndex-1].value;
   		var http = initiateRequest();
		var url = "<%=request.getContextPath()%>/empPayroll/checkBasic.jsp?basic="+currentPay+"&payHead="+val;
		http.open("GET", url, true);
		http.onreadystatechange = function()
			{
			if (http.readyState == 4)
			{
				if (http.status == 200)
				{
					var statusString =http.responseText.split("^");

					if(statusString[0]=="false")
					{
						var popup = statusString[1];
						if(popup!="")
						{
							alert(popup);
							document.forms[0].currBasicPay[row.rowIndex-1].value = "";
							document.forms[0].currBasicPay[row.rowIndex-1].focus();
						}
					}
				}
			}
		};
		http.send(null);
	}
}

function checkIncrDate(obj)
{
	var val = obj.value;
	var row=getRow(obj);
	var annuldte = getControlInBranch(row,"annualIncrDate");
	
	annuldte.value=obj.value;
	if(obj.value != "")
	{
		if(obj.value.length == 2)
		{
			obj.value = obj.value+"/";
		}
		if(obj.value.length>5)
		{
			obj.value = obj.value.substr(0,5);
		}
	}
}

function checkIncrLength(obj)
{
	var len =obj.value.length;
	
	var dtStr = obj.value;
	
	month=dtStr.substr(3,2);
	day=dtStr.substr(0,2);
	if(obj.value != "")
	{
		if (len !=5 || dtStr.indexOf("/")!= 2  || day>31 || month>12 || month == 0)
		{
			alert('<bean:message key="alertDdMmFormat"/>');
			obj.value="";
			obj.focus;
			return false;
		}
	}
}

function populatebasicOnload()
{

	<%
	String modeonload=((String)(session.getAttribute("viewMode"))).trim();
	if(modeonload.equalsIgnoreCase("Modify")||modeonload.equalsIgnoreCase("view"))
	{
	%>
	var PayHeaderTable =document.getElementById('PayHeaderTable');
	var len = PayHeaderTable.rows.length;
	
	if(len>3)
	{
		for(var i=0 ;i<len-2;i++)
		{
			if(document.payrollForm.payScaleHeader[i].value!="" )
			{

			populatebasic(document.payrollForm.payScaleHeader[i]);


			}
		}
	}
	else
	{
	    
		populatebasic(document.getElementById('payScaleHeader'));
	}
	
	<%
	}
	%>
}

function populateEff(obj,selectedPayscaleId)
{
	var tbl= document.getElementById("PayHeaderTable");
	var len = tbl.rows.length;
	var effDate= obj.value;
	if(len==3)
	{
		loadPayscaleData(effDate,'payScaleHeader',selectedPayscaleId);
	}
	else if(len > 3)
	{
		loadPayscaleDataForCurrentRow(effDate,obj,'payScaleHeader','PayHeaderTable',selectedPayscaleId)
	}
}

function calbLength(obj)
{
	if(obj.value !=="")
	{
		var strTemp = obj.value;
		
		if(strTemp.length > 20)
		{
			alert( '<bean:message key="alertBnkAccNum"/>');
			obj.focus();
			return false;
		}
		return true;
	}
}


function checkBranch(obj)
{
	var tbl= document.getElementById("BDnameTable");
	var len = tbl.rows.length;
	loadSelectData('../commonyui/egov/loadComboAjax.jsp', 'BANKBRANCH', 'ID', 'BRANCHNAME', 'BANKID=#1 order by ID', 'bank' , 'branch');
}

function checkBank(obj)
{
	if(obj.options.selectedIndex != 0)
	{
		 if(document.payrollForm.bank.options.selectedIndex=='0')
		 {
			 alert("please chose the bank");
			 obj.options.selectedIndex.value ==0;
			 document.payrollForm.bank.focus();
			 return false;
		
		 }
	}
}

function checkBranchPrsnt(obj)
{
	if(obj.value != "" )
	{
		 if(document.payrollForm.branch.options.selectedIndex=='0')
		 {
			 alert("please choose the branch");
			 obj.value = "";
			 document.payrollForm.branch.focus();
			 return false;
		 }
	}
}

function loadPayscaleData(effObj,destobj,selectedPayscaleId)
{
	var effDate = effObj;
	var selectedIdx = 0;
	var type='loadPayscaleData';
	var url = "${pageContext.request.contextPath}/empPayroll/populatePayScaleAjax.jsp?effDate="+effDate+"&type="+type;
	
	if(trimAll(effDate) != "" )
	{
		var request = initiateRequest();
		request.open("GET", url, false);
		request.send(null);		
		if (request.status == 200) 
		{
			var respTxt = trimAll(request.responseText);
			if(respTxt!="")
			{
				var response=respTxt.split("^");
				var id = response[0].split("+");
				var name = response[1].split("+");
		
				var comboObj = document.getElementById(destobj);
				comboObj.options.length = 0;
				comboObj.options[0] = new Option("--Choose--","");
				for(var i = 1 ; i <= id.length  ; i++)
				{
					comboObj.options[i] = new Option(name[i-1],id[i-1]);		
					if (selectedPayscaleId!=null && selectedPayscaleId == id[i-1])	
					{
						selectedIdx = i;
					}
				}
				comboObj.selectedIndex = selectedIdx;
			}
		}		
		else{
			alert("Error occurred while populating payscale name ");
		}
	}
	else
	{
		var comboObj = document.getElementById(destobj);
		comboObj.options.length = 0;	
	}
}

function loadPayscaleDataForCurrentRow(effobj,eff,destobj,destTableName,selectedPayscaleId)
{
	var payRowobj=getRow(eff);
	var selectedIdx = 0;
	var payscaleTable= document.getElementById(destTableName);
	var destCol=getControlInBranch(payscaleTable.rows[payRowobj.rowIndex],destobj);
	var effDate=effobj;
	var type='loadPayscaleDataForCurrentRow';
	var url = "${pageContext.request.contextPath}/empPayroll/populatePayScaleAjax.jsp?effDate="+effDate+"&type="+type;
	
	if(trimAll(effDate) != "")
	{
		var request = initiateRequest();
		request.open("GET", url, false);
		request.send(null);		
		if (request.status == 200) 
		{
			var respTxt1 = trimAll(request.responseText);
			if(respTxt1!="")
			{
				var response=respTxt1.split("^");
				var id = response[0].split("+");
				var name = response[1].split("+");
				var comboObj = destCol;
				comboObj.options.length = 0;
				comboObj.options[0] = new Option("--Choose---","");
				for(var i = 1 ; i <= id.length  ; i++)
				{
					comboObj.options[i] = new Option(name[i-1],id[i-1]);		
					if (selectedPayscaleId!=null && selectedPayscaleId == id[i-1])	
					{
						selectedIdx = i;
					}	
				}		
				comboObj.selectedIndex = selectedIdx;
			}		
		}
		else
		{
			alert("Error occurred while populating payscale name ");
		}
	}
	else
	{
		var comboObj = destCol;
		comboObj.options.length = 0;	
		comboObj.options[0] = new Option("--Choose--","");
	}
}


function populatePayscaleName()
{

	<%
	PayStructure payStructure=null;
	String mode=((String)(session.getAttribute("viewMode"))).trim();
	
	if(mode.equalsIgnoreCase("Modify")||mode.equalsIgnoreCase("view"))
	{ 
	          System.out.println("dagjbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
		     Integer empId =egpimsPersonalInformation.getIdPersonalInformation();
			 System.out.println("empId>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+empId);
			  paySet = PayrollManagersUtill.getPayRollService().getPayStructureByEmp(empId);
			  Iterator paySetitr= paySet.iterator();
			 if (paySet.size() >1)
			 {
	
			for(int i=0;paySetitr.hasNext();i++)
			{
				payStructure = (PayStructure)paySetitr.next();	
				%>
				 populateEff(document.payrollForm.effDate[<%= i %>],<%= payStructure.getPayHeader().getId()%>);
				 
				 document.payrollForm.payScaleHeader[<%= i %>].value = "<%= payStructure.getPayHeader().getId()%>";
				
				<%
	
			}
			 }
			 else {
			      
				if (paySetitr.hasNext()) {
					payStructure = (PayStructure)paySetitr.next();
				 %>
				populateEff(document.payrollForm.effDate,<%=payStructure.getPayHeader().getId()%>);
				
				document.payrollForm.payScaleHeader.options[document.payrollForm.payScaleHeader.selectedIndex].value= "<%=payStructure.getPayHeader().getId()%>";
				document.payrollForm.payScaleHeader.options[document.payrollForm.payScaleHeader.selectedIndex].text="<%=payStructure.getPayHeader().getName()%>";
				<%
				}
			 }
	}
	%>

}

function ButtonPressNew(arg)
{
	if(arg == "savenew")
	{
		if(document.payrollForm.statusMaster.options.selectedIndex=='0')
		{
			alert('Please choose the status');
			document.payrollForm.statusMaster.focus();
			return false;
		}
		if(document.payrollForm.mOrId.value == "0")
		{
			alert('<bean:message key="alertModeRecr"/>');
			document.payrollForm.mOrId.focus();
			return false;
		}
		/*if(document.payrollForm.recruitmentTypeId.value == "0")
		{
			alert('<bean:message key="alertRecruitmentType"/>');
			document.payrollForm.recruitmentTypeId.focus();
			return false;
		}*/
		if(document.getElementById("paymentMethod").value =="0")
		{
			alert("please choose the payment method");
			document.payrollForm.paymentMethod.focus();
			return false;
		}
		
		if(document.getElementById("paymentMethod").value =="dbt")
		{
			if(document.payrollForm.bank.options.selectedIndex =='0')
			{
				alert('<bean:message key="alertChooseBnkNme"/>');
				document.payrollForm.bank.focus();
				return false;
			}
			if(document.payrollForm.branch.options.selectedIndex=='0')
			{
				alert('<bean:message key="alertChooseBrnchNme"/>');
				document.payrollForm.branch.focus();
				return false;
			}
			if(document.payrollForm.accountNumber.value=="")
			{
				alert('<bean:message key="alertChooseAccnum"/>');
				document.payrollForm.accountNumber.focus();
				return false;
			}
		}
		
		var pHeaderTable= document.getElementById("PayHeaderTable");
		var rows= pHeaderTable.rows.length;
		if(rows>3)
		{
			for(var i=0 ;i<rows-2;i++)
			{
				if(document.payrollForm.effDate[i].value=="" )
				{
					alert('<bean:message key="alertEnterEffDate"/>');
					document.payrollForm.effDate[i].focus();
					return false;
				}
				if(document.payrollForm.payScaleHeader[i].value=="" || document.payrollForm.payScaleHeader[i].value=="0")
				{
					alert('<bean:message key="alertPayscaleNme"/>');
					document.payrollForm.payScaleHeader[i].focus();
					return false;
				}
		        if(document.payrollForm.annualIncrDateShow[i].value =="" )
				{
					alert('<bean:message key="alertEnterAnnIncDate"/>');
					document.payrollForm.annualIncrDateShow[i].focus();
					return false;
				}
				if(document.payrollForm.annualIncrDateShow[i].value !="" )
				{
				
					var today=new Date();
					var curyear = today.getYear();
					var len=document.payrollForm.annualIncrDate[i].value.length;
					if(len==5)
					{
						document.payrollForm.annualIncrDate[i].value=document.payrollForm.annualIncrDate[i].value+"/";
						document.payrollForm.annualIncrDate[i].value=document.payrollForm.annualIncrDate[i].value+ curyear;
					}
					else if(len==6)
					{
						document.payrollForm.annualIncrDate[i].value=document.payrollForm.annualIncrDate[i].value+ curyear;
					}
					
					if(len==0)
					{
						if(document.payrollForm.annualIncrDateShow[i].value.length ==10)
						{
							document.payrollForm.annualIncrDate[i].value=document.payrollForm.annualIncrDateShow[i].value.substr(0,5);
						}else
						{
							document.payrollForm.annualIncrDate[i].value=document.payrollForm.annualIncrDateShow[i].value;
						}
						document.payrollForm.annualIncrDate[i].value=document.payrollForm.annualIncrDate[i].value+"/";
						document.payrollForm.annualIncrDate[i].value=document.payrollForm.annualIncrDate[i].value+ curyear;
						document.payrollForm.annualIncrDateShow[i].value=document.payrollForm.annualIncrDateShow[i].value.substr(0,5);
					}
				}
		
				if(i>=1)
				{
					if(compareDate(document.payrollForm.effDate[i].value,document.payrollForm.effDate[i-1].value) == 1)
					{
						alert('<bean:message key="alertEffGtPreDate"/>');
						document.payrollForm.effDate[i].focus();
						document.payrollForm.effDate[i].value="";
						return false;
					}
				}
		
				if(document.payrollForm.currDailyPay[i].value != ""&&document.payrollForm.currMnthPay[i].value != ""   
			 		||document.payrollForm.currMnthPay[i].value != "" && document.payrollForm.currDailyPay[i].value != "" ) 
				{ 
				
				    alert("Enter either Monthly or Daily Pay");
					document.payrollForm.currMnthPay[i+1].value="";
					document.payrollForm.currDailyPay[i+1].value="";
			        return false;
			    }
			 	if(document.payrollForm.currDailyPay[i].value == "" && document.payrollForm.currMnthPay[i].value == ""
			 		||document.payrollForm.currMnthPay[i].value == "" &&  document.payrollForm.currDailyPay[i].value =="")
			    { 
				
					alert("Enter either Monthly or Daily Pay");
					return false;
				
			    }
			}
		}
		else if(rows==3)
		{
			if(document.payrollForm.currDailyPay.value != ""&&document.payrollForm.currMnthPay.value != ""   
			 ||document.payrollForm.currMnthPay.value != ""&&document.payrollForm.currDailyPay.value != "" )
			{ 
			    alert("Enter either Monthly or Daily Pay");
				document.payrollForm.currMnthPay.value="";
				document.payrollForm.currDailyPay.value="";
		        return false;
			}
			if(document.payrollForm.currDailyPay.value == "" && document.payrollForm.currMnthPay.value == ""
			 ||document.payrollForm.currMnthPay.value == "" &&  document.payrollForm.currDailyPay.value =="")
			{ 
			    alert("Enter either Monthly or Daily Pay");
				return false;
				
			}
			 
			if(document.payrollForm.effDate.value=="" )
			{
				alert('<bean:message key="alertEnterEffDate"/>');
				document.payrollForm.effDate.focus();
				return false;
			}
			if(document.payrollForm.payScaleHeader.value=="" || document.payrollForm.payScaleHeader.value=="0")
			{
				alert('<bean:message key="alertPayscaleNme"/>');
				document.payrollForm.payScaleHeader.focus();
				return false;
			} 
			
			if(document.payrollForm.annualIncrDateShow.value=="" )
			{
				alert('<bean:message key="alertEnterAnnIncDate"/>');
				document.payrollForm.annualIncrDateShow.focus();
				return false;
			}
		
			if(document.payrollForm.annualIncrDateShow.value !="" )
			{
				var today=new Date();
				var curyear = today.getYear();
		        var len=document.payrollForm.annualIncrDate.value.length;
				if(len==5)
				{
					document.payrollForm.annualIncrDate.value=document.payrollForm.annualIncrDate.value+"/";
					document.payrollForm.annualIncrDate.value=document.payrollForm.annualIncrDate.value+ curyear;
				}
				else if(len==6)
				{
					document.payrollForm.annualIncrDate.value=document.payrollForm.annualIncrDate.value+ curyear;
				}
				if(len==0)
				{
					document.payrollForm.annualIncrDate.value=document.payrollForm.annualIncrDateShow.value.substr(0,5);
					document.payrollForm.annualIncrDate.value=document.payrollForm.annualIncrDate.value+"/";
					document.payrollForm.annualIncrDate.value=document.payrollForm.annualIncrDate.value+ curyear;
					document.payrollForm.annualIncrDateShow.value=document.payrollForm.annualIncrDateShow.value.substr(0,5);
				}
			}
		}
	}
	
	var personalInfoId=<%= egpimsPersonalInformation.getIdPersonalInformation().toString()%>;
	document.payrollForm.action = "${pageContext.request.contextPath}/empPayroll/payrollCreate.do?submitType=saveDetailsEmployee&Id="+personalInfoId;

}

function checkMajorsnumeric(obj){
	if(obj.value!=""){
		var num=obj.value;
		var objRegExp  = /^[A-Za-z0-9-/()]+$/;
		if(!objRegExp.test(num)){
			alert('<bean:message key="alertNoSpecChar"/>');
			obj.value="";
			obj.focus();
		}
	}
}
function checkForUnique(obj)
{
	if(obj.value!="")
	{
		var dedId;
		var curEff= obj.value;
		var table;
		table = document.getElementById("PayHeaderTable");
		var rowObj = getRow(obj);
		var len = rowObj.rowIndex;
	
		if(len > 2)
		{
			for(var i=len-1;i>=2;i--)
			{
				var eff = getControlInBranch(table.rows[i],'effDate').value;
				
				if(curEff == eff)
				{
			        alert("Duplicate effective date ");
					obj.value="";
					var payscaleHdrObj = getControlInBranch(table.rows[i+1],'payScaleHeader');
					payscaleHdrObj.options.length=0;
					payscaleHdrObj.options[0] = new Option("--Choose--","");
				}
			}
		}
	 
	 }
 }

 function populateBranch()
 {
	
	if(document.getElementById('bank')!=null && document.getElementById('bank').value!='0')
			checkBranch(document.getElementById('bank').value);
 	<%
	  
	Set empBankDet=egpimsPersonalInformation.getEgpimsBankDets();
	
	Integer branchId = 0;
	if(empBankDet!=null && !empBankDet.isEmpty())
	 {
		for(Iterator itr = empBankDet.iterator();itr.hasNext();)
		{
			branchId = ((BankDet)itr.next()).getBranch().getId();
		}
	 }
	
	 %>

		
	var branch = <%= branchId %>;
	document.getElementById('branch').value = branch;

}


function addButtonPayscale(tbl,obj,objr)
{
   if(checkPayHead(tbl))
   {
	   addRowToTable(tbl,obj);
   }
}

//Changing according to row

function checkPayHead(tableName)
{
	
	if(tableName=='PayHeaderTable')
	{
		var tbl = document.getElementById('PayHeaderTable');
		var rCount=tbl.rows.length-2;

		if(tbl.rows.length > 3)
		{
			if(document.payrollForm.effDate[rCount-1].value=="" )
			{
				alert('<bean:message key="alertEnterEffDate"/>');
				document.payrollForm.effDate[rCount-1].focus();
				return false;
			}
			if(document.payrollForm.payScaleHeader[rCount-1].value=="" || document.payrollForm.payScaleHeader[rCount-1].value=="0")
			{
				alert('<bean:message key="alertPayscaleNme"/>');
				document.payrollForm.payScaleHeader[rCount-1].focus();
				return false;
			}
	        if(document.payrollForm.annualIncrDateShow[rCount-1].value =="" )
			{
				alert('<bean:message key="alertEnterAnnIncDate"/>');
				document.payrollForm.annualIncrDateShow[rCount-1].focus();
				return false;
			}
			
			if(document.payrollForm.annualIncrDateShow[rCount-1].value !="" )
			{
				var today=new Date();
				var curyear = today.getYear();
				var len=document.payrollForm.annualIncrDate[rCount-1].value.length;
		
				if(len==5)
				{
					document.payrollForm.annualIncrDate[rCount-1].value=document.payrollForm.annualIncrDate[rCount-1].value+"/";
					document.payrollForm.annualIncrDate[rCount-1].value=document.payrollForm.annualIncrDate[rCount-1].value+ curyear;
				}
				else if(len==6)
				{
					document.payrollForm.annualIncrDate[rCount-1].value=document.payrollForm.annualIncrDate[rCount-1].value+ curyear;
				}
				
				if(len==0)
				{
					if(document.payrollForm.annualIncrDateShow[rCount-1].value.length ==10)
					{
						document.payrollForm.annualIncrDate[rCount-1].value=document.payrollForm.annualIncrDateShow[rCount-1].value.substr(0,5);
					}else
					{
						document.payrollForm.annualIncrDate[rCount-1].value=document.payrollForm.annualIncrDateShow[rCount-1].value;
					}
					document.payrollForm.annualIncrDate[rCount-1].value=document.payrollForm.annualIncrDate[rCount-1].value+"/";
					document.payrollForm.annualIncrDate[rCount-1].value=document.payrollForm.annualIncrDate[rCount-1].value+ curyear;
					document.payrollForm.annualIncrDateShow[rCount-1].value=document.payrollForm.annualIncrDateShow[rCount-1].value.substr(0,5);
				}
			}
		
			if(rCount-1>=1)
			{
				if(compareDate(document.payrollForm.effDate[rCount-1].value,document.payrollForm.effDate[rCount-1-1].value) == 1)
				{
					alert('<bean:message key="alertEffGtPreDate"/>');
					document.payrollForm.effDate[rCount-1].focus();
					document.payrollForm.effDate[rCount-1].value="";
					return false;
				}
			}
		
			if(document.payrollForm.currDailyPay[rCount-1].value != ""&&document.payrollForm.currMnthPay[rCount-1].value != ""   
			 ||document.payrollForm.currMnthPay[rCount-1].value != "" && document.payrollForm.currDailyPay[rCount-1].value != "" ) 
			{ 
			    alert("Enter either Monthly or Daily Pay");
				document.payrollForm.currMnthPay[rCount-1+1].value="";
				document.payrollForm.currDailyPay[rCount-1+1].value="";
		        return false;
		    }
			
			if(document.payrollForm.currDailyPay[rCount-1].value == "" && document.payrollForm.currMnthPay[rCount-1].value == ""
			 ||document.payrollForm.currMnthPay[rCount-1].value == "" &&  document.payrollForm.currDailyPay[rCount-1].value =="")
			{ 
			    alert("Enter either Monthly or Daily Pay");
				return false;
			}
			
		}
		else if(tbl.rows.length == 3)
		{
			if(document.payrollForm.currDailyPay.value != ""&&document.payrollForm.currMnthPay.value != ""   
				 ||document.payrollForm.currMnthPay.value != ""&&document.payrollForm.currDailyPay.value != "" )
			{ 
			
			    alert("Enter either Monthly or Daily Pay");
				document.payrollForm.currMnthPay.value="";
				document.payrollForm.currDailyPay.value="";
		        return false;
			}
			if(document.payrollForm.currDailyPay.value == "" && document.payrollForm.currMnthPay.value == ""
			 ||document.payrollForm.currMnthPay.value == "" &&  document.payrollForm.currDailyPay.value =="")
			{ 
				alert("Enter either Monthly or Daily Pay");
				return false;
				
			}
				 
			if(document.payrollForm.effDate.value=="" )
			{
				alert('<bean:message key="alertEnterEffDate"/>');
				document.payrollForm.effDate.focus();
				return false;
			}
			if(document.payrollForm.payScaleHeader.value=="" || document.payrollForm.payScaleHeader.value=="0")
			{
				alert('<bean:message key="alertPayscaleNme"/>');
				document.payrollForm.payScaleHeader.focus();
				return false;
			} 
				

			if(document.payrollForm.annualIncrDateShow.value=="" )
			{
				alert('<bean:message key="alertEnterAnnIncDate"/>');
				document.payrollForm.annualIncrDateShow.focus();
				return false;
			}

			if(document.payrollForm.annualIncrDateShow.value !="" )
			{
				var today=new Date();
				var curyear = today.getYear();
            	var len=document.payrollForm.annualIncrDate.value.length;
				if(len==5)
				{
					document.payrollForm.annualIncrDate.value=document.payrollForm.annualIncrDate.value+"/";
					document.payrollForm.annualIncrDate.value=document.payrollForm.annualIncrDate.value+ curyear;
				}
				else if(len==6)
				{
					document.payrollForm.annualIncrDate.value=document.payrollForm.annualIncrDate.value+ curyear;
				}
				
				if(len==0)
				{
					
				document.payrollForm.annualIncrDate.value=document.payrollForm.annualIncrDateShow.value.substr(0,5);
				document.payrollForm.annualIncrDate.value=document.payrollForm.annualIncrDate.value+"/";
				document.payrollForm.annualIncrDate.value=document.payrollForm.annualIncrDate.value+ curyear;
				document.payrollForm.annualIncrDateShow.value=document.payrollForm.annualIncrDateShow.value.substr(0,5);
				}
			}
		}
		else
		{
			if(tbl.rows.length>3)
			{	
				if(document.payrollForm.comments[rCount-1].value=="")
				{
					alert("Please Give your comments for change");
					document.pIMSForm.comments[rCount-1].focus();
					return false;
				}
				if(document.payrollForm.commentDate[rCount-1].value=="")
				{
					alert("Please Enter the current Date");
					document.pIMSForm.commentDate[rCount-1].focus();
					return false;
				}
				if(document.payrollForm.reason[rCount-1].value=="")
				{
					alert("Please Enter the reason");
					document.pIMSForm.reason[rCount-1].focus();
					return false;
				}
				
			}
		}
	}
	
	return true;
}

function addRowToTable(tbl,obj)
{
  	tableObj=document.getElementById(tbl);
  	var rowObj1=getRow(obj);
  	var tbody=tableObj.tBodies[0];
  	var lastRow = tableObj.rows.length-1;
	if(tbl=='PayHeaderTable')
  	{
		var rowObj = tableObj.rows[lastRow].cloneNode(true);
       	tbody.appendChild(rowObj);
  	   	var remlen1=document.payrollForm.effDate.length;
 	   	document.payrollForm.effDate[remlen1-1].value="";
	   	document.payrollForm.payScaleId[remlen1-1].value="0";
	   	document.payrollForm.payScaleHeader[remlen1-1].options.length=0;
 	   	document.payrollForm.payScaleHeader[remlen1-1].options[0] = new Option("--Choose--","");
 	   	document.payrollForm.annualIncrDate[remlen1-1].value="";
	   	document.payrollForm.annualIncrDateShow[remlen1-1].value="";
	   	document.payrollForm.basicFrom[remlen1-1].value="";
		document.payrollForm.basicto[remlen1-1].value="";
		document.payrollForm.currMnthPay[remlen1-1].value="";
		document.payrollForm.currDailyPay[remlen1-1].value="";
		document.payrollForm.stagnantPayId[remlen1-1].value="N";
	 }
}
 

function deletePayscaleRow(table,obj)
{
	if(table=='PayHeaderTable')
	{
		var tbl = document.getElementById(table);
		var rowNumber=getRow(obj).rowIndex;
		var idPayscale =	getControlInBranch(tbl.rows[rowNumber],'payScaleId').value;
	
		if(${fn:length(payrollForm.comments)}<(eval(rowNumber)-2))
		{
			tbl.deleteRow(rowNumber);
			<%
			if(modeonloadView.equalsIgnoreCase("modify"))
			{
			%>
			if(idPayscale != null && idPayscale != "")
				populateDeleteSet("delPayscale" , idPayscale);
			
			<%
			}
			%>
		}
		else
		{
		     alert("You cannot delete this row");
		     return false;
		}
	}
}	

function populateDeleteSet(setName, delId)
{
	if(delId != null && delId != "")
	{
    	var http = initiateRequest();
		var url = "${pageContext.request.contextPath}/empPayroll/updateDelSets.jsp?type="+setName+"&id="+delId;
		http.open("GET", url, true);
		http.onreadystatechange = function()
		{
			if (http.readyState == 4)
			{
				if (http.status == 200)
				{
				       var statusString =http.responseText.split("^");

				 }
			}
		};
		http.send(null);
	}
}


</script>

