<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<!-- Inclusion of the CSS files that contains the styles -->
<script language="javascript">
window.dataRetreived=false;
var mainGridObj=new Array();


function onClickModify(){
	//   ***1 only FO***,  ***2 FO***,  ***Admin, 3 All***    //
	if(!isValidUser(2, CookieManager.getCookie('userRole')) ) return false;

	 if(!window.dataRetreived){
	alert(" Get Details ");
		return;
	}
	window.open("WorksDetailAdd_VMC.jsp?worksDetail_code="+document.getElementById('worksDetail_codeToSearch').value,"","height=650,width=900,scrollbars=yes,left=0,top=0,status=yes");
	//window.location="WorksDetailAdd.htm?worksDetail_code="+document.getElementById('worksDetail_codeToSearch').value;
}
function openSearch(obj)
{
	 var a = new Array(2);
	 var sRtn = showModalDialog("Search.html?tableNameForCode=worksDetail","","dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=300pt;status=no;");
	 	// alert(sRtn);
	 if ( sRtn != '' )
	   {
		 a = sRtn.split("`~`");
	 		// alert(a[0]);
	 		// alert(a[2]);
		var x =PageManager.DataService.getControlInBranch(obj.parentNode,'worksDetail_codeToSearch');
		//var y =PageManager.DataService.getControlInBranch(obj.parentNode.parentNode,'worksDetail_name');
		x.value = a[0];
	 		document.getElementById("worksDetail_codeToSearch").value=a[0];
	 		document.getElementById("worksDetail_SearchId").value=a[2];
		getData();
	   }
	 }
function getCode()
{
var codeDetail=document.getElementById("worksDetail_codeToSearch").value;
//alert(codeDetail);
if(codeDetail)
{
PageManager.DataService.setQueryField('worksDetail_codeDetail',codeDetail);
PageManager.DataService.callDataService('worksDetailCodeId');
}
else
{alert ("Please Enter PO Code Correctly !!!");
clearData();
return;
}

}
function beforeRefreshPage(dc){


	/*
	if(dc.values['serviceID']=='worksDetailDataView')
	{
		//alert("Inside before ref");
		
		if(dc.values['po_Type']=='2')
		{
		PageManager.DataService.callDataService("tds_List");
		document.getElementById('tds_List').setAttribute('exilListSource',"tds_List");
		}
	}
	*/
	
	/*
	if(dc.values['serviceID']=='tds_List')
	{
			mainGridObj=dc.grids['tds_List'];
					
			
	}
	*/


if(dc.values['serviceID']=='worksDetailCodeId')
	{
	if(dc.values['worksDetail_codeToSearch']!='undefined' && dc.values['worksDetail_SearchId']!='undefined')
		{
	document.getElementById("worksDetail_codeToSearch").value=dc.values['worksDetail_codeToSearch'];
	document.getElementById("worksDetail_SearchId").value=dc.values['worksDetail_SearchId'];
	getData();
	}
	}
	return true;

}

