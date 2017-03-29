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

<%@ include file="/includes/taglibs.jsp" %>
<s:if test="%{!licenseHistory.isEmpty}">
    <div class="panel-heading  custom_form_panel_heading subheadnew">
        <div class="panel-title"><s:text name='lbl.licensehistory'/></div>
    </div>
    <table class="table table-bordered" style="width:97%;margin:0 auto;">
        <thead>
        <tr>
            <th class="bluebgheadtd"><s:text name="lbl.wf.date"/></th>
            <th class="bluebgheadtd"><s:text name="lbl.wf.updatedby"/></th>
            <th class="bluebgheadtd"><s:text name="lbl.wf.currentowner"/></th>
            <th class="bluebgheadtd"><s:text name="lbl.wf.status"/></th>
            <th class="bluebgheadtd"><s:text name="lbl.wf.comments"/></th>
        </tr>
        </thead>
        <tbody>

        <s:iterator value="%{licenseHistory}" var="history">
            <tr>
                <td class="blueborderfortd" style="text-align: left"><s:date
                        name="#history.date" var="updatedDate"
                        format="dd/MM/yyyy hh:mm a"/> <s:property
                        value="#updatedDate"/></td>
                <td class="blueborderfortd" style="text-align: left"><s:property
                        value="%{#history.updatedBy}"/></td>
                <td class="blueborderfortd" style="text-align: left"><s:property
                        value="%{#history.user}"/></td>
                <td class="blueborderfortd" style="text-align: left"><s:property
                        value="%{#history.status}"/></td>
                <td class="blueborderfortd" style="text-align: left"><s:property
                        value="%{#history.comments}"/></td>
            </tr>
        </s:iterator>

        </tbody>
    </table>
</s:if>
