<script type="text/javascript">
function check_fields(){
 	if(dom.get("month").value=="0"){
 		 alert('<bean:message key="alert.month"/>');
		alert("Month required");
		return false;
	}
 	if(dom.get("year").value=="0"){
 		alert('<bean:message key="alert.year"/>');
 		alert("Year required");
		return false;
	}
 	if(dom.get("department").value=="0"){
 		
 		alert('<bean:message key="alert.select.dept"/>');
 		alert("Department required");
		return false;  
	}
}

function billNumberSetting(){
	if(document.getElementById('billNumberId').value !=0)
		   document.getElementById('billNumber').value = document.getElementById('billNumberId').options[document.getElementById('billNumberId').selectedIndex].text;
	else
		document.getElementById('billNumber').value = "";
}

function populatebillno(obj)
{
	var deptid = document.getElementById("department").value;
	populatebillNumberId({departmentId :deptid});
}


</script>

<tr>
  	<td class="whiteboxwk"><font color="red">*</font><bean:message key="month"/></td>
	<td class="whitebox2wk" style="width: 20;">
	<s:select name="month" id="month"  cssClass="selectwk" headerValue="Choose" headerKey="0"
	list="#{'1':'JAN','2':'FEB','3':'MAR','4':'APR','5':'MAY','6':'JUN','7':'JUL',
	'8':'AUG','9':'SEP','10':'OCT','11':'NOV','12':'DEC'}" onchange="setMonthStr()"/>
		<s:hidden name="monthStr" id="monthStr"></s:hidden>
	</td>
	<td class="whiteboxwk"><font color="red">*</font><bean:message key="year"/></td>
	<td class="whitebox2wk">	
		<s:select name="year" id="year" cssClass="selectwk"  list="dropdownData.finYearList" 
		listKey="id" listValue="finYearRange" headerValue="Choose" headerKey="0" onselect="yearString"/>
	</td>
</tr>	
   
 <tr>
	<td width="11%" class="greyboxwk"><font color="red">*</font><bean:message key="dept"/></td>
	<td width="21%" class="greybox2wk">
		<s:select id="department" name="department" headerKey="0"
			headerValue="--Choose--" cssClass="selectwk"
			list="dropdownData.departmentList" listKey="id"
			listValue="deptName"  onchange="populatebillno(this);" />
			<egov:ajaxdropdown id="billNumberId" fields="['Text','Value']" dropdownId="billNumberId" url="billNumber/billNumberMaster!getBillNumberListByDepartment.action"  />
				

	</td>
			<td class="greyboxwk"><bean:message key="bill.no"/></td>
			<td  class="greybox2wk" width="20%" valign="top" >  	
  			 <s:select id="billNumberId"  name="billNumberId" cssClass="selectwk" 
							headerValue="Choose" headerKey="0" list="dropdownData.billNoList" cssStyle="width:100%"
							listKey="id" listValue="billNumber" value="%{billNumberId}" />
							<s:hidden id="billNumber" name="billNumber" value="%{billNumber}"/>
						</td>
			</td>
			
</tr>

 <tr>
		<td colspan="4" align="center" >
			<p>
			<s:submit id="searchButton" name="action" value="SEARCH" cssClass="buttonfinal" method="search" onclick="billNumberSetting();"/>
			<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
			</p>
		</td>
</tr> 