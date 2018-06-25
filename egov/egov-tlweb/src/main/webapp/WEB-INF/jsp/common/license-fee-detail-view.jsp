<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2018  eGovernments Foundation
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
<s:set value="outstandingFee" var="feeInfo"/>
<s:if test="%{id !=null}">
    <div class="panel panel-primary">
        <div class="panel-heading custom_form_panel_heading">
            <div class="panel-title">
                <s:text name='license.title.feedetail'/>
            </div>
        </div>

        <div class="panel-body">
            <s:if test="%{#attr.feeInfo.size > 0 }">
                <table class="table table-bordered " style="width:97%;margin:0 auto;">
                    <thead>
                    <tr>
                        <th><s:text name='license.fee.type'/></th>
                        <th><s:text name='license.fee.current'/></th>
                        <th><s:text name='license.fee.arrears'/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <s:iterator value="feeInfo" var="fee" status="status">
                        <tr>
                            <td>${fee.key}</td>
                            <td>${fee.value['current']}</td>
                            <td>${fee.value['arrear']}</td>
                        </tr>
                    </s:iterator>
                    </tbody>
                </table>
            </s:if>

            <table border="0" style="width:97%;margin:0 auto;">
                <tr>
                    <td colspan="3">
                        <a name="viewdcb" class="btn btn-secondary " id="viewdcb-btn"
                           onclick="window.open('/tl/dcb/view/' + '<s:property value="%{uid}"/>', '_blank', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');"><s:text
                                name='license.show.dcb'/></a>
                    </td>
                </tr>
            </table>

        </div>
    </div>
</s:if>

