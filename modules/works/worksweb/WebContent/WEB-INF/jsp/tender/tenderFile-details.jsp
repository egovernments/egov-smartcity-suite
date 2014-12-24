<script>

function update(elemValue1) {
	var eId="";
	elemValue1=elemValue1.split('~');
	elemValue=elemValue1[1].split(',');
	
	var eleLen =elemValue.length;
	for(var i=0;i<eleLen;i++)
	{
		if(eId=="")
			eId=elemValue[i];
		else
			eId=eId+'`~`'+elemValue[i];
	}
	
	if(elemValue1[0]=='estimate') {
		if(resetTenderFileDetails(dom.get("indentListTable"),dom.get("tenderFileIndentDetailsTable"))) {
			dom.get("tenderFileDetailsTable").style.display='';
			if(dom.get("estimateListTable")!=null){
				if(dom.get("estimateListTable").rows.length>1){
					var len =dom.get("estimateListTable").rows.length;
					if(len>2){
						for(var i=0;i<len-1;i++)
						{
							if(eId=="")
								eId=document.forms[0].estId[i].value;
							else
								eId=eId+'`~`'+document.forms[0].estId[i].value;
						}
				 	 }
				 	 else{
				 	 	if(eId=="")
							eId=document.forms[0].estId.value;
						else
							eId=eId+'`~`'+document.forms[0].estId.value;
				 	 }
				  }
			}
		
			var actionUrl = '${pageContext.request.contextPath}/tender/ajaxTenderFile!estimateList.action';
			var params    = 'estId=' + eId;
			var updatePage = 'tenderFile_estimatelist';
			<s:if test="%{sourcepage=='inbox' && model.egwStatus.code=='REJECTED'}">
				updatePage='tenderFile_estimatelist1';
			</s:if>
			
			var ajaxCall = new Ajax.Updater(updatePage,actionUrl,{parameters:params});
					
		}
	}
	else if(elemValue1[0]=='indent') {
		if(resetTenderFileDetails(dom.get("estimateListTable"),dom.get("tenderFileDetailsTable"))) {
			dom.get("tenderFileIndentDetailsTable").style.display='';
			if(dom.get("indentListTable")!=null){
				if(dom.get("indentListTable").rows.length>1){
					var len =dom.get("indentListTable").rows.length;
					if(len>2){
						for(var i=0;i<len-1;i++)
						{
							if(eId=="")
								eId=document.forms[0].indntId[i].value;
							else
								eId=eId+'`~`'+document.forms[0].indntId[i].value;
						}
				 	 }
				 	 else{
				 	 	if(eId=="")
							eId=document.forms[0].indntId.value;
						else
							eId=eId+'`~`'+document.forms[0].indntId.value;
				 	 }
				  }
			}
			var actionUrl = '${pageContext.request.contextPath}/tender/ajaxTenderFile!indentList.action';
			var params    = 'indntId=' + eId;
			var updatePage = 'tenderFile_indentlist';
			<s:if test="%{sourcepage=='inbox' && model.egwStatus.code=='REJECTED'}">
				updatePage='tenderFile_indentlist1';
			</s:if>
			
			var ajaxCall = new Ajax.Updater(updatePage,actionUrl,{parameters:params});
		}
	}
	setTimeout('toggleSpillOverWorks();', 1500); 	
}

function resetTenderFileDetails(obj,header) {
	if(obj!=null && obj.rows.length!=0) {
		var ans=confirm("Do you want to reset Tender File Details ?");
		if(!ans) {
			return false;
		}
		for(var i = obj.rows.length; i > 0;i--) {
			obj.deleteRow(i-1);
		}
		if(header!=null) {
			header.style.display='none';
		}
	}
	return true;
}

function toggleSpillOverWorks() {
	<s:if test="%{sourcepage!='search'}">
	setEstimateType();
	if(document.forms[0].isSpillOverWorks!=null) {
		if(document.forms[0].isSpillOverWorks.value=='true' || (document.forms[0].isSpillOverWorks.length>1 && document.forms[0].isSpillOverWorks[0].value=='true') ) {
			dom.get("spilloverApprove").style.display='';
			dom.get("normalbuttons").style.display='none';
			if(dom.get("manual_workflow")!=null){
	 			dom.get("manual_workflow").style.display='none';
	 		}			
		}	
		else {
			dom.get("spilloverApprove").style.display='none';
			dom.get("normalbuttons").style.display='';
			if(dom.get("manual_workflow")!=null){
	 			dom.get("manual_workflow").style.display='block';
	 		}
		}			
	}	
	</s:if>	
}

