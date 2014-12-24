<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<style type="text/css">
.yui-dt table{
  width:100%;
}
.yui-skin-sam .yui-dt td.align-right  { 
		text-align:right;
}
th.yui-dt-hidden,
tr.yui-dt-odd .yui-dt-hidden,
tr.yui-dt-even .yui-dt-hidden {
display:none;
}
</style>
<script src="<egov:url path='js/works.js'/>"></script>
<script src="<egov:url path='css/works.css'/>"></script>
<script type="text/javascript">


function escapeSpecialChars(str) {
	str1 = str.replace(/\'/g, "\\'");
	str2 = str1.replace(/\r\n/g, "<br>");
	return str2;
}


function getActivities() {
var act=new Array();
	for(var i=0;i<woDataTable.getRecordSet().getLength();i++){
			 act[i]=getNumber(dom.get('tenderresponseactivityid'+woDataTable.getRecord(i).getId()).value);
	}
	window.open("${pageContext.request.contextPath}/tender/searchTenderResponseActivities.action?tenderRespId="+
	dom.get('tenderRespId').value+"&estimateId="+dom.get('estimateId').value+"&selectedactivities="+act,"","height=600,width=900,scrollbars=yes,left=0,top=0,status=yes");
}

function update(elemValue){

var trAId="";
var eleLen =elemValue.length;
	for(var i=0;i<eleLen;i++)
	{
		if(trAId==""){
			trAId=elemValue[i];
			}
		else{
			trAId=trAId+'^'+elemValue[i];
			}
			
	}
	makeJSONCall(["tenderresponseactivityid","activityId","schno","description","estimateno","estimateqty","uom","negotiatedRate","negotiatedQty","uomfactor","estimateMSheetLength"],'${pageContext.request.contextPath}/workorder/ajaxWorkOrder!getTenderResponseActivityList.action',{traIds:trAId},activityLoadHandler,activityLoadFailureHandler) ;
}

activityLoadHandler = function(req,res){
  results=res.results;
  for(var k=0;k<results.size();k++){
  if(results[k].estimateMSheetLength>0) {
	 	woDataTable.addRow({SlNo:woDataTable.getRecordSet().getLength()+1,schNo:results[k].schno,workorderDescription:results[k].description,
	 	estimate:results[k].estimateno,estimateQuantity:results[k].estimateqty,approvedQuantity:0,workorderUOM:results[k].uom,
	 	workorderRate:results[k].negotiatedRate,workorderAmount:'0.00',uomFactor:results[k].uomfactor,approvedRate:results[k].negotiatedRate,
	 	activity:results[k].activityId,tenderresponseactivityid:results[k].tenderresponseactivityid,origApprovedQty:eval(results[k].estimateqty-results[k].negotiatedQty),unAssignedQty:eval(results[k].estimateqty-results[k].negotiatedQty),estimateMSheetLength:results[k].estimateMSheetLength});
 	}
 	else {
 		woDataTable.addRow({SlNo:woDataTable.getRecordSet().getLength()+1,schNo:results[k].schno,workorderDescription:results[k].description,
	 	estimate:results[k].estimateno,estimateQuantity:results[k].estimateqty,approvedQuantity:eval(results[k].estimateqty-results[k].negotiatedQty),workorderUOM:results[k].uom,
	 	workorderRate:results[k].negotiatedRate,workorderAmount:'0.00',uomFactor:results[k].uomfactor,approvedRate:results[k].negotiatedRate,
	 	activity:results[k].activityId,tenderresponseactivityid:results[k].tenderresponseactivityid,origApprovedQty:eval(results[k].estimateqty-results[k].negotiatedQty),unAssignedQty:eval(results[k].estimateqty-results[k].negotiatedQty),estimateMSheetLength:results[k].estimateMSheetLength});
 	}
 	var record = woDataTable.getRecord(parseInt(k));
 	if(results[k].estimateqty==0){
 		dom.get("approvedQuantity"+record.getId()).disabled=true; 
	    dom.get("approvedQuantity"+record.getId()).readOnly=true;
 }
 }
 recalculateAmount();
}

activityLoadFailureHandler= function(){
    alert('Unable to load activities');
}

function calculateAmount(elem,recordId,column){
	var apprvdQuantElemId = 'approvedQuantity' + recordId;
 	if(elem.id==apprvdQuantElemId){
  	if(!checkUptoFourDecimalPlace(elem,dom.get('workOrder_error'),"WorkOrder Quantity")) {
  		window.scroll(0,0);
  		return false;}
  	}	
	if(!validateNumberInTableCell(woDataTable,elem,recordId)) return;
	if(!validateApprovedQtyNew(elem,recordId,column)) return;
    calculateTotalAmount(recordId,record);
}
function calculateTotalAmount(recordId,record){
	 oldWOAmount=getNumber(record.getData("workorderAmount"));
    woDataTable.updateCell(record,woDataTable.getColumn('workorderAmount'),roundTo(getNumber(dom.get('approvedQuantity'+recordId).value)*getNumber(record.getData("uomFactor"))*getNumber(record.getData("workorderRate"))));
    var totalWOAmount=roundTo(getNumericValueFromInnerHTML("amtTotal") - oldWOAmount + getNumber(record.getData("workorderAmount")));
	 dom.get("amtTotal").innerHTML=totalWOAmount;
	 document.getElementById("workOrderAmount").value=totalWOAmount;
      var securityDepositConfValue='<s:property value="%{securityDepositConfValue}"/>';
	     document.getElementById("securityDeposit").value=roundTo(eval(securityDepositConfValue)/100*totalWOAmount);
}

function recalculateAmount(){
	for(var i=0;i<woDataTable.getRecordSet().getLength();i++){
			 calculateTotalAmount(woDataTable.getRecord(i).getId(),woDataTable.getRecord(i));
	}
	if(woDataTable.getRecordSet().getLength()==0){
		dom.get("amtTotal").innerHTML='0.0';
	 	document.getElementById("workOrderAmount").value='0.0';
	 	document.getElementById("securityDeposit").value='0.0';
	}

}

function validateApprovedQty(elem,recordId,column){
	  var activityId=getNumber(record.getData("activity"));
   	  var negotiationNumber=document.getElementById("negotiationNumber").value; 
   	  	makeJSONCall(["assignedQty","recordId"],'${pageContext.request.contextPath}/tender/searchTenderResponseActivities!getAssignedQuantity.action',{activityId:activityId,negotiationNumber:negotiationNumber,recordId:recordId},assignedQtyLoadHandler,assignedQtyLoadFailureHandler) ;
}

assignedQtyLoadHandler = function(req,res){
  results=res.results;
  for(var k=0;k<results.size();k++){
 	var recordId=results[k].recordId;
 	var qty=getNumber(dom.get('approvedQuantity'+recordId).value);
 	var assignedQty=eval(results[k].assignedQty);
 	var record1=woDataTable.getRecord(recordId);
    var estimateQty=getNumber(record.getData("estimateQuantity"));
    dom.get('errorapprovedQuantity'+recordId).style.display='none';
      clearMessage('workOrder_error');
      var unassignedQty=estimateQty-assignedQty;
     <s:if test="%{sourcepage=='inbox' && (model.currentState.value=='NEW' || model.currentState.value=='REJECTED') }">//To ignore the saved qty while validating
     	var originalApprovedQty=getNumber(record1.getData("origApprovedQty"));
     	unassignedQty=unassignedQty+originalApprovedQty;
 	</s:if>
   	if(qty>unassignedQty){
 		dom.get('errorapprovedQuantity'+recordId).style.display='';
 		 dom.get("workOrder_error").style.display='';
    	 document.getElementById("workOrder_error").innerHTML='<s:text name="workOrder.quantitynotgreaterthanestimateqty" />';
     }
   } 
 }

assignedQtyLoadFailureHandler= function(){
    alert('Unable to load assignedQty');
}

function validateApprovedQtyNew(elem,recordId,column){
	var qty=getNumber(dom.get('approvedQuantity'+recordId).value);
	var record1=woDataTable.getRecord(recordId);
    var estimateQty=getNumber(record1.getData("estimateQuantity"));
    dom.get('errorapprovedQuantity'+recordId).style.display='none';
    clearMessage('workOrder_error');
    var unassignedQty=getNumber(record1.getData("unAssignedQty"));
     <s:if test="%{sourcepage=='inbox' && (model.currentState.value=='NEW' || model.currentState.value=='REJECTED') }">//To ignore the saved qty while validating
     	var originalApprovedQty=getNumber(record1.getData("origApprovedQty"));
     	unassignedQty=unassignedQty+originalApprovedQty;
 	</s:if>
   	if(qty>unassignedQty){
 		dom.get('errorapprovedQuantity'+recordId).style.display='';
 		 dom.get("workOrder_error").style.display='';
    	 document.getElementById("workOrder_error").innerHTML='<s:text name="workOrder.quantitynotgreaterthanestimateqty" />';
    	return false;
     }
     return true;
}

function createHiddenFormatter(el, oRecord, oColumn, oData){
var hiddenFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
  var fieldName = "actionWorkOrderActivities[" + oRecord.getCount() + "]." + oColumn.getKey();
   var fieldValue=value;
    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+fieldValue+"' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
}
return hiddenFormatter;
}

