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

<div class="display-hide report-section">
	<div id="searchCriteria" class="text-center"></div>
	<div id="dataRun" class="text-center"></div>
	<div class="form-group report-table-container">
		<table class="table table-bordered table-responsive table-hover multiheadertbl"
			id="resultTable">
			<thead>
                            <tr>
                             <th rowspan="2"><spring:message code="lbl.slno" /></th>
                             <th rowspan="2"><spring:message code="lbl.election.ward" /></th>
                             <th rowspan="2"><spring:message code="lbl.contractor.name" /></th>
                             <th colspan="2"><spring:message code="lbl.loacreated" /></th>
                             <th colspan="2"><spring:message code="lbl.sitenothandedover" /></th>
                             <th colspan="2"><spring:message code="lbl.sitehandedover.notworkcommenced" /></th>
                             <th colspan="2"><spring:message code="lbl.workcommenced.inprogess" /></th>
                             <th colspan="2"><spring:message code="lbl.workcompleted" /></th>
                             <th colspan="2"><spring:message code="lbl.balancework" /></th>
                             <th rowspan="2"><spring:message code="lbl.liableamt" /></th>
                            </tr>
							<tr>
								<th><spring:message code="lbl.noofestimates" /></th>
								<th><spring:message code="lbl.amtcrores" /></th>
								<th><spring:message code="lbl.noofestimates" /></th>
								<th><spring:message code="lbl.amtcrores" /></th>
								<th><spring:message code="lbl.noofestimates" /></th>
								<th><spring:message code="lbl.amtcrores" /></th>
								<th><spring:message code="lbl.noofestimates" /></th>
								<th><spring:message code="lbl.amtcrores" /></th>
								<th><spring:message code="lbl.noofestimates" /></th>
								<th><spring:message code="lbl.amtcrores" /></th>
								<th><spring:message code="lbl.noofestimates" /></th>
								<th><spring:message code="lbl.amtcrores" /></th>
							</tr>
						</thead>
			<tbody class="no-pointer" >
			</tbody>
		</table>
	</div>
	<div class="row">
	<br/>
		<div class="col-sm-12 text-center">
			<button style="display: none;" type='button' class='btn btn-primary' id="btndownloadpdf">
				<spring:message code='lbl.download.pdf' />
			</button>
			<button style="display: none;" type='button' class='btn btn-primary' id="btndownloadexcel">
				<spring:message code='lbl.download.excel' />
			</button>
		</div>
	</div>
</div>