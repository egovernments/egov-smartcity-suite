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
		org.egov.infstr.commons.*,

		org.egov.pims.commons.client.*,
		org.egov.infstr.commons.dao.*,
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
		public Map getGradeMapForSkill(Integer skillId)
		{

		Map gradeMap = new HashMap();
		System.out.println("Skill ID-------------"+skillId);
		if(skillId!=null&&!skillId.equals(new Integer(0)))
		{
	//try
		//{
		SkillMasterDAO skillMasterDAO = new SkillMasterDAO();
		SkillMaster skillMaster = (SkillMaster) skillMasterDAO.getSkillMaster(skillId.intValue());
		Set gradeSet = skillMaster.getSettechnicalGradesMaster();
		if(gradeSet!=null&&!gradeSet.isEmpty())
		{
		Iterator gradeitr = gradeSet.iterator();
		while(gradeitr.hasNext())
		{
		TechnicalGradesMaster 	technicalGradesMaster = (TechnicalGradesMaster)gradeitr.next();
		gradeMap.put(technicalGradesMaster.getId(),technicalGradesMaster.getGradeName());
		}
		}

		}
	   return gradeMap;

		}
		%>
				<%
						Map deptMap = (Map)session.getAttribute("deptmap");
						session.setAttribute("deptMap",deptMap);
						Set deptSet = null;
						Set eduDetailsSet = null;
					
						Set teckQualSet = null;
						Set ImmovablePropDetailsSet = null;
						Set movablePropDetailsSet = null;
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


						if(egpimsPersonalInformation.getEgpimsDeptTests().isEmpty()){
							deptSet = new HashSet();
							deptSet.add(new DeptTests());
						} else
							deptSet = egpimsPersonalInformation.getEgpimsDeptTests();

						if(egpimsPersonalInformation.getEgpimsEduDetails().isEmpty()){
							eduDetailsSet= new LinkedHashSet();
							eduDetailsSet.add(new EduDetails());
						} else
							eduDetailsSet = egpimsPersonalInformation.getEgpimsEduDetails();
						
						if(egpimsPersonalInformation.getEgpimsTecnicalQualification().isEmpty()) {
							teckQualSet= new LinkedHashSet();
							teckQualSet.add(new TecnicalQualification());
						}else
							teckQualSet = egpimsPersonalInformation.getEgpimsTecnicalQualification();

							//property set values

						if(egpimsPersonalInformation.getEgpimsImmovablePropDetailses().isEmpty()) {
							ImmovablePropDetailsSet = new HashSet();
							ImmovablePropDetailsSet.add(new ImmovablePropDetails());
						} else
						 	ImmovablePropDetailsSet = egpimsPersonalInformation.getEgpimsImmovablePropDetailses();
						
						if(egpimsPersonalInformation.getEgpimsMovablePropDetailses().isEmpty()) {
							movablePropDetailsSet = new HashSet();
							movablePropDetailsSet.add(new MovablePropDetails());
						} else
							movablePropDetailsSet = egpimsPersonalInformation.getEgpimsMovablePropDetailses();
						
	
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");


				%>
