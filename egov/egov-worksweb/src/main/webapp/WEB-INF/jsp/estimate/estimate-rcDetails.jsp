<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 
<style type="text/css">
#yui-dt0-bodytable, #yui-dt1-bodytable, #yui-dt2-bodytable {
    Width:100%;
} 
.yui-dt0-col-TaxAmt {
  width: 5%;
}
.yui-dt-col-Percentage{
	text-align:right;
}

</style> 
<script>
       
var rcDetailsDataTable;
var makeRCDetailsDataTable= function() {
	var rcDetailsColumnDefs = [		
		{key:"SlNo", label:'<s:text name="column.title.SLNo" />', sortable:false, resizeable:false},
		{key:"rcNo",label:'<s:text name="ratecontract.rcNo.label" />', sortable:false, resizeable:false},
		{key:"fromDate",label:'<s:text name="ratecontract.fromdate.label" />', sortable:false, resizeable:false},
		{key:"toDate",label:'<s:text name="ratecontract.todate.label" />', sortable:false, resizeable:false},
		{key:"contractorNameCode",label:'<s:text name="ratecontract.contractorNameCode.label" />', sortable:false, resizeable:false},
		{key:"rcAmount",label:'<s:text name="ratecontract.rcamount.label" />', sortable:false, resizeable:false},
		{key:"utilizedAmount",label:'<s:text name="ratecontract.utilizedAmount.label" />', sortable:false, resizeable:false},
		{key:"budgetAvailable",label:'<s:text name="ratecontract.balanceAmount.label" />', sortable:false, resizeable:false}
	];
		
	var rcDetailsDataSource = new YAHOO.util.DataSource(); 
	rcDetailsDataTable= new YAHOO.widget.DataTable("rcDetailsTable",rcDetailsColumnDefs, rcDetailsDataSource , {initialRequest:"query=orders&results=10"});	
	rcDetailsDataTable.subscribe("cellClickEvent", rcDetailsDataTable.onEventShowCellEditor); 
			
	return {
	    oDS: rcDetailsDataSource,
	    oDT: rcDetailsDataTable
	};        

};


</script>

	<div class="errorstyle" id="rcDetails_error" style="display:none;"></div>
	<table id="rcDetailsHeaderTable" width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
	   		<td align="center" >
	   			<b>
		   			<s:text name ="ratecontract.label" />
		   			<input type="checkbox" id="rcCheckbox" checked="checked" disabled="disabled" />
		   			<s:hidden name="utilizedAmount"/>
		   		</b>	
	   		</td>
	   		<td colspan="2" >
		 		<s:text name ="ratecontract.type" />:
		   		<b><s:property value="rateContract.indentTender.tenderType" /></b>
	   		</td>
	   		<td colspan="2">&nbsp;</td>
	 	</tr>
	 	<tr>
	   		<td colspan="5" class="headingwk"><div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" /></div><div class="headplacer"><s:text name="ratecontract.estimate.header" /></div></td>
	 	</tr>
	 	<tr>
		   	<td colspan="5">
			   	<div class="yui-skin-sam">
			       	<div id="rcDetailsTable"></div>
			   	</div>
		   	</td>
		</tr>
	    <tr>
	   		<td colspan="5" class="shadowwk"></td>
		</tr>
	</table> 
<script>
	makeRCDetailsDataTable();
          rcDetailsDataTable.addRow(
                  {SlNo:'1',
                   rcNo:'<s:property value="rateContract.rcNumber"/>',   
                   fromDate:'<s:text name="format.date"><s:param value="%{rateContract.indent.startDate}"/></s:text>',
                   toDate:'<s:text name="format.date"><s:param value="%{rateContract.indent.endDate}"/></s:text>',
                   contractorNameCode:'<s:property value="rateContract.contractor.name"/>/<s:property value="rateContract.contractor.code"/>',
                   rcAmount:roundTo('<s:property value="rateContract.rcAmount"/>'),
                   utilizedAmount:roundTo('<s:property value="utilizedAmount"/>'),
                   budgetAvailable:roundTo('<s:property value="rateContract.rcAmount"/>'-'<s:property value="utilizedAmount"/>')});
</script>       
