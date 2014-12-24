<c:set var="trclass" value="greybox" />
<tr>
	<td colspan="5" class="headingwk">
		<div class="arrowiconwk">
			<img src="${pageContext.request.contextPath}/images/arrow.gif" height="20" />
		</div>
		<div class="headplacer">
			<s:text name='license.search.title' />
		</div>
	</td>
</tr>
<c:choose>
	<c:when test="${trclass=='greybox'}">
		<c:set var="trclass" value="bluebox" />
	</c:when>
	<c:when test="${trclass=='bluebox'}">
		<c:set var="trclass" value="greybox" />
	</c:when>
</c:choose>
<s:token />
<tr>
	<td class="<c:out value="${trclass}"/>">
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name="license.applicationnumber" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:textfield label="applNumber" tabindex="1" name="applNumber" value="%{applNumber}" size="20" onBlur="trimAll(this.value);" id="applNumber" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name="licensee.applicantName" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<span class="bluebox"> <s:textfield label="Applicant Name" tabindex="2" name="applicantName" value="%{applicantName}" size="30" onBlur="trimAll(this.value);" id="applicantName" /> </span>
	</td>
</tr>
<c:choose>
	<c:when test="${trclass=='greybox'}">
		<c:set var="trclass" value="bluebox" />
	</c:when>
	<c:when test="${trclass=='bluebox'}">
		<c:set var="trclass" value="greybox" />
	</c:when>
</c:choose>
<tr>
	<td class="<c:out value="${trclass}"/>">
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name="application.fromdate" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<span class="bluebox"> <s:date name="applicationFromDate" id="appFrmDateFmtd" format="dd/MM/yyyy" /> <s:textfield name="applicationFromDate" id="applicationFromDate" onfocus="waterMarkTextIn('applicationFromDate','dd/mm/yyyy');" onblur="validateDateFormat(this);waterMarkTextOut('applicationFromDate','dd/mm/yyyy');lessThanOrEqualToCurrentDate(this);" maxlength="10" size="10" value="%{appFrmDateFmtd}" tabindex="4" onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a href="javascript:show_calendar('forms[0].applicationFromDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"> <img src="${pageContext.request.contextPath}/images/calendaricon.gif" alt="Date" width="18" height="18" border="0" align="absmiddle" id="calenderImgId" /> </a>
		</span>
	</td>

	<td class="<c:out value="${trclass}"/>">
		<s:text name="application.todate" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:date name="applicationToDate" id="appToDateFmtd" format="dd/MM/yyyy" />
		<s:textfield name="applicationToDate" id="applicationToDate" onfocus="waterMarkTextIn('applicationToDate','dd/mm/yyyy');" onblur="validateDateFormat(this);waterMarkTextOut('applicationToDate','dd/mm/yyyy');lessThanOrEqualToCurrentDate(this);" maxlength="10" size="10" value="%{appToDateFmtd}" tabindex="4" onkeyup="DateFormat(this,this.value,event,false,'3')" />
		<a href="javascript:show_calendar('forms[0].applicationToDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"> <img src="${pageContext.request.contextPath}/images/calendaricon.gif" alt="Date" width="18" height="18" border="0" align="absmiddle" id="calenderImgId" /> </a>
	</td>
</tr>
<c:choose>
	<c:when test="${trclass=='greybox'}">
		<c:set var="trclass" value="bluebox" />
	</c:when>
	<c:when test="${trclass=='bluebox'}">
		<c:set var="trclass" value="greybox" />
	</c:when>
</c:choose>
<tr>
	<td class="<c:out value="${trclass}"/>">
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name="licensee.no" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:textfield label="licenseNumber" tabindex="1" name="licenseNumber" value="%{licenseNumber}" size="20" onBlur="trimAll(this.value);" id="licenseNumber" />
	</td>

	<td class="<c:out value="${trclass}"/>">
		<s:text name="license.oldlicensenum" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<span class="bluebox"> <s:textfield label="oldlicensenum" tabindex="2" name="oldLicenseNumber" value="%{oldLicenseNumber}" size="30" onBlur="trimAll(this.value);" id="oldLicenseNum" /> </span>
	</td>
</tr>
<c:choose>
	<c:when test="${trclass=='greybox'}">
		<c:set var="trclass" value="bluebox" />
	</c:when>
	<c:when test="${trclass=='bluebox'}">
		<c:set var="trclass" value="greybox" />
	</c:when>
</c:choose>

<tr>
	<td class="<c:out value="${trclass}"/>" width="5%">
	</td>
	<td class="<c:out value="${trclass}"/>" align="right">
		<s:text name="license.zone" />
	</td>
	<td class="<c:out value="${trclass}"/>" align="left">
		<s:select headerKey="-1" headerValue="%{getText('license.default.select')}" name="zone" id="zone" list="dropdownData.zoneList" listKey="id" listValue='name' onChange="setupAjaxDivision(this);" />
		<egov:ajaxdropdown id="populateDivision" fields="['Text','Value']" dropdownId='division' url='/common/commonAjax!populateDivisions.action' />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name="license.division" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:select headerKey="-1" headerValue="%{getText('license.default.select')}" name="division" id="division" list="dropdownData.divisionListLicense" listKey="id" listValue='name' value="division" />
	</td>
