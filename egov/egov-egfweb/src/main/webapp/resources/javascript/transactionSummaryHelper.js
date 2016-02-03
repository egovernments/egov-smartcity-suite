/**
 * 
 */

var allGlcodes={};
var slDetailTableIndex = 1;
var billDetailTableIndex = 0;
var TRANSACTIONSUMMARYLIST='transactionSummaryList';
var entities;
var detailTypeId=0;
var codeObj;
var $currRow;
function loadDropDownCodes()
{
/*	alert(document.getElementById("major").value);
	alert(document.getElementById("minor").value);
		alert(document.getElementById("type").value);*/
	
	if(codeObj)
	{
	codeObj=null;
	if(oAutoCompCode!=null)
		oAutoCompCode.destroy();
	}	
	//var	url = "/EGF/voucher/common-ajaxLoadCOA.action?accountDetailType="+obj.value;
	var	url="";
	if(document.getElementById("minor").value!="")
	{	
	var	url = "/EGF/commons/Process.jsp?type=coaDetailCode?glCode="+document.getElementById("minor").value;
	}else if(document.getElementById("major").value!="")
	{	
 	url = "/EGF/commons/Process.jsp?type=coaDetailCode?glCode="+document.getElementById("major").value;
	}else if(document.getElementById("type").value=="A")
	{	
	 	url = "/EGF/commons/Process.jsp?type=getAllAssetCodes";
	}else{
		url = "/EGF/commons/Process.jsp?type=getAllLiabCodes";
	}
	
		
	var req2 = initiateRequest();
	req2.onreadystatechange = function()
	{
	  if (req2.readyState == 4)
	  {
		  if (req2.status == 200)
		  {
			var codes2=req2.responseText;
			var a = codes2.split("^");
			var codes = a[0];
			acccodeArray=codes.split("+");
			for(i=0;i<acccodeArray.length;i++){
				data = acccodeArray[i].split("`~`")
				acccodeArray[i] = data[0];
				var key = data[0];
				var value = data[1]
				allGlcodes[key] = value;
			}			
			codeObj = new YAHOO.widget.DS_JSArray(acccodeArray);
		  }
	  }
 	};
	req2.open("GET", url, true);
	req2.send(null);
}

var funcObj;
var funcArray;
function loadDropDownCodesFunction()
{
	var url = "/EGF/commons/Process.jsp?type=getAllFunctionName";
	var req2 = initiateRequest();
	req2.onreadystatechange = function()
	{
	  if (req2.readyState == 4)
	  {
		  if (req2.status == 200)
		  {
			var codes2=req2.responseText;
			var a = codes2.split("^");
			var codes = a[0];
			funcArray=codes.split("+");
			funcObj= new YAHOO.widget.DS_JSArray(funcArray);
		  }
	   }
	};
	req2.open("GET", url, true);
	req2.send(null);
}

var yuiflag = new Array();
var oAutoCompCode;
function autocompletecode(obj,myEvent)
{
	//alert('autocomplete');
	
	//loadDropDownCodes();
	var src = obj;	
	var target = document.getElementById('codescontainer');	
	var coaCodeObj=obj;
	$currRow=getRowIndex(obj);
	//40 --> Down arrow, 38 --> Up arrow
	if(yuiflag[$currRow] == undefined)
	{
		var key = window.event ? window.event.keyCode : myEvent.charCode;  
		if(key != 40 )
		{
			if(key != 38 )
			{
				oAutoCompCode = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainer', codeObj);
				oAutoCompCode.queryDelay = 0;
				oAutoCompCode.prehighlightClassName = "yui-ac-prehighlight";
				oAutoCompCode.useShadow = true;
				oAutoCompCode.maxResultsDisplayed = 15;
				oAutoCompCode.useIFrame = true;
				oAutoCompCode.applyLocalFilter = true;
				oAutoCompCode.queryMatchContains = true;
				oAutoCompCode.minQueryLength = 0;
				 oAutoCompCode.doBeforeExpandContainer = function(oTextbox, oContainer, sQDetauery, aResults) {
		           var pos = YAHOO.util.Dom.getXY(oTextbox);
		           pos[1] += YAHOO.util.Dom.get(oTextbox).offsetHeight + 6;
		           oContainer.style.width=300;
		           YAHOO.util.Dom.setXY(oContainer,pos);
		           return true;

			};  
				
				oAutoCompCode.formatResult = function(oResultData, sQuery, sResultMatch) {
					var data = oResultData.toString();
				    return data.split("`~`")[0];
				};
			}
		}
		yuiflag[$currRow] = 1;
	}	
}

