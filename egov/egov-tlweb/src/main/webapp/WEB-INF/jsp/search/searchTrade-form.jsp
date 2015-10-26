<!-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#     accountability and the service delivery of the government  organizations.
#  
#      Copyright (C) <2015>  eGovernments Foundation
#  
#      The updated version of eGov suite of products as by eGovernments Foundation 
#      is available at http://www.egovernments.org
#  
#      This program is free software: you can redistribute it and/or modify
#      it under the terms of the GNU General Public License as published by
#      the Free Software Foundation, either version 3 of the License, or
#      any later version.
#  
#      This program is distributed in the hope that it will be useful,
#      but WITHOUT ANY WARRANTY; without even the implied warranty of
#      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#      GNU General Public License for more details.
#  
#      You should have received a copy of the GNU General Public License
#      along with this program. If not, see http://www.gnu.org/licenses/ or 
#      http://www.gnu.org/licenses/gpl.html .
#  
#      In addition to the terms of the GPL license to be adhered to in using this
#      program, the following additional terms are to be complied with:
#  
#  	1) All versions of this program, verbatim or modified must carry this 
#  	   Legal Notice.
#  
#  	2) Any misrepresentation of the origin of the material is prohibited. It 
#  	   is required that all modified versions of this material be marked in 
#  	   reasonable ways as different from the original version.
#  
#  	3) This license does not grant any rights to any user of the program 
#  	   with regards to rights under trademark law for use of the trade names 
#  	   or trademarks of eGovernments Foundation.
#  
#    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------->
<c:set var="trclass" value="greybox" />
<tr>
	<td colspan="5" class="headingwk">
		<div class="subheadnew text-left">
			<s:text name='license.search.title' />
		</div>
	</td>
</tr>
<s:token />
<tr>
	<td class="greybox">
	</td>
	<td class="greybox">
		<s:text name="license.applicationnumber" />
	</td>
	<td class="greybox">
		<s:textfield label="applNumber" tabindex="1" name="applNumber" value="%{applNumber}" size="20" onBlur="trimAll(this.value);" id="applNumber" />
	</td>
	<td class="greybox">
		<s:text name="licensee.applicantName" />
	</td>
	<td class="greybox">
		<span class="bluebox"> <s:textfield label="Applicant Name" tabindex="2" name="applicantName" value="%{applicantName}" size="30" onBlur="trimAll(this.value);" id="applicantName" /> </span>
	</td>
</tr>
<tr>
	<td class="greybox">
	</td>
	<td class="greybox">
		<s:text name="application.fromdate" />
	</td>
	<td class="greybox">
		<span class="bluebox"> <s:date name="applicationFromDate" id="appFrmDateFmtd" format="dd/MM/yyyy" /> <s:textfield name="applicationFromDate" id="applicationFromDate" onfocus="waterMarkTextIn('applicationFromDate','dd/mm/yyyy');" onblur="validateDateFormat(this);waterMarkTextOut('applicationFromDate','dd/mm/yyyy');lessThanOrEqualToCurrentDate(this);" maxlength="10" size="10" value="%{appFrmDateFmtd}" tabindex="4" onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a href="javascript:show_calendar('forms[0].applicationFromDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"> <img src="${pageContext.request.contextPath}/resources/image/calendaricon.gif" alt="Date" width="18" height="18" border="0" align="absmiddle" id="calenderImgId" /> </a>
		</span>
	</td>

	<td class="greybox">
		<s:text name="application.todate" />
	</td>
	<td class="greybox">
		<s:date name="applicationToDate" id="appToDateFmtd" format="dd/MM/yyyy" />
		<s:textfield name="applicationToDate" id="applicationToDate" onfocus="waterMarkTextIn('applicationToDate','dd/mm/yyyy');" onblur="validateDateFormat(this);waterMarkTextOut('applicationToDate','dd/mm/yyyy');lessThanOrEqualToCurrentDate(this);" maxlength="10" size="10" value="%{appToDateFmtd}" tabindex="4" onkeyup="DateFormat(this,this.value,event,false,'3')" />
		<a href="javascript:show_calendar('forms[0].applicationToDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"> <img src="${pageContext.request.contextPath}/resources/image/calendaricon.gif" alt="Date" width="18" height="18" border="0" align="absmiddle" id="calenderImgId" /> </a>
	</td>
