<!--  #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#      accountability and the service delivery of the government  organizations.
#   
#       Copyright (C) <2015>  eGovernments Foundation
#   
#       The updated version of eGov suite of products as by eGovernments Foundation 
#       is available at http://www.egovernments.org
#   
#       This program is free software: you can redistribute it and/or modify
#       it under the terms of the GNU General Public License as published by
#       the Free Software Foundation, either version 3 of the License, or
#       any later version.
#   
#       This program is distributed in the hope that it will be useful,
#       but WITHOUT ANY WARRANTY; without even the implied warranty of
#       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#       GNU General Public License for more details.
#   
#       You should have received a copy of the GNU General Public License
#       along with this program. If not, see http://www.gnu.org/licenses/ or 
#       http://www.gnu.org/licenses/gpl.html .
#   
#       In addition to the terms of the GPL license to be adhered to in using this
#       program, the following additional terms are to be complied with:
#   
#   	1) All versions of this program, verbatim or modified must carry this 
#   	   Legal Notice.
#   
#   	2) Any misrepresentation of the origin of the material is prohibited. It 
#   	   is required that all modified versions of this material be marked in 
#   	   reasonable ways as different from the original version.
#   
#   	3) This license does not grant any rights to any user of the program 
#   	   with regards to rights under trademark law for use of the trade names 
#   	   or trademarks of eGovernments Foundation.
#   
#     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------  -->
<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page  import="com.exilant.eGov.src.reports.LongAmountWrapper,java.io.*,java.util.*,java.sql.*,javax.sql.*,javax.naming.InitialContext,com.exilant.GLEngine.*"%>
<%@ page import ="org.egov.infstr.utils.database.utils.EgovDatabaseManager,java.util.ArrayList" %>
<%@ page import ="com.exilant.eGov.src.reports.RptBudgetList" %>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">
<META http-equiv=pragma content=no-cache>
<link rel=stylesheet href="../css/egov.css" type="text/css">
<!--SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js"></SCRIPT-->
<SCRIPT LANGUAGE="javascript" SRC="../exility/CookieManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js"></SCRIPT>
<script language="javascript" src="../resources/javascript/jsCommonMethods.js"></script>
<SCRIPT type="text/javascript" src="../resources/javascript/calendar.js" type="text/javascript" ></SCRIPT>
<link rel="stylesheet" href="../css/lockRowHead.css" type="text/css" />
<link rel="stylesheet" href="../exility/screen.css" type="text/css" media="screen, print" />
<SCRIPT LANGUAGE="javascript">
var kmfNoNames=new Array(
					"REVENUE RECEIPTS (FUNCTION WISE)",
					"REVENUE PAYMENTS (FUNCTION WISE)",
					"CAPITAL RECEIPTS",
					"CAPITAL PAYMENTS (FUNCTION WISE)",
					"EXTRA-ORDINARY RECEIPTS",
					"EXTRA-ORDINARY PAYMENTS ");

function buttonFlush()
{
	
	window.location="budgetReport.jsp";

}

<%! ArrayList accType = new ArrayList();
	ArrayList finYear = new ArrayList();
%>

function getDataList(){
	//bootbox.alert(document.getElementById("finId").value);
	//bootbox.alert(document.getElementById("conSupTypeId").value);
	var objKmfNo=document.getElementById('kmfNo');
	var objFinyear=document.getElementById('finyear'); 
	if(objKmfNo.selectedIndex <= 0 || objFinyear.selectedIndex <= 0)  return;
	if (!PageValidator.validateForm())	return;	
	
	document.getElementById("displayCondition").value=1;
		
	if(document.getElementById('displayCondition').value!=0)	
	{	//bootbox.alert(getFormNOName(objKmfNo));
		var obj=getFormNOName(objKmfNo);
		var a=obj.split('~~');
			
		document.getElementById('accTypeName').value=a[1];
		document.getElementById('accType').value=a[0];
		document.getElementById('rule').value=a[2];
		document.getElementById('kmfNum').value=objKmfNo.options[objKmfNo.selectedIndex].text; 
		document.getElementById('year').value=objFinyear.options[objFinyear.selectedIndex].text;
		
		document.getElementById('act_PrevYr').value="Actuals for the previous year "; 
		document.getElementById('budget_CurYr').value="Budget Estimate for the current year ";  
		document.getElementById('act_UptoDec').value="Actuals upto December of the current year "; 
		document.getElementById('revisedBudget_CurYr').value="Revised Budget Estimate for the current year ";  
		document.getElementById('budget_NextYr').value="Budget Estimate for the Next Year ";  
	
		document.BudgetRpt.submit();
	}
}

