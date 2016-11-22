/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
var contractorMsArray=new Array(200);
var addItemsArray = new Array(200);
var headstart="<!--only for validity head start -->";
var headend="<!--only for validity head end -->";
var tailstart="<!--only for validity tail start -->";
var tailend="<!--only for validity tail end -->";
function addMBMSheet(obj)
{
	var rowid=obj.id;
	sorId=rowid.split(".");
	var	sortable=sorId[0];

	var msfieldsName=rowid.replace("msadd","measurementSheets");
	var   mscontent=document.getElementById(rowid.replace("msadd","mstd")).innerHTML;

	var   msopen=document.getElementById(rowid.replace("msadd","msopen")).value;
	if(msopen==1)
		return ;

	if(mscontent!='')
	{
		if(mscontent.indexOf(headstart) >=0)
		  {
		  var head= mscontent.substring(mscontent.indexOf(headstart),mscontent.indexOf(headend));
		  var tail= mscontent.substring(mscontent.indexOf(tailstart),mscontent.indexOf(tailend));
		  mscontent= mscontent.replace(head,"");
		  mscontent= mscontent.replace(tail,"");
		  }
		
		var curRow = $(obj).closest('tr');
		var k= "<tr id=\""+sortable+".mstr\" class=\"msheet-tr\"><td colspan=\"15\">";
		mscontent=k+mscontent+"</td></tr>";
		curRow.after(mscontent);
		document.getElementById(rowid.replace("msadd","mstd")).innerHTML="";
		document.getElementById(rowid.replace("msadd","msopen")).value="1";
		var idx=sortable.substr(sortable.indexOf("["),sortable.indexOf("]"));
		
		if(sortable.indexOf("contractorMBDetails") >= 0)
		{
			contractorMsArray[idx]=mscontent;
		} else if(sortable.indexOf("additionalMBDetails") >= 0) {
			addItemsArray[idx]=mscontent;
		}
	}else
	{
		var curRow = $(obj).closest('tr');
		var newrow= $('#msheaderrowtemplate').html();

		newrow=  newrow.replace(/msrowtemplate/g,'msrow'+sortable);
		if(sortable.indexOf("additionalMBDetails") >= 0)
			newrow=  newrow.replace(/templatesorActivities\[0\]/g,sortable);
		else
			newrow=  newrow.replace(/templatesorMbDetails\[0\]/g,sortable);
		newrow=  newrow.replace(/measurementSheetList/g,"measurementSheets");
		document.getElementById(rowid.replace("msadd","msopen")).value="1";
		document.getElementById(rowid.replace("msadd","mspresent")).value="1";
		curRow.after(newrow);
		var idx=sortable.substr(sortable.indexOf("["),sortable.indexOf("]"));
		if(sortable.indexOf("contractorMBDetails") >= 0)
		{
			contractorMsArray[idx]="";
		} else if(sortable.indexOf("additionalMBDetails") >= 0) {
			addItemsArray[idx]=mscontent;
		}

	}
	patternvalidation();
}

