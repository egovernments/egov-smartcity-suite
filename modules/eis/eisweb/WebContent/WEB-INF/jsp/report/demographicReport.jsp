<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>

<html>

<head>
	<title>Employee Demographic Report </title>  
<SCRIPT type="text/javascript">		
		function setDateToCurrentDate()
		{		
			if(document.getElementById("givenDate").value=="")
				document.getElementById("givenDate").value="<%=(new java.text.SimpleDateFormat("dd/MM/yyyy")).format(new java.util.Date())%>";
		}
		
		function checkOnSubmit()
		{			
			if(document.getElementById("givenDate").value=="")
			{
				alert("Please enter date");
				document.getElementById("givenDate").focus();
				return false;
			}
			else
			{
				var isValid = valCurrentDate(document.getElementById("givenDate").value);
				//alert("isValid="+isValid);
				if(!isValid)
				{
					alert("Date must be less than current date");
					document.getElementById("givenDate").focus();
					return false;
				}
				else
					return true;
			}						
				
		}	
		
		
		</SCRIPT>
</head>

    <body onload="setDateToCurrentDate()">
<s:form name="demographicReportForm" action ="demographicReport"  theme="simple" onsubmit="return checkOnSubmit()">

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
				<div class="headplacer">Employee Demographic Report</div>
			</td>
		</tr>
		<tr>
		<td>&nbsp;</td>
		</tr>

		<tr>
			
			<td class="whiteboxwk">Report By</font></td>
			<td class="whitebox2wk">
			<s:select  headerValue="Choose"  headerKey="-1"  
					list="#{'1':'Gender', '2':'Age', '3':'Religion', '4':'Community'}" 
					label="typeId" id="typeId" name="typeId" value="%{typeId}"/>
			</td>			
		
      				<td class="whiteboxwk" id="fromDateLabel"><span class="mandatory">*</span>As On Date (dd/MM/yyyy)</td>
				<td class="whitebox2wk" id="fromDateTxt">
				
				<s:date name="givenDate" var="givenDateValue" format="dd/MM/yyyy"/>
				<s:textfield name="givenDate" id="givenDate" value="%{givenDateValue}" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);"/>
				<a 	name="dateFromAnchor" id="dateFromAnchor" href="javascript:show_calendar('forms[0].givenDate');"	onmouseover="window.status='Date Picker';return true;" 
							onmouseout="window.status='';return true;"><img src="<%=request.getContextPath()%>/common/image/calendar.png"  border="0">
				</a>
				
				</td>
										          				
			</tr>	
		<tr>
		<td colspan="4">&nbsp;	</td>
		
		</tr>

		
		
		</table>
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

					<s:submit name="action" value="Submit" cssClass="buttonfinal" method="getDemoGraphicInfo" />
					<input type="button" name="closeBut" id="closeBut" value="Close" onclick="window.close();" class="buttonfinal"/> 
				</div>
		<s:if test="%{!genderList.isEmpty()}" >
					
				<table width="100%"  cellpadding="0" cellspacing="0" border="0" >
				<tr>
			  <td colspan="4" >
					<div class="tbl3-container" id="tbl-container">
					<display:table name="genderList" uid="currentRowObject1" pagesize = "10"  class="its" export="true" style="width: 750;" requestURI=""  >
						<display:caption style="text-align: center" class="headerbold">Employee Gender Wise Report</display:caption>
						<display:column    title="Gender" >
						<c:out value="${currentRowObject1[0]}"/>
						</display:column>
						
						<display:column     title="No. of Employees" >
						<c:out value="${currentRowObject1[1]}"/>
						</display:column>
						
						<display:column    title="Percentage">						
						<c:out value="${currentRowObject1[2]}"/>
						</display:column>
						
						
						<div STYLE="display:table-header-group;">									      
							<display:setProperty name="basic.show.header" value="true" />
							<display:setProperty name="basic.empty.showtable" value="true" />
							<display:setProperty name="basic.msg.empty_list_row">
							<tr class="empty"><td colspan="{0}">No records found.</td></tr> 																					  		
							</display:setProperty>
							<display:setProperty name="export.pdf.filename" value="EmployeeGenderwiseReport.pdf" />
							<display:setProperty name="export.excel.filename" value="EmployeeGenderwiseReport.xls"/>
							<display:setProperty name="export.csv" value="false"/>
							<display:setProperty name="export.xml" value="false"/>
						</div>
						
					</display:table >
				    </div>

			</td>
		    </tr>
		    </table>
		 </s:if>
		
		 <s:if test="%{!religionList.isEmpty()}" >
					
				<table width="100%"  cellpadding="0" cellspacing="0" border="0" >
				<tr>
			  <td colspan="4" >
					<div class="tbl3-container" id="tbl-container">
					<display:table name="religionList" uid="currentRowObject2" pagesize = "10"  class="its" export="true" style="width: 800;" requestURI=""  >
						<display:caption style="text-align: center" class="headerbold">Employee Religion Wise Report</display:caption>
						<display:column    title="Religion" >
						<c:out value="${currentRowObject2[0]}"/>
						</display:column>
						
						<display:column     title="No. of Employees" >
						<c:out value="${currentRowObject2[1]}"/>
						</display:column>
						
						<display:column    title="Percentage">						
						<c:out value="${currentRowObject2[2]}"/>
						</display:column>
						
						
						<div STYLE="display:table-header-group;">									      
							<display:setProperty name="basic.show.header" value="true" />
							<display:setProperty name="basic.empty.showtable" value="true" />
							<display:setProperty name="basic.msg.empty_list_row">
							<tr class="empty"><td colspan="{0}">No records found.</td></tr> 																					  		
							</display:setProperty>
							<display:setProperty name="export.pdf.filename" value="EmployeeReligionwiseReport.pdf" />
							<display:setProperty name="export.excel.filename" value="EmployeeReligionwiseReport.xls"/>
							<display:setProperty name="export.csv" value="false"/>
							<display:setProperty name="export.xml" value="false"/>
						</div>
						
					</display:table >
				    </div>

			</td>
		    </tr>
		    </table>
		 </s:if>
		<s:if test="%{!communityList.isEmpty()}" >
					
				<table width="100%"  cellpadding="0" cellspacing="0" border="0" >
				<tr>
			  <td colspan="4" >
					<div class="tbl3-container" id="tbl-container">
					<display:table name="communityList" uid="currentRowObject3" pagesize = "10"  class="its" export="true" style="width: 800;" requestURI=""  >
						<display:caption style="text-align: center" class="headerbold">Employee Community Wise Report</display:caption>
						<display:column    title="Community" >
						<c:out value="${currentRowObject3[0]}"/>
						</display:column>
						
						<display:column     title="No. of Employees" >
						<c:out value="${currentRowObject3[1]}"/>
						</display:column>
						
						<display:column    title="Percentage">						
						<c:out value="${currentRowObject3[2]}"/>
						</display:column>
						
						
						<div STYLE="display:table-header-group;">									      
							<display:setProperty name="basic.show.header" value="true" />
							<display:setProperty name="basic.empty.showtable" value="true" />
							<display:setProperty name="basic.msg.empty_list_row">
							<tr class="empty"><td colspan="{0}">No records found.</td></tr> 																					  		
							</display:setProperty>
							<display:setProperty name="export.pdf.filename" value="EmployeeCommunitywiseReport.pdf" />
							<display:setProperty name="export.excel.filename" value="EmployeeCommunitywiseReport.xls"/>
							<display:setProperty name="export.csv" value="false"/>
							<display:setProperty name="export.xml" value="false"/>
						</div>
						
					</display:table >
				    </div>

			</td>
		    </tr>
		    </table>
		 </s:if>
		 <s:if test="%{!ageRangeList.isEmpty()}" >
					
				<table width="100%"  cellpadding="0" cellspacing="0" border="0" >
				<tr>
			  <td colspan="4" >
					<div class="tbl3-container" id="tbl-container">
					<display:table name="ageRangeList" uid="currentRowObject4" pagesize = "10"  class="its" export="true" style="width: 800;" requestURI=""  >
						<display:caption style="text-align: center" class="headerbold">Employee Age Wise Report</display:caption>
						<display:column    title="Age" >
						<c:out value="${currentRowObject4[0]}"/>
						</display:column>
						
						<display:column     title="No. of Employees" >
						<c:out value="${currentRowObject4[1]}"/>
						</display:column>
						
						<display:column    title="Percentage">						
						<c:out value="${currentRowObject4[2]}"/>
						</display:column>
						
						
						<div STYLE="display:table-header-group;">									      
							<display:setProperty name="basic.show.header" value="true" />
							<display:setProperty name="basic.empty.showtable" value="true" />
							<display:setProperty name="basic.msg.empty_list_row">
							<tr class="empty"><td colspan="{0}">No records found....</td></tr> 																					  		
							</display:setProperty>
							<display:setProperty name="export.pdf.filename" value="EmployeeAgewiseReport.pdf" />
							<display:setProperty name="export.excel.filename" value="EmployeeAgewiseReport.xls"/>
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
