<%@ include file="/includes/taglibs.jsp" %>
<%@page import="java.util.*,
                java.util.Map,
                java.util.Set,
                org.egov.infstr.utils.*,
                java.math.BigDecimal,org.egov.payroll.model.*,org.egov.infstr.utils.NumberToWord"%>


  <html>
 <head>
 	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 	<title>Payscale - View/Print</title>

  
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


<img src="../common/image/egovlogo-pwr-trans.gif" width="70" height="70" border="0" align="left"/>
<br>
<br>
 <div align="center">
   <center>
    <p align="center"><b>eGov Payroll System
   <br>
   <%=session.getAttribute("cityname")%>
	</b></p>
	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;
	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;
	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 <br>
	 <br>
	 <table>
    <table  width="100%" cellpadding ="0" cellspacing ="0" border = "0">
    <tr>
		<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                  <div class="headplacer">Payscale Details</div></td>
	</tr>
	
    <tr>
         <td><b>Payscale Name&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<c:out value="${payHeader.name}" escapeXml="false"/></td>
         <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
         <b>Pay Commission&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<c:out value="${payHeader.payCommision.name}" escapeXml="false"/></td>
         </tr>
        <tr>
		  <td><b>Amount From/To&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<c:out value="${payHeader.amountFrom}" escapeXml="false"/>/<c:out value="${payHeader.amountTo}" escapeXml="false"/> </td>
		  <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		  <b>Type&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;
		  <% if(payHeader.getType().equals("-1"))
        	{%>
				<c:out value="" escapeXml="false"/>
			<% 
			}
			else
			{%>
				<c:out value="${payHeader.type}" escapeXml="false"/></td>
			<%
			}%>
         </tr>
         <tr>
           <td><b>Effective From&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<fmt:formatDate value="${payHeader.effectiveFrom}" pattern="dd/MM/yyyy" /></td>
                      <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Rule Script&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<c:out value="${payHeader.ruleScript.description}"/></td>
         </tr>

         <tr>
           <td><b></td>
           <td></td>
         </tr>
  
         </table>
      <br>
  <table width="100%" border="0" cellPadding=0 cellSpacing=0 >
  <tr>
		<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                  <div class="headplacer">Payhead Details</div></td>
	</tr>
	
  <tr >
  <td align="center"><b>PayHead</td>
  <td align="center"><b>Percentage</td>
  <td align="center"><b>Amount</td>
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
            <td align="center"><c:out value="${payDetail.amount+'0.00'}" escapeXml="false"/></td>

	  </tr>
	  </c:if>
	  </c:forEach>
		 </c:forEach>
		 </table>
		 
	<br>
	<table width="100%" border="0" cellPadding=0 cellSpacing=0>
	<tr>
		<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                  <div class="headplacer">Increment Details</div></td>
	</tr>
		
	  <tr>
	  <td align="center"><b><bean:message key="incSlabFrmAmt"/></td>
	  <td align="center"><b><bean:message key="incSlabToAmt"/></td>
	  <td align="center"><b><bean:message key="Increment"/></td>
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
			 <table align='center' width="100%">
			    <tr>
			    <br>
			 	<td><b>Gross Pay&nbsp;:</b>&nbsp;<c:out value="${totalAmount+'0.00'}" /></p></td>
			 	</tr>
			<tr>
			   <td><b>Amount in Words&nbsp;:</b>&nbsp;<%=NumberToWord.translateToWord(grossTotalpay.toString())%></p></td>
			</tr>

		<br>	
	<tr>
                <td colspan="4" class="shadowwk"></td>
              </tr>
  

	</table>

      <table align='center' width="100%">
      <br><br><br>
        	 <tr><td>
        	 <P align="center">
        	  <input type="button" class="buttonfinal" name="printButton" onclick="printpage(this)" value="PRINT" />
		<%  if("create".equals(mode)){	%>
        	  <input class="buttonfinal" type="button" name="back" value="Back" onclick="window.location = '<%=request.getContextPath()%>/payslip/CreatePayScale.jsp?mode=<%=mode%>';"/>
		<%	}	
			if("view".equals(mode) || "modify".equals(mode)){	%>
        	  <input class="buttonfinal" type="button" name="back" value="Back" onclick="window.location = '<%=request.getContextPath()%>/payslip/payScaleSearch.jsp?mode=<%=mode%>';"/>
		<%	}	%>

  	  </tr>
  	   
  	  </table>
  	  </center>
      </div>
      </div>
     </form>

         
         </body>
         
   </html>
