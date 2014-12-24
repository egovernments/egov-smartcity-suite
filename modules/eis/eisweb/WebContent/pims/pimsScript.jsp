
<%@ include file="/includes/taglibs.jsp"%>
<%@ page
	import="java.util.*,org.egov.infstr.utils.*,org.apache.log4j.Logger,org.egov.pims.*,org.egov.pims.dao.*,org.egov.pims.service.*,org.egov.pims.utils.*,org.hibernate.LockMode,org.egov.pims.model.*,org.egov.infstr.commons.*,org.egov.infstr.commons.dao.*,org.egov.infstr.utils.*,org.egov.lib.rjbac.dept.*,org.egov.lib.address.model.*,java.math.BigDecimal,org.egov.lib.address.dao.*,java.text.SimpleDateFormat,java.util.StringTokenizer,org.egov.pims.client.*,org.egov.lib.rjbac.user.UserImpl"%>
<html>
<head>

<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
<title>eGov Employee Information System (EIS)</title>
<%
Logger LOGGER = Logger.getLogger("pimsscript.jsp");
	Integer i_status_Id;
	i_status_Id = Helper.getStatusTypeByDescAndModule();
	LOGGER.debug("*************************" + i_status_Id);

	Set personAddSet = null;
	PersonalInformation egpimsPersonalInformation = null;
	String id = "";
	if (request.getAttribute("employeeOb") != null) {

		//id = request.getParameter("Id").trim();

		egpimsPersonalInformation = (PersonalInformation) request
				.getAttribute("employeeOb");

	} else {
		egpimsPersonalInformation = new PersonalInformation();

	}

	personAddSet = egpimsPersonalInformation.getEgpimsPersonAddresses();
	String pan = egpimsPersonalInformation.getPanNumber();
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
%>

