<%@ include file="/includes/taglibs.jsp" %>
<%@ page import="org.egov.payroll.dao.*,java.util.*,org.egov.infstr.*,org.egov.infstr.utils.*,org.egov.infstr.commons.dao.*,org.egov.infstr.commons.* "%>


<html>
<head>
	<title>Search Payscale</title>

<%
try{
	System.out.println("for checking============================================");
	ArrayList payScaleHeaderList=(ArrayList)EgovMasterDataCaching.getInstance().get("pay-payScaleHeader");
	System.out.println(">>>>>>>>>>>>>>>>>>> pay  " + payScaleHeaderList.size());
	String mode = request.getParameter("mode");
	System.out.println("mode--------"+request.getParameter("mode"));

%>
<script language="JavaScript"  type="text/JavaScript">
function validation(arg)
 {
	if(document.salaryPaySlipForm.payScaleName.value =="")
	{
		alert("Please Enter the PayScale Name!!!");
		document.salaryPaySlipForm.payScaleName.focus();
		return false;
	}
	if(arg=="viewPayScale")
	{
	 	document.forms[0].action ="${pageContext.request.contextPath}/payslip/viewPayScale.do?path=print&mode=payscale";
	}
	else if(arg=="modifyPayScale")
	{
		document.forms[0].action ="${pageContext.request.contextPath}/payslip/modifyPayScale.do?path=go&mode=payscale";
	}
	document.forms[0].submit();	
 }

</script>
</head>
<body >   

<html:form method="POST"	action="/payslip/viewGenPaySlips">
    <table width="95%" cellpadding ="0" cellspacing ="0" border = "0" >
	<tr>
			<%	if("view".equals(mode)){	%>	
	    		
	    		<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                  <div class="headplacer">View PayScale</div></td>
                  
	    	<%} %>	
	    	<%	if("modify".equals(mode)){	%>	
	    		
	    		<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                  <div class="headplacer">Modify PayScale</div></td>
	    		
	    	<%} %>	
	  </tr>
	  <tr>
			<td class="whiteboxwk"><span class="mandatory">*</span>PayScale Name</td>
			<td class="whitebox2wk"><html:select  property="payScaleName" styleClass="selectwk" >
						<html:option value="">----choose----</html:option>
						<c:forEach var="pay" items="<%=payScaleHeaderList%>" >
						<html:option value="${pay.id}">${pay.name}</html:option>
						</c:forEach>
	
			</html:select>
	 </td>
	  </tr>
	  </tr>
	  </table>
	  <br>
	   <table  width="95%" cellpadding ="0" cellspacing ="0" border = "0" >
	  <tr >
<%	if("view".equals(mode)){	%>	
	  <td align="center">
	  <input type="button" class="buttonfinal" name="view" value="View PayScale" onclick="return validation('viewPayScale');"/></p>  
	  </td>
<% } %>		  
<% if("modify".equals(mode)){ %>
	  <td align="center">	  		  
	  <input type="button" class="buttonfinal" name="modify" value="Modify PayScale" onclick="return validation('modifyPayScale');"/>
	  </td>
<% } %>	  
	</tr>
	
     </table>
     </html:form>
     <%
     }catch(Exception e)
     {
       e.printStackTrace();
     }
     %>
     
</body>
</html>