function afterRefreshPage(dc){
window.dataRetreived=true;
// added by iliyaraja
	if(dc.values['serviceID']=='worksDetailDataView')
	{
	if(dc.values['isFixedAsset']=='1')
		document.getElementById('isFixedAssetValue').innerHTML = 'Yes';
	else
		document.getElementById('isFixedAssetValue').innerHTML = 'No';

		if(dc.values['worksDetail_code']=='undefined')
		{
		document.getElementById("worksDetail_codeToSearch").value="";
		alert("Enter Procurement Order Code Correctly !!!");
		}
	
		/*
		if(dc.values['po_Type']=='2' && dc.values['worksDetail_code']!="")
		{
			//alert("Inside after ref --view");
			var wo_Id=dc.values['worksDetail_code'];
			PageManager.DataService.setQueryField('wo_Id',wo_Id);
			PageManager.DataService.callDataService('worksDetailTdsData');
		}
		*/
			
		if(dc.values['po_Type']=='1' && dc.values['worksDetail_code']!="")
		document.getElementById('trWorkTypeSubType').style.display="none";
		else
		document.getElementById('trWorkTypeSubType').style.display="block";
				
		
	}
	/*
	if(dc.values['serviceID']=='worksDetailTdsData')
	{
		var selectedGridObj=dc.grids['worksDetailTdsData'];
		//alert(selectedGridObj);
		var j=0;
		//alert("mainGridObj length"+mainGridObj.length);
		
		if(selectedGridObj!=undefined)
		{
					
			//alert("If not undefined");
			for(var i=0;i<selectedGridObj.length;i++)
			{

				//alert("After ref Grid value selected is-->"+selectedGridObj[i]);

				var arrData=selectedGridObj[i];
				 arrData=arrData[j].split(",");

				//alert("after split");
				//alert("arrData[j]"+arrData[j]);
				highLight(document.getElementById("tds_List"),arrData[j]);

			}// for
		}
	}
	*/
		
		
	

	if(dc.values['worksDetail_isActive']=='1')
		document.getElementById('worksDetail_isActive').innerHTML='Active';
		else
		document.getElementById('worksDetail_isActive').innerHTML='Cancel';

		var worksDetail_orderDate=dc.values['worksDetail_orderDate'];
		if(!worksDetail_orderDate)	return false;

		worksDetail_orderDate=formatDate(worksDetail_orderDate);
		document.getElementById('worksDetail_orderDate').innerHTML=worksDetail_orderDate;

		var worksDetail_sanctionDate=dc.values['worksDetail_sanctionDate'];
		worksDetail_sanctionDate=formatDate(worksDetail_sanctionDate);
		document.getElementById('worksDetail_sanctionDate').innerHTML=worksDetail_sanctionDate;

		var wards=dc.values['worksDetail_levelOfWork'];
		if(wards == 'Ward' || wards == 'ward')
		document.getElementById('wrdName').innerHTML="Ward No";
		else
		document.getElementById('wrdName').innerHTML="";

		var type=dc.values['worksDetail_type'];
		var potype=dc.values['po_Type'];
		//alert(type);
		if(type && potype!='1'){
		document.getElementById('typeName').innerHTML="Type Of Work";
		}else{
		document.getElementById('typeName').innerHTML="";
		document.getElementById('hideworksDetail_type').style.display="none";
		}
		window.dataRetreived=true;
		return true;

} // after ref page
/*
function highLight(obj,val){

	if(obj==null)return false;
	for(var i=0; i<=mainGridObj.length; i++){
	
	//alert("obj.options[i].value"+obj.options[i].value);
	//alert("val"+val);
		if(obj.options[i].value==val){
		
		//alert("Inside selected tds");
		
			obj.options[i].setAttribute('selected',true);
			//obj.selectedIndex = i;
			return true;
		}
	}
}
*/

/*function formatDate(dateText){
	var months = ["NA","Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"];

	dateText=dateText.substring(0,10);
	var dateArray= dateText.split('-');
	var newDate=dateArray[2]+"-"+ months[parseInt(dateArray[1], 10)] +"-"+dateArray[0];
	return newDate;
}*/