var hiddenFormatter = createHiddenFormatter(10,10);



function createActivityHiddenFormatter(el, oRecord, oColumn, oData){
var hiddenFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
  var fieldName = "actionWorkOrderActivities[" + oRecord.getCount() + "]." + oColumn.getKey() + ".id";
   var fieldValue=value;
    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+fieldValue+"' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
}
return hiddenFormatter;
}

var activityHiddenFormatter = createActivityHiddenFormatter(10,10);



function createTRAIDHiddenFormatter(el, oRecord, oColumn, oData){
var hiddenFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
  var fieldName = "actionWorkOrderActivities[" + oRecord.getCount() + "]." + "tenderResponseActivityId";
   var fieldValue=value;
    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+fieldValue+"' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
}
return hiddenFormatter;
}

var tenderresponseactivityidHiddenFormatter = createTRAIDHiddenFormatter(10,10);



function createWOQuantityTextboxFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName="actionWorkOrderActivities[" + oRecord.getCount() + "]." + woDataTable.getColumn('approvedQuantity').getKey();
    markup="<input type='text' id='"+id+"' name='"+fieldName+"' value='"+value+"' size='"+size+"' maxlength='"+maxlength+"' class='selectamountwk'  onblur='calculateAmount(this,\""+oRecord.getId()+"\",\""+oColumn.getKey()+"\");'/><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
	}
	return textboxFormatter;
}
var woQuantityTextboxFormatter = createWOQuantityTextboxFormatter(11,13);


