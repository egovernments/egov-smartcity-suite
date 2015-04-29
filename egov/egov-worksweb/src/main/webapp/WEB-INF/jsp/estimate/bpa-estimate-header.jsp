<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<style>
#warning {
  display:none;
  color:blue;
}
</style>

<script>

var warnings=new Array();
warnings['natureOfWorkChanged']='<s:text name="estimate.header.warning.natureOfWorkChanged"/>'

function warn(type){
    dom.get(type+"Warning").innerHTML=warnings[type]
    dom.get(type+"Warning").style.display='';
    YAHOO.lang.later(3000,null,function(){dom.get(type+"Warning").style.display='none';});
}

function setupPreparedByList(elem){
    deptId=elem.options[elem.selectedIndex].value;    
    populatepreparedBy({executingDepartment:deptId,employeeCode:dom.get("loggedInUserEmployeeCode").value});
}
function clearDesignation(elem) {
    dom.get("designation").value='';
}

function setupSubTypes(elem){
    categoryId=elem.options[elem.selectedIndex].value;
    populatecategory({category:categoryId});
}
function populatepreparedBy(params){
	makeJSONCall(['Text','Value','Designation'],'/egworks/estimate/ajaxEstimate!usersInExecutingDepartment.action',params,preparedBySuccessHandler,preparedByFailureHandler) ;
}
preparedBySuccessHandler=function(req,res){
	enablePreparedBy();
	preparedByDropdown=dom.get("preparedBy");
	var resLength =res.results.length+1;
	var dropDownLength = preparedByDropdown.length;
	for(i=0;i<res.results.length;i++){
	preparedByDropdown.options[i+1]=new Option(res.results[i].Text,res.results[i].Value);
	if(res.results[i].Value=='null') preparedBy.Dropdown.selectedIndex = i;
	preparedByDropdown.options[i+1].Designation=res.results[i].Designation;
	}
	while(dropDownLength>resLength)
	{
		preparedByDropdown.options[res.results.length+1] = null;
		dropDownLength=dropDownLength-1;
	}
	document.getElementById('preparedBy').value='<s:property value="%{estimatePreparedBy.idPersonalInformation}" />';
	if(res.results.length == 1) {
		disablePreparedBy();
		document.getElementById('designation').value='<s:property value="%{estimatePreparedByView.desigId.designationName}" />';
	}
}
preparedByFailureHandler=function(){
	alert('Unable to load preparedBy');
}

function validateCharacters(obj,msg){
	var string=obj.value;
	string=string.replace(/(\r\n|\r|\n){3,}/g,"\n\n\n");
	string=string.replace(/(\t)/g," ");
	string=string.replace(/([ ]){3,}/g,"  ");
	obj.value=string;
	if(string.search(/http:/i)>-1){
		alert(msg);
		return false;
	}
	else{
		return true;
	}
}

