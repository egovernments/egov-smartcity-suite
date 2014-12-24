<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<title>
	Scheme Master
</title>
<script language="javascript">
pageMode="modify";
var sameWindow=0;
var str="";
function getData()
{
PageManager.ListService.callListService();
PageValidator.addCalendars();
document.getElementById('schemecode').focus();
	var mode = PageManager.DataService.getQueryField('showMode');
	var cgn=PageManager.DataService.getQueryField('code');
	//added By abdulla
	if(cgn && (mode=="view"))
	 {
		isDrillDown=true;
		PageManager.DataService.setQueryField('code',cgn);
		PageManager.DataService.callDataService('schemeList');
		
		//alert("inside");
		PageManager.DataService.callDataService('getschemeDetails');
		document.getElementById('trNew').style.display ="none";
		document.getElementById('adddeleterow').style.display='none';
		//disableControls(0,true);
		for(var i=0;i<document.forms[0].length;i++)
				{
					if(document.forms[0].elements[i].value != "  Back  ")
					{
						document.forms[0].elements[i].disabled =true;
					}													
				}

	 }
	 if(cgn && (mode=="modify"))
	 {
		PageManager.DataService.setQueryField('code',cgn);
		PageManager.DataService.callDataService('schemeList');
		PageManager.DataService.callDataService('getschemeDetails');
		document.getElementById('schemecode').readOnly =true;
		document.getElementById('schemename').readOnly =true;
		document.getElementById('fund_id').focus();
		document.getElementById('savenNew').style.display ="none";
		document.getElementById('savenNewl').style.display ="none";
		
	 }

	//End

}
function disableControls(frmIndex, isDisable){
			for(var i=0;i<document.forms[frmIndex].length;i++)
				document.forms[frmIndex].elements[i].disabled =isDisable;
		}
	function fillDate1(objName)
	{
		PageValidator.showCalendar('selectedDate');
		document.getElementById(objName).value=document.getElementById('selectedDate').value;
		document.getElementById('selectedDate').value = "";
	}

 function getFunId(event) 
 {
		  		clearTable(document.getElementById("completeTable"));
				PageManager.DescService.onblur(event);
}
function Trim(str)
	{  
		while(str.charAt(0) == (" ") )
  			{  str = str.substring(1);
 			 }
 			 while(str.charAt(str.length-1) == " " )
 			 {  str = str.substring(0,str.length-1);
 			 }
 		 return str;
	}	

function ButtonPress(name)
{
	if(name.toLowerCase()=='savenew')
	{
           str="new";
		   sameWindow=1;
	}
    else if(name.toLowerCase()=='saveclose')
	{
            str="close";
			sameWindow=0;
	}
	var buttonvalue=PageManager.DataService.getQueryField('showMode');

	 if (buttonvalue=="modify")
	{
		document.getElementById('modeOfExec').value="modify";
		document.getElementById('codeofscheme').value=PageManager.DataService.getQueryField('code');
	}
	if(!PageValidator.validateForm())
				return false;
	 strtDate = document.getElementById('startDate').value;
	 endDate = document.getElementById('endDate').value;
	 var schemecode=document.getElementById('schemecode').value;
	 var schemename=document.getElementById('schemename').value;
	 var isactive=document.getElementById('isActive').value;
	 var fund_id=document.getElementById('fund_id').value;
	 var description=document.getElementById('description').value;

	 var dbDate=document.getElementById('databaseDate').value;
	 if(compareDate(formatDateToDDMMYYYY1(endDate),formatDateToDDMMYYYY1(dbDate)) == -1 )
		{
			alert('End Date should be less than or equal to '+dbDate);
			document.getElementById('endDate').focus();
			return false;
		}
	 if(strtDate.length !=0 && endDate.length !=0)
	   {
		if( compareDate(formatDateToDDMMYYYY1(strtDate),formatDateToDDMMYYYY1(endDate)) == -1 )
		{
			alert('Start Date can not be greater than End Date');
			document.getElementById('startDate').focus();
			return false;
	    }

	   PageManager.DataService.setQueryField('startDate',strtDate);
	   PageManager.DataService.setQueryField('isActive',isactive);
	   PageManager.DataService.setQueryField('endDate',endDate);
	   PageManager.DataService.setQueryField('schemename',schemename);
	   PageManager.DataService.setQueryField('schemecode',schemecode);
	   PageManager.DataService.setQueryField('fund_id',fund_id);
	   PageManager.DataService.setQueryField('description',description);
	   //PageManager.DataService.setQueryField('modeOfExec',buttonvalue);
	  }
	 if(strtDate.length !=0 && endDate.length ==0){
	  PageManager.DataService.setQueryField('startDate',formatDate3(strtDate));
	  }
	  if(endDate.length !=0 && strtDate.length ==0){
		 PageManager.DataService.setQueryField('endDate',formatDate3(endDate));
	  }
	var scheme="";
	 PageManager.UpdateService.submitForm('getSchemeMaster');				

}
function onClickCancel(){
	window.location="${pageContext.request.contextPath}/HTML/AddScheme.jsp?showMode=new";
}