function findPos(ob) 
{
	var obj=eval(ob);
	var curleft = curtop = 0;
	if (obj.offsetParent) 
	{
		curleft = obj.offsetLeft;
		curtop = obj.offsetTop;
		while (obj = obj.offsetParent) 
		{	//alert(obj.nodeName+"---"+obj.offsetTop+"--"+obj.offsetLeft+"-----"+curtop);
			curleft =curleft + obj.offsetLeft;
			curtop =curtop + obj.offsetTop; 
			//alert(curtop);
		}
	}
	//alert(curleft+"             "+curtop);
	return [curleft,curtop];
		
}

function fillNeibrAfterSplitGlcode(obj)
{
	var key = obj.value;
	var temp = obj.value;
	temp = temp.split("`-`");
	$currRow=getRowIndex(obj);
	var acchead= document.getElementById('transactionSummaryList['+$currRow+'].accounthead').value;
	if(acchead !=null && acchead !=""){
		key = key+"`-`";
		key = key+acchead;
	}
	var accCodeid = allGlcodes[key];
	if(temp.length>1)
	{ 
		obj.value=temp[0];
		$currRow=getRowIndex(obj);
		document.getElementById('transactionSummaryList['+$currRow+'].glcodeid.id').value=temp[2];
		document.getElementById('transactionSummaryList['+$currRow+'].accounthead').innerHTML=temp[1];//.split("`~`")[0];
		var flag=false;
		for (var i=0; i<slDetailTableIndex;i++ )
		{
			for(var j=0; j<billDetailTableIndex;j++){
				if(null != document.getElementById(TRANSACTIONSUMMARYLIST+'['+i+'].glcode.id')){
					var subledgerSel = document.getElementById(TRANSACTIONSUMMARYLIST+'['+i+'].glcode.id').value;
					
				}
				if(null != document.getElementById(TRANSACTIONSUMMARYLIST+'['+j+'].glcodeid.id')){
					var billDetailSel = document.getElementById(TRANSACTIONSUMMARYLIST+'['+j+'].glcodeid.id').value;
				}
				if(subledgerSel == billDetailSel){
					
					flag = true;break;
				}
				
			}
			if(!flag){
				//document.getElementById(TRANSACTIONSUMMARYLIST+'['+i+'].glcode.id').value=0;
				//document.getElementById(TRANSACTIONSUMMARYLIST+'['+i+'].accountdetailtype.id').value=0;
				//document.getElementById(TRANSACTIONSUMMARYLIST+'['+i+'].detailTypeName').value="";
				//document.getElementById(TRANSACTIONSUMMARYLIST+'['+i+'].accountdetailkey').value="";
				//document.getElementById(TRANSACTIONSUMMARYLIST+'['+i+'].detailKeyId').value="";
				//document.getElementById(TRANSACTIONSUMMARYLIST+'['+i+'].detailKey').value="";
				//document.getElementById(TRANSACTIONSUMMARYLIST+'['+i+'].amount').value="";
			}
			
		}
		for (var i=0; i<slDetailTableIndex;i++ )
		{
			d=document.getElementById(TRANSACTIONSUMMARYLIST+'['+i+'].glcode.id');
			if(null != d){
				for(p=d.options.length-1;p>=0;p--)
				{
					var flag1 = false;
					for(var j=0; j<billDetailTableIndex;j++){
						if(null != document.getElementById(TRANSACTIONSUMMARYLIST+'['+j+'].glcodeid.id')){
							if(d.options[p].value == document.getElementById(TRANSACTIONSUMMARYLIST+'['+j+'].glcodeid.id').value){
									flag1=true;
							}			
						}
						
					}
					if(!flag1 && d.options[p].value !=0){
						d.remove(p);
					}
				}
			}
			
		}
		check();
	}else if (temp!="" &&(accCodeid==null || accCodeid=="")){
		alert("Invalid Account Code selected .Please select code from auto complete.");
		obj.value="";
		document.getElementById('transactionSummaryList['+$currRow+'].glcodeid.id').value="";
	}

	var subledgerid=document.getElementById('transactionSummaryList['+$currRow+'].glcodeDetail');
	var accountCode = subledgerid.value;
	//document.getElementById('subLedgerlist['+$currRow+'].subledgerCode').value =accountCode;
	if(accountCode != 'Select'){
		var url = '/EGF/voucher/common-getDetailType.action?accountCode='+accountCode+'&index=0';
		var transaction = YAHOO.util.Connect.asyncRequest('POST', url, postType, null);
	}else{
			var d = document.getElementById('transactionSummaryList['+$currRow+'].accountdetailtype.id');
			d.options.length=1;
			d.options[0].text='Select';
			d.options[0].value='';
	}

	
	$currRow=getRowIndex(obj);
	var funcObj = document.getElementById('transactionSummaryList['+$currRow+'].functionDetail');
	//fillNeibrAfterSplitFunction(funcObj);
	
}

