<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<!-- <%@ page buffer = "16kb" %> -->
<%@page  import="java.util.Date,java.text.SimpleDateFormat,org.egov.infstr.utils.EGovConfig,java.io.*,com.exilant.eGov.src.reports.GeneralLedgerReport,com.exilant.eGov.src.reports.*,com.exilant.eGov.src.transactions.*,java.io.*,java.util.*,java.sql.*,javax.sql.*,javax.naming.InitialContext,com.exilant.GLEngine.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html>
<head>
<title>Sub-Ledger Report</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">
<META http-equiv=pragma content=no-cache> 
<!-- Inclusion of the CSS files that contains the styles -->
<link rel=stylesheet href="../css/egov.css" type="text/css" media="screen" />
<link rel=stylesheet href="../css/print.css" type="text/css" media="print" /> 
<!--
<link rel=stylesheet href="../exility/global.css" type="text/css">
-->
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/CookieManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js"></SCRIPT>
<script language="javascript" src="../script/jsCommonMethods.js"></script>
<SCRIPT type="text/javascript" src="../script/calendar.js" type="text/javascript" ></SCRIPT>
 <script type="text/javascript" src="../commonyui/build/yahoo/yahoo.js"></script>
<script type="text/javascript" src="../commonyui/build/dom/dom.js" ></script>
<script type="text/javascript" src="../commonyui/build/autocomplete/autocomplete-debug.js"></script>
<script type="text/javascript" src="../commonyui/build/event/event-debug.js"></script>
<script type="text/javascript" src="../commonyui/build/animation/animation.js"></script>
<link type="text/css" rel="stylesheet" href="/commonyui/build/reset/reset.css">
<link type="text/css" rel="stylesheet" href="/commonyui/build/fonts/fonts.css">
<link type="text/css" rel="stylesheet" href="/commonyui/examples/autocomplete/css/examples.css">

<style type="text/css">
#codescontainer {position:absolute;left:11em;width:9%}
#codescontainer .yui-ac-content {position:absolute;width:100%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:60%;background:#a0a0a0;z-index:9049;}
#codescontainer ul {padding:5px 0;width:100%;}
#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
#codescontainer li.yui-ac-highlight {background:#ff0;}
#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
</style>

<!--
<link rel="stylesheet" href="../exility/screen.css" type="text/css" media="screen, print" />
-->

<SCRIPT LANGUAGE="javascript">
<%
 	SimpleDateFormat sdf = new  SimpleDateFormat("dd/MM/yyyy");
	Date crntDate = new Date();
	String currDate = sdf.format(crntDate);
%>
var path="${pageContext.request.contextPath}";
var index=null;
var flag='0', tbNames, tbIds;
var rptType;
var accEntText='';
var rptTitle='', rptStartDate='', rptEndDate='', rptGLCode='', rptAccName='', detail='';
var fuId="";
var fuSrId="";
var sDate="";
var eDate="";
var gl="";
var gl2="";
var accEntityKey="";
var reportType="";
var accEntityId="";
var coaId="";
var entityDetailArray;           
var codeObj;
function onBodyLoad()
{
	PageValidator.addCalendars();	
	PageManager.ListService.callListService();
	PageManager.DataService.callDataService('cgNumber');
	var accCode=document.getElementById('glCode1');	
	var beanVal= <%=request.getParameter("fromBean")%>;
	if(beanVal==2)
	{
		document.getElementById("row3").style.display="block";
		document.getElementById("row2").style.display="none";		
	}		
	var drillDownFromSchedule=<%=request.getParameter("drillDownFromSchedule")%>;
	if( drillDownFromSchedule == true)
	{
		document.getElementById('tbl-header1').style.display="none";
		document.getElementById('fund_id').style.display="none";
		//document.getElementById('fundSource_id').style.display="none";
	}
	var type='getSubLedgerScheduleCodes';
    var url = "../commons/Process.jsp?type="+type;
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
					acccodeArray=codes.split("+");
					codeObj = new YAHOO.widget.DS_JSArray(acccodeArray);
                }
            }
      };
      req2.open("GET", url, true);
      req2.send(null);	    
}

