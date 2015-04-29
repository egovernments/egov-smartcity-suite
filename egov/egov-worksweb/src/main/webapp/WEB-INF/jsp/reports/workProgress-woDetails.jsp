<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page import="java.util.*" %>

<html>
	<title><s:text name='page.title.workprogressabstract.woDetails' />
	</title>
	<script>
	function openWO(id){
			window.open("${pageContext.request.contextPath}/workorder/workOrder!edit.action?id="+id+
		"&mode=search",'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}

	function openWorkPrgReport(id){
		window.open("${pageContext.request.contextPath}/reports/workProgressRegister!searchDetails.action?woId="+id+
			"&sourcePage=deptWiseReport",'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
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
		<s:form name="workProgressForm" action="workProgress!search.action"
			id="workProgressForm" theme="simple"
			>
			<s:hidden name="reportType" id="reportType" /> 
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
									
									<logic:notEmpty name="woDetailList">
				  				        <display:table name="woDetailList" uid="currentRowObject"
               							 cellpadding="0" cellspacing="0" export="false" id="reportsmodule"
                						 style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;"
               							 requestURI="">
               							 <display:column headerClass="pagetableth"
			   								class="pagetabletd" title="Sl No"
			   								titleKey="column.title.SLNo"
			   									style="width:3%;text-align:left" >
			     								<s:property value="#attr.reportsmodule_rowNum"/>
										</display:column>
               							   	<display:column  headerClass="pagetableth" class="pagetabletd" style="width:10%;text-align:center" title="Date">
                								<s:date name="#attr.reportsmodule.workOrderDate" format="dd/MM/yyyy" /> 
                							</display:column>
                							<display:column headerClass="pagetableth" class="pagetabletd" title="Work Order Number"
                    							style="text-align:left;width:10%" >
                    							<a href="Javascript:openWO(<s:property  value='%{#attr.reportsmodule.id}' />)">
													 <s:property  value='%{#attr.reportsmodule.workOrderNumber}' />
												</a>
										</display:column>
                    						<display:column headerClass="pagetableth" class="pagetabletd" title="Work Order Value"
                    							style="text-align:right;width:10%" property="workOrderAmount" />
                    						<display:column headerClass="pagetableth" class="pagetabletd" title="Contractor"
                    							style="text-align:left;width:10%" property="contractor.name" />
                    						<display:column headerClass="pagetableth" class="pagetabletd" title="Work Order Alloted to"
                    							style="text-align:left;width:10%" property="engineerIncharge.employeeName" />
                    						
                    						<s:if test="%{reportType=='deptwise'}">	
	                    						<display:column headerClass="pagetableth" class="pagetabletd" title="View Work Progress Register"
	                    							style="text-align:center;width:10%" >
	                    							<a href="Javascript:openWorkPrgReport(<s:property  value='%{#attr.reportsmodule.id}' />)">
	                   										<img
															src="${pageContext.request.contextPath}/image/book_open.png"
															alt="Calendar" width="16" height="16" border="0"
															align="absmiddle" />
													</a>
												</display:column>	
                    						</s:if>
                						</display:table>
                						</logic:notEmpty>
                					</td>
                				</tr>
                				<tr><td><center>
                				<input type="button" class="buttonfinal" value="CLOSE"
															id="closeButton" name="button" onclick="window.close();" /></center></td></tr>
                		</table>
                		</div>
                	</div>
                </div>
               </div>
              </s:form>
              
             </body>
             </html>
             	