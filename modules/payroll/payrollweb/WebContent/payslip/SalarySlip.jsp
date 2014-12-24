<%@ include file="/includes/taglibs.jsp" %>


<%@page import="java.util.*,
                java.util.Map,
                java.util.Set,org.egov.payroll.utils.*,
                java.math.BigDecimal,org.egov.payroll.model.*,
                org.egov.payroll.rules.PayheadRuleUtil,
		org.egov.payroll.dao.*,org.egov.infstr.utils.*,org.egov.lib.admbndry.BoundaryDAO,
				org.egov.pims.model.BankDet,org.egov.pims.model.PersonalInformation,org.egov.pims.model.Assignment,
				org.egov.commons.Functionary,org.egov.lib.admbndry.CityWebsite,org.egov.lib.admbndry.CityWebsiteDAO,
				org.egov.infstr.utils.*,org.egov.lib.admbndry.BoundaryDAO,org.egov.infstr.services.PersistenceService,
				org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.web.context.WebApplicationContext"%>




  <html>
 <head>
<% 
ArrayList salaryCodes=(ArrayList)EgovMasterDataCaching.getInstance().get("pay-salaryCodes");
//System.out.println(">>>>>>>>>>>>>>>>>>> salaryCodes  " + salaryCodes.size());
ArrayList chartOfAccounts=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-accountCodes");
//System.out.println(">>>>>>>>>>>>>>>>>>> chartOfAccounts  " + chartOfAccounts.size());

PayrollExternalInterface payrollExternalInterface=new PayrollExternalImpl();
List finYearList = (List)payrollExternalInterface.getAllFinancialYearList();

EmpPayroll paySlip = (EmpPayroll) session.getAttribute("paySlip");
WebApplicationContext webAppContext =WebApplicationContextUtils.getWebApplicationContext(application);
PersistenceService persistenceService = (PersistenceService) webAppContext.getBean("persistenceService");
paySlip=(EmpPayroll)persistenceService.find("from EmpPayroll where id=?", paySlip.getId());
Set<BankDet> bankDetails=paySlip.getEmployee().getEgpimsBankDets();
PayStructure payStructure  = (PayStructure)PayrollManagersUtill.getPayRollService().getPayStructureForEmpByDate(paySlip.getEmployee().getIdPersonalInformation(),paySlip.getToDate());		

request.setAttribute("bankDetails",bankDetails);
BankDet bankdet=null;
for(Iterator itr1=bankDetails.iterator(); itr1.hasNext();)
{
	bankdet=(BankDet)itr1.next(); 
	//System.out.println(">>>>>>>>>>>>>>>>>>> bankdet="+bankdet.getBank().getName());
	break; //An employee is mapped to only one bank
}

Assignment assignment=paySlip.getEmpAssignment();
Functionary functionary=assignment.getFunctionary();
request.setAttribute("functionary",functionary);
Set<Earnings> earnings=paySlip.getEarningses();
Set<Deductions> deductions=paySlip.getDeductionses();
List deductionList = new ArrayList(deductions);
List earningList = new ArrayList(earnings);
Collections.sort(earningList, Earnings.SalarycodeComparator);
BigDecimal percentageDA = new BigDecimal(0);
percentageDA=new PayheadRuleUtil().getPercentagebyBulkRule("DA", paySlip.getToDate(),paySlip.getEmployee().getGroupCatMstr().getId());
BigDecimal percentageHRA = new BigDecimal(0);
percentageHRA=new PayheadRuleUtil().getPercentagebyBulkRule("HRA", paySlip.getToDate(),null);
//System.out.println(">>>>>>>>>>>>>>>>>>> percentage="+percentage);
String gpfNo="";
String soc1="";
String soc2="";
double licTotal=0.0;

for(Iterator itr=deductions.iterator(); itr.hasNext();)
{
   Deductions ded = (Deductions)itr.next();
   if(ded.getSalaryCodes() != null)
   {
	   if(ded.getReferenceno() !=null)
	   {
		if(ded.getSalaryCodes().getHead().equalsIgnoreCase("GPFSUB"))
			gpfNo=ded.getReferenceno();
		else if(ded.getSalaryCodes().getHead().equalsIgnoreCase("SOCNMC"))
			soc1=ded.getReferenceno();
		else if(ded.getSalaryCodes().getHead().equalsIgnoreCase("SOCMEHTAR"))
			soc2=ded.getReferenceno();
	  }
	  if(ded.getSalaryCodes().getHead().equalsIgnoreCase("LIC"))
		licTotal=licTotal+ded.getAmount().doubleValue();	  
   }	
}

