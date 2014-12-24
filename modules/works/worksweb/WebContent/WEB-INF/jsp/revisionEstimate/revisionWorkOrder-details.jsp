<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<style type="text/css">
</style>

<script src="<egov:url path='js/works.js'/>"></script>
<script src="<egov:url path='css/works.css'/>"></script>
<script type="text/javascript">

var checkboxFormatter = function(el, oRecord, oColumn, oData){
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
	markup="<input type='checkbox' 	 id='"+id+"' />"; 
    el.innerHTML = markup; 
}


var revisionWOEstimateMSheetDataTable;
var revisionWOEstimateMSheetCol;
var makeRevisionWOEstimateMSheetDataTable = function() { 
    var cellEditor=new YAHOO.widget.TextboxCellEditor();
    revisionWOEstimateMSheetCol = [ 
	   	{key:"SlNo",label:'<s:text name="column.title.SLNo"/>', sortable:false, resizeable:false},
	   	{key:"description",label:'<s:text name="revisionWO.estimate.msheet.description"/>',sortable:false, resizeable:false},
	   	{key:"number",label:'<s:text name="revisionWO.estimate.msheet.no"/>',sortable:false, resizeable:false},
	   	{key:"length",label:'<s:text name="revisionWO.estimate.msheet.length"/>',sortable:false, resizeable:false},
	   	{key:"width",label:'<s:text name="revisionWO.estimate.msheet.width"/>',sortable:false, resizeable:false},
	   	{key:"depthOrHeight",label:'<s:text name="revisionWO.estimate.msheet.depthorheight"/>',sortable:false, resizeable:false},
	   	{key:"quantity",label:'<s:text name="revisionWO.estimate.msheet.quantity"/>',sortable:false, resizeable:false},
	   	{key:"uom",label:'<s:text name="revisionWO.estimate.msheet.uom"/>',sortable:false, resizeable:false},
	   	{key:"deduction",label:'<s:text name="revisionWO.estimate.msheet.deduction"/>',formatter:checkboxFormatter,sortable:false, resizeable:false}
	 ];
    var revisionWOEstimateMSheetDataSource = new YAHOO.util.DataSource();
    revisionWOEstimateMSheetDataTable = new YAHOO.widget.DataTable("revisionWOEstimateMSheetTable",revisionWOEstimateMSheetCol, revisionWOEstimateMSheetDataSource);
    revisionWOEstimateMSheetDataTable.subscribe("cellClickEvent", revisionWOEstimateMSheetDataTable.onEventShowCellEditor); 
    revisionWOEstimateMSheetDataTable.on('cellClickEvent',function (oArgs) {
        var target = oArgs.target;
        var record = this.getRecord(target);
        var column = this.getColumn(target); 
    });
    var tfoot = revisionWOEstimateMSheetDataTable.getTbodyEl().parentNode.createTFoot();
	var tr = tfoot.insertRow(-1);
	var th = tr.appendChild(document.createElement('td'));
	th.colSpan = 5;
	th.className= 'whitebox4wk';
	th.innerHTML = '&nbsp;';
	
	var td = tr.insertCell(1);
	td.className= 'whitebox4wk';
	td.id = 'revisionWOEstimateMSheetTotal';
	td.innerHTML = '<span class="bold">Total:</span>';
	addCell(tr,2,'quantityTotal','0');
	addCell(tr,3,'filler','');
	addCell(tr,4,'filler','');
	return {
        oDS: revisionWOEstimateMSheetDataSource,
        oDT: revisionWOEstimateMSheetDataTable 
    };
}

function showEstimateMSheet(activityId){
	makeJSONCall(["description","no","uomLength","width","depthOrHeight","identifier","uom","quantity"],'${pageContext.request.contextPath}/workorder/ajaxWorkOrder!getEstimateMSheetDetails.action',{activityId:activityId},revisionEstimateMSheetSuccessHandler,revisionEstimateMSheetFailureHandler);
}


