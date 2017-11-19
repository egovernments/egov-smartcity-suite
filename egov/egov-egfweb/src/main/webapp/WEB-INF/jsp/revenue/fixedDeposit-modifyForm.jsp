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


<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>

<SCRIPT type="text/javascript">
		var selectedname;
		var childName;
		var extendCount=0;
		var res;
		var makeFDTable = function(objTable) {                   
    	var fixedDepositTableColumns = [ 
			{key:"SlNo",label:'Sl No',width:30},
			{key:"id",label:'id',width:100,hidden:true, formatter:createTextFieldFormatter('FDtable',FDLIST,".id","hidden")},
			{key:"fileNo",label:'File Number',width:100, formatter:createTextFieldFormatter('FDtable',FDLIST,".fileNo","text")},
			{key:"amount",label:'Amount Of Deposit',width:100, formatter:createTextFieldFormatter('FDtable',FDLIST,".amount","text")},
			{key:"date",label:'Date Of Deposit<br />(dd/mm/yyyy)',width:100, formatter:createDateFieldFormatter('FDtable',FDLIST,".date","text")},
			{key:"bankBranch",label:'Bank and Branch',width:100,formatter:createBankBranchDropDownFormatter('FDtable',FDLIST,".bankBranch.id","text")},
			{key:"bankAccount",label:'Bank Account',width:100,formatter:createBankAccountDropDownFormatter('FDtable',FDLIST,".bankAccount.id","text")},
			//{key:"bankAccountList",label:'bankAccountList',width:100,hidden:true, formatter:createTextFieldFormatter('FDtable',FDLIST,".bankAccountList","hidden")},
			//{key:"refNumber",label:'refrenceNumber',width:100,hidden:true, formatter:createTextFieldFormatter('FDtable',FDLIST,".referenceNumber","hidden")},
			{key:"interestRate",label:'Rate Of Interest',width:100, formatter:createTextFieldFormatter('FDtable',FDLIST,".interestRate","text")},
			{key:"period",label:'Period Of Deposit',width:100, formatter:createTextFieldFormatter('FDtable',FDLIST,".period","text")},
			{key:"serialNumber",label:'FixedDeposit SerialNumber',width:100, formatter:createTextFieldFormatter('FDtable',FDLIST,".serialNumber","text")},
			{key:"GJVvhid",label:'GJVvhid',width:100,hidden:true, formatter:createTextFieldFormatter('FDtable',FDLIST,".outFlowVoucher.id","hidden")},
			{key:"outFlowVoucher",label:'GJV Number',width:100, formatter:createTextFieldFormatterImg('FDtable',FDLIST,".outFlowVoucher.voucherNumber","text")},
			{key:"GJVDate",label:'Date',width:100, formatter:createTextFieldFormatter('FDtable',FDLIST,".outFlowVoucher.voucherDate","text")},
			{key:"gjvAmount",label:'GJV Amount',width:100, formatter:createTextFieldFormatter('FDtable',FDLIST,".gjvAmount","text")},
			{key:"maturityAmount",label:'Maturity Amount',width:100, formatter:createTextFieldFormatter('FDtable',FDLIST,".maturityAmount","text")},
			{key:"maturityDate",label:'Maturity Date<br />(dd/mm/yyyy)',width:100, formatter:createDateFieldFormatter('FDtable',FDLIST,".maturityDate","text")},
			{key:"withdrawalDate",label:'Withdrawal Date <br />(dd/mm/yyyy)',width:100, formatter:createDateFieldFormatter('FDtable',FDLIST,".withdrawalDate","text")},
			{key:"receiptGJVvhid",label:'receiptGJVvhid',width:100,hidden:true, formatter:createTextFieldFormatter('FDtable',FDLIST,".inFlowVoucher.id","hidden")},
			{key:"inFlowVoucher",label:'InFlow VoucherNumber',width:100, formatter:createTextFieldFormatterImg('FDtable',FDLIST,".inFlowVoucher.voucherNumber","text")},
			{key:"receiptGJVDate",label:'Date',width:100, formatter:createTextFieldFormatter('FDtable',FDLIST,".inFlowVoucher.voucherDate","text")},
			{key:"challanvhid",label:'receiptGJVvhid',width:100,hidden:true, formatter:createTextFieldFormatter('FDtable',FDLIST,".challanReceiptVoucher.id","hidden")},
			{key:"challanGJV",label:'Challan Number',width:100, formatter:createTextFieldFormatterImg('FDtable',FDLIST,".challanReceiptVoucher.voucherNumber","text")},
			{key:"receiptGJVDate",label:'Date',width:100, formatter:createTextFieldFormatter('FDtable',FDLIST,".challanReceiptVoucher.voucherDate","text")},
			{key:"receiptAmount",label:'Receipt Amount',width:100, formatter:createTextFieldFormatterWithStyle('FDtable',FDLIST,".receiptAmount","width:100px")},
			{key:"instrumentid",label:'InstrumentHeaderid',width:100,hidden:true, formatter:createTextFieldFormatter('FDtable',FDLIST,".instrumentHeader.id","hidden")},
			{key:"instrumentNumber",label:'Cheuqe No',width:100, formatter:createTextFieldFormatter('FDtable',FDLIST,".instrumentHeader.instrumentNumber","text")},
			{key:"instrumentDate",label:'Cheque Date',width:100, formatter:createTextFieldFormatter('FDtable',FDLIST,".instrumentHeader.instrumentDate","text")},
			{key:"remarks",label:'Remarks',width:100, formatter:createTextFieldFormatterWithStyle('FDtable',FDLIST,".remarks","width:100px")},
			{key:"parentId",label:'parentId',width:100,formatter:createTextFieldFormatter('FDtable',FDLIST,".parentId","text")},
			{key:"extend",label:'Extend',width:25, formatter:createCheckboxFieldFormatter('FDtable',FDLIST,".extend","checkbox")},
			{key:'Add',label:'Add',width:13,formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
			{key:'Delete',label:'Del',width:13,formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
		];
		var fdTableDS = new YAHOO.util.DataSource();
		fdTableDT = new YAHOO.widget.DataTable(objTable,fixedDepositTableColumns, fdTableDS);
		fdTableDT.on('cellClickEvent',function (oArgs) {
		var target = oArgs.target;
		var record = this.getRecord(target);
		var column = this.getColumn(target);
		if (column.key == 'Add') { 
			fdTableDT.addRow({SlNo:fdTableDT.getRecordSet().getLength()+1});
			updateFDTableIndex();
		}
		if (column.key == 'Delete') { 	
			if(this.getRecordSet().getLength()>1){			
				this.deleteRow(record);
				allRecords=this.getRecordSet();
				for(var i=0;i<allRecords.getLength();i++){
					this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
				}
			}
			else{
				bootbox.alert("This row can not be deleted");
			}
		}
	});
		<s:iterator value="fixedDepositList" status="stat">
			fdTableDT.addRow({"SlNo":fdTableDT.getRecordSet().getLength()+1,
			"id":'<s:property value="id"/>',
				"fileNo":'<s:property value="fileNo"/>',
				"amount":'<s:property value="amount"/>',
				"date":'<s:property value="date"/>',
				"bankBranch":'<s:property value="bankBranch.id"/>',
				"bankAccount":'<s:property value="bankAccount.id"/>',
				//"bankAccountList":'<s:property value="bankAccountList"/>',
				//"referenceNumber":'<s:property value="referenceNumber"/>',
				"interestRate":'<s:property value="interestRate"/>',
				"period":'<s:property value="period"/>',
				"serialNumber":'<s:property value="serialNumber"/>',
				"GJVvhid":'<s:property value="outFlowVoucher.id"/>',
				"generalVoucher":'<s:property value="outFlowVoucher.voucherNumber"/>',
				"GJVDate":'<s:property value="outFlowVoucher.voucherDate"/>',
				"gjvAmount":'<s:property value="gjvAmount"/>',		
				"maturityAmount":'<s:property value="maturityAmount"/>',
				"maturityDate":'<s:property value="maturityDate"/>',
				"WithdrawalDate":'<s:property value="withdrawalDate"/>',
				"receiptGJVvhid":'<s:property value="inFlowVoucher.id"/>',
				"inFlowVoucher":'<s:property value="inFlowVoucher.voucherNumber"/>',
				"receiptGJVDate":'<s:property value="inFlowVoucher.voucherDate"/>',
				"challanvhid":'<s:property value="challanReceiptVoucher.id"/>',
				"challanGJV":'<s:property value="challanReceiptVoucher.voucherNumber"/>',
				"challanGJVDate":'<s:property value="challanReceiptVoucher.voucherDate"/>',
				"receiptAmount":'<s:property value="receiptAmount"/>',
				"instrumentid":'<s:property value="instrumentHeader.id"/>',
				"instrumentNumber":'<s:property value="instrumentHeader.instrumentNumber"/>',
				"instrumentDate":'<s:property value="instrumentHeader.instrumentDate"/>',
				"remarks":'<s:property value="remarks"/>',
				"parentId":'<s:property value="parentId"/>',
				"extend":'<s:property value="extend"/>'
		});
		
			var index = '<s:property value="#stat.index"/>';
			updateYUIGrid(FDLIST,'id',index,'<s:property value="id"/>');
			updateYUIGrid(FDLIST,'fileNo',index,'<s:property value="fileNo"/>');
			updateYUIGrid(FDLIST,'amount',index,'<s:property value="amount"/>');
			updateYUIGrid(FDLIST,'date',index,'<s:property value="date"/>');
			updateYUIGrid(FDLIST,'bankBranch.id',index,'<s:property value="bankBranch.id"/>');
			updateYUIGrid(FDLIST,'bankAccount.id',index,'<s:property value="bankAccount.id"/>');
			//updateYUIGrid(FDLIST,'bankAccountList',index,'<s:property value="bankAccountList"/>');
			//updateYUIGrid(FDLIST,'referenceNumber',index,index+1);
			updateYUIGrid(FDLIST,'interestRate',index,'<s:property value="interestRate"/>');
			updateYUIGrid(FDLIST,'period',index,'<s:property value="period"/>');
			updateYUIGrid(FDLIST,'serialNumber',index,'<s:property value="serialNumber"/>');
			updateYUIGrid(FDLIST,'outFlowVoucher.id',index,'<s:property value="outFlowVoucher.id"/>');
			updateYUIGrid(FDLIST,'outFlowVoucher.voucherNumber',index,'<s:property value="outFlowVoucher.voucherNumber"/>');
			updateYUIGrid(FDLIST,'outFlowVoucher.voucherDate',index,'<s:property value="outFlowVoucher.voucherDate"/>');
			updateYUIGrid(FDLIST,'gjvAmount',index,'<s:property value="gjvAmount"/>');
			updateYUIGrid(FDLIST,'maturityAmount',index,'<s:property value="maturityAmount"/>');
			updateYUIGrid(FDLIST,'maturityDate',index,'<s:property value="maturityDate"/>');
			updateYUIGrid(FDLIST,'withdrawalDate',index,'<s:property value="withdrawalDate"/>');
			updateYUIGrid(FDLIST,'inFlowVoucher.id',index,'<s:property value="inFlowVoucher.id"/>');
			updateYUIGrid(FDLIST,'inFlowVoucher.voucherNumber',index,'<s:property value="inFlowVoucher.voucherNumber"/>');
			updateYUIGrid(FDLIST,'inFlowVoucher.voucherDate',index,'<s:property value="inFlowVoucher.voucherDate"/>');
			updateYUIGrid(FDLIST,'challanReceiptVoucher.id',index,'<s:property value="challanReceiptVoucher.id"/>');
			updateYUIGrid(FDLIST,'challanReceiptVoucher.voucherNumber',index,'<s:property value="challanReceiptVoucher.voucherNumber"/>');
			updateYUIGrid(FDLIST,'challanReceiptVoucher.voucherDate',index,'<s:property value="challanReceiptVoucher.voucherDate"/>');
			updateYUIGrid(FDLIST,'receiptAmount',index,'<s:property value="receiptAmount"/>');
			updateYUIGrid(FDLIST,'instrumentHeader.id',index,'<s:property value="instrumentHeader.id"/>');
			updateYUIGrid(FDLIST,'instrumentHeader.instrumentNumber',index,'<s:property value="instrumentHeader.instrumentNumber"/>');
			updateYUIGrid(FDLIST,'instrumentHeader.instrumentDate',index,'<s:property value="instrumentHeader.instrumentDate"/>');
			updateYUIGrid(FDLIST,'remarks',index,'<s:property value="remarks"/>');
			updateYUIGrid(FDLIST,'parentId',index,'<s:property value="parentId.id"/>');
			updateYUIGrid(FDLIST,'extend',index,'<s:property value="extend"/>');
			updateFDTableIndex();	                          
			</s:iterator>
		
				         
			
    }
        
                                  
    
    var FDLIST='fixedDepositList';
    var fdTableIndex=0;
  //  var childTableIndex=0;
    var challanGJV=false;
    function createBankBranchDropDownFormatter(tableType,prefix,suffix){
	return function(el, oRecord, oColumn, oData) {
	var index=getIndexForTableType(tableType);
	var value = (YAHOO.lang.isValue(oData))?oData:"";
		var element=" <select  id='"+prefix+"["+index+"]"+suffix+"' name='"+prefix+"["+index+"]"+suffix+"'  style=width:90px  onchange=getbranchAccountId(this);return false;' >";
		element=element+"<option value=-1 selected='selected' > --- Choose --- </option>  ";
		
		<s:iterator value="bankBranchList" status="stat">
			var name='<s:property value="bank.name"/>'+"-"+'<s:property value="branchname"/>';
			var id='<s:property value="id" />';
			element=element+" <option value="+id +" > "+ name+" </option>  ";
		</s:iterator>
		element=element+" </select>";
		el.innerHTML =element ;
		}
	}                                        
	function createBankAccountDropDownFormatter(tableType,prefix,suffix){
	  return function(el, oRecord, oColumn, oData) {
	    var index=getIndexForTableType(tableType);
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		
		var accountOptions=[{label:"--- Select ---", value:"0"}];
		
		var element=" <select  id='"+prefix+"["+index+"]"+suffix+"' name='"+prefix+"["+index+"]"+suffix+"' >";
		element=element+" </select>";
		el.innerHTML =element ;
		}   
	}            
	function createTextFieldFormatterImg(tableType,prefix,suffix,type){
	   return function(el, oRecord, oColumn, oData) {
		var tableIndex=getIndexForTableType(tableType);
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		var imgsuffix=suffix+"img";
		el.innerHTML = " <input type='"+type+"' id='"+prefix+"["+tableIndex+"]"+suffix+"' name='"+prefix+"["+tableIndex+"]"+suffix+"' style='width:90px;' /><img src='/egi/resources/erp2/images/searchicon.gif' id='"+prefix+"["+tableIndex+"]"+imgsuffix+"' name='"+prefix+"["+tableIndex+"]"+imgsuffix+"' onclick='openViewVouchers(this)'/>";
		}    
	}
	function createTextFieldFormatterWithStyle(tableType,prefix,suffix,style){
		return function(el, oRecord, oColumn, oData) {
		var tableIndex=getIndexForTableType(tableType);
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = " <input type='text' id='"+prefix+"["+tableIndex+"]"+suffix+"' name='"+prefix+"["+tableIndex+"]"+suffix+"' style='"+style+"' />";
		}
	}
	
	
	function createDateFieldFormatter(tableType,prefix,suffix)
	{	
		return function(el, oRecord, oColumn, oData) {
			var value = (YAHOO.lang.isValue(oData))?oData:"";
			var index=getIndexForTableType(tableType);
			//bootbox.alert("hiii");
			var fieldName = prefix+"[" + index + "]" +  suffix;
			var idt=oColumn.getKey()+oRecord.getId();
			var id=idt.replace("-","");
			var CALENDERURL="/egi/resources/erp2/images/calendaricon.gif";
			var HREF='javascript:show_calendar("forms[0].'+id+'")';
			markup="<input type='text' id='"+id+"' name='"+fieldName+"' value='"+value+"'    maxlength='10' style=\"width:70px\" onkeyup='DateFormat(this,this.value,event,false,3);' onblur='checkDateLG(this);' /><a href='#' style='text-decoration:none' onclick='"+HREF+"'><img src='"+CALENDERURL+"' border='0'  /></a>";
	 		el.innerHTML = markup;
		}
	}    
	  
	function createTextFieldFormatter(tableType,prefix,suffix,type){
	return function(el, oRecord, oColumn, oData) {
		var tableIndex=getIndexForTableType(tableType);
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		
			el.innerHTML = " <input type='"+type+"' id='"+prefix+"["+tableIndex+"]"+suffix+"' name='"+prefix+"["+tableIndex+"]"+suffix+"'  style='width:90px;' />";

	}
}
function createCheckboxFieldFormatter(tableType,prefix,suffix,type){
	return function(el, oRecord, oColumn, oData) {
		var tableIndex=getIndexForTableType(tableType);
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = " <input type='"+type+"' id='"+prefix+"["+tableIndex+"]"+suffix+"' name='"+prefix+"["+tableIndex+"]"+suffix+"' style=width:90px  onclick=extendFixedDeposit(this);return false;' />";
	}                          
}      

 
	function loadChequeNoAndDate(billVhId,name){
		var url = '../voucher/common!ajaxLoadChequeNoAndDate.action?billVhId='+billVhId;
		YAHOO.util.Connect.asyncRequest('POST', url, chequeNoAndDate, null);
	}
	
	var chequeNoAndDate={
		success: function(o) {
			if(o.responseText!="")
			{
				var docs=o.responseText.split("$");
				document.getElementById(onlyName+".instrumentHeader.id").value= ((docs[0]=='0')?"":docs[0]);
				document.getElementById(onlyName+".instrumentHeader.instrumentNumber" ).value= ((docs[1]=='0')?"":docs[1]);
				document.getElementById(onlyName+".instrumentHeader.instrumentDate" ).value= ((docs[2]=='-')?"":docs[2]);
				//document.getElementById(onlyName+".instrumentHeader.instrumentAmount" ).value= ((docs[3]=='0')?"":docs[3]);
				
			}
		},
		failure: function(o) {
			bootbox.alert('Cannot fetch instrument and account details');
		}
	                  
}
	function getbranchAccountId(obj){
		var branchId=obj.value;
		selectedname=obj.name;
		
		//bootbox.alert("object in get branch>>"+selectedname);
		var url = '../voucher/common!ajaxLoadBranchAccountNumbers.action?branchId='+branchId;
		YAHOO.util.Connect.asyncRequest('POST', url, bankAccountList, null);
	
	}
	function getAccountId(obj){
		var branchId=obj.value;
		selectedname=obj.name;
		
		var indexobj=selectedname.split("]");
		var test=true;
		var i=0;
		var j=0;
		var ind=indexobj[0].substring(indexobj[0].length,indexobj[0].length-1);
		var listSize='<s:property value="fixedDepositList.size()"/>';
		var res1=new Array(listSize);
		var resId=new Array(listSize);
	
		
		<s:iterator var="p" value="fixedDepositList" status="fixedDep_staus">
		//bootbox.alert('<s:property value="#fixedDep_staus.index"/>'+"   "+ind);
		
		if('<s:property value="#fixedDep_staus.index"/>'==ind){
		
		<s:iterator value="#p.bankAccountList" status="bankAccount_status">
	
			var accSize='<s:property value="#p.bankAccountList.size()"/>';
			if(i<listSize)
			 {
				res1[i]=new Array(accSize);
				resId[i]=new Array(accSize);
				///bootbox.alert(accSize);
				
				if(j<accSize){
					res1[i][j]='<s:property value="accountnumber"/>'+"-"+'<s:property value="%{chartofaccounts.glcode}"/>';
			 		resId[i][j]='<s:property value="id" />';
			 		
			 		j++;
			}i++;
		}
		</s:iterator>
		
		}
		</s:iterator>    
		//bootbox.alert(res1.length);
		var accNumid1=selectedname;
		accNumid1=accNumid1.replace('Branch','Account');
		//bootbox.alert("accnum "+accNumid);
		var x=document.getElementById(accNumid1);
				x.length=0;
				x.options[0]=new Option("----Choose----","-1");  
				//bootbox.alert(res1);     
				var k=0;    
 							for(var i=0;i<res1.length;i++)
 							{ 
 								for(var j=0;j<res1[i].length;j++){
 							   		x.options[i]=new Option(res1[i][j],resId[i][j]);
 						   		 }  
						   }
	}
	var bankAccountList={
		success: function(o) {
			if(o.responseText!="")
			{
				var docs=o.responseText;               
				res=docs.split("$");
				var accNumid=selectedname;
				//bootbox.alert("docs "+docs);
				accNumid=accNumid.replace('Branch','Account');
				//bootbox.alert("accnum "+accNumid);
				var x=document.getElementById(accNumid);
				x.length=0;
				x.options[0]=new Option("----Choose----","-1");  
				x.options[0].text='---Select---';
				x.options[0].value=0;          
 							for(var i=0;i<res.length-1;i++)
 							{
 							      
	 							var idandnum=res[i].split('~');
	 							x.options[i]=new Option(idandnum[0],idandnum[1]);
 						    }                     
			}
		},                                         
		failure: function(o) {
			bootbox.alert('Cannot fetch instrument and account details');
		}
	}
	            
	           
	function openViewVouchers(obj)
	{
		var url = '../voucher/voucherSearch!beforesearch.action?showMode=sourceLink';
		var val=	window.showModalDialog(url,"SearchBillVouchers","dialogwidth: 800; dialogheight: 600;");
		if(val!=undefined && val!=null && val!="" && val.split("$").length>0)
		{
		var objName=obj.name;
		var name=objName.replace("img","");
		var data=val.split("$");
		document.getElementById(name).value=data[0];    // voucher number
		var id=name.replace("voucherNumber","id");
		var date=name.replace("Number","Date");
		document.getElementById(date).value=data[1];    // voucherDate
		document.getElementById(id).value=data[2];   // vouchderid
		patt1 = name.match("outFlowVoucher.voucherNumber");
		patt2 = name.match("inFlowVoucher.voucherNumber");
		//bootbox.alert(name+" >>> hi name");                     
		              
		if(patt1=="outFlowVoucher.voucherNumber"){
			onlyName=name.replace(".outFlowVoucher.voucherNumber","")
		}        
		else if(patt2=="inFlowVoucher.voucherNumber"){
			onlyName=name.replace(".inFlowVoucher.voucherNumber","")
		}
		else{
			bootbox.alert("inside Challan reciept voucher");
			onlyName=name.replace(".challanReceiptVoucher.voucherNumber","")
			loadChequeNoAndDate(data[2],onlyName);
			
		}
		}
			
	}
function getIndexForTableType(tableType)
{	               
		if(tableType=='FDtable'){
 			return fdTableIndex;
		}
}

	
function updateYUIGrid(fdlist, field,index,value){
	  var obj="";
	  if(value==null)
		return;
	 if(field=='date' || field=='maturityDate' || field=='withdrawalDate' ||field=='extend'){
		document.getElementsByName(fdlist+'['+index+'].'+field)[0].value=value;
	 }else if(field=='bankBranch.id'){
	 	document.getElementById(fdlist+'['+index+'].'+field).value=value;
	 	var docobj=document.getElementById(fdlist+'['+index+'].'+'bankBranch.id');
		getAccountId(docobj);
	 }else {
		 	document.getElementById(fdlist+'['+index+'].'+field).value=value;
	}
	          
}	
 
function updateFDTableIndex()
{
	fdTableIndex++;
}

</SCRIPT>

<div id="labelAD" align="center">
	<h1>
		<s:text name="fixeddeposit.heading.view" />
	</h1>
</div>
<br></br>

<div class="formmainbox">
	<div class="formheading" />
	<div class="subheadnew">
		<s:text name="fixeddeposit.heading.detail" />
	</div>
</div>
</div>
<div id="listid" style="display: block"></div>
<br></br>
<div class="yui-skin-sam" align="center" style="overflow-x: scroll">
	<div id="fdTablediv"></div>
</div>


<script type="text/javascript">
     		makeFDTable('fdTablediv');
     		document.getElementById('fdTablediv').getElementsByTagName('table')[0].width="80%";
     		 
     	</script>
