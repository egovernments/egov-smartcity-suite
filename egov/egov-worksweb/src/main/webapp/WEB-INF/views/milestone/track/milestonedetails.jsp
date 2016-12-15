<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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
  --%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="lbl.milestonedetails" />
		</div>
	</div>
	<input type="hidden" id="notCompletedPercentageError" value='<spring:message code="error.trackmilestone.not.completed.percentage.hundred" />' />
	<div class="panel-body">
		<div class="alert text-left" style="color: red;" id="errorMessage" hidden="true">
		</div>
		<table class="table table-bordered" id="tblmilestone">
			<thead>
				<tr>
					<th width="3%"><spring:message code="lbl.stageordernumber" /></th>
					<th width="20%"><spring:message code="lbl.stagedescription" /></th>
					<th width="5%"><spring:message code="lbl.percentage.stage" /></th>
					<th><spring:message code="lbl.schedulestartdate" /></th>
					<th><spring:message code="lbl.scheduleenddate" /></th>
					<th width="20%"><spring:message code="lbl.currentstatus.stage" /><span
						class="mandatory"></span></th>
					<th width="5%"><spring:message code="lbl.completed.percentage.stage" /></th>
					<th width="5%"><spring:message code="lbl.actual.percentage.stage" /></th>
					<th><spring:message code="lbl.completion.date" /></th>
					<th width="20%"><spring:message code="lbl.reason.for.delay" /></th>
				</tr>
			</thead>
			<tbody id="milestoneDetailsTbl">
				<tr id="milestoneRow">
					<td align="center" class="stageOrderNumber_0">
						<fmt:formatNumber pattern="###" value="" />
					</td>
					<td class='description_0'>
						<c:out value="" />
					</td>
					<td align="right" class="percentage_0">
						<c:out value="" />
					</td>
					<td class='scheduleStartDate_0'>
					</td>
					<input type="hidden" name="hiddenScheduleStartDate_0" id="hiddenScheduleStartDate_0" class="hiddenScheduleStartDate_0"
							value='' />
					<td class='scheduleEndDate_0'>
					</td>
					<input type="hidden" name="hiddenScheduleEndDate_0" id="hiddenScheduleEndDate_0" class="hiddenScheduleEndDate_0"
							value='' />
					<td class="">
						<select name="trackMilestone[0].activities[0].status" data-first-option="false" id="currentStatus_0" class="form-control"
						required="required" onchange="makeCompletionMandatory(this);">
							<c:forEach items="${currentStatus}" var="cs">
								<option value="${cs }">${cs.toString() }</option>
							</c:forEach>
						</select>
					</td>
					<td class="">
						<input name="trackMilestone[0].activities[0].completedPercentage"
							id="completedPercentage_0" value=""
							data-errormsg="Completed Percentage is mandatory!" data-idx="0"
							data-optional="" maxlength="3"
							class="form-control table-input text-right completedPercentage"
							onkeyup="validateCompletedPercentage(this);" onblur="validatePercentage(this);" />
					</td>
					<td class="">
						<input id="actualPercentage_0" name="actualPercentage_0" value=""
							data-errormsg="Actual Percentage is mandatory!" data-idx="0"
							data-optional=""
							class="form-control table-input text-right actualPercentage" readonly="true" />
					</td>
					<td class="">
						<input name="trackMilestone[0].activities[0].completionDate"
							value="" id="completionDate_0"
							data-errormsg="Completion Date is mandatory!" data-idx="0"
							data-optional="0" onchange="makeReasonMandatory(this);"
							class="form-control datepicker completionDate"
							maxlength="10" data-date-format="dd/mm/yyyy"
							data-inputmask="'mask': 'd/m/y'" />
					</td>
					<td class="">
						<input name="trackMilestone[0].activities[0].remarks"
							id="reasonForDelay_0" value=""
							data-errormsg="Reason For Delay is mandatory!" data-idx="0"
							data-optional="" maxlength="256" readonly="true"
							class="form-control table-input text-left reasonForDelay" />
					</td>
				</tr>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="2" class="text-right"><spring:message
							code="lbl.total" /></td>
					<td class="text-right"><span id="totalPercentage">0</span></td>
					<td colspan="4" class="text-right"><spring:message
							code="lbl.workcompletionpercentage" /></td>
					<td class="text-right"><span id="totalActualPercentage">0</span></td>
				</tr>
			</tfoot>
		</table>
	</div>
</div>
