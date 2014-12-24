 <%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
 
 <script>
 var index=1;
 
 //This is to add Multiple rows to a shopdetail table
 
 function addRow()
  {    
  jQuery('select[id^="designation"]').each(function(index) {
	
   		jQuery(this).multiselect("destroy");
			});
			
			  jQuery('select[id^="buttons"]').each(function(index) {
	
   		jQuery(this).multiselect("destroy");
			});
	
	    	var tableObj=document.getElementById('workflowDetailstbl');
			var tbody=tableObj.tBodies[0];
			var lastRow = tableObj.rows.length;
			var rowObj = tableObj.rows[1].cloneNode(true);
			tbody.appendChild(rowObj);
	  		var rowno = parseInt(tableObj.rows.length)-2;
	  		  		
	  		document.forms[0].approverNo[lastRow-1].value=tableObj.rows.length - 1;
			document.forms[0].designation[lastRow-1].value="";
			document.forms[0].state[lastRow-1].value="";
			document.forms[0].action[lastRow-1].value="";
			document.forms[0].status[lastRow-1].value="";
			document.forms[0].buttons[lastRow-1].value="";
			document.forms[0].approverNo[lastRow-1].setAttribute("name","workFlowMatrixDetails["+index+"].approverNo");
			document.forms[0].designation[lastRow-1].setAttribute("name","workFlowMatrixDetails["+index+"].designation");
			document.forms[0].state[lastRow-1].setAttribute("name","workFlowMatrixDetails["+index+"].state");
			document.forms[0].action[lastRow-1].setAttribute("name","workFlowMatrixDetails["+index+"].action");	
			document.forms[0].status[lastRow-1].setAttribute("name","workFlowMatrixDetails["+index+"].status");	
			document.forms[0].buttons[lastRow-1].setAttribute("name","workFlowMatrixDetails["+index+"].buttons");	
				
			index++;
		
  		
  		jQuery('select[id^="designation"]').each(function(index) {
   		jQuery(this).multiselect({
         open: function(){
          
            
            jQuery('select[id^="designation"]').each(function(index) {
            
            jQuery(this).multiselect('disable');
            });
             jQuery(this).multiselect('enable');
            
   }, beforeclose: function(){
     jQuery('select[id^="designation"]').each(function(index) {
            
            jQuery(this).multiselect('enable');
            });
   }
   		
   		
   		
   		}
   		);
			});
			
			
			jQuery('select[id^="buttons"]').each(function(index) {
   		jQuery(this).multiselect({
         open: function(){
          
            
            jQuery('select[id^="buttons"]').each(function(index) {
            
            jQuery(this).multiselect('disable');
            });
             jQuery(this).multiselect('enable');
            
   }, beforeclose: function(){
     jQuery('select[id^="buttons"]').each(function(index) {
            
            jQuery(this).multiselect('enable');
            });
   }
   		
   		
   		
   		}
   		);
			});
   }



  //This method is to remove rows from shopdetail table 
   function removeRow(obj)
   {
     	var tb1=document.getElementById("workflowDetailstbl");
        var lastRow = (tb1.rows.length)-1;
        var curRow=getRow(obj).rowIndex;
       // dom.get("shop_error").style.display='none';
        if(lastRow ==1)
      	{
     		// dom.get("shop_error").style.display='none';
     		// document.getElementById("shop_error").innerHTML='This row can not be deleted';
   			 //dom.get("shop_error").style.display='';
      	     return false;
        }
      	else
      	{
       		var updateserialnumber=curRow;
			for(updateserialnumber;updateserialnumber<tb1.rows.length-1;updateserialnumber++)
			{
				if(document.forms[0].approverNo[updateserialnumber]!=null)
					document.forms[0].approverNo[updateserialnumber].value=updateserialnumber;
			}
			tb1.deleteRow(curRow);
	      	return true;
      }
   }
 
 
 //This method is to validate form  
 
 function validateForm()
 {
 	  if (document.getElementById('objectType').value == "-1") {
		alert("Please select the Object Type")
			return false;
	  }
      
        if (document.getElementById('fromDate').value == "") {
		alert("Please select the From Date")
			return false;
	  }
	
	   if (document.getElementById('department').value == "") {
			alert("Please select the department")
			return false;
		}
 
	  
	 if (document.getElementById('amountRule').checked) {
			  if (document.getElementById('fromAmount').value == "") {
		        alert("Please select the From Amount");
			return false;
	  }  
	  
	  }
		
	var mode= document.getElementById('mode').value;  
	 if(mode=='edit'){
	if(!validateMatrixDetail())
	  return false;
	
	//if(!uniqueStateName())
	 // return false;
	
	if(!uniqueActionName())
	  return false;
	  
	  if(checkreject()){
	 if(!validateRejectMatrixLines())
	 return false
	 }else return true;
	}
	
	
	
	return true;
	  
 }

    
 
 function checkreject(){
 
 var tableObj=document.getElementById('workflowDetailstbl');
    var lastRow = tableObj.rows.length;
    if(lastRow>2){
  var tableObj=document.getElementById('rejectiontable');
    var lastrejRow = tableObj.rows.length;
  
    if(lastrejRow<=2){
  if(document.getElementById('rejectState').value!="-1"||
   	    document.getElementById('rejectDesignation').value!=""||
   	    document.getElementById('rejectAction').value!="-1"||
        document.getElementById('rejectStatus').value!="-1"||
        document.getElementById('rejectButtons').value!="") {
        return true;
        
        }
        }
        else return true;
        }
        else return false;
 }
 
  

 
 
 // This method is to validate Line level items
 
 function validateMatrixDetail()
{
      
    var tableObj=document.getElementById('workflowDetailstbl');
    var lastRow = tableObj.rows.length;
    var desn,stat,act,status,buttons;
    var i;
    if(lastRow>2)
    {
    	for(i=0;i<lastRow-1;i++)
    	{  
    		var idelta=i+1;
    		if(idelta==(lastRow-1)){
			desn="value";
			  if(document.forms[0].action[i].value!="END"){
			   alert("The Next action for Last level of approver should be END");
			   return false;
			   }
			}else{
         	 desn=document.forms[0].designation[i].value;
         	 }
          	 stat=document.forms[0].state[i].value;
          	  act=document.forms[0].action[i].value;
          	   status=document.forms[0].status[i].value;
          	   buttons=document.forms[0].buttons[i].value;
          	 
         	if(!validateMatrixLines(desn,stat,act,status,buttons,i+1,"Workflow Details"))
         		return false;
        
            
          	   		}
   		return true;
   	}
   	else  
   	{
   	    desn="value";
   	    stat=document.getElementById('state').value;
   	    act=document.getElementById('action').value;
   	     buttons=document.getElementById('buttons').value;  	    
   	    if(act!="END"){
			   alert("The Next action for Last level of approver should be END");
			   return false;
			   }
        status=document.getElementById('status').value;
   	  if(!validateMatrixLines(desn,stat,act,status,buttons,1,"Workflow Details"))
   	       return false;
   	     else
   	       return true;
   	    
   	}
  }
  
  function validateRejectMatrixLines(){ 
  
  var tableObj=document.getElementById('rejectiontable');
    var lastRow = tableObj.rows.length;
    var desn,stat,act,status,buttons;
    var i;
    if(lastRow>2)
    {
    	for(i=0;i<lastRow-1;i++)
    	{  
    		var idelta=i+1;
         	 desn=document.forms[0].rejectDesignation[i].value;        	 
          	 stat=document.forms[0].rejectState[i].value;
          	  act=document.forms[0].rejectAction[i].value;
          	   status=document.forms[0].rejectStatus[i].value;
          	    buttons=document.forms[0].rejectButtons[i].value;
         	if(!validateMatrixLines(desn,stat,act,status,buttons,i+1,"Rejection Details"))
         		return false;
        }
   		return true;
   	}
   	else
   	{
   	    desn=document.getElementById('rejectDesignation').value;
   	    stat=document.getElementById('rejectState').value;
   	    act=document.getElementById('rejectAction').value;
        status=document.getElementById('rejectStatus').value;
        buttons=document.getElementById('rejectButtons').value;
   	  if(!validateMatrixLines(desn,stat,act,status,buttons,1,"Rejection Details"))
   	       return false;
   	     else
   	       return true;
   	    
   	}
 }
  
  function validateMatrixLines(desn,stat,act,status,buttons,row,tables)
  {
 
     if(desn!='' && stat!='-1' && act!='-1' && status!='-1' && buttons!='')
         {
       
    	return true;
       
         
         }else
         
         {  
         if(desn=='') {
         alert("Please select Designation for "+tables+" level "+row);  
          return false;    
         }else if(stat=='-1'){
          alert("Please select State for "+tables+" level "+row);  
           return false;
        } else if(act=='-1'){
          alert("Please select Action for  "+tables+" level "+row);  
           return false;
        } else if(status=='-1'){
          alert("Please select Status for  "+tables+" level "+row);  
   		 return false;
   		 }
   		 else if(buttons==''){
          alert("Please select Buttons for "+tables+" level "+row);  
   		 return false;
   		 }
         }
       
  }
 
 //This method is to validate unique Amenity name on the form
 
 function uniqueStateName()
  {
      var tableObj=document.getElementById('workflowDetailstbl');
      var lastRow = tableObj.rows.length;
      var amenity1,amenity2;
      var i,j;
      if(lastRow>2)
      {
    	for(i=0;i<lastRow-2;i++)
    	{
         	 amenity1=document.forms[0].state[i].value;
         	 for(j=i+1;j<lastRow-1;j++)
         	 {
          	    amenity2=document.forms[0].state[j].value;
          	    if(amenity1==amenity2)
          	    { 
          	        alert("The State should be unique across different approver")
          	   		return false;
          	   	}
          	  }
   		}
   		
   	}
   	return true;
  }
  
  
   function uniqueActionName()
  {
      var tableObj=document.getElementById('workflowDetailstbl');
      var lastRow = tableObj.rows.length;
      var amenity1,amenity2;
      var i,j;
      if(lastRow>2)
      {
    	for(i=0;i<lastRow-2;i++)
    	{
         	 amenity1=document.forms[0].action[i].value;
         	 for(j=i+1;j<lastRow-1;j++)
         	 {
          	    amenity2=document.forms[0].action[j].value;
          	    if(amenity1==amenity2)
          	    { 
          	        alert("The Action should be unique across different approver")
          	   		return false;
          	   	}
          	  }
   		}
   		
   	}
   	return true;
  }
  
  
  //----------------------------------------------------------
  var rejectindex=1;
 
 //This is to add Multiple rows to a shopdetail table
 
 function addRowReject()
  {   
	    jQuery('select[id^="rejectDesignation"]').each(function(index) {
	
   		jQuery(this).multiselect("destroy");
			});
			
			  jQuery('select[id^="rejectButtons"]').each(function(index) {
	
   		jQuery(this).multiselect("destroy");
			});
	    	var tableObj=document.getElementById('rejectiontable');
			var tbody=tableObj.tBodies[0];
			var lastRow = tableObj.rows.length;
			var rowObj = tableObj.rows[1].cloneNode(true);
			tbody.appendChild(rowObj);
	  		var rowno = parseInt(tableObj.rows.length)-2;
	  		
	  		document.forms[0].rejectApproverNo[lastRow-1].value=tableObj.rows.length - 1;
	  	
			document.forms[0].rejectDesignation[lastRow-1].value="";
			document.forms[0].rejectState[lastRow-1].value="";
			document.forms[0].rejectAction[lastRow-1].value="";
			document.forms[0].rejectStatus[lastRow-1].value="";
			document.forms[0].rejectButtons[lastRow-1].value="";
			document.forms[0].rejectApproverNo[lastRow-1].setAttribute("name","workFlowMatrixRejectDetails["+rejectindex+"].rejectApproverNo");
			document.forms[0].rejectDesignation[lastRow-1].setAttribute("name","workFlowMatrixRejectDetails["+rejectindex+"].rejectDesignation");
			document.forms[0].rejectState[lastRow-1].setAttribute("name","workFlowMatrixRejectDetails["+rejectindex+"].rejectState");
			document.forms[0].rejectAction[lastRow-1].setAttribute("name","workFlowMatrixRejectDetails["+rejectindex+"].rejectAction");	
			document.forms[0].rejectStatus[lastRow-1].setAttribute("name","workFlowMatrixRejectDetails["+rejectindex+"].rejectStatus");	
			document.forms[0].rejectButtons[lastRow-1].setAttribute("name","workFlowMatrixRejectDetails["+rejectindex+"].rejectButtons");	
			rejectindex++;
		
  			jQuery('select[id^="rejectDesignation"]').each(function(index) {
   		jQuery(this).multiselect({
         open: function(){
          
            
            jQuery('select[id^="rejectDesignation"]').each(function(index) {
            
            jQuery(this).multiselect('disable');
            });
             jQuery(this).multiselect('enable');
            
   }, beforeclose: function(){
     jQuery('select[id^="rejectDesignation"]').each(function(index) {
            
            jQuery(this).multiselect('enable');
            });
   }
   		
   		
   		
   		}
   		);
			});
			
			
			jQuery('select[id^="rejectButtons"]').each(function(index) {
   		jQuery(this).multiselect({
         open: function(){
          
            
            jQuery('select[id^="rejectButtons"]').each(function(index) {
            
            jQuery(this).multiselect('disable');
            });
             jQuery(this).multiselect('enable');
            
   }, beforeclose: function(){
     jQuery('select[id^="rejectButtons"]').each(function(index) {
            
            jQuery(this).multiselect('enable');
            });
   }
   		
   		
   		
   		}
   		);
			});
   }



  //This method is to remove rows from shopdetail table 
   function removeRowReject(obj)
   {
     		var tb1=document.getElementById("rejectiontable");
        var lastRow = (tb1.rows.length)-1;
        var curRow=getRow(obj).rowIndex;
       // dom.get("shop_error").style.display='none';
        if(lastRow ==1)
      	{
      	     return false;
        }
      	else
      	{
       		var updateserialnumber=curRow;
			for(updateserialnumber;updateserialnumber<tb1.rows.length-1;updateserialnumber++)
			{
				if(document.forms[0].approverNo[updateserialnumber]!=null)
					document.forms[0].approverNo[updateserialnumber].value=updateserialnumber;
			}
			tb1.deleteRow(curRow);
	      	return true;
      }
   }
 
 
 </script>