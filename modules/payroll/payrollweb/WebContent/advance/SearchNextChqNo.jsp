<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ include file="/includes/taglibs.jsp" %>

<%@page  import="com.exilant.eGov.src.transactions.*,
java.util.HashMap,java.util.*"%>
<html>
	<head>	
		<title>Search Screen - Next Cheque Number</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<meta http-equiv="KEYWORDS" content="">
		<meta http-equiv="DESCRIPTION" content="">
		<META http-equiv=Pragma content=no-cache>
		<meta http-equiv="Expires" content="0">
		<meta http-equiv="Cache-Control" content="no-cache">

		

<script language='javascript'>

function onLoad()
{
	//alert("Hi");     
}
function getKeyInfo(obj)
{
	if(obj=='NO CHEQUES AVAILABLE!!')  
		obj='';
	
	window.returnValue=obj;		
	window.close();
}
</script>
</head>

	<body onload="onLoad()" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth ="0" bgColor=white>
	 <form name="frmChqNoSearch" action = "SearchNextChqNo.jsp" method="post">		
	<%
		if(!request.getParameter("accntNoId").equals(""))
		{
			NextChequeNumber nextCheque = new NextChequeNumber();
			ArrayList dataList=(ArrayList)nextCheque.getNextChequeNumber(request.getParameter("accntNoId"));		
			if(dataList!=null) 
			{	
				request.setAttribute("nextCheque",dataList);				

	%>
	
	<div class="tbl-container" id="tbl-container" style="width:300px;height:400px">
	<display:table cellspacing="0" name="nextCheque" id="currentRowObject" export="false" style="width:300px" sort="list" class="simple">	
	<display:column property="chqRange" class="labelcellwithborder" style="width:150px" title="Cheque Range"/>
	<display:column title="Next Cheque No" class="labelcellwithborder" style="width:100px">	
	 <%	 
	 	String code=(((HashMap)pageContext.getAttribute("currentRowObject")).get("nextChqNo").toString());		 
		%>
		<table><tr>
 		<td id="lblNextChqNo" class="labelcellwithborder" style="border:0px"><a onMouseOver="this.style.cursor='hand'" onClick="getKeyInfo('<%=code%>')"><%=code%></a></td>		
		</tr></table>	
	</display:column>
	 
	</display:table>
	</div>
<%	 
	}	
	}
%>
</form>
</body>
</html>