</tr>
<c:choose>
	<c:when test="${trclass=='greybox'}">
		<c:set var="trclass" value="bluebox" />
	</c:when>
	<c:when test="${trclass=='bluebox'}">
		<c:set var="trclass" value="greybox" />
	</c:when>
</c:choose>
<tr>
	<td class="<c:out value="${trclass}"/>" width="5%">
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name="license.tradename" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:select headerValue="%{getText('license.default.select')}" headerKey="-1" list="dropdownData.tradeNameList" listKey="id" listValue="name" name="tradeName" disabled="%{sDisabled}" id="tradeName" value="%{tradeName.id}" width="230" style="width: 230px" size="0"/>
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name="licensee.establishmentname" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<span class="bluebox"> <s:textfield label="establishmentname" tabindex="2" name="establishmentName" value="%{establishmentName}" size="30" onBlur="trimAll(this.value);" id="establishmentName" /> </span>
	</td>
</tr>
<c:choose>
	<c:when test="${trclass=='greybox'}">
		<c:set var="trclass" value="bluebox" />
	</c:when>
	<c:when test="${trclass=='bluebox'}">
		<c:set var="trclass" value="greybox" />
	</c:when>
</c:choose>
<tr>
	<td class="<c:out value="${trclass}"/>" width="5%"></td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name="license.feefrom" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:textfield name="licenseFeeFrom" id="licenseFeeFrom" value="%{licenseFeeFrom}" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name="license.feeto" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:textfield name="licenseFeeTo" id="licenseFeeTo" value="%{licenseFeeTo}" />
	</td>
</tr>
<c:choose>
	<c:when test="${trclass=='greybox'}">
		<c:set var="trclass" value="bluebox" />
	</c:when>
	<c:when test="${trclass=='bluebox'}">
		<c:set var="trclass" value="greybox" />
	</c:when>
</c:choose>

<tr>
	<td colspan="5" class="headingwk">
	<div class="arrowiconwk">
				<img src="${pageContext.request.contextPath}/images/arrow.gif" height="22"/>
			</div>
		<a href="#" onclick="flipAdvanceOptions();" class="advlink"  style="text-decoration: none">
			<div class="headplacer">
				<s:text name='license.title.advanceSearchOptions' /> 
			</div> 
		</a>
	</td>
</tr>
<tr>
	<td colspan="5">
		
		<div id="advSearchOpt" class="advSearchOpt">
			<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
				<tbody>
					
					<c:choose>
						<c:when test="${trclass=='greybox'}">
							<c:set var="trclass" value="bluebox" />
						</c:when>
						<c:when test="${trclass=='bluebox'}">
							<c:set var="trclass" value="greybox" />
						</c:when>
					</c:choose>
					<tr>
						<td class="<c:out value="${trclass}"/>" width="5%"></td>
						<td class="<c:out value="${trclass}"/>" width="41%">
						<s:checkbox name="licenseexpired" default="false" label="licenseexpired" id="licenseexpired" value="%{licenseexpired}">
							</s:checkbox>&nbsp;
							<s:text name="license.expiredlicense" /> 
						</td>
						
						<td class="<c:out value="${trclass}"/>">
						<s:checkbox name="licenseCancelled" default="false" label="licenseCancelled" id="licenseCancelled" value="%{licenseCancelled}">
							</s:checkbox>&nbsp;
							<s:text name="license.cancelledlicense" /> 
						</td>
						<td class="<c:out value="${trclass}"/>">
							&nbsp;
						</td>
						<td class="<c:out value="${trclass}"/>">
							&nbsp;
						</td>
					</tr>
					<c:choose>
						<c:when test="${trclass=='greybox'}">
							<c:set var="trclass" value="bluebox" />
						</c:when>
						<c:when test="${trclass=='bluebox'}">
							<c:set var="trclass" value="greybox" />
						</c:when>
					</c:choose>
					<tr>
						<td class="<c:out value="${trclass}"/>" width="5%"></td>
						<td class="<c:out value="${trclass}"/>">
						<s:checkbox name="licenseSuspended" default="false" label="licenseSuspended" id="licenseSuspended" value="%{licenseSuspended}">
							</s:checkbox>&nbsp;
							<s:text name="license.suspendedlicense" /> 
						</td>
						<td class="<c:out value="${trclass}"/>">
						<s:checkbox name="licenseObjected" default="false" label="licenseObjected" id="licenseObjected" value="%{licenseObjected}">
							</s:checkbox>&nbsp;
							<s:text name="license.objectedlicense" /> 
						</td>
						<td class="<c:out value="${trclass}"/>">
							&nbsp;
						</td>
						<td class="<c:out value="${trclass}"/>">
							&nbsp;
						</td></tr>
					<c:choose>
						<c:when test="${trclass=='greybox'}">
							<c:set var="trclass" value="bluebox" />
						</c:when>
						<c:when test="${trclass=='bluebox'}">
							<c:set var="trclass" value="greybox" />
						</c:when>
					</c:choose>
					<tr>
						<td class="<c:out value="${trclass}"/>" ></td>
						<td class="<c:out value="${trclass}"/>">
						<s:checkbox name="licenseRejected" default="false" label="isLicenseRejected" id="licenseRejected" value="%{licenseRejected}">
							</s:checkbox>&nbsp;
							<s:text name="license.rejectedlicense" /> 
						</td>
						<td class="<c:out value="${trclass}"/>">
							<s:checkbox name="motorInstalled" tabindex="14" disabled="%{sDisabled}" label="motorInstalled" id="motorInstalled" value="%{motorInstalled}">
							</s:checkbox>&nbsp;
							<s:text name="license.ismotorinstalled" />
						</td>
						<td class="<c:out value="${trclass}"/>">
							&nbsp;
						</td>
						<td class="<c:out value="${trclass}"/>">
							&nbsp;
						</td>
						
					</tr>
				</tbody>
			</table>
		</div>
	</td>
