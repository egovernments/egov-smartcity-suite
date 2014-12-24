
<%@ include file="/includes/taglibs.jsp" %>

<%@ page import="java.util.*,
		 org.egov.infstr.utils.*,
		 org.apache.log4j.Logger,
		 org.egov.pims.dao.*,
		 org.egov.pims.model.*,
		 org.egov.pims.empLeave.model.*,
		 org.egov.pims.utils.*,
		 org.egov.pims.service.*,
		 org.egov.commons.service.*,
		 org.egov.commons.ObjectType,
		 org.egov.infstr.commons.service.*,
		 org.egov.pims.commons.client.*,
		 org.egov.infstr.commons.dao.*,
		 org.egov.infstr.commons.*,
		 org.egov.exceptions.EGOVRuntimeException,
		 org.egov.lib.address.dao.AddressDAO,
		 java.util.StringTokenizer,
		 org.egov.lib.rjbac.user.User,
		 org.egov.lib.rjbac.dept.ejb.api.*,
		 org.egov.infstr.utils.*,
		 org.egov.pims.commons.dao.*,
		 org.egov.pims.commons.Position,
		 org.egov.commons.*,
		 org.egov.pims.commons.DesignationMaster,
		 org.egov.infstr.utils.*,
		 org.egov.lib.rjbac.dept.ejb.api.*,
		 org.egov.lib.address.dao.AddressTypeDAO,
		 org.egov.lib.address.model.*,
		 org.egov.lib.address.dao.*,
		 java.text.SimpleDateFormat,
		 org.egov.pims.client.*"
%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Search Employee</title>
		<link href="../common/css/eispayroll.css" rel="stylesheet" type="text/css" />
		<link href="../common/css/commonegov.css" rel="stylesheet" type="text/css" />
		<style type="text/css">
		#codescontainer {position:absolute;left:11em;width:9%}
		#codescontainer .yui-ac-content {position:absolute;width:80%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
		#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:40%;background:#a0a0a0;z-index:9049;}
		#codescontainer ul {padding:5px 0;width:80%;}
		#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
		#codescontainer li.yui-ac-highlight {background:#ff0;}
		#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
    </style>
	<link type="text/css" rel="stylesheet" href="../commonyui/build/reset/reset.css"> <link type="text/css" rel="stylesheet" href="../commonyui/build/fonts/fonts.css">

       <SCRIPT type="text/javascript" src="${pageContext.request.contextPath}/javascript/dateValidation.js" type="text/javascript"></SCRIPT>
	<SCRIPT type="text/javascript" src="<%=request.getContextPath()%>/javascript/commoneis.js" type="text/javascript"></SCRIPT>


<%!
String mast ="";
Integer userId =new Integer(0);
Logger LOGGER = Logger.getLogger("searchEmployee.jsp");

%>
<%
String essStr="";//if ess exist in request add it to the urls of leavetx and leave card
userId = (Integer) session.getAttribute("com.egov.user.LoginUserId");
String module = (String)request.getAttribute("module");
 if(request.getParameter(EisConstants.ESS)!=null && !request.getParameter(EisConstants.ESS).trim().equals(""))
 {
 essStr=EisConstants.ESS+"="+request.getParameter(EisConstants.ESS)+"&";
 }

%>

