<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  ~
  --%>

<%@ include file="/includes/taglibs.jsp" %>

<html>
	<head>
		<title><s:text name='contractor.search.title' /></title>
	</head> 
	<body>
		<s:form name="searchContractorForm" action="searchContractorAction" theme="simple" >
			<div class="formmainbox">
				<div class="insidecontent">
					<div class="rbroundbox2">
						<div class="rbtop2">
							<div></div>
						</div>
						<div class="rbcontent2">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<s:hidden name="rowId" id="rowid"/>
								<tr>
									<td></td>
								</tr>
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
														<img src="<cdn:url value='/egworks/resources/erp2/images/arrow.gif' />">
													</div>
													<div class="headplacer">
														<s:text name='contractor.search.label.search' />
													</div>
												</td>
											</tr>
											<tr>
												<td width="19%" class="greyboxwk">
													<s:hidden name="searchDate" />
													<s:text name='contractor.search.label.department' />:
												</td>
												<td width="30%" class="greybox2wk">
													<s:select id="status" name="departmentId" cssClass="selectwk"
														list="%{dropdownData.departmentList}" listKey="id" listValue="name" 
														headerKey="" headerValue="--- Select ---" />
												</td>
												<td width="14%" class="greyboxwk">
													<s:text name='contractor.search.label.code' />:
												</td>
												<td width="37%" class="greybox2wk">
													<s:textfield name="contractorCode" id="contractorCode" cssClass="selectboldwk"/>
												</td>
											</tr>
											<tr>
												<td class="whiteboxwk">
													<s:text name='contractor.search.label.grade' />:
												</td>
												<td class="whitebox2wk">
													<s:select id="status" name="gradeId" cssClass="selectwk"
														list="%{dropdownData.gradeList}" listKey="id" listValue="grade" 
														headerKey="" headerValue="--- Select ---" />
												</td>
												<td class="whiteboxwk">
													<s:text name='contractor.search.label.name' />:
												</td>
												<td class="whitebox2wk">
													<s:textfield name="contractorName" id="contractorName" cssClass="selectboldwk"/>
												</td>
											</tr>
											<tr>
												<td colspan="4" class="shadowwk"></td>
											</tr>
											<tr>
												<td colspan="4">
													<div class="buttonholdersearch">
														<input type="submit" class="buttonadd" value="Search" id="searchButton" 
															name="Search"  onclick="return validateAndSubmit();" />
													</div>
													<s:if test="%{contractorList != null}">
													<div id="capsearch" >
														<table width="100%" border="0" cellspacing="0"
															cellpadding="0">
															<tr>
																<td>
																	<table width="100%" border="0" cellspacing="0"
																		cellpadding="0">
																		<tr>
																			<td width="4%" class="headingwk">
																				<div class="arrowiconwk">
																					<img src="<cdn:url value='/egworks/resources/erp2/images/arrow.gif' />">
																				</div>
																				<div class="headplacer">
																					<s:text name='contractor.search.label.searchResult' />
																				</div>
																			</td>
																		</tr>
																		<tr>
																			<td class="aligncenter">
																				<table width="400" border="0" cellpadding="0"
																					cellspacing="0" align="center">
																					<tr>
																						<td width="17%" class="tablesubheadwka">
																							<s:text name='contractor.search.label.slno' />:
																						</td>
																						<td width="63%" class="tablesubheadwka">
																							<s:text name='contractor.search.label.name' />:
																						</td>
																						<td width="20%" class="tablesubheadwka">
																							<s:text name='contractor.search.label.code' />:
																						</td>
																					</tr>
																				</table>
																				<div class="searchscrollershort">

																					<table width="400" border="0" cellpadding="0"
																						cellspacing="0">
																						<s:iterator value="contractorList" var="cont" status="stat">
																						<tr onmousedown="" onmouseover="ChangeColor(this, true);"
																							onmouseout="ChangeColor(this, false);" href="#"
																							onclick="javascript:returnBackToParent('<s:property value="id"/>','<s:property value="code"/>','<s:property value="name"/>')"
																							id="getdate" style="cursor: hand">
																							<td width="17%" class="whitebox3wka">
																								<s:label value="%{#stat.index + 1}" />
																							</td>
																							<td width="63%" class="whitebox3wka">
																								<s:property value="name"/>
																							</td>
																							<td width="20%" class="whitebox3wka">
																								<s:property value="code"/>
																							</td>
																						</tr>
																						</s:iterator>
																					</table>
																				</div>
																			</td>
																		</tr>
																		<tr>
																			<td class="shadowwk"></td>
																		</tr>
																	</table>
																</td>
															</tr>
														</table>
													</div>
													</s:if>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td>
										<div align="right" class="mandatory">
											* <s:text name='message.mandatory' />
										</div>
									</td>
								</tr>
							</table>
						</div>
						<div class="rbbot2">
							<div></div>
						</div>
					</div>
				</div>
			</div>
			<div class="buttonholderwk">
			<input type="button" class="buttonfinal" value="CLEAR" id="button" name="clear" onclick="this.form.reset();">
			&nbsp;
			<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();" />
			</div>
		</s:form>
		
		<script type="text/javascript">
			function validateAndSubmit() {
				document.searchContractorForm.action='${pageContext.request.contextPath}/masters/contractor-searchResult.action';
    			document.searchContractorForm.submit();
			}
			
			function returnBackToParent(contractorId,contractorCode,contractorname) {
				var wind;
				var data = new Array();
				wind=window.dialogArguments;
				row_id = $('rowid').value;
				
				if(wind==undefined){
					wind=window.opener;
					data=row_id+ '`~`' +contractorId+ '`~`' + contractorCode+ '`~`' + contractorname;
					window.opener.update(data);
				}
		
				else{
					wind=window.dialogArguments;
					wind.result=row_id+ '`~`' +contractorId+ '`~`' + contractorCode+ '`~`' + contractorname;
				}
				window.close();
			}

			

			function ChangeColor(tableRow, highLight)
			{
				if (highLight)
				{
				  tableRow.style.backgroundColor = '#dcfac9';
				}
				else
				{
				  tableRow.style.backgroundColor = 'white';
				}
			}
	    
		</script>
	</body>
</html>
