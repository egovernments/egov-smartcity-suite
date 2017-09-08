<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~ accountability and the service delivery of the government  organizations.
  ~
  ~  Copyright (C) 2016  eGovernments Foundation
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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<div class="row">
    <div class="col-md-12">
        <c:if test="${not empty message}">
            <div class="alert alert-success" role="alert"><spring:message code="${message}" arguments="${name}"/></div>
        </c:if>
        <div class="panel panel-primary" data-collapsed="0">
            <div class="panel-heading">
                &nbsp;
            </div>
            <div class="panel-body custom-form">
                <form:form id="password-form" cssClass="form-horizontal form-groups-bordered">
                    <input style="display:none" type="text">
                    <input style="display:none" type="password"/>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">
                            <spring:message code="lbl.employee.name"/> <span class="mandatory"></span>
                        </label>
                        <div class="col-sm-6 add-margin">
                            <input name="username" placeholder="Start typing employee name" class="form-control" id="username" required="required" maxlength="200" autocomplete="off"/>
                            <input name="userId" id="userId" type="hidden" required="required">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">
                            <spring:message code="lbl.new.pwd"/><span class="mandatory"></span>
                        </label>
                        <div class="col-sm-6 add-margin">
                            <input type="password" name="password" class="form-control" id="password" value="demo" autocomplete="off" required="required" maxlength="32"/>
                        </div>
                    </div>
                    <div class="form-group text-center">
                        <div class="col-md-12 add-margin">
                            <button type="button" id="submitbtn" class="btn btn-primary"><spring:message code="title.reset.password"/></button>
                            <button type="button" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close"/></button>
                        </div>
                    </div>
                </form:form>
                <span class="mandatory"></span> <spring:message code="lbl.user.reset.pwd.info"/>
            </div>
        </div>
    </div>
</div>
<script>
    $(document).ready(function () {
        $("#submitbtn").click(function () {
            if ($("#userId").val() !== '')
                $("#password-form").submit();
            else
                bootbox.alert("Start typing the employee name and choose one from shown items.")

        });
        var usernameautocomplete = new Bloodhound({
            datumTokenizer: function (datum) {
                return Bloodhound.tokenizers.whitespace(datum.value);
            },
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            limit: 10,
            remote: {
                url: 'employee-name-like/?employeeName=',
                replace: function (url, query) {
                    if ($('#username').val()) {
                        url += encodeURIComponent($('#username').val());
                    }
                    return url;
                },
                filter: function (data) {
                    return $.each(JSON.parse(data), function (user) {
                        return {
                            name: user.name,
                            value: user.id
                        };
                    });
                }
            }
        });

        usernameautocomplete.initialize();
        $('#username').typeahead({
            hint: true,
            highlight: true,
            minLength: 1
        }, {
            name: "users",
            displayKey: 'name',
            templates: {
                empty: [
                    '<div class="alert-info">&nbsp;&nbsp;',
                    'Unable to find the employee with given name',
                    '&nbsp;</div>'
                ].join('\n'),
                suggestion: function (data) {
                    return '&nbsp;&nbsp;<strong>' + data.name + '</strong> [ ' + data.userName + ' ]';
                }
            },
            source: usernameautocomplete.ttAdapter(),
        }).on('typeahead:selected', function (e, data) {
            $('#userId').val(data.id);
        });
        $("#username").focus();
    });

</script>
