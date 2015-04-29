<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<script>

</script>
<html>
	<head>
	    <title>- <s:text name="Work Order Search" /></title>
	</head>
	<body>

		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<s:if test="%{hasActionMessages()}">
			<div class="messagestyle">
				<s:actionmessage theme="simple" />
			</div>
		</s:if>
		<s:form action="measurementBook" theme="simple"
			name="workOrderSearchForm">
			<div class="errorstyle" id="search_error" style="display:none;"></div>
			<div class="navibarshadowwk">
			</div>
			<div class="formmainbox">
			<div class="insidecontent">
			<div class="rbroundbox2">
			<div class="rbtop2">
			</div>
			<div class="rbcontent2">
			<s:hidden name="userMode" />
			<s:hidden name="rowId" id="rowid"/>
			<table id="formTable" width="100%" border="0" cellspacing="0"
				cellpadding="0">
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table id="assetSearchTable" width="100%" border="0"
							cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="4">&nbsp;</td>
							</tr>
							<tr>
								<td colspan="4">
								<div>
									<table width="100%" border="0" cellspacing="0"
										cellpadding="0">
										<tr>
											<td colspan="7" class="headingwk">
												<div class="arrowiconwk">
													<img
														src="${pageContext.request.contextPath}/image/arrow.gif" />
												</div>
												<div class="headplacer">
													<s:text name="search.result" />
												</div>
											</td>
										</tr>
										<tr>
											<td width="10%" class="tablesubheadwk">
												Sl No
											</td>
											<td width="15%" class="tablesubheadwk">
												WO No
											</td>
										</tr>
										<% int count=0;	%>
										<s:iterator id="workOrderIterator" value="workOrderList">
											<% count++;	%>
										</s:iterator>
										<s:if test="%{workOrderList.size != 0}">
											<% if(count>20){ %>
											<div style="height: 350px"
												class="scrollerboxaddestimate">
												<%}%>
												<table width="100%" border="0" cellspacing="0"
													cellpadding="0">
													<s:iterator id="workOrderIterator" value="workOrderList"
														status="row_status">
														<tr onmousedown="" onmouseover="changeColor(this, true);"
															onmouseout="changeColor(this, false);" href="#"
															onclick="javascript:returnBackToParent('<s:property value="id"/>','<s:property value="workOrderNumber"/>')"
															id="getworesult" style="cursor: hand">
															<td width="10%"">
																<s:property value="#row_status.count" />
																&nbsp;
															</td>
															<td width="15%">
																<s:property value="%{workOrderNumber}" />
																&nbsp;
															</td>
														</tr>
													</s:iterator>
												</table>
												<% if(count>20){ %>
											</div>
											<%}%>
										</s:if>
										<s:elseif test="%{workOrderList.size == 0}">
											<tr>
												<td colspan="2" align="center">
													<font color="red">No record Found.</font>
												</td>
											</tr>
										</s:elseif>
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
			</div>
			</div>
			</div>
			</div>
		</s:form>
		<script type="text/javascript">
			
			function returnBackToParent(id,wonumber) {
				var wind;
				var data = new Array();
				wind=window.dialogArguments;
				if(wind==undefined){
					wind=window.opener;
					data = id + '`~`' + wonumber;
					window.opener.update(data);
				}
		
				else{
					wind=window.dialogArguments;
					wind.result = id + '`~`' + wonumber;
				}
				window.close();
			}

			function changeColor(tableRow, highLight)
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
