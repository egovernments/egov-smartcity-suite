<table id="dlpDataTable" style="display: none;" width="100%" border="0" cellspacing="0" cellpadding="0">
				<s:if test="%{isLegacyProjectCode=='yes'}">
					<tr id="dlpDateFieldsForLegacyTR">
							<td class="whiteboxwk" id="dlpLabel"><span class="mandatory">*</span>
					   			<s:text name="defect.liability.period"/> :</td>
							<td class="whitebox2wk" id="dlpField">
								<s:textfield  name="defectLiabilityPeriod" value="%{defectLiabilityPeriod}" id="defectLiabilityPeriod" cssClass="selectamountwk" onblur="roundOffDLP()" />
							</td>
							<td class="whiteboxwk" id="completionDateLabel"><span class="mandatory">*</span>
					        	<s:text name="contractorBill.completionDate"/> :
					        </td>
					        <td class="whitebox2wk" id="completionDateField">
								<s:date name="workCompletionDate" var="completionDateFormat" format="dd/MM/yyyy"/>
					        	<s:textfield name="workCompletionDate" value="%{completionDateFormat}" id="workCompletionDate" cssClass="selectboldwk" 
					        		 onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')"  maxlength="10"/>
					        		 <a href="javascript:show_calendar('forms[0].workCompletionDate',null,null,'DD/MM/YYYY');" 
					        		 onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;">
					        		 <img src="${pageContext.request.contextPath}/image/calendar.png" id="wpDateImg" alt="Calendar" width="16" height="16" 
					       			  border="0" align="absmiddle" /></a> 
					       	</td>		  
					</tr>
					<tr id="estimateAndWONumberTR">
									<td class="whiteboxwk" id="estimateNoLabel">
													<s:text name="estimate.number" />:
									</td>
									<td class="whitebox2wk" id="estimateNoField">
													<s:textfield name="estimateNumber" id="estimateNumber" maxlength="50" onChange="estimateNoUniqueCheck(this);" onBlur="estimateNoUniqueCheck(this);"	cssClass="selectboldwk"/>
									</td>
									<td class="whiteboxwk" id="workOrderNoLabel">
													<s:text name='workOrder.search.label.workOrderNo' />:
									</td>
									<td class="whitebox2wk" id="workOrderNoField">
													<s:textfield name="workOrderNumber" id="workOrderNumber" maxlength="50" cssClass="selectboldwk"/>
									</td>
					</tr>
				</s:if>
				<s:else>
					<tr id="dlpDateFieldsForWorksTR">
							<td class="whiteboxwk" id="dlpLabel"><span class="mandatory">*</span>
					   			<s:text name="defect.liability.period"/> :</td>
							<td class="whitebox2wk" id="dlpField">
								<s:textfield  name="defectLiabilityPeriod" value="%{defectLiabilityPeriod}" id="defectLiabilityPeriod" cssClass="selectamountwk" onblur="roundOffDLP()" />
							</td>
							<td colspan="2"></td>	  
					</tr>
				</s:else>	
				</table>