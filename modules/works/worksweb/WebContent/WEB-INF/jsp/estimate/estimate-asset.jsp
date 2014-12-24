<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 
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
<script src="<egov:url path='js/works.js'/>"></script>
<script>

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
    markup="<input type='text' id='"+id+"' name='"+fieldName+"' value='"+value+"' disabled='true' size='"+size+"' maxlength='"+maxlength+"' class='selectwk' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
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
    markup="<input type='text' id='"+id+"' name='"+fieldName+"' value='"+value+"' size='"+size+"' maxlength='"+maxlength+"' class='selectwk' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
	}
	return textboxFormatter;
}
var assetCodeTextboxFormatter = createAssetCodeTextboxFormatter(14,22);

var assetsTable;
var makeAssetsTable = function() {
	var assetColumns = [ 
		{key:"asset", hidden:true, formatter:assetIDFormatter, sortable:false, resizeable:false} ,
		{key:"SlNo", label:'<s:text name="column.title.SLNo"/>', sortable:false, resizeable:false},
		{key:"name",label:'<s:text name="column.title.Name"/>', formatter:assetDescTextboxFormatter,sortable:false, resizeable:false},
		{key:"code",label:'<s:text name="column.title.code"/>', formatter:assetCodeTextboxFormatter,sortable:false, resizeable:false},
		//{key:'New',label:'<s:text name="column.title.asset.create"/>',formatter:createNewImageFormatter("${pageContext.request.contextPath}")},
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
		<s:iterator id="typeListiterator" value="dropdownData.typeList" status="row_status">
		if(document.getElementById('type').options[document.getElementById('type').selectedIndex].text=='<s:property value="name"/>')
		{
			status = getStatusForCode('<s:property value="code"/>');
		}
		</s:iterator>
		if(status==''){
			alert('<s:text name="estimate.assets.table.message"/>');
			return false;
		}
		var records = assetsTable.getRecordSet();
		if (column.key == 'Search' && status!='') {
			window.open("${pageContext.request.contextPath}/../egassets/assetmaster/asset!showSearchPage.action?rowId="+records.getRecordIndex(record)+"&assetStatus="+status,"",
	 			"height=600,width=1200,scrollbars=yes,left=0,top=0,status=yes");
		}
		/*if (column.key == 'New' && status!='') {
			window.open("${pageContext.request.contextPath}/../egassets/assetmaster/asset!showCreatePage.action?rowId="+records.getRecordIndex(record)+"&assetStatus="+status,"",
	 				"height=600,width=600,scrollbars=yes,left=200,top=75,status=yes");	
		}*/        
	});
	assetsTable.addRow({SlNo:assetsTable.getRecordSet().getLength()+1});
}
function getStatusForCode(code){
	if(code=='Capital Works' || code=='Deposit Works Own Asset')
		return 'Created';
	else if(code=='Improvement Works' || code=='Repairs and maintenance')
		return 'Capitalized&assetStatus=Revaluated';
	else
		return '';
}
function getStatusListForNatureOfWork(code){
	if(code=='Capital Works' || code=='Deposit Works Own Asset')
		return 'Created';
	else if(code=='Improvement Works' || code=='Repairs and maintenance')
		return 'Capitalized,Revaluated';
	else
		return '-1';
}

function setAssetStatusHiddenField(){
		var status='';
		<s:iterator id="typeListiterator" value="dropdownData.typeList" status="row_status">
		if(document.getElementById('type').options[document.getElementById('type').selectedIndex].text=='<s:property value="name"/>')
		{
			status = getStatusListForNatureOfWork('<s:property value="code"/>');
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
	    // var imageURL=baseURL+"/image/magnifier.png";
	    markup='<a href="#">Create Asset</a>';
	    el.innerHTML = markup;
	}
	return newImageFormatter;
}

function createDeleteImageFormatter(baseURL){
	var deleteImageFormatter = function(el, oRecord, oColumn, oData) {
	    var imageURL=baseURL+"/image/cancel.png";
	    markup='<a href="#"><img src="'+imageURL+'" height=16  width=16 border="0" alt="Delete" align="absmiddle"></a>';
	    el.innerHTML = markup;
	}
	return deleteImageFormatter;
}

function createAddImageFormatter(baseURL){
	var addImageFormatter = function(el, oRecord, oColumn, oData) {
	    var imageURL=baseURL+"/image/add.png";
	    markup='<a href="#"><img src="'+imageURL+'" height=16  width=16 border="0" alt="Add" align="absmiddle"></a>';
	    el.innerHTML = markup;
	}
	return addImageFormatter;
}

function createSearchImageFormatter(baseURL){
	var searchImageFormatter = function(el, oRecord, oColumn, oData) {
	    var imageURL=baseURL+"/image/magnifier.png";
	    markup='<a href="#"><img src="'+imageURL+'" height=16  width=16 border="0" alt="Search" align="absmiddle"></a>';
	    el.innerHTML = markup;
	}
	return searchImageFormatter;
}



</script>
<div class="errorstyle" id="asset_error" style="display:none;"></div>
<table id="assetsHeaderTable" width="100%" border="0" cellspacing="0"
	cellpadding="0">
	<tr>
		<td colspan="7" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
		<div class="headplacer"><s:text name="estimate.title.assets"/></div></td>
	</tr>
	<s:hidden name="assetStatus" id="assetstatus"/>
	<tr>
		<td colspan="7">
			<div class="yui-skin-sam">
				<div id="assetTable"></div>
				<div id="altMassage" style="display:none;">
					<s:text name="estimate.assets.table.message"/>
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
		                                    //New:createNewImageFormatter("${pageContext.request.contextPath}"),
		                                    Search:createSearchImageFormatter("${pageContext.request.contextPath}"),
		                                    Add:createAddImageFormatter("${pageContext.request.contextPath}"),
		                                    Delete:'X'});
		          </s:if>
		          <s:else>
                      assetsTable.addRow({asset:'<s:property value="asset.id"/>',
		                                    SlNo:'<s:property value="#row_status.count"/>',
		                                    name:'<s:property value="asset.name"/>',
		                                    code:'<s:property value="asset.code"/>',
		                                    //New:createNewImageFormatter("${pageContext.request.contextPath}"),
		                                    Search:createSearchImageFormatter("${pageContext.request.contextPath}"),
		                                    Add:createAddImageFormatter("${pageContext.request.contextPath}"),
		                                    Delete:'X'});
		          </s:else>
		          
		         </s:iterator>
                </script>
		</td>
	</tr>
	<tr>
		<td colspan="7" class="shadowwk"></td>
	</tr>

</table>