<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ include file="/includes/taglibs.jsp" %>
<%@ page buffer = "16kb" %>


<html>

<head>

	<title>Nominee Payhead Details </title>
	
<script language="JavaScript"  type="text/JavaScript">


	var empCodeSelectionHandler = function(sType, arguments) { 
             var oData = arguments[2];
			 //alert(oData);
             dom.get("empCode").value=oData[0];
             dom.get("empId").value = oData[1];
    }
    
    var empCodeSelectionEnforceHandler = function(sType, arguments) {
      		warn('improperEmpCodeSelection');
  	}	  
  	
  	function checkOnSubmit(){
		if(document.nomineePayheadForm.empCode.value==""){
			alert("Enter employee code");
			return false;
		}
		return true;
	}

</script>   

</head>

<body>
  
   <s:form name="nomineePayheadForm" action="nomineePayhead" theme="simple" >  
   <s:hidden name="mode" />
   <s:token/>
   	<center>
		
			<div class="formmainbox">
				<div class="insidecontent">
			  		<div class="rbroundbox2">
						<div class="rbtop2">
							<div>
							</div>
						</div>
						<div class="rbcontent2">
							<div class="datewk">	
							    <span class="bold"></span>
							</div>	
	<!-- Decorator -->							
		
			
	<table width="95%" cellpadding="0" cellspacing="0" border="0" align="center" id="nomineePayheadId">
		<tr>
        	<td colspan="4" class="headingwk">
         	<div class="arrowiconwk">
         		<img src="../common/image/arrow.gif" />
         	</div>
           	<div class="headplacer">Nominee Payhead Details</div>
          	</td>
         </tr>
    
	  	<tr>
		   	<td class="whiteboxwk" width="10%">Employee Code<font color="red">*</font></td>
		   	<td class="whitebox2wk">
				<div class="yui-skin-sam">
		    	<div id="empSearch_autocomplete">
		    	<div>
		    	    <s:textfield id="empCode" name="empCode"  />
		    	    <s:hidden id="empId" name="empId"/>				    	   
		    	</div>
		   	    <span id="empCodeSearchResults"></span>
		    	</div>
		    	</div>
		   	    <egovtags:autocomplete name="empCode"  field="empCode" 
		   	    	url="ajaxNomineePayhead!empAjaxList.action" queryQuestionMark="true" results="empCodeSearchResults" 
		   	    	handler="empCodeSelectionHandler" forceSelectionHandler="empCodeSelectionEnforceHandler"/>
		   	    <span class='warning' id="improperempCodeSelectionWarning"></span>	
		   	</td>
		   	<td class="whiteboxwk" colspan="2"></td>
		   		
	    </tr>
	</table>

	<br>

	
<!-- Decorator div -->	
 	</div>	
 	<div class="rbbot2"><div/></div>
	</div>
	</div>
	</div>
	<div class="buttonholderwk">
					<s:submit name="action" value="submit" method="beforeCreate" onclick="return checkOnSubmit();"/>
				</div>
				<%@ include file='../common/payrollFooter.jsp'%>
	</center>
		
		
		
	</s:form>
  </body>
</html>