$('#addnonSorRow').click(function() {
	if(ismsheetOpen())
	{
		bootbox.alert("Measurement Sheet is open Please close it first");
		return ;
	}
	var hiddenRowCount = $("#tblNonSor tbody tr:hidden[id='nonSorRow']").length;
	if(hiddenRowCount == 0) {
		var key = $("#tblNonSor tbody tr:visible[id='nonSorRow']").length;
		addRow('tblNonSor', 'nonSorRow');
		resetIndexes();
		$('#activityid_' + key).val('');
		$('#nonSorId_' + key).val('');
		$('#nonSorDesc_' + key).val('');
		$('#nonSorUom_' + key).val('');
		$('#nonSorRate_' + key).val('');
		$('#nonSorQuantity_' + key).val('');
		$('#nonSorQuantity_' + key).removeAttr('readonly');
		$('.nonSorAmount_' + key).html('');
		$('#nonSorServiceTaxPerc_' + key).val('');
		$('.nonSorVatAmt_' + key).html('');
		$('.nonSorTotal_' + key).html('');
		if(document.getElementById('additionalMBDetails['+key+'].mstd'))
			document.getElementById('additionalMBDetails['+key+'].mstd').innerHTML=""; 
		if(document.getElementById('additionalMBDetails['+key+'].mspresent'))
			document.getElementById('additionalMBDetails['+key+'].mspresent').value="0"; 

		generateSlno();
	} else {
		var key = 0;
		$('#nonSorDesc_' + key).attr('required', 'required');
		$('#nonSorUom_' + key).attr('required', 'required');
		$('#nonSorRate_' + key).attr('required', 'required');
		$('#nonSorEstimateRate_' + key).attr('required', 'required');
		$('#nonSorQuantity_' + key).attr('required', 'required');
		$('#nonSorQuantity_' + key).removeAttr('readonly');
		$('.nonSorRate').val('');
		$('.nonSorQuantity').val('');
		$('.nonSorServiceTaxPerc').val('');
		$('#nonSorMessage').attr('hidden', 'true');
		$('#nonSorRow').removeAttr('hidden');
		$('#nonSorRow').removeAttr('nonsorinvisible');
		if(document.getElementById('additionalMBDetails['+key+'].mstd'))
			document.getElementById('additionalMBDetails['+key+'].mstd').innerHTML=""; 
		if(document.getElementById('additionalMBDetails['+key+'].mspresent'))
			document.getElementById('additionalMBDetails['+key+'].mspresent').value="0"; 
	}
});

function addRow(tableName,rowName) {
	if (document.getElementById(rowName) != null) {
		// get Next Row Index to Generate
		var nextIdx = 0;
		var sno = 1;
		var isValid=1;//for default have success value 0  
		//nextIdx =document.getElementsByName("sorRow").length;
		nextIdx = jQuery("#"+tableName+" > tbody > tr").length-1;
		 
		// Generate all textboxes Id and name with new index
		var $row;
		if (tableName.indexOf("overheadTable") >= 0) {
			$row = jQuery("#" + tableName + " tr:eq(1)").clone();
			nextIdx = nextIdx + 1;
		} else if (tableName.indexOf("deductionTable") >= 0) {
			$row = jQuery("#" + tableName + " tr:eq(1)").clone();
			nextIdx = nextIdx + 1;
		} else {
			var $row = jQuery("#" + tableName + " tr:eq(2)").clone();
		}

		$row.find("a,input,select, errors,button, span,textarea").each(function() {
			var classval = jQuery(this).attr('class');
			if (jQuery(this).data('server')) {
				jQuery(this).removeAttr('data-server');
			}
			if(classval == 'spansorslno') {
				jQuery(this).text(nextIdx+1);
			}
			
			if(classval == 'spansno') {
				jQuery(this).text(nextIdx+1);
			}

			if(classval == 'assetdetail' || classval == 'viewAsset') {
				$(this).html('');
				$(this).val(''); 
				jQuery(this).text('');
			} 
			jQuery(this).attr(
					{
						'name' : function(_, name) {
							if(!(jQuery(this).attr('name')===undefined))
								return name.replace(/\d+/, nextIdx); 
						},
						'id' : function(_, id) {
							if(!(jQuery(this).attr('id')===undefined))
								return id.replace(/\d+/, nextIdx); 
						},
						'class' : function(_, name) {
							if(!(jQuery(this).attr('class')===undefined))
								return name.replace(/\d+/, nextIdx); 
						},
						'data-idx' : function(_,dataIdx)
						{
							return nextIdx;
						}
					});
			// if element is static attribute hold values for next row, otherwise it will be reset
			if (!jQuery(this).data('static')) {
				jQuery(this).val('');
			}

		}).end().appendTo("#"+tableName+" > tbody");	
		sno++;
	}
}

