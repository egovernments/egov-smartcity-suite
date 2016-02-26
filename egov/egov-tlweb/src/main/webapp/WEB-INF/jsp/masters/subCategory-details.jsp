<!-- -------------------------------------------------------------------------------
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
#yui-dt0-bodytable, #yui-dt1-bodytable, #yui-dt2-bodytable {
    Width:100%;
} 
</style>
<style>
#subCategoryMappingTable table{
	width:98%;
	margin:0 auto;
}
</style>
<script>

function createScdIDFormatter(el, oRecord, oColumn){
	var hiddenFormatter = function(el, oRecord, oColumn, oData) {
	    var value = (YAHOO.lang.isValue(oData))?oData:"";
	    var id=oColumn.getKey()+oRecord.getId();
	    markup="<input type='hidden' id='"+id+"' value='"+value+"'/>";
	    el.innerHTML = markup;
	}
	return hiddenFormatter;
}
var scdIdHiddenFormatter= createScdIDFormatter(10,10); 

function createIDFormatter(el, oRecord, oColumn){
	var hiddenFormatter = function(el, oRecord, oColumn, oData) {
	    var value = (YAHOO.lang.isValue(oData))?oData:"";
	    var id=oColumn.getKey()+oRecord.getId();
	    var fieldName = "subCategoryMappingDetails[" + oRecord.getCount() + "]." + oColumn.getKey() + ".id";
	    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+value+"'/>";
	    el.innerHTML = markup;
	}
	return hiddenFormatter;
}
var hiddenFormatter= createIDFormatter(10,10); 

function createRateTypeFormatter(el, oRecord, oColumn){
	var hiddenFormatter = function(el, oRecord, oColumn, oData) { 
	    var value = (YAHOO.lang.isValue(oData))?oData:"";
	    var id=oColumn.getKey()+oRecord.getId();
	    var fieldName = "subCategoryMappingDetails[" + oRecord.getCount() + "]." + oColumn.getKey();
	    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+value+"'/>";
	    el.innerHTML = markup;
	}
	return hiddenFormatter;
}
var rateTypeHiddenFormatter= createRateTypeFormatter(10,10); 

var feeTypeDropdownOptions=[{label:"--- Select ---", value:"0"},
<s:iterator var="s" value="dropdownData.feeTypeList" status="status">  
{"label":"<s:property value="%{name}"/>" ,
	"value":"<s:property value="%{id}" />"
}<s:if test="!#status.last">,</s:if>
</s:iterator>       
]

var rateTypeDropdownOptions=[{label:"--- Select ---", value:"0"},
<s:iterator var="s" value="dropdownData.rateTypeList" status="status">  
{"label":"<s:property value="%{s}"/>" ,
	"value":"<s:property value="%{s}" />"
}<s:if test="!#status.last">,</s:if>
</s:iterator>       
]

var uomDropdownOptions=[{label:"--- Select ---", value:"0"},
<s:iterator var="s" value="dropdownData.uomList" status="status">  
{"label":"<s:property value="%{name}"/>" ,
	"value":"<s:property value="%{id}" />",
}<s:if test="!#status.last">,</s:if>  
</s:iterator>       
]