function ButtonPress()
{
	if (!PageValidator.validateForm())
		return;	
	var accEntity = document.getElementById('accEntityList');
	var accCode = document.getElementById('glCode1');
	sDate=document.getElementById("startDate").value;
	eDate=document.getElementById("endDate").value;	
	var accEntityText = document.getElementById('accEntityList');
	if(document.getElementById('accEntityId').value!='')
	{
		document.forms[0].accEntityText.value=accEntityText.value;
	} 	
	if(accCode.value.length==0)
	{
		alert("please select Account Code");
		return;
	}
	if(sDate.length==0||eDate.length==0)
	{
		alert("please select start dates and end dates");
		return;
	}
	if(document.getElementById('accEntityKey').value=='')
	{
		alert('please select a valid Accounting Entity');
		return;
	}  	
		document.getElementById('fromBean').value = 1;
		document.getElementById('glCode2').value=document.getElementById('glCode1').value;
		var fuObj=document.getElementById('fund_id');
		fuId=fuObj.options[fuObj.selectedIndex].value;
		//var fuSrObj=document.getElementById('fundSource_id');
		//if(fuSrObj.value !='')
		//	fuSrId=fuSrObj.options[fuSrObj.selectedIndex].value;	
		reportType=document.getElementById('reportType').value;			
		accEntityId=document.getElementById('accEntityId').value;		
		var accEntityObj=document.getElementById('accEntityList');
		document.getElementById("accEntityId").value=document.getElementById("subLedgerList").value;
		
		/*to check whether start date is greater than the end date*/
		if( compareDate(formatDate6(sDate),formatDate6(eDate)) == -1 )
		{
			alert('Start Date cannot be greater than End Date');
			document.getElementById('startDate').value='';
			document.getElementById('endDate').value='';
			document.getElementById('startDate').focus();
			return false;
		}
		gl=document.getElementById("glCode1").value;
		gl2=document.getElementById("glCode2").value;

		if(rptType == "sl")	
		{
			var accEntObj=document.getElementById('accEntityList');
			getDescription(accEntObj);
			accEntText=document.getElementById('accEntityText').value;
		}			
		document.getElementById('chartOfAccounts_id').value=coaId;

		 /* checking Fiscal year start Date with End Date of Fiscal year */	
		var tMonth=eDate.substr(eDate.length-7,2);
		if(tMonth<4)
		var fiscalYearStartDate="01/04/"+(eDate.substr(eDate.length-4,4)-1);
		else
		var fiscalYearStartDate="01/04/"+eDate.substr(eDate.length-4,4);
		if(compareDate(fiscalYearStartDate,sDate) == -1 )
		{ 
		   alert("Start Date and End Date should be in same financial year");
		   document.getElementById('startDate').focus();
		   return;
		}
		 /*to check whether the End Date is greater than the Current Date*/
		if( compareDate(formatDate6("<%=currDate%>"),formatDate6(eDate)) == 1 )
		{
			alert('End Date cannot be greater than Current Date');
			document.getElementById('endDate').value='';
			document.getElementById('endDate').value='';
			return;
		}
		
		document.forms[0].submit();		
} 
function openSearch(obj, index)
{	var a = new Array(2);
	var screenName="report";
	var str="";
	document.getElementById("glCode1").value="";
	if (obj.toLowerCase() == 'relation')
			str = "../HTML/Search.html?tableNameForCode="+obj;
		else if(obj.toLowerCase() == 'chartofaccounts')
		{			
			str = "../HTML/Search.html?tableNameForCode=chartofaccounts_controlcodes";					
		}
	
	var sRtn = showModalDialog(str,"","dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=400pt;status=no;");
			PageManager.DataService.setQueryField('index', '0');
    if(index==1)
    {
    	a = sRtn.split("`~`");
		var type = document.getElementById('glCode1');
		type.value = a[0]; 	
		rptGLCode = a[0];
		rptAccName = a[1];
		
		document.getElementById('chartOfAccounts_id').value = a[2];		
	}
	
	coaId=document.getElementById('chartOfAccounts_id').value;
	if(sRtn!='')
		loadSubLedgerTypeList();	
	type.value = a[0];	
}

function openEntitySearch()
{	
	var detailtypeid = document.getElementById("subLedgerList").value;
	if( detailtypeid != null && detailtypeid != 0) {
		var	url = "../voucher/common!searchEntites.action?accountDetailType="+detailtypeid;
		window.open(url, 'EntitySearch','resizable=no,scrollbars=yes,left=300,top=40, width=400, height=500');
	} else {
		alert("Select the Type.");
	}
}
function popupCallback(arg0, srchType) {
	var entity_array = arg0.split("^#");
	if(srchType == 'EntitySearch' ) {
		if(entity_array.length==3)
		{
			document.getElementById('accEntityList').value=entity_array[0];
			document.getElementById('accEntityKey').value=entity_array[2];
			document.getElementById('entityName').value=entity_array[1];
		}
		else
		{
			alert("Invalid entity selected.");
			document.getElementById('accEntityList').value="";
			document.getElementById('accEntityKey').value="";
			document.getElementById('entityName').value="";
		}
	}
}
 
