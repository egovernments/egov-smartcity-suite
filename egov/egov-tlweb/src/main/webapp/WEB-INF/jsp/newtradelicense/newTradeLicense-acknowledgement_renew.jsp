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

<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Acknowledgement Slip for Renewal of Trade License</title>
    <script>
        function refreshInbox() {
            if (opener && opener.top.document.getElementById('inboxframe')) {
                opener.top.document.getElementById('inboxframe').contentWindow.egovInbox
                    .refresh();
            }
        }
    </script>
</head>
<body onload="refreshInbox()">
<div id="message">
    <s:actionmessage/>
</div>
<div class="printable">
    <s:form name="certificateform" action="viewTradeLicense">
        <s:push value="model">
            <table width="100%" border="0" style="margin-left: 25px">
                <tr class="add-margin">
                    <td colspan="4" align="center">
                        <img src="<c:url value='/downloadfile/logo.jpg' context='/egi'/>" height="60">
                    </td>
                </tr>

                <tr>
                    <td colspan="4" align="center" style="font-size: 15px; font-weight: bolder;">
                            ${sessionScope.citymunicipalityname}
                        <br/>
                    </td>
                </tr>
                <tr>
                    <td colspan="4" align="center" style="font-size: 15px; font-weight: bolder;">
                        <s:text name="license.acknowledgement.slip.for.renew.tradelicense"/>
                        <br/>
                        <br/>
                        <br/>
                    </td>

                </tr>
                <tr>
                    <td width="40%">
                        <s:text name="license.licensenumber"/>
                        :
                    </td>
                    <td colspan="3">
                        <b><s:property value="licenseNumber"/>&nbsp;</b>
                    </td>
                </tr>
                <tr>
                    <td>
                        <s:text name="license.applied.for"/>
                        :
                    </td>
                    <td colspan="3">
                        <b><s:property value="tradeName.name"/>&nbsp;</b>
                    </td>
                </tr>
                <tr>
                    <td>
                        <s:text name="license.division"/>
                        :
                    </td>
                    <td colspan="3">
                        <b><s:property value="boundary.name"/>&nbsp;</b>
                    </td>
                </tr>
                <tr>
                    <td width="40%">
                        <s:text name="licensee.applicantname"/>
                        :
                    </td>
                    <td width="40%">
                        <b><s:property value="licensee.applicantName"/>&nbsp;</b>
                    </td>
                    <td colspan="2"/>
                </tr>
                <tr>
                    <td width="40%">
                        <s:text name="licensee.address"/>
                        :
                    </td>
                    <td width="40%">
                        <b> <s:property value="licensee.address" default="N/A"/></b>
                    </td>
                    <td colspan="2"/>
                </tr>
                <tr>
                    <td width="20%">
                        <s:text name="license.amount.to.be.paid"/>
                        :
                    </td>
                    <td colspan="3">
                        <b><s:property value="getPayableAmountInWords()"/>&nbsp;</b>
                    </td>
                </tr>
                <tr>
                    <td colspan="4">
                        <s:text name="license.renew.acknowledgement.bottom.text"/>
                    </td>
                </tr>
            </table>
        </s:push>
    </s:form>
</div>
<div align="center">
    <s:if test="%{hasCscOperatorRole == true}">
        <input type="button" value="Print" name="PrintAck" id="PrintAck" class="button"
               onclick="window.open('/tl/newtradelicense/newtradelicense-printAck.action?model.id=<s:property value="%{id}"/>',
                       '_blank', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');">
    </s:if>
    <s:else>
        <input type="button" id="print" class="button printbtn" value="Print"/> </s:else>
    &nbsp;&nbsp;
    <input type="button" id="close" value="Close" class="button" onclick="javascript:window.close();"/>
</div>

<script src="<cdn:url  value='/resources/global/js/jquery/plugins/jQuery.print.js' context='/egi'/>"></script>
</body>
</html>
