<%@page import="java.util.*,
                java.util.Map,
                java.util.Set,
                java.math.BigDecimal,org.egov.payroll.model.*,org.egov.infstr.utils.NumberToWord"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>

  <html>
 <head>

 	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 	<title>Acknowledgement Page</title>

  <script language="JavaScript" src="/dhtml.js" type="text/JavaScript"></script>
  <script>

  function printpage(obj)
  {  
	  window.print();
  }
  </script>
  </head>
 <body >

 <table align='center'>
 <tr>
 <td>

 <!-- Tab Navigation Begins -->
 <center>

 </center>
 <!-- Tab Navigation Ends -->

 <!-- Body Begins -->

 <form name="modFrom" method="post" action="#">

<img src="/images/egovlogo-pwr-trans.gif" width="70" height="70" border="0" align="left"/>
<br>
<br>
 <div align="center">
   <center>
    <p align="center"><b>eGov Payroll System
   <br>Bangalore</br>
	</b></p>
	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;
	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;
	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 <br>
  <table align='center' width="600">
    <tr>
         <td>Employee Code&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;
         	<c:out value="${payslip.employee.employeeCode}" escapeXml="false"/>
         </td>
	     <td>
	         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	         Name&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;
	         <c:out value="${payslip.employee.employeeFirstName}" escapeXml="false"/>&nbsp;
	         <c:out value="${payslip.employee.employeeMiddleName}" escapeXml="false"/>&nbsp;
	         <c:out value="${payslip.employee.employeeLastName}" escapeXml="false"/>
         </td>
    </tr>
    <tr>         
	 	<td>Department&nbsp;Name&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;
	 		<c:out value="${payslip.empAssignment.deptId.deptName}"/>	 		
	 	</td>
		<td>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	       	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Designation&nbsp;&nbsp;:&nbsp;
	       	<c:out value="${payslip.empAssignment.desigId.designationName}" escapeXml="false"/></td>
	 </tr>
	 <tr>         
	 	 <td>Fund&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 	 		 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;
	 	 	<c:out value="${payslip.empAssignment.fundId.name}" escapeXml="false"/>
	 	 </td>
		 <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	       	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Function
	       	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;
	 	 	<c:out value="${payslip.empAssignment.functionId.name}" escapeXml="false"/>
	 	 </td>
	 </tr>	
	 <tr>
	 	<td>
	       	Fild&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	       	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	       	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;	 	 	
	 	 </td>
	 	 <td>
	 	 </td>
	 </tr>
  </table>
 
  <table border=1 width=600 border="1" cellPadding=0 cellSpacing=0>
	  <tr>
		  <td align="center">Salary Details</td>
		  <td align="center">Deductions</td>
	  </tr>
	  <tr valign=top>
	      <td width="452">
		       <table width="300">
		       <c:forEach var="salObj" items="${salaryCodes}">		       
  				 <c:forEach var="earning" items="${payslip.earningses}" >
					<c:if test="${salObj.id==earning.salaryCodes.id}">
					 <c:set var="totalEarnings" value="${earning.amount+totalEarnings}"/>		      			 
				      <tr>
		          	  	 <td><p  style="text-align:left;">
		          	  	 	<c:out value="${earning.salaryCodes.head}"/></p>
		          	  	 </td>
			             <td><p  style="text-align:right;">
			             	<c:out value="${earning.amount + '0.00'}"/></p>
			             </td>
			          </tr>
     		         </c:if>
 				  </c:forEach>
				</c:forEach>
			   <tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
		        </tr>
		         <tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
		        </tr>          
		      </table>
	      </td>
	      <td>
		  	<table width="300">
		        <c:forEach var="deduction" items="${payslip.deductionses}">
		         <c:if test = "${deduction.salaryCodes != null}">	
		         	<c:set var="totalDedAmount" value="${deduction.amount+totalDedAmount}"/>        			
		             <tr>
		                 <td><p  style="text-align:left;"><c:out value="${deduction.salaryCodes.head}"/></p></td>
		                 <td><p  style="text-align:right;"><c:out value="${deduction.amount + '0.00'}"/></p></td>
		             </tr>
		        </c:if>	         
		        </c:forEach>    
		        <c:forEach var="otherDeduction" items="${payslip.deductionses}">
		         <c:if test = "${otherDeduction.chartofaccounts!=null}">	
		         	<c:set var="totalOtherDedAmount" value="${otherDeduction.amount+totalOtherDedAmount}"/>	        			
		             <tr>
		                 <td><p  style="text-align:left;"><c:out value="${otherDeduction.chartofaccounts.name}"/></p></td>
		                 <td><p  style="text-align:right;"><c:out value="${otherDeduction.amount + '0.00'}"/></p></td>
		             </tr>
		         </c:if>		         
		        </c:forEach>    		           
	         </table>	
	      </td>
  	 </tr>
  	 <tr>
  		<td><p  style="text-align:right;">Gross Pay&nbsp;:&nbsp;
  			<c:out value="${totalEarnings+'0.00'}" />
  		</td>
  		<td><p  style="text-align:right;">Total Deductions&nbsp;:&nbsp;
  			<c:out value="${totalDedAmount + totalOtherDedAmount + '0.00'}"/>
  		</td>
  	 </tr>
  </table>
  
  <table align='center' width="500">
  	<tr>
	   <br>
		<td><p  style="text-align:right;">Net Pay&nbsp;:&nbsp;
			<c:out value="${totalEarnings - (totalDedAmount+totalOtherDedAmount)}"/>			
		</td>
	</tr>	
  </table>
  <table align='center' width="500">
     <br><br><br>
      <tr>
      		<td>
       	 <P align="center">
       	  <input type="button" name="printButton" onclick="printpage(this)" value="PRINT" />
 	  </tr>
  </table>
 
	  
  	  </center>
      </div>
      </div>
</form>

      </div></div></div>
         </td></tr>
         <!-- Body Section Ends -->
         </table>
         </body>
   </html>