</head> 
<script type="text/javascript">
		var pan;
		var userUnique;
		var userCode;
		var toDateCheck ;
		var selectedPositions;
		var selectedEmpCode;
		var selectPostion;
		var yuiflag1 = new Array();
		var oAutoCompDesg;
		var oAutoCompPos;
		var globalSaveRowNumber;
		
	
		

		Array.prototype.contains = function (element) 
			{
				for (var i = 0; i < this.length; i++) {
				if (this[i] == element) {
				return true;
				}
				}
				return false;
			}



	

		
		function CompDate(obj)
		{
		var tbl= document.getElementById("EOTable");
		var len = EOTable.rows.length;

		if(len>2)
		{
		for(var i =0;i<len-1;i++)
		{
		if(document.pIMSForm.toDate[i].value !="")
		{

		if(document.pIMSForm.fromDate[i].value =="")
		{
		alert('<bean:message key="alertFromDate"/>');
		document.pIMSForm.fromDate[i].focus();
		document.pIMSForm.toDate[i].value ="";
		return false;
		}

		if(compareDate(document.pIMSForm.toDate[i].value,document.pIMSForm.fromDate[i].value) == 1||compareDate(document.pIMSForm.toDate[i].value,document.pIMSForm.fromDate[i].value)==0)
		{
		alert('<bean:message key="alertToGTFromDt"/>');
		document.pIMSForm.toDate[i].focus();
		document.pIMSForm.toDate[i].value="";
		return false;
		}

		}

		}
		}

		else if(document.pIMSForm.toDate.value !="")
		{

		if(document.pIMSForm.fromDate.value =="")
		{
		alert('<bean:message key="alertFromDate"/>');
		document.pIMSForm.fromDate.focus();
		document.pIMSForm.toDate.value ="";
		return false;
		}

		if(compareDate(document.pIMSForm.toDate.value,document.pIMSForm.fromDate.value) == 1||compareDate(document.pIMSForm.toDate.value,document.pIMSForm.fromDate.value)==0)
		{
		alert('<bean:message key="alertToGTFromDt"/>');
		document.pIMSForm.toDate.focus();
		document.pIMSForm.toDate.value="";
		return false;
		}
		}
		}


		
		var docNoRowIndex=null;
		var edudocNoRowIndex=null;
		var docNo = null;
		 var docNumberUpdater =function (docNumber){
		       docNo.value = docNumber;
		  }		
		
		
		function onUpload(obj)
		{
			docNoRowIndex=null;
		    docNoRowIndex = (getRow(obj).rowIndex-2);

		    var serviceDocLenth=document.getElementsByName("serviceDocNo").length;
		    var servicedocVal="";
		    var servicedocObj="";
		    if(serviceDocLenth==1)
			    {
		    	servicedocVal= document.getElementById("serviceDocNo").value;
		    	 servicedocObj= document.getElementById('serviceDocNo');
			    }
		    else if(serviceDocLenth>1)
			    {
		    	servicedocVal= document.getElementsByName("serviceDocNo")[docNoRowIndex].value;
		    	servicedocObj= document.getElementsByName('serviceDocNo')[docNoRowIndex];
			    }
		    
		    var url;
		    if(servicedocVal==null||servicedocVal==''||servicedocVal=='To be assigned')
		    {
		       url="/egi/docmgmt/basicDocumentManager.action?moduleName=EIS";
		    }
		    else
		    {
		       url = "/egi/docmgmt/basicDocumentManager!editDocument.action?docNumber="+servicedocVal+"&moduleName=EIS";
		    }
		    var wdth = 1000;
		    var hght = 400;
		    window.open(url,'docupload','width='+wdth+',height='+hght);
		   docNo=servicedocObj;
		 }

		function uploadEduDoc(obj)
		{
			edudocNoRowIndex=null;
			edudocNoRowIndex = (getRow(obj).rowIndex-2);
		
		    var eduDocLen=document.getElementsByName("eduDocNo").length;
		    var edudocVal="";
		    var edudocObj="";
		    if(eduDocLen==1)
			    {
		    	edudocVal= document.getElementById("eduDocNo").value;
		    	edudocObj= document.getElementById('eduDocNo');
			    }
		    else if(eduDocLen>1)
			    {
			    edudocVal= document.getElementsByName("eduDocNo")[edudocNoRowIndex].value;
			    edudocObj= document.getElementsByName('eduDocNo')[edudocNoRowIndex];
			    }
		    
		    var url;
		    if(edudocVal==null||edudocVal==''||edudocVal=='To be assigned')
		    {
		       url="/egi/docmgmt/basicDocumentManager.action?moduleName=EIS";
		    }
		    else
		    {
		       url = "/egi/docmgmt/basicDocumentManager!editDocument.action?docNumber="+edudocVal+"&moduleName=EIS";
		    }
		    var wdth = 1000;
		    var hght = 400;
		    window.open(url,'docupload','width='+wdth+',height='+hght);
		    docNo=edudocObj;
		 }


		function uploadTechDoc(obj){
			techdocNoRowIndex=null;
			techdocNoRowIndex = (getRow(obj).rowIndex-2);
		
		    var techDocLen=document.getElementsByName("techDocNo").length;
		    var techdocVal="";
		    var techdocObj="";
		    if(techDocLen==1)
			    {
		    	techdocVal= document.getElementById("techDocNo").value;
		    	techdocObj= document.getElementById('techDocNo');
			    }
		    else if(techDocLen>1)
			    {
			    techdocVal= document.getElementsByName("techDocNo")[techdocNoRowIndex].value;
			    techdocObj= document.getElementsByName('techDocNo')[techdocNoRowIndex];
			    }
		
		    var url;
		    if(techdocVal==null||techdocVal==''||techdocVal=='To be assigned')
		    {
		       url="/egi/docmgmt/basicDocumentManager.action?moduleName=EIS";
		    }
		    else
		    {
		       url = "/egi/docmgmt/basicDocumentManager!editDocument.action?docNumber="+techdocVal+"&moduleName=EIS";
		    }
		    var wdth = 1000;
		    var hght = 400;
		    window.open(url,'docupload','width='+wdth+',height='+hght);
		    docNo=techdocObj;
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

		function checkappoint(obj){
		var tbl= document.getElementById("EOTable");
		var len = EOTable.rows.length;
/*
		<egov:ifModuleEnabled moduleName="<%=EisConstants.MODULE_PAYROLL%>">
		if(document.pIMSForm.effDate.value !="" &&  document.pIMSForm.fromDate.value !="")

		if(document.pIMSForm.dateOfFA.value=="")
		{
		alert('<bean:message key="alertDoApp"/>');
		document.pIMSForm.dateOfFA.focus();
		document.pIMSForm.effDate.value="";
		document.pIMSForm.fromDate.value="";
		return false;
		}
		</egov:ifModuleEnabled>
		*/

		}


		//this method can be called from date of first appointment

		function CompeffecDate(obj)
		{
		
		if(compareDate(obj.value,document.pIMSForm.dateOfFA.value) == 1)
			{
			//alert("CompeffecDate");
			alert('<bean:message key="alertFromDtGtAppDt"/>'+' -'+document.pIMSForm.dateOfFA.value);
			document.getElementById('fromDateEnter').focus();
			document.getElementById('fromDateEnter').value="";
			return false;
			}


		}

		function checkDobWithDeceased(){
			var dateofbirth = document.getElementById("employeeDob");
			var deathdate = document.getElementById("deathDate");
			if ((dateofbirth != null && dateofbirth.value != "") && (deathdate != null && deathdate.value != "")){
				if (compareDate(deathdate.value,dateofbirth.value) == 1){
					alert('Death Date cannot be before date of birth');
				}
			}
		}
			
			
		function checkDeathDate(obj)
		{
			 var today = new Date();
    		 var day = today.getDate();
    		 var month = today.getMonth();
             var year = today.getYear();
			
			if (obj.value != null || obj.value != "")
			{
				var dte = obj.value;
				if (!validateDate(dte))
				{

					alert('Death Date cannot be in the future');
					obj.focus();
					obj.value="";
					return false;
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

		alert('<bean:message key="alertProperDOB"/>');
		obj.focus();
		obj.value="";
		return false;
		}
		}
		}

		//function check from date of birth
		function checkfromDateofBirth(obj)
		{

		if (obj.value != null || obj.value != "")
		{

		var date = obj.value;

		var year = date.substr(6,4);
		
		if(document.pIMSForm.dateOfFA.value!=null && document.pIMSForm.dateOfFA.value!="")
		{
		var appoDate = document.pIMSForm.dateOfFA.value;
		var appoYear= appoDate.substr(6,4);

		var diffYr = eval(appoYear) - (year);

		if(diffYr < 16)
		{

		alert('<bean:message key="alertApp15GtBd"/>');

		obj.focus();
		obj.value="";
		return false;

		}

		}
		
		}
		}

	
	function checkunique()
		{

		if(pan!=document.getElementById("panNumber").value)
		{

		uniqueChecking('${pageContext.request.contextPath}/commonyui/egov/uniqueCheckAjax.jsp',  'EG_EMPLOYEE', 'PAN_NUMBER', 'panNumber', 'yes', 'yes');
		}

		}

		function empAutoGeneratedCode()
		{
			<%String mode = ((String) (session.getAttribute("viewMode"))).trim();%>
			if(userCode!=document.getElementById("employeeCode").value)
			{
				uniqueChecking('${pageContext.request.contextPath}/commonyui/egov/uniqueCheckAjax.jsp',  'EG_EMPLOYEE', 'CODE', 'employeeCode', 'yes', 'no');
				document.getElementById("employeeCode").value=document.getElementById("employeeCode").value.toUpperCase();
				<%if (mode.equals("modify")) {%>	
					if(document.getElementById("employeeCode").value==''||document.getElementById("employeeCode").value==null)
						document.getElementById("employeeCode").value='<%=egpimsPersonalInformation.getEmployeeCode()%>';
				<%}%>		
					
			}

		}


function isEmpUserMapExists(url,tablename1,tablename2,columnname1,columnname2,fieldobj,uppercase,lowercase)
{
	var userName;
	var isUserNameMapped = "true";
	<%List empMapUserId = (List) session
					.getAttribute("empUserNotMappedList");
			Iterator iterate = empMapUserId.iterator();
			UserImpl entry = new UserImpl();%>
	
	var fieldvalue = document.getElementById(fieldobj).value;
	if (url != "" && tablename1 != "" && columnname1 != "" && tablename2 != "" && columnname2 != ""&& fieldvalue != "" && uppercase != "" && lowercase != "")	{
		fieldvalue = trimText(fieldvalue);
		var link = ""+url+"?tablename1=" + tablename1+"&tablename2=" +tablename2+"&columnname1=" + columnname1+"&columnname2=" 				   +columnname2+" &fieldvalue=" + fieldvalue+ "&uppercase=" + uppercase+"&lowercase=" + lowercase+ " ";
		var request = initiateRequest();
		request.onreadystatechange = function() {	
		if (request.readyState == 4) {
				if (request.status == 200) {
					var response=request.responseText.split("^");
					userName = document.getElementById("userFirstName").value;
					if(response[1] != "false"){
						<%while (iterate.hasNext()) {
				entry = (UserImpl) iterate.next();%>
								if(userName==trimAll('<%=entry.getUserName()%>'))
								{
									isUserNameMapped = "false";
								}
						<%}%>	
						if(isUserNameMapped=="true"){
							alert("Entered user name already exists ");
							document.getElementById(fieldobj).value="";
							document.getElementById(fieldobj).focus();
						}	
					}
				}
			}
		};
		request.open("GET",link, true);
		request.send(null);
	}
}	



		function checkIsUserMapped()
		{	
			if(userUnique != document.getElementById("userFirstName").value)
			{
				isEmpUserMapExists('${pageContext.request.contextPath}/pims/uniqueEmpUserCheck.jsp','EG_EMPLOYEE','EG_USER','ID_USER','USER_NAME','userFirstName','NO','YES');
			}	
		}		

		function checkUserunique()
		{

		if(userUnique != document.getElementById("userFirstName").value)
		{
		uniqueChecking('${pageContext.request.contextPath}/commonyui/egov/uniqueCheckAjax.jsp',  'EG_USER', 'USER_NAME', 'userFirstName', 'no', 'no');
		}
		}

		/*function checkIncr2Date()

		{
		<egov:ifModuleEnabled moduleName="<%=EisConstants.MODULE_PAYROLL%>" >
		var ptablermr =document.getElementById('PayHeaderTable');
		var len = ptablermr.rows.length;
		//alert(len);
		if(len==2)
		{
		var tempdt =document.pIMSForm.annualIncrDateShow.value;
		document.pIMSForm.annualIncrDate.value =tempdt;

		if(tempdt.length>5)
		{
		tempdt= tempdt.substr(0,5);
		document.pIMSForm.annualIncrDateShow.value=tempdt;
		}

		}
		if(len>2)
		{

		var annual=document.getElementsByName("annualIncrDateShow");

		for(var j=0;j<len-1;j++)
		{


		var tempdt =document.pIMSForm.annualIncrDateShow[j].value;
		document.pIMSForm.annualIncrDate[j].value =tempdt;

		if(tempdt.length>5)
		{
		tempdt= tempdt.substr(0,5);
		document.pIMSForm.annualIncrDateShow[j].value=tempdt;
		}
		}

		}
		</egov:ifModuleEnabled>

		}*/

		//to display/hide hod  on demand and copy the selected hod values from multiselectdropdown to grid
		function enableHodOndemand(str)
		{
		if(str==1)
		{
		document.getElementById("deptHodOnTopLbl").style.display="";
		document.getElementById("deptHodOnTopTd").style.display="";
		//document.getElementById('deptHodOnTopSelect').style.display="";
		
		}
		else
		{
		document.getElementById("deptHodOnTopLbl").style.display="none";
		document.getElementById("deptHodOnTopTd").style.display="none";
		//document.getElementById('deptHodOnTopSelect').style.display="none";
		}
			var tempDeptValue= document.getElementById('deptHodOnTopSelect');
			var outer=0;
			for(outer=0;outer<tempDeptValue.options.length;outer++)
			{				
	        			tempDeptValue.options[outer].selected=false;
	              		
	        }
		}
		//will get call on load
		function checkHod()
		{
		
		<%String modeOnHod = ((String) (session.getAttribute("viewMode")))
					.trim();
			if (!modeOnHod.equalsIgnoreCase("view")) {%>
		document.getElementsByName('checkhod')[1].checked=true;
		document.getElementById("deptHodOnTopLbl").style.display="none";
		document.getElementById("deptHodOnTopTd").style.display="none";
		//document.getElementById('deptHodOnTopSelect').style.display="none";
		
		<%} else {%>
		
		document.getElementsByName('checkhod').checked=true;
		<%}%>
		
		var Tbllen=document.getElementById('EOTable').rows.length;
			for(var i=0;i<Tbllen-1;i++)
			{
				if(document.getElementsByName("departmentIdHodSelect")[i].options.length==0 || document.getElementsByName("departmentIdHodSelect")[i].options.length=="")
				{
					document.getElementsByName("depId0")[i].style.display="none";
					document.getElementsByName("departmentIdDisplay")[i].style.display="";
				}
				else{
				document.getElementsByName("depId0")[i].style.display="";
					document.getElementsByName("departmentIdDisplay")[i].style.display="none";
				}
			}
		}

		function onLoadFns(){
		document.getElementById("Technical").style.display="none";
		
		document.getElementById("Assignment").style.display="none";
		
		document.getElementById("Nomimation").style.display="none";
		
		document.getElementById("ServiceDetailsHistory").style.display="none";
		
		document.getElementById("deathDate").style.display="none";
		
		document.getElementById("death").style.display="none";
		
		onLoadpis();
		setLength();
		
		checkHod();
		setReadOnly();
		
		//checkIncr2Date();
		//populatebasicOnload();
		//changes after employee master changes
		//populatePayscaleName();
		
		}

		function showPersonalInfo(){
			document.getElementById("Technical").style.display="none";
			
			document.getElementById("Assignment").style.display="none";
			
			document.getElementById("Nomimation").style.display="none";
			
			document.getElementById("ServiceDetailsHistory").style.display="none";
			
			//document.getElementById("deathDate").style.display="none";
			
			//document.getElementById("death").style.display="none";
			
			onLoadpis();
			setLength();
		
			checkHod();

			setReadOnly();
			
		
		
		}
		
		
		
		function initiateRequest() {
		if (window.XMLHttpRequest) {
		return new XMLHttpRequest();
		} else if (window.ActiveXObject) {
		isIE = true;
		return new ActiveXObject("Microsoft.XMLHTTP");
		}
		}
		/*
		* This function returns absolue left and top position of the object
		*/
		function findPos(obj)
		{
		var curleft = curtop = 0;
		if (obj.offsetParent)
		{
		curleft = obj.offsetLeft;
		curtop = obj.offsetTop;
		while (obj = obj.offsetParent)
		{	//alert(obj.nodeName);
		curleft =curleft + obj.offsetLeft;
		curtop =curtop + obj.offsetTop; //alert(curtop);
		}
		}
		return [curleft,curtop];
		}
		function getRow(obj)
		{
		if(!obj)return null;
		tag = obj.nodeName.toUpperCase();
		while(tag != 'BODY'){
		if (tag == 'TR') return obj;
		obj=obj.parentNode;
		tag = obj.nodeName.toUpperCase();
		}
		return null;
		}

		//Used for Auto Complete
		function loadEmpDesignation() { 
			var type='getAllDesignation';
			var url = "${pageContext.request.contextPath}/pims/employeeGradeAjax.jsp?type="+type;
			var req2 = initiateRequest();
			req2.open("GET", url, false);
			req2.send(null);
			if (req2.status == 200)
			{
				var codes2=req2.responseText;
				var a = codes2.split("^");
				var codes = a[0];
				empCodeArray=codes.split("+");
				selectedEmpCode = new YAHOO.widget.DS_JSArray(empCodeArray);
				
			}
		}

		
		function checkDeptDesigIfEmpty()
		{
			
			if(document.getElementById("deptEntry").value == 0 && (document.pIMSForm.checkPrimary[0].checked))
			{
				alert('Please select department');
				document.getElementById('positionEntryName').value="";
				document.getElementById("deptEntry").focus();
				return;
			}
			if( document.getElementById("desginationName").value == "")
			{
					alert ('Please select designation');
					document.getElementById('positionEntryName').value="";
					document.getElementById("desginationName").focus();
					return;
			}

		}

		
		
		
		//Used for Auto Complete
		function autocompleteForDesignation(obj,event){
		// set position of dropdown
		var src = obj;
		var target = document.getElementById('codescontainer');
		var posSrc=findPos(src);
		target.style.left=posSrc[0];

		target.style.top=posSrc[1]+25;
		
		if(obj.name=='desginationName') target.style.left=posSrc[0]+0;
		target.style.width=500;

		var currRow=getRow(obj);
		var coaCodeObj = obj;
        if(yuiflag1[currRow.rowIndex] == undefined)
		{	
		//40 --> Down arrow, 38 --> Up arrow
		if(event.keyCode != 40 )
		{

		if(event.keyCode != 38 )
		{
		oAutoCompDesg = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainer', selectedEmpCode);
		oAutoCompDesg.queryDelay = 0;
		oAutoCompDesg.useShadow = true;
		oAutoCompDesg.maxResultsDisplayed = 15;
		///oAutoCompDesg.useIFrame = true;
		}

		}
		}
		}
		function fillDesgAfterSplit(obj,neibrObjName){

			
		var currRow=getRow(obj);	
		yuiflag1[currRow.rowIndex] = undefined;
		neibrObj=getControlInBranch(currRow,neibrObjName);
		//neibrObj=document.getElementsByName(neibrObjName)[currRow.rowIndex-1];
		var temp = obj.value;
		
		temp = temp.split("`-`");
		if(temp[1]!='' && temp[1]!=undefined){
		neibrObj.value = temp[1];
		}	
		obj.value=temp[0];	
		
		


		}



		//Used for Auto Complete
		function autocompletePosition(obj,event){
		// set position of dropdown
		
		var src = obj;
		var target = document.getElementById('codescontainerPos');
		var posSrc=findPos(src);
		target.style.left=posSrc[0];

		target.style.top=posSrc[1]+25;
		if(obj.name=='positionEntryName') target.style.left=posSrc[0]+0;
		target.style.width=500;

		var currRow=getRow(obj);
		var coaCodeObj = obj;

		if(yuiflag1[currRow.rowIndex] == undefined)
		{	
		//40 --> Down arrow, 38 --> Up arrow
		if(event.keyCode != 40 )
		{
		if(event.keyCode != 38 )
		{
		oAutoCompPos = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainerPos', selectPostion);
		oAutoCompPos.queryDelay = 0;
		oAutoCompPos.useShadow = true;
		oAutoCompPos.maxResultsDisplayed = 15;
		//oAutoCompPos.useIFrame = true;
		}
		}

		}

		}
		
		function fillPositionAfterSplit(obj,neibrObjName){
		
			var currRow=getRow(obj);	
			yuiflag1[currRow.rowIndex] = undefined;
			neibrObj=getControlInBranch(currRow,neibrObjName);  
			
			var temp = obj.value;
			temp = temp.split("`-`");
			
			if(temp[1]!='' && temp[1]!=undefined){
			neibrObj.value = temp[1];
			}	
			obj.value=temp[0];
		
		}
	




		//to calculate the difference between 2 dates
		function diffdate(obj)
		{
		// need payroll
		/*
		<egov:ifModuleEnabled moduleName="<%=EisConstants.MODULE_PAYROLL%>" >
		var fromDate=obj.value;
		var dateOfFA = document.getElementById("dateOfFA").value;
		var tbl= document.getElementById("EOTable");
		var row=getRow(obj);
		if(row.rowIndex-1==0)
		{
		var x=document.getElementsByName('fromDate');
		var froDate=x[0];

		var http = initiateRequest();
		var url = "<%=request.getContextPath()%>/pims/checkDaysDifferenceAjax.jsp?dateOfFA="+dateOfFA+"&fromDate="+fromDate;
		http.open("GET", url, true);
		http.onreadystatechange = function()
		{

		if (http.readyState == 4)
		{

		if (http.status == 200)
		{

		var statusString =http.responseText.split("^");
		if(""!=trimAll(statusString[0]) && statusString[0]!="0")
		{

		alert('Date of first appointment and assignment From Date are not matching.Please verify');


		}

		}
		}

		};
		http.send(null);
		}

		</egov:ifModuleEnabled>

        */

		}

		

		function setReadOnly()
		{


			<%String modeonloadView = ((String) (session.getAttribute("viewMode")))
					.trim();
			if (modeonloadView.equalsIgnoreCase("view")) {%>
			//pIMSForm("employeeCode").readOnly= true;
			document.getElementById("employeeDob").disabled= true;
			document.getElementById("firstName").disabled= true;
			document.getElementById("middleName").disabled= true;
			//if no values for last name set empty
			if(document.getElementById("lastName").value=='Last Name')
				document.getElementById("lastName").value='';
			//if no values for middle name set empty
			if(document.getElementById("middleName").value=='Middle Name')
				document.getElementById("middleName").value='';
	
			document.getElementById("lastName").disabled= true;
	
			if(document.getElementById("fatherfirstName").value=='First Name')
				document.getElementById("fatherfirstName").value='';
	
			//if no values for last name set empty
			if(document.getElementById("fatherlastName").value=='Last Name')
				document.getElementById("fatherlastName").value='';
			//if no values for middle name set empty
			if(document.getElementById("fathermiddleName").value=='Middle Name')
				document.getElementById("fathermiddleName").value='';
	
	
			document.getElementById("fatherfirstName").disabled= true;
			document.getElementById("fathermiddleName").disabled= true;
			document.getElementById("fatherlastName").disabled= true;
			if(document.getElementById("identificationMarks1")!=null)
			{
				document.getElementById("identificationMarks1").disabled= true;
			}
			if(document.getElementById("identificationMarks2")!=null)
			{
				document.getElementById("identificationMarks2").disabled= true;
			}
			document.getElementById("panNumber").disabled= true;
			document.getElementById("userFirstName").disabled=true;
	
			if(document.getElementById("gender").value!=null)
			{
				document.getElementById("gender").disabled= true;
			}
			document.getElementById("statusTypeId").disabled= true;
			document.getElementById("statusMaster").disabled= true;
			document.getElementById("empGrpMstr").disabled= true;
			
			document.getElementById("bloodGroup").disabled= true;
			document.getElementById("religionId").disabled= true;
			document.getElementById("communityId").disabled= true;
			document.getElementById("lanId").disabled=true;
			document.getElementById("tamillangaugequlified").disabled= true;
			document.getElementById("motherTounge").disabled= true;
			//pIMSForm("propertyNopre").readOnly= true;
			//pIMSForm("streetNamepre").readOnly= true;
			document.getElementById("propertyNoper").disabled= true;
			document.getElementById("propertyNoper").disabled= true;
			document.getElementById("streetNameper").disabled= true;
	
	
			document.getElementById("dateOfFA").disabled= true;
			document.getElementById("dateOfjoin").disabled= true;
			document.getElementById("dateOfRetirement").disabled= true;
			document.getElementById("retirementAge").disabled= true;
			
			//Death Date
			var isDeceased = document.getElementById("statusTypeId").options[document.getElementById("statusTypeId").selectedIndex].text
			if(isDeceased.toUpperCase()=='DECEASED')
			{
				document.getElementById.pIMSForm("deathDate").disabled= true;
			}
	
			var EOTable= document.getElementById("EOTable");
			var rows= EOTable.rows.length;
			if(rows>2)
			{
			for(var i=0 ;i<rows-1;i++)
			{
			document.pIMSForm.fromDate[i].disabled= true;
			document.pIMSForm.toDate[i].disabled= true;
			document.pIMSForm.fundId[i].disabled= true;
			document.pIMSForm.functionId[i].disabled= true;
			document.pIMSForm.designationId[i].disabled= true;
			document.pIMSForm.posName[i].disabled= true;
			document.pIMSForm.functionaryId[i].disabled= true;
			document.pIMSForm.departmentId[i].disabled= true;
			<%String modeOnHodDisMoreVal = ((String) (session
						.getAttribute("viewMode"))).trim();
				if (!modeOnHodDisMoreVal.equalsIgnoreCase("view")) {%>
			document.pIMSForm.hod[i].disabled= true;
			<%}%>
			document.pIMSForm.mainDepartmentId[i].disabled= true;
			document.pIMSForm.assignmentOrderNo[i].disabled= true;
	
			document.pIMSForm.gradeId[i].disabled= true;
	
	
			}
			}
			else
			{
			document.getElementById("fromDate").disabled= true;
			document.getElementById("toDate").disabled= true;
			document.getElementById("fundId").disabled= true;
			document.getElementById("functionId").disabled= true;
			document.getElementById("designationId").disabled= true;
			document.getElementById("posName").disabled= true;
			document.getElementById("functionaryId").disabled= true;
			document.getElementById("departmentId").disabled= true;
			
			<%String modeOnHodDis = ((String) (session
						.getAttribute("viewMode"))).trim();
				if (!modeOnHodDis.equalsIgnoreCase("view")) {%>
			document.getElementById("hod").disabled= true;
			<%}%>
			document.getElementById("mainDepartmentId").disabled= true;
			document.getElementById("assignmentOrderNo").disabled= true;
	
			document.getElementById("gradeId").disabled= true;
	
			}
		
		    
			//read only for service 
			var serviceTable= document.getElementById("ServiceTable");
			var rows= serviceTable.rows.length;
			if(rows>3)
			{
				for(var i=0 ;i<rows-2;i++)
				{
					document.pIMSForm.commentDate[i].disabled= true;
					document.pIMSForm.comments[i].disabled= true;
					document.pIMSForm.reason[i].disabled= true;
					document.pIMSForm.serviceOrderNo[i].disabled= true;
					document.pIMSForm.payScale[i].disabled=true;
				}
			}
			else
			{
				document.getElementById("commentDate").disabled= true;
				document.getElementById("comments").disabled= true;
				document.getElementById("reason").disabled= true;
				document.getElementById("serviceOrderNo").disabled= true;
				document.getElementById("payScale").disabled= true;
				
			}
	
			//readonly for probation
			var ProbationTable= document.getElementById("PronameTable");
			var rows= ProbationTable.rows.length;
			
			if(rows>3)
			{
				for(var i=0 ;i<rows-2;i++)
				{
					document.pIMSForm.proPostId[i].disabled= true;
					document.pIMSForm.proDec[i].disabled= true;
					document.pIMSForm.proOrderNo[i].disabled= true;
					document.pIMSForm.proOrderDate[i].disabled= true;
				}
			}
			else
			{
				document.getElementById("proPostId").disabled= true;
				document.getElementById("proDec").disabled= true;
				document.getElementById("proOrderNo").disabled= true;
				document.getElementById("proOrderDate").disabled= true;
			}
	
	
			//readonly for Regularisation
			var regTable= document.getElementById("RegnameTable");
			var rows= regTable.rows.length;
			if(rows>3)
			{
				for(var i=0 ;i<rows-2;i++)
				{
					document.pIMSForm.regPostId[i].disabled= true;
					document.pIMSForm.regDate[i].disabled= true;
					document.pIMSForm.regOrder[i].disabled= true;
					document.pIMSForm.regOrderDate[i].disabled= true;
				}
			}
			else
			{
				document.getElementById("regPostId").disabled= true;
				document.getElementById("regDate").disabled= true;
				document.getElementById("regOrder").disabled= true;
				document.getElementById("regOrderDate").disabled= true;
			}
	
	
			//readonly for Educational Qualification
			var eduTable= document.getElementById("EqnameTable");
			var rows= eduTable.rows.length;
			if(rows>3)
			{
				for(var i=0 ;i<rows-2;i++)
				{
					document.pIMSForm.qulification[i].disabled= true;
					document.pIMSForm.majorSubject[i].disabled= true;
					document.pIMSForm.monthandYearOfPass[i].disabled= true;
					document.pIMSForm.universityBoard[i].disabled= true;
					document.pIMSForm.eduDocNo[i].disabled= true; 
				}
			}
			else
			{
				document.getElementById("qulification").disabled= true;
				document.getElementById("majorSubject").disabled= true;
				document.getElementById("monthandYearOfPass").disabled= true;
				document.getElementById("universityBoard").disabled= true;
				document.getElementById("eduDocNo").disabled= true;
			}
	
			//readonly for Tecnical Qualification
			var techTable= document.getElementById("TeckDetails");
			var rows= techTable.rows.length; 
			if(rows>3)
			{
				for(var i=0 ;i<rows-2;i++)
				{
						document.pIMSForm.skillId[i].disabled= true;
						document.pIMSForm.gradeId1[i].disabled= true;
						document.pIMSForm.yearOfPassTQ[i].disabled= true;
						document.pIMSForm.remarks[i].disabled= true;
						document.pIMSForm.techDocNo[i].disabled= true;
				}
			}
			else
			{
				document.getElementById("skillId").disabled= true;
				document.getElementById("gradeId1").disabled= true;
				document.getElementById("yearOfPassTQ").disabled= true;
				document.getElementById("remarks").disabled= true;
				document.getElementById("techDocNo").disabled= true;
			}
			
			//readonly for Deparmental Test
			var deptTable= document.getElementById("DepnameTable");
			var rows= deptTable.rows.length;
			if(rows>3)
			{
				for(var i=0 ;i<rows-2;i++)
				{
					document.pIMSForm.nameOfTheTestId[i].disabled= true;
					document.pIMSForm.monthandYearOfPassDT[i].disabled= true;
				}
			}
			else
			{
				document.getElementById("nameOfTheTestId").disabled= true;
				document.getElementById("monthandYearOfPassDT").disabled= true;
			}
	
	
			//readonly for Immovable Property Details
			var immovableTable= document.getElementById("PDImmnameTable");
			var rows= immovableTable.rows.length;
			if(rows>2)
			{
				for(var i=0 ;i<rows-1;i++)
				{
					document.pIMSForm.propertydiscriptionImm[i].disabled= true;
					document.pIMSForm.howAcquiredImm[i].disabled= true;
					document.pIMSForm.placeImm[i].disabled= true;
					document.pIMSForm.presentValueImm[i].disabled= true;
					document.pIMSForm.permissionObtainedImm[i].disabled= true;
					document.pIMSForm.orderNoImm[i].disabled= true;
					document.pIMSForm.dateImm[i].disabled= true;
				}
			}
			else
			{
				document.getElementById("propertydiscriptionImm").disabled= true;
				document.getElementById("howAcquiredImm").disabled= true;
				if(document.getElementById("placeImm")!=null)
				{
					document.getElementById("placeImm").disabled= true;
				}
				document.getElementById("presentValueImm").disabled= true;
				document.getElementById("permissionObtainedImm").disabled= true;
				document.getElementById("orderNoImm").disabled= true;
				document.getElementById("dateImm").disabled= true;
			}
	
	
			//readonly for Movable Property Details
			var movableTable= document.getElementById("PDMovnameTable");
			var rows= movableTable.rows.length;
			
			if(rows>2)
			{
				for(var i=0 ;i<rows-1;i++)
				{
					document.pIMSForm.propertydiscriptionMo[i].disabled= true;
					document.pIMSForm.valueOfTimeOfPurchaseMo[i].disabled= true;
					document.pIMSForm.howAcquiredMov[i].disabled= true;
					document.pIMSForm.permissionObtainedMo[i].disabled= true;
					document.pIMSForm.dateMo[i].disabled= true;
					document.pIMSForm.orderNoMo[i].disabled= true;
				}
			}
			else
			{
				if(document.getElementById("propertydiscriptionMo")!=null)
				{
					document.getElementById("propertydiscriptionMo").disabled= true;
				}
				if(document.getElementById("valueOfTimeOfPurchaseMo")!=null)
				{
					document.getElementById("valueOfTimeOfPurchaseMo").disabled= true;
				}
				if(document.getElementById("howAcquiredMov")!=null)
				{
					document.getElementById("howAcquiredMov").disabled= true;
				}
				if(document.getElementById("permissionObtainedMo")!=null)
				{
					document.getElementById("permissionObtainedMo").disabled= true;
				}
				if(document.getElementById("dateMo")!=null)
				{
					document.getElementById("dateMo").disabled= true;
				}
				if(document.getElementById("orderNoMo")!=null)
				{
					document.getElementById("orderNoMo").disabled= true;
				}
			}
		<%}%>



		}
		function show(arg)
		{
		
			document.getElementById("PersonalInfo").style.display="";
			if(document.getElementById("pisTable").style.display!="none")
			{
				<%if (!modeonloadView.equalsIgnoreCase("view")) {%> 
					if(document.pIMSForm.statusTypeId.value == '0')
					{
						alert("Please choose the status.");
						document.pIMSForm.statusTypeId.focus();
						return false;
					}
					<%if (EisManagersUtill.getEisCommonsService()
						.isEmployeeAutoGenerateCodeYesOrNo() == false) {
					if (modeonloadView.equals("create")) {%>
						if(document.pIMSForm.employeeCode.value == "")
						{
							alert('<bean:message key="alertEnterEmpCode"/>');
							document.pIMSForm.employeeCode.focus();
							return false;
						}
		
					<%}
				}%>
					var isDeceased = document.getElementById("statusTypeId").options[document.getElementById("statusTypeId").selectedIndex].text
					 if(isDeceased.toUpperCase()=='DECEASED')
					{
						 if(document.getElementById("deathDate").value=="")
						{
							alert('Please enter the Death Date');
							document.getElementById("deathDate").focus();
							return false;
						}
					}
					
					if(isDeceased.toUpperCase()=='RETIRED')
					{
						 if(document.getElementById("dateOfRetirement").value=="")
						{
							alert('Please enter the Retired Date');
							document.getElementById("dateOfRetirement").focus();
							return false;
						}
					}
					
			
					if(document.pIMSForm.statusMaster.value=='0')
					{
						alert('<bean:message key="alertEnterEmpType"/>');
						document.pIMSForm.statusMaster.focus();
						return false;
					}
					if(document.pIMSForm.firstName.value == "" || document.pIMSForm.firstName.value=="First Name")
					{
						alert('<bean:message key="alertEnterFirstName"/>');
						document.pIMSForm.firstName.focus();
						return false;
					}
					if(document.pIMSForm.employeeDob.value == "" )
					{
						alert('<bean:message key="alertEnterDob"/>');
						document.pIMSForm.employeeDob.focus();
						return false;
					}
					if(document.pIMSForm.empGrpMstr.value == '0')
					{
						alert("Please choose the Employee Group ");
						document.pIMSForm.empGrpMstr.focus();
						return false;
					}
					
					
					if(document.pIMSForm.propertyNoper.value == "")
					{
						alert('<bean:message key="alertPermAddr"/>');
						document.pIMSForm.propertyNoper.focus();
						return false;
					}
					
					if(document.pIMSForm.dateOfFA.value == "" )
					{
							alert('<bean:message key="alertEnterDOAppointment"/>');
							document.pIMSForm.dateOfFA.focus();
							return false;
					}
			<%}%>
			}
	
			
	
			
			switch (arg)
			{
	
			case "PersonalInfo":
	
			
	
			
	
			//SERVICE CHANGE
			
	
			document.getElementById("Assignment").style.display="none";
			
			document.getElementById("Technical").style.display="none";
			
			document.getElementById("ServiceDetailsHistory").style.display="none";
			
			document.getElementById("PersonalInfoDetails").style.display="";
			
			
			setCSSClasses('empPage','First Active');
			setCSSClasses('assignmentPage','');
			 
			 setCSSClasses('servicePage','Befor');
			 setCSSClasses('otherPage','Last');
	
			break;
			
			case "Assignment":
	
			
			document.getElementById("Assignment").style.display="";
			document.getElementById("PersonalInfoDetails").style.display="none";
			document.getElementById("Technical").style.display="none";
			
			document.getElementById("ServiceDetailsHistory").style.display="none";
			
			document.getElementById("Nomimation").style.display="none";
			document.getElementById("Nomimation").style.visibility="hidden";
			setCSSClasses("assignmentPage","First Active");
			 setCSSClasses("empPage","Last");
			 setCSSClasses("servicePage","Befor");
			 setCSSClasses("otherPage","Last");
			 
			break;
			case "Technical":
			
			
			//on click of Technical tab block the corresponding jsp
			document.getElementById("PersonalInfoDetails").style.display="none";
			document.getElementById("Assignment").style.display="none";
			document.getElementById("Nomimation").style.display="none";
			document.getElementById("ServiceDetailsHistory").style.display="none";
			document.getElementById("Technical").style.display="";
			 setCSSClasses("otherPage","First Active ActiveLast");
			 setCSSClasses("empPage","Befor");
			 setCSSClasses("assignmentPage","Befor");
			 setCSSClasses("servicePage","Last");
	
	
			break;
	
			case "Nomimation":
			
	
			//on click of Nomimation tab block the corresponding jsp
			document.getElementById("Nomimation").style.display="";
			document.getElementById("PersonalInfoDetails").style.display="none";
			document.getElementById("Assignment").style.display="none";
			document.getElementById("Technical").style.display="none";
			document.getElementById("ServiceDetailsHistory").style.display="none";
			
			break;
	
			case "ServiceDetailsHistory":
			
			
			
			document.getElementById("ServiceDetailsHistory").style.display="";
	
			// document.getElementById('ServiceDetails').setAttribute('class','Active');
			//document.getElementById('ServiceDetails').setAttribute('className','Active');  
			document.getElementById("PersonalInfoDetails").style.display="none";
			document.getElementById("Assignment").style.display="none";
			document.getElementById("Technical").style.display="none";
			document.getElementById("Nomimation").style.display="none";
	
			setCSSClasses('servicePage','First Active');
			setCSSClasses('empPage','Befor');
			setCSSClasses('assignmentPage','Last');
			setCSSClasses('otherPage','Last');
	
			break;
	
	
			default :
			enable();
	
			//SERVICE CHANGE
			/*document.getElementById("PronameTable").style.display="none";
			document.getElementById("PronameTableBtn").style.display="none";
			document.getElementById("RegnameTable").style.display="none";
			document.getElementById("RegTableBtn").style.display="none";
	
			document.getElementById("edu").style.display="none";
			document.getElementById("EqnameTable").style.display="none";
			document.getElementById("EduTableBtn").style.display="none";
			document.getElementById("TeckDetails").style.display="none";
			document.getElementById("TeckDetailsBtn").style.display="none";
			document.getElementById("DepnameTable").style.display="none";
			document.getElementById("DeptTestBtn").style.display="none";
			document.getElementById("nomPir").style.display="none";
			document.getElementById("NDnameTable").style.display="none";
			document.getElementById("NPBtn").style.display="none";
			document.getElementById("immpropDet").style.display="none";
			document.getElementById("PDImmnameTable").style.display="none";
			document.getElementById("ImmProBtn").style.display="none";
			document.getElementById("movableProperty").style.display="none";
			document.getElementById("PDMovnameTable").style.display="none";
			document.getElementById("MovProBtn").style.display="none";
			document.getElementById("EOTableBtn").style.display="none";
			alert('In default');*/
					document.getElementById("PersonalInfoDetails").style.display="";
	
			setCSSClasses('empPage','First Active');
	
			setCSSClasses('servicePage','');
			setCSSClasses('assignmentPage','');
			setCSSClasses('otherPage','Last');
	
	
		
			}
			
		}


		
		function setCSSClasses(id,classes)
			{
				
				document.getElementById(id).setAttribute('class',classes);
				document.getElementById(id).setAttribute('className',classes);

			}
		function checkTodatePresnt(obj)
		{
		var row = getRow(obj);
		var tbl= document.getElementById("EOTable");

		var len = tbl.rows.length;
		if(len>2){
		for(var i=0;i<len-1;i++)
		{
		var todts=document.pIMSForm.toDate[i].value;
		if(todts == null || todts =="")
		{
		alert('<bean:message key="alertFillTodateBfDesg"/>');
		document.pIMSForm.toDate[i].focus();
		document.pIMSForm.designationId[i].value="";

		}


		}
		}else{

		var todte=document.pIMSForm.toDate.value;
		if(todte == null || todte =="")
		{
		alert('<bean:message key="alertFillTodateBfDesg"/>');
		document.pIMSForm.toDate.focus();
		document.pIMSForm.designationId.value="";

		}

		}
		}
		function toRetirementDate(obj)
		{
		//include with payroll

		var row= getRow(obj);
		var toDate = getControlInBranch(row,"toDate");

		var todate = toDate.value;
		/*
		<egov:ifModuleEnabled moduleName="<%=EisConstants.MODULE_PAYROLL%>" >
		var retire = document.pIMSForm.dateOfRetirement.value;
		if(obj.value != "")
		{
		if(document.pIMSForm.dateOfRetirement.value == null || document.pIMSForm.dateOfRetirement.value == "")
		{
		alert("Please fill the Retirement Date");
		document.pIMSForm.dateOfRetirement.focus();
		document.pIMSForm.toDate.value="";
		return false;
		}

		else
		if(compareDate(document.pIMSForm.dateOfRetirement.value,toDate.value) == 1)
		{
		alert("Todate  must be less than Retirement Date");
		obj.focus();
		obj.value="";
		return false;
		}
		}
		</egov:ifModuleEnabled>*/

		}


		
		
		function checkPositionForAnEmp(obj)

		{

		
		<%String empid = null;
			if (request.getParameter("Id") != null) {
				empid = request.getParameter("Id").trim();

			}%>
		var tbl= document.getElementById("EOTable");
		var row=getRow(obj);
		var url;
		var pos;
		var posto;
		var dteto;
		var datefrom
		var posId;
		

		var http ;
		if(document.getElementById('positionId')!='undefined')
		{
				
			
				
			var date = document.getElementById('fromDateEnter');
			datefrom = date.value;
			
			var dateto = document.getElementById('toDateEnter');
			dteto = dateto.value;
			var postn = document.getElementById('positionId');
			posto =postn.value;
			pos=obj.value;
			
			var isPrimary = "";
		
			if(document.pIMSForm.checkPrimary[0].checked){
				isPrimary = 'Y'; 
			}
			if(document.pIMSForm.checkPrimary[1].checked){
				isPrimary = 'N'; 
			}
	
	
		

		


		if(toDateCheck !=obj.value)
		{
		url = "<%=request.getContextPath()%>/pims/checkPositionForAnDesig.jsp?pos="+posto+"&dateFrom="+datefrom+"&dateTo="+dteto+"&empId="+<%=empid%>+"&isPrimary="+isPrimary;
		}


		
		

		}

		if(url != undefined){



		http = initiateRequest();
		http.open("GET", url, true);


		http.onreadystatechange = function()
		{

		if (http.readyState == 4)
		{
		if (http.status == 200)
		{

		var statusString =http.responseText.split("^");
		
		if(statusString[0]=="true")
		{

		var popup = statusString[0];
		if(popup!="")
				{
						alert('<bean:message key="alertPosExist"/>');
						obj.value = "";
						return false;
				}
		}
		}
		}
		};
		http.send(null);
		}

		}

		
		function checkForDublicateAssignment(){

			<%String empId = request.getParameter("Id");%>
			var fromDate = document.getElementById('fromDateEnter').value;
			var dateto = document.getElementById('toDateEnter').value;
			var desigId = document.getElementById('desgEnterId').value;
			var deptId = document.getElementById('deptEntry').value;
			var functionaryId = document.getElementById('functionaryEntry').value;
			var empId = <%=empId%>
			var action = "checkDublicateAssignment";			
			var url = "<%=request.getContextPath()%>"+"/pims/checkDublicateAssignmentAjax.jsp?deptId="+deptId+"&desigId="+desigId+
						"&functionaryId="+functionaryId+"&fromDate="+fromDate+"&empId="+empId;
			var isDublicate="false";
			var req = initiateRequest();
	      	req.onreadystatechange = function(){
		      if (req.readyState == 4){
		            if (req.status == 200){
	                   	var payheads = req.responseText
	                   	var a = payheads.split("^");
	                   	var codes = a[0];
	                   	if(codes=="true"){
	                   		alert("This temp assignment already assigned to other employee");
	                   		isDublicate = "true";
	                   	}
	                   	else if(codes=="false"){
	         				isDublicate = "false";
	                   	}
		       		}
		       	}
	        };
			req.open("GET", url, false);
			req.send(null);		
			return isDublicate;
		}
		
		
		function addAssignmentRow(obj,tableName,row)
		{
		   	
			    var checkdesgVal=checkDesg();
			    var checkPos=checkPosAutoComp();
			    var index=document.getElementById('rowIndexOfAssignment').value;
				if(checkMandatoryAssignment() && checkdesgVal && checkPos)
				{
					
					
						  globalSaveRowNumber="";
						if(document.getElementById('assignEntered').value!="")
						{
							var tbl=tableName;
							var rowO=tbl.rows.length;
							var name=obj.name;
							
							if(rowO ==2)
							{
								document.pIMSForm.fromDate.value=document.getElementById('fromDateEnter').value;
								document.pIMSForm.functionId.value=document.getElementById('functionEntry').value;
								document.pIMSForm.toDate.value=document.getElementById('toDateEnter').value;
							
								document.pIMSForm.gradeId.value=document.getElementById('gradeEntry').value;
		
								document.pIMSForm.designationId.value=document.getElementById('desginationName').value;
								document.pIMSForm.posName.value=document.getElementById('positionEntryName').value;
								document.pIMSForm.functionaryId.value=document.getElementById('functionaryEntry').value
								document.pIMSForm.mainDepartmentId.value=document.getElementById('deptEntry').value;
		
								document.pIMSForm.assignmentOrderNo.value=document.getElementById('govtEntry').value;
								document.pIMSForm.fundId.value=document.getElementById('fundEntry').value;
								document.pIMSForm.desgId.value=document.getElementById('desgEnterId').value;
								document.pIMSForm.posId.value=document.getElementById('positionId').value;
								
								showHodValsOnDemand((rowO-2));							
								
								if(document.pIMSForm.checkPrimary[0].checked){
									document.pIMSForm.isPrimary.value='Yes';
								}
								else{
									document.pIMSForm.isPrimary.value='No';	
								}
								resetFieldValue();
							}
							else
							{
								document.pIMSForm.fromDate[index].value=document.getElementById('fromDateEnter').value;
								document.pIMSForm.functionId[index].value=document.getElementById('functionEntry').value;
								document.pIMSForm.toDate[index].value=document.getElementById('toDateEnter').value;
							
								document.pIMSForm.gradeId[index].value=document.getElementById('gradeEntry').value;
		
								 document.pIMSForm.designationId[index].value=document.getElementById('desginationName').value;
								document.pIMSForm.posName[index].value=document.getElementById('positionEntryName').value;
								document.pIMSForm.functionaryId[index].value=document.getElementById('functionaryEntry').value
								document.pIMSForm.mainDepartmentId[index].value=document.getElementById('deptEntry').value;
		
								document.pIMSForm.assignmentOrderNo[index].value=document.getElementById('govtEntry').value;
								document.pIMSForm.fundId[index].value=document.getElementById('fundEntry').value;
								document.pIMSForm.desgId[index].value=document.getElementById('desgEnterId').value;
								document.pIMSForm.posId[index].value=document.getElementById('positionId').value;
								showHodValsOnDemand(index);
								if(document.pIMSForm.checkPrimary[0].checked){
									document.pIMSForm.isPrimary[index].value='Yes';
								}
								else{
									document.pIMSForm.isPrimary[index].value='No';	
								}
								resetFieldValue();
							}
						}
						else
						{
							var tbl=tableName;
							var rowO=tbl.rows.length;
							var name=obj.name;
							if(rowO ==2)
								{
								if(document.pIMSForm.fromDate.value=="")
									{
										document.getElementById('fromDate').value =document.getElementById('fromDateEnter').value;
										document.getElementById('toDate').value =document.getElementById('toDateEnter').value;
										document.getElementById('functionId').value =document.getElementById('functionEntry').value;
										document.getElementById('gradeId').value =document.getElementById('gradeEntry').value;
										document.getElementById('designationId').value =document.getElementById('desginationName').value;
										document.getElementById('posName').value =document.getElementById('positionEntryName').value;
										document.getElementById('functionaryId').value =document.getElementById('functionaryEntry').value;
										document.getElementById('mainDepartmentId').value =document.getElementById('deptEntry').value;
										document.getElementById('assignmentOrderNo').value =document.getElementById('govtEntry').value;
										document.getElementById('fundId').value =document.getElementById('fundEntry').value;
										
										//set all the hidden fields
										document.getElementById('desgId').value=document.getElementById('desgEnterId').value;
										document.getElementById('assignmentId').value=0;
										document.getElementById('posId').value=document.getElementById('positionId').value;
										showHodValsOnDemand((rowO-2));
										if(document.pIMSForm.checkPrimary[0].checked){
											document.pIMSForm.isPrimary.value='Yes';
										}
										else{
											document.pIMSForm.isPrimary.value='No';	
										}
										resetFieldValue();
								}
								else 
								{
									if(row != null)
									{
										var tbody=tbl.tBodies[0];
										var lastRow = tbl.rows.length;
										var rowObj = row.cloneNode(true);
										rIndex = 0;
										if(name=="addAssButn")
										{
											getControlInBranch(rowObj,'fromDate').value = document.getElementById('fromDateEnter').value; 		
											getControlInBranch(rowObj,'toDate').value = document.getElementById('toDateEnter').value;		
											getControlInBranch(rowObj,'functionId').value = document.getElementById('functionEntry').value;
											getControlInBranch(rowObj,'gradeId').value = document.getElementById('gradeEntry').value;
											getControlInBranch(rowObj,'designationId').value = document.getElementById('desginationName').value;
											getControlInBranch(rowObj,'posName').value = document.getElementById('positionEntryName').value;
											getControlInBranch(rowObj,'functionaryId').value = document.getElementById('functionaryEntry').value;
											getControlInBranch(rowObj,'mainDepartmentId').value = document.getElementById('deptEntry').value;
											getControlInBranch(rowObj,'assignmentOrderNo').value = document.getElementById('govtEntry').value;
											getControlInBranch(rowObj,'fundId').value = document.getElementById('fundEntry').value;
											
											//set all the hidden fields
											getControlInBranch(rowObj,'desgId').value=document.getElementById('desgEnterId').value;
											getControlInBranch(rowObj,'assignmentId').value=0;
											getControlInBranch(rowObj,'posId').value=document.getElementById('positionId').value;
											if(document.pIMSForm.checkPrimary[0].checked){
												getControlInBranch(rowObj,"isPrimary").value='Yes';
											}
											else{
												getControlInBranch(rowObj,"isPrimary").value='No';	
											}
		
		
											tbody.appendChild(rowObj);
											showHodValsOnDemand(tbody.rows.length-2);
											resetFieldValue();
											
										}
									}
								}
							}
							else
							{
								if(row != null)
								{
									var tbody=tbl.tBodies[0];
									var lastRow = tbl.rows.length;
									var rowObj = row.cloneNode(true);
									rIndex = 0;
									if(name=="addAssButn")
									{
										
										
										getControlInBranch(rowObj,'fromDate').value =document.getElementById('fromDateEnter').value;
										getControlInBranch(rowObj,'toDate').value =document.getElementById('toDateEnter').value;
										
										getControlInBranch(rowObj,'functionId').value =document.getElementById('functionEntry').value;
										getControlInBranch(rowObj,'gradeId').value =document.getElementById('gradeEntry').value;
										getControlInBranch(rowObj,'designationId').value =document.getElementById('desginationName').value;
										getControlInBranch(rowObj,'posName').value =document.getElementById('positionEntryName').value;
										getControlInBranch(rowObj,'functionaryId').value =document.getElementById('functionaryEntry').value;
										getControlInBranch(rowObj,'mainDepartmentId').value =document.getElementById('deptEntry').value;
										getControlInBranch(rowObj,'assignmentOrderNo').value =document.getElementById('govtEntry').value;
										getControlInBranch(rowObj,'fundId').value =document.getElementById('fundEntry').value;
										
										//set all the hidden fields
										getControlInBranch(rowObj,'desgId').value=document.getElementById('desgEnterId').value;
										getControlInBranch(rowObj,'assignmentId').value=0;
										getControlInBranch(rowObj,'posId').value=document.getElementById('positionId').value;
										if(document.pIMSForm.checkPrimary[0].checked){
											getControlInBranch(rowObj,"isPrimary").value='Yes';
										}
										else{
											getControlInBranch(rowObj,"isPrimary").value='No';
										}
		
	
		
										tbody.appendChild(rowObj);
										showHodValsOnDemand(tbody.rows.length-2);
										resetFieldValue();
										
									}
								}
							}
					     }
						
					document.getElementsByName('checkhod')[1].checked=true;
				document.getElementById("deptHodOnTopLbl").style.display="none";
				document.getElementById("deptHodOnTopTd").style.display="none";
				}
		}

		function resetFieldValue()
		{
			document.getElementById('fromDateEnter').value="";
			document.getElementById('toDateEnter').value="";
			document.getElementById('functionEntry').value="0";
			document.getElementById('gradeEntry').value="0";
			document.getElementById('desginationName').value="";

			document.getElementById('positionEntryName').value="";
			document.getElementById('functionaryEntry').value="0";
			document.getElementById('deptEntry').value="0";
			document.getElementById('govtEntry').value="";
			document.getElementById('fundEntry').value="0";

			document.getElementById('desgEnterId').value="";
			document.getElementById('positionId').value="";
			document.getElementById('assignEntered').value="";
			document.getElementById('rowIndexOfAssignment').value="";
			
			document.getElementsByName('checkhod')[1].checked=true;
		document.getElementById("deptHodOnTopLbl").style.display="none";
		document.getElementById("deptHodOnTopTd").style.display="none";
			//document.getElementById('checkPrimary').value="";
			

		}

		function addRow(obj,tableName,rowname)
		{

		
		var tbl=document.getElementById(tableName);
		var row = document.getElementById(rowname);
		var rowO=tbl.rows.length;
		var name=obj.name;
		var tname = "resetRowValues"+name;
		if(rowO<11)
		{
		if(row != null)
		{
		var tbody=tbl.tBodies[0];
		var lastRow = tbl.rows.length;
		var rowObj = row.cloneNode(true);
		rIndex = 0;

		if(name=="addeoBtn")
		{

		rowObj.document.getElementById('assignmentId').value = 0;

		rowObj.document.getElementById('designationId').value ="";
		rowObj.document.getElementById('posName').value ="";

		rowObj.document.getElementById('assignmentOrderNo').value = "";
		}
		
		if(name=="AddRowPay")
		{


		rowObj.document.getElementById('payScaleId').value = 0;

		}
		else if(name=="AddRowPro")
		{

		rowObj.document.getElementById('idProbation').value = 0;
		}
		else if(name=="AddRowReg")
		{
		rowObj.document.getElementById('regularisationId').value = 0;
		}
		else if(name=="AddrowEduDet")
		{
		rowObj.document.getElementById('educationDetailsId').value = 0;
		}
		else if(name=="AddrowTeckDet")
		{
		rowObj.document.getElementById('tecnicalQualificationId').value = 0;
		}
		else if(name=="AddrowDeptTst")
		{
		rowObj.document.getElementById('deptTestsId').value = 0;
		}
		else if(name=="AddRowNom")
		{
		rowObj.document.getElementById('nomDetailsId').value = 0;
		}
		else if(name=="AddRowImm")
		{
		rowObj.document.getElementById('immPropertyDetailsId').value = 0;
		}
		else if(name=="AddRowMov")
		{
		rowObj.document.getElementById('movPropertyDetailsId').value = 0;
		}
	

		rIndex = rowObj.rowIndex;
		//alert('rIndex'+lastRow);
		var counter = eval(lastRow-1);
		//alert('rIndex'+counter);
		if(getControlInBranch(rIndex,"departmentId")!=null)
		{
		getControlInBranch(rIndex,"departmentId").name = 'departmentId'+counter;
		
		getControlInBranch(rIndex,"hod").name = 'hod'+counter;
		}
		tbody.appendChild(rowObj);
		//alert('lastRow'+lastRow);
	
		resetRowValues(tbl,lastRow,name);
		}

		}
		}



		var asslength=0;

		
		var PronameTablelength=0;
		var PayHeaderTablelength=0;
		var RegnameTablelength=0;
		var EqnameTablelength=0;
		var TeckTablelength=0;
		var DepnameTablelength=0;
		var NDnameTablelength=0;
		var PDImmname=0;
		var PDMovnameTablelength=0;
		
		var rIndex = 0;
		function setLength()
		{

		pan = document.getElementById("panNumber").value;
		userUnique=document.getElementById("userFirstName").value;
		userCode=document.getElementById("employeeCode").value;
		if(document.getElementById("toDate")!=null)
			toDateCheck = document.getElementById("toDate").value;
		var assTable =document.getElementById('EOTable');
		
		var PronameTable =document.getElementById('PronameTable');
		var RegnameTable =document.getElementById('RegnameTable');
		var EqnameTable =document.getElementById('EqnameTable');
		var TeckDetails =document.getElementById('TeckDetails');
		var DepnameTable =document.getElementById('DepnameTable');
		var NDnameTable =document.getElementById('NDnameTable');
		var PDImmnameTable =document.getElementById('PDImmnameTable');
		var PDMovnameTable =document.getElementById('PDMovnameTable');
		//var PayHeaderTable =document.getElementById('PayHeaderTable');


		PronameTablelength=PronameTable.rows.length;
		//PayHeaderTablelength = PayHeaderTable.rows.length;
		RegnameTablelength=RegnameTable.rows.length;
		EqnameTablelength=EqnameTable.rows.length;
		TeckTablelength=TeckDetails.rows.length;
		DepnameTablelength=DepnameTable.rows.length;
		NDnameTablelength=NDnameTable.rows.length;
		PDImmnameTablelength=PDImmnameTable.rows.length;
		PDMovnameTablelength=PDMovnameTable.rows.length;
	

		asslength = assTable.rows.length;
		}

		

		function deleteAssRow(obj,tableName,addButton)
		{
			var tbl=document.getElementById(tableName);
			var rowno=getRow(obj).rowIndex;
			var rIndex=tbl.rows.length-1;
			var firstLength = 0;
			var name=obj.name;
			var rowNumber=getRow(obj).rowIndex;
			if(name=="deltp")
			{
				firstLength = asslength;
			}
			
			if(rIndex<2)
			{
			alert("This  can not be deleted");
			return false;
			}
			else
			{
			tbl.deleteRow(rowno);
			return true;
			}
		}

		function deleteRow(obj,tableName,addButtion)
		{
		var tbl=tableName;
		var rowo=tbl.rows.length;
		var firstLength = 0;
		var name=obj.name;


		if(name=="deltp")
		{
		firstLength = asslength;
		}

		
		
		if(name=="DeleteRowPro")
		{

		firstLength = PronameTablelength;
		}
		else if(name=="DeleteRowReg")
		{
		firstLength = RegnameTablelength;
		}
		else if(name=="DeleteRowEdu")
		{
		firstLength = EqnameTablelength;
		}
		else if(name=="DeleteRowTeck")
		{
		firstLength = TeckTablelength;

		}
		else if(name=="DeleteRowDept")
		{
		firstLength = DepnameTablelength;
		}
		else if(name=="DeleteRowNom")
		{
		firstLength = NDnameTablelength;
		}
		else if(name=="DeleteRowImm")
		{
		firstLength = PDImmnameTablelength;
		}
		else if(name=="DeleteRowMov")
		{
		firstLength = PDMovnameTablelength;
		}
		

		if(rowo<=firstLength)
		{
		alert("This  can not be deleted");
		return false;
		}
		else
		{
		tbl.deleteRow(rIndex);
		return true;
		}
		}
		
		

		function resetRowValues(lastRow,name)
		{

		
		
		if(name=="AddRowPay")
		{
		document.pIMSForm.payScaleHeader[lastRow].value="0";
		document.pIMSForm.effDate[lastRow].value="";

		document.pIMSForm.currBasicPay[lastRow].value="";

		}
		if(name=="AddRowPro")
		{
		document.pIMSForm.proPostId[lastRow-1].value="0";
		document.pIMSForm.proDec[lastRow-1].value="";
		document.pIMSForm.proOrderNo[lastRow-1].value="";
		document.pIMSForm.proOrderDate[lastRow-1].value="";
		}
		else if(name=="AddRowReg")
		{
		document.pIMSForm.regPostId[lastRow-1].value="0";
		document.pIMSForm.regDate[lastRow-1].value="";
		document.pIMSForm.regOrder[lastRow-1].value="";
		document.pIMSForm.regOrderDate[lastRow-1].value="";
		}
		else if(name=="AddrowEduDet")
		{
		getControlInBranch(lastRow,'quilification').value="";
		getControlInBranch(lastRow,'majorSubject').value="";
		getControlInBranch(lastRow,'monthandYearOfPass').value="";
		getControlInBranch(lastRow,'universityBoard').value="";
		
		/*document.pIMSForm.majorSubject[lastRow].value="";
		document.pIMSForm.monthandYearOfPass[lastRow].value="";
		document.pIMSForm.universityBoard[lastRow].value="";*/
		}
		else if(name=="AddrowTeckDet")
		{
		document.pIMSForm.skillId[lastRow-1].value="0";
		document.pIMSForm.gradeId1[lastRow-1].value="0";
		document.pIMSForm.yearOfPassTQ[lastRow-1].value="";
		}
		else if(name=="AddrowDeptTst")
		{
		document.pIMSForm.nameOfTheTestId[lastRow-1].value="0";
		document.pIMSForm.monthandYearOfPassDT[lastRow-1].value="";
		}
		else if(name=="AddRowNom")
		{
		document.pIMSForm.nameOfTheNominee[lastRow].value="";
		document.pIMSForm.age[lastRow].value="";
		document.pIMSForm.maritialStatus[lastRow].value="";
		document.pIMSForm.relationId[lastRow].value="";
		document.pIMSForm.gpfnd[lastRow].value="";
		document.pIMSForm.spfgs[lastRow].value="";
		document.pIMSForm.fbf[lastRow].value="";
		document.pIMSForm.dcrg[lastRow].value="";
		document.pIMSForm.pension[lastRow].value="";
		}
		else if(name=="AddRowImm")
		{
		document.pIMSForm.propertydiscriptionImm[lastRow].value="";
		document.pIMSForm.placeImm[lastRow].value="";
		document.pIMSForm.howAcquiredImm[lastRow].value="0";
		document.pIMSForm.presentValueImm[lastRow].value="";
		document.pIMSForm.permissionObtainedImm[lastRow].value="N";
		document.pIMSForm.orderNoImm[lastRow].value="";
		document.pIMSForm.dateImm[lastRow].value="";
		}
		else if(name=="AddRowMov")
		{
		document.pIMSForm.propertydiscriptionMo[lastRow].value="";
		document.pIMSForm.valueOfTimeOfPurchaseMo[lastRow].value="";
		document.pIMSForm.howAcquiredMov[lastRow].value="0";
		document.pIMSForm.permissionObtainedMo[lastRow].value="N";
		document.pIMSForm.orderNoMo[lastRow].value="";
		document.pIMSForm.dateMo[lastRow].value="";
		}
	
		if(name=="addAssButn")
		{

		if(document.pIMSForm.toDate[lastRow-1].value!="")
		{

		var dte = document.pIMSForm.toDate[lastRow-1].value;
		populateFromDate1(dte,lastRow);

		//document.pIMSForm.fromDate[lastRow].value = ret;
		document.pIMSForm.toDate[lastRow].value="";
		document.pIMSForm.fundId[lastRow].value="0";
		document.pIMSForm.functionId[lastRow].value="0";
		document.pIMSForm.designationId[lastRow].value="0";
		document.pIMSForm.designationId[lastRow].id="designationId"+lastRow;
		//alert("designationId[lastRow]="+document.pIMSForm.designationId[lastRow].id);
		document.pIMSForm.posId[lastRow].value="0";
		document.pIMSForm.posId[lastRow].id="posId"+lastRow;
		//alert("posId[lastRow]="+document.pIMSForm.posId[lastRow].id);
		document.pIMSForm.fromDate[lastRow].id="fromDate"+lastRow;

		document.pIMSForm.toDate[lastRow].id="toDate"+lastRow;
		document.pIMSForm.functionaryId[lastRow].value="0";
		document.pIMSForm.assignmentId[lastRow].value="0";

		}
		else if(document.pIMSForm.toDate[lastRow-1].value=="" && document.pIMSForm.fromDate[lastRow-1].value !=""||document.pIMSForm.toDate[lastRow-1].value=="")
		{
		alert('<bean:message key="alertFillToDateBfAdd"/>');
		var tbl= document.getElementById("EOTable");
		tbl.deleteRow(lastRow+1);
		return  false;
		}


		}

		}


		function populateFromDate1(str,lastRow)
		{


		var fad = str;
		var http = initiateRequest();
		var url = "<%=request.getContextPath()%>/pims/populateFromDate.jsp?fromDate="+fad;
		http.open("GET", url, true);
		http.onreadystatechange = function()
		{
		if (http.readyState == 4)
		{
		if (http.status == 200)
		{

		var statusString =http.responseText.split("^");
		var fDate = statusString[0];


		document.pIMSForm.fromDate[lastRow].value = fDate;
		document.pIMSForm.toDate[lastRow].value="";
		document.pIMSForm.fundId[lastRow].value="0";
		document.pIMSForm.functionId[lastRow].value="0";
		//change after AutoComplete
		document.pIMSForm.designationId[lastRow].value="";
		document.pIMSForm.posId[lastRow].value="0";
		document.pIMSForm.functionaryId[lastRow].value="0";
		document.pIMSForm.assignmentId[lastRow].value="0";


		}
		}
		};
		http.send(null);

		}
		function validateName( strValue)
		{
		var iChars = "!@#$%^&*()+=-[]\\\';,/{}|\":<>?0123456789";

		for (var i = 0; i < strValue.value.length; i++)
		{
		if (iChars.indexOf(strValue.value.charAt(i)) != -1)
		{
		alert('<bean:message key="alertValNme"/>');
		strValue.value="";
		strValue.focus();
		return false;
		}
		}
		}
		
		function grtDeathDateGtrTodate()
		{
			if(document.pIMSForm.deathDate.value!="")
			{
				
				 
				var tbl= document.getElementById("EOTable");
				var len = tbl.rows.length;
				if(len < 3)
				{
					
					
					if(document.pIMSForm.toDate.value!="")
					{
						if(compareDate(document.pIMSForm.deathDate.value,document.pIMSForm.toDate.value) == 1)
						{
							alert('The employee assignment should end before the death date.You entered the death date--'+document.pIMSForm.deathDate.value);
							return false;
						}
					}
				}
				else
				{
					
					if(compareDate(document.pIMSForm.deathDate.value,document.pIMSForm.toDate[len-2].value) == 1)
					{
							alert('Last Assignment should end before the Death Date. The death date you entered is--'+document.pIMSForm.deathDate.value);
							return false;
					}
					
				}

				
				
			}
			return true;
		}
		
		
		function ButtonPressNew(arg)
		{
		
		var scripts = new Array();
		if(arg == "savenew")
		{



				var tbl= document.getElementById("EOTable");
				var len = tbl.rows.length;
				if(len>2)
				{
					for(var i=0;i<len-1;i++)
					{
						scripts.push(document.pIMSForm.isPrimary[i].value);
						
					}
				}
				else
				{
					
					scripts.push(document.pIMSForm.isPrimary.value);
					
				}

					
					
		
		
		if(document.getElementById("pisTable").style.display!="none")
		{
			

		<%String modeForAutoEmpCode = ((String) (session
					.getAttribute("viewMode"))).trim();
			if (!"modify".equalsIgnoreCase(modeForAutoEmpCode)) {
				if (EisManagersUtill.getEisCommonsService()
						.isEmployeeAutoGenerateCodeYesOrNo() == false) {%>
	
				if(document.pIMSForm.employeeCode.value == "")
				{
					alert('<bean:message key="alertEnterEmpCode"/>');
					document.pIMSForm.employeeCode.focus();
					return false;
				}
	
			<%}
			}%>
		
		if(document.pIMSForm.statusTypeId.value!="0")
		{
			var isDeceased = document.getElementById("statusTypeId").options[document.getElementById("statusTypeId").selectedIndex].text;
			if(isDeceased.toUpperCase()=='DECEASED')
			{
				if(document.pIMSForm.deathDate.value=="")
				{
					alert('Please choose the Death date');
					document.pIMSForm.deathDate.focus();
					return false;
				}
			}
			
			if(isDeceased.toUpperCase()=='RETIRED')
			{
				 if(document.getElementById("dateOfRetirement").value=="")
				{
					alert('Please enter the Retired Date');
					document.getElementById("dateOfRetirement").focus();
					return false;
				}
			}
		}



		if(document.pIMSForm.employeeDob.value == "" )
		{
		alert('<bean:message key="alertEnterDob"/>');
		document.pIMSForm.employeeDob.focus();
		return false;
		}
		
		if(document.pIMSForm.statusMaster.value=='0')
			{
				alert('<bean:message key="alertEnterEmpType"/>');
				document.pIMSForm.statusMaster.focus();
				return false;
			}
       

		if(document.pIMSForm.firstName.value == "" || document.pIMSForm.firstName.value=='First Name')
		{
		alert('<bean:message key="alertEnterFirstName"/>');
		document.pIMSForm.firstName.focus();
		return false;
		}
		
			
		if(document.pIMSForm.propertyNoper.value == "")
		{
		alert('<bean:message key="alertPermAddr"/>');
		document.pIMSForm.propertyNoper.focus();
		return false;
		}
		

		if(document.pIMSForm.statusTypeId.value == '0')
		{
		alert("Please choose the status.");
		document.pIMSForm.statusTypeId.focus();
		return false;
		}
		
		if(document.pIMSForm.empGrpMstr.value == '0')
		{
			alert("Please choose the Employee Group ");
			document.pIMSForm.empGrpMstr.focus();
			return false;
		}	
		
		if(document.pIMSForm.dateOfFA.value == "" )
		{
				alert('<bean:message key="alertEnterDOAppointment"/>');
				document.pIMSForm.dateOfFA.focus();
				return false;
		}

		
		
       
		var tbl= document.getElementById("EOTable");
		var rows= tbl.rows.length;

		


		}

		<%String modermr = ((String) (session.getAttribute("viewMode")))
					.trim();
			if (modermr.equalsIgnoreCase("modify")) {%>

			
		
		var rmrtab= document.getElementById("EOtableRmr");
		rmrtab.style.display="";
		if(document.pIMSForm.modifyremarks.value=="")
		{
		alert('<bean:message key="alertRmkModify"/>');
		document.pIMSForm.modifyremarks.focus();
		return false;
		}
		
		

		<%}%>

	


		var submitType="";
		<%String mode1 = ((String) (session.getAttribute("viewMode"))).trim();
			if (mode1.equalsIgnoreCase("modify")) {%>

		submitType="modifyDetailsEmployee";

		<%} else if (mode1.equalsIgnoreCase("create")) {%>
		submitType="saveDetailsEmployee";
		<%}%>
		
		//enable all disabled assignment values
					if(!scripts.contains("Yes"))
						{
							alert('Employee should have a primary assignment');
							return false;
						}
						
			var test=ValidateAssignment();
			var checkdeathdate=grtDeathDateGtrTodate();
			var retValidation=validateRetirementDate();
			var checkFirstAppointmentDate = validateFirstAppointmentDate();
			var checkFromDateGrtToDate=compareDateRangeFromGrtTodate();
			if(test && checkdeathdate && checkFromDateGrtToDate)
			{
				var tbl= document.getElementById("EOTable");

				var len = tbl.rows.length;
				if(len>2)
				{
					for(var i=0;i<len-1;i++)
					{
						document.pIMSForm.fromDate[i].disabled=false;
						document.pIMSForm.toDate[i].disabled=false;
						document.pIMSForm.fundId[i].disabled=false;
						document.pIMSForm.functionId[i].disabled=false;
						document.pIMSForm.gradeId[i].disabled=false;
						document.pIMSForm.designationId[i].disabled=false;
						document.pIMSForm.posName[i].disabled=false;
						document.pIMSForm.functionaryId[i].disabled=false;
						document.pIMSForm.mainDepartmentId[i].disabled=false;
						document.pIMSForm.assignmentOrderNo[i].disabled=false;
						document.pIMSForm.isPrimary[i].disabled=false;
						document.pIMSForm.departmentIdHodSelect[i].disabled=false;
					}
				}
				else
				{
						document.pIMSForm.fromDate.disabled=false;
						document.pIMSForm.toDate.disabled=false;
						document.pIMSForm.fundId.disabled=false;
						document.pIMSForm.functionId.disabled=false;
						document.pIMSForm.gradeId.disabled=false;
						document.pIMSForm.designationId.disabled=false;
						document.pIMSForm.posName.disabled=false;
						document.pIMSForm.functionaryId.disabled=false;
						document.pIMSForm.mainDepartmentId.disabled=false;
						document.pIMSForm.assignmentOrderNo.disabled=false;
						document.pIMSForm.isPrimary.disabled=false;
						document.pIMSForm.departmentIdHodSelect.disabled=false;
				}
			}
		
			if(test==false && test!='undefined')
			{
				return false;
			}
			if(checkdeathdate==false && checkdeathdate!=undefined)
			{
				return false;
			}
			if(retValidation==false && retValidation!=undefined)
			{
			  return false;
			}
			if(checkFirstAppointmentDate==false)
			{
			makeReadOnlyAssignmentTable();
			  return false;
			}
			
			if(checkFromDateGrtToDate==false)
			{
			  return false;
			}
			copyDeptValstoHidden();
			if(!checkProbTableIfIncomplete())
			{
				return false;
			}
				document.pIMSForm.action = "${pageContext.request.contextPath}/pims/AfterPIMSAction.do?submitType="+submitType;
				document.pIMSForm.submit();
			
		}
		}
		
		function validateRetirementDate()
		{
		   var maxTodate=maxDateFromArray();
			var maxDateString = getMax(maxTodate);
			var retDate= document.pIMSForm.dateOfRetirement.value;
			if(retDate!=null && retDate!='' && maxDateString!=null && maxDateString!='' && maxDateString!=undefined)
				{
				    if(compareDate(retDate,maxDateString) == 1)
						{
							alert("Todate must be less than Retirement Date. ToDate-->"+maxDateString);
							document.pIMSForm.dateOfRetirement.value="";
							document.pIMSForm.retirementAge.value="";
							return false;
						}
				}
				return true;
				
			
		}
		function validateFirstAppointmentDate()
		{
		   var firstAssignment = document.pIMSForm.fromDate.value;
		   var appointmentDate = document.pIMSForm.dateOfFA.value;
		   if(firstAssignment!=null && firstAssignment!='' && appointmentDate!=null && appointmentDate!='')
		   {
			   if(compareDate(firstAssignment,appointmentDate) == 1)
				{
					alert("Assignment should start after the Appointment Date-->"+appointmentDate);
					return false;
				}
		  }
		}
		
		function hideTab()
		{
		document.getElementById("PersonalInfo").style.display="none";

		}

		function onLoadpis()
		{
		//setCSSClasses('empPage','Active');
		<%if (!((String) (session.getAttribute("viewMode"))).trim().equals(
					"create")) {%>
		document.pIMSForm.gender.value="<%=egpimsPersonalInformation.getGender()%>";
		//set style on load of page
		 if(document.getElementById('firstName').value!="" && document.getElementById('firstName').value!="First Name")
			 {
				document.getElementById('firstName').style.color='black';
				}
				if(document.getElementById('middleName').value!="" && document.getElementById('middleName').value!="Middle Name")
			 {
				document.getElementById('middleName').style.color='black';
				}
				if(document.getElementById('lastName').value!="" && document.getElementById('lastName').value!="Last Name")
			 {
				document.getElementById('lastName').style.color='black';
				}

				//set Same for father/spouse name
				if(document.getElementById('fatherfirstName').value!="" && document.getElementById('fatherfirstName').value!="First Name")
				 {
					document.getElementById('fatherfirstName').style.color='black';
				}
				if(document.getElementById('fathermiddleName').value!="" && document.getElementById('fathermiddleName').value!="Middle Name")
				 {
					document.getElementById('fathermiddleName').style.color='black';
				}

				if(document.getElementById('fatherlastName').value!="" && document.getElementById('fatherlastName').value!="Last Name")
				 {
					document.getElementById('fatherlastName').style.color='black';
				}



		<%}%>

		

			//SERVICE CHANGE
		document.getElementById("Technical").style.display="none";
		
		
		document.getElementById("Assignment").style.display="none";
		document.getElementById("ServiceDetailsHistory").style.display="none";
		showDeceased();
		
	//	document.getElementById("ServiceTableBtn").style.display="none";
		
	


		}

		function enable()
		{

		/*document.getElementById("pisTable").style.display="block";
		document.getElementById("EOTable").style.display="block";
		document.getElementById("EOTable0").style.display="block";
		document.getElementById("EOTableBtn").style.display="block";
		document.getElementById("AssignmentSaveTable").style.display="block";*/
		
	
		

		//SERVICE CHANGE
		/*document.getElementById("PronameTable").style.display="block";
		document.getElementById("PronameTableBtn").style.display="block";
		document.getElementById("RegnameTable").style.display="block";
		document.getElementById("RegTableBtn").style.display="block";*/


		//document.getElementById("BDnameTable").style.display="block";
		/*document.getElementById("edu").style.display="block";
		document.getElementById("EqnameTable").style.display="block";
		document.getElementById("EduTableBtn").style.display="block";
		document.getElementById("TeckDetails").style.display="block";
		document.getElementById("TeckDetailsBtn").style.display="block";
		document.getElementById("DepnameTable").style.display="block";
		document.getElementById("DeptTestBtn").style.display="block";
		document.getElementById("nomPir").style.display="block";
		document.getElementById("NDnameTable").style.display="block";
		document.getElementById("NPBtn").style.display="block";
		document.getElementById("immpropDet").style.display="block";
		document.getElementById("PDImmnameTable").style.display="block";
		document.getElementById("ImmProBtn").style.display="block";
		document.getElementById("movableProperty").style.display="block";
		document.getElementById("PDMovnameTable").style.display="block";
		document.getElementById("MovProBtn").style.display="block";*/

		//document.getElementById("ServiceTable").style.display="block";
		//document.getElementById("ServiceTableBtn").style.display="block";
		document.getElementById("PersonalInfoDetails").style.display="";
		document.getElementById("Technical").style.display="none";
		
		document.getElementById("Assignment").style.display="none";
		
		document.getElementById("ServiceDetailsHistory").style.display="none";
		
		//alert('In enable');
		
		//document.getElementById("submit").style.display="block";
		<%if (((String) (session.getAttribute("viewMode"))).trim().equals(
					"modify")) {%>
		document.getElementById("EOTableRmr").style.display="";
		<%}%>
		}
		function populateFromDate(obj)
		{
		<%if (((String) (session.getAttribute("viewMode"))).trim()
					.equalsIgnoreCase("create")
					|| ((String) (session.getAttribute("viewMode"))).trim()
							.equalsIgnoreCase("modify")) {%>

		document.getElementById("fromDate").value=obj.value;


		var temp;
		var payTable = document.getElementById("EOTable");

		var len = payTable.rows.length;




		if(obj.value != "")

		{
		if(obj.value.length == 2)
		{
		obj.value =obj.value+"/";
		}
		if(obj.value.length>5)
		{


		document.getElementById("annualIncrDate").value =obj.value;
		document.getElementById("annualIncrDateShow").value = obj.value.substr(0,5);
		//document.pIMSForm.effDate.value= obj.value;

		}


		}

		<%}%>

		}
		function checkAlphaNumeric(obj){
		if(obj.value!=""){
		var num=obj.value;
		var objRegExp  = /^[a-zA-Z  .]+$/;
		if(!objRegExp.test(num)){
		alert('<bean:message key="alertEnterValNme"/>');
		obj.value="";
		obj.focus();
		}
		}
		}


		function checkPanAlphaNumeric(obj){
		if(obj.value!="")
		{
		var num=obj.value;
		var objRegExp  = /^[a-zA-Z0-9]+$/;
		if(!objRegExp.test(num)){
		alert('<bean:message key="alertEnterValPanNum"/>');
		obj.value="";
		obj.focus();
		}
		}
		}

		function checkIdentAlphaNumeric(obj){
		if(obj.value!=""){
		var num=obj.value;
		var objRegExp  = /^[a-zA-Z0-9- () _ @  & "" ]+$/;
		if(!objRegExp.test(num)){
		alert('<bean:message key="alertValidIdenMark1"/>');
		obj.value="";
		obj.focus();
		}
		}
		}




		function calLength(obj)
		{
		if(obj.value !="")
		{

		var strTemp = obj.value;

		if(strTemp.length !== 10)
		{
		alert( '<bean:message key="alertTenpanNum"/>');
		obj.focus();
		obj.value="";
		return false;
		}
		obj.value=strTemp.toUpperCase();
		return true;
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
				alert( 'Permanent Address Exceeded The Permissible Length' );
			}
			else if(obj.name =='streetNameper')
			{
				alert( 'Correspondence Address Exceeded The Permissible Length' );
			}
			else
			{
				alert( obj.name+' Exceeded The Permissible Length' );
			}
		obj.focus();
		obj.value="";
		return false;
		}
		return true;
		}

		}
		function checkUserLength(obj)
		{
		if(obj.value !="")
		{
		var strNmae = obj.value;
		if(strNmae.length > 32)
		{
		alert( obj.name+' Exceeded The Permissible Length' );
		obj.focus();
		obj.value="";
		return false;
		}
		return true;
		}

		}




		function chckfromto(obj)
		{
			
		var tbl= document.getElementById("EOTable");
		var row=tbl.rows.length;
		
		var toDate = getControlInBranch(tbl.rows[(row-1)],'toDate');
		var isPrimary = getControlInBranch(tbl.rows[(row-1)],'isPrimary');
		var isPrimary="";
		if(document.pIMSForm.checkPrimary[0].checked){
			isPrimary = 'Yes'; 
		}
		if(document.pIMSForm.checkPrimary[1].checked){
			isPrimary = 'No'; 
		}
		//getControlInBranch(row-1,"toDate");
				var frmDate = obj.value;
				if(document.getElementById('assignEntered').value=="")
				{
					if(obj.value!="")
					{

						if(isPrimary=='Yes'){
							if(toDate!=null && frmDate==toDate.value || compareDate(frmDate,toDate.value) == 1)
							{
								alert('<bean:message key="alertFromDtGtLastToDt"/>');
								obj.focus();
								obj.value="";
								return false;
							}
						}
					}
				}
		
		}

		// check difference betweem from to

		function checkDiffFromto(obj)
		{
			
			var tbl= document.getElementById("EOTable");
			var row=tbl.rows.length;
			
			var toDate = getControlInBranch(tbl.rows[(row-1)],'toDate');
			//getControlInBranch(row-1,"toDate");
			var frmDate = obj;
			var flag=validateDateFormat(frmDate);
			
			var isPrimary="";
			if(document.pIMSForm.checkPrimary[0].checked){
				isPrimary = 'Yes'; 
			}
			if(document.pIMSForm.checkPrimary[1].checked){
				isPrimary = 'No'; 
			}

			if(document.getElementById('assignEntered').value=="")
			{
				if(frmDate.value!="")
				{
				if(obj.value!="")
				{
				var http = initiateRequest();
				var url = "<%=request.getContextPath()%>/pims/checkDaysDifferenceAjax.jsp?dateOfFA="+toDate.value+"&fromDate="+frmDate.value;
				http.open("GET", url, true);
				http.onreadystatechange = function()
				{

				if (http.readyState == 4)
				{

				if (http.status == 200)
				{

				var statusString =http.responseText.split("^");
				if(flag="true" && statusString[0]!="" && statusString[0]!="1" && statusString[0]!="0")
				{
					if(isPrimary=='Yes')
					{
						alert('There is a gap between two Assignments.Please Verify');
					}



				}

				}
				}

				};
				http.send(null);
				}
			}
			}		
		}

		//date check



		function chcktoFromNextRow(obj)
		{

		var tbl= document.getElementById("EOTable");
		var row=getRow(obj);
		if(row.rowIndex<tbl.rows.length)
		{

		var  toDate = getControlInBranch(row,"toDate");
		if(row.rowIndex<tbl.rows.length-1)
		{
		var frmDate = getControlInBranch(tbl.rows[(row.rowIndex+1)],'fromDate');
		if(compareDate(frmDate.value,toDate.value) == 1)
		{
		alert('<bean:message key="alertFromDtGtLastToDt"/>');
		frmDate.focus();
		frmDate.value="";
		return false;
		}


		}

		}


		}



		//check difference from to date



		function chckDiffTonextFrom(obj)
		{

		var tbl= document.getElementById("EOTable");
		var row=getRow(obj);
		if(row.rowIndex<tbl.rows.length)
		{

		var  toDate = getControlInBranch(row,"toDate");
		if(row.rowIndex<tbl.rows.length-1)
		{
		var frmDate = getControlInBranch(tbl.rows[(row.rowIndex+1)],'fromDate');

		var http = initiateRequest();
		var url = "<%=request.getContextPath()%>/pims/checkDaysDifferenceAjax.jsp?dateOfFA="+toDate.value+"&fromDate="+frmDate.value;
		http.open("GET", url, true);
		http.onreadystatechange = function()
		{

		if (http.readyState == 4)
		{

		if (http.status == 200)
		{

		var statusString =http.responseText.split("^");
		if(statusString!="1" && statusString!="0")
		{
		alert('There is a gap between two Assignment dates.Please Verify');

		}

		}
		}

		};
		http.send(null);



		}

		}


		}



		function checkUserName(obj){
		if(obj.value!=""){
		var num=obj.value;
		var objRegExp  = /^[a-zA-Z0-9-._]+$/;
		if(!objRegExp.test(num)){
		alert('<bean:message key="alertpropUsernme"/>');
		obj.value="";
		obj.focus();
		}
		}
		}





		function addTitleAttributes(obj)
		{
		var objName = obj.name;
		numOptions = document.getElementById(objName).options.length;
		for (i = 0; i < numOptions; i++)
		document.getElementById(objName).options[i].title =
		document.getElementById(objName).options[i].text;
		}
		
		function checkPosForDesig()
		{
		   if(document.pIMSForm.desginationName.value=="")
		   {		   		   
			document.pIMSForm.desgEnterId.value="";
			document.pIMSForm.positionEntryName.value="";
			document.pIMSForm.positionId.value="";
			document.pIMSForm.desginationName.focus();		
		   }
		}


		function checkPosition(obj,event)
		{
			// has to tested. has to shift to checkPosition()
			
			document.getElementById('codescontainerPos').innerHTML="";
			/*
			if(oAutoCompPos!=null || oAutoCompPos!=undefined)
			{
	
			if(!(oAutoCompPos==""))
			{
	
			oAutoCompPos.destroy();
			}
			}
			*/
			//var desgId=document.pIMSForm.desgId.value;
			
			var tbl= document.getElementById("EOTable");
			var len = tbl.rows.length;
			var desnid;
			var row=getRow(obj);
			var posHead = getControlInBranch(row,"positionId");
			var desgId= getControlInBranch(row,"desgEnterId").value;
			var desHead = getControlInBranch(row,"obj");
			var posId = "posId"+(row.rowIndex-1);
			var fromDate =document.getElementById("fromDateEnter").value;
			var toDateEntered =document.getElementById("toDateEnter").value;
			var isPrimary = "";
			
			if(document.pIMSForm.checkPrimary[0].checked){
				isPrimary = 'Y'; 
			}
			if(document.pIMSForm.checkPrimary[1].checked){
				isPrimary = 'N'; 
			}
			
			
			if(desgId!=null && desgId!="" && desgId!='undefined')
			{
				
	
			//loadSelectData('${pageContext.request.contextPath}/commonyui/egov/loadComboAjax.jsp', 'EG_POSITION', 'ID', 'POSITION_NAME', //'DESIG_ID=#1 order by ID', 'desgId' , 'posId');
	
	
			// Changed for Auto Complete
	
	
	
			//loadSelectDataForCurrentRow('${pageContext.request.contextPath}/commonyui/egov/loadComboAjax.jsp', 'EG_POSITION', 'ID', //'POSITION_NAME', 'DESIG_ID=#1 order by ID', 'designationId', 'posId',obj,'EOTable');
			<%String employeeId = request.getParameter("Id");%>
			var employeeId = <%=employeeId%>
			var type='getAllDesgBasedPosition';
			var url = "${pageContext.request.contextPath}/pims/employeeGradeAjax.jsp?type="+type+"&desgId="+desgId+"&fromDate="+fromDate+"&toDate="+toDateEntered+"&isPrimary="+isPrimary+"&empId="+employeeId;
			var req2 = initiateRequest();
			req2.open("GET", url, false);
			
			req2.send(null);
			
			if (req2.status == 200)
			{
			   
					var codes2=req2.responseText;
					
					var a = codes2.split("^");
					var codes = a[0];
					empCodeArray=codes.split("+");
					selectPostion = new YAHOO.widget.DS_JSArray(empCodeArray);
					
			}
			
				
			}	
	 }
	
	
		function checkTodatePresnt(obj)
		{

		var row = getRow(obj);
		var tbl= document.getElementById("EOTable");

		var len = tbl.rows.length;
		if(len>2){
		for(var i=0;i<len-1;i++)
		{
		var todts=document.pIMSForm.toDate[i].value;
		if(todts == null || todts =="")
		{
		alert('<bean:message key="alertFillTodateBfDesg"/>');
		document.pIMSForm.toDate[i].focus();
		document.pIMSForm.designationId[i].value="";

		}


		}
		}else{

		var todte=document.pIMSForm.toDate.value;
		if(todte == null || todte =="")
		{
		alert('<bean:message key="alertFillTodateBfDesg"/>');
		document.pIMSForm.toDate.focus();
		document.pIMSForm.designationId.value="";

		}

		}
		}
		function toRetirementDate(obj)
		{
			var todate = obj.value;
			
			var retire = document.pIMSForm.dateOfRetirement.value;
			if(obj.value != "" && retire!=null && retire!="")
			{
				if(compareDate(document.pIMSForm.dateOfRetirement.value,todate) == 1)
					{
					alert('<bean:message key="alertToLesRetire"/>'+' -'+document.pIMSForm.dateOfRetirement.value);
					obj.focus();
					obj.value="";
					return false;
					}
			}
			
		}


		function checkNumericForCode(obj)
		{

		if(obj.value!="")
		{
		var num=obj.value;//[^a-zA-Z0-9_]*$
		var objRegExp  = /^[a-zA-Z0-9_-]+$/;
		if(!objRegExp.test(num))
		{
		alert('Please fill the proper code number. Only numbers, alphabets, -, _ are allowed');
		obj.value="";
		obj.focus();
		}
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
		alert('Please fill the proper age');
		obj.value="";
		obj.focus();
		}
		}
		}

		function checkDesg()
		{
			    var desgId= document.pIMSForm.desgEnterId.value;			    
			    if(desgId=='undefined' || (document.pIMSForm.desginationName.value!="" && desgId==""))
				{
					alert('Please select a valid designation');
					document.pIMSForm.desginationName.value="";
					document.pIMSForm.desgEnterId.value="";
					document.pIMSForm.positionEntryName.value="";
					document.pIMSForm.positionId.value="";
					document.pIMSForm.desginationName.focus();
					return false;

				}
				return true;
			
		}

		function checkPosAutoComp()
		{
				var posId= document.pIMSForm.positionId.value;				
				if(posId=='undefined' || (document.pIMSForm.positionEntryName.value!="" && posId==""))
				{
					alert('Please select a valid Position');					
					document.pIMSForm.positionEntryName.value="";
					document.pIMSForm.positionId.value="";
					document.pIMSForm.positionEntryName.focus();
					return false;
				}
				return true;
			

		}

	//Shifting required script

	function whichButtonService(tbl,obj,objr)
{
   if(checkPayHead(tbl))
	{
	   addRowToTable(tbl,obj);
   }
}