var woDataTable;
var makeWODataTable = function() {
        var woColumns = [
            {key:"SlNo",label:'<s:text name="workorder.slNo.title"/>', sortable:false, resizeable:false},
            {key:"schNo",label:'<s:text name="workorder.sch.no"/>', sortable:false, resizeable:false},
            {key:"workorderDescription",label:'<s:text name="workorder.description"/>', sortable:false, resizeable:false},
            {key:"estimate",label:'<s:text name="workorder.estimatenumber"/>', sortable:false, resizeable:false},
            {key:"estimateQuantity",label:'<s:text name="workorder.estimatequantity"/>', sortable:false, resizeable:false,className:'align-right'},
            {key:"woEstimateMSheetDtls",label:'<s:text name="workorder.mSheet"/>',formatter:createPageEditFormatter("${pageContext.request.contextPath}"), sortable:false, resizeable:false},
            {key:"approvedQuantity",label:'<s:text name="workorder.woquantity"/>',formatter:woQuantityTextboxFormatter, sortable:false, resizeable:false},
            {key:"woMSheetDtls",label:'<s:text name="workorder.detailed.mSheet"/>',formatter:createPageEditFormatter("${pageContext.request.contextPath}"), sortable:false, resizeable:false},
            {key:"workorderUOM",label:'<s:text name="workorder.uom"/>', sortable:false, resizeable:false},
            {key:"workorderRate",label:'<s:text name="workorder.workorderrate.title"/>', sortable:false, resizeable:false,className:'align-right'},
            {key:"workorderAmount",label:'<s:text name="workorder.amount.title"/>', sortable:false, resizeable:false,className:'align-right'},
            {key:'Delete',label:'<s:text name="column.title.delete"/>',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")},
		    {key:"uomFactor",label:'Unit Rate', hidden:true,sortable:false, resizeable:false},
            {key:"approvedRate",label:'approvedRate',formatter:hiddenFormatter, hidden:true,sortable:false, resizeable:false},
            {key:"activity",label:'activity',formatter:activityHiddenFormatter, hidden:true,sortable:false, resizeable:false},
            {key:"tenderresponseactivityid",label:'tenderresponseactivityid',formatter:tenderresponseactivityidHiddenFormatter, hidden:true,sortable:false, resizeable:false},
            {key:"origApprovedQty",label:'origApprovedQty',hidden:true,sortable:false, resizeable:false},
            {key:"unAssignedQty",label:'unAssignedQty',hidden:true,sortable:false, resizeable:false},
            {key:"estimateMSheetLength",label:'Estimate<br>MSheetLength',hidden:true,sortable:false, resizeable:false}
           ];
        var woDataSource = new YAHOO.util.DataSource();
        woDataTable = new YAHOO.widget.DataTable("woTable",
                woColumns, woDataSource);
         woDataTable.on('cellClickEvent',function (oArgs) {
            var target = oArgs.target;
            var record = this.getRecord(target);
            var column = this.getColumn(target);
            if (column.key == 'Add') { 
			woDataTable.addRow({SlNo:woDataTable.getRecordSet().getLength()+1});
			}
			dom.get("activity_Id").value=record.getData("activity"); 
            dom.get("woDetailsRecId").value=record.getId();
            <s:if test="%{model.id==null || (sourcepage == 'inbox' &&( model.currentState.value == 'NEW' || model.currentState.value=='REJECTED'))}">
				if (column.key == 'Delete') {
					removeDetails(dom.get("activity_Id").value);
	            	this.deleteRow(record);
	            	recalculateAmount();
	                allRecords=this.getRecordSet();
	                for(i=0;i<allRecords.getLength();i++){
	                  this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
	                }
	            }
            </s:if>
            if (column.key == 'woEstimateMSheetDtls') {
            	if(record.getData("estimateMSheetLength")==0) {
                       alert("No Estimate Measurement details found for this Activity");
               		}
            	else
                    showEstimateMSheet(record.getData("activity"));
            }
           	if(column.key == 'woMSheetDtls') {
           		if(record.getData("estimateMSheetLength")==0) {
                       alert("No Estimate Measurement details found for this Activity");
               		}
            	else
           			makeJSONCall(["description","Id","no","uomLength","width","depthOrHeight","identifier","uom"],'${pageContext.request.contextPath}/workorder/ajaxWorkOrder!getEstimateMSheetDetails.action',{activityId:record.getData("activity")},detailedWOMSheetSuccessHandler,detailedWOMSheetFailureHandler);
           	}
           	
           	if(column.key=='approvedQuantity'){
	       	if(record.getData("estimateMSheetLength")!=0) {
	       		dom.get("approvedQuantity"+record.getId()).disabled=true; 
	       		dom.get("approvedQuantity"+record.getId()).readOnly=true;
	       	}
	       }
        });
        var tfoot = woDataTable.getTbodyEl().parentNode.createTFoot();
		var tr = tfoot.insertRow(-1);
		var th = tr.appendChild(document.createElement('td'));
		th.colSpan = 9;
		th.className= 'whitebox4wk';
		th.innerHTML = '&nbsp;';

		var td = tr.insertCell(1);
		td.className= 'whitebox4wk';
		td.id = 'Total';
		td.innerHTML = '<span class="bold">Total:</span>';
		addCell(tr,2,'amtTotal','0.00');
		dom.get("amtTotal").align='right';
		var th1 = tr.appendChild(document.createElement('td'));
		th1.colSpan = 3;
		th1.className= 'whitebox4wk';
		th1.innerHTML = '&nbsp;';
    }
    
detailedWOMSheetSuccessHandler=function(req,res){
	woMSheetDataTable.deleteRows(0,woMSheetDataTable.getRecordSet().getLength());
	var allresults=res.results;
    for(var i=0;i<allresults.length;i++){
	     woMSheetDataTable.addRow({
	     activityId:dom.get("activity_Id").value,
	     estimateMSheetId:allresults[i].Id,
	     SlNo:woMSheetDataTable.getRecordSet().getLength()+1,
	     apprvdEstimateMSheetNo:allresults[i].no,
	     apprvdEstimateMSheetLength:allresults[i].uomLength,
	     apprvdEstimateMSheetWidth:allresults[i].width,
	     apprvdEstimateMSheetDH:allresults[i].depthOrHeight,
	   	 apprvdEstimateMSheetDeduction:'',
	  	 description:allresults[i].description,
	     number:"",
	     length:"",
	     width:"",
		 depthOrHeight:"",
		 quantity:"",
		 uom:allresults[i].uom
		 });
		 var record = woMSheetDataTable.getRecord(parseInt(i));
		 if(allresults[i].identifier=='D')
		   	 dom.get("apprvdEstimateMSheetDeduction"+record.getId()).checked=true;
	   	 else
	   	 	dom.get("apprvdEstimateMSheetDeduction"+record.getId()).checked=false;	
	   	 dom.get("apprvdEstimateMSheetDeduction"+record.getId()).disabled='true';
	     dom.get("apprvdEstimateMSheetDeduction"+record.getId()).readOnly='true';
		 disableWOMsheetFields(record);
    }
	var estimateMSCont = document.getElementById('woMSheetDetailsContainer');
	estimateMSCont.style.display='block';
	estimateMSCont.style.top = '425px';
	estimateMSCont.style.left = '80px';
	estimateMSCont.style.width = '60%';
	estimateMSCont.style.height = '40%';	
	document.getElementById('detailedWOMSheetTable').style.display='block';
	document.getElementById('buttons').style.display='block';  
	clearErrorMessage();	
	reLoad(dom.get("activity_Id").value);	
	<s:if test="%{((sourcepage=='inbox' && model.currentState.value=='CHECKED') || (sourcepage=='inbox' && model.currentState.value!='REJECTED' && model.currentState.value!='END') || (mode=='search'))}">
		disableWOMSheetTable();
	</s:if> 
}    

detailedWOMSheetFailureHandler=function(req,res) {
	alert("Unable to load Work Order Measurement Sheet");
}

 
function showEstimateMSheet(activityId){
	makeJSONCall(["description","no","uomLength","width","depthOrHeight","identifier","uom","quantity"],'${pageContext.request.contextPath}/workorder/ajaxWorkOrder!getEstimateMSheetDetails.action',{activityId:activityId},estimateMSheetSuccessHandler,estimateMSheetFailureHandler);
}

estimateMSheetSuccessHandler=function(req,res){
	woEstimateMSheetDataTable.deleteRows(0,woEstimateMSheetDataTable.getRecordSet().getLength()); 
	var estimateQtyTotal=0;
	var allresults=res.results;
	var quantity=0;
    for(var i=0;i<allresults.length;i++){
	  woEstimateMSheetDataTable.addRow({SlNo:woEstimateMSheetDataTable.getRecordSet().getLength()+1,
	  	 description:allresults[i].description,
	     number:allresults[i].no,
	     length:allresults[i].uomLength,
	     width:allresults[i].width,
		 depthOrHeight:allresults[i].depthOrHeight,
		 quantity:allresults[i].quantity,
		 uom:allresults[i].uom,
		 deduction:''
		});
		var record = woEstimateMSheetDataTable.getRecord(parseInt(i));
		if(allresults[i].identifier=='D'){
		   	 dom.get("deduction"+record.getId()).checked=true;
		   	 if(record.getData("quantity")!="" && record.getData("quantity")>0){
		   	 	quantity=record.getData("quantity");
				quantity="-"+quantity;
				woEstimateMSheetDataTable.updateCell(record,woEstimateMSheetDataTable.getColumn('quantity'),quantity);
		   	 }
	   	} 
	   	else
	   	 dom.get("deduction"+record.getId()).checked=false;	
	   	dom.get("deduction"+record.getId()).disabled='true';
	    dom.get("deduction"+record.getId()).readOnly='true';
	    estimateQtyTotal=estimateQtyTotal+getNumber(record.getData("quantity"));
    }
    
	var estimateMSCont = document.getElementById('estimateMSheetContainer');
	estimateMSCont.style.display='block';
	estimateMSCont.style.top = '425px';
	estimateMSCont.style.left = '80px';
	estimateMSCont.style.width = '50%';
	estimateMSCont.style.height = '40%';	
	document.getElementById('estimateMSheetTable').style.display='block';
	
	
   	dom.get("quantityTotal").innerHTML=roundTo(estimateQtyTotal);
} 

estimateMSheetFailureHandler=function(req,res) {
	alert("Unable to load Estimate Measurement Sheet");
}


</script>
                      <div id="header-container">
                      <table id="table-header" cellpadding="0" cellspacing="0" align="center">
                       <tr>
                      <td colspan="5" class="headingwk">
                      		<div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" /></div>
                      		<div class="headplacer"><s:text name='page.title.workorder.detail' /></div>
          				</td>	
          				<td align="right" class="headingwk" style="border-left-width: 0px">
          				 <s:if test="%{noOfTenderResponses > 1}"> 
          				 <s:if test="%{model.id==null || (sourcepage == 'inbox' && (model.currentState.value == 'NEW' || model.currentState.value=='REJECTED'))}">
			       			<a id="addHref" href="#" onclick="getActivities();">
			       			<img border="0" alt="Add Activities" src="${pageContext.request.contextPath}/image/add.png" /></a>
			       			</s:if>
			       		</s:if>
			       		</td>
                      </tr>
                      </table>
                       </div>
                      <s:if test="%{type=='workOrderForRC'}">
                      	<s:hidden name="estimateRcId" id="estimateRcId" value="%{estimateRcId}"></s:hidden>
                      <div id="header-container">
	                        <table id="table-header" cellpadding="0" cellspacing="0" align="center">
									<tr>
											<th width="2%"><s:text name='column.title.SLNo' /></th>	
											<th width="8%"><s:text name='workorder.sch.no' /></th>
											<th width="8%"><s:text name='workorder.quantity' /></th>
											<th width="7%"><s:text name='workorder.mSheet' /></th>
											<th width="25%"><s:text name='workorder.description' /></th>
											<th width="8%"><s:text name='workorder.uom' /></th>
											<th width="8%"><s:text name='workorder.workorderrate' /></th>
											<th width="8%"><s:text name='workorder.amount' /></th>
									</tr>
							</table>
                           </div>
                            <s:hidden name="workOrderValue" id="workOrderValue"></s:hidden>
                            <table width="100%" border="0" cellpadding="0" cellspacing="0" id="table-body" name="tenderNegoSearchInnerTable">		
                           <s:if test="%{rcEstimate.estimateRCDetailList.size != 0}">
                            <s:iterator id="tenderIterator" value="rcEstimate.estimateRCDetailList" status="row_status">
                            <s:hidden name="amount" id="amount" value="%{activity.amount.value}"></s:hidden>	
                           						
												<tr>
													<td width="2%"><s:property value="#row_status.count" /></td>
													<td width="8%"><s:property value='%{activity.schedule.code}' /></td>
													<td width="8%"><div align="right"><s:property value='%{activity.quantity}' /></div></td>
													<s:if test="%{activity.measurementSheetList.size!=0}">
														<td width="7%">
																<img height="16" border="0" width="16" alt="Search" src="${pageContext.request.contextPath}/image/page_edit.png" onclick="showEstimateMSheet(<s:property value='%{activity.id}' />);" />
														</td>
													</s:if>
													<s:else>
														<td width="7%">
																<img height="16" border="0" width="16" alt="Search" src="${pageContext.request.contextPath}/image/page_edit.png" onclick="alert('No Estimate Measurement details found for this Activity');" />
														</td>
													</s:else>
													<s:if test="%{activity.schedule.code!=null && schedule.code!=''}">
													<td width="25%"><s:property value='%{activity.schedule.summary}' /><a href="#" class="hintanchor" onMouseover="showhint('<s:property value="%{activity.descriptionJS}" />', this, event, '300px')"><img src="${pageContext.request.contextPath}/image/help.png" alt="Help" width="16" height="16" border="0" align="absmiddle"/></a></td>
													</s:if>
													<s:else>
													<td width="25%"><s:property value='%{activity.description}' /></td>
													</s:else>
													<td width="8%"><s:property value='%{activity.uom.uom}' /></td>
													<td width="8%"><div align="right"><s:property value='%{activity.rate.value}' />
												   	</div></td>
													<td width="8%"><div align="right"><s:text name="contractor.format.number" >
                                                    <s:param name="value" value="%{activity.amount.value}"/></s:text></div></td>
                                           			
                                           			</tr>
													</s:iterator>
													<tr>
                           <td>&nbsp;</td>
                           <td colspan="5" /><td><div align="left"><b><s:text name="Total" /></b></div></td><td><div align="right"><s:text name="contractor.format.number" ><s:param value='%{workOrderAmount}' /></s:text></div></td>
                         
                           </tr>                    
                           </s:if>
                           	<s:elseif test="%{rcEstimate.estimateRCDetailList.size == 0}">
                               <tr>
								<td align="center">
											<font color="red">No record Found.</font>
										</td>
							 </tr>
							 </s:elseif>
                          
                           </table>
                      </s:if> 
                       
                  <s:elseif test="%{tenderResponse!=null && noOfTenderResponses==1}">  
                      <div id="header-container">
	                        <table id="table-header" cellpadding="0" cellspacing="0" align="center">
									<tr>
											<th width="2%"><s:text name='column.title.SLNo' /></th>	
											<th width="8%"><s:text name='workorder.sch.no' /></th>
											<th width="8%"><s:text name='workorder.quantity' /></th>
											<th width="7%"><s:text name='workorder.mSheet' /></th>
											<th width="25%"><s:text name='workorder.description' /></th>
											<th width="8%"><s:text name='workorder.uom' /></th>
											<th width="8%"><s:text name='workorder.workorderrate' /></th>
											<th width="8%"><s:text name='workorder.amount' /></th>
									</tr>
									
							</table>
                           </div>
                           <s:hidden name="workOrderValue" id="workOrderValue"></s:hidden>
                            <table width="100%" border="0" cellpadding="0" cellspacing="0" id="table-body" name="tenderNegoSearchInnerTable">		
                           <s:if test="%{ActivitiesForWorkorder.size != 0}">
                              <s:iterator id="tenderIterator" value="ActivitiesForWorkorder" status="row_status">	
                           						<tr>
													<td width="2%"><s:property value="#row_status.count" /></td>
													<td width="8%"><s:property value='%{code}' /></td>
													<td width="8%"><div align="right"><s:property value='%{quantity}' /></div></td>
													
													<s:if test="%{activity.measurementSheetList.size!=0}">
														<td width="7%">
																<img height="16" border="0" width="16" alt="Search" src="${pageContext.request.contextPath}/image/page_edit.png" onclick="showEstimateMSheet(<s:property value='%{activity.id}' />);" />
														</td>
													</s:if>
													<s:else>
														<td width="7%"></td>
													</s:else>
													
													<s:if test="%{code!=null && code!=''}">
													<td width="25%"><s:property value='%{summary}' /><a href="#" class="hintanchor" onMouseover="showhint('<s:property value="%{descriptionJS}" />', this, event, '300px')"><img src="${pageContext.request.contextPath}/image/help.png" alt="Help" width="16" height="16" border="0" align="absmiddle"/></a></td>
													</s:if>
													<s:else>
													<td width="25%"><s:property value='%{description}' /></td>
													</s:else>
													<td width="8%"><s:property value='%{uom}' /></td>
												   	<td width="8%"><div align="right"><s:property value='%{rate}' />
												    </div></td>
												   	<td width="8%"><div align="right"><s:text name="contractor.format.number" >
                                                    <s:param name="value" value="%{amt}"/></s:text></div></td>
												</tr>
												</s:iterator>
							<tr>
                           <td>&nbsp;</td>
                           <td colspan="5" /><td><div align="left"><b><s:text name="Total" /></b></div></td><td><div align="right"><s:text name="contractor.format.number" ><s:param value='%{workOrderAmount}' /></s:text></div></td>
                         
                           </tr>                    
                           </s:if>
                           	<s:elseif test="%{ActivitiesForWorkorder.size == 0}">
                               <tr>
								<td align="center">
											<font color="red">No record Found.</font>
										</td>
							 </tr>
                           </s:elseif>
                           </table>
                  </s:elseif>   <s:else>   		
			                <div class="yui-skin-sam">
							<div id="woTable"></div>
						</div>
						<script>makeWODataTable();
						<s:if test="%{!hasErrors() }">
						<s:iterator id="woActivityIterator" value="woActivities" status="actrow_status">
		            	 <s:if test="%{activity.schedule!=null}">
		                  	woDataTable.addRow({SlNo:'<s:property value="#actrow_status.count"/>',
		                                    schNo:'<s:property value="activity.schedule.code"/>',
		                                    tenderresponseactivityid:'<s:property value="tenderResponseActivityId"/>',
		                                    workorderDescription:'<s:property value="activity.schedule.summaryJS"/>',
		                                    estimate:'<s:property value="activity.abstractEstimate.estimateNumber"/>',
		                                    estimateQuantity:'<s:property value="activity.quantity"/>',
		                                    approvedQuantity:'<s:property value="approvedQuantity"/>',
		                                    workorderUOM:'<s:property value="activity.schedule.uom.uom"/>',
		                                    workorderRate:'<s:property value="approvedRate"/>',
		                                    approvedRate:'<s:property value="approvedRate"/>',
		                                    activity:'<s:property value="activity.id"/>',
		                                    workorderAmount:roundTo('<s:property value="approvedAmount"/>'),
		                                    uomFactor:'<s:property value="activity.conversionFactor"/>',
		                                    origApprovedQty:'<s:property value="approvedQuantity"/>',
		                                    unAssignedQty:'<s:property value="unAssignedQuantity"/>',
		                                    estimateMSheetLength:'<s:property value="activity.measurementSheetList.size"/>',						
		                                    Add:'X',
		                                    Delete:'X'});
    	
   						</s:if>
   						<s:else>
   							woDataTable.addRow({SlNo:'<s:property value="#actrow_status.count"/>',
		                                    schNo:'',
		                                    tenderresponseactivityid:'<s:property value="tenderResponseActivityId"/>',
		                                    workorderDescription:'<s:property value="activity.nonSor.descriptionJS"/>',
		                                    estimate:'<s:property value="activity.abstractEstimate.estimateNumber"/>',
		                                    estimateQuantity:'<s:property value="activity.quantity"/>',
		                                    approvedQuantity:'<s:property value="approvedQuantity"/>',
		                                    workorderUOM:'<s:property value="activity.nonSor.uom.uom"/>',
		                                    workorderRate:'<s:property value="approvedRate"/>',
		                                    approvedRate:'<s:property value="approvedRate"/>',
		                                    activity:'<s:property value="activity.id"/>',
		                                    workorderAmount:roundTo('<s:property value="approvedAmount"/>'),
		                                    uomFactor:'<s:property value="activity.conversionFactor"/>',
		                                    origApprovedQty:'<s:property value="approvedQuantity"/>',
		                                    unAssignedQty:'<s:property value="unAssignedQuantity"/>',
		                                    estimateMSheetLength:'<s:property value="activity.measurementSheetList.size"/>',
		                                    Add:'X',
		                                    Delete:'X'});
   		
   						</s:else>
   					
   						var record = woDataTable.getRecord(parseInt('<s:property value="#actrow_status.index"/>')); 
   
   					 	dom.get("amtTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("amtTotal") - 0.0 + getNumber(record.getData("workorderAmount")));
		             </s:iterator>
		           </s:if>
		          <s:else>
		          	<s:iterator id="woActivityIterator" value="actionWorkOrderActivityList" status="actrow_status">
		            	 <s:if test="%{activity.schedule!=null}">
		                 	 woDataTable.addRow({SlNo:'<s:property value="#actrow_status.count"/>',
		                                    schNo:'<s:property value="activity.schedule.code"/>',
		                                    tenderresponseactivityid:'<s:property value="tenderResponseActivityId"/>',
		                                    workorderDescription:'<s:property value="activity.schedule.summaryJS"/>',
		                                    estimate:'<s:property value="activity.abstractEstimate.estimateNumber"/>',
		                                    estimateQuantity:'<s:property value="activity.quantity"/>',
		                                    approvedQuantity:'<s:property value="approvedQuantity"/>',
		                                    workorderUOM:'<s:property value="activity.schedule.uom.uom"/>',
		                                    workorderRate:'<s:property value="approvedRate"/>',
		                                    approvedRate:'<s:property value="approvedRate"/>',
		                                    activity:'<s:property value="activity.id"/>',
		                                    workorderAmount:roundTo('<s:property value="approvedRate"/>*<s:property value="approvedQuantity"/>*<s:property value="activity.conversionFactor"/>'),
		                                    uomFactor:'<s:property value="activity.conversionFactor"/>',
		                                    origApprovedQty:'<s:property value="approvedQuantity"/>',
		                                    unAssignedQty:'<s:property value="unAssignedQuantity"/>',
		                                    estimateMSheetLength:'<s:property value="activity.measurementSheetList.size"/>',
		                                    Add:'X',
		                                    Delete:'X'});
    	
   						</s:if>
   						<s:else>
    						woDataTable.addRow({SlNo:'<s:property value="#actrow_status.count"/>',
		                                    schNo:'',
		                                    tenderresponseactivityid:'<s:property value="tenderResponseActivityId"/>',
		                                    workorderDescription:'<s:property value="activity.nonSor.descriptionJS"/>',
		                                    estimate:'<s:property value="activity.abstractEstimate.estimateNumber"/>',
		                                    estimateQuantity:'<s:property value="activity.quantity"/>',
		                                    approvedQuantity:'<s:property value="approvedQuantity"/>',
		                                    workorderUOM:'<s:property value="activity.nonSor.uom.uom"/>',
		                                    workorderRate:'<s:property value="approvedRate"/>',
		                                    approvedRate:'<s:property value="approvedRate"/>',
		                                    activity:'<s:property value="activity.id"/>',
		                                    workorderAmount:roundTo('<s:property value="approvedRate"/>*<s:property value="approvedQuantity"/>'),
		                                    uomFactor:'<s:property value="activity.conversionFactor"/>',
		                                    origApprovedQty:'<s:property value="approvedQuantity"/>',
		                                    unAssignedQty:'<s:property value="unAssignedQuantity"/>',
		                                    estimateMSheetLength:'<s:property value="activity.measurementSheetList.size"/>',
		                                    Add:'X',
		                                    Delete:'X'});
   		
   						</s:else>
   					
   						var record = woDataTable.getRecord(parseInt('<s:property value="#actrow_status.index"/>')); 
   
   						 dom.get("amtTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("amtTotal") - 0.0 + getNumber(record.getData("workorderAmount")));
		             </s:iterator>
		          </s:else>
		       </s:else>
		          </script>