<%@ include file="/includes/taglibs.jsp" %>
<jsp:include page="/empPension/pensionScript.jsp" flush="true" />
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="java.util.*,
org.egov.budget.services.*,
org.apache.log4j.Logger,
org.egov.infstr.utils.HibernateUtil,
org.egov.EGOVRuntimeException,
org.egov.infstr.commons.*,
org.egov.commons.*,
java.text.SimpleDateFormat,
org.egov.payroll.model.*,
org.egov.pims.model.EmployeeNomineeMaster,
org.egov.payroll.model.*,
org.egov.payroll.model.pension.*,
org.egov.payroll.client.*,
org.egov.pims.model.*"


%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Search Pension Details</title>

<LINK rel="stylesheet" type="text/css" href="../css/ccMenu.css">

<SCRIPT type="text/javascript" src="../javascript/dateValidation.js" type="text/javascript"></SCRIPT>
<SCRIPT type="text/javascript" src="../common/js/eispayroll.js" type="text/javascript"></SCRIPT>
<script>

var temp;

function execute()
  {
	var target="<%=(request.getAttribute("alertMessage"))%>";
	 if(target!="null")
	 {
		alert("<%=request.getAttribute("alertMessage")%>");
		<%	request.setAttribute("alertMessage",null);	%>
	 }

}
</script>
</head>
<%
	
	PersonalInformation pensionEmployee = null;
	PensionHeader pensionHeader= null;
	Date dateRetirement = null;
	Assignment pensionAssign = null;
	Integer Id=null;
	BankDet egpimsBankDet = null;
	Integer bnkId=null;
	Set delNominees= new HashSet();
	session.setAttribute("delNominees", delNominees);
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	String dob = "";
	String retDate = "";
		try
		{
			pensionEmployee = (PersonalInformation)request.getAttribute("pimsEmployeee");
		    System.out.print("TEST FOR PENSION EMPLOYEE---"+pensionEmployee);
			if(pensionEmployee == null)
				throw new EGOVRuntimeException("PensionEmployee is null");

			dateRetirement=(Date)request.getAttribute("Retirementdate");
			pensionAssign = (Assignment)request.getAttribute("assignEmployee");

			Id=pensionEmployee.getIdPersonalInformation();
			pensionHeader= (PensionHeader)request.getAttribute("pensionHeader");
			System.out.println(">>>>>>>>>>>>>>"+pensionHeader.getId());

			Set bnkSet= pensionEmployee.getEgpimsBankDets();
			if(bnkSet!=null)
				for (Iterator it = bnkSet.iterator(); it.hasNext(); )
				{
					egpimsBankDet=(BankDet)it.next();
				}

		   if(pensionEmployee.getDateOfBirth() != null)
				dob = sdf.format(pensionEmployee.getDateOfBirth());
		   if(dateRetirement!=null)
				retDate = sdf.format(dateRetirement);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}

%>
<body onLoad="execute();display();readyOnlyOnLoad();disableDisType();disableBankOnLoad();onLoadModifyEligible();">
<html:form  action="/empPension/AfterPensionHeaderAction.do?submitType=modifyDetails">
<div class="navibarshadowwk"></div>
<div class="formmainbox">
<div class="insidecontent">
  <div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	  <div class="rbcontent2">
	    <div class="datewk"></div>

<input type="hidden" name="pensionId" id="pensionId" value="<%=pensionHeader.getId()%>" />

