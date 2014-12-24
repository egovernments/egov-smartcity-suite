<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>

<head>
<title>Modify supplier Contractors </title>

<script language="javascript">

var pageMode="modify";
var str="";
function getData(){
	document.getElementById('relation_code').focus();
	PageManager.ListService.callListService();
	cntrl = document.getElementById('relation_tdsId');
		cntrl.style.display="none";
	obj = document.getElementById('tbMain');
	document.getElementById('tds').innerHTML="";
	
	loadSelectData('../../commonyui/egov/loadComboAjax.jsp', "eg_partytype","id", "code", " parentid is null ", 'dummy', 'relation_partyTypeId');

	//loadSelectData('../commonyui/egov/loadComboAjax.jsp', "eg_partytype","id", "code", " parentid is not null ", 'dummy', 'relation_subTypeId');
	//loadSelectData('../commonyui/egov/loadComboAjax.jsp', "eg_partytype","id", "code", " parentid is not null ", 'relation_partyTypeId', 'relation_subTypeId');
	//PageManager.ListService.callListService();
	var relationcodeToSearch = PageManager.DataService.getQueryField('relationcodeToSearch');
	if (relationcodeToSearch){
		PageManager.DataService.setQueryField('relationcodeToSearch',relationcodeToSearch);
		PageManager.DataService.callDataService('relationModData');
		var newrecord=document.getElementById('newrec')
		newrecord.style.display = "none";
	} else {
		pageMode="add";
	//	document.getElementById('screenName').innerHTML="Create Supplier/Contractor";
		var mod=document.getElementById('modrec')
		mod.style.display = "none";
	}
	//document.relationModify.relation_code.focus();
	PageManager.DataService.callDataService('getTitle');
}

function beforeRefreshPage(dc){
if(dc.values['serviceID']=='relationModData'){
		document.getElementById('dummy').value=dc.values['relation_partyTypeId'];
		getSubPartyType('dummy','relation_subTypeId');
		}	

	return true;
}
function afterRefreshPage(dc){
/* we will use after enhancement ;allow user to give tds here and use that tds in journal
	if(dc.values['relation_tdsId']){
		cntrl = document.getElementById('relation_tdsId');
		cntrl.style.display="block";
		document.getElementById('tds').innerHTML="TDS<font color=red>*</font>";
	}
*/
	if(dc.values['relation_isActive']=='0')
		document.getElementById('relation_isActive').checked=false;
	else
		document.getElementById('relation_isActive').checked=true;
		
	if(dc.values['serviceID']=='getTitle')
		document.getElementById("relation_createdBy").value=dc.values['current_UserID'];

	return true;
}
function getRelationType(){
/* we will use after enhancement ;allow user to give tds here and use that tds in journal
	var control=document.getElementById('relation_relationTypeId');
	var type=control.options[control.selectedIndex].text;
	cntrl = document.getElementById('relation_tdsId');
	if(type=='Supplier'){
		cntrl.style.display="none";
		document.getElementById('tds').innerHTML="";
		cntrl.removeAttribute('exilMustEnter');
	}else{
		cntrl.style.display="block";
		document.getElementById('tds').innerHTML="TDS<font color=red>*</font>";
		cntrl.setAttribute('exilMustEnter',"true");
	}
	*/
}
function getRelationTypeId()
{
var control2=document.getElementById('relation_relationTypeId');

var control=document.getElementById('relation_partyTypeId');

	var type=control.options[control.selectedIndex].text;
	for(var i=0;i<control2.options.length;i++)
	{
	if(type==control2.options[i].text)
	{
	control2.selectedIndex=i;
	}
}
}
function ButtonPress(name)
{
	var pan = document.getElementById('relation_panno').value;
	var tin = document.getElementById('relation_tinno').value;
	var tempcode = document.getElementById('relation_code').value;
	var tempreltype = document.getElementById('relation_partyTypeId').value;
	if(pan!='' || tin!='')
	{
		var url = "relationValidation.jsp?type=validaUniquePanTinno&panno="+pan+"&tinno="+tin+"&code="+tempcode+"&relationtype="+tempreltype;
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
					if(codes!='')
					{
						alert(codes);
						return false;
					}
					else
					{
						ButtonPressOld(name);
						return true;
					}
               }
           }
	     };
	     req2.open("GET", url, true);
	     req2.send(null);
	     
    }
    else
    {
    	ButtonPressOld(name);
    }
}

