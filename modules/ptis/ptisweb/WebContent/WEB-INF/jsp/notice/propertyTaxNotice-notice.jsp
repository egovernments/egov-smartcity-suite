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