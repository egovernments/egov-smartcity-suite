<!-- -------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency,
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It
# 	   is required that all modified versions of this material be marked in
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program
# 	   with regards to rights under trademark law for use of the trade names
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
<%@ include file="/includes/taglibs.jsp" %> 
<html>
<title><s:text name="contractor.grade.list" /></title>
<body >
 
	<s:if test="%{hasErrors()}">
        <div class="alert alert-danger">
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
   
    
    <div class="row">
						<div class="col-md-12">
							<div class="panel panel-primary" data-collapsed="0">
								
								<div class="panel-heading">
									<div class="panel-title text-center no-float">
										 <s:if test="%{hasActionMessages()}">
									        <s:actionmessage theme="simple"/>
									     </s:if>
									     <s:else>
									        <s:text name="contractor.grade.master" />
									     </s:else>
										
									</div>
									
								</div>
								<div class="panel-body">
								
								    <s:iterator value="contractorGradeList">
										<div class="row add-border">
											
											<div class="col-xs-3 add-margin">
												<s:text name="contractor.grade.master.grade" />
											</div>
											<div class="col-xs-3 add-margin view-content">
												<s:property value="grade"/>
											</div>
											<div class="col-xs-3 add-margin">
												<s:text name="contractor.grade.master.description" />
											</div>
											<div class="col-xs-3 add-margin view-content">
												<s:property value="description"/>
											</div>
											
										</div>
										<div class="row add-border">
											
											<div class="col-xs-3 add-margin">
												<s:text name="contractor.grade.master.minamount" />
											</div>
											<div class="col-xs-3 add-margin view-content">
												<s:text name="contractor.format.number" >
									   			<s:param name="rate" value='%{minAmount}' /></s:text>
											</div>
											<div class="col-xs-3 add-margin">
												<s:text name="contractor.grade.master.maxamount" />
											</div>
											<div class="col-xs-3 add-margin view-content">
												<s:text name="contractor.format.number" >
									   	        <s:param name="rate" value='%{maxAmount}' /></s:text>
											</div>
											
										</div>
										
									</s:iterator>
									
								
									<%-- <display:table name="contractorGradeList"  uid="currentRow"
										cellpadding="0" cellspacing="0" requestURI=""
										class="table table-bordered">
										
										<display:column headerClass="pagetableth" class="pagetabletd" 
											title="Sl.No" titleKey="column.title.SLNo"
											style="width:4%;text-align:right" >
											<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize" />
										</display:column>
										
										<display:column headerClass="pagetableth" class="pagetabletd" 
											title="Grade" titleKey="contractor.grade.master.grade"
											style="width:20%;text-align:left" property="grade"/>
																							
										<display:column headerClass="pagetableth" class="pagetabletd" 
											title="Description" titleKey="contractor.grade.master.description"
											style="width:40%;text-align:left" property="description"/>
																							
										<display:column headerClass="pagetableth" class="pagetabletd" 
											title="Minimum Amount " titleKey="contractor.grade.master.minamount"
											style="width:10%;text-align:right" >
											<s:text name="contractor.format.number" >
									   	<s:param name="rate" value='%{#attr.currentRow.minAmount}' /></s:text>
										</display:column>
										
										<display:column headerClass="pagetableth" class="pagetabletd" 
											title="Maximum Amount" titleKey="contractor.grade.master.maxamount"
											style="width:10%;text-align:right"  >
											<s:text name="contractor.format.number" >
									   	<s:param name="rate" value='%{#attr.currentRow.maxAmount}' /></s:text>
										</display:column>
										
										<display:column headerClass="pagetableth" class="pagetabletd" 
											title="Edit/View" style="width:13%;text-align:left" >
											<table width="100" border="0" cellpadding="0" cellspacing="2">
												<tr>                    		
													<td width="20">
														
													</td>
													<td width="20">
														<a href="${pageContext.request.contextPath}/masters/contractorGrade-edit.action?id=<s:property value='%{#attr.currentRow.id}'/>&mode=edit">
															<img src='/egi/resources/erp2/images/page_edit.png' alt="Edit Data" width="16" height="16" border="0" align="absmiddle" />
														</a>
													</td>
													<td width="20">&#47;
													</td>
													<td width="20">
														<a href="${pageContext.request.contextPath}/masters/contractorGrade-edit.action?id=<s:property value='%{#attr.currentRow.id}'/>&mode=view">
															<s:text name="sor.view" />
														</a>
													</td>
													<td width="20">
														<a href="${pageContext.request.contextPath}/masters/contractorGrade-edit.action?id=<s:property value='%{#attr.currentRow.id}'/>&mode=view" contenteditable="false">
															<img src='/egi/resources/erp2/images/book_open.png' alt="View Data" width="16" height="16" border="0" align="absmiddle" />
														</a>
													</td>
												</tr>
											</table>
										</display:column>
									</display:table> --%>
								</div>
							</div>
						</div>
					</div>
					
					<div class="row text-center">
						<div class="add-margin">
							<a class="btn btn-primary" href="${pageContext.request.contextPath}/masters/contractorGrade-edit.action?id=<s:property value='%{#attr.currentRow.id}'/>&mode=edit">
							    <s:text name="sor.edit" />
							</a>&nbsp;
							<a class="btn btn-primary" href="${pageContext.request.contextPath}/masters/contractorGrade-edit.action?id=<s:property value='%{#attr.currentRow.id}'/>&mode=view">
								<s:text name="sor.view" />
							</a>&nbsp;
							<input type="submit" name="closeButton" id="closeButton" value="Close" class="btn btn-default" onclick="window.close();" />
						</div>
					</div>

</body>

</html>

			
