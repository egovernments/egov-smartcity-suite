<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
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
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
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
  ~
  --%>

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
<script type="text/javascript">

function getActivities() {
var act=new Array();
	for(var i=0;i<woDataTable.getRecordSet().getLength();i++){
			 act[i]=getNumber(dom.get('activity'+woDataTable.getRecord(i).getId()).value);
	}
	window.open("${pageContext.request.contextPath}/tender/searchTenderResponseActivities.action?tenderRespId="+
	dom.get("tenderRespId").value+"&selectedactivities="+act,"","height=600,width=900,scrollbars=yes,left=0,top=0,status=yes");
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
	makeJSONCall(["tenderresponseactivityid","activityId","schno","description","estimateno","estimateqty","uom","negotiatedRate","negotiatedQty","uomfactor"],'${pageContext.request.contextPath}/workorder/ajaxWorkOrder!getTenderResponseActivityList.action',{traIds:trAId},activityLoadHandler,activityLoadFailureHandler) ;
}

activityLoadHandler = function(req,res){
  results=res.results;
  for(var k=0;k<results.size();k++){
 	woDataTable.addRow({SlNo:woDataTable.getRecordSet().getLength()+1,schNo:results[k].schno,workorderDescription:results[k].description,
 	estimate:results[k].estimateno,estimateQuantity:results[k].estimateqty,approvedQuantity:eval(results[k].estimateqty-results[k].negotiatedQty),workorderUOM:results[k].uom,
 	workorderRate:results[k].negotiatedRate,workorderAmount:'0.00',uomFactor:results[k].uomfactor,approvedRate:results[k].negotiatedRate,
 	activity:results[k].activityId,tenderresponseactivityid:results[k].tenderresponseactivityid,origApprovedQty:eval(results[k].estimateqty-results[k].negotiatedQty),unAssignedQty:eval(results[k].estimateqty-results[k].negotiatedQty) });
 }
 recalculateAmount();
}

activityLoadFailureHandler= function(){
    alert('Unable to load activities');
}

function calculateAmount(elem,recordId,column){
	if(!validateNumberInTableCell(woDataTable,elem,recordId)) return;
	if(!validateApprovedQtyNew(elem,recordId,column)) return;
    calculateTotalAmount(recordId,record);
}
function calculateTotalAmount(recordId,record){
	 oldWOAmount=getNumber(record.getData("workorderAmount"));
    woDataTable.updateCell(record,woDataTable.getColumn('workorderAmount'),roundTo(getNumber(dom.get('approvedQuantity'+recordId).value)*getNumber(record.getData("uomFactor"))*getNumber(record.getData("workorderRate"))));
    var totalWOAmount=roundTo(getNumericValueFromInnerHTML("amtTotal") - oldWOAmount + getNumber(record.getData("workorderAmount")));
	 dom.get("amtTotal").innerHTML=totalWOAmount;
	<s:if test="%{tenderResponseType!=null && percTenderType.equalsIgnoreCase(tenderResponseType)}" >
	 var negoPercAbs=Math.abs('<s:property value="tenderResponse.percNegotiatedAmountRate" />');
	 var negoPerc='<s:property value="tenderResponse.percNegotiatedAmountRate" />'; 
	 dom.get("activityAssignedAmt").value=totalWOAmount;
	 if(eval(negoPerc)>=0) {
		 totalWOAmount=roundTo(eval(totalWOAmount)+(eval(totalWOAmount)/100*eval(negoPercAbs)));
	 }
	 else {
		 totalWOAmount=roundTo(eval(totalWOAmount)-(eval(totalWOAmount)/100*eval(negoPercAbs)));
	 }
	 document.getElementById("workOrderAmount").value=totalWOAmount;
	 dom.get("negotiatedAmt").innerHTML=totalWOAmount;
     var securityDepositConfValue='<s:property value="%{securityDepositConfValue}"/>';
     var labourWelfareFundConfValue='<s:property value="%{labourWelfareFundConfValue}"/>';
     document.getElementById("securityDeposit").value=roundTo(eval(securityDepositConfValue)/100*totalWOAmount);
     document.getElementById("labourWelfareFund").value=roundTo(eval(labourWelfareFundConfValue)/100*totalWOAmount);
	</s:if>
	<s:else>
	 document.getElementById("workOrderAmount").value=totalWOAmount;
     var securityDepositConfValue='<s:property value="%{securityDepositConfValue}"/>';
     var labourWelfareFundConfValue='<s:property value="%{labourWelfareFundConfValue}"/>';
     document.getElementById("securityDeposit").value=roundTo(eval(securityDepositConfValue)/100*totalWOAmount);
     document.getElementById("labourWelfareFund").value=roundTo(eval(labourWelfareFundConfValue)/100*totalWOAmount);
    </s:else>
}

