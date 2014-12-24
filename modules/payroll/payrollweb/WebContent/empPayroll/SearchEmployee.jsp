
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
		 org.egov.lib.rjbac.user.ejb.api.UserService,
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
		 org.egov.pims.client.*,
		 org.egov.payroll.utils.PayrollExternalInterface,
		 org.egov.payroll.utils.PayrollExternalImpl"


%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><bean:message key="payroll.title"/></title>
   <style type="text/css">
.yui-ac-input {
height:17px;
position:absolute;
width:50px;
}

</style>


</head>


<%!
String mast ="";
Integer userId =new Integer(0);

%>
<%
userId = (Integer) session.getAttribute("com.egov.user.LoginUserId");
String module = (String)request.getAttribute("module");
//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>hereeee"+module);
	java.util.Date date = new java.util.Date();
SimpleDateFormat sdff = new SimpleDateFormat("dd/MM/yyyy");
%>

<%!
	public boolean checkSupEmployee(Integer emp,ObjectType objectType)
	{
		boolean b = false;
		try
		{
			PayrollExternalInterface payrollExternalInterface=new PayrollExternalImpl();
			boolean check = payrollExternalInterface.checkLeave(emp);
			if(check==true)
			{

				PersonalInformation supiriorEmp =null;
				//System.out.println("emp= "+emp);
				//System.out.println("userId= "+userId);
				if(userId!=null)
					supiriorEmp = payrollExternalInterface.getEmpForUserId(userId);
				//System.out.println("supiriorEmpID="+supiriorEmp.getIdPersonalInformation());
				Position pos =payrollExternalInterface.getPositionforEmp(emp);
				//System.out.println("posId="+pos.getId());
				User user =payrollExternalInterface.getSupUserforPositionandObjectType(pos,objectType);
				//System.out.println("user11111="+user);
				PersonalInformation personalInformation = null;
				if(user != null)
				{
					//System.out.println("user.getId()="+user.getId());
					personalInformation = payrollExternalInterface.getEmpForUserId(user.getId());
				}

				Integer supId = new Integer(0);

				if(personalInformation!=null)
				{
					supId = personalInformation.getIdPersonalInformation();
					//System.out.println("yes4");
					//System.out.println("yes4"+supId);
				}

				//System.out.println("yes6"+supId.equals(supiriorEmp.getIdPersonalInformation()));
				if(supiriorEmp!=null)
				{
					  if(supId.equals(supiriorEmp.getIdPersonalInformation()))
					 {
						b=true;
						//System.out.println("yes1");
					 }
					 else
					 {
						b=false;
						//System.out.println("yes2");
					 }
				 }
				 else
				 {
					b=false;
					//System.out.println("yes3");
				 }
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
		//System.out.println("return b :"+b);
	 	return b;
	}
%>
<%

    	String mode = (String)request.getAttribute("mode");
        
    	String master = (String)request.getAttribute("masters");
    	mast = master;
//System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<"+mode+">>>>>>>>>>>>>>>>>>>"+master);
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
		PayrollExternalInterface payrollExternalInterface=new PayrollExternalImpl();
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
			persInfoList = payrollExternalInterface.getListOfPersonalInformationByEmpIdsList(empIdsList);
		}
		if(persInfoList != null && !persInfoList.isEmpty())
		{
			for(Iterator perInfoIter = persInfoList.iterator(); perInfoIter.hasNext();)
			{
				Map optionsMap = new HashMap();
				List compOffsList = null;
				egpimsPersonalInformation = (PersonalInformation)perInfoIter.next();
				if(mast.equals("empPayroll"))
				{
				    //System.out.println("in hereee nhcdcccccccccccccccccccc"+mast);
					optionsMap.put("empPayroll","true");
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
  else
  {
  mode="modify";
  }
  
	   <%
	   
		List listOfEmp = (List)request.getAttribute("employeeList");
		Map optionsMap = getMapIdVsOptions(listOfEmp);

    		Set empIdSet = optionsMap.keySet();
		for(Iterator iter = empIdSet.iterator();iter.hasNext();)
		{
				Integer empId = (Integer)iter.next();
				//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>empId"+empId);
	 	%>	
			if(value=='<%= empId %>')
			{
	          <%
				Map booleanMap = (Map)optionsMap.get(empId);
				if("empPayroll".equals(master))
				{

					String val = (String)booleanMap.get(master);
                     //System.out.println("in hereee nhcdccccccccccccccccccccvalllllllllllllllllllllll"+val);
					if(val.equals("true"))
					{
						if("empPayroll".equals(master))
						{
				%>
                      window.location ="${pageContext.request.contextPath}/empPayroll/payrollBeforeCreate.do?submitType=beforeCreate&Id="+value+"&mode="+mode;

						<%
						}
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
	
		if(!checkDeptMandatory(document.searchForm.departmentId)){
			return false;
		}
	
	  	if(document.searchForm.designationId.options.selectedIndex ==0 && document.searchForm.departmentId.options.selectedIndex ==0 && document.searchForm.code.value == "" && document.searchForm.name.value == "" && document.searchForm.functionaryId.options.selectedIndex ==0)
	  	{
	  		alert('<bean:message key="alertselectvalues"/>');
	  		document.searchForm.status.value="0";
	  		return false;
	  	}
  	
  		return true;
 	}
 function checkAlphaNumeric(obj){
  		if(obj.value!=""){
  		var num=obj.value;
  		var objRegExp  = /^[a-zA-Z0-9_-]+$/;
  		if(!objRegExp.test(num)){
  		alert('<bean:message key="alertcode"/>');
  		obj.value="";
  		obj.focus();
  		}
  		}
		}
var codeSelectionHandler = function(sType, arguments)
    { 
        var oData = arguments[2];
	 	var empDetails = oData[0];
	 	var empCode = empDetails.split(EMPCODE_SEP)[0];
	 	var empName = empDetails.split(EMPCODE_SEP)[1];
	 	dom.get("code").value = empCode;
	 	dom.get("name").value = empName;	 	
	 	name=empName;
	 	code=empCode;
 	}
    var codeSelectionEnforceHandler = function(sType, arguments) {
      		warn('impropercodeSelection');
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
			 <%
			String masterMode="Search";
			if(master!=null && mode!=null)
			{
			masterMode=master+mode;
			}
			%>
                  <div class="headplacer"><bean:message key="payroll.heading"/></div></td>
              </tr>   
			
			<html:hidden property="module" value="<%=module%>" />
			<html:hidden property="master" value="<%=master%>" />
			<html:hidden property="mode" value="<%=mode%>" />
			<tr>
			<td   class="whiteboxwk" ><bean:message key="designation"/></td>
			<td class="whitebox2wk" >
				<select  name="designationId"   id="designationId"  onMouseOver="addTitleAttributes(this)" class="selectwk">
				<option value='0' >---choose---</option>
	
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
			<td   class="whiteboxwk" ><egovtags:filterByDeptMandatory/> <bean:message key="department"/></td>
			<td class="whitebox2wk" >
			<select  name="departmentId"   id="departmentId"  onMouseOver="addTitleAttributes(this)" class="selectwk">
				<option value='0' >---choose---</option>
				<egovtags:filterByDeptSelect/>
			</select>
			</td>
			</tr>
			<tr>
			<td class="greyboxwk"><bean:message key="empcode"/></td>
			<td  class="greybox2wk" width="20%" valign="top" >  	
  			<div class="yui-skin-sam">
	    	<div id="empSearch_autocomplete" class="yui-ac" >
	    	    <input type="text" id="code" name="code" onBlur="checkAlphaNumeric(this);" size="10"   class="selectwk"/> 	    
	   	   <div id="codeSearchResults"></div> 
	    	</div>
		</div>
			<egovtags:autocomplete name="code"  field="code" 
		   	    	url="${pageContext.request.contextPath}/common/employeeSearch!getEmpListByEmpCodeLike.action" queryQuestionMark="true"  results="codeSearchResults" 
		   	    	handler="codeSelectionHandler" forceSelectionHandler="codeSelectionEnforceHandler"/>
		   	    <span class='warning' id="impropercodeSelectionWarning"></span>
			</td>
			<td  class="greyboxwk" ><bean:message key="empname"/></td>
			<td  class="greybox2wk">
			<input type="text" id="name" name="name" size="10"  class="selectwk" >
			</td>
			</tr>
			<tr>
			<td  class="whiteboxwk"  align = "left"><bean:message key="status"/></td>
			<td  class="whitebox2wk" >
			<html:select tabindex="1" styleId="status" property="status" styleClass="dropdownsize">
			<html:option value="0"><bean:message key="select"/></html:option>
			<html:options collection="statusMasterList" property="id" labelProperty="description"/>
			</html:select>
			</td>



			<td   class="whiteboxwk " ><bean:message key="Functionary"/></td>
			<td class="whitebox2wk " >
			<select  name="functionaryId"   id="functionaryId" onMouseOver="addTitleAttributes(this)" class="selectwk">
			<option value='0'><bean:message key="choose"/></option>
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
			
			<td  class="greyboxwk"><bean:message key="type"/></td>
			<td  colspan="7" class="greybox2wk">
			<html:select tabindex="1" styleId="empType" property="empType" styleClass="selectwk">
			<html:option value="0"><bean:message key="choose"/></html:option>
			<html:options collection="employeeStatusMasterList" property="id" labelProperty="name"/>
			</html:select>
            </td>
			
			</tr>
			
			<tr>
		<td colspan="4" class="shadowwk"></td>
		</tr>
		<tr>
		<td colspan="4"><div align="right" class="mandatory"><bean:message key="mandatory"/></div></td>
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
			if (module.equals("Payslip"))
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
			<display:column style="width:5%"   property="code" titleKey="Employee Code" />
			<display:column style="width:5%"   property="name" title="Employee Name" />
			<display:column style="width:5%"   property="desig" title="Employee Designation" />
			<display:column style="width:5%"   property="dept" titleKey="Employee Department" />
			<% if("ViewModify".equalsIgnoreCase(mode) && "empPayroll".equalsIgnoreCase(master))
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
			<%}else{%>
			<a  href="#" onclick="SetActionValue(<%= ((Map)pageContext.getAttribute("eid")).get("Id")%>,
			<%=master%>,<%=mode%>,<%= ((Map)pageContext.getAttribute("eid")).get("prdId")%>)";>
			cancel
			</a>
			
			
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
		
		</html:form>
		</body>
		</html>