function validateHeaderBeforeSubmit(abstractEstimateForm) {
	var nameObj=document.getElementById('name');
	var descriptionObj=document.getElementById('description');
	if(!validateCharacters(nameObj,'<s:text name="estimate.name.error" />') || !validateCharacters(descriptionObj,'<s:text name="estimate.description.error" />')){
		return false;
	}
    var wardName = abstractEstimateForm.wardSearch.value;
    var workType=document.getElementById('parentCategory').value;
      
      if (wardName.length == 0) {
        abstractEstimateForm.wardID.value = -1;
       }
      if(dom.get('userDepartment').value==-1)
      {
    	  dom.get("worktypeerror").style.display='';
          dom.get("worktypeerror").innerHTML='<s:text name="estimate.userDept.null" />';
          window.scroll(0,0);
          return false;   
      }
      else
      {
          if(dom.get("worktypeerror").innerHTML=='<s:text name="estimate.userDept.null" />')
          {
        	  dom.get("worktypeerror").style.display='none';
              dom.get("worktypeerror").innerHTML='';    
          }    
      }
      if(document.getElementById('wardSearch').value=='')
      {
    	  dom.get("worktypeerror").style.display='';
          dom.get("worktypeerror").innerHTML='<s:text name="estimate.ward.null" />';
          window.scroll(0,0);
          return false;   
      }
      else
      {
          if(dom.get("worktypeerror").innerHTML=='<s:text name="estimate.ward.null" />')
          {
        	  dom.get("worktypeerror").style.display='none';
              dom.get("worktypeerror").innerHTML='';    
          }    
      }
      if(dom.get('type').value==-1)
      {
    	  dom.get("worktypeerror").style.display='';
          dom.get("worktypeerror").innerHTML='<s:text name="estimate.natureofwork.null" />';
          window.scroll(0,0);
          return false;   
      }
      else
      {
          if(dom.get("worktypeerror").innerHTML=='<s:text name="estimate.natureofwork.null" />')
          {
        	  dom.get("worktypeerror").style.display='none';
              dom.get("worktypeerror").innerHTML='';    
          }    
      }
      if(document.getElementById('name').value=='')
      {
    	  dom.get("worktypeerror").style.display='';
          dom.get("worktypeerror").innerHTML='<s:text name="estimate.name.null" />';
          window.scroll(0,0);
          return false;   
      }
      else
      {
          if(dom.get("worktypeerror").innerHTML=='<s:text name="estimate.name.null" />')
          {
        	  dom.get("worktypeerror").style.display='none';
              dom.get("worktypeerror").innerHTML='';    
          }    
      }
      if(document.getElementById('description').value=='')
      {
    	  dom.get("worktypeerror").style.display='';
          dom.get("worktypeerror").innerHTML='<s:text name="estimate.desc.null" />';
          window.scroll(0,0);
          return false;   
      }
      else
      {
          if(dom.get("worktypeerror").innerHTML=='<s:text name="estimate.desc.null" />')
          {
        	  dom.get("worktypeerror").style.display='none';
              dom.get("worktypeerror").innerHTML='';    
          }    
      }
      if(dom.get('fundSource').value==-1)
      {
    	  dom.get("worktypeerror").style.display='';
          dom.get("worktypeerror").innerHTML='<s:text name="estimate.fund.null" />';
          window.scroll(0,0);
          return false;   
      }
      else
      {
          if(dom.get("worktypeerror").innerHTML=='<s:text name="estimate.fund.null" />')
          {
        	  dom.get("worktypeerror").style.display='none';
              dom.get("worktypeerror").innerHTML='';    
          }    
      }
      
   	 if(workType=='-1'){
       
   	    dom.get("worktypeerror").style.display='';
        dom.get("worktypeerror").innerHTML='<s:text name="estimate.worktype.null" />';
        window.scroll(0,0);
        return false;
	}
 	
   	if(!validateFinancialDetails()){
		return false;
	}
   	 
   	if(dom.get('preparedBy').value==-1 || dom.get('preparedBy').value==0 || dom.get('preparedBy').value== '') {
   	  	 dom.get("worktypeerror").style.display='';
         dom.get("worktypeerror").innerHTML='<s:text name="estimate.preparedBy.null" />';
         window.scroll(0,0);
         return false;   
    }
   	else{
        dom.get("worktypeerror").style.display='none';
        dom.get("worktypeerror").innerHTML='';
        return true;
    }
}

function trim(str) {
        return str.replace(/^\s+|\s+$/g,"");
}

function resetDetails(){    
	resetSorTable();
	resetNonSorTable();
    var elem = document.getElementById('type');
    resetOverheads();
}

