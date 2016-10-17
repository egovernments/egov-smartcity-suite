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
var sorMsArray=new Array(200);
var nonSorMsArray=new Array(200);
var nonTenderedMsArray=new Array(200);
var lumpSumMsArray=new Array(200);
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
		
		if(sortable.indexOf("sorMbDetails") >= 0)
		{
			sorMsArray[idx]=mscontent;
		} else if(sortable.indexOf("nonTenderedMbDetails") >= 0)
		{
			nonTenderedMsArray[idx]=mscontent;
		}
		else if(sortable.indexOf("lumpSumMbDetails") >= 0)
		{
			lumpSumMsArray[idx]=mscontent;
		}
		else
		{
			nonSorMsArray[idx]=mscontent;
		}
	}else
	{
		var curRow = $(obj).closest('tr');
		var newrow= $('#msheaderrowtemplate').html();

		newrow=  newrow.replace(/msrowtemplate/g,'msrow'+sortable);
		newrow=  newrow.replace(/templatesorMbDetails\[0\]/g,sortable);
		document.getElementById(rowid.replace("msadd","msopen")).value="1";
		document.getElementById(rowid.replace("msadd","mspresent")).value="1";
		curRow.after(newrow);
		var idx=sortable.substr(sortable.indexOf("["),sortable.indexOf("]"));
		if(sortable.indexOf("sorMbDetails") >= 0)
		{
			sorMsArray[idx]="";
		} else if(sortable.indexOf("nonTenderedMbDetails") >= 0)
		{
			nonTenderedMsArray[idx]="";
		}
		else if(sortable.indexOf("lumpSumMbDetails") >= 0)
		{
			lumpSumMsArray[idx]="";
		}
		else
		{
			nonSorMsArray[idx]="";
		}

	}
	patternvalidation();
}

$(document).on('click','.hide-ms',function () {

	var sid=$(this).closest('tr').attr("id");
	var name=	sid.split(".")[0]
	var idx=name.substr(name.indexOf("["),name.indexOf("]"));
	if(sid.split(".")[0].indexOf("sorMbDetails") >= 0)
	{
		document.getElementById(sid.split(".")[0]+".mstd").innerHTML=sorMsArray[idx];
		if(sorMsArray[idx].length==0)
			document.getElementById(sid.split(".")[0]+".mspresent").value="0";
			
	}else if(sid.split(".")[0].indexOf("nonTenderedMbDetails") >= 0)
	{
		document.getElementById(sid.split(".")[0]+".mstd").innerHTML=nonTenderedMsArray[idx];
		if(nonTenderedMsArray[idx].length==0)
			document.getElementById(sid.split(".")[0]+".mspresent").value="0";
	} else if(sid.split(".")[0].indexOf("lumpSumMbDetails") >= 0)
	{
		document.getElementById(sid.split(".")[0]+".mstd").innerHTML=lumpSumMsArray[idx];
		if(lumpSumMsArray[idx].length==0)
			document.getElementById(sid.split(".")[0]+".mspresent").value="0";
			
	}else
	{
		document.getElementById(sid.split(".")[0]+".mstd").innerHTML=nonSorMsArray[idx];
		if(nonSorMsArray[idx].length==0)
			document.getElementById(sid.split(".")[0]+".mspresent").value="0";
	}

	document.getElementById(sid.split(".")[0]+".msopen").value="0";
	
	var mstr=document.getElementById(sid.split(".")[0]+".mstr");
	$(mstr).remove();
});