function getRowIndex(obj)
{
	var temp =obj.name.split('[');
	var temp1 = temp[1].split(']');
	return temp1[0];
}

function check(){
	var accountCodes=new Array();
	for(var i=0;i<billDetailTableIndex+1;i++){
	if(null != document.getElementById('transactionSummaryList['+i+'].glcodeDetail')){
		accountCodes[i] = document.getElementById('transactionSummaryList['+i+'].glcodeDetail').value;
	}
	}
	var url = '/EGF/voucher/common-getDetailCode.action?accountCodes='+accountCodes;
	var transaction = YAHOO.util.Connect.asyncRequest('POST', url, callbackJV, null);
}

var callbackJV = {
		success: function(o) {
				var test= o.responseText;
				test = test.split('~');
				for (var j=0; j<slDetailTableIndex;j++ )
				{
					
					if(null != document.getElementById('transactionSummaryList['+j+'].glcode.id')&& test.length >1 )
					{
						d=document.getElementById('transactionSummaryList['+j+'].glcode.id');
						d.options.length=((test.length)/2)+1;
						for (var i=1; i<((test.length)/2)+1;i++ )
						{
							d.options[i].text=test[i*2-2];
							d.options[i].value=test[i*2 -1];
							
						}
					} 
					if(test.length<2)
					{
						var d = document.getElementById('transactionSummaryList['+j+'].glcode.id');
						if(d)
						{
						d.options.length=1;
						d.options[0].text='Select';
						d.options[0].value='';
						}
					}
				}
					
		    },
		    failure: function(o) {
		    	alert('failure');
		    }
		}

var postType = {
		success: function(o) {
				var detailType= o.responseText;
				var detailRecord = detailType.split('#');
				var eachItem;
				var obj;
				for(var i=0;i<detailRecord.length;i++)
				{
					eachItem =detailRecord[i].split('~');
					if(obj==null)
					{
						obj = document.getElementById('transactionSummaryList['+$currRow+']'+'.accountdetailtype.id');
						if(obj!=null)
							obj.options.length=detailRecord.length+1;
					}
					if(obj!=null)
					{
						obj.options[i+1].text=eachItem[1];
						obj.options[i+1].value=eachItem[2];
						//document.getElementById('subLedgerlist['+parseInt(eachItem[0])+']'+'.detailTypeName').value = eachItem[1];
					}
					
					if(eachItem.length==1) // for deselect the subledger code
					{
						var d = document.getElementById('transactionSummaryList['+$currRow+'].accountdetailtype.id');
						d.options.length=1;
						d.options[0].text='Select';
						d.options[0].value='';
					}
				} 
		    },
		    failure: function(o) {
		    	alert('failure');
		    }
		}

