<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page import="java.util.*" %>

<html>
	<title><s:text name='page.title.workprogressabstract.woDetails' />
	</title>
	<script>
	function openTN(id,wpId,tenderHeaderId){
			window.open("${pageContext.request.contextPath}/tender/tenderNegotiation!edit.action?id="+id+
					"&worksPackageId="+wpId+"&tenderHeader.id="+tenderHeaderId+"&mode=search",'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
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
									
									<logic:notEmpty name="approvedTNList">
				  				        <display:table name="approvedTNList" uid="currentRowObject"
               							 cellpadding="0" cellspacing="0" export="false" id="reportsmodule"
                						 style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;"
               							 requestURI="">
               							 <display:column headerClass="pagetableth"
			   								class="pagetabletd" title="Sl No"
			   								titleKey="column.title.SLNo"
			   									style="width:3%;text-align:left" >
			     								<s:property value="#attr.reportsmodule_rowNum"/>
										</display:column>
										
               							   	<display:column  headerClass="pagetableth" class="pagetabletd" style="width:10%;text-align:center" title="Tender Negotiation Date">
                								<s:date name="#attr.reportsmodule.negotiationDate" format="dd/MM/yyyy" /> 
                							</display:column>
                							
                							<display:column  headerClass="pagetableth" class="pagetabletd" style="width:10%;text-align:center" title="TN Approved Date">
                								<s:date name="#attr.reportsmodule.state.previous.createdDate" format="dd/MM/yyyy" /> 
                							</display:column>
                							
                							<display:column headerClass="pagetableth" class="pagetabletd" title="Tender Negotiation Number"
                    							style="text-align:left;width:10%" >
                    							<a href="Javascript:openTN(<s:property  value='%{#attr.reportsmodule.id}' />,<s:property  value='%{#attr.reportsmodule.tenderEstimate.worksPackage.id}' />,<s:property  value='%{#attr.reportsmodule.tenderEstimate.tenderHeader.id}' />)">
													 <s:property  value='%{#attr.reportsmodule.negotiationNumber}' />
												</a>
										</display:column>
										
										<display:column headerClass="pagetableth" class="pagetabletd" title="Type"
                    							style="text-align:right;width:10%" property="tenderEstimate.tenderType" />
                    						
                    					<s:if test="%{tenderResponseContractors!=null && !tenderResponseContractors.isEmpty && tenderResponseContractors.size==1}">		
                   							<display:column headerClass="pagetableth" class="pagetabletd" title="Contractor Name"
                   								style="text-align:right;width:10%" property="tenderResponseContractors.get(0).contractor.name" /> 
                   						</s:if>
                   						                    						
                   						<display:column headerClass="pagetableth" class="pagetabletd" title="Prepared By"
                   							style="text-align:left;width:10%" property="negotiationPreparedBy.employeeName" />
										
                    						
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
             	