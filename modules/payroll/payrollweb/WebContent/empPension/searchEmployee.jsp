
<%@ include file="/includes/taglibs.jsp" %>

<%@ page import="java.util.*,
		 org.egov.infstr.utils.*,
		
		 org.egov.budget.services.*,
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
		 org.egov.EGOVRuntimeException,
		 org.egov.lib.address.dao.AddressDAO,
		 java.util.StringTokenizer,
		 org.egov.infstr.utils.ServiceLocator,
		 org.egov.lib.rjbac.user.User,
		 org.egov.lib.rjbac.user.ejb.api.IUserManager,
		 org.egov.lib.rjbac.user.ejb.api.UserManager,
		 org.egov.lib.rjbac.user.ejb.api.UserManagerHome,
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
		 java.text.SimpleDateFormat,
		 org.egov.pims.client.*"


%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Payroll Search Screen</title>


</head>
<%!
String mast ="";
Integer userId =new Integer(0);

%>
<%
userId = (Integer) session.getAttribute("com.egov.user.LoginUserId");
String module = (String)request.getAttribute("module");
System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>hereeee"+module);
	java.util.Date date = new java.util.Date();
SimpleDateFormat sdff = new SimpleDateFormat("dd/MM/yyyy");
%>

<%!
	public boolean checkSupEmployee(Integer emp,ObjectType objectType)
	{
		boolean b = false;
		try
		{
			boolean check = EisManagersUtill.getEmpLeaveManager().checkLeave(emp);
			if(check==true)
			{

				PersonalInformation supiriorEmp =null;
				//System.out.println("emp= "+emp);
				//System.out.println("userId= "+userId);
				if(userId!=null)
					supiriorEmp = EisManagersUtill.getEisManager().getEmpForUserId(userId);
				//System.out.println("supiriorEmpID="+supiriorEmp.getIdPersonalInformation());
				Position pos =EisManagersUtill.getEisManager().getPositionforEmp(emp);
				//System.out.println("posId="+pos.getId());
				User user = EisManagersUtill.getEisCommonsManager().getSupUserforPositionandObjectType(pos,objectType);
				//System.out.println("user11111="+user);
				PersonalInformation personalInformation = null;
				if(user != null)
				{
					//System.out.println("user.getId()="+user.getId());
					personalInformation = EisManagersUtill.getEisManager().getEmpForUserId(user.getId());
				}

				Integer supId = new Integer(0);

				if(personalInformation!=null)
				{
					supId = personalInformation.getIdPersonalInformation();
					System.out.println("yes4");
					System.out.println("yes4"+supId);
				}

				System.out.println("yes6"+supId.equals(supiriorEmp.getIdPersonalInformation()));
				if(supiriorEmp!=null)
				{
					  if(supId.equals(supiriorEmp.getIdPersonalInformation()))
					 {
						b=true;
						System.out.println("yes1");
					 }
					 else
					 {
						b=false;
						System.out.println("yes2");
					 }
				 }
				 else
				 {
					b=false;
					System.out.println("yes3");
				 }
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
		System.out.println("return b :"+b);
	 	return b;
	}
%>
<%

    	String mode = (String)request.getAttribute("mode");
        
		String master = (String)request.getAttribute("masters");
    	mast = master;
System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<"+mode+">>>>>>>>>>>>>>>>>>>"+master+"MOdule-----"+module);
    	


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
			persInfoList = EisManagersUtill.getEisManager().getListOfPersonalInformationByEmpIdsList(empIdsList);
		}
		if(persInfoList != null && !persInfoList.isEmpty())
		{
			for(Iterator perInfoIter = persInfoList.iterator(); perInfoIter.hasNext();)
			{
				Map optionsMap = new HashMap();
				List compOffsList = null;
				egpimsPersonalInformation = (PersonalInformation)perInfoIter.next();
				if(mast.equals("Pension"))
				{
				    
					optionsMap.put("Pension","true");
	       	}
				IdVsoptionsMap.put(egpimsPersonalInformation.getIdPersonalInformation(),optionsMap);

			}
		}
		}catch(Exception e)
		{
			e.printStackTrace();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
	return IdVsoptionsMap;

}
%>
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
function SetActionValue(value,master,mode,prdId,type)
  {

   
  if(type==1)
  {
    mode="view";
  }
  else if(type==2)
  {
     mode="modify";
  }
  
	<%
        
		List listOfEmp = (List)request.getAttribute("employeeList");
		System.out.println(listOfEmp==null?"":listOfEmp.size());
		Map optionsMap = getMapIdVsOptions(listOfEmp);

    		Set empIdSet = optionsMap.keySet();
		for(Iterator iter1 = empIdSet.iterator();iter1.hasNext();)
		{
				Integer empId = (Integer)iter1.next();
				
		%>

           
			if(value=='<%= empId %>')
			{
				
				
				
				
			if(mode=='view')
			{
			 window.location ="${pageContext.request.contextPath}/empPension/AfterPensionSearchAction.do?submitType=beforeViewDetails&Id="+value+"&master="+master;
			 }
		         else if(mode=='modify')
		         {
		          window.location ="${pageContext.request.contextPath}/empPension/AfterPensionSearchAction.do?submitType=beforeModifyDetails&Id="+value+"&master="+master;
		          }
		
				 else
				 {
				     window.location ="${pageContext.request.contextPath}/empPension/AfterPensionSearchAction.do?submitType=populateDetails&Id="+value+"&master="+master;
				     }
				  
			}

	<%
		
	 }
	%>
 


  }
  
                    
	  function showMsg()
	  {

	  	if(!checkDeptMandatory(document.searchForm.departmentId)){
			return false;
		}
		
	  	if(document.searchForm.designationId.options.selectedIndex ==0 && document.searchForm.departmentId.options.selectedIndex ==0 && document.searchForm.code.value == "" && document.searchForm.name.value == "" && document.searchForm.functionaryId.options.selectedIndex ==0)
	  	{
	  		alert("Please select any other parameter (Designation, Department, Functionary,Code, Name) for more specific search.");
	  		document.searchForm.status.value="0";
	  		return false;
	  	}	
	  	return true;
	}
 function checkAlphaNumeric(obj){
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

</script>
<body onload = "execute();">

<!-- Header Section Begins -->

<!-- Header Section Ends -->


<!-- Tab Navigation Begins -->

<!-- Tab Navigation Ends -->

<!-- Body Begins -->


<!-- Body Begins -->


		<html:form  action="/empPayroll/executeSearch.do?submitType=executeSearch" >
		<div class="navibarshadowwk"></div>
		<div class="formmainbox"><div class="insidecontent">
		<div class="rbroundbox2">
		<div class="rbtop2"><div></div></div>
		<div class="rbcontent2">
		<div class="datewk"></div>
		<span class="bold">Today :  </span>"<%=sdff.format(date)%>"

		<table width="95%" border="0" cellspacing="0" cellpadding="0">
		<tr>
		<td></td>
		</tr>
		<tr>
	<table  width="95%" cellpadding ="0" cellspacing ="0" border = "0"  >
	<tbody>
	<tr>
	 <tr>
             <td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/common/image/arrow.gif" /></div>
			
                  <div class="headplacer">Pension Search</div></td>
              </tr>   
			

			
			<html:hidden property="module" value="<%=module%>" />
			<html:hidden property="master" value="<%=master%>" />
			<html:hidden property="mode" value="<%=mode%>" />
			<tr>
			<td   class="whiteboxwk" >Designation</td>
			<td class="whitebox2wk" >
			<select  name="designationId"   id="designationId"  onMouseOver="addTitleAttributes(this)" class="selectwk">
			<option value='0'>---Choose---</option>

			<%
			Map desMap = (Map)session.getAttribute("mapOfDesignation");
			if(desMap!=null)			for (Iterator it = desMap.entrySet().iterator(); it.hasNext(); )
			{
			Map.Entry entry = (Map.Entry) it.next();
			%>
			<option value='<%= entry.getKey().toString() %>'><%= entry.getValue()  %></option>

			<%
			}

			%>
			</select>
			</td>

			<td   class="whiteboxwk" ><egovtags:filterByDeptMandatory/> Department</td>
			<td class="whitebox2wk" >
			<select  name="departmentId"   id="departmentId"  onMouseOver="addTitleAttributes(this)" class="selectwk">
				<option value='0' >---choose---</option>
				<egovtags:filterByDeptSelect/>
			</select>
			</td>
			</tr>
			<tr>
			<td  class="greyboxwk"  >Employee Code</td>
			<td   class="greybox2wk"  >
			<input type="text"id="code" name="code" onBlur="checkAlphaNumeric(this);"size="10"   class="selectwk">
			</td>
			<td  class="greyboxwk" >Employee Name</td>
			<td  class="greybox2wk">
			<input type="text"id="name" name="name" size="10"  class="selectwk" >
			</td>
			</tr>
			<tr>
			<td  class="whiteboxwk"  align = "left">Status<td  class="whitebox2wk" >
			<html:select tabindex="1" styleId="status" property="status" styleClass="dropdownsize">
			<html:option value="0">--Select--</html:option>
			<html:options collection="statusMasterList" property="id" labelProperty="description"/>
			</html:select>
			</td>
			</td>


			<td   class="whiteboxwk " ><bean:message key="Functionary"/></td>
			<td class="whitebox2wk " >
			<select  name="functionaryId"   id="functionaryId" onMouseOver="addTitleAttributes(this)" class="selectwk">
			<option value='0' >---Choose---</option>
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

			</tr>
			
			<tr>
			
			<td  class="greyboxwk">Type</td>
			<td  colspan="7" class="greybox2wk">
			<html:select tabindex="1" styleId="empType" property="empType" styleClass="selectwk">
			<html:option value="0">---Choose---</html:option>
			<html:options collection="employeeStatusMasterList" property="id" labelProperty="name"/>
			</html:select>
            </td>
			
			</tr>
			
			<tr>
		<td colspan="4" class="shadowwk"></td>
		</tr>
		<tr>
		<td colspan="4"><div align="right" class="mandatory">* Mandatory Fields</div></td>
		</tr>
		
		

			<table id = "submit" width="95%"  cellpadding ="0" cellspacing ="0" border = "0" >
			<tr align = "center">
			<td colspan="4" align="center"><div class="buttonholderwk">
			<html:submit styleClass="buttonfinal" value="Submit" property="b4" onclick="return showMsg();" />
			</div>
			</td>
			<tr>
			</table>
			<%

			try{
			List employeeList = (List)request.getAttribute("employeeList");

			CommonsManager commonsManager =EisManagersUtill.getCommonsManager();
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
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Map masterOptionsMap = getMapIdVsOptions(employeeList);

			ObjectType objectType = null;
			while (iter.hasNext())
			{


			Hashtable map=new Hashtable();
			EmployeeView cataEl = (EmployeeView)iter.next();
			Map masterMap = (Map)masterOptionsMap.get((Integer)cataEl.getId());
			String boolStr = (String)masterMap.get((String)master);
			boolean bAddtoList = false;
			if (module.equals("Pension"))
			{
			bAddtoList = true;
			}
			if (bAddtoList) {
			prdId = cataEl.getAssignmentPrd().getId().toString();
			name = cataEl.getEmployeeName();
			code = cataEl.getEmployeeCode();
			dept = cataEl.getDeptId().getDeptName();
			desig = cataEl.getDesigId().getDesignationName();
			ID = cataEl.getId().toString();
			map.put("name",name);
			map.put("code",code);
			map.put("dept",dept);
			map.put("prdId",prdId);
			map.put("desig",desig);
			map.put("Id",ID);
			links.add(map);
			}

			}
			}


			}
			catch(Exception e){}
			%>
            <table width="95%" border="0" >
			<tr>
			<td align="center">
			<c:if test="${employeeList != null}">
            
			<display:table name="links" id="eid" cellspacing="0" style="Width:95%" requestURI="${pageContext.request.contextPath}/empPayroll/executeSearch.do"
			export="false" defaultsort="2" pagesize = "15" sort="list"  class="its"  >
			<display:column style="width:5%"   property="code" title="Employee Code" />
			<display:column style="width:5%"   property="name" title="Employee Name" />
			<display:column style="width:5%"   property="desig" title="Employee Designation" />
			<display:column style="width:5%"   property="dept" title="Employee Department" />
			<% if("ViewModify".equalsIgnoreCase(mode) && "Pension".equalsIgnoreCase(master))
			{
			%>
			<display:column style="width:5%" title="Masters" >
			
			<a  href="#" onclick="SetActionValue('<%= ((Map)pageContext.getAttribute("eid")).get("Id")%>',
			'<%=master%>','<%=mode%>','<%= ((Map)pageContext.getAttribute("eid")).get("prdId")%>','1')";>
			View
			</a>
            </display:column >
            <egov:authorize actionName="payrollButton">
            <display:column style="width:5%" title="Masters">
			<a  href="#"   onclick="SetActionValue('<%= ((Map)pageContext.getAttribute("eid")).get("Id")%>',
			'<%=master%>','<%=mode%>','<%= ((Map)pageContext.getAttribute("eid")).get("prdId")%>','2')";>
			Modify
			</a>
            </display:column >
            </egov:authorize>
			<%}else{
			%>
			<egov:authorize actionName="payrollButton">
            <display:column style="width:5%" title="Masters">
			<a  href="#"   onclick="SetActionValue('<%= ((Map)pageContext.getAttribute("eid")).get("Id")%>',
			'<%=master%>','<%=mode%>','<%= ((Map)pageContext.getAttribute("eid")).get("prdId")%>')";>
			Create
			</a>
            </display:column >
            </egov:authorize>
			
			<%
			}
			%>


		</display:table>
		
		</c:if>
		</td>
		</tr>
		</table>
	

		</tbody>
		</table>
		</td>
		</tr>
		</table>
		</div>
		<div class="rbbot2"><div></div></div>
		</div>
		</div>
		</div>
		<div class="buttonholderwk"><input type="submit" name="button" id="button" value="CLOSE"  class="buttonfinal" onclick="window.close()"/>
		</div>
		<div class="urlwk">City Administration System Designed and Implemented by 
		<a href="http://www.egovernments.org/">eGovernments Foundation</a> All Rights Reserved
		</div>
		</html:form>
		</body>
		</html>