<br>
		
     		<table align='center' class="tableStyle" cellpadding ="0" cellspacing ="0" border = "0"  name ="EqnameTable" id ="EqnameTable" WIDTH=95% style="border: 1px solid #D7E5F2">
		<tbody>
       <tr>
                <td colspan="7" class="headingwk"><div class="arrowiconwk"><img src="<%=request.getContextPath()%>/common/image/arrow.gif" /></div>
                  <div class="headplacer"><bean:message key="EducationalQualification"/></div></td>
              </tr>

		<tr>
		
		<td   width="15%" class="tablesubheadwk" ><bean:message key="Qualification"/></td>
		<td  width="46%"  class="tablesubheadwk" ><bean:message key="MajorSubject"/></td>
		<td   width="9%" class="tablesubheadwk" ><bean:message key="PassDateDayMonYr"/></td>
		<td   width="20%" class="tablesubheadwk" ><bean:message key="UnivBoard"/></td>
		
		<%
		if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
		{	
		%>
		<td width="5%"  class="tablesubheadwk" ><bean:message key="DocAttach"/></td>
		<td width="5%" class="tablesubheadwk">Add/Dell</td>
		<%
		}
		else
		{%>
			<td  width="5%"  class="tablesubheadwk" ><bean:message key="DocView"/></td>
         <%
		}
		%>

		</tr>
		<%
		Iterator itred = eduDetailsSet.iterator();
		for(int i=0;itred.hasNext();i++)
		{
		EduDetails egpimsDeptTests = (EduDetails)itred.next();
		%>
		<tr id="EqnameRow">
		
		
		
		<td class="whitebox3wk">
		<input type = "hidden" name="educationDetailsId" id="educationDetailsId" value="<%=egpimsDeptTests.getEducationDetailsId()==null?"0":egpimsDeptTests.getEducationDetailsId().toString()%>" />
		
		<input  type="text"  class="selectwk textmxwidth2" name="qulification" id="qulification" value="<%=egpimsDeptTests.getQulificationMaster()==null?"":egpimsDeptTests.getQulificationMaster().toString()%>" onBlur ="checkQualificationnumeric(this)"; ></td>
		<td class="whitebox3wk">
		
		<input  type="text"  class="selectwk textmxwidth2" name="majorSubject"  id="majorSubject" value="<%=egpimsDeptTests.getMajorSubject()==null?"":egpimsDeptTests.getMajorSubject()%>" onBlur ="checkQulification(this);checkMajorsnumeric(this);"></td>

		<td class="whitebox3wk"><input  type="text"  size="10" class="selectwk textmxwidth2" name="monthandYearOfPass" id="monthandYearOfPass"onBlur = "validateDateFormat(this);validateDateJS(this);checkQulification(this)" value = "<%=egpimsDeptTests.getMonthYearPass()==null?"":sdf.format(egpimsDeptTests.getMonthYearPass())%>" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" >
		<a href="javascript:show_calendar('pIMSForm.monthandYearOfPass');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="<c:url value='../common/image/calendar.png' />" border=0></a>
		</td>

		<td class="whitebox3wk">
		
		<input  type="text"  class="selectwk textmxwidth2" name="universityBoard"  id="universityBoard" onBlur ="checkQulification(this);checkBoardnumeric(this);"  value="<%=egpimsDeptTests.getUniBoard()==null?"":egpimsDeptTests.getUniBoard().toString()%>">
		<input type="hidden"  class="selectwk textmxwidth2" name="eduDocNo" id="eduDocNo" value="<%=egpimsDeptTests.getDocNo()==null?"":egpimsDeptTests.getDocNo().toString()%>">
		</td>
		
		<%
		if(((String)(session.getAttribute("viewMode"))).trim().equals("view")){%>
		
		 
		<td>
			<egovtags:docFiles moduleName="EIS" documentNumber='<%=egpimsDeptTests.getDocNo()==null?"":egpimsDeptTests.getDocNo().toString()%>' id='<%="EmpEducation"+i%>' showOnLoad="true"/>
		</td>
	
	
	<%
	}
	else{
		if(egpimsDeptTests.getDocNo()==null){
			%>
			<td>  
				<div class="buttonholderrnew">
					<a href="#"><img src="<%=request.getContextPath()%>/images/page_text.gif" title="Add" alt="Add" width="16" height="16" border="0" onclick="uploadEduDoc(this);return false;" /></a>
				</div>
			</td>
	<%
		}else
			{
			if(!((String)(session.getAttribute("viewMode"))).trim().equals("view")){
				%>
				<td>
					<egovtags:docFiles moduleName="EIS" documentNumber='<%=egpimsDeptTests.getDocNo()==null?"":egpimsDeptTests.getDocNo().toString()%>' id='<%="EmpEducation"+i%>' showOnLoad="true"/>
					<div class="buttonholderrnew">
						<a href="#"><img src="<%=request.getContextPath()%>/images/page_text.gif" title="Edit" alt="Edit" width="16" height="16" border="0" onclick="uploadEduDoc(this);return false;" /></a>
					</div>
				</td>
		<%
			}
			}

	}
	
	if(!((String)(session.getAttribute("viewMode"))).trim().equals("view")){ %>
		 <td class="labelcell">
			
			<a href="#"><img src="<%=request.getContextPath()%>/common/image/add.png" alt="Add" width="16" height="16" border="0" onclick="whichButtonService('EqnameTable',this,'EqnameRow');"/></a>
			<a href="#"><img src="<%=request.getContextPath()%>/common/image/cancel.png" alt="Delete" width="16" height="16" border="0" onclick="deleteServiceRow('EqnameTable',this);" /></a>
		</td>
		
		<%
		}
		}
		%>

		</tr></tbody>