function onloadFunction(){
	str = window.location.search;
	var mode = PageManager.DataService.getQueryField('showMode');
	var po_Type = PageManager.DataService.getQueryField('po_Type');
	//alert("Inside View page"+po_Type);
	
	/*
	if(po_Type==2)
	{
	document.getElementById('hideTDS').style.display="block";
	PageManager.DataService.callDataService("tds_List");
	document.getElementById('tds_List').setAttribute('exilListSource',"tds_List");
	}
	else
	document.getElementById('hideTDS').style.display="none";
	*/
		

	//var POcode1 = PageManager.DataService.getQueryField('POcode1');
	
	if (mode == "modify")
	{
	  // document.getElementById('screenName').innerHTML="Modify Procurement Order";
       document.getElementById('trModify').style.display='block';
	   document.getElementById('trView').style.display='none';
	}else{
	   document.getElementById('trModify').style.display='none';
	   document.getElementById('trView').style.display='block';
	}
		/*
		if(POcode1)
		{
		alert(POcode1);
		document.getElementById('worksDetail_codeToSearch').value=POcode1;
		PageManager.DataService.setQueryField('worksDetail_codeToSearch',POcode1);
		PageManager.DataService.setQueryField('worksDetail_code',POcode1);
	   	PageManager.DataService.callDataService('worksDetailData');
		}*/
	
	var codeToSearch=PageManager.DataService.getQueryField('worksDetail_code');
	if (str && codeToSearch)
	{
	PageManager.DataService.setQueryField('worksDetail_code',codeToSearch);
	PageManager.DataService.callDataService('worksDetailDataView');
	}
	document.getElementById('worksDetail_codeToSearch').focus();
}
function clearData()
{
	document.getElementById('worksDetail_orderDate').innerHTML="";
	document.getElementById('worksDetail_totalValue').innerHTML="";
	document.getElementById('worksDetail_bankGuarantee').innerHTML="";
	document.getElementById('worksDetail_wardId').innerHTML="";
	document.getElementById('worksDetail_relationId').innerHTML="";
	document.getElementById('worksDetail_advancePayable').innerHTML="";
	document.getElementById('worksDetail_authorizedBy').innerHTML="";
	document.getElementById('worksDetail_levelOfWork').innerHTML="";
	document.getElementById('worksDetail_securityDeposit').innerHTML="";
	document.getElementById('worksDetail_retention').innerHTML="";
	document.getElementById('worksDetail_fundId').innerHTML="";
	document.getElementById('worksDetail_fundSourceId').innerHTML="";
	document.getElementById('worksDetail_sanctionno').innerHTML="";
	document.getElementById('worksDetail_sanctionDate').innerHTML="";
	document.getElementById('worksDetail_isActive').innerHTML="";
	document.getElementById('worksDetail_type').innerHTML="";
	document.getElementById('worksDetail_name').innerHTML="";
	document.getElementById('worksDetail_remarks').innerHTML="";
}
function getData(){
	clearData();
	//clearList();
	//document.getElementById('worksDetail_fundingFrom').innerHTML="";

	var codeToSearch=document.getElementById('worksDetail_SearchId').value;
	//alert(codeToSearch);
	   if(codeToSearch == null || codeToSearch == ""){
		alert ("Please Enter PO Code Correctly !!!");
	      }
	else{
		PageManager.DataService.setQueryField('worksDetail_code',codeToSearch);

		PageManager.DataService.callDataService('worksDetailDataView');
	}
}
/*
function clearList()
	{
	  var toObj=document.getElementById('selected_Asset');
		for(var i=toObj.length-1;i>=0;i--)
		{
			toObj.remove(i);
		}
	}
	*/

</script>
</head>
<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onload="javascript:onloadFunction();" onKeyDown ="CloseWindow(window.self);">
<center>
<br>
<form name="WorksDetailEnq">
<input type="hidden" name="worksDetail_SearchId" id="worksDetail_SearchId">

	<table align='center' class="tableStyle" id="table3"> 