function recalculateAmount(){
	for(var i=0;i<woDataTable.getRecordSet().getLength();i++){
			 calculateTotalAmount(woDataTable.getRecord(i).getId(),woDataTable.getRecord(i));
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
     <s:if test="%{sourcepage=='inbox' 
         && (model.egwStatus!=null && (model.egwStatus.code=='NEW' || model.egwStatus.code=='REJECTED')) }">//To ignore the saved qty while validating
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
     <s:if test="%{model.egwStatus!=null && (model.egwStatus.code=='NEW' || model.egwStatus.code=='REJECTED') }">//To ignore the saved qty while validating
     	var originalApprovedQty=getNumber(record1.getData("origApprovedQty"));
     	unassignedQty=unassignedQty+originalApprovedQty;
 	</s:if>
 	 <s:if test="%{(model.egwStatus==null || model.egwStatus.code=='NEW' || model.egwStatus.code=='REJECTED') }">//To ignore the saved qty while validating
    if(qty>unassignedQty){
 		dom.get('errorapprovedQuantity'+recordId).style.display='';
 		 dom.get("workOrder_error").style.display='';
    	 document.getElementById("workOrder_error").innerHTML='<s:text name="workOrder.quantitynotgreaterthanestimateqty" />';
    	return false;
     }
     </s:if>
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
  var fieldName = "actionWorkOrderActivities[" + oRecord.getCount() + "]." + oColumn.getKey() + ".id";
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
            {key:"approvedQuantity",label:'<s:text name="workorder.woquantity"/>',formatter:woQuantityTextboxFormatter, sortable:false, resizeable:false},
            {key:"workorderUOM",label:'<s:text name="workorder.uom"/>', sortable:false, resizeable:false},
            {key:"workorderRate",label:'<s:text name="workorder.workorderrate.title"/>', sortable:false, resizeable:false,className:'align-right'},
            {key:"workorderAmount",label:'<s:text name="workorder.amount.title"/>', sortable:false, resizeable:false,className:'align-right'},
            {key:'Delete',label:'<s:text name="column.title.delete"/>',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")},
		    {key:"uomFactor",label:'Unit Rate', hidden:true,sortable:false, resizeable:false},
            {key:"approvedRate",label:'approvedRate',formatter:hiddenFormatter, hidden:true,sortable:false, resizeable:false},
            {key:"activity",label:'activity',formatter:activityHiddenFormatter, hidden:true,sortable:false, resizeable:false},
            {key:"tenderresponseactivityid",label:'tenderresponseactivityid',formatter:tenderresponseactivityidHiddenFormatter, hidden:true,sortable:false, resizeable:false},
            {key:"origApprovedQty",label:'origApprovedQty',hidden:true,sortable:false, resizeable:false},
            {key:"unAssignedQty",label:'unAssignedQty',hidden:true,sortable:false, resizeable:false}
            
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
            if (column.key == 'Delete') {
            	this.deleteRow(record);
            	recalculateAmount(); 
                allRecords=this.getRecordSet();
                for(i=0;i<allRecords.getLength();i++){
                  this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
                }
                
            }
            
        });
        
        var tfoot = woDataTable.getTbodyEl().parentNode.createTFoot();
		var tr = tfoot.insertRow(-1);
		var th = tr.appendChild(document.createElement('td'));
		th.colSpan = 7;
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