</tr>
<tr>
	<td colspan="5" class="headingwk">
		<div class="arrowiconwk">
			<img src="${pageContext.request.contextPath}/images/arrow.gif" height="20" />
		</div>
		<div class="headplacer">
			<s:text name='Search Notice' />
		</div>
	</td>
</tr>
<c:choose>
	<c:when test="${trclass=='greybox'}">
		<c:set var="trclass" value="bluebox" />
	</c:when>
	<c:when test="${trclass=='bluebox'}">
		<c:set var="trclass" value="greybox" />
	</c:when>
</c:choose>
<tr>
	<td class="<c:out value="${trclass}"/>" width="5%"></td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name="Notice Number" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:textfield name="noticeNumber" id="noticeNumber" value="%{noticeNumber}" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name="Document Number" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:textfield name="docNumber" id="docNumber" value="%{docNumber}" />
	</td>
</tr>

<c:choose>
	<c:when test="${trclass=='greybox'}">
		<c:set var="trclass" value="bluebox" />
	</c:when>
	<c:when test="${trclass=='bluebox'}">
		<c:set var="trclass" value="greybox" />
	</c:when>
</c:choose>
<tr>
	<td class="<c:out value="${trclass}"/>" width="5%"></td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name="Notice Type" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:select headerKey="-1" headerValue="%{getText('license.default.select')}" name="noticeType" id="noticeType" list="dropdownData.noticetypelist"  />
	</td>
	<td class="<c:out value="${trclass}"/>">
		&nbsp;
	</td>
	<td class="<c:out value="${trclass}"/>">
	&nbsp;
	</td>
</tr>
<tr>
	<td class="<c:out value="${trclass}"/>" width="5%"></td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name="Notice From Date" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:date name="noticeFromDate" id="noticeFromDateFmtd" format="dd/MM/yyyy" />
		<s:textfield name="noticeFromDate" id="noticeFromDate" onfocus="waterMarkTextIn('noticeFromDate','dd/mm/yyyy');" onblur="validateDateFormat(this);waterMarkTextOut('noticeFromDate','dd/mm/yyyy');lessThanOrEqualToCurrentDate(this);" maxlength="10" size="10" value="%{noticeFromDateFmtd}" tabindex="4" onkeyup="DateFormat(this,this.value,event,false,'3')" />
		<a href="javascript:show_calendar('forms[0].noticeFromDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"> <img src="${pageContext.request.contextPath}/images/calendaricon.gif" alt="Date" width="18" height="18" border="0" align="absmiddle" id="calenderImgId" /> </a>
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name="Notice To Date" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:date name="noticeToDate" id="noticeToDateFmtd" format="dd/MM/yyyy" />
		<s:textfield name="noticeToDate" id="noticeToDate" onfocus="waterMarkTextIn('noticeToDate','dd/mm/yyyy');" onblur="validateDateFormat(this);waterMarkTextOut('noticeToDate','dd/mm/yyyy');lessThanOrEqualToCurrentDate(this);" maxlength="10" size="10" value="%{noticeToDateFmtd}" tabindex="4" onkeyup="DateFormat(this,this.value,event,false,'3')" />
		<a href="javascript:show_calendar('forms[0].noticeToDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"> <img src="${pageContext.request.contextPath}/images/calendaricon.gif" alt="Date" width="18" height="18" border="0" align="absmiddle" id="calenderImgId" /> </a>
	</td>
</tr>