function loadEntityList()
{			
	var glCodeId = document.getElementById('chartOfAccounts_id').value;	
	glcode=document.getElementById('glCode1').value;	
	if(glCodeId != '')
	{
		PageManager.DataService.setQueryField('glcode', glcode);		
		PageManager.DataService.setQueryField('glCodeId', glCodeId);				
		PageManager.DataService.callDataService('accEntityList');				
	}	
}

function loadSubLedgerTypeList()
{	
	var glCodeId = document.getElementById('chartOfAccounts_id').value;	
	glcode=document.getElementById('glCode1').value;	
	if(glCodeId != '')
	{
		PageManager.DataService.setQueryField('glCode', glcode);		
		PageManager.DataService.callDataService('subLedgerList');				
	}	
}


function callEntity() {
	var detailtypeid = document.getElementById("subLedgerList").value;
	if( detailtypeid != null && detailtypeid != 0) {
		var	url = "../voucher/common!searchEntites.action?accountDetailType="+detailtypeid;
		window.open(url, 'EntitySearch','resizable=no,scrollbars=yes,left=300,top=40, width=400, height=500');
	} else {
		alert("Select the Type.");
	}
}    
function autocompletecode(obj,eventObj)
{
	// set position of dropdown
	var src = obj;
	var target = document.getElementById('codescontainer');
	var posSrc=findPos(src);
	target.style.left=posSrc[0]-30;
	target.style.top=posSrc[1]+15;
	target.style.width=600;
	if(obj.name=='accEntityList') target.style.left=posSrc[0]-200;	
	var currRow=PageManager.DataService.getRow(obj);
	var coaCodeObj = obj;
	if(eventObj.keyCode != 40 )
	{
		if(eventObj.keyCode != 38 )
		{
	
			var oAutoComp = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainer', codeObj);
			oAutoComp.queryDelay = 0;
			oAutoComp.prehighlightClassName = "yui-ac-prehighlight";
			oAutoComp.useShadow = true;
		}
	}	
}

//fills the related neighboor object after splitting
function fillNeibrAfterSplit(obj,neibrObjName)
{  
	var currRow=PageManager.DataService.getRow (obj);
 	neibrObj=PageManager.DataService.getControlInBranch(currRow,neibrObjName);
 	var temp = obj.value; 
 	temp = temp.split("`-`");
 	if(obj.value==null || obj.value=="") { neibrObj.value= ""; return; }
 	if(temp[1]==null && (neibrObj.value!='' || neibrObj.value!=null) ) {  return ;}
 	else {
   		var temp1=temp[1].split("-$-");
   		obj.value=temp[0];
   		neibrObj.value = temp1[1];
		//document.getElementById("entityName").value=temp1[0];
   	}
   	loadSubLedgerTypeList();
}
function beforeRefreshPage(dc)
{
	var accName = dc.values['accName'];
	if(accName != null && accName != '')
		rptAccName = dc.values['accName'];	
	if(dc.values['glCode1']!=dc.values['glCode'])
	{	
		dc.values['accEntityList']='';
		dc.values['accEntityKey']='';
		dc.values['entityName']='';
	}
	var comboSubledgerList = document.getElementById('subLedgerList'); 			
	clearCombo(comboSubledgerList);	
	if(dc.values['glCode'])	
		dc.values['glCode1']=dc.values['glCode'];	
	
}

function afterRefreshPage(dc)
{
	coaId=dc.values['glCodeId'];
	if(dc.values['serviceID']=='cgNumber')
	{ 	
		loadSubLedgerTypeList();
	}

	if(dc.values['serviceID']=='getAccCOAID')
	{			
			var glCodeId = document.getElementById('chartOfAccounts_id').value;
			var glCode= document.getElementById('glCode1').value;
			if(glCodeId != '')
			{
				clearCombo(document.getElementById('subLedgerList'));
				PageManager.DataService.setQueryField('glCode', glCode);
				PageManager.DataService.callDataService('subLedgerList')
			}
	}
	if(dc.values['reportCodeFailed'])
	{
		alert(dc.values['reportCodeFailed']);
	}
	PageManager.DataService.removeQueryField('startDate');
	PageManager.DataService.removeQueryField('endDate');
	rptAccName = dc.values['accName'];
	
	/* to fill the SubLedgerList Combo*/
	if(dc.values['serviceID']=='subLedgerList')
	{
		if(coaId && coaId != '')
			document.getElementById('chartOfAccounts_id').value=coaId;
		var subLedgerList = dc.grids['subLedgerList'];
		if(subLedgerList != null)
		{
			document.getElementById('accEntityId').value = dc.values['accountDetailTypeId'];
			var comboSubledgerList = document.getElementById('subLedgerList'); 			
			clearCombo(comboSubledgerList);
			var option;
			for(var i=0; i<subLedgerList.length; i++)
			{			
				option = document.createElement('OPTION');
				option.value=subLedgerList [i][0];
				option.text = subLedgerList [i][1];
				
				comboSubledgerList.add(option);			
			} 
			//callEntity();
			
		 }
	}
}
 
