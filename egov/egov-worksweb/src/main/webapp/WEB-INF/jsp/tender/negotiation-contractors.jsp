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

<%@ include file="/includes/taglibs.jsp" %>
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
<script src="<egov:url path='resources/js/works.js?${app_release_no}'/>"></script>
<script>

function createContractorIDFormatter(el, oRecord, oColumn){
var hiddenFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName = "actionTenderResponseContractors[" + oRecord.getCount() + "]." + oColumn.getKey() + ".id";
    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+value+"'/><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
	}	
	return hiddenFormatter;
}
var contractorIDFormatter = createContractorIDFormatter(10,10);

function createContractorNameTextboxFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName="actionTenderResponseContractors[" + oRecord.getCount() + "]." + contractorsDataTable.getColumn('contractor').getKey() + ".name";
    markup="<input type='text' id='"+id+"' name='"+fieldName+"' value='"+value+"' disabled='true' size='"+size+"' maxlength='"+maxlength+"' class='selectwk' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
	}
	return textboxFormatter;
}
var contractorNameTextboxFormatter = createContractorNameTextboxFormatter(45,100);

function createContractorCodeTextboxFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName="actionTenderResponseContractors[" + oRecord.getCount() + "]." + contractorsDataTable.getColumn('contractor').getKey() + ".code";
    markup="<input type='text' id='"+id+"' name='"+fieldName+"' value='"+value+"' disabled='true' size='"+size+"' maxlength='"+maxlength+"' class='selectwk' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
	}
	return textboxFormatter;
}
var contractorCodeTextboxFormatter = createContractorCodeTextboxFormatter(14,22);

