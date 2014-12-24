<%@ include file="/includes/taglibs.jsp" %>
<jsp:include page="/empPayroll/payrollScript.jsp" flush="true" />
<%@ page import="java.util.*,
		org.egov.infstr.utils.*,
		org.apache.log4j.Logger,
		org.egov.pims.*,
		org.egov.pims.dao.*,
		org.egov.pims.service.*,
		org.egov.pims.utils.*,
		org.egov.payroll.utils.*,
		org.egov.pims.model.*,
		org.egov.infstr.commons.*,
		org.egov.commons.*,
		org.egov.exceptions.EGOVRuntimeException,
		org.egov.pims.commons.client.*,
		org.egov.infstr.commons.dao.*,
		org.egov.infstr.utils.*,
		org.egov.payroll.services.payslip.*,
		org.egov.lib.rjbac.dept.*,
		org.egov.lib.address.model.*,
		java.math.BigDecimal,
		org.egov.payroll.model.*,
		org.egov.lib.address.dao.*,
		java.text.SimpleDateFormat,
		java.util.StringTokenizer,
		org.egov.pims.client.*"
		%>
	<html>
		<head>
		<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
		<title><bean:message key="payrolldetails.title"/></title>
	
		<SCRIPT type="text/javascript" src="../script/jsCommonMethods.js" type="text/javascript"></SCRIPT>
		<script>
		function callFileSearch()
	  {
	   var mozillaFirefox=document.getElementById&&!document.all;
	   var url="/dms/dms/searchFile.action"
	   <c:if test="${param.mode=='modify'}">
	   		url=url+"?module=external";
	   </c:if>
	   if(mozillaFirefox)
	   {
	    window.open(url,"Searchfile","height=500,width=600px,scrollbars=yes,left=30,top=30,status=yes");
	   }
	   else
	   {
	   window.open(url);
	   }
	   
	   
	   
	  }
	  function viewFile()
	  {
	   var mozillaFirefox=document.getElementById&&!document.all;
	   var url="/dms/dms/genericFile.action?fileId=";
	   <c:choose>
		<c:when test="${not empty fileId }">

        if(document.getElementById("fileId").value!="")//when attch file happened
	   		url=url+document.getElementById("fileId").value;
	   	else
	   		url=url+"${fileId}";
		</c:when>

		<c:otherwise>
		if(document.getElementById("fileId").value!="")
		{
		url=url+document.getElementById("fileId").value;
		}
		</c:otherwise>
	</c:choose>
	   
	   if(mozillaFirefox)
	   {
	    window.open(url,"View File","height=500,width=600px,scrollbars=yes,left=30,top=30,status=yes");
	   }
	   else
	   {
	   window.open(url);
	   }
	   
	   
	   
	  }
	  var fileUpdater=function(fileId)
		{
			 if(fileId != null && fileId != "")
		     {
		     	if(document.getElementById("fileImg")!=null)//it exists only in modify mode
		     	document.getElementById("fileImg").style.display="";
		     	document.getElementById("fileId").value=fileId;
		     }
		}	
		
		</script>
	

				<%!
				
						public Map getPayScaleMap(ArrayList list)
					   {
								Map payScaleHeaderMap = new HashMap();
								try {
									for(Iterator iter = list.iterator();iter.hasNext();)
									{
										PayScaleHeader payScaleHeader = (PayScaleHeader)iter.next();
										payScaleHeaderMap.put(payScaleHeader.getId(), payScaleHeader.getName());
									}
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
								}
								return payScaleHeaderMap;
					 }
				%>


				<%
				       System.out.println("inside the payscaleTab.jsp"); 
						Map deptMap = (Map)session.getAttribute("deptmap");
						session.setAttribute("deptMap",deptMap);
						
						//Integer codeNum = eisManager.getEmployeeCode();
						Set bankSet = null;

						
						List assPrdList = new ArrayList();
						
						List paySet = null;
						PersonalInformation egpimsPersonalInformation =null;

						String id ="";
						System.out.println("deptSet"+(String)session.getAttribute("viewMode"));
						System.out.println("deptSet"+request.getParameter("Id"));
						if(request.getAttribute("employeeOb")!=null)
						{

						//id = request.getParameter("Id").trim();

						egpimsPersonalInformation = (PersonalInformation)request.getAttribute("employeeOb");

						}
						else
						{
						egpimsPersonalInformation = new PersonalInformation();

						}
						
						if(request.getParameter("Id")!=null)
						{

						id = request.getParameter("Id").trim();
						System.out.println("the id in the jsp ::::: " + id);
						paySet = PayrollManagersUtill.getPayRollService().getPayStructureByEmp(new Integer(id));
						//egpimsPersonalInformation = eisManager.getEmloyeeById(new Integer(id));

						}
						else
						{
						//egpimsPersonalInformation = new PersonalInformation();
						paySet =new ArrayList();
						}

						bankSet = egpimsPersonalInformation.getEgpimsBankDets();
						

						//Removed
						/*if(bankSet.isEmpty())
						bankSet.add(new BankDet());*/
						if(paySet.isEmpty())
						{
						PayStructure payStructure = new PayStructure();
						payStructure.setPayHeader(new PayScaleHeader());
						paySet.add(payStructure);
						}
						if(assPrdList.isEmpty())
						{
						assPrdList = new ArrayList();
						Assignment assignment = new Assignment();
						AssignmentPrd assignmentPrd = new AssignmentPrd();
						assignmentPrd.addAssignment(assignment);

						assPrdList.add(assignmentPrd);
						}


						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");


							
				%>
        </head>
       
		<body  onLoad="populatebasicOnload();populatePayscaleName();setReadOnlyPayrollRelated();populateBranch();">
	
         <html:form  action="/empPayroll/payrollCreate.do?submitType=saveDetailsEmployee&Id=<%=egpimsPersonalInformation.getIdPersonalInformation()%>" >


	<table width ="100%" border="0" cellspacing="0" cellpadding="0" >
	<tr>
	<td>
		  
		  <table width="100%" border="0" cellspacing="0" cellpadding="0" name ="employeeTable" id ="employeeTable" >
	    

               <tr>
                <td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="<%=request.getContextPath()%>/common/image/arrow.gif" /></div>
                <div class="headplacer"><bean:message key="empdetails.heading"/></div></td>
                </tr>
		<tr>
		  <td class="greyboxwk" ><bean:message key="empcode"/></td>
	      <td  class="greybox2wk" >
			<input type="text"  value="<%=egpimsPersonalInformation.getEmployeeCode()%>" readonly="true" />
		  </td>
		  <td   class="greyboxwk " ><bean:message key="empname"/></td>
			<td   class="greybox2wk " >
		    	<input type="text"  value="<%=egpimsPersonalInformation.getEmployeeName()%>" readonly="true" />
			</td>
		</tr>
	   <tr>
	  <td class="greyboxwk" ><span class="mandatory">*</span><bean:message key="emp.status"/></td>
      <td  class="greybox2wk" >
		<%
		Integer statusMaster = new Integer(0);
		EgwStatus employeeStatusMaster = egpimsPersonalInformation.getStatusMaster();
		if(employeeStatusMaster!=null)
		statusMaster = employeeStatusMaster.getId();

		%>
		<select  name="statusMaster" id="statusMaster"class="selectwk" >
		<option value='0' selected="selected"><bean:message key="Choose"/></option>
		<%
		Map employeeStatusMasterMap =(Map)session.getAttribute("statusMasterMap");
		for (Iterator it = employeeStatusMasterMap.entrySet().iterator(); it.hasNext(); )
		{
		Map.Entry entry = (Map.Entry) it.next();
		%>
		<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).intValue() == statusMaster.intValue())? "selected":"")%>><%= entry.getValue() %></option>
		<%
		}
		%>
		</select>
		</td>

		<td   class="greyboxwk " ><bean:message key="Govt.OrdeNo"/></td>
		<td   class="greybox2wk " >
	    <input type="text"  value="<%=egpimsPersonalInformation.getGovtOrderNo()==null?"":egpimsPersonalInformation.getGovtOrderNo().toString()%>"
		name="govtOrderNo" id="govtOrderNo" class="greyboxwk"></td>
		</tr>
		
		
		<tr>
		
		<td class="whiteboxwk"  > <span class="mandatory">*</span><bean:message key="ModeOfRecruitment"/></td>
		<%
		Integer recruiment = new Integer(0);
		RecruimentMaster recruimentmstr = egpimsPersonalInformation.getModeOfRecruimentMstr();
		if(recruimentmstr!=null)
		recruiment = recruimentmstr.getId();

		%>
		<td  class="whitebox2wk">
		<select  name="mOrId" id="mOrId" class="selectwk" >
		<option value='0' selected="selected"><bean:message key="Choose"/></option>
		<%
		Map recruimentMasterMap =(Map)session.getAttribute("recruimentMasterMap");
		for (Iterator it = recruimentMasterMap.entrySet().iterator(); it.hasNext(); )
		{
		Map.Entry entry = (Map.Entry) it.next();
		System.out.println("entry.getKey().toString()"+entry.getKey().toString());
		%>
		<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).intValue() == recruiment.intValue())? "selected":"")%>><%= entry.getValue() %></option>
		<%
		}
		%>
		</select>
		</td>
		<%
		Integer recruimenttype = new Integer(0);
		TypeOfRecruimentMaster recruimenttypemstr = egpimsPersonalInformation.getRecruitmentTypeMstr();
		if(recruimenttypemstr!=null)
		recruimenttype = recruimenttypemstr.getId();

		%>

		    <td  class="whiteboxwk" ><bean:message key="RecruitmentType"/></td>
			<td  class="whitebox2wk" >
			<select  name="recruitmentTypeId" id="recruitmentTypeId" class="selectwk" >
			<option value='0' selected="selected"><bean:message key="Choose"/></option>
			<%
			Map recruimentMap =(Map)session.getAttribute("recruimentMap");
			for (Iterator it = recruimentMap.entrySet().iterator(); it.hasNext(); )
			{
			Map.Entry entry = (Map.Entry) it.next();
			%>
			<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).intValue() == recruimenttype.intValue())? "selected":"")%>><%= entry.getValue() %></option>
			<%
			}
			%>
			</select>
			</td>
		</tr>
		<tr>
		<td class="greyboxwk"><bean:message key="EmployeeCategory"/></td>
		
		<%
		Integer category = new Integer(0);
		CategoryMaster categoryMaster = egpimsPersonalInformation.getCategoryMstr();
		if(categoryMaster!=null)
		category = categoryMaster.getId();

		%>
		<td   class="greybox2wk" >
		<select  name="category" id="category" class="selectwk" >
		<option value='0' selected="selected"><bean:message key="Choose"/></option>
		<%
		Map catMap =(Map)session.getAttribute("catMap");
		for (Iterator it = catMap.entrySet().iterator(); it.hasNext(); )
		{
		Map.Entry entry = (Map.Entry) it.next();
		%>
		<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).intValue() == category.intValue())? "selected":"")%>><%= entry.getValue() %></option>
		<%
		}
		%>
		
		</select>
		<input type="hidden" name="fileId" id="fileId" />
		<input type="hidden" name="fileNo" id="fileNo" />
		
		
		
		<c:if test="${not empty fileNo  and param.mode=='view'}">
		<img src="../images/download.gif" name="ViewFile" onclick="viewFile()" alt="View File" />
		</c:if>
		<c:if test="${empty fileNo     and  param.mode=='modify' }">		
		<img src="../images/download.gif" id="fileImg" name="ViewFile" onclick="viewFile()" alt="View File" style="display:none"/>
		<input type="button" onclick="callFileSearch();" value="Attach File" class="buttonfinal" />
		</c:if>
		<c:if test="${not empty fileNo  and param.mode=='modify'}">
		<img src="../images/download.gif" name="ViewFile" onclick="viewFile()" alt="View File"/>
		<input type="button" onclick="callFileSearch();" value="Attach File" class="buttonfinal" />
		</c:if>
		
		
		</td>
		
		
		<td class="greyboxwk" >
		<bean:message key="GPFAccountNo"/></td>
		<td   class="greybox2wk">
		<input type="text" name="gpf"  value="<%=egpimsPersonalInformation.getGpfAcNumber()==null?"":egpimsPersonalInformation.getGpfAcNumber()%>" 
		class="greyboxwk2"onBlur = "uniqueChecking('/commonyui/egov/uniqueCheckAjax.jsp',  'EG_EMPLOYEE', 'GPF_AC_NUMBER', 'gpf', 'no', 'no');checkMajorsnumeric(this);" >
		</td>
		</tr>

		<%
		Integer payFixedIn = new Integer(0);
		PayFixedInMaster payFixedInMaster = egpimsPersonalInformation.getPayFixedInMstr();
		if(payFixedInMaster!=null)
		payFixedIn = payFixedInMaster.getId();
		%>
		<tr>
		<td  class="whiteboxwk" ><bean:message key="payFixedIn"/></td>
		<td   class="whitebox2wk" >
		<select  name="payFixIn" id="payFixIn" class="selectwk" >
		<option value='0' selected="selected"><bean:message key="Choose"/></option>
		<%
		Map payFixedInMaste =(Map)session.getAttribute("payFixedInMaster");
		for (Iterator it = payFixedInMaste.entrySet().iterator(); it.hasNext(); )
		{
		Map.Entry entry = (Map.Entry) it.next();
		%>
		<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).intValue() == payFixedIn.intValue())? "selected":"")%>><%= entry.getValue() %></option>
		<%
		}
		%>
		
		</select>
		
		</td>
		

		<td class="whiteboxwk"><span class="mandatory">*</span><bean:message key="PaymentMethod"/></td>
			<td class="whitebox2wk">
			<html:select styleId="paymentMethod" property="paymentMethod" styleClass="selectwk" value="<%=egpimsPersonalInformation.getPaymentType()%>">
				<html:option value="0"><bean:message key="Choose"/></html:option>
				<html:option value="cheque"><bean:message key="Cheque"/></html:option>
				<html:option value="cash"><bean:message key="Cash"/></html:option>
				<html:option value="dbt"><bean:message key="DBT"/></html:option>
			</html:select>
			</td>	
		<td>
		<%
		
		Integer age = new Integer(0);
		String dt = "";
			
		%>
		</td>

		
		
		</tr>
        <td colspan="12" class ="greyboxwk"></td>
        <tr>
        <%
		Integer postId = new Integer(0);
		TypeOfPostingMaster postingMaster = egpimsPersonalInformation.getPostingTypeMstr();
		if(postingMaster!=null)
		postId = postingMaster.getId();
		%>
	
		<td  class="greyboxwk">postingType</td>
		<td   class="greybox2wk">
		<select  name="postingTypeId" id="postingTypeId" class="selectwk" >
		<option value='0' selected="selected"><bean:message key="Choose"/></option>
		<%
		Map postingMap =(Map)session.getAttribute("postingMap");
		for (Iterator it = postingMap.entrySet().iterator(); it.hasNext(); )
		{
		Map.Entry entry = (Map.Entry) it.next();
		%>
		<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).intValue() == postId.intValue())? "selected":"")%>><%= entry.getValue() %></option>
		<%
		}
		%>
		
		</select>
		</td>
		<td  class="greyboxwk"><bean:message key="emp.Type"/></td>
		<td   class="greybox2wk">
		<input type="text" class="selectwk grey" name="empType" id="empType" value="<%=egpimsPersonalInformation.getEmployeeTypeMaster().getName()%>" readOnly="true"/>
        </td>
		</tr>

		</tbody>
		</table>
		<br>
			
		<div class="ScrollAuto">
		  <table width="100%" border="1" cellspacing="0" cellpadding="0" name ="PayHeaderTable" id ="PayHeaderTable" >
          <tbody>
          
          	<tr>
				<td colspan="9" class="headingwk">
               		<div class="arrowiconwk"><img src="<%=request.getContextPath()%>/common/image/arrow.gif" /></div>
               		<div class="headplacer"><bean:message  key="payscaledetails.header" /></div>
               	</td>
                
            </tr>
			
			<tr>
				<td class="tablesubheadwk" width="6%" ><span class="mandatory">*</span><bean:message key="EmployeeEffectiveDate"/><bean:message key="DD"/></td>
				<td class="tablesubheadwk" width="3%" ><span class="mandatory">*</span><bean:message key="PayscaleName"/></td>
				<td class="tablesubheadwk" width="7%" ><span class="mandatory">*</span><bean:message key="EmployeeAnnualIncDate"/><bean:message key="DDMM"/></td>
				<td class="tablesubheadwk" width="6%" ><bean:message key="EmployeeBasicFrom"/></td>
				<td class="tablesubheadwk" width="5%" ><bean:message key="EmployeeBasicTo"/></td>
				<td class="tablesubheadwk" width="5%" ><span class="mandatory">*</span><bean:message key="monthlypay"/></td>
				<td class="tablesubheadwk" width="5%" ><span class="mandatory">*</span><bean:message key="dailypay"/></td>
				<td  class="tablesubheadwk" width="4%" ><bean:message key="stagnantpay"/></td>
				<%
					if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
					{
					%>
						<td width="2%" class="tablesubheadwk"><bean:message key="add/del"/></td>
					<%
					}else{
					%>
						<td width="2%" class="tablesubheadwk"></td>
					<%
					}
					%>
			</tr>
		<%

		Iterator paySetitr= paySet.iterator();
		for(int i=0;paySetitr.hasNext();i++)
		{
		PayStructure payStructure = (PayStructure)paySetitr.next();
		%>
		<tr id="PayRow">
		
		<td class="whitebox2wknew">
		 	<input type="hidden" name="payScaleId" id="payScaleId"  value="<%=payStructure.getId()==null?"0":payStructure.getId().toString()%>" />
			<input type="text"  class="greyboxwk2" size="10" name="effDate"  id="effDate"    value="<%=payStructure.getEffectiveFrom()==null?"":sdf.format(payStructure.getEffectiveFrom())%>" 
				onfocus="javascript:vDateType='3'" 
				onkeyup="DateFormat(this,this.value,event,false,'3')"onblur="validateDateFormat(this);CompeffDate(this);populateEff(this);checkForUnique(this);" > 
		</td>
       
		<td class="whitebox2wknew"  >
        	<select  class="selectwk" name="payScaleHeader" id="payScaleHeader"  onChange = "populatebasic(this);">
				<option value="<%=payStructure.getPayHeader().getId()==null?0:payStructure.getPayHeader().getId().intValue()%>" selected="selected">
				<bean:message key="Choose"/></option>
			</select>
		</td>

	     <td class="whitebox2wknew">
			<input type="hidden" name="annualIncrDate" id="annualIncrDate"/>
			<input type="text"  class="greyboxwk2"  size="10" name="annualIncrDateShow" id="annualIncrDateShow"     value="<%=payStructure.getAnnualIncrement()==null?"":sdf.format(payStructure.getAnnualIncrement()).substring(0,5)%>" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="checkIncrDate(this);checkIncrLength(this);" >
		</td>
		<td class="whitebox2wknew"><input type="text"  class="greyboxwk2" size="10" name="basicFrom"  id="basicFrom"    readOnly = "true"  ></td>
		<td class="whitebox2wknew"><input type="text"  class="greyboxwk2" size="10" name="basicto"  id="basicto"    readOnly = "true" ></td>
		<td class="whitebox2wknew">
			<input type="text" class="greyboxwk2" size="10" name="currMnthPay"  id="currMnthPay"     
			value="<%=payStructure.getCurrBasicPay()==null?"":payStructure.getCurrBasicPay().toString()%>" onBlur = "checkBasic(this);"   >
		</td>
		
		<td class="whitebox2wknew">
			<input type="text" class="greyboxwk2" size="10" name="currDailyPay"  id="currDailyPay"    
			value="<%=payStructure.getDailyPay()==null?"":payStructure.getDailyPay().toString()%>"  >
		</td>
		
		<td class="whitebox2wknew">
			<input type="hidden" name="stagnantPayId" class="greyboxwk2" id="stagnantPayId" value="<%=payStructure.getStagnantPay()=='Y'?payStructure.getStagnantPay():'N'%>"/>
			<input type="checkbox" name="stagnantPay"  <%if(payStructure.getStagnantPay()=='Y'){%> checked="true" <%}%> onclick="setCheckBoxValue(this);"/>
		</td>
        <td class="whitebox2wknew">
		<% if(request.getParameter("mode")!=null && !request.getParameter("mode").equals("view")){%>
				<a href="#"><img src="<%=request.getContextPath()%>/common/image/add.png" alt="Add" width="16" height="16" border="0" onclick="addButtonPayscale('PayHeaderTable',this,'PayRow');" /></a>
				<a href="#"><img src="<%=request.getContextPath()%>/common/image/cancel.png" alt="Delete" width="16" height="16" border="0" onclick="deletePayscaleRow('PayHeaderTable',this);" /></a>
		
		<% }%>
		</td>			
		</tr>
		
		<%
		}
		%>
		</tbody></table>
	</div>
			