function submitAndNew(){
   sameWindow=1;
   onClickModify();
}

function onClickCancelMod(){
	window.location="${pageContext.request.contextPath}/HTML/schemeEnq.htm";

}

function onClickModify(){
	//   ***1 only FO***,  ***2 FO***,  ***Admin, 3 All***    //
	//alert(document.getElementById('modeOfExec').value);
	if(document.getElementById('modeOfExec').value=="new")
	{

		doInsert();
	}
	if(document.getElementById('modeOfExec').value=="modify")
	{

		doUpdate();
	}

}

function beforeRefreshPage(dc){

	return true;
}
function afterRefreshPage(dc){
	return true;
}
function doInsert(){
	PageManager.UpdateService.submitForm('schemeInsert');
}
function doUpdate(){
		sameWindow = 2;
	PageManager.UpdateService.submitForm('schemeUpdate');
}

function afterUpdateService(dc)
{
	//alert(sameWindow);
	if(sameWindow == 1){
	  window.location="${pageContext.request.contextPath}/HTML/AddScheme.jsp?showMode=new";
      }else if(sameWindow == 0)
	  {window.close();}
	  else if(sameWindow == 2) {
	window.location="${pageContext.request.contextPath}/HTML/schemeEnq.htm?showMode=modifyScheme";
	}
	sameWindow=0;
	return true;
}

</script>
</head><body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onload="getData()" onKeyDown ="CloseWindow(window.self);"><!------------------ Header Begins Begins--------------------->
 <jsp:useBean id = "SchemeMasterBean" scope ="session" class ="com.exilant.eGov.src.master.SchemeMasterBean"/>
<jsp:setProperty name = "SchemeMasterBean" property="*"/>
<center>
<br>
<form name="addscheme">
<input type="hidden" name="databaseDate"  id="databaseDate">
<input type="hidden" name="modeOfExec" id="modeOfExec">
<input type="hidden" name="codeofscheme" id="codeofscheme">
<input type="hidden" name="selectedDate" id="selectedDate">

