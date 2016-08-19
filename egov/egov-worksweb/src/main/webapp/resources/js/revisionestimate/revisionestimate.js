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
$ExceptionalUOMs = "";
var hint='<a href="#" class="hintanchor" title="@fulldescription@"><i class="fa fa-question-circle" aria-hidden="true"></i></a>';
var nonTenderedMsArray=new Array(200);
var lumpSumMsArray=new Array(200);

var headstart="<!--only for validity head start -->";
var headend="<!--only for validity head end -->";
var tailstart="<!--only for validity tail start -->";
var tailend="<!--only for validity tail end -->";

$isServiceVATRequired = $('#isServiceVATRequired').val();

if($isServiceVATRequired == 'true') {
	//For Non Tendered Screen
	$('#serviceVatHeader').removeAttr('hidden');
	$('#vatAmountHeader').removeAttr('hidden');
	$('.serviceTaxPerc').removeAttr('hidden');
	$('.vatAmount').removeAttr('hidden');
	$('.emptytd').removeAttr('hidden');
	$('.serviceVatAmt').removeAttr('hidden');

	//For lump Sum Screen
	$('#lumpSumServiceVatHeader').removeAttr('hidden');
	$('#lumpSumVatAmountHeader').removeAttr('hidden');
	$('.lumpSumServiceTaxPerc').removeAttr('hidden');
	$('.lumpSumVatAmount').removeAttr('hidden');
	$('.emptytd').removeAttr('hidden');
	$('.lumpSumServiceVatAmt').removeAttr('hidden');
}


$(document).ready(function(){
	$ExceptionalUOMs = $('#exceptionaluoms').val();  
});

function getRow(obj) {
	if(!obj)return null;
	tag = obj.nodeName.toUpperCase();
	while(tag != 'BODY'){
		if (tag == 'TR') return obj;
		obj=obj.parentNode ;
		tag = obj.nodeName.toUpperCase();
	}
	return null;
}

