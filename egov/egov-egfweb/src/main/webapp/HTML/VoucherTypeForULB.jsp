<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  ~
  --%>


/*
This File reads the VoucherNumber Generation Type From egf_Config.xml For the ULB.
Pass The Type of Voucher Tobe generated is 'Journal','Contra','Payment' Or 'Receipt'
It will read the Value from egf_config.xml for the current ULB And Returns 'Auto'or 'Manual' which is mentioned in egf_config.xml
1. If Auto Based on the mode it hides the VoucherNumber and ReverseVoucherNumber fields 
2. If Manual It continues as usual
3. Department mandatory check is included with this

*/


<%@ page import="com.exilant.eGov.src.transactions.VoucherTypeForULB,org.apache.log4j.Logger" %>


<% 
Logger LOGGER = Logger.getLogger("VoucherTypeForULB.jsp");
LOGGER.info("Calling VoucherTypes");
VoucherTypeForULB voucherType=new VoucherTypeForULB();
String vt=(String)request.getParameter("vType");
String vType=(String)voucherType.readVoucherTypes(vt);
String deptMandatory=(String)voucherType.readIsDepartmentMandtory();
LOGGER.info("Called VoucherTypes---->"+vType);
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
 
