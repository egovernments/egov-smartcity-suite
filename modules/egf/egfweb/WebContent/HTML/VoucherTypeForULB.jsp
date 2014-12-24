/*
This File reads the VoucherNumber Generation Type From egf_Config.xml For the ULB.
Pass The Type of Voucher Tobe generated is 'Journal','Contra','Payment' Or 'Receipt'
It will read the Value from egf_config.xml for the current ULB And Returns 'Auto'or 'Manual' which is mentioned in egf_config.xml
1. If Auto Based on the mode it hides the VoucherNumber and ReverseVoucherNumber fields 
2. If Manual It continues as usual
3. Department mandatory check is included with this

*/


<%@ page import="com.exilant.eGov.src.transactions.VoucherTypeForULB;" %>


<% 
System.out.println("Calling VoucherTypes");
VoucherTypeForULB voucherType=new VoucherTypeForULB();
String vt=(String)request.getParameter("vType");
String vType=(String)voucherType.readVoucherTypes(vt);
String deptMandatory=(String)voucherType.readIsDepartmentMandtory();
System.out.println("Called VoucherTypes---->"+vType);
String vid="";
String vText="";
String vidRev="";
String vTextRev="";
String jvid="";
String jvText="";
String jvidRev="";
String jvTextRev="";
String deptId="";
String deptStar="";
String isPayment="NO";
%>
var deptMandatory='<%=deptMandatory%>';
 Type='<%=vType%>';
	var showMode=PageManager.DataService.getQueryField("showMode");
	if(Type=="Auto")
	{
	<%
		 vid=(String)request.getParameter("VoucherNumberNew");
		 vText=(String)request.getParameter("VoucherText");
		 vidRev=(String)request.getParameter("VoucherNumberRev");
		 vTextRev=(String)request.getParameter("VoucherTextRev");
		 jvid=(String)request.getParameter("JVoucherNumberNew");
		 jvText=(String)request.getParameter("JVoucherText");
		 jvidRev=(String)request.getParameter("JVoucherNumberRev");
		 jvTextRev=(String)request.getParameter("JVoucherTextRev");
		 deptId=(String)request.getParameter("DepartmentId");
		 deptStar=(String)request.getParameter("DepartmentStar");
		 isPayment=(String)request.getParameter("isPayment");
	%>
		if(showMode=='new')
		{
		document.getElementById('<%=vid%>').style.display="none";
		document.getElementById('<%=vText%>').style.display="none";
		document.getElementById('<%=vid%>').readOnly=true;
		document.getElementById('<%=vid%>').removeAttribute("exilMustEnter");
		
		<%//this only for payment Interfund JVS
			if(isPayment!=null && isPayment.equalsIgnoreCase("YES")){%> 
		document.getElementById('<%=jvid%>').style.display="none";
		document.getElementById('<%=jvText%>').style.display="none";
		document.getElementById('<%=jvid%>').readOnly=true;
		document.getElementById('<%=jvid%>').removeAttribute("exilMustEnter");
		<%} %>
		}
		else if(showMode=='modify')
		{
		document.getElementById('<%=vidRev%>').style.display="none";
		document.getElementById('<%=vTextRev%>').style.display="none";
		document.getElementById('<%=vidRev%>').readOnly=true;
		document.getElementById('<%=vidRev%>').removeAttribute("exilMustEnter");
		<%//this only for payment Interfund JVS
			if(isPayment!=null && isPayment.equalsIgnoreCase("YES")){
			%> 
		document.getElementById('<%=jvidRev%>').style.display="none";
		document.getElementById('<%=jvTextRev%>').style.display="none";
		document.getElementById('<%=jvidRev%>').readOnly=true;
		document.getElementById('<%=jvidRev%>').removeAttribute("exilMustEnter");
		<%} %>
		}
		else
		{
		document.getElementById('<%=vid%>').readOnly=true;
		var obj=document.getElementById('<%=jvid%>')
		if(obj!=null && obj!=undefined)
		obj.readOnly=true;
		
		}
	}
	else if(Type=="Manual")
	{
		if(showMode=='modify')
		{
		document.getElementById('<%=vidRev%>').setAttribute('exilMustEnter','true');
		}
	}
//tocheck whether department is mandatory	
 
	if(deptMandatory=="Y")
	{
		var deptObj=	document.getElementById('<%=deptId%>');
		
		if(deptObj!=null && deptObj!=undefined)
		{
			deptObj.setAttribute('exilMustEnter','true');
		}
		
	}
	else
	{
	var deptStarObj=document.getElementById('<%=deptStar%>');
	if(deptStarObj!=null && deptStarObj!=undefined)
		{
			deptStarObj.style.display='none';
		}
	}
 