
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-nested" prefix="nested"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="java.util.*,
		 org.egov.infstr.utils.*,
 		 org.apache.log4j.Logger,
 		 org.egov.pims.*,
 		 org.egov.pims.utils.*,
 		 org.egov.pims.empLeave.client.*,
 		 org.egov.pims.empLeave.model.*,
 		 org.egov.pims.empLeave.service.*,
 		 org.egov.commons.CFinancialYear,
 		 org.egov.infstr.commons.*,
 		 org.egov.pims.commons.client.*,
 		 org.egov.infstr.commons.dao.*,
 		 org.egov.infstr.commons.service.*,
 		 java.text.SimpleDateFormat,
 		 org.egov.pims.client.*"
%>

<%!
		List holidayMasterSet = null;
		CFinancialYear	financial =null;
		CalendarYear calYear=null;

%>
<%
        String  currentfinYear = EisManagersUtill.getCommonsService().getCurrYearFiscalId();
        CFinancialYear financialY =EisManagersUtill.getCommonsService().findFinancialYearById(new Long(currentfinYear));
        List fYMasterList=EisManagersUtill.getCommonsService().getAllFinancialYearList();

        Map finMap = getFinMap(fYMasterList);
   		Set delHolidays = new HashSet();	
   		session.setAttribute("delHolidays", delHolidays);

           %>
<%!
		public Map getFinMap(List list)
		{
			Map finMap = new HashMap();
			for(Iterator iter = list.iterator();iter.hasNext();)
			{
				CFinancialYear cFinancialYear = (CFinancialYear)iter.next();
				finMap.put(cFinancialYear.getId(), cFinancialYear.getFinYearRange());
			}
			return finMap;
		}
%>


<html>
	<head>
	    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	    <title><bean:message key="HolidayMaster"/></title>
	
	
	    <SCRIPT type="text/javascript" src="${pageContext.request.contextPath}/javascript/Employeevalidation.js" type="text/javascript"></SCRIPT>
	    <SCRIPT type="text/javascript" src="${pageContext.request.contextPath}/javascript/dateValidation.js" type="text/javascript"></SCRIPT>
		<script type="text/JavaScript" src="${pageContext.request.contextPath}/commonjs/calender.js"></script>
	    <script language="text/JavaScript" src="${pageContext.request.contextPath}/Admin-Homepage_files/dhtml.js" type="text/JavaScript"></script>