</table>




     <br>
		<table align='center' class="tableStyle" cellpadding ="0" cellspacing ="0" border = "0"  name ="TeckDetails" id ="TeckDetails" WIDTH=95% style="border: 1px solid #D7E5F2">
		<tbody>
		 <tr>
                <td colspan="7" class="headingwk"><div class="arrowiconwk"><img src="<%=request.getContextPath()%>/common/image/arrow.gif" /></div>
                  <div class="headplacer"><span class="subheadnew"><bean:message key="TechQualification"/></span></div></td>
              </tr>

		<tr>
		
		<td width="20%"   class="tablesubheadwk"  ><bean:message key="Skills"/></td>
		<td   width="5%" class="tablesubheadwk"  ><bean:message key="EmployeeGrade"/></td>
		<td  width="9%" class="tablesubheadwk"  ><bean:message key="PassDate"/></td>
		<td   width="56%" class="tablesubheadwk"  ><bean:message key="Remarks"/></td>
		<%
		if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
		{
		%>
		<td   class="tablesubheadwk" width="5%" ><bean:message key="DocAttach"/></td>
		<td   class="tablesubheadwk" width="5%" >Add/Dell</td>
		<%}
		else
		{%>
			<td   class="tablesubheadwk" width="5%" ><bean:message key="DocView"/></td>
		<%
		}
		%>
		</tr>
		<%
		Iterator teckitr = teckQualSet.iterator();
		for(int i=0;teckitr.hasNext();i++)
		{
		TecnicalQualification egpimsTecnicalQualification = (TecnicalQualification)teckitr.next();
		%>
		<tr id="TeckDetailsnameRow">
		
		
		
		<td class="whitebox3wk" >
		<input type = hidden name="tecnicalQualificationId" id="tecnicalQualificationId" value="<%=egpimsTecnicalQualification.getTecnicalQulificationId()==null?"0":egpimsTecnicalQualification.getTecnicalQulificationId().toString()%>" />
		<select class="selectwk textmxwidth2" name="skillId" id="skillId" onchange="populateGrades();">
		<option value='0' selected="selected"><bean:message key="Choose"/></option>
		<%
		Map skillMap =(Map)session.getAttribute("skillMap");
		for (Iterator it = skillMap.entrySet().iterator(); it.hasNext(); )
		{
		Map.Entry entry = (Map.Entry) it.next();

		%>
		<option  value = "<%= entry.getKey().toString() %>"<%=(((Integer)entry.getKey()).intValue() == (egpimsTecnicalQualification.getSkillMaster()==null?0:egpimsTecnicalQualification.getSkillMaster().getId().intValue())? "selected":"")%>><%= entry.getValue() %></option>
		<%
		}

		%>
		</select></td>
		<td class="whitebox3wk" >
		<egovtags:ajaxdropdown id="gradeId1" fields="['Text','Value']" dropdownId="gradeId1" url="common/employeeSearch!getGradesBySkill.action"/>
				<html:select property="gradeId1"  styleId="gradeId1" styleClass="selectwk">
					<html:option value="-1">---choose---</html:option>
				
			
		</html:select></td>
			
		<td class="whitebox3wk"> 
		<input type="text"  class="selectwk grey" name="yearOfPassTQ"  id="yearOfPassTQ" onBlur = "validateDateFormat(this);validateDateJS(this);checkSkills(this)" value="<%=egpimsTecnicalQualification.getPassedDate()==null?"":sdf.format(egpimsTecnicalQualification.getPassedDate())%>" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" size="10" >
		<a href="javascript:show_calendar('pIMSForm.yearOfPassTQ');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="<c:url value='../common/image/calendar.png' />" border=0></a>
		</td>
		<td class="whitebox3wk" >
		<input type="text"   class="selectwk textmxwidth2" name="remarks" id="remarks"  value="<%=egpimsTecnicalQualification.getRemarks()==null?"":egpimsTecnicalQualification.getRemarks()%>"  onBlur = "checkSkills(this)" >
		<input type="hidden"  class="selectwk textmxwidth2" name="techDocNo" id="techDocNo" value="<%=egpimsTecnicalQualification.getDocNo()==null?"":egpimsTecnicalQualification.getDocNo().toString()%>">
		</td>
				
		<%if(((String)(session.getAttribute("viewMode"))).trim().equals("view")){%>
		
		 
			<td>
				<egovtags:docFiles moduleName="EIS" documentNumber='<%=egpimsTecnicalQualification.getDocNo()==null?"":egpimsTecnicalQualification.getDocNo().toString()%>' id='<%="EmpTechnical"+i%>' showOnLoad="true"/>
			</td>
		
		
		<%
		}
		else{
			if(egpimsTecnicalQualification.getDocNo()==null){
				%>
				<td>
					<div class="buttonholderrnew">
						<a href="#"><img src="<%=request.getContextPath()%>/images/page_text.gif" title="Add" alt="Add" width="16" height="16" border="0" onclick="uploadTechDoc(this);return false;" /></a>
					</div>
				</td>
		<%
			}else
				{
				if(!((String)(session.getAttribute("viewMode"))).trim().equals("view")){
						%>
						<td>
							<egovtags:docFiles moduleName="EIS" documentNumber='<%=egpimsTecnicalQualification.getDocNo()==null?"":egpimsTecnicalQualification.getDocNo().toString()%>' id='<%="EmpTechnical"+i%>' showOnLoad="true"/>
							<div class="buttonholderrnew">
								<a href="#"><img src="<%=request.getContextPath()%>/images/page_text.gif" title="Edit" alt="Edit" width="16" height="16" border="0" onclick="uploadTechDoc(this);return false;" /></a>
							</div>
						</td>
				<%
				}
				}

		}
		if(!((String)(session.getAttribute("viewMode"))).trim().equals("view")){
		%>
		<td class="labelcell">
			
			<a href="#"><img src="<%=request.getContextPath()%>/common/image/add.png" alt="Add" width="16" height="16" border="0" onclick="whichButtonService('TeckDetails',this,'TeckDetailsnameRow');"/></a>
			<a href="#"><img src="<%=request.getContextPath()%>/common/image/cancel.png" alt="Delete" width="16" height="16" border="0" onclick="deleteServiceRow('TeckDetails',this);" /></a>
		</td>
		
		<%}}
		%>
		</tr></tbody>
		</table>

      <br>
		<table align='center' class="tableStyle" cellpadding ="0" cellspacing ="0" border = "0"  name ="DepnameTable" id ="DepnameTable" WIDTH=95% style="border: 1px solid #D7E5F2">
		<tbody>

			<tr>
                <td colspan="3" class="headingwk"><div class="arrowiconwk"><img src="<%=request.getContextPath()%>/common/image/arrow.gif" /></div>

                  <div class="headplacer"><bean:message key="DeparmentalTest"/></div></td>
              </tr>


		<tr>
		
		<td  width="79%"  class="tablesubheadwk"  ><bean:message key="TestNme"/></td>
		<td   width="16%" class="tablesubheadwk"  ><bean:message key="PassDate"/></td>
		<%
		if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
		{
		%>
		 <td width="5%" class="tablesubheadwk">Add/Dell</td>
		 <%}%>

		</tr>
		<%
		Iterator itrdept = deptSet.iterator();
		while (itrdept.hasNext()){
		DeptTests egpimsDeptTests = (DeptTests)itrdept.next();
		%>
		<tr id="DepnameRow">
		
		<input type = "hidden" name="deptTestsId" id="deptTestsId" value="<%=egpimsDeptTests.getDeptTestsId()==null?"0":egpimsDeptTests.getDeptTestsId().toString()%>" />
		<td class="whitebox3wk"  >
		<select class="selectwk textmxwidth2" name="nameOfTheTestId" id="nameOfTheTestId"  >
		<option value='0' selected="selected"><bean:message key="Choose"/></option>
		<%
		Map testNameMasterMap =(Map)session.getAttribute("testNameMasterMap");
		for (Iterator it = testNameMasterMap.entrySet().iterator(); it.hasNext(); )
		{
		Map.Entry entry = (Map.Entry) it.next();

		%>
		<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).intValue() == (egpimsDeptTests.getNameOfTestMstr()==null?0:egpimsDeptTests.getNameOfTestMstr().getId().intValue()))? "selected":"")%>><%= entry.getValue() %></option>

		<%
		}
		%>
		</select></td>
		<td class="whitebox3wk" >
		<input type="text"  class="selectwk grey"   class="fieldcell" name="monthandYearOfPassDT" id="monthandYearOfPassDT" onBlur = "validateDateFormat(this);validateDateJS(this);checknameOfTheTestId(this)" value="<%=egpimsDeptTests.getDateOfPass()==null?"":sdf.format(egpimsDeptTests.getDateOfPass())%>" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" size="32" >
		<a href="javascript:show_calendar('pIMSForm.monthandYearOfPassDT');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="<c:url value='../common/image/calendar.png' />" border=0></a>
		</td>
		<%if(!((String)(session.getAttribute("viewMode"))).trim().equals("view")){%>
		<td class="labelcell">
			<a href="#"><img src="<%=request.getContextPath()%>/common/image/add.png" alt="Add" width="16" height="16" border="0" onclick="whichButtonService('DepnameTable',this,'DepnameRow');"/></a>
			<a href="#"><img src="<%=request.getContextPath()%>/common/image/cancel.png" alt="Delete" width="16" height="16" border="0" onclick="deleteServiceRow('DepnameTable',this);" /></a>
		</td>
		
		<%
		} 
		}
		%>
		</tr>
		</table>

		
		 <br>

		<table  name = "immpropDet" align='center'  cellpadding ="0" class="tableStyle" cellspacing ="0" border = "0" id = "immpropDet" WIDTH=95% colspan="5" >
		<tbody>
		
		

		<tr>
                <td colspan="8" class="headingwk"><div class="arrowiconwk"><img src="<%=request.getContextPath()%>/common/image/arrow.gif" /></div>
                  <div class="headplacer"><bean:message key="ImMovablePropDetails"/></div></td>

              </tr>

		</table>
     
		<table  WIDTH=95% align='center' colspan="5" cellpadding ="0" class="tableStyle" cellspacing ="0" border = "0" id="PDImmnameTable" name="PDImmnameTable" style="border: 1px solid #D7E5F2">
		<tbody>
		<tr>
		<td   width="13%" class="tablesubheadwk" ><bean:message key="PropDesc"/></td>
		<td  width="18%" class="tablesubheadwk" ><bean:message key="Place"/></td>
		<td   width="30%" class="tablesubheadwk" ><bean:message key="HowAcq"/></td>
		<td   width="12%" class="tablesubheadwk" ><bean:message key="PresentValue"/></td>
		<td   width="7%" class="tablesubheadwk" ><bean:message key="PermissionObtained"/></td>
		<td   width="7%" class="tablesubheadwk" ><bean:message key="OrderNo"/></td>
		<td   width="8%" class="tablesubheadwk" ><bean:message key="OrderDate"/></td>
		<%
		if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
		{
		%>
		<td width="5%" class="tablesubheadwk">Add/Dell</td>
		<%}%>
		</tr>
		<%
		Iterator itrimm = ImmovablePropDetailsSet.iterator();
		while(itrimm.hasNext())
		{
		ImmovablePropDetails egpimsImmovablePropDetails = (ImmovablePropDetails)itrimm.next();
		%>
		<tr id="PDImmnameRow">
		<input type = hidden name="immPropertyDetailsId" id="immPropertyDetailsId" value="<%=egpimsImmovablePropDetails.getImmPropertyDetailsId()==null?"0":egpimsImmovablePropDetails.getImmPropertyDetailsId().toString()%>" />
		<td class="whitebox3wk">
		<input type="text"   id="propertydiscriptionImm" class="selectwk textmxwidth2" name="propertydiscriptionImm" value="<%=egpimsImmovablePropDetails.getPropertyDescription()==null?"":egpimsImmovablePropDetails.getPropertyDescription()%>" ></td>
		<td class="whitebox3wk">
		<input type="text"   class="selectwk textmxwidth2" name="placeImm"  id="placeImm"  onBlur =  "checkpropertydiscriptionImm(this);" value="<%=egpimsImmovablePropDetails.getPlace()==null?"":egpimsImmovablePropDetails.getPlace()%>"></td>
		<td class="whitebox3wk">
		<select class="selectwk textmxwidth2"   name="howAcquiredImm" id="howAcquiredImm" >
		<option value='0' selected="selected" ><bean:message key="Choose"/></option>
		<%
		Map howAcquiredMasterMap =(Map)session.getAttribute("howAcquiredMasterMap");
		for (Iterator it = howAcquiredMasterMap.entrySet().iterator(); it.hasNext(); )
		{
		Map.Entry entry = (Map.Entry) it.next();
		%>
		<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).intValue() == (egpimsImmovablePropDetails.getHowAcquiredMstr()==null?0:egpimsImmovablePropDetails.getHowAcquiredMstr().getId().intValue()))? "selected":"")%>><%= entry.getValue() %></option>
		<%
		}
		%>
		</select></td>

		<td class="whitebox3wk">
		<input type="text"  class="selectamountwk"  id="presentValueImm" name="presentValueImm" value="<%=egpimsImmovablePropDetails.getPresentValue()==null?"":egpimsImmovablePropDetails.getPresentValue().toString()%>" onBlur = "checkNumber(this);checkpropertydiscriptionImm(this)"></td>
		<td class="whitebox3wk">
		<input type="text"   class="selectwk textmxwidth2" id="permissionObtainedImm" name="permissionObtainedImm" onBlur =  "checkpropertydiscriptionImm(this);checkPerObtained(this)"  value="<%=egpimsImmovablePropDetails.getPermissionObtained().toString().equals("0")?"N":"Y"%>"></td>
		<td class="whitebox3wk">
		<input type="text"   class="selectwk textmxwidth2" id="orderNoImm" name="orderNoImm" value="<%=egpimsImmovablePropDetails.getOrderNo()==null?"":egpimsImmovablePropDetails.getOrderNo()%>" onBlur =  "checkpropertydiscriptionImm(this);"  ></td>
		<td class="whitebox3wk">
		<input type="text"   class="selectwk textmxwidth2"  id="dateImm" name="dateImm" value="<%=egpimsImmovablePropDetails.getOrderDate()==null?"":sdf.format(egpimsImmovablePropDetails.getOrderDate())%>" onBlur = "validateDateFormat(this);validateDateJS(this);checkpropertydiscriptionImm(this)" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')">
		<a href="javascript:show_calendar('pIMSForm.dateImm');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="<c:url value='../common/image/calendar.png' />" border=0></a></td>
		<%if(!((String)(session.getAttribute("viewMode"))).trim().equals("view")) {%>
		 <td class="labelcell">>
			<a href="#"><img src="<%=request.getContextPath()%>/common/image/add.png" alt="Add" width="16" height="16" border="0" onclick="whichButtonService('PDImmnameTable',this,'PDImmnameRow');"/></a>
			<a href="#"><img src="<%=request.getContextPath()%>/common/image/cancel.png" alt="Delete" width="16" height="16" border="0" onclick="deleteServiceRow('PDImmnameTable',this);" /></a>
		</td>
		
		

		<%
		}}
		%>
		</tr>
		</tbody>
		</table>
		<br>
		<table  id="movableProperty" WIDTH=95% align='center' class="tableStyle" cellpadding ="0" cellspacing ="0" border = "0" name="movableProperty"  colspan="5" style="border: 1px solid #D7E5F2" >
		
			<tr>
                <td colspan="8" class="headingwk"><div class="arrowiconwk"><img src="<%=request.getContextPath()%>/common/image/arrow.gif" /></div>
                  <div class="headplacer"><bean:message key="MovablePropDetails"/></div></td>

              </tr>


		</table>
        
		<table  WIDTH=95%  cellpadding ="0" align='center' class="tableStyle" cellspacing ="0" border = "0" colspan="5" id="PDMovnameTable" name="PDMovnameTable" style="border: 1px solid #D7E5F2">
		<tbody>
		<tr>

		<td   width="14%" class="tablesubheadwk" ><bean:message key="PropDesc"/></td>
		<td  width="14%" class="tablesubheadwk" width="148" ><bean:message key="TimeOfPur"/></td>
		<td  width="24%" class="tablesubheadwk" width="145" ><bean:message key="HowAcq"/></td>
		<td  width="25%" class="tablesubheadwk" ><bean:message key="PermissionObtained"/></td>
		<td  width="9%" class="tablesubheadwk" ><bean:message key="OrderNo"/> </td>
		<td  width="9%" class="tablesubheadwk" ><bean:message key="OrderDate"/></td>
		<%
		if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
		{
		%>
		<td width="5%" class="tablesubheadwk">Add/Dell</td>
		<%}%>

		</tr>
		<%
		Iterator itrmov = movablePropDetailsSet.iterator();
		for(int i=0;itrmov.hasNext();i++)
		{
		MovablePropDetails egpimsMovablePropDetails = (MovablePropDetails)itrmov.next();

		%>
		<tr id="PDMovnameRow">
		<input type = hidden name="movPropertyDetailsId" id="movPropertyDetailsId" value="<%=egpimsMovablePropDetails.getMovPropertyDetailsId()==null?"0":egpimsMovablePropDetails.getMovPropertyDetailsId().toString()%>" />

		<td class="whitebox3wk">
		<input type="text"   class="selectwk textmxwidth2" name="propertydiscriptionMo" id="propertydiscriptionMo" value="<%=egpimsMovablePropDetails.getPropertyDiscription()==null?"":egpimsMovablePropDetails.getPropertyDiscription()%>" onBlur = "checkpropertydiscriptionMo(this);" ></td>

		<td class="whitebox3wk">
		<input type="text"  class="selectamountwk" name="valueOfTimeOfPurchaseMo" id="valueOfTimeOfPurchaseMo" value="<%=egpimsMovablePropDetails.getValAtPurchase()==null?"":egpimsMovablePropDetails.getValAtPurchase().toString()%>" onBlur = "checkNumber(this);checkpropertydiscriptionMo(this)" ></td>

		<td class="whitebox3wk">
		<select  class="labelcell" name="howAcquiredMov" id="howAcquiredMov"  >
		<option value='0' class="selectwk textmxwidth2"  selected="selected"><bean:message key="Choose"/></option>
		<%
		Map howAcquiredMasterMapmo =(Map)session.getAttribute("howAcquiredMasterMap");
		for (Iterator it = howAcquiredMasterMapmo.entrySet().iterator(); it.hasNext(); )
		{
		Map.Entry entry = (Map.Entry) it.next();

		%>
		<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).intValue() == (egpimsMovablePropDetails.getHowAcquiredMstr()==null?0:egpimsMovablePropDetails.getHowAcquiredMstr().getId().intValue()))? "selected":"")%>><%= entry.getValue() %></option>

		<%
		}
		%>
		</select></td>

		<td class="whitebox3wk">
		<input type="text"   class="selectwk textmxwidth2" name="permissionObtainedMo" id="permissionObtainedMo"  onBlur = "checkpropertydiscriptionMo(this);checkPerObtained(this)" value="<%=egpimsMovablePropDetails.getPermissionObtained().toString().equals("0")?"N":"Y"%>"></td>
		<td class="whitebox3wk">
		<input type="text"   class="selectwk textmxwidth2" value="<%=egpimsMovablePropDetails.getOrderNo()==null?"":egpimsMovablePropDetails.getOrderNo()%>" name="orderNoMo" id="orderNoMo" onBlur = "checkpropertydiscriptionMo(this);"  ></td>
		<td class="whitebox3wk">
		<input   type="text"  class="selectwk grey" name="dateMo" id="dateMo" onBlur = "validateDateFormat(this);validateDateJS(this);checkpropertydiscriptionMo(this)"  value="<%=egpimsMovablePropDetails.getOrderDate()==null?"":sdf.format(egpimsMovablePropDetails.getOrderDate())%>" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" >
		<a href="javascript:show_calendar('pIMSForm.dateMo');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="<c:url value='../common/image/calendar.png' />" border=0></a>
		</td>
		<%if(!((String)(session.getAttribute("viewMode"))).trim().equals("view")) {%>
		 <td class="labelcell">
			<a href="#"><img src="<%=request.getContextPath()%>/common/image/add.png" alt="Add" width="16" height="16" border="0" onclick="whichButtonService('PDMovnameTable',this,'PDMovnameRow');"/></a>
			<a href="#"><img src="<%=request.getContextPath()%>/common/image/cancel.png" alt="Delete" width="16" height="16" border="0" onclick="deleteServiceRow('PDMovnameTable',this);" /></a>
		</td>
		
		
		<%
		}}//for
		%>
		</tr>
		</tbody>
		</table>
		
		