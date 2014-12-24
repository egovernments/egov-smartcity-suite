<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 

<script src="<egov:url path='js/works.js'/>"></script>
<script>

function afterSORResults(sType,results){
    clearMessage('sor_error');
    document.getElementById("loadImage").style.display='none';
    if(results[2].length==0) showMessage('sor_error','No such SOR available')
}

function sorSearchParameters(){
	if(dom.get('scheduleCategory').value!=-1)
	{
	   	return "scheduleCategoryId="+dom.get('scheduleCategory').value;
	}
}

var hint='<a href="#" class="hintanchor" onMouseover="showhint(\'@fulldescription@\', this, event, \'300px\')"><img src="${pageContext.request.contextPath}/image/help.png" alt="Help" width="16" height="16" border="0" align="absmiddle"/></a>'

function createReadOnlyTextBoxFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+sorDataTable.getRecordSet().getLength();
    var fieldName="sorIndentDetails[" + oRecord.getCount() + "]." + oColumn.getKey();
    markup="<input type='text' class='selectamountwk' id='"+id+"' name='"+fieldName+"' value='"+value+"' size='"+size+"' maxlength='"+maxlength+"' readonly='true' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
}
return textboxFormatter;
}
var readOnlyTextboxFormatter = createReadOnlyTextBoxFormatter(5,5);

function createTextBoxFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id="sor"+oColumn.getKey()+oRecord.getId();
    var fieldName="sorIndentDetails[" + oRecord.getCount() + "]." + oColumn.getKey();
    markup="<input type='text' class='selectamountwk' id='"+id+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"'/><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
}
return textboxFormatter;
}
var textboxFormatter = createTextBoxFormatter(11,13);
var stFormatter = createTextBoxFormatter(5,5);
function escapeSpecialChars(str) {
	str1 = str.replace(/\'/g, "\\'");
	str2 = str1.replace(/\r\n/g, "<br>");
	return str2;
}
var descriptionFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var divId="full-"+oColumn.getKey()+oRecord.getId();
    markup="<span class='yui-dt-liner'>"+value+"</span>" + hint.replace(/@fulldescription@/g,escapeSpecialChars(oRecord.getData('FullDescription')));
    el.innerHTML = markup;
}

function createHiddenFormatter(el, oRecord, oColumn, oData){
var hiddenFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName = "sorIndentDetails[" + oRecord.getCount() + "]." + oColumn.getKey() + ".id";
    var fieldValue=value;
    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+fieldValue+"' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
}
return hiddenFormatter;
}
var hiddenFormatter = createHiddenFormatter(10,10);


var searchSelectionHandler = function(sType, arguments) { 

            dom.get("search").value='';
 	        var oData = arguments[2];
 	       	var mySuccessHandler = function(req,res) {
                dom.get("sor_error").style.display='none';
                dom.get("sor_error").innerHTML='';
                records=sorDataTable.getRecordSet();
                
                for(i=0;i<records.getLength();i++){
                    
                   if(sorDataTable.getRecord(i).getData("scheduleOfRate")==res.results[0].Id){
                      dom.get("sor_error").style.display='';
                      document.getElementById("sor_error").innerHTML='<s:text name="indent.sor.duplicate"/>';
                      return;
                   }
                }
                sorDataTable.addRow({scheduleOfRate:res.results[0].Id,Code:res.results[0].Code,SlNo:sorDataTable.getRecordSet().getLength()+1,Description:res.results[0].Description,UOM:res.results[0].UOM,Delete:'X',FullDescription:res.results[0].FullDescription});
                //getFactor(res.results[0].UOM,records);
            };
            
	        var myFailureHandler = function() {
	            dom.get("sor_error").style.display='';
	            document.getElementById("sor_error").innerHTML='<s:text name="indent.sor.invalid.sor"/>';
	        };
			var indentDate=dom.get('indentDate').value;
	        makeJSONCall(["Id","Description","Code","UOM","FullDescription"],'${pageContext.request.contextPath}/masters/scheduleOfRateSearch!findSORAjax.action',{sorID:oData[1]},mySuccessHandler,myFailureHandler) ;

		}
