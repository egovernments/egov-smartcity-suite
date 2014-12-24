<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>

<html>
	<head>
		<title><s:text name='workOrder.search.title' /></title>
	</head> 
		<div class="errorstyle" id="form_error" style="display: none;">
			<span id="date_error" style="display: none;"><s:text name="workorder.search.createDate.invalid" /></span><br>
			<span id="mandatory_error" style="display: none;"><s:text name="workorder.serach.mandatory.error" /></span>
			<span id="mandatory_length_error" style="display: none;"><s:text name="workorder.serach.mandatory.length.error" /></span>
		    <span id='errorcreateDate' style="display:none;color:red;font-weight:bold">&nbsp;</span>
		</div>
	<body>
		<s:form name="searchWorkOrderForm" id="searchWorkOrderForm" action="searchWorkOrderAction" theme="simple" onsubmit="validateAndSubmit();">
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
														<s:text name='workOrder.search.label' />
													</div>
												</td>
											</tr>
											<tr>
												<td width="19%" class="greyboxwk">
													<s:text name='workOrder.search.label.contractor' />:
												</td>
												<td width="30%" class="greybox2wk">
													<s:select id="contractorId" name="contractorId" cssClass="selectwk"
														list="%{dropdownData.contractorList}" listKey="id" listValue="name" 
														headerKey="-1" headerValue="--- Select ---" />
												</td>
												<td width="14%" class="greyboxwk">
													<s:text name='workOrder.search.label.createDate' />:
												</td>
												<td width="37%" class="greybox2wk">
													<s:date name="createDate" var="createDate" format="dd/MM/yyyy"/>
										 			<s:textfield name="createDate"  
													        id="createDate" cssClass="selectboldwk" onfocus="javascript:vDateType='3';"
												         	onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="checkDate(this)"/>
										         	<a href="javascript:show_calendar('forms[0].createDate',null,null,'DD/MM/YYYY');" 
												         	onmouseover="window.status='Date Picker';return true;" 
											          		onmouseout="window.status='';return true;">
										          			<img src="${pageContext.request.contextPath}/image/calendar.png" 
													          alt="Calendar" width="16" height="16" border="0" align="absmiddle" /></a>
												</td>
											</tr>
											<tr>
												<td class="whiteboxwk">
													<s:text name='workOrder.search.label.tenderNo' />:
												</td>
												<td class="whitebox2wk">
													<s:select id="tenderNo" name="tenderNo" cssClass="selectwk"
														list="%{dropdownData.tenderHeaderList}" listKey="tenderNo" listValue="tenderNo" 
														headerKey="" headerValue="--- Select ---" />
												</td>
												<td class="whiteboxwk">
													<s:text name='workOrder.search.label.workOrderNo' />:
												</td>
												<td class="whitebox2wk">
													<s:textfield name="workorderNo" id="workorderNo" cssClass="selectboldwk"/>
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
													<s:if test="%{workOrderList != null}">
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
																					<s:text name='workOrder.search.label.searchResult' />
																				</div>
																			</td>
																		</tr>
																		<tr>
																			<td class="aligncenter">
																				<table width="400" border="0" cellpadding="0"
																					cellspacing="0" align="center">
																					<tr>
																						<td width="17%" class="tablesubheadwka">
																							<s:text name='workOrder.search.label.slno' />:
																						</td>
																						<td width="83%" class="tablesubheadwka">
																							<s:text name='workOrder.search.label.workOrderNo' />:
																						</td>
																					</tr>
																				</table>
																				<div class="searchscrollershort">

																					<table width="400" border="0" cellpadding="0"
																						cellspacing="0">
																						<s:if test="%{workOrderList.size() == 0}">
																							<s:text name="search.noresultfound" />
																						</s:if>
																						<s:iterator value="workOrderList" var="cont" status="stat">
																						<tr onmousedown="" onmouseover="ChangeColor(this, true);"
																							onmouseout="ChangeColor(this, false);" href="#"
																							onclick="javascript:returnBackToParent('<s:property value="id"/>','<s:property value="workOrderNumber"/>')"
																							id="getdate" style="cursor: hand">
																							<td width="17%" class="whitebox3wka">
																								<s:label value="%{#stat.index + 1}" />
																							</td>
																							<td width="83%" class="whitebox3wka">
																								<s:property value="workOrderNumber"/>
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
											* <s:text name='workorder.serach.mandatory' />
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
			<input type="button" class="buttonfinal" value="CLEAR" id="button" name="clear" onclick="clearAll();">
			&nbsp;
			<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();" />
			</div>
		</s:form>
		
		<script type="text/javascript">
			function clearAll() {
				$('searchWorkOrderForm').reset();
				$('contractorId').value= '-1';
				$('tenderNo').value = '';
				$('createDate').clear();
				$('workorderNo').clear();
			}
		
			function validateAndSubmit() {
				if($F('contractorId') == -1
						&& $F('tenderNo') == ''
						&& $F('workorderNo').blank()
						&& $F('createDate').blank()){
						
			    	$('date_error').hide();
					$('mandatory_length_error').hide();	
					$('mandatory_error').show();
					$('form_error').show();				
					return false;
				}
				else
					$('mandatory_error').hide();
				
				if(!$F('workorderNo').blank() && $F('workorderNo').strip().length < 4) {
					$('mandatory_length_error').show();		
					$('form_error').show();			
					return false;
				}
				else
					$('mandatory_length_error').hide();	
				
				if(!checkDate($('createDate'))) {
					return false;
				}
						
				$('form_error').hide();
				document.searchWorkOrderForm.action='${pageContext.request.contextPath}/workorder/workOrder!searchWOForMB.action';
    			document.searchWorkOrderForm.submit();
			}
			
			function checkDate(obj){
				if(!validateDateFormat(obj)) {
					$('mandatory_error').hide();
			    	$('date_error').show();
			    	$('form_error').show();
					$('createDate').focus();
			    	return false;
				}
				
				return true;
			}
			
			function returnBackToParent(workOrderId,workOrderNumber) {
				//document.all.ret.value =  workOrderId+ '`~`' + workOrderCode+ '`~`' + workOrdername;
				var wind;
				var data = new Array();
				wind=window.dialogArguments;
		
				if(wind==undefined){
					wind=window.opener;
					data=workOrderId+ '`~`' + workOrderNumber;
					window.opener.update(data);
				}
		
				else{
					wind=window.dialogArguments;
					wind.result=workOrderId+ '`~`' + workOrderNumber;
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