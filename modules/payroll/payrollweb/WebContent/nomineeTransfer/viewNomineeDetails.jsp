<%@ include file="/includes/taglibs.jsp" %>

<%@ page import="java.util.*,

org.egov.infstr.utils.HibernateUtil,
org.egov.EGOVRuntimeException,
org.apache.log4j.Logger,
java.text.SimpleDateFormat,
java.math.BigDecimal,
org.egov.commons.Bankbranch,
org.egov.pims.model.*"

%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Transfer Nominee</title>

<LINK rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/ccMenu.css">

<SCRIPT type="text/javascript" src="${pageContext.request.contextPath}/javascript/dateValidation.js" type="text/javascript"></SCRIPT>
<script>

function execute()
{
	
	var target="<%=(request.getAttribute("alertMessage"))%>";

	 if(target!=null && target != "null")
	 {
		alert("<%=request.getAttribute("alertMessage")%>");
		<%	request.setAttribute("alertMessage",null);	%>
	 }

}


function checkEligibleAmt(obj,value)
{
	if(obj.value=="")
	{
		alert('Please enter a proper amount ');
		obj.value=value;
		obj.focus();
		return false;
	}
}

function checkNumeric(obj,amount)
	{
		
			var objt = obj;
			var amt = obj.value;
		if(amt != null && amt != "")
		{
			
				if(obj.value < 0 )
				{
				alert("Please enter positive value for the amount");
				obj.value=amount;
				objt.focus();
				return false;

				}
				if(isNaN(amt))
				{
				alert("Please enter a numeric value for the amount");
				obj.value=amount;
				objt.focus();
				return false;
				}
		}

	}

	function cancelMsg()
	{
		var submitType="";
		var empId=document.pensionNomineeTransferForm.employeeId.value;
		var answer = confirm ('Do u want to cancel the nominee?')
			if (answer)
		{
			
			submitType="terminateNominee";

		}
		else
		{
			return true;
		}
		
		document.forms("pensionNomineeTransferForm").action = "${pageContext.request.contextPath}/nomineeTransfer/AfterNomineeAction.do?submitType="+submitType;
 		 document.forms("pensionNomineeTransferForm").submit();

	}

function showMsg()
{
	 var length=document.getElementsByName('monthlyPensionAmount').length;
      var totalAmt=document.pensionNomineeTransferForm.totalEligiblePensionAmount.value;
	   var sum = 0;
		var value=0;

	   if(length<2)
	{
		value=eval(document.pensionNomineeTransferForm.monthlyPensionAmount.value);
		sum=sum+value;

		
	 }
	   else
	{
	   for(var i=0;i<length;i++)
		{
		   value = eval(document.pensionNomineeTransferForm.monthlyPensionAmount[i].value);
		   sum = sum+value;
		  
		}
		
    
	}


	 if(sum>totalAmt)
	{
		 alert('The total amount being limited to the pension amount');
		 return false;
	 }

	 if(document.pensionNomineeTransferForm.employeeDeathDate.value =="")
	{
		 alert('Please enter the employee death date');
		  return false;
	}

	else
	{
		
		document.forms("pensionNomineeTransferForm").action = "${pageContext.request.contextPath}/nomineeTransfer/AfterNomineeAction.do?submitType=updateNominee";
 		document.forms("pensionNomineeTransferForm").submit();

  		return true;
	}
}
	
</script>