function deleteNonSor(obj) {
	var rIndex = getRow(obj).rowIndex;

	var id = $(getRow(obj)).children('td:first').children('input:first').val();
	//To get all the deleted rows id
	var aIndex = rIndex - 1;
	if(!$("#removedActivityIds").val()==""){
		$("#removedActivityIds").val($("#removedActivityIds").val()+",");
	}
	$("#removedActivityIds").val($("#removedActivityIds").val()+id);

	var rowId = $(obj).attr('class').split('_').pop();
	var rowcount=$("#tblNonSor > tbody > tr").length;

	if(rowcount==2) {
		$('#activityid_' + rowId).val('');
		$('#nonSorId_' + rowId).val('');
		$('#nonSorId_' + rowId).val('');
		$('#nonSorDesc_' + rowId).val('');
		$('#nonSorUom_' + rowId).val('');
		$('#nonSorEstimateRate_' + rowId).val('');
		$('#nonSorRate_' + rowId).val('');
		$('#nonSorQuantity_' + rowId).val('');
		$('#nonSorQuantity_' + rowId).removeAttr('required');
		$('#nonSorDesc_' + rowId).removeAttr('required');
		$('#nonSorUom_' + rowId).removeAttr('required');
		$('#nonSorRate_' + rowId).removeAttr('required');
		$('.nonSorAmount_' + rowId).html('');
		$('#nonSorServiceTaxPerc_' + rowId).val('');
		$('.nonSorVatAmount_' + rowId).html('');
		$('.nonSorTotal_' + rowId).html('');
		$('#nonSorRow').attr('hidden', 'true');
		$('#nonSorRow').attr('nonsorinvisible', 'true');
		$('#nonSorMessage').removeAttr('hidden');
	} else {
		deleteRow('tblNonSor',obj);
	}
	resetIndexes();
	//starting index for table fields
	generateSlno();

	calculateNonSorEstimateAmountTotal();
	return true;
}

function deleteRow(tableName,obj){
	if(ismsheetOpen())
	{
		bootbox.alert("Measurement Sheet is open Please close it first");
		return ;
	}
	var rIndex = getRow(obj).rowIndex;
	var id = jQuery(getRow(obj)).children('td:first').children('input:first').val();
	//To get all the deleted rows id
	var aIndex = rIndex - 1;
	var tbl=document.getElementById(tableName);	
	var rowcount=jQuery("#"+tableName+" > tbody > tr").length;
	if(rowcount<=1) {
		bootbox.alert("This row can not be deleted");
		return false;
	} else {
		tbl.deleteRow(rIndex);
		//starting index for table fields
		var idx= 0;
		var sno = 1;
		//regenerate index existing inputs in table row
		jQuery("#"+tableName+" > tbody > tr").each(function() {
			if( tableName=="tblNonSor")
			{
				jQuery(this).find("input, select,textarea,td,tbody,tr,table, errors, span, input:hidden").each(function() {
					var classval = jQuery(this).attr('class');

					if(classval == 'spansno') {
						jQuery(this).text(sno);
						sno++;
					} else {
						jQuery(this).attr({
							'name': function(_, name) {
								if(!(jQuery(this).attr('name')===undefined))
									return name.replace(/nonSorActivities\[.\]/g, "nonSorActivities["+idx+"]"); 
							},
							'id': function(_, id) {
								if(!(jQuery(this).attr('id')===undefined))
									return id.replace(/nonSorActivities\[.\]/g, "nonSorActivities["+idx+"]"); 
							},
							'class' : function(_, name) {
								if(!(jQuery(this).attr('class')===undefined))
									return name.replace(/nonSorActivities\[.\]/g, "nonSorActivities["+idx+"]"); 
							},
							'data-idx' : function(_,dataIdx)
							{
								if(!(jQuery(this).attr('data-idx')===undefined))
									return idx;
							}
						});
					}
				});

				idx++;
			}
		});
		return true;
	}
}

