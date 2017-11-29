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

<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
<script>
   var chequeDetailsGridTable;
   var chequeRangeArray = new Array();
   var deletedChqDeptId="";
   var CHQDETAILSLIST = "chequeDetailsList";
   var chqDetailsIndex = 0;
	var makeChequeDetailsGridTable = function() {    
    var chequeDetailsGridColumns = [ 
	{key:"fromChqNo",label:'From Cheque Number', formatter:createTextField(CHQDETAILSLIST,".fromChqNo")},
	{key:"isExhaustedH",hidden:true,formatter:createHiddenField(CHQDETAILSLIST,".isExhusted","hidden")},
	{key:"toChqNo",label:'To Cheque Number', formatter:createTextField(CHQDETAILSLIST,".toChqNo")},
	{key:"chequeDeptId",hidden:true,formatter:createHiddenField(CHQDETAILSLIST,".chequeDeptId","hidden")},
	{key:"deptName",label:'Department', formatter:createLabelSamll(CHQDETAILSLIST,".deptName")},
	{key:"deptId",hidden:true,formatter:createHiddenField(CHQDETAILSLIST,".deptId","hidden")},
	{key:"receivedDateL",label:'Received Date', formatter:createLabelSamll(CHQDETAILSLIST,".receivedDateL")},
	{key:"receivedDateH",hidden:true,formatter:createHiddenField(CHQDETAILSLIST,".receivedDate","hidden")},
	{key:"serialNoL",label:'Financial Year', formatter:createLabelSamll(CHQDETAILSLIST,".serialNoL")},
	{key:"serialNoH",hidden:true,formatter:createHiddenField(CHQDETAILSLIST,".serialNo","hidden")},
   	{key:"isExhaustedL",label:'Exhausted', formatter:createLabelSamll(CHQDETAILSLIST,".isExhustedL")},
	{key:"nextChqPresent",hidden:true,formatter:createHiddenField(CHQDETAILSLIST,".nextChqPresent","hidden")},
	{key:"accountChequeId",hidden:true,formatter:createHiddenField(CHQDETAILSLIST,".accountChequeId","hidden")},
        {key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
	];
		
		var chqueDetailsGridDS = new YAHOO.util.DataSource(); 
		chequeDetailsGridTable = new YAHOO.widget.DataTable("chequeDetailsGridTable",chequeDetailsGridColumns, chqueDetailsGridDS);
		chequeDetailsGridTable.on('cellClickEvent',function (oArgs) {
			var target = oArgs.target;
			var record = this.getRecord(target);
			var column = this.getColumn(target);
			if (column.key == 'Delete') { 	
						
					if(record.getData("isExhustedL") =="Yes" || record.getData("nextChqPresent")=="Yes"){
						
						bootbox.alert("cannot be deleted");
					}else{
						this.deleteRow(record);
						var index = chequeRangeArray.indexOf(record.getData("fromChqNo")+"-"+record.getData("toChqNo")+"-"+record.getData("deptId")+"-"+record.getData("serialNo"));
						chequeRangeArray.splice(index,1);
						if(record.getData("chequeDeptId")){ // for new cheque leaf records the chequeDeptId value will be blank
							document.getElementById("deletedChqDeptId").value = document.getElementById("deletedChqDeptId").value==""?record.getData("chequeDeptId"):document.getElementById("deletedChqDeptId").value+","+record.getData("chequeDeptId");
							
						}
						
					}
			}
			
           });
	
	<s:iterator value="chequeDetailsList" status="stat">
		chequeDetailsGridTable.addRow({
			"fromChqNo":'<s:property value="fromChqNo"/>',
			"toChqNo":'<s:property value="toChqNo"/>',
			"deptName":'<s:property value="deptName"/>',
			"deptId":'<s:property value="deptId"/>',
			"receivedDateL":'<s:property value="receivedDate"/>',
			"receivedDateH":'<s:property value="receivedDate"/>',
			"serialNoL":'<s:property value="serialNoH"/>',
			"serialNoH":'<s:property value="serialNo"/>',
			"isExhustedL":'<s:property value="isExhusted"/>',
			"isExhustedH":'<s:property value="isExhusted"/>',
			"nextChqPresent":'<s:property value="nextChqPresent"/>',
			"accountChequeId":'<s:property value="accountChequeId"/>',
			"chequeDeptId":'<s:property value="chequeDeptId"/>'
		});
		updateFieldChq('fromChqNo',chqDetailsIndex,'<s:property value="fromChqNo"/>','<s:property value="nextChqPresent"/>','<s:property value="isExhusted"/>');
		updateFieldChq('toChqNo',chqDetailsIndex,'<s:property value="toChqNo"/>','<s:property value="nextChqPresent"/>','<s:property value="isExhusted"/>');
		updateLabel('deptName',chqDetailsIndex,'<s:property value="deptName"/>');
		updateField('deptId',chqDetailsIndex,'<s:property value="deptId"/>');
		updateLabel('receivedDateL',chqDetailsIndex,'<s:property value="receivedDate"/>');
		updateField('receivedDate',chqDetailsIndex,'<s:property value="receivedDate"/>');
		updateLabel('serialNoL',chqDetailsIndex,'<s:property value="serialNoH"/>');
		updateField('serialNo',chqDetailsIndex,'<s:property value="serialNo"/>');
		updateLabel('isExhustedL',chqDetailsIndex,'<s:property value="isExhusted"/>');
		updateField('isExhusted',chqDetailsIndex,'<s:property value="isExhusted"/>');
		updateField('accountChequeId',chqDetailsIndex,'<s:property value="accountChequeId"/>');
		updateField('chequeDeptId',chqDetailsIndex,'<s:property value="chequeDeptId"/>');
		updateField('nextChqPresent',chqDetailsIndex,'<s:property value="nextChqPresent"/>');
		chqDetailsIndex = chqDetailsIndex + 1;
		var chequeRange = '<s:property value="fromChqNo"/>'+"-"+'<s:property value="toChqNo"/>'+"-"+'<s:property value="deptId"/>'+"-"+'<s:property value="serialNo"/>';
		if(chequeRangeArray.indexOf(chequeRange) == -1){
			chequeRangeArray.push(chequeRange);
		}
		
        </s:iterator>
}

function createLabelSamll(prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<label id='"+prefix+"["+chqDetailsIndex+"]"+suffix+"'  size='1' style='font-size:0.9em;'/>";
	}
}
function createTextField(prefix,suffix){
	
	 return function(el, oRecord, oColumn, oData) {
			var value = (YAHOO.lang.isValue(oData))?oData:"";
			el.innerHTML = "<input type='text'  id='"+prefix+"["+chqDetailsIndex+"]"+suffix+"' name='"+prefix+"["+chqDetailsIndex+"]"+suffix+"' onblur='validateCheque(this);' style='width:130px;' maxlength='18' onkeyup='validateOnlyNumber(this);'/>";
		}
}
function createHiddenField(prefix,suffix,type){
	 return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='"+type+"' name='"+prefix+"["+chqDetailsIndex+"]"+suffix+"' id='"+prefix+"["+chqDetailsIndex+"]"+suffix+"'/>";
	}
	
}
function updateFieldChq(field,index,value,nxtChqPrsnt,isExhausted){
	
	document.getElementById(CHQDETAILSLIST+'['+index+'].'+field).value =value;
	if(nxtChqPrsnt=="Yes" || isExhausted =="Yes" ){
		document.getElementById(CHQDETAILSLIST+'['+index+'].'+field).readOnly=true;
	}
}
function updateField(field,index,value){
	
	document.getElementById(CHQDETAILSLIST+'['+index+'].'+field).value =value;
}
function updateLabel(field,index,value){
	document.getElementById(CHQDETAILSLIST+'['+index+'].'+field).innerHTML =value;
}