function splitEntitiesDetailCode(obj)
{
	$currRow=getRowIndex(obj);
	var entity=obj.value;
	if(entity.trim()!="")
	{
		var entity_array=entity.trim().split("`~`");

		if(entity_array.length==2)
		{
			document.getElementById(TRANSACTIONSUMMARYLIST+'['+$currRow+']'+'.accountdetailkeyValue').value=entity_array[0].split("`-`")[0];
			document.getElementById(TRANSACTIONSUMMARYLIST+'['+$currRow+']'+'.accountdetailkey').value=entity_array[1];
			//document.getElementById(TRANSACTIONSUMMARYLIST+'['+$currRow+']'+'.detailKey').value=entity_array[0].split("`-`")[1];
		}
	}
	
	var glcodeid = document.getElementById(TRANSACTIONSUMMARYLIST+'['+$currRow+']'+'.glcodeid.id').value;
	
	var accountdetailtypeid = document.getElementById(TRANSACTIONSUMMARYLIST+'['+$currRow+']'+'.accountdetailtype.id').value;
	var accountdetailkey = document.getElementById(TRANSACTIONSUMMARYLIST+'['+$currRow+']'+'.accountdetailkey').value;
	var accountdetailkeyValue = document.getElementById(TRANSACTIONSUMMARYLIST+'['+$currRow+']'+'.accountdetailkeyValue').value;

	if(glcodeid != '' && accountdetailtypeid != '' && accountdetailkey != '' && accountdetailkeyValue != '') {
    	$.ajax(
    	    {
    	        url : 'ajax/getTransactionSummary',
    	        type: "get",
    	        data : {glcodeid : glcodeid,
    	        	accountdetailtypeid : accountdetailtypeid.trim(),
    	        	accountdetailkey : accountdetailkey
    	        	},
    	        success:function(data, textStatus, jqXHR) 
    	        {
    	        	if(data.length != 0)
    	        		alert("Transaction Summary for this combination already exists");
    	        },
    	        error: function(jqXHR, textStatus, errorThrown) 
    	        {
    	            //alert("Error validating duplicate Transaction");
    	        }
    	    });
	}
}

var oAutoCompEntityForJV;
function autocompleteEntities(obj)
{
	 oACDS = new YAHOO.widget.DS_XHR("/EGF/voucher/common-ajaxLoadEntitesBy20.action", [ "~^"]);
	   oACDS.responseType = YAHOO.widget.DS_XHR.TYPE_FLAT;
	   oACDS.scriptQueryParam = "startsWith";
	 //alert(obj.name);
	   if (oAutoCompEntityForJV!= undefined) {
		   oAutoCompEntityForJV.destroy();
		   oAutoCompEntityForJV = null;
	   } 
	   
	   oAutoCompEntityForJV = new YAHOO.widget.AutoComplete(obj.name,'codescontainer',oACDS);
	   oAutoCompEntityForJV.doBeforeSendQuery = function(sQuery){
		   loadWaitingImage(); 
		   var detailTypeName=obj.name.replace('accountdetailkeyValue','accountdetailtype.id');
		   return sQuery+"&accountDetailType="+document.getElementById(detailTypeName).value;
	   } 
	   oAutoCompEntityForJV.queryDelay = 0.5;
	   oAutoCompEntityForJV.minQueryLength = 1;
	   oAutoCompEntityForJV.prehighlightClassName = "yui-ac-prehighlight";
	   oAutoCompEntityForJV.useShadow = true;
	   oAutoCompEntityForJV.forceSelection = true;
	   oAutoCompEntityForJV.maxResultsDisplayed = 10;
	   oAutoCompEntityForJV.useIFrame = true;
	   oAutoCompEntityForJV.doBeforeExpandContainer = function(oTextbox, oContainer, sQDetauery, aResults) {
		   clearWaitingImage();
	           var pos = YAHOO.util.Dom.getXY(oTextbox);
	           pos[1] += YAHOO.util.Dom.get(oTextbox).offsetHeight + 6;
	           oContainer.style.width=100;
	           YAHOO.util.Dom.setXY(oContainer,pos);
	           return true;
	   };
}

function onFocusDetailCode(obj){
	$currRow=getRowIndex(obj);
	var detailtypeidObj=document.getElementById('transactionSummaryList['+$currRow+'].accountdetailtype.id');
	if(detailTypeId != detailtypeidObj.value){
		detailTypeId = detailtypeidObj.value;
		loadDropDownCodesForEntities(detailtypeidObj); 
	}
}

function loadDropDownCodesForEntities(obj)
{
	if(entities)
	{
	entities=null;
	if(oAutoCompEntity)
		oAutoCompEntity.destroy();
	}	
	var	url = "/EGF/voucher/common-ajaxLoadEntites.action?accountDetailType="+obj.value;
	var req2 = initiateRequest();
	req2.onreadystatechange = function()
	{
	  if (req2.readyState == 4)
	  {
		  if (req2.status == 200)
		  {
			var entity=req2.responseText;
			
			var a = entity.split("^");
			var eachEntity = a[0];
			entitiesArray=eachEntity.split("+");
			//alert(":"+entitiesArray[0]+":");
			entities = new YAHOO.widget.DS_JSArray(entitiesArray);
		  }
	  }
 	};
	req2.open("GET", url, true);
	req2.send(null);
}

