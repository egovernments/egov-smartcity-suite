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
<%@ include file="/includes/taglibs.jsp"%>

<html>
<title><s:text name="contractor.search.title" /></title>
<div class="new-page-header">
	<s:text name="contractor.search.title" />
</div>

<body>
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
	<s:if test="%{hasActionMessages()}">
		<div id="msgsDiv" class="messagestyle">
			<s:actionmessage theme="simple" />
		</div>
	</s:if>
	
	<div id="contractorError" class="alert alert-danger"
		style="display: none;"></div>
	<s:form action="/masters/contractor-viewResult.action" theme="simple"
		name="contractor">
		<div class="navibarshadowwk"></div>
		<div class="formmainbox">
			<div class="insidecontent">
				<div class="rbroundbox2">
					<div class="rbtop2">
						<div></div>
					</div>
					<div class="rbcontent2">
						<table id="formTable" width="100%" border="0" cellspacing="0"
							cellpadding="0">
							<tr>
								<td>&nbsp;</td>
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
												<s:text	name="contractor.name" />:</td>
											<td width="21%" class="whitebox2wk">
												<s:textfield name="contractorName" id="contractorName" cssClass="selectboldwk" /></td>
											<td width="11%" class="whiteboxwk">
												<s:text name="contractor.code" />:</td>
											<td width="21%" class="whitebox2wk">
												<s:textfield name="contractorCode" id="contractorCode" cssClass="selectboldwk" /></td>
										</tr>
										<tr>
											<td width="11%" class="greyboxwk">
												<s:text	name="contractor.department" />:</td>
											<td width="21%" class="greybox2wk">
												<s:select id="status" name="departmentId" cssClass="selectwk" list="%{dropdownData.departmentList}" listKey="id" listValue="name" headerKey="" headerValue="--- Select ---" />
											</td>
											<td width="15%" class="greyboxwk">
												<s:text name="contractor.status" />:</td>
											<td width="53%" class="greybox2wk">
												<s:select id="status" name="statusId" cssClass="selectwk" list="%{dropdownData.statusList}" listKey="id" listValue="description" headerKey="" headerValue="--- Select ---" /></td>
										</tr>
										<tr>
											<td width="11%" class="whiteboxwk">
												<s:text name="contractor.grade" />:</td>
											<td width="21%" colspan="3" class="whitebox2wk">
												<s:select id="status" name="gradeId" cssClass="selectwk" list="%{dropdownData.gradeList}" listKey="id" listValue="grade" headerKey="" headerValue="--- Select ---" />
											</td>
										</tr>
										<tr>
											<td>&nbsp;</td>
										</tr>
										<tr>
											<td colspan="4">
												<div class="buttonholdersearch">
													<input type="submit" class="btn btn-primary" value="Search"	id="searchButton" onclick="validate()" />
													<input type="button" class="btn btn-default" value="Close" id="closeButton" onclick="window.close();" />
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
																	<img src="/egi/resources/erp2/images/arrow.gif" />
																</div>
																<div class="headplacer">
																	<s:text name="title.search.result" />
																</div>
															</td>
														</tr>
														<tr>
															<td><s:text id="select"
																	name="%{getText('column.title.select')}"></s:text> <s:text
																	id="slNo" name="%{getText('column.title.SLNo')}"></s:text>
																<s:text id="code" name="%{getText('contractor.code')}"></s:text>
																<s:text id="name" name="%{getText('contractor.name')}"></s:text>
																<s:text id="class" name="%{getText('contractor.grade')}"></s:text>
																<s:text id="status"
																	name="%{getText('contractor.status')}"></s:text> <s:if
																	test="%{searchResult.fullListSize != 0}">
																	<display:table name="searchResult" pagesize="30"
																		uid="currentRow" cellpadding="0" cellspacing="0"
																		requestURI=""
																		style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">

																		<display:column headerClass="pagetableth"
																			class="pagetabletd" title="${select}"
																			style="width:2%;" titleKey="column.title.select">
																			<input name="radio" type="radio" id="radio"
																				value="<s:property value='%{#attr.currentRow.id}'/>"
																				onClick="setContractorId('<s:property value='%{#attr.currentRow.id}'/>');" />
																			<s:hidden name="id" id="id" />
																		</display:column>

																		<display:column headerClass="pagetableth"
																			class="pagetabletd" title="${slNo}"
																			style="width:4%;text-align:right">
																			<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize" />
																		</display:column>

																		<display:column headerClass="pagetableth"
																			class="pagetabletd" title="${name}"
																			style="width:15%;text-align:left" property="name" />

																		<display:column headerClass="pagetableth"
																			class="pagetabletd" title="${code}"
																			style="width:15%;text-align:left" property="code" />

																		<display:column headerClass="pagetableth"
																			class="pagetabletd" title="${class}"
																			style="width:15%;text-align:left">
																			<s:property value="#attr.currentRow.contractorDetails[0].contractorGrade.grade" />
																		</display:column>

																		<display:column headerClass="pagetableth"
																			class="pagetabletd" title="${status}"
																			style="width:15%;text-align:left">
																			<s:property value="#attr.currentRow.contractorDetails[0].status.description" />
																		</display:column>
																	</display:table>
																</s:if>
																<s:elseif test="%{searchResult.fullListSize == 0}">
																	<div>
																		<table width="100%" border="0" cellpadding="0"
																			cellspacing="0">
																			<tr>
																				<td align="center"><font color="red">
																				<s:text name="label.no.records.found" /></font></td>
																			</tr>
																		</table>
																	</div>
																</s:elseif></td>
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
					<div class="rbbot2">
						<div></div>
					</div>
				</div>
			</div>
		</div>
	</s:form>

<script type="text/javascript">		
function viewData() {
    if (document.getElementById('id').value == '' || document.getElementById('id').value == null) {
        showMessage('contractorError', '<s:text name="contractor.select.error" />');
        window.scrollTo(0, 0);
        return false;
    } else
        window.location = '${pageContext.request.contextPath}/masters/contractor-edit.action?mode=view&id=' + dom.get('id').value;
    return true;
}

function modifyData() {
    if (document.getElementById('id').value == '' || document.getElementById('id').value == null) {
        showMessage('contractorError', '<s:text name="contractor.select.error" />');
        window.scrollTo(0, 0);
        return false;
    } else
        window.location = '${pageContext.request.contextPath}/masters/contractor-edit.action?mode=edit&id=' + dom.get('id').value;
    return true;
}			
function setContractorId(val) {
	document.getElementById('id').value = val;
}
function validate(){
	document.searchContractorForm.action='${pageContext.request.contextPath}/masters/contractor-viewResult.action'; 
   	document.searchContractorForm.submit();
}
</script>

<s:if test="%{searchResult.fullListSize != 0}">
	<div align="center">
		<input type="submit" name="VIEW" Class="btn btn-primary" value="View"
			id="VIEW" onclick="viewData();" /> 
		<input type="submit"
			name="MODIFY" Class="btn btn-primary" value="Modify" id="MODIFY"
			onclick="modifyData();" /> 
		<input type="submit" name="closeButton"
			id="closeButton" value="Close" Class="btn btn-default"
			onclick="window.close();" /> &nbsp;&nbsp;
	</div>
</s:if>

</body>
</html>