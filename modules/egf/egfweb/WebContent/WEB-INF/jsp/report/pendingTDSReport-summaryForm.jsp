<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<head>
	<script type="text/javascript" src="/EGF/commonjs/ajaxCommonFunctions.js"></script>
	<script type="text/javascript" src="/EGF/javascript/tdsReportHelper.js"></script>
	<script type="text/javascript" src="/EGF/javascript/helper.js"></script>
	<style type="text/css">
	#codescontainer {position:absolute;left:11em;width:9%;text-align: left;}
	#codescontainer .yui-ac-content {position:absolute;width:350px;border:1px solid #404040;background:#fff;overflow:hidden;z-index:20000;}
	#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:300px;background:#a0a0a0;z-index:19999;}
	.yui-skin-sam .yui-ac-input{width:100%;}
	#codescontainer ul {padding:5px 0;width:100%;}
	#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
	#codescontainer li.yui-ac-highlight {background:#ff0;}
	#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
	.yui-skin-sam tr.yui-dt-odd{background-color:#FFF;}

	#detailcodescontainer {position:absolute;left:11em;width:9%;text-align: left;}
	#detailcodescontainer .yui-ac-content {position:absolute;width:350px;border:1px solid #404040;background:#fff;overflow:hidden;z-index:20000;}
	#detailcodescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:300px;background:#a0a0a0;z-index:19999;}
	#detailcodescontainer ul {padding:5px 0;width:100%;}
	#detailcodescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
	#detailcodescontainer li.yui-ac-highlight {background:#ff0;}
	#detailcodescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
	</style>
</head>
<script>
var callback = {
		success: function(o){
			document.getElementById('results').innerHTML=o.responseText;
			clearWaitingImage();
			},
			failure: function(o) {
		    }
		}
function getData(){
	var asOnDate =  document.getElementById('asOnDate').value;
	var department =  document.getElementById('department').value;
	var fund =  document.getElementById('fund').value;
	var recovery =  document.getElementById('recovery').value;
	var detailKey =  document.getElementById('detailKey').value;
	var partyName =  document.getElementById('partyName').value;
	if(detailKey == 'undefined' && document.getElementById('partyName').value!='')
		detailKey = 0;
	if(detailKey == 'undefined' && document.getElementById('partyName').value=='')
		detailKey = -1;
	isValid = validateData();
	if(isValid == false)
		return false;
	var url = '/EGF/report/pendingTDSReport!ajaxLoadSummaryData.action?skipPrepare=true&asOnDate='+asOnDate+'&department.id='+
							department+'&fund.id='+fund+'&partyName='+partyName+'&recovery.id='+recovery+'&detailKey='+detailKey;
	YAHOO.util.Connect.asyncRequest('POST', url, callback, null);
	loadWaitingImage();
}

function exportXls(){
	var asOnDate =  document.getElementById('asOnDate').value;
	var department =  document.getElementById('department').value;
	var fund =  document.getElementById('fund').value;
	var recovery =  document.getElementById('recovery').value;
	var detailKey =  document.getElementById('detailKey').value;
	var partyName =  document.getElementById('partyName').value;
	window.open('/EGF/report/pendingTDSReport!exportSummaryXls.action?skipPrepare=true&asOnDate='+asOnDate+'&department.id='+department+'&fund.id='+fund+
	'&recovery.id='+recovery+'&detailKey='+detailKey+'&partyName='+partyName,'','resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
}

function exportPdf(){
	var asOnDate =  document.getElementById('asOnDate').value;
	var department =  document.getElementById('department').value;
	var fund =  document.getElementById('fund').value;
	var recovery =  document.getElementById('recovery').value;
	var detailKey =  document.getElementById('detailKey').value;
	var partyName =  document.getElementById('partyName').value;
	window.open('/EGF/report/pendingTDSReport!exportSummaryPdf.action?skipPrepare=true&asOnDate='+asOnDate+'&department.id='+department+'&fund.id='+
	fund+'&recovery.id='+recovery+'&detailKey='+detailKey+'&partyName='+partyName,'','resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
}
function hideIncludeRemittance(){
	document.getElementById('showRemittedEntries').style.display = "none";
	document.getElementById('showRemittedEntrieslabel').style.display = "none";
}
</script>
<body onload="hideIncludeRemittance();loadEntities();">
<div id="loading" style="position:absolute; left:25%; top:70%; padding:2px; z-index:20001; height:auto;width:500px;display: none;">
	<div class="loading-indicator" style="background:white;  color:#444; font:bold 13px tohoma,arial,helvetica; padding:10px; margin:0; height:auto;">
		<img src="/EGF/images/loading.gif" width="32" height="32" style="margin-right:8px;vertical-align:top;"/> Searching...
	</div>
</div>

<jsp:include page="pendingTDSReport-form.jsp"></jsp:include>
</body>
</html>