var subCategoryMappingDataTable;
var makeSubCategoryMappingDataTable= function() {
	var cellEditor=new YAHOO.widget.TextboxCellEditor()
	var subCategoryMappingColumnDefs = [		
		//{key:"financialYear", hidden:true, formatter:financialYearIdHiddenFormatter, sortable:false, resizeable:false},
		{key:"scDtlsId", hidden:true, formatter:scdIdHiddenFormatter, sortable:false, resizeable:false},
		{key:"feeType", hidden:true, formatter:hiddenFormatter, sortable:false, resizeable:false},
		{key:"uom", hidden:true, formatter:hiddenFormatter, sortable:false, resizeable:false},
		{key:"rateType", hidden:true, formatter:rateTypeHiddenFormatter, sortable:false, resizeable:false}, 
		{key:"SlNo", label:'Sl No', sortable:false, resizeable:false},
		{key:"FeeType", label:'Fee Type<span class="mandatory"></span>', formatter:"dropdown", dropdownOptions:feeTypeDropdownOptions},	
		{key:"RateType", label:'Rate Type<span class="mandatory"></span>', formatter:"dropdown", dropdownOptions:rateTypeDropdownOptions},		
		{key:"Uom", label:'UOM<span class="mandatory"></span>', formatter:"dropdown", dropdownOptions:uomDropdownOptions},			
		{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}")}, 
		{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
	];
		
	var subCategoryMappingDataSource = new YAHOO.util.DataSource(); 
	subCategoryMappingDataTable= new YAHOO.widget.DataTable("subCategoryMappingTable",subCategoryMappingColumnDefs, subCategoryMappingDataSource , {initialRequest:"query=orders&results=10"});	
	subCategoryMappingDataTable.subscribe("cellClickEvent", subCategoryMappingDataTable.onEventShowCellEditor); 
	subCategoryMappingDataTable.on('cellClickEvent',function (oArgs) {
		var target = oArgs.target;
		var record = this.getRecord(target);
		var column = this.getColumn(target);
		if (column.key == 'Add') {  
			subCategoryMappingDataTable.addRow({SlNo:subCategoryMappingDataTable.getRecordSet().getLength()+1});
		}

		if (column.key == 'Delete') { 
			// saved records should not be deleted
			<s:if test="%{userMode=='edit'}">
				if(document.getElementById("scDtlsId"+record.getId()).value!="" && document.getElementById("scDtlsId"+record.getId()).value!=null){
					alert("Existing row cannot be deleted.");
					return false;
				}
			</s:if>			
			if(this.getRecordSet().getLength()>1){	
					this.deleteRow(record);
					allRecords=this.getRecordSet();
					for(i=0;i<allRecords.getLength();i++){
						this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
				}
			} 
			else 
			{
				alert("This row can not be deleted");
			}

		} 
		// once fee is defined for the subcategory, should not allow modification of existing record
		<s:if test="%{userMode=='edit'}">
		if(document.getElementById("scDtlsId"+record.getId()).value!="" && document.getElementById("scDtlsId"+record.getId()).value!=null){
			if(document.getElementById("feeExists").value=='true'){
				 if (column.key == 'FeeType' || column.key == 'Uom' || column.key == 'RateType') { 
					alert("Fees defined. Cannot change the value.");
					return false;
				} 
			}
		}
		</s:if>	       
	});	


 	subCategoryMappingDataTable.on('dropdownChangeEvent', function (oArgs) {	
	    var record = this.getRecord(oArgs.target);
        var column = this.getColumn(oArgs.target);
        if(column.key=='FeeType'){
    	    var selectedIndex=oArgs.target.selectedIndex; 
    		this.updateCell(record,this.getColumn('feeType'),feeTypeDropdownOptions[selectedIndex].value);
    		if(this.getRecordSet().getLength()>1){
        		if(document.getElementById("feeType"+record.getId()).value!=0)
    	   		 validateDuplicate(record,oArgs); 
    		}
        } else if(column.key=='Uom'){
    	    var selectedIndex=oArgs.target.selectedIndex; 
        	this.updateCell(record,this.getColumn('uom'),uomDropdownOptions[selectedIndex].value);
        } else if(column.key=='RateType'){
    	    var selectedIndex=oArgs.target.selectedIndex; 
         	this.updateCell(record,this.getColumn('rateType'),rateTypeDropdownOptions[selectedIndex].value);
         }
	}); 

 	if(document.getElementById("userMode").value=='new'){
	 	var defaultVal;
	 	var defaultId;
	 	for(i=0; i < feeTypeDropdownOptions.length; i++) {
	        if (feeTypeDropdownOptions[i].label == document.getElementById("licenseFee").value) {
	        	defaultVal=i;
	        	defaultId=feeTypeDropdownOptions[i].value;
	        }
	    } // default fee type to license fee in create screen
		subCategoryMappingDataTable.addRow({SlNo:subCategoryMappingDataTable.getRecordSet().getLength()+1,FeeType:defaultVal,feeType:defaultId});
 	} else {
 		subCategoryMappingDataTable.addRow({SlNo:subCategoryMappingDataTable.getRecordSet().getLength()+1});
 	}
	
	return { 
	    oDS: subCategoryMappingDataSource,
	    oDT: subCategoryMappingDataTable
	};        
}



function validateDuplicate(record,oArgs){ 
    for(i=0;i<subCategoryMappingDataTable.getRecordSet().getLength();i++){ 
        var selectedIndex=oArgs.target.selectedIndex;
	     if(document.getElementById("feeType"+record.getId())!= document.getElementById("feeType"+subCategoryMappingDataTable.getRecord(i).getId())){
		       if(document.getElementById("feeType"+record.getId()).value==document.getElementById("feeType"+subCategoryMappingDataTable.getRecord(i).getId()).value){
		          document.getElementById("scDtl_error").innerHTML='The Fee Type "'+feeTypeDropdownOptions[selectedIndex].label+'" is already selected'; 
		          document.getElementById("scDtl_error").style.display='';
		          oArgs.target.selectedIndex=0;
		          document.getElementById("feeType"+record.getId()).value=0;
		          return;
		       } 
	     }
    }
    document.getElementById("scDtl_error").style.display='none';
  	document.getElementById("scDtl_error").innerHTML='';	
    return;
}


</script>

<div class="panel-heading custom_form_panel_heading">
    <div class="panel-title">Sub Category Mapping Details</div>
</div>
<div class="error-msg" id="scDtl_error" style="display:none;"></div>
<table id="mappingTable" width="100%" border="0" cellspacing="0" cellpadding="0">
   	<tr>
     	<td colspan="5">
     	<div class="yui-skin-sam">
         	<div id="subCategoryMappingTable"></div>
     	</div>
     	</td>
     </tr>
      <tr>
     	<td colspan="5" class="shadowwk"></td>
     </tr>
</table> 
<script>
		makeSubCategoryMappingDataTable();
		<s:iterator id="scDtlsIterator" value="licenseSubCategoryDetails" status="row_status">
        <s:if test="#row_status.count == 1">
        subCategoryMappingDataTable.updateRow(0,
                                 {	scDtlsId:'<s:property value="id"/>',
            						feeType:'<s:property value="feeType.id"/>',
        							uom:'<s:property value="uom.id"/>',
        							rateType:'<s:property value="rateType"/>',
	                                SlNo:'<s:property value="#row_status.count"/>',
	                                FeeType:'<s:property value="feeType.id"/>',   
	                                RateType:'<s:property value="rateType"/>',
	                                Uom:'<s:property value="uom.id"/>',
	                                Add:createAddImageFormatter("${pageContext.request.contextPath}"),
	                                Delete:createDeleteImageFormatter("${pageContext.request.contextPath}")});
        </s:if>
        <s:else>
        subCategoryMappingDataTable.addRow(
                                 {	scDtlsId:'<s:property value="id"/>',
                                    feeType:'<s:property value="feeType.id"/>',
        							uom:'<s:property value="uom.id"/>',
        							rateType:'<s:property value="rateType"/>',
				                    SlNo:'<s:property value="#row_status.count"/>',
				                    FeeType:'<s:property value="feeType.id"/>',   
	                                RateType:'<s:property value="rateType"/>',
	                                Uom:'<s:property value="uom.id"/>',
				                    Add:createAddImageFormatter("${pageContext.request.contextPath}"),
				                    Delete:createDeleteImageFormatter("${pageContext.request.contextPath}")});
        </s:else>
        
      var record = subCategoryMappingDataTable.getRecord(parseInt('<s:property value="#row_status.index"/>'));
      </s:iterator>

</script>