</tr>
<tr>
	<td class="greybox">
	</td>
	<td class="greybox">
		<s:text name="licensee.no" />
	</td>
	<td class="greybox">
		<s:textfield label="licenseNumber" tabindex="1" name="licenseNumber" value="%{licenseNumber}" size="20" onBlur="trimAll(this.value);" id="licenseNumber" />
	</td>

	<td class="greybox">
		<s:text name="license.oldlicensenum" />
	</td>
	<td class="greybox">
		<span class="bluebox"> <s:textfield label="oldlicensenum" tabindex="2" name="oldLicenseNumber" value="%{oldLicenseNumber}" size="30" onBlur="trimAll(this.value);" id="oldLicenseNum" /> </span>
	</td>
</tr>
<tr>
	<td class="greybox">
	</td>
	<td class="greybox">
		<s:text name="license.tradename" />
	</td>
	<td class="greybox">
		<s:select headerValue="%{getText('license.default.select')}" headerKey="-1" list="dropdownData.tradeNameList" listKey="id" listValue="name" name="tradeName" disabled="%{sDisabled}" id="tradeName" value="%{tradeName.id}" width="230" style="width: 230px" size="0"/>
	</td>
	<td class="greybox">
		<s:text name="licensee.establishmentname" />
	</td> 
	<td class="greybox">
		<span class="bluebox"> <s:textfield label="establishmentname" tabindex="2" name="establishmentName" value="%{establishmentName}" size="30" onBlur="trimAll(this.value);" id="establishmentName" /> </span>
	</td>
</tr>
<tr>
	<td class="greybox"></td>
	<td class="greybox">
		<s:text name="license.feefrom" />
	</td>
	<td class="greybox">
		<s:textfield name="licenseFeeFrom" id="licenseFeeFrom" value="%{licenseFeeFrom}" />
	</td>
	<td class="greybox">
		<s:text name="license.feeto" />
	</td>
	<td class="greybox">
		<s:textfield name="licenseFeeTo" id="licenseFeeTo" value="%{licenseFeeTo}" />
	</td>
</tr>
<tr>
	<td colspan="5" class="headingwk">
		<a href="#" onclick="flipAdvanceOptions();" class="advlink"  style="text-decoration: none">
			<div class="subheadnew text-left">
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
					<tr>
						<td class="greybox" width="5%"></td>
						<td class="greybox">
						<s:checkbox name="licenseexpired" default="false" label="licenseexpired" id="licenseexpired" value="%{licenseexpired}">
							</s:checkbox>&nbsp;
							<s:text name="license.expiredlicense" /> 
						</td>
						
						<td class="greybox">
						<s:checkbox name="licenseCancelled" default="false" label="licenseCancelled" id="licenseCancelled" value="%{licenseCancelled}">
							</s:checkbox>&nbsp;
							<s:text name="license.cancelledlicense" /> 
						</td>
						<td class="greybox">
							&nbsp;
						</td>
						<td class="greybox">
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="greybox" width="5%"></td>
						<td class="greybox">
						<s:checkbox name="licenseSuspended" default="false" label="licenseSuspended" id="licenseSuspended" value="%{licenseSuspended}">
							</s:checkbox>&nbsp;
							<s:text name="license.suspendedlicense" /> 
						</td>
						<td class="greybox">
						<s:checkbox name="licenseObjected" default="false" label="licenseObjected" id="licenseObjected" value="%{licenseObjected}">
							</s:checkbox>&nbsp;
							<s:text name="license.objectedlicense" /> 
						</td>
						<td class="greybox">
							&nbsp;
						</td>
						<td class="greybox">
							&nbsp;
						</td></tr>
					<tr>
						<td class="greybox" width="5%"></td>
						<td class="greybox">
						<s:checkbox name="licenseRejected" default="false" label="isLicenseRejected" id="licenseRejected" value="%{licenseRejected}">
							</s:checkbox>&nbsp;
							<s:text name="license.rejectedlicense" /> 
						</td>
						<td class="greybox">
							<s:checkbox name="motorInstalled" tabindex="14" disabled="%{sDisabled}" label="motorInstalled" id="motorInstalled" value="%{motorInstalled}">
							</s:checkbox>&nbsp;
							<s:text name="license.ismotorinstalled" />
						</td>
						<td class="greybox">
							&nbsp;
						</td>
						<td class="greybox">
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
		<div class="subheadnew text-left">
			<s:text name='Search Notice' />
		</div>
	</td>
