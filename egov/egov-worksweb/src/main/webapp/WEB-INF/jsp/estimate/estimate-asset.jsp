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
.yui-dt-col-Add{
  width:5%;
}
.yui-dt-col-Delete{
  width:5%;
}

</style>
<script>
jQuery.noConflict();
function createAssetIDFormatter(el, oRecord, oColumn){
var hiddenFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName = "actionAssetValues[" + oRecord.getCount() + "]." + oColumn.getKey() + ".id";
    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+value+"'/><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
	}	
	return hiddenFormatter;
}
var assetIDFormatter = createAssetIDFormatter(10,10);

function createAssetDescTextboxFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName="actionAssetValues[" + oRecord.getCount() + "]." + assetsTable.getColumn('asset').getKey() + ".name";
    markup="<input type='text' id='"+id+"' name='"+fieldName+"' value='"+value+"' disabled='true' size='"+size+"' maxlength='"+maxlength+"' class='selectwk' style='border:0px;background:none;'/><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
	}
	return textboxFormatter;
}
var assetDescTextboxFormatter = createAssetDescTextboxFormatter(45,100);

function createAssetCodeTextboxFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName="actionAssetValues[" + oRecord.getCount() + "]." + assetsTable.getColumn('asset').getKey() + ".code";
    markup="<input type='text' id='"+id+"' name='"+fieldName+"' value='"+value+"' size='"+size+"' disabled='false' readonly='readonly' maxlength='"+maxlength+"'  class='selectwk assetCode' style='color:#0099cc;text-decoration: underline;position: relative; background:none; cursor: pointer;border:0px;'/><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
	}
	return textboxFormatter;
}
var assetCodeTextboxFormatter = createAssetCodeTextboxFormatter(30,30);