<!-- <table width="100%" height="74%" border="0" cellpadding="0" cellspacing="0"> 
	  </table>
	<table width="100%" border=0 cellpadding="6" cellspacing="0">
			<tr>
			<td valign="top"><!------------Content begins here ------------------>
		
					<!--	<table width="100%" border=0 cellpadding="3" cellspacing="0"> -->
							
							<tr>
							  <td width="168" class="labelcell"><div align="right" ><bean:message key="WorksDetailEnq.Code"/><br>Code<span class="leadon">*</span></div></td>
							   <td width="280" class="fieldcellforsingletd">
								<input name="worksDetail_codeToSearch" class="fieldinput" id="worksDetail_codeToSearch" size="10"  exilMustEnter="true" exilDataType="exilAlphaNumeric"> <IMG onclick=openSearch(this); height=22 src="../../images/plus1.gif"
           					 width=25 align=top border=0><input type=button value="Go" class="leadon" onclick="getCode();">
							  </td>
						  <td width="225"><div align="right" class="labelcell"><bean:message key="WorksDetailEnq.Name"/><br>Name</div></td>
							  <td width="207"><br><span id="worksDetail_name" name="worksDetail_name" class="displayData" width="213">&nbsp;</span></td>
							</tr>

							<tr>
							  <td width="168"><div align="right" class="labelcell"><bean:message key="WorksDetailEnq.OrderDate"/><br>Order Date</div></td>
							  <td width="280"><br><span id="worksDetail_orderDate" name="worksDetail_orderDate" class="displayData">
                              </span></td>

							  <td width="225"><div align="right" class="labelcell"><bean:message key="WorksDetailEnq.TotalValue"/><br>Total Value</div></td>
							  <td width="207"><br><span id="worksDetail_totalValue" name="worksDetail_totalValue" class="displayData"></span></td>
							</tr>
							<tr>
							<td width="168"><div align="right" class="labelcell"><bean:message key="WorksDetailEnq.Narration"/><br> Narration</div></td>
							  <td width="280"><br><span id="worksDetail_bankGuarantee" name="worksDetail_bankGuarantee" class="displayData"></span></td>
								<td width="214" ><div align="right" class="labelcell"><bean:message key="WorksDetailEnq.Remarks"/><br>Remarks</div></td>
								<td width="203"><br><span name="worksDetail_remarks" class="displayData" id="worksDetail_remarks" ></span> </td>
							</tr>
							<tr>
							 <td align="right" class="labelcell" width="168"><bean:message key="WorksDetailEnq.AdvancePayable"/><br>Advance Payable</td>
							 <td width="280"><br><span id="worksDetail_advancePayable" name="worksDetail_advancePayable" class="displayData"></span></td>
							 <td width="225"><div align="right" class="labelcell"><bean:message key="WorksDetailEnq.AuthorizedBy"/><br>Authorized By</div></td>
							  <td width="207"><br><span id="worksDetail_authorizedBy" name="worksDetail_authorizedBy" class="displayData"></span></td>
						</tr>

						<tr>
							  <td width="168"><div align="right" class="labelcell"><bean:message key="WorksDetailEnq.LevelofWork"/><br>Level of Work</div></td>
							  <td width="280"><br><span id="worksDetail_levelOfWork" name="worksDetail_levelOfWork" class="displayData"></span></td>
							  <td width="225"><div align="right" class="labelcell" id="wrdName"></div></td>
							  <td width="207"><span id="worksDetail_wardId" name="worksDetail_wardId" class="displayData"></span></td>
						</tr>
							<tr>
                        <td width="168"><div align="right" class="labelcell"><bean:message key="WorksDetailEnq.SanctionDate"/><br>Department</div></td>
						 <td width="280"><br><span id="worksDetail_departmentId" name="worksDetail_departmentId" class="displayData">
                              </span></td>
							

                      
						</tr>
						<tr id="trWorkTypeSubType">
							  <td width="168"><div align="right" class="labelcell"><bean:message key="WorksDetailEnq.LevelofWork"/><br>WorkType</div></td>
							  <td width="280"><br><span id="worksDetail_workCategory" name="worksDetail_workCategory" class="displayData"></span></td>
							  <td width="225"><div align="right" class="labelcell" id="wrdName">SubType</div></td>
							  <td width="207"><span id="worksDetail_subCategory" name="worksDetail_subCategory" class="displayData"></span></td>
						</tr>

						<tr>							  <td width="168"><div align="right" class="labelcell"><bean:message key="WorksDetailEnq.AdvancedTo"/><br>Awarded To</div></td>
							  <td width="280"><br><span id="worksDetail_relationId" name="worksDetail_relationId" class="displayData"></span></td>
							  <td width="225"><div align="right" class="labelcell" id="typeName" name="typeName"></div></td>
							  <td id="hideworksDetail_type" width="207"><br><span id="worksDetail_type" name="worksDetail_type" class="displayData"></span></td>
						</tr>
						<tr>
							  <td width="168"><div align="right" class="labelcell"><bean:message key="WorksDetailEnq.Fund"/><br>Fund</div></td>
							  <td width="280"><br><span id="worksDetail_fundId" name="worksDetail_fundId" class="displayData"></span></td>
							  <td width="225"><div align="right" class="labelcell"><bean:message key="WorksDetailEnq.FinancingSource"/><br>Financing Source</div></td>
							  <td width="207"><br><span id="worksDetail_fundSourceId" name="worksDetail_fundSourceId" class="displayData"></span></td><!--			   <td width="168"><div align="right" class="labelcell"></div></td>
							  <td width="152"><div align="right" class="labelcell"></div></td>    -->

						</tr>

							<tr>
							  <td width="168"><div align="right" class="labelcell"><bean:message key="WorksDetailEnq.scheme"/><br>
								Scheme</div></td>
							  <td width="280">
								<br><span id="worksDetail_scheme" name="worksDetail_scheme" class="displayData"></span></td>
							  <td width="225"><div align="right" class="labelcell"><bean:message key="WorksDetailEnq.subscheme"/><br>
								Sub Scheme</div></td>
							  <td width="207">
								<br><span id="worksDetail_subscheme" name="worksDetail_subscheme" class="displayData"></span></td>
							</tr>

							<tr>
							  <td width="168"><div align="right" class="labelcell"><bean:message key="WorksDetailEnq.Retention"/><br>Retention</div></td>
							  <td width="280"><br><span id="worksDetail_retention" name="worksDetail_retention" class="displayData"></span></td>
							   <td width="225"><div align="right" class="labelcell"><bean:message key="WorksDetailEnq.SecurityDeposit"/><br>Security Deposit</div></td>
							  <td width="207"><br><span id="worksDetail_securityDeposit" name="worksDetail_securityDeposit" class="displayData"></span></td>
						</tr>

							<tr>
							<td width="168"><div align="right" class="labelcell"> <bean:message key="WorksDetailEnq.SanctionNo"/><br>Sanction No</div></td>
							  <td width="280"><br><span id="worksDetail_sanctionno" name="worksDetail_sanctionno" class="displayData"></span></td>
							   <td align="right" class="labelcell" width="225"><bean:message key="WorksDetailEnq.Status"/><br>Status</td>
							 <td width="207"><br><span id="worksDetail_isActive" name="worksDetail_isActive" class="displayData"></span></td>


							</tr>
							<tr>
                        <td width="168"><div align="right" class="labelcell"><bean:message key="WorksDetailEnq.SanctionDate"/><br>Sanction Date</div></td>
						 <td width="280"><br><span id="worksDetail_sanctionDate" name="worksDetail_sanctionDate" class="displayData">
                              </span></td>
							   <!-- td align="right" class="labelcell" width="225"><DIV class=labelcell align=right>TDS</DIV></td>
							 <td width="207"><span id="worksDetail_tds" name="worksDetail_tds" class="displayData"></span></td -->

                        <td width="168"><div align="right" class="labelcell">Is Fixed Asset</div></td>
			<td width="280"><br><div id="isFixedAssetValue" name="isFixedAssetValue" class="displayData"></div></td>