</script>
<style type="text/css">
#codescontainer {
	position: absolute;
	left: 11em;
	width: 9%;
	text-align: left;
}

#codescontainer .yui-ac-content {
	position: absolute;
	width: 600px;
	border: 1px solid #404040;
	background: #fff;
	overflow: hidden;
	z-index: 9050;
}

#codescontainer .yui-ac-shadow {
	position: absolute;
	margin: .3em;
	width: 300px;
	background: #a0a0a0;
	z-index: 9049;
}

#codescontainer ul {
	padding: 5px 0;
	width: 100%;
}

#codescontainer li {
	padding: 0 5px;
	cursor: default;
	white-space: nowrap;
}

#codescontainer li.yui-ac-highlight {
	background: #ff0;
}

#codescontainer li.yui-ac-prehighlight {
	background: #FFFFCC;
}

.yui-skin-sam tr.yui-dt-odd {
	background-color: #f7f7f7;
}

.yui-skin-sam td.yui-dt-col-isExhusted .yui-dt-liner {
	text-align: center;
}

.subheadcustom {
	font-family: Arial, Helvetica, sans-serif;
	font-weight: bold;
	color: #000000;
	background-color: #CCCCCC;
	text-align: center;
	padding-right: 8px;
	padding-left: 8px;
	border: 1px solid #CCCCCC;
	padding-top: -2px;
	padding-bottom: -2px;
}
</style>
