<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ include file="/includes/taglibs.jsp" %>
<% response.setHeader("Cache-Control","no-cache"); //HTTP 1.1 
response.setHeader("Pragma","no-cache"); //HTTP 1.0 
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server %>


<html>


<title>Create Land Type</title>

<head>

 


</head>

<script language="javascript" type="text/javascript">
	
            function set(target) {
            
            	var i=target;	
            	document.getElementById('mode').value=i;
            		 
            }
            
            function closeWindow(){
             window.close();
            }
            
            function checkForCode() {	
	 			if(dom.get("codeunique").style.display =="" ){
			    document.getElementById('type').value="";
			   
			 		}	 
				}
            function validationForm1()
			{
		
			if(!limitText(256))
		    return false;
			if(!validationCheckcode())
	  		return false;
			if(!validationCheck())
		    return false;
		    set('close');
			}
			
			 function validationForm()
			{
			
			if(!limitText(256))
		    return false;
			if(!validationCheckcode())
	  		return false;
			if(!validationCheck())
		    return false;
		    set('new');
			}
  
       function checkNotSpecial(obj)
		{

		var iChars = "!@$%^*+=[']`~';{}|\":<>?#_";
		for (var i = 0; i < obj.value.length; i++)
			{

				if ((iChars.indexOf(obj.value.charAt(i)) != -1)||(obj.value.charAt(0)==" "))
			{
			dom.get("shoppingcomplex_error").style.display='';
			document.getElementById("shoppingcomplex_error").innerHTML='Special characters are not allowed';
			obj.value="";
			obj.focus();
			return false;
			}
		}
			return true;
		}
      
		
		function validationCheck()
			{
		
			
			var landType=document.getElementById("type").value;
			dom.get("shoppingcomplex_error").style.display='none';
			document.getElementById('codeunique').style.display ='none';
			document.getElementById('codeuniques').style.display ='none';
			  if(landType=="" || landType==null )
			  {
			  
				dom.get("shoppingcomplex_error").style.display='';
				document.getElementById("shoppingcomplex_error").innerHTML='LandType is mandatory';
				return false;
				}

			  else
			  	return true;
			}
		
			function validationCheckcode()
			{
			
		
			var landType=document.getElementById("code").value;
			dom.get("shoppingcomplex_error").style.display='none';
			document.getElementById('codeunique').style.display ='none';
			document.getElementById('codeuniques').style.display ='none';
			  if(landType=="" || landType==null )
			  {
			  
				dom.get("shoppingcomplex_error").style.display='';
				document.getElementById("shoppingcomplex_error").innerHTML='LandTypeID is mandatory';
				return false;
				}

			  else
			  	return true;
			
			}
		
		
			
		function init()
		{
			
		    if(document.getElementById('mode').value=="close"){			 
		     
		    <s:if test="%{!hasErrors()}">		       
		    dom.get("shoppingcomplex_error").style.display='';
			 document.getElementById("shoppingcomplex_error").innerHTML='Land Type successfully created';
		      window.close();		    
		    </s:if>
		    
		     }

		     else
		     reset();
		}	
			
		function reset()
		{   
			<s:if test="%{!hasErrors()}">
			document.getElementById('code').value="";
			document.getElementById('type').value="";
			document.getElementById('description').value="";
			document.getElementById('mode').value="";
			document.getElementById('id').value="";
			</s:if>
		}
	
	
		
		function checkuniqueness(){
			document.getElementById('codeunique').style.display ='none';
			var code=document.getElementById('type').value;
			var id=document.getElementById('id').value;
   	
   			populatecodeunique({type:code,id:id});
   			
		}
		
		
		
		function checkuniqueness1(){
			document.getElementById('codeuniques').style.display ='none';	
			var code=document.getElementById('code').value;
   			var id=document.getElementById('id').value;
   			populatecodeuniques({code:code,id:id});


		}

	function limitText(limitNum) {
	var limitField = document.getElementById('description');
    if (limitField.value.length > limitNum) {
        	dom.get("shoppingcomplex_error").style.display='';
         document.getElementById("shoppingcomplex_error").innerHTML='Only 256 characters allowed ';
          document.getElementById('description').value="";
          return false;         
    }else
    return true;
}


		
		
		function trimAll(sString)
       {
               while (sString.substring(0,1) == ' ')
               {
               sString = sString.substring(1, sString.length);
               }
               while (sString.substring(sString.length-1, sString.length) == ' ')
               {
               sString = sString.substring(0,sString.length-1);
               }
          return sString;
       }                        
		
		
		
		
		
		
   /*
	* Resetting fields manually
	*
	*/


</script>
<script>
jQuery.noConflict();



jQuery(document).ready(function(){
 jQuery('#created').click(function() {
 var str=jQuery('#abc').serialize();
 alert(str)
             jQuery.ajax({
			type: 'POST',
		  url:"<%=request.getContextPath()%>/inspection/inspectionDetails!newForm.action",
			data: jQuery('#abc').serialize(),
			success: function(data) {
				if(data == "true") {
					alert("alert");
				}
			}
        });
                return false;
     
 }); 
        });
</script>
<body onload="">
<s:if test="%{hasErrors()}">
		<div class="errorstyle" id="fieldError">
			<s:actionerror />
			<s:fielderror />
		</div>
</s:if>
<div class="errorstyle" id="shoppingcomplex_error" style="display:none"></div>

<div class="errorstyle" style="display:none" id="codeunique" >
         <s:text name="codeno.exists"/>
</div>  
<div class="errorstyle" style="display:none" id="codeuniques" >
         <s:text name="codeno1.exists"/>      
         
</div>
<s:form id="abc" action="" theme="simple" name="searchForm" onsubmit="">
<s:token/>
<s:push value="model">
<s:hidden id="createdBy" name="createdBy" value="%{createdBy.id}"/>
<s:hidden id="modifiedBy" name="modifiedBy" value="%{modifiedBy.id}"/>
<s:hidden id="createdDate" name="createdDate" value="%{createdDate}"/>
 <div id="" class="formmainbox">
 		
		  <table width="100%" border="0" cellspacing="0" cellpadding="2">
		   
			<tr>	
		<td  class="bluebox" width="12%">&nbsp;</td>			
		<td class="bluebox"><s:text name="Land Type"/>
		<span class="mandatory">*</span></td>	
		<td class="bluebox"><s:textfield name="inspectionNum" id="inspectionNum" maxlength="256" value="%{inspectionNum}" />		
		</td>
	   <td  class="bluebox" width="12%">&nbsp;</td>			
		<td class="bluebox"><s:text name="Land Type"/>
		<span class="mandatory">*</span></td>	
		<td class="bluebox"><s:textfield name="id" id="type" maxlength="256" value="%{id}" />		
		</td>
		
					
				</tr>
		<tr>
		<td id="" class="greybox"><button id="created" >Save</button></td>	
					<td class="greybox">&nbsp;</td>	
		</tr>		
		
		
		
	
	 			
		    </table>
</div>
		      

 
</s:push>
</s:form >
<script>

</script>
</body>
</html>