function getFormNOName(obj)
{	
	var name='';
	var num=obj.options[obj.selectedIndex].value;
	var rule='';
	var acctype='';
	
	switch(num)
	{	case '80': acctype=1; name="REVENUE RECEIPTS (FUNCTION WISE)"; rule="(Rule 132(2))"; break;
		case '81': acctype=2;  name="REVENUE PAYMENTS (FUNCTION WISE)"; rule="(Rule 132(2))"; break;
		case '82': acctype=3; name="CAPITAL RECEIPTS"; rule="(Rule 132(2))"; break;
		case '83': acctype=4; name="CAPITAL PAYMENTS (FUNCTION WISE)"; rule="(Rule 132(2))"; break;
		case '84': acctype=5; name="EXTRA-ORDINARY RECEIPTS"; rule="(Rule 132(2))"; break;
		case '85': acctype=6; name="EXTRA-ORDINARY PAYMENTS"; rule="(Rule 132(2))"; break;
		case '86': acctype=7; name=""; rule="(Rules 135(1))"; break;
		case '87': acctype=8; name=""; rule="(Rule 140(1))"; break;
		
	}
	//bootbox.alert(acctype+'~~'+name+'~~'+rule);
	return acctype+'~~'+name+'~~'+rule;
}


function onLoadTask()
{
<%			String kmf_No=request.getParameter("kmfNo");
		    String yr=request.getParameter("year"); 
%>
			var formN0=document.getElementById('kmfNo');
			var index=0,i;
			formN0.options[index++]=new Option('-----Choose------');
			for(i=80;i<=85;i++)
			{
				formN0.options[index++]=new Option(i + " -- " + kmfNoNames[i-80],i);
				if( null != "<%=kmf_No%>" && "<%=kmf_No%>"==i)
					formN0.options[index-1].selected=true;
				
			}
/*		
<% 	 		String type=request.getParameter("accType");
		    String year=request.getParameter("finYear");
			Connection conn= EgovDatabaseManager.openConnection();
			Statement stmt=conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT ID,DESCRIPTION FROM EGF_BUDGET_ACCTYPE");
			accType =new ArrayList();
			while (rs.next())
			{
				String typeId=rs.getString("ID");
				String typeName=rs.getString("DESCRIPTION");
				accType.add(typeId);
				accType.add(typeName);
				
			}
			 rs.close();
			 stmt.close();
	%>		 
			 var arr=new Array();
			<% Iterator itr=accType.iterator();
				while (itr.hasNext())
					{
						String val=(String)itr.next();
			%>
						arr.push("<%=val%>");
				<%	} %>
			
			var typeCombo=document.getElementById('accType');
			typeCombo.options[0]=new Option('-------------Choose-----------------');
			var j=1;
			
			for (var i=0;i<arr.length-1;i=i+2)
			{
				var aId=arr[i];
				var aName=arr[i+1];
				typeCombo.options[j++]=new Option(aName,aId);
				if(null!="<%=type%>" && aId=="<%=type%>")
		 		 typeCombo.options[j-1].selected=true;

			}
	*/
<%
			 
			Statement stmt1=conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery("SELECT ID,FINANCIALYEAR FROM FINANCIALYEAR where isactive=1 ");
			finYear =new ArrayList();
			while (rs1.next())
			{
				String typeId1=rs1.getString("ID");
				String typeName1=rs1.getString("FINANCIALYEAR");
				finYear.add(typeId1);
				finYear.add(typeName1);
				
			}
				rs1.close();
				
				rs1=null;
				rs1 = stmt1.executeQuery("select name from companydetail");
				rs1.next();
				//System.out.println("company name:"+rs1.getString(1));
				String companyName=rs1.getString(1);
				rs1.close();
				
				stmt1.close();
				EgovDatabaseManager.releaseConnection(conn,stmt1);		
	%>
	
	if('<%=companyName %>' !=null)
		document.getElementById('companyName').value='<%=companyName %>';
	
		
	var arr1=new Array();
	<% Iterator itr1=finYear.iterator();
		while (itr1.hasNext())
			{
				String val1=(String)itr1.next();
	%>
				arr1.push("<%=val1%>");
		<%	} %>
	
	var finYearCombo=document.getElementById('finYear');
	finYearCombo.options[0]=new Option('-----Choose------');
	var j=1;
	for (var i=0;i<arr1.length-1;i=i+2)
	{
		var aId1=arr1[i];
		var aName1=arr1[i+1];
		finYearCombo.options[j++]=new Option(aName1,aId1);
		if(null!="<%=yr%>" && aName1=="<%=yr%>")
		  finYearCombo.options[j-1].selected=true;
	}
}

