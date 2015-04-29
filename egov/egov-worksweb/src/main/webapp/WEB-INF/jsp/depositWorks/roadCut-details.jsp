<script>
    var rowId='';
    var wardId='';
    var areaId='';
    var localityId='';
    var streetId='';
    var agencyId;
    var dwCategory='';
    var DETAILS_ELEMENT_PREFIX;
	<s:if test="%{mode=='edit' && applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@BPA && applicationRequest.applicant.organization != null}">
		DETAILS_ELEMENT_PREFIX ='roadCutDetList';
	</s:if>
	<s:else>
		DETAILS_ELEMENT_PREFIX ='applicationRequest.roadCutDetailsList';
	</s:else>
    function createDetailsTableHiddenIdFormatter(suffix){
        var textboxFormatter = function(el, oRecord, oColumn, oData) {
            var value = (YAHOO.lang.isValue(oData))?oData:"";
            var fieldName = DETAILS_ELEMENT_PREFIX+"[" + oRecord.getCount() + "]." + suffix;
            markup="<input type='hidden' id='"+oColumn.getKey()+oRecord.getId()+"'  value='"+value+"' class='selectmultilinewk' name='"+fieldName+"'  />";
            el.innerHTML = markup;
        };
        return textboxFormatter;
    }
    var dataTableHiddenId = createDetailsTableHiddenIdFormatter('id');
    
    function createDetailsTableTextAreaFormatter(suffix){
        var textboxFormatter = function(el, oRecord, oColumn, oData) {
            var value = (YAHOO.lang.isValue(oData))?oData:"";
            var fieldName = DETAILS_ELEMENT_PREFIX+"[" + oRecord.getCount() + "]." + suffix;
                if(value!=null)
                    markup="<textarea rows='2' cols='50' id='"+oColumn.getKey()+oRecord.getId()+"'   style=width:160px  class='selectmultilinewk'  name='"+fieldName+"' >"+value+"</textarea>";
                else
                    markup="<textarea rows='2' cols='50' id='"+oColumn.getKey()+oRecord.getId()+"'   style=width:160px  class='selectmultilinewk'  name='"+fieldName+"' ></textarea>";
            el.innerHTML = markup;
        };
        return textboxFormatter;
    }
    function createDetailsTableTBNumberFormatter( suffix){
        var textboxFormatter = function(el, oRecord, oColumn, oData) {
            var fieldName = DETAILS_ELEMENT_PREFIX+"[" + oRecord.getCount() + "]." + suffix;
            var value = (YAHOO.lang.isValue(oData))?oData:"";
            markup="<input type='text' id='"+oColumn.getKey()+oRecord.getId()+"' value='"+value+"' class='selectamountwk' style='width:55px'  name='"+fieldName+"' onblur='validateNumber(this)' />";
            el.innerHTML = markup;
        };
        return textboxFormatter;
    }
    var dataTableTBRoadName = createDetailsTableTextAreaFormatter('locationName');
    var dataTableTBRemarks = createDetailsTableTextAreaFormatter('remarks');
    var dataTableTBLength = createDetailsTableTBNumberFormatter('roadLength');
    var dataTableTBBreadth = createDetailsTableTBNumberFormatter('roadBreadth');
    var dataTableTBDepth = createDetailsTableTBNumberFormatter('roadDepth');
    var dataTableTBRemarksEmergency = createDetailsTableTextAreaFormatter('remarksEmergency');
    
    function createDetailsTableZoneDropDown(suffix){
        return function(el, oRecord, oColumn, oData) {
            var fieldName = DETAILS_ELEMENT_PREFIX+"[" + oRecord.getCount() + "]." + suffix;
            var element="<select  id='"+oColumn.getKey()+oRecord.getId()+"' name='"+fieldName+"'  style=width:90px  onchange='resetDropDowns(this,4);'  >";
            element=element+"<option value=-1 selected='selected' >Choose</option>  ";
            <s:iterator value="dropdownData.zoneList" status="stat">
                var name='<s:property value="name"/> - <s:property value="parent.name"/>';
                var id1='<s:property value="id" />';
                element=element+" <option value="+id1 +" > "+ name+" </option>  ";
            </s:iterator>
            element=element+" </select>";
            el.innerHTML =element ;
            };
    }
    var dataTableDDZone = createDetailsTableZoneDropDown('zone.id');
    
    function createEmptyDropDown(suffix){
        return function(el, oRecord, oColumn, oData) {
            var onchangeParam;
            if(suffix=='ward.id')
                onchangeParam='resetDropDowns(this,3);';
            if(suffix=='area.id')
                 onchangeParam='resetDropDowns(this,2);';
             if(suffix=='locality.id')
                 onchangeParam='resetDropDowns(this,1);';                    
                        
            var fieldName = DETAILS_ELEMENT_PREFIX+"[" + oRecord.getCount() + "]." + suffix;
            var element="<select  id='"+oColumn.getKey()+oRecord.getId()+"' name='"+fieldName+"'  style=width:170px  onchange='"+onchangeParam+"'  ><option value=-1 selected='selected' > --- Choose --- </option>  ";
            if(suffix=='ward.id')
                element="<select  id='"+oColumn.getKey()+oRecord.getId()+"' name='"+fieldName+"'  style=width:90px  onchange='"+onchangeParam+"'  ><option value=-1 selected='selected' >Choose</option>  ";
            if(suffix=='street.id')
                element="<select  id='"+oColumn.getKey()+oRecord.getId()+"' name='"+fieldName+"'  style=width:170px  ><option value=-1 selected='selected' > --- Choose --- </option>  ";
            element=element+" </select>";
            el.innerHTML =element ;
            };
    }
    var dataTableDDWard = createEmptyDropDown('ward.id');
    var dataTableDDArea = createEmptyDropDown('area.id');
    var dataTableDDLocality = createEmptyDropDown('locality.id');
    var dataTableDDStreet = createEmptyDropDown('street.id');
    
    function resetDropDowns(obj,level)
    {
        var objName = obj.name;
        var rowIndex = getRowIndexWithRowName(objName);
        var namePrefix = DETAILS_ELEMENT_PREFIX + "[" + rowIndex + "]." ;
        var dropdownObj;
        var i;
        if(level>=1)
        {
            dropdownObj = document.getElementsByName(namePrefix+"street.id")[0];
            for(i=dropdownObj.options.length-1;i>=1;i--)
            {
                dropdownObj.remove(i);
            }
        }
        if(level>=2)
        {
            dropdownObj = document.getElementsByName(namePrefix+"locality.id")[0];
            for(i=dropdownObj.options.length-1;i>=1;i--)
            {
                dropdownObj.remove(i);
            }
        }
        if(level>=3)
        {
            dropdownObj = document.getElementsByName(namePrefix+"area.id")[0];
            for(i=dropdownObj.options.length-1;i>=1;i--)
            {
                dropdownObj.remove(i);
            }
        }
        if(level>=4)
        {
            dropdownObj = document.getElementsByName(namePrefix+"ward.id")[0];
            for(i=dropdownObj.options.length-1;i>=1;i--)
            {
                dropdownObj.remove(i);
            }
        }
        if(level==4)
            ajaxLoadDropdown(obj,"populateWard","zoneId","zone","ward");
        if(level==3)
            ajaxLoadDropdown(obj,"populateArea","wardId","ward","area");
        if(level==2)
            ajaxLoadDropdown(obj,"populateLocality","areaId","area","locality");
        if(level==1)
            ajaxLoadDropdown(obj,"populateStreets","locationId","locality","street");
        
    }
    function getRowIndexWithRowName(name)
    {
        var temp = name.split("[");
        var rowIndex = temp[1].split("]")[0];
        return rowIndex;
    }
    function escapeSpecialChars(str) {
        str1 = str.replace(/\'/g, "\\'");
        str2 = str1.replace(/\"/g, '&quot;');
        str3 = str2.replace(/\r\n/g, "&#13;");
        return str3;
    }

    function getIndexOfRowBasedOnRecordId(recordId)
    {
    	var records= jurisdictionDetailsTable.getRecordSet();
		var i;
		var recordId1;
        for(i=0;i<records.getLength();i++)
        {
        	recordId1=records.getRecord(i).getId();
        	if(recordId==recordId1)
            	return i;
        }
        return 0;	
    }

    function onclickOfBPASearch(recordId)
    {
         <s:if test="%{id == null}">
         var individualRadioStatus = document.getElementById("individualOrOrgRadiotrue").checked;
         var bpaCutRadioStatus = false;
			if(document.getElementById("depositWorksCategoryBPA") != null)
				bpaCutRadioStatus = document.getElementById("depositWorksCategoryBPA").checked;
		
			if(individualRadioStatus && bpaCutRadioStatus){
				window.open("${pageContext.request.contextPath}/../bpa/search/search!searchApplForm.action?rowId="+0,"","height=600,width=1200,scrollbars=yes,left=0,top=0,status=yes");
			}else{
				 var index = getIndexOfRowBasedOnRecordId(recordId);
	            if(document.getElementById("bpaNumberSpanId"+recordId)){
	                window.open("${pageContext.request.contextPath}/../bpa/search/search!searchApplForm.action?rowId="+index,"","height=600,width=1200,scrollbars=yes,left=0,top=0,status=yes");
	            }
			}
        </s:if>
        <s:elseif test="%{mode=='edit' && applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@BPA && applicationRequest.applicant.organization != null}">
        	var index = getIndexOfRowBasedOnRecordId(recordId);
        	if(document.getElementById("bpaNumberSpanId"+recordId)){
            	window.open("${pageContext.request.contextPath}/../bpa/search/search!searchApplForm.action?rowId="+index,"","height=600,width=1200,scrollbars=yes,left=0,top=0,status=yes");
        	}
    	</s:elseif>
        <s:elseif test="%{id != null}">
            showBPAViewPage(dom.get("BPANumber"+recordId).value);
        </s:elseif>
    } 

    function createBPANumberTextboxFormatter(size,maxlength){
        var textboxFormatter = function(el, oRecord, oColumn, oData) {
            var bpaNumber = (YAHOO.lang.isValue(oData))?oData:"";
            var id=oColumn.getKey()+oRecord.getId();
            var onclickCallForAnchor = "showBPAViewPageById('"+id+"')";
            var spanId="bpaNumberSpanId"+oRecord.getId()
            var fieldName=DETAILS_ELEMENT_PREFIX+"[" + oRecord.getCount() + "].bpaNumber";
            markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+bpaNumber+"' size='"+size+"' maxlength='"+maxlength+"' class='selectwk' /><a href='#' onclick="+onclickCallForAnchor+" ><span id='"+spanId+"'>"+bpaNumber+"</span></a>";
            var markupForSearchImage='';
            var onclickCallForImage = "onclickOfBPASearch('"+oRecord.getId()+"')";
            <s:if test="%{id == null || (mode=='edit' && applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@BPA && applicationRequest.applicant.organization != null)}">
                var imageURL="${pageContext.request.contextPath}/image/magnifier.png";
                markupForSearchImage='&nbsp;&nbsp;&nbsp;&nbsp;<a href="#"><img src="'+imageURL+'" height=16  width=16 border="0" alt="Search" onclick='+onclickCallForImage+' align="absmiddle"></a>';
            </s:if>
            el.innerHTML = markup+markupForSearchImage;
            }
            return textboxFormatter;
        }
        var bpaNumberTextboxFormatter = createBPANumberTextboxFormatter(15,50); 

        function showBPAViewPage(bpaNumber) {
            window.open("/bpa/register/registerBpa!viewRegForm.action?planSubmissionNum="+bpaNumber,"","height=600,width=1200,scrollbars=yes,left=0,top=0,status=yes");
        }

        function showBPAViewPageById(objId) {
            var bpaNumber = document.getElementById(objId).value;
            window.open("/bpa/register/registerBpa!viewRegForm.action?planSubmissionNum="+bpaNumber,"","height=600,width=1200,scrollbars=yes,left=0,top=0,status=yes");
        }

    function createDepthOfCutDropDown(suffix){
        return function(el, oRecord, oColumn, oData) {
            var fieldName = DETAILS_ELEMENT_PREFIX+"[" + oRecord.getCount() + "]." + suffix;
            var element="<select  id='"+oColumn.getKey()+oRecord.getId()+"' name='"+fieldName+"'  style='width:70px' >";
            element=element+"<option value=-1 selected='selected' >Choose</option>  ";
            <s:iterator value="dropdownData.depthOfCutList" status="stat">
                var name='<s:property value="label"/>';
                var id1='<s:property value="value" />';
                element=element+" <option value="+id1 +" > "+ name+" </option>  ";
            </s:iterator>
            element=element+" </select>";
            el.innerHTML =element ;
            };
    }
    var dataTableDDDepthOfCut = createDepthOfCutDropDown('roadDepthEmergency');
        
    var jurisdictionDetailsTable;
    var makeJurisdictionDetailsTable = function() {
        var jurisDetailsColumns = [
            {key:"SlNo", label:'SNo', width:15,sortable:false, resizeable:false},
            {key:"Id",hidden:true, formatter:dataTableHiddenId,sortable:false, resizeable:false},
            {key:"BPANumber",hidden:true,label:'BPA Number<span class="mandatory">*</span>',  width:100, formatter:bpaNumberTextboxFormatter,sortable:false, resizeable:false},                                   
            {key:"LocationName",label:'Address/Description<span class="mandatory">*</span>',  width:170, formatter:dataTableTBRoadName,sortable:false, resizeable:false},                                    
            {key:"Zone", label:'Zone<span class="mandatory">*</span>', width:90,formatter:dataTableDDZone,resizeable:true},        
            {key:"Ward", label:'Ward<span class="mandatory">*</span>', width:90, formatter:dataTableDDWard,resizeable:true},
            {key:"Area", label:'Area<span class="mandatory">*</span>', width:170, formatter:dataTableDDArea,resizeable:true},
            {key:"Locality", label:'Locality<span class="mandatory">*</span>', width:170, formatter:dataTableDDLocality,resizeable:true},
            {key:"Street", label:'Street<span class="mandatory">*</span>', width:170, formatter:dataTableDDStreet,resizeable:true},            
            {key:"Length",label:'Cut Length<span class="mandatory">*</span><br />(meters)', width:60, formatter:dataTableTBLength,sortable:false, resizeable:false},
            {key:"Breadth",label:'Cut Breadth<span class="mandatory">*</span><br />(meters)', width:60, formatter:dataTableTBBreadth,sortable:false, resizeable:false},
            {key:"Depth",label:'Cut Depth<br />(feet)', width:60, formatter:dataTableTBDepth,sortable:false, resizeable:false},
            {key:"Remarks",label:'Purpose of Cut', width:170, formatter:dataTableTBRemarks,sortable:false, resizeable:false},
            {key:"DepthDropDown",hidden:true,label:'Depth of Cut<span class="mandatory">*</span><br />(meters)', width:70, formatter:dataTableDDDepthOfCut,resizeable:true},
            {key:"RemarksEmergency",hidden:true,label:'Remarks', width:170, formatter:dataTableTBRemarksEmergency,sortable:false, resizeable:false},
            {key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
            {key:'Delete',label:'Delete', width:22,formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}  
        ];
        var jurisdictionDS = new YAHOO.util.DataSource(); 
        jurisdictionDetailsTable = new YAHOO.widget.DataTable("jurisdictionDetailsTableDiv",jurisDetailsColumns, jurisdictionDS);     
        jurisdictionDetailsTable.on('cellClickEvent',function (oArgs) {
            var target = oArgs.target;
            var record = this.getRecord(target);
            var column = this.getColumn(target);
            if (column.key == 'Add') { 
                jurisdictionDetailsTable.addRow({SlNo:jurisdictionDetailsTable.getRecordSet().getLength()+1});
            }

            if (column.key == 'Delete') {      
                if(this.getRecordSet().getLength()>1){    
                    this.deleteRow(record);
                    var allRecords=this.getRecordSet();
                    var i;
                    for(i=0;i<allRecords.getLength();i++){
                        this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1)); 
                    }
                }
                else
                {
                    alert("This row can not be deleted");
                }
            }        
        });
        
        <s:if test="%{applicationRequest.roadCutDetailsList==null || applicationRequest.roadCutDetailsList.size()==0}" >
            jurisdictionDetailsTable.addRow({SlNo:jurisdictionDetailsTable.getRecordSet().getLength()+1});
        </s:if>    
    };
    var selectedDropdownObjectName;
    var suffixToBeReplaced;
    var suffixToReplaceWith;
    
    function ajaxLoadDropdown(obj, urlMethod, paramPassed, paramSuffixToBeReplaced, paramSuffixToReplaceWith){
        var paramValue=obj.value;
        selectedDropdownObjectName=obj.name;
        suffixToBeReplaced = paramSuffixToBeReplaced;
        suffixToReplaceWith = paramSuffixToReplaceWith;
        var url = '../depositWorks/ajaxDepositWorks!'+urlMethod+'.action?'+paramPassed+'='+paramValue;
        YAHOO.util.Connect.asyncRequest('POST', url, ajaxResultList, null);
    
    }

    var ajaxResultList={
            success: function(o) {
                if(o.responseText!="")
                {
                    var docs=o.responseText;               
                    res=docs.split("$");
                    var dropdownName=selectedDropdownObjectName;
                    dropdownName=dropdownName.replace(suffixToBeReplaced,suffixToReplaceWith);
                    var x=document.getElementsByName(dropdownName)[0];
                    x.length=0;
                    x.options[0]=new Option("---Choose---","-1");  
                    var j=0;         
                    for(var i=0;i<res.length-1;i++)
                    {
                        var idandnum=res[i].split('~');
                        x.options[++j]=new Option(idandnum[0],idandnum[1]);
                    }                   
                }
            },                                         
            failure: function(o) {
                alert('Cannot fetch zone list');
            }
        };
    function validateNumber(obj)
    {
        var text = obj.value;
        if(text=='')
            return;
        if(isNaN(text))
        {
            alert('<s:text name="depositworks.roadcut.invalid.cutdetails" />');
            obj.value="";
            return;
        }
        if(text<=0)
        {
            alert('<s:text name="depositworks.roadcut.negative.cutdetails" />');
            obj.value='';
            return;
        }
        if(text.replace("+","~").search("~")!=-1)
        {
            alert('<s:text name="depositworks.roadcut.plus.notallowed" />');
            obj.value='';
            return;
        }
    }
    function hideColumn(colKey)
    {
        jurisdictionDetailsTable.hideColumn(colKey);
    }
    function showColumn(colKey)
    {
        jurisdictionDetailsTable.showColumn(colKey);
    }
    function showOrHideAdd(obj)
    {
        if(obj.value=='true')
        {
            jurisdictionDetailsTable.showColumn("Add");
            jurisdictionDetailsTable.showColumn("Delete");
            document.getElementById("schemenameTextTD").style.visibility='visible';
            document.getElementById("schemenameTD").style.visibility='visible';
            document.getElementById("schemedetailsTextTD").style.visibility='visible';
            document.getElementById("schemedetailsTD").style.visibility='visible';
        }
        else
        {
            jurisdictionDetailsTable.hideColumn("Add");
            jurisdictionDetailsTable.hideColumn("Delete");
            //RESET ELEMENTS
            document.getElementById("applicationRequest.schemeName").value="";
            document.getElementById("applicationRequest.schemeDetails").value="";

            //HIDE ELEMENTS
            document.getElementById("schemenameTextTD").style.visibility='hidden';
            document.getElementById("schemenameTD").style.visibility='hidden';
            document.getElementById("schemedetailsTextTD").style.visibility='hidden';
            document.getElementById("schemedetailsTD").style.visibility='hidden';
        }    
    }
    function showOrHideSchemeDetails(obj)
    {
        var objText = obj[obj.selectedIndex].text;
        var showScheme=false;
        <s:iterator value="schemeBasedDWTypeList" var="dwTypeList">
            if (objText=="<s:property value='#dwTypeList' />")
            {
                showScheme=true;
            }
        </s:iterator>
        if(showScheme)
        {
            document.getElementById("isSchemeBasedTextTD").style.visibility='visible';
            document.getElementById("isSchemeBasedTD").style.visibility='visible';
        }
        else
        {
            jurisdictionDetailsTable.hideColumn("Add");
            // RESET VALUES
            document.getElementById("isSchemeBasedRadiofalse").checked="checked";
            document.getElementById("applicationRequest.schemeName").value="";
            document.getElementById("applicationRequest.schemeDetails").value="";

            // HIDE THE ELEMENTS
            document.getElementById("isSchemeBasedTextTD").style.visibility='hidden';
            document.getElementById("isSchemeBasedTD").style.visibility='hidden';
            document.getElementById("schemenameTextTD").style.visibility='hidden';
            document.getElementById("schemenameTD").style.visibility='hidden';
            document.getElementById("schemedetailsTextTD").style.visibility='hidden';
            document.getElementById("schemedetailsTD").style.visibility='hidden';
        }        
    }
    
    function update(elemValue) {    

    		var individualRadioStatus = document.getElementById("individualOrOrgRadiotrue").checked;
    		var bpaCutRadioStatus = false;
    		if(document.getElementById("depositWorksCategoryBPA") != null)
    			bpaCutRadioStatus = document.getElementById("depositWorksCategoryBPA").checked;
    		
    	    if(elemValue!="" || elemValue!=null) {
    	        var a = elemValue.split("`~`");
    	        rowId=a[0];
    	        var bpaNumber=a[1];
    	        var zoneId=a[2];
    	        wardId=a[3];
    	        areaId=a[4];
    	        localityId=a[5];
    	        streetId=a[6];
    	        if(individualRadioStatus && bpaCutRadioStatus){
    	        	dom.get("bpaNumber").value=bpaNumber;
    				dom.get("zoneId").value=zoneId;
    				dom.get("wardId").value=wardId;
    				dom.get("areaId").value=areaId;
    				dom.get("localityId").value=localityId;
    				dom.get("streetId").value=streetId;
    				document.getElementById("bpaNumberSpanId").innerHTML=bpaNumber;
    				enableServiceConnections();
    				disableServiceConnections(bpaNumber);
    	        }
    	        else{
    		        var records= jurisdictionDetailsTable.getRecordSet();
    		        dom.get("BPANumber"+records.getRecord(getNumber(rowId)).getId()).value=bpaNumber;
    		        document.getElementById("bpaNumberSpanId"+records.getRecord(getNumber(rowId)).getId()).innerHTML=bpaNumber;
    		        if(zoneId !=null && zoneId != '') {
    		            dom.get("Zone"+records.getRecord(getNumber(rowId)).getId()).value=zoneId;
    		            dom.get("Zone"+records.getRecord(getNumber(rowId)).getId()).disabled=true;
    		            ajaxLoadDropdownForBPA(dom.get("Zone"+records.getRecord(getNumber(rowId)).getId()),"populateWard","zoneId","zone","ward");
    		        }
    	        }
    	        if (wardId == null || wardId == '') {
					dom.get("Ward"+records.getRecord(getNumber(rowId)).getId()).disabled=false;
				}
				if (areaId == null || areaId == '') {
					dom.get("Area"+records.getRecord(getNumber(rowId)).getId()).disabled=false;
				}
				if (localityId == null || localityId == '') {
					dom.get("Locality"+records.getRecord(getNumber(rowId)).getId()).disabled=false;
				}
				if (streetId == null || streetId == '') {
					dom.get("Street"+records.getRecord(getNumber(rowId)).getId()).disabled=false;
				}
    	    }
    	}


function setupServiceDeptInDtls(elem){
    var orgId = elem.value;
       populatercDtlsServiceDept({organizationId:orgId});
}

function ajaxLoadDropdownForBPA(obj, urlMethod, paramPassed, paramSuffixToBeReplaced, paramSuffixToReplaceWith){
    var paramValue=obj.value;
    selectedDropdownObjectName=obj.name;
    suffixToBeReplaced = paramSuffixToBeReplaced;
    suffixToReplaceWith = paramSuffixToReplaceWith;
    var url = '../depositWorks/ajaxDepositWorks!'+urlMethod+'.action?'+paramPassed+'='+paramValue;
    YAHOO.util.Connect.asyncRequest('POST', url, ajaxResultListBPA, null);

}

var ajaxResultListBPA={
        success: function(o) {
            if(o.responseText!="")
            {
                var docs=o.responseText;               
                res=docs.split("$");
                var dropdownName=selectedDropdownObjectName;
                dropdownName=dropdownName.replace(suffixToBeReplaced,suffixToReplaceWith);
                var x=document.getElementsByName(dropdownName)[0];
                x.length=0;
                x.options[0]=new Option("---Choose---","-1");  
                var j=0;         
                for(var i=0;i<res.length-1;i++)
                {
                    var idandnum=res[i].split('~');
                    x.options[++j]=new Option(idandnum[0],idandnum[1]);
                }              
                populateBoundaryDataFromBPA();                  
            }
        },                                         
        failure: function(o) {
            alert('Cannot fetch boundary list');
        }
    };

function populateBoundaryDataFromBPA() {
    var records= jurisdictionDetailsTable.getRecordSet();
    if(wardId !=null && wardId != '' && suffixToReplaceWith == 'ward') {
        dom.get("Ward"+records.getRecord(getNumber(rowId)).getId()).value=wardId;
        dom.get("Ward"+records.getRecord(getNumber(rowId)).getId()).disabled=true;
        ajaxLoadDropdownForBPA(dom.get("Ward"+records.getRecord(getNumber(rowId)).getId()),"populateArea","wardId","ward","area");
    } 
    else if(areaId !=null && areaId != '' && suffixToReplaceWith == 'area') {
        dom.get("Area"+records.getRecord(getNumber(rowId)).getId()).value=areaId;
        dom.get("Area"+records.getRecord(getNumber(rowId)).getId()).disabled=true;
        ajaxLoadDropdownForBPA(dom.get("Area"+records.getRecord(getNumber(rowId)).getId()),"populateLocality","areaId","area","locality");
    } 
    else if(localityId !=null && localityId != '' && suffixToReplaceWith == 'locality') {
        dom.get("Locality"+records.getRecord(getNumber(rowId)).getId()).disabled=true;
        dom.get("Locality"+records.getRecord(getNumber(rowId)).getId()).value=localityId;
        ajaxLoadDropdownForBPA(dom.get("Locality"+records.getRecord(getNumber(rowId)).getId()),"populateStreets","locationId","locality","street")
    }
    else if(streetId !=null && streetId != '' && suffixToReplaceWith == 'street') {
        dom.get("Street"+records.getRecord(getNumber(rowId)).getId()).value=streetId;
        dom.get("Street"+records.getRecord(getNumber(rowId)).getId()).disabled=true;
    }
}

//TODO - Right now this method is not used. This might be required to disable boundary details on validation error in case of BPA Road cut 
function disableBoundaryDetailsForBPARoadCut() {
    for(var i=0;i<document.forms[0].length;i++) {    
        domElement = document.forms[0].elements[i];
        if((domElement.name.indexOf("zone.id") != -1) ||(domElement.name.indexOf("ward.id") != -1) || (domElement.name.indexOf("area.id") != -1)  )    {
            domElement.disabled =true;
        }
        if((domElement.name.indexOf("locality.id") != -1) ||(domElement.name.indexOf("street.id") != -1)) {
            domElement.disabled =true;
        }
    }
}

function enableBoundaryDetailsForBPARoadCut() {
    for(var i=0;i<document.forms[0].length;i++) {    
        domElement = document.forms[0].elements[i];
        if((domElement.name.indexOf("zone.id") != -1) ||(domElement.name.indexOf("ward.id") != -1) || (domElement.name.indexOf("area.id") != -1)  )    {
            domElement.disabled =false;
        }
        if((domElement.name.indexOf("locality.id") != -1) ||(domElement.name.indexOf("street.id") != -1)) {
            domElement.disabled =false;
        }
    }
}

function defaultDepthOfCut(obj) {
	records = jurisdictionDetailsTable.getRecordSet();
    for(i=0;i < records.getLength();i++) {
    	recordId1=records.getRecord(i).getId();
    	if(dom.get("DepthDropDown"+recordId1) != null) {
    		if(obj.value=='<s:property value="@org.egov.works.models.depositWorks.PurposeOfRoadCut@SERVICE"/>') {
	     		dom.get("DepthDropDown"+recordId1).value = '<s:property value="@org.egov.works.utils.DepositWorksConstants@DEPTH_OF_CUT_TWO"/>';
	     		dom.get("DepthDropDown"+recordId1).disabled=true;
    		}
    		else { 
    			dom.get("DepthDropDown"+recordId1).value = -1;
	     		dom.get("DepthDropDown"+recordId1).disabled=false;
        	}
    	}        	
    }
}
    
</script>
<table width="100%" cellspacing="0" cellpadding="0" border="0" >
   
    <tr>
        <td class="greyboxwk">
            <span class="mandatory">*</span><s:text name="depositworks.roadcut.applicant.name.desig" />:
        </td>
        <td class="greybox2wk" colspan="5">
            <s:textfield name="applicationRequest.applicantName" id="applicantName" size="30" />
            &nbsp&nbsp&nbsp&nbsp<span class="mandatory">#<s:text name="depositworks.roadcut.applicant.name.warning" /></span>
        </td>
    </tr>
    <tr id="newOrgznServiceDeptTR" style="display: none;">
        <td class="whiteboxwk">
            <span class="mandatory"></span><s:text name="depositworks.applicant.organization" />:
        </td>
        <td class="whitebox2wk">
            <s:select id="rcDtlsOrganization" name="rcDtlsOrganization" class="input" onchange="setupServiceDeptInDtls(this);setTypeOfCut(this,true);"
                list="dropdownData.organizationList" headerKey="-1" headerValue="--- Select ---"
                listKey="id" listValue="name" value="%{applicationRequest.organization.id}" />
            <egov:ajaxdropdown id="rcDtlsServiceDeptDropdown" fields="['Text','Value']" dropdownId='rcDtlsServiceDept' url='depositWorks/ajaxDepositWorks!getAllServiceDepartmentsForOrganization.action' selectedValue="%{applicationRequest.organization.id}"/>
        </td>
        <td class="whiteboxwk">
            <span class="mandatory"></span><s:text name="depositworks.applicant.service.dept" />:
        </td>
        <td class="whitebox2wk">
            <s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="rcDtlsServiceDept" id="rcDtlsServiceDept" cssClass="selectwk" list="dropdownData.serviceDeptInDtlsList" listKey="id" listValue="name" value="%{applicationRequest.serviceDepartment.id}"  />
        </td>
        <td class="whiteboxwk"></td>
        <td class="whitebox2wk"></td>
    </tr>
    <tr id="mirrorApplicantOrgznServDeptTR" style="display: none;">
        <td class="whiteboxwk">
            <span class="mandatory"></span><s:text name="depositworks.applicant.organization" />:
        </td>
        <td class="whitebox2wk">
            <s:select id="mirrorApplicantOrgzn" name="mirrorApplicantOrgzn" class="input" disabled="true"
                list="dropdownData.organizationList" headerKey="-1" headerValue="--- Select ---"
                listKey="id" listValue="name" value="%{applicationRequest.applicant.organization.id}" />
        </td>
        <td class="whiteboxwk">
            <span class="mandatory"></span><s:text name="depositworks.applicant.service.dept" />:
        </td>
        <td class="whitebox2wk">
            <s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" disabled="true" name="mirrorApplicantServDept" id="mirrorApplicantServDept" cssClass="selectwk" list="dropdownData.allServiceDeptList" listKey="id" listValue="name" value="%{applicationRequest.applicant.serviceDepartment.id}"  />
        </td>
        <td class="whiteboxwk"></td>
        <td class="whitebox2wk"></td>
    </tr>
    <tr id="serviceDeptTR" style="display: none;">
        <td class="whiteboxwk">
            <span class="mandatory"></span><s:text name="depositworks.roadcut.service.department" />:
        </td>
        <td class="whitebox2wk">
            <s:select id="serviceDept" name="serviceDeptId" cssClass="selectwk" disabled="true"
                list="dropdownData.allServiceDeptList" headerKey="-1" headerValue="--- Select ---"
                listKey="id" listValue="name" value="%{applicationRequest.applicant.serviceDepartment.id}" />
        </td>
        <td class="whiteboxwk" colspan="4" />
    </tr>
    <tr>
        <td class="greyboxwk">
            <span class="mandatory">*</span><s:text name="depositworks.roadcut.typeofcut" />:
        </td>
        <td class="greybox2wk">
            <s:select id="typeOfCut" name="applicationRequest.depositWorksType.id" cssClass="selectwk" 
                list="dropdownData.typesOfRoadCut" headerKey="-1" headerValue="--- Select ---"
                listKey="id" listValue="code" onchange="showOrHideSchemeDetails(this)" value="%{applicationRequest.depositWorksType.id}" />
        </td>
        <td class="greyboxwk">
            <span class="mandatory">*</span><s:text name="depositworks.aplication.date">:</s:text>
        </td>
        <td class="greybox2wk">
            <s:if test="%{applicationRequest.applicationDate!=null}">
                <s:date name="applicationRequest.applicationDate" var="appRequestDateFormat" format="dd/MM/yyyy"/>
                 <s:textfield name="applicationRequest.applicationDate" value="%{appRequestDateFormat}" id="appRequestDate" cssClass="selectboldwk" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')" />
                 <a name="aprdDatelnk" id="aprdDatelnk" href="javascript:show_calendar('forms[0].appRequestDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;"><img name="aprdDateimg" id="aprdDateimg" src="${pageContext.request.contextPath}/image/calendar.png" alt="Calendar" width="16" height="16" border="0" align="absmiddle" /></a>
            </s:if>
            <s:else>
                 <s:textfield name="applicationRequest.applicationDate" value="%{todaysDate}" id="appRequestDate" cssClass="selectboldwk" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')" />
                 <a name="aprdDatelnk" id="aprdDatelnk" href="javascript:show_calendar('forms[0].appRequestDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;"><img name="aprdDateimg" id="aprdDateimg" src="${pageContext.request.contextPath}/image/calendar.png" alt="Calendar" width="16" height="16" border="0" align="absmiddle" /></a>
             </s:else>    
        </td>       
        <td class="greyboxwk" id="purposeOfCutLabelTD" style="visibility:hidden">
            <span class="mandatory">*</span><s:text name="dw.roadcut.details.purposeofcut" />:
        </td>
        <td class="greybox2wk" id="purposeOfCutFieldTD" style="visibility:hidden">
            <s:select id="purposeOfRoadCut" name="applicationRequest.purposeOfRoadCut" cssClass="selectwk" 
                list="dropdownData.purposeOfRoadCutList" headerKey="" headerValue="--- Select ---"
                 value="%{applicationRequest.purposeOfRoadCut}" onchange="defaultDepthOfCut(this)" />
        </td>
    </tr>
    <tr id="schemeDetailsTR" style="visibility:hidden">
    
        <td class="whiteboxwk" id="isSchemeBasedTextTD"  style="visibility:hidden">
            <span class="mandatory">*</span><s:text name="depositworks.roadcut.schemebasedcut" />:
        </td>
        <td class="whitebox2wk" id="isSchemeBasedTD"  style="visibility:hidden">
            <s:radio name="applicationRequest.isSchemeBased" id="isSchemeBasedRadio" onchange="showOrHideAdd(this);" list="#{true:'Yes',false:'No'}" value="%{applicationRequest.isSchemeBased}"/>
        </td>
        <td class="whiteboxwk" id="schemenameTextTD">
            <span class="mandatory">*</span><s:text name="depositworks.roadcut.schemename" />:
        </td>
        <td class="whitebox2wk" id="schemenameTD">
            <s:textfield name ="applicationRequest.schemeName" id="applicationRequest.schemeName" />
        </td>
        <td class="whiteboxwk" id="schemedetailsTextTD">
            <span class="mandatory">*</span><s:text name="depositworks.roadcut.schemedetails" />:
        </td>
        <td class="whitebox2wk" id="schemedetailsTD">
            <s:textfield name ="applicationRequest.schemeDetails" id="applicationRequest.schemeDetails" />
        </td>
    </tr>
    </table>
        
           <div align="left" style="width:1200px;overflow:auto">
            <div class="yui-skin-sam">
                <div id="jurisdictionDetailsTableDiv"></div>
            </div>
            <br><br>
        </div>
        <script>
            makeJurisdictionDetailsTable();
            var rowCount = 0;
            <s:if test="%{applicationRequest.roadCutDetailsList!=null && applicationRequest.roadCutDetailsList.size()!=0}" >
            var iteratorIndex =0;
            var zoneDD,wardDD,areaDD,localityDD,streetDD,locName,remarks;
            <s:iterator var="rcDetailsList" value="applicationRequest.roadCutDetailsList" status="row_status">
                          jurisdictionDetailsTable.addRow({Id:"<s:property value='id'/>",
                                        SlNo:"<s:property value='#row_status.count'/>",
                                        BPANumber:"<s:property value='bpaNumber'/>",
                                        LocationName:"<s:property value='locationNameJS'/>",
                                        Length:"<s:property value='roadLength'/>",
                                        Breadth:"<s:property value='roadBreadth'/>",
                                        Depth:"<s:property value='roadDepth'/>",
                                        Remarks:"<s:property value='remarksJS'/>",
                                        DepthDropDown:"<s:property value='roadDepth'/>",
                                        RemarksEmergency:"<s:property value='remarksJS'/>",
                                        Add:createAddImageFormatter("${pageContext.request.contextPath}"),
                                        Delete:createDeleteImageFormatter("${pageContext.request.contextPath}")});

                 <s:if test="%{bpaNumber!= null && bpaNumber!=''}" >
                    jurisdictionDetailsTable.showColumn("BPANumber");
                </s:if>
                <s:if test="%{model.applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@EMERGENCYCUT || model.applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@BPA}">
	                jurisdictionDetailsTable.hideColumn("Depth");
	                jurisdictionDetailsTable.hideColumn("Remarks");
	                jurisdictionDetailsTable.showColumn("DepthDropDown");
	                jurisdictionDetailsTable.showColumn("RemarksEmergency");
	                document.getElementsByName(DETAILS_ELEMENT_PREFIX+"["+rowCount+"].roadDepthEmergency")[0].value=roundTo("<s:property value='roadDepth'/>",2);
	                <s:if test="model.applicationRequest.purposeOfRoadCut==@org.egov.works.models.depositWorks.PurposeOfRoadCut@SERVICE">
	                	document.getElementsByName(DETAILS_ELEMENT_PREFIX+"["+rowCount+"].roadDepthEmergency")[0].disabled=true;
	                </s:if>
	            </s:if>
                 zoneDD = document.getElementsByName(DETAILS_ELEMENT_PREFIX+"["+rowCount+"].zone.id")[0];
                wardDD = document.getElementsByName(DETAILS_ELEMENT_PREFIX+"["+rowCount+"].ward.id")[0];
                areaDD = document.getElementsByName(DETAILS_ELEMENT_PREFIX+"["+rowCount+"].area.id")[0];
                localityDD = document.getElementsByName(DETAILS_ELEMENT_PREFIX+"["+rowCount+"].locality.id")[0];
                streetDD = document.getElementsByName(DETAILS_ELEMENT_PREFIX+"["+rowCount+"].street.id")[0];

                wardDD.length=0;
                areaDD.length=0;
                localityDD.length=0;
                streetDD.length=0;
                
                wardDD.options[0]=new Option("---Choose---","-1");
                areaDD.options[0]=new Option("---Choose---","-1");
                localityDD.options[0]=new Option("---Choose---","-1");
                streetDD.options[0]=new Option("---Choose---","-1");

                iteratorIndex=0;
                
                <s:iterator id="wardDDList" value="#rcDetailsList.jurisdictionDropDownLists.wardList" status="ward_status">
                    wardDD.options[iteratorIndex]=new Option("<s:property value='name'/>",<s:property value="id"/>);
                    iteratorIndex++;
                </s:iterator>     
                   
                iteratorIndex = 0;
                      
                <s:iterator id="areaDDList" value="#rcDetailsList.jurisdictionDropDownLists.areaList" status="area_status">
                    areaDD.options[iteratorIndex]=new Option("<s:property value='name'/>",<s:property value="id"/>);
                    iteratorIndex++;
                </s:iterator>

                iteratorIndex = 0;
                              
                <s:iterator id="localityDDList" value="#rcDetailsList.jurisdictionDropDownLists.localityList" status="locality_status">
                    localityDD.options[iteratorIndex]=new Option("<s:property value='name'/>",<s:property value="id"/>);
                    iteratorIndex++;
                </s:iterator>

                iteratorIndex = 0;
                         
                <s:iterator id="streetDDList" value="#rcDetailsList.jurisdictionDropDownLists.streetList" status="street_status">
                    streetDD.options[iteratorIndex]=new Option("<s:property value='name'/>",<s:property value="id"/>);
                    iteratorIndex++;
                </s:iterator>              
        
                rowCount++;

                  zoneDD.value=<s:property value="#rcDetailsList.zone.id" />;
                  wardDD.value=<s:property value="#rcDetailsList.ward.id" />;
                  areaDD.value=<s:property value="#rcDetailsList.area.id" />;
                  localityDD.value=<s:property value="#rcDetailsList.locality.id" />;
                  streetDD.value=<s:property value="#rcDetailsList.street.id" />;
                  
            </s:iterator>
            </s:if>
        </script>