<table  width="96%" cellpadding ="0" cellspacing ="0" border = "0" id="PensionTable" name="PensionTable" >
<tbody>
<tr>
<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                  <div class="headplacer">Employee Pension Details</div></td>
</tr>
<tr id="pensionRow">
<td  class="whiteboxwk"  width="15%">Code</td>
<td class="whitebox2wk" width="26%">
<input type="text"id="employeeId" name="employeeId" value="<%=pensionEmployee.getEmployeeCode()==null?"":pensionEmployee.getEmployeeCode().toString() %>" readOnly ="true">
</td>
<td  class="whiteboxwk"  width ="14%">Name</td>
<td class="whitebox2wk" width ="45%">
<input type="text"id="employeeName" name="employeeName" value="<%=pensionEmployee.getEmployeeName()==null?"":pensionEmployee.getEmployeeName().toString() %>" readOnly ="true">
</td>
</tr>
<tr>
<td  class="greyboxwk"  >Designation</td>
<td class="greybox2wk">
<input type="text"id="employeeDesignation" name="employeeDesignation" value="<%=pensionAssign.getDesigId().getDesignationName()==null?"":pensionAssign.getDesigId().getDesignationName().toString() %>" readOnly ="true">
</td>
<td class="greyboxwk"  >Department</td>
<td class="greybox2wk">
<input type="text"id="employeeDepartment" name="employeeDepartment"  value="<%=pensionAssign.getDeptId().getDeptName()==null?"":pensionAssign.getDeptId().getDeptName().toString() %>" readOnly ="true">
</td>


		
</tr>
<tr>
<td  class="whiteboxwk" >Birth Date</td>
<td class="whitebox2wk"  >
<%	//FIXME : Surround with try-catch block for date formats %>
<input id="employeeDob" name="employeeDob" value="<%=pensionEmployee.getDateOfBirth()==null?"":sdf.format(pensionEmployee.getDateOfBirth()) %>" readOnly ="true">
</td>

<td  class="whiteboxwk" >Deceased Date</td>
		<td class="whitebox2wk">
		<input type="text"id="deceasedDate" name="deceasedDate" value="<%=pensionEmployee.getDeathDate()==null?"":sdf.format(pensionEmployee.getDeathDate())%>"  disabled ="true">
		</td>
		
</tr>
<td  class="greyboxwk"  >SuperAnnuation Date</td>
<td class="greybox2wk" colspan="6">
<input type="text"id="employeeSuperAnnuationDate" name="employeeSuperAnnuationDate" value = "<%=dateRetirement==null?"":sdf.format(dateRetirement)%>" readOnly ="true">
</td>
</tr>

<tr>
<td  class="whiteboxwk" >Pensioner Status</td>
<td class="whitebox2wk" colspan="6">
<input type="text"id="statusMaster" name="statusMaster" value="<%=pensionEmployee.getStatusMaster().getDescription()%>" readOnly ="true">
</td>
</tr>

<tr>
<td   class="greyboxwk" >Disbursement Type<font color="red">*</font></td>
<td   class="greybox2wk" colspan="6">
<html:select  property="employeeDisbursementType" styleId="employeeDisbursementType"  value="${pensionHeader.disbursementType}"onchange="hideTable()" >
<html:option value="0" >----Choose----</html:option>
<html:option  value = "dbt">Bank Transfer</html:option>
<html:option  value = "cheque">Cheque</html:option>
<html:option  value = "cash">Cash</html:option>
</html:select>
</td>
</td>
</tr>
</tbody>
</table>
<table  width="96%"  cellpadding ="0" cellspacing ="0" border = "0" id="bankTable" name="bankTable" style="display:none" >
<tr >
<td  class="tablesubheadwk" width="44%" ><bean:message key="Bank"/><font color="red">*</font></td>
		<td  class="tablesubheadwk" width="45%"><bean:message key="BranchName"/><font color="red">*</font></td>
		<td  class="tablesubheadwk" width="45%" colspan="2"><bean:message key="AccountNumber"/><font color="red">*</font></td>
</tr>
<tr>
<td class="whitebox2wk"  width="40%">
<%
Integer branchId = new Integer(0);
Bankbranch bankBranch = pensionHeader.getIdBranch();
Bank bank=null;
Integer bankId= new Integer(0);
if(bankBranch != null)
{
branchId = bankBranch.getId();
bank = bankBranch.getBank();
bankId= bank.getId();
}
%>

<select  name="employeeBank" id="employeeBank" class="whitebox2wk" onchange = "checkBranch(this,'bankTable');">
<option value='0' selected="selected">--choose--</option>
<%
	//FIXME : Use Collection Iterator
Map bankMap =(Map)session.getAttribute("bankMap");

if(bankMap!=null&&bankMap.keySet().isEmpty()!=true)
{
for (Iterator it = bankMap.entrySet().iterator(); it.hasNext(); )
{
Map.Entry entry = (Map.Entry) it.next();
%>

<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).intValue() == bankId.intValue())? "selected":"")%>><%= entry.getValue() %></option>
<%
}
}
%>
</select>
</td>
<td class="whitebox2wk" width="40%">