function ButtonPressOld(name)
{
	getRelationTypeId();

	if(name.toLowerCase()=='savenew')
	str="new";
	if(name.toLowerCase()=='saveclose')
	str="close";
	if(!isValidUser(2, CookieManager.getCookie('userRole')) ) return false;
	if(!PageValidator.validateForm())
				return false;

	document.getElementById('egUser_id').value = CookieManager.getCookie('currentUserId');

	if(!checkNarration('relation_narration')) return false;
	var control=document.getElementById('relation_relationTypeId');
	var type=control.options[control.selectedIndex].value;

	if(type=='Supplier'){
		var tds=document.getElementById('relation_tdsId');
		if(tds.selectedIndex>=0) {
			tds.options[0].value="null";
			tds.options[0].setAttribute('selected','true');
		}
	}

	if (pageMode == "add"){
		PageManager.UpdateService.submitForm('relationInsert');

	}

	if (pageMode == "modify")
	{
	if(!isValidUser(2, CookieManager.getCookie('userRole')) ) return false;

		document.getElementById('egUser_id').value = CookieManager.getCookie('currentUserId');

		if(!checkNarration('relation_narration')) return false;
		var control=document.getElementById('relation_relationTypeId');
		var type=control.options[control.selectedIndex].value;
		if(type=='Supplier'){
			var tds=document.getElementById('relation_tdsId');
			if(tds.selectedIndex>=0) {
				tds.options[0].value="null";
				tds.options[0].setAttribute('selected','true');
			}
		}

		PageManager.UpdateService.submitForm('relationUpdate');
	}

}

function onClickCancel(){
	window.location="RelationMod_VMC.jsp";
}


function onCancel(){
	window.location="Relation_VMC.jsp";
}

function afterUpdateService(dc)
{
	if(dc.values['serviceID']=='relationUpdate')
	{
	//window.location = "Relation_VMC.jsp?relation_codeToSearch="+dc.values['relation_ID'];
	window.location = "Relation_VMC.jsp";
	}
	if(str=="new")
	{
		window.location = "RelationMod_VMC.jsp";
	}
	if(str=="close")
	{
	window.close();
	}
}
//loads subparty type 
function getSubPartyType(objName1,objName2)
{ 
	loadSelectData('../../commonyui/egov/loadComboAjax.jsp', "eg_partytype","id", "code", " parentid=#1 ", objName1, objName2);	
}



</script>
</head>
<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onLoad="getData();" onKeyDown ="CloseWindow(event);"><!------------------ Header Begins Begins--------------------->
<center>
<br>

<form name="relationModify" >	
<input type="hidden" name="relation_ID" id="relation_ID" >
<input type="hidden" name="relation_lastModified" id="relation_lastModified" >
<input type="hidden" name="egUser_id" id="egUser_id">
<input type="hidden" name="tableName" id="tableName" value="relation">
<input type="hidden" id="dummy" name="dummy" value="dummy">


