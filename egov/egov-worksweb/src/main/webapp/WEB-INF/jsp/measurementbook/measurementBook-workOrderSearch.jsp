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
														src="/egworks/resources/erp2/images/arrow.gif" />
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
