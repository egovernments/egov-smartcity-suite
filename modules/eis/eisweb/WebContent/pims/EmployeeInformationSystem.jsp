<%@ include file="/includes/taglibs.jsp" %>
        

		<%@ page import="java.util.*,
		org.egov.infstr.utils.*,
		org.apache.log4j.Logger,
		org.egov.pims.*,
		org.egov.pims.dao.*,
		org.egov.pims.service.*,
		org.egov.pims.utils.*,
		org.hibernate.LockMode,
		org.egov.pims.model.*,
		org.egov.pims.commons.Position,
		org.egov.commons.*,
		org.egov.pims.commons.client.*,
		org.egov.infstr.commons.dao.*,
		org.egov.pims.commons.dao.*,
		org.egov.infstr.utils.*,
		org.egov.lib.rjbac.dept.*,
		org.egov.lib.address.model.*,
		java.math.BigDecimal,
		org.egov.lib.address.dao.*,
		java.text.SimpleDateFormat,
		java.util.StringTokenizer,
		org.egov.pims.client.*"
		%>
		<html>
		<head>
		
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>eGov Employee Information System (EIS)</title>

		
	<style type="text/css">
		#codescontainer {position:absolute;left:11em;width:9%}
		#codescontainer .yui-ac-content {position:absolute;width:80%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
		#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:40%;background:#a0a0a0;z-index:9049;}
		#codescontainer ul {padding:5px 0;width:80%;}
		#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
		#codescontainer li.yui-ac-highlight {background:#ff0;}
		#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
    </style>

	<style type="text/css">
		#codescontainerPos {position:absolute;left:11em;width:9%}
		#codescontainerPos .yui-ac-content {position:absolute;width:80%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
		#codescontainerPos .yui-ac-shadow {position:absolute;margin:.3em;width:40%;background:#a0a0a0;z-index:9049;}
		#codescontainerPos ul {padding:5px 0;width:80%;}
		#codescontainerPos li {padding:0 5px;cursor:default;white-space:nowrap;}
		#codescontainerPos li.yui-ac-highlight {background:#ff0;}
		#codescontainerPos li.yui-ac-prehighlight {background:#FFFFCC;}
    </style>

		
		<SCRIPT type="text/javascript" src="<%=request.getContextPath()%>/javascript/Employeevalidation.js" type="text/javascript"></SCRIPT>
		<SCRIPT type="text/javascript" src="<%=request.getContextPath()%>/javascript/dateValidation.js"  type="text/javascript"></SCRIPT>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
         
		<%
			String readOnly = "";
		%>

				<%
				       Logger LOGGER = Logger.getLogger("EmployeeInformationSystem.jsp");
						Map deptMap = (Map)session.getAttribute("deptmap");
						session.setAttribute("deptMap",deptMap);

						Set langKnownSet = null;
						Set personAddSet = null;

						
						PersonalInformation egpimsPersonalInformation =null;

						String id ="";
						LOGGER.debug("viewMode="+(String)session.getAttribute("viewMode"));
						LOGGER.debug("id="+request.getParameter("Id"));
						if(request.getAttribute("employeeOb")!=null)
						{

						//id = request.getParameter("Id").trim();

						egpimsPersonalInformation = (PersonalInformation)request.getAttribute("employeeOb");

						}
						else
						{
						egpimsPersonalInformation = new PersonalInformation();

						}
						
						
						String pan = egpimsPersonalInformation.getPanNumber();
					
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");


				%>
 <jsp:include page="/pims/pimsScript.jsp" flush="true"/> 
        
        </head>
		
		<body onload = "onLoadFns();showDeceased();" id="Home">
	

		<div class="formmainbox">
			<div class="insidecontent">
		  <div class="rbroundbox2">
			<div class="rbtop2"><div></div></div>
			  <div class="rbcontent2" width="100%">

		<%
		if(!((String)(session.getAttribute("viewMode"))).trim().equals("create"))
		{
		%>
		
		<div class="datewk"><div class="estimateno" align="left">Employee No: &lt;<%=egpimsPersonalInformation.getEmployeeCode()==null?"":egpimsPersonalInformation.getEmployeeCode().toString() %>&gt;</div></div><%}
		else{%>
		<div class="datewk"><div class="estimateno" align="left"> Employee No: &lt;Not Assigned&gt;</div></div>
		<%} %>

		</div>

		


		
		
		<div id="header" >
		<ul id="Tabs" >
		
		<li id="empPage" class="First Active"><a href="#"  id="PersonalInfo" name="PersonalInfo" onclick="show('PersonalInfo')"><bean:message key="EmployeeDetails"/></a></li>
		<li id="assignmentPage" class=""><a href="#"  id="AssignmentDeatils"  name="AssignmentDeatils" onclick="show('Assignment');loadEmpDesignation();">Assignment details</a></li>
	    <li id="servicePage" class="Befor"><a href="#"  id="ServiceDetails"  name="ServiceDetails" onclick="show('ServiceDetailsHistory')">Service details</a></li>
		<li id="otherPage" class="Last"><a href="#"  id="technicalDetails" onclick="show('Technical')">Other Details</a></li>
		


		</ul>
		</div>
		

			<html:form  action="/pims/AfterPIMSAction.do?submitType=saveDetails" >

			<input type="hidden" name="checkEmpCode"/>

		
				<div id="Technical">
				  <jsp:include page="/pims/technical.jsp"/>
				 </div>

				 <div id="Assignment" >
				  <jsp:include page="/pims/assignmentTabDetails.jsp"/>
				 </div>

				 <div id="Nomimation">
					 <jsp:include page="/pims/nomination.jsp"/>
				 </div>

				<div id="ServiceDetailsHistory">
				   <jsp:include page="/pims/serviceHistoryDetail.jsp"/>

				 </div>
				  

			
		<div id ="PersonalInfoDetails">
		<%
		if(!((String)(session.getAttribute("viewMode"))).trim().equals("create"))
		{
		%>
		<input type="hidden" name="Id" id="Id" value="<%= request.getParameter("Id")==null?"":request.getParameter("Id").trim() %> " />
		<%
		}
		%>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" name ="pisTable" id ="pisTable">
		<tr>
		<td colspan="8" class="headingwk"><div class="arrowiconwk"><img src="<%=request.getContextPath()%>/common/image/arrow.gif" /></div>
		<div class="headplacer"><bean:message key="EmployeePersonalDetails"/></div></td>

		</tr>
		
		<tr>
         <%
		Integer status = new Integer(0); 

		EgwStatus  egwStatusMaster = egpimsPersonalInformation.getStatusMaster();
		if(egwStatusMaster!=null)
		status = egwStatusMaster.getId();
		%>
		<td class="whiteboxwk"><span class="mandatory">*</span>Status:</td>
		<td class="whitebox2wk">
		<select  name="statusTypeId" id="statusTypeId" class="selectwk" onchange="mandatoryDeathFiled(this);">

		<option value='0' selected="selected"><bean:message key="Choose"/></option>
		<%
		Map employeeStatMasterMap =(Map)session.getAttribute("statusMasterMap");
		for (Iterator it = employeeStatMasterMap.entrySet().iterator(); it.hasNext(); )
		{
		Map.Entry entry = (Map.Entry) it.next();
		%>
		<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).intValue() == status.intValue())? "selected":"")%>><%= entry.getValue() %></option>
		<%
		}
		%>
				
								
			

		</select>
				
		<c:if test="${isSuspended}"><span class="mandatory">Employee Suspended</span>
								</c:if>
				</td>
 


				<%
				
		if(((String)(session.getAttribute("viewMode"))).trim().equals("create"))
		
		{
		LOGGER.debug("test----------"+EisManagersUtill.getEisCommonsService());
		if(EisManagersUtill.getEisCommonsService().isEmployeeAutoGenerateCodeYesOrNo()){
		%>
		 <input type="hidden" id="employeeCode" name="employeeCode" size="10"  readOnly = "true" >
		<%}else{%>
			<td  class="whiteboxwk" ><span class="mandatory">*</span><bean:message key="Code"/></td>
		    <td  class="whitebox2wk" align="left">
           <input type="text" id="employeeCode" name="employeeCode" class="selectwk grey" size="10" onblur="checkNumericForCode(this);empAutoGeneratedCode();" maxlength="9">
           </td>
       <%
		}}
		%>
		
		<%
		if(((String)(session.getAttribute("viewMode"))).trim().equals("view"))
		{
		%>
				<td  class="whiteboxwk" ><span class="mandatory">*</span><bean:message key="Code"/></td>
			    <td  class="whitebox2wk" align="left">
			    <input type="text" id="employeeCode" name="employeeCode"  disabled="true" class="selectwk grey" size="10" value="<%=egpimsPersonalInformation.getEmployeeCode()==null?"":egpimsPersonalInformation.getEmployeeCode().toString() %>" >&nbsp;&nbsp;&nbsp;
			    <% if(egpimsPersonalInformation.getIsActive()==0)
		    	{
		    	%>
		    		 <input type="checkbox"   name="employeeActiveCheckbox" id="isEmployeeActive"  disabled="true" value="false" />&nbsp;Is Employee Active
		    	<%
		    	}
		    	else
		    	{	
		    	 %>
		    	 	 <input type="checkbox"   name="employeeActiveCheckbox" id="isEmployeeActive" disabled="true" value="true"  checked="true" />&nbsp;Is Employee Active 
		    	 <%
		    	 }
		    	  %>	
		    </td>
		<%
		}
		 %>   
		
		<%
		if(((String)(session.getAttribute("viewMode"))).trim().equals("modify"))
		{
		 %>
			<td  class="whiteboxwk" ><span class="mandatory">*</span><bean:message key="Code"/></td>
		    <td  class="whitebox2wk" align="left">
		    	<input type="text" id="employeeCode" name="employeeCode" class="selectwk grey" size="10" value="<%=egpimsPersonalInformation.getEmployeeCode()==null?"":egpimsPersonalInformation.getEmployeeCode().toString() %>" onblur="checkNumericForCode(this);empAutoGeneratedCode();" maxlength="9">&nbsp;&nbsp;&nbsp;
		    	<% if(egpimsPersonalInformation.getIsActive()==0)
		    	{
		    	%>
		    		 <input type="checkbox"   name="employeeActiveCheckbox" id="isEmployeeActive"  value="false" onclick="readEmpActive(this)"/>&nbsp;Is Employee Active
		    	<%
		    	}
		    	else
		    	{	
		    	 %>
		    	 	 <input type="checkbox"   name="employeeActiveCheckbox" id="isEmployeeActive"  value="true"  checked="true" onclick="readEmpActive(this)"/>&nbsp;Is Employee Active 
		    	 <%
		    	 }
		    	  %>	
		    </td>
		<%
		}
		 %>

		</tr>
		
		<tr id="death">
			<td  class="whiteboxwk" ><span class="mandatory">*</span>Death Date</td>
	        <td class="whitebox2wk">
			<input type="text" id="deathDate" name="deathDate" value="<%=egpimsPersonalInformation.getDeathDate()==null?"":sdf.format(egpimsPersonalInformation.getDeathDate())%>" onBlur = "validateDateFormat(this);checkDeathDate(this);checkDobWithDeceased();" size="10" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')"class="selectwk">
			</td>
		</tr>
		
		<tr>
		<%
		Integer type = new Integer(0); 
		EmployeeStatusMaster  egTypeMaster = egpimsPersonalInformation.getEmployeeTypeMaster();
		if(egTypeMaster!=null)
		status =egTypeMaster.getId();
		%>
		<td width="16%" class="greyboxwk"><span class="mandatory">*</span>Employee Type</td>
		<td   class="greybox2wk" >
		<select  name="statusMaster" id="statusMaster" class="selectwk" >

		<option value='0' selected="selected"><bean:message key="Choose"/></option>
		<%
		Map employeeTypeMasterMap =(Map)session.getAttribute("employeeStatusMasterMap");
		for (Iterator it = employeeTypeMasterMap.entrySet().iterator(); it.hasNext(); )
		{
		Map.Entry entry = (Map.Entry) it.next();
		%>
		<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).intValue() == status.intValue())? "selected":"")%>><%= entry.getValue() %></option>
		<%
		}
		%>

		</select>

				</td>
		<td width="16%" class="greyboxwk"><span class="mandatory">*</span>Employee Group</td>
		<%
			Integer grpCat = new Integer(3);// default the emp group master to nmc employee 
			EmployeeGroupMaster employeeGroupMaster = egpimsPersonalInformation.getGroupCatMstr();
			if(employeeGroupMaster!=null)
			{
				grpCat = employeeGroupMaster.getId();
			}
			
			
		 %>
			<td   class="greybox2wk" >
			<select  name="empGrpMstr" id="empGrpMstr" class="selectwk" >		
				<option value='0' selected="selected"><bean:message key="Choose"/></option>
				<%
					Map empGrpMstrMap =(Map)session.getAttribute("groupMasterMap");
					for (Iterator it = empGrpMstrMap.entrySet().iterator(); it.hasNext(); )
					{
						Map.Entry entry = (Map.Entry) it.next();
						%>
						<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).intValue() == grpCat.intValue())? "selected":"")%>><%= entry.getValue() %></option>
						<%
					}
				%>
			</select>
			</td>	
				</tr>

				
		<tr>
		
		 <td width="16%" class="whiteboxwk"><span class="mandatory">*</span><bean:message key="EmpName"/>&nbsp;</td>
	    <td colspan="3" class="whitebox2wk">

		<input  value="<%=egpimsPersonalInformation.getEmployeeFirstName()==null?"First Name":egpimsPersonalInformation.getEmployeeFirstName() %>"  class="selectwk grey" type="text" id="firstName"  name="firstName" onblur="setBlur(this,'First Name');checkAlphaNumeric(this);DataTrimStr(this);checkMaxLengthName(this);"   onfocus="setFocus(this,'First Name')" > 

		
		<input  type="text" class="selectwk grey" id="middleName"  name="middleName" 
		onblur="setBlur(this,'Middle Name');checkAlphaNumeric(this);validateName(this);DataTrimStr(this);checkMaxLengthName(this);"  value="<%=egpimsPersonalInformation.getEmployeeMiddleName()==null?"Middle Name":egpimsPersonalInformation.getEmployeeMiddleName() %>" onfocus="setFocus(this,'Middle Name')">

		
		<input  type="text" class="selectwk grey" name="lastName" id="lastName"  
		onblur="setBlur(this,'Last Name');checkAlphaNumeric(this);validateName(this);DataTrimStr(this);checkMaxLengthName(this);" value="<%=egpimsPersonalInformation.getEmployeeLastName()==null?"Last Name":egpimsPersonalInformation.getEmployeeLastName() %>"
		onfocus="setFocus(this,'Last Name')"></td>

		&nbsp;&nbsp;
		</tr>
		
		
		<tr>
		<td class="greyboxwk"><bean:message key="FatherHusbName"/>&nbsp;</td>
		<td colspan="3" class="greybox2wk">

		
		<input  value="<%=egpimsPersonalInformation.getFatherHusbandFirstName()==null?"First Name":egpimsPersonalInformation.getFatherHusbandFirstName() %>" type="text" class="selectwk grey" id="fatherfirstName" name="fatherfirstName" 
		onblur="setBlur(this,'First Name');checkAlphaNumeric(this);validateName(this);DataTrimStr(this);checkMaxLengthName(this);" onfocus="setFocus(this,'First Name')">
		
		
		
		<input  type="text" class="selectwk grey" name="fathermiddleName" id="fathermiddleName" 
		onblur="setBlur(this,'Middle Name');checkAlphaNumeric(this);validateName(this);DataTrimStr(this);checkMaxLengthName(this);" value="<%=egpimsPersonalInformation.getFatherHusbandMiddleName()==null?"Middle Name":egpimsPersonalInformation.getFatherHusbandMiddleName() %>"
		onfocus="setFocus(this,'Middle Name')">
		
		
		<input  type="text" class="selectwk grey" name="fatherlastName" id="fatherlastName"
		onblur="setBlur(this,'Last Name');checkAlphaNumeric(this);validateName(this);DataTrimStr(this);checkMaxLengthName(this);" value="<%=egpimsPersonalInformation.getFatherHusbandLastName()==null?"Last Name":egpimsPersonalInformation.getFatherHusbandLastName() %>"
		onfocus="setFocus(this,'Last Name')"></td>
		</tr>
		<tr>

		
		<td class="whiteboxwk"><span class="mandatory">*</span><bean:message key="EmpDob"/><bean:message key="DD"/></td>
		<td class="whitebox2wk">
		<input type="text" id="employeeDob"  name="employeeDob" class="selectwk grey"  value="<%=egpimsPersonalInformation.getDateOfBirth()==null?"":sdf.format(egpimsPersonalInformation.getDateOfBirth())%>" onblur = "validateDateFormat(this);checkfromDateofBirth(this);checkDateOfBirth(this);checkDobWithDeceased();" size="10" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" >
		<a href="javascript:show_calendar('pIMSForm.employeeDob');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="<c:url value='../common/image/calendar.png' />" border="0"></a>
		</td>
		<td class="whiteboxwk" ><bean:message key="Gender"/>
		</td>
         <td class="whitebox2wk">
		<%
		Integer gender = new Integer(0);
		%>

		<html:select  property="gender" styleId="gender" styleClass="selectwk" >
		<html:option value="0" ><bean:message key="Choose"/></html:option>

        <html:option  value = "M">Male</html:option>
		<html:option  value = "F">Female</html:option>

        </html:select>
        </td>
        </td>
		</tr>

		<tr>
		

		<%
		
		langKnownSet = egpimsPersonalInformation.getEgpimsLangKnowns();
		personAddSet = egpimsPersonalInformation.getEgpimsPersonAddresses();
		%>
		<td  class="greyboxwk"><bean:message key="BloodGroup"/></td>
		<td  class="greybox2wk" >
		<%
		Integer blood = new Integer(0);
		BloodGroupMaster bloodGroupMstr = egpimsPersonalInformation.getBloodGroupMstr();
		if(bloodGroupMstr!=null)
		blood = bloodGroupMstr.getId();
		else
		blood = new Integer(0);
		%>
		<select  name="bloodGroup" id="bloodGroup" class="selectwk" >
		<option value='0' selected="selected"><bean:message key="Choose"/></option>
		<%
		Map bloodGroupMap =(Map)session.getAttribute("bloodGroupMap");
		if(bloodGroupMap!=null&&bloodGroupMap.keySet().isEmpty()!=true)
		{
		for (Iterator it = bloodGroupMap.entrySet().iterator(); it.hasNext(); )
		{
		Map.Entry entry = (Map.Entry) it.next();
		%>
		<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).intValue() == blood.intValue())? "selected":"")%>><%= entry.getValue() %></option>
		<%
		}
		}
		%>
		</select>
		</td>

		</td>
		 
			<td  class="greyboxwk" ><bean:message key="MotherToungue"/></td>
			<td   class="greybox2wk" >
			<select  name="motherTounge"  id="motherTounge" class="selectwk">
			<option value='0' selected="selected"><bean:message key="Choose"/></option>
			<%
			
				String motherToungue = egpimsPersonalInformation.getMotherTonuge();
				LOGGER.debug("Mother Toungue"+motherToungue);
			Map MotherToungueMap =(Map)session.getAttribute("langKnownMap");
			for (Iterator it = MotherToungueMap.entrySet().iterator(); it.hasNext(); )
			{
			Map.Entry entry = (Map.Entry) it.next();
			%>
			<option  value = "<%=entry.getValue().toString()%>"<%=entry.getValue().equals(egpimsPersonalInformation.getMotherTonuge())?"selected":""%>><%= entry.getValue()%></option>
			<%
			 }
			%>
			</select>
			</td>

		</tr>
		
		<tr>
		
		<td  class="whiteboxwk"><bean:message key="Religion"/></td>
		<td  class="whitebox2wk" >
		<%
		Integer religion = new Integer(0);
		ReligionMaster egpimsReligionMstr = egpimsPersonalInformation.getReligionMstr();
		if(egpimsReligionMstr!=null)
		religion = egpimsReligionMstr.getId();
		%>
		<select  name="religionId" id="religionId" class="selectwk" >
		<option value='0' selected="selected"><bean:message key="Choose"/></option>
		<%
		Map religionMap =(Map)session.getAttribute("religionMap");
		if(religionMap!=null&&religionMap.keySet().isEmpty()!=true)
		{
		for (Iterator it = religionMap.entrySet().iterator(); it.hasNext(); )
		{
		Map.Entry entry = (Map.Entry) it.next();
		%>
		<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).intValue() == religion.intValue())? "selected":"")%>><%= entry.getValue() %></option>
		<%
		}
		}
		%>
		</select>
		</td>
		<%
		Integer community = new Integer(0);
		CommunityMaster communityMstr = egpimsPersonalInformation.getCommunityMstr();
		if(communityMstr!=null)
		community = communityMstr.getId();

		%>
		<td class="whiteboxwk">
		<bean:message key="Community"/> </td>
		<td  class="whitebox2wk">
		<select  name="communityId" id="communityId" class="selectwk" >
		<option value='0' selected="selected" ><bean:message key="Choose"/></option>
		<%
		Map commMap =(Map)session.getAttribute("commMap");
		LOGGER.debug("commMap"+commMap);
		for (Iterator it = commMap.entrySet().iterator(); it.hasNext(); )
		{
		Map.Entry entry = (Map.Entry) it.next();
		%>
		<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).intValue() == community.intValue())? "selected":"")%>><%= entry.getValue() %></option>
		<%
		}
		%>
		</select>
		</td>
		
		</tr>
		<tr>
		<td  class="greyboxwk"><span class="mandatory">*</span><bean:message key="PhysicallyHC"/></td>
		<%

		String hc = "0";
		String medStr ="0";
		Character handy = egpimsPersonalInformation.getIsHandicapped();
		Character med = egpimsPersonalInformation.getIsMedReportAvailable();
		if(handy !=null)
		{
		hc = handy.toString();
		}
		if(med!=null)
		{
		medStr = med.toString();
		}
		%>
		<%
		String modeonloadViewphand=((String)(session.getAttribute("viewMode"))).trim();
		if(modeonloadViewphand.equalsIgnoreCase("view"))
		{
		%>
		<%
		if(hc.equals("0"))
		{
		%>

		<td class="greybox2wk"> <bean:message key="Yes"/>
		<input type="radio" value="1" disabled = "true" name="phand" id="phand" > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:message key="No"/><input  type="radio" value="0" checked="true" name="phand" disabled = "true" id="phand">
		</td>
		<%
		}
		else
		{
		%>
		<td class="greybox2wk" > <bean:message key="Yes"/>
		<input type="radio" value="1" disabled = "true" name="phand" id="phand"  checked="true"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:message key="No"/><input type="radio" value="0"  name="phand" disabled = "true" id="phand">
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
		if(hc.equals("0"))
		{
		%>

		<td  class="greybox2wk" > <bean:message key="Yes"/>
		<input type="radio" value="1" name="phand" id="phand" > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:message key="No"/><input  type="radio" value="0" checked="true" name="phand" id="phand">
		</td>
		<%
		}
		else
		{
		%>
		<td class="greybox2wk"> <bean:message key="Yes"/>
		<input type="radio" value="1" name="phand" id="phand"  checked="true"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:message key="No"/><input type="radio" value="0"  name="phand" id="phand">
		</td>
		<%
		}
		%>

		<%
		}
		%>

		<%
		String modeonloadViewmed=((String)(session.getAttribute("viewMode"))).trim();
		if(modeonloadViewmed.equalsIgnoreCase("view"))
		{
		%>



		<%
		if(medStr.equals("0"))
		{

		%>
		<td class="greyboxwk" >
		<bean:message key="MedicalReport"/> </td>
		<td  class="greybox2wk">
		<bean:message key="Yes"/>
		<input type="radio" value="1" disabled = "true" name="isMed" id="isMed"  > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:message key="No"/><input type="radio" value="0" name="isMed" disabled = "true"  checked="true" id="isMed" >
		</td>
		<%
		}
		else
		{

		%>
		<td class="greyboxwk">
		<bean:message key="MedicalReport"/> </td>
		<td class="greybox2wk">
		<bean:message key="Yes"/>
		<input type="radio" id="isMed" disabled = "true" checked="true" value="1"  name="isMed"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:message key="No"/><input type="radio" value="0" disabled = "true" name="isMed" id="isMed" >
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
		if(medStr.equals("0"))
		{

		%>
		<td class="greyboxwk" >
		<bean:message key="MedicalReport"/> </td>
		<td  class="greybox2wk" >
		<bean:message key="Yes"/>
		<input type="radio" value="1" name="isMed" id="isMed"  > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:message key="No"/><input type="radio" value="0" name="isMed" checked="true" id="isMed" >
		</td>
		<%
		}
		else
		{

		%>
		<td class="greyboxwk" >
		<bean:message key="MedicalReport"/> </td>
		<td class="greybox2wk" >
		<bean:message key="Yes"/>
		<input type="radio" id="isMed" checked="true" value="1"  name="isMed"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:message key="No"/><input type="radio" value="0" name="isMed" id="isMed" >
		</td>
		<%
		}
		%>
		<%


		}
		%>




		</tr>
				<tr>
		<td  class="whiteboxwk"><bean:message key="EmpIDMark1"/></td>
		<td class="whitebox2wk"  ><span class="whitebox2wk">

		<textarea  class="selectwk textmxwidth"  onblur="checkIdentAlphaNumeric(this);checkMaxLengthName(this);" name="identificationMarks1" id="identificationMarks1"  size="10"><%=egpimsPersonalInformation.getIdentificationMarks1()==null?"":egpimsPersonalInformation.getIdentificationMarks1() %></textarea>
		</span>
		</td>
		<td  class="whiteboxwk"><bean:message key="EmpIDMark2"/></td>
		<td  class="whitebox2wk"  >

		<textarea class="selectwk textmxwidth" id="identificationMarks2"  name="identificationMarks2"  onblur="checkIdentAlphaNumeric(this);checkMaxLengthName(this);" size="10"><%=egpimsPersonalInformation.getIdentificationMarks2()==null?"":egpimsPersonalInformation.getIdentificationMarks2() %></textarea>
		</span>
		</td>
		</tr>

		

		<%
		Set presonAdd = (Set)egpimsPersonalInformation.getEgpimsPersonAddresses();

		Iterator itr = presonAdd.iterator();
		Address addressper =new Address();
		Address addresspre =new Address();
		for(int i=0;itr.hasNext();i++)
		{
		PersonAddress egPadd = (PersonAddress)itr.next();
		Address address = egPadd.getPersonAddress();
		AddressTypeMaster addressType = address.getAddTypeMaster();
		if(addressType!=null && addressType.getAddressTypeName()!=null && addressType.getAddressTypeName().equals("PRESENTADDRESS"))
		addresspre = address;
		else
		addressper = address;
		}
		%>


	<tr>
		<td   class="greyboxwk" ><span class="mandatory">*</span><bean:message key="PermanentAddress"/></td>
		
		<td class="greybox2wk"><textarea   class="selectwk textmxwidth" name="propertyNoper" id="propertyNoper" onBlur = "checkMaxLengthName(this);" ><%=addressper.getStreetAddress1()==null?"":addressper.getStreetAddress1()%></textarea></td>

		<!--	<td class="fieldcell" colspan="4"><input  type="text"  style="width:306px" name="propertyNopre" id="propertyNopre"   value="<%=addresspre.getStreetAddress1()==null?"":addresspre.getStreetAddress1()%>" onBlur = "checkMaxLengthName(this);"></td>-->

		<!-- Corresponding Address-->

		<td   class="greyboxwk"><bean:message key="CorrAddress"/></td></a>
		
		<td class="greybox2wk" ><textarea  class="selectwk textmxwidth" name="streetNameper" id="streetNameper" onBlur = "checkMaxLengthName(this);" ><%=addressper.getStreetAddress2()==null?"":addressper.getStreetAddress2()%></textarea></td>

	<!--	<td class="fieldcell" colspan="4" ><input  type="text"  style="width:306px" name="streetNamepre" id="streetNamepre"   value="<%=addresspre.getStreetAddress2()==null?"":addresspre.getStreetAddress2()%>" onBlur = "checkMaxLengthName(this);" ></td>-->
</tr>
		
		
		
		   <%
			String mod1=((String)(session.getAttribute("viewMode"))).trim();
			if(mod1.equalsIgnoreCase("modify") || mod1.equalsIgnoreCase("view"))
			{
			%>
			<td  class="whiteboxwk">Languages Known </td>
			<td  class="whitebox2wk">
			<select  name="lanId"  id="lanId" class="selectwk" multiple="true">
			<%
			Map langKnownMap =(Map)session.getAttribute("langKnownMap");
			LangKnown lan=null;
			String name = null;
			Integer idss = null;
			Map hashLang = new HashMap();

			Set newLang=new HashSet();

			for (Iterator it = langKnownMap.entrySet().iterator(); it.hasNext(); )
			{
			Map.Entry entry = (Map.Entry) it.next();

			for(Iterator it1 = langKnownSet.iterator();it1.hasNext();)
			{
			lan = (LangKnown)it1.next();

			idss = lan. getLangKnown().getId();

			newLang.add(idss);
			hashLang.put(lan. getLangKnown().getId(),lan. getLangKnown().getName());

			}

			if(newLang.contains(entry.getKey()))
			{

			%>
			<option  value = "<%= entry.getKey().toString() %>"<%="selected"%>><%=entry.getValue() %></option>
			<%
			 }
			else
			{
			if(mod1.equalsIgnoreCase("modify"))
            {
			%>
			<option  value = "<%= entry.getKey().toString() %>"><%= entry.getValue() %></option>
			<%
			    }
		      }
			}
			%>
			</select>
			</td>

			<%
			}
			else if(mod1.equalsIgnoreCase("create"))
			{
			%>
			<td  class="whiteboxwk">Languages Known </td>
			<td class="whitebox2wk">

			<select  name="lanId" id="lanId" class="selectwk" multiple="true">
			<option value='0' selected="selected">----Choose----</option>
			<%
			Map langKnownMap =(Map)session.getAttribute("langKnownMap");
			for (Iterator it = langKnownMap.entrySet().iterator(); it.hasNext(); )
			{
			Map.Entry entry = (Map.Entry) it.next();
			%>
			<option  value = "<%= entry.getKey().toString() %>"><%= entry.getValue() %></option>
			<%
			}
			%>
		</select>


		</td>
		<%
		}
		%>
		<td class="whiteboxwk"><bean:message key="LocalLanguageQualified"/></td>
		<td class="whitebox2wk">
		<%
		Integer language = new Integer(0);
		LanguagesQulifiedMaster languagesQulifiedMaster = egpimsPersonalInformation.getLangQulMstr();
		if(languagesQulifiedMaster!=null)
		language = languagesQulifiedMaster.getId();

		%>
		<select  name="tamillangaugequlified" id="tamillangaugequlified" class="selectwk" >
		<option value='0' selected="selected"><bean:message key="Choose"/></option>
		<%
		Map langQualiMap =(Map)session.getAttribute("langQualiMap");
		for (Iterator it = langQualiMap.entrySet().iterator(); it.hasNext(); )
		{
		Map.Entry entry = (Map.Entry) it.next();
		%>
		<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).intValue() == language.intValue())? "selected":"")%>><%= entry.getValue() %></option>
		<%
		}
		%>
		</select>
		</td>

		</tr>
	
		<tr>

		<td  class="greyboxwk"><bean:message key="PanNumber"/> </td>

		<td  class="greybox2wk">					
        
		<input type="text" id="panNumber"  name="panNumber" value="<%=egpimsPersonalInformation.getPanNumber()==null?"":egpimsPersonalInformation.getPanNumber()%>" class="selectwk" type="text" id="panNumber" onblur="checkPanAlphaNumeric(this);calLength(this);checkunique();"name="panNumber" size="10" >
		</td>
		<td colspan="2" class="greybox2wk">&nbsp;</td>
		</tr>

		<tr>
		<td class="whiteboxwk" ><span class="mandatory">*</span>Date of Appointment</td>
		<td  class="whitebox2wk"><span><input class="selectwk grey" type="text"  value="<%=egpimsPersonalInformation.getDateOfFirstAppointment()==null?"":sdf.format(egpimsPersonalInformation.getDateOfFirstAppointment())%>" name="dateOfFA" id="dateOfFA"  class="selectwk" onBlur = "validateDateFormat(this);chckprsnt(this);CompfaDate(this);checkDateofAppontment(this);populateJoinDate(this)" size="10"  onfocus="setFocus(this,'dd/mm/yyyy');javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" >
		<a href="javascript:show_calendar('pIMSForm.dateOfFA');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="<c:url value='../common/image/calendar.png' />" border="0"></a></span></td>

		<td class="whiteboxwk">Date of Joining/Deputation</td>
		<td class="whitebox2wk"><span><input type="text"  class="selectwk grey" value="<%=egpimsPersonalInformation.getDateOfjoin()==null?"":sdf.format(egpimsPersonalInformation.getDateOfjoin())%>" name="dateOfjoin" id="dateOfjoin" class="selectwk" onBlur = "validateDateFormat(this);CompfaDate(this);" size="10"  onfocus="setFocus(this,'dd/mm/yyyy');javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" >
		<a href="javascript:show_calendar('pIMSForm.dateOfjoin');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="<c:url value='../common/image/calendar.png' />" border="0"></a></span></td>



		</tr>
		<tr>

		<td  class="greyboxwk"><bean:message key="EmployeeRetirementAge"/></td>
		<td class="greybox2wk">
		<input type="text" name="retirementAge"  id="retirementAge"   value = "<%=egpimsPersonalInformation.getRetirementAge()==null?"":egpimsPersonalInformation.getRetirementAge().toString() %>"  size="10" class="selectwk" onBlur = "checkNumericForAge(this);populateRetDate(this);">
		</td>


		<td  class="greyboxwk"><bean:message key="EmployeeRetirementDate"/> </td>
		<td  colspan="3" class="greybox2wk" width="305">
		<input type="text" name="dateOfRetirement" class="selectwk grey" id="dateOfRetirement" value = "<%=egpimsPersonalInformation.getRetirementDate()==null?"":sdf.format(egpimsPersonalInformation.getRetirementDate()) %>" size="10" onBlur = "validateDateFormat(this); checkRetire(this);"onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')"onchange="populateAge(this)">
		</td>

		</tr>
				<tr>
		<td colspan="4" class="subheadnew"><div class="arrowiconwk"><img src="<%=request.getContextPath()%>/common/image/arrowsmall.gif" width="17" height="13" alt="Arrow" /></div>
		<bean:message key="UserDetails"/></td>
		</tr>
		<tr>
		<td  class="whiteboxwk" ><bean:message key="UserActive"/></td>
		<%
		String uac="0";
		Integer uactive= new Integer(0);
		if(egpimsPersonalInformation.getUserMaster()!=null)
		uactive= egpimsPersonalInformation.getUserMaster().getIsActive();
		if(uactive!=null)
		{
		uac= uactive.toString();
	}

		%>
		<%
		String modeonloadViewuserStatus=((String)(session.getAttribute("viewMode"))).trim();
		if(modeonloadViewuserStatus.equalsIgnoreCase("view"))
		{
		%>
		<%
		if(uac.equals("0"))
		{
		%>

		<td class="whitebox2wk" > <bean:message key="Yes"/>
		<input type="radio" value="1"  disabled = "true" name="userStatus" id="userStatus" > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:message key="No"/><input type="radio" value="0"  checked="true" name="userStatus" id="userStatus"  disabled = "true">
		</td>
		<%

		}
		else
		{
		%>
		<td class="whitebox2wk" > <bean:message key="Yes"/>
		<input  disabled = "true" type="radio" value="1" name="userStatus" id="userStatus"  checked="true"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:message key="No"/><input type="radio" value="0"  name="userStatus" id="userStatus"  disabled = "true">
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
		if(uac.equals("0"))
		{
		%>

		<td  class="whitebox2wk"  > <bean:message key="Yes"/>
		<input type="radio" value="1" name="userStatus" id="userStatus" > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:message key="No"/><input type="radio" value="0"  checked="true" name="userStatus" id="userStatus">
		</td>
		<%

		}
		else
		{
		%>
		<td class="whitebox2wk" > <bean:message key="Yes"/>
		<input type="radio" value="1" name="userStatus" id="userStatus"  checked="true"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:message key="No"/><input type="radio" value="0"  name="userStatus" id="userStatus">
		</td>
		<%
		}
		%>



		<%
		}
		%>
        <%
		String modeonViewUser=((String)(session.getAttribute("viewMode"))).trim();
		if(!modeonViewUser.equalsIgnoreCase("view"))
		{
		%>

		<td  class="whiteboxwk"><bean:message key="EmployeeUserName"/></td>
		<td  class="whitebox2wk" >
			<input  value="<%=egpimsPersonalInformation.getUserMaster()==null?"":egpimsPersonalInformation.getUserMaster().getUserName() %>"  class="selectwk" type="text" id="userFirstName" name="userFirstName" size="10"  onblur="checkUserName(this);checkIsUserMapped();checkUserLength(this);"  >
			<html:button styleClass="buttonfinal"  property="p2" value="Search" onclick="SearchUser();"/>
        </td>
        <%

		}
		else
		{
		%>

		<td  class="whiteboxwk" ><bean:message key="EmployeeUserName"/>
		<td   class="whitebox2wk"  >
			<input  value="<%=egpimsPersonalInformation.getUserMaster()==null?"":egpimsPersonalInformation.getUserMaster().getUserName() %>"  class="selectwk" type="text" id="userFirstName" name="userFirstName" size="10"    >
        </td>
        <%
		}
		%>
		

		</tr>
		<tr>
            <td colspan="4"><div align="right" class="mandatory">* Mandatory Fields</div></td>
          </tr>
         <%
		if(((String)(session.getAttribute("viewMode"))).trim().equals("modify"))
		{
		%>
		<tr>
               <td colspan="4" class="shadowwk"></td>

             </tr>

		<tr>
		
		</tr>
		<%
		}
		%>
		
		
		

		


				
		<!-- Body Section Ends -->
		
		</table>
		
		</div>
		<%if((!((String)(session.getAttribute("viewMode"))).trim().equals("create")) &&
		(!((String)(session.getAttribute("viewMode"))).trim().equals("view")))
		{%>
		<div >
		<table id="EOtableRmr" cellspacing="0" cellpadding="0" border="0">
		<tr>
			<td  class="whiteboxwk" ><span class="mandatory">*</span><bean:message key="Remarks"/></td>
			<td  colspan="3" class="whitebox2wk" >
				<textarea  cols="50" class="selectwk"  name="modifyremarks" id="modifyremarks" onblur="checkMaxLengthName(this);" /></textarea></td>
			</tr>
		</table>
		</div>
		<%} %>
		<div class="rbbot2"><div></div></div>
		</div>
		  </div>
		</div>
		<%
		if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
		{
		%>
		<div align="center" class="buttonholderwk">

		<center><html:button styleClass="buttonfinal"  value="Save" property="b2" onclick="ButtonPressNew('savenew');" />
		<input type="button" name="button" id="button" value="Close"  class="buttonfinal" onclick="window.close();"/>
		</center>
	
		</div>
		<%}else{%>
		<div align="center" class="buttonholderwk">
		<center>
		<input type="button" name="button" id="button" value="Close"  class="buttonfinal" onclick="window.close();"/>
		</center>
	
		</div>
		<%} %>
		</html:form>
	
		</body>
		
		</html>