<%!
	public boolean checkSupEmployee(Integer emp,ObjectType objectType)
	{
		boolean b = false;
		try
		{
			boolean check = EisManagersUtill.getEmpLeaveService().checkLeave(emp);
			if(check==true)
			{

				PersonalInformation supiriorEmp =null;

				if(userId!=null)
					supiriorEmp = EisManagersUtill.getEmployeeService().getEmpForUserId(userId);
				Position pos =EisManagersUtill.getEmployeeService().getPositionforEmp(emp);

				User user = EisManagersUtill.getEisCommonsService().getSupUserforPositionandObjectType(pos,objectType);
				PersonalInformation personalInformation = null;
				if(user != null)
				{
					personalInformation = EisManagersUtill.getEmployeeService().getEmpForUserId(user.getId());
				}

				Integer supId = new Integer(0);

				if(personalInformation!=null)
				{
					supId = personalInformation.getIdPersonalInformation();
				}

				if(supiriorEmp!=null)
				{
					  if(supId.equals(supiriorEmp.getIdPersonalInformation()))
					 {
						b=true;
					 }
					 else
					 {
						b=false;
					 }
				 }
				 else
				 {
					b=false;
				 }
			}
		}
		catch(Exception e)
		{
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
	 	return b;
	}
%>
<%

    	String mode = (String)request.getAttribute("mode");

    	String master = (String)request.getAttribute("masters");

    	mast = master;

    	String con = master.trim()+mode.trim();


    %>
<%!
public Map getMapIdVsOptions(List employeeList)
{

	Map IdVsoptionsMap = new HashMap();

	try{
		//ServiceLocator 		serviceloc           = 	ServiceLocator.getInstance();
		//EisManagerHome	eisManagerHome					=	(EisManagerHome)serviceloc.getLocalHome("EisManagerHome");
		//EisManager 		eisManager						=	eisManagerHome.create();
		PersonalInformation egpimsPersonalInformation =null;
		Set ltcSet = new HashSet();
		Set dpSet = new HashSet();
		Set lpSet = new HashSet();
		Set elSet = new HashSet();
		Set tpSet = new HashSet();

		Set lSet = new HashSet();
		List empIdsList = new ArrayList();
		if(employeeList!= null && !employeeList.isEmpty())
		{

			Iterator iter = employeeList.iterator();
			while (iter.hasNext())
			{
				//Map optionsMap = new HashMap();
				//List compOffsList = null;
				EmployeeView cataEl = (EmployeeView)iter.next();
				Integer empId = cataEl.getId();
				//egpimsPersonalInformation = eisManager.getEmloyeeById(empId);
				if(empId != null && !empIdsList.contains(empId))
					empIdsList.add(empId);
			}
		}
		List persInfoList = null;
		if(empIdsList != null && !empIdsList.isEmpty())
		{
			persInfoList = EisManagersUtill.getEmployeeService().getListOfPersonalInformationByEmpIdsList(empIdsList);
		}
		if(persInfoList != null && !persInfoList.isEmpty())
		{
			for(Iterator perInfoIter = persInfoList.iterator(); perInfoIter.hasNext();)
			{
				Map optionsMap = new HashMap();
				List compOffsList = null;
				egpimsPersonalInformation = (PersonalInformation)perInfoIter.next();
				if(mast.equals("AvailedParticulars"))
				{
					ltcSet = egpimsPersonalInformation.getEgpimsLtcPirticularses();
					if(ltcSet!=null&&!ltcSet.isEmpty())
						optionsMap.put("AvailedParticulars","true");
					else
						optionsMap.put("AvailedParticulars","false");

				}
				else if(mast.equals("Disciplinary"))
				{
					dpSet= egpimsPersonalInformation.getEgpimsDisciplinaryPunishments();
					if(dpSet!=null&&!dpSet.isEmpty())
						optionsMap.put("Disciplinary","true");
					else
						optionsMap.put("Disciplinary","false");
				}
				else if(mast.equals("LeaveApplication"))
				{
					lSet = egpimsPersonalInformation.getLeaveApplicationSet();
					if(lSet!=null&&!lSet.isEmpty())
						optionsMap.put("LeaveApplication","true");
					else
						optionsMap.put("LeaveApplication","false");
				}
				else if(mast.equals("TraningPirticulars"))
				{
					tpSet = egpimsPersonalInformation.getEgpimsTrainingPirticularses();
					if(tpSet!=null&&!tpSet.isEmpty())
						optionsMap.put("TraningPirticulars","true");
					else
						optionsMap.put("TraningPirticulars","false");
				}
				else if(mast.equals("Employee")||mast.equals("EmployeeLight")||mast.equals("Assignment"))
				{
					optionsMap.put("Employee","true");
					optionsMap.put("EmployeeLight","true");
					optionsMap.put("Assignment","true");


				}
				else if(mast.equals("CompOff"))
				{
					compOffsList =  EisManagersUtill.getEmpLeaveService().getListCompOffObjects(egpimsPersonalInformation.getIdPersonalInformation());
					if(compOffsList!=null && !compOffsList.isEmpty())
						optionsMap.put("CompOff","true");
					else
						optionsMap.put("CompOff","false");
				}
				else if(mast.equals("Pension"))
				{
				optionsMap.put("Pension","true");
			  }
				IdVsoptionsMap.put(egpimsPersonalInformation.getIdPersonalInformation(),optionsMap);

			}
		}
		}catch(Exception e)
		{
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
	return IdVsoptionsMap;

}
%>
<script>
	function execute()
	{
		var target="<%=(request.getAttribute("alertMessage"))%>";
		
	<c:if test="${not empty ess}">
	document.getElementById('searchTable').style.display="none";
	</c:if>
		  <%if(request.getParameter("departmentId")!=null && !request.getParameter("departmentId").equals("")){%>
	     document.searchForm.departmentId.value = <%=request.getParameter("departmentId")%>;
	     <%}else{%>
	     document.searchForm.departmentId.value=0;
	     <%}%>
	    
		 if(target!=null && target != "null")
		 {
			alert("<%=request.getAttribute("alertMessage")%>");
			<%	request.setAttribute("alertMessage",null);	%>
		 }
	}

	function SetActionValue(value,master,mode,prdId)
  	{

	<%

		List listOfEmp = (List)request.getAttribute("employeeList");
		Map optionsMap = getMapIdVsOptions(listOfEmp);

    		Set empIdSet = optionsMap.keySet();
		for(Iterator iter = empIdSet.iterator();iter.hasNext();)
		{
				Integer empId = (Integer)iter.next();
		%>
			if(value=='<%= empId %>')
			{


		<%

				Map booleanMap = (Map)optionsMap.get(empId);
				if(mode.equalsIgnoreCase("View")||mode.equals("Modify"))
				{

					String val = (String)booleanMap.get(master);

					if(val.equals("true"))
					{
						if(master.equals("Pension") && mode.equals("Modify"))
						{
						%>
                      		window.location ="${pageContext.request.contextPath}/empPension/AfterPensionSearchAction.do?submitType=beforeModifyDetails&Id="+value+"&master="+master;

						<%
						}
						else if(master.equals("Pension") && mode.equals("View"))
						{
							%>
                      		window.location ="${pageContext.request.contextPath}/empPension/AfterPensionSearchAction.do?submitType=beforeViewDetails&Id="+value+"&master="+master;

						<%
						}
						else if(master.equals("Disciplinary")||master.equals("LeaveApplication"))
						{
							if(master.equals("Disciplinary"))
							{
		%>						//window.location ="${pageContext.request.contextPath}/pims/BeforeListAction.do?submitType=beforeCreate&master="+master+"&action="+mode+"&Id="+value;
								window.location ="${pageContext.request.contextPath}/disciplinaryPunishment/disciplinaryPunishment!loadToList.action?mode="+mode+"&disEmpId="+value+"&modifyType=menutree";
		<%					}
							else
							{
		%>
								window.location ="${pageContext.request.contextPath}/pims/BeforeListAction.do?submitType=beforeCreate&master="+master+"&action="+mode+"&Id="+value;
		<%
							}
						}
						else
						{
		%>
							window.location ="${pageContext.request.contextPath}/pims/BeforePIMSMasterAction.do?submitType=setIdForDetails"+mode+"&master="+master+"&Id="+value+"&prdId="+prdId;
		<%
						}

					}
					else
					{
		%>
						alert('Master is not created i.e cannot modify or view');
		<%
					}
		%>

	<%
				}
				else if(mode.equals("Create"))
				{
					if(master.equals("Pension"))
					{
	%>
						window.location ="${pageContext.request.contextPath}/empPension/AfterPensionSearchAction.do?submitType=populateDetails&Id="+value+"&master="+master;
	<%
					}
					else if(master.equals("LeaveApplication"))
					{
				%>
						window.location ="${pageContext.request.contextPath}/leave/BeforeLeaveAction.do?submitType=beforeCreate&master="+master+"&Id="+value;
	           <%

					}
					else if(master.equals("Disciplinary"))
					{
					
				%>					
						window.location ="${pageContext.request.contextPath}/disciplinaryPunishment/disciplinaryPunishment!loadToCreate.action?mode=create&disEmpId="+value;
				<%
					}
					else if(master.equals("TraningPirticulars"))
					{
				%>				
					window.location ="${pageContext.request.contextPath}/pims/BeforePIMSMasterAction.do?submitType=beforeCreate&master="+master+"&Id="+value;
				<%	
					}
					
				}
				else
				{
					 if(master.equals("LeaveApplication") || master.equals("Disciplinary"))
					 {
		%>
						window.location ="${pageContext.request.contextPath}/pims/BeforeListAction.do?submitType=beforeCreate&Id="+value+"&master="+master+"&action=approve";
		<%
					}
					else if(master.equals("CompOff"))
					{
		%>
						window.location ="${pageContext.request.contextPath}/pims/BeforeListAction.do?submitType=beforeCreate&Id="+value+"&master="+master+"&action=approve";

		<%
					}

				}
	%>

			}

	<%
		}
	%>
  }

	function showMsg()
  	{
	  	
		<c:if test="${!(mode=='View' && masters=='Employee')}">
		if(!checkDeptMandatory(document.searchForm.departmentId.options.selectedIndex)){
				return false;
		}
		</c:if>
		
		<c:if test="${(masters=='Employee')}"> 
		
		if(document.searchForm.designationId.options.selectedIndex ==0 && document.searchForm.departmentId.options.selectedIndex ==0 && 
				document.searchForm.code.value == "" && document.searchForm.name.value == "" 
					&& document.searchForm.functionaryId.options.selectedIndex != 0 )
	      {
			 alert("please select any one of the fields in (Department,Designation,Employee name,Employee code)");
			 return false;
		  }
		if(document.searchForm.designationId.options.selectedIndex ==0 && document.searchForm.departmentId.options.selectedIndex ==0 && 
				document.searchForm.code.value == "" && document.searchForm.name.value == "" 
				&& document.searchForm.status.options.selectedIndex !=0)
		 {
			 alert("please select any one of the fields in (Department,Designation,Employee name,Employee code)");
			 return false;
		}
		if(document.searchForm.designationId.options.selectedIndex ==0 && document.searchForm.departmentId.options.selectedIndex ==0 && 
				document.searchForm.code.value == "" && document.searchForm.name.value == "" 
					&& document.searchForm.empType.options.selectedIndex != 0 )
	      {
			 alert("please select any one of the fields in (Department,Designation,Employee name,Employee code)");
			 return false;
		  }
		
		 
	</c:if>

	  	if(document.searchForm.designationId.options.selectedIndex ==0 && document.searchForm.departmentId.options.selectedIndex ==0 && 
	  		  	document.searchForm.code.value == "" && document.searchForm.name.value == "" && 
	  		  	document.searchForm.functionaryId.options.selectedIndex ==0  && document.searchForm.empType.options.selectedIndex ==0 
	  		  	 && document.searchForm.status.options.selectedIndex ==0)
	  	{
	  		alert("Please select any other parameter (Designation, Department, Functionary,Code, Name,Status or Type) for more specific search.");
	  		document.searchForm.status.value="0";
	  		return false;
	  	}
	  	
	  	return true;
  	}

	
 	function checkAlphaNumeric(obj)
 	{
  		if(obj.value!=""){
	  		var num=obj.value;
	  		var objRegExp  = /^[a-zA-Z0-9]+$/;
	  		if(!objRegExp.test(num)){
	  		alert('Please Enter the proper code');
	  		obj.value="";
	  		obj.focus();
	  		}
  		}
	}
	
	function readCheckBox(obj)
	{
		if(obj.checked)
		{
			dom.get("isUserActive").value=true;
			dom.get("isUserActive").checked=true;
        } 
		else if(!obj.checked)
		{
			dom.get("isUserActive").value=false;
			dom.get("isUserActive").checked=false;
        }     
	}
	
	var selectUserName;
	var yuiflag = new Array();
	var oAutoCompUserName;
	
	function loadEmpUserName() { 
			var type='getAllUserName';
			var url = "${pageContext.request.contextPath}/pims/employeeUserMapAjax.jsp?type="+type;
			var req = initiateRequest();
			req.open("GET", url, false);
			req.send(null);
			if (req.status == 200)
			{
				var codes=req.responseText;
				var a = codes.split("^");
				var codes = a[0];
				empCodeArray=codes.split("+");
				selectUserName = new YAHOO.widget.DS_JSArray(empCodeArray);
				
			}
		}


		//Used for Auto Complete
		function autocompleteUserName(obj,event){
		// set position of dropdown
		
		var src = obj;
		var target = document.getElementById('codescontainer');
		var posSrc=findPos(src);
		target.style.top=posSrc[1]+25;
		if(obj.name=='userName') target.style.left=posSrc[0]+0;
		target.style.width=200;

		var currRow=getRow(obj);
		var coaCodeObj = obj;
        if(yuiflag[currRow.rowIndex] == undefined)
		{	
		//40 --> Down arrow, 38 --> Up arrow
		if(event.keyCode != 40 )
		{

		if(event.keyCode != 38 )
		{
		oAutoCompUserName = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainer', selectUserName);		
		oAutoCompUserName.queryDelay = 0;
		oAutoCompUserName.useShadow = true;
		oAutoCompUserName.maxResultsDisplayed = 15;
		
		
		}

		}
		}
		}
	
		function fillUserNameAfterSplit(obj,neibrObjName)
		{
			var currRow=getRow(obj);	
			var yuiflag = new Array();
			yuiflag[currRow.rowIndex] = undefined;
			neibrObj=getControlInBranch(currRow,neibrObjName);  
			var temp = obj.value;
			temp = temp.split("-");
			
			if(temp[1]!='' && temp[1]!=undefined)
			{
				neibrObj.value = temp[1];
			}	
			obj.value=temp[0];	
		}
	
</script>
</head>
<body onload = "execute();loadEmpUserName();">
	<html:form  action="/pims/AfterSearchAction.do?submitType=executeSearch" >
		<div class="formmainbox">
			<div class="insidecontent">
			  	<div class="rbroundbox2">
					<div class="rbtop2">
						<div></div>
					</div>
				  	<div class="rbcontent2">

						<!-- Header Section Begins -->
						
						<!-- Header Section Ends -->
						
						<table width="95%" cellpadding ="0" cellspacing ="0" border = "0" id="table2">
							<tr>
								<td>
									<!-- Tab Navigation Begins -->
									
									<!-- Tab Navigation Ends -->
									
									<!-- Body Begins -->
									
									
									<!-- Body Begins -->
									<div id="codescontainer"></div>
									<table width="100%" cellpadding ="0" cellspacing ="0" border = "0"  id="searchTable">
										<tbody>
											<tr>
												<td>&nbsp;</td>
											</tr>
											<tr>
									  			<td colspan="8" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
												 <%
													String masterMode="Search";
													if(master!=null && mode!=null)
													{
														masterMode=master+mode;
													}
												  %>
												<div class="headplacer"><p><bean:message key="<%=masterMode%>"/></p></div></td>
									  		</tr>
									  		<tr>
									  			<td>&nbsp;</td>
									  		</tr>
											<html:hidden property="module" value="<%=module%>" />
											<html:hidden property="master" value="<%=master%>" />
											<html:hidden property="mode" value="<%=mode%>" />
											<tr>
												<td class="whiteboxwk" >Designation</td>
												<td class="whitebox2wk" >
													<select  name="designationId"   id="designationId" class="selectwk" >
										 				<option value='0' selected="selected"><bean:message key="chooseType"/></option>
										
														<%
														Map desMap = (Map)session.getAttribute("mapOfDesignation");
														for (Iterator it = desMap.entrySet().iterator(); it.hasNext(); )
														{
														Map.Entry entry = (Map.Entry) it.next();
														%>
														
														<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).intValue() == (request.getParameter("designationId")==null?0:Integer.parseInt(request.getParameter("designationId").trim())))? "selected":"")%>><%= entry.getValue() %></option>
														<%
														}
														%>
													</select>
												</td>
												
												<td  class="whiteboxwk" >Status</td>
												<td  class="whitebox2wk" >
													<html:select tabindex="1" styleId="status" property="status" styleClass="dropdownsize">
													<html:option value="0"><bean:message key="chooseType"/></html:option>
													<html:options collection="statusMasterList" property="id" labelProperty="description"/>
													</html:select>
												</td>
												
												<td  class="whiteboxwk" >Type</td>
												<td  class="whitebox2wk" width="305" colspan="3">
													<html:select tabindex="1" styleId="empType" property="empType" styleClass="dropdownsize">
													<html:option value="0"><bean:message key="chooseType"/></html:option>
													<html:options collection="employeeStatusMasterList" property="id" labelProperty="name"/>
													</html:select>
												</td>
											</tr>
											<tr>
											    <td  class="greyboxwk">Employee Code</td>
											    <td   class="greybox2wk" width="10%" >
													<input type="text" id="code" name="code"onBlur="checkAlphaNumericForCode(this);" class="selectwk" >
												</td>
												<td  class="greyboxwk">Employee Name</td>
												<td  class="greybox2wk" >
												  <input type="text" id="name" name="name" class="selectwk">
												</td>
												<c:if test="${(masters=='Employee')}">
													<td class="greyboxwk">Employee User Name</td>
													<td class="greybox2wk" width="10%">
														<input  type="hidden" name="userNameId" id="userNameId"  />
														<input name="userName" type="text" id="userName" class="selectwk" onkeyup="autocompleteUserName(this,event);" onblur="fillUserNameAfterSplit(this,'userNameId');"/>
													</td>
												</c:if>	
									  		</tr>
									  		<tr>
									            <td   class="whiteboxwk" align = "left">  
									            <c:if test="${not(mode=='View' && masters=='Employee')}">
									            	<egovtags:filterByDeptMandatory/>
									            	</c:if>Department</td>
												<td class="whitebox2wk" >
													<select  name="departmentId"   id="departmentId" class="selectwk" >
														 <option value='0' selected="selected" ><bean:message key="chooseType"/></option>
														 <c:choose>
														 <c:when test="${not(mode=='View' && masters=='Employee')}"><egovtags:filterByDeptSelect/>
														 </c:when>
														 <c:otherwise>
														 
														 <c:forEach var="department" items="${allDeptList}">
														 <option value="${department.id}">${department.deptName}</option>
														</c:forEach>
														
														 </c:otherwise>
														 </c:choose>
													</select>
												</td>
												
												<td   class="whiteboxwk" ><bean:message key="Functionary"/></td>
												<td class="whitebox2wk" >
													<select  name="functionaryId"   id="functionaryId" class="selectwk">
														<option value='0' selected="selected"><bean:message key="chooseType"/></option>
														<%
														Map functionaryMap =(Map)session.getAttribute("functionaryMap");
														for (Iterator it = functionaryMap.entrySet().iterator(); it.hasNext(); )
														{
														Map.Entry entry = (Map.Entry) it.next();
											
														%>
														<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).intValue() == (request.getParameter("functionaryId")==null?0:Integer.parseInt(request.getParameter("functionaryId").trim())))? "selected":"")%>><%= entry.getValue() %></option>
											
														<%
														}
														%>
													</select>
												</td>
												<c:if test="${(masters=='Employee')}">
													<td class="whiteboxwk">
														<input type="checkbox" name="userActiveCheckbox" id="isUserActive"  value="${userActiveCheckbox}" onclick="readCheckBox(this)"/>&nbsp;User Active
													</td>
													<td class="whiteboxwk"/>
												</c:if>		
											</tr>
										</tbody>
									</table>
									<br>
								
									<%
								  	 try
								  	 {
								   	 	List employeeList = (List)request.getAttribute("employeeList");
								   	 	CommonsService commonsManager =EisManagersUtill.getCommonsService();
								   	 	LinkedList links = new LinkedList();
										request.setAttribute("links",links);
								
									  	if(employeeList!= null && !employeeList.isEmpty())
									  	{
									  		Iterator iter = employeeList.iterator();
									  		String code = "";
									  		String name = "";
									  		String dept = "";
									  		String desig = "";
									  		String prdId = "";
									  		String ID = "";
									  		String status="";
									  		String userName = "";
									  		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
									  		Map masterOptionsMap = getMapIdVsOptions(employeeList);
											int i=0;
									  		ObjectType objectType = null;
									  		while (iter.hasNext())
											{
												Hashtable map=new Hashtable();
												EmployeeView cataEl = (EmployeeView)iter.next();
												Map masterMap = (Map)masterOptionsMap.get((Integer)cataEl.getId());
												String boolStr = (String)masterMap.get((String)master);
												boolean bAddtoList = false;
																
												if("Pension".equals(master)||"true".equals(boolStr) && (mode.equals("approve") || mode.equalsIgnoreCase("View") || mode.equals("Modify")))
												{
													if(master.equals("Disciplinary")||master.equals("LeaveApplication")||master.equals("Pension"))
													{
														objectType = commonsManager.getObjectTypeByType(EisConstants.OBJECTTYPE_EMPLOYEE);
														if(master.equals("Disciplinary"))
														{
															objectType = commonsManager.getObjectTypeByType(EisConstants.OBJECTTYPE_DISCIPLINARY);
														}
														else if(master.equals("LeaveApplication"))
														{
															objectType = commonsManager.getObjectTypeByType(EisConstants.OBJECTTYPE_LEAVE);
														}
								
														if(master.equals("Pension") && mode.equals("Create"))
														{
															bAddtoList = true;
														}
								
														boolean b = checkSupEmployee((Integer)cataEl.getId(),objectType);
														if(b==true && mode.equals("approve"))
														{
															bAddtoList = true;
														}
														else if(mode.equalsIgnoreCase("View") || mode.equals("Modify") )
														{
															bAddtoList = true;
														}
													}
													else
													{
														bAddtoList = true;
													}
												}
												else if(mode.equals("Create") || mode.equals("Reports"))
												{
													if(mode.equals("Reports") && master.equals("Leave"))
													{
														if(EisManagersUtill.getEmpLeaveService().checkLeaveReportsForAnEmployee((Integer)cataEl.getId())==true)
														{
															bAddtoList = true;
															
															//If employee is retired , no need to show in leave report for that employee ### added by jagadeesan
															if(cataEl.getEmployeeStatus()!=null && cataEl.getEmployeeStatus().getDescription().toString().equals(EisConstants.STATUS_TYPE_RETIRED))
															{
																bAddtoList = false;
															}
														}
													}
													else
													{
														bAddtoList = true;
													}
												}

												if ((master.equals("Disciplinary")||master.equals("LeaveApplication"))
															&& cataEl.getIsActive().intValue() == 0) {
													bAddtoList = false;
												}
												//If retried employee , no need to show in leave application for that employee ### added by jagadeesan
												if(master.equals("LeaveApplication") && cataEl.getEmployeeStatus()!=null && cataEl.getEmployeeStatus().getDescription().toString().equals(EisConstants.STATUS_TYPE_RETIRED))
												{
													bAddtoList = false;
												}
												
												if (bAddtoList) {
													prdId = cataEl.getAssignmentPrd().getId().toString();
													name = cataEl.getEmployeeName();
													code = cataEl.getEmployeeCode();
													dept = cataEl.getDeptId().getDeptName();
													desig = cataEl.getDesigId().getDesignationName();
													if(cataEl.getUserMaster()!=null)
													{
														userName = cataEl.getUserMaster().getUserName();
													}	
													else
													{
														userName= "";
													}	
													ID = cataEl.getId().toString();
													
													if(cataEl.getEmployeeStatus()==null){
														status="";
													}
													else{
														status = cataEl.getEmployeeStatus().getDescription().toString();
													}

													map.put("name",name);
													map.put("code",code);
													map.put("dept",dept);
													map.put("prdId",prdId);
													map.put("desig",desig);
													map.put("Id",ID);
													map.put("status",status);
													map.put("userName",userName);
													links.add(map);
												}
								
											}
										}
									}
									catch(Exception e)
									{
									LOGGER.error(e.getMessage());
									}
								 	%>
									
									<c:if test="${employeeList != null}">
										<table align="center" width="100%" border="0" cellpadding="0" cellspacing="0">
											<tr>
											  <td class="headingwk" colspan="5">
											  <div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
											  <div align="left" style="margin-top:4px;">Employee Details</div>
											
											  </td>
											</tr>
								
											<tr>
												<td>
											 		<display:table name="links" id="eid" cellspacing="0" style="width: 100%;" requestURI="${pageContext.request.contextPath}/pims/AfterSearchAction.do"
											  			export="false" defaultsort="2" pagesize = "15" sort="list"  class="its" >
														<display:setProperty name="paging.banner.placement" value="bottom"/>
												
														 <display:column style="tablesubheadwk:10%"   property="code" title="Employee Code" />
														 <display:column style="tablesubheadwk:5%"   property="name" title="Employee Name" />
														 <display:column style="tablesubheadwk:5%"   property="desig" title="Employee Designation" />
														 <display:column style="tablesubheadwk:5%"   property="dept" title="Employee Department" />
														 <display:column style="tablesubheadwk:5%"   property="userName" title="Employee User Name" />
														 <%
														 if(!mode.equals("Reports"))
														 {
														 %>
														 	<display:column style="width:5%" title="Masters" >
													
														 	   <%
														 	    if(mode.equalsIgnoreCase("Modify") && master.equalsIgnoreCase("LeaveApplication"))
														 		{
														 		%>
														 			<a  href="#" onclick="SetActionValue('<%= ((Map)pageContext.getAttribute("eid")).get("Id")%>','<%=master%>','<%=mode%>','<%= ((Map)pageContext.getAttribute("eid")).get("prdId")%>');">
														 	 		cancel
														 	 		</a>
																<%
																}
																else
																{
																	if( mode.equalsIgnoreCase("Modify") && master.equalsIgnoreCase("Employee") && ((Map)pageContext.getAttribute("eid")).get("status").toString().equals(EisConstants.STATUS_TYPE_RETIRED) )
																	{
																	%>
																 	 	<a  href="#" onclick="SetActionValue('<%= ((Map)pageContext.getAttribute("eid")).get("Id")%>','<%=master%>','View','<%= ((Map)pageContext.getAttribute("eid")).get("prdId")%>');">
																 	 	View
																 	 	</a>
															 	 	<%
															 	 	}
															 	 	else
															 	 	{
															 	 	 %>
																 	 	<a  href="#" onclick="SetActionValue('<%= ((Map)pageContext.getAttribute("eid")).get("Id")%>','<%=master%>','<%=mode%>','<%= ((Map)pageContext.getAttribute("eid")).get("prdId")%>');">
																 	 	<%=mode%>
																 	 	</a>
																	<%
																	}
																}
																%>
															</display:column>
														 <%
														 }
														 %>
													
														<%
														if(mode.equals("Reports")&& master.equals("Employee") || mode.equals("Reports")&& master.equals("EmployeeLight"))
														{
														%>
															<display:column style="width:5%" title="Reports" >
																<%
																String id = (String)((Map)pageContext.getAttribute("eid")).get("Id");
																%>
															 	<a  href="${pageContext.request.contextPath}/pims/AfterSearchAction.do?submitType=executeReportSearch&Id=<%=id%>">
															 		<FONT class="labelcell">Employee History</FONT>
															 	</a>
															</display:column>
														<%
														}
														%>
													
														<%
														if(mode.equalsIgnoreCase("Reports") && master.equalsIgnoreCase("Leave"))
														{
														%>
															<display:column style="width:5%" title="Reports" >
																<%
																String id = (String)((Map)pageContext.getAttribute("eid")).get("Id");
																%>
																<a  href="${pageContext.request.contextPath}/leave/BeforeLeaveAction.do?<%=essStr%>submitType=beforeCreate&master=LeaveTrax&Id=<%=id%>" >
																	<FONT class="labelcell">Leave Tranx</FONT>
																</a>
																<p/>
																<a  href="${pageContext.request.contextPath}/leave/BeforeLeaveAction.do?<%=essStr%>submitType=beforeCreate&master=LeaveCard&Id=<%=id%>" >
																	<FONT class="labelcell">Leave Card</FONT>
																</a>
															</display:column>
														<%
														}
														%>
													</display:table>
												</td>
											</tr>
										</table>
									</c:if>
								</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
							</tr>
							<tr>
							   <%if(mast.equals("Employee")){%>
									  			<td><div align="right" class="mandatory">Note: At least Department or Designation or Employee Name or Employee Code should be entered</div></td>
									  		<%
									  			}
									  			else{
									  		%>
									  			<td><div align="right" class="mandatory">* Mandatory Fields</div></td>
									  		<%
									  			}
									  		 %>	
							</tr>
						</table>
					</div>
					<div class="rbbot2">
						<div></div>
					</div>
				</div>
			</div>
		</div>

		<div>
			<html:submit styleClass="buttonfinal" value="Submit" property="b4" onclick="return showMsg();" />
		  	<input type="button" name="button" id="button" value="CLOSE"  class="buttonfinal" onclick="window.close();"/>
		</div>

	</html:form>
</body>
</html>