$(document).on('click','.add-msrow',function () {
	var len=$(this).closest('table').find('tr').length;
	var msrowname= $(this).closest('table').attr('id');
	len=len-2;
	var msrownameid=msrowname.split(".")[0];
	var rep='measurementSheets\['+len+'\]';

	var $newrow= "<tr>"+$('#msrowtemplate').html()+"</tr>";
	$newrow=  $newrow.replace(/templatesorActivities\[0\]/g,msrownameid);
	$newrow=  $newrow.replace(/measurementSheetList\[0\]/g,rep);
	$newrow=$newrow.replace('value="1"','value="'+(len+1)+'"');
	$(this).closest('tr').before($newrow);

	patternvalidation();
})

function  deleteThisRow(obj) {
	var rIndex = getRow(obj).rowIndex;
	var tablename=$(obj).closest('table').attr('id');
	var tbl=document.getElementById( tablename);
	var rowcount=$(obj).closest('table').find('tr').length;
	if(rowcount<=3) {
		
		var retVal = confirm("This action will remove complete Measurement Sheet for SOR/NonSOR. Do you want to continue ?");
		if( retVal == false )
		{
			return ;
		}
		else{
	   var sid=	tablename.split(".")[0];	
	   var mstr=document.getElementById(sid+".msopen").value=0;
	   var mstr=document.getElementById(sid+".mspresent").value=0;
	   var mstr=document.getElementById(sid+".mstd").innerHTML="";
	   document.getElementsByName(sid+".quantity")[0].value=0;
	   var quantity=document.getElementsByName(sid+".quantity")[0];
	   $(quantity).removeAttr("readonly");
	   var mstr=document.getElementById(sid+".mstr");
	   $(mstr).remove();
			calculateNonSorEstimateAmount(document.getElementsByName(sid+".quantity")[0]);
		}
		return ;
	} else {
		tbl.deleteRow(rIndex);
	}
	reindex(tablename);
	findNet(tbl);  

}

function resetIndexes() {
	var idx = 0;
	//regenerate index existing inputs in table row
	$(".nonSorRow").each(function() {
		$(this).find("input,button, select,textarea,td,tbody,tr,table, errors, span, input:hidden").each(function() {

			if (!$(this).is('span')) {
				$(this).attr({
					'name' : function(_, name) {
						if(name)
							return name.replace(/nonSorActivities\[.\]/g, "nonSorActivities["+idx+"]");
					},
					'id' : function(_, id) {
						if(id)
							return id.replace(/nonSorActivities\[.\]/g, "nonSorActivities["+idx+"]");
					},
					'data-idx' : function(_, dataIdx) {
						return idx;
					}
				});
			} else {
				$(this).attr({
					'class' : function(_, name) {
						if(name)
							return name.replace(/nonSorActivities\[.\]/g, "nonSorActivities["+idx+"]");
					},
					'id' : function(_, id) {
						if(id)
							{	
							id= id.replace(/nonSorActivities\[.\]/g, "nonSorActivities["+idx+"]");
							return id.replace(/_\d+/,"_"+idx);
								}
					}
				});
			}
		});
		idx++;
	});
}

function generateSlno()
{
	var idx=1;
	$(".spannonsorslno").each(function(){
		$(this).text(idx);
		idx++;
	});
}

$(document).on('click','.hide-ms',function () {

	var sid=$(this).closest('tr').attr("id");
	var name=	sid.split(".")[0]
	var idx=name.substr(name.indexOf("["),name.indexOf("]"));
	if(sid.split(".")[0].indexOf("contractorMBDetails") >= 0)
	{
		document.getElementById(sid.split(".")[0]+".mstd").innerHTML=contractorMsArray[idx];
		if(contractorMsArray[idx].length==0)
			document.getElementById(sid.split(".")[0]+".mspresent").value="0";
	} else if (sid.split(".")[0].indexOf("additionalMBDetails") >= 0) {
		document.getElementById(sid.split(".")[0]+".mstd").innerHTML=addItemsArray[idx];
		if(addItemsArray[idx].length==0)
			document.getElementById(sid.split(".")[0]+".mspresent").value="0";
	}

	document.getElementById(sid.split(".")[0]+".msopen").value="0";
	
	var mstr=document.getElementById(sid.split(".")[0]+".mstr");
	$(mstr).remove();
});

