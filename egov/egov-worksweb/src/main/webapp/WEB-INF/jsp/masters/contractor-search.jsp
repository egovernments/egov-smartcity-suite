<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>

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
						
						<!--  div class="datewk">
								<div class="estimateno">
									Estimate No: &lt;Not Assigned&gt;
								</div>
								<span class="bold">Today</span> 12/12/2009
							</div>
							
						-->
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
														<img src="${pageContext.request.contextPath}/image/arrow.gif" />
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
														list="%{dropdownData.departmentList}" listKey="id" listValue="deptName" 
														headerKey="" headerValue="--- Select ---" />
												</td>
												<td width="14%" class="greyboxwk">
													<s:text name='contractor.search.label.code' />:
												</td>
												<td width="37%" class="greybox2wk">
													<s:textfield name="contractorcode" id="contractorcode" cssClass="selectboldwk"/>
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
																					<img src="${pageContext.request.contextPath}/image/arrow.gif" />
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
				document.searchContractorForm.action='${pageContext.request.contextPath}/masters/contractor!searchResult.action';
    			document.searchContractorForm.submit();
			}
			
			function returnBackToParent(contractorId,contractorCode,contractorname) {
				//document.all.ret.value =  contractorId+ '`~`' + contractorCode+ '`~`' + contractorname;
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