</head>

		<body onload ="execute()">

		<%
			final Logger logger = Logger.getLogger("viewNomineeDetails.jsp");
			PersonalInformation pensionEmployee = null;
			Date dateRetirement = null;
			Assignment pensionAssign = null;
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String dob = "";
			String retDate = "";
			BigDecimal totalAmt=null;
				try
				{
					pensionEmployee = (PersonalInformation)request.getAttribute("pimsEmployeee");
					if(pensionEmployee == null)
						throw new EGOVRuntimeException("PensionEmployee is null");

					dateRetirement=(Date)request.getAttribute("Retirementdate");
					pensionAssign = (Assignment)request.getAttribute("assignEmployee");

				  if(pensionEmployee.getDateOfBirth() != null)
						dob = sdf.format(pensionEmployee.getDateOfBirth());
				   if(dateRetirement!=null)
						retDate = sdf.format(dateRetirement);

					totalAmt=(BigDecimal)request.getAttribute("monthlyNomineeAmount");
					

				}
				catch(Exception e)
				{
					e.printStackTrace();
					logger.error("Exception Encountered!!!"+e.getMessage());
					HibernateUtil.rollbackTransaction();
					throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
				}




       %>
		


        	
		<html:form  action="/nomineeTransfer/AfterNomineeAction">
		<table  WIDTH=97%  cellpadding ="0" cellspacing ="0" border = "1" id="PensionTable" name="PensionTable" >
		<tbody>
		<tr>
		<td colspan="15" height=20  align=middle  class="tableheader">
		<p>Employee Pension Details</p></td>
		</tr>
		<tr>
		<td  class="labelcell"  width ="400"><bean:message key="Code"/>
		</td>
		<input type=hidden name="employeeId" id="employeeId" value="<%=pensionEmployee.getIdPersonalInformation()%>" />
		<td class="labelcell" width="165">
		<input type="text"id="employeeCode" name="employeeCode" value="<%=pensionEmployee.getEmployeeCode()==null?"":pensionEmployee.getEmployeeCode().toString()%>"  disabled="true">
		</td>
		<td  class="labelcell"  width ="400"><bean:message key="EmpName"/></td>
		<td class="labelcell" width="165">
		<input type="text"id="employeeName" name="employeeName" value="<%=pensionEmployee.getEmployeeName()==null?"":pensionEmployee.getEmployeeName()%>" disabled="true">
		</td>
		</tr>
		
		
		
		<tr>
		<td  class="labelcell" ><bean:message key="dob"/></td>
		<td class="labelcell"  >
		<input type="text"id="employeeDob" name="employeeDob" value="<%=dob%>" disabled="true">
		</td>
		</tr>
		<td  class="labelcell"  ><bean:message key="Retirementdate"/></td>
		<td class="labelcell" >
		<input type="text"id="employeeSuperAnnuationDate" name="employeeSuperAnnuationDate" value = "<%=retDate%>"  disabled="true">
		</td>
		</tr>


		</tr>
		<td  class="labelcell"  >Death Date<font color="red">*</font></td>
		<td class="labelcell" >
		<input type="text"id="employeeDeathDate" name="employeeDeathDate" value = "" onblur="validateDateFormat(this);"
		onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')">
		</td>
		</tr>


		</tbody>
		</table>
		
		
		<c:if test="${nomTypepPensionList != null}">

		
		<table  WIDTH=97%  cellpadding ="0" cellspacing ="0" border = "1" id="NomineeTable" name="NomineeTable" >
		<tbody>
		<tr>

		<td colspan="30" height=20  align=middle  class="tableheader">
		<p>Nominee Details</p>
		</td>
		</tr>
		
		<tr>
		<td  class="labelcell"  >Nominee Name</td>
		
		<td  class="labelcell"  ><bean:message key="Relation"/></td>
		
		<td  class="labelcell"  ><bean:message key="Status"/></td>

		<td  class="labelcell"  >Eligible Amount</td>

		<input type=hidden name="totalEligiblePensionAmount" id="totalEligiblePensionAmount" value="<%=totalAmt%>" />
		
		<c:forEach var="nom" items="${nomTypepPensionList}">
		<c:set var= "status" value="${nom.isEligible}" />
		<c:set var= "nomineeId" value="${nom.id}" />
		<c:set var= "eligibleAmount" value="${nom.monthlyPensionAmount}" />
		
		<% 
		 Integer status = (Integer)pageContext.getAttribute("status"); 
		 int statusVal=status.intValue();

		  Integer nomineeId = (Integer)pageContext.getAttribute("nomineeId"); 
		 int nomineeVal=nomineeId.intValue();

		  BigDecimal eligibleAmount = (BigDecimal)pageContext.getAttribute("eligibleAmount"); 
		  int eliAmt=0;
		  if(eligibleAmount!=null)
			 eliAmt=eligibleAmount.intValue();
		
		 %>	
      
		
		<tr id="NomineeRow" >
		
		

        <td  class="labelcell">
			<input type="text"style="width: 125; height:20" id="employeeName" name="employeeName" value="${nom.employeeName}" disabled="true">
		</td>

		  <td  class="labelcell">
			<input type="text"style="width: 125; height:20" id="firstName" name="firstName" value="${nom.nomineeType.nomineeType}" disabled="true">
		</td>
		



		<td  class="labelcell">
		       <input type="text" style="width: 125; height:20" name="isEligible"  id="isEligible" value="<%=statusVal==1?"Eligible":"Not Eligible"%>"   disabled="true"></td>
		</td>
		<input type="hidden" name="nomineeId" value="<%=nomineeVal%>">
		<td  class="labelcell">
		       <input type="text" style="width: 125; height:20" name="monthlyPensionAmount"  id="monthlyPensionAmount"  value="${nom.monthlyPensionAmount}"  onBlur="checkNumeric(this,'<%=eliAmt%>'),checkEligibleAmt(this,'<%=eliAmt%>');"></td>
		</td>

		</c:forEach>
		</tr>
		</tbody>
		</table>

		</c:if>
		<table id = "submit" WIDTH="100%"  cellpadding ="0" cellspacing ="0" border = "1" >
		<tr WIDTH="100%" colspan="3">
		 <%
	    Set nomineeList = (Set)request.getAttribute("nomTypepPensionList");
		if(nomineeList != null && !nomineeList.isEmpty()){%>
		<td WIDTH="30%" align="center">
			<html:button styleClass="button" value="Update Amount" property="b4" onclick="return showMsg();"/>

		</td>
		<%}else{%>

		<td WIDTH="30%" align="center"><html:button styleClass="button" value="Terminate Nominee" property="b4" onclick="cancelMsg();"/>
		<%}%>
		<tr>
		</table>
</html:form>
		
		</body>
	
		</html>
