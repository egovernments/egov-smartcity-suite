<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page buffer = "64kb" %> 
<%@page  import="com.exilant.eGov.src.reports.LongAmountWrapper,java.text.SimpleDateFormat,com.exilant.eGov.src.reports.*,javax.naming.InitialContext,java.io.*,
			java.util.*"%>
			
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">
<META http-equiv=pragma content=no-cache> 
<!-- Inclusion of the CSS files that contains the styles -->

<link rel=stylesheet href="../css/egov.css" type="text/css" media="screen" />
<link rel=stylesheet href="../css/print.css" type="text/css" media="print" />


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
#codescontainer .yui-ac-content {position:absolute;width:80%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:40%;background:#a0a0a0;z-index:9049;}
#codescontainer ul {padding:5px 0;width:80%;}
#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
#codescontainer li.yui-ac-highlight {background:#ff0;}
#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}  
</style>

<SCRIPT LANGUAGE="javascript">

<%
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	Date curntDate = new Date();
	String currDate = sdf.format(curntDate);
%>

var strtDate="";
var endDate="";
var detail_TypeId="",detail_Key="";
var accEntityKey="";

var entityDetailArray;           
var codeObj;
var yuiflag = new Array();

var validPartyNameArray1; 

function onLoad()
{
	PageValidator.addCalendars();
	if(document.getElementById("startDate").value=="")
	{		
		PageManager.DataService.callDataService("finYearDate");
	}
	PageManager.ListService.callListService();
	var beanVal= <%=request.getParameter("fromBean")%>;
	
	if(beanVal==2)
	{
		//document.getElementById("row2").style.display="block";
		//document.getElementById("row1").style.display="none";
	}
	
	document.getElementById("endDate").value="<%=currDate%>";
	document.getElementById("startDate").focus();
}
function ButtonPress()
{
	if (!PageValidator.validateForm())
	return;
	
	if(!checkDateValidation())
	return;
	
	strtDate = document.getElementById('startDate').value;
	endDate = document.getElementById('endDate').value;
	//alert(endDate);
		
	if(endDate!="")
	{
		if( compareDate(formatDate6(strtDate),formatDate6(endDate)) == -1 )
		{
			//alert("Inside compare dt");
			alert('From Date cannot be greater than To Date');
			//document.getElementById('startDate').value='';
			//document.getElementById('endDate').value='';
			document.getElementById('startDate').focus();
			return false;
		}
	}
		
	
	//alert(document.getElementById("partytype_id").value);
	var pTypeObj=document.getElementById("partytype_id");
	var pNameObj=document.getElementById("accEntityList");
	var pKeyObj=document.getElementById("accEntityKey");
	
	if(pNameObj.value!="")  
	{
		if(pTypeObj.value==""){
			alert("Select Party Type First !!!");
			pTypeObj.focus();
			return false;
		}
	}
	
	 if(pTypeObj.value!="" && pNameObj.value!="" && pKeyObj.value!="")
	{
		var partyTypeId=pTypeObj.value;
		var paramPartyName=pNameObj.value;
		var paramPartyKey=pKeyObj.value;
		
	//For Party Name Validation 
		checkPartyNameForAdvRegister(partyTypeId,paramPartyName,paramPartyKey);
		 if(validPartyNameArray1=='null')
		{		
			alert("Enter Valid Party Name !!!");
			pNameObj.focus();
			document.getElementById('entityName').value='';
			document.getElementById('accEntityKey').value='';
			return false;			
			
		}
		
	}
	else
	{
		alert("Enter Valid Party Name !!!");
		pNameObj.focus();
		document.getElementById('entityName').value='';
		document.getElementById('accEntityKey').value='';
		pKeyObj.value='';
		return false;
	}
			
	if(pTypeObj.value!="")
	detail_TypeId=pTypeObj.value;
	
	if(document.getElementById('accEntityKey').value!="")
	detail_Key=document.getElementById('accEntityKey').value;
	
	//alert("party name--->"+document.getElementById("accEntityList").value);
	//alert("code----->"+document.getElementById("entityName").value);
	//alert("detail key id--->"+document.getElementById("accEntityKey").value);
		
	
	document.getElementById('fromBean').value = 1;
 	document.AdvanceRegister.submit();

}
function afterRefreshPage(dc)
{
	var afterSub="<%=(request.getParameter("fromBean"))%>";
	//alert("afterSub"+afterSub);
	
	if(afterSub != "null")
	{
		var partyTypeId=dc.values['partytype_id']
		var paramFromDate=dc.values['startDate']
		var paramToDate=dc.values['endDate']
		//alert(partyTypeId);
		
	
		if(partyTypeId !="" && partyTypeId != undefined && (paramFromDate!=undefined && paramToDate!=undefined))
		{
		       var type='getEntityDetailForAdvRegister';
		       var url = "../commons/Process.jsp?type=" +type+ "&partyTypeId="+partyTypeId+ "&paramFromDate="+paramFromDate+ "&paramToDate="+paramToDate;
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
					entityDetailArray=codes.split("+");
					//alert(entityDetailArray);
					codeObj = new YAHOO.widget.DS_JSArray(entityDetailArray);

				  }
			      }
			};
			req2.open("GET", url, false);
			req2.send(null);
		}
	}
	
	if(afterSub != "null")
		{
			if(dc.grids['partyTypeListForAdvReg'])
			{
				PageManager.DataService.removeQueryField('partytype_id');
				PageManager.DataService.removeQueryField('detail_Key');
				PageManager.DataService.removeQueryField('strtDate');
				PageManager.DataService.removeQueryField('endDate');
				PageManager.DataService.removeQueryField('accEntityList');
			}
	}
}