$(document).on('change','.runtime-update',function (e) {
	if($(this).is("input"))
	{
		if($(this).val()==0)
			{
			alert("Zero is not allowed");
			$(this).val('');
			}
		$(this).attr('value', $(this).val());
	}
	else if($(this).is("select"))
	{
		if($(this).val()=='A')
			{
			$(this).find('option[value="D"]').removeAttr('selected');	
			$(this).find('option[value="A"]').attr('selected', 'selected');
			}else
				{
				$(this).find('option[value="A"]').removeAttr('selected');	
				$(this).find('option[value="D"]').attr('selected', 'selected');	
				}
		
	}
	else if($(this).is("textarea"))
	{
		$(this).html($(this).val());
	}
	if($(this).attr('id').indexOf("quantity")>=0)
		findNet(this);
	else
		findTotal(this);
});

$(document).on('click','.ms-submit',function () {

	var sid=$(this).attr("id");
	var mscontent="<tr id=\""+sid.split(".")[0]+".mstr\">";

	var net=eval(document.getElementById(sid.split(".")[0]+".msnet").innerHTML);
	var qobj1=sid.split(".")[0];
	if(!validateMsheet(qobj1))
	{
		return false;
	}

	document.getElementsByName(sid.split(".")[0]+".quantity")[0].value=document.getElementById(sid.split(".")[0]+".msnet").innerHTML;
	mscontent=document.getElementById(sid.split(".")[0]+".mstr").innerHTML;
	document.getElementById(sid.split(".")[0]+".mstr")
	document.getElementById(sid.split(".")[0]+".mstd")
	document.getElementById(sid.split(".")[0]+".mstd").innerHTML=mscontent;
	document.getElementById(sid.split(".")[0]+".msopen").value="0";
	var mstr=document.getElementById(sid.split(".")[0]+".mstr");
	$(mstr).remove();
	var qobj=document.getElementsByName(sid.split(".")[0]+".quantity")[0];
	if(sid.split(".")[0].indexOf("contractorMBDetails") >= 0)
	{
		calculateActivityAmounts(document.getElementsByName(sid.split(".")[0]+".quantity")[0]);
	} else if (sid.split(".")[0].indexOf("additionalMBDetails") >= 0) {
		calculateNonSorEstimateAmount(document.getElementsByName(sid.split(".")[0]+".quantity")[0]);
	}
});

function reindex(tableId)
{
	var idx=0;
	tbl=document.getElementById(tableId);

	$(tbl).find("tbody tr").each(function(e) {

		$(this).find("input,select,textarea").each(function() {
			var classval = jQuery(this).attr('class');	
			if(classval.indexOf("spanslno") > -1) {
				jQuery(this).val(idx+1);
			}

			$(this).attr({
				'name' : function(_, name) {
					if(name)
						return name.replace(/measurementSheets\[.\]/g, "measurementSheets["+idx+"]");
				},
				'id' : function(_, id) {
					if(id)
						return id.replace(/measurementSheets\[.\]/g, "measurementSheets["+idx+"]");
				},
				'data-idx' : function(_, dataIdx) {
					return idx;
				}
			});

		});
		idx++;
	});
}

$(document).on('click','.reset-ms',function () {
	$(this).closest('table').find('.runtime-update:not(select):not(:read-only)').val("");
	$(this).closest('table').find('.runtime-update:not(select):not(:read-only)').attr('value', '');
	$(this).closest('table').find('textarea').html('');
	$(this).closest('table').find('.runtime-update:last').trigger('change');
});

