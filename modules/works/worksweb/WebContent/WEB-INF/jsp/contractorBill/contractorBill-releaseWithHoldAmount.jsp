<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  

<script src="<egov:url path='js/works.js'/>"></script>
<style type="text/css">
#yui-dt0-bodytable, #yui-dt1-bodytable, #yui-dt2-bodytable {
    Width:200%;
} 	
.yui-dt-col-amount{
	text-align:right;
}
</style>
<script>
var retMoneyDropdownOptions=[{label:"--- Select ---", value:"0"},
    <s:iterator var="s" value="retentionMoneyAccountList" status="status">    
    {"label":"<s:property value="%{glcode}"/>-<s:property value="%{name}"/>",
    "value":"<s:property value="%{id}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
        
    function createreleaseWHAmountTextboxFormatter(size,maxlength){
		var textboxFormatter = function(el, oRecord, oColumn, oData) {
		    var value = (YAHOO.lang.isValue(oData))?oData:"";
		    var id=oColumn.getKey()+oRecord.getId();
		    var fieldName = "releaseWithHeldAmountDeductions[" + oRecord.getCount() + "]." + oColumn.getKey();
		    markup="<input type='text' id='"+id+"' value='"+value+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' class='selectamountwk' onblur='validatereleaseWHAmount(this,\""+oRecord.getId()+"\");calculateTotalBillAmount(\"field\");' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
		    el.innerHTML = markup;
		}
		return textboxFormatter;
	}
	var releaseWHAmountTextboxFormatter = createreleaseWHAmountTextboxFormatter(10,8);
	
	function validatereleaseWHAmount(elem,recordId){
	dom.get("contractorBill_error").style.display="none";
		record=releaseAmountDataTable.getRecord(recordId);
      if(!validateNumberInTableCell(releaseAmountDataTable,elem,recordId)) return;
       var totalWHAmount=getNumber(record.getData("totalamount"));
       var releaseAmount=dom.get("debitamount"+record.getId()).value;
      if(totalWHAmount>0 && totalWHAmount>=releaseAmount ){
      	dom.get("debitamount"+record.getId()).value =  roundTo(releaseAmount);
      	calculateTotal();
      }
      else{
      	dom.get("contractorBill_error").style.display="block";
    	dom.get('contractorBill_error').innerHTML='<s:text name="releaseWHAmount.amount.error"/>';
      	dom.get("debitamount"+record.getId()).value =  0;
      }
    }
	
	function createRetDescTextboxFormatter(size,maxlength) {
		var textboxDescFormatter = function(el, oRecord, oColumn, oData) {
		   var value = (YAHOO.lang.isValue(oData))?oData:"";
		   var fieldName = "releaseWithHeldAmountDeductions[" + oRecord.getCount() + "]." + oColumn.getKey();   
		   var id = oColumn.getKey()+oRecord.getId();
		   markup="<input type='text' id='"+id+"' class='selectmultilinewk' value='"+value+"' size='"+size+"' maxlength='"+maxlength+"' name='"+fieldName+ "'/>";
		   el.innerHTML = markup; 
		}
		return textboxDescFormatter;	
	}
	var retDescTextboxFormatter = createRetDescTextboxFormatter(25,50);
    
var releaseAmountDataTable;
	var makereleaseAmountDataTable = function() {
			var releaseAmountColumnDefs = [ 
				{key:"SlNo", label:'<s:text name="column.title.SLNo"/>', sortable:false, resizeable:false},				
				{key:"glcodeid", label:'<s:text name="releaseWHAmount.column.accCode"/>', formatter:createDropdownFormatter('releaseWithHeldAmountDeductions',''), dropdownOptions:retMoneyDropdownOptions},
				{key:"totalamount", label:'<s:text name="releaseWHAmount.column.amountwithholding"/>',  sortable:false, resizeable:true},				
				{key:"debitamount",label:'<s:text name="releaseWHAmount.column.releaseamount"/><span class="mandatory">*</span>', formatter:releaseWHAmountTextboxFormatter,sortable:false, resizeable:false},
				{key:'delete',label:'Clear',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}  
			];
			var releaseAmountDataSource = new YAHOO.util.DataSource(); 
			releaseAmountDataTable = new YAHOO.widget.DataTable("releaseAmountDataTable",releaseAmountColumnDefs, releaseAmountDataSource, {MSG_EMPTY:"<s:text name='contractorBill.retentionMoney.initial.table.message'/>"});
			releaseAmountDataTable.addRow({SlNo:releaseAmountDataTable.getRecordSet().getLength()+1});
			releaseAmountDataTable.on('cellClickEvent',function (oArgs) {
				var target = oArgs.target;
				var record = this.getRecord(target);
				var column = this.getColumn(target);
				if (column.key == 'delete') { 		
						this.deleteRow(record);
						releaseAmountDataTable.addRow({SlNo:releaseAmountDataTable.getRecordSet().getLength()+1});
						calculateTotal();
						allRecords=this.getRecordSet();
						for(i=0;i<allRecords.getLength();i++){
							this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
						}
				}        
			});
			releaseAmountDataTable.on('dropdownChangeEvent',function (oArgs) {
    			var record = this.getRecord(oArgs.target);
    			var column = this.getColumn(oArgs.target);
	   			if(column.key=='glcodeid'){
	   				var records =releaseAmountDataTable.getRecordSet();
	   				var row_id = records.getRecordIndex(record);
	   				var recordId=record.getId();
	   				var glCodeId = dom.get("glcodeid" + record.getId()).value;
	   				var estimateId=dom.get('estimateId').value;
	   				var workOrderId = dom.get("workOrderId").value;
					var workOrderEstimateId = dom.get("workOrderEstimateId").value;
					var billDate=dom.get('billdate').value;
	   				var url='';
					url = '${pageContext.request.contextPath}/contractorBill/ajaxContractorBill!getWithHoldAmount.action';
					if(glCodeId>0){
						makeJSONCall(["totalWithHoldAmount","recordId"],url,{glCodeId:glCodeId,workOrderEstimateId:workOrderEstimateId,recordId:recordId,billDate:billDate},withHoldAmountLoadHandler,withHoldAmountLoadFailureHandler);
					}
					else{
						releaseAmountDataTable.updateCell(record,releaseAmountDataTable.getColumn('totalamount'),0.0);
					}
	   			
	    	   	}
    		});		
			return {
			    oDS: releaseAmountDataSource,
			    oDT: releaseAmountDataTable
			}; 
 	}
 withHoldAmountLoadHandler = function(req,res){
 	dom.get("contractorBill_error").style.display="none";
	    var results=res.results;
	    var record1=releaseAmountDataTable.getRecord(results[0].recordId);
	   releaseAmountDataTable.updateCell(record1,releaseAmountDataTable.getColumn('totalamount'),results[0].totalWithHoldAmount);
	     }

   withHoldAmountLoadFailureHandler= function(){
    dom.get("contractorBill_error").style.display="block";
    dom.get('contractorBill_error').innerHTML='<s:text name="releaseWHAmount.WHamount.error"/>'; 
   }
</script>
 <tr><td colspan="4">

<table border="0" cellpadding="0" cellspacing="0" align="center" width="100%" >
<tr>
		<td colspan="4" class="headingwk">
			<div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
			<div class="headplacer"><s:text name="contractorBill.tab.releasewithholdamount" /></div>
		</td>
		</tr>
     	<tr>
       	<td colspan="4">
       	<div class="yui-skin-sam">
       	    <div id="releaseAmountDataTable"></div>                    	
       	</div>
       	<script>
           var workOrderEstimateId = dom.get("workOrderEstimateId").value;
	   	   var billDate=dom.get('billdate').value;
               makereleaseAmountDataTable();
                <s:if test="%{ReleaseWHMoneyDetails.size != 0}">
			releaseAmountDataTable.deleteRows(0,releaseAmountDataTable.getRecordSet().getLength());
		</s:if>
		  <s:iterator id="retentionMoneyListiterator" value="ReleaseWHMoneyDetails" status="row_status">
		              releaseAmountDataTable.addRow({SlNo:'<s:property value="#row_status.count"/>',
                      					    glcodeid:'<s:property value="glcodeid"/>',
		                                    debitamount:'<s:property value="debitamount"/>',
		                                    Delete:'X'});
		  	var record2 = releaseAmountDataTable.getRecord(parseInt('<s:property value="#row_status.index"/>')); 
    		var recordId2=record2.getId();
	   		var glCodeId = dom.get("glcodeid" + record2.getId()).value;
	   		var url= '${pageContext.request.contextPath}/contractorBill/ajaxContractorBill!getWithHoldAmount.action';
					
    		makeJSONCall(["totalWithHoldAmount","recordId"],url,{glCodeId:glCodeId,workOrderEstimateId:workOrderEstimateId,recordId:recordId2,billDate:billDate},withHoldAmountLoadHandler,withHoldAmountLoadFailureHandler);
						
		 </s:iterator>
        </script>
       	</td>
       </tr>

</table>
</tr></td>