revisionEstimateMSheetSuccessHandler=function(req,res){
	revisionWOEstimateMSheetDataTable.deleteRows(0,revisionWOEstimateMSheetDataTable.getRecordSet().getLength()); 
	var estimateQtyTotal=0;
	var allresults=res.results;
	var quantity=0;
    for(var i=0;i<allresults.length;i++){
	  revisionWOEstimateMSheetDataTable.addRow({SlNo:revisionWOEstimateMSheetDataTable.getRecordSet().getLength()+1,
	  	 description:allresults[i].description,
	     number:allresults[i].no,
	     length:allresults[i].uomLength,
	     width:allresults[i].width,
		 depthOrHeight:allresults[i].depthOrHeight,
		 quantity:allresults[i].quantity,
		 uom:allresults[i].uom,
		 deduction:''
		});
		var record = revisionWOEstimateMSheetDataTable.getRecord(parseInt(i));
		if(allresults[i].identifier=='D'){
		   	 dom.get("deduction"+record.getId()).checked=true;
		   	 if(record.getData("quantity")!="" && record.getData("quantity")>0){
		   	 	quantity=record.getData("quantity");
				quantity="-"+quantity;
				revisionWOEstimateMSheetDataTable.updateCell(record,revisionWOEstimateMSheetDataTable.getColumn('quantity'),quantity);
		   	 }
	   	} 
	   	else
	   	 dom.get("deduction"+record.getId()).checked=false;	
	   	dom.get("deduction"+record.getId()).disabled='true';
	    dom.get("deduction"+record.getId()).readOnly='true';
	    estimateQtyTotal=estimateQtyTotal+getNumber(record.getData("quantity"));
    }
    
	var estimateMSCont = document.getElementById('revisionEstimateMSheetContainer');
	estimateMSCont.style.display='block';
	estimateMSCont.style.top = '425px';showEstimateMSheet
	estimateMSCont.style.left = '80px';
	estimateMSCont.style.width = '50%';
	estimateMSCont.style.height = '40%';	
	document.getElementById('revisionEstimateMSheetTable').style.display='block';
	
	
   	dom.get("quantityTotal").innerHTML=roundTo(estimateQtyTotal);
} 

revisionEstimateMSheetFailureHandler=function(req,res) {
	alert("Unable to load Estimate Measurement Sheet");
}


function minimizeContainer(obj) {
	var msCont;
	if(obj.id=='minRevisionEstimateMSheet'){
	     msCont = document.getElementById('revisionEstimateMSheetContainer');
	     if (obj.innerHTML == '-'){
	             obj.innerHTML = '^';
	             obj.title='Maximize';
	             msCont.style.width = '21%';
	             msCont.style.height = '45px';
	             msCont.style.left = '25%';
	             msCont.style.top = '37%';
	     } else {
	             obj.innerHTML = '-';
	             obj.title='Minimize';
	             msCont.style.width = '65%';
	             msCont.style.height = '40%';
	             msCont.style.left = '80px';
	             msCont.style.top = '425px'; 
	     }
	}
}

function closeContainer(obj) {
	if(obj.id=='closeRevisionEstimateMSheet'){
		document.getElementById('revisionEstimateMSheetContainer').style.display='none';
		document.getElementById('minRevisionEstimateMSheet').innerHTML='-';
		document.getElementById('minRevisionEstimateMSheet').title='Minimize';
	}
}