function findTotal(obj)
{
	var name=obj.name.split(".");
	var lengthname=name[0]+'.'+name[1]+'.length';
	var no1,depthOrHeight1,width1,length1;
	var lent=$('input[id="'+lengthname+'"]');
	var length=$(lent).attr('value');
	var no=$('input[id="'+name[0]+'.'+name[1]+'.no'+'"]').attr('value');
	var depthOrHeight=$('input[id="'+name[0]+'.'+name[1]+'.depthOrHeight'+'"]').attr('value');
	var width=$('input[id="'+name[0]+'.'+name[1]+'.width'+'"]').attr('value');

	if(isEmpty(length) && isEmpty(no) && isEmpty(depthOrHeight)  && isEmpty(width))
		$('input[id="'+name[0]+'.'+name[1]+'.quantity'+'"]').attr('value',0);
	else {
		if (length === undefined || length == '' || length == 0)
			length = 1;
		if (no === undefined || no == '' || no == 0)
			no = 1;
		if (depthOrHeight === undefined || depthOrHeight == '' || depthOrHeight == 0)
			depthOrHeight = 1;
		if (width === undefined || width == '' || width == 0)
			width = 1;
		var net=parseFloat(length * no * width * depthOrHeight).toFixed(4);

		document.getElementById(name[0]+'.'+name[1]+'.quantity').value=net;
		$('input[id="'+name[0]+'.'+name[1]+'.quantity'+'"]').attr('value',net);

	}
	var netObj=document.getElementById(name[0]+'.'+name[1]+'.quantity');
	$(netObj).attr('value', document.getElementById(name[0] + '.' + name[1] + '.quantity').value);
	var len=$(obj).closest('table').find('tbody').children.length;
	findNet(netObj);
}

function isEmpty(str)
{
	if(!str)
	{
		return true;
	}
	else if(!str.trim()) {
		return true;
	}

	return false;
}


function findNet(obj)
{
	var len=$(obj).closest('tbody').find('tr').length;
	
	var name=obj.id.split(".");
	
	var index = name[0].split('[')[1].split(']')[0];

	var sum=0;
	for(var i=0;i<len - 1;i++)
	{
		var qname=name[0]+'.measurementSheets['+i+'].quantity';
		var quantity=eval(document.getElementById(qname).value);
		var oname= '';
		var operation= '';
		if (name[0].indexOf("additionalMBDetails") >= 0) {
			oname = 'additionalMBDetails['+ index +'].measurementSheets[' + i + '].identifier';
			var operationObj=document.getElementById(oname);
			var operation=operationObj.options[operationObj.selectedIndex].value;
		} else if (name[0].indexOf("contractorMBDetails") >= 0) {
			oname= '#msrowidentifier_' + index + '_' + i;
			operation=$(oname).html().trim();
		}
		console.log(quantity+"---"+operation);
		if(quantity===undefined)
			quantity=0;
		if(quantity==NaN)
			quantity=0;
		if(quantity=='')
			quantity=0;
		if(operation=='No' || operation=='A')
			sum=sum+quantity;
		else
			sum=sum-quantity;
	}
	var netName=name[0]+'.msnet';
	sum=parseFloat(sum).toFixed(4);
	document.getElementById(netName).innerHTML=sum;
}



function openAllmsheet() {
	$(".openmbsheet:visible").each(function() {
		$(this).trigger('click');
	});
}

function closeAllmsheet() {
	$(".hide-ms:visible").each(function() {
		$(this).trigger('click');
	});
}

function ismsheetOpen()
{
	var open=false;
	$('.classmsopen').each(function (index)
			{

		if($( this ).val()==1)
			open=true
			});
	return open;
}