var sorSearch = new Bloodhound({
		datumTokenizer: function (datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer: Bloodhound.tokenizers.whitespace,
		remote: {
			url: '/egworks/abstractestimate/ajaxsor-byschedulecategoriesandestimateid?code=',
			replace: function (url, query) {
				var scheduleCategories = $('#scheduleCategory').val();
				var workOrderDate = $('#workOrderDate').val();
				var estimateId = $('#estimateId').val();
				if(scheduleCategories == null)
					bootbox.alert($('#msgschedulecategory').val());
				if(workOrderDate == "" || workOrderDate == null)
					bootbox.alert($('#msgworkorderdate').val());
				return url + query + '&scheduleCategories=' + scheduleCategories + "&estimateDate=" + workOrderDate+ "&estimateId=" + estimateId;
			},
			filter: function (data) {
				return $.map(data, function (ct) {
					return {
						id: ct.id,
						code: ct.code,
						description: ct.description,
						uom: ct.uom.uom,
						uomid: ct.uom.id,
						estimateRate: parseFloat(ct.sorRate).toFixed(2),
						summary: ct.summary,
						categoryCode: ct.scheduleCategory.code,
						displayResult: ct.code+' : '+ct.summary+' : '+ct.scheduleCategory.code 
					};
				});
			}
		}
	});

	sorSearch.initialize();
	var sorSearch_typeahead = $('#sorSearch').typeahead({
		hint : true,
		highlight : true,
		minLength : 2
	}, {
		displayKey : 'displayResult',
		source : sorSearch.ttAdapter()
	}).on('typeahead:selected', function (event, data) {

		if(ismsheetOpen())
		{
			bootbox.alert("Measurement Sheet is open Please close it first");
			return ;
		}
		var flag = false;
		$('.sorhiddenid').each(function() {
			if($(this).val() == data.id) {
				flag = true;
			}
		});
		if(flag) {
			bootbox.alert($('#erroradded').val(), function() {
				$('#sorSearch').val('');
			});
		}
		else {
			var hiddenRowCount = $("#tblNonTendered > tbody > tr:hidden[id='nonTenderedRow']").length;
			var key = $("#tblNonTendered > tbody > tr:visible[id='nonTenderedRow']").length;
			if(hiddenRowCount == 0) {
				addRow('tblNonTendered', 'nonTenderedRow');
				resetIndexes();
				$('#soractivityid_' + key).val('');
				$('#quantity_' + key).val('');
				$('#quantity_' + key).removeAttr('readonly');
				$('.amount_' + key).html('');
				$('#vat_' + key).val('');
				$('.vatAmount_' + key).html('');
				$('.total_' + key).html('');
				if(document.getElementById('nonTenderedActivities['+key+'].mstd'))
					document.getElementById('nonTenderedActivities['+key+'].mstd').innerHTML=""; 
				if(document.getElementById('nonTenderedActivities['+key+'].mspresent'))
					document.getElementById('nonTenderedActivities['+key+'].mspresent').value="0"; 
				//generateSorSno();
			} else {
				$('#quantity_0').val('');
				$('#quantity_0').removeAttr('readonly');
				$('#quantity_0').attr('required', 'required');
				$('#vat_0').val('');
				key = 0;
				$('#message').attr('hidden', 'true');;
				$('#nonTenderedRow').removeAttr('hidden');
				$('#nonTenderedRow').removeAttr('sorinvisible');
				if(document.getElementById('nonTenderedActivities[0].mstd'))
					document.getElementById('nonTenderedActivities[0].mstd').innerHTML=""; 
				if(document.getElementById('nonTenderedActivities[0].mspresent'))
					document.getElementById('nonTenderedActivities[0].mspresent').value="0"; 
			}

			$.each(data, function(id, val) {
				if(id == "id")
					$('#' + id + "_" + key).val(val);
				else if(id == "uomid")
					$('#sorUomid_' + key).val(val);
				else if(id == 'description') {
					$('.' + id + "_" + key).html(hint.replace(/@fulldescription@/g, val));
				} else if(id == 'estimateRate') {
					if(val != null) {
						$('.' + id + "_" + key).html(val);
						$('#' + id + "_" + key).val(val);
						//$('#rate_' + key).val(val);
					} else {
						$('.' + id + "_" + key).html(0);
						$('#' + id + "_" + key).val(0);
						//$('#rate_' + key).val(0);
					}
				}else
					$('.' + id + "_" + key).html(val);
			});
			$('#rate_' + key).val(getUnitRate($('.uom_' + key).html(),$('#estimateRate_' + key).val()));

		}
		$('#sorSearch').typeahead('val','');
	});

	
	function addRow(tableName,rowName) {


		if (document.getElementById(rowName) != null) {
			// get Next Row Index to Generate
			var nextIdx = 0;
			var sno = 1;
			var isValid=1;//for default have success value 0  
			//nextIdx =document.getElementsByName("sorRow").length;
			nextIdx = jQuery("#"+tableName+" > tbody > tr").length-1;
			 
			//sno = nextIdx;

			//console.log('TABLE ->', tableName);

			//console.log('NEXT IDX ->', nextIdx);

			// Generate all textboxes Id and name with new index
			var $row;
			if(tableName.indexOf("overheadTable")>=0)
				{
				$row=jQuery("#"+tableName+" tr:eq(1)").clone();
				nextIdx=nextIdx+1;
				} else
					{
			      var $row=jQuery("#"+tableName+" tr:eq(2)").clone();
					}

			if(tableName == 'tblassetdetails') {
				//validate existing rows in table
				jQuery("#"+tableName+" tr").find("input:hidden").each(function() {
					if((jQuery(this).data('optional') === 0) && (!jQuery(this).val()))
					{
						jQuery(this).focus();
						bootbox.alert("Please enter value for the row");
						isValid=0;//set validation failure
						return false;
					}
				});

				if (isValid === 0) {
					return false;
				}
				
				$row=jQuery("#"+tableName+" tr:eq(1)").clone();
				nextIdx++;
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

			//console.log($row.html());

			sno++;

		}
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
			if(tableName == 'tblassetdetails') {
				$('a[id="assetcode[0]').html('');
				$('span[id="assetname[0]').html('');
				$('input[name="tempAssetValues[0].asset.code"]').val('');
				$('input[name="tempAssetValues[0].asset.name"]').val('');
				$('input[name="tempAssetValues[0].asset.id"]').val('');
				$('input[name="tempAssetValues[0].id"]').val('');
			}
		  else {
			bootbox.alert("This row can not be deleted");
			return false;
			}
		} else {
			tbl.deleteRow(rIndex);
			//starting index for table fields
			var idx= 0;
			var sno = 1;
			//regenerate index existing inputs in table row
			jQuery("#"+tableName+" > tbody > tr").each(function() {
				if(tableName=='tblNonTendered')
				{
					jQuery(this).find("input,button, select,textarea,td,tbody,table, errors, span, input:hidden").each(function() {
						var classval = jQuery(this).attr('class');

						if(classval == 'spansno') {
							jQuery(this).text(sno);
							sno++;
						} else {
							jQuery(this).attr({
								'name': function(_, name) {
									if(!(jQuery(this).attr('name')===undefined))
										return name.replace(/nonTenderedActivities\[.\]/g, "nonTenderedActivities["+idx+"]"); 
								},
								'id': function(_, id) {
									if(!(jQuery(this).attr('id')===undefined))
										return id.replace(/nonTenderedActivities\[.\]/g, "nonTenderedActivities["+idx+"]"); 
								},
								'class' : function(_, name) {
									if(!(jQuery(this).attr('class')===undefined))
										return name.replace(/nonTenderedActivities\[.\]/g, "nonTenderedActivities["+idx+"]"); 
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
				else if( tableName=="tblLumpSum")
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
										return name.replace(/lumpSumActivities\[.\]/g, "lumpSumActivities["+idx+"]"); 
								},
								'id': function(_, id) {
									if(!(jQuery(this).attr('id')===undefined))
										return id.replace(/lumpSumActivities\[.\]/g, "lumpSumActivities["+idx+"]"); 
								},
								'class' : function(_, name) {
									if(!(jQuery(this).attr('class')===undefined))
										return name.replace(/lumpSumActivities\[.\]/g, "lumpSumActivities["+idx+"]"); 
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




				}else
				{

					jQuery(this).find("a,input, select,button,textarea, errors, span, input:hidden").each(function() {
						var classval = jQuery(this).attr('class');

						if(classval == 'spansno') {
							jQuery(this).text(sno);
							sno++;
						} else {
							jQuery(this).attr({
								'name': function(_, name) {
									if(!(jQuery(this).attr('name')===undefined))
										return name.replace(/\[.\]/g, '['+ idx +']'); 
								},
								'id': function(_, id) {
									if(!(jQuery(this).attr('id')===undefined))
										return id.replace(/\[.\]/g, '['+ idx +']'); 
								},
								'class' : function(_, name) {
									if(!(jQuery(this).attr('class')===undefined))
										return name.replace(/\[.\]/g, '['+ idx +']'); 
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

	function ismsheetOpen()
	{
		var open=false;
		$('.classmsopen').each(function (index)
				{

			if($( this ).val()==1)
				open=true
				});
		//console.log("mssheet open:"+open);
		return open;
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
	
	
	function resetIndexes() {
		var idx = 0;

		//regenerate index existing inputs in table row
		$(".nonTenderedRow").each(function() {
			$(this).find("input,button, select,textarea,td,tbody,tr,table, errors, span, input:hidden").each(function() {


				if (!$(this).is('span')) {
					$(this).attr({
						'name' : function(_, name) {
							//console.log(name);
							if(name)
								{
								name= name.replace(/nonTenderedActivities\[.\]/g, "nonTenderedActivities["+idx+"]");
								return name.replace(/_\d+/,"_"+idx);
								}
						},
						'id' : function(_, id) {
							//console.log(id);
							if(id)
								{
								id= id.replace(/nonTenderedActivities\[.\]/g, "nonTenderedActivities["+idx+"]");
								return id.replace(/_\d+/,"_"+idx);
								}
								
						},
						'data-idx' : function(_, dataIdx) {
							return idx;
						}
					});
				} else {
					$(this).attr({
						'class' : function(_, name) {
							//console.log(name);
							if(name)
								{
								name= name.replace(/nonTenderedActivities\[.\]/g, "nonTenderedActivities["+idx+"]");
							return	name=name.replace(/_\d+/,"_"+idx);
								
								}
						},
						'id' : function(_, id) {
							if(id)
							{
								//console.log(id);
								id= id.replace(/nonTenderedActivities\[.\]/g, "nonTenderedActivities["+idx+"]");
								return id.replace(/_\d+/,"_"+idx);
							}
						}
					});
				}
			});
			idx++;
		});

		idx = 0;

		$(".lumpSumRow").each(function() {
			$(this).find("input,button, select,textarea,td,tbody,tr,table, errors, span, input:hidden").each(function() {

				if (!$(this).is('span')) {
					$(this).attr({
						'name' : function(_, name) {
							if(name)
								return name.replace(/lumpSumActivities\[.\]/g, "lumpSumActivities["+idx+"]");
						},
						'id' : function(_, id) {
							if(id)
								return id.replace(/lumpSumActivities\[.\]/g, "lumpSumActivities["+idx+"]");
						},
						'data-idx' : function(_, dataIdx) {
							return idx;
						}
					});
				} else {
					$(this).attr({
						'class' : function(_, name) {
							if(name)
								return name.replace(/lumpSumActivities\[.\]/g, "lumpSumActivities["+idx+"]");
						},
						'id' : function(_, id) {
							if(id)
								{	
								id= id.replace(/lumpSumActivities\[.\]/g, "lumpSumActivities["+idx+"]");
								return id.replace(/_\d+/,"_"+idx);
									}
						}
					});
				}
			});
			idx++;
		});
	}

	
	function addMSheet(obj)    
	{
//		console.log("adding msheet for "+obj.id);
		var rowid=obj.id;
		sorId=rowid.split(".");
		var	sortable=sorId[0];


		var msfieldsName=rowid.replace("msadd","measurementSheetList");
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
			var k= "<tr class='msheet-tr' id=\""+sortable+".mstr\"><td colspan=\"9\">";
			mscontent=k+mscontent+"</td></tr>";
			curRow.after(mscontent);
			if(document.getElementById(rowid.replace("msadd","mstd")))
				document.getElementById(rowid.replace("msadd","mstd")).innerHTML="";
			if(document.getElementById(rowid.replace("msadd","msopen")))
				document.getElementById(rowid.replace("msadd","msopen")).value="1";
			var idx=sortable.substr(sortable.indexOf("["),sortable.indexOf("]"));
			
			if(sortable.indexOf("nonTenderedActivities") >= 0)
			{
				nonTenderedMsArray[idx]=mscontent;
			}
			else
			{
				lumpSumMsArray[idx]=mscontent;
			}


		}else
		{

			var curRow = $(obj).closest('tr');
			var newrow= $('#msheaderrowtemplate').html();

			newrow=  newrow.replace(/msrowtemplate/g,'msrow'+sortable);
			newrow=  newrow.replace(/templatesorActivities\[0\]/g,sortable);
			if(document.getElementById(rowid.replace("msadd","msopen")))
				document.getElementById(rowid.replace("msadd","msopen")).value="1";
			if(document.getElementById(rowid.replace("msadd","mspresent")))
				document.getElementById(rowid.replace("msadd","mspresent")).value="1";
			curRow.after(newrow);
			var idx=sortable.substr(sortable.indexOf("["),sortable.indexOf("]"));
			if(sortable.indexOf("nonTenderedActivities") >= 0)
			{
				nonTenderedMsArray[idx]="";
			}
			else
			{
				lumpSumMsArray[idx]="";
			}

		}
		patternvalidation();
	}

	$('#addlumpSumRow').click(function() {
		if(ismsheetOpen())
		{
			bootbox.alert("Measurement Sheet is open Please close it first");
			return ;
		}
		var hiddenRowCount = $("#tblLumpSum tbody tr:hidden[id='lumpSumRow']").length;
		if(hiddenRowCount == 0) {
			var key = $("#tblLumpSum tbody tr:visible[id='lumpSumRow']").length;
			addRow('tblLumpSum', 'lumpSumRow');
			resetIndexes();
			$('#activityid_' + key).val('');
			$('#lumpSumId_' + key).val('');
			$('#lumpSumDesc_' + key).val('');
			$('#lumpSumUom_' + key).val('');
			$('#lumpSumRate_' + key).val('');
			$('#lumpSumEstimateRate_' + key).val('');
			$('#lumpSumQuantity_' + key).val('');
			$('#lumpSumQuantity_' + key).removeAttr('readonly');
			$('.lumpSumAmount_' + key).html('');
			$('#lumpSumServiceTaxPerc_' + key).val('');
			$('.lumpSumVatAmt_' + key).html('');
			$('.lumpSumTotal_' + key).html('');
			if(document.getElementById('lumpSumActivities['+key+'].mstd'))
				document.getElementById('lumpSumActivities['+key+'].mstd').innerHTML=""; 
			if(document.getElementById('lumpSumActivities['+key+'].mspresent'))
				document.getElementById('lumpSumActivities['+key+'].mspresent').value="0"; 

			generateSlno();
		} else {
			var key = 0;
			$('#lumpSumDesc_' + key).attr('required', 'required');
			$('#lumpSumUom_' + key).attr('required', 'required');
			$('#lumpSumEstimateRate_' + key).attr('required', 'required');
			$('#lumpSumQuantity_' + key).attr('required', 'required');
			$('#lumpSumQuantity_' + key).removeAttr('readonly');
			$('.lumpSumEstimateRate').val('');
			$('.lumpSumRate').val('');
			$('.lumpSumQuantity').val('');
			$('.lumpSumServiceTaxPerc').val('');
			$('#lumpSumMessage').attr('hidden', 'true');
			$('#lumpSumRow').removeAttr('hidden');
			$('#lumpSumRow').removeAttr('lumpSuminvisible');
			if(document.getElementById('lumpSumActivities['+key+'].mstd'))
				document.getElementById('lumpSumActivities['+key+'].mstd').innerHTML=""; 
			if(document.getElementById('lumpSumActivities['+key+'].mspresent'))
				document.getElementById('lumpSumActivities['+key+'].mspresent').value="0"; 
		}
	});
	
	
	function generateSlno()
	{
		var idx=1;
		$(".spanlumpsumslno").each(function(){
			$(this).text(idx);
			idx++;
		});
	}
	
	function generateNonTenderedSno()
	{
		var idx=1;
		$(".spannontenderedslno").each(function(){
			$(this).text(idx);
			idx++;
		});
	}
	
	function deleteNonTendered(obj) {
		var rIndex = getRow(obj).rowIndex;
		var id = $(getRow(obj)).children('td:first').children('input:first').val();
		//To get all the deleted rows id
		var aIndex = rIndex - 1;
		if(!$("#removedActivityIds").val()==""){
			$("#removedActivityIds").val($("#removedActivityIds").val()+",");
		}
		$("#removedActivityIds").val($("#removedActivityIds").val()+id);

		var tbl=document.getElementById('tblNonTendered');	
		var rowcount=$("#tblNonTendered > tbody >tr").length;
		if(rowcount==2) {
			var rowId = $(obj).attr('class').split('_').pop();
			$('#nonTenderedActivityid_' + rowId).val('');
			$('.sorhiddenid').val('');
			$('#quantity_' + rowId).val('');
			$('#quantity_' + rowId).removeAttr('required');
			$('.amount_' + rowId).html('');
			$('#vat_' + rowId).val('');
			$('.vatAmount_' + rowId).html('');
			$('.total_' + rowId).html('');
			$('#nonTenderedRow').attr('hidden', 'true');
			$('#nonTenderedRow').attr('sorinvisible', 'true');
			$('#message').removeAttr('hidden');
		} else {
			deleteRow('tblNonTendered',obj);
		}
		resetIndexes();
		//starting index for table fields
		generateNonTenderedSno();
		calculateEstimateAmountTotal();
		calculateVatAmountTotal();
		total();
		return true;
	}

	function calculateEstimateAmountTotal() {
		var total = 0;
		$('.amount').each(function() {
			if($(this).html().trim() != "")
				total = parseFloat(parseFloat(total) + parseFloat($(this).html().replace(',', ''))).toFixed(2);
		});
		$('#nonTenderedEstimateTotal').html(total);
	}

	function calculateVatAmountTotal() {
		var total = 0;
		$('.vatAmt').each(function() {
			if($(this).html().trim() != "")
				total = parseFloat(parseFloat(total) + parseFloat($(this).html().replace(',', ''))).toFixed(2);
		});
		$('#serviceVatAmtTotal').html(total);
	}

	function total() {
		var total = 0.0;
		$('.total').each(function() {
			if($(this).html().trim() != "")
				total = parseFloat(parseFloat(total) + parseFloat($(this).html().replace(',', ''))).toFixed(2);
		});
		$('#nonTenderedTotal').html(total);
		calculateEstimateValue();
	}

	function calculateEstimateValue() {
		var nonTenderedTotal = $('#nonTenderedTotal').html();
		var lumpSumTotal = $('#lumpSumTotal').html();
		if(nonTenderedTotal == '')
			nonTenderedTotal = 0.0;
		if(lumpSumTotal == '')
			lumpSumTotal = 0.0;

		var workValue = parseFloat(parseFloat(nonTenderedTotal) + parseFloat(lumpSumTotal) ).toFixed(2);
		var estimateValue =  parseFloat(workValue).toFixed(2);
		$('#estimateValue').val(estimateValue);
		$('#workValue').val(workValue);
		$('#estimateValueTotal').html(estimateValue);
		$('#workValueTotal').html(workValue);
	}
	
	function validateInput(object) {
		var valid = /^[1-9](\d{0,9})(\.\d{0,2})?$/.test($(object).val()),
		val = $(object).val();

		if(!valid){
			//console.log("Invalid input!");
			$(object).val(val.substring(0, val.length - 1));
		}
	}
	
	function validateQuantityInput(object) {
		var valid = /^[0-9](\d{0,9})(\.\d{0,4})?$/.test($(object).val()),
		val = $(object).val();

		if(!valid){
			//console.log("Invalid input!");
			$(object).val(val.substring(0, val.length - 1));
		}
	}
	
	function calculateEstimateAmount(currentObj) {
		rowcount = $(currentObj).attr('id').split('_').pop();
		var rate = parseFloat($('#rate_' + rowcount).val().trim());
		var amount = parseFloat($(currentObj).val() * rate).toFixed(2);
		var vatAmount = parseFloat(($('#vat_' + rowcount).val() * amount) / 100).toFixed(2);
		$('.amount_' + rowcount).html(amount);
		$('.vatAmount_' + rowcount).html(vatAmount);
		$('.total_' + rowcount).html(parseFloat(parseFloat(amount) + parseFloat(vatAmount)).toFixed(2));
		calculateEstimateAmountTotal();
		calculateVatAmountTotal();
		total();
	}

	function updateUom(obj) {
		var rowId = $(obj).attr('id').split('_').pop();
		$('#lumpSumUomid_' + rowId).val($(obj).val());
		$('#lumpSumUomid_' + rowId).val($(obj).val());
		$('#lumpSumEstimateRate_' + rowId).val("");
		$('#lumpSumQuantity_' + rowId).val("");
		calculateLumpSumEstimateAmount($('#lumpSumQuantity_' + rowId));
	}
	
	
	function calculateLumpSumEstimateAmount(currentObj) {
		var rowcount = $(currentObj).attr('id').split('_').pop();
		var description = $('#lumpSumDesc_' + rowcount).val();
		var uom = $('#lumpSumUom_' + rowcount).val();
		var flag = false;
		if(description == '') {
			bootbox.alert($('#errordescription').val());
			$('#lumpSumDesc_' + rowcount).val('');
			flag = true;
		}
		if(!flag && uom == '') {
			bootbox.alert($('#erroruom').val());
			$('#lumpSumUom_' + rowcount).val('');
			$('.lumpSumAmount_' + rowcount).html('');
			$('.lumpSumVatAmount_' + rowcount).html('');
			$('.lumpSumTotal_' + rowcount).html('');
			calculateLumpSumEstimateAmountTotal();
			calculateLumpSumVatAmountTotal();
			lumpSumTotal();
			flag = true;
		}
		if(!flag) {
			var estimateRate = $('#lumpSumEstimateRate_' + rowcount).val();
			var unitRate;
			if(estimateRate == "")
				unitRate = 0.0;
			else{
				unitRate = getUnitRate($('#lumpSumUom_' + rowcount).find(":selected").text().split(" -- ")[1],estimateRate);
				$('#lumpSumRate_' + rowcount).val(unitRate);
			}
			var quantity = $('#lumpSumQuantity_' + rowcount).val();
			if(quantity == "")
				quantity = 0.0;
			var amount = parseFloat(parseFloat(quantity) * parseFloat(unitRate)).toFixed(2);
			var vatAmount = parseFloat(($('#lumpSumServiceTaxPerc_' + rowcount).val() * amount) / 100).toFixed(2);
			$('.lumpSumAmount_' + rowcount).html(amount);
			$('.lumpSumVatAmount_' + rowcount).html(vatAmount);
			$('.lumpSumTotal_' + rowcount).html(parseFloat(parseFloat(amount) + parseFloat(vatAmount)).toFixed(2));
			calculateLumpSumEstimateAmountTotal();
			calculateLumpSumVatAmountTotal();
			lumpSumTotal();
		}
	}
	
	function calculateLumpSumEstimateAmountTotal() {
		var total = 0;
		$('.lumpsumamount').each(function() {
			if($(this).html().trim() != "")
				total = parseFloat(parseFloat(total) + parseFloat($(this).html().replace(',', ''))).toFixed(2);
		});
		$('#lumpSumEstimateTotal').html(total);
	}

	function calculateLumpSumVatAmountTotal() {
		var total = 0;
		$('.lumpSumVatAmt').each(function() {
			if($(this).html().trim() != "")
				total = parseFloat(parseFloat(total) + parseFloat($(this).html().replace(',', ''))).toFixed(2);
		});
		$('#lumpSumServiceVatAmtTotal').html(total);
	}

	function lumpSumTotal() {
		var total = 0.0;
		$('.lumpSumTotal').each(function() {
			if($(this).html().trim() != "")
				total = parseFloat(parseFloat(total) + parseFloat($(this).html().replace(',', ''))).toFixed(2);
		});
		$('#lumpSumTotal').html(total);
		calculateEstimateValue();
	}
	
	function deleteLumpSum(obj) {
		var rIndex = getRow(obj).rowIndex;

		var id = $(getRow(obj)).children('td:first').children('input:first').val();
		//To get all the deleted rows id
		var aIndex = rIndex - 1;
		if(!$("#removedActivityIds").val()==""){
			$("#removedActivityIds").val($("#removedActivityIds").val()+",");
		}
		$("#removedActivityIds").val($("#removedActivityIds").val()+id);

		var rowId = $(obj).attr('class').split('_').pop();
		var rowcount=$("#tblLumpSum tbody tr").length;

		if(rowcount==2) {
			$('#activityid_' + rowId).val('');
			$('#nonSorId_' + rowId).val('');
			$('#lumpSumId_' + rowId).val('');
			$('#lumpSumDesc_' + rowId).val('');
			$('#lumpSumUom_' + rowId).val('');
			$('#lumpSumEstimateRate_' + rowId).val('');
			$('#lumpSumRate_' + rowId).val('');
			$('#lumpSumQuantity_' + rowId).val('');
			$('#lumpSumQuantity_' + rowId).removeAttr('required');
			$('.lumpSumAmount_' + rowId).html('');
			$('#lumpSumServiceTaxPerc_' + rowId).val('');
			$('.lumpSumVatAmount_' + rowId).html('');
			$('.lumpSumTotal_' + rowId).html('');
			$('#lumpSumRow').attr('hidden', 'true');
			$('#lumpSumRow').attr('nonsorinvisible', 'true');
			$('#lumpSumMessage').removeAttr('hidden');
		} else {
			deleteRow('tblLumpSum',obj);
		}
		resetIndexes();
		//starting index for table fields
		generateSlno();

		calculateLumpSumEstimateAmountTotal();
		calculateLumpSumVatAmountTotal();
		lumpSumTotal();
		return true;
	}
	
	function limitCharatersBy3_2(object)
	{
		var valid = /^[0-9](\d{0,2})(\.\d{0,2})?$/.test($(object).val()),
		val = $(object).val();

		if(!valid){
			//console.log("Invalid input!");
			$(object).val(val.substring(0, val.length - 1));
		}	

	}
	
	function limitCharatersBy10_4(object)
	{
		var valid = /^[0-9](\d{0,9})(\.\d{0,4})?$/.test($(object).val()),
		val = $(object).val();

		if(!valid){
			//console.log("Invalid input!");
			$(object).val(val.substring(0, val.length - 1));
		}	

	}
	
	function findNet(obj)
	{
		var len=$(obj).closest('table').find('tr').length;


		var name=obj.id.split(".");

		var sum=0;
		for(var i=0;i<len-2;i++)
		{
			var qname=name[0]+'.measurementSheetList['+i+'].quantity';
			var quantity=eval(document.getElementById(qname).value);
			var oname=name[0]+'.measurementSheetList['+i+'].identifier';
			var operationObj=document.getElementById(oname);
			var operation=operationObj.options[operationObj.selectedIndex].value;
			//console.log(quantity+"---"+operation);
			if(quantity===undefined)
				quantity=0;
			if(quantity==NaN)
				quantity=0;
			if(quantity=='')
				quantity=0;
			if(operation=='A')
				sum=sum+quantity;
			else
				sum=sum-quantity;
		}
		//var fname=obj.name.split(".");
		var netName=name[0]+'.msnet';
		var x=sum+"";
		var y=x.split(".");
		if(y.length>1)
		  if(y[1].length>4)
			  sum=sum.toFixed(4);  
		
		//sum=parseFloat(sum).toFixed(4);
		//console.log(document.getElementById(netName).innerHTML);
		document.getElementById(netName).innerHTML=sum;
		return true;


	}
	
	
	$(document).on('click','.ms-submit',function () {

		var sid=$(this).attr("id");
		var mscontent="<tr id=\""+sid.split(".")[0]+".mstr\">";

		var net=eval(document.getElementById(sid.split(".")[0]+".msnet").innerHTML);
		if(net==NaN ||net<=0)
		{
			bootbox.alert("Net Quantity should be greater than 0");
			return false;
		}
		var qobj1=document.getElementById(sid.split(".")[0]+".measurementSheetList[0].no");
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
		if(sid.split(".")[0].indexOf("nonTenderedActivities") >= 0)
		{
			calculateEstimateAmount(document.getElementsByName(sid.split(".")[0]+".quantity")[0]);
		}else
		{
			calculateLumpSumEstimateAmount(document.getElementsByName(sid.split(".")[0]+".quantity")[0]);
		}
		$(qobj).attr("readonly","readonly");


	});
	
	function validateMsheet(obj)
	{

		var len=$(obj).closest('table').find('tr').length;
	
	
		var name=obj.id.split(".");
	
		var sum=0;
		for(var i=0;i<len-2;i++)
		{
			var qname=name[0]+'.measurementSheetList['+i+'].quantity';
			var no=eval(document.getElementById(name[0]+'.measurementSheetList['+i+'].no').value);
			var lent=eval(document.getElementById(name[0]+'.measurementSheetList['+i+'].length').value);
			var width=eval(document.getElementById(name[0]+'.measurementSheetList['+i+'].width').value);
			var depthorheight=eval(document.getElementById(name[0]+'.measurementSheetList['+i+'].depthOrHeight').value);
			var qunatity=eval(document.getElementById(name[0]+'.measurementSheetList['+i+'].quantity').value);
	
			if((no===undefined ||no==NaN) && (width===undefined ||width==NaN) && (lent===undefined ||lent==NaN) 
					&&(depthorheight===undefined ||depthorheight==NaN) &&  (qunatity===undefined ||qunatity==NaN))
			{
				bootbox.alert("Empty row is not allowed. Please delete the empty row or Enter Quantity");
				return false;
			}
			if(qunatity==NaN || qunatity<=0)
			{
				bootbox.alert("Zero is not allowed in Quantity");
				return false;
			}
				
	
	
		}
		return true;

	}
	
	$(document).on('click','.add-msrow',function () {
		var len=$(this).closest('table').find('tr').length;
		var msrowname= $(this).closest('table').attr('id');
		
	 

		//var msrowname1=	msrowname.id;
		len=len-2;
		var msrownameid=msrowname.split(".")[0];
		var rep='measurementSheetList\['+len+'\]';

		//console.log(len+'===='+rep);
		var $newrow= "<tr>"+$('#msrowtemplate').html()+"</tr>";
		$newrow=  $newrow.replace(/templatesorActivities\[0\]/g,msrownameid);
		$newrow=  $newrow.replace(/measurementSheetList\[0\]/g,rep);
		$newrow=$newrow.replace('value="1"','value="'+(len+1)+'"');
		////console.log($newrow)
		$(this).closest('tr').before($newrow);

		patternvalidation();


	});
	
	$(document).on('click','.reset-ms',function () {

		var len=$(this).closest('table').find('tr').length;
		var msrowname= $(this).closest('table').attr('id');
		var tbl=document.getElementById(msrowname);
		var sid=msrowname.split(".")[0];
		var newrow= document.getElementById("templatesorActivities[0].mstr").innerHTML;

		newrow=  newrow.replace(/msrowtemplate/g,'msrow'+sid);
		newrow=  newrow.replace(/templatesorActivities\[0\]/g,sid);
		document.getElementById(sid+".mstr").innerHTML=newrow;
		
		
	});
	
	
	function openAllmsheet()
	{
		var open=false;
		$('.classmsopen').each(function (index)
				{

			if($( this ).val()==0)
				
			{
				var sid=$( this ).attr('id');
				var	sortable=sid.split(".")[0];
				if(document.getElementById(sid.split(".")[0]+".mspresent").value==1)
				{
					
					var   mscontent=document.getElementById(sid.replace("msopen","mstd")).innerHTML;

					if(mscontent!='')
					{
						if(mscontent.indexOf(headstart) >=0)
						{
							var head= mscontent.substring(mscontent.indexOf(headstart),mscontent.indexOf(headend));
							var tail= mscontent.substring(mscontent.indexOf(tailstart),mscontent.indexOf(tailend));
							mscontent= mscontent.replace(head,"");
							mscontent= mscontent.replace(tail,"");
						}

						var curRow = $(this).closest('tr');
						var k= "<tr id=\""+sortable+".mstr\" class='msheet-tr'><td colspan=\"9\">";
						mscontent=k+mscontent+"</td></tr>";
						curRow.after(mscontent);
						document.getElementById(sid.replace("msopen","mstd")).innerHTML="";
						$( this ).val(1);
						var idx=sortable.substr(sortable.indexOf("["),sortable.indexOf("]"));
						
						if(sortable.indexOf("nonTenderedActivities") >= 0)
						{
							nonTenderedMsArray[idx]=mscontent;
						}
						else
						{
							lumpSumMsArray[idx]=mscontent;
						}

						
					}

				}
			}

				});
		return open;	
	}
	
	function closeAllmsheet()
	{
		var retVal = confirm("This will validate and update quantities . Do you want to continue?");
		if( retVal == false )
		{
			return ;
		}
		else{


			var open=false;
			$('.classmsopen').each(function (index)
					{

				if($( this ).val()==1)
				{

					var sid=$( this ).attr('id');
					var qobj1=document.getElementById(sid.split(".")[0]+".measurementSheetList[0].no");
					if(!validateMsheet(qobj1))
					{
						return false;
					}
					
					var mscontent="<tr id=\""+sid.split(".")[0]+".mstr\">";
					document.getElementsByName(sid.split(".")[0]+".quantity")[0].value=document.getElementById(sid.split(".")[0]+".msnet").innerHTML;

					mscontent=document.getElementById(sid.split(".")[0]+".mstr").innerHTML;

					
					document.getElementById(sid.split(".")[0]+".mstd").innerHTML=mscontent;
					document.getElementById(sid.split(".")[0]+".msopen").value="0";
					var mstr=document.getElementById(sid.split(".")[0]+".mstr");
					$(mstr).remove(); 
					var qobj=document.getElementsByName(sid.split(".")[0]+".quantity")[0];
					$(qobj).attr("readonly","readonly");
					if(sid.split(".")[0].indexOf("nonTenderedActivities") >= 0)
					{
						calculateEstimateAmount(document.getElementsByName(sid.split(".")[0]+".quantity")[0]);
					}else
					{
						calculateLumpSumEstimateAmount(document.getElementsByName(sid.split(".")[0]+".quantity")[0]);
					}

				    }
					});
		}
		//console.log("mssheet open:"+open);
		return open;

	}
	
	function  deleteThisRow(obj) {
		var rIndex = getRow(obj).rowIndex;
		var tablename=$(obj).closest('table').attr('id');
		var tbl=document.getElementById( tablename);
		var rowcount=$(obj).closest('table').find('tr').length;
		//console.log(tbl);
		if(rowcount<=3) {
			
			var retVal = confirm("This action will remove complete Measurement Sheet for Non Tendered/Lum Sump. Do you want to continue ?");
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
		   if(sid.indexOf("nonTenderedActivities") >= 0)
			{
				calculateEstimateAmount(document.getElementsByName(sid+".quantity")[0]);
			}else
			{
				calculateLumpSumEstimateAmount(document.getElementsByName(sid+".quantity")[0]);
			}
			}
			return ;
		} else {
			tbl.deleteRow(rIndex);
		}
		reindex(tablename);
		findNet(tbl);  

	}
	
	function reindex(tableId)
	{

		var idx=0;
		tbl=document.getElementById(tableId);
		////console.log($(tbl).html());

		$(tbl).find("tbody tr").each(function(e) {

			//console.log('for loop');
			$(this).find("input,select,textarea").each(function() {
				var classval = jQuery(this).attr('class');	
				 
				if(classval && classval.indexOf("spanslno") > -1) {
					jQuery(this).val(idx+1);
					$(this).attr('value', $(this).val());
				}

				$(this).attr({
					'name' : function(_, name) {
						if(name)
							return name.replace(/measurementSheetList\[.\]/g, "measurementSheetList["+idx+"]");
					},
					'id' : function(_, id) {
						if(id)
							return id.replace(/measurementSheetList\[.\]/g, "measurementSheetList["+idx+"]");
					},
					'data-idx' : function(_, dataIdx) {
						return idx;
					}
				});

			});
			idx++;
		});


	}
	
	$(document).on('click','.hide-ms',function () {

		var sid=$(this).closest('tr').attr("id");
		var name=	sid.split(".")[0]
		var idx=name.substr(name.indexOf("["),name.indexOf("]"));
		if(sid.split(".")[0].indexOf("nonTenderedActivities") >= 0)
		{
			//to support view close option
			if(nonTenderedMsArray[idx])
				{
			document.getElementById(sid.split(".")[0]+".mstd").innerHTML=nonTenderedMsArray[idx];
			if(nonTenderedMsArray[idx].length==0)
				document.getElementById(sid.split(".")[0]+".mspresent").value="0";
				}
				
		}else
		{
			if(lumpSumMsArray[idx])
				{
			document.getElementById(sid.split(".")[0]+".mstd").innerHTML=lumpSumMsArray[idx];
			if(lumpSumMsArray[idx].length==0)
				document.getElementById(sid.split(".")[0]+".mspresent").value="0";
				}
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
				bootbox.alert("Zero is not allowed");
				$(this).val('');
				}
				$(this).attr('value', $(this).val());
			

		}
		else if($(this).is("select"))
		{
			if($(this).val()=='A')
				{
				$(this).find('option[value="D"]').removeAttr('selected');	
				//console.log('dropdown value change triggered!');
				$(this).find('option[value="A"]').attr('selected', 'selected');
				}else
					{
					$(this).find('option[value="A"]').removeAttr('selected');	
					//console.log('dropdown value change triggered!');
					$(this).find('option[value="D"]').attr('selected', 'selected');	
					}
			
		}
		else if($(this).is("textarea"))
		{
			//console.log('dropdown value change triggered!');
			$(this).html($(this).val());
		}
		if($(this).attr('id').indexOf("quantity")>=0)
			findNet(this);
		else
		findTotal(this);
		//$(this).closest('tr').hide();
	});

	
	function findTotal(obj)
	{

		var name=obj.name.split(".");
		var lengthname=name[0]+'.'+name[1]+'.length';
		var no1,depthOrHeight1,width1,length1;
		var lent=$('input[id="'+lengthname+'"]');
		//console.log($(lent).attr('value'));
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
			var net=length * no * width * depthOrHeight;
			var x=net+"";
			var y=x.split(".");
			if(y.length>1)
			  if(y[1].length>4)
				  net=net.toFixed(4);  
			
			document.getElementById(name[0]+'.'+name[1]+'.quantity').value=net;
			$('input[id="'+name[0]+'.'+name[1]+'.quantity'+'"]').attr('value',net);

		}
		var netObj=document.getElementById(name[0]+'.'+name[1]+'.quantity');
		$(netObj).attr('value', document.getElementById(name[0] + '.' + name[1] + '.quantity').value);
		var len=$(obj).closest('table').find('tbody').children.length;
		//console.log(len);
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

function viewLineEstimate(id) {
	window.open("/egworks/lineestimate/view/" + id, '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

function viewLOA(id) {
	window.open("/egworks/letterofacceptance/view/" + id, '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

function viewEstimate(id) {
	window.open("/egworks/abstractestimate/view/" + id, '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

$('#submitForm').click(function() {
	deleteHiddenRows();
	document.forms[0].submit();
});

function deleteHiddenRows(){
	var hiddenRowCount = $("#tblNonTendered tbody tr[sorinvisible='true']").length;
	if(hiddenRowCount == 1) {
		var tbl=document.getElementById('tblNonTendered');
		tbl.deleteRow(2);
	}

	hiddenRowCount = $("#tblLumpSum tbody tr[nonsorinvisible='true']").length;
	if(hiddenRowCount == 1) {
		var tbl=document.getElementById('tblLumpSum');
		tbl.deleteRow(2);
	}
}


function calculateVatAmount(currentObj) {
	rowcount = $(currentObj).attr('id').split('_').pop();
	var estimatedAmount = parseFloat($('.amount_' + rowcount).html().trim());
	var vatAmount = parseFloat(($(currentObj).val() * estimatedAmount) / 100).toFixed(2);
	$('.vatAmount_' + rowcount).html(vatAmount);
	$('.total_' + rowcount).html(parseFloat(parseFloat(estimatedAmount) + parseFloat(vatAmount)).toFixed(2));
	calculateVatAmountTotal();
	total();
}


function calculateVatAmountTotal() {
	var total = 0;
	$('.vatAmt').each(function() {
		if($(this).html().trim() != "")
			total = parseFloat(parseFloat(total) + parseFloat($(this).html().replace(',', ''))).toFixed(2);
	});
	$('#serviceVatAmtTotal').html(total);
}

function calculateLumpSumVatAmount(currentObj) {
	var rowcount = $(currentObj).attr('id').split('_').pop();
	var estimatedAmount = $('.lumpSumAmount_' + rowcount).html();
	if(estimatedAmount == "")
		estimatedAmount = 0.0;
	var serviceTaxPerc = $('#lumpSumServiceTaxPerc_' + rowcount).val();
	if(serviceTaxPerc == "")
		serviceTaxPerc = 0.0;
	var vatAmount = parseFloat((serviceTaxPerc * estimatedAmount) / 100).toFixed(2);
	$('.lumpSumVatAmount_' + rowcount).html(vatAmount);
	$('.lumpSumTotal_' + rowcount).html(parseFloat(parseFloat(estimatedAmount) + parseFloat(vatAmount)).toFixed(2));
	calculateLumpSumVatAmountTotal();
	lumpSumTotal();
}

function calculateLumpSumEstimateAmountTotal() {
	var total = 0;
	$('.lumpSumamount').each(function() {
		if($(this).html().trim() != "")
			total = parseFloat(parseFloat(total) + parseFloat($(this).html().replace(',', ''))).toFixed(2);
	});
	$('#lumpSumEstimateTotal').html(total);
}
