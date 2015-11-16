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
<title><s:text name="contractor.search.title" /></title>

<script type="text/javascript">
     
	function validate(){
		document.searchContractorForm.action='${pageContext.request.contextPath}/masters/contractor-viewResult.action'; 
    	document.searchContractorForm.submit();
		
	}
	</script>
<body >
		<s:if test="%{hasErrors()}">
       		 <div class="errorstyle">
          		<s:actionerror/>
          		<s:fielderror/>
        	</div>
    	</s:if>
   		<s:if test="%{hasActionMessages()}">
       		<div id="msgsDiv" class="messagestyle">
        		<s:actionmessage theme="simple"/>
        	</div>
    	</s:if>
		<s:form action="/masters/contractor-viewResult.action" theme="simple" name="contractor" >
			<div class="navibarshadowwk">
			</div>
			<div class="formmainbox">
			<div class="insidecontent">
			<div class="rbroundbox2">
			<div class="rbtop2"><div></div></div>
			<div class="rbcontent2">
			<table id="formTable" width="100%" border="0" cellspacing="0"
				cellpadding="0">
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table id="contractorViewSearchTable" width="100%" border="0"
							cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="4">&nbsp;</td>
							</tr>
							<tr>
								<td colspan="4" class="headingwk">
									<div class="arrowiconwk">
										<img src="/egi/resources/erp2/images/arrow.gif" />
									</div>
									<div class="headplacer">
										<s:text name='title.search.criteria' />
									</div>
								</td>
							</tr>
							<tr>
								<td width="11%" class="whiteboxwk">
									<s:text name="contractor.name" />:
								</td>
								<td width="21%" class="whitebox2wk">
									<s:textfield name="contractorName" id="contractorName" cssClass="selectboldwk"/>
								</td>
								<td width="11%" class="whiteboxwk">
									<s:text name="contractor.code" />:
								</td>
								<td width="21%" class="whitebox2wk">
									<s:textfield name="contractorCode" id="contractorCode" cssClass="selectboldwk"/>
								</td>
							</tr>
							<tr>
								<td width="11%" class="greyboxwk">
									<s:text name="contractor.department" />:
								</td>
								<td width="21%" class="greybox2wk">
									<s:select id="status" name="departmentId" cssClass="selectwk"
										list="%{dropdownData.departmentList}" listKey="id" listValue="name" 
										headerKey="" headerValue="--- Select ---" />	
								</td>
								<td width="15%" class="greyboxwk">
									<s:text name="contractor.status" />:
								</td>
								<td width="53%" class="greybox2wk">
									<s:select id="status" name="statusId" cssClass="selectwk"
										list="%{dropdownData.statusList}" listKey="id" listValue="description" 
										headerKey="" headerValue="--- Select ---" />									
								</td>
							</tr>
							<tr>
								<td width="11%" class="whiteboxwk">
									<s:text name="contractor.grade" />:
								</td>
								<td width="21%" colspan="3" class="whitebox2wk">
									<s:select id="status" name="gradeId" cssClass="selectwk"
										list="%{dropdownData.gradeList}" listKey="id" listValue="grade" 
										headerKey="" headerValue="--- Select ---" />	
								</td>
								<!-- td width="11%" class="whiteboxwk">
									label
								</td>
								<td width="21%" class="whitebox2wk">
									value
								</td-->
							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
							</tr>
							<tr>
								<td colspan="4">
									<div class="buttonholdersearch">
										<input type="submit" class="buttonadd" value="Search" id="searchButton" 
											onClick="validate()" />
										<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="button"
											onclick="window.close();" />
									</div>
								</td>
							</tr>
							<tr>
								<td colspan="4">
								<div>
									<table width="100%" border="0" cellspacing="0" cellpadding="0" id="contractorViewSearchInnerTable">
										<tr>
											<td colspan="7" class="headingwk">
												<div class="arrowiconwk">
													<img
														src="/egi/resources/erp2/images/arrow.gif" />
												</div>
												<div class="headplacer">
													<s:text name="title.search.result" />
												</div>
											</td>
										</tr>
										<tr>
											<td>
												<s:if test="%{searchResult.fullListSize != 0}">
													<display:table name="searchResult" pagesize="30"
														uid="currentRow" cellpadding="0" cellspacing="0"
														requestURI="" style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
														
														<display:column headerClass="pagetableth"
															class="pagetabletd" title="Sl.No"
															style="width:4%;text-align:right">
															<s:property
																value="#attr.currentRow_rowNum + (page-1)*pageSize" />
														</display:column>

														<display:column headerClass="pagetableth"
															class="pagetabletd" title="Name"
															style="width:15%;text-align:left" property="name" />

														<display:column headerClass="pagetableth"
															class="pagetabletd" title="Code"
															style="width:15%;text-align:left" property="code" />

														<display:column headerClass="pagetableth"
															class="pagetabletd" title="View/Edit"
															style="width:15%;text-align:left">
															<table width="40" border="0" cellpadding="0" cellspacing="2">
																<tr>
																	<td width="20">
																		<a href="${pageContext.request.contextPath}/masters/contractor-edit.action?id=<s:property value='%{#attr.currentRow.id}'/>&mode=edit">
																			<s:text name="sor.edit" />
																		</a>
																	</td>
																	<td width="20">
																		<a href="${pageContext.request.contextPath}/masters/contractor-edit.action?id=<s:property value='%{#attr.currentRow.id}'/>&mode=edit">
																			<img src='/egi/resources/erp2/images/page_edit.png' alt="Edit Data" width="16" height="16" border="0" align="absmiddle" /></a>																						
																	</td>
																	<td>&#47;</td>
																	<td width="20">
																		<a href="${pageContext.request.contextPath}/masters/contractor-edit.action?id=<s:property value='%{#attr.currentRow.id}'/>&mode=view"><s:text name="sor.view" />
																		</a>
																	</td>
																	<td width="20">
																		<a href="${pageContext.request.contextPath}/masters/contractor-edit.action?id=<s:property value='%{#attr.currentRow.id}'/>&mode=view">
																			<img src='/egi/resources/erp2/images/book_open.png' alt="View Data" width="16" height="16" border="0" align="absmiddle" />
																		</a>
																	</td>
																</tr>
															</table>

														</display:column>

													</display:table>
												</s:if>
												<s:elseif test="%{searchResult.fullListSize == 0}">
													<div>
														<table width="100%" border="0" cellpadding="0" cellspacing="0">
															<tr>
																<td align="center">
																	<font color="red"><s:text name="label.no.records.found"/></font>
																</td>
															</tr>
														</table>
													</div>
												</s:elseif>
											</td>
										</tr>
									</table>
								</div>
								</td>
							</tr>
							
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="4" class="shadowwk"></td>
				</tr>
				
			</table>
			</div>
			<div class="rbbot2"><div></div></div>
			</div>
			</div>
			</div>
		</s:form>
		<script type="text/javascript">
			
						
		</script>
	</body>
</html>
