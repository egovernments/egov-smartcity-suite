<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/egov-authz.tld" prefix="egov-authz" %>

<html>
<title><s:text name="sor.list" /></title>

<script type="text/javascript">
     
	function validate(){	
		//alert(document.forms[0].action='/egworks/masters/scheduleOfRate!searchSorDetails.action'
		document.forms[0].action='<%=request.getContextPath()%>/masters/scheduleOfRate!searchSorDetails.action';
		//alert(document.forms[0].action);
		document.forms[0].submit();
		//return false;
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
		 <s:form name="searchSORForm" id="searchSORForm" action="/masters/scheduleOfRate!searchSorDetails.action" theme="simple">			
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
						<table id="sorSearchTable" width="100%" border="0" cellspacing="0" cellpadding="0">
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
									<font color="red">*</font>
									<s:text name="master.sor.category" />:
								</td>
								<td width="21%" class="whitebox2wk">
									<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}"
										name="categry" id="category" cssClass="selectwk"
										list="dropdownData.categorylist" listKey="id"
										listValue="code"/>		
								</td>
								<td width="11%" class="whiteboxwk">
									&nbsp;
								</td>
								<td width="21%" class="whitebox2wk">
									&nbsp;
								</td>
							</tr>
							<tr>
								<td width="11%" class="greyboxwk">
									<s:text name="master.sor.code" />:
								</td>
								<td width="21%" class="greybox2wk">
									<s:textfield name="code" id="code" cssClass="selectwk"/>
								</td>
								<td width="15%" class="greyboxwk">
									<s:text name="master.sor.description" />:
								</td>
								<td width="53%" class="greybox2wk">
									<s:textfield name="description" id="description" cssClass="selectwk"/>
								</td>
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
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td colspan="7" class="headingwk">
												<div class="arrowiconwk">
													<img src="${pageContext.request.contextPath}/image/arrow.gif" />
												</div>
												<div class="headplacer">
													<s:text name="title.search.result" />
												</div>
											</td>
										</tr>
										<tr>
											<td>
											<s:if test="%{searchResult.fullListSize != 0 && displData=='yes'}">
												<display:table name="searchResult" pagesize="30" uid="currentRow"
													cellpadding="0" cellspacing="0" requestURI=""
													style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
													
													<display:column headerClass="pagetableth" class="pagetabletd" 
														title="Sl.No" titleKey="column.title.SLNo"
														style="width:4%;text-align:right" >
														<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize" />
													</display:column>
													
													<display:column headerClass="pagetableth" class="pagetabletd" 
														title="Category Type" titleKey="master.sor.category"
														style="width:10%;text-align:left" property="category.code">
													</display:column>
													
													<display:column headerClass="pagetableth" class="pagetabletd" 
														title="SOR Code" titleKey="master.sor.code"
														style="width:10%;text-align:left" property="code">
													</display:column>
													
													<display:column headerClass="pagetableth" class="pagetabletd" 
														title="SOR Decsription" titleKey="master.sor.description"
														style="width:51%;text-align:left" property="description">
													</display:column>
													
													<display:column headerClass="pagetableth" class="pagetabletd" 
														title="Unit of Measure" titleKey="master.sor.uom"
														style="width:9%;text-align:left" property="uom.uom">
													</display:column>
													
													<display:column headerClass="pagetableth" class="pagetabletd" 
														title="Action" style="width:13%;text-align:left" >
														<table width="100" border="0" cellpadding="0" cellspacing="2">
															<tr> <egov-authz:authorize actionName="WorksSOREditAutho">                   		
																<td width="20">
																	<a href="${pageContext.request.contextPath}/masters/scheduleOfRate!edit.action?id=<s:property value='%{#attr.currentRow.id}'/>&mode=edit">
																		<s:text name="sor.edit" /></a>
																</td>
																<td width="20">
																	<a href="${pageContext.request.contextPath}/masters/scheduleOfRate!edit.action?id=<s:property value='%{#attr.currentRow.id}'/>&mode=edit">
																		<img src='${pageContext.request.contextPath}/image/page_edit.png' alt="Edit Data" width="16" height="16" border="0" align="absmiddle" />
																	</a>
																</td>
																<td width="20">&#47;
																</td>
																</egov-authz:authorize>
																<egov-authz:authorize actionName="WorksSORViewAutho">
																<td width="20" align="right">
																	<a href="${pageContext.request.contextPath}/masters/scheduleOfRate!edit.action?id=<s:property value='%{#attr.currentRow.id}'/>&mode=view">
																		<s:text name="sor.view" />
																	</a>
																</td>
																<td width="20" align="left">
																	<a href="${pageContext.request.contextPath}/masters/scheduleOfRate!edit.action?id=<s:property value='%{#attr.currentRow.id}'/>&mode=view">
																		<img src='${pageContext.request.contextPath}/image/book_open.png' alt="View Data" width="16" height="16" border="0" align="absmiddle" />
																	</a>
																</td>
															 </egov-authz:authorize>
															</tr>
														</table>
													</display:column>
												</display:table>
												</s:if>
												<s:elseif test="%{searchResult.fullListSize == 0 && displData=='noData'}">
													<div >	
														<table width="100%" border="0" cellpadding="0"
														cellspacing="0">
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
										<tr>
										    <td colspan="6">
										    	<div align="right" class="mandatory">* <s:text name="message.mandatory" /></div>
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
			<div class="rbbot2"><div></div>	</div>
			</div>
			</div>
			</div>
		</s:form>
		<script type="text/javascript">
			
						
		</script>
	</body>
</html>
