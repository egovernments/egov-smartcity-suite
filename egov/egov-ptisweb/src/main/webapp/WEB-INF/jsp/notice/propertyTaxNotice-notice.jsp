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
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title><s:text name='ptis.notice.title'/></title>
  <script>
  function refreshInboxByNoticeType(){
  	var noticeType='<s:property value="%{noticeType}"/>';
  	var prativrutta='<s:property value="%{@org.egov.ptis.nmc.constants.NMCPTISConstants@NOTICE_PRATIVRUTTA}"/>';
  	var notice127='<s:property value="%{@org.egov.ptis.nmc.constants.NMCPTISConstants@NOTICE127}"/>';
  	var notice134='<s:property value="%{@org.egov.ptis.nmc.constants.NMCPTISConstants@NOTICE134}"/>';
  	var mutationCertificate='<s:property value="%{@org.egov.ptis.nmc.constants.NMCPTISConstants@MUTATION_CERTIFICATE}"/>';
 	if (noticeType == notice127 || noticeType == notice134) {
		opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
	} else if (noticeType == prativrutta) {
  		opener.opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
	} else if (noticeType == mutationCertificate) {
		opener.opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
	}
  }
  </script>
  </head>
  
   <body onload=" refreshInboxByNoticeType(); ">
  <s:form name="CreatePropertyNotice" theme="simple">
  <s:if test="%{hasErrors()}">
		<div class="errorstyle"><s:actionerror /> <s:fielderror /></div>
	</s:if>
	<s:if test="%{hasActionMessages()}">
		<div class="messagestyle"><s:actionmessage theme="simple" /></div>
	</s:if>
	<iframe src="../reportViewer?reportId=<s:property value='reportId'/>" width="98%"
		height="70%">
	<p>Your browser does not support iframes.</p>
	</iframe>
	<br/>
	<div class="buttonbottom">
		<input name="buttonClose" type="button" class="button"	id="buttonClose" value="Close" onclick="window.close();" />&nbsp;
	</div>
  </s:form>
  </body>
</html>
