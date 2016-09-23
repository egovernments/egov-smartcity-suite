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
var headstart="<!--only for validity head start -->";
var headend="<!--only for validity head end -->";
var tailstart="<!--only for validity tail start -->";
var tailend="<!--only for validity tail end -->";
 
function addMSheet(obj)    
{
//	console.log("adding msheet for "+obj.id);
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
		var k= "<tr id=\""+sortable+".mstr\"  class='msheet-tr' ><td colspan=\"9\">";
		mscontent=k+mscontent+"</td></tr>";
		curRow.after(mscontent);
		document.getElementById(rowid.replace("msadd","mstd")).innerHTML="";
		document.getElementById(rowid.replace("msadd","msopen")).value="1";
		var idx=sortable.substr(sortable.indexOf("["),sortable.indexOf("]"));
		
		if(sortable.indexOf("sorActivities") >= 0)
		{
			sorMsArray[idx]=mscontent;
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
		newrow=  newrow.replace(/templatesorActivities\[0\]/g,sortable);
		document.getElementById(rowid.replace("msadd","msopen")).value="1";
		document.getElementById(rowid.replace("msadd","mspresent")).value="1";
		curRow.after(newrow);
		var idx=sortable.substr(sortable.indexOf("["),sortable.indexOf("]"));
		if(sortable.indexOf("sorActivities") >= 0)
		{
			sorMsArray[idx]="";
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
	if(sid.split(".")[0].indexOf("sorActivities") >= 0)
	{
		//to support view close option
		if(sorMsArray[idx])
			{
		document.getElementById(sid.split(".")[0]+".mstd").innerHTML=sorMsArray[idx];
		if(sorMsArray[idx].length==0)
			document.getElementById(sid.split(".")[0]+".mspresent").value="0";
			}
			
	}else
	{
		if(nonSorMsArray[idx])
			{
		document.getElementById(sid.split(".")[0]+".mstd").innerHTML=nonSorMsArray[idx];
		if(nonSorMsArray[idx].length==0)
			document.getElementById(sid.split(".")[0]+".mspresent").value="0";
			}
	}

	document.getElementById(sid.split(".")[0]+".msopen").value="0";
	
	var mstr=document.getElementById(sid.split(".")[0]+".mstr");
	$(mstr).remove();

	
});



$(document).on('click','.ms-submit',function () {

	var sid=$(this).attr("id");
	var mscontent="<tr id=\""+sid.split(".")[0]+".mstr\">";

	var net=eval(document.getElementById(sid.split(".")[0]+".msnet").innerHTML);
	if(net==NaN ||net<=0)
	{
		alert("Net Quantity should be greater than 0");
		return false;
	}
	var qobj1=document.getElementById(sid.split(".")[0]+".measurementSheetList[0].no");
	if(!validateMsheet(qobj1))
	{
		return false;
	}

	document.getElementsByName(sid.split(".")[0]+".quantity")[0].value=document.getElementById(sid.split(".")[0]+".msnet").innerHTML;
	mscontent=document.getElementById(sid.split(".")[0]+".mstr").innerHTML;
	document.getElementById(sid.split(".")[0]+".mstd").innerHTML=mscontent;
	document.getElementById(sid.split(".")[0]+".msopen").value="0";
	var mstr=document.getElementById(sid.split(".")[0]+".mstr");
	$(mstr).remove();
	var qobj=document.getElementsByName(sid.split(".")[0]+".quantity")[0];
	if(sid.split(".")[0].indexOf("sorActivities") >= 0)
	{
		calculateEstimateAmount(document.getElementsByName(sid.split(".")[0]+".quantity")[0]);
	}else
	{
		calculateNonSorEstimateAmount(document.getElementsByName(sid.split(".")[0]+".quantity")[0]);
	}
	$(qobj).attr("readonly","readonly");


})






function closeAllViewmsheet()
{
	var open=false;
	$('.classmsopen').each(function (index)
			{

		if($( this ).val()==1)
		{
			var sid=$( this ).attr('id');
			var mscontent="<tr id=\""+sid.split(".")[0]+".mstr\">";
			mscontent=document.getElementById(sid.split(".")[0]+".mstr").innerHTML;
			document.getElementById(sid.split(".")[0]+".mstd").innerHTML=mscontent;
			document.getElementById(sid.split(".")[0]+".msopen").value="0";
			var mstr=document.getElementById(sid.split(".")[0]+".mstr");
			$(mstr).remove(); 
		}
			});
	
}
function openAllViewmsheet()
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
					if(sortable.indexOf("sorActivities") >= 0)
					{
						sorMsArray[idx]=mscontent;
					}
					else
					{
						nonSorMsArray[idx]=mscontent;
					}
				}

			}
		}

			});
	return open;	
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

