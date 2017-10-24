<%--
  ~ eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~ accountability and the service delivery of the government  organizations.
  ~
  ~  Copyright (C) <2017>  eGovernments Foundation
  ~
  ~  The updated version of eGov suite of products as by eGovernments Foundation
  ~  is available at http://www.egovernments.org
  ~
  ~  This program is free software: you can redistribute it and/or modify
  ~  it under the terms of the GNU General Public License as published by
  ~  the Free Software Foundation, either version 3 of the License, or
  ~  any later version.
  ~
  ~  This program is distributed in the hope that it will be useful,
  ~  but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~  GNU General Public License for more details.
  ~
  ~  You should have received a copy of the GNU General Public License
  ~  along with this program. If not, see http://www.gnu.org/licenses/ or
  ~  http://www.gnu.org/licenses/gpl.html .
  ~
  ~  In addition to the terms of the GPL license to be adhered to in using this
  ~  program, the following additional terms are to be complied with:
  ~
  ~      1) All versions of this program, verbatim or modified must carry this
  ~         Legal Notice.
  ~ 	Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~         Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~         derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~ 	For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~ 	For any further queries on attribution, including queries on brand guidelines,
  ~         please contact contact@egovernments.org
  ~
  ~      2) Any misrepresentation of the origin of the material is prohibited. It
  ~         is required that all modified versions of this material be marked in
  ~         reasonable ways as different from the original version.
  ~
  ~      3) This license does not grant any rights to any user of the program
  ~         with regards to rights under trademark law for use of the trade names
  ~         or trademarks of eGovernments Foundation.
  ~
  ~  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<%@ include file="/includes/taglibs.jsp" %>
<html>
<head>
    <title><s:text name="page.title.renewtrade"/></title>
</head>
<body>
<div class="row">
    <div class="col-md-12">
        <s:if test="%{hasErrors()}">
            <div align="center" class="error-msg">
                <s:actionerror/>
                <s:fielderror/>
            </div>
        </s:if>
        <s:if test="%{hasActionMessages()}">
            <div class="messagestyle">
                <s:actionmessage theme="simple"/>
            </div>
        </s:if>
        <s:form id="licenseForm" action="newTradeLicense-renewal" theme="simple" name="renewForm"
                enctype="multipart/form-data"
                cssClass="form-horizontal form-groups-bordered">
            <s:token/>
            <s:push value="model">
                <s:hidden name="docNumber"/>
                <s:hidden name="actionName" value="renew"/>
                <s:hidden name="model.id" id="id"/>
                <s:hidden id="detailChanged" name="detailChanged"></s:hidden>
                <s:hidden name="feeTypeId" id="feeTypeId"/>
                <s:hidden name="newWorkflow" id="newWorkflow"/>
                <input type="hidden" name="applicationNo" value="${param.applicationNo}"
                       id="applicationNo"/>
                <s:if test="%{status=='Active'}">
                    <s:hidden id="additionalRule" name="additionalRule" value="%{additionalRule}"/>
                </s:if>
                <div class="panel panel-primary" data-collapsed="0">
                    <div class="panel-heading">
                        <div class="panel-title" style="text-align:center">
                            <s:text name="page.title.renewtrade"/>
                        </div>
                        <ul class="nav nav-tabs" id="settingstab">
                            <li class="active"><a data-toggle="tab" href="#tradedetails" data-tabidx="0"
                                                  aria-expanded="true"><s:text name="license.tradedetail"/></a></li>
                            <li class=""><a data-toggle="tab" href="#tradeattachments" data-tabidx="1"
                                            aria-expanded="false"><s:text name="license.support.docs"/></a></li>
                        </ul>
                    </div>

                    <div class="panel-body">
                        <div class="tab-content">
                            <div class="tab-pane fade active in" id="tradedetails">
                                <%@ include file='../common/license-detail-view.jsp' %>

                                <div class="panel-heading custom_form_panel_heading">
                                    <div class="panel-title">Editable <s:text name='license.details.lbl'/></div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label text-right"><s:text
                                            name='license.locality.lbl'/><span class="mandatory"></span></label>
                                    <div class="col-sm-3 add-margin">
                                        <s:select name="boundary" id="boundary" list="dropdownData.localityList"
                                                  listKey="id" listValue="name" headerKey=""
                                                  headerValue="%{getText('default.select')}" required="true"
                                                  value="%{boundary.id}" class="form-control"/>
                                    </div>
                                    <label class="col-sm-2 control-label text-right"><s:text
                                            name='license.division'/><span class="mandatory"></span></label>
                                    <div class="col-sm-3 add-margin">
                                        <select name="parentBoundary" id="parentBoundary" class="form-control"
                                                required="true">
                                            <option value=""><s:text name='default.select'/></option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label text-right"><s:text
                                            name='license.category.lbl'/><span class="mandatory"></span></label>
                                    <div class="col-sm-3 add-margin">
                                        <s:select name="category" id="category" list="dropdownData.categoryList"
                                                  listKey="id" listValue="name" headerKey="-1"
                                                  headerValue="%{getText('default.select')}" value="%{category.id}"
                                                  class="form-control" onChange="setupAjaxSubCategory(this);"/>
                                        <egov:ajaxdropdown id="populateSubCategory" fields="['Text','Value']"
                                                           dropdownId='subCategory'
                                                           url='domain/commonTradeLicenseAjax-populateSubCategory.action'/>
                                    </div>

                                    <label class="col-sm-2 control-label text-right"><s:text
                                            name='license.subCategory.lbl'/><span class="mandatory"></span></label>
                                    <div class="col-sm-3 add-margin">
                                        <s:select name="tradeName" id="subCategory" list="dropdownData.subCategoryList"
                                                  listKey="id" listValue="name" headerKey="-1"
                                                  headerValue="%{getText('default.select')}" value="%{tradeName.id}"
                                                  class="form-control select2"/>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-sm-3 control-label text-right"><s:text
                                            name='license.uom.lbl'/><span class="mandatory"></span></label>
                                    <div class="col-sm-3 add-margin">
                                        <s:textfield name="uom" maxlength="8" id="uom"
                                                     value="%{tradeName.licenseSubCategoryDetails.iterator.next.uom.name}"
                                                     readOnly="true" class="form-control"/>
                                    </div>
                                    <label class="col-sm-2 control-label text-right"><s:text
                                            name='license.premises.lbl'/><span class="mandatory"></span></label>
                                    <div class="col-sm-3 add-margin">
                                        <s:textfield name="tradeArea_weight" maxlength="8" id="tradeArea_weight"
                                                     value="%{tradeArea_weight}"
                                                     cssClass="form-control patternvalidation"
                                                     data-pattern="number"/>
                                    </div>
                                </div>
                            </div>
                            <div class="tab-pane fade" id="tradeattachments">
                                <%@include file="../common/supportdocs-renew.jsp" %>
                            </div>
                        </div>
                    </div>
                </div>
                <s:if test="%{newWorkflow ==null}">
                    <s:if test="%{status!='Active'}">
                        <div class="panel panel-primary" id="workflowDiv">
                            <%@ include file='../common/license-workflow-dropdown.jsp' %>
                            <%@ include file='../common/license-workflow-button.jsp' %>
                        </div>
                    </s:if>
                    <s:else>
                        <div class="row">
                            <div class="text-center">
                                <button type="submit" id="btnsave" class="btn btn-primary" onclick="return onSubmit();">
                                    Save
                                </button>
                                <button type="button" id="btnclose" class="btn btn-default" onclick="window.close();">
                                    Close
                                </button>
                            </div>
                        </div>
                    </s:else>
                </s:if>
                <s:else>
                    <div class="panel panel-primary" id="workflowDiv">
                        <%@ include file='../common/license-workflow-dropdown.jsp' %>
                        <%@ include file='../common/license-workflow-button.jsp' %>
                    </div>
                </s:else>
            </s:push>
        </s:form>
    </div>