function setExpenditureType(){
	 document.getElementById('expenditureType').value = document.getElementById('type').options[document.getElementById('type').selectedIndex].value;
}
function clearMsg(obj)
{
	if(obj.value==dom.get("executingDepartment").value && '<s:property value="%{errorCode}" />'=='estimate.depositworks.dept.check')
	{
		dom.get('errorstyle').style.display='none';
	}	
}
function openMap()
{
	var params = [
	              'height='+screen.height,
	              'width='+screen.width,
	              'fullscreen=yes' 
	          ].join(',');
	var popup ;
	var  lat = document.getElementById("lat").value ;
	var lon = document.getElementById("lon").value ;
	var status = '<s:property value="%{egwStatus.code}" />';
	if(status==null || status=='' || status =='NEW' || status=='REJECTED')
	{
		if(lat!='' && lon!='')
		{
			popup = window.open('${pageContext.request.contextPath}/estimate/abstractEstimate!maps.action?mapMode=edit&latitude='+lat+'&longitude='+lon,'popup_window', params);
		}
		else
		{
			popup = window.open('${pageContext.request.contextPath}/estimate/abstractEstimate!maps.action?mapMode=edit','popup_window', params);	
		}
	}	
	else
	{
		if(lat!='' && lon!='')
		{
			popup = window.open('${pageContext.request.contextPath}/estimate/abstractEstimate!maps.action?mapMode=view&latitude='+lat+'&longitude='+lon,'popup_window', params);
		}
		else
			return;
	}	
	popup.moveTo(0,0);
}
function updateLocation(values)
{
	if(values!=null && values.length>0)
	{
		document.getElementById("lat").value=values[0];
		document.getElementById("lon").value=values[1];
		document.getElementById("location").value=values[2];
		document.getElementById("latlonDiv").style.display="";
	}
	if(values==null)
	{
		document.getElementById("lat").value="";
		document.getElementById("lon").value="";
		document.getElementById("location").value="";
		document.getElementById("latlonDiv").style.display="none";	
	}			
}

