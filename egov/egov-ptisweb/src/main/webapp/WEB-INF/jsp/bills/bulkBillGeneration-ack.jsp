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

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title><s:text name='BulkBillAck'/></title>
     <script type="text/javascript">
	   function goBacktoBulkBillGeneration(){
		  	document.BillGenerationForm.action='${pageContext.request.contextPath}/bills/bulkBillGeneration-newForm.action';
		   	document.BillGenerationForm.submit(); 
		  	return true;
		  }    
  	</script>
  </head>
  <body>
  <s:form name="BillGenerationForm" theme="simple"> 
  <s:token />
  <div class="formmainbox">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
	       	<td colspan="5" style="color: #0000A0;font-size: 15px;" align="center">
	        	<s:property value="%{ackMessage}"/><a href="billGeneration-billsGenStatus.action">Generated Bills Report</a>
	        </td>
		</tr>
		</table> 
		<br/>
	</div>
	<div class="buttonbottom">
		<input type="submit" class="buttonsubmit" value="Go Back" id="newform" name="newform" onclick="return goBacktoBulkBillGeneration();" />&nbsp;</td>
		<input name="buttonClose" type="button" class="button"	id="buttonClose" value="Close" onclick="window.close();" />&nbsp;
	</div>
	
  </s:form>
  </body>
</html>