$('body').on('click', '#add-row', function() {
	var rowCount = $('#result tr').length - 1;
	var content= $('#resultrow0').html();
	resultContent=   content.replace(/0/g,rowCount);   
	$(resultContent).find("input").val("");
	$('#result > tbody:last').append("<tr>"+resultContent+"</tr>"); 
	$('#result tr:last').find("input").val("");
	var obj = document.getElementById('transactionSummaryList['+rowCount+'].accountdetailtype.id');
	$(obj).html('');
	$(obj).append($("<option></option>").attr("value",'').text('---Select---'));
	document.getElementById('transactionSummaryList['+rowCount+'].accounthead').innerHTML = '';
	$("#result tr:last #remove-row").show();
});

$('body').on('click', '#remove-row', function() {

	var count = $("#result > tbody tr").length;
	if(count > 1){
		var obj = $(this).closest("tr").get();
	    var id = $(obj).children(':first-child').val();
	    if(id != '') {
	    	$.ajax(
	    	    {
	    	        url : 'ajax/deleteTransaction',
	    	        type: "get",
	    	        data : {id : id},
	    	        success:function(data, textStatus, jqXHR) 
	    	        {
	    	        	//alert("Success deleted data")
	    	        },
	    	        error: function(jqXHR, textStatus, errorThrown) 
	    	        {
	    	            alert("Error deleting data");
	    	        }
	    	    });
		}
	    
	    $(obj).remove();
	}
	else
		alert("Can not remove this row");
	    return false;
});

$('#search').click(function(e) {
	if(validSearch()) {
		$.ajax(
			    {
			        url : 'ajax/searchTransactionSummaries',
			        type: "get",
			        data : {
			        	finYear : document.getElementById('financialyear.id').value,
			        	fund : document.getElementById('fund.id').value,
			        	functn : document.getElementById('functionid.id').value,
			        	department : document.getElementById('departmentid.id').value,
					},
			        success:function(data, textStatus, jqXHR) 
			        {
				        if(data.length != 0) {
				        	if(data[0].transactionSummary.fundsource != null)
				        		document.getElementById('fundsource.id').value = data[0].transactionSummary.fundsource.id;
					        if(data[0].transactionSummary.divisionid != null)
				        		document.getElementById('divisionid').value = data[0].transactionSummary.divisionid;
					        if(data[0].transactionSummary.functionaryid != null)
				        		document.getElementById('functionaryid.id').value = data[0].transactionSummary.functionaryid.id;
					    }
			        	$.each(data, function(index) {
			                document.getElementById('transactionSummaryList['+index+'].id').value = data[index].transactionSummary.id;
			                document.getElementById('transactionSummaryList['+index+'].glcodeid.id').value = data[index].transactionSummary.glcodeid.id;
			                document.getElementById('transactionSummaryList['+index+'].glcodeDetail').value = data[index].transactionSummary.glcodeid.glcode;
			                var obj = document.getElementById('transactionSummaryList['+index+'].accountdetailtype.id');
			                $(obj).html('');
			                var output = '<option>Select</option>';
							$.each(data[index].accountdetailtypes, function(count, value) {
								output += '<option value="'+value.id+'">'+value.name+'</option>';   
							});
							$(obj).append(output);
			  			  
			                document.getElementById('transactionSummaryList['+index+'].accounthead').innerHTML = data[index].transactionSummary.glcodeid.name;
			                document.getElementById('transactionSummaryList['+index+'].accountdetailkey').value = data[index].transactionSummary.accountdetailkey;
			                document.getElementById('transactionSummaryList['+index+'].accountdetailkeyValue').value = data[index].entityCode;
			                document.getElementById('transactionSummaryList['+index+'].openingdebitbalance').value = data[index].transactionSummary.openingdebitbalance;
			                document.getElementById('transactionSummaryList['+index+'].openingcreditbalance').value = data[index].transactionSummary.openingcreditbalance;
			                document.getElementById('transactionSummaryList['+index+'].narration').value = data[index].transactionSummary.narration;
			                var rowCount = $('#result tr').length - 1;
			                if(index < data.length - 1 && rowCount < data.length){
				                $('#add-row').trigger('click');
				            }
			                document.getElementById('transactionSummaryList['+index+'].accountdetailtype.id').value = data[index].transactionSummary.accountdetailtype.id;
			            });
			        },
			        error: function(jqXHR, textStatus, errorThrown) 
			        {
			            alert("Error searching data");
			        }
			    });
	}
	else {
		alert("Please select all mandatory fields");
	}
});

