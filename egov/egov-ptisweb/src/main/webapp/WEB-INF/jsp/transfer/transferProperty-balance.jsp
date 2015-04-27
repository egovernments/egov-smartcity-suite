#-------------------------------------------------------------------------------
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
#-------------------------------------------------------------------------------
<%@ include file="/includes/taglibs.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<html>
<head>
</head>
<body>
<div class="formmainbox">
			<div class="formheading"></div>
			<div class="headingbg">
				<s:text name="arrdndpen" />
			</div>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
<s:form  name="transferform" theme="simple">
<tr>
						<td class="bluebox" width="15%"></td>
						<td class="bluebox">
							<s:text name="CurrentTax" />
							:
						</td>
						<td class="bluebox" colspan="3">
							<span class="bold">Rs. <s:property default="N/A"
									value="%{currDemand}" /> </span>
						</td>
					</tr>
					<tr>
						<td class="greybox" width="15%"></td>
						<td class="greybox">
							<s:text name="CurrentTaxDue" />
							:
						</td>
						<td class="greybox" colspan="3">
							<span class="bold">Rs. <s:property default="N/A"
									value="%{currDemandDue}" /> </span>
						</td>
					</tr>
					<tr>
						<td class="bluebox" width="15%"></td>
						<td class="bluebox">
							<s:text name="ArrearsDue" />
							:
						</td>
						<td class="bluebox" colspan="3">
							<span class="bold">Rs. <s:property default="N/A"
									value="%{arrDemand}" /> </span>
						</td>
					</tr>
					<div align="left" class="mandatory" style="font-size: 11px">
			* Arrears in demand must be cleared in order for the user to carry out this action.
					</div>
					
					</s:form>
</table>
<tr>
					<div class="buttonbottom" align="center">
						<td>
							<input type="button" value="Close" class="button" align="center"
								onClick="window.close()" />
						</td>
					</div>
					</tr>
</div>
</body>
</html>