<script>
	function goindex(arg)
	{
		if(arg == "Index")
		{
			document.forms[0].action = "${pageContext.request.contextPath}/staff/index.jsp";
			document.forms[0].submit();
		}
	}
	
	function addRow(obj,tableName,row)
	{
    	var tbl=tableName;
    	var rowO=tbl.rows.length;
    	var name=obj.alt;
        var tname = "resetRowValues"+name;

       	if(row != null)
       	{
       		var tbody=tbl.tBodies[0];
       		rIndex = 0;
   			var lastRow = tbl.rows.length;
			
   			var rowObj = row.cloneNode(true);
			tbody.appendChild(rowObj);
			
   			getControlInBranch(tbl.rows[tbl.rows.length-1],'deltp').id=tbl.rows.length-1;
   			getControlInBranch(tbl.rows[tbl.rows.length-1],'holidayId').value=0;
   			rIndex = rowObj.rowIndex-1;
  			resetRowValues(lastRow,name);
		}
	}
	function addRowHoliday(tableName,row,id,date,name)
	{

    	var tbl=tableName;
    	var rowO=tbl.rows.length;
       	if(row != null)
       	{
       		var tbody=tbl.tBodies[0];
       		var lastRow = tbl.rows.length;
   			var rowObj = row.cloneNode(true);
   			rowObj.document.getElementById('holidayId').value =id;
   			rowObj.document.getElementById('holidayDate').value =date;
   			rowObj.document.getElementById('holidayName').value =name;

   			tbody.appendChild(rowObj);
		}
	 }

	  function deleteRow(obj,tableName)
	  {
	  	var tbl = document.getElementById("TPTable");
		var rowNumber=getRow(obj).rowIndex;	
		var rowObj = getRow(obj);
		
		if(rowNumber > 1)
		{
			var holidayId =	getControlInBranch(tbl.rows[rowObj.rowIndex],'holidayId').value;
            if(holidayId!=null)
            populateDeleteSet("delHolidays" , holidayId);
		    tbl.deleteRow(rowNumber);
		}
		else
		{
		 	alert('<bean:message key="alertRowCannotDel"/>');
 			return false
		}
	 }
      
	function populateDeleteSet(setName, delId)
	{
		var http = initiateRequest();   
		var url = "${pageContext.request.contextPath}/leave/updateDelSets.jsp?type="+setName+"&id="+delId;		      
		http.open("GET", url, true);
		http.onreadystatechange = function()
		{
			if (http.readyState == 4) 
			{
				if (http.status == 200) 
				{
				       var statusString =http.responseText.split("^");
	
				       
				 } 
			} 
		};
		http.send(null);   
	}


	/**
	*	This method is used delete all cells within that row
	*/
	function deleteRows(rowObjArray)
	{
		for (var i=1; i<rowObjArray.length+1; i++) {
			var rIndex = rowObjArray[i].sectionRowIndex;
			rowObjArray[i].parentNode.deleteRow(rIndex);
		}
	}


	var firstLength = 0;
	var rIndex = 0;
	
	function setLength()
  	{
	 	if(document.getElementById('TPTable') != null)
		{
			var tbl=document.getElementById('TPTable');
			var rowo=tbl.rows.length;
			firstLength = rowo;
		}
  	}
  	
  	function ButtonPressNew(arg)
  	{
		if(arg == "savenew")
  		{
  			var tbl= document.getElementById("TPTable");
  			var rows= tbl.rows.length;

  			if(rows>2)
  			{
  				for(var i=0 ;i<rows-1;i++)
  				{
  					if(document.holidayForm.holidayDate[i].value=="" )
  					{
  						alert('<bean:message key="alertEnterDt"/>');
  						document.holidayForm.holidayDate[i].focus();
  						return false;
  					}
  					if(document.holidayForm.holidayName[i].value=="" )
  					{
  						alert('<bean:message key="alertEnterHolidayNme"/>');
  						document.holidayForm.holidayName[i].focus();
  						return false;
  					}
  				}
  			}
  			else
  			{
  				if(document.holidayForm.holidayDate.value=="" )
  				{
  					alert('<bean:message key="alertChooseDt"/>');
  					document.holidayForm.holidayDate.focus();
  					return false;
  				}
  				if(document.holidayForm.holidayName.value=="" )
  				{
  					alert('<bean:message key="alertEnterHolidayNme"/>');
  					document.holidayForm.holidayName.focus();
  					return false;
  				}
  			}

  			document.holidayForm.action = "${pageContext.request.contextPath}/leave/AfterHolidaysMasterAction.do?submitType=saveOrUpdateHolidays";
  			document.holidayForm.submit();
  		}

  	}
	
	var length =0;
	
	function resetRowValues(lastRow,name)
	{
		if(name=="Add")
		{
			document.holidayForm.holidayDate[lastRow-1].value="";
			document.holidayForm.holidayName[lastRow-1].value="";
		}
		length =lastRow;
	}

	function changeFy()
	{	
		<%
			
			EmpLeaveServiceImpl empLeaveServiceImpl=new EmpLeaveServiceImpl();
			String finYear = "";
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat sqlUtil = new SimpleDateFormat("dd-MMM-yyyy");
			Map mp = getFinMap(EisManagersUtill.getCommonsService().getAllFinancialYearList());
			finYear = request.getParameter("financialId");
			if(finYear != null)
			{			
			    if(EisManagersUtill.getEmpLeaveService().isLeaveCalendarBased())
				{
					calYear=EisManagersUtill.getEmpLeaveService().getCalendarYearById(new Long(finYear));
					holidayMasterSet = 	empLeaveServiceImpl.getHolidayListByCalendarYearID(calYear);
				}
				else
				{
					financial =EisManagersUtill.getCommonsService().findFinancialYearById(new Long(finYear));
					holidayMasterSet = 	empLeaveServiceImpl.getHolidaysUlbsFotFinalsialYearId(financial);
				}
			}
		%>
	}

	function checkDateForOutOfRange(obj)
	{
		if(obj!=null)
		{
			var finId = document.getElementById('financialId').value;
			var http = initiateRequest();
			var url = "${pageContext.request.contextPath}/leave/checkDateAjax.jsp?date="+obj.value+"&finId="+finId;
			http.open("GET", url, true);
			http.onreadystatechange = function()
			{
				if (http.readyState == 4)
				{
					if (http.status == 200)
					{
					       var statusString = "";
					        statusString =http.responseText.split("^");	
							
					       if(statusString[0] == "false")
					       {
					       		alert('<bean:message key="alertOutOfRange"/>');
					       		obj.value = "";
								obj.focus();
					       		return false;
	
					       	}
					 }
				}
			};
			http.send(null);
		}
	}

	function disableFields()
	{
		<%
		   if(((String)(session.getAttribute("viewMode"))).trim().equals("view"))
		   {
	   	%>
	   			var tbl= document.getElementById("TPTable");
				var len= tbl.rows.length;
	
				if(len == 2)
				{
					document.forms[0].holidayDate.disabled=true;
					document.forms[0].holidayName.readOnly=true;
				}
				else
				{
					for(var i=0 ;i<len;i++)
					{
						document.forms[0].holidayDate[i].disabled=true;
						document.forms[0].holidayName[i].readOnly=true;
					}
				}
	   	<%
	   		}
	   	%>
	}

	function chkDublicate(obj)
	{
		if(obj.value!="")
		{
		    var tbl = document.getElementById('TPTable');
			var len = tbl.rows.length;
			
			var rowObj = getRow(obj);
			var holidayDate =obj.value;	
			if(len > 2){
				for(var i=0; i<len-2; i++){
					if(rowObj.rowIndex != i){	
						if(holidayDate == document.holidayForm.holidayDate[i].value){
							
							alert('<bean:message key="alertdateEntered"/>');
							obj.value="";
							obj.focus();
							break;
						}
					}
				}
			}
		}
	}
