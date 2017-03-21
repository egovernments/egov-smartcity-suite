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
			function onSubmit(obj,formId){
				var formObj = document.getElementById(formId);
				formObj.action=obj;
				formObj.submit;
			   return true;
			} 
		</script>
		<title><s:text name="searchProp.title"></s:text></title>
	</head>
	<body>
		<div class="formmainbox">
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
				<table border="0" cellspacing="0" cellpadding="0" width="100%">
					<s:form action="searchProperty" name="assessmentform" theme="simple" id="assessmentform">
						<s:hidden name="applicationType" value="%{applicationType}"/> 
						<s:hidden name="actionNamespace" value="%{actionNamespace}"/>
						<s:hidden name="applicationSource" value="%{applicationSource}"/>
						<tr>
							<td width="100%" colspan="4" class="headingbg">												
								<div class="headingbg">					
									<s:text name="search.assessment.num" />									
								</div>									
							</td>
						</tr>					
						<tr><td colspan="4"><br/></td></tr>							
						<tr>
							<td class="bluebox">&nbsp;</td>
							<td class="bluebox">
								<s:text name="prop.Id" />
								<span class="mandatory1">*</span> :
							</td>
							
							<td class="bluebox">
								<s:textfield name="assessmentNum" id="assessmentNum" value="%{assessmentNum}" maxlength="15"/>
							</td>
							<td class="bluebox">&nbsp;</td>
						</tr>
						<tr>
							<td class="greybox">&nbsp;</td>
							<td class="greybox" colspan="2">
							   <br/>
								<div class="greybox" style="text-align:center">
									<s:hidden id="mode" name="mode" value="assessment"></s:hidden>
										<s:hidden id="meesevaApplicationNumber" name="meesevaApplicationNumber" value="%{meesevaApplicationNumber}"></s:hidden>
											<s:hidden id="meesevaServiceCode" name="meesevaServiceCode" value="%{meesevaServiceCode}"></s:hidden>
									<s:submit name="search" value="Search" cssClass="buttonsubmit" onclick="return onSubmit('searchProperty-commonSearch.action', 'assessmentform');"></s:submit>
								</div>
							</td>
							<td class="greybox">&nbsp;</td>
						</tr>
					</s:form>
				</table>			
			<div align="left" class="mandatory1" style="font-size: 11px">
			  &nbsp;&nbsp;<s:text name="mandtryFlds"></s:text>
			</div>
		</div>
	</body>
</html>