function validateMsheet(obj)
{
	var len=$(obj).closest('tbody').find('tr').length;
	var sum=0;
	for(var i=0;i<len-1;i++){
		var remarks=document.getElementById(obj+'.measurementSheets['+i+'].remarks').value;
		var no=eval(document.getElementById(obj+'.measurementSheets['+i+'].no').value);
		var lent=eval(document.getElementById(obj+'.measurementSheets['+i+'].length').value);
		var width=eval(document.getElementById(obj+'.measurementSheets['+i+'].width').value);
		var depthorheight=eval(document.getElementById(obj+'.measurementSheets['+i+'].depthOrHeight').value);
		var quantity=eval(document.getElementById(obj+'.measurementSheets['+i+'].quantity').value);

		if ((remarks != '' || no != '' || lent != '' || width != '' || depthorheight != '') && quantity == '') {
			bootbox.alert("Please Enter Quantity");
			return false;
		}
	}
	return true;
}

function limitCharatersBy10_4(object) {
	var valid = /^[0-9](\d{0,9})(\.\d{0,4})?$/.test($(object).val()),
	val = $(object).val();
	if(!valid){
		$(object).val(val.substring(0, val.length - 1));
	}	
}

function limitCharatersBy3_2(object) {
	var valid = /^[0-9](\d{0,2})(\.\d{0,2})?$/.test($(object).val()),
	val = $(object).val();
	if(!valid){
		$(object).val(val.substring(0, val.length - 1));
	}	
}

function calculateNonSorEstimateAmount(currentObj) {
	var rowcount = $(currentObj).attr('id').split('_').pop();
	var description = $('#nonSorDesc_' + rowcount).val();
	var uom = $('#nonSorUom_' + rowcount).val();
	var flag = false;
	if(description == '') {
		bootbox.alert($('#errordescription').val());
		$('#nonSorDesc_' + rowcount).val('');
		flag = true;
	}
	if(!flag && uom == '') {
		bootbox.alert($('#erroruom').val());
		$('#nonSorUom_' + rowcount).val('');
		$('.nonSorAmount_' + rowcount).html('');
		calculateNonSorEstimateAmountTotal();
		flag = true;
	}
	if(!flag) {
		var unitRate = $('#nonSorRate_' + rowcount).val();
		var quantity = $('#nonSorQuantity_' + rowcount).val();
		if(quantity == "")
			quantity = 0.0;
		var amount = parseFloat(parseFloat(quantity) * parseFloat(unitRate)).toFixed(2);
		$('.nonSorAmount_' + rowcount).html(amount);
		$('#hiddenNonSorAmount_' + rowcount).val(amount);
		calculateNonSorEstimateAmountTotal();
	}
}

function calculateNonSorEstimateAmountTotal() {
	var total = 0;
	$('.nonsoramount').each(function() {
		if($(this).html().trim() != "")
			total = parseFloat(parseFloat(total) + parseFloat($(this).html().replace(',', ''))).toFixed(2);
	});
	$('#nonSorEstimateTotal').html(total);
	calculateMBAmount();
}

function getUnitRate(uom,estimateRate){
	var unitRate=0;
	var exceptionalUOMValues = $ExceptionalUOMs.split(':');
	var exceptionalUOMArray = $.makeArray( exceptionalUOMValues );
	$.map( exceptionalUOMArray, function( val, i ) {
		if(val.split(",")[0] == uom)
			unitRate = parseFloat( parseFloat(estimateRate) / parseFloat( val.split(",")[1] ));
	});
	if(unitRate!=0)
		return unitRate;
	else
		return estimateRate;
}

function calculateMBAmount() {
	var sorTotal = $('#mbTotal').html();
	var nonSorTotal = $('#nonSorEstimateTotal').html();
	if(sorTotal == '')
		sorTotal = 0.0;
	if(nonSorTotal == '')
		nonSorTotal = 0.0;
	var mbAmount = parseFloat(parseFloat(sorTotal) + parseFloat(nonSorTotal)).toFixed(2);
	$('#mbAmountTotal').html(mbAmount);
	$('#mbAmount').val(mbAmount);
}