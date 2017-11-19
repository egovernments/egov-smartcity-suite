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

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<div class="panel panel-primary" data-collapsed="0">
								<div class="panel-heading">
									<div class="panel-title">
									<spring:message  code="lbl.closureheader"/> 
									</div>
									
								</div>
								
					<div class="panel-body">
					<div class="form-group" id="radioValue">
										<label class="col-sm-3 col-xs-12 control-label text-right"><spring:message  code="lbl.connectiontype"/>
                                        </label>
						<%-- <div class="col-sm-6 add-margin dynamic-span capitalize">
							<form:radiobuttons path="closeConnectionType" items="${radioButtonMap}"   name="closeConnectionType" element="span" /> 
						</div> --%>
						<label class="col-sm-2 col-xs-4 control-label text-right"><spring:message code="lbl.temporary" /></label>
			<div class="col-sm-2 col-xs-4 add-margin">
				<form:radiobutton path="closeConnectionType" id="temporary" value="T" />
			</div>
			<label class="col-sm-2 col-xs-4 control-label "><spring:message code="lbl.permanent" />
			</label>
			<div class="col-sm-2 col-xs-4 add-margin">
				<form:radiobutton path="closeConnectionType" id="permanent" value="P" />
			</div>
					</div>
					</div>
                    <div class="form-group">
										<label class="col-sm-3 col-xs-12 control-label text-right"><spring:message  code="lbl.closerReason"/>
                                        </label>
                                        <div class="col-sm-8 col-xs-12 add-margin">
                                           <form:input class="form-control" id="closeconnectionreason" path="closeconnectionreason" name="closeconnectionreason" />
	
                    </div>
				</div>
                                    
</div>
<script>
var mode =$('#mode').val();
if(mode !='closeredit'){
$(':radio:not(:checked)').attr('disabled', false);
$('#closeconnectionreason').attr('disabled', false);
}
if(mode =='closereditForAE')
	{
	$('#closeconnectionreason').attr('disabled', false);
	$(':radio:not(:checked)').attr('disabled', false);
	}
else
{
	$('#closeconnectionreason').attr('disabled', false);
	$(':radio:not(:checked)').attr('disabled', false);
}
if(mode == "" ){
	$(':radio:not(:checked)').attr('disabled', true);
	$('#closeconnectionreason').attr('disabled', true);
	} 
	
</script>

