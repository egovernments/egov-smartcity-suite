<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>

<html>
<head>
<title><s:text name="contractor.list" /></title>

<script type="text/javascript">
     
	function validate(){
		document.contractor.action='${pageContext.request.contextPath}/masters/contractor!viewResult.action';
    	document.contractor.submit();
		
	}
	</script>
</head>
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
		<s:form action="/masters/contractor!viewResult.action" theme="simple" name="contractor" >
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
										<img src="${pageContext.request.contextPath}/image/arrow.gif" />
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
									<s:textfield name="contractorcode" id="contractorcode" cssClass="selectboldwk"/>
								</td>
							</tr>
							<tr>
								<td width="11%" class="greyboxwk">
									<s:text name="contractor.department" />:
								</td>
								<td width="21%" class="greybox2wk">
									<s:select id="status" name="departmentId" cssClass="selectwk"
										list="%{dropdownData.departmentList}" listKey="id" listValue="deptName" 
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
														src="${pageContext.request.contextPath}/image/arrow.gif" />
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
																		<a href="${pageContext.request.contextPath}/masters/contractor!edit.action?id=<s:property value='%{#attr.currentRow.id}'/>&mode=edit">
																			<s:text name="sor.edit" />
																		</a>
																	</td>
																	<td width="20">
																		<a href="${pageContext.request.contextPath}/masters/contractor!edit.action?id=<s:property value='%{#attr.currentRow.id}'/>&mode=edit">
																			<img src='${pageContext.request.contextPath}/image/page_edit.png' alt="Edit Data" width="16" height="16" border="0" align="absmiddle" /></a>																						
																	</td>
																	<td>&#47;</td>
																	<td width="20">
																		<a href="${pageContext.request.contextPath}/masters/contractor!edit.action?id=<s:property value='%{#attr.currentRow.id}'/>&mode=view"><s:text name="sor.view" />
																		</a>
																	</td>
																	<td width="20">
																		<a href="${pageContext.request.contextPath}/masters/contractor!edit.action?id=<s:property value='%{#attr.currentRow.id}'/>&mode=view">
																			<img src='${pageContext.request.contextPath}/image/book_open.png' alt="View Data" width="16" height="16" border="0" align="absmiddle" />
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
																	<font color="red">No record Found.</font>
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