$('#buttonSubmit').click(function(e) {
	if (validateInput()) {
		var postData = $("#transactionSummaryform").serializeArray();
	    var formURL = $("#transactionSummaryform").attr("action");
	    $.ajax(
	    {
	        url : formURL,
	        type: "post",
	        data : postData,
	        success:function(data, textStatus, jqXHR) 
	        {
	        	document.getElementById('errors').innerHTML = 'Data Saved Successfully';
	        	$.each(data, function(index) {
	                var obj = $("#result tbody tr").get(index);
	                $(obj).children(':first-child').val(data[index].id);
	            });
	        },
	        error: function(jqXHR, textStatus, errorThrown) 
	        {
	            alert("Error saving data");
	        }
	    });
	    e.preventDefault(); //STOP default action
	} else {
		window.scrollTo(0,0);
		e.preventDefault();
	}
});


function validateInput(){ 
	var flag = true;
	var elems = document.getElementsByClassName("mandatory");
	for (var i=0; i<elems.length; i++) {
		if(elems[i].id != '') {
		var val = document.getElementById(elems[i].id).value;
		 if (val == null || val == ''){
			 document.getElementById('errors').innerHTML = 'Please check * marked fields are mandatory';
			 flag = false;
		 }
		}
	}

	var elems1 = document.getElementsByClassName("mandatoryField");
	var debit = document.getElementById(elems1[0].id).value;
	var credit = document.getElementById(elems1[1].id).value;
	if(debit == '' && credit == '') {
		document.getElementById('errors').innerHTML = 'Please check * marked fields are mandatory';
		flag = false;
	}
	
	return flag;
}

function validSearch() {
	var finYear = document.getElementById('financialyear.id').value;
	var fund = document.getElementById('fund.id').value;
	var functn = document.getElementById('functionid.id').value;
	var department = document.getElementById('departmentid.id').value;
	var flag = true;

	
	if (finYear == '' || fund == ''
				|| functn == '' || department == '')
		flag = false;

	return flag;
}

	$('#type').change(
			function() {
				loadDropDownCodes();
				$.ajax({
					method : "GET",
					url : "ajax/getMajorHeads",
					data : {
						type : $('#type').val()
					},
					async : true
				}).done(
						function(msg) {
							$('#major').empty();
							$('#minor').empty();
							var output = '<option value="">Select</option>';
							$.each(msg, function(index, value) {
								output += '<option value='+value.majorCode+'>'
										+ value.majorCode + ' - ' + value.name
										+ '</option>';
							});
							$('#major').append(output);
						});
				$('#major').trigger('change');
			});

	$('#major').change(
			
			function() {
				//loadDropDownCodes();
				$.ajax({
					method : "GET",
					url : "ajax/getMinorHeads",
					data : {
						majorCode : $('#major').val(),
						classification : 2
					},
					async : true
				}).done(
						function(msg) {
							$('#minor').empty();
							var output = '<option value="">Select</option>';
							$.each(msg, function(index, value) {
								output += '<option value='+value.glcode+'>'+value.glcode + ' - '+ value.name + '</option>';
							});
							$('#minor').append(output);
						});
			});
	
		$('#major').change(
			
			function() {
				//loadDropDownCodes();
			});

function makeOtherReadonly(obj) {
	var id = $(obj).attr('id');
	var rowCount = getRowIndex(obj);
	var val = $(obj).val();
	if(val != '') {
		if(id.indexOf("openingdebitbalance") > -1) {
			document.getElementById('transactionSummaryList['+rowCount+'].openingcreditbalance').readOnly = true;
		}
		else {
			document.getElementById('transactionSummaryList['+rowCount+'].openingdebitbalance').readOnly = true;
		}
	}
	else {
		document.getElementById('transactionSummaryList['+rowCount+'].openingcreditbalance').readOnly = false;
		document.getElementById('transactionSummaryList['+rowCount+'].openingdebitbalance').readOnly = false;
	}
}