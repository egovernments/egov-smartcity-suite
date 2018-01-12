/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
$(document).ready(function () {
    var docTable;
    var id;

    if ($("#id").val())
        id = $("#id").val();
    else
        id = $("#licenseId").val();
    $('#getdocuments').click(function (e) {
        if (docTable == undefined)
            $.ajax({
                url: "/tl/license/document/" + id,
                type: "GET",
                async: false,
                dataType: "json",
                success: function (data) {
                    var applicationTypes = Object.keys(data);
                    for (var i = 0, len = applicationTypes.length; i < len; i++) {
                        var applicationType = applicationTypes[i];
                        var documentDetails = Object.keys(data[applicationType]);
                        if (documentDetails.length > 0) {
                            $.each(documentDetails, function (j) {
                                var table = $("<table/>");
                                var documentTypeName = documentDetails[j];
                                var licenseDocument = data[applicationType][documentTypeName];

                                var files = [];
                                for (var fileIndex = 0; fileIndex < licenseDocument.length; fileIndex++) {
                                    files = files.concat(licenseDocument[fileIndex].files);
                                }
                                if (files != "") {
                                    docTable = table.DataTable({
                                        destroy: true,
                                        sort: false,
                                        filter: false,
                                        searching: false,
                                        responsive: true,
                                        data: files,
                                        columns: [
                                            {
                                                "data": function (row) {
                                                    return {fileName: row.fileName, fileStoreId: row.fileStoreId};
                                                },
                                                "sTitle": "File Name",
                                                "render": function (data, type, row) {
                                                    if (data) {
                                                        return '<a href="javascript:void(0);" onclick="viewDocument(\''
                                                            + data.fileStoreId
                                                            + '\');">'
                                                            + data.fileName + '</a>';
                                                    }
                                                    else return "NA";
                                                },
                                            }, {
                                                "data": "createdDate",
                                                "sTitle": "Uploaded Date",
                                                "render": function (data) {
                                                    var date = new Date(data);
                                                    var month = date.getMonth() + 1;
                                                    return date.getDate() + "/" + (month.length > 1 ? month : "0" + month) + "/" + date.getFullYear();
                                                }
                                            }
                                        ]
                                    });

                                    $("#" + applicationType.toLowerCase() + "Tbl").append("<strong><h4 div class='panel-heading'>" +
                                        "<div class='panel-title aqua-background'>" + documentTypeName + "</div></h4></strong>").append(table).append("<br/>");
                                }

                            });
                        }
                        else {
                            $("#" + applicationType.toLowerCase() + "Tbl").append("<strong><h4 div class='align-center'><span>No documents are attached</span></h4></strong>");
                        }
                    }
                }
            });
    });
});