</div>
<script>
    $('#subCategory').change(function () {
        $('#uom').val('');
        if ($('#feeTypeId').val() !== '') {
            $.ajax({
                url: "/tl/licensesubcategory/detail-by-feetype",
                type: "GET",
                data: {
                    subCategoryId: $('#subCategory').val(),
                    feeTypeId: $('#feeTypeId').val()
                },
                cache: false,
                dataType: "json",
                success: function (response) {
                    if (response)
                        $('#uom').val(response.uom.name);
                    else {
                        $('#uom').val('');
                        bootbox.alert("No UOM mapped for the selected Sub Category");
                    }
                }
            });
        }
    });

    var parentBoundary = '${model.parentBoundary.id}';
    $(document).ready(function () {
        if ($('#boundary').val() != '') {
            $('#boundary').trigger('blur');
        }
    });

    function onSubmitValidations() {
        return true;
    }
    function onSubmit() {
        if (!validateEditableFields()) {
            return false;
        }
        document.renewForm.action = '${pageContext.request.contextPath}/newtradelicense/newTradeLicense-renewal.action';
        return true;
    }
    function validateEditableFields() {
        if ($('#licenseForm').valid()) {
            if (document.getElementById("category").value == '-1') {
                showMessage('newLicense_error', '<s:text name="newlicense.category.null" />');
                window.scroll(0, 0);
                return false;
            } else if (document.getElementById("subCategory").value == '-1') {
                showMessage('newLicense_error', '<s:text name="newlicense.subcategory.null" />');
                window.scroll(0, 0);
                return false;
            } else if (document.getElementById("tradeArea_weight").value == '' || document.getElementById("tradeArea_weight").value == null) {
                showMessage('newLicense_error', '<s:text name="newlicense.tradeareaweight.null" />');
                window.scroll(0, 0);
                return false;
            } else if (document.getElementById("uom").value == "") {
                showMessage('newLicense_error', '<s:text name="newlicense.uom.null" />');
                window.scroll(0, 0);
                return false;
            }
            return true;
        }
        else return false;
    }

</script>
</body>
</html>