//new code added
	<s:if test="%{tenderResponseType!=null && percTenderType.equalsIgnoreCase(tenderResponseType)}" >

		tr = tfoot.insertRow(-1);
		th = tr.appendChild(document.createElement('td'));
		th.colSpan = 7;
		th.className= 'whitebox4wk';
		th.innerHTML = '&nbsp;';

		td = tr.insertCell(1);
		td.className= 'whitebox4wk';
		td.id = 'percentage';
		td.innerHTML = '<span class="bold">Percentage Negotiated:</span>';
		addCell(tr,2,'percentageNegotiated',roundTo('<s:property value="tenderResponse.percNegotiatedAmountRate" />'));
		dom.get("percentageNegotiated").align='right';
		th1 = tr.appendChild(document.createElement('td'));
		th1.colSpan = 3;
		th1.className= 'whitebox4wk';
		th1.innerHTML = '&nbsp;';

		tr = tfoot.insertRow(-1);
		th = tr.appendChild(document.createElement('td'));
		th.colSpan = 7;
		th.className= 'whitebox4wk';
		th.innerHTML = '&nbsp;';

		td = tr.insertCell(1);
		td.className= 'whitebox4wk';
		td.id = 'totalafterNego';
		td.innerHTML = '<span class="bold">Work Order Amount:</span>';
		addCell(tr,2,'negotiatedAmt','0.00');
		dom.get("negotiatedAmt").align='right';
		th1 = tr.appendChild(document.createElement('td'));
		th1.colSpan = 3;
		th1.className= 'whitebox4wk';
		th1.innerHTML = '&nbsp;';
	</s:if>
    }
