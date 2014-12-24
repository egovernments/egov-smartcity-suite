<%@ include file="/includes/taglibs.jsp" %>
<%@page	import="java.util.*,
				org.egov.payroll.utils.PayrollManagersUtill
				"%>
<html>
<head>

	<title>Gratuity compute </title>

	<style type="text/css">
		#codescontainer {position:absolute;left:11em;width:9%}
		#codescontainer .yui-ac-content {position:absolute;width:80%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
		#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:40%;background:#a0a0a0;z-index:9049;}
		#codescontainer ul {padding:5px 0;width:80%;}
		#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
		#codescontainer li.yui-ac-highlight {background:#ff0;}
		#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
</style>

<%
	String mode = request.getParameter("mode");
	List financialYears = PayrollManagersUtill.getCommonsManager().getAllActivePostingFinancialYear();
	request.setAttribute("financialYears",financialYears);

%>
<script language="JavaScript"  type="text/JavaScript">
	
	
	function onBodyLoad(){ 
		var msg = '<%=request.getAttribute("alertMessage")%>';		
		if(msg != 'null')
			alert(msg);
	}	

	function validateOnSearch(){		
		if(document.pensionForm.month.value == "0"){
			alert("Slect month");
			document.pensionForm.month.focus();
			return false;
		}
		if(document.pensionForm.financialsYear.value == "0"){
			alert("Slect financialsYear");
			document.pensionForm.financialsYear.focus();
			return false;
		}
	   var mode = "<%= mode%>";
	   document.forms("pensionForm").action ="${pageContext.request.contextPath}/pension/generateBatchPensionDisburse.do?mode="+mode;	   
	   document.forms("pensionForm").submit();
	}
		
    


</script>

</head>
<body onLoad="onBodyLoad();">
<html:form action ="/pension/generateBatchPensionDisburse" >
 	
  <table style="width: 800; " align="center" cellpadding="0" cellspacing="0" border="1" id="employeeTable" >
   	<tr>
	    <td colspan="6" height=30 bgcolor=#bbbbbb align=middle  class="tablesubcaption">Pension Disbursement</td>
    </tr>
	<tr>	
  		<td class="labelcell">Month<font color="red">*</font></td>
	    <td class="labelcellbig">
			<select name="month" id="month" style="width:70px">
				<option value="0">Choose</option>
				<option value="1">JAN</option>
				<option value="2">FEB</option>
				<option value="3">MAR</option>
				<option value="4">APR</option>
				<option value="5">MAY</option>
				<option value="6">JUN</option>
				<option value="7">JUL</option>
				<option value="8">AUG</option>
				<option value="9">SEP</option>
				<option value="10">OCT</option>
				<option value="11">NOV</option>
				<option value="12">DEC</option>
	    	</select>
	    </td>
	    <td class="labelcell">Year<font color="red">*</font></td>
	    <td>	
	    	<select name="financialsYear" id="year" style="width:70px" >
				<option value="0">Choose</option>
				<c:forEach var="financialYearObj" items="${financialYears}">
				<c:if test = "${financialYearObj.isActive=='1'}">
				<option value="${financialYearObj.id}">${financialYearObj.finYearRange}</option>
	    		</c:if>
	    	</c:forEach>
	     	</select>
	    </td>
	 </tr>
  </table>
  <table style="width: 800; " align="center" cellpadding="0" cellspacing="0" border="0" id="sumbitTable" >
    <tr>
	    <td class="labelcell"></td>
    	<td class="labelcell" align="center">	
		  <% if("create".equals(mode)){	%>	
    	 	<input type="button" class="button" name="create" value="Disburse Pension" onclick="return validateOnSearch();"/>
		  <% }if("view".equals(mode)){	%>		
			<input type="button" class="button" name="create" value="View Disburse Pension" onclick="return validateOnSearch();"/>
		  <% }	%> 
		</td>
   	</tr>
   </table>
   
   
</html:form>
</body>
</html>
