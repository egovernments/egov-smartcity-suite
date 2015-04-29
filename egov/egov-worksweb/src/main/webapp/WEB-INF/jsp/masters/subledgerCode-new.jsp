<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>

<html>
  <head>
     <title><s:text name='page.title.subledgerCode'/></title>
  </head>
<script src="<egov:url path='js/works.js'/>"></script>
  <script type="text/javascript">
  
  var currentDate='<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>';
  
  function onBodyLoad(){
		// To show fields based on Account Entity Types - Deposit / Project Code  
   	  	<s:if test="%{prjctCode==true}">
   	  		document.subledgerCode.prjctCode.checked=true;
   	  		document.subledgerCode.prjctCode.value=true;
   	  		document.subledgerCode.depCode.checked=false;
   	  		document.subledgerCode.depCode.value=false;
  	  		dom.get("codeName1").innerHTML='<span class="mandatory">*</span>'+"<s:text name="projectCode.name" />";
  	  		dom.get("description1").innerHTML="<s:text name="projectCode.description" />";  
  	  	</s:if>	 
  	  	<s:if test="%{depCode==true}"> 
   	  		document.subledgerCode.prjctCode.checked=false;
   	  		document.subledgerCode.prjctCode.value=false;
   	  		document.subledgerCode.depCode.checked=true;
   	  		document.subledgerCode.depCode.value=true;
  	  		dom.get("codeName1").innerHTML='<span class="mandatory">*</span>'+"<s:text name="depositCode.work.name" />";
  	  		dom.get("description1").innerHTML="<s:text name="depositCode.work.description" />";
  	  	</s:if>	
  	  	
  		// To Enable onChange Event for parent dropdowns in case child dropdowns are present
  	  	if(document.subledgerCode.scheme!=undefined)
  	  	    	   document.subledgerCode.fund.setAttribute("onChange","setupSchemes(this);"); 
	  	if(document.subledgerCode.subScheme!=undefined)
  	  	    	   document.subledgerCode.scheme.setAttribute("onChange","setupSubSchemes(this);");  
  	  	if(document.subledgerCode.ward!=undefined)
  	  	    	   document.subledgerCode.zone.setAttribute("onChange","setupAjaxWards(this);"); 
  	  	if(document.subledgerCode.subTypeOfWork!=undefined)
  	  	    	   document.subledgerCode.typeOfWork.setAttribute("onChange","setupSubTypes(this);"); 
  }
  
  // To show fields based on Account Entity Types - Deposit / Project Code 
  function viewInputFields(val){
	    if(val=='dc'){
  	  		window.open('${pageContext.request.contextPath}/masters/subledgerCode!newform.action?depCode=true','_self');
	    }else if(val=='pc'){  
	    	window.open('${pageContext.request.contextPath}/masters/subledgerCode!newform.action?prjctCode=true','_self');
	    }
  }
   	
  //To load SubType Of Work based on Type Of Work 	
  function setupSubTypes(elem){
   		clearMessage('subledgerCodeError');
	    categoryId=elem.options[elem.selectedIndex].value;
	    populatesubTypeOfWork({category:categoryId}); 
	}
	
  //To load Ward based on Zone
  function setupAjaxWards(elem){
 		clearMessage('subledgerCodeError');
	    zone_id=elem.options[elem.selectedIndex].value;
	    populateward({zoneId:zone_id});
	}
  
  //To load Scheme based on Fund
  function setupSchemes(elem){
		clearMessage('subledgerCodeError');
	    var fundElem = document.subledgerCode.fund;
		var fundId = fundElem.options[elem.selectedIndex].value;
		if(fundId !='-1'){
			var id=elem.options[elem.selectedIndex].value;
		    var date=currentDate;
		    populatescheme({fundId:id,estimateDate:date});
	    }
	}
	
	//To load SubScheme based on Scheme
	function setupSubSchemes(elem){
		clearMessage('subledgerCodeError');
		var id=elem.options[elem.selectedIndex].value;
	    var date=currentDate;
	  	populatesubScheme({schemeId:id,estimateDate:date});
		
	}
	
	//Show Message if scheme is selected without selecting  Fund 
	function checkFund(elem){
	 var fundElem = document.subledgerCode.fund;
	 if(fundElem.value =='-1' && elem.value=='-1')
	   		showMessage('subledgerCodeError',"<s:text name="sl.checkFund.null" />");
	}
	
	//Show Message if SubScheme is selected without selecting Scheme 
	function checkScheme(elem){
	 var schemeElem = document.subledgerCode.scheme;
	 if(schemeElem.value =='-1' && elem.value=='-1')
	   	showMessage('subledgerCodeError',"<s:text name="sl.checkScheme.null" />");
	}
	
	//Show Message if SubType Of Work is selected without selecting Type Of Work 
	function checkTypeOfWork(elem){
		var typeOfWorkElem =document.subledgerCode.typeOfWork;
		if(typeOfWorkElem.value =='-1' && elem.value=='-1')
	   		showMessage('subledgerCodeError',"<s:text name="sl.checkTypeOfWork.null" />");
	}
	
	//Show Message if Ward is selected without selecting Zone 
	function checkZone(elem){
		var zoneElem = document.subledgerCode.zone;
		if(zoneElem.value =='-1' && elem.value=='-1')
	   		showMessage('subledgerCodeError',"<s:text name="sl.checkZone.null" />");
	}
	
	//To Validate Fields Before Saving the Transaction	
	function validateBeforeSubmit(){
	//Fields to be shown are defined in python script. 
	//This piece of code is to validate mandatory fields returned from the script and other default fields.
	 	for(i=0;i<document.subledgerCode.elements.length;i++){
		 	 if(document.subledgerCode.elements[i].value=="" || document.subledgerCode.elements[i].value==null || document.subledgerCode.elements[i].value=='-1'){
				 	if(document.getElementById((document.subledgerCode.elements[i].id)+1).innerHTML.split("*")[1]!=undefined){
			 	 	    dom.get("subledgerCodeError").innerHTML=document.getElementById((document.subledgerCode.elements[i].id)+1).innerHTML.split(":")[0].split("*")[1]+" is required";
			 	 	    dom.get("subledgerCodeError").style.display=''; 
			 	 	 	return false;
			 	 	 }
		 	   }
	 	}
	  dom.get("subledgerCodeError").style.display='none'; 
	  dom.get("subledgerCodeError").innerHTML='';
	  return true; 
	}
  
  </script>
  <body  onload="onBodyLoad()" class="simple">
    <s:form action="subledgerCode" theme="simple" name="subledgerCode" > 
    <s:token/>
    <s:push value="model">  
    <div id="subledgerCodeError" class="errorstyle" style="display:none;"></div>
    <s:if test="%{hasErrors()}">
        <div id="errorstyle" class="errorstyle" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
    <s:if test="%{hasActionMessages()}">
        <div class="messagestyle">
        	<s:actionmessage theme="simple"/>
        </div>
    </s:if>
 <div class="formmainbox">
 <div class="insidecontent">
 <div class="rbroundbox2">
 <div class="rbtop2"><div></div></div>
 <div class="rbcontent2">
    <table width="100%" border="0" cellspacing="0" cellpadding="0" id="table1">
	 <tr>
	    <td width="11%" class="whiteboxwk"><s:text name="subledgerCode.type.depositCode" /></td>
		<td width="21%" class="whitebox2wk">
		   <input name="depCode" type="radio" id="depCode" onClick="viewInputFields('dc');"  value='%{depCode}'/>
		</td>
		<td width="11%" class="whiteboxwk"><s:text name="subledgerCode.type.projectCode" /></td>
		<td width="21%" class="whitebox2wk">
		  <input name="prjctCode" type="radio" id="prjctCode" onClick="viewInputFields('pc');"/>
		</td>  
	 </tr>	
	</table>
	
   <!-- 
   	Fields to be shown are defined in python script. This piece of code is to show fields returned from the script and is made mandatory.
   -->	
  
	<table width="100%" border="0" cellspacing="0" cellpadding="0" id="table2">
		 <tr>
			<td class="greyboxwk" id="codeName1"><span class="mandatory">*</span><s:text name="depositCode.work.name" /></td>
	        <td class="greybox2wk"><s:textfield name="codeName" type="text" cssClass="selectwk" id="codeName" value="%{codeName}"/></td>
	
	        <td class="greyboxwk" id="description1"><s:text name="depositCode.work.description" /></td>
	        <td class="greybox2wk"><s:textarea name="description" cols="35" cssClass="selectwk" id="description" value="%{description}"/></td>
	     </tr>
	     
     	 <tr>
	       <s:if test="%{depCode==true || (prjctCode==true && list.contains('fund') || list.contains('scheme') || list.contains('subScheme')) }" >
		        <td width="11%" class="whiteboxwk" id="fund1"><span class="mandatory">*</span><s:text name='subledgerCode.fund'/> : </td>
		        <td width="21%" class="whitebox2wk" >
		            <s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="fund" id="fund" cssClass="selectwk" list="dropdownData.fundList" listKey="id" listValue="name" value="%{fund.id}" />
		         	<egov:ajaxdropdown id="schemeDropdown" fields="['Text','Value']" dropdownId='scheme' url='estimate/ajaxFinancialDetail!loadSchemes.action' selectedValue="%{scheme.id}"/>
		         </td>
		    </s:if>   
		
         		<td class="whiteboxwk" id="financialYear1"><span class="mandatory">*</span><s:text name='subledgerCode.financialYear'/> : </td>
         		<td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="financialYear" id="financialYear" cssClass="selectwk" list="dropdownData.financialYearList" listKey="id" listValue="finYearRange" value="%{currentFinancialYearId}" /></td>
      	</tr> 
     
      <s:if test="%{list.isEmpty()!=true}"> 
      	<tr>
       		<s:if test="%{list.contains('department')}" >
          		 <td width="15%" class="greyboxwk" id="department1" ><span class="mandatory">*</span><s:text name="subledgerCode.executing.department" />:</td>
           		<td width="53%" class="greybox2wk" ><s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="department" id="department" cssClass="selectwk" list="dropdownData.departmentList" listKey="id" listValue="deptName" value="%{department.id}" /></td>
          	</s:if> 
          	<s:if test="%{list.contains('fundSource')}" >  
           		 <td width="11%" class="greyboxwk"  id="fundSource1"><span class="mandatory">*</span><s:text name="subledgerCode.fundSource.name" />:</td>
            	<td width="21%" class="greybox2wk">
                	<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="fundSource" id="fundSource" 
               			 cssClass="selectwk" list="dropdownData.fundSourceList" listKey="id" listValue="name" value="%{fundSource.id}"/>
				</td>
			</s:if>          
       </tr>
       
       	<tr>
       	 	<s:if test="%{list.contains('typeOfWork') || list.contains('subTypeOfWork')}" >
            	<td class="whiteboxwk"  id="typeOfWork1"><span class="mandatory">*</span><s:text name="subledgerCode.typeOfWork" />:</td>
            	<td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="typeOfWork" id="typeOfWork" cssClass="selectwk" list="dropdownData.typeOfWorkList" listKey="id" listValue="description" value="%{typeOfWork.id}"/>
            	<egov:ajaxdropdown id="subTypeOfWorkDropdown" fields="['Text','Value']" dropdownId='subTypeOfWork' url='estimate/ajaxEstimate!subcategories.action' selectedValue="%{subTypeOfWork.id}"/>
        		</td>
			</s:if>
	 		<s:if test="%{list.contains('subTypeOfWork')}" >
            	<td class="whiteboxwk"  id="subTypeOfWork1"><span class="mandatory">*</span><s:text name="subledgerCode.subTypeOfWork" />:</td>
            	<td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="subTypeOfWork" value="%{subTypeOfWork.id}" id="subTypeOfWork" cssClass="selectwk" list="dropdownData.subTypeOfWorkList" listKey="id" listValue="description" onClick="checkTypeOfWork(this);"/></td>
            </s:if>
       </tr> 
    
       <tr>
         	<s:if test="%{list.contains('function')}" >
       			<td width="15%" class="greyboxwk" id="function1"><span class="mandatory">*</span><s:text name='subledgerCode.function'/> : </td>
            	<td width="53%" class="greybox2wk" ><s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="function" id="function" cssClass="selectwk" list="dropdownData.functionList" listKey="id" listValue="name" value="%{function.id}" /></td>
          	</s:if>  
         	<s:if test="%{list.contains('worksType')}" >
           		<td width="15%" class="greyboxwk" id="worksType1" ><span class="mandatory">*</span><s:text name="subledgerCode.natureOfWork" />:</td>
           		<td width="53%" class="greybox2wk"><s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="worksType" id="worksType" cssClass="selectwk" list="dropdownData.worksTypeList" listKey="id" listValue="name" value="%{worksType.id}" /></td>
       		</s:if>
      </tr>
        
       <tr>
          <s:if test="%{list.contains('zone') || list.contains('ward')}" >
          		<td width="11%" class="whiteboxwk" id="zone1"><span class="mandatory">*</span><s:text name="subledgerCode.zone" />:</td>
                <td width="21%" class="whitebox2wk"><s:select id="zone" name="zone" cssClass="selectwk" 
										list="dropdownData.zoneList" listKey="id" listValue="name" 
										headerKey="-1" headerValue="%{getText('default.dropdown.select')}"
										value="%{zone.id}" />	
										<egov:ajaxdropdown id="populateWard"
							fields="['Text','Value']" dropdownId='ward'
							url='masters/ajaxSubledgerCode!populateWard.action' />
                </td>
          </s:if>     
          <s:if test="%{list.contains('ward')}" >     
                <td width="11%" class="whiteboxwk" id="ward1"><span class="mandatory">*</span><s:text name="subledgerCode.ward" /></td>
				<td width="21%" class="whitebox2wk" >
								<s:select id="ward" name="ward" cssClass="selectwk" 
									list="dropdownData.wardList" listKey="id" listValue="name" 
									headerKey="-1" headerValue="%{getText('default.dropdown.select')}" value="%{ward.id}" onClick="checkZone(this);"/>
				</td>
			</s:if>
		</tr>  
		
		 <tr>
		    <s:if test="%{list.contains('scheme') || list.contains('subScheme') }" >   
                <td class="greyboxwk" id="scheme1"><span class="mandatory">*</span><s:text name='subledgerCode.scheme'/> : </td>
                <td class="greybox2wk" ><s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="scheme" id="scheme" cssClass="selectwk" list="dropdownData.schemeList" listKey="id" listValue="name" value="%{scheme.id}"  onClick="checkFund(this);"/>
                 <egov:ajaxdropdown id="subSchemeDropdown" fields="['Text','Value']" dropdownId='subScheme' url='estimate/ajaxFinancialDetail!loadSubSchemes.action' selectedValue="%{scheme.id}"/>
                </td>
             </s:if>
             <s:if test="%{list.contains('subScheme')}" >   
                <td class="greyboxwk" id="subScheme1"><span class="mandatory">*</span><s:text name='subledgerCode.subscheme'/> : </td>
                <td class="greybox2wk"><s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="subScheme" id="subScheme" cssClass="selectwk" list="dropdownData.subSchemeList" listKey="id" listValue="name" value="%{subScheme.id}" onClick="checkScheme(this);"/></td>
 			</s:if>      
	 	</tr>  
	 	</s:if>
	 </table>
	
	<br>
	
	<div id="mandatary" align="right" class="mandatory" style="font-size: 11px; padding-right: 20px;">*
		<s:text name="message.mandatory" />
	</div>
	</div>
	<div class="rbtop2"><div></div></div>
	</div>
	</div>
	</div>
	
	<!-- To Show Save and Close Buttons -->
	<div class="buttonholderwk" id="slCodeButtons">
		<s:submit value="Save " cssClass="buttonfinal" value="SAVE" id="saveButton" name="saveButton" method="save" onclick="return validateBeforeSubmit();"/>
	  			&nbsp;
	 <input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="confirmClose('<s:text name='subledger.close.confirm'/>');"/>
    </div>
    </s:push>
   </s:form>
  </body>
</html>
