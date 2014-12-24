<%@ include file="/includes/taglibs.jsp" %>
<%@page import="org.egov.infstr.utils.DateUtils,java.util.Calendar,java.util.List,java.util.Date" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>

<html>

<head>
	<title>Grade Seniority Report </title>

<script language="JavaScript"  type="text/JavaScript">

function validateMandatory()
{
	if(document.getElementById("gradeId").value==-1)
	{
	alert("Please Select Grade");
	return false;
	}
	return true;
}
</script>   

</head>

<body>
<s:form name="gradeDurationForm" action ="gradeSeniorityReport"  theme="simple">

<div class="formmainbox">
			<div class="insidecontent">
		  		<div class="rbroundbox2">
					<div class="rbtop2">
						<div></div>
					</div>
					<div class="rbcontent2">

						<!-- Header Section Begins -->
						<!-- Header Section Ends -->
						<table width="90%" cellpadding ="0" cellspacing ="0" border = "0" id="table2" border="0">
							<tr>
								<td>
									<!-- Tab Navigation Begins -->
									<!-- Tab Navigation Ends -->
									<!-- Body Begins -->
									<!-- Body Begins -->
									<div>
<center>
			<table width="100%"  cellpadding="0" cellspacing="0" border="0" >
				<tr>
			<td colspan="4" class="headingwk">
				<div class="arrowiconwk">
					<img src="../common/image/arrow.gif" />
				</div>
				<div class="headplacer">Employee Grade Seniority Report</div>
			</td>
		</tr>
		<tr>
		<td>&nbsp;</td>
		</tr>

		<tr>
			
			<td class="whiteboxwk">Grade<font color="red">*</font></td>
			<td class="whitebox2wk">
			<s:select  headerValue="Choose"  headerKey="-1"  
					list="dropdownData.gradeList" listKey="id" listValue="name" 
					label="gradeId" id="gradeId" name="gradeId" value="%{gradeId}"/>
			</td>
			
			
			<td class="whiteboxwk" >Department</td>
			<td class="whitebox2wk">

				<s:select  headerValue="Choose"  headerKey="-1"  
					list="dropdownData.departmentList" listKey="id" listValue="deptName" 
					label="deptId" id="deptId" name="deptId" value="deptId"/>

			</td>	
			
		</tr>
		<tr>
		<td colspan="4">&nbsp;	</td>
		
		</tr>

		
		
		</table>
		<s:if test="%{resultEmpList!=null}" >
					
				<table width="100%"  cellpadding="0" cellspacing="0" border="0" >
				<tr>
			  <td colspan="4" >
					<div class="tbl3-container" id="tbl-container">
					<display:table name="resultEmpList" uid="currentRowObject" pagesize = "10"  class="its" export="true" style="width: 750;" requestURI=""  >
						<display:caption style="text-align: center" class="headerbold">Employee Grade Seniority Report</display:caption>
						<display:column    title="Employee Id" >
						<c:out value="${currentRowObject[0]}"/>
						</display:column>
						
						<display:column     title="Employee Name" >
						<c:out value="${currentRowObject[1]}"/>
						</display:column>
						
						<display:column    title="Duration">						
						<s:property value="%{getDateInWords(#attr.currentRowObject[2])}" />
						</display:column>
						
						
						<div STYLE="display:table-header-group;">	
						<display:setProperty name="paging.banner.placement" value="bottom" />								      
							<display:setProperty name="basic.show.header" value="true" />
							<display:setProperty name="basic.empty.showtable" value="true" />
							<display:setProperty name="basic.msg.empty_list_row">
							<tr class="empty"><td colspan="{0}">No records found.</td></tr> 																					  		
							</display:setProperty>
							<display:setProperty name="export.pdf.filename" value="GradeSeniority.pdf" />
							<display:setProperty name="export.excel.filename" value="GradeSeniority.xls"/>
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
		</div>
								</td>
							</tr>
							<tr><td>&nbsp;</td></tr>
							<tr>
           						<td><div align="right" class="mandatory">* Mandatory Fields</div></td>
         					</tr>
						</table>
						
			<div class="rbbot2"><div></div></div>
		</div>
</div></div>


				<div class="buttonholderwk" align="center">

					<s:submit name="action" value="Search" cssClass="buttonfinal" method="getGradeDurationInfo" onclick="return validateMandatory();"/>
				</div>
			

</s:form>
</body>
</html>
