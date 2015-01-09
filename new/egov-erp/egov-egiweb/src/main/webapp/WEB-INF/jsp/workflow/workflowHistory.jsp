
<%
String cssClass = request.getParameter("cssClass");
String stateId  = request.getParameter("stateId");
if (cssClass == null || cssClass.trim().equals(""))
	cssClass = "workflowHistory";
int urlIndex = request.getRequestURL().toString().indexOf("/",8);
String egiURL = request.getRequestURL().toString().substring(0,urlIndex)+"/egi/";
%>
<style>
table.workflowHistory {
	background-color : transparent;
	color : navy;
	width :100%;
}
table.workflowHistory th {
	color:black;
	text-align: center;
	font-size: 15px;
	border:1px solid gray;
}

table.workflowHistory td {
	text-align:center;
	padding : 5px;
	color:navy;
	font-size : 11px;
	border : 1px solid gray;
		
}
</style>

<table id="workflowHistory" class="<%=cssClass%>" >
	<tr>
		<th>Date</th>
		<th>Sender</th>
		<th>Status</th>
		<th>Action to Perform</th>
		<th>Comments</th>
		<th>Signature</th>
	</tr>
	<tbody>
	</tbody>
</table>
<script type="text/javascript" src="<%=egiURL%>commonyui/yui2.7/yahoo/yahoo-min.js"></script> 
<script type="text/javascript" src="<%=egiURL%>commonyui/yui2.7/event/event-min.js"></script>  

<script type="text/javascript" src="<%=egiURL%>commonyui/yui2.7/connection/connection-min.js"></script><script>
	var sUrl = "<%=egiURL%>workflow/inbox!populateHistory.action?stateId=<%=stateId%>";
	var callback = {
		success:function (oResponse) {
			 var historyData = oResponse.responseText;
			 createWorkflowHistory(historyData);
		}, 
	 	failure:function (oResponse) {
	 	}, 
		timeout:30000, 
		cache:false
	};
	YAHOO.util.Connect.asyncRequest("GET", sUrl, callback);
	
	function createWorkflowHistory (historyDta) {
		var historyDatas  = eval('('+historyDta+')');
		var historySize  = historyDatas.length;
		var historyTable = document.getElementById('workflowHistory');
		var tbody = historyTable.lastChild.previousSibling;
		for (var i = 0; i < historySize ; i++ ) {
			var historyData = historyDatas[i];
			var status = historyData.Status.split("~");
			if (status[0] != 'NEW' && status[0] != 'END') {
				var tr  = document.createElement('tr');
				var td  = document.createElement('td');
				td.innerHTML = historyData.Date;
				tr.appendChild(td);
				var td1 = document.createElement('td');
				td1.innerHTML = historyData.Sender;
				tr.appendChild(td1);
				var td2 = document.createElement('td');
				td2.innerHTML = status[0];
				tr.appendChild(td2);
				var td3 = document.createElement('td');
				if (status[1])
					td3.innerHTML = status[1]; 
				tr.appendChild(td3);
				var td4 = document.createElement('td');
				td4.innerHTML = historyData.Details;
				tr.appendChild(td4);
				var td5 = document.createElement('td');
				td5.innerHTML = historyData.Signature;
				tr.appendChild(td5);
				tbody.appendChild(tr);
			}
		}
	}
</script>