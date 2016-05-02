<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency,
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It
# 	   is required that all modified versions of this material be marked in
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program
# 	   with regards to rights under trademark law for use of the trade names
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->

<style type="text/css">
#yui-dt0-bodytable,#yui-dt1-bodytable,#yui-dt2-bodytable {
	Width: 100%;
}
</style>

<script>


// YUI DataTable
var financialYearDropdownOptions=[{label:"--- Select ---", value:"0"},
    <s:iterator var="s" value="dropdownData.financialYearList" status="status">  
    {"label":"<s:property value="%{finYearRange}"/>" ,
    	"value":"<s:property value="%{id}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
 
var rateTextboxFormatter = createRateTextBoxFormatter(5,5);  
function createRateTextBoxFormatter(size,maxlength){
	var textboxFormatter = function(el, oRecord, oColumn, oData) {
	    var value = (YAHOO.lang.isValue(oData))?oData:"";
	    var id=oColumn.getKey()+oRecord.getId();    
	    var fieldName = "depMetaDatas[" + oRecord.getCount() + "]." + oColumn.getKey();
	    markup="<input type='text' class='selectamountwk' id='"+id+"' name='"+fieldName+"' size='"+
	    	size+"' maxlength='"+
	    	maxlength+"' onblur='calculateMaxLife(this,\""+
	    	oRecord.getId()+"\");' />%<span id='error"+id+
	    	"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	    el.innerHTML = markup;
	}
	return textboxFormatter;
}

var lifeTextboxFormatter = createLifeTextBoxFormatter(5,5);  
function createLifeTextBoxFormatter(size,maxlength){
	var textboxFormatter = function(el, oRecord, oColumn, oData) {
	    var value = (YAHOO.lang.isValue(oData))?oData:"";
	    var id=oColumn.getKey()+oRecord.getId();    
	    var fieldName = "";
	    markup="<input type='text' readonly class='selectamountwk' id='"+id+"' name='"+fieldName+"' size='"+
	    	size+"' maxlength='"+maxlength+"' /><span id='error"+id+
	    	"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	    el.innerHTML = markup;
	}
	return textboxFormatter;
}

var finYearIdHiddenFormatter= createFinYearIDFormatter(10,10);
function createFinYearIDFormatter(el, oRecord, oColumn){
    var hiddenFormatter = function(el, oRecord, oColumn, oData) {
	    var value = (YAHOO.lang.isValue(oData))?oData:"";
	    var id=oColumn.getKey()+oRecord.getId();
	    var fieldName = "depMetaDatas[" + oRecord.getCount() + "]." + oColumn.getKey() + ".id";
	    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+
	    	value+"'/><span id='error"+id+
	    	"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	    el.innerHTML = markup;
	}
	return hiddenFormatter;
}

var depDetailDataTable;
var makeDepDetailDataTable= function() {
	var cellEditor=new YAHOO.widget.TextboxCellEditor()
	var depDetailColumnDefs = [		
		{key:"SlNo", label:'Sl No', sortable:false, resizeable:false},
		{key:"financialYear", hidden:true, formatter:finYearIdHiddenFormatter, sortable:false, resizeable:false},
		{key:"year", label:'Financial Year', formatter:"dropdown", dropdownOptions:financialYearDropdownOptions},	
		{key:"depreciationRate",label:'Depreciation Rate',formatter:rateTextboxFormatter, sortable:false, resizeable:false},
		{key:"life",label:'Useful Life in Years',formatter:lifeTextboxFormatter, sortable:false, resizeable:false}	
	];
		
	var depDetailDataSource = new YAHOO.util.DataSource(); 
	depDetailDataTable= new YAHOO.widget.DataTable("depDetailTable",depDetailColumnDefs, depDetailDataSource , {initialRequest:"query=orders&results=10"});	
	depDetailDataTable.subscribe("cellClickEvent", depDetailDataTable.onEventShowCellEditor); 
	
	depDetailDataTable.on('dropdownChangeEvent', function (oArgs) {	
	    var record = this.getRecord(oArgs.target);
        var column = this.getColumn(oArgs.target);
        if(column.key=='year'){
    	    var selectedIndex=oArgs.target.selectedIndex; 
    	    validateDuplicate(this.getRecordSet(),oArgs); 
        	this.updateCell(record,this.getColumn('financialYear'),financialYearDropdownOptions[selectedIndex].value);
        }
	});
	// depDetailDataTable.addRow({SlNo:depDetailDataTable.getRecordSet().getLength()+1,year:"${currentFinancialYearId}",financialYear:"${currentFinancialYearId}"});	
	
	return {
	    oDS: depDetailDataSource,
	    oDT: depDetailDataTable
	};        

}


var unsignedInt = /^\d*$/;
var unsignedDecimal=/^\d*\.?\d*$/;

function calculateMaxLife(elem,recordId){
	var rate = dom.get("depreciationRate"+recordId).value
	if(rate.match(unsignedDecimal) && rate != ""){
		if(rate==0){
			dom.get("category_error").style.display='block';
			document.getElementById("category_error").innerHTML='Rate should be greater than Zero';
		}
		else{
			dom.get("life"+recordId).value = Math.round(100/rate);
			dom.get("category_error").style.display='none';
		}
	}
	else if(rate == ""){
		dom.get("life"+recordId).value ="";
	}
	else{
		dom.get("life"+recordId).value ="";
		dom.get("category_error").style.display='block';
		document.getElementById("category_error").innerHTML='Rate is Invalid. Rate should be greater than Zero';
		dom.get("life"+recordId).focus();
	}
}

function getMaxLife(recordId){
	var rate = dom.get("depreciationRate"+recordId).value
	if(rate.match(unsignedDecimal) && rate != ""){
		return Math.round(100/rate);
	}
	else{
		return "";
	}
}

function validateDuplicate(records,oArgs){
    for(i=0;i<records.getLength();i++){ 
     var selectedIndex=oArgs.target.selectedIndex;
       if(dom.get("financialYear"+records.getRecord(i).getId()).value==financialYearDropdownOptions[selectedIndex].value){
          document.getElementById("category_error").innerHTML='The Year '+financialYearDropdownOptions[selectedIndex].label+' is already selected'; 
          document.getElementById("category_error").style.display='';
          oArgs.target.selectedIndex=0;
          return;
       } 
       else if(dom.get("financialYear"+records.getRecord(i).getId()).value=='' && financialYearDropdownOptions[selectedIndex].value=="${currentFinancialYearId}" && i==0){
       	  document.getElementById("category_error").innerHTML='The Year '+financialYearDropdownOptions[selectedIndex].label+' is already selected'; 
          document.getElementById("category_error").style.display='';
          oArgs.target.selectedIndex=0;
          return;
       }       
       else{
       	 document.getElementById("category_error").style.display='none';
       	 document.getElementById("category_error").innerHTML='';	
       	 }
    }
}
</script>
<table id="depDetailsTable" width="100%" border="0" cellspacing="0"
	cellpadding="0">
	<tr>
		<td colspan="3" class="headingwk">
			<div class="arrowiconwk">
				<img src="/egassets/resources/erp2/images/arrow.gif" />
			</div>
			<div class="headplacer">
				<s:text name='title.dep.details' />
			</div>
		</td>
		<td align="right" class="headingwk">
			<a href="#"
				onclick="depDetailDataTable.addRow({SlNo:depDetailDataTable.getRecordSet().getLength()+1});return false;">
				<img border="0" alt="Add Depreciation"
					src="/egassets/resources/erp2/images/add.png" /> </a>
		</td>
	</tr>
	<tr>
		<td align="center" colspan="3">
			<div class="yui-skin-sam">
				<div id="depDetailTable"></div>
			</div>
			<script>
				makeDepDetailDataTable();		    	
				<s:iterator id="depDetailIterator" value="depreciationMetaDataList" status="row_status">
			        depDetailDataTable.addRow(
		                                {SlNo:'<s:property value="#row_status.count"/>',
		        						financialYear:'<s:property value="financialYear.id"/>',
		                                year:'<s:property value="financialYear.finYearRange"/>',
		                                depreciationRate:'<s:property value="depreciationRate"/>'
		                                });
		                                    
		        var record = depDetailDataTable.getRecord(parseInt('<s:property value="#row_status.index"/>'));
		    
		        var column = depDetailDataTable.getColumn('year');
		        for(i=0; i < financialYearDropdownOptions.length; i++) {
		            if (financialYearDropdownOptions[i].value == '<s:property value="financialYear.id"/>') {
		                depDetailDataTable.getTdEl({record:record, column:column}).getElementsByTagName("select").item(0).selectedIndex = i;
		            }
		        } 
		        
		        var column = depDetailDataTable.getColumn('depreciationRate');  
		        dom.get(column.getKey()+record.getId()).value = '<s:property value="depreciationRate"/>';

				var column = depDetailDataTable.getColumn('life');  
		        dom.get(column.getKey()+record.getId()).value = getMaxLife(record.getId());

		        </s:iterator>
      		</script>
		</td>
	</tr>
	<tr>
		<td colspan="4" class="shadowwk"></td>
	</tr>
	<tr>
		<td align="right" colspan="4">
			<div align="right" class="mandatory"
				style="font-size: 11px; padding-right: 20px;">
				*
				<s:text name="default.message.mandatory" />
			</div>
		</td>
	</tr>
</table>