function clearCombo(obj)
{
	try{
		for(var i=obj.length-1;i>=0;i--)				
			obj.remove(i);			
	}catch(err) {}
		
}
function getAccountName(obj)
{
	var subLedgerType = document.getElementById('subLedgerList');
	clearCombo(subLedgerType);
	if(obj.value != '')
	{
		PageManager.DataService.setQueryField('glCode', obj.value);
		PageManager.DataService.callDataService('getAccName');
		PageManager.DataService.callDataService('getAccCOAID');
		loadSubLedgerTypeList();
	}
	else
		document.getElementById("chartOfAccounts_id").value='';
}
function getDescription(obj)
{
	coaId=document.getElementById('chartOfAccounts_id').value;

	document.getElementById("index").value=obj.selectedIndex;
	if(document.getElementById('accEntityId').value!='')
	{
		document.forms[0].accEntityKey.value=obj.options[obj.selectedIndex].value;
	}
}

function buttonFlush()
{	
	window.location="SubLedgerReport.jsp?reportType=sl";
}

function pageSetup()
{
	document.body.leftMargin=0.75;
	document.body.rightMargin=0.75;
	document.body.topMargin=0.75;
	document.body.bottomMargin=0.75;	
}


//For Print Preview method
function buttonPrintPreview()
{
	document.getElementById('accEntityId').value=document.getElementById('subLedgerList').value;	
	document.getElementById('fromBean').value ="2";	   
	document.forms[0].submit();	
}

//For print method
function buttonPrint()
{
	document.getElementById('accEntityId').value=document.getElementById('subLedgerList').value;
	document.getElementById('fromBean').value ="2";
    var hide1,hide2; 
    hide1 = document.getElementById("tbl-header1");
	hide1.style.display = "none";	
	document.forms[0].submit();
  	if(window.print)
  	{ 
  		window.print();
	} 
}
var detailTypeId=0;
function onFocusDetailCode(){
	var detailtypeidObj=document.getElementById("subLedgerList");
	if(detailTypeId != detailtypeidObj.value){
		detailTypeId = detailtypeidObj.value;
		loadDropDownCodesForEntities(detailtypeidObj); 
	}
}
var entities;
var oAutoCompEntity;
function loadDropDownCodesForEntities(obj)
{
	if(entities)
	{
		entities=null;
		if(oAutoCompEntity)
			oAutoCompEntity.destroy();
	}	
	var	url = path+"/voucher/common!ajaxLoadEntites.action?accountDetailType="+obj.value;
	var req2 = initiateRequest();
	req2.onreadystatechange = function()
	{
	  if (req2.readyState == 4)
	  {
		  if (req2.status == 200)
		  {
			var entity=req2.responseText;
			var a = entity.split("^");
			var eachEntity = a[0];
			entitiesArray=eachEntity.split("+");
			entities = new YAHOO.widget.DS_JSArray(entitiesArray);
		  }
	  }
 	};
	req2.open("GET", url, true);
	req2.send(null);
}
function splitEntitiesDetailCode(obj)
{
	var entity=obj.value;
	if(entity.trim()!="")
	{
		var entity_array=entity.split("`~`");
		if(entity_array.length==2)
		{
			document.getElementById('accEntityList').value=entity_array[0].split("`-`")[0];
			document.getElementById('accEntityKey').value=entity_array[1];
			document.getElementById('entityName').value=entity_array[0].split("`-`")[1];
		}
	}
}
String.prototype.trim = function () {
    return this.replace(/^\s*/, "").replace(/\s*$/, "");
}
function autocompleteEntities1(obj,myEvent)
{
	//alert('autocomplete');
	var src = obj;	
	var target = document.getElementById('codescontainer');	
	var posSrc=findPos(src); 
	
	target.style.left=posSrc[0]-220;	
	target.style.top=posSrc[1];
	target.style.width=470;	
	      		
	
	var coaCodeObj=obj;
		var key = window.event ? window.event.keyCode : myEvent.charCode;  
		if(key != 40 )
		{
			if(key != 38 )
			{
				oAutoCompEntity = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainer', entities);
				oAutoCompEntity.queryDelay = 0;
				oAutoCompEntity.prehighlightClassName = "yui-ac-prehighlight";
				oAutoCompEntity.useShadow = true;
				oAutoCompEntity.maxResultsDisplayed = 15;
				oAutoCompEntity.useIFrame = true;
				if(entities)
				{
					entities.applyLocalFilter = true;
					entities.queryMatchContains = true;
				}
				oAutoCompEntity.minQueryLength = 0;
				oAutoCompEntity.formatResult = function(oResultData, sQuery, sResultMatch) {
					var data = oResultData.toString();
				    return data.split("`~`")[0];
				};
			}
		}
}

