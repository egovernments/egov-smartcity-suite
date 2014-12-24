
		<%@ taglib uri="/tags/struts-html" prefix="html" %>
		<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
		<%@ taglib uri="/tags/struts-nested" prefix="nested"%>
		<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
		<%@ page import="java.util.*,
		org.egov.infstr.utils.*,
		org.apache.log4j.Logger,
		org.egov.pims.*,
		org.egov.pims.model.*,
		org.egov.pims.service.*,
		org.egov.pims.dao.*,
		org.egov.infstr.commons.*,
		org.egov.pims.commons.client.*,
		org.egov.lib.address.model.*,
		java.text.SimpleDateFormat,
		org.egov.pims.client.*"
		%>
		<script type="text/javascript" src="${pageContext.request.contextPath}/commonyui/build/yahoo/yahoo.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/commonyui/build/dom/dom.js"></script>
		<script type="text/javascript"
			src="/commonyui/build/autocomplete/autocomplete-debug.js"></script>
			<script type="text/javascript" src="${pageContext.request.contextPath}/commonyui/build/animation/animation.js"></script>
			<script type="text/javascript" src="${pageContext.request.contextPath}/commonyui/build/event/event-debug.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/commonyui/build/animation/animation.js"></script>
		<SCRIPT type="text/javascript" src="${pageContext.request.contextPath}/commonjs/ajaxCommonFunctions.js"></Script>

		<link type="text/css" rel="stylesheet"
			href="${pageContext.request.contextPath}/commonyui/build/reset/reset.css">
		<link type="text/css" rel="stylesheet"
			href="${pageContext.request.contextPath}/commonyui/build/fonts/fonts.css">
		<link type="text/css" rel="stylesheet"
			href="${pageContext.request.contextPath}/commonyui/examples/autocomplete/css/examples.css">


		<style type="text/css">
				#codescontainer {position:absolute;left:11em;width:9%}
				#codescontainer .yui-ac-content {position:absolute;width:80%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
				#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:40%;background:#a0a0a0;z-index:9049;}
				#codescontainer ul {padding:5px 0;width:80%;}
				#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
				#codescontainer li.yui-ac-highlight {background:#ff0;}
				#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