request.setAttribute("earnings",earningList);
request.setAttribute("deductions",deductionList);
String cityLogo=(String)session.getAttribute("cityLogo");


			if(cityLogo==null)
			{
			String cityurl=(String)request.getSession().getAttribute("cityurl");
			CityWebsite cityWebsite = new CityWebsiteDAO().getCityWebSiteByURL(cityurl);
				cityLogo=cityWebsite.getLogo();
			}

%>
 	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 	<title>Acknowledgement Page</title>

  </script>     
  <script>

  function printpage(obj)
  {
    document.getElementById("printButton").style.display="none";
    window.print();
  }

 <%String m_names[] = {"","January", "February", "March",
  "April", "May", "June", "July", "August", "September",
  "October", "November", "December"};%>


  </script>
  </head>
 <body class="body" >


 <table width="95%" cellpadding="0" cellspacing="0" border="0" id="table2" >
	 <tr>
	 <td>
	
	<!-- Body Begins -->

 <form name="modFrom" method="post" action="#">

<!--<img src="${pageContext.request.contextPath}/images/egovlogo-pwr-trans.gif" width="70" height="70" border="0" align="left"/>-->


 <div align="center">
   <center>
    <p align="center">
   <img src="${pageContext.request.contextPath}/images/<%=cityLogo%>" width="70" height="70" border="0" align="center"/>
   <br>