</script>
	
	</head>
<!-- Header Section Begins -->

<!-- Header Section Ends -->
<body onLoad = "setLength();changeFy();disableFields();"/>

	<html:form  action="/leave/AfterHolidaysMasterAction.do?submitType=saveOrUpdateHolidays" >
 		<div class="navibarshadowwk"></div>
		<div class="formmainbox">
			<div class="insidecontent">
				<div class="rbroundbox2">
					<div class="rbtop2">
						<div></div>
					</div>
					<div class="rbcontent2">

						<table width ="95%" cellspacing="0" border="0" cellpadding ="0">
							<tr>
								<td>
									<table  width ="95%"  cellpadding ="0" cellspacing ="0" border = "0" >
										<tbody>
											<tr>
												<td  class="headingwk" colspan="7">
													<div class="arrowiconwk"><img src="<%=request.getContextPath()%>/common/image/arrow.gif" /></div>
													<div class="headplacer"><bean:message key="Calender"/></div>
												</td>
											</tr>

											<% 
											if(EisManagersUtill.getEmpLeaveService().isLeaveCalendarBased())
											{
											%>

											<tr>
												<td  class="whiteboxwk" />
												<td class="whitebox2wk">
													<b>For Calendar Year &nbsp;<%=calYear!=null?(calYear.getCalendarYear()!=null?calYear.getCalendarYear():""):""%></b>
												</td>
												</td>
											</tr>

											<%
											}else{
											%>

											<tr>
												<td  class="greyboxwk"/>
												<td class="greybox2wk">
													<b><bean:message key="forFinacialYr"/>&nbsp;<%=financial!=null?(financial.getFinYearRange()!=null?financial.getFinYearRange():""):""%></b>
												</td>
												</td>
											</tr>
											<%
											}
											%>
										</tbody>
									</table>

										<br>

 										<table  width="95%"  cellpadding ="0" cellspacing ="0" border = "0" id="TPTable" name="TPTable" >
	  									<tbody>
	   										<tr>
										   		<td  class="whiteboxwk"  />
										   		<td  class="whitebox2wk" colspan="7" >
										   			<SPAN class="mandatory">*</SPAN><bean:message key="HolidayDate"/>
										   		</td>
										   		<td   class="whitebox2wk" colspan="7" >
										   			<SPAN class="mandatory">*</SPAN><bean:message key="HolidayNme"/>
										   		</td>
										   		<td>&nbsp;</td>
										   		</td>
										  	</tr>
										 	<%
								 			if(holidayMasterSet==null || holidayMasterSet.isEmpty())
											{
												holidayMasterSet.add(new HolidaysUlb());
											}
											Iterator itr = holidayMasterSet.iterator();
						
											for(int i=0;itr.hasNext();i++)
											{
												 HolidaysUlb holidaysUlb = ( HolidaysUlb)itr.next();
												 
											%>
													
												<tr id="TPRow">
													<td  class="greyboxwk">
														<input type = "hidden" name="financialId" id="financialId" value ="<%=finYear%>"/>
														<input type = "hidden" name="holidayId" id="holidayId"
																value = "<%=holidaysUlb.getId()==null?"0":holidaysUlb.getId().toString()%>"/>
													</td>
													<td class="greybox2wk" colspan="7">
														<input type="text" id="holidayDate" 
															name="holidayDate"  onBlur = "validateDateFormat(this);chkDublicate(this);checkDateForOutOfRange(this);" 
															size="10" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" 
															value = "<%=holidaysUlb.getHolidayDate()==null?"":sdf.format(holidaysUlb.getHolidayDate())%>"  />
															<!--a href="javascript:show_calendar('holidayForm.holidayDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;">
																<img id="holidayDateImg" src="${pageContext.request.contextPath}/common/image/calendar.png" width="15" height="15" border="0">
															</a-->
													</td>
													<td class="greybox2wk" colspan="7" >
														<input type="text" id="holidayName" name="holidayName" size="10" 
															value = "<%=holidaysUlb.getHolidayName()==null?"":holidaysUlb.getHolidayName()%>" >
													</td>
													<%
														if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
													   	{
								   					%>
															<td class="greybox2wk" colspan="7" >
																<div align="center">
																	<a href="#">
																		<img src="../common/image/add.png"  alt="Add" width="16" height="16" border="0"
														      				onclick="javascript:addRow(this,document.getElementById('TPTable'),document.getElementById('TPRow'))"/></a>
																	<a href="#">
																		<img src="../common/image/cancel.png"  name="deltp" alt="Del" width="16" height="16" border="0"
																			onclick="deleteRow(this,document.getElementById('TPTable'))"/></a>
																</div>
															</td>					
												  	<%
												   		}else
												   		{
							   						%>
							   								<td>&nbsp;</td>
							   						<%
							   							}
							   						%>
																	
												</tr>
										<%
										   	 }
										%>
										</tbody>
									</table>

									<br>

									<table id = "submit" width="95%"  cellpadding ="0" cellspacing ="0" border = "0" value = "submit">
										<tr>
										</tr> 

										<tr>
           									<td><div align="right" class="mandatory">* Mandatory Fields</div></td>
         								</tr>
									</table>
								</td>
							</tr>
						</table>
					</div>
					<div class="rbbot2">
						<div></div>
					</div>
				</div>
			</div>
		</div>

		<div class="buttonholderwk">
		<%
			if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
			{
			%>
				<td align="center"><html:button styleClass="buttonfinal" value="Save" property="b2" onclick="ButtonPressNew('savenew');" /></td>
		 	<%
		 	}
		 	%>
				<input type="button" name="button" id="button" value="CLOSE"  class="buttonfinal" onclick="window.close()"/>
		</div>
 	</html:form>
</body>
</html>
