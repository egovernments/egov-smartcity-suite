
	<%@ include file="/includes/taglibs.jsp" %>
			<%@ page import="java.util.*,
			org.egov.infstr.utils.*,
			org.apache.log4j.Logger,
			org.egov.commons.CFinancialYear,
			org.egov.pims.empLeave.dao.*,
			org.egov.pims.empLeave.client.*,
			org.egov.pims.empLeave.model.*,
			org.egov.pims.empLeave.service.*,
			org.egov.pims.dao.*,
			org.egov.pims.model.*,
			org.egov.pims.service.*,
			org.egov.pims.utils.*,
			org.egov.infstr.utils.*,
			java.text.SimpleDateFormat,
			org.egov.pims.client.*"


			%>

	<html>
			<head>
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
			<title>Employee Attendance</title>

			<SCRIPT type="text/javascript" src="<%=request.getContextPath()%>/javascript/PinSysvalidation.js" type="text/javascript"></SCRIPT>
			<SCRIPT type="text/javascript" src="<%=request.getContextPath()%>/javascript/dateValidation.js" type="text/javascript"></SCRIPT>
			<script language="text/JavaScript" src="<%=request.getContextPath()%>/Admin-Homepage_files/dhtml.js" type="text/JavaScript"></script>
			
			
			<%!
			EmployeeServiceImpl eisServiceImpl=new EmployeeServiceImpl();
			%>
			<%
			String module = (String)request.getAttribute("module");
			%>

			<script>
			function execute()
			{
			
			<%if(request.getParameter("departmentId")!=null && !request.getParameter("departmentId").equals("")){%>
	     document.searchForm.departmentId.value = <%=request.getParameter("departmentId")%>;
	     <%}else{%>
	     document.searchForm.departmentId.value=0;
	     <%}%>
	    
	     var target="<%=(request.getAttribute("alertMessage"))%>";
			if(target!="null")
			{
			alert("<%=request.getAttribute("alertMessage")%>");
			
			<%	request.setAttribute("alertMessage",null);	if(request.getAttribute("alertMessage")==null){%>
				document.getElementById("functionaryId").value=0;
				document.getElementById("departmentId").value=0;
				document.getElementById("code").value="";
				document.getElementById("name").value="";
				<%}%>
			}

			}

			function validateSearch()
			{
			
			var bool ;
			var url = "<%=request.getContextPath()%>/leave/validateSearchAjax.jsp?month="+document.getElementById("monthId").value+"&fYear="+document.getElementById("finYear").value;
			var http = initiateRequest();
			http.open("GET", url, false);
			http.send(null);
			if (http.status == 200)
			{
			var statusString =http.responseText.split("^");
			if(statusString[0]=="false")
			{
			bool = "false";
			}
			else
			{
			bool = "true";

			}
			}
			
			
			
			
			return bool;


			}

			function populateDesgBasedOnFunctionDept()
			{
			    var deptId=document.getElementById("departmentId").value;
				var functionaryId=document.getElementById("functionaryId").value;
				loadDesignationBasedOnFunDept('functionaryId','departmentId','designationId');
				
				
			}

			function loadDesignationBasedOnFunDept(functionaryObj,deptObj,destobj)
			{
				
				var functionaryId = document.getElementById(functionaryObj).value;
				var deptId = document.getElementById(deptObj).value;
				var type='loadDesgBasedOnDeptFunctionary';
				var url = "${pageContext.request.contextPath}/leave/populateDesgBasedOnFuncAndDeptAjax.jsp?deptId="+deptId+"&type="+type+"&functionaryId="+functionaryId;
				var request = initiateRequest();
				request.open("GET", url, false);
				request.send(null);		
				if (request.status == 200) 
				{
					
				var response=request.responseText.split("^");
				var id = response[0].split("+");
				var name = response[1].split("+");
				var comboObj = document.getElementById(destobj);
				comboObj.options.length = 0;
				comboObj.options[0] = new Option("--Choose--","");
				for(var i = 1 ; i <= id.length  ; i++)
				{
					comboObj.options[i] = new Option(name[i-1],id[i-1]);	
				
				}			
				}		
				else
				{
				var comboObj = document.getElementById(destobj);
				comboObj.options.length = 0;	
				}
			}

			function submitSearch()
			{

				if(!checkDeptMandatory(document.searchForm.departmentId.options.selectedIndex)){
					return false;
					}
					

					if(document.getElementById("fromName").value=="0")
					{
						if(document.getElementById("toName").value!="0")
						{
							alert('Please choose the From value');
							document.getElementById("fromName").focus();
							return false;
						}

					}
					else
					{
						if(document.getElementById("toName").value=="0")
						{
							alert('Please choose the To value');
							document.getElementById("toName").focus();
							return false;
						}
					}


			var boole = validateSearch();
			if(boole=="true")
			{

			document.getElementById("fromName").disabled=false;
			document.getElementById("toName").disabled=false;
			document.searchForm.action = "${pageContext.request.contextPath}/pims/AfterSearchAction.do?submitType=executeSearchForAttendence";
			document.searchForm.submit();
			return true;
			}
			else if(boole=="false")
			{
			alert('<bean:message key="alertCannotAlter"/>');
			return false;
			}

			}
			
			function disableSearch(obj)
			{
				
				if((document.getElementById("code")!=null && document.getElementById("code").value!="") || (document.getElementById("name")!=null && document.getElementById("name").value!="") )
				{
					document.getElementById("fromName").disabled=true;
					document.getElementById("toName").disabled=true;
					document.getElementById("fromName").value="0";
					document.getElementById("toName").value="0";
				}
				else
				{
					document.getElementById("fromName").disabled=false;
					document.getElementById("toName").disabled=false;
				}
			}

			function checkNameCode(obj)
			{
				if((document.getElementById("code")!=null && document.getElementById("code").value!="") || (document.getElementById("name")!=null && document.getElementById("name").value!="") )
				{
					alert('Search by Name Criteria cannot be selected for the given Code or Name ');
					document.getElementById("fromName").value="0";
					document.getElementById("toName").value="0";
					return false;
				}
			}

			function checkLessValue(obj)
			{
				if(obj!=null && obj.value!="")
				{
					if(document.getElementById("toName").value!="0")
					{
						if(document.getElementById("toName").value < obj.value)
						{
							alert('From Value should be less than To Value');
							document.getElementById("fromName").value="0";
							
							return false;
						}
					}
				}
			}
			
			function checkMoreValue(obj)
			{
				if(obj!=null && obj.value!="")
				{
					if(document.getElementById("fromName").value!="0")
					{
						if(document.getElementById("fromName").value > obj.value)
						{
							alert('To Value should be greater than From Value');
							document.getElementById("toName").value="0";
							
							return false;
						}
					}
				}
			}		
			
			
		var codeSelectionHandler = function(sType, arguments) {
   	 	var oData = arguments[2];
    		var billDetails = oData[0];
    		document.getElementById('billNumber').value = billDetails;
		document.getElementById('billId').value = oData[1];				
		}
		var codeSelectionEnforceHandler = function(sType, arguments) {
    		warn('impropercodeSelection');
		}
			</script>
			
			</head>
			<body onLoad ="execute();populateDesgBasedOnFunctionDept();" >
			<div id="container">
				<div id="codescontainer" style="text-align:left; width: 120px;" ></div>
			</div>
		<div class="formmainbox">
			<div class="insidecontent">
		  <div class="rbroundbox2">
			<div class="rbtop2"><div></div></div>
			  <div class="rbcontent2">


			<!-- Header Section Begins -->

			<!-- Header Section Ends -->

			<table width="95%" border="0" cellspacing="0" cellpadding="0" id="table2">
			<tr>
			<td>
			<!-- Tab Navigation Begins -->

			<!-- Tab Navigation Ends -->

			<!-- Body Begins -->



			<!-- Body Begins -->

			<div align="center">
			<center>
			<html:form  action="/pims/AfterSearchAction.do?submitType=executeSearchForAttendence"  >
			<table  width="100%" border="0" cellspacing="0" cellpadding="0"  >
			<tbody>
			<tr>
			<td colspan="4" class="headingwk" ><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
					 <% 
					String masterMode="Search";
					if(module!=null)
					{
							masterMode="search"+module; 
					}
				 %>
			<div class="headplacer"><bean:message key="<%=masterMode%>"/></div></td>
			</tr>
			<html:hidden property="module" value="<%=module%>" />
			<tr>
			<td   class="whiteboxwk" ><bean:message key="Functionary"/></td>
			<td class="whitebox2wk" >
			<select  name="functionaryId"   id="functionaryId" class="selectwk" onChange="populateDesgBasedOnFunctionDept();">
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
			<td   class="whiteboxwk" ><egovtags:filterByDeptMandatory/><bean:message key="Department"/></td>
			<td class="whitebox2wk" >
			<select  name="departmentId"   id="departmentId" class="selectwk" onChange="populateDesgBasedOnFunctionDept();">
			 <option value='0' selected="selected"><bean:message key="chooseType"/></option>
			 <egovtags:filterByDeptSelect/>
			</select>
			</td>
			</tr>

			<tr>
			<td  class="greyboxwk" ><bean:message key="EmployeeCode"/></td>
			<td   class="greybox2wk" width="10%">
			<input type="text"id="code" name="code" size="10"  class="selectwk" value = "<%=(request.getParameter("code")==null?"":request.getParameter("code"))%>" onblur="disableSearch(this);">
			</td>
			<td  class="greyboxwk"><bean:message key="EmployeeName"/></td>
			<td   class="greybox2wk">
			<input type="text"id="name" name="name" size="10" class="selectwk"  value = "<%=(request.getParameter("name")==null?"":request.getParameter("name"))%>"
			onblur="disableSearch(this);">
			</td>
			</tr>
			<tr id = "modifyAttendence">
			<%
			String  currentfinYear = EisManagersUtill.getCommonsService().getCurrYearFiscalId();
			CFinancialYear financialY =EisManagersUtill.getCommonsService().findFinancialYearById(new Long(currentfinYear));
			int currentmonth=Calendar.getInstance().get(Calendar.MONTH) + 1;
			String mon  = (String)EisManagersUtill.getMonthsStrVsDaysMap().get(new Integer(currentmonth));
			int monReq = currentmonth;
			String s= "sdfbgsd sdfg (+) ";
			System.out.println("dddddddddddddddddd"+s.lastIndexOf("(+)"));
			String monthStr = request.getParameter("monthId");
			if(monthStr!=null)
			monReq = Integer.parseInt(monthStr.trim());
			int fYearReq = Integer.parseInt(currentfinYear.trim());
			String fYearStr = request.getParameter("finYear");
			if(fYearStr!=null)
			fYearReq = Integer.parseInt(fYearStr.trim());

			%>

			<td  class="whiteboxwk" width="149"><bean:message key="Month"/></td>
			<td class="whitebox2wk" >
			<span class="greybox2wk">

			<select  name="monthId"   id="monthId" style = "width:120px">
			

			<%

			Map mMap =EisManagersUtill.getMonthsStrVsDaysMap();
			TreeSet set = new TreeSet(mMap.keySet());
			for (Iterator it = set.iterator(); it.hasNext(); )
			{
			Integer id = (Integer) it.next();
			/*
				remove duplicate current month
			*/
			
			%>
			<option  value = "<%= id.toString() %>"<%=((((Integer)id).intValue() == monReq)? "selected":"")%>><%= (String)mMap.get(id) %></option>

			<%
			}

			%>
			</span>
			</select>
			</td>

			</td> <td  class="whiteboxwk" width="149"><bean:message key="FinancialYear"/></td>
			<td class="whitebox2wk" >
			<select  name="finYear"   id="finYear" style = "width:150px">
			
			<%

			Map finMap =(Map)session.getAttribute("finMap");
			for (Iterator it = finMap.entrySet().iterator(); it.hasNext(); )
			{
			Map.Entry entry = (Map.Entry) it.next();
			/*
				remove duplicate current Financial year
			*/
			
			%>
			<option  value = "<%= entry.getKey().toString() %>"<%=((((Long)entry.getKey()).intValue() == new Long(fYearReq).intValue())? "selected":"")%>><%= entry.getValue() %></option>

			<%
			}

			%>
			</select>
			</td>

			</td>

			</tr>
			<tr>
			
			<td   class="greyboxwk" ><bean:message key="Designation"/></td>
			<td class="greybox2wk" >
			<select  name="designationId"   id="designationId" class="selectwk">
			<option value='0' selected="selected"><bean:message key="chooseType"/></option>
			
			</select>
			</td>
			
			<td  class="greyboxwk" width="149">Function</td>
			<td class="greybox2wk" >
			<select  name="functionId"   id="functionId" class="selectwk">
			<option value='0' selected="selected"><bean:message key="chooseType"/></option>
			
			<%

			Map functionMap =(Map)session.getAttribute("functionMap");
			for (Iterator it = functionMap.entrySet().iterator(); it.hasNext(); )
			{
			Map.Entry entry = (Map.Entry) it.next();
			
			
			%>
			<option  value = "<%= entry.getKey().toString() %>"<%=((((Long)entry.getKey()).intValue() == (request.getParameter("functionId")==null?0:Integer.valueOf(request.getParameter("functionId")).intValue()))? "selected":"")%>><%= entry.getValue() %></option>

			<%
			}

			%>
			</select>
			</td>
			
			
			</tr>

			<tr>
			<td   class="whiteboxwk" >Search Name Criteria</td>
			<td class="whitebox2wk" colspan="1" >
			From&nbsp<select  name="fromName"   id="fromName" class="selectwk" onChange="checkNameCode(this); checkLessValue(this);">
			<option value='0' selected="selected"><bean:message key="chooseType"/></option>
			<option value='A' >A</option>
			<option value='B' >B</option>
			<option value='C'>C</option>
			<option value='D'>D</option>
			<option value='E'>E</option>

			<option value='F'>F</option>
			<option value='G'>G</option>
			<option value='H'>H</option>
			<option value='I'>I</option>
			<option value='J'>J</option>
			<option value='K'>K</option>
			<option value='L'>L</option>
			<option value='M'>M</option>
			<option value='N'>N</option>
			<option value='O'>O</option>
			<option value='P'>P</option>
			<option value='Q'>Q</option>
			<option value='R'>R</option>
			<option value='S'>S</option>
			<option value='T'>T</option>
			<option value='U'>U</option>
			<option value='V'>V</option>
			<option value='W'>W</option>
			<option value='X'>X</option>
			<option value='Y'>Y</option>
			<option value='Z'>Z</option>
			</select>