function alertRedColor()
{
	var objKmfNo=document.getElementById('kmfNo');
	//bootbox.alert("redcolor");
	var objFinyear=document.getElementById('finyear'); 
	if(objKmfNo.selectedIndex <= 0) objKmfNo.style.backgroundColor="OrangeRed";
	if(objFinyear.selectedIndex <= 0)   objFinyear.style.backgroundColor="OrangeRed";
}

function alertNoColor()
{
	var objKmfNo=document.getElementById('kmfNo');
	//bootbox.alert("nocolor");
	var objFinyear=document.getElementById('finyear'); 
	if(objKmfNo.selectedIndex <= 0) objKmfNo.style.backgroundColor="White";
	if(objFinyear.selectedIndex <= 0)   objFinyear.style.backgroundColor="White";
}

function pageSetup()
{
document.body.leftMargin=0.75;
document.body.rightMargin=0.75;
document.body.topMargin=0.75;
document.body.bottomMargin=0.75;
}

function buttonPrint()
{
	document.getElementById('displayCondition').value ="1";	
    var hide1,hide2; 
    hide1 = document.getElementById("tbl-header1");
	hide1.style.display = "none";     
	//hide2 = document.getElementById("TBR");
	//document.forms[0].submit();
	getDataList();
  	if(window.print)
  		{
  		window.print();
	  	}
}


	</script>

</head>
<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onLoad="onLoadTask()">
<jsp:useBean id = "rptBRBean" scope ="request" class="com.exilant.eGov.src.reports.RptBudgetBean"/>
<form name="BudgetRpt" action = "budgetReport.jsp?year="+year&"kmfNum="+kmfNum&"rule="+rule  method = "post">
<input type="hidden" id="act_PrevYr" name="act_PrevYr" value="Actuals for the previous year" >
<input type=hidden id="budget_CurYr" name="budget_CurYr" value="Budget Estimate for the current year" >
<input type=hidden id="act_UptoDec" name="act_UptoDec" value="Actuals upto December of the current year" >
<input type=hidden id="revisedBudget_CurYr" name="revisedBudget_CurYr" value="Revised Budget Estimate for the current year" >
<input type=hidden id="budget_NextYr" name="budget_NextYr" value="Budget Estimate for the Next Year" >
<input type=hidden id="companyName" name="companyName"value="">

<jsp:setProperty name = "rptBRBean" property="*"/> 
<input type=hidden id="year" name="year" value="">
<input type=hidden id="accTypeName" name="accTypeName" value="">
<input type=hidden id="kmfNum" name="kmfNum" value="">
<input type=hidden id="accType" name="accType" value="">
<input type=hidden id="rule" name="rule" value="">

