<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page import="java.util.*" %>

<html>
<title><s:text name='page.title.workprogressabstract.bytypeofwork' /></title>
	<script type="text/javascript">
	function setupSubTypes(elem){
		categoryId=elem.options[elem.selectedIndex].value;
    	populatecategory({category:categoryId});
	}	
	function openWODetails(source,criteria,subcriteria){
		var url="${pageContext.request.contextPath}/reports/workProgress!getWoDetailsForTypeOfWork.action?source="+source+"&criteria="+criteria+"&subcriteria="+subcriteria;
		url=url+"&fromDate="+document.getElementById("fromDate").value+"&toDate="+document.getElementById("toDate").value+"&workStatus="+document.getElementById("workStatus").value;
		url=url+"&category="+document.getElementById("category").value+"&parentCategory="+document.getElementById("parentCategory").value+"&expenditureType="+document.getElementById("expenditureType").value;
		url=url+"&fund="+document.getElementById("fund").value+"&function="+document.getElementById("function").value+"&budgetHead="+document.getElementById("budgetHead").value;
		url=url+"&preparedBy="+document.getElementById("preparedBy").value+"&engineerIncharge="+document.getElementById("engineerIncharge").value+"&engineerIncharge2="+document.getElementById("engineerIncharge2").value;
		window.open(url,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	</script>
	<body>
		<div class="errorstyle" id="workProgress_error"
			style="display: none;"></div>
		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<s:form name="workProgressForm" action="workProgress!getWorkProgressForTypeOfWork.action"
			id="workProgressForm" theme="simple"
			>
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
													<s:text name="workprogressabstract.workwise.fromdate" />
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
												<td  class="greyboxwk">
													<s:text name="workprogressabstract.workwise.todate" />
												</td>
												<td class="greybox2wk">
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
												<td class="whiteboxwk">
													<s:text name="workprogressabstract.work.type" />
													:
												</td>
												<td class="whitebox2wk">
													<s:select headerKey="-1" headerValue="%{getText('workprogressabstract.select')}"
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

												<td class="whiteboxwk">
													<s:text name="workprogressabstract.work.subtype" />
													:
												</td>
												<td class="whitebox2wk">
													<s:select headerKey="-1" headerValue="%{getText('workprogressabstract.select')}" name="category"
														value="%{category}" id="category" cssClass="selectwk"
														list="dropdownData.categoryList" listKey="id"
														listValue="description" />
												</td>
											</tr>
											<tr>
												<td  class="greyboxwk">
													<s:text name="workprogressabstract.work.nature" />
													:
												</td>
												<td  class="greybox2wk">
													<s:select headerKey="-1" headerValue="%{getText('workprogressabstract.select')}"
														name="expenditureType" id="expenditureType" cssClass="selectwk"
														list="dropdownData.typeList" listKey="id" listValue="name"
														value="%{expenditureType}" />
												</td>
												<td  class="greyboxwk">
													<s:text name='workprogressabstract.work.status'/> :
												</td>
												<td  class="greybox2wk">
													<s:select 
		headerKey="-1" headerValue="%{getText('workprogressabstract.select')}"
		list="#{'given':'Work Order Given', 'started':'Work Started', 'notstarted':'Work Not Started', 'total0to25':'Inprogress 0-25 %',
		'total26to50':'Inprogress 26-50 %', 'total51to75':'Inprogress 51-75 %', 'total76to100':'Inprogress 76-100 %', 'completed':'Completed',
		'cancelled':'Work Order Cancelled','overduecontractperiod':'Work Contract Overdue'}" 
		name="workStatus" id="workStatus" value="%{workStatus}" />
													
													
													
												</td>
											</tr>
											
											<tr>
												<td  class="whiteboxwk">
													<s:text name='workprogressabstract.fund'/> :				
												</td>
												<td class="whitebox2wk">
													<s:select headerKey="-1" headerValue="%{getText('workprogressabstract.select')}"
													name="fund" id="fund" cssClass="selectwk" list="dropdownData.fundList" listKey="id" 
													listValue="name" value="%{fund}"  />
												</td>
												<td  class="whiteboxwk">
													<s:text name='workprogressabstract.function'/> :
												</td>
												<td  class="whitebox2wk">
													<s:select headerKey="-1" headerValue="%{getText('workprogressabstract.select')}"
									 					name="function" id="function" cssClass="selectwk" list="dropdownData.functionList" 
									 					listKey="id" listValue="name" value="%{function}"   />
												</td>
											</tr>
											<tr>
											<td  class="greyboxwk">
													<s:text name='workprogressabstract.budgethead'/> :
												</td>
												<td class="greybox2wk" colspan="3">
													<s:select headerKey="-1" headerValue="%{getText('workprogressabstract.select')}"
														name="budgetHead" id="budgetHead" cssClass="selectwk" list="dropdownData.budgetHeadList" 
														listKey="id" listValue="name" value="%{budgetHead}"   />
												</td>
											</tr>
											<tr>
											<tr>
											<td class="whiteboxwk"><s:text name='workprogressabstract.woassigneduser1'/>
											</td ><td class="whitebox2wk" >
											<s:select headerKey="-1"
															headerValue="%{getText('workprogressabstract.select')}"
															name="engineerIncharge" value="%{engineerIncharge}" id="engineerIncharge"
															cssClass="selectwk" list="dropdownData.engineerInchargeList"
															listKey="id" listValue="employeeName" />
											</td>
											<td class="whiteboxwk"><s:text name='workprogressabstract.woassigneduser2'/>
											</td >
											<td class="whitebox2wk">
											<s:select headerKey="-1"
															headerValue="%{getText('workprogressabstract.select')}"
															name="engineerIncharge2" value="%{engineerIncharge2}" id="engineerIncharge2"
															cssClass="selectwk" list="dropdownData.engineerIncharge2List"
															listKey="id" listValue="employeeName" />
											</td></tr>
											<tr><td class="greyboxwk"><s:text name='workprogressabstract.createdby'/>
											</td >
											<td class="greybox2wk" colspan="3">
											<s:select headerKey="-1"
															headerValue="%{getText('workprogressabstract.select')}"
															name="preparedBy" value="%{preparedBy}" id="preparedBy"
															cssClass="selectwk" list="dropdownData.preparedByList"
															listKey="id" listValue="employeeName" />
											</td></tr>
								<td colspan="4">
									<div class="buttonholdersearch">
									<s:hidden name="reportType" id="reportType" value="workwise"/>
									    <s:submit cssClass="buttonadd" value="SEARCH" id="saveButton" name="button" method="getWorkProgressForTypeOfWork"
									    /> &nbsp;&nbsp;&nbsp;
									    <input type="button" class="buttonfinal" value="CLEAR" id="button" name="clear" onclick="this.form.reset();">&nbsp;&nbsp;&nbsp;
									    <input type="button" class="buttonfinal" value="CLOSE"
															id="closeButton" name="button" onclick="window.close();" />
									</div>
								</td>
							</tr>
							<tr><td colspan="4"> <logic:notEmpty name="woList">
				  				        <display:table name="woList" uid="currentRowObject" 
               							 cellpadding="0" cellspacing="0" export="true" id="reportsmodule"
                						 style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;"
               							 requestURI="" >
               							 <display:caption media="pdf"> <center><s:text name='page.title.workprogressabstract.bytypeofwork' /></center>
               							 </display:caption>
               							   <display:column  headerClass="pagetableth" class="pagetabletd" style="width:10%;text-align:center" title="Type Of Work"   property="CONDITION">
                								<s:text name ="CONDITION"/> 
                							</display:column>
                							<display:column  headerClass="pagetableth" class="pagetabletd" style="width:10%;text-align:center" title="Sub Type Of Work"  property="SubTypeOfWork">
                								<s:text name ="SubTypeOfWork"/> 
                							</display:column>
               							 <display:column  headerClass="pagetableth" class="pagetabletd" title="Work Order Given"
                    							style="text-align:right;width:10%"  >
                    							<s:if test="%{#attr.reportsmodule.CONDITION!='Total'}">
                    							<a href="Javascript:openWODetails('given','<s:property  value='%{#attr.reportsmodule.CONDITION}' />','<s:property  value='%{#attr.reportsmodule.SubTypeOfWork}' />')">
													 <s:property  value='%{#attr.reportsmodule.given}' />
												</a>
												</s:if>
												<s:else>
													<s:property  value='%{#attr.reportsmodule.given}' />
												</s:else>
										</display:column>
               							 <display:column headerClass="pagetableth" class="pagetabletd" title="Work Started"
                    							style="text-align:right;width:10%" >
                    							<s:if test="%{#attr.reportsmodule.CONDITION!='Total'}">
                    							<a href="Javascript:openWODetails('started','<s:property  value='%{#attr.reportsmodule.CONDITION}' />','<s:property  value='%{#attr.reportsmodule.SubTypeOfWork}' />')">
													 <s:property  value='%{#attr.reportsmodule.started}' />
												</a>
												</s:if>
												<s:else>
													<s:property  value='%{#attr.reportsmodule.started}' />
												</s:else>
										</display:column>
                    					<display:column headerClass="pagetableth" class="pagetabletd" title="Work Not Started"
                    							style="text-align:right;width:10%" >
                    							<s:if test="%{#attr.reportsmodule.CONDITION!='Total'}">
                    							<a href="Javascript:openWODetails('notstarted','<s:property  value='%{#attr.reportsmodule.CONDITION}' />','<s:property  value='%{#attr.reportsmodule.SubTypeOfWork}' />')">
													 <s:property  value='%{#attr.reportsmodule.notstarted}' />
												</a>
												</s:if>
												<s:else>
													<s:property  value='%{#attr.reportsmodule.notstarted}' />
												</s:else>
										</display:column>
                    					<display:column headerClass="pagetableth" class="pagetabletd" title="Inprogress 0-25 %"
                    							style="text-align:right;width:10%" >
                    							<s:if test="%{#attr.reportsmodule.CONDITION!='Total'}">
                    							<a href="Javascript:openWODetails('total0to25','<s:property  value='%{#attr.reportsmodule.CONDITION}' />','<s:property  value='%{#attr.reportsmodule.SubTypeOfWork}' />')">
													 <s:property  value='%{#attr.reportsmodule.total0to25}' />
												</a>
												</s:if>
												<s:else>
													<s:property  value='%{#attr.reportsmodule.total0to25}' />
												</s:else>
										</display:column>
                    					<display:column headerClass="pagetableth" class="pagetabletd" title="Inprogress 26-50 %"
                    							style="text-align:right;width:10%" >
                    							<s:if test="%{#attr.reportsmodule.CONDITION!='Total'}">
                    							<a href="Javascript:openWODetails('total26to50','<s:property  value='%{#attr.reportsmodule.CONDITION}' />','<s:property  value='%{#attr.reportsmodule.SubTypeOfWork}' />')">
													 <s:property  value='%{#attr.reportsmodule.total26to50}' />
												</a>
												</s:if>
												<s:else>
													<s:property  value='%{#attr.reportsmodule.total26to50}' />
												</s:else>
										</display:column>
                    					<display:column headerClass="pagetableth" class="pagetabletd" title="Inprogress 51-75 %"
                    							style="text-align:right;width:10%" >
                    							<s:if test="%{#attr.reportsmodule.CONDITION!='Total'}">
                    							<a href="Javascript:openWODetails('total51to75','<s:property  value='%{#attr.reportsmodule.CONDITION}' />','<s:property  value='%{#attr.reportsmodule.SubTypeOfWork}' />')">
													 <s:property  value='%{#attr.reportsmodule.total51to75}' />
												</a>
												</s:if>
												<s:else>
													<s:property  value='%{#attr.reportsmodule.total51to75}' />
												</s:else>
										</display:column>
                    					<display:column headerClass="pagetableth" class="pagetabletd" title="Inprogress 76-100 %"
                    							style="text-align:right;width:10%">
                    							<s:if test="%{#attr.reportsmodule.CONDITION!='Total'}">
                    							<a href="Javascript:openWODetails('total76to100','<s:property  value='%{#attr.reportsmodule.CONDITION}' />','<s:property  value='%{#attr.reportsmodule.SubTypeOfWork}' />')">
													 <s:property  value='%{#attr.reportsmodule.total76to100}' />
												</a>
												</s:if>
												<s:else>
													<s:property  value='%{#attr.reportsmodule.total76to100}' />
												</s:else>
										</display:column>
                    					<display:column headerClass="pagetableth" class="pagetabletd" title="Completed"
                    							style="text-align:right;width:10%" >
                    							<s:if test="%{#attr.reportsmodule.CONDITION!='Total'}">
                    							<a href="Javascript:openWODetails('completed','<s:property  value='%{#attr.reportsmodule.CONDITION}' />','<s:property  value='%{#attr.reportsmodule.SubTypeOfWork}' />')">
													 <s:property  value='%{#attr.reportsmodule.completed}' />
												</a>
												</s:if>
												<s:else>
													<s:property  value='%{#attr.reportsmodule.completed}' />
												</s:else>
										</display:column>
                    					<display:column headerClass="pagetableth" class="pagetabletd" title="Work Order Value"
                    							style="text-align:right;width:10%" property="wovalue" />
                    					<display:column headerClass="pagetableth" class="pagetabletd" title="Payment Released (Nos,Amount)"
                    							style="text-align:right;width:10%" property="paymentInfo" />
                    					<display:column headerClass="pagetableth" class="pagetabletd" title="Overdue Contract period"
                    							style="text-align:right;width:10%" >
                    							<s:if test="%{#attr.reportsmodule.CONDITION!='Total'}">
                    							<a href="Javascript:openWODetails('overduecontractperiod','<s:property  value='%{#attr.reportsmodule.CONDITION}' />','<s:property  value='%{#attr.reportsmodule.SubTypeOfWork}' />')">
													 <s:property  value='%{#attr.reportsmodule.overduecontractperiod}' />
												</a>
												</s:if>
												<s:else>
													<s:property  value='%{#attr.reportsmodule.overduecontractperiod}' />
												</s:else>
										</display:column>
									 
                    					 <display:setProperty name="export.csv" value="false" />
                <display:setProperty name="export.excel" value="true" />
                <display:setProperty name="export.xml" value="false" />
                <display:setProperty name="export.pdf" value="true" />
                <display:setProperty name="export.pdf.filename" value="WorkProgress-ByTypeOfWork.pdf" />
		<display:setProperty name="export.excel.filename" value="WorkProgress-ByTypeOfWork.xls" />
               							</display:table>
				  				        </logic:notEmpty></td></tr>
				  				       
				 			<tr><td colspan="4">&nbsp;</td></tr>
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