$(document).on('change','.runtime-update',function (e) {
	if($(this).is("input"))
	{
		//console.log('input value change triggered!');
		if($(this).val()==0)
			{
			alert("Zero is not allowed");
			$(this).val('');
			}
		$(this).attr('value', $(this).val());
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
	if(net==NaN ||net<=0)
	{
		alert("Net quantity should be greater than 0");
		return false;
	}
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
	if(sid.split(".")[0].indexOf("sorMbDetails") >= 0)
	{
		calculateSorAmounts(document.getElementsByName(sid.split(".")[0]+".quantity")[0]);
	}else if(sid.split(".")[0].indexOf("nonTenderedMbDetails") >= 0)
	{
		calculateNonTenderedAmounts(document.getElementsByName(sid.split(".")[0]+".quantity")[0]);
	} else if(sid.split(".")[0].indexOf("lumpSumMbDetails") >= 0)
	{
		calculateLumpSumAmounts(document.getElementsByName(sid.split(".")[0]+".quantity")[0]);
	}else
	{
		calculateNonSorAmounts(document.getElementsByName(sid.split(".")[0]+".quantity")[0]);
	}
});

function reindex(tableId)
{

	var idx=0;
	tbl=document.getElementById(tableId);
	////console.log($(tbl).html());

	$(tbl).find("tbody tr").each(function(e) {

		//console.log('for loop');
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
	var len=$(this).closest('table').find('.runtime-update').val("");
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
		var net=parseFloat(length * no * width * depthOrHeight).toFixed(4);

		document.getElementById(name[0]+'.'+name[1]+'.quantity').value=net;
		$('input[id="'+name[0]+'.'+name[1]+'.quantity'+'"]').attr('value',net);

	}
	var netObj=document.getElementById(name[0]+'.'+name[1]+'.quantity');
	$(netObj).attr('value', document.getElementById(name[0] + '.' + name[1] + '.quantity').value);
	var len=$(obj).closest('table').find('tbody').children.length;
	//console.log(len);
	if(name[0].indexOf("sorMbDetails") >= 0)
		findNet(netObj);
	else if(name[0].indexOf("nonTenderedMbDetails") >= 0)
		findNonTenderedNet(netObj);
	else if(name[0].indexOf("lumpSumMbDetails") >= 0)
		findLumpSumNet(netObj);
	else 
		findNonSorNet(netObj);
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
		var oname= '#msrowidentifier_' + index + '_' + i;
		var operation=$(oname).html().trim();
		console.log(quantity+"---"+operation);
		if(quantity===undefined)
			quantity=0;
		if(quantity==NaN)
			quantity=0;
		if(quantity=='')
			quantity=0;
		if(operation=='No')
			sum=sum+quantity;
		else
			sum=sum-quantity;
	}
	//var fname=obj.name.split(".");
	var netName=name[0]+'.msnet';
	sum=parseFloat(sum).toFixed(4);
	//console.log(document.getElementById(netName).innerHTML);
	document.getElementById(netName).innerHTML=sum;
}

function findNonTenderedNet(obj)
{
	var len=$(obj).closest('tbody').find('tr').length;
	
	var name=obj.id.split(".");
	
	var index = name[0].split('[')[1].split(']')[0];

	var sum=0;
	for(var i=0;i<len - 1;i++)
	{
		var qname=name[0]+'.measurementSheets['+i+'].quantity';
		var quantity=eval(document.getElementById(qname).value);
		var oname= '#nontenderedmsrowidentifier_' + index + '_' + i;
		var operation=$(oname).html().trim();
		console.log(quantity+"---"+operation);
		if(quantity===undefined)
			quantity=0;
		if(quantity==NaN)
			quantity=0;
		if(quantity=='')
			quantity=0;
		if(operation=='No')
			sum=sum+quantity;
		else
			sum=sum-quantity;
	}
	//var fname=obj.name.split(".");
	var netName=name[0]+'.msnet';
	sum=parseFloat(sum).toFixed(4);
	//console.log(document.getElementById(netName).innerHTML);
	document.getElementById(netName).innerHTML=sum;
}


function findLumpSumNet(obj)
{
	var len=$(obj).closest('tbody').find('tr').length;
	
	var name=obj.id.split(".");
	
	var index = name[0].split('[')[1].split(']')[0];

	var sum=0;
	for(var i=0;i<len - 1;i++)
	{
		var qname=name[0]+'.measurementSheets['+i+'].quantity';
		var quantity=eval(document.getElementById(qname).value);
		var oname= '#lumpsummsrowidentifier_' + index + '_' + i;
		var operation=$(oname).html().trim();
		console.log(quantity+"---"+operation);
		if(quantity===undefined)
			quantity=0;
		if(quantity==NaN)
			quantity=0;
		if(quantity=='')
			quantity=0;
		if(operation=='No')
			sum=sum+quantity;
		else
			sum=sum-quantity;
	}
	//var fname=obj.name.split(".");
	var netName=name[0]+'.msnet';
	sum=parseFloat(sum).toFixed(4);
	//console.log(document.getElementById(netName).innerHTML);
	document.getElementById(netName).innerHTML=sum;
}

function findNonSorNet(obj)
{
	var len=$(obj).closest('tbody').find('tr').length;
	
	var name=obj.id.split(".");
	
	var index = name[0].split('[')[1].split(']')[0];

	var sum=0;
	for(var i=0;i<len - 1;i++)
	{
		var qname=name[0]+'.measurementSheets['+i+'].quantity';
		var quantity=eval(document.getElementById(qname).value);
		var oname= '#nonsormsrowidentifier_' + index + '_' + i;
		var operation=$(oname).html().trim();
		console.log(quantity+"---"+operation);
		if(quantity===undefined)
			quantity=0;
		if(quantity==NaN)
			quantity=0;
		if(quantity=='')
			quantity=0;
		if(operation=='No')
			sum=sum+quantity;
		else
			sum=sum-quantity;
	}
	//var fname=obj.name.split(".");
	var netName=name[0]+'.msnet';
	sum=parseFloat(sum).toFixed(4);
	//console.log(document.getElementById(netName).innerHTML);
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
		//console.log("Invalid input!");
		$(object).val(val.substring(0, val.length - 1));
	}	
}

function limitCharatersBy3_2(object) {
	var valid = /^[0-9](\d{0,2})(\.\d{0,2})?$/.test($(object).val()),
	val = $(object).val();
	if(!valid){
		//console.log("Invalid input!");
		$(object).val(val.substring(0, val.length - 1));
	}	
}

function viewMBHistory() {
	var mbheaderId = $('#id').val();
	window.open("/egworks/mb/history/" + mbheaderId, '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}