function deleterow(obj)
{
	var tbl = dom.get('estimateListTable');
	var rowNumber=getRow(obj).rowIndex;
	var currRow1=getRow(obj);
	dom.get('worktotalValue').value = roundTo(eval(dom.get('worktotalValue').value-getControlInBranch(currRow1,'wvIncldTaxes').value));
	tbl.deleteRow(rowNumber);
	if(dom.get("estimateListTable")!=null){
		if(dom.get("estimateListTable").rows.length>0){ 
		var len =dom.get("estimateListTable").rows.length;
		if(len>2){
			for(var i=0;i<len-1;i++)
			{
				document.forms[0].slNo[i].value=eval(i+1);
			}
	 	 }
	 	 /*else{
	 	 	document.forms[0].slNo.value=1;
	 	 }*/
	 	 }
	  }
	  var oRows = document.getElementById("estimateListTable").getElementsByTagName("tr");
	  var iRowCount = oRows.length;
	  if(iRowCount==1) {
	  	dom.get("estimateType").value="notype";
	  }
	  else {
	  	setEstimateType();
	  }
}

function deleteIndentRow(obj) {
	var tbl = dom.get('indentListTable');
	var rowNumber=getRow(obj).rowIndex;
	var currRow1=getRow(obj);
	tbl.deleteRow(rowNumber);
	if(dom.get("indentListTable")!=null){
		if(dom.get("indentListTable").rows.length>0){
		var len =dom.get("indentListTable").rows.length;
		if(len>2){
			for(var i=0;i<len-1;i++)
			{
				document.forms[0].slNo[i].value=eval(i+1);
			}
	 	 }
	 	 else{
	 	 	document.forms[0].slNo.value=1;
	 	 }
	  }
	}
}
</script>

<table id="tenderFileDetailsTable" width="100%" border="0" cellspacing="0" cellpadding="0" style="display:none">
        <tr>
          	<td colspan="6" class="headingwk"><div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" />
          	</div><div class="headplacer"><s:text name="tenderfile.details"/></div></td>
          	
       	</tr>
       	<td width="3%" class="tablesubheadwk">
			<s:text name='estimate.search.slno' />
		</td>
		<td width="25%" class="tablesubheadwk">
			<s:text name='estimate.search.estimateNo' />
		</td>
		<td width="35%" class="tablesubheadwk" style="WORD-BREAK:BREAK-ALL">
			<s:text name='estimate.search.name' />
		</td>
		<td width="20%" class="tablesubheadwk">
			<s:text name='estimate.search.estimateDate' />
		</td>
		<td width="35%" class="tablesubheadwk">
			<s:text name='estimate.search.total' />
		</td>
		<s:if test="%{!(model.egwStatus != null && sourcepage=='inbox' && (model.egwStatus.code == 'CREATED' || model.egwStatus.code=='APPROVED' || model.egwStatus.code=='RESUBMITTED' || model.egwStatus.code=='CHECKED'))}">
		<td width="10%" class="tablesubheadwk">
			Delete
		</td>
		</s:if>			
	</tr>	
</table>

<s:if test="%{abstractEstimateList.isEmpty()}">
	<div id="tenderFile_estimatelist">
       	</div> 
