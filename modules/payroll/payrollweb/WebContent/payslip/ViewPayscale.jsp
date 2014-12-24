<%@ include file="/includes/taglibs.jsp" %>
<%@page import="java.util.*,
                java.util.Map,
                java.util.Set,
                org.egov.infstr.utils.*,
                java.math.BigDecimal,org.egov.payroll.model.*,org.egov.infstr.utils.NumberToWord"%>


  <html>
 <head>
 	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 	<title>Acknowledgement Page</title>

  
  <script>

<%	PayScaleHeader payHeader = (PayScaleHeader) session.getAttribute("payHeader");
	String type = (String)session.getAttribute("payscaleModify");
	String mode = "";
	if("payscaleModify".equals(type))
		mode = "modify";
	else if("payscaleCreate".equals(type))
		mode = "create";
	else
		mode= "view";

%> 

function onBodyLoad()
 {
	<%
		ArrayList salaryCodes=(ArrayList)EgovMasterDataCaching.getInstance().get("pay-salaryCodes");
	%>
 }   

  function printpage(obj)
  {
  obj.style.visibility = "hidden";
  document.getElementById("back").style.visibility ="hidden";  
  window.print();
  obj.style.visibility = "visible";
  document.getElementById("back").style.visibility ="visible";  
  
  }



  </script>
  </head>
 <body onLoad="onBodyLoad();">



 <form name="modFrom" method="post" action="#">


<img src="${pageContext.request.contextPath}/images/egovlogo-pwr-trans.gif" width="70" height="70" border="0" align="left"/>
<br>
<br>
 <div align="center">
   <center>
    <p align="center"><b>eGov Payroll System <br/>
	<%=session.getAttribute("cityname")%>
	</b></p>	
	 <br>
    <table align='center' width="600">
    <tr>
         <td class="labelcell">Name</td>
		 <td class="labelcell">
			<c:out value="${payHeader.name}" escapeXml="false"/>
		 </td>
         <td class="labelcell">
			 Pay Commission
		 </td>	
		 <td class="labelcell">	 
			 <c:out value="${payHeader.payCommision.name}" escapeXml="false"/>
		 </td>
      </tr>
      <tr>
		  <td class="labelcell">Amount from/to</td>
		  <td class="labelcell"><c:out value="${payHeader.amountFrom}" escapeXml="false"/>/<c:out value="${payHeader.amountTo}" escapeXml="false"/> </td>
		  <td class="labelcell">Type</td>
			
			<td class="labelcell"><c:out value="${payHeader.type}" escapeXml="false"/></td>
		  </td>
         </tr>
         <tr>
           <td class="labelcell">Effective From</td>
		   <td class="labelcell"><fmt:formatDate value="${payHeader.effectiveFrom}" pattern="dd/MM/yyyy" /></td>
           <td class="labelcell">Current Basic</td>
		   <td class="labelcell"><c:out value="${paystructure.currBasicPay}" escapeXml="false"/></td>
         </tr>
           
         </table>
      <br>
  <table border="1" width="600" border="1" cellPadding="0" cellSpacing="0">
  <tr>
  <td align="center">PayHead</td>
  <td align="center">Percentage</td>
  <td align="center">Amount</td>
  </tr>
  <c:forEach var="sal" items="<%=salaryCodes%>" varStatus="s">
    <c:forEach var="payDetail" items="${payHeader.payscaleDetailses}" >
     <c:if test = "${sal.id==payDetail.salaryCodes.id}">
	 <tr>
		 <td align="center"><c:out value="${sal.head}" escapeXml="false"/></td>
		 <c:if test = "${sal.calType=='MonthlyFlatRate'}">
		   <td align="center">-----</td>
		 </c:if>
		 <c:if test = "${sal.calType=='SlabBased'}">
			 <td align="center">-----</td>
		 </c:if>
		 <c:if test = "${sal.calType=='ComputedValue'}">
		   	  <td align="center"><c:out value="${payDetail.pct}" escapeXml="false"/></td>
		 </c:if>
		     <c:set var="totalAmount" value="${payDetail.amount+totalAmount}"/>
            <td align="right"><c:out value="${payDetail.amount+'0.00'}" escapeXml="false"/></td>

	  </tr>
	  </c:if>
	  </c:forEach>
		 </c:forEach>
		 </table>
		 
	
	<table border=1 width=600 border="1" cellPadding=0 cellSpacing=0>
	<tr><td colspan="3" align="center">Increment Details</td></tr>
	  <tr>
	  <td align="center"><bean:message key="incSlabFrmAmt"/></td>
	  <td align="center"><bean:message key="incSlabToAmt"/></td>
	  <td align="center"><bean:message key="Increment"/></td>
	  </tr>	  
       <c:forEach var="incrSlabs" items="${payHeader.incrSlabs}" >	    
	 <tr>
	 <td align="center"><c:out value="${incrSlabs.incSlabFrmAmt}" escapeXml="false"/></td>
	 <td align="center"><c:out value="${incrSlabs.incSlabToAmt}" escapeXml="false"/></td>
	 <td align="center"><c:out value="${incrSlabs.incSlabAmt}" escapeXml="false"/></td>			 
	  </tr>		  
	  </c:forEach>
		 
	 </table>	 
	<%
			 BigDecimal grossTotalpay = new BigDecimal(0);
			 for(Iterator iter = payHeader.getPayscaleDetailses().iterator(); iter.hasNext();)
			 {
			 	PayScaleDetails payDetails = (PayScaleDetails)iter.next();
				grossTotalpay = grossTotalpay.add(payDetails.getAmount());
			}%>
			 <table align='center' width="500">
			    <tr>
			    <br>
			 	<td><p  style="text-align:right;">Gross Pay&nbsp;:&nbsp;<c:out value="${totalAmount+'0.00'}" /></p></td>
			 	</tr>
			<tr>
			   <td><p  style="text-align:right;">Amount in Words&nbsp;:&nbsp;<%=NumberToWord.translateToWord(grossTotalpay.toString())%></p></td>
			</tr>
	
	</table>
	
     
  	  </center>
      </div>
      </div>
     </form>

   
   </html>