</SCRIPT>
</head>

<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onLoad="onBodyLoad()">
<jsp:useBean id = "glReportBean" scope ="request" class="com.exilant.eGov.src.reports.GeneralLedgerReportBean"/>
<jsp:setProperty name = "glReportBean" property="*"/>  
<form name="subLedger" action="SubLedgerReport.jsp?reportType=sl&fund_id="+fuId+"&glcode="+gl+"&glCode2="+gl2+"&startDate="+sDate+"&endDate="+eDate+ "&reportType="+reportType+ "&accEntityId="+accEntityId+ "&accEntityKey=" +accEntityKey method = "post">
<input type="hidden" name="fromBean" id="fromBean" value="0"> 
<input type="hidden" name="index" id="index" value="null"> 

<input type="hidden" name="databaseDate" id="databaseDate" value="">
<input type=hidden id=reportType name=reportType value="sl">
<input type="hidden" name="glCode2" id="glCode2" value=""> 

<div class="tbl-header1" id="tbl-header1">
<center>
<br>
<table align='center' id="table2">
<tr><td>
<div id="main"><div id="m2"><div id="m3" style="width:810px">


	<table width="100%" border=0 cellpadding="3" cellspacing="0"  id="mainTable">
	<!-- row-0 -->
		<tr>
			<td class="tableheader" valign="center" width="100%" colspan="4"><span id="screenName">Books of Accounts-SubLedgerReport</span><span id="partyName" class="headerwhite2"></span></td>
		</tr>
		
		<!-- row-1 -->
		<tr>
			<td align="right"><div  valign="center" class="labelcell" >Account Code<span class="leadon">* &nbsp;</span></div></td>
			<td class="smallfieldcell">
				<input type="hidden" name="chartOfAccounts_id" id="chartOfAccounts_id" value="">
				<input class="fieldinput" name="glCode1" id="glCode1" exilMustEnter="true" autocomplete="off" onkeyup="autocompletecode(this,event);" onblur="fillNeibrAfterSplit(this,'chartOfAccounts_id')" ><IMG id=IMG1 onclick="openSearch('chartofaccounts',1)" height=22 src="../images/plus1.gif" width=25 align=top border=0>
			</td>
			<td align="right"><div  valign="center" class="labelcell">Fund<span class="leadon">* &nbsp;</span></div></td>
			<td align="left" class="smallfieldcell"> 
				<SELECT class="fieldinput" id=fund_id name="fund_id" exilListSource="fundNameList" exilMustEnter="true"></SELECT>
			</td>
		</tr>
		<tr>
			<td align="right"><div  valign="center" class="labelcell">Starting 
				Date<span class="leadon">* &nbsp;</span></div></td>
			<td align="left" class="smallfieldcell">
				<input name="startDate" id="startDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')" exilDataType="exilAnyDate" exilCalendar="true" exilMustEnter="true">
			</td>
			<td align="right"> <div  valign="center" class="labelcell" >End Date<span class="leadon">* &nbsp;</span></div></td>
			<td class="smallfieldcell">
				<input name="endDate" id="endDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')" exilDataType="exilAnyDate" exilCalendar="true" exilMustEnter="true">
			</td>
		</tr>
		
		<!-- row-4 -->
		<tr>
			<td  align=right><div class="labelcell">Sub Ledger Type<span class="leadon">* &nbsp;</span></td></div>
			<td align="left" class="smallfieldcell"><input type="hidden" name="accEntityId" id="accEntityId">
			
			 <SELECT class="fieldinput" name="subLedgerList" id="subLedgerList" exilMustEnter="true" onChange="getDescription(this)" ></SELECT>
		
			</td>


			<td  align=right><div class="labelcell"> Entity Detail<span class="leadon">* &nbsp;</span></td></div>
			<td class="smallfieldcell">
			<input class="fieldinput" type="hidden" name="accEntityText" id="accEntityText"> 
			<input class="fieldinput" type="hidden" name="accEntityKey" id="accEntityKey">			
			<input class="fieldinput" name="accEntityList" id="accEntityList" exilMustEnter="true" autocomplete="off" onblur="splitEntitiesDetailCode(this)" onfocus="onFocusDetailCode();" onkeyup="autocompleteEntities1(this,event)"/>
			<IMG id=IMG1 onclick="openEntitySearch()" height=22 src="../images/plus1.gif" width=25 align=top border=0>
			<input class="fieldinput"  name="entityName" id="entityName"    readonly tabIndex=-1>
			</td>


		</tr>
		<tr>
		<td>
		<div id="codescontainer"></div>
		</td>
		</tr>
		
	<tr id="row2" name="row2">
		<td colspan="4" align="middle">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td align="right">
			<input type=button class=button onclick="ButtonPress()" href="#" value="Search"></td>
			
			<td align="right">
			<input type=button class=button onclick="buttonFlush()" href="#" value="Cancel"></td>
			
			<td align="right">
			<input type=button class=button onclick=window.close() href="#" value="Close"></td>
			
			<!-- Print Button start -->
			<td align="right">
			<input type=button class=button onclick="buttonPrintPreview()" href="#" value="Print Preview"></td>
			<!-- Print end-->

		</tr>
		</table>
		</td>
	</tr>
	
	<tr style="DISPLAY: none" id="row3" name="row3">
		<td colspan="4" align="middle">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td align="right">
			<input type=button class=button onclick="ButtonPress()" href="#" value="Search"></td>
			
			<td align="right">
			<input type=button class=button onclick="buttonFlush()" href="#" value="Cancel"></td>
			
			<td align="right">
			<input type=button class=button onclick=window.close() href="#" value="Close"></td>
			
			<!-- Print Button start -->
			<td align="right">
			<input type=button class=button onclick="pageSetup(); buttonPrint()" href="#" value="Print"></td>
			<!-- Print end-->

		</tr>
		</table>
		</td>
	</tr>


	</table>