<table align='center' class="tableStyle" id="table3">
<!------------------ Header Begins Begins--------------------->
  <!-- <table width="993" height="74%" border="0" cellpadding="0" cellspacing="0">
  </table> 
  <table width="100%" border="0" cellpadding="6" cellspacing="0">-->
    <tr>
      <td valign="top" ><!------------Content begins here ------------------>
        <table width="100%" border="0" cellpadding="3" cellspacing="0" id="tbMain" name="tbMain">
          
          <tr >
            <td width="25%" align="right" valign="center" class="labelcell">
              <div align="right">
                Code<span class="leadon">*</span>
              </div>
            </td>
            <td width="25%" class="fieldcell"><input name="relation_code" id="relation_code" maxlength="50" class="fieldinput" size="25" exilmustenter="true" exildatatype="exilAlphaNumeric"></td>
            <td width="25%" align="right" valign="center" class="labelcell">
              <div align="right">
                Name<span class="leadon">*</span>
              </div>
            </td>
            <td width="25%" class="fieldcell"><input name="relation_name" id="relation_name" maxlength="50" class="fieldinput" size="25" exilmustenter="true" exildatatype="exilAnyChar"></td>
          </tr>
          <tr>
            <td align="right" valign="center" class="labelcell">
              <div align="right">
                Address<span class="leadon">*</span>
              </div>
            </td>
            <td class="fieldcell"><input name="relation_address1" class="fieldinput" id="relation_address1" maxlength="150" size="25" exilmustenter="true" exildatatype="exilAnyChar"></td>
            <td align="right" valign="center" class="normaltext">
              <div align="right">
                &nbsp;
              </div>
            </td>
            <td class="fieldinput">&nbsp;</td>
          </tr>
          <tr>
            <td align="right" valign="center" class="normaltext">&nbsp;</td>
            <td class="fieldcell"><input name="relation_address2" class="fieldinput" id="relation_address2" maxlength="150" size="25" exildatatype="exilAnyChar"></td>
            <td align="right" valign="center" class="normaltext">
              <div align="right">
                &nbsp;
              </div>
            </td>
            <td class="fieldinput">&nbsp;</td>
          </tr>
          <tr>
            <td align="right" valign="center" class="labelcell">
              <div align="right">
                City
              </div>
            </td>
            <td class="fieldcell"><input name="relation_city" class="fieldinput" id="relation_city" maxlength="50" size="25" exildatatype="exilAnyChar"></td>
            <td align="right" valign="center" class="labelcell">
              <div align="right">
                Pin
              </div>
            </td>
            <td class="fieldcell"><input name="relation_pin" class="fieldinput" id="relation_pin" maxlength="15" size="25" exildatatype="exilUnsignedInt"></td>
          </tr>
          <tr>
            <td align="right" valign="center" class="labelcell">
              <div align="right">
                Phone
              </div>
            </td>
            <td class="fieldcell"><input name="relation_phone" class="fieldinput" id="relation_phone" maxlength="15" size="25" exildatatype="exilAnyChar"></td>
            <td align="right" valign="center" class="labelcell">
              <div align="right">
                Fax
              </div>
            </td>
            <td class="fieldcell"><input name="relation_fax" class="fieldinput" id="relation_fax" maxlength="15" size="25" exildatatype="exilAnyChar"></td>
          </tr>
          <tr>
            <td align="right" valign="center" class="labelcell">
              <div align="right">
                Mobile
              </div>
            </td>
            <td class="fieldcell"><input name="relation_mobile" class="fieldinput" id="relation_mobile" maxlength="25" size="25" exildatatype="exilAnyChar"></td>
            <td align="right" valign="center" class="labelcell">
              <div align="right">
                E-Mail
              </div>
            </td>
            <td class="fieldcell"><input name="relation_email" class="fieldinput" id="relation_email" maxlength="25" size="25" exildatatype="exilEmail"></td>
          </tr>
          <tr>
            <td align="right" valign="center" class="labelcell" >
              <div align="right">Contact person</div>
            </td>
            <td class="fieldcell"><input name="relation_contactPerson" class="fieldinput" id="relation_contactPerson" maxlength="50" size="25" exildatatype="exilAnyChar"></td>
            <td align="right" valign="center" class="labelcell"  rowspan="2">
              <div align="right">Narration</div>
            </td>
            <td class="fieldcelldesc"  rowspan="2"><textarea class="narrationfieldinput " id="relation_narration" name="relation_narration" rows="3" cols="40" exildatatype="exilAnyChar" maxlength="250"></textarea></td>
          </tr>
          <tr >
            <td align="right" valign="center" class="labelcell">
              <div align="right">IFSC/MICR Code</div>
            </td>
			<td class="fieldcell" ><input name="relation_ifscCode" class="fieldinput" id="relation_ifscCode" maxlength="50" size="25" exildatatype="exilAlphaNumeric"></td>
          </tr>
           
           <tr >
            <td align="right" valign="center" class="labelcell">
              <div align="right">
                PAN No
              </div>
            </td>
            <td class="fieldcell"><input name="relation_panno" class="fieldinput" id="relation_panno" maxlength="50" size="25" exildatatype="exilAlphaNumeric"></td>
            <td align="right" valign="center" class="labelcell">
              <div align="right">
                TIN No
              </div>
            </td>
            <td class="fieldcell"><input name="relation_tinno" class="fieldinput" id="relation_tinno" maxlength="50" size="25" exildatatype="exilAlphaNumeric"></td>
          </tr>
          <tr>
            <td align="right" valign="center" class="labelcell">
              <div align="right">
                Bank Name
              </div>
            </td>
            <td class="fieldcell"><input name="relation_bankname" class="fieldinput" id="relation_bankname" maxlength="50" size="25" exildatatype="exilAnyChar"></td>
            <td align="right" valign="center" class="labelcell">
              <div align="right">
                Bank Account
              </div>
            </td>
            <td class="fieldcell"><input name="relation_bankaccount" class="fieldinput" id="relation_bankaccount" maxlength="50" size="25" exildatatype="exilAnyChar"></td>
          </tr>
          <tr style="Display: none" >
            <td>
              <div align="right" valign="center" class="labelcell">
                Type <span class="leadon">*</span>
              </div>
            </td>
            <td class="smallfieldcell"><select class="fieldinput" name="relation_relationTypeId" id="relation_relationTypeId" onchange="getRelationType();" exilmustenter="true" exillistsource="selectRelationType" >
              </select></td>
            <td>
              <div align="right" valign="center" class="labelcell" id="tds" name="tds">
                TDS<span class="leadon">*</span>
              </div>
            </td>
            <td class="smallfieldcell"><select class="fieldinput" name="relation_tdsId" id="relation_tdsId" style="DISPLAY: none" exillistsource="selectTds">
              </select></td>
          </tr>
          	<tr>
									<td ><div align="right" valign="center" class="labelcell" >Party Type  <span class="leadon">*</span>
 </div></td>
									<!-- 	<td><select class="fieldinput" name="relation_partyTypeId" id="relation_partyTypeId" onchange="getSubPartyType('relation_partyTypeId','relation_subTypeId');" exilMustEnter="true" > -->
									<td class="smallfieldcell"><select class="fieldinput" name="relation_partyTypeId" id="relation_partyTypeId" onchange="getSubPartyType('relation_partyTypeId','relation_subTypeId');" exilMustEnter="true"> 
										</select></td>
										<td><div align="right" valign="center" class="labelcell">Sub type</div></td>
										<td class="smallfieldcell"><select class="fieldinput" name="relation_subTypeId" id="relation_subTypeId" >
										</select></td>
		
							</tr>	

          <tr >
            <td align="right" valign="center" class="labelcell">
              <div align="right">
                Active
              </div>
            </td>
            <td class="fieldinput"><input type="checkbox" name="relation_isActive" id="relation_isActive" value="1" checked></td>
            <td align="right" valign="center" class="normaltext">&nbsp;</td>
            <td class="fieldinput">&nbsp;</td>
          </tr>
          <tr style="display:none">
            <td align="right" valign="center" class="labelcell">Created By</td>
            <td class="fieldcell"><input type="text" id="relation_createdBy" name="relation_createdBy" size="20"></td>
            <td align="right" valign="center" class="normaltext">
              <div align="right">
                &nbsp;
              </div>
            </td>
            <td class="fieldinput">&nbsp;</td>
          </tr>
          <tr>
            <td height="25" colspan="4" valign="bottom" class="smalltext">
              <p class="smalltext"><span class="leadon">*</span> - Mandatory
              Fields</p>
            </td>
          </tr>
          <tr>
            <td colspan="4" align="middle"><!-- Buttons Start Here -->
              <table border="0" name="modrec" id="modrec" cellpadding="0" cellspacing="0">
                <tr>
                  <td align="right">
                  <input type=button class=button onclick="ButtonPress('saveclose');" href="#" value="Submit"></td>
                  
                  <td align="right">
                  <input type=button class=button onclick="onCancel();" href="#" value="Cancel"></td>
                  
                  <td align="right">
                  <input type=button class=button onclick="window.close();" href="#" value="Close"></td>
                </tr>
              </table>
              <!-- Buttons End Here -->

              <table border="0" name="newrec" id="newrec" cellpadding="0" cellspacing="0">
                <tr name="trNew" id="trNew">
                  <td align="right">
                  <input type=button class=button id="savenNew" onclick="ButtonPress('savenew');" href="#" value="Save&amp; New"></td>
                  
                  <td>
                  <input type=button class=button id="savenClose" onclick="ButtonPress('saveclose');" href="#" value="Save&amp; Close"></td>
                  
                  <td align="right">
                  <input type=button class=button onclick="onClickCancel();" href="#" value="Cancel"></td>
                  
                  <td align="right">
                  <input type=button class=button onclick="window.close();" href="#" value="Close"></td>
                </tr>
              </table>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
  <!-- Buttons End Here --><!------------ Content ends here ------------------><!------------Right Navigation Ends here------------------><!---------------- Footer begins here ----------><!---------------- Footer ends here ---------->
</form>

</center>

</body>

</html>
