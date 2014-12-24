 <%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>

<script>
/*
 * Before submit enable all the fields
 */
 
function enablingFields()
{
	for(var i=0;i<document.forms[0].length;i++)
	{
		document.forms[0].elements[i].disabled =false;
	}
}

 function validateLines()
{
    var tableObj=document.getElementById('tenderNoticeDetailTable');
    var lastRow = tableObj.rows.length;
    var i;
    var date;
     var d = new Date();
		var curr_date = d.getDate();
		var curr_month = d.getMonth();
			curr_month++;
		var curr_year = d.getFullYear();
  	    date=curr_date+"/"+curr_month+"/"+curr_year;
  	    
    if(lastRow>=2)
    {
    	for(i=0;i<lastRow-1;i++)
    	{
    	
    	if(trimAll(getControlInBranch(tableObj,"tenderUnitDetailsList["+i+"].dateofSale").value)=="")
    	{ 
    	   document.getElementById("tenderNotice_error").innerHTML='';
   				document.getElementById("tenderNotice_error").innerHTML='Date of sale is mandatory at line'+" "+(i+1);
   				dom.get("tenderNotice_error").style.display='';
    			return false;
    	}
    	/* if(compareDate(getControlInBranch(tableObj,"dateofSale["+i+"]").value,date)<0)
  	    {
  	    	 alert('Date of Sale should be greater than todays date at line ' +(i+1));
  	    	 return false;
  	    
    	} */
    	if(trimAll(getControlInBranch(tableObj,"tenderUnitDetailsList["+i+"].dateofSubmission").value)=="")   
    	{
    	   document.getElementById("tenderNotice_error").innerHTML='';
   				document.getElementById("tenderNotice_error").innerHTML='Date of submission is mandatory at line'+" "+(i+1);
   				dom.get("tenderNotice_error").style.display='';
    			return false;
    	}
    	if(trimAll(getControlInBranch(tableObj,"tenderUnitDetailsList["+i+"].bidMeetingDate").value)=="")
    	{
    	   document.getElementById("tenderNotice_error").innerHTML='';
   				document.getElementById("tenderNotice_error").innerHTML='Date of bid meeting is mandatory at line'+" "+(i+1);
   				dom.get("tenderNotice_error").style.display='';
    			return false;
    	}
    	if(trimAll(getControlInBranch(tableObj,"tenderUnitDetailsList["+i+"].dateOfOpeningOfEtender").value)=="")
    	{
    	   document.getElementById("tenderNotice_error").innerHTML='';
   				document.getElementById("tenderNotice_error").innerHTML='Date of opening of e-tender is mandatory at line'+" "+(i+1);
   				dom.get("tenderNotice_error").style.display='';
    			return false;
    	}
       //var test=getControlInBranch(tableObj,"dateofSale["+i+"]").value);
       
      //   alert("test"+test+"aa");
	     
   		}
   		
   	}
   	return true;
  }
</script>