function beforeRefreshPage(dc){

	if(dc.values['serviceID']=="finYearDate")
	{

	var dt=dc.values['startFinDate'];
	dt=formatDate2(dt);
	document.getElementById("startDate").value=dt;
	}
}
function buttonFlush()
{
	window.location="AdvanceRegister.jsp?showMode=new";
}
function getAllPartyName()
{
	
	checkDateValidation();
	var obj=document.getElementById("partytype_id");
      	var partyTypeId=obj.value;
      	var paramFromDate = document.getElementById('startDate').value;
	var paramToDate = document.getElementById('endDate').value;
         	
      	// alert(partyTypeId);
      	
	if(partyTypeId !="" && (paramFromDate!="" && paramToDate!=""))
	{
       var type='getEntityDetailForAdvRegister';
       var url = "../commons/Process.jsp?type=" +type+ "&partyTypeId="+partyTypeId+ "&paramFromDate="+paramFromDate+ "&paramToDate="+paramToDate;
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
			entityDetailArray=codes.split("+");
			//alert(entityDetailArray);
			codeObj = new YAHOO.widget.DS_JSArray(entityDetailArray);
					
                  }
              }
        };
        req2.open("GET", url, false);
        req2.send(null);
        }
}
function autocompletecode(obj)
{
	// set position of dropdown
	var src = obj;
	var target = document.getElementById('codescontainer');
	var posSrc=findPos(src);
	target.style.left=posSrc[0];
	target.style.top=posSrc[1]+25;
	target.style.width=500;
	
	if(obj.name=='accEntityList') target.style.left=posSrc[0]-10;	
	var currRow=PageManager.DataService.getRow(obj);
	var coaCodeObj = obj;
	
	//alert("rowIndex"+yuiflag[currRow.rowIndex]);
	if(yuiflag[currRow.rowIndex] == undefined)
 	{
		
		if(event.keyCode != 40 )
		{
			if(event.keyCode != 38 )
			{
				var oAutoComp = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainer', codeObj);
				oAutoComp.queryDelay = 0;
				oAutoComp.prehighlightClassName = "yui-ac-prehighlight";
				oAutoComp.useShadow = true;
				oAutoComp.useIFrame = true;
			}
		}
		yuiflag[currRow.rowIndex]=1;
  	}
}