<br>
   <h1><b><%=session.getAttribute("cityname")%> </h1>Payslip For the month of <%=m_names[paySlip.getMonth().intValue()]%>&nbsp;
					<c:forEach var="fin" items="<%= finYearList%>" >
						<c:if test = "${fin.id==paySlip.financialyear.id}">		           
						<c:out value="${fin.finYearRange}"/> 
		             </c:if>
					</c:forEach>
	</br>
	</b></p>
	 
	 <br>
    <table align='center' width="600">
    <tr>
         <td class="labelcell">Employee Code</td>
		 <td class="labelcell"><b>	<c:out value="${paySlip.employee.employeeCode}" escapeXml="false"/></b></td>
		 <td class="labelcell">Employee Name</td>
         <td width="230"><b>
         <c:if test="${paySlip.employee.gender == 'M'}"> <c:out value="Mr."/></c:if>
         <c:if test="${paySlip.employee.gender == 'F'}"> <c:out value="Ms."/></c:if>
         <c:out value="${paySlip.employee.employeeFirstName}" escapeXml="false"/>&nbsp;<c:out value="${paySlip.employee.employeeMiddleName}" escapeXml="false"/>&nbsp;<c:out value="${paySlip.employee.employeeLastName}" escapeXml="false"/></b></td>
         </tr>
                
         <tr>                  
           <td class="labelcell">Designation</td>
		   <td class="labelcell"><c:out value="${desig.designationName}" escapeXml="false"/> </td>
		   <td class="labelcell">PayScale/Grade Pay</td>
		   	  <td class="labelcell"><%=payStructure.getPayHeader().getName()%> 
      </td>
         </tr>
       <!--  <tr>
			<td class="labelcell">Month	</td>
		  <td class="labelcell">
		  <%=m_names[paySlip.getMonth().intValue()]%></td>
		   <c:forEach var="fin" items="<%= finYearList%>" >
		            <c:if test = "${fin.id==paySlip.financialyear.id}">
		           <td class="labelcell">Year </td>
         <td class="labelcell"><c:out value="${fin.finYearRange}"/> </td>
		             </c:if>
		</c:forEach>
         </tr>	-->
         <tr>
        <td class="labelcell">Department Name</td>
       	 		   <td class="labelcell"> <c:out value="${depart.deptName}" escapeXml="false"/></td>
	       <td class="labelcell">Attendance</td>
	       <td class="labelcell">
	 		${paySlip.numdays}/${paySlip.workingDays}
    </td>
     </tr> 
     <tr><td>&nbsp;</td>
	<td>&nbsp;</td></tr>
	<tr><td>&nbsp;</td>
	<td>&nbsp;</td></tr>

     <tr>       
     	 <td width="400"><b>GPF No : <c:out value="${paySlip.employee.gpfAcNumber}"/></b></td>
     	<td  width="400"><b>Society 1 No : <%=soc1%></b></td>
     	<td  width="400"><b>Society 2 No : <%=soc2%></b></td>
	</tr>	
	 </table>
      <br>
  <table width=60" border="0" cellPadding=0 cellSpacing=0>
   <tr>
                  <td colspan="4" class="shadowwk"></td>
                </tr>

 <tr>
  <td align="center" class="labelcellforbg"><b>&nbsp;&nbsp;&nbsp;Earnings</b></td>
  <td align="center" class="labelcellforbg"><b>&nbsp;&nbsp;&nbsp;Deductions</b></td>
  </tr>
  <tr>
                 <td colspan="4" class="shadowwk"></td>
               </tr>

  <tr valign=top>
      <td width="452">
          <table width="300">
          <c:forEach var="sal" items="<%=salaryCodes%>" varStatus="s">
		  <c:forEach var="earnings" items="${earnings}" >
			 <c:if test = "${sal.id==earnings.salaryCodes.id}">
			  <c:set var="totalEarnings" value="${earnings.amount+totalEarnings}"/>			
			  <tr>					  
			  		<c:if test="${(earnings.salaryCodes.head!='DA') && (earnings.salaryCodes.head!='HRA')}">						
			  			<td><p  style="text-align:left;">&nbsp;<c:out value="${sal.description}"/></p></td>			  			
					</c:if>					
					<c:if test="${earnings.salaryCodes.head=='DA'}">	
						<td width="60"><p  style="text-align:left;">&nbsp;<c:out value="${sal.description}"/>(<%=percentageDA%>%)</p></td>
					</c:if>
					<c:if test="${earnings.salaryCodes.head=='HRA'}">	
						<td width="60"><p  style="text-align:left;">&nbsp;<c:out value="${sal.description}"/><%=percentageHRA.intValue()>0?"("+percentageHRA+")%":""%></p></td>
					</c:if>
					<c:if test="${(earnings.salaryCodes.head=='BASIC') || (earnings.salaryCodes.head=='GP')}">
					     <c:set var="totalBasicPlusGP" value="${earnings.amount+totalBasicPlusGP}"/>
						<td  width="60" colspan="2"><p  style="text-align:right;"><c:out value="${earnings.amount+'0.00'}"/>&nbsp;</p></td>					
														
					</c:if>
					<c:if test="${(earnings.salaryCodes.head!='BASIC') && (earnings.salaryCodes.head!='GP')}">
						<td  width="130" colspan="3"><p  style="text-align:right;"><c:out value="${earnings.amount+'0.00'}"/>&nbsp;</p></td>					
															
					</c:if>										
					<c:if test="${earnings.salaryCodes.head=='GP'}">
					<tr>
					  <td class="labelcell"><p  style="text-align:left;"><b>&nbsp;Total(Basic+GP)</b></p></td>
					 <td  width="130" colspan="3"><p  style="text-align:right;"><b><c:out value="${totalBasicPlusGP+'0.00'}"/>&nbsp;</b></p></td>
					 </tr>
					</c:if>
					
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
           <c:set var="count" value="1"/>
           <c:set var="licAmount" value="<%=new BigDecimal(licTotal)%>"/>	
          <c:forEach var="sal" items="<%=salaryCodes%>" >
           <c:forEach var="ded" items="${deductions}" >
		  	 <c:if test = "${sal.id==ded.salaryCodes.id}">
			  <c:set var="totalDedAmount" value="${ded.amount+totalDedAmount}"/>				  
                                                              
                 <c:if test = "${ded.salaryCodes.head!='LIC'}">	
                 <tr>
                 <td class="labelcell"><p  style="text-align:left;">&nbsp;<c:out value="${sal.description}"/>
                 <c:if test = "${not empty  ded.advanceScheduler}">	
		   			<c:set var="instNo" value="${ded.advanceScheduler.installmentNo}"/>		   
		   			<c:set var="instTotal" value="${ded.salAdvances.numOfInst}"/>	
		   			<c:out value="(${instNo}/${instTotal})"/>
 				</c:if>
                 </p></td>
                 
                  <td class="labelcell"><p  style="text-align:right;"><c:out value="${ded.amount+'0.00'}"/>&nbsp;</p></td>
                  
                  </tr>
                  </c:if>
                  <c:if test = "${ded.salaryCodes.head=='LIC'}">	
                   <c:if test = "${count==1}">	
                   <tr>
                   <td class="labelcell"><p  style="text-align:left;">&nbsp;<c:out value="${sal.description}"/>
                 	</p></td>
		        <td class="labelcell"><p  style="text-align:right;"><c:out value="${licAmount+'0.00'}"/>&nbsp;</p></td>
		        </tr>
		    </c:if>
		    <c:set var="count" value="2"/>		    
                  </c:if>		  
                 </c:if>
			  </c:forEach>
            </c:forEach>
            <c:forEach var="accounts" items="<%=chartOfAccounts%>" >
			<c:forEach var="ded" items="${paySlip.deductionses}" >
			 <c:if test = "${accounts.id==ded.chartofaccounts.id}">
			  <c:set var="totalaccountAmount" value="${ded.amount+totalaccountAmount}"/>
			  <tr>
				  <td class="labelcell"><p  style="text-align:left;"><c:out value="${accounts.name}"/></p></td>
				   <td class="labelcell"><p  style="text-align:right;"><c:out value="${ded.amount+'0.00'}"/></p></td>

			  </tr>
				 </c:if>
			  </c:forEach>
			</c:forEach>
          </table>

      </td>
  </tr>
  <tr>
  <td class="labelcellforbg"><b><p  style="text-align:right;">Gross Pay&nbsp;:&nbsp;<c:out value="${totalEarnings+'0.00'}" />&nbsp;</p></b></td>
  <td><b><p  style="text-align:right;">Total Deductions&nbsp;:&nbsp;<c:out value="${totalDedAmount+totalaccountAmount+'0.00'}" />&nbsp;</p></b></td>
  </tr>

  <tr>
                <td colspan="4" class="shadowwk"></td>
              </tr>

  </table>
   <table align='center' width="590">
   <tr >
   <br>
	<td width="900" colspan="3"><p  style="text-align:left;">Net Amount Payable&nbsp;:&nbsp;<%= '\u20B9'%>&nbsp;<b><c:out value="${totalEarnings-(totalDedAmount+totalaccountAmount)+'0.00'}"/></b>&nbsp;&nbsp;(<%='\u20B9'%>&nbsp;<%=NumberToWord.translateToWord(paySlip.getNetPay().toString()).replace("Rupees", "")%>)</p></td>
	</tr>
	<tr><td>&nbsp;</td>
			<td>&nbsp;</td></tr>
	
	<tr><td>&nbsp;</td>
	<td>&nbsp;</td></tr>
	<tr>
	<tr>
		 <td width="400"><b>Bank : <%=(bankdet!=null)?bankdet.getBank().getName():""%></b></td>
		<td  width="400"><b>Bank Branch : <%=(bankdet!=null && bankdet.getBranch() != null)?bankdet.getBranch().getBranchname():""%></b></td>
		<td  width="400"><b>Bank Account #: <%=(bankdet!=null && bankdet.getAccountNumber()!=null)?bankdet.getAccountNumber():""%></b></td>
	</tr>
	<tr><td>&nbsp;</td>
			<td>&nbsp;</td></tr>
	<tr>
	   <td class="labelcellforbg"><p  style="text-align:left;">This is a computer generated payslip&nbsp;&nbsp;</p></td>
	</tr>
	</table>
      <table align='center' width="500">
      <br><br><br>
        	 <tr><td>
        	 <P align="center">
        	 <input type="button" class="Buttonfinal" name="printButton" onclick="printpage(this)" value="PRINT" id="printButton" />
        	 <!--  <input class="button" type="button" name="home" value="Home" onclick="window.location = '/staff/index.jsp';"/>	-->

  	  </tr>

  	  </table>
  	  </center>
      
     
     </form>
			<% 	session.removeAttribute("paySlip");
			session.removeAttribute("financialYear");
			session.removeAttribute("deductionsTaxList");
			session.removeAttribute("deductionsAdvList");
			session.removeAttribute("deductionsotherList");
			session.removeAttribute("empPayroll");
			session.removeAttribute("payHeader");
				 %>
       </div>
         </td></tr>
         <!-- Body Section Ends -->
         </table>
         </body>
   </html>

