<%@ include file="/includes/taglibs.jsp" %>
<jsp:include page="/empPension/pensionScript.jsp" flush="true" />
<%@ page import="java.util.*,

org.egov.infstr.utils.HibernateUtil,
org.egov.EGOVRuntimeException,
org.apache.log4j.Logger,
java.text.SimpleDateFormat,
org.egov.payroll.model.pension.*,
org.egov.payroll.model.pension.NomineeDetails,
org.egov.payroll.client.*,
org.egov.commons.Bankbranch,
org.egov.pims.model.*"

%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Create Pension Header</title>

<LINK rel="stylesheet" type="text/css" href="../css/ccMenu.css">

<SCRIPT type="text/javascript" src="../javascript/dateValidation.js" type="text/javascript"></SCRIPT>
<SCRIPT type="text/javascript" src="../common/js/eispayroll.js" type="text/javascript"></SCRIPT>
<script>


</script>

</head>
<%
	 final Logger logger = Logger.getLogger("pensionMasterCreate.jsp");
		PersonalInformation pensionEmployee = null;
		Date dateRetirement = null;
		Assignment pensionAssign = null;
		Integer Id=null;
		BankDet egpimsBankDet = new BankDet();

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	String dob = "";
	String retDate = "";
	String deceasedDate = "";
		try
		{
			pensionEmployee = (PersonalInformation)request.getAttribute("pimsEmployeee");
			if(pensionEmployee == null)
				throw new EGOVRuntimeException("PensionEmployee is null");

			dateRetirement=(Date)request.getAttribute("Retirementdate");
			pensionAssign = (Assignment)request.getAttribute("assignEmployee");

			Id=pensionEmployee.getIdPersonalInformation();

			Set bnkSet= pensionEmployee.getEgpimsBankDets();
			if(bnkSet!=null)
				for (Iterator it = bnkSet.iterator(); it.hasNext(); )
				{
					egpimsBankDet=(BankDet)it.next();
					System.out.println(">>>>>>>>>>>>>>>>>>>>>"+egpimsBankDet);
				}

		   if(pensionEmployee.getDateOfBirth() != null)
		   {
				dob = sdf.format(pensionEmployee.getDateOfBirth());
			}
			
			if(pensionEmployee.getDeathDate() != null)
			{
			   deceasedDate = sdf.format(pensionEmployee.getDeathDate());
			}
		   if(dateRetirement!=null)
		   {
				retDate = sdf.format(dateRetirement);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error("Exception Encountered!!!"+e.getMessage());
			HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}




%>
<body onload="disableDisType();">
	<html:form  action="/empPension/AfterPensionHeaderAction.do?submitType=saveDetails">
		<div class="navibarshadowwk"></div>
<div class="formmainbox">
<div class="insidecontent">
  <div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	  <div class="rbcontent2">
	    <div class="datewk"></div>
		
        	<input type="hidden" name="employeeId" id="employeeId" value="<%= Id %> " />
		<table  width="96%" cellpadding ="0" cellspacing ="0" border = "0" id="PensionTable" name="PensionTable" align="center">
		<tbody>
		<tr>
		<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                  <div class="headplacer">Employee Pension Details</div></td>
		</tr>

		<tr id="pensionRow">
		<td  class="whiteboxwk" width="15%"><bean:message key="Code"/>
		</td>
		<td class="whitebox2wk" width="26%">
		<input type="text"id="employeeId" name="employeeId" value="<%=pensionEmployee.getEmployeeCode()==null?"":pensionEmployee.getEmployeeCode().toString() %>" disabled ="true">
		</td>
		<td  class="whiteboxwk" width="14%"><bean:message key="EmpName"/></td>
		<td class="whitebox2wk" width="45%">
		<input type="text" id="employeeName" name="employeeName" value="<%=pensionEmployee.getEmployeeName()==null?"":pensionEmployee.getEmployeeName().toString() %>" disabled ="true">
		</td>
		</tr>
		
		<tr>
		<td  class="greyboxwk" ><bean:message key="Designation"/></td>
		<td class="greybox2wk">
		<input type="text" id="employeeDesignation" name="employeeDesignation" value="<%=pensionAssign.getDesigId().getDesignationName()==null?"":pensionAssign.getDesigId().getDesignationName().toString() %>" disabled ="true">
		</td>
		<td class="greyboxwk"  ><bean:message key="Department"/></td>
		 <td class="greybox2wk">
		<input type="text"id="employeeDepartment" name="employeeDepartment"  value="<%=pensionAssign.getDeptId().getDeptName()==null?"":pensionAssign.getDeptId().getDeptName().toString() %>" disabled ="true">
		</td>
		</tr>
		
		<tr>
		<td  class="whiteboxwk" ><bean:message key="dob"/></td>
		<td class="whitebox2wk"  >
		<input type="text"id="employeeDob" name="employeeDob" value="<%=dob%>" disabled ="true">
		</td>		
		<td  class="whiteboxwk" >Deceased Date</td>
		<td class="whitebox2wk"  >
		<input type="text"id="deceasedDate" name="deceasedDate" value="<%=deceasedDate%>" disabled ="true">
		</td>
		</tr>
		
		<tr>
		<td  class="greyboxwk"  ><bean:message key="Retirementdate"/></td>
		<td class="greybox2wk" colspan="6">
		<input type="text"id="employeeSuperAnnuationDate" name="employeeSuperAnnuationDate" value = "<%=retDate%>" disabled ="true">
		</td>
		</tr>
		
		<tr>
		<td  class="whiteboxwk" ><bean:message key="StatusPensioner"/></td>
		<td class="whitebox2wk" colspan="6">
	    	<input type="text"id="statusMaster" name="statusMaster" value="<%=pensionEmployee.getStatusMaster().getDescription()%>" disabled="true">
		</td>
		</select>
		<%
		if(egpimsBankDet!=null && egpimsBankDet.getBank()!=null)
		{
		%>
		</tr>
		
		<tr>
		<td   class="greyboxwk" ><bean:message key="DisType"/><font color="red">*</font></td>
		<td   class="greybox2wk" colspan="6">
		<html:select  property="employeeDisbursementType" styleId="employeeDisbursementType" onchange="hideTable()" >
		 <option  value = "dbt" <%=(egpimsBankDet!= null && egpimsBankDet.getBank()!=null)? "selected":""%>>Bank Transfer</option>
		<html:option  value = "cheque">Cheque</html:option>
		<html:option  value = "cash" >Cash</html:option>
		</html:select>
		</td>
		</tr>
		<%
	     }
	     else
	     {
		%>
		<tr>
		<td   class="greyboxwk" ><bean:message key="DisType"/><font color="red">*</font></td>
		<td   class="greybox2wk" colspan="6">
		<html:select  property="employeeDisbursementType" styleId="employeeDisbursementType" onchange="hideTable()" >
		<html:option  value = "cash" >Cash</html:option>
		<option  value = "dbt" >Bank Transfer</option>
		<html:option  value = "cheque">Cheque</html:option>
		</html:select>
		</td>
		</tr>
		<%
	    }
	    %>
	    </tbody>
		</table>
		
		<table width="96%"  cellpadding ="0" cellspacing ="0" border = "0" id="bankTable" name="bankTable" align="center" >
		<tr>
		<td  class="tablesubheadwk" width="40%" ><bean:message key="Bank"/><font color="red">*</font></td>
		<td  class="tablesubheadwk" width="40%"><bean:message key="BranchName"/><font color="red">*</font></td>
		<td  class="tablesubheadwk" width="40%" colspan="2"><bean:message key="AccountNumber"/><font color="red">*</font></td>
		</tr>
		
		<tr>
		<%
		Integer bnkId = new Integer(0);
			if(egpimsBankDet!=null && egpimsBankDet.getBank()!=null)
			bnkId=egpimsBankDet.getBank().getId();
		%>
		<td class="whitebox2wk" width="40%">
		<select  name="employeeBank" id="employeeBank"  class="whitebox2wk" onBlur = "checkBranch(this,'bankTable');">
			<option value="0" selected="selected">--choose--</option>
			<%
			Map bankMap =(Map)session.getAttribute("bankMap");
			if(bankMap!=null&&bankMap.keySet().isEmpty()!=true)
			  {
			for (Iterator it = bankMap.entrySet().iterator(); it.hasNext(); )
			  {
			Map.Entry entry = (Map.Entry) it.next();
			  %>
			<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).intValue() == bnkId.intValue())? "selected":"")%>><%= entry.getValue() %></option>
			<%
		  }
			}
			%>
		 </select>
		</td>
		<td class="whitebox2wk" width="40%" >
		<%
		Bankbranch brnch= null;
		Integer branchId =new Integer(0);
		if(egpimsBankDet!=null &&egpimsBankDet.getBranch()!=null)
		branchId=egpimsBankDet.getBranch().getId();
		
		
		%>
		<select  name="employeeBranch" id="employeeBranch"  class="whitebox2wk" onBlur= "checkBank(this,'bankTable');" >
		<option value="0" selected="selected">--choose--</option>
		<%
			Map branchMap =(Map)session.getAttribute("branchMap");

		if(branchMap!=null&&branchMap.keySet().isEmpty()!=true)
				{
			for (Iterator it = branchMap.entrySet().iterator(); it.hasNext(); )
			{
			Map.Entry entry = (Map.Entry) it.next();
			%>
			<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).intValue() ==branchId.intValue())? "selected":"")%>><%= entry.getValue() %></option>
		<%
				}
			}
		%>
		</select>
		</td>
		<td class="whitebox2wk" colspan="2" width="40%">
		<input type="text" id="employeeAccountNumber" class="whitebox2wk" name="employeeAccountNumber" value="<%=egpimsBankDet.getAccountNumber()==null?"":egpimsBankDet.getAccountNumber()%>">
		</td>
		</tr>
		</table>
		
		<table border="0" width="96%" cellspacing="0" cellpadding="0" align="center">
		<tr>
		<td class="whiteboxwk" width="15%">Create A Nominee</td>
		<td class="whitebox2wk" colspan="6">
		 <input type="radio" value="yes"  name="nomineeRadio" id="nomineeRadio" onclick="showNominee();"/>Yes
		 <input type="radio" value="no"  name="nomineeRadio" id="nomineeRadio"  onclick="hideNominee();" checked="yes" />No
		</td>
		</tr>
		<tr>
		<td colspan="7">
		<table  width="96%"  cellpadding ="0" cellspacing ="0" border = "0" id="NomineeTable" name="NomineeTable" style="display:none;" align="left">
		<tbody>
		<tr>
			<td colspan="13" class="headingwk">
				<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
	            <div class="headplacer">Nominee Details</div></td>
			</td>
		</tr>

		<tr>
		<td  class="tablesubheadwk"  >Nominee Name<font color="red">*</font></td>
		<td  class="tablesubheadwk"  ><bean:message key="Relation"/></td>
		<td  class="tablesubheadwk"  ><bean:message key="alive"/></td>
		<td  class="tablesubheadwk"  ><bean:message key="married"/></td>
		<td  class="tablesubheadwk"  ><bean:message key="EmpDob"/></td>
		<td  class="tablesubheadwk"  ><bean:message key="employed"/></td>
		<td  class="tablesubheadwk"  ><bean:message key="Status"/></td>
		<td  class="tablesubheadwk"  ><bean:message key="DisType"/></td>
		<td id="1Row" class="tablesubheadwk" ><bean:message key="Bank"/></td>
		<td id="3Row" class="tablesubheadwk" ><bean:message key="BranchName"/></td>
		<td id="3Row" class="tablesubheadwk" ><bean:message key="AccountNumber"/></td>
		<td id="3Row" class="tablesubheadwk" >Percentage<font color="red">*</font></td>
		<td  class="tablesubheadwk"  >Add/Del</td>	
		</tr>

		<tr id="NomineeRow" >
			<td class="whitebox3wk" >
				
				<input type="hidden" name="nomineeMstrId" id="nomineeMstrId"/>
				<select  name="firstName" id="firstName" class="labelcell"  onchange="checkduplicate('NomineeTable',this,this.value,'Nominee Name','2','Yes','dropDown');populateNomineeMstr(this.value,this);">

				<option value='0' selected="selected">---choose---</option>
				<%
			
				Map nomineeMap =(Map)request.getAttribute("nomineeMap");

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
			<input type="hidden" name="nomineeId" id="nomineeId" value="" />

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
				<html:select  property="maritialStatus" styleId="maritialStatus" styleClass="labelcell"  disabled="true">
				<html:option value="-1" >-Choose-</html:option>
				<html:option  value = "1">Yes</html:option>
				<html:option  value = "2">No</html:option>
		</html:select>
		</td>

        <td id="dobRow" class="whitebox2wk">
			<input type="text" class="textmxwidth3" id="dateOfBirth" name="dateOfBirth" onkeyup="DateFormat(this,this.value,event,false,'3')"onblur="validateDateFormat(this);validateDateSSS(this);" disabled="true">
		</td>

		<td id="employmentRow" class="whitebox3wk"  >
				<html:select  property="employed" styleId="employed" styleClass="labelcell" disabled="true">
				<html:option value="-1" >-Choose-</html:option>
				<html:option  value = "1">Yes</html:option>
				<html:option  value = "2">No</html:option>
		</html:select>
		</td>

		<td  class="whitebox3wk">
		       <input type="text" class="textmxwidth3" name="status"  id="status" readonly="true"></td>
		</td>

		<td  id="disRow" class="whitebox3wk" >
				<html:select  property="nomDisType" styleClass="labelcell" onchange="showBank(this)" disabled="true">
				<html:option value="-1" >-Choose-</html:option>
				<html:option  value = "cash">Cash</html:option>
				<html:option  value = "dbt">Bank Transfer</html:option>
				<html:option  value = "cheque">Cheque</html:option>
		</html:select>
		</td>

		<td id="2Row" class="whitebox3wk" >
				<select  name="bank" id="bank" class="textmxwidth3"  disabled="true">
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
				<select  name="branchNominee" id="branchNominee"  class="textmxwidth3" disabled="true">
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
				<input type="text" class="textmxwidth3" id="bankAccount" name="bankAccount" readonly="true">
		</td>
		<td class="whitebox3wk" >
				<input type="text" class="textmxwidth2" id="percentage" name="percentage" onblur="checkForPct(this);">
		</td>
				         
         <td class="whiteboxwk" >
				<div align="left"><a href="#"><img src="../common/image/add.png"  alt="Add" width="16" height="16" border="0"
				onclick="addRowToTable('NomineeTable');" /></a>
				<a href="#"><img src="../common/image/cancel.png" alt="Del" width="16" height="16" border="0"
				onclick="deleteRow('NomineeTable',this);"/></a></div>
		</td> 
		</tr>
		</tbody>
		</table>
		</td>
		</tr>
		</table>
		</td>
		</tr>
		</table>

		<table id = "submit" width="96%"  cellpadding ="0" cellspacing ="0" border = "0" >
		<tr><td>&nbsp;</td></tr>
		
		<tr>
		<td align="center">
		<html:button styleClass="buttonfinal" value="Update Status" property="updateStatus"  onclick="checkEligible();" style="display:none" />
		</td>
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




		<div class="buttonholderwk">
			<html:button styleClass="buttonfinal" value="Save" property="b4"  onclick="validateOnSubmit();"  />
			<input type="button" name="button" id="button" value="Close"  class="buttonfinal" onclick="window.close()"/>
		</div>
		<div class="urlwk">City Administration System Designed and Implemented by <a href="http://www.egovernments.org/">eGovernments Foundation</a> All Rights Reserved </div>
	</html:form>
</body>
</html>