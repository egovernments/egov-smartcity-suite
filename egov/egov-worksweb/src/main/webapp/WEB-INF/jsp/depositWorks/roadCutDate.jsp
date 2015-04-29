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
<div  id="restorationDates_div" style="display:none;">
	<br>
	<table width="100%" cellspacing="0" cellpadding="0" border="0" >
		<tr>
			<td colspan="4" class="headingwk">
				<div class="arrowiconwk">
					<img src="${pageContext.request.contextPath}/image/arrow.gif" /> 
				</div>
				<div class="headplacer"><s:text name="depositworks.roadcut.restoration.dates.title" /></div>
			</td>
		</tr>
		 <tr> 
			<td width="11%" class="whiteboxwk"><s:text name="depositworks.roadcut.updateDate.restoration.startdate" />:</td>
			<td width="21%" class="whitebox2wk"><s:date name="applicationRequest.restorationWorkStartDate" var="roadCutRestorationStartDate"	format="dd/MM/yyyy" />
									<s:textfield name="applicationRequest.restorationWorkStartDate" id="rcRestorationStartDate"
										cssClass="selectwk" value="%{roadCutRestorationStartDate}"
										onfocus="javascript:vDateType='3';"
									onkeyup="DateFormat(this,this.value,event,false,'3')" />
									
								<a	name="restorationStartDatelnk" id="restorationStartDatelnk" href="javascript:show_calendar('forms[0].rcRestorationStartDate',null,null,'DD/MM/YYYY');"
									onmouseover="window.status='Date Picker';return true;"
									onmouseout="window.status='';return true;"> <img
										src="${pageContext.request.contextPath}/image/calendar.png"
											alt="Calendar" width="16" height="16" border="0"
											align="absmiddle" />
									</a>
			</td>
			<td width="11%" class="whiteboxwk"><s:text name="depositworks.roadcut.updateDate.restoration.enddate" />:</td>
			<td width="21%" class="whitebox2wk"><s:date name="applicationRequest.restorationWorkEndDate" var="roadCutRestorationEndDate"	format="dd/MM/yyyy" />
												<s:textfield name="applicationRequest.restorationWorkEndDate" id="rcRestorationEndDate"
													value="%{roadCutRestorationEndDate}" cssClass="selectwk"
													onfocus="javascript:vDateType='3';"
												onkeyup="DateFormat(this,this.value,event,false,'3')" />
											<a	name="restorationEndDatelnk" id="restorationEndDatelnk" href="javascript:show_calendar('forms[0].rcRestorationEndDate',null,null,'DD/MM/YYYY');"
												onmouseover="window.status='Date Picker';return true;"
												onmouseout="window.status='';return true;"> <img
													src="${pageContext.request.contextPath}/image/calendar.png"
														alt="Calendar" width="16" height="16" border="0"
														align="absmiddle" /> 
												</a>
			</td>	 
		 </tr>
	</table>
</div>
<script>
<s:if test="%{((model.applicationRequest.restorationWorkEndDate!=null || model.applicationRequest.restorationWorkStartDate!=null ) && sourcepage=='search' && mode=='view')}">
document.getElementById("restorationDates_div").style.display="block";
</s:if>
</script>