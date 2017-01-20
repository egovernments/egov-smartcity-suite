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

<%@ taglib prefix="s" uri="/WEB-INF/taglib/struts-tags.tld" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<html>
<head>
    <title>Acknowledgement Slip for Trade License</title>
</head>
<body onload="refreshInbox()">
<div id="main" class="printable">
    <div class="row">
        <div class="col-md-12">
            <s:form name="certificateform" action="viewTradeLicense">
                <s:push value="model">
                    <div class="panel panel-primary" data-collapsed="0">
                        <div class="panel-heading">
                            <div class="panel-title text-center no-float">
                                <s:text name="license.acknowledgement.slip.for.tradelicense"/>
                            </div>
                        </div>
                        <div class="panel-body">
                            <div class="row add-border">
                                <div class="col-sm-3 col-xs-6 add-margin">
                                    <s:text name="license.applicationnumber"/>
                                </div>
                                <div class="col-sm-3 col-xs-6 add-margin view-content">
                                    <s:property value="applicationNumber"/>
                                </div>
                                <div class="col-sm-3 col-xs-6 add-margin">
                                    <s:text name="license.applicationdate"/>
                                </div>
                                <div class="col-sm-3 col-xs-6 add-margin view-content">
                                    <s:date name="applicationDate" format="dd-MMM-yyyy" var="applicationDateFrmttd"/>
                                    <s:property value="%{applicationDateFrmttd}"/>
                                </div>
                            </div>
                            <div class="row add-border">
                                <div class="col-sm-3 col-xs-6 add-margin">
                                    <s:text name="license.applied.for"/>
                                </div>
                                <div class="col-sm-3 col-xs-6 add-margin view-content">
                                    <s:property value="tradeName.name"/>
                                </div>
                            </div>
                            <div class="row add-border">
                                <div class="col-sm-3 col-xs-6 add-margin">
                                    <s:text name="license.division"/>
                                </div>
                                <div class="col-sm-3 col-xs-6 add-margin view-content">
                                    <s:property value="parentBoundary.name"/>
                                </div>
                                <div class="col-sm-3 col-xs-6 add-margin">
                                    <s:text name="licensee.applicantname"/>
                                </div>
                                <div class="col-sm-3 col-xs-6 add-margin view-content">
                                    <s:property value="licensee.applicantName"/>
                                </div>
                            </div>
                            <div class="row add-border">
                                <div class="col-sm-3 col-xs-6 add-margin">
                                    <s:text name="licensee.address"/>
                                </div>
                                <div class="col-sm-3 col-xs-6 add-margin view-content">
                                    <s:if test="%{licensee.address!=null}"><s:property
                                            value="licensee.address"/>
                                    </s:if>
                                </div>
                                <div class="col-sm-3 col-xs-6 add-margin">
                                    <s:text name="license.amount.to.be.paid"/>
                                </div>
                                <div class="col-sm-3 col-xs-6 add-margin view-content">
                                    <s:property value="getPayableAmountInWords()"/>
                                </div>
                            </div>


                            <div class="row add-border">
                                <div class="col-sm-3 col-xs-6 add-margin">
                                    <s:text name="license.startdate"/>
                                </div>
                                <div class="col-sm-3 col-xs-6 add-margin view-content">
                                    <s:date name="commencementDate" format="dd-MMM-yyyy" var="commencementDateFrmttd"/>
                                    <s:property value="%{commencementDateFrmttd}"/>
                                </div>
                            </div>

                        </div>
                    </div>
                </s:push>
            </s:form>
        </div>
    </div>
</div>
<div align="center">
    <input type="button" id="print" class="button printbtn" value="Print"
    /> &nbsp;&nbsp; <input
        type="button" id="close" value="Close" class="button"
        onclick="javascript:window.close();"/>
</div>
<script src="<cdn:url  value='/resources/global/js/jquery/plugins/jQuery.print.js' context='/egi'/>"></script>
</body>
</html>
