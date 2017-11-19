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
var FUNDSOUCELIST = "fundSourceList";
var fundSourceGridIndex = 0;
var fundSourceGridTable;
function updateGridData(){
	var validationSuccess = validateHeaderBeforeAddingToGrid();	
	if(validationSuccess ){
		if(document.getElementsByName('subschmselectionOpt')[0].checked){ 
			addDataToGridWithsubscheme();
		}else{
			addDataToGridWithoutsubscheme();
		}
		fundSourceGridIndex = fundSourceGridIndex + 1;
	}
	
	
}
function addDataToGridWithoutsubscheme(){
	document.getElementById("finSrcGrid").style.display="block";
	fundSourceGridTable.addRow({SlNo:fundSourceGridTable.getRecordSet().getLength()+1});
	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].serialNo').innerHTML =fundSourceGridIndex+1;

	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].code').value = document.getElementById('codeUpper').value;
	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].codeLable').innerHTML = document.getElementById('codeUpper').value;

	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].name').value = document.getElementById('nameUpper').value;
	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].nameLable').innerHTML = document.getElementById('nameUpper').value;
	
	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].sourceAmount').value = document.getElementById('sourceAmountUpper').value;
	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].srcAmtLabel').innerHTML =  document.getElementById('sourceAmountUpper').value;
	
	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].isactive').value = document.getElementById('isactiveChkUpper').checked;
	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].isactiveL').innerHTML = document.getElementById('isactiveChkUpper').checked?"Yes":"No";
	
	$('form').clearForm();
}

