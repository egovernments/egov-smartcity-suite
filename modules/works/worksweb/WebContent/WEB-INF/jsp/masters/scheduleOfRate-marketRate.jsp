<!--     ------------------------ starts on oct309 for market rate details ------------------------  -->
<script>
function createDeleteImageFormatterMR(baseURL){
	var deleteImageFormatter = function(el, oRecord, oColumn, oData) {
	
	    var imageURL=baseURL+"/image/cancel.png";
	    markup='<img height="16" border="0" width="16" alt="Delete" src="'+imageURL+'"/>';
	    el.innerHTML = markup;
	}
	return deleteImageFormatter;
}

function createTextBoxFormatterMR(size,maxlength) {
	var textboxFormatter1 = function(el, oRecord, oColumn, oData) {
	   var fieldName = "actionMarketRates[" + oRecord.getCount() + "]." +  oColumn.getKey();   
	   var id = oColumn.getKey()+oRecord.getId();	   
	   markup="<input type='text' id='"+id+"' class='selectmultilinewk' size='20' maxlength='"+maxlength+"' style=\"width:100px; text-align:right\" name='"+fieldName+ "'" 
	   + " onblur='validateNumberInTableCell(scheduleOfRateDataTableMR,this,\"" + oRecord.getId()+ "\");'/>"
	   + " <span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	   el.innerHTML = markup; 
	}
	return textboxFormatter1;	
}

var rateTextboxFormatterMR = createTextBoxFormatterMR(11,10);

var dateFormatterMR = function(e2, oRecord, oColumn, oData) {
	var fieldName = "actionMarketRates[" + oRecord.getCount() + "].validity." +  oColumn.getKey();
	var id =oColumn.getKey() + oRecord.getId();	
	var markup= "<input type='text' id='"+id+"' class='selectmultilinewk' size='20' maxlength='10' style=\"width:100px\" name='"+fieldName 
	            + "'  onkeyup=\"DateFormat(this,this.value,event,false,'3')\" onblur=\"validateDateFormat(this)\" />"
				+ " <span id='error"+ id +"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	 e2.innerHTML = markup;
}

function createHiddenIdFormatter(el, oRecord, oColumn, oData){
var hiddenIdFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName = "actionMarketRates[" + oRecord.getCount() + "]." + oColumn.getKey();
    var fieldValue=value;
    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+fieldValue+"' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
}
return hiddenIdFormatter;
}
var hiddenIdFormatter = createHiddenIdFormatter(10,10); 
 
 
var scheduleOfRateDataTableMR;
var makeScheduleOfRateDataTableForMR = function() {
	var cellEditor=new YAHOO.widget.TextboxCellEditor()
	var scheduleOfRateColumnDefsMR = [ 
		{key:"id", hidden:true,formatter:hiddenIdFormatter,sortable:false, resizeable:false} ,		
		{key:"SlNo", label:'Sl No', sortable:false, resizeable:false, width:50},
		{key:"marketRate", label:'<span class="mandatory">*</span>Market Rate', formatter:rateTextboxFormatterMR, sortable:false, resizeable:false, width:180},		
		{key:"startDate", label:'<span class="mandatory">*</span>Start Date', formatter:dateFormatterMR,sortable:false, resizeable:false, width:130},
		{key:"endDate",label:'End Date', formatter:dateFormatterMR,sortable:false, resizeable:false, width:130},
		{key:'deleteRate',label:'Delete',formatter:createDeleteImageFormatterMR("${pageContext.request.contextPath}")}  
	];
	
	// DataSource for a server accessed over XHR   
//var scheduleOfRateDataSourceMR = new YAHOO.util.XHRDataSource(url);  
//new YAHOO.util.DataSource(url);
//var scheduleOfRateDataSourceMR=new YAHOO.util.DataSource(url);
	
	var scheduleOfRateDataSourceMR = new YAHOO.util.DataSource(); 
	
	scheduleOfRateDataTableMR = new YAHOO.widget.DataTable("scheduleOfRateTableMR",scheduleOfRateColumnDefsMR, scheduleOfRateDataSourceMR, {MSG_EMPTY:"<s:text name='master.sor.initial.table.message'/>"});
	scheduleOfRateDataTableMR.subscribe("cellClickEvent", scheduleOfRateDataTableMR.onEventShowCellEditor); 
	scheduleOfRateDataTableMR.on('cellClickEvent',function (oArgs) {
		var target = oArgs.target;
		var record = this.getRecord(target);
		var column = this.getColumn(target);
		if (column.key == 'deleteRate') { 			
				this.deleteRow(record);
				allRecords=this.getRecordSet();
				for(i=0;i<allRecords.getLength();i++){
					this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
				}
		} 
		
	});
	return {
	    oDS: scheduleOfRateDataSourceMR,
	    oDT: scheduleOfRateDataTableMR
	};  
}
</script>
<!-- -------------------- ends  on oct309 for market rate details  --------------------- -->

	  <tr>
      <td>&nbsp;</td>
      </tr>
          <tr>
            <td>
            <table id="marketRatesTable" width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td colspan="3" class="headingwk" style="border-right-width: 0px">
				<div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
				<div class="headplacer"><s:text name="sor.marketrateDetails" /></div>
				</td>
				<td align="right" class="headingwk" style="border-left-width: 0px">
				<a href="#" onclick="scheduleOfRateDataTableMR.addRow({SlNo:scheduleOfRateDataTableMR.getRecordSet().getLength()+1});return false;">
				<img border="0" alt="Add SOR Market Rate" src="${pageContext.request.contextPath}/image/add.png" />
				</a>
				</td>
			</tr>
		<tr>
			<td colspan="4">
			<div class="yui-skin-sam">
			<div id="scheduleOfRateTableMR"></div>
	<script>
	var imgURL="${pageContext.request.contextPath}/image/cancel.png";	
            makeScheduleOfRateDataTableForMR();
			
            <s:iterator id="marketRateIterator" value="model.marketRates" status="marketrate_row_status">
				        scheduleOfRateDataTableMR.addRow(
			        						{id:'<s:property value="id"/>',											
			                                SlNo:'<s:property value="#marketrate_row_status.count"/>',
			                                marketRate:'<s:property value="marketRate"/>',
			                                startDate:'<s:property value="validity.startDate"/>',
			                                endDate:'<s:property value="validity.endDate"/>'
											}
											);
			        var record = scheduleOfRateDataTableMR.getRecord(parseInt('<s:property value="#marketrate_row_status.index"/>'));										
					var column = scheduleOfRateDataTableMR.getColumn('marketRate');  
			        dom.get(column.getKey()+record.getId()).value = '<s:property value="marketRate"/>';
			        
			        var column = scheduleOfRateDataTableMR.getColumn('startDate');  
			        dom.get(column.getKey()+record.getId()).value = '<s:property value="validity.startDate"/>';
			       			        
			        var column = scheduleOfRateDataTableMR.getColumn('endDate');  
			        dom.get(column.getKey()+record.getId()).value = '<s:property value="validity.endDate"/>';
		</s:iterator>	
       </script>
  		</div>
		</td>
	</tr>
    </table>
    </td>
   </tr> 
   <script>
	scheduleOfRateDataTableMR.removeListener('cellClickEvent');
   </script>