//fills the related neighboor object after splitting
 function fillNeibrAfterSplit(obj,neibrObjName)
 {  
   	
   	//alert(document.getElementById("partytype_id").value);
   	
   	var pTypeObj=document.getElementById("partytype_id");
   	
   	if(pTypeObj.value=="")
   	{
   	
   	alert("Select Party Type First !!!");
   	pTypeObj.focus();
   	return;
   	
   	}
   	
   	 //alert("party name--->"+document.getElementById("accEntityList").value);
	 //alert("code----->"+document.getElementById("entityName").value);
	 //alert("detail key id--->"+document.getElementById("accEntityKey").value);
   	
   	var temp = obj.value; 
	temp = temp.split("`-`");
	
	
		
	var currRow=getRow(obj);
	yuiflag[currRow.rowIndex] = undefined;

	 var neibrObj=getControlInBranch(currRow,neibrObjName);

	 if(obj.value==null || obj.value=="") { neibrObj.value= ""; return; }
	 if(temp[1]==null && (neibrObj.value!='' || neibrObj.value!=null) )
	 { return ;}
	 else {
	   var temp1=temp[1].split("-$-");
	   obj.value=temp[0];
	   
	    currRow=getRow(obj);
	    yuiflag[currRow.rowIndex] = undefined;

	   neibrObj.value = temp1[1];
		document.getElementById("entityName").value=temp1[0];
	   }
	   
}
function checkPartyNameForAdvRegister(arg1,arg2,arg3)
{
	var partyTypeId=arg1;
	var paramPartyName=arg2;
	var paramPartyKey=arg3;
	var paramFromDate = document.getElementById('startDate').value;
	var paramToDate = document.getElementById('endDate').value;
	
	var type;	
	type='checkPartyNameForAdvRegister';
	
	       var url = "../commons/Process.jsp?type=" +type+ "&partyTypeId="+partyTypeId+"&paramPartyName="+paramPartyName+"&paramPartyKey="+paramPartyKey+ "&paramFromDate="+paramFromDate+ "&paramToDate="+paramToDate;
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
				validPartyNameArray1=codes.split("+");
				//alert(validPartyNameArray1);
			  }
		      }
		};
		req2.open("GET", url, false);
		req2.send(null);
       	 

}
function clearValue()
{	
	if(document.getElementById('accEntityList').value=='')
	{
		document.getElementById('accEntityList').value='';
		document.getElementById('entityName').value='';
		document.getElementById('accEntityKey').value='';
	}
}
 function clearPartyName()
 {	
	document.getElementById('accEntityList').value='';
	document.getElementById('entityName').value='';
	document.getElementById('accEntityKey').value='';
 }
  
 function buttonPrint()
 {
 	document.getElementById("tbl-header1").style.display = "none";
	document.getElementById("msgRow").style.display = "none";
	
	if(window.print)
	{
	  window.print();
	}
	document.getElementById("tbl-header1").style.display = "block";
}
function checkDateValidation()
 {	
	var  startDate = document.getElementById('startDate').value;
	var endDate = document.getElementById('endDate').value;
	
	if(startDate.length==0)
	{
	    alert("Enter From Date !!!");
	    document.getElementById('partytype_id').value="";
	    document.getElementById('startDate').focus();
 	    return false;
	}
	if(endDate.length==0)
	{
	     alert("Enter To Date !!!");
	     document.getElementById('partytype_id').value="";
	    document.getElementById('endDate').focus();
	    return false;
	}

	var tMonth = endDate.split('/')[1];
 	if(tMonth<4)
 		var fiscalYearStartDate="01/04/"+(endDate.split('/')[2]-1);
 	else
 		var fiscalYearStartDate="01/04/"+endDate.split('/')[2];
 	if(compareDate(fiscalYearStartDate,startDate) == -1 )
 	{ 
 	   alert("Start Date and End Date should be in same financial year");
 	   document.getElementById('startDate').focus();
 	   return false;
	}
	
	return true;
	
 }
  

</SCRIPT>
</head>
<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onLoad="onLoad()">
<jsp:useBean id = "arReportBean" scope ="request" class="com.exilant.eGov.src.reports.AdvanceRegisterBean"/> 
<jsp:setProperty name = "arReportBean" property="*"/> 
<form name="AdvanceRegister" action="">

<input type="hidden" name="fromBean" id="fromBean" value="0">

<center>
<br>
<table align='center' id="table2">
<tr><td>
<div id="main"><div id="m2"><div id="m3">
<div class="tbl-header1" id="tbl-header1">



<table align='center' class="tableStyle" id="table3"> 

	<tr >
			<td colspan="6" class="rowheader" valign="center"  width="100%"><span id="screenName" class="headerwhite2">
				Register Of Advance</span>
			</td>
	</tr>
	<tr class ="row1">
		<td></td>
		<td></td> <td></td> <td></td>
	</tr>
	<tr>
		<td class="labelCell"><div align="right" valign="center" class="labelcell">Date From<span class="leadon">*</span></div></td>
		<td class="smallfieldcell" align="left">
			<input name="startDate" id="startDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilCalendar="true" exilMustEnter="true">
		</td>
		<td class="labelCell"><div align="right" valign="center" class="labelcell" >Date To<span class="leadon">*</span></div></td>
		<td class="smallfieldcell">
			<input name="endDate" id="endDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilCalendar="true" exilMustEnter="true">
		</td>
	</tr>
	<tr>
		<td class="labelCell">
		<div align="right" valign="center" class="labelcell"> Party Type <span class="leadon">*</span></div>
		</td>
		<td class="smallFieldCell">
			<select  name="partytype_id" id="partytype_id" class="combowidth1" onChange="getAllPartyName();clearPartyName();"  exilListSource="partyTypeListForAdvReg" exilMustEnter="true"></select>
		</td>
		<td></td> <td></td> 

		
	</tr>
	<tr>
	
		<td class="labelCell">
		<div  align="right" valign="center" class="labelcell">Party Name<span class="leadon">*</span></div>
		</td>
		<td class="smallfieldcell">
		<input class="fieldinputlarge" style="width: 250" name="accEntityList" id="accEntityList" autocomplete="off" onkeyup="autocompletecode(this);" onblur="fillNeibrAfterSplit(this,'accEntityKey');clearValue(); " exilMustEnter="true">
		<td class="labelCell"><div  align="right" valign="center" class="labelcell">Code</div></td>
		<td class="smallfieldcell">
		<input class="fieldinput"  name="entityName" id="entityName"    readonly tabIndex="-1">
		</td>
		<input class="fieldinput" type="hidden" name="accEntityKey" id="accEntityKey">			
		</td>
		<td></td> <td></td> 
	
	
	</tr>
	
	
	<tr>
		<td>
		<div id="codescontainer"></div>
		</td>
	</tr>
	
	<tr><td>&nbsp;</td></tr>
		<tr id="msgRow">
		<td align="left" class="labelcellforsingletd" style="FONT-SIZE: 12px;" colspan="10">
		To&nbsp;print&nbsp;the&nbsp;report,&nbsp;please&nbsp;ensure&nbsp;the&nbsp;following&nbsp;settings:<br>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1. Paper size: A4<br>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2. Paper Orientation: Landscape
		</td>
	 </tr>
		
	<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
	
	
	<tr>
			<td colspan="10" align="middle">
			<table border="0" cellpadding="0" cellspacing="0">
			<tr>	
			<td>		
				<input type=button class=button onclick="ButtonPress()" href="#" value="Search">
				<input type=button class=button onclick="buttonFlush()" href="#" value="Cancel">
				<input type=button class=button onclick="buttonPrint()" href="#" value="Print">
				<input type=button class=button onclick="window.close();" href="#" value="Close">
			</td>
			</tr>
			 </table>
			 </td>
	</tr>
	