function addDataToGridWithsubscheme(){
	
	document.getElementById("finSrcGrid").style.display="block";
	fundSourceGridTable.addRow({SlNo:fundSourceGridTable.getRecordSet().getLength()+1});
	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].serialNo').innerHTML =fundSourceGridIndex+1;

	var subschemeObj = document.getElementById('subschemeid');
	if(!subschemeObj.disabled){
		document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].subSchemeId.id').value = subschemeObj.options[subschemeObj.selectedIndex].value;
		document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].subscemeLabel').innerHTML = subschemeObj.options[subschemeObj.selectedIndex].text;

	}
	
	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].code').value = document.getElementById('codeMiddle').value;
	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].codeLable').innerHTML = document.getElementById('codeMiddle').value;

	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].name').value = document.getElementById('nameMiddle').value;
	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].nameLable').innerHTML = document.getElementById('nameMiddle').value;
	
	var finInstObj = document.getElementById('finInstId');
	if(! finInstObj.disabled){
		document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].finInstId.id').value = finInstObj.options[finInstObj.selectedIndex].value;
		document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].instNameLabel').innerHTML = finInstObj.options[finInstObj.selectedIndex].text;
	}
	
	if( document.getElementById('fundingType').disabled){
		
	     document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].type').value = "Shared Source";
	     var finSrcOwnSrcObj = document.getElementById('finSrcOwnSrc');
	     document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].fsrcTypLabel').innerHTML =  finSrcOwnSrcObj.options		[finSrcOwnSrcObj.selectedIndex].text;
	}else{

		document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].type').value = document.getElementById('fundingType').value;
		document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].fsrcTypLabel').innerHTML = document.getElementById('fundingType').value; 
		
	}

	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].sourceAmount').value = document.getElementById('sourceAmountMiddle').disabled ? document.getElementById('sourceAmountOwnSrc').value : document.getElementById('sourceAmountMiddle').value;
	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].srcAmtLabel').innerHTML =  document.getElementById('sourceAmountMiddle').disabled ? document.getElementById('sourceAmountOwnSrc').value : document.getElementById('sourceAmountMiddle').value;
	
	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].isactive').value = document.getElementById('isactiveChkMiddle').checked;
	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].isactiveL').innerHTML = document.getElementById('isactiveChkMiddle').checked?"Yes":"No";

	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].loanPercentage').value =  document.getElementById('loanPercentage').disabled ? document.getElementById('ownSrcPerc').value : document.getElementById('loanPercentage').value;
	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].amtPerctgLabel').innerHTML = document.getElementById('loanPercentage').disabled ? document.getElementById('ownSrcPerc').value : document.getElementById('loanPercentage').value;

	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].rateOfIntrest').value = document.getElementById('rateOfIntrest').value;
	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].rtofIntrLabel').innerHTML = document.getElementById('rateOfIntrest').value;
	
	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].loanPeriod').value = document.getElementById('loanPeriod').value;
	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].loanprdLabel').innerHTML = document.getElementById('loanPeriod').value;
	
	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].moratoriumPeriod').value = document.getElementById('moratoriumPeriod').value;
	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].mrtmPrdLabel').innerHTML=document.getElementById('moratoriumPeriod').value;
	
	var repyfrqObj = document.getElementById('repaymentFrequency');
	if(repyfrqObj.options[repyfrqObj.selectedIndex].value !=-1){
		document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].repaymentFrequency').value = repyfrqObj.options[repyfrqObj.selectedIndex].value;
		document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].repymntLabel').innerHTML=repyfrqObj.options[repyfrqObj.selectedIndex].text;
	
	}
	
	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].noOfInstallment').value = document.getElementById('noOfInstallment').value;
	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].noOfInstLabel').innerHTML=document.getElementById('noOfInstallment').value;

	var accnumObj = document.getElementById('accountNumber');
	if( ! accnumObj.disabled){
		document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].bankAccountId.id').value = accnumObj.options[accnumObj.selectedIndex].value;
		document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].accNumLabel').innerHTML=accnumObj.options[accnumObj.selectedIndex].text;

	}
	
	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].govtOrder').value = document.getElementById('govtOrder').value;
	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].govtOrderLabel').innerHTML=document.getElementById('govtOrder').value;

	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].govtDate').value = document.getElementById('govtDate').value;
	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].govtDateLabel').innerHTML=document.getElementById('govtDate').value;
	
	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].dpCodeNum').value = document.getElementById('dpCodeNum').value;
	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].dpCodeNumLabel').innerHTML=document.getElementById('dpCodeNum').value;
	
	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].finInstLetterNum').value = document.getElementById('finInstLetterNum').value;
	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].finInstLetNumLabel').innerHTML=document.getElementById('finInstLetterNum').value;
	
	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].finInstLetterDate').value = document.getElementById('finInstLetterDate').value;
	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].finInstLetDtLabel').innerHTML=document.getElementById('finInstLetterDate').value;
	
	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].finInstSchmNum').value = document.getElementById('finInstSchmNum').value;
	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].finInstSchmNumLabel').innerHTML=document.getElementById('finInstSchmNum').value;

	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].finInstSchmDate').value = document.getElementById('finInstSchmDate').value;
	document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].finInstSchmDtLabel').innerHTML=document.getElementById('finInstSchmDate').value;
        
	if(!document.getElementById('finSrcOwnSrc').disabled){
		document.getElementById(FUNDSOUCELIST+'['+fundSourceGridIndex+'].id').value = document.getElementById('finSrcOwnSrc').value;
	}
	$('form').clearForm();
}
var makeFundSourceGridTable = function() {
   var fundSourceGridColumns = [ 
	{key:"serialNo",label:'Sl no',width:30,formatter:createLabelSamll(FUNDSOUCELIST,".serialNo")},	
	{key:"fsrcTypL",label:'<s:text name="masters.finsrc.fundingType"/>',width:110, formatter:createLabelSamll(FUNDSOUCELIST,".fsrcTypLabel")},
	{key:"subschemeL",label:'<s:text name="masters.subscheme"/>',width:140, formatter:createLabelSamll(FUNDSOUCELIST,".subscemeLabel")},
	{key:"codeL",label:'<s:text name="masters.funsrc.code"/>',width:70, formatter:createLabelSamll(FUNDSOUCELIST,".codeLable")},
	{key:"nameL",label:'<s:text name="masters.funsrc.name"/>',width:70, formatter:createLabelSamll(FUNDSOUCELIST,".nameLable")},
	{key:"srcAmtL",label:'<s:text name="masters.finsrc.srcAmount"/>',width:90, formatter:createLabelSamll(FUNDSOUCELIST,".srcAmtLabel")},
	{key:"isactiveL",label:'Is Active',width:50, formatter:createLabelSamll(FUNDSOUCELIST,".isactiveL")},
	{key:"instNameL",label:'<s:text name="masters.finsrc.fininstname"/>',width:210, formatter:createLabelSamll(FUNDSOUCELIST,".instNameLabel")},
	{key:"rtofIntrL",label:'<s:text name="masters.finsrc.RtOfIntr"/>',width:90, formatter:createLabelSamll(FUNDSOUCELIST,".rtofIntrLabel")},
	{key:"loanprdL",label:'<s:text name="masters.finsrc.prdOfLoan"/>',width:80, formatter:createLabelSamll(FUNDSOUCELIST,".loanprdLabel")},
	{key:"mrtmPrdL",label:'<s:text name="masters.finsrc.moratoriumPrd"/>',width:80, formatter:createLabelSamll(FUNDSOUCELIST,".mrtmPrdLabel")},
	{key:"rePymtFrqL",label:'<s:text name="masters.finsrc.rePymtFrq"/>',width:140, formatter:createLabelSamll(FUNDSOUCELIST,".repymntLabel")},
	{key:"noofinstL",label:'<s:text name="masters.finsrc.noOfInstalments"/>',width:160,formatter:createLabelSamll(FUNDSOUCELIST,".noOfInstLabel")},
	{key:"GovtOrderL",label:'<s:text name="masters.finsrc.GovtOrder"/>',width:100, formatter:createLabelSamll(FUNDSOUCELIST,".govtOrderLabel")},
	{key:"GovtOrderDateL",label:'<s:text name="masters.finsrc.GovtOrderDate"/>',width:140, formatter:createLabelSamll(FUNDSOUCELIST,".govtDateLabel")},
	{key:"dpCodeNumL",label:'<s:text name="masters.finsrc.dpCodeNum"/>',width:180, formatter:createLabelSamll(FUNDSOUCELIST,".dpCodeNumLabel")},
	{key:"finINmL",label:'<s:text name="masters.finsrc.finInstletterNum"/>',width:220, formatter:createLabelSamll(FUNDSOUCELIST,".finInstLetNumLabel")},
	{key:"finIDtL",label:'<s:text name="masters.finsrc.finInstLetterDt"/>',width:220, formatter:createLabelSamll(FUNDSOUCELIST,".finInstLetDtLabel")},
	{key:"finINumL",label:'<s:text name="masters.finsrc.finInstSchmNum"/>',width:330, formatter:createLabelSamll(FUNDSOUCELIST,".finInstSchmNumLabel")},
	{key:"finISDtL",label:'<s:text name="masters.finsrc.finInstSchDt"/>',width:330, formatter:createLabelSamll(FUNDSOUCELIST,".finInstSchmDtLabel")},
	{key:"amtPerctgL",label:'<s:text name="masters.finsrc.amtPerc"/>',width:120, formatter:createLabelSamll(FUNDSOUCELIST,".amtPerctgLabel")},
	{key:"bankAccL",label:'<s:text name="masters.finsrc.bankAcc"/>',width:140, formatter:createLabelSamll(FUNDSOUCELIST,".accNumLabel")},
	
	
	{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")},
	{key:"subscheme",hidden:true,formatter:createHiddenField(FUNDSOUCELIST,".subSchemeId.id","hidden")},
	{key:"code",hidden:true,formatter:createHiddenField(FUNDSOUCELIST,".code","hidden")},
	{key:"nameh",hidden:true,formatter:createHiddenField(FUNDSOUCELIST,".name","hidden")},
	{key:"instNameh",hidden:true,formatter:createHiddenField(FUNDSOUCELIST,".finInstId.id","hidden")},
	{key:"fundingTyph",hidden:true,formatter:createHiddenField(FUNDSOUCELIST,".type","hidden")},
	{key:"srcAmt",hidden:true,formatter:createHiddenField(FUNDSOUCELIST,".sourceAmount","hidden")},
	{key:"amtPerctg",hidden:true,formatter:createHiddenField(FUNDSOUCELIST,".loanPercentage","hidden")},
	{key:"rtofIntr",hidden:true,formatter:createHiddenField(FUNDSOUCELIST,".rateOfIntrest","hidden")},
	{key:"loanprd",hidden:true,formatter:createHiddenField(FUNDSOUCELIST,".loanPeriod","hidden")},
	{key:"mrtmPrd",hidden:true,formatter:createHiddenField(FUNDSOUCELIST,".moratoriumPeriod","hidden")},
	{key:"rePymtFrq",hidden:true,formatter:createHiddenField(FUNDSOUCELIST,".repaymentFrequency","hidden")},
	{key:"noofinst",hidden:true,formatter:createHiddenField(FUNDSOUCELIST,".noOfInstallment","hidden")},
	{key:"bankAcc",hidden:true,formatter:createHiddenField(FUNDSOUCELIST,".bankAccountId.id","hidden")},
	{key:"GovtOrder",hidden:true,formatter:createHiddenField(FUNDSOUCELIST,".govtOrder","hidden")},
	{key:"GovtOrderDate",hidden:true,formatter:createHiddenField(FUNDSOUCELIST,".govtDate","hidden")},
	{key:"dpCodeNum",hidden:true,formatter:createHiddenField(FUNDSOUCELIST,".dpCodeNum","hidden")},
	{key:"finINm",hidden:true,formatter:createHiddenField(FUNDSOUCELIST,".finInstLetterNum","hidden")},
	{key:"finIDt",hidden:true,formatter:createHiddenField(FUNDSOUCELIST,".finInstLetterDate","hidden")},
	{key:"finINum",hidden:true,formatter:createHiddenField(FUNDSOUCELIST,".finInstSchmNum","hidden")},
	{key:"finISDt",hidden:true,formatter:createHiddenField(FUNDSOUCELIST,".finInstSchmDate","hidden")},
	{key:"isactive",hidden:true,formatter:createHiddenField(FUNDSOUCELIST,".isactive","hidden")},
	{key:"finSrcId",hidden:true,formatter:createHiddenField(FUNDSOUCELIST,".id","hidden")}
	
	];
		
		var fundSourceGridDS = new YAHOO.util.DataSource(); 
		fundSourceGridTable = new YAHOO.widget.DataTable("fundSourceGridTable",fundSourceGridColumns, fundSourceGridDS);
		fundSourceGridTable.on('cellClickEvent',function (oArgs) {
			var target = oArgs.target;
			var record = this.getRecord(target);
			var column = this.getColumn(target);
			if (column.key == 'Delete') { 	
					fundSourceGridIndex = fundSourceGridIndex -1;	
					this.deleteRow(record);
					allRecords=this.getRecordSet();
					for(var i=0;i<allRecords.getLength();i++){
						this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
					}
					
				
			}
			
			        
		});
}

function createLabelSamll(prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<label id='"+prefix+"["+fundSourceGridIndex+"]"+suffix+"'  size='3'/>";
	}
}
function createHiddenField(prefix,suffix,type){
	 return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='"+type+"' name='"+prefix+"["+fundSourceGridIndex+"]"+suffix+"' id='"+prefix+"["+fundSourceGridIndex+"]"+suffix+"'/>";
	}
	
}
 function validateDecimal(obj){
		var fieldValue = obj.value;

		if(null == fieldValue.match(/(^-*\d+$)|(^-*\d+\.\$)|(^-*\d+\.\d+$)/)){
			obj.value = fieldValue.substring(0,fieldValue.length-1) ;
		}
		
}
function validateGrid(){
	if(fundSourceGridIndex == 0){
		document.getElementById('lblGridError').innerHTML = "Can not create financial source : Nothing found in the grid";
		return false;
	}
	return true;
	fundSourceGridIndex = 0;
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
</style>
