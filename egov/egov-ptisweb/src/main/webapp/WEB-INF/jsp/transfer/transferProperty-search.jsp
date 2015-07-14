<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<html>
	<head>
		<title>Property Transfer Fee Payment</title>
	</head>
		<div class="formmainbox">
			<s:if test="%{hasErrors()}">
				<div class="errorstyle" id="property_error_area">
					<div class="errortext">
						<s:actionerror />
					</div>
				</div>
			</s:if>
			<s:form action="collectFee" name="transferform" theme="simple">
				<s:push value="model">
				<s:token/>
				<div class="headingbg">
					Property Transfer Fee Payment
				</div>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="bluebox2" style="width:5%;">
							&nbsp;
						</td>
						<td class="bluebox" style="width:20%">
							Application No :
						</td>
						<td class="bluebox">
							<span class="bold"><input type="text" name="applicationNo"/></span>
						</td>
						<td class="bluebox">
							&nbsp;
						</td>
						<td style="width:25%;">&nbsp;</td>
					</tr>
					<tr>
						<td colspan="5" align="center">OR</td>
					</tr>
					<tr>
						<td class="bluebox2" style="width:5%;">
							&nbsp;
						</td>
						<td class="bluebox" style="width:20%">
							Assessment No :
						</td>
						<td class="bluebox">
							<span class="bold"><input type="text" name="applicationNo"/></span>
						</td>
						<td class="bluebox">
							&nbsp;
						</td>
						<td style="width:25%;">&nbsp;</td>
					</tr>
				</table>
				<div class="buttonbottom">
					<s:submit value="Pay Fee" cssClass="buttonsubmit" align="center"></s:submit>
					<input type="button" value="Close" class="button" align="center" onClick="return confirmClose();" />
				</div>
				</s:push>
			</s:form>
		</div>
	</body>
</html>