<br>
</div></div></div>
</td></tr>
</table>
</center>
</div>

<%
    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("1"))
    {
		 String[] accName=null;
		try{
		 GeneralLedgerReport glReport = new GeneralLedgerReport ();
		 request.setAttribute("links",glReport.getGeneralLedgerList(glReportBean));
		accName = glReportBean.getAccountCode().split(" : ");
		 }
		 catch(Exception e){
		 System.out.println("Exception in Jsp Page "+ e.getMessage());
		 %>
		 <script>
		 alert("Error :<%=e.getMessage()%>");
		 PageManager.DataService.setQueryField('endDate',null);
		 </script>
		 <%
		 }
%>
	<center><u><b><div class = "normaltext">Sub Ledger-<%= request.getParameter("entityName") %><br><bean:message key="SL.Name"/><br>
	<%=accName[1] %><br>

			<%= glReportBean.getStartDate() %> to <%= request.getParameter("endDate") %>  </div></b></u></center>
			
	<table border="1" style="border: solid 1px #000000" style="width:970px;">
		<tr colspan=2 >
			<td width=50% class="normaltext" style="border: solid 1px #000000" align="center"><B><center>Debit</center></B></td>
			<td width=50% class="normaltext" style="border: solid 1px #000000" align="center"><B><center>Credit</center></B></td>
		</tr>
	</table>

	<div class="tbl2-container" id="tbl-container" style="width:970px;" >
		<display:table cellspacing="0" decorator="org.displaytag.decorator.TotalTableDecorator" name="links" id="currentRowObject" class="its"  export="true" sort="list" >
		<display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		Sub Ledger-<%= request.getParameter("entityName") %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<%=accName[1] %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%= glReportBean.getStartDate() %> to <%= request.getParameter("endDate") %> 
		</display:caption>	
		<div STYLE="display:table-header-group">

		<display:column property="voucherdate" title="Voucher&nbsp;Date"  style="width:95px;"   />
		<display:column media="pdf" property="vouchernumber" title="Voucher No." />
		<display:column media="excel" property="vouchernumber" title="Voucher No." />
		<display:column media="xml" property="vouchernumber" title="Voucher No." />
		<display:column media="csv" property="vouchernumber" title="Voucher No." />
		<display:column media="html" title="Voucher No." style="width:100px;">

	  		<% String cgn =((GeneralLedgerBean)pageContext.findAttribute("currentRowObject")).getCGN();
	     		String deployment=EGovConfig.getProperty("egf_config.xml", "DEPLOYMENT","", "general");
	     		String cgn1="",link="";
	     		if(cgn.length()>0)
	     		{
	     			cgn1=cgn.substring(0,3);
	     		}
	     		if(cgn.length()>0)
	     		{
	     			if(deployment.equalsIgnoreCase("NN"))
		     			link="javascript:callme('"+cgn+"','"+cgn1+"')";
		     		else 	
	     				link="javascript:callmeAP('"+cgn+"','"+cgn1+"')";	
	     		}
			String url = ((GeneralLedgerBean)pageContext.findAttribute("currentRowObject")).getVouchernumber();%>

			<a href="<%=link%>"><%=url%></a>
			</display:column>
			<display:column property="debitVoucherTypeName" title="Voucher Type- Name"/>
			<display:column property="debitparticular" title="Particulars" style="width:130px;"/>
			<display:column property="debitamount" title="&nbsp;&nbsp;&nbsp;Amount&nbsp;(Rs.)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" class="textAlign" />
			<display:column property="creditdate" title="Voucher&nbsp;Date"  style="width:90px;" />
			<display:column media="pdf" property="creditvouchernumber" title="Voucher No." />
			<display:column media="excel" property="creditvouchernumber" title="Voucher No." />
			<display:column media="xml" property="creditvouchernumber" title="Voucher No." />
			<display:column media="csv" property="creditvouchernumber" title="Voucher No." />
			<display:column media="html" title="Voucher No."  style="width:100px;" >

				<% String cgn =((GeneralLedgerBean)pageContext.findAttribute("currentRowObject")).getCGN();
   					String deployment=EGovConfig.getProperty("egf_config.xml", "DEPLOYMENT","", "general");
					String cgn1="",link="";
					if(cgn.length()>0)
					{
						cgn1=cgn.substring(0,3);
					}
					if(cgn.length()>0)
					{
						if(deployment.equalsIgnoreCase("NN"))	
							link="javascript:callme('"+cgn+"','"+cgn1+"')";
						else 	
	     					link="javascript:callmeAP('"+cgn+"','"+cgn1+"')";	
					}
				String url = ((GeneralLedgerBean)pageContext.findAttribute("currentRowObject")).getCreditvouchernumber();%>

				<a href="<%=link%>"><%=url%></a>
				</display:column>
				<display:column property="creditVoucherTypeName" title="Voucher Type- Name"/>
				<display:column property="creditparticular" title="Particulars" style="width:130px;"/>

				<display:column property="creditamount" title="&nbsp;&nbsp;&nbsp;Amount&nbsp;(Rs.)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" class="textAlign3" />
				<display:setProperty name="export.pdf" value="true" />
				<display:setProperty name="export.pdf.filename" value="SubLedgerReport.pdf" /> 
			    <display:setProperty name="export.excel" value="true" />
			    <display:setProperty name="export.excel.filename" value="SubLedgerReport.xls"/>
			    <display:setProperty name="export.csv" value="false" />
				<display:setProperty name="export.xml" value="false" />

		</div>

		<display:footer>
			<tr>
				<td style="border-left: solid 0px #000000" colspan="20"><div style="border-top: solid 1px #000000 ">&nbsp;</div></td>
			<tr>
  		</display:footer>
</display:table>

</div>


<%
	}
