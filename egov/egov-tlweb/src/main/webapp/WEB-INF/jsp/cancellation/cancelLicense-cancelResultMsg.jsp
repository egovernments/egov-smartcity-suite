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
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/includes/taglibs.jsp"%>

<html>
	<head>
		<title><s:text name="page.title.cancellicense" />
		</title>
	</head>
	<script>
		function closethis() {
			if (confirm("Do you want to close this window ?")) {
				window.close();
			}
		}
		
		function printthis() {
			if (confirm("Do you want to print this screen ?")) {
				var html="<html>";
				html+= document.getElementById('content').innerHTML;
				html+="</html>";
				
				var printWin = window.open('','','left=0,top=0,width=1,height=1,toolbar=0,scrollbars=0,status=0');
				printWin.document.write(html);
				printWin.document.close();
				printWin.focus();
				printWin.print();
				printWin.close();
			}		
		}
	</script>
	<body onload="refreshInbox();">
	<div id="newLicense_error" class="errorstyle" style="display:none;"></div> 
	    <div class="row">
	        <div class="col-md-12">
				<s:form action="cancelLicense" theme="simple" name="cancelLicenseForm">
				<s:push value="model">
				<s:hidden id="zoneName" name="zoneName" value="%{zoneName}" />
				<s:hidden id="wardName" name="wardName" value="%{wardName}" />
	 			<div class="panel panel-primary" data-collapsed="0">
                    <div class="panel-heading">
						<div class="panel-title" style="text-align:center">
						<s:text name='page.title.cancellicense' /> 
						</div>
                    </div>
                        
                    <div class="panel-body custom-form">
                        <%@ include file='../common/view.jsp'%>
                        
                        <div class="panel-heading custom_form_panel_heading">
						    <div class="panel-title"><s:text name='license.title.cancellationdetails' /></div>
						</div>
						
						<div class="panel-body">
							<div class="row add-border">
								<div class="col-xs-3 add-margin"><s:text name='license.LicenseCancelInfo.reasonForCancellation' /></div>
								<div class="col-xs-3 add-margin view-content"><s:property value="%{reasonMap.get(reasonForCancellation)}" /></div>
								<div class="col-xs-3 add-margin"><s:text name='license.license.refernceno' /></div>
								<div class="col-xs-3 add-margin view-content"><s:property value="%{refernceno}" /></div>
							</div>
							
							<div class="row add-border">
								<div class="col-xs-3 add-margin"><s:text name='license.license.referencedate' /></div>
								<div class="col-xs-3 add-margin view-content">
									<s:date name="commdateApp" var="endDateFormat" format="dd/MM/yyyy"/> 
									<s:property value="%{endDateFormat}" />
								</div>
								<div class="col-xs-3 add-margin"><s:text name='license.license.Remarks' /></div>
								<div class="col-xs-3 add-margin view-content"><s:property value="%{cancelInforemarks}" /></div>
							</div>
						</div>			
                  	</div>  		
				</div>
				</s:push>
				
				<div class="row">
					<div class="text-center">
						<button type="button" id="print" class="btn btn-primary" onclick="printthis();">
						Print</button>
						<button type="button" id="btnclose" class="btn btn-default" onclick="closethis();">
						Close</button>
					</div>
				</div>
				
				</s:form>
			</div>
		</div>
	</body>
</html>