<select  name="employeeBranch" id="employeeBranch"  class="whitebox2wk" >
<option value='0' selected="selected">--choose--</option>
<%
Map branchMap =(Map)session.getAttribute("branchMap");

if(branchMap!=null&&branchMap.keySet().isEmpty()!=true)
{
for (Iterator it = branchMap.entrySet().iterator(); it.hasNext(); )
{
Map.Entry entry = (Map.Entry) it.next();

%>
<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).intValue() == branchId.intValue())? "selected":"")%>><%= entry.getValue() %></option>
<%
}
}
%>
</select>
</td>
<td class="whitebox2wk" >
<input type="text" id="employeeAccountNumber" name="employeeAccountNumber" value="${pensionHeader.accountNumber}">
</td>
</tr>
</td>
</tbody>
</table>





	<%
	if(pensionHeader.getNomineeDetails() == null || pensionHeader.getNomineeDetails().isEmpty())
	{
       
	%>
	
	<table  border="0" width="96%" cellspacing="0" cellpadding="0" align="center" id="NomineTable" name="NomineTable" >
	<tr><td>&nbsp;</td></tr>
	<tbody>
	<tr align = "center"><td><font color="red">Nominee details does not exist.</font></td></tr>
	</tbody>
	</table>
	

			
		
	<table  border="0" width="96%" cellspacing="0" cellpadding="0" align="center" id="NomineeTable" name="NomineeTable">
	<%
		if(!((String)(request.getAttribute("mode"))).trim().equals("view"))
		{
		%>
		<tbody>

				<tr>
                <td colspan="13" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                  <div class="headplacer">Nominee Details</div></td>
				</tr>

				<tr>
				<td  class="tablesubheadwk" width="10%" >Nominee Name<font color="red">*</font></td>
				<td  class="tablesubheadwk" width="9%" ><bean:message key="Relation"/></td>
				<td  class="tablesubheadwk" width="8%" ><bean:message key="alive"/></td>
				<td  class="tablesubheadwk" width="6%" ><bean:message key="married"/></td>
				<td  class="tablesubheadwk" width="10%" ><bean:message key="EmpDob"/></td>
				<td  class="tablesubheadwk" width="8%" ><bean:message key="employed"/></td>
				<td  class="tablesubheadwk" width="10%" ><bean:message key="Status"/></td>
				<td  class="tablesubheadwk" width="10%" ><bean:message key="DisType"/></td>
				<td id="1Row" class="tablesubheadwk" width="12%"><bean:message key="Bank"/></td>
				<td id="3Row" class="tablesubheadwk" width="10%"><bean:message key="BranchName"/></td>
				<td id="3Row" class="tablesubheadwk" width="10%"><bean:message key="AccountNumber"/></td>
				<td id="3Row" class="tablesubheadwk" width="7%">Percentage<font color="red">*</font></td>
				<td  class="tablesubheadwk" width="5%" >Add/Del</td>	
				</tr>

				<tr id="NomineeRow" >
				<td class="labelcell" >
		


		<input type="hidden" name="nomineeMstrId" id="nomineeMstrId"/>
		<select  name="firstName" id="firstName" class="whitebox3wk"   onchange="checkduplicate('NomineeTable',this,this.value,'Nominee Name','2','Yes','dropDown');populateNomineeMstr(this.value,this);">

				<option value='0' selected="selected">---choose----</option>
				<%
			  
				Map nomineeMap =(Map)request.getAttribute("nomineeMap");
				System.out.println("Nominee Map Enter with No Pension Header Values--"+nomineeMap);
				if(nomineeMap!=null && nomineeMap.keySet().isEmpty()!=true)
				{
					for (Iterator it = nomineeMap.entrySet().iterator(); it.hasNext(); )
					{
					Map.Entry entry = (Map.Entry) it.next();
					
					%>

					<option  value = "<%= entry.getKey().toString() %>"><%= entry.getValue() %></option>
					<%
					}
				}
				%>
				</select>
				</td>
				
				<td class="whitebox3wk" >
				<input type="hidden" name="nomineeId" id="nomineeId" value="${NomineeDetails.id}" />
						<select  name="relationId" id="relationId" class="labelcell" disabled="true">

						<option value='0' selected="selected">---choose---</option>
						<%
						//FIXME : Use Collection Iterator
						Map nomineetypeMap =(Map)session.getAttribute("nomineeTypeMap");

						if(nomineetypeMap!=null && nomineetypeMap.keySet().isEmpty()!=true)
						{
							for (Iterator it = nomineetypeMap.entrySet().iterator(); it.hasNext(); )
							{
							Map.Entry entry = (Map.Entry) it.next();
							%>

							<option  value = "<%= entry.getKey().toString() %>"<%=(((Long)entry.getKey()).longValue()) %>><%= entry.getValue() %></option>
							<%
							}
						}
						%>
						</select>
				</td>

				<td class="whitebox3wk" >
						<html:select  property="isAlive" styleId="isAlive" styleClass="labelcell" disabled="true" >
						<html:option value="-1" >-Choose-</html:option>
						<html:option  value = "1">Yes</html:option>
						<html:option  value = "2">No</html:option>
				</html:select>
				</td>

				<td id="marryRow" class="whitebox3wk" >
						<html:select  property="maritialStatus" styleClass="labelcell" disabled="true" >
						<html:option value="-1" >-Choose-</html:option>
						<html:option  value = "1">Yes</html:option>
						<html:option  value = "2">No</html:option>
				</html:select>
				</td>

			<td id="dobRow" class="whitebox3wk">
					<input type="text" class="selectwk textmxwidth2" id="dateOfBirth" name="dateOfBirth" disabled="true">
				</td>

				<td id="employmentRow" class="whitebox3wk">
						<html:select  property="employed" styleId="employed" styleClass="labelcell" disabled="true">
						<html:option value="-1" >-Choose-</html:option>
						<html:option  value = "1">Yes</html:option>
						<html:option  value = "2">No</html:option>
				</html:select>
				</td>



				<td  class="whitebox3wk">
				       <input type="text" class="selectwk textmxwidth2" name="status"  id="status"    readOnly = "true"  ></td>
				</td>

				<td  id="disRow" class="whitebox3wk" >
						<html:select  property="nomDisType" styleClass="labelcell" disabled="true">
						<html:option  value = "cash">Cash</html:option>
						<html:option  value = "dbt">Bank Transfer</html:option>
						<html:option  value = "cheque">Cheque</html:option>
				</html:select>
				</td>

				<td id="2Row" class="whitebox3wk" >
						<select  name="bank" id="bank" class="labelcell" onblur = "checkBranch(this,'NomineeTable');"   disabled="true">
						<option value="0" selected="selected">-choose-</option>
						<%
						Map bankNomMap =(Map)session.getAttribute("bankMap");

						if(bankNomMap!=null&&bankNomMap.keySet().isEmpty()!=true)
						{
							for (Iterator it = bankNomMap.entrySet().iterator(); it.hasNext(); )
							{
							Map.Entry entry = (Map.Entry) it.next();
							%>

							<option  value = "<%= entry.getKey().toString() %>"<%=(((Integer)entry.getKey()).intValue()) %>><%= entry.getValue() %></option>
							<%
							}
						}
						%>
				</select>
				</td>
				<td class="whitebox3wk" >
						<select  name="branchNominee" id="branchNominee"  class="labelcell"   disabled="true">
						<option value="0" selected="selected">-choose-</option>
						<%
						Map branchesMap =(Map)session.getAttribute("branchMap");

						if(branchesMap!=null&&branchesMap.keySet().isEmpty()!=true)
						{
							for (Iterator it = branchesMap.entrySet().iterator(); it.hasNext(); )
							{
							Map.Entry entry = (Map.Entry) it.next();

							%>
							<option  value = "<%= entry.getKey().toString() %>"<%=(((Integer)entry.getKey()).intValue()) %>><%= entry.getValue() %></option>
							<%
							}
						}
						%>
				</select>
				</td>
				<td id="4Row" class="whitebox3wk">
						<input type="text"  class="selectwk textmxwidth2" id="bankAccount" name="bankAccount"  readonly="true" >
				</td>
				
				<td class="whitebox3wk" >
				<input type="text" class="selectwk textmxwidth2" id="percentage" name="percentage">
				</td>
				
				<%
					System.out.println("mode check -----"+request.getAttribute("mode"));
				if(!((String)(request.getAttribute("mode"))).trim().equals("view"))
				{
				%>
				
				<td class="whiteboxwk" >
				<div align="left"><a href="#"><img src="../common/image/add.png"  alt="Add" width="16" height="16" border="0"
				onclick="addRowToTable('NomineeTable');" /></a>
				<a href="#"><img src="../common/image/cancel.png" alt="Del" width="16" height="16" border="0"
				onclick="deleteRow('NomineeTable',this);"/></a></div>
				</td> 
				<% }%>
				</tr>
				</tbody>
			<%} %>
	</table>
	

<%
	}
	else
	{
	
%>
	
	<table  width="96%" cellpadding ="0" cellspacing ="0" border = "2" id="NomineeTable" name="NomineeTable">
	<tbody>
	<tr>

	<td colspan="13" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                  <div class="headplacer">Nominee Details</div></td>
	</tr>
	<tr>
		<td  class="tablesubheadwk" >Nominee Name<font color="red">*</font></td>
		<td  class="tablesubheadwk" ><bean:message key="Relation"/></td>
		<td  class="tablesubheadwk" ><bean:message key="alive"/></td>
		<td  class="tablesubheadwk" ><bean:message key="married"/></td>
		<td  class="tablesubheadwk" ><bean:message key="EmpDob"/></td>
		<td  class="tablesubheadwk" ><bean:message key="employed"/></td>
		<td  class="tablesubheadwk" ><bean:message key="Status"/></td>
		<td  class="tablesubheadwk" ><bean:message key="DisType"/></td>
		<td id="1Row" class="tablesubheadwk"><bean:message key="Bank"/></td>
		<td id="3Row" class="tablesubheadwk"><bean:message key="BranchName"/></td>
		<td id="3Row" class="tablesubheadwk"><bean:message key="AccountNumber"/></td>
		<td id="3Row" class="tablesubheadwk">Percentage<font color="red">*</font></td>
		<%
		if(!((String)(request.getAttribute("mode"))).trim().equals("view"))
		{
		%>
		<td  class="tablesubheadwk" width="5%" >Add/Del</td>	
		<%} %>
		</tr>
<c:forEach var="NomineeDetails" items="${pensionHeader.nomineeDetails}" varStatus="counter">

		<tr id="NomineeRow" >

		<td class="whitebox3wk" >
        <input type="hidden" name="nomineeMstrId" id="nomineeMstrId" value="${NomineeDetails.nomineeMstr.id}"/>
        
        <%
			
			
           Long nomineeMstrId=(((NomineeDetails)pageContext.getAttribute("NomineeDetails")).getNomineeMstr().getId());
		  System.out.println("TEST---"+nomineeMstrId);
		
		%>
    <select  name="firstName" id="firstName" class="labelcell"   onchange="checkduplicate('NomineeTable',this,this.value,'Nominee Name','2','Yes','dropDown');populateNomineeMstr(this.value,this);">

				<option value='0' selected="selected">---choose---</option>
				<%
			  
				Map nomineeMap =(Map)request.getAttribute("nomineeMap");
				System.out.println("Nominee Map Enter--"+nomineeMap);
				if(nomineeMap!=null && nomineeMap.keySet().isEmpty()!=true)
				{
					for (Iterator it = nomineeMap.entrySet().iterator(); it.hasNext(); )
					{
					Map.Entry entry = (Map.Entry) it.next();
					
					%>

					<option  value = "<%= entry.getKey().toString() %>"<%=((((Long)entry.getKey()).longValue() == nomineeMstrId.longValue())? "selected":"")%>><%= entry.getValue() %></option>
					<%
					}
				}
				%>
				</select>
	</td>
		<td class="whitebox3wk" >
			<%

		Integer nomId= new Integer(0);
		Set nomDetails= pensionHeader. getNomineeDetails();
		


		%>
		<input type="hidden" name="nomineeHiddenId" id="nomineeHiddenId" value="${NomineeDetails.id}" />
		<input type="hidden" name="nomineeId" id="nomineeId" value="${NomineeDetails.id}" />
		<html:select property="relationId" styleClass="labelcell" value="${NomineeDetails.nomineeMstr.relationType.id}" disabled="true" >
		<html:option value='0'>--choose--</html:option>
		<%
			//FIXME : Use Collection Iterator
		Map nomineetypeMap =(Map)session.getAttribute("nomineeTypeMap");

		if(nomineetypeMap!=null&&nomineetypeMap.keySet().isEmpty()!=true)
		{
		for (Iterator it = nomineetypeMap.entrySet().iterator(); it.hasNext(); )
		{
		Map.Entry entry = (Map.Entry) it.next();
	   //	System.out.println("sdghfkhsdkfhsdfk"+entry.getKey().toString());
		%>
			<html:option value = "<%= entry.getKey().toString() %>"><%= entry.getValue() %></html:option>
		<%
		}
		}

		%>

		</html:select>
		</td>
		<td class="whitebox3wk" >
				<html:select  property="isAlive" styleId="isAlive" styleClass="labelcell"  value="${NomineeDetails.nomineeMstr.isActive}"  disabled="true">
				<html:option value="-1" >-Choose-</html:option>
				<html:option  value = "1">Yes</html:option>
				<html:option  value = "2">No</html:option>
		</html:select>
		</td>
		<td class="whitebox3wk" >
				<html:select  property="maritialStatus" styleId="maritialStatus" styleClass="labelcell"  value="${NomineeDetails.nomineeMstr.maritalStatus}" disabled="true" >
				<html:option value="-1" >-Choose-</html:option>
				<html:option  value = "1">Yes</html:option>
				<html:option  value = "2">No</html:option>
		</html:select>
		</td>
		<td class="whitebox3wk">
			<input type="text"  class="selectwk textmxwidth3" name="dateOfBirth"  id="dateOfBirth"   value="<fmt:formatDate  type="date" value="${NomineeDetails.nomineeMstr.nomineeDob}"  pattern="dd/MM/yyyy"/>" disabled="true"/>
		</td>
		
		
		<td class="whitebox3wk" >
				<html:select  property="employed" styleId="employed"  styleClass="labelcell" value="${NomineeDetails.nomineeMstr.isWorking}" disabled="true">
				<html:option value="-1" >-Choose-</html:option>
				<html:option  value = "1">Yes</html:option>
				
				<html:option  value = "2">No</html:option>
		</html:select>
			</td>


		<td class="whitebox3wk" >
				<c:if test="${NomineeDetails.isEligible == '1'}">
				<input type="text"  class="selectwk textmxwidth3" name="status"  id="status"    readOnly = "true" value="Eligible" >
				</c:if>
				<c:if test="${NomineeDetails.isEligible == '0'}">
				<input type="text"  class="selectwk textmxwidth3" name="status"  id="status"    readOnly = "true" value="Not Eligible" >
				</c:if>
				<c:if test="${NomineeDetails.isEligible == '2'}">
					<input type="text"  class="selectwk textmxwidth3" name="status"  id="status"    readOnly = "true" value="Unknown" >
				</c:if>
		</td>


		<td   class="whitebox3wk" >
		<html:select  property="nomDisType" styleId="nomDisType" styleClass="labelcell"  value="${NomineeDetails.nomineeDisType}" disabled="true">
		<html:option  value = "cash">Cash</html:option>
		<html:option  value = "dbt">Bank Transfer</html:option>
		<html:option  value = "cheque">Cheque</html:option>
		</html:select>
		</td>
			 <%
			 System.out.println("request.getAttribute="+request.getAttribute("mode"));
		if(((String)(request.getAttribute("mode"))).trim().equals("view"))
		{
		%>
		<td id="2Row" class="whitebox3wk" >

			<input type="text" class="textmxwidth" id="bank" name="bank" value="${NomineeDetails.bankBranch.bank.name}" disabled="true" >
		</td>
		<%
		}
		else
		{
		%>

		<td id="2Row" class="whitebox3wk" >
		   <html:select property ="bank" styleClass="labelcell textmxwidth" value="${NomineeDetails.nomineeMstr.bankBranch.bank.id}" disabled="true" >
		<html:option value="0">-choose-</html:option>
		<%
		Map bankNomMap =(Map)session.getAttribute("bankMap");

		if(bankNomMap!=null&&bankNomMap.keySet().isEmpty()!=true)
		{
		for (Iterator it = bankNomMap.entrySet().iterator(); it.hasNext(); )
		{
		Map.Entry entry = (Map.Entry) it.next();
		%>

			<html:option value = "<%= entry.getKey().toString() %>"><%= entry.getValue() %></html:option>
			<%
		}
		}
		%>
		</html:select>

			<%
		   }
		%>
		</td>
		<%
		if(((String)(request.getAttribute("mode"))).trim().equals("view"))
		{
		%>
		<td id="2Row" class="whitebox3wk" >

		<input type="text" class="selectwk textmxwidth3" id="branchNominee" name="branchNominee" value="${NomineeDetails.nomineeMstr.bankBranch.branchname}">
		</td>
		<%
		}
		else
		{
		%>
		<td class="whitebox3wk" >
			<html:select  property ="branchNominee"  styleClass="labelcell" value="${NomineeDetails.nomineeMstr.bankBranch.id}" disabled="true" >
			<html:option value="0" >-choose-</html:option>
			<%
			Map branchesMap =(Map)session.getAttribute("branchMap");

			if(branchesMap!=null&&branchesMap.keySet().isEmpty()!=true)
			{
			for (Iterator it = branchesMap.entrySet().iterator(); it.hasNext(); )
			{
			Map.Entry entry = (Map.Entry) it.next();

			%>
			<html:option value = "<%= entry.getKey().toString() %>"><%= entry.getValue() %></html:option>
			<%
			}
			}
			%>
		  </html:select>
		   <%
			}
		%>
		</td>

		<td id="4Row" class="whitebox3wk" >
		<input type="text" class="selectwk textmxwidth3" id="bankAccount" name="bankAccount" value="${NomineeDetails.nomineeMstr.accountNumber}" readonly="true">
		</td>
		
		<td class="whitebox3wk" >
				<input type="text" class="selectwk textmxwidth2" id="percentage" name="percentage" value="${NomineeDetails.percentage}" >
		</td>
		<%
		System.out.println("mode check -----"+request.getAttribute("mode"));
		if(!((String)(request.getAttribute("mode"))).trim().equals("view"))
		{
		%>
			<td class="whiteboxwk" >
				<div align="left"><a href="#"><img src="../common/image/add.png"  alt="Add" width="16" height="16" border="0"
				onclick="addRowToTable('NomineeTable');" /></a>
				<a href="#"><img src="../common/image/cancel.png" alt="Del" width="16" height="16" border="0"
				onclick="deleteRow('NomineeTable',this);"/></a></div>
				</td> 
		<%
		}
		%>
		
		

			</tr>
	</c:forEach>
</tbody>
</table>

	<%
	}
	if(((String)(request.getAttribute("mode"))).trim().equals("modify"))
	{
	%>
		<table id = "submit" width="96%"  cellpadding ="0" cellspacing ="0" border = "0">
		<tr ><td >&nbsp;</td></tr>
		<tr align = "center">
	<%
	if(pensionHeader.getNomineeDetails() != null && !pensionHeader.getNomineeDetails().isEmpty())
	{
	%>
	
	<td align="center">
	<html:button styleClass="buttonfinal" value="Update Status" property="updateStatus"  onclick="checkEligible();"  />
	</td>
	<%
	}
	else
	{
	%>
	
	<td align="center">
	<html:button styleClass="buttonfinal" value="Update Status" property="updateStatus"  onclick="checkEligible();"  />
	</td>
	<%
	}
	%>

<%
}
%>
</tr>

<tr>
            <td><div align="right" class="mandatory">* Mandatory Fields</div></td>
          </tr>
</table>

</div>
		<div class="rbbot2"><div></div></div>
		</div>
		</div>
		</div>
</div>


		<div class="buttonholderwk" >

	<%
		if(!((String)(request.getAttribute("mode"))).trim().equals("view"))
		{
		%>
			<html:button styleClass="buttonfinal" value="Save" property="b4"  onclick="validateOnSubmitForModify();"  />
		<% } %>
			<input type="button" name="button" id="button" value="Close"  class="buttonfinal" onclick="window.close();"/>

	</div>
	<div class="urlwk">City Administration System Designed and Implemented by <a href="http://www.egovernments.org/">eGovernments Foundation</a> All Rights Reserved </div>

</html:form>
</body>
</html>