<table align='center' class="tableStyle" id="table3">
		<tr><td>
			<table align='center' id="table4">

	<!-- <table width="100%" border=0 cellpadding="6" cellspacing="0"> -->
		<tr>
			<td valign="top" ><!------------Content begins here ----------------
				<table width="976" border=0 cellpadding="3" cellspacing="0">-->
					<tr>
						<td align="right" valign="center" class="labelcell" width="229">
						<div align="right"><bean:message key="addscheme.schemecode"/>Scheme Code<span class="leadon">*</span></div></td>
						<td width="731" class="fieldcell">
						<input name="schemecode" class="fieldinput" id="schemecode" exilDataType="exilAnyChar" exilMustEnter="true" maxlength=20 exilDataSource="billCodeVerification" exilDescField="scheme_code" size=20>
						</td>
					<!--</tr>
					<tr>-->
						<td align="right" valign="center"  width="229" >
						<div align="right" class="labelcell"><bean:message key="addscheme.schemename"/>Scheme Name<span class="leadon">*</span></div></td>
						<td width="731" class="fieldcell">
						<input name="schemename" class="fieldinput" id="schemename" exilDataType="exilAnyChar" exilMustEnter="true" maxlength=20 exilDataSource="billCodeVerification" exilDescField="scheme_name" size=20>
						</td>

					</tr>
					<tr>
						<td><div align="right" valign="center" class="labelcell"><bean:message key="addscheme.fund_id"/>Fund Name<span class="leadon">*</span></div></td>
						<td align="left" class="smallfieldcell">
							<SELECT name="fund_id" id="fund_id" class="fieldinput" exilListSource="fundNameList" exilMustEnter="true"></SELECT>
						</td>
						<td><div align="right" valign="center" class="normaltext">&nbsp;</div></td>
						<td><div align="right" valign="center" class="normaltext">&nbsp;</div></td>
					</tr>
					<tr>
						<td align="right" valign="center" class="labelcell" width="229"><div align="right"><bean:message key="addscheme.startDate"/>Start Date<span class="leadon">*</span></div></td>

						<td width="731" class="smallfieldcell">
							<input onkeyup="DateFormat(this,this.value,event,false,'3')" name="startDate" id="startDate" class="datefieldinput"  exilDataType="exilAnyDate" exilCalendar="true" exilMustEnter="true"></td>
					<!--</tr>
					<tr>-->
						<td align="right" valign="center"  width="229"><div class="labelcell"><bean:message key="addscheme.endDate"/>End Date<span class="leadon">*</span></div></td>
						<td width="731" class="smallfieldcell">
							<input onkeyup="DateFormat(this,this.value,event,false,'3')" name="endDate" id="endDate" class="datefieldinput"  exilDataType="exilAnyDate" exilCalendar="true" exilMustEnter="true"></td>
					</tr>
                    <tr>
							  <td><div align="right" valign="center" class="labelcell"><bean:message key="addscheme.isActive"/>Active</div></td>
							  <td><input type="checkbox" name="isActive" id="isActive" value="1" checked>
								</td>
							  <td></td>
							  <td></td>
					</tr>
		  			<tr>
						<td align="right" valign="center" class="labelcell" width="229"><div align="right"><bean:message key="addscheme.description"/>Description</div></td>
						<td width="731" colspan="3" class="fieldcelldesc">
                                <TEXTAREA id="description" class="narrationfieldinput" name="description" exilTrimLength="20" maxlength=250 rows=3 cols=40 exilDataType="exilAnyChar"></TEXTAREA>
						</td>
					</tr>
        <tr><td id="auto-row">&nbsp;<td/></tr>
					<!--<tr>
                    	<td  colspan=4 width="884">&nbsp;</td>
                    </tr>-->
                    <tr>
			<td height="25" colspan="4" valign="bottom" class="smalltext" width="968"><p class="smalltext"><span class="leadon">*</span>
					- Mandatory Fields</p>
			</td>
		</tr>
			
			</table></TD></TR>

		
			<table>
				<tr>
							<td colspan="4" align="middle" width="898"><!-- Buttons Start Here -->
								<table border="0" cellpadding="0" cellspacing="0">
						
						<tr name="trNew" id="trNew">
						<td align="right" id="savenNewl">
						<input type="button" class="button" id="savenNew" onclick="ButtonPress('savenew')" href="#" value="Save &amp; New"></td>
						
						<td align="right">
						<input type=button class=button id=savenClose onclick=ButtonPress('saveclose') href="#" value="Save &amp; Close"></td>
						
						<td align="right">
						<input type=button class=button onclick=onClickCancel(); href="#" value="Cancel"></td>
						
						<td align="right">
						<input type=button class=button onclick=window.close() href="#" value="Close"></td>
						</tr>

						</table><!-- Buttons End Here -->
					</td>
				</tr>
			</table>
</form>
<div style="position: absolute;top:170px;width:118px;height:30px" id="menu-popup">
		 <table id="completeTable" bgcolor="#cae1ff" border="1"/>
		 </div>
</center>		 
</body>
</html>
