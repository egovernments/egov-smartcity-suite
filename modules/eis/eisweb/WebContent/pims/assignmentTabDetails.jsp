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

	  

	   <%!
			EmployeeServiceImpl employeeServiceImpl;
		%>

				<%!
		//rename to getDeptIntegerObj
		public Set getDeptIntegerObj(Set set)
		{
			Set  newset = new HashSet();
			for(Iterator iter = set.iterator();iter.hasNext();)
			{
			EmployeeDepartment employeeDepartment = (EmployeeDepartment)iter.next();
			newset.add(employeeDepartment.getDept().getId());
			}
			return newset;
		}

		public Set getHodIntegerObj(Set set)
			{
			Set  newset = new HashSet();
			for(Iterator iter = set.iterator();iter.hasNext();)
			{
			EmployeeDepartment employeeDepartment = (EmployeeDepartment)iter.next();
			if(employeeDepartment.getHodept()!=null)
			newset.add(employeeDepartment.getHodept().getId());
			}
			return newset;
		}


		%>


				<%
						Map deptMap = (Map)session.getAttribute("deptmap");
						session.setAttribute("deptMap",deptMap);
						employeeServiceImpl=new EmployeeServiceImpl();
						List assPrdList = new ArrayList();
						PersonalInformation egpimsPersonalInformation =null;

						String id ="";
						
						if(request.getAttribute("employeeOb")!=null)
						{

						//id = request.getParameter("Id").trim();

						egpimsPersonalInformation = (PersonalInformation)request.getAttribute("employeeOb");

						}
						else
						{
						egpimsPersonalInformation = new PersonalInformation();

						}


						

						if(egpimsPersonalInformation.getEgpimsAssignmentPrd()!=null && !egpimsPersonalInformation.getEgpimsAssignmentPrd().isEmpty())
							{

							//List assignmentPrdList = eisManager.getAssPrdIdsForEmployee(egpimsPersonalInformation.getIdPersonalInformation());
							//System.out.println("assignmentPrdList"+assignmentPrdList);
							assPrdList.addAll(egpimsPersonalInformation.getEgpimsAssignmentPrd());
							//to sort by from date
							Collections.sort(assPrdList,AssignmentPrd.assignPrdComparator);	
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
		
		<br>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" name="assignmentTbl" id="assignmentTbl" ">
		<tr>
		<td colspan="4" class="headingwk">
		
		</td>
		</tr>
		<%
				if(! ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("view"))
				{
				%>
		<tr>
		<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="<%=request.getContextPath()%>/common/image/arrow.gif" /></div>
		<div class="headplacer">Assignment Details</div></td>

		</tr>
		<%} %>
		
		<div id="codescontainer"></div>
				<div id="codescontainerPos"></div>
			            
         <tr>
			  <%
				if(! ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("view"))
				{
				%>
                <td width="15%" class="whiteboxwk"><span class="mandatory">*</span>Is Primary:</td>
                <td colspan="3" width="26%" class="whitebox2wk">
                Yes&nbsp;<input type="radio" value="Y" name="checkPrimary"  id="checkPrimary" checked="true" onclick="loadDesg();document.pIMSForm.fromDateEnter.focus();"/>
                No&nbsp;<input type="radio" value="N" name="checkPrimary"  id="checkPrimary" onclick="loadDesg();document.pIMSForm.fromDateEnter.focus();"/>
                </td>                	
				<%}%>
         </tr>
              
              
         <tr>
			  
			  <input name="assignEntered" type="hidden" class="selectwk grey" id="assignEntered" size="10"/>
			  <input name="rowIndexOfAssignment" type="hidden" class="selectwk grey" id="rowIndexOfAssignment" size="10"/>
			 
			  <%
				if(! ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("view"))
				{
				%>
	                <td width="15%" class="whiteboxwk"><span class="mandatory">*</span><bean:message key="FromDate"/>:</td>
	                <td width="26%" class="whitebox2wk">
	                <input type="hidden" id="fromDateForWF" name="fromDateForWF"/>
	                
					<input name="fromDateEnter" type="text" class="selectwk grey" id="fromDateEnter" size="10" onblur="validateDateFormat(this);ToGrtThenFromDate();checkToDateGrtFromdate(this);CompeffecDate(this);"  
					onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')"  
					onChange="checkDiffFromtoAssignement(this)" /> <span class="whitebox3wk">
					
					<a href="javascript:show_calendar('pIMSForm.fromDateEnter');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="<c:url value='../common/image/calendar.png' />" border="0"></a>
					
					</span></td>
					
					<input type="hidden" id="toDateForWF" name="toDateForWF"/>
	                <td width="14%" class="whiteboxwk"><span class="mandatory">*</span><bean:message key="ToDate"/>:</span></td>
	
	                <td width="45%" class="whitebox2wk"><input name="toDateEnter" type="text" class="selectwk grey" id="toDateEnter" size="10"
					onblur="validateDateFormat(this);ToGrtThenFromDate();chckfromto(this);toRetirementDate(this);"
					onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')"/> <span class="whitebox3wk">
					<a href="javascript:show_calendar('pIMSForm.toDateEnter');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="<c:url value='../common/image/calendar.png' />" border="0"></a>
					
					</span></td>
				<%}%>
          </tr>
		  <tr>
			  <%
				if(! ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("view"))
				{
				%>
                <td class="greyboxwk"><span class="mandatory">*</span><bean:message key="EmployeeFund"/>:</td>

                <td colspan="3" class="greybox2wk">
                  <select name="fundEntry" class="selectwk" id="fundEntry" />
				  <option value='0' selected="selected"><bean:message key="chooseType"/></option>
				<%


				HashMap fund = (HashMap)session.getAttribute("fundMap");
			    for (Iterator itr = fund.entrySet().iterator(); itr.hasNext(); )
				{
					Map.Entry entry = (Map.Entry) itr.next();
				
				%>
				<option value="<%= entry.getKey().toString() %>"><%= entry.getValue() %></option>
				<%
				}
				%>
				  </select>
                </td>
				<%}%>
           </tr>
		   <tr>
				 <%
				if(! ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("view"))
				{
				%>
                <td class="whiteboxwk"><span class="mandatory">*</span><bean:message key="EmployeeFunction"/>:</td>
                <td colspan="3" class="whitebox2wk">
				<select name="functionEntry" class="selectwk" id="functionEntry" >
				<option value='0' selected="selected"><bean:message key="chooseType"/></option>
				<%

					Map functionMapEnter =(Map)session.getAttribute("functionMap");
					for (Iterator it = functionMapEnter.entrySet().iterator(); it.hasNext(); )
					{
					Map.Entry entry = (Map.Entry) it.next();

					%>
					<option  value = "<%= entry.getKey().toString() %>"><%= entry.getValue() %></option>
					<%
					}
					%>
				</select>
				</td>
            </tr>
				<%}%>
              
				
			<tr>			
				<%
				if(! ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("view"))
				{
				%>
                <td class="greyboxwk"><span class="mandatory">*</span><bean:message key="EmployeeGrade"/>:</td>

                <td class="greybox2wk">
                  <select name="gradeEntry" id="gradeEntry" class="selectwk" onChange="">
                    <option value="0" selected="selected"><bean:message key="chooseType"/></option>
                   <%
					Map gradeMapEnter =(Map)session.getAttribute("gradeMap");
					for (Iterator it = gradeMapEnter.entrySet().iterator(); it.hasNext(); )
					{
					Map.Entry entry = (Map.Entry) it.next();
					%>
					<option  value = "<%= entry.getKey().toString() %>"><%= entry.getValue() %></option>
					<%
					}
					%>
                  </select>
               </td>
                <td class="greyboxwk" width="20%"><span class="mandatory">*</span><bean:message key="Designation"/>:</td>
                <td class="greybox2wk" width="25%">
				<input  type="hidden" name="desgEnterId" id="desgEnterId"  />
                  <input name="desginationName" type="text" class="selectwk" id="desginationName" size="20" autocomplete="off" style="width: 120px;"
	  				 onkeyup="autocompleteForDesignation(this,event);" onblur="fillDesgAfterSplit(this,'desgEnterId');checkPosition(this,event);"
					 onchange="checkPosition(this,event);checkPosForDesig();"/>
              	</td>
			  <%}%>
        </tr>
		<tr>
				<%
				if(! ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("view"))
				{
				%>
                <td class="whiteboxwk"><span class="mandatory">*</span><bean:message key="Position"/>:</td>
                <td class="whitebox2wk" >
				<input  type="hidden" name="positionId" id="positionId"  />
				<input  type="hidden" name="positionForWF" id="positionForWF"  />
				<input name="positionEntryName" style="width: 150px;" type="text" class="selectwk" id="positionEntryName" size="50" autocomplete="off"
			
			onkeyup="autocompletePosition(this,event);"  onblur="fillPositionAfterSplit(this,'positionId');"/></td>
                
				<td class="whiteboxwk" width="20%"><span class="mandatory">*</span><bean:message key="Functionary"/>:</td>
                <td class="whitebox2wk" width="25%">
				<select style="width: 200px;" name="functionaryEntry" class="selectwk" id="functionaryEntry" />
				<option value="0" selected="selected"><bean:message key="chooseType"/></option>
				<%

				Map functionaryEnterMap =(Map)session.getAttribute("functionaryMap");
				for (Iterator it = functionaryEnterMap.entrySet().iterator(); it.hasNext(); )
				{
				Map.Entry entry = (Map.Entry) it.next();

				%>
				<option  value = "<%= entry.getKey().toString() %>"><%= entry.getValue() %></option>

				<%
				}
				
				%>
				</select>
				</td>
				<%}%>
       </tr>


		<tr>
			 <%
				if(! ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("view"))
				{
				%>
                <td class="greyboxwk"><span class="mandatory">*</span><bean:message key="MainDepartment"/>:</td>

                <td class="greybox2wk">

                  <select name="deptEntry" class="selectwk" id="deptEntry" onchange="checkPrimaryTempCombo();clearPosDesigAfterDeptChange();"/>
				  <option value="0" selected="selected"><bean:message key="chooseType"/></option>
				  <%

						Map MaindeptEntryMap =(Map)session.getAttribute("deptMap");

						for (Iterator it = MaindeptEntryMap.entrySet().iterator(); it.hasNext(); )
						{
							Map.Entry entry = (Map.Entry) it.next();%>
						
						<option  value = "<%= entry.getKey().toString() %>"><%= entry.getValue() %></option>
						<%
						}
			%>
				  </select>
                </td>

                <td class="greyboxwk">Govt Order No:</td>
                <td class="greybox2wk">
                  <input name="govtEntry" type="text" class="selectwk" id="govtEntry" size="10" />
                  </td>
				  
         </tr>
		 <tr>
			  <td class="whiteboxwk" ><bean:message key="IfHOD"/></td>

		<%
		boolean b1 = false;
		if(request.getParameter("Id")!=null)
		{

		//String empid = request.getParameter("Id").trim();
		//egpimsPersonalInformation = eisManager.getEmloyeeById(new Integer(id));
		Set prdst = egpimsPersonalInformation.getEgpimsAssignmentPrd();
		for(Iterator prdstIter = prdst.iterator() ;prdstIter.hasNext();)
		{
		AssignmentPrd assignmentPrd =(AssignmentPrd)prdstIter.next();

		Set st = assignmentPrd.getEgpimsAssignment();
		for(Iterator setaIter = st.iterator();setaIter.hasNext();)
		{
		Assignment assignment =(Assignment)setaIter.next();
		if(employeeServiceImpl.getHodById(assignment.getId())==true)
		b1 = true;
		}
		}
		}


		%>

		<%
		String modeonView=((String)(session.getAttribute("viewMode"))).trim();
		if(!modeonView.equalsIgnoreCase("view"))
		{		
		%>

			<td   class="whitebox2wk" colspan="1"> <bean:message key="Yes"/>
			<input type="radio" value="1" name="checkhod"   id="checkhod" onclick="enableHodOndemand(this.value)"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:message key="No"/><input type="radio" value="0" name="checkhod"  id="checkhod" onclick="enableHodOndemand(this.value)">
			</td>
		<td class="whiteboxwk" id = "deptHodOnTopLbl"  name = "deptHodOnTopLbl"><bean:message key="Departmnents"  /></td>
		<td   id = "deptHodOnTopTd"  name = "deptHodOnTopTd"  width="2%" >

				<select    name="deptHodOnTopSelect"  id="deptHodOnTopSelect" multiple="true" size="5" onMouseOver="addTitleAttributes(this);" >
				<option value='0' >choose</option>
				<%

				Map deMapForHodOnTop =(Map)session.getAttribute("deptmap");

				for (Iterator it = deMapForHodOnTop.entrySet().iterator(); it.hasNext(); )
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
		}
		%>
		
		</tr>


		
		<tr>
		<td colspan="4">
			<table WIDTH="100%"  cellpadding ="0" cellspacing ="0" border = "0" id="AssignmentSaveTable" name="AssignmentSaveTable" >
			<%
					if(! ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("view"))
					{
					%>
			    <tr>
	                <td colspan="4" class="aligncenter">	 			
						<input type="button" name="addAssButn" id="addAssButn" value="SAVE/MODIFY" class="buttonfinal"
						onclick="javascript:getRetirementAgeAndDate();addAssignmentRow(this,document.getElementById('EOTable'),document.getElementById('EORow'));"/>&nbsp;&nbsp;
						<input type="button" name="button4" id="button4" value="CLEAR"  class="buttonfinal" onclick="javascript:resetFieldValue();"/>
					</td>
	        	</tr>
			<%}%>
			</table>	
		
		</td></tr>
		</table>
		<div class="ScrollAuto">
		<table WIDTH="100%"  cellpadding ="0" cellspacing ="0" border = "0" id="asstablelist">
		<tr><td colspan="4" class="headingwk" >
		<div class="arrowiconwk" ><img src="<%=request.getContextPath()%>/common/image/arrow.gif" /></div>
					
						<div class="headplacer" style="width: 50%;">Assignment Detail List</div></td>			
				
		</tr>
			
				
		
		<tr><td colspan="4">
			
			<table cellpadding ="0" width="100%" cellspacing ="0" border = "0" id="EOTable" name="EOTable"  align = "center">
		 

			<tbody>
			
			
			<tr>
                <td  class="tablesubheadwk" ><bean:message key="FromDate"/></td>
				<td  class="tablesubheadwk" ><bean:message key="ToDate"/></td>
				<td  class="tablesubheadwk" ><bean:message key="EmployeeFund"/></td>
				<td  class="tablesubheadwk" ><bean:message key="EmployeeFunction"/></td>
				<td class="tablesubheadwk" ><bean:message key="EmployeeGrade"/></td>
				<td  class="tablesubheadwk"><bean:message key="Designation"/></td>
				<td class="tablesubheadwk"   ><bean:message key="Position"/></td>
				<td  class="tablesubheadwk" ><bean:message key="Functionary"/></td>
				<td class="tablesubheadwk" id = "departmnent"  name = "departmnent"><bean:message key="Departmnents"/></td>
				<td   class="tablesubheadwk" ><bean:message key="MainDepartment"/></td>
				<td class="tablesubheadwk" >Govt Order No</td>
				<td  class="tablesubheadwk" >Primary</td>
				<%
						if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
						{
						%>
				 <td class="tablesubheadwk" colspan="2">edit</td>
				 <%}%>
             </tr>
			<%

			String assignmentId[]=null;
			String fromDate[]=null;
			String toDate[]=null;
			String fundId[]=null;
			String functionId[]=null;
			String designationId[]=null;
			String functionaryId[]=null;
			String ptcallocation[]=null;
			String posId[]=null;
			String departmentId[]=null;
			String designationName =null;
			String positionName=null;
			/*
			   Government Assignment Number
			*/

			String govtNo[]=null;
			/*
			   Grade changes. Moved from egi to Pims
			*/

			String grade[]=null;

			
			int setSize=assPrdList.size();
			System.out.println("asdf"+setSize);
			assignmentId=new String[setSize];
			fromDate=new String[setSize];
			toDate=new String[setSize];
			fundId=new String[setSize];
			functionId=new String[setSize];
			designationId=new String[setSize];
			functionaryId=new String[setSize];
			ptcallocation=new String[setSize];
			posId=new String[setSize];
			Set departSet = new HashSet();
			Set hodSet = new HashSet();
			departmentId=new String[setSize];
			//Government Assignment Number
			govtNo=new String[setSize];
			//grade 
			grade=new String[setSize];

			Iterator assPrdSetitr = assPrdList.iterator();
			String deptvalue ="";
			String hodvalue ="";
			String mainDeptvalue ="";
			String counter ="";
			String deptval ="";
			String hodval ="";
			for(int x=0;assPrdSetitr.hasNext();x++)
			{
			deptvalue = "departmentId" + x;
			hodvalue = "hod" + x;
			deptval = "depId" + x;
			hodval = "headod" + x;

			counter = new Integer(x).toString();
			AssignmentPrd egEmpAssignmentPrd = (AssignmentPrd)assPrdSetitr.next();
			//assignmentId[x]=egEmpAssignmentPrd.getId()==null?"":egEmpAssignmentPrd.getId().toString();
			fromDate[x]=egEmpAssignmentPrd.getFromDate()==null?"":sdf.format(egEmpAssignmentPrd.getFromDate());
			toDate[x]=egEmpAssignmentPrd.getToDate()==null?"":sdf.format(egEmpAssignmentPrd.getToDate());
			Set egEmpAssignmentSet = egEmpAssignmentPrd.getEgpimsAssignment();
			if(egEmpAssignmentPrd.getId()!=null)
			HibernateUtil.getCurrentSession().lock(egEmpAssignmentPrd,LockMode.NONE);
			Iterator egEmpAssignmentSetitr = egEmpAssignmentSet.iterator();
			for(int i=0;egEmpAssignmentSetitr.hasNext();i++)
			{

			departSet = new HashSet();
			hodSet = new HashSet();
			Assignment egEmpAssignment= (Assignment)egEmpAssignmentSetitr.next();
			assignmentId[i]=egEmpAssignment.getId()==null?"":egEmpAssignment.getId().toString();
			designationId[i]=egEmpAssignment.getDesigId()==null?"0":egEmpAssignment.getDesigId().getDesignationId().toString();
			if(designationId[i]!=null && !designationId[i].equals("") && !designationId[i].equals("0") )
				{
				
				   DesignationMasterDAO designationMasterDAO = new DesignationMasterDAO();
				   designationName=designationMasterDAO.getDesignationMaster(new Integer(designationId[i]).intValue()).getDesignationName();
				}
			fundId[i]=egEmpAssignment.getFundId()==null?"0":egEmpAssignment.getFundId().getId().toString();
			functionId[i]=egEmpAssignment.getFunctionId()==null?"0":egEmpAssignment.getFunctionId().getId().toString();
			functionaryId[i]=egEmpAssignment.getFunctionary()==null?"0":egEmpAssignment.getFunctionary().getId().toString();

			govtNo[i]=egEmpAssignment.getGovtOrderNo()==null?"0":egEmpAssignment.getGovtOrderNo();
			//grade[i] = egEmpAssignment.getGradeId().getId().toString();
			grade[i] = egEmpAssignment.getGradeId()==null?"0":egEmpAssignment.getGradeId().getId().toString();


			posId[i] = egEmpAssignment.getPosition()==null?"0":egEmpAssignment.getPosition().getId().toString();
			if(posId[i]!=null && !posId[i].equals("") && !posId[i].equals("0"))
			{
				PositionMasterDAO positionDao = new PositionMasterDAO();
				positionName=positionDao.getPosition(new Integer(posId[i]).intValue()).getName();
				System.out.println("positionName-->>>"+positionName);

			}
			departSet = getDeptIntegerObj(egEmpAssignment.getDeptSet());
			hodSet = getHodIntegerObj(egEmpAssignment.getDeptSet());
			System.out.println("egEmpAssignment.getDeptSet()"+egEmpAssignment.getDeptSet());
			System.out.println("posId[i]"+posId[i]);
			System.out.println("gradeId[i]"+grade[i]);			
			System.out.println("egEmpAssignment "+egEmpAssignment.getId()+" deptset "+egEmpAssignment.getDeptSet()+" index "+i);



			%>
			<tr id="EORow">
				<input type = "hidden" name="assignmentId" id="assignmentId" value="<%=assignmentId[x]%>" />
				<input type = "hidden" name="counter" id="counter" value="<%=counter%>" />
				<input type = "hidden" name="ptcallocation" id="ptcallocation" value="<%="100"%>" />
			
				<%
				if( ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("modify") ||  ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("view"))
				{
				%>
				<td class="whitebox3wk" width="2%"><input type="text" style="width: 67px;" name="fromDate" id="fromDate" value="<%=fromDate[x]%>"  disabled="disabled"/></td>
				<%
				}
				else
				{
				%>
				<td class="whitebox3wk" width="2%"><input type="text" style="width: 67px;" name="fromDate" id="fromDate" disabled="true"/></td>
				<%
				}
				%>
				<td class="whitebox3wk" width="2%">
				<input type="text"  style="width: 67px;" name="toDate" id="toDate"   value="<%=toDate[x]%>"    class="dataList" disabled="true"/></td>

				<td class="whitebox3wk" width="2%">
				<select  name="fundId" style="width: 150px;" id="fundId" disabled="true">
				<option value='0' selected="selected"><bean:message key="chooseType"/></option>
				<%


				HashMap fundMap = (HashMap)session.getAttribute("fundMap");


				for (Iterator it = fundMap.entrySet().iterator(); it.hasNext(); )
				{
				Map.Entry entry = (Map.Entry) it.next();
				//TreeSet sortedSetmp = new TreeSet(entry.getValue());
				%>
				<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).intValue() == new Integer(fundId[i]).intValue())? "selected":"")%>><%= entry.getValue() %></option>
				<%
				}
				%>
				</select>
				</td>
				<td class="whitebox3wk" width="2%" >
				<select  name="functionId"  style="width: 150px;"  id="functionId" disabled="true">
				<option value='0' selected="selected"><bean:message key="chooseType"/></option>
				<%

				Map functionMap =(Map)session.getAttribute("functionMap");
				for (Iterator it = functionMap.entrySet().iterator(); it.hasNext(); )
				{
				Map.Entry entry = (Map.Entry) it.next();

				%>
				<option  value = "<%= entry.getKey().toString() %>"<%=((((Long)entry.getKey()).intValue() == new Long(functionId[i]).intValue())? "selected":"")%>><%= entry.getValue() %></option>
				<%
				}
				%>
				</select>
				</td>

				<td class="whitebox3wk" width="2%">

				<select  name="gradeId" style="width: 50px;" id="gradeId" disabled="true">

				<!--<select  name="gradeId" id="gradeId" style = "width:80; height:21">-->
				<option value='0' selected="selected"><bean:message key="chooseType"/></option>
				<%
				Map gradeMap =(Map)session.getAttribute("gradeMap");
				for (Iterator it = gradeMap.entrySet().iterator(); it.hasNext(); )
				{
				Map.Entry entry = (Map.Entry) it.next();
				%>
				<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).toString().equals(grade[i]))? "selected":"")%>><%= entry.getValue() %></option>
				<%
				}
				%>
				</select>
				</td>
				
		

				<%
				if( ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("modify") ||  ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("view"))
				{
				%>
				<td class="whitebox3wk" width="2%">
				   <input type="hidden" name="desgId" id="desgId"  value="<%=designationId[i]%>" />
				   <input type="text" style="width: 150px;"  name="designationId" id ="designationId"  value="<%=designationName%>" disabled="true"/>

					
				</td>
				<%}else{%>

				<td class="whitebox3wk" width="2%">
				   <input  type="hidden" name="desgId" id="desgId"  />
					<input type="text" style="width: 80px;"  name="designationId" id ="designationId" disabled="true" />

					
				</td>

				<%}%>


		 

			  <%
				if( ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("modify") ||  ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("view"))
				{
				%>
			   <td class="whitebox3wk" width="2%" >
				   
					<input type="hidden" name="posId" id="posId" value="<%=posId[i]%>" />
					<input type="text" name="posName" style="width: 150px;" id ="posName" value="<%=positionName%>" disabled="true"/>

					 
				</td>
		  <%}else{%>

			  <td class="whitebox3wk" width="2%">
				   
					<input type="hidden" name="posId" id="posId"  />
					<input type="text" style="width: 100px;" name="posName" id ="posName" disabled="true"/>

					 
				</td>

				<%}%>

				<td class="whitebox3wk" width="2%">
				<select  name="functionaryId" style="width: 200px;"  id="functionaryId"  disabled="true">
				<option value='0' selected="selected"><bean:message key="chooseType"/></option>
				<%

				Map functionaryMap =(Map)session.getAttribute("functionaryMap");
				for (Iterator it = functionaryMap.entrySet().iterator(); it.hasNext(); )
				{
				Map.Entry entry = (Map.Entry) it.next();

				%>
				<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).intValue() == new Integer(functionaryId[i]).intValue())? "selected":"")%>><%= entry.getValue() %></option>

				<%
				}
				%>
				</select>
				</td>



				<%
				String mod=((String)(session.getAttribute("viewMode"))).trim();
				if(mod.equalsIgnoreCase("modify") || mod.equalsIgnoreCase("view"))
				{
					
				%>
				<td class="whitebox3wk" id = "depId0"  name = "depId0" width="2%">
				 <input name="departmentIdOfHod" type="hidden"  id="departmentIdOfHod" />
				<select   name="departmentIdHodSelect"  id="departmentId" multiple="true"  size="3" disabled="true">
					
				<%

					Map depMap =(Map)session.getAttribute("deptmap");
	
					for (Iterator it = depMap.entrySet().iterator(); it.hasNext(); )
					{
					Map.Entry entry = (Map.Entry) it.next();
					if(departSet.contains(entry.getKey()))
					{

				%>
				<option  value = "<%= entry.getKey().toString() %>"<%="selected"%>><%= entry.getValue() %></option>
				<%
					}
				
					}
				%>
				</select>
				</td>
				
				<td id="departmentIdDisplay" name="departmentIdDisplay" class="whitebox3wk" >None </td>

				<%
				}
				else if(mod.equalsIgnoreCase("create"))
				{
				%>
				<td class="whitebox3wk"  id = "depId0"  name = "depId0"  width="2%">
				<input name="departmentIdOfHod" type="hidden"  id="departmentIdOfHod" />
				<select    name="departmentIdHodSelect"  id="departmentId" multiple="true" size="3" >
				<option value='0' selected="selected">choose</option>
				
				</select>
				</td>
				<td id="departmentIdDisplay" name="departmentIdDisplay" class="whitebox3wk" >None</td>
				<%
				}
				
				String mode=((String)(session.getAttribute("viewMode"))).trim();
				if(mode.equalsIgnoreCase("modify") || mode.equalsIgnoreCase("view"))
				{
				%>
				<td class="whitebox3wk" width="2%">

				<select  name="mainDepartmentId"  id="mainDepartmentId"  disabled="true">

				<%

				Map MaindeptMap =(Map)session.getAttribute("deptMap");

				for (Iterator it = MaindeptMap.entrySet().iterator(); it.hasNext(); )
				{
				Map.Entry entry = (Map.Entry) it.next();
				if(egEmpAssignment.getDeptId() !=null && egEmpAssignment.getDeptId().getId().equals((Integer)entry.getKey()))
				{

				%>
				<option  value = "<%= entry.getKey().toString() %>"<%="selected"%>><%= entry.getValue() %></option>
				<%
				}
				else
				{
				%>
				<option  value = "<%= entry.getKey().toString() %>"><%= entry.getValue() %></option>
				<%
				}
				}
				%>
				</select>
				</td>

				<%
				}
				else if(mode.equalsIgnoreCase("create"))
				{
				%>
				<td class="whitebox3wk" width="2%">

				<select style="width:100px" name="mainDepartmentId"  id="mainDepartmentId" disabled="true"  >
				<option value='0' selected="selected"><bean:message key="chooseType"/></option>
				<%

				Map MaindeptMap =(Map)session.getAttribute("deptMap");

				for (Iterator it = MaindeptMap.entrySet().iterator(); it.hasNext(); )
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

				<td class="whitebox3wk" width="2%">
					<input type="text"  style="width:40px" value="<%=egEmpAssignment.getGovtOrderNo()==null?"":egEmpAssignment.getGovtOrderNo().toString()%>" name="assignmentOrderNo" id="assignmentOrderNo" class="fieldcell" disabled="true" >
				</td> 
				<td class="whitebox3wk" width="2%">
					<input type="text" style="width:35px" name="isPrimary" value="<%=egEmpAssignment.getIsPrimary()=='Y'?"Yes":"No"%>"  id="isPrimaryId" class="fieldcell" readOnly="true" >
				</td> 	
					<%
						if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
						{
						%>
				<td width="2%"><span><a href="#"><img src="<%=request.getContextPath()%>/common/image/book_edit.png" alt="Edit Data"  border="0" align="absmiddle" onclick="javascript:editAssRow(this,'EOTable')"/></a>
					<%if( egEmpAssignment.getIsPrimary()!='Y'){%>
					<a href="#"><img src="<%=request.getContextPath()%>/common/image/cancel.png" alt="Delete" width="16" height="16" border="0" onclick="javascript:deleteAssRow(this,'EOTable')" /></a></span>
					 
					<%}%>
					</td>
					<%}%>

			</tr>
			<%			
			}
			}
			%>

			</tbody>
		
		</table>
		
		
					
		
		</td></tr>
	</table>
	</div>