//Changing according to row

function checkPayHead(tableName)
{
	if(tableName=='ServiceTable')
	{
		var tbl = document.getElementById('ServiceTable');
		var rCount=tbl.rows.length-2;
		if(tbl.rows.length == 3)
		{
			if(document.pIMSForm.comments.value=="")
			{
				alert("Please enter the service entry description");
				document.pIMSForm.comments.focus();
				return false;
			}

			if(document.pIMSForm.commentDate.value=="")
			{
				alert("Please enter the date");
				document.pIMSForm.commentDate.focus();
				return false;
			}
		}
		else
		{
			if(tbl.rows.length>3)
			{	
				if(document.pIMSForm.comments[rCount-1].value=="")
				{
					alert("Please enter the service entry description");
					document.pIMSForm.comments[rCount-1].focus();
					return false;
				}
				if(document.pIMSForm.commentDate[rCount-1].value=="")
				{
					alert("Please enter the date");
					document.pIMSForm.commentDate[rCount-1].focus();
					return false;
				}
			}
		 }
	}

	else if(tableName=='PronameTable')
	{
		var tbl = document.getElementById('PronameTable');
		var rCount=tbl.rows.length-2;
		if(tbl.rows.length == 3)
		{
			if(document.pIMSForm.proPostId.value=="0")
			{
				alert("Please choose the probation post");
				document.pIMSForm.proPostId.focus();
				return false;
			}

			if(document.pIMSForm.proFrom.value=="")
			{
				alert("Please Enter probation from date");
				document.pIMSForm.proFrom.focus();
				return false;
			}
			if(document.pIMSForm.proTo.value=="")
			{
				alert("Please Enter probation to date");
				document.pIMSForm.proTo.focus();
				return false;
			}
			if(document.pIMSForm.proOrderNo.value=="")
			{
				alert("Please Enter the Order");
				document.pIMSForm.proOrderNo.focus();
				return false;
			}
			if(document.pIMSForm.proOrderDate.value=="")
			{
				alert("Please Enter the Order date");
				document.pIMSForm.proOrderDate.focus();
				return false;
			}
		}
		else
		{
			
			if(tbl.rows.length>3)
			{	
					
					if(document.pIMSForm.proPostId[rCount-1].value=="0")
					{
						alert("Please choose the probation post");
						document.pIMSForm.proPostId[rCount-1].focus();
						return false;
					}
					if(document.pIMSForm.proFrom[rCount-1].value=="")
					{
						alert("Please Enter probation from date");
						document.pIMSForm.proFrom[rCount-1].focus();
						return false;
					}
					if(document.pIMSForm.proTo[rCount-1].value=="")
					{
						alert("Please Enter probation from date");
						document.pIMSForm.proTo[rCount-1].focus();
						return false;
					}
					if(document.pIMSForm.proOrderNo[rCount-1].value=="")
					{
						alert("Please Enter the Order");
						document.pIMSForm.proOrderNo[rCount-1].focus();
						return false;
					}
					if(document.pIMSForm.proOrderDate[rCount-1].value=="")
					{
						alert("Please Enter the Order date");
						document.pIMSForm.proOrderDate[rCount-1].focus();
						return false;
					}
				
			}
		 }

	}
	else if(tableName=='RegnameTable')
	{
		var tbl = document.getElementById('RegnameTable');
		var rCount=tbl.rows.length-2;
		if(tbl.rows.length == 3)
		{
			if(document.pIMSForm.regPostId.value=="0")
			{
				alert("Please choose the Regularisation post");
				document.pIMSForm.regPostId.focus();
				return false;
			}

			if(document.pIMSForm.regDate.value=="")
			{
				alert("Please Enter Regularisation declared date");
				document.pIMSForm.regDate.focus();
				return false;
			}
			if(document.pIMSForm.regOrder.value=="")
			{
				alert("Please Enter the Order");
				document.pIMSForm.regOrder.focus();
				return false;
			}
			if(document.pIMSForm.regOrderDate.value=="")
			{
				alert("Please Enter the Order date");
				document.pIMSForm.regOrderDate.focus();
				return false;
			}
		}
		else
		{
			
			if(tbl.rows.length>3)
			{	
					
					if(document.pIMSForm.regPostId[rCount-1].value=="0")
					{
						alert("Please choose the probation post");
						document.pIMSForm.regPostId[rCount-1].focus();
						return false;
					}
					if(document.pIMSForm.regDate[rCount-1].value=="")
					{
						alert("Please Enter probation declared date");
						document.pIMSForm.regDate[rCount-1].focus();
						return false;
					}
					if(document.pIMSForm.regOrder[rCount-1].value=="")
					{
						alert("Please Enter the Order");
						document.pIMSForm.regOrder[rCount-1].focus();
						return false;
					}
					if(document.pIMSForm.regOrderDate[rCount-1].value=="")
					{
						alert("Please Enter the Order date");
						document.pIMSForm.regOrderDate[rCount-1].focus();
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

   	
   	if(tbl=='ServiceTable')
  	{  
	    var testDate = new Date();
    	testMo = testDate.getMonth() + 1;
		testDay = testDate.getDate();
    	testYr = testDate.getFullYear();

		var currentDate;
		if(testMo<10){
			currentDate=testDay+'/0'+testMo+'/'+testYr;
		}
		else{
	  		currentDate=testDay+'/'+testMo+'/'+testYr;
	  	}

	    var rCellLen=tableObj.rows[lastRow].cells.length;
	 	tableObj.insertRow(lastRow+1);
	 	for(var i=0;i<rCellLen;i++)
		{
			if(i==6)
			{
				var cellObj = document.createElement("TD");
				cellObj.innerHTML = ' <div class="buttonholderrnew">'+
								    ' <a href="#">'+
								    ' <img src="<%=request.getContextPath()%>/images/page_text.gif" title="Add" alt="Add" width="16" height="16" border="0" onclick="onUpload(this);return false;" /></a>'+
								    ' </div>';
				tableObj.rows[lastRow+1].appendChild(cellObj);	  
			}
			else
			{
				var rowCellObj=null;
				rowCellObj=tableObj.rows[lastRow].cells[i].cloneNode(true);
				tableObj.rows[lastRow+1].appendChild(rowCellObj);
			}
		}

 	   document.pIMSForm.serialNo[lastRow-1].value=lastRow;
 	   document.pIMSForm.comments[lastRow-1].value="";
	   document.pIMSForm.idService[lastRow-1].value="0";
	   document.pIMSForm.serviceOrderNo[lastRow-1].value="";
	   document.pIMSForm.serviceDocNo[lastRow-1].value="";
 	   document.pIMSForm.commentDate[lastRow-1].value=currentDate;
 	   document.pIMSForm.reason[lastRow-1].value="";
 	   document.pIMSForm.payScale[lastRow-1].value="";
	   
 	   
  }
  else if(tbl=='PronameTable')
	{
		 var rowObj = tableObj.rows[lastRow].cloneNode(true);
       tbody.appendChild(rowObj);
  	   var remlen1=document.pIMSForm.proFrom.length;
 	   document.pIMSForm.proPostId[remlen1-1].value="0";
	   document.pIMSForm.idProbation[remlen1-1].value="0";
 	   document.pIMSForm.proFrom[remlen1-1].value="";
 	  document.pIMSForm.proTo[remlen1-1].value="";
 	   document.pIMSForm.proOrderNo[remlen1-1].value="";
	    document.pIMSForm.proOrderDate[remlen1-1].value="";
  }
   else if(tbl=='RegnameTable')
	{
		 var rowObj = tableObj.rows[lastRow].cloneNode(true);
       tbody.appendChild(rowObj);
  	   var remlen1=document.pIMSForm.regDate.length;
 	   document.pIMSForm.regularisationId[remlen1-1].value="0";
	   document.pIMSForm.regPostId[remlen1-1].value="0";
 	   document.pIMSForm.regDate[remlen1-1].value="";
 	   document.pIMSForm.regOrder[remlen1-1].value="";
	    document.pIMSForm.regOrderDate[remlen1-1].value="";
  }
  
  else if(tbl=='EqnameTable')
	{

	  var rCellLen=tableObj.rows[lastRow].cells.length;
	 	tableObj.insertRow(lastRow+1);
	 	
	 	for(var i=0;i<rCellLen;i++)
		{
			if(i==4)
			{
				var cellObj = document.createElement("TD");
				cellObj.innerHTML = ' <div class="buttonholderrnew">'+
								    ' <a href="#">'+
								    ' <img src="<%=request.getContextPath()%>/images/page_text.gif" title="Add" alt="Add" width="16" height="16" border="0" onclick="uploadEduDoc(this);return false;" /></a>'+
								    ' </div>';
				tableObj.rows[lastRow+1].appendChild(cellObj);	  
			}
			else
			{
				var rowCellObj=null;
				rowCellObj=tableObj.rows[lastRow].cells[i].cloneNode(true);
				tableObj.rows[lastRow+1].appendChild(rowCellObj);
			}
		}
		
	  document.pIMSForm.educationDetailsId[lastRow-1].value="0";
	  document.pIMSForm.qulification[lastRow-1].value="";
 	  document.pIMSForm.majorSubject[lastRow-1].value="";
 	  document.pIMSForm.monthandYearOfPass[lastRow-1].value="";
	  document.pIMSForm.universityBoard[lastRow-1].value="";
	  document.pIMSForm.eduDocNo[lastRow-1].value="";
	  
	
		   
  }
  
  else if(tbl=='TeckDetails')
	{

	  var rCellLen=tableObj.rows[lastRow].cells.length;
	 	tableObj.insertRow(lastRow+1);
	 	
	 	for(var i=0;i<rCellLen;i++)
		{
			if(i==4)
			{
				var cellObj = document.createElement("TD");
				cellObj.innerHTML = ' <div class="buttonholderrnew">'+
								    ' <a href="#">'+
								    ' <img src="<%=request.getContextPath()%>/images/page_text.gif" title="Add" alt="Add" width="16" height="16" border="0" onclick="uploadTechDoc(this);return false;" /></a>'+
								    ' </div>';
				tableObj.rows[lastRow+1].appendChild(cellObj);	  
			}
			else
			{
				var rowCellObj=null;
				rowCellObj=tableObj.rows[lastRow].cells[i].cloneNode(true);
				tableObj.rows[lastRow+1].appendChild(rowCellObj);
			}
		}
  	   document.pIMSForm.tecnicalQualificationId[lastRow-1].value="0";
  	   document.pIMSForm.skillId[lastRow-1].value="0";
	   document.pIMSForm.gradeId1[lastRow-1].value="0";
 	   document.pIMSForm.yearOfPassTQ[lastRow-1].value="";
 	   document.pIMSForm.remarks[lastRow-1].value="";
 	   document.pIMSForm.techDocNo[lastRow-1].value="";
	    
  }
  else if(tbl=='DepnameTable')
	{
	 
	   var rowObj = tableObj.rows[lastRow].cloneNode(true);
       tbody.appendChild(rowObj);
  	   var remlen1=document.pIMSForm.deptTestsId.length;
  	    document.pIMSForm.deptTestsId[remlen1-1].value="0";
  	    document.pIMSForm.nameOfTheTestId[remlen1-1].value="0";
	   document.pIMSForm.monthandYearOfPassDT[remlen1-1].value="";
 	   
  }
  else if(tbl=='PDImmnameTable')
	{
	 
	   var rowObj = tableObj.rows[lastRow].cloneNode(true);
       tbody.appendChild(rowObj);
       
       var remlen1=document.pIMSForm.immPropertyDetailsId.length;
  	   
  	    document.pIMSForm.immPropertyDetailsId[remlen1-1].value="0";
  	    document.pIMSForm.propertydiscriptionImm[remlen1-1].value="";
	    document.pIMSForm.placeImm[remlen1-1].value="";
	   
	    document.pIMSForm.howAcquiredImm[remlen1-1].value="0";
  	    document.pIMSForm.presentValueImm[remlen1-1].value="";
	    document.pIMSForm.permissionObtainedImm[remlen1-1].value="N";
	   
	    document.pIMSForm.orderNoImm[remlen1-1].value="";
	    document.pIMSForm.dateImm[remlen1-1].value="";
 	   
  }
  else if(tbl=='PDMovnameTable')
	{
	 
	   var rowObj = tableObj.rows[lastRow].cloneNode(true);
       tbody.appendChild(rowObj);
       
       var remlen1=document.pIMSForm.movPropertyDetailsId.length;
  	   
  	    document.pIMSForm.movPropertyDetailsId[remlen1-1].value="0";
  	    document.pIMSForm.propertydiscriptionMo[remlen1-1].value="";
	    document.pIMSForm.valueOfTimeOfPurchaseMo[remlen1-1].value="";
	   
	    document.pIMSForm.howAcquiredMov[remlen1-1].value="0";
  	    document.pIMSForm.permissionObtainedMo[remlen1-1].value="N";
	    document.pIMSForm.orderNoMo[remlen1-1].value="";
	    document.pIMSForm.dateMo[remlen1-1].value="";
 	   
  }
  
 

}
function deleteServiceRow(table,obj)
{
	
	if(table=='ServiceTable')
	{
		 var tbl = document.getElementById(table);
		var rowNumber=getRow(obj).rowIndex;
		var idService =	getControlInBranch(tbl.rows[rowNumber],'idService').value;
	
		if(${fn:length(pIMSForm.comments)}<(eval(rowNumber)-2))
		{
			tbl.deleteRow(rowNumber);
			<%if (modeonloadView.equalsIgnoreCase("modify")) {%>
			if(idService != null && idService != "")
				populateDeleteSet("delServices" , idService);
				
			<%}%>

	
		}
		else
		{
		     alert("You cannot delete this row");
		     return false;
		}
		
	}
	else if(table=='PronameTable')
	{
		var tbl = document.getElementById(table);
		var rowNumber=getRow(obj).rowIndex;
		var idProbation =	getControlInBranch(tbl.rows[rowNumber],'idProbation').value;
	
	   
		if(${fn:length(pIMSForm.proPostId)}<(eval(rowNumber)-2))
		{
			tbl.deleteRow(rowNumber);
			<%if (modeonloadView.equalsIgnoreCase("modify")) {%>
			if(idProbation != null && idProbation != "")
				populateDeleteSet("delProbation" , idProbation);
				
			<%}%>

	
		}
		else
		{
		     alert("You cannot delete this row");
		     return false;
		}
		
	}

	else if(table=='RegnameTable')
	{
		var tbl = document.getElementById(table);
		var rowNumber=getRow(obj).rowIndex;
		var idReg =	getControlInBranch(tbl.rows[rowNumber],'regularisationId').value;
	
	   
		if(${fn:length(pIMSForm.regPostId)}<(eval(rowNumber)-2))
		{
			tbl.deleteRow(rowNumber);
			<%if (modeonloadView.equalsIgnoreCase("modify")) {%>
			if(idReg != null && idReg != "")
				populateDeleteSet("delReg" , idReg);
				
			<%}%>

	
		}
		else
		{
		     alert("You cannot delete this row");
		     return false;
		}
		
	}
	else if(table=='EqnameTable')
	{
	  
		var tbl = document.getElementById(table);
		var rowNumber=getRow(obj).rowIndex;
		
		var idEdu =	getControlInBranch(tbl.rows[rowNumber],'educationDetailsId').value;
	   	if(${fn:length(pIMSForm.educationDetailsId)}<(eval(rowNumber)-2))
		{
			tbl.deleteRow(rowNumber);
			<%if (modeonloadView.equalsIgnoreCase("modify")) {%>
			if(idEdu != null && idEdu != "")
				populateDeleteSet("delEdu" , idEdu);
				
			<%}%>

	
		}
		else
		{
		     alert("You cannot delete this row");
		     return false;
		}
		
	}
	else if (table =='TeckDetails'){
		var tbl = document.getElementById(table);
		var rowNumber=getRow(obj).rowIndex;
		var idTeck =	getControlInBranch(tbl.rows[rowNumber],'tecnicalQualificationId').value;
	
	   
		if(${fn:length(pIMSForm.skillId)}<(eval(rowNumber)-2))
		{
			tbl.deleteRow(rowNumber);
			<%if (modeonloadView.equalsIgnoreCase("modify")) {%>
			if(idTeck != null && idTeck != "")
				populateDeleteSet("delTeck" , idTeck);
				
			<%}%>

	
		}
		else
		{
		     alert("You cannot delete this row");
		     return false;
		}
	}
	
	else if (table =='DepnameTable'){
		var tbl = document.getElementById(table);
		var rowNumber=getRow(obj).rowIndex;
		var idDep =	getControlInBranch(tbl.rows[rowNumber],'deptTestsId').value;
	
	   
		if(${fn:length(pIMSForm.nameOfTheTestId)}<(eval(rowNumber)-2))
		{
			tbl.deleteRow(rowNumber);
			<%if (modeonloadView.equalsIgnoreCase("modify")) {%>
			if(idDep != null && idDep != "")
				populateDeleteSet("delDep" , idDep);
				
			<%}%>

	
		}
		else
		{
		     alert("You cannot delete this row");
		     return false;
		}
	}
	
	else if (table =='PDImmnameTable'){
		var tbl = document.getElementById(table);
		var rowNumber=getRow(obj).rowIndex;
		var idImm =	getControlInBranch(tbl.rows[rowNumber],'immPropertyDetailsId').value;
	
	   
		if(${fn:length(pIMSForm.howAcquiredImm)}<(eval(rowNumber)-1))
		{
			tbl.deleteRow(rowNumber);
			<%if (modeonloadView.equalsIgnoreCase("modify")) {%>
			if(idImm != null && idImm != "")
				populateDeleteSet("delImm" , idImm);
				
			<%}%>

	
		}
		else
		{
		     alert("You cannot delete this row");
		     return false;
		}
	}
	
	else if (table =='PDMovnameTable'){
		var tbl = document.getElementById(table);
		var rowNumber=getRow(obj).rowIndex;
		var idMov =	getControlInBranch(tbl.rows[rowNumber],'movPropertyDetailsId').value;
	
	   
		if(${fn:length(pIMSForm.propertydiscriptionMo)}<(eval(rowNumber)-1))
		{
			tbl.deleteRow(rowNumber);
			<%if (modeonloadView.equalsIgnoreCase("modify")) {%>
			if(idMov != null && idMov != "")
				populateDeleteSet("delMov" , idMov);
				
			<%}%>

	
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
		var url = "${pageContext.request.contextPath}/pims/updateDelSets.jsp?type="+setName+"&id="+delId;
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

function checkProbTableIfIncomplete()
	{
		var tbl = document.getElementById('PronameTable');
		var rCount=tbl.rows.length-2;
		if(tbl.rows.length == 3)
		{
			if((document.pIMSForm.proPostId.value=="0" && document.pIMSForm.proFrom.value=="" &&
					document.pIMSForm.proTo.value=="" && document.pIMSForm.proOrderNo.value=="" 	&&
					document.pIMSForm.proOrderDate.value=="" )
					 || (document.pIMSForm.proPostId.value!="0" && document.pIMSForm.proFrom.value!="" &&
							document.pIMSForm.proTo.value!="" && document.pIMSForm.proOrderNo.value!="" 	&&
							document.pIMSForm.proOrderDate.value!="" ))
				{
					return true;
				}
			else
				{
				if(document.pIMSForm.proPostId.value=="0")
				{
					alert("Please choose the probation post");
					document.pIMSForm.proPostId.focus();
					return false;
				}

				if(document.pIMSForm.proFrom.value=="")
				{
					alert("Please Enter probation from date");
					document.pIMSForm.proFrom.focus();
					return false;
				}
				if(document.pIMSForm.proTo.value=="")
				{
					alert("Please Enter probation to date");
					document.pIMSForm.proTo.focus();
					return false;
				}
				if(document.pIMSForm.proOrderNo.value=="")
				{
					alert("Please Enter the Order");
					document.pIMSForm.proOrderNo.focus();
					return false;
				}
				if(document.pIMSForm.proOrderDate.value=="")
				{
					alert("Please Enter the Order date");
					document.pIMSForm.proOrderDate.focus();
					return false;
				}
				
				}
			}
		if(tbl.rows.length > 3)
			{
			if(document.pIMSForm.proPostId[rCount -1].value=="0" || document.pIMSForm.proFrom[rCount -1].value=="" ||
					document.pIMSForm.proTo[rCount -1].value=="" || document.pIMSForm.proOrderNo[rCount -1].value=="" 	||
					document.pIMSForm.proOrderDate[rCount -1].value=="" )
				{
				
				if(document.pIMSForm.proPostId[rCount -1].value=="0")
				{
					alert("Please choose the probation post");
					document.pIMSForm.proPostId[rCount -1].focus();
					return false;
				}

				if(document.pIMSForm.proFrom[rCount -1].value=="")
				{
					alert("Please Enter probation from date");
					document.pIMSForm.proFrom[rCount -1].focus();
					return false;
				}
				if(document.pIMSForm.proTo[rCount -1].value=="")
				{
					alert("Please Enter probation to date");
					document.pIMSForm.proTo[rCount -1].focus();
					return false;
				}
				if(document.pIMSForm.proOrderNo[rCount -1].value=="")
				{
					alert("Please Enter the Order");
					document.pIMSForm.proOrderNo[rCount -1].focus();
					return false;
				}
				if(document.pIMSForm.proOrderDate[rCount -1].value=="")
				{
					alert("Please Enter the Order date");
					document.pIMSForm.proOrderDate[rCount -1].focus();
					return false;
				}
				
				}
			}
		return true;
		}
function getTable(obj)
{
if(!obj)return null;
tag = obj.nodeName.toUpperCase();
while(tag != 'BODY'){
if (tag == 'TABLE') return obj;
obj=obj.parentNode;
tag = obj.nodeName.toUpperCase();
}
return null;
}


function chkPrbnPrevFromCurToDate(obj)
{
	var probTable=getTable(obj);
	if(probTable!= null)
	{
	var probRows = probTable.rows.length;
	var nodeName = "";
	var prvRecFromDate="";
	var prvRecToDate="";
	var curRecFromDate="";
		if(probRows > 3)
		{
			for (i=probRows-1;i>2;i--)
			{
			nodeName=probTable.rows[i].cells[1].childNodes[1].nodeName;
			if(nodeName=='INPUT')
				{
					prvRecFromDate=probTable.rows[i-1].cells[1].childNodes[1].value;
					prvRecToDate=probTable.rows[i-1].cells[2].childNodes[1].value;
					curRecFromDate=probTable.rows[i].cells[1].childNodes[1].value;
					
					if(compareDate(curRecFromDate,prvRecToDate)!=-1 || compareDate(curRecFromDate,prvRecFromDate)!=-1)
					{
						alert("ProbationFrom date " + curRecFromDate+ " should be greater than the dates in previous records");
						probTable.rows[i].cells[1].childNodes[1].value="";
						probTable.rows[i].cells[2].childNodes[1].value="";
						return false;
					}						
				}
			}
		}
	}	
}

function probationDateOverlap(obj)
{
	var probTable=getTable(obj);
	if(probTable!= null)
	{
		var probRows = probTable.rows.length;
		var nodeName = "";
		var prvRecFromDate="";
		var prvRecToDate="";
		var curRecFromDate="";
		var curRecToDate="";
		if (probRows == 3)
		{
			if(probTable.rows[2].cells[1].childNodes[1].nodeName =='INPUT' && probTable.rows[2].cells[2].childNodes[1].nodeName =='INPUT')
			{ 
					curRecFromDate=probTable.rows[2].cells[1].childNodes[1].value;
					curRecToDate=probTable.rows[2].cells[2].childNodes[1].value;
					if(compareDate(curRecToDate,curRecFromDate) !=-1)
					{
						alert("ProbationTo date should be greater than ProbationFrom date" );
			//			probTable.rows[2].cells[1].childNodes[1].value="";
						probTable.rows[2].cells[2].childNodes[1].value="";
						return false;
					}
			}
		}
		if(probRows > 3)
		{
			for (i=probRows-1;i>2;i--)
			{
				nodeName=probTable.rows[i].cells[1].childNodes[1].nodeName;
				if(nodeName=='INPUT')
					{
					prvRecFromDate=probTable.rows[i-1].cells[1].childNodes[1].value;
					prvRecToDate=probTable.rows[i-1].cells[2].childNodes[1].value;
					curRecFromDate=probTable.rows[i].cells[1].childNodes[1].value;
					curRecToDate=probTable.rows[i].cells[2].childNodes[1].value;
					if(compareDate(curRecFromDate,prvRecFromDate) !=-1 ||compareDate(curRecFromDate,prvRecToDate)!=-1 || compareDate(curRecToDate,prvRecToDate)!=-1 || compareDate(curRecToDate,prvRecToDate)!= -1  || compareDate(curRecToDate,curRecFromDate)!=-1 )
							{
							probTable.rows[i].cells[2].childNodes[1].value="";
							alert( "ProbationTo date should be greater than ProbationFrom date" );
							return false;
						}
				}
			}
		}
	}
}

function checkprobation(obj)
{
if(obj.value!="")
{
if(document.pIMSForm.proPostId.value=="0")
{
alert('<bean:message key="alertChoosePost"/>');
obj.focus();
obj.value="";
return false;

}
}
}

function checkRegularisation(obj)
{
if(obj.value!="")
{
if(document.pIMSForm.regPostId.value=="0")
{
alert('<bean:message key="alertChoosePost"/>');
obj.focus();
obj.value="";
return false;

}
}

}

function checkQualificationnumeric(obj){
if(obj.value!=""){
var num=obj.value;
var objRegExp  = /^[A-Za-z0-9-/()&.,_/(\s)/]+$/;
if(!objRegExp.test(num)){
alert('<bean:message key="alertValidQua"/>');
obj.value="";
obj.focus();
}
}
}

function checkQulification(obj)
{
if(obj.value!="")
{
if(document.pIMSForm.qulification.value=="")
{
alert('<bean:message key="alertFillQualification"/>');
obj.focus();
obj.value="";
return false;

}
}

}

function checkMajorsnumeric(obj){
if(obj.value!=""){
var num=obj.value;
var objRegExp  = /^[A-Za-z0-9-/()&.,_/(\s)/]+$/;
if(!objRegExp.test(num)){
alert('<bean:message key="alertNoSpecChar"/>');
obj.value="";
obj.focus();
}
}
}

function checkBoardnumeric(obj){
if(obj.value!=""){
var num=obj.value;
var objRegExp  = /^[A-Za-z0-9-/()&.,_/(\s)/']+$/;
if(!objRegExp.test(num)){
alert('<bean:message key="alertOnlyalpha"/>');
obj.value="";
obj.focus();
}
}
}

function checkSkills(obj)
{
if(obj.value!="")
{
if(document.pIMSForm.skillId.value=="0")
{
alert('<bean:message key="alertChooseSkill"/>');
obj.focus();
obj.value="";
return false;

}
}

}
function ToGrtThenFromDate()
{
	
	if(document.pIMSForm.toDateEnter.value!="" )
		{

		if(compareDate(document.pIMSForm.toDateEnter.value,document.pIMSForm.fromDateEnter.value) == 1||compareDate(document.pIMSForm.toDateEnter.value,document.pIMSForm.fromDateEnter.value)==0)
			{
			alert('<bean:message key="alertToGTFromDt"/>');
			document.pIMSForm.toDateEnter.focus();
			document.pIMSForm.toDateEnter.value="";
			return false;
			}
		
		}
}
function checkMandatoryAssignment()
{

	if(document.pIMSForm.fromDateEnter.value=="" )
		{
		alert('<bean:message key="alertFromDate"/>');
		document.pIMSForm.fromDateEnter.focus();
		return false;
		}
		if(document.pIMSForm.toDateEnter.value=="" )
		{
		alert('<bean:message key="alertFillToDate"/>');
		document.pIMSForm.toDateEnter.focus();
		return false;
		}
		if(document.pIMSForm.toDateEnter.value!="" )
		{

		if(compareDate(document.pIMSForm.toDateEnter.value,document.pIMSForm.fromDateEnter.value) == 1||compareDate(document.pIMSForm.toDateEnter.value,document.pIMSForm.fromDateEnter.value)==0)
			{
			alert('<bean:message key="alertToGTFromDt"/>');
			document.pIMSForm.toDateEnter.focus();
			document.pIMSForm.toDateEnter.value="";
			return false;
			}
		
		}
		if(document.pIMSForm.fundEntry.value=="0" )
		{

		alert('<bean:message key="alertChooseFund"/>');
		document.pIMSForm.fundEntry.focus();
		return false;
		}
		if(document.pIMSForm.functionEntry.value=="0" )
		{

		alert('<bean:message key="alertChooseFunction"/>');
		document.pIMSForm.functionEntry.focus();
		return false;
		}

		if(document.pIMSForm.gradeEntry.value=="0" )
		{

		alert('Please choose the grade');
		document.pIMSForm.gradeEntry.focus();
		return false;
		}


		if(document.pIMSForm.desginationName.value=="" )
		{
		alert('<bean:message key="alertChooseDesg"/>');
		document.pIMSForm.desginationName.focus();
		return false;
		}
		if(document.pIMSForm.positionEntryName.value=="")
		{
		alert('<bean:message key="alertChooseposition"/>');
		document.pIMSForm.positionEntryName.focus();
		return false;
		}
		if(document.pIMSForm.functionaryEntry.value=="0" )
		{
		alert('<bean:message key="alertChoosefuncary"/>');
		document.pIMSForm.functionaryEntry.focus();
		return false;
		}
		if(document.pIMSForm.deptEntry.value=="0" )
		{
		alert('<bean:message key="alertchooseMnDept"/>');
		document.pIMSForm.deptEntry.focus();
		return false;
		}
		//alert("checkhod "+document.getElementById("checkhod").value );
		//alert("deptHodOnTopSelect "+document.getElementById("deptHodOnTopSelect").value);
		
		document.getElementById("deptHodOnTopSelect").options[0].selected=false;
		
		if(document.getElementsByName('checkhod')[0].checked && (document.getElementById("deptHodOnTopSelect").value=="0" || document.getElementById("deptHodOnTopSelect").value=="" ))
		{	
		document.getElementById("deptHodOnTopSelect").options[0].selected=false;	
		alert('Please Select Departments ');
		return false;
		}

		return true;

}
function editAssRow(obj,tableName)
{
	var tbl = document.getElementById(tableName);
	var len = tbl.rows.length;
	var rowNumber=getRow(obj).rowIndex;
	globalSaveRowNumber = rowNumber;
	var index = rowNumber-1;
	var idAssignment =	getControlInBranch(tbl.rows[rowNumber],'assignmentId').value;
	
	if(len>2)
	{
		document.getElementById('fromDateEnter').value=document.pIMSForm.fromDate[index].value;
		document.getElementById('toDateEnter').value=document.pIMSForm.toDate[index].value;
		document.getElementById('functionEntry').value=document.pIMSForm.functionId[index].value;
		document.getElementById('gradeEntry').value=document.pIMSForm.gradeId[index].value;
	
	    document.getElementById('desginationName').value=document.pIMSForm.designationId[index].value;
		document.getElementById('positionEntryName').value=document.pIMSForm.posName[index].value;
		document.getElementById('functionaryEntry').value=document.pIMSForm.functionaryId[index].value;
		document.getElementById('deptEntry').value=document.pIMSForm.mainDepartmentId[index].value;
		document.getElementById('govtEntry').value=document.pIMSForm.assignmentOrderNo[index].value;
		document.getElementById('fundEntry').value=document.pIMSForm.fundId[index].value;
		document.getElementById('desgEnterId').value=document.pIMSForm.desgId[index].value;
		document.getElementById('positionId').value=document.pIMSForm.posId[index].value;
	
		document.getElementById('assignEntered').value=idAssignment;
		document.getElementById('rowIndexOfAssignment').value=index;

		document.getElementById('fromDateForWF').value=document.pIMSForm.fromDate[index].value;
		document.getElementById('toDateForWF').value=document.pIMSForm.toDate[index].value;
		document.getElementById('positionForWF').value=document.pIMSForm.posId[index].value; 
		
		if(document.pIMSForm.isPrimary[index].value=='Yes'){
			document.pIMSForm.checkPrimary[0].checked=true;
		}
		else{
			document.pIMSForm.checkPrimary[1].checked=true;
		}
		if(document.getElementsByName('departmentIdDisplay')[index].style.display=="" || document.getElementsByName('departmentIdDisplay')[index].style.display=="block")
		{
		document.getElementsByName('checkhod')[1].checked=true;
		document.getElementById('deptHodOnTopLbl').style.display="none";
		document.getElementById('deptHodOnTopTd').style.display="none";
		//document.getElementsByName('departmentIdHodSelect')[index].style.display="none";
		}
			else{
			document.getElementsByName('checkhod')[0].checked=true;
			document.getElementById('deptHodOnTopLbl').style.display="";
			document.getElementById('deptHodOnTopTd').style.display="";
			//document.getElementsByName('departmentIdHodSelect')[index].style.display="";
			var tempDeptValue= document.getElementById('deptHodOnTopSelect');
			var departmentIdObj= document.getElementsByName('departmentIdHodSelect')[index];
			var outer=0;
			var inner=0;
			for(outer=0;outer<tempDeptValue.options.length;outer++)
			{
				for(inner=0;inner<departmentIdObj.options.length;inner++)
	              	{
	              		if(tempDeptValue.options[outer].value==departmentIdObj.options[inner].value)
	              		{
	              			tempDeptValue.options[outer].selected=true;
	              		}
	              			
							
	              	}
	        }
		}
    }
	else
	{
		document.getElementById('fromDateEnter').value=document.pIMSForm.fromDate.value;
		document.getElementById('toDateEnter').value=document.pIMSForm.toDate.value;
		document.getElementById('functionEntry').value=document.pIMSForm.functionId.value;
		document.getElementById('gradeEntry').value=document.pIMSForm.gradeId.value;

		document.getElementById('desginationName').value=document.pIMSForm.designationId.value;
		document.getElementById('positionEntryName').value=document.pIMSForm.posName.value;
		document.getElementById('functionaryEntry').value=document.pIMSForm.functionaryId.value;
		document.getElementById('deptEntry').value=document.pIMSForm.mainDepartmentId.value;
		document.getElementById('govtEntry').value=document.pIMSForm.assignmentOrderNo.value;
		document.getElementById('fundEntry').value=document.pIMSForm.fundId.value;
		document.getElementById('desgEnterId').value=document.pIMSForm.desgId.value;
		document.getElementById('positionId').value=document.pIMSForm.posId.value;

		document.getElementById('assignEntered').value=idAssignment;
		document.getElementById('rowIndexOfAssignment').value=index;

		document.getElementById('fromDateForWF').value=document.pIMSForm.fromDate.value;
		document.getElementById('toDateForWF').value=document.pIMSForm.toDate.value;
		document.getElementById('positionForWF').value=document.pIMSForm.posId.value; 
		
		if(document.pIMSForm.isPrimary.value=='Yes'){
			document.pIMSForm.checkPrimary[0].checked=true;
		}
		else{
			document.pIMSForm.checkPrimary[1].checked=true;
		}
		if(document.getElementById('departmentIdDisplay').style.display=="" || document.getElementById('departmentIdDisplay').style.display=="block")
		{
		document.getElementsByName('checkhod')[1].checked=true;
		document.getElementById('deptHodOnTopLbl').style.display="none";
		document.getElementById('deptHodOnTopTd').style.display="none";
		document.getElementsByName('departmentIdHodSelect')[0].style.display="none";
		}
			else{			
			document.getElementsByName('checkhod')[0].checked=true;
			document.getElementById('deptHodOnTopLbl').style.display="";
			document.getElementById('deptHodOnTopTd').style.display="";
			document.getElementsByName('departmentIdHodSelect')[0].style.display="";
			var tempDeptValue= document.getElementById('deptHodOnTopSelect');
			var departmentIdObj= document.getElementById('departmentId');
			var outer=0;
			var inner=0;
			for(outer=0;outer<tempDeptValue.options.length;outer++)
			{
				for(inner=0;inner<departmentIdObj.options.length;inner++)
	              	{
	              		if(tempDeptValue.options[outer].value==departmentIdObj.options[inner].value)
	              		{
	              			tempDeptValue.options[outer].selected=true;
	              		}
	              			
							
	              	}
	        }
		}
	}
   
						
							
							
							

}

function chckprsnt(obj)
{
//alert('In chckprsnt');
if(document.pIMSForm.dateOfFA.value !="")
{
	if(document.pIMSForm.employeeDob.value =="")
	{
		alert('<bean:message key="alertfilldob"/>');
		document.pIMSForm.employeeDob.focus();
		document.pIMSForm.dateOfFA.value = "";
		return false;
	}
}


}
function CompfaDate(obj)
{
	//alert('In CompfaDate');
	if(document.pIMSForm.dateOfFA.value!=="")
	{
		if(compareDate(document.pIMSForm.dateOfFA.value,document.pIMSForm.employeeDob.value) == 1 ||compareDate(document.pIMSForm.dateOfFA.value,document.pIMSForm.employeeDob.value) == 0)
		{
				alert('<bean:message key="alertFstGtBirthDy"/>');
				document.pIMSForm.dateOfFA.focus();
				document.pIMSForm.dateOfFA.value="";
				return false;
		}
	}
}
function checkDateofAppontment(obj)
{
//alert('In checkDateOfAppointment');
if (obj.value != null || obj.value != "")
{
var date = obj.value;
var year = date.substr(6,4);
var appoDate = document.pIMSForm.employeeDob.value;
var appoYear= appoDate.substr(6,4);

var diffYr = eval(year) - (appoYear);

		if(diffYr < 16)
		{

				alert('<bean:message key="alertApp15GtBd"/>');

				obj.focus();
				obj.value="";
				return false;

		}

}
}
function populateJoinDate(obj)
{

<%if (((String) (session.getAttribute("viewMode"))).trim()
					.equalsIgnoreCase("create")
					|| ((String) (session.getAttribute("viewMode"))).trim()
							.equalsIgnoreCase("modify")) {%>
var joinDate=document.getElementById("dateOfjoin").value;
//alert('In populateJoinDate '+joinDate);
if((joinDate=="")|| (joinDate == "dd/mm/yyyy")){
document.getElementById("dateOfjoin").value=obj.value;
document.getElementById("dateOfjoin").style.color='black';
}
<%}%>
}

function setFocus(obj,defaultval)
{
	if(obj.value==defaultval ){
		document.getElementById(obj.id).value="";
		document.getElementById(obj.id).style.color='black';
		}
}
function setBlur(obj,defaultval)
{
	if(obj.value == ""){
		document.getElementById(obj.id).value=defaultval;
		document.getElementById(obj.id).style.color='';
	}
}
function checkRetire(obj)
{
if(document.pIMSForm.dateOfRetirement.value !="")
{
if(document.pIMSForm.dateOfFA.value =="")
{

alert('<bean:message key="alertDoApp"/>');
document.pIMSForm.dateOfFA.focus();
document.pIMSForm.dateOfRetirement.value="";
return false;
}
else
{
if(compareDate(document.pIMSForm.dateOfRetirement.value,document.pIMSForm.dateOfFA.value) == 1||compareDate(document.pIMSForm.dateOfRetirement.value,document.pIMSForm.dateOfFA.value)==0)
{

alert('<bean:message key="alertDoRetrGtAppDate"/>');
document.pIMSForm.dateOfRetirement.focus();
document.pIMSForm.dateOfRetirement.value="";
return false;
}
}
}

}
function populateAge(obj)
{
var retireDate = obj.value;
var dob = document.getElementById("employeeDob").value;
if(dob== null || dob == "")
{
alert("please fill the date of birth of first");
document.pIMSForm.employeeDob.focus();
obj.value = "0";
return false;

}
else
{
var http = initiateRequest();
var url = "<%=request.getContextPath()%>/pims/populateDiffDbRetireDate.jsp?retireDate="+retireDate+"&dob="+dob;
http.open("GET", url, true);
http.onreadystatechange = function()
{
		if (http.readyState == 4)
		{
				if (http.status == 200)
				{
						var statusString =http.responseText.split("^");
						var age = statusString[0];
						document.getElementById("retirementAge").value = age;
						document.getElementById("dateOfRetirement").value = statusString[1];
				}
		}
}
};
http.send(null);

}


function populateRetDate(obj)
{
	var age = obj.value;
	var dateOfBirth =document.getElementById("employeeDob").value;
	if(age!=null && age!="" )
	{
	if(dateOfBirth!=null && dateOfBirth!="")
	{
		if(document.pIMSForm.dateOfFA.value =="")
		{
				alert('<bean:message key="alertDoApp"/>');
				document.pIMSForm.dateOfFA.focus();
				obj.value="";
				return false;
		}
		else
		{
			if(obj.value!=null && obj.value!="")
			{
			var dobYear =dateOfBirth.substr(6,4);
			var sumOfDate = parseInt(dobYear) + parseInt(age);
			document.getElementById("dateOfRetirement").value = dateOfBirth.substr(0,5)+'/'+sumOfDate;
			}
		}
	}
	else
	{
		alert('<bean:message key="alertEnterDob"/>');
		document.pIMSForm.employeeDob.focus();
		obj.value = "";
		return false;
	}
	}

}

function checkRetireAgeDiff(obj)
{
if (obj.value != null || obj.value != "")
{
var date = obj.value;
var year = date.substr(6,4);
var appoDate = document.pIMSForm.employeeDob.value;
var appoYear= appoDate.substr(6,4);

var diffYr = eval(year) - (appoYear);

		if(diffYr < 50)
		{

				alert('Retirement age should not be less than 50');
				obj.focus();
				obj.value="";
				document.getElementById("retirementAge").value="";
				return false;

		}

}
}


function ValidateAssignment()
{
	if(document.pIMSForm.fromDate.value == "" )
		{
			if(document.getElementById("EOTable").style.display!="none")
			{
				alert('Please enter the assignement values');
			}
			else
			{
			alert('<bean:message key="alertAssignmentMandatory"/>');
			}
			return false;
		}
				
			
	
		return true;
}

function clearPosDesigAfterDeptChange(name)
{
	if(document.pIMSForm.checkPrimary[0].checked )
	{
		document.getElementById('desgEnterId').value="0";
		document.getElementById('desginationName').value="";
	
		document.getElementById('positionEntryName').value="";
		document.getElementById('positionId').value="0";
	}
	
}

function checkPrimaryTempCombo()
{
	var tbl = document.getElementById('EOTable');
	var len = tbl.rows.length;
	if(len>2)
	{	
		for(var i=0; i<len-1; i++)
		{ 
			if(document.pIMSForm.fundEntry.value !="0" && 
				document.pIMSForm.functionaryEntry.value !="0"&& 
				document.pIMSForm.desginationName.value !="" && 
				document.pIMSForm.positionEntryName.value !="" &&
				document.pIMSForm.deptEntry.value !="0")
			 {	
					if((document.pIMSForm.fundId[i].value==document.getElementById('fundEntry').value) && 
						(document.pIMSForm.functionId[i].value==document.getElementById('functionEntry').value) && 
						(document.pIMSForm.desgId[i].value==document.getElementById('desgEnterId').value) && 
						(document.pIMSForm.functionaryId[i].value==document.getElementById('functionaryEntry').value) && 
						(document.pIMSForm.posName[i].value==document.getElementById('positionEntryName').value) &&
						(document.pIMSForm.mainDepartmentId[i].value==document.getElementById('deptEntry').value))
					{
						alert('Combination of(Fund,function,Functionary,Designation,Position,Main Dept) of Temprorary and primary should be unique');

						document.getElementById('fundEntry').value="0";
						document.getElementById('functionaryEntry').value="0";
						document.getElementById('deptEntry').value="0";

						document.getElementById('desgEnterId').value="0";
						document.getElementById('desginationName').value="";

						document.getElementById('positionEntryName').value="";
						document.getElementById('positionId').value="0";

						return false;

					}
			}

		}
		
	}
	else
	{
		if(document.pIMSForm.fundEntry.value !="0" && 
			document.pIMSForm.functionaryEntry.value !="0"&& 
			document.pIMSForm.desginationName.value !="" && 
			document.pIMSForm.positionEntryName.value !="" &&
			document.pIMSForm.deptEntry.value !="0")
		{
		    //alert('fund'+document.pIMSForm.fundId.value+'entry'+document.getElementById('fundEntry').value);

			//alert('functionary'+document.pIMSForm.functionaryId.value+'entry'+document.getElementById('functionaryEntry').value);
			//alert('Position'+document.pIMSForm.posName.value+'Position'+document.getElementById('positionEntryName').value);
			//alert('Dept'+document.pIMSForm.mainDepartmentId.value+'entry'+document.getElementById('deptEntry').value);	
			
			if((document.pIMSForm.fundId.value==document.getElementById('fundEntry').value) &&
				(document.pIMSForm.functionId.value==document.getElementById('functionEntry').value) &&
				(document.pIMSForm.desgId.value==document.getElementById('desgId').value) &&
				(document.pIMSForm.functionaryId.value==document.getElementById('functionaryEntry').value) && 
				(document.pIMSForm.posName.value==document.getElementById('positionEntryName').value) &&
				(document.pIMSForm.mainDepartmentId.value==document.getElementById('deptEntry').value))
			  {
				alert('Combination of(Fund,Function,Functionary,Position,Main Dept) of Temprorary and primary should be unique');

				document.getElementById('fundEntry').value="0";
				document.getElementById('functionaryEntry').value="0";
				document.getElementById('deptEntry').value="0";

				document.getElementById('desgEnterId').value="0";
				document.getElementById('desginationName').value="";

				document.getElementById('positionEntryName').value="";
				document.getElementById('positionId').value="0";

				return false;
			}
		}
	}		
}


//This Function returns a Array with Primary Assigned todate
function maxDateFromArray()
{

    var isPrimary="";
    var maxTodate = new Array();
	if(document.pIMSForm.checkPrimary[0].checked){
			isPrimary = 'Yes'; 
		}
		if(document.pIMSForm.checkPrimary[1].checked){
			isPrimary = 'No'; 
		}
		
			if(isPrimary=='Yes')
			{
			  var tbl= document.getElementById("EOTable");
			  var len = tbl.rows.length;
					if(len>2)
					{
						for(var i=0;i<len-1;i++)
						{
							if(document.pIMSForm.isPrimary[i].value=='Yes')
							{
								   if(document.pIMSForm.toDate[i].value!=null)
								   {
								    	maxTodate.push(document.pIMSForm.toDate[i].value);		
								   }
						    }
									
						}
					}
					else
					{
						
						
						if(document.pIMSForm.isPrimary.value=='Yes')
						{
						   if(document.pIMSForm.toDate.value!=null)
						   {
						  		
						  		maxTodate.push(document.pIMSForm.toDate.value);
						  		
						  	}
						}
						
					}
	}
	
	return maxTodate;
}
function checkToDateGrtFromdate(obj)
{      
 		 var maxTodate=maxDateFromArray();
 		 //works for edit 
		 if(globalSaveRowNumber!=undefined  && globalSaveRowNumber>0)
		 {
 		 var currentToDate ;
		   		 var dateStrings = maxTodate; 
		   		 
		  		 var rowNumber=globalSaveRowNumber;
				 var index = rowNumber-1;
				 var tblLen=document.getElementById("EOTable").rows.length;
				 if(tblLen==3)
				 {
				  currentToDate = document.pIMSForm.toDate[index-1].value;
				  }
				 else if(tblLen>3)
				 {
				    currentToDate = document.pIMSForm.toDate[index-1].value;
				 }
				 var index=dateStrings.indexOf(currentToDate);
			  
			   if(index!=-1)
			   {
				   
				   	    
						     var previousRowTodate=dateStrings[index];
						     if(compareDate(document.pIMSForm.fromDateEnter.value,previousRowTodate) == 1||compareDate(document.pIMSForm.fromDateEnter.value,previousRowTodate)==0)
							  {
												alert('From Date should be greater than last Primary Assignment ToDate ->'+previousRowTodate);
												document.pIMSForm.fromDateEnter.value="";
												return false;
							   }
						   
					  
				
		       }
		      
		 }
		 //works for add save/modify
		 else
		 {
            
            var maxDateString = getMax(maxTodate);
			if(maxDateString!=undefined)
  			{
  			     if(document.pIMSForm.fromDateEnter.value!="")
  			     {
			  			if(compareDate(document.pIMSForm.fromDateEnter.value,maxDateString) == 1||compareDate(document.pIMSForm.fromDateEnter.value,maxDateString)==0)
							{
								alert('From Date should be greater than last Primary Assignment ToDate ->'+maxDateString);
								document.pIMSForm.fromDateEnter.value="";
								return false;
							}
				}
			}
  		
  		}
  		 
  		
  		
 
    
}

function checkDiffFromtoAssignement(obj)
{        
   			 var maxTodate=maxDateFromArray();
   			 //works for edit 
		 if((globalSaveRowNumber!=undefined || globalSaveRowNumber!='') && globalSaveRowNumber>0)
		 {
		      var dateStrings = maxTodate; 
  			  var rowNumber=globalSaveRowNumber;
		      var index = rowNumber-1;
		       var currentToDate;
		  		var tblLen=document.getElementById("EOTable").rows.length;
		  		if(tblLen==2)
				 {
				  currentToDate = document.pIMSForm.toDate.value;
				  }
				 else if(tblLen>2)
				 {
				 currentToDate = document.pIMSForm.toDate[index].value;
				 }
				 
	          var index=dateStrings.indexOf(currentToDate);
	   
				   if(index!=-1)
				   {
					   if(index!=0)
					   	  {
						     var previousRowTodate=dateStrings[index-1];
						     var toDate=getValidDateObject(previousRowTodate);
						     var fromDate = getValidDateObject(obj.value);
									      var difference = fromDate.getTime() - toDate.getTime();
										  var daysDifference = Math.abs(difference/1000/60/60/24);
									       if(daysDifference>1)
										   {
										      alert('There is a gap between two Assignments.Please Verify');
										   }
						  }
					
			       }
			       
		 }
		 //works for add save/modify
   			else
   			 {
			     var maxDateString = getMax(maxTodate);
			     if(maxDateString!=undefined)
			  		{
						      var toDate=getValidDateObject(maxDateString);
						      var fromDate = getValidDateObject(obj.value);
						      var difference = fromDate.getTime() - toDate.getTime();
							  var daysDifference = Math.abs(difference/1000/60/60/24);
						       if(daysDifference>1)
							   {
							      alert('There is a gap between two Assignments.Please Verify');
							   }
			       }
		    
		      }
		     
		    
}

function mandatoryDeathFiled(obj)
{
	var selectedStatus = document.getElementById("statusTypeId").options[obj.selectedIndex].text;
	if(selectedStatus.toUpperCase()=='DECEASED')
	{
		
		document.getElementById("death").style.display="";
		document.getElementById("deathDate").style.display="";
	}
	else
	{
		document.getElementById("death").style.display="none";
		//document.getElementById("death").style.display="none";
		document.getElementById("deathDate").value="";
	}
}

function showDeceased()
{
	var isDeceased = document.getElementById("statusTypeId").options[document.getElementById("statusTypeId").selectedIndex].text;
	if(isDeceased.toUpperCase()=='DECEASED')
	{
		document.getElementById("death").style.display="";
		document.pIMSForm.deathDate.style.display="";
	}
	else{
		document.getElementById("death").style.display="none";
		document.pIMSForm.deathDate.style.display="none";
	}
			
}
function loadDesg()
{

   document.getElementById("desgEnterId").value="";
   document.getElementById("desginationName").value="";
   document.getElementById("desginationName").focus();
   
   document.getElementById("positionId").value="";
   document.getElementById("positionEntryName").value="";
   
}


//  copy the values hod dept to grid dept
 function getMultipleDepts(ob,tblLen) 
 {
         	var departmentList=new Array();
              	var i,j=0;
              	var deptValue;
              	var tempDeptValue= document.getElementById('deptHodOnTopSelect');
              	var eoTblLen=document.getElementById('EOTable').rows.length;
              	if(tblLen==0 && eoTblLen==2)
              	{
              	 	deptValue= document.getElementById('departmentId');
              	}
              	else if((tblLen==0 && eoTblLen>2) || (tblLen>0))
              	 {
              		 deptValue= document.getElementsByName('departmentIdHodSelect')[(tblLen)];
              	 }
                  deptValue.options.length=0;   
              	for(i=0;i<tempDeptValue.options.length;i++)
              	{
              		if(tempDeptValue.options[i].selected) 
              		{
              			deptValue.options[j]= new Option(tempDeptValue.options[i].text,tempDeptValue.options[i].value);
              			deptValue.options[j].selected=true;
              			tempDeptValue.options[i].selected=false;
              			j++;
						
					}
              	}
 }
 //while adding AssignmentDetails as a row in the grid ,based onthe hod it will show/hide department
function showHodValsOnDemand(rowO)
{
		var eoTblLen=document.getElementById('EOTable').rows.length;
		if(document.getElementsByName('checkhod')[0].checked)
		{		
			if(rowO==0 && eoTblLen==2)
			{
			document.getElementById("depId0").style.display="";
			document.getElementsByName("departmentIdHodSelect")[0].style.display="";
			document.getElementById('departmentIdDisplay').style.display="none";		
			}
			else if((rowO==0 && eoTblLen>2) || (rowO>0))
			{
				document.getElementsByName("depId0")[rowO].style.display="";
				document.getElementsByName("departmentIdHodSelect")[rowO].style.display="";
				document.getElementsByName('departmentIdDisplay')[rowO].style.display="none";	
			}	
					
			getMultipleDepts(document.getElementById('deptHodOnTopSelect'),(rowO));
		}
		else
		{
			if(rowO==0 && eoTblLen==2)
			{
			document.getElementById("depId0").style.display="none";
			document.getElementsByName("departmentIdHodSelect")[0].style.display="none";
			document.getElementById('departmentIdDisplay').style.display="";
			document.getElementsByName("departmentIdHodSelect")[0].options.length=0;		
			}
			else if((rowO==0 && eoTblLen>2) || (rowO>0))
			{
				document.getElementsByName("depId0")[rowO].style.display="none";
				document.getElementsByName("departmentIdHodSelect")[rowO].style.display="none";
				document.getElementsByName('departmentIdDisplay')[rowO].style.display="";
				document.getElementsByName("departmentIdHodSelect")[rowO].options.length=0;	
			}			
			
		}
		
		
		
}
//to copy all the dept vals to the respective hidden field,action class accessing only the hidden field,
//since no support for multiselect in multirows doing this
function copyDeptValstoHidden()
{
var hodHidden="";  	
var tempDeptValue; 
var tblLen= document.getElementById("EOTable").rows.length; 
				for(var i=0;i<tblLen-1;i++)
				{
					tempDeptValue=document.getElementsByName("departmentIdHodSelect")[i];
					hodHidden="";
	              	for(var j=0;j<tempDeptValue.options.length;j++)
	              	{	              		
	              			hodHidden=hodHidden+tempDeptValue.options[j].value+"#";				
						
	              	}
	              	document.getElementsByName('departmentIdOfHod')[i].value=hodHidden;
              	}
}
//This Function a array of temprorary dates.
function getTempToDate()
{

    var isPrimary="";
    var toDateArray = new Array();
	var tbl= document.getElementById("EOTable");
			  var len = tbl.rows.length;
					if(len>2)
					{
						for(var i=0;i<len-1;i++)
						{
							if(document.pIMSForm.isPrimary[i].value=='No')
							{
								   if(document.pIMSForm.toDate[i].value!=null)
								   {
								        toDateArray.push(document.pIMSForm.toDate[i].value);		
								   }
						    }
									
						}
					}
					else
					{
						
						
						if(document.pIMSForm.isPrimary.value=='No')
						{
						   if(document.pIMSForm.toDate.value!=null)
						   {
						  		
						  		toDateArray.push(document.pIMSForm.toDate.value);
						  		
						  	}
						}
						
					}
	
	
	return toDateArray;
}

//This Function a array of primary dates.
function getPrimaryToDate()
{

    var isPrimary="";
    var toDateArray = new Array();
	var tbl= document.getElementById("EOTable");
			  var len = tbl.rows.length;
			   if(len>2)
					{
						for(var i=0;i<len-1;i++)
						{
							if(document.pIMSForm.isPrimary[i].value=='Yes')
							{
								   if(document.pIMSForm.toDate[i].value!=null)
								   {
								        toDateArray.push(document.pIMSForm.toDate[i].value);		
								   }
						    }
									
						}
					}
					else
					{
						
						
						if(document.pIMSForm.isPrimary.value=='Yes')
						{
						   if(document.pIMSForm.toDate.value!=null)
						   {
						  		
						  		toDateArray.push(document.pIMSForm.toDate.value);
						  		
						  	}
						}
						
					}
	
	
	return toDateArray;
}

function compareDateRangeFromGrtTodate()
{
   var toDateTempArray= getTempToDate();
    var toDatePriArray=getPrimaryToDate();
    
   if(toDateTempArray!=null && toDateTempArray!=undefined && toDateTempArray!='' && toDatePriArray!=null && toDatePriArray!=undefined && toDatePriArray!='')
   {
  
	   var lenFromDate = toDatePriArray.length;
	   var lenthTodate = toDateTempArray.length;
	   
	  
	   toDateTempArray.sort();
	   toDateTempArray.reverse();
	   
	   toDatePriArray.sort();
	   toDatePriArray.reverse();
	   if(compareDate(toDatePriArray[0],toDateTempArray[0]) == 1)
		{
			alert('Temporary date Should be within the Primary date range');
			return false;
		}
			
	 }  
   return true;
   
   
}

	function SearchUser()
	{
		window.open("<%=request.getContextPath() + "/pims/loadUserEmpMap.jsp"%>","mywindow","height=300,width=600px,scrollbars=yes,left=30,top=30,status=yes");
	}
	
	function readEmpActive(obj)
	{
		if(obj.checked)
		{
			document.getElementById("isEmployeeActive").value=true;
			document.getElementById("isEmployeeActive").checked=true;
        } 
		else 
		{
			document.getElementById("isEmployeeActive").value=false;
			document.getElementById("isEmployeeActive").checked=false;
        }    
	}			 
        
	

function makeReadOnlyAssignmentTable()
{
	var EOTable= document.getElementById("EOTable");
	var rows= EOTable.rows.length;
	if(rows>2)
	{
		for(var i=0 ;i<rows-1;i++)
		{
			document.pIMSForm.fromDate[i].disabled= true;
			document.pIMSForm.toDate[i].disabled= true;
			document.pIMSForm.fundId[i].disabled= true;
			document.pIMSForm.functionId[i].disabled= true;
			document.pIMSForm.designationId[i].disabled= true;
			document.pIMSForm.posName[i].disabled= true;
			document.pIMSForm.functionaryId[i].disabled= true;
			document.pIMSForm.departmentId[i].disabled= true;
		
			document.pIMSForm.mainDepartmentId[i].disabled= true;
			document.pIMSForm.assignmentOrderNo[i].disabled= true;
			document.pIMSForm.gradeId[i].disabled= true;
		
		}
	}
	else
	{
		document.getElementById("fromDate").disabled= true;
		document.getElementById("toDate").disabled= true;
		document.getElementById("fundId").disabled= true;
		document.getElementById("functionId").disabled= true;
		document.getElementById("designationId").disabled= true;
		document.getElementById("posName").disabled= true;
		document.getElementById("functionaryId").disabled= true;
		document.getElementById("departmentId").disabled= true;
	
		document.getElementById("mainDepartmentId").disabled= true;
		document.getElementById("assignmentOrderNo").disabled= true;	
		document.getElementById("gradeId").disabled= true;
	}
}
function populateGrades(){
	 var skillId=document.getElementById("skillId").value;
   	  populategradeId1({skillId:skillId});
}
function validatePayscale(obj){
	if(obj.value!=""){
		var num=obj.value;
		var regExp  = /^[0-9-_]+$/;
		if(!regExp.test(num)){
		alert('Please enter valid payscale');
		obj.value="";
		obj.focus();
		}
	}
}

function getRetirementAgeAndDate(){

	var gradeId=document.getElementById("gradeEntry").value;
	var empId=document.getElementById("employeeCode").value;
	var fromDate=document.getElementById('fromDateEnter').value;
	var toDate=document.getElementById('toDateEnter').value;
	var retAgeObj=document.getElementById('retirementAge');
	if (document.pIMSForm.checkPrimary[0].checked)		
		primary =  'Y';
			else
				primary =  'N';
	
    if( primary == 'Y' && (validateDate(fromDate))  && !(validateDate(toDate))){
        
	var url = "<%=request.getContextPath()%>"+"/pims/getRetirementAgeandDateAjax.jsp?gradeId="+gradeId+"&empId="+empId;
	var req2 = initiateRequest();
	
	req2.onreadystatechange = function() {
		if (req2.readyState == 4) {
			if (req2.status == 200) {
				var grades = req2.responseText;
				var a = grades.split("+");
				var age = a[0];
				retAgeObj.value=age;
				retAgeObj.value.focus();
				
			}
		}
	};
	
	req2.open("GET", url, true);
	req2.send(null);
	setTimeout(function(){populateRetDate(retAgeObj)}, 150);
	
 }         
    
    
}


</script>   



</body>

</html>