%>

	<%
      if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("2"))
    {
	 String[] accName=null;
		try{
		 GeneralLedgerReport glReport = new GeneralLedgerReport ();
		 request.setAttribute("links",glReport.getGeneralLedgerList(glReportBean));
		 accName = glReportBean.getAccountCode().split(" : ");
		 }
		 catch(Exception e){
		 System.out.println("Exception in Jsp Page "+ e.getMessage());
		 %>
		 <script>
		
		 PageManager.DataService.setQueryField('endDate',null);
		 </script>
		 <%
		 }
%>


	<% if(null != accName){ %>

    <center><u><b><div class = "normaltext">Sub Ledger-<%= request.getParameter("entityName") %><br><bean:message key="SL.Name"/><br>
        <%= accName[1]%> <br>
    <%}%>
	
	<%= glReportBean.getStartDate() %> to <%= request.getParameter("endDate") %>  </div></b></u></center>

	 <table border="1"  class="normaltext" cellspacing="0"  style="width:970px;">
		<tr colspan=2 >
			<td width=49%  align="center"><B><center>Debit</center></B></td>
			<td width=49%  align="center"><B><center>Credit</center></B></td>
		</tr></center>
	</table>

	 <div class="tbl4-container" id="tbl-container" style="width:970px;">

		<display:table cellspacing="0" decorator="org.displaytag.decorator.TotalTableDecorator" name="links" id="currentRowObject" class="its"  export="true" sort="list" >
		<display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		Sub Ledger-<%= request.getParameter("entityName") %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<%=accName[1] %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%= glReportBean.getStartDate() %> to <%= request.getParameter("endDate") %> 
		</display:caption>
		<div STYLE="display:table-header-group">

		<display:column property="voucherdate" title="Voucher&nbsp;Date"  style="width:95px;"   />
		<display:column media="pdf" property="vouchernumber" title="Voucher No." />
		<display:column media="excel" property="vouchernumber" title="Voucher No." />
		<display:column media="xml" property="vouchernumber" title="Voucher No." />
		<display:column media="csv" property="vouchernumber" title="Voucher No." />
		<display:column media="html" title="Voucher No."  style="width:100px;">

	  		<% String cgn =((GeneralLedgerBean)pageContext.findAttribute("currentRowObject")).getCGN();
		  		String deployment=EGovConfig.getProperty("egf_config.xml", "DEPLOYMENT","", "general");
	     		String cgn1="",link="";
	     		if(cgn.length()>0)
	     		{
	     			cgn1=cgn.substring(0,3);
	     		}
	     		if(cgn.length()>0)
	     		{
	     			if(deployment.equalsIgnoreCase("NN"))	
	     				link="javascript:callme('"+cgn+"','"+cgn1+"')";
	     			else 	
	     				link="javascript:callmeAP('"+cgn+"','"+cgn1+"')";	
	     		}
			String url = ((GeneralLedgerBean)pageContext.findAttribute("currentRowObject")).getVouchernumber();%>

			<a href="<%=link%>"><%=url%></a>
			</display:column>
			<display:column property="debitVoucherTypeName" title="Voucher Type- Name"/>
			<display:column property="debitparticular" title="Particulars" style="width:130px;"/>
			<display:column property="debitamount" title="&nbsp;&nbsp;&nbsp;Amount&nbsp;(Rs.)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" class="textAlign" />
			<display:column property="creditdate" title="Voucher&nbsp;Date" style="width:90px;" />
			<display:column media="pdf" property="creditvouchernumber" title="Voucher No." />
			<display:column media="excel" property="creditvouchernumber" title="Voucher No." />
			<display:column media="xml" property="creditvouchernumber" title="Voucher No." />
			<display:column media="csv" property="creditvouchernumber" title="Voucher No." />
			<display:column media="html" title="Voucher No."  style="width:100px;" >

				<% String cgn =((GeneralLedgerBean)pageContext.findAttribute("currentRowObject")).getCGN();
					String deployment=EGovConfig.getProperty("egf_config.xml", "DEPLOYMENT","", "general");
					String cgn1="",link="";
					if(cgn.length()>0)
					{
						cgn1=cgn.substring(0,3);
					}
					if(cgn.length()>0)
					{
						if(deployment.equalsIgnoreCase("NN"))
							link="javascript:callme('"+cgn+"','"+cgn1+"')";
						else 	
	     					link="javascript:callmeAP('"+cgn+"','"+cgn1+"')";		
					}
				String url = ((GeneralLedgerBean)pageContext.findAttribute("currentRowObject")).getCreditvouchernumber();%>

				<a href="<%=link%>"><%=url%></a>
				</display:column>
				<display:column property="creditVoucherTypeName" title="Voucher Type- Name"/>
				<display:column property="creditparticular" title="Particulars" style="width:130px;"/>
				<display:column property="creditamount" title="&nbsp;&nbsp;&nbsp;Amount&nbsp;(Rs.)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" class="textAlign3" />
				<display:setProperty name="export.pdf" value="true" />
				<display:setProperty name="export.pdf.filename" value="SubLedgerReport.pdf" /> 
			    <display:setProperty name="export.excel" value="true" />
			    <display:setProperty name="export.excel.filename" value="SubLedgerReport.xls"/>
			    <display:setProperty name="export.csv" value="false" />
				<display:setProperty name="export.xml" value="false" />

		</div>

		<display:footer>
			<tr>
				<td style="border-left: solid 0px #000000" colspan="20"><div style="border-top: solid 1px #000000 ">&nbsp;</div></td>
			</tr> 
  		</display:footer>
  	</display:table>
	</div>
<%
	}
%>
</form>
</body>
</html>