&nbsp&nbsp
			To&nbsp<select  name="toName"   id="toName" class="selectwk" onChange="checkNameCode(this); checkMoreValue(this);">
			<option value='0' selected="selected"><bean:message key="chooseType"/></option>
			<option value='A'>A</option>
			<option value='B'>B</option>
			<option value='C'>C</option>
			<option value='D'>D</option>
			<option value='E'>E</option>

			<option value='F'>F</option>
			<option value='G'>G</option>
			<option value='H'>H</option>
			<option value='I'>I</option>
			<option value='J'>J</option>
			<option value='K'>K</option>
			<option value='L'>L</option>
			<option value='M'>M</option>
			<option value='N'>N</option>
			<option value='O'>O</option>
			<option value='P'>P</option>
			<option value='Q'>Q</option>
			<option value='R'>R</option>
			<option value='S'>S</option>
			<option value='T'>T</option>
			<option value='U'>U</option>
			<option value='V'>V</option>
			<option value='W'>W</option>
			<option value='X'>X</option>
			<option value='Y'>Y</option>
			<option value='Z'>Z</option>
			</select>
			</td>
			
				<td  class="whiteboxwk">Employee type</td>
				<td  class="whitebox2wk">
				  <html:select tabindex="1" styleId="empType" property="empType" styleClass="dropdownsize">
					<html:option value="0" ><bean:message key="chooseType"/></html:option>
					<html:options collection="employeeStatusMasterList" property="id" labelProperty="name"/>											
				  </html:select>
				</td>
		       
		       
			</tr>
			
		

            <td class="greyboxwk">Bill Number</td>
            <td  class="greybox2wk" width="20%" valign="top" >     
              <div class="yui-skin-sam">
              <div id="billNumberSearch_autocomplete" class="yui-ac" >
                <input type="text" id="billNumber" name="billNumber" size="10"   class="selectwk"/> 
		<input type="hidden" id="billId" name="billId" size="10"   class="selectwk"/>        
                  <div id="codeSearchResults"></div>
              </div>
              </div>
            <egovtags:autocomplete name="billNumber"  field="billNumber"
                       url="/payroll/billNumber/billNumberMaster!getBillNumberList.action" queryQuestionMark="true"  results="codeSearchResults"
                       handler="codeSelectionHandler" forceSelectionHandler="codeSelectionEnforceHandler"/>
                      <span class='warning' id="impropercodeSelectionWarning"></span>
            </td>
			<td class="greyboxwk">&nbsp;</td>  <td  class="greybox2wk" >&nbsp;</td>
			</tbody>
			</table>

<br>
			<table id = "submit" cellpadding ="0" cellspacing ="0" border = "0" >
			<tr >
			<td align = "center"><html:button styleClass="buttonfinal" value="Submit" property="b4" onclick="return submitSearch();" /></td>
			<tr>
			</table>
			
			<%
			Map mapofemp = (Map)request.getAttribute("employeeMap");

			if(mapofemp!=null&&!mapofemp.isEmpty())
			{
			%>
			<%@ include file="AttendenceCreate.jsp" %>
			<%
			}
			%>
			</div>

			</table>
			
			 <tr>
            <td><div align="right" class="mandatory">* Mandatory Fields</div></td>
          </tr>
          
          </div>
	  <div class="rbbot2"><div></div></div>
</div>
  </div>
  </table>
  </div>
<div class="buttonholderwk">&nbsp;

<input type="button" name="button" id="button" value="CLOSE"  class="buttonfinal" onclick="window.close();"/>
			</center>
			</html:form>
			

			</body>