var sorDataTable;  
var makeSORDataTable = function() {
        var cellEditor=new YAHOO.widget.TextboxCellEditor()
        var sorColumnDefs = [
        	{key:"scheduleOfRate", hidden:true,formatter:hiddenFormatter,sortable:false, resizeable:false},
            {key:"SlNo",label:'Sl No', width:80, sortable:false, resizeable:false},
            {key:"Code",label:'Code', width:80, sortable:false, resizeable:false},
            <s:if test="%{mode!='view'}">
            {key:"Description", width:531, formatter:descriptionFormatter,sortable:false, resizeable:false},
              </s:if>
              <s:if test="%{mode=='view'}">
            {key:"Description", width:630, formatter:descriptionFormatter,sortable:false, resizeable:false},
            </s:if>
            {key:"UOM", width:300, sortable:false, resizeable:false},
             
            <s:if test="%{mode!='view'}">
            {key:'Delete', width:80, formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")},
            </s:if>
            
            {key:"FullDescription",hidden:true,sortable:false, resizeable:false}
        ];

        var sorDataSource = new YAHOO.util.DataSource();
        sorDataTable = new YAHOO.widget.DataTable("sorTable",
                sorColumnDefs, sorDataSource,{MSG_EMPTY:"<s:text name='indent.sor.initial.table.message'/>"});
         sorDataTable.subscribe("cellClickEvent", sorDataTable.onEventShowCellEditor); 
         
         sorDataTable.on('cellClickEvent',function (oArgs) {
            var target = oArgs.target;
            var record = this.getRecord(target);
            var column = this.getColumn(target);
            if (column.key == 'Delete') { 
                 this.deleteRow(record);
                allRecords=this.getRecordSet();
                for(i=0;i<allRecords.getLength();i++){
                  this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
                }
                
            }
            
        });

        return {
            oDS: sorDataSource,
            oDT: sorDataTable
        };
      }

function resetSorTable(){
	sorDataTable.deleteRows(0,sorDataTable.getRecordSet().getLength());
	
}

function checkForCategorySelected(){
	if(dom.get("scheduleCategory").value==-1){
		dom.get("sor_error").style.display='';
		document.getElementById("sor_error").innerHTML='<s:text name='indent.sor.category'/>';
		return false;
	}
	return true;
}

function clearCategoryMessage(){
	dom.get("sor_error").style.display='none';
	document.getElementById("sor_error").innerHTML='';
}

function showProcessImage(event) {
	if(!checkForCategorySelected())
		return false;

	var unicode=event.keyCode? event.keyCode : event.charCode;
	if((unicode==46 || unicode==8) && dom.get("search").value.length==1){
	   document.getElementById("loadImage").style.display='none';
	}
	else if(unicode!=9 && unicode!=37 && unicode!=38 && unicode!=39 && unicode!=40){
	   document.getElementById("loadImage").style.display='';
	}

	return true;
}

</script>
<div class="errorstyle" id="sor_error" style="display:none;"></div>

<table id="baseSORTable" width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>        
		<td class="whiteboxwk"><span class="mandatory">*</span><s:text name="rateContract.scheduleCategory.name" />:</td>
        <td class="whitebox2wk" colspan="2"><s:select onchange="clearCategoryMessage();" headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="scheduleCategory" id="scheduleCategory" cssClass="selectwk" list="dropdownData.scheduleCategoryList" listKey="id" listValue="code+' : '+description"/>
        </td>
	</tr>
              <tr>
                <td width="30%" class="whiteboxwk"><span class="bold">Add SOR:</span></td>
                <td width="50%" class="whitebox2wk">
    <div class="yui-skin-sam">
    <div id="sorSearch_autocomplete">
    <div><input id="search" type="text" name="item" class="selectwk" onkeypress="if(event.keyCode==13) return false;return showProcessImage(event);"></div>
    <span id="searchResults"></span>
    </div>
    </div>
    
<egov:autocomplete name="sorSearch" width="50" field="search" url="../masters/scheduleOfRateSearch!searchAjax.action" results="searchResults" handler="searchSelectionHandler" paramsFunction="sorSearchParameters" afterHandler="afterSORResults" />          

                  <label>

                  </label><td width="20%" class="whitebox2wk"><div id="loadImage" style="display:none"><image src="<egov:url path='/images/loading.gif'/>" />Loading SOR's. Please wait..</div></td>
            </table></td>
          </tr>
          <!--<tr>
            <td>&nbsp;</td>
          </tr>-->
          <tr>
            <td>
<table id="sorHeaderTable" width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td colspan="11" class="headingwk" align="left"><div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" /></div><div class="headplacer">SOR</div></td>
              </tr>
              <tr>
                <td >
                <div class="yui-skin-sam">
                    <div id="sorTable"></div>
                                                   
                </div>
                </td></tr>
                <tr>
                	<td colspan="11" class="shadowwk"></td>
                </tr>
			<tr><td>&nbsp;</td></tr>
</table> 
<script>
makeSORDataTable();
<s:iterator id="soriterator" value="SorIndentDetails" status="row_status">
    sorDataTable.addRow({scheduleOfRate:'<s:property value="scheduleOfRate.id"/>',
                        SlNo:'<s:property value="#row_status.count"/>',
                        Code:'<s:property value="scheduleOfRate.code"/>',
                        Description:'<s:property value="scheduleOfRate.summaryJS"/>',
                        UOM:'<s:property value="scheduleOfRate.uom.uom"/>',
                        Delete:'X',
                        FullDescription:'<s:property value="scheduleOfRate.descriptionJS"/>'});
                        
     
    var record = sorDataTable.getRecord(parseInt('<s:property value="#row_status.index"/>'));

</s:iterator>  

</script>
</td>
          </tr>
            </table>
<script type="text/javascript">
  <s:if test="%{mode=='view'}">
	for(i=0;i<document.rateContractForm.elements.length;i++){
		document.rateContractForm.elements[i].disabled=true;
		document.rateContractForm.elements[i].readonly=true;
	} 
  </s:if>
</script>  