<%@ include file="/includes/taglibs.jsp" %>
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

<div id="content" class="printable">
    <div class="formmainbox panel-primary">
        <div class="subheadnew text-center" id="headingdiv">
            <s:text name="licensefee.verification.title"/>
        </div>
        <table>
            <tr>
                <td align="left" style="color: #FF0000">
                    <s:actionerror cssStyle="color: #FF0000"/>
                    <s:fielderror/>
                    <s:actionmessage/>
                </td>
            </tr>
        </table>
        <s:form action="LicenseBillCollect" theme="simple" name="viewForm">
        <s:set value="outstandingFee" var="feeInfo"></s:set>
        <s:if test="%{#attr.feeInfo.size > 0}">
            <div class="panel-heading  custom_form_panel_heading subheadnew">
                <div class="panel-title"><s:text name='title.licensefee.detail'/></div>
            </div>
            <table class="table table-bordered" style="width:97%;margin:0 auto;">
                <thead>
                <tr>
                    <th>Installment Year</th>
                    <th>Feetype</th>
                    <th>Amount</th>
                </tr>
                </thead>
                <tbody>
                <s:iterator value="feeInfo" var="installment" status="status">
                    <s:iterator value="%{#attr.installment.value}" var="val" status="status">
                        <tr>
                            <td>${installment.key}</td>
                            <td>${val.key}</td>
                            <td>${val.value}</td>
                        </tr>
                    </s:iterator>
                </s:iterator>
                </tbody>
            </table>
        </s:if>
    </div>
</div>

<div align="center" class="buttonbottom" id="buttondiv">
    <a href="#" class="btn btn-primary"
       onclick="window.open('/tl/integration/licenseBillCollect-collectfees.action?licenseId='+${licenseId}, '_self'); return false;">Continue
        to payment</a>
    <a href='javascript:void(0)' class='btn btn-default' onclick='self.close()'>Close</a>
</div>
</s:form>