</table>
  <br>
  
  </div>
</div></div></div>
  </td></tr>
  </table>
</center>


<%
    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("1"))
    {
		try{
		 AdvanceRegister arReport = new AdvanceRegister ();
		// arReport.getAdvanceRegisterReport(arReportBean);
		 request.setAttribute("links",arReport.getAdvanceRegisterReport(arReportBean));
		 }
		 catch(Exception e){
		 System.out.println("Exception in Jsp Page "+ e.getMessage());
		 %>
		 <script>
		 alert("Error :<%=e.getMessage()%>");
		 </script>
		 <%
		  }
		 %>	
		 
		  
		 <center><u><b><div class = "normaltext"> Register of Advance from <%= request.getParameter("startDate") %> to <%= request.getParameter("endDate") %></div></b></u></center>   		
		<div class="tbl2-container" id="tbl-container">
		<display:table cellspacing="0" name="links"  id="currentRowObject" export="true"  sort="list" class="its">
		<display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Register of Advance from <%= request.getParameter("startDate") %> to <%= request.getParameter("endDate") %>
		</display:caption>
		<display:caption media="excel">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Register of Advance from <%= request.getParameter("startDate") %> to <%= request.getParameter("endDate") %>
		</display:caption>
		<div STYLE="display:table-header-group">

		<display:column property="serialNo" style="width:30px" title="Sl No."  />

		<display:column property="vdate"  style="width:80px" title="<center>Voucher&nbsp;Date</center>"       /> 
		<display:column property="slname"  style="width:100px" title="<center>SubLedger&nbsp;Name</center>"      />	
		<display:column property="payordernumberdate" style="width:40px" title="<center>Payment Order No.&nbsp;Date</center>"  style="text-align:right"   />
		<display:column property="bankpayvnumberdate" style="width:80px" title="<center>Bank Payment No.&nbsp;Date</center>" style="text-align:right"   />	


		<display:column property="particulars" style="width:80px" title="<center>Particulars&nbsp;</center>"  style="text-align:right"   />	
		<display:column property="payamount" style="width:80px" title="<center>Payment&nbsp;Amount&nbsp;(Rs.)</center>"  style="text-align:right"   />	
		<display:column property="recovnumberdate" style="width:80px" title="<center>Recovery No.&nbsp;Date</center>"  style="text-align:right"   />	
		<display:column property="recovamount" style="width:80px" title="<center>Recovery&nbsp;Amount&nbsp;(Rs.)</center>"  style="text-align:right"   />	
		<display:column property="remarks" style="width:80px" title="<center>Remarks&nbsp;</center>"  style="text-align:right"   />
		<display:column property="closingBal" style="width:80px" title="<center>Closing&nbsp;Balance&nbsp;(Rs.)</center>" style="text-align:right"   />	

		<display:setProperty name="export.pdf" value="true" />
		<display:setProperty name="export.pdf.filename" value="AdvanceRegister.pdf" /> 
		<display:setProperty name="export.csv" value="false" />
		<display:setProperty name="export.xml" value="false" />
		<display:setProperty name="export.excel" value="true" />
		<display:setProperty name="export.excel.filename" value="AdvanceRegister.xls"/> 			
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
		 	
		
 	 
</form>
</body>
</html>