</script>
	<div id="header-container">
	<table id="table-header" cellpadding="0" cellspacing="0" align="center">
	 <tr>
		<td colspan="5" class="headingwk">
			<div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" /></div>
			<div class="headplacer"><s:text name='page.title.workorder.detail' /></div>
		</td>	
     </tr>
   	</table>
    </div>
	                                
  <div id="header-container">
     <table id="table-header" cellpadding="0" cellspacing="0" align="center">
		<tr>
			<th width="2%"><s:text name='column.title.SLNo' /></th>	
			<th width="9%"><s:text name='revisionWO.sch.no' /></th>
			<th width="25%"><s:text name='revisionWO.description' /></th>
			<th width="9%"><s:text name='workorder.revision.type' /></th>
			<th width="9%"><s:text name='workorder.revised.quantity' /></th>
			<th width="9%"><s:text name='revisionWO.mSheet' /></th>
			<th width="9%"><s:text name='revisionWO.uom' /></th>
			<th width="9%"><s:text name='revisionWO.workorderrate' /></th>
			<th width="9%">Revision <s:text name='revisionWO.amount' /></th>
		</tr>
	  </table>
	</div>
	                      
	<table width="100%" border="0" cellpadding="0" cellspacing="0" id="table-body" name="reWODetailsTable">
	<s:if test="%{workOrderEstimates!=null  && workOrderEstimates.get(0).workOrderActivities.size != 0}">
		<s:iterator id="woeIterator" value="workOrderEstimates.get(0).workOrderActivities" status="row_status">
			<tr>
				<td width="2%"><s:property value="#row_status.count" /></td>
				
				<s:if test="%{activity.schedule!=null}">
				<td width="9%"><s:property value='%{activity.schedule.code}' /></td>
				</s:if>
				<s:else>
				<td width="9%"></td>
				</s:else>
	
				<s:if test="%{activity.schedule!=null}">
				<td width="25%"><s:property value='%{activity.schedule.description}' /><a href="#" class="hintanchor" onMouseover="showhint('<s:property value="%{activity.schedule.descriptionJS}" />', this, event, '300px')"><img src="${pageContext.request.contextPath}/image/help.png" alt="Help" width="16" height="16" border="0" align="absmiddle"/></a></td>
				</s:if>
				<s:else>
				<td width="25%"><s:property value='%{activity.nonSor.description}' /><a href="#" class="hintanchor" onMouseover="showhint('<s:property value="%{activity.nonSor.descriptionJS}" />', this, event, '300px')"><img src="${pageContext.request.contextPath}/image/help.png" alt="Help" width="16" height="16" border="0" align="absmiddle"/></a></td>
				</s:else>
	
				<td width="9%"><s:property value='%{activity.revisionType}' /></td>
	
				<td width="9%"><div align="right"><s:property value='%{approvedQuantity}' /></div></td>
	
				<s:if test="%{woMeasurementSheetList.size!=0}">
				<td width="9%">
					<img height="16" border="0" width="16" alt="Search" src="${pageContext.request.contextPath}/image/page_edit.png" onclick="showEstimateMSheet(<s:property value='%{activity.id}' />);" />
				</td>
				</s:if>
				<s:else>
				<td width="9%"></td>
				</s:else>
	
				<td width="9%"><s:property value='%{activity.uom.uom}' /></td>
				
				<td width="9%"><div align="right"><s:property value='%{approvedRate}' /></div></td>
				
				<td width="9%"><div align="right"><s:text name="contractor.format.number" >
					<s:param name="value" value="%{approvedAmount}"/></s:text></div></td>
			</tr>
		</s:iterator> 
		<tr>
	       <td>&nbsp;</td>
	       <td colspan="6" /><td><div align="left"><b><s:text name="Total" /></b></div></td><td><div align="right"><s:text name="contractor.format.number" ><s:param value='%{workOrderAmount}' /></s:text></div></td>
		</tr>                    
	</s:if>
	<s:elseif test="%{workOrderEstimates!=null  && workOrderEstimates.get(0).workOrderActivities.size  == 0}">
        <tr>
			<td align="center">
				<font color="red">No record Found.</font>
			</td>
		</tr>
    </s:elseif>
    </table>
	             	
	<div class="yui-skin-sam"> 
		<div id="woTable"></div>
	</div>
						
	<div style="position: obsolute;" id="revisionEstimateMSheetContainer" class="tableContainer">
 	<span class="titles msheettitle" >Measurement Sheet Details</span>
	<span class="titler msheetminimize" onclick="minimizeContainer(this);" id="minRevisionEstimateMSheet" title="Minimize">-</span>
	<span class="titler msheetclose" onclick="closeContainer(this);" id="closeRevisionEstimateMSheet" title="Close">X</span>
	<table  height="25px" border="0" cellspacing="0" width="100%" id="movebar">
		<tr height="11px" class="titlrTr1" width="100%"><td width="100%" class="bordertopb" align="right"></td></tr>
		<tr height="14px" class="titlrTr2" width="100%"><td width="100%" class="borderbtmb"></td></tr>
	</table>

	<table id="revisionEstimateMSheetTable" width="100%" border="0" cellspacing="0" cellpadding="0" style="display: none"> 
		<tr>
         	<td colspan="8" class="shadowwk"></td>
     	</tr> 
       	<tr>
         <td  class="headingwk" colspan="8" style="border-right-width: 0px;">
          <div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" /></div>
          <div class="headplacer"><s:text name='revisionWorkOrder.estimate.msheet'/></div>
         </td>
       	</tr>  
      
        <tr>
          <td>
          <div class="yui-skin-sam">
              <div id="revisionWOEstimateMSheetTable"></div>  
              <div id="revisionWOEstimateMSheetTotal"></div>  
          </div>
          </td>
        </tr>
	</table>
</div>

<script>
  makeRevisionWOEstimateMSheetDataTable(); 
</script>						