</tr>
<tr>
	<td class="greybox" width="5%"></td>
	<td class="greybox">
		<s:text name="Notice Number" />
	</td>
	<td class="greybox">
		<s:textfield name="noticeNumber" id="noticeNumber" value="%{noticeNumber}" />
	</td>
	<td class="greybox">
		<s:text name="Document Number" />
	</td>
	<td class="greybox">
		<s:textfield name="docNumber" id="docNumber" value="%{docNumber}" />
	</td>
</tr>
<tr>
	<td class="greybox" width="5%"></td>
	<td class="greybox">
		<s:text name="Notice Type" />
	</td>
	<td class="greybox">
		<s:select headerKey="-1" headerValue="%{getText('license.default.select')}" name="noticeType" id="noticeType" list="dropdownData.noticetypelist"  />
	</td>
	<td class="greybox">
		&nbsp;
	</td>
	<td class="greybox">
	&nbsp;
	</td>
</tr>
<tr>
	<td class="greybox" width="5%"></td>
	<td class="greybox">
		<s:text name="Notice From Date" />
	</td>
	<td class="greybox">
		<s:date name="noticeFromDate" id="noticeFromDateFmtd" format="dd/MM/yyyy" />
		<s:textfield name="noticeFromDate" id="noticeFromDate" onfocus="waterMarkTextIn('noticeFromDate','dd/mm/yyyy');" onblur="validateDateFormat(this);waterMarkTextOut('noticeFromDate','dd/mm/yyyy');lessThanOrEqualToCurrentDate(this);" maxlength="10" size="10" value="%{noticeFromDateFmtd}" tabindex="4" onkeyup="DateFormat(this,this.value,event,false,'3')" />
		<a href="javascript:show_calendar('forms[0].noticeFromDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"> <img src="${pageContext.request.contextPath}/resources/image/calendaricon.gif" alt="Date" width="18" height="18" border="0" align="absmiddle" id="calenderImgId" /> </a>
	</td>
	<td class="greybox">
		<s:text name="Notice To Date" />
	</td>
	<td class="greybox">
		<s:date name="noticeToDate" id="noticeToDateFmtd" format="dd/MM/yyyy" />
		<s:textfield name="noticeToDate" id="noticeToDate" onfocus="waterMarkTextIn('noticeToDate','dd/mm/yyyy');" onblur="validateDateFormat(this);waterMarkTextOut('noticeToDate','dd/mm/yyyy');lessThanOrEqualToCurrentDate(this);" maxlength="10" size="10" value="%{noticeToDateFmtd}" tabindex="4" onkeyup="DateFormat(this,this.value,event,false,'3')" />
		<a href="javascript:show_calendar('forms[0].noticeToDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"> <img src="${pageContext.request.contextPath}/resources/image/calendaricon.gif" alt="Date" width="18" height="18" border="0" align="absmiddle" id="calenderImgId" /> </a>
	</td>
</tr>