</script>
                      <div id="header-container">
                      <table id="table-header" cellpadding="0" cellspacing="0" align="center">
                       <tr>
                      <td colspan="5" class="headingwk">
                      		<div class="arrowiconwk"><image src="<egov:url path='/resources/erp2/images/arrow.gif'/>" /></div>
                      		<div class="headplacer"><s:text name='page.title.workorder.detail' /></div>
          				</td>	
          				<td align="right" class="headingwk" style="border-left-width: 0px">
          				 <s:if test="%{tenderResponse != null && tenderResponse.tenderResponseContractors.size != 1}"> 
			       			<a id="addHref" href="#" onclick="getActivities();">
			       			<img border="0" alt="Add Activities" src="/egworks/resources/erp2/images/add.png" /></a>
			       		</s:if>
			       		</td>
                      </tr>
                      </table>
                       </div>
                  <s:if test="%{!isRCEstimate && tenderResponse != null && tenderResponse.tenderResponseContractors.size == 1}">  
                      <div id="header-container">
	                        <table id="table-header" cellpadding="0" cellspacing="0" align="center">
									<tr>
											<th width="2%"><s:text name='column.title.SLNo' /></th>	
											<th width="8%"><s:text name='workorder.sch.no' /></th>
											<th width="8%"><s:text name='workorder.quantity' /></th>
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
													<s:if test="%{code!=null && code!=''}">
													<td width="25%"><s:property value='%{summary}' /><a href="#" class="hintanchor" onMouseover='showhint("<s:property value='%{description}' />", this, event, "300px")'><img src="/egworks/resources/erp2/images/help.gif" alt="Help" width="16" height="16" border="0" align="absmiddle"/></a></td>
													</s:if>
													<s:else>
													<td width="25%"><s:property value='%{description}' /></td>
													</s:else>
													<td width="8%"><s:property value='%{uom}' /></td>
												   	<td width="8%"><div align="right"><s:text name="contractor.format.number" >
												   	<s:param name="rate" value='%{rate}' /></s:text></div></td>
												    <td width="8%"><div align="right"><s:text name="contractor.format.number" >
                                                    <s:param name="value" value="%{amt}"/></s:text></div></td>
												</tr>
												</s:iterator>
							<tr>
                           <td>
                           <td  colspan="4"><td><div align="left"><b><s:text name="Total" /></b></div></td></td><td><div align="right"><s:text name="contractor.format.number" ><s:param value='%{tenderResponse.workOrderAmount}' /></s:text></div></td>
                           </td>
                           </tr>                    
							<s:if test="%{tenderResponseType!=null && percTenderType.equalsIgnoreCase(tenderResponseType)}" >
								<tr>
	                           <td>
	                           <td  colspan="4"><td><div align="left"><b><s:text name="workorder.percentage.negotiated" /></b></div></td></td><td><div align="right"><s:text name="contractor.format.number" ><s:param value='%{tenderResponse.percNegotiatedAmountRate}' /></s:text></div></td>
	                           </td>
	                           </tr>                    
								<tr>
	                           <td>
	                           <td  colspan="4"><td><div align="left"><b><s:text name="workorder.total" /></b></div></td></td><td><div align="right"><s:text name="contractor.format.number" ><s:param value='%{workOrder.workOrderAmount}' /></s:text></div></td>
	                           </td>
	                           </tr>                    
							</s:if>
                           </s:if>
                           	<s:elseif test="%{ActivitiesForWorkorder.size == 0}">
                               <tr>
								<td align="center">
											<font color="red"><s:text name="label.no.records.found" /></font>
										</td>
							 </tr>
                           </s:elseif>
                           </table>
                  </s:if>                                 
                  <s:else>   		
			                <div class="yui-skin-sam">
							<div id="woTable"></div>
						</div>
						<script>makeWODataTable();
						
		          <s:if test="%{!hasErrors() }"> 
		         	 <s:iterator id="woActivityIterator" value="woActivities" status="actrow_status">
		            	 <s:if test="%{activity.schedule!=null}">
		                  	woDataTable.addRow({SlNo:'<s:property value="#actrow_status.count"/>',
		                                    schNo:'<s:property value="activity.schedule.code"/>',
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
		                                    Add:'X',
		                                    Delete:'X'});
    	
   						</s:if>
   						<s:else>
    						woDataTable.addRow({SlNo:'<s:property value="#actrow_status.count"/>',
		                                    schNo:'',
		                                    workorderDescription:'<s:property value="activity.nonSor.descriptionJS" escape="false"/>',
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
		                                    Add:'X',
		                                    Delete:'X'});
   		
   						</s:else>
   					
   						var record = woDataTable.getRecord(parseInt('<s:property value="#actrow_status.index"/>')); 
   
   					 	dom.get("amtTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("amtTotal") - 0.0 + getNumber(record.getData("workorderAmount")));
   					 	</s:iterator>
	   					 recalculateAmount(); //for calculate totals in case of validation error
   				  </s:if>
		          <s:else>
		          	<s:iterator id="woActivityIterator" value="actionWorkOrderActivityList" status="actrow_status">
		            	 <s:if test="%{activity.schedule!=null}">
		                 	 woDataTable.addRow({SlNo:'<s:property value="#actrow_status.count"/>',
		                                    schNo:'<s:property value="activity.schedule.code"/>',
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
		                                    Add:'X',
		                                    Delete:'X'});
    	
   						</s:if>
   						<s:else>
    						woDataTable.addRow({SlNo:'<s:property value="#actrow_status.count"/>',
		                                    schNo:'',
		                                    workorderDescription:'<s:property value="activity.nonSor.descriptionJS" escape="false"/>',
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
		                                    Add:'X',
		                                    Delete:'X'});
   		
   						</s:else>
   					
   						var record = woDataTable.getRecord(parseInt('<s:property value="#actrow_status.index"/>')); 
   
   						 dom.get("amtTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("amtTotal") - 0.0 + getNumber(record.getData("workorderAmount")));
		             </s:iterator>
		             recalculateAmount();
		          </s:else>
		       </s:else>
         		<s:if test="%{tenderResponse != null && tenderResponse.getTenderResponseContractors().size()>1}" >
                    var totalWOAmount=roundTo(getNumericValueFromInnerHTML("amtTotal"));
					if(eval(totalWOAmount)>0) {
						<s:if test="%{tenderResponseType!=null && percTenderType.equalsIgnoreCase(tenderResponseType)}" >
					 		var negoPercAbs=Math.abs('<s:property value="tenderResponse.percNegotiatedAmountRate" />');
					 		var negoPerc='<s:property value="tenderResponse.percNegotiatedAmountRate" />';
					 		dom.get("activityAssignedAmt").value=totalWOAmount; 
					 		if(eval(negoPerc)>=0) {
						 		totalWOAmount=roundTo(eval(totalWOAmount)+(eval(totalWOAmount)/100*eval(negoPercAbs)));
					 		}
					 		else {
						 		totalWOAmount=roundTo(eval(totalWOAmount)-(eval(totalWOAmount)/100*eval(negoPercAbs)));
					 		}
					 		document.getElementById("workOrderAmount").value=totalWOAmount;
					 		dom.get("negotiatedAmt").innerHTML=totalWOAmount;
				     		var securityDepositConfValue='<s:property value="%{securityDepositConfValue}"/>';
				     		var labourWelfareFundConfValue='<s:property value="%{labourWelfareFundConfValue}"/>';
				     		document.getElementById("securityDeposit").value=roundTo(eval(securityDepositConfValue)/100*eval(totalWOAmount));
				    		document.getElementById("labourWelfareFund").value=roundTo(eval(labourWelfareFundConfValue)/100*eval(totalWOAmount));
						</s:if>
					}
				</s:if>
				</script>