</tr>



							<!--<tr>
		                        <td width="168"><div align="right" class="normaltext">Asset List:</div></td>
								 <td width="280">
									<select name="selected_Asset" id="selected_Asset"    multiple size="5" exilListSource="selected_Asset" style="width:380;HEIGHT: 80; COLOR: #000000;background-color: #f2f2f2 " readonly ><option></option>
												 </select></td>
								 <td align="right" class="normaltext" width="225">

								 <td width="207"><span id="worksDetail_tds" name="worksDetail_tds" class="displayData"></span></td>


							</tr> -->
							
							<!--
							<tr class="row2" id="hideTDS">
								<td align="right" valign="center" >
								 <DIV align=right class="labelcell" >TDS&nbsp;</DIV>
								</td>
								<td>
								<SELECT   id="tds_List" name="tds_List"  class="fieldinput"  
								 multiple style="WIDTH: 139px; HEIGHT: 122px" exilListSource="tds_List">
								</SELECT>&nbsp;
								</td>
								<td >&nbsp;</td>
								<td >&nbsp;</td>
							</tr>
							-->

                             <tr>

						<td height="25" colspan="4" valign="bottom" class="smalltext" width="898"><p class="smalltext">&nbsp;</p>
						</td>
						</tr>
							<tr>
							<td colspan="4" align="middle" width="898"><!-- Buttons Start Here -->
								<table border="0" cellpadding="0" cellspacing="0">
						<tr name="trModify" id="trModify">
						<td align="right">
						<input type=button class=button onclick=onClickModify() href="#" value="Modify"></td>

						<td align="right">
						<input type=button class=button onclick=window.close() href="#" value="Close"></td>
						<tr name="trView" id="trView">

						<td align="right">
						<input type=button class=button onclick=window.close() href="#" value="Close"></td>
						</tr>

						</table><!-- Buttons End Here -->
					</td>
				</tr>


                              </table>
							 </td>
						  </tr>

				</table><!------------ Content ends here ------------------>
					</TD></TR></TABLE></TD><!------------Right Navigation Ends here------------------>
					</TR></TABLE><!---------------- Footer begins here ----------><!---------------- Footer ends here ---------->
</form>
</center>
</body>
</html>