</s:if>
<s:else>
<div  id="tenderFile_estimatelist1">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" id="estimateListTable" name="estimateListTable">
	<s:iterator var="e" value="abstractEstimateList" status="s">	
	<tr>
		<td width="3%"  class="whitebox3wk">&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="text" name="slNo" id="slNo" disabled="true" size="1" value='<s:property value='%{#s.index+1}' />'/>
		</td>
		<td width="25%" class="whitebox3wk">&nbsp;&nbsp;&nbsp;&nbsp;
			<s:property value='%{estimateNumber}' /><s:hidden name="estId" id="estId" value="%{id}"/>
			<s:hidden name="isSpillOverWorks" value="%{isSpillOverWorks}" id="isSpillOverWorks"/>
			
		</td>
		<td width="35%" class="whiteboxwkwrap" style="WORD-BREAK:BREAK-ALL">
			<s:property value='%{name}' />
		</td>
		<td width="20%" class="whitebox3wk">&nbsp;&nbsp;
			<s:date name="estimateDate" var="estDateFormat" format="dd/MM/yyyy"/>
			<s:property value='%{estDateFormat}' />
		</td>
		<td width="35%" class="whitebox3wk">
			<div align="right">
				<s:property value='%{workValue.formattedString}' />
				<s:hidden name="wvIncldTaxes" id="wvIncldTaxes" value="%{workValue.formattedString}"/> 
			</div>
		</td>
		<s:if test="%{!(model.egwStatus != null && sourcepage=='inbox' && (model.egwStatus.code == 'CREATED' || model.egwStatus.code=='APPROVED' || model.egwStatus.code=='RESUBMITTED' || model.egwStatus.code=='CHECKED'))}">
		<td align="right" class="headingwk" style="border-left-width: 0px">
       		<a id="delHref" href="#" onclick="deleterow(this)">
       		<img border="0" alt="Delete Estimates" src="${pageContext.request.contextPath}/image/cancel.png" /></a>
       	</td>
       	</s:if>
	</tr>
</s:iterator>	
<tr><td colspan="5" style="background-color:#F4F4F4;" align="right"><b>Total:</b>&nbsp;
<input type="text" size="8" name="worktotalValue" id="worktotalValue" value="<s:property value="%{worktotalValue.formattedString}" />" 
class="amount" readonly="readonly"/></td>				
<td colspan="5" style="background-color:#F4F4F4;">&nbsp;</td>
</tr>	
</table>
</div> 
</s:else>

<table id="tenderFileIndentDetailsTable" width="100%" border="0" cellspacing="0" cellpadding="0" style="display:none">
        <tr>
          	<td colspan="6" class="headingwk"><div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" />
          	</div><div class="headplacer"><s:text name="tenderfile.details"/></div></td>
          	
       	</tr>
       	
       	 	<td width="3%" class="tablesubheadwk">
			<s:text name='estimate.search.slno' />
		</td>
       	<td width="30%" class="tablesubheadwk">
			<s:text name='indent.search.date' />
		</td>
		<td width="50%" class="tablesubheadwk">
			<s:text name='indent.search.indentNo' />
		</td>
		<s:if test="%{!(model.egwStatus != null && sourcepage=='inbox' && (model.egwStatus.code == 'CREATED' || model.egwStatus.code=='APPROVED' || model.egwStatus.code=='RESUBMITTED' || model.egwStatus.code=='CHECKED' ))}">
		<td width="10%" class="tablesubheadwk">
			Delete
		</td>
		</s:if>
	</tr>	
</table>

<s:if test="%{indentList.isEmpty()}">
	<div id="tenderFile_indentlist">
       	</div> 
</s:if>
<s:else>
<div  id="tenderFile_indentlist1">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" id="indentListTable" name="indentListTable" >
	<s:iterator var="e" value="indentList" status="s">
	<tr>
		<td width="3%"  class="whitebox3wk">&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="text" name="slNo" id="slNo" disabled="true" size="1" value='<s:property value='%{#s.index+1}' />'/>
		</td>
		<td width="30%" class="whitebox3wk">&nbsp;&nbsp;
			<s:date name="indentDate" var="indentDateFormat" format="dd/MM/yyyy"/>
			<s:property value='%{indentDateFormat}' />
		</td>
		<td width="50%" class="whitebox3wk">&nbsp;&nbsp;&nbsp;&nbsp;
			<s:property value='%{indentNumber}' /><s:hidden name="indntId" id="indntId" value="%{id}"/>
		</td>
		<s:if test="%{!(model.egwStatus != null && sourcepage=='inbox' && (model.egwStatus.code == 'CREATED' || model.egwStatus.code=='APPROVED' || model.egwStatus.code=='RESUBMITTED' || model.egwStatus.code=='CHECKED' ))}">
		<td align="right" width="10%" class="headingwk" style="border-left-width: 0px">
       		<a id="delHref" href="#" onclick="deleteIndentRow(this)">
       		<img border="0" alt="Delete Indents" src="${pageContext.request.contextPath}/image/cancel.png" /></a>
       	</td>
       	</s:if>	
	</tr>
</s:iterator>	

</table>
</div> 
</s:else>