<table id="tbl-header1" name="tbl-header1"><tr><td><div id="main"><div id="m2"><div id="m3">
<table width="100%" border=0 cellpadding="3" cellspacing="0"  class="tableStyle">
<tr >
		<td  align =center class="tableheader" valign="center"  class="normaltext" COLSPAN =2 width="100%" HEIGHT="23" ><span id="screenname"   class= "" >Budget Report</span></td>
	</tr>
	<tr ><td width="50%"><div align="right" valign="center" class="labelcellmedium" >KMF NO.</div></td>
	<td class="smallfieldcell"><select  name="kmfNo" id="kmfNo" ></select></td>
	</tr>
	<!-- tr class="row1" >
		<td width="50%"><div align="right" valign="center" class="normaltext" >Type<span class="leadon">
			</span></div></td>
		<td width="50%">
			<select  name="accType" id="accType" ></select>
		</td>
	
	</tr -->
	<tr class="row2">
	<td><div align="right" valign="center" class="labelcellmedium">Financing Year</div></td>
		<td align="left" class="smallfieldcell">
			<SELECT  id="finYear" name="finYear"></SELECT>
		</td>
		</td>
	</tr>
	<!-- tr class="row2" height="5"><td>&nbsp;</td><td>&nbsp;</td></tr -->
	<tr class="row2">
		<td colspan="2" align="middle" valign="center">
			<!--table border="0" cellpadding="0" cellspacing="0" width="178">
				<tr class="button">
					<td align="right"><IMG height=18 src="/egi/resources/erp2/images/Button_leftside.gif" width=7></td>
					<td bgcolor="#fe0000" valign="center" nowrap><A class=buttonprimary    onmouseout="alertNoColor()" onmouseup="alertNoColor()" onmousedown="alertRedColor()" onclick="getDataList()" href="#">Search</A></td>
					<td><IMG height=18 src="/egi/resources/erp2/images/Button_rightside.gif" width=7></td>
					<td><IMG src="/egi/resources/erp2/images/spacer.gif" width=8></td>	
					<td align="right"><IMG height=18 src="/egi/resources/erp2/images/Button_second_leftside.gif" width=6></td>
					<td bgcolor="#ffffff" valign="center" nowrap background="/egi/resources/erp2/images/Button_second_middle.gif"><A class=buttonsecondary onclick=buttonFlush(); href="#">Cancel</A></td>
					<td><IMG height=18 src="/egi/resources/erp2/images/Button_second_rightside.gif" width=6></td>						
					<td><IMG src="/egi/resources/erp2/images/spacer.gif" width=8></td>								
					<td align="right"><IMG height=18 src="/egi/resources/erp2/images/Button_second_leftside.gif" width=6></td>
					<td bgcolor="#ffffff" valign="center" nowrap background="/egi/resources/erp2/images/Button_second_middle.gif"><A class=buttonsecondary onclick=window.close() href="#">Close</A></td>
					<td><IMG height=18 src="/egi/resources/erp2/images/Button_second_rightside.gif" width=6></td>
				</tr>
			</table -->
			<div></div>
			<table align="center">
			 	<tr>
			 	<td colspan=3><input Class="button" type="button" name=""   value="Search"  onclick="getDataList()" />
			 		<input  Class="button" type="button"   value="Cancel"  name="" onclick="buttonFlush()" />
			 		
			 	 </td>	</div>	
			 	 <td><input  Class="button" type="button" value="Close" onclick="window.close()" /></td>
			 	 <td><input  Class="button" type="button" value="Print" onclick="pageSetup();buttonPrint()" /></td>
				</tr>
			</table>

 </div></div></div></td></tr></table>
			
		</td>
	</tr>
</table>

<input type="hidden" name="displayCondition" id="displayCondition" value="0"/> 