var assetsTable;
var makeAssetsTable = function() {
	var assetColumns = [ 
		{key:"asset", hidden:true, formatter:assetIDFormatter, sortable:false, resizeable:false} ,
		{key:"SlNo", label:'<s:text name="column.title.SLNo"/>', sortable:false, resizeable:false},
		{key:"name",label:'<s:text name="column.title.Name"/>', formatter:assetDescTextboxFormatter,sortable:false, resizeable:false},
		{key:"code",label:'<s:text name="column.title.code"/>', formatter:assetCodeTextboxFormatter,sortable:false, resizeable:false},
		{key:'Search',label:'<s:text name="column.title.asset.search"/>',formatter:createSearchImageFormatter("${pageContext.request.contextPath}")},
		{key:'Add',label:'<s:text name="column.title.add"/>',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
		{key:'Delete',label:'<s:text name="column.title.delete"/>',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}  
	];
	var assetsDS = new YAHOO.util.DataSource(); 
	assetsTable = new YAHOO.widget.DataTable("assetTable",assetColumns, assetsDS);	
			
	assetsTable.on('cellClickEvent',function (oArgs) {
		var target = oArgs.target;
		var record = this.getRecord(target);
		var column = this.getColumn(target);
		if (column.key == 'Add') { 
			assetsTable.addRow({SlNo:assetsTable.getRecordSet().getLength()+1});
		}

		if (column.key == 'Delete') { 			
			if(this.getRecordSet().getLength()>1){			
				this.deleteRow(record);
				allRecords=this.getRecordSet();
				for(i=0;i<allRecords.getLength();i++){
					this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
				}
			}
			else
			{
				this.deleteRow(record);
				assetsTable.addRow({SlNo:assetsTable.getRecordSet().getLength()+1});
			}
		}
		var status='';
		<s:iterator id="natureOfWorkListiterator" value="dropdownData.natureOfWorkList" status="row_status">
		if(document.getElementById('natureOfWork').options[document.getElementById('natureOfWork').selectedIndex].text=='<s:property value="name"/>') {
			status = getStatusForNatureOfWork('<s:property value="name"/>');
		}
		</s:iterator>
		if(status==''){
			alert('<s:text name="estimate.assets.table.message"/>');
			return false;
		}
		var records = assetsTable.getRecordSet();
		if (column.key == 'Search' && status!='') {
			window.open("${pageContext.request.contextPath}/../egassets/assetmaster/asset-showSearchPage.action?rowId="+records.getRecordIndex(record)+"&assetStatus="+status,"",
	 			"height=600,width=1200,scrollbars=yes,left=0,top=0,status=yes");
		}    
	});
	assetsTable.addRow({SlNo:assetsTable.getRecordSet().getLength()+1});
}
function getStatusForNatureOfWork(name){
	if(name=='Deposit Works' || name=='Deposit Works')
		return '';
	else
		return 'Created&assetStatus=CWIP&assetStatus=Capitalized&assetStatus=Revaluated';
}

function getStatusListForNatureOfWork(name){
	if(name=='Deposit Works' || name=='Deposit Works')
		return '-1';
	else
		return 'Created,CWIP,Capitalized,Revaluated';
}

function setAssetStatusHiddenField(){
		var status='';
		<s:iterator id="natureOfWorkListiterator" value="dropdownData.natureOfWorkList" status="row_status">
		if(document.getElementById('natureOfWork').options[document.getElementById('natureOfWork').selectedIndex].text=='<s:property value="name"/>')
		{
			status = getStatusListForNatureOfWork('<s:property value="name"/>');
		}
		</s:iterator>
		document.getElementById('assetstatus').value = status;
}

function resetAssets(){
		assetsTable.deleteRows(0,assetsTable.getRecordSet().getLength());
		setAssetStatusHiddenField();
  		if(document.getElementById('assetstatus').value!=-1){
  			document.getElementById('assetTable').style.display='block';
  			document.getElementById('altMassage').style.display='none';
  			assetsTable.addRow({SlNo:assetsTable.getRecordSet().getLength()+1});
  		}
  		else{
  			document.getElementById('assetTable').style.display='none';
  			document.getElementById('altMassage').style.display='block';
  		}	  
}

function setAssetTableMessage(){
		setAssetStatusHiddenField();
  		if(document.getElementById('assetstatus').value!=-1){
  			document.getElementById('assetTable').style.display='block';
  			document.getElementById('altMassage').style.display='none';
  		}
  		else{
  			document.getElementById('assetTable').style.display='none';
  			document.getElementById('altMassage').style.display='block';
  		}	  
}
	
function createNewImageFormatter(baseURL){
	var newImageFormatter = function(el, oRecord, oColumn, oData) {
	    markup='<a href="javascript:void(0);">Create Asset</a>';
	    el.innerHTML = markup;
	}
	return newImageFormatter;
}

function createDeleteImageFormatter(baseURL){
	var deleteImageFormatter = function(el, oRecord, oColumn, oData) {
	    var imageURL=baseURL+"/resources/erp2/images/cancel.png";
	    markup='<a href="javascript:void(0);"><img src="'+imageURL+'" height=16  width=16 border="0" alt="Delete" align="absmiddle"></a>';
	    el.innerHTML = markup;
	}
	return deleteImageFormatter;
}

function createAddImageFormatter(baseURL){
	var addImageFormatter = function(el, oRecord, oColumn, oData) {
	    var imageURL=baseURL+"/resources/erp2/images/add.png";
	    markup='<a href="javascript:void(0);"><img src="'+imageURL+'" height=16  width=16 border="0" alt="Add" align="absmiddle"></a>';
	    el.innerHTML = markup;
	}
	return addImageFormatter;
}

function createSearchImageFormatter(baseURL){
	var searchImageFormatter = function(el, oRecord, oColumn, oData) {
	    var imageURL=baseURL+"/resources/erp2/images/magnifier.png";
	    markup='<a href="javascript:void(0);"><img src="'+imageURL+'" height=16  width=16 border="0" alt="Search" align="absmiddle"></a>';
	    el.innerHTML = markup;
	}
	return searchImageFormatter;
}

function update(elemValue) {	
	if(elemValue!="" || elemValue!=null) {
		var a = elemValue.split("`~`");
		var records= assetsTable.getRecordSet();
		var row_id=a[0];
		var asset_id=a[1];
		var asset_code=a[2];
		var asset_name=a[3];
		dom.get("code"+records.getRecord(getNumber(row_id)).getId()).value=asset_code;
		dom.get("name"+records.getRecord(getNumber(row_id)).getId()).value=asset_name;
		dom.get("asset"+records.getRecord(getNumber(row_id)).getId()).value=asset_id;
		dom.get("code"+records.getRecord(getNumber(row_id)).getId()).disabled=false;		
		dom.get("name"+records.getRecord(getNumber(row_id)).getId()).disabled=true;		
	}
}

jQuery(document).on('click', '.assetCode', function(){
	var indexNum = jQuery(this).attr("name").substring(jQuery(this).attr("name").indexOf('[')+1,jQuery(this).attr("name").indexOf(']'));
	var assetId = jQuery('#assetyui-rec'+indexNum).val();
	var url = "${pageContext.request.contextPath}/../egassets/assetmaster/asset-showform.action?id="+assetId+"&userMode=view";
	window.open(url,'', 'height=650,width=980,scrollbars=yes,status=yes'); 
});

</script>

<s:hidden name="assetStatus" id="assetstatus"/>
<div id="assetsHeaderTable" class="panel panel-primary" data-collapsed="0" style="text-align:left">
	<div class="panel-heading">
		<div class="panel-title">
		   <s:text name="estimate.title.assets"/>
		</div>
	</div>
	<div class="panel-body">		
		<div class="alert alert-danger" id="asset_error" style="display:none;"></div>
	    <div class="form-group no-margin-bottom">
	    	<div class="yui-skin-sam">
				<div id="assetTable"></div>
				<div id="altMassage" class="mtb-5" style="display:none;">
					<s:text name="estimate.assets.table.message"/>
				</div>
			</div>
	    </div>
	</div>
</div>

			<script>
                makeAssetsTable();
                
                <s:iterator id="assetsiterator" value="assetValues" status="row_status">
		          <s:if test="#row_status.count == 1">
		              assetsTable.updateRow(0, 
		                                   {asset:'<s:property value="asset.id"/>',
		                                    SlNo:'<s:property value="#row_status.count"/>',
		                                    name:'<s:property value="asset.name"/>',
		                                    code:'<s:property value="asset.code"/>',
		                                    Search:createSearchImageFormatter("${pageContext.request.contextPath}"),
		                                    Add:createAddImageFormatter("${pageContext.request.contextPath}"),
		                                    Delete:'X'});
		          </s:if>
		          <s:else>
                      assetsTable.addRow({asset:'<s:property value="asset.id"/>',
		                                    SlNo:'<s:property value="#row_status.count"/>',
		                                    name:'<s:property value="asset.name"/>',
		                                    code:'<s:property value="asset.code"/>',
		                                    Search:createSearchImageFormatter("${pageContext.request.contextPath}"),
		                                    Add:createAddImageFormatter("${pageContext.request.contextPath}"),
		                                    Delete:'X'});
		          </s:else>
		          
		         </s:iterator>
                </script>