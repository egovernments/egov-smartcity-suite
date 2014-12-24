<%@ include file="/includes/taglibs.jsp" %>
<html>
<head>
<title>
	<s:text name="billNoDet"/>
</title>

<style type="text/css">
		#codescontainer {position:absolute;left:11em;width:9%}
		#codescontainer .yui-ac-content {position:absolute;width:80%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
		#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:40%;background:#a0a0a0;z-index:9049;}
		#codescontainer ul {padding:5px 0;width:80%;}
		#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
		#codescontainer li.yui-ac-highlight {background:#ff0;}
		#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
</style>

<script type="text/javascript">
	var posCodeArray = new Array();
	var yuiflag1 = new Array();
	var neibrObj;
	var oAutoCompPos;
	var selectedPositionCode;
	function loadAllPositions() {
		var type = 'getAllPositions';
		var url = "<%=request.getContextPath()%>"+"/commons/process.jsp?type=" +type;
		var req2 = initiateRequest();
		req2.open("GET", url, false);
		req2.send(null);
		if (req2.status == 200) {
			var codes2 = req2.responseText;
			var a = codes2.split("^");
			var codes = a[0];
			posCodeArray = codes.split("+");
			selectedPositionCode = new YAHOO.widget.DS_JSArray(posCodeArray);
		}
	}

	function autocompleteForPosition(obj, event) {
		var src = obj;		 
		var currRow = getRow(obj);
		var target = document.getElementById('codescontainer');
		var autocomp = document.createElement("div");
		var targetId = 'codescontainer' + currRow.rowIndex;
		autocomp.setAttribute("id", targetId);
		target.appendChild(autocomp);
		var posSrc = findPos(src);
		target.style.left = posSrc[0];

		target.style.top = posSrc[1] + 25;

		if (obj.name == 'positionName')
			target.style.left = posSrc[0] + 0;
		target.style.width = 500;
		
		var coaCodeObj = obj;
		if (yuiflag1[currRow.rowIndex] == undefined) {
			if (event.keyCode != 40) {
				if (event.keyCode != 38) {
					oAutoCompPos = new YAHOO.widget.AutoComplete(coaCodeObj, targetId, selectedPositionCode);
					oAutoCompPos.queryDelay = 0;
					oAutoCompPos.useShadow = true;
					oAutoCompPos.maxResultsDisplayed = 15;
				}

			}
		}
	}

	function fillPositionAfterSplit(obj, neibrObjName) {

		var currRow = getRow(obj);
		yuiflag1[currRow.rowIndex] = undefined;
		neibrObj = getControlInBranch(currRow, neibrObjName);
		var temp = obj.value;

		temp = temp.split("`-`");
		if (temp[1] != '' && temp[1] != undefined) {
			neibrObj.value = temp[1];
		}
		obj.value = temp[0];

	}

	function addRow() 
		{				
		var mozillaFirefox=document.getElementById&&!document.all;
		
		var tbl = document.getElementById('billNumberDetails');							
		var lastRow = tbl.rows.length;		
		var iteration = lastRow-1;		
		var row = tbl.insertRow(lastRow);
		row.id='billNumberInfo';
			
		//insert empty cell
		var cell1 = row.insertCell(0);
		cell1.setAttribute('class','whitebox2wk');
		cell1.width='30%';	
				
		//insert billnumberId
		var cell2 = row.insertCell(1);		
		cell2.width='20%';
		if(mozillaFirefox)
		{	
			cell2.setAttribute('class','whitebox2wk');
		}
		else
		
		{
			cell2.setAttribute('className','whitebox2wk selected');
		}
		var elhidden = document.createElement('input');
		elhidden.type = 'hidden';
		elhidden.name = 'billNumberBeans['+ iteration + '].billNumberId';
		elhidden.value= "";
		cell2.appendChild(elhidden);	
	
		//insert billnumber		
		var eltext = document.createElement('input');
		eltext.setAttribute('type','text');
		eltext.name = 'billNumberBeans['+ iteration + '].billNumber';
		eltext.id = 'billNumberBeans['+ iteration + '].billNumber';
		eltext.setAttribute('maxlength','10');
		eltext.value='';
		if(mozillaFirefox)
		{	
			eltext.setAttribute('class','selectwk');
		}
		else
		{			
			eltext.setAttribute('className','selectwk selected');
		}
		cell2.appendChild(eltext);	
		
	
		//insert positionId
		var cell3 = row.insertCell(2);
		cell3.width='20%';		
		var elhidden1 = document.createElement('input');
		elhidden1.type = 'hidden';
		elhidden1.name = 'billNumberBeans['+ iteration + '].positionId';
		elhidden1.value= '';
		cell3.appendChild(elhidden1);
		
		//insert positionName		
		var eltext1 = document.createElement('input');
		eltext1.type = 'text';
		eltext1.name = 'billNumberBeans['+ iteration + '].positionName';
		eltext1.id = 'billNumberBeans['+ iteration + '].positionName';
		eltext1.setAttribute('maxlength','10');
		eltext1.value='';
		if(mozillaFirefox)
		{		
			eltext1.setAttribute('onkeyup','autocompleteForPosition(this,event)');
			eltext1.setAttribute('onblur','fillPositionAfterSplit(this,"billNumberBeans['+ iteration + '].positionId")');
			cell3.setAttribute('class','whitebox2wk');
			eltext1.setAttribute('class','selectwk');
		}else
		{
			eltext1.onkeyup=callAutoComplete;
			eltext1.onblur=callFillPosition;
			cell3.setAttribute('className','whitebox2wk');
			eltext1.setAttribute('className','selectwk selected');
		}				
		cell3.appendChild(eltext1);	
		
		
		//insert add button
		var cell4 = row.insertCell(3);
		var elImg1 = document.createElement('img');
		elImg1.id="adRow";
		elImg1.name="adRow";
		elImg1.setAttribute('src','${pageContext.request.contextPath}/images/addrow.gif');
		elImg1.setAttribute('alt','Add');
		if(mozillaFirefox)
		{
			elImg1.setAttribute('onclick','javascript:addRow()');
		}
		else
		{
			elImg1.onclick=addRow;
		}
		elImg1.width="18";
		elImg1.height="18";
		elImg1.border="0";
		cell4.appendChild(elImg1);
		if(mozillaFirefox)
		{
			cell4.setAttribute('class','whitebox2wk');
		}
		else
		{
			cell4.setAttribute('className','whitebox2wk selected');
		}
		
		
		
		//insert remove button			
		var elImg2 = document.createElement('img');
		elImg2.id="deRow";
		elImg2.name="deRow";
		elImg2.setAttribute('src','${pageContext.request.contextPath}/images/removerow.gif');
		elImg2.setAttribute('alt','Remove');
		if(mozillaFirefox)
		{
				elImg2.setAttribute('onclick','javascript:delRow(this)');
		}
		else
		{
			elImg2.onclick=deleteRow;
		}
		elImg2.width="18";
		elImg2.height="18";
		elImg2.border="0";
		cell4.appendChild(elImg2);		
			    	
	}
	
	function callAutoComplete()
	{
		autocompleteForPosition(this,event);
	}
	function callFillPosition()
	{
		var tbl = document.getElementById('billNumberDetails');							
		var lastRow = tbl.rows.length;		
		var iteration = lastRow-2;		
		fillPositionAfterSplit(this,'billNumberBeans['+ iteration + '].positionId');
	}
	function deleteRow()
	{		
		delRow(this);
	}
	function delRow(obj) {
		rIndex = getRow(obj).rowIndex;
		var tbl = document.getElementById('billNumberDetails');
		var rowo = tbl.rows.length;
		if (rowo <= 2) {
			alert("This Row cannot be deleted");
			return false;
		} else {
			tbl.deleteRow(rIndex);
			return true;
		}
	}
