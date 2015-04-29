<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page import="java.util.*" %>

<html>
  <s:if test="%{reportType=='deptwise'}">
		<title><s:text name='page.title.estimateabstract.bydeptwise' /></title>
  </s:if>
  <s:else>
		<title><s:text name='page.title.estimateabstract.byworkwise' /></title>
  </s:else>
 
 <script type="text/javascript">

	function setupSubTypes(elem){
		categoryId=elem.options[elem.selectedIndex].value;
    	populatecategory({category:categoryId});
	}	

	function viewPDF(){
		var reportType=document.getElementById("reportType").value;
		var url="${pageContext.request.contextPath}/reports/estimateAbstractReport!viewPDF.action?reportType="+reportType;
		url=url+"&fromDate="+document.getElementById("fromDate").value+"&toDate="+document.getElementById("toDate").value+"&executingDepartment="+document.getElementById("executingDepartment").value;
		url=url+"&category="+document.getElementById("category").value+"&parentCategory="+document.getElementById("parentCategory").value+"&expenditureType="+document.getElementById("expenditureType").value;
		url=url+"&status="+document.getElementById("status").value+"&fund="+document.getElementById("fund").value+"&function="+document.getElementById("function").value+"&budgetHead="+document.getElementById("budgetHead").value;
		url=url+"&preparedBy="+document.getElementById("preparedBy").value;
		window.open(url,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}

 </script>
	<body>
		<div class="errorstyle" id="estimateAbstract_error"
			style="display: none;"></div>
		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<s:form name="estimateAbstractRportForm" action="estimateAbstractReport!getEstimateAbstract.action"
			id="estimateAbstractRportForm" theme="simple" >
			<div class="formmainbox">
				<div class="insidecontent">
					<div id="printContent" class="rbroundbox2">
						<div class="rbtop2">
							<div></div>
						</div>
						<div class="rbcontent2">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td>
										&nbsp;
									</td>
								</tr>
								<tr>
									<td>
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td colspan="4" class="headingwk">
													<div class="arrowiconwk">
														<img
															src="${pageContext.request.contextPath}/image/arrow.gif" />
													</div>
													<div class="headplacer">
														<s:text name="page.subheader.search.estimate" />
													</div>
												</td>
											</tr>
											<tr>
												<td class="greyboxwk">
													<s:text name="estimateabstract.fromdate" />
												</td>
												<td class="greybox2wk">
													<s:date name="fromDate" var="fromDateFormat"
														format="dd/MM/yyyy" />
													<s:textfield name="fromDate" id="fromDate"
														cssClass="selectwk" value="%{fromDateFormat}"
														onfocus="javascript:vDateType='3';"
														onkeyup="DateFormat(this,this.value,event,false,'3')" />
													<a
														href="javascript:show_calendar('forms[0].fromDate',null,null,'DD/MM/YYYY');"
														onmouseover="window.status='Date Picker';return true;"
														onmouseout="window.status='';return true;"> <img
															src="${pageContext.request.contextPath}/image/calendar.png"
															alt="Calendar" width="16" height="16" border="0"
															align="absmiddle" />
													</a>

												</td>
												<td width="17%" class="greyboxwk">
													<s:text name="estimateabstract.todate" />
												</td>
												<td width="17%" class="greybox2wk">
													<s:date name="toDate" var="toDateFormat"
														format="dd/MM/yyyy" />
													<s:textfield name="toDate" id="toDate"
														value="%{toDateFormat}" cssClass="selectwk"
														onfocus="javascript:vDateType='3';"
														onkeyup="DateFormat(this,this.value,event,false,'3')" />
													<a
														href="javascript:show_calendar('forms[0].toDate',null,null,'DD/MM/YYYY');"
														onmouseover="window.status='Date Picker';return true;"
														onmouseout="window.status='';return true;"> <img
															src="${pageContext.request.contextPath}/image/calendar.png"
															alt="Calendar" width="16" height="16" border="0"
															align="absmiddle" />
													</a>
												</td>
											</tr>
											<tr>
												<td width="11%" class="whiteboxwk">
													<s:text name='estimateabstract.executing.department'/> :
												</td>
												<td width="21%" class="whitebox2wk">
													<s:select headerKey="-1" headerValue="%{getText('estimateabstract.select')}" name="executingDepartment" 
													id="executingDepartment" cssClass="selectwk" list="dropdownData.executingDepartmentList" listKey="id" 
													listValue="deptName" value="%{executingDepartment}" />
													
												</td>
													<td width="15%" class="whiteboxwk">
													<s:text name="estimateabstract.work.nature" />
													:
												</td>
												<td width="53%" class="whitebox2wk">
													<s:select headerKey="-1" headerValue="%{getText('estimateabstract.select')}"
														name="expenditureType" id="expenditureType" cssClass="selectwk"
														list="dropdownData.typeList" listKey="id" listValue="name"
														value="%{expenditureType}" />
												</td>
											</tr>
											<tr>
												<td class="greyboxwk">
													<s:text name="estimateabstract.work.type" />
													:
												</td>
												<td class="greybox2wk">
													<s:select headerKey="-1" headerValue="%{getText('estimateabstract.select')}"
														name="parentCategory" id="parentCategory"
														cssClass="selectwk" list="dropdownData.parentCategoryList"
														listKey="id" listValue="description"
														value="%{parentCategory}"
														onChange="setupSubTypes(this);" />
													<egov:ajaxdropdown id="categoryDropdown"
														fields="['Text','Value']" dropdownId='category'
														url='estimate/ajaxEstimate!subcategories.action'
														selectedValue="%{category.id}" />
												</td>

												<td class="greyboxwk">
													<s:text name="estimateabstract.work.subtype" />
													:
												</td>
												<td class="greybox2wk">
													<s:select headerKey="-1" headerValue="%{getText('estimateabstract.select')}" name="category"
														value="%{category}" id="category" cssClass="selectwk"
														list="dropdownData.categoryList" listKey="id"
														listValue="description" />
												</td>
											</tr>
											<tr>
												<td width="15%" class="whiteboxwk">
													<s:text name='estimateabstract.fund'/> :				
												</td>
												<td width="53%" class="whitebox2wk">
													<s:select headerKey="-1" headerValue="%{getText('estimateabstract.select')}"
													name="fund" id="fund" cssClass="selectwk" list="dropdownData.fundList" listKey="id" 
													listValue="name" value="%{fund}"  />
												</td>
												<td width="11%" class="whiteboxwk">
													<s:text name='estimateabstract.function'/> :
												</td>
												<td width="21%" class="whitebox2wk">
													<s:select headerKey="-1" headerValue="%{getText('estimateabstract.select')}"
									 					name="function" id="function" cssClass="selectwk" list="dropdownData.functionList" 
									 					listKey="id" listValue="name" value="%{function}"   />
												</td>
											</tr>
											<tr><td class="greyboxwk"><s:text name='estimateabstract.createdby'/> :
											</td >
											<td class="greybox2wk">
											<s:select headerKey="-1"
															headerValue="%{getText('estimateabstract.select')}"
															name="preparedBy" value="%{preparedBy}" id="preparedBy"
															cssClass="selectwk" list="dropdownData.preparedByList"
															listKey="id" listValue="employeeName" />
											</td>
												
												<td class="greyboxwk"><s:text name='estimateabstract.estimate.status'/> :
												</td>
											<td width="21%" class="greybox2wk">
												<s:select id="status" name="status" headerKey="-1"
														headerValue="ALL" cssClass="selectwk"
														list="%{estimateStatuses}" listKey="code"
														listValue="description" />

											</td>
											</tr>
											<tr><td width="11%" class="whiteboxwk">
													<s:text name='estimateabstract.budgethead'/> :
												</td>
												<td class="whitebox2wk" colspan="3">
													<s:select headerKey="-1" headerValue="%{getText('estimateabstract.select')}"
														name="budgetHead" id="budgetHead" cssClass="selectwk" list="dropdownData.budgetHeadList" 
														listKey="id" listValue="name" value="%{budgetHead}"   />
												</td>
											</tr>
											<tr>
								<td colspan="4">
									<div class="buttonholdersearch">
										<s:hidden name="reportType" id="reportType" value="%{reportType}"/>
									    <s:submit cssClass="buttonadd" value="SEARCH" id="saveButton" name="button" method="getWorkProgress"/> &nbsp;&nbsp;&nbsp;
									    <input type="button" class="buttonfinal" value="CLEAR" id="button" name="clear" onclick="this.form.reset();">&nbsp;&nbsp;&nbsp;
									    <input type="button" class="buttonfinal" value="CLOSE"
															id="closeButton" name="button" onclick="window.close();" />
									</div>
								</td>
							</tr>
							<tr>
								<td colspan="4" class="headingwk">
									<div class="arrowiconwk">
										<img src="${pageContext.request.contextPath}/image/arrow.gif" />
									</div>
									<div class="headplacer">
										<s:text name="title.search.result" />
									</div>
								</td>
						    </tr>
						    <tr>
								<td colspan="4">
									&nbsp;
								</td>
							</tr>

							<tr><td colspan="4"> <logic:notEmpty name="searchResultList">
				  				        <display:table name="searchResultList" pagesize="" uid="row"
               							 cellpadding="0" cellspacing="0" requestURI="" 
                						 style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;" >

               							<display:column headerClass="pagetableth" class="pagetabletd" title="Sl. No."
			   								titleKey="column.title.SLNo" style="width:5%;text-align:left" >
               							   	<s:if test="%{#attr.row.subTypeOfWork=='Total' || #attr.row.department=='Total'}" >
            							   	</s:if>
											<s:else>
			     								<s:property value="#attr.row_rowNum" />
			     							</s:else>
										</display:column>
               							 
										<s:if test="%{reportType=='workwise'}" >
               							   <display:column  headerClass="pagetableth" class="pagetabletd" 
               							   		style="width:15%;text-align:left" title="Type of Work" >
               							   		<s:if test="%{#attr.row.subTypeOfWork=='Total'}" >
               							   		</s:if>
												<s:else>
													<s:property  value='%{#attr.row.typeOfWork}' />
												</s:else>
											</display:column>
               							   <display:column  headerClass="pagetableth" class="pagetabletd" 
               							   		style="width:15%;text-align:left" title="Sub Type Of Work" >
               							   		<s:if test="%{#attr.row.subTypeOfWork=='Total'}" >
               							   			<b><s:text name="estimateabstract.total" /></b>
               							   		</s:if>
												<s:else>
													<s:property  value='%{#attr.row.subTypeOfWork}' />
												</s:else>
											</display:column>
										</s:if>
										<s:else>
               							   <display:column  headerClass="pagetableth" class="pagetabletd" 
               							   		style="width:15%;text-align:left" title="Department" >
               							   		<s:if test="%{#attr.row.department=='Total'}" >
               							   			<b></b><s:text name="estimateabstract.total" /></b>
               							   		</s:if>
												<s:else>
													<s:property  value='%{#attr.row.department}' />
												</s:else>
											</display:column>
               							</s:else>
               							 <display:column  headerClass="pagetableth" class="pagetabletd" title="Created"
                    							style="text-align:right;width:10%" >
               							   	<s:if test="%{#attr.row.subTypeOfWork=='Total' || #attr.row.department=='Total'}" >
               							   		<s:property  value='%{#attr.row.totalCreated}' />
               							   	</s:if>
											<s:else>
												<s:property  value='%{#attr.row.created}' />
											</s:else>
										</display:column>
               							 <display:column headerClass="pagetableth" class="pagetabletd" title="Tech Sanctioned"
                    							style="text-align:right;width:10%" >
               							   	<s:if test="%{#attr.row.subTypeOfWork=='Total' || #attr.row.department=='Total'}" >
               							   		<s:property  value='%{#attr.row.totalTechSan}' />
               							   	</s:if>
											<s:else>
												<s:property  value='%{#attr.row.techSanctioned}' />
											</s:else>
										</display:column>
                    					<display:column headerClass="pagetableth" class="pagetabletd" title="Budget App Done"
                    							style="text-align:right;width:10%" >
               							   	<s:if test="%{#attr.row.subTypeOfWork=='Total' || #attr.row.department=='Total'}" >
               							   		<s:property  value='%{#attr.row.totalBudgetAppDone}' />
               							   	</s:if>
											<s:else>
												<s:property  value='%{#attr.row.budgetAppDone}' />
											</s:else>
										</display:column>
                    					<display:column headerClass="pagetableth" class="pagetabletd" title="Admin Sanctioned"
                    							style="text-align:right;width:10%" >
               							   	<s:if test="%{#attr.row.subTypeOfWork=='Total' || #attr.row.department=='Total'}" >
               							   		<s:property  value='%{#attr.row.totalAdminSan}' />
               							   	</s:if>
											<s:else>
												<s:property  value='%{#attr.row.adminSanctioned}' />
											</s:else>
										</display:column>
                    					<display:column headerClass="pagetableth" class="pagetabletd" title="Rejected"
                    							style="text-align:right;width:10%" >
               							   	<s:if test="%{#attr.row.subTypeOfWork=='Total' || #attr.row.department=='Total'}" >
               							   		<s:property  value='%{#attr.row.totalReject}' />
               							   	</s:if>
											<s:else>
												<s:property  value='%{#attr.row.rejected}' />
											</s:else>
										</display:column>
                    					<display:column headerClass="pagetableth" class="pagetabletd" title="Cancelled"
                    							style="text-align:right;width:10%" >
               							   	<s:if test="%{#attr.row.subTypeOfWork=='Total' || #attr.row.department=='Total'}" >
               							   		<s:property  value='%{#attr.row.totalCancel}' />
               							   	</s:if>
											<s:else>
												<s:property  value='%{#attr.row.cancelled}' />
											</s:else>
										</display:column>
               						</display:table>
				  				        </logic:notEmpty></td></tr>
				  				       
				 					<tr><td colspan="4">&nbsp;</td></tr>
									<s:if test="%{searchResultList.size()!=0}">
				 					   <tr>
											<td colspan="4">
												<div class="buttonholdersearch">
									    			<input type="button" class="buttonpdf" value="VIEW PDF" id="pdfbutton" name="pdfbutton" onclick="viewPDF();">
												</div>
											</td>
										</tr>
				 					</s:if>
										</table>
									</td>
								</tr>
							</table>
						</div>
					</div>
				</div>
			</div>
												
		</s:form>
	</body>
</html>