<br>
		
		
        <table width="100%" border="0" cellspacing="0" cellpadding="0" name ="BDnameTable" id ="BDnameTable">
		
		        <tr>
                <td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="<%=request.getContextPath()%>/common/image/arrow.gif" /></div>
                <div class="headplacer"><bean:message key="AccountDetails"/>	</div></td>
                </tr>
        <tr id = "bamkFields">
		<td  class="tablesubheadwk" ><bean:message key="BankName"/> </td>
		<td  class="tablesubheadwk" ><bean:message key="BranchName"/></td>
		<td  class="tablesubheadwk" ><bean:message key="AccountNumber"/></td>
		<td class="tablesubheadwk" ><bean:message key="SalaryAccount"/></td>
		</tr>
			
		<tr id="BDnameRow">
		<%
		BankDet egpimsBankDet = new  BankDet();

		if(bankSet!=null&&!bankSet.isEmpty())
		{
		Collection c = (Collection)bankSet;
		ArrayList bankList = new ArrayList(c);
		egpimsBankDet = (BankDet)bankList.get(0);
		}
		String salaryBank = "0";
		String viewMode1=((String)(session.getAttribute("viewMode"))).trim();
		if(viewMode1.equals("create"))
		{
		salaryBank = "1";
		}
		else
		{
		if(egpimsBankDet.getSalaryBank()!=null)
		salaryBank = egpimsBankDet.getSalaryBank().toString();
		}
		%>
	
	
		<td class="greybox2wk">

		<%
		Integer bankId = new Integer(0);
		Bank  bmk = egpimsBankDet.getBank();

		if(bmk!=null)
		bankId = bmk.getId();
		%>
		<select  name="bank" id="bank" onBlur = "checkBranch(this);" class="selectwk" >
		<option value='0' selected="selected"><bean:message key="Choose"/></option>
		<%
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
     
		<td class="greybox2wk">
		<%
		//FIXME : Review this code
		Integer branchId = new Integer(0);
		org.egov.commons.Bankbranch bankBranch = egpimsBankDet.getBranch();
		if(bankBranch != null)
		branchId = bankBranch.getId();
		%>
		<select  name="branch" id="branch" onBlur= "checkBank(this);" class="selectwk">
		<option value='0' selected="selected"><bean:message key="Choose"/></option>
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

		<td class="greybox2wk">
		<input type="text"  class="selectwk grey"   name="accountNumber" id="accountNumber"value="<%=egpimsBankDet.getAccountNumber()==null?"":egpimsBankDet.getAccountNumber()%>" 
		onblur="calbLength(this);checkBranchPrsnt(this);" ></td>

	
		<%
		String modeonloadViewsb=((String)(session.getAttribute("viewMode"))).trim();
		if(modeonloadViewsb.equalsIgnoreCase("view"))
		{
		%>

		<%
		if(salaryBank.equals("0"))
		{
		%>
		<td  class="greybox2wk" > 
		<bean:message key="Yes"/>
		<input type="radio" value="1" disabled = "true" name="salaryBank" id="salaryBank" > <bean:message key="No"/>
		<input type="radio" value="0" checked="true" name="salaryBank" id="salaryBank" disabled = "true" >
		</td>
		<%
		}
		else
		{
		%>
		<td   class="greybox2wk" > <bean:message key="Yes"/>
		<input type="radio" disabled = "true" value="1" name="salaryBank" id="salaryBank"  checked="true" >
		<bean:message key="No"/><input type="radio" value="0"  name="salaryBank" id="salaryBank"  disabled = "true">
		</td>
		<%
		}
		%>
		<%
		}
		else
		{
		%>
		<%
		if(salaryBank.equals("0"))
		{
		%>
		<td   class="greybox2wk" >
		<bean:message key="Yes"/>
		<input type="radio" value="1" name="salaryBank" id="salaryBank" > 
		<bean:message key="No"/><input type="radio" value="0" checked="true" name="salaryBank" id="salaryBank" >
		</td>
		<%
		}
		else
		{
		%>
		<td   class="greybox2wk" >
		<bean:message key="Yes"/>
		<input type="radio" value="1" name="salaryBank" id="salaryBank"  checked="true" > <bean:message key="No"/>
		<input type="radio" value="0"  name="salaryBank" id="salaryBank"  >
		</td>
		<%
		}
		%>
		<%
		}

		%>

		</tr>
	<tr>
		<td colspan="4" class="shadowwk"></td>
		</tr>
		<tr>
		<td colspan="4"><div align="right" class="mandatory"><bean:message key="mandatory"/></div></td>
		</tr>
		
		   <table id = "submit" width="97%"  cellpadding ="0" cellspacing ="0" border = "0" >
			
			<td colspan="4" >
			<div class="buttonholderwk">
			<%
			if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
			{
			%>
			<input type="submit" name="button3" id="button3" value="SAVE" class="buttonfinal" onclick="return ButtonPressNew('savenew');" />
			<%
			}
			%>
			</div>
			</td>
			
			</table>
    </table>
	</td>
		</tr>
		</table>
		</div>
		
		</html:form>
		</body>
	</html>