<%
	  
	     RptBudgetList BudgetList = new RptBudgetList();
	     LinkedList list= new LinkedList();
	    if(request.getParameter("displayCondition") !=null && request.getParameter("displayCondition").equals("1"))
	    {
		
		try{
			
			// System.out.println(getRptBudgetList(rptBRBean));
			// if(BudgetList.getRptBudgetList(rptBRBean).size()!=0)			   
			list=BudgetList.getRptBudgetList(rptBRBean);
			 request.setAttribute("links",list);
			 
			 //SETTING TITLES TO DISPLAY TAG
			 if(request.getParameter("act_PrevYr") != null)
			 {
			 request.setAttribute("act_PrevYr",request.getParameter("act_PrevYr")+rptBRBean.getPreviousYear());
			 //System.out.println("request:"+request.getAttribute("act_PrevYr"));
			 }
			 if(request.getParameter("budget_CurYr") != null)
			 {
			 request.setAttribute("budget_CurYr",request.getParameter("budget_CurYr")+rptBRBean.getCurrentYear());
			 //System.out.println("request:"+request.getAttribute("budget_CurYr"));
			 }
			 if(request.getParameter("act_UptoDec") != null)
			 {
			 request.setAttribute("act_UptoDec",request.getParameter("act_UptoDec")+rptBRBean.getCurrentYear());
			 //System.out.println("request:"+request.getAttribute("act_UptoDec"));
			 }
			 if(request.getParameter("revisedBudget_CurYr") != null)
			 {
			 request.setAttribute("revisedBudget_CurYr",request.getParameter("revisedBudget_CurYr")+rptBRBean.getCurrentYear());
			 //System.out.println("request:"+request.getAttribute("revisedBudget_CurYr"));
			 }
			 if(request.getParameter("budget_NextYr") != null)
			 {
			 request.setAttribute("budget_NextYr",request.getParameter("budget_NextYr")+rptBRBean.getNextYear());
			 //System.out.println("request:"+request.getAttribute("budget_NextYr"));
			 }
		   }catch(Exception e){  }
		   
		   if(list.size()>0)
		   {
%>	 

			<div  align="center" class = "normaltext"><b><font size=2>NAME: <%=request.getParameter("companyName")%>  </div>
			<div  align="center" class = "normaltext"><b><font size=2>BUDGET FOR THE YEAR: <%=request.getParameter("year") %> </div>
			<div  align="center" class = "normaltext"><b><font size=1>Estimate of &nbsp;<%=request.getParameter("accTypeName") %></div>
<%
			}
%>

<div class="tbl1-container" id="tbl-container" style="width: 100%" >	
	 <display:table name="links" id="links"   export="false" sort="list"  class="its" cellspacing="0" style="margin-top:0"> 
		<display:column    property="slno" title="<CENTER>Sl No</CENTER>"   class="textAlignCenter" style=" width:2%" style="align:center"/>
		<display:column property="slno" media="pdf" />
		<display:column property="slno" media="excel" />
		<display:column property="slno" media="xml" />
		<display:column property="slno" media="csv" />
		
		<display:column   property="particulars" title="<CENTER>Particulars</CENTER>"   class="textAlignLeft" style="width:44%"/>
		<display:column   property="code" title="<CENTER>Code</CENTER>"  class="textAlignLeft" style=" width:4%"/>	
		<display:column    property="actPrevYr" title='<%=request.getAttribute("act_PrevYr").toString()%>' class="textAlign" style=" width:10%"/>
		  <display:column   property="budgetCurYr" title='<%=request.getAttribute("budget_CurYr").toString()%>'  class="textAlign"  style=" width:10%"/>
		  <display:column   property="actUptoDec" title='<%=request.getAttribute("act_UptoDec").toString()%>' class="textAlign"  style=" width:10%"/>
		  <display:column   property="revisedBudgetCurYr" title='<%=request.getAttribute("revisedBudget_CurYr").toString()%>'  style=" width:10%" class="textAlign" />			
		 <display:column   property="budgetNextYr" title='<%=request.getAttribute("budget_NextYr").toString()%>' class="textAlign"  style=" width:10%"/>
		  	 
		
		 <display:setProperty name="export.pdf" value="false" />
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
 <!-- /div></div></div></td></tr></table -->
</body>
</html>