</script>
</head>
<body onload="loadAllPositions()">
	<s:actionerror/>
	<s:form name="billNumberMasterCreateForm" theme="simple">
		<s:hidden name="deptId" value="%{deptId}" />
		<table width="100%" border="0"  cellpadding="0" cellspacing="0" id="billNumberDetails">
			<tr>
				<td class="tablesubheadwk" width="30%">&nbsp;</td>
				<td class="tablesubheadwk" width="20%"><s:text name="billNo"/></td>
				<td class="tablesubheadwk" width="20%"><s:text name="approverPosition"/></td>
				<td class="tablesubheadwk">&nbsp;</td>
			</tr>
			<s:if test="%{billNumberBeans.isEmpty()}">
				<tr id="billNumberInfo">
					<td class="whitebox2wk" width="30%">&nbsp;</td>
					<td class="whitebox2wk" width="20%">
						<s:textfield
							name="billNumberBeans[0].billNumber"
							id="billNumberBeans[0].billNumber"
							value="%{billNumberBeans[0].billNumber}" maxlength="10"
							cssClass="selectwk" /></td>
					<td class="whitebox2wk" width="20%">
						<s:hidden
							name="billNumberBeans[0].positionId"
							id="billNumberBeans[0].positionId"></s:hidden>
						<s:textfield
							name="billNumberBeans[0].positionName"
							onkeyup="autocompleteForPosition(this,event);"
							onblur="fillPositionAfterSplit(this,'billNumberBeans[0].positionId')"
							id="billNumberBeans[0].positionName" cssClass="selectwk">
						</s:textfield></td>
					<td class="whitebox2wk"><img id="adRow" name="adRow"
						src="${pageContext.request.contextPath}/images/addrow.gif"
						alt="Add" onclick="javascript:addRow();" width="18" height="18"
						border="0" /> <img id="deRow" name="deRow"
						src="${pageContext.request.contextPath}/images/removerow.gif"
						alt="Remove" onclick="javascript:delRow(this);" width="18"
						height="18" border="0" /></td>
				</tr>
			</s:if>		
			<s:else>
				<s:iterator status="billStatus" value="billNumberBeans">
					<tr id="billNumberInfo">
					<td class="whitebox2wk" width="30%">&nbsp;</td>
					<td class="whitebox2wk" width="20%">
						<s:hidden name="billNumberBeans[%{#billStatus.index}].billNumberId" value="%{billNumberBeans[#billStatus.index].billNumberId}" />
						<s:textfield
							name="billNumberBeans[%{#billStatus.index}].billNumber"
							id="billNumberBeans[%{#billStatus.index}].billNumber"
							value="%{billNumberBeans[#billStatus.index].billNumber}" maxlength="10"
							cssClass="selectwk" /></td>
					<td class="whitebox2wk" width="20%">
						<s:hidden
							name="billNumberBeans[%{#billStatus.index}].positionId"
							id="billNumberBeans[%{#billStatus.index}].positionId"></s:hidden> 
						<s:textfield
							name="billNumberBeans[%{#billStatus.index}].positionName"
							onkeyup="autocompleteForPosition(this,event);"
							onblur="fillPositionAfterSplit(this,'billNumberBeans[%{#billStatus.index}].positionId')"
							id="billNumberBeans[%{#billStatus.index}].positionName" cssClass="selectwk">
						</s:textfield></td>
					<td class="whitebox2wk"><img id="adRow" name="adRow"
						src="${pageContext.request.contextPath}/images/addrow.gif"
						alt="Add" onclick="javascript:addRow();" width="18" height="18"
						border="0" /> <img id="deRow" name="deRow"
						src="${pageContext.request.contextPath}/images/removerow.gif"
						alt="Remove" onclick="javascript:delRow(this);" width="18"
						height="18" border="0" /></td>
				</tr>
				</s:iterator>
			</s:else>
			
		</table>
		<table width="100%" border="0"  cellpadding="0" cellspacing="0">
			<tr>
       			<td class="greyboxwk" width="30%">&nbsp;</td>
				<td class="greyboxwk"  width="25%">
					<s:submit name="Save" value="Save" method="save" cssClass="buttonfinal"></s:submit>
					<s:submit name="close" value="Close" cssClass="buttonfinal" onclick="window.close();"></s:submit>
				</td>
				<td class="greyboxwk">&nbsp;</td>
				<td class="greyboxwk">&nbsp;</td>
			</tr>
		</table>
		<div id="codescontainer"></div>
	</s:form>
</body>
</html>