function viewApplicationRequest(id){ 
	window.open("${pageContext.request.contextPath}/depositWorks/roadCut!view.action?mode=view&sourcepage=search&appDetailsId="+id,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

</script>


<table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
                <div class="headplacer"><s:text name="estimate.header" />:</div></td>
                </tr>
              <tr>
                <td width="11%" class="whiteboxwk"><span class="mandatory">*</span><s:text name="estimate.user.department" />:</td>
                <td width="21%" class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="userDepartment" id="userDepartment" cssClass="selectwk" list="dropdownData.userDepartmentList" onchange="clearMsg(this);" listKey="id" listValue="deptName" value="%{userDepartment.id}"/>

                <td width="15%" class="whiteboxwk"><span class="mandatory">*</span><s:text name="estimate.executing.department" />:</td>
                <td width="53%" class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="executingDepartment" id="executingDepartment" cssClass="selectwk" list="dropdownData.executingDepartmentList" listKey="id" listValue="deptName" value="%{executingDepartment.id}" onChange="setupPreparedByList(this);clearDesignation(this);"/>
                </td>
              </tr>
              <tr>
                <td width="11%" class="greyboxwk"><span class="mandatory">*</span><s:text name="estimate.ward" />:</td>
                <td width="21%" class="greybox2wk">              
                <div><s:textfield id="wardSearch" type="text" name="wardName" value="%{ward.parent?(ward.parent?ward.name+'('+ward.parent.name+')':''):(ward.name?ward.name:'')}" class="selectwk" disabled="true"/>
                <s:hidden id="wardID" name="ward" value="%{ward.id}"/>            
                </div>
                </td> 
                <td class="greyboxwk"><span class="mandatory">*</span><s:text name="estimate.date" />:</td>
                <td class="greybox2wk"><s:date name="estimateDate" var="estDateFormat" format="dd/MM/yyyy"/><s:textfield name="estimateDate" value="%{estDateFormat}" id="estimateDate" cssClass="selectwk" disabled="true" />                 
                </td>
              </tr>
              <tr>
                <td class="whiteboxwk"><s:text name="estimate.location" />:</td>
                <td class="whitebox2wk"><s:textfield name="location" value="%{location}" id="location" size="35" cssClass="selectwk" /><a href="javascript:openMap();" id="mapAnchor" title="Click here to add/view gis marker on map"><img height="18" width="18" align="absmiddle" src="${pageContext.request.contextPath}/image/map_icon_small.jpg" /></a></td>
                <td width="15%" class="whiteboxwk"><span class="mandatory">*</span><s:text name="estimate.work.nature" /></td>
                <td width="53%" class="whitebox2wk">
                	<s:select name="type" id="type" cssClass="selectwk" list="dropdownData.typeList" listKey="id" listValue="name" value="%{type.id}" />
                	<s:select id="expenditureType" cssClass="selectwk" list="dropdownData.typeList" listKey="id" listValue="expenditureType.value" value="%{type.id}" style="display:none;"/>
                </td>
              </tr>
              <tr id="latlonDiv" style="display:none;">
	                <td width="11%" class="whiteboxwk"><s:text name="estimate.latitude" />:</td>
	                <td width="21%" class="whitebox2wk"><s:textfield name="lat" value="%{lat}" id="lat" readonly='true' cssClass="selectwk" /></td>
	                <td width="15%" class="whiteboxwk"><s:text name="estimate.longitude" />:</td>
	                <td width="53%" class="whitebox2wk"><s:textfield name="lon" value="%{lon}" id="lon" readonly='true' cssClass="selectwk" /></td>
              </tr>
              <tr>
                <td class="greyboxwk"><span class="mandatory">*</span><s:text name="estimate.work.name" />:</td>
                <td class="greybox2wk" colspan="3"><s:textarea name="name" cols="100" rows="4" cssClass="selectwk" id="name" value="%{name}"/></td>
              </tr>
              <tr>
                <td class="whiteboxwk"><span class="mandatory">*</span><s:text name="estimate.work.description" />:</td>
                <td class="whitebox2wk" colspan="3"><s:textarea name="description" cols="100" rows="4" cssClass="selectwk" id="description" value="%{description}"/></td>
              </tr>

              <tr>
                <td width="11%" class="greyboxwk"><span class="mandatory">*</span><s:text name="estimate.fund.name" />:</td>
                <td width="21%" class="greybox2wk">
	                <s:select name="fundSource" id="fundSource" 
	                cssClass="selectwk" list="dropdownData.fundSourceList" listKey="id" listValue="name" value="%{fundSource.id}"/>
				</td>
				
                <td width="15%" class="greyboxwk"><s:text name="depositworks.applicationReqNo.header" /></td>
                <td width="53%" class="greybox2wk">
               		<a href="#" id="appRequestNo" onclick="viewApplicationRequest(<s:property  value="applicationRequest.applicationDetails.id"/>)">
               			<s:property value="%{applicationRequest.applicationNo}" />
               		</a>			
               <tr>
                <td class="whiteboxwk"><span class="mandatory">*</span><s:text name="estimate.work.type" />:</td>
                <td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="parentCategory" id="parentCategory" cssClass="selectwk" list="dropdownData.parentCategoryList" listKey="id" listValue="description" value="%{parentCategory.id}" onChange="setupSubTypes(this);"/>
                <egov:ajaxdropdown id="categoryDropdown" fields="['Text','Value']" dropdownId='category' url='estimate/ajaxEstimate!subcategories.action' selectedValue="%{category.id}"  />
                </td>

                <td class="whiteboxwk"><s:text name="estimate.work.subtype" />:</td>
                <td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="category" value="%{category.id}" id="category" cssClass="selectwk" list="dropdownData.categoryList" listKey="id" listValue="description"/>
                </td> 
        		</tr>
        		<tr>
                	<td  colspan="4" class="shadowwk"> </td>                 
                </tr>
                <s:if test="%{id != null}">
	      			<div id="rc_div">
    	  	 			<%@ include file="bpa-estimate-ratecontract.jsp"%>
        			</div>
        		</s:if>
                <tr><td>&nbsp;</td></tr>			
            </table>   
                     
