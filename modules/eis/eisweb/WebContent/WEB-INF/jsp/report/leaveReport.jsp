<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%> 

<html>

<head>
	<title>Leave Collision Report</title>

<script language="JavaScript"  type="text/JavaScript">

	
</script>   

</head>

<body>

	
	<s:if test="%{hasErrors()}">
	    <div id="errorstyle" class="errorstyle" >
	      <s:actionerror/>
	      <s:fielderror/>
	    </div>
	</s:if>
	<s:if test="%{hasActionMessages()}">
	    <div class="messagestyle">
		<s:property value="%{estimateNumber}"/> &nbsp; <s:actionmessage theme="simple"/>

	    </div>
	</s:if>
					
	<s:form name="leaveReportForm" action ="leaveReport"  theme="simple">	
		<center>
			<table style="width: 810;" align="center" cellpadding="0" cellspacing="0" border="0" >
				<tr>
			<td colspan="5" class="headingwk">
				<div class="arrowiconwk">
					<img src="../common/image/arrow.gif" />
				</div>
				<div class="headplacer">Leave Collision Report</div>
			</td>
		</tr>

		<tr>
			
			<td class="whiteboxwk" >Department<font color="red">*</font></td>
			<td class="whitebox2wk">

				<s:select  headerValue="Choose"  headerKey="-1"  
					list="dropdownData.departmentList" listKey="id" listValue="deptName" 
					label="departmentId" id="departmentId" name="departmentId" />

			</td>	
			<td class="whiteboxwk"></td>
			<td class="whitebox2wk">
			</td>
		</tr>

		<tr>
			<td class="greyboxwk" >From Date <font color="red">*</font></td>
			<td class="greybox2wk">

				<s:textfield cssClass="selectwk grey"  name="fromDate" id="fromDate"  onblur="setBlur(this,'dd/mm/yyyy');" onfocus="setFocus(this,'dd/mm/yyyy')" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
				<a href="javascript:show_calendar('forms[0].fromDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img  src="${pageContext.request.contextPath}/common/image/calendar.png"  border=0></a>


			</td>	

			<td class="greyboxwk"> To Date <font color="red">*</font></td>
			<td class="greybox2wk">
				<s:textfield cssClass="selectwk grey"  name="toDate" id="toDate" onblur="setBlur(this,'dd/mm/yyyy');" onfocus="setFocus(this,'dd/mm/yyyy')" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
				<a href="javascript:show_calendar('forms[0].toDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img  src="${pageContext.request.contextPath}/common/image/calendar.png"  border=0></a>
			</td>
		    </tr>



		<tr>
			<td colspan="4">
				<div class="buttonholderwk" align="center">

					<s:submit name="action" value="Search" cssClass="buttonfinal" method="list" />
				</div>
			</td>
		</tr>
		</table>

		<s:if test="%{results != null}" >
				<table style="width: 810;" align="center" cellpadding="0" cellspacing="0" border="0" >
				<tr>
			  <td colspan="4" >
					<div class="tbl3-container" id="tbl-container">
					<display:table name="results" uid="currentRowObject" class="its" export="true" style="width:790px" requestURI=""  >

						<display:caption class="headerbold">Leave Collision Report from <fmt:formatDate pattern="dd/MM/yyyy" value="${fromDate}" /> to 
						<fmt:formatDate pattern="dd/MM/yyyy" value="${toDate}" /></display:caption>
						<display:column style="border:0;" title="Date" ><fmt:formatDate pattern="dd/MM/yyyy" value="${currentRowObject[0]}" /></display:column>
						<display:column style="border:0;" title="Code" ><c:out value="${currentRowObject[1].employeeId.employeeCode}" /></display:column>
						<display:column style="border:0;" title="Name" ><c:out value="${currentRowObject[1].employeeId.employeeName}" /></display:column>
						<display:column style="border:0;" title="Leave Type" ><c:out value="${currentRowObject[1].typeOfLeaveMstr.name}" /></display:column>
						<display:column style="border:0;" title="Leave Range"><fmt:formatDate pattern="dd/MM/yyyy" value="${currentRowObject[1].fromDate}" /> - 
						<fmt:formatDate pattern="dd/MM/yyyy" value="${currentRowObject[1].toDate}" /></display:column>
						<display:column style="border:0;" title="Status" ><c:out value="${currentRowObject[1].statusId.name}" /></display:column>											  

						
						<div STYLE="display:table-header-group;">			      
							<display:setProperty name="basic.show.header" value="true" />
							<display:setProperty name="basic.empty.showtable" value="true" />
							<display:setProperty name="basic.msg.empty_list_row">
							<tr class="empty"><td colspan="{0}">No records found.</td></tr> 																					  		
							</display:setProperty>
							<display:setProperty name="export.pdf.filename" value="LeaveCollision.pdf" />
							<display:setProperty name="export.excel.filename" value="LeaveCollision.xls"/>
							<display:setProperty name="export.csv" value="false"/>
							<display:setProperty name="export.xml" value="false"/>

						</div>
					</display:table >
				    </div>

			</td>
		    </tr>
		    </table>
		 </s:if>



	    </center>
	</s:form>
				
</body>
</html>