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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/includes/taglibs.jsp" %>
<html>
	<head>
		<script type="text/javascript">
			function onSubmit(){
			   var licenseNumber= document.getElementById("licenseNumber").value;
				 if (licenseNumber == ""){
					showMessage('search_error', '<s:text name="searchLicense.licenseNumber.null" />');
					return false;
				} else {
				    	clearMessage('search_error')
				    	document.searchLicenseForm.action='${pageContext.request.contextPath}/search/searchLicense-commonSearch.action';
				    	document.searchLicenseForm.submit();
				}
			} 
		</script>
		<title><s:text name="license.search"></s:text></title>
	</head>
	<body>
	  <div class="row">
      	<div class="col-md-12">
			<s:if test="%{hasErrors()}">
				<div align="left">
					<s:actionerror />
					<s:fielderror/>
				</div>			
			</s:if>
			<s:if test="%{hasActionMessages()}">
			    <div id="actionMessages" class="messagestyle">
			    	<s:actionmessage theme="simple"/>
			    </div>
			</s:if>
			<div id="search_error" class="error-msg" style="display:none;"></div> 
			<s:form action="searchLicense" name="searchLicenseForm" theme="simple" id="searchLicenseForm">
				<s:hidden name="mode" id="mode" value="%{mode}"/>
						
				<div class="panel panel-primary" data-collapsed="0">
                	<div class="panel-heading">
						<div class="panel-title" style="text-align:center">
								<s:text name='search.license.num' /> 
						</div>
                    </div>
						
					<div class="panel-body custom-form">
						<div class="form-group">
							<label for="field-1" class="col-sm-4 control-label text-right"><s:text
									name="licensee.no" /><span class="mandatory"></span></label>
							<div class="col-sm-4 add-margin">
								<s:textfield id="licenseNumber"	name="licenseNumber" value="%{licenseNumber}" class="form-control"/>
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="text-center">
						<button type="button" id="search" class="btn btn-primary" onclick="return onSubmit();">
						Search</button>
						<button type="button" id="btnclose" class="btn btn-default" onclick="window.close();">
						Close</button>
					</div>
				</div>
			</s:form>
			<%-- <div align="left" class="mandatory1" style="font-size: 11px">
			  &nbsp;&nbsp;<s:text name="mandtryFlds"></s:text>
			</div> --%>
		</div>
	  </div>
	</body>
</html>