var contractorsDataTable;
var makeContractorsDataTable = function() {
	var contractorColumns = [ 
		{key:"contractor", hidden:true, formatter:contractorIDFormatter, sortable:false, resizeable:false} ,
		{key:"SlNo", label:'<s:text name="column.title.SLNo"/>', sortable:false, resizeable:false},
		{key:"code",label:'<s:text name="column.title.code"/>', formatter:contractorCodeTextboxFormatter,sortable:false, resizeable:false},
		{key:"name",label:'<s:text name="column.title.Name"/>', formatter:contractorNameTextboxFormatter,sortable:false, resizeable:false},	
		{key:'Search',label:'<s:text name="column.title.asset.search"/>',formatter:createSearchImageFormatter("${pageContext.request.contextPath}")},	
		{key:'Add',label:'<s:text name="column.title.add"/>',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
		{key:'Delete',label:'<s:text name="column.title.delete"/>',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}  
	];
	var contractorsDS = new YAHOO.util.DataSource(); 
	contractorsDataTable = new YAHOO.widget.DataTable("contractorsTable",contractorColumns, contractorsDS);	
			
	contractorsDataTable.on('cellClickEvent',function (oArgs) {
		var target = oArgs.target;
		var record = this.getRecord(target);
		var column = this.getColumn(target);
		if (column.key == 'Add') { 
			contractorsDataTable.addRow({SlNo:contractorsDataTable.getRecordSet().getLength()+1});
		}

		if (column.key == 'Delete') { 			
			if(this.getRecordSet().getLength()>1){	
				if(document.tenderNegotiationForm.tenderType.value==dom.get("tenderType")[2].value && dom.get('code'+record.getId()).value!=''){
					var reset=false;
					if(!resetItemRates(reset))
						return;
					else {
						deleteContractorDetails(dom.get('contractor'+record.getId()).value);
						this.deleteRow(record);
						allRecords=this.getRecordSet();
						for(i=0;i<allRecords.getLength();i++){
							this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
						}		
					}						
				}
				else {									
					this.deleteRow(record);
					allRecords=this.getRecordSet();
					for(i=0;i<allRecords.getLength();i++){
						this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
					}
					
				}			
			}
			else
			{
				if(document.tenderNegotiationForm.tenderType.value==dom.get("tenderType")[2].value && dom.get('code'+record.getId()).value!=''){
					var reset=false;
					if(!resetItemRates(reset))
						return;
					else {
						deleteContractorDetails(dom.get('contractor'+record.getId()).value);
						this.deleteRow(record);
						contractorsDataTable.addRow({SlNo:contractorsDataTable.getRecordSet().getLength()+1});
					}
				}
				else {	
					this.deleteRow(record);
					contractorsDataTable.addRow({SlNo:contractorsDataTable.getRecordSet().getLength()+1});
				}
			}
			setL1BidderName();			
		}
		
		var records = contractorsDataTable.getRecordSet();
		if (column.key == 'Search') {
			rowId=records.getRecordIndex(record);
			searchContractor(rowId);
		}
	});
	contractorsDataTable.addRow({SlNo:contractorsDataTable.getRecordSet().getLength()+1});
}



function createSearchImageFormatter(baseURL){
	var searchImageFormatter = function(el, oRecord, oColumn, oData) {
	    var imageURL="/resources/erp2/images/magnifier.png";
	    markup='<a href="#"><img src="'+imageURL+'" height=16  width=16 border="0" alt="Search" align="absmiddle"></a>';
	    el.innerHTML = markup;
	}
	return searchImageFormatter;
}
function resetItemRates(reset){
	var ans=confirm('<s:text name="negotiation.contractorDelete.warning"/>');
	if(ans){
		if(reset){
			itemRateDataTable.render();
		}
		return true;
	}
	else {
		  return false;		
	}
}
function deleteContractorDetails(contractorId){
	var contractorListObj=document.getElementById('contractorList');
	for (var i = contractorListObj.length - 1; i>=0; i--) {
    	if (contractorListObj.options[i].value==contractorId) {
    		contractorListObj.remove(i);
    	}
  	}
  	for(var i=0;i<itemRateDataTable.getRecordSet().getLength();i++){
	 	var tobedeleteddivTag = document.getElementById("contractorTable"+contractorId+"["+ itemRateDataTable.getRecord(i).getCount() + "]");
	 	var tobedeletedtotaldiv=document.getElementById('quotedTotal_'+contractorId);
	 	
	 	if(tobedeleteddivTag){
			document.getElementById("contractorT["+ itemRateDataTable.getRecord(i).getCount() + "]").removeChild(tobedeleteddivTag);
	 	}
	 	if(tobedeletedtotaldiv){
	 		document.getElementById("quotedTotal").removeChild(tobedeletedtotaldiv);
	 	}
	}
					
	changeContractor(contractorListObj);
	calculateQuotedTotalForContractors();
						
}

</script>

<div class="errorstyle" id="contractor_error" style="display:none;"></div>
<table id="contractorsHeaderTable" width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="7" class="headingwk"><div class="arrowiconwk"><image src="<egov:url path='/resources/erp2/images/arrow.gif'/>" /></div>
			<div class="headplacer"><s:text name="negotiation.contractor.details"/></div>
		</td>
	</tr>
	<tr>
		<td colspan="7">
		<div class="yui-skin-sam">
        	<div id="contractorsTable"></div>
        </div>
		</td>
	</tr>
	<tr>
		<td colspan="7" class="shadowwk"></td>
	</tr>
</table>
			<script>
       makeContractorsDataTable();
                <s:if test="%{!hasErrors()}">
                <s:iterator id="contractorsIterator" value="tenderResponseContractors" status="row_status">
		          <s:if test="#row_status.count == 1">
		              contractorsDataTable.updateRow(0, 
		                                   {contractor:'<s:property value="contractor.id"/>',
		                                    SlNo:'<s:property value="#row_status.count"/>',
		                                    code:'<s:property value="contractor.code"/>',
		                                    name:'<s:property value="contractor.name"/>',
		                                    Search:'X',
		                                    Add:'X',
		                                    Delete:'X'});
		          </s:if>
		          <s:else>
                      contractorsDataTable.addRow({contractor:'<s:property value="contractor.id"/>',
		                                    SlNo:'<s:property value="#row_status.count"/>',
		                                    code:'<s:property value="contractor.code"/>',
		                                    name:'<s:property value="contractor.name"/>',
		                                    Search:'X',
		                                    Add:'X',
		                                    Delete:'X'});
		          </s:else>
		           var record = contractorsDataTable.getRecord(parseInt('<s:property value="#row_status.index"/>'));    
				    var column = contractorsDataTable.getColumn('code');  
				    dom.get(column.getKey()+record.getId()).readonly = true;
				    dom.get(column.getKey()+record.getId()).disabled = true;
					column = contractorsDataTable.getColumn('name');  
				    dom.get(column.getKey()+record.getId()).readonly = true;
				    dom.get(column.getKey()+record.getId()).disabled = true; 
		         </s:iterator>
		         </s:if>
		         <s:elseif test="%{hasErrors()}">
		         <s:iterator id="contractorsIterator1" value="actionTenderResponseContractorsList" status="row_status">
		          <s:if test="#row_status.count == 1">
		              contractorsDataTable.updateRow(0, 
		                                   {contractor:'<s:property value="contractor.id"/>',
		                                    SlNo:'<s:property value="#row_status.count"/>',
		                                    code:'<s:property value="contractor.code"/>',
		                                    name:'<s:property value="contractor.name"/>',
		                                    Search:'X',
		                                    Add:'X',
		                                    Delete:'X'});
		          </s:if>
		          <s:else>
                      contractorsDataTable.addRow({contractor:'<s:property value="contractor.id"/>',
		                                    SlNo:'<s:property value="#row_status.count"/>',
		                                    code:'<s:property value="contractor.code"/>',
		                                    name:'<s:property value="contractor.name"/>',
		                                    Search:'X',
		                                    Add:'X',
		                                    Delete:'X'});
		          </s:else>
		           var record = contractorsDataTable.getRecord(parseInt('<s:property value="#row_status.index"/>'));    
				    var column = contractorsDataTable.getColumn('code');  
				    dom.get(column.getKey()+record.getId()).readonly = true;
				    dom.get(column.getKey()+record.getId()).disabled = true;
					column = contractorsDataTable.getColumn('name');  
				    dom.get(column.getKey()+record.getId()).readonly = true;
				    dom.get(column.getKey()+record.getId()).disabled = true; 
		         </s:iterator>
		         </s:elseif>
                </script>
		