</style>
		<%!
		private String getApplicationNumber()
		{
		String appNo = "";
		while(true)
		{	try
		{
		appNo = UtilityMethods.getRandomString();
		EmployeeServiceImpl eisServiceImpl=new EmployeeServiceImpl();
		if(eisServiceImpl.checkDisciplinaryNo(appNo)==true)
		{
		continue;
		}
		else
		{
		break;
		}
		}
		catch(Exception e)
		{}
		}
		return appNo;
		}
		%>
		<html>
		<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><bean:message key="PersonalInfoSys"/></title>

		<SCRIPT type="text/javascript" src="${pageContext.request.contextPath}/javascript/dateValidation.js" type="text/javascript"></SCRIPT>
		<script language="text/JavaScript" src="${pageContext.request.contextPath}/Admin-Homepage_files/dhtml.js" type="text/JavaScript"></script>

		</head>

		<%
		Set employeeOfficer = null;
		DisciplinaryPunishment egpimsDisciplinaryPunishment =null;
		PersonalInformation egpimsPersonalInformation =new PersonalInformation();
		EmployeeServiceImpl employeeServiceImpl=new EmployeeServiceImpl();
		if( ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("modify") ||  ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("view"))
		{

		egpimsDisciplinaryPunishment =employeeServiceImpl.getDisciplinaryPunishmentById(new Integer(request.getParameter("disiplinaryId").trim()));
		egpimsPersonalInformation = egpimsDisciplinaryPunishment.getEmployeeId();
		employeeOfficer = egpimsDisciplinaryPunishment.getEgpimsDetOfEnquiryOfficers();
		}
		else if(((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("create"))
		{
		egpimsDisciplinaryPunishment= new DisciplinaryPunishment();
		employeeOfficer = egpimsDisciplinaryPunishment.getEgpimsDetOfEnquiryOfficers();
		}

		if(employeeOfficer.isEmpty())
		{
		DetOfEnquiryOfficer egpimsDetOfEnquiryOfficers = new DetOfEnquiryOfficer();
		employeeOfficer=new HashSet();
		employeeOfficer.add(egpimsDetOfEnquiryOfficers);
		egpimsDisciplinaryPunishment.setEgpimsDetOfEnquiryOfficers(employeeOfficer);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Set delSet = new HashSet();
		session.setAttribute("DelSet",delSet);

		System.out.println("22"+request.getParameter("Id"));
		String Id = request.getParameter("Id");
		if(request.getParameter("Id")!=null)
		{
		System.out.println("22"+request.getParameter("Id"));
		egpimsPersonalInformation = employeeServiceImpl.getEmloyeeById(new Integer(request.getParameter("Id").trim()));
		}

		%>

		<script>
		function checkabsent(str)
		{
		if(str==1)
		{
		document.getElementById("absentRow").style.display="block";
		document.getElementById("absentRow1").style.display="block";
		}
		else
		{
		document.getElementById("absentRow").style.display="none";
		document.getElementById("absentRow1").style.display="none";
		}
		}
		function checkSuspended(str)
		{
		if(str==1)
		{
		document.getElementById("SuspendedRow").style.display="block";
		}
		else
		{
		document.getElementById("SuspendedRow").style.display="none";
		}
		}
		function absent()
		{
		if(document.pIMSForm.absent[0].checked == true)
		{
		document.getElementById("absentRow").style.display="block";
		document.getElementById("absentRow1").style.display="block";
		}
		else
		{
		document.getElementById("absentRow").style.display="none";
		document.getElementById("absentRow1").style.display="none";
		}
		}
		function Suspended()
		{
		if(document.pIMSForm.whetherSuspended[0].checked == true)
		{
		document.getElementById("SuspendedRow").style.display="block";
		}
		else
		{
		document.getElementById("SuspendedRow").style.display="none";
		}

		}
		function populateMap(obj)
		{
		var http = initiateRequest();
		var url = "${pageContext.request.contextPath}/pims/getPopulateEmpDisAjax.jsp?type="+obj;
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

		function goindex(arg)
		{

		if(arg == "Index")
		{

		document.forms("pIMSForm").action = "${pageContext.request.contextPath}/staff/index.jsp";
		document.forms("pIMSForm").submit();
		}


		}
		function addRow(obj,tableName,row)
		{
		var tbl=tableName;
		var rowO=tbl.rows.length;
		var name=obj.value;
		var tname = "resetRowValues"+name;
		if(rowO<11)
		{
		if(row != null)
		{
		var tbody=tbl.tBodies[0];
		var lastRow = tbl.rows.length;
		var rowObj = row.cloneNode(true);
		rowObj.document.getElementById('idemployee').value = 0;
		rIndex = rowObj.rowIndex;
		tbody.appendChild(rowObj);
		resetRowValues(lastRow-1,name);
		}
		}
		}
		function resetRowValues(lastRow,name)
		{
		if(name=="Add Row")
		{
		document.pIMSForm.enquiryOfficeName[lastRow].value="";
		document.pIMSForm.eoDesignation[lastRow].value="";
		document.pIMSForm.eoNoDate[lastRow].value="";
		document.pIMSForm.eoReDate[lastRow].value="";
		}
		}
		var firstLength = 0;
		var rIndex = 0;
		function setLength()
		{
		var tbl=document.getElementById('EOTable');
		var rowo=1;
		firstLength = rowo;

		}
		function deleteRow(obj,tableName,addButtion)
		{



		var tbl=tableName;
		var rowo=tbl.rows.length;
		if(rowo<=11)
		{
		var len = "idemployee"+firstLength;

		var d = document.forms[0].idemployee[0].value;

		populateMap(d);
		tbl.deleteRow(firstLength);
		return true;
		}
		}
		function validateNotEmpty( strValue )
		{
		var strTemp = strValue;
		strTemp = trimAll(strTemp);
		if(strTemp.length > 0)
		{
		return true;
		}
		return false;
		}

		function validateName( strValue)
		{
		var iChars = "!@#$%^&*()+=-[]\\\';,/{}|\":<>?0123456789";
		for (var i = 0; i < strValue.value.length; i++)
		{
		if (iChars.indexOf(strValue.value.charAt(i)) != -1)
		{
		alert('<bean:message key="alertValNme"/>');
		strValue.focus();
		return false;
		}
		}
		}
		function setReadOnly()
		{

		<%
		String modeonloadView=((String)(session.getAttribute("viewMode"))).trim();
		if(modeonloadView.equalsIgnoreCase("view"))
		{
		%>



		for(var i=0;i<document.forms[0].length;i++)
	    {
		if(document.forms[0].elements[i].value != "Close" )
		{
	    document.forms[0].elements[i].disabled =true

        }

        }


		<%
		}
		%>
		}
		function fillNeibrAfterSplit(obj,neibrObjName)
		{
			var currRow=getRow(obj);
			var indrow=currRow.rowIndex;

			yuiflag[currRow.rowIndex] = undefined;
			neibrObj=getControlInBranch(currRow,neibrObjName);
			var temp = obj.value;temp = temp.split("`-`");

			if(temp[2]==null || temp[2]=="") {
				neibrObj.value="";
				getControlInBranch(currRow,'eoDesignation').value="";
				return;
				}
			if(temp[1]==null && (neibrObj.value!='' || neibrObj.value!=null) ) {  return ;}
			else {
				 obj.value=temp[0];
				  var PronameTable= document.getElementById("EOTable");
				 	    var rows= PronameTable.rows.length;


					getControlInBranch(currRow,"enquiryOfficeName").value=temp[1];
					getControlInBranch(currRow,'eoDesignation').value = temp[3] ;
					document.getElementById("emploid").value=temp[2];
					neibrObj.value=temp[2];

				 }


			 	}





/*function fillNeibrAfterSplit(obj,neibrObjName)
  {
  	var currRow=getRow(obj);
  	yuiflag[currRow.rowIndex] = undefined;
  	neibrObj=getControlInBranch(currRow,neibrObjName);
  	var temp = obj.value;temp = temp.split("`-`");
  	obj.value=temp[0];
  	enquiryOfficeName=temp[1];
 	eoDesignation=temp[3];
 	department=temp[4];
 	yearOfJoining=temp[5];
 	payScaleName=temp[6];
 	empcode=temp[0];
  	if(temp[2]==null || temp[2]=="") { neibrObj.value=""; return; }
  	if(temp[1]==null && (neibrObj.value!='' || neibrObj.value!=null) ) {  return ;}
  	else {


  			neibrObj.value=temp[1];alert(temp[1]);
  			getControlInBranch(currRow,'eoDesignation').value = temp[3] ;
  			getControlInBranch(currRow,"enquiryOfficeName").value=temp[1];
  			//document.getElementById("monthAndYear").value="${asd}";
  		 }

   }*/


   		function autocompleteEmpCode(obj)
		{
		// set position of dropdown
		var src = obj;
		var target = document.getElementById('codescontainer');
		var posSrc=findPos(src);
		target.style.left=posSrc[0];
		target.style.top=posSrc[1]+25;
		if(obj.name=='employeeCode') target.style.left=posSrc[0]+0;

		target.style.width=500;

		var currRow=getRow(obj);
		var coaCodeObj = obj;
		if(yuiflag[currRow.rowIndex] == undefined)
		{
		//40 --> Down arrow, 38 --> Up arrow
		if(event.keyCode != 40 )
		{
		if(event.keyCode != 38 )
		{

		var oAutoComp1 = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainer', selectedEmpCode);
		oAutoComp1.queryDelay = 0;
		oAutoComp1.useShadow = true;
		oAutoComp1.maxResultsDisplayed = 15;
		oAutoComp1.useIFrame = true;
		}
		}
		yuiflag[currRow.rowIndex]=1;
		}
		}
		function onBodyLoad()
        {
			loadEmpCodes();
		}
		function loadEmpCodes()
		{
		var type='getAllEmployeeCodes';
		var url = "${pageContext.request.contextPath}/pims/process.jsp?type=" +type+ " ";
		var req2 = initiateRequest();
		req2.onreadystatechange = function()
		{
		if (req2.readyState == 4)
		{
		if (req2.status == 200)
		{

		var codes2=req2.responseText;
		var a = codes2.split("^");
		var codes = a[0];
		empCodeArray=codes.split("+");
		selectedEmpCode = new YAHOO.widget.DS_JSArray(empCodeArray);
		}
		}
		};
		req2.open("GET", url, true);
		req2.send(null);

        }
		function ButtonPressNew(arg)
		{


		if(arg == "savenew")
		{
		if(document.pIMSForm.natureOfAlligations.value == "" )
		{
		alert('<bean:message key="alertNtAlegation"/>');
		document.pIMSForm.natureOfAlligations.focus();
		return false;
		}
		if(document.pIMSForm.chargeMemoNumber.value == "" )
		{
		alert('<bean:message key="alertChargeMemoNum"/>');
		document.pIMSForm.chargeMemoNumber.focus();
		return false;
		}
		if(document.pIMSForm.chargeMemoDate.value == "" )
		{
		alert('<bean:message key="alertFillChargememoDt"/>');
		document.pIMSForm.chargeMemoDate.focus();
		return false;
		}
		if(document.pIMSForm.chargeMemoServedDate.value == "" )
		{
		alert('<bean:message key="alertFillServerDt"/>');
		document.pIMSForm.chargeMemoServedDate.focus();
		return false;
		}

		var tbl= document.getElementById("EOTable");
		var rows1= tbl.rows.length;
		if(rows1>2)
		{
		for(var i=0 ;i<rows1-1;i++)
		{
		//alert("Name="+document.propWithTaxCollectionForm.firstName[i].value);
		if(document.pIMSForm.enquiryOfficeName[i].value=="" )
		{
		alert('<bean:message key="alertFillEnqNme"/>');
		document.pIMSForm.enquiryOfficeName[i].focus();
		return false;
		}
		if(document.pIMSForm.eoDesignation[i].value=="" )
		{
		alert('<bean:message key="alertFillEnqOfficerDesg"/>');
		document.pIMSForm.eoDesignation[i].focus();
		return false;
		}


		}
		}else{
			if(document.pIMSForm.idemployee.value=="" )
			{
			alert('<bean:message key="alertFillEnqOfficerCode"/>');
			document.pIMSForm.idemployee.focus();
			return false;
			}

			if(document.pIMSForm.enquiryOfficeName.value=="" )
			{
			alert('<bean:message key="alertFillEnqNme"/>');
			document.pIMSForm.enquiryOfficeName.focus();
			return false;
			}
			if(document.pIMSForm.eoDesignation.value=="" )
			{
			alert('<bean:message key="alertFillEnqOfficerDesg"/>');
			document.pIMSForm.eoDesignation.focus();
			return false;
		}




		}

		if(document.pIMSForm.absent[0].checked == false)
		{
		document.pIMSForm.from.value = "<%="na"%>";
		document.pIMSForm.to.value = "<%="na"%>";

		}
		else if(document.pIMSForm.absent[0].checked == true)
		{
		if(document.pIMSForm.from.value=="" )
		{
		alert('<bean:message key="alertFillFromDtAbs"/>');
		document.pIMSForm.from.focus();
		return false;
		}
		if(document.pIMSForm.to.value=="" )
		{
		alert('<bean:message key="alertFillToDTAbs"/>');
		document.pIMSForm.to.focus();
		return false;
		}

		}

		if(document.pIMSForm.whetherSuspended[0].checked == false)
		{
		document.pIMSForm.dos.value = "<%="na"%>";
		document.pIMSForm.dor.value = "<%="na"%>";

		}
		else if(document.pIMSForm.whetherSuspended[0].checked == true)
		{
		if(document.pIMSForm.dos.value=="" )
		{
		alert('<bean:message key="alertFillSuspfmDt"/>');
		document.pIMSForm.dos.focus();
		return false;
		}
		if(document.pIMSForm.dor.value=="" )
		{
		alert('<bean:message key="alertfillTodt"/>');
		document.pIMSForm.dor.focus();
		return false;
		}

		}

		var submitType="";
		<%
		String mode1=((String)(session.getAttribute("viewMode"))).trim();
		if(mode1.equalsIgnoreCase("modify"))
		{
		%>

		submitType="modifyDetailsDisciplinary";

		<%
		}
		else if(mode1.equalsIgnoreCase("create"))
		{
		%>

		submitType="saveDetailsDisciplinary";
		<%
		}
		%>
		document.forms("pIMSForm").action = "${pageContext.request.contextPath}/pims/AfterPIMSAction.do?submitType="+submitType;
		document.forms("pIMSForm").submit();
		}



		}


		</script>


		<body onload = "setLength();absent();Suspended();setReadOnly();onBodyLoad();"/>

		<!-- Header Section Begins -->

		<!-- Header Section Ends -->

		<table align='center' id="table2" style="width: 820px; " >
		<tr>
		<td>
		<!-- Tab Navigation Begins -->

		<!-- Tab Navigation Ends -->

		<!-- Body Begins -->


		<!-- Body Begins -->

		<div align="center">
		<center>
		<html:form  action="/pims/AfterPIMSAction.do?submitType=saveDetails" >
		<input type=hidden name="applicationNumber" id="applicationNumber" value="<%= getApplicationNumber() %>" />
		<%
		if(request.getParameter("Id")!=null)
		{
		%>
		<input type=hidden name="Id" id="Id" value="<%= Id %>" />
		<%
		}
		%>

		<%
		if(request.getParameter("disiplinaryId")!=null)
		{
		%>
		<input type=hidden name="disiplinaryId" id="disiplinaryId" value="<%= request.getParameter("disiplinaryId").trim() %>" />
		<%
		}
		%>
		<table  cellpadding ="0" cellspacing ="0" border = "1" name ="disTable" id ="disTable" width="810" style="border: 1px solid #D7E5F2"  >
		<tbody>

		<tr>
		<td  height=20 bgcolor=#dddddd align=middle  class="tableheader" colspan = "4">
		<p><bean:message key="DisciplinaryPunishments"/>&nbsp;&nbsp;&nbsp;</td>
		</tr>

		<tr>
		<td class="labelcell" >
		<bean:message key="EmployeeName"/> </td>

		<td  class="labelcell" ><%=egpimsPersonalInformation.getEmployeeFirstName()==null?"":egpimsPersonalInformation.getEmployeeFirstName().toString()%></td>
		<td class="labelcell" >
		<bean:message key="EmployeeCode"/> </td>

		<td  class="labelcell" ><%=egpimsPersonalInformation.getEmployeeCode()==null?"":egpimsPersonalInformation.getEmployeeCode().toString()%></td>
		</tr>
		<tr>
		<td  class="labelcell" width="300"><bean:message key="NatureOfAllegation"/><SPAN class="leadon">*</SPAN></td>
		<td   class="labelcell" >
		<input class="fieldcell"  type="text" name="natureOfAlligations" id="natureOfAlligations"  value="<%=egpimsDisciplinaryPunishment.getNatureOfAlligations()==null?"":egpimsDisciplinaryPunishment.getNatureOfAlligations()%>" >
		</td>
		<td class="labelcell" width="300">
		<bean:message key="ChargeMemoNum"/><SPAN class="leadon">*</SPAN></td>
		<td   class="labelcell" >
		<input class="fieldcell"  type="text" name="chargeMemoNumber" id="chargeMemoNumber"    value="<%=egpimsDisciplinaryPunishment.getChargeMemoNo()==null?"":egpimsDisciplinaryPunishment.getChargeMemoNo()%>">
		</td>
		</tr>

		<tr>
		<td  class="labelcell" width="300"><bean:message key="chargeMemoDt"/><SPAN class="leadon">*</SPAN></td>
		<td   class="labelcell"  >
		<input class="fieldcell"  type="text" name="chargeMemoDate" id="chargeMemoDate" value="<%=egpimsDisciplinaryPunishment.getChargeMemoDate()==null?"":sdf.format(egpimsDisciplinaryPunishment.getChargeMemoDate())%>"   onBlur = "validateDateFormat(this)" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')">
		</td>
		<td class="labelcell" width="300">
		<bean:message key="chargeMemoServDt"/><SPAN class="leadon">*</SPAN></td>
		<td   class="labelcell" >

		<input class="fieldcell"  type="text" name="chargeMemoServedDate" id="chargeMemoServedDate" value="<%=egpimsDisciplinaryPunishment.getChargeMemoServedDate()==null?"":sdf.format(egpimsDisciplinaryPunishment.getChargeMemoServedDate())%>"   onBlur = "validateDateFormat(this)" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')">
		</td>
		</tr>
		<tr>
		<td  class="labelcell" width="300"><bean:message key="natureOfDisp"/></td>
		<td   class="labelcell" >
		<input class="fieldcell"  type="text" name="natureOfDisposal" id="natureOfDisposal"  value="<%=egpimsDisciplinaryPunishment.getNatureOfDisposal()==null?"":egpimsDisciplinaryPunishment.getNatureOfDisposal()%>" >
		</td>
		<td class="labelcell" width="300">
		<bean:message key="UnauthorisedAbs"/> </td>
		<%
		Character isa = egpimsDisciplinaryPunishment.getIsUnauthorisedAbsent();


		String hc = isa.toString();

		if(hc.equals("0"))
		{
		%>

		<td   class="labelcellforsingletd" > <bean:message key="Yes"/>
		<input type="radio" value="1" name="absent" id="absent" onclick="checkabsent(this.value)"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:message key="No"/><input type="radio" value="0" checked name="absent" id="absent" onclick="checkabsent(this.value)">
		<%

		}
		else
		{
		%>
		<td   class="labelcellforsingletd" > <bean:message key="Yes"/>
		<input type="radio" value="1" name="absent" id="absent"  checked onclick="checkabsent(this.value)"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:message key="No"/><input type="radio" value="0" name="absent" id="absent"  onclick="checkabsent(this.value)">
		<%
		}
		%>
		<tr id = "absentRow">
		<td  class="labelcell"  align = "left"><bean:message key="PeriodOfAbs"/></td>
		</tr>

		<tr id = "absentRow1">
		<td class="labelcell" ><bean:message key="fromDt"/></td>
		<td   class="labelcell" >
		<input class="fieldcell"  type="text" name="from" id="from" value ="<%=egpimsDisciplinaryPunishment.getAbsentFrom()==null?"":sdf.format(egpimsDisciplinaryPunishment.getAbsentFrom())%>" onkeyup="DateFormat(this,this.value,event,false,'3')"onBlur = "validateDateFormat(this)" >
		</td>
		<td  class="labelcell"   ><bean:message key="Todte"/></td>
		<td   class="labelcell" ><input class="fieldcell"  type="text" name="to" id="to"     value = "<%=egpimsDisciplinaryPunishment.getAbsentTo()==null?"":sdf.format(egpimsDisciplinaryPunishment.getAbsentTo())%>" align ="center"onkeyup="DateFormat(this,this.value,event,false,'3')" onBlur = "validateDateFormat(this)">
		</td>
		</tr>

		<tr>
		<td class="labelcell" width="250">
		<bean:message key="WhetherSuspended"/> </td>

		<%
		Character whsu = egpimsDisciplinaryPunishment.getWhetherSuspended();


		String check = whsu.toString();

		if(check.equals("0"))
		{
		%>

		<td   class="labelcellforsingletd"  width="305"> <bean:message key="Yes"/>
		<input type="radio" value="1" name="whetherSuspended" id="whetherSuspended" onclick="checkSuspended(this.value)"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:message key="No"/><input name="whetherSuspended" id="whetherSuspended"  type="radio" value="0"  checked onclick="checkSuspended(this.value)">
		<%

		}
		else
		{
		%>
		<td   class="labelcellforsingletd"  width="305"> <bean:message key="Yes"/>
		<input type="radio" value="1" checked name="whetherSuspended" id="whetherSuspended"  onclick="checkSuspended(this.value)"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:message key="No"/><input name="whetherSuspended" id="whetherSuspended"  type="radio" value="0"  onclick="checkSuspended(this.value)">
		<%
		}
		%>
		</td>

		<td class="labelcell" width="165">
		<bean:message key="WhetherSubsistencePd"/> </td>

		<%
		Character subpaid = egpimsDisciplinaryPunishment.getIsSubsistencePaid();


		String subpaidck = subpaid.toString();

		if(subpaidck.equals("0"))
		{
		%>

		<td   class="labelcellforsingletd" width="305"><bean:message key="Yes"/>
		<input type="radio" value="1" name="wsp" id="wsp" > <bean:message key="No"/><input name="wsp" id="wsp"  type="radio" value="0"  checked>
		<%

		}
		else
		{
		%>
		<td   class="labelcellforsingletd" width="305"> <bean:message key="Yes"/>
		<input type="radio" value="1" checked name="wsp" id="wsp"  ><bean:message key="No"/><input name="wsp" id="wsp"  type="radio" value="0"  >
		<%
		}
		%>
		</td>



		</tr>
		<tr id = "SuspendedRow">
		<td  class="labelcell" width="300"><bean:message key="fromDt"/></td>
		<td   class="labelcell" >
		<input class="fieldcell"  type="text" name="dos" id="dos" value="<%=egpimsDisciplinaryPunishment.getDateOfSuspension()==null?"":sdf.format(egpimsDisciplinaryPunishment.getDateOfSuspension())%>"   onBlur = "validateDateFormat(this)" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')">
		</td>
		<td class="labelcell" width="300">
		<bean:message key="Todte"/> </td>
		<td   class="labelcell" >
		<input class="fieldcell"  type="text" name="dor" id="dor" value="<%=egpimsDisciplinaryPunishment.getDateOfReinstatement()==null?"":sdf.format(egpimsDisciplinaryPunishment.getDateOfReinstatement())%>"    onBlur = "validateDateFormat(this)" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')">
		</td>
		</tr>
		</tbody>
		</table>
		<table  style="width: 810;"  cellpadding ="0" cellspacing ="0" border = "1" id="EOTable" name="EOTable" >
		<tbody>
		<tr>
        <td   class="labelcell" ><bean:message key="EnquiryOfficerId"/><SPAN class="leadon">*</SPAN></td>
        <td   class="labelcell" ><bean:message key="EnquiryOfficerNme"/><SPAN class="leadon">*</SPAN></td>
        <td   class="labelcell" ><bean:message key="EODesignation"/><SPAN class="leadon">*</SPAN></td>
		<td   class="labelcell" ><bean:message key="EONominatedDate"/></td>
		<td   class="labelcell" ><bean:message key="EOReportDate"/></td>
		</tr>
		<%
		Iterator itr1 = employeeOfficer.iterator();
		for(int i=0;itr1.hasNext();i++)
		{
		DetOfEnquiryOfficer egpimsDetOfEnquiryOfficer = ( DetOfEnquiryOfficer)itr1.next();
		String code = "";
		if(egpimsDetOfEnquiryOfficer.getEmployeeId() != null){
			code = egpimsDetOfEnquiryOfficer.getEmployeeId().getEmployeeCode().toString();
		}

		%>
		<tr id="EORow">
		<input type ="hidden" name = "emploid" id = "emploid" />
         <div id="codescontainer"></div></td>
	   <!-- <input type="hidden" name="idemployee" id="idemployee">	-->


        <td class="labelcell"><input type="text"  class="fieldcell" name="idemployee" id ="idemployee"  autocomplete="off"   onkeyup="autocompleteEmpCode(this);" onblur="fillNeibrAfterSplit(this,'emploid');trim(this,this.value)" value="<%= code%>" ></td>
		<td class="labelcell"><input class="fieldcell"     name="enquiryOfficeName" id="enquiryOfficeName" value="<%=egpimsDetOfEnquiryOfficer.getEnquiryOfficerName()==null?"":egpimsDetOfEnquiryOfficer.getEnquiryOfficerName()%>"></td>
		<td class="labelcell"><input class="fieldcell"    name="eoDesignation" id="eoDesignation" value="<%=egpimsDetOfEnquiryOfficer.getDesignation()==null?"":egpimsDetOfEnquiryOfficer.getDesignation()%>" ></td>
		<td class="labelcell"><input class="fieldcell"    name="eoNoDate" id="eoNoDate" onBlur = "validateDateFormat(this);validateDateJS(this)" value="<%=egpimsDetOfEnquiryOfficer.getNominatedDate()==null?"":sdf.format(egpimsDetOfEnquiryOfficer.getNominatedDate())%>" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')"></td>
		<td class="labelcell"><input class="fieldcell"    name="eoReDate" id="eoReDate" onBlur = "validateDateFormat(this);validateDateJS(this)" value="<%=egpimsDetOfEnquiryOfficer.getReportDate()==null?"":sdf.format(egpimsDetOfEnquiryOfficer.getReportDate())%>" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')"></td>
		<%
		}
		%>
		</tr>
		</tbody>
		</table>
		<table  style="width: 810;"  cellpadding ="0" cellspacing ="0" border = "1" >
		<tr>
		<%
		if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
		{
		%>
		<td> <input class="button2" id="addeoBtn" name="addeoBtn"  align ="center" type="button" value="Add Row" onclick="javascript:addRow(this,document.getElementById('EOTable'),document.getElementById('EORow'))">
		<input class="button2" id="deltp" name="deltp"  type="button" value="Delete Row" onclick="javascript:deleteRow(this,document.getElementById('EOTable'),document.getElementById('addeoBtn'))" ></td>
		<%
		}
		%>
		</tr>
		<tr>
		</table>
		<table  style="width: 810;"  cellpadding ="0" cellspacing ="0" border = "1" >
		<tbody>
		<tr>
		<td  class="labelcell" width="149"><bean:message key="PunishmentOrderDate"/></td>
		<td   class="labelcell" width="305" >
		<input class="fieldcell"  type="text" name="punismentOrderDate" id="punismentOrderDate" value="<%=egpimsDisciplinaryPunishment.getPunisOrderDate()==null?"":sdf.format(egpimsDisciplinaryPunishment.getPunisOrderDate())%>"  onBlur = "validateDateFormat(this)" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')">
		</td>
		<td  class="labelcell" width="149"><bean:message key="UnauthoriseabsperReg"/></td>
		<td   class="labelcell" width="305" >
		<input class="fieldcell"  type="text" name="howSuspention" id="howSuspention" value = "<%=egpimsDisciplinaryPunishment.getHowSuspention()==null?"":egpimsDisciplinaryPunishment.getHowSuspention()%>"  >
		</td>
		</tr>
		<tr>
		<td  class="labelcell" width="149"><bean:message key="NatureOfPunish"/></td>
		<td   class="labelcell" width="305">
		<input class="fieldcell"  type="text" name="natureOfPunishment" id="natureOfPunisment" value = "<%=egpimsDisciplinaryPunishment.getNatureOfPunisment()==null?"":egpimsDisciplinaryPunishment.getNatureOfPunisment()%>"   >
		</td>
		<td  class="labelcell" width="149"><bean:message key="PunishmentEffDate"/></td>
		<td  class="labelcell" width="305">

		<input class="fieldcell"  type="text" name="punismenteffectiveDate" id="punismenteffectiveDate" value="<%=egpimsDisciplinaryPunishment.getPunEffectDate()==null?"":sdf.format(egpimsDisciplinaryPunishment.getPunEffectDate())%>"   onBlur = "validateDateFormat(this)" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')">
		</td>
		</tr>
		</tbody>
		</table>
		<table align='center' class="tableStyle" cellpadding ="0" cellspacing ="0" border = "1"  name ="submit" id ="submit" width="785" style="border: 1px solid #D7E5F2">
		<tr >
		<%
		if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
		{
		%>
		<td><html:button styleClass="button" value="Save" property="b2" onclick="ButtonPressNew('savenew');" /></td>
		<%
		}
		%>
		<td><html:button styleClass="button" value="Search" property="b4" onclick="goindex('Index')" /></td>
		</tr>
		</table>
		</html:form>
		</table>
		</center>
		</div>
		</div>
		</td>
		</tr>
		<!-- Body Section Ends -->
		</<table>
		</body>
