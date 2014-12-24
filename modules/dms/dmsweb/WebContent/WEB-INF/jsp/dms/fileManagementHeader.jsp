<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
	<head>
		 <title><s:text name="titl.DMS"/></title>
		 <link href="../commonyui/yui2.7/fonts/fonts-min.css" type="text/css" rel="stylesheet"></link>
		 <link href="../commonyui/yui2.7/calendar/assets/skins/sam/calendar.css" rel="stylesheet" type="text/css"></link>
		 <link href="../commonyui/yui2.7/datatable/assets/skins/sam/datatable.css" type="text/css" rel="stylesheet"/>
		 <link href="../css/dms/fileManagement.css" rel="stylesheet" type="text/css" />	
	<script>
	function refreshInbox() {
		if(opener && opener.top.document.getElementById('inboxframe')!=null){
			if(opener.top.document.getElementById('inboxframe').contentWindow.name!=null && opener.top.document.getElementById('inboxframe').contentWindow.name=="inboxframe"){
				opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
			}
		}
	}
	var id = '${id}';
	</script>

	</head>	
	<body onload="refreshInbox()" style="background-color: white" class="yui-skin-sam">
			<egovtags:breadcrumb/>
			<div class="mainhead">
				<s:text name="titl.DMS"/>
			</div>
