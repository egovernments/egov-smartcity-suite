<script>
</script>
<table width="100%" cellspacing="0" cellpadding="0" border="0" >
<br>
	<tr>
		<td colspan="4" class="headingwk">
			<div class="arrowiconwk">
				<img src="${pageContext.request.contextPath}/image/arrow.gif" />  
			</div>
			<div class="headplacer"><s:text name="depositworks.roadCut.updateDate.title" /></div>
		</td>
	</tr>
	
	 <tr> 
			<td width="11%" class="whiteboxwk"><s:text name="depositworks.roadcut.updateDate.startdate" />:</td>
			<td width="21%" class="whitebox2wk"><s:date name="applicationRequest.workStartDate" var="roadCutStartDate"	format="dd/MM/yyyy" />
									<s:textfield name="applicationRequest.workStartDate" id="rcStartDate"
										cssClass="selectwk" value="%{roadCutStartDate}"
										onfocus="javascript:vDateType='3';"
										onkeyup="DateFormat(this,this.value,event,false,'3')" />
										
									<a	name="startDatelnk" id="startDatelnk" href="javascript:show_calendar('forms[0].rcStartDate',null,null,'DD/MM/YYYY');"
										onmouseover="window.status='Date Picker';return true;"
										onmouseout="window.status='';return true;"> <img
											src="${pageContext.request.contextPath}/image/calendar.png"
											alt="Calendar" width="16" height="16" border="0"
											align="absmiddle" />
									</a>
			</td>
			<td width="11%" class="whiteboxwk"><s:text name="depositworks.roadcut.updateDate.enddate" />:</td>
			<td width="21%" class="whitebox2wk"><s:date name="applicationRequest.workEndDate" var="roadCutEndDate"	format="dd/MM/yyyy" />
												<s:textfield name="applicationRequest.workEndDate" id="rcEndDate"
													value="%{roadCutEndDate}" cssClass="selectwk"
													onfocus="javascript:vDateType='3';"
													onkeyup="DateFormat(this,this.value,event,false,'3')" />
												<a	name="endDatelnk" id="endDatelnk" href="javascript:show_calendar('forms[0].rcEndDate',null,null,'DD/MM/YYYY');"
													onmouseover="window.status='Date Picker';return true;"
													onmouseout="window.status='';return true;"> <img
														src="${pageContext.request.contextPath}/image/calendar.png"
														alt="Calendar" width="16" height="16" border="0"
														align="absmiddle" /> 
												</a>
			</td>	 
	   </tr>
</table>