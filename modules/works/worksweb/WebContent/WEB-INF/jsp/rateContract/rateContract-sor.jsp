<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 
<style type="text/css">

.yui-skin-sam .yui-dt .yui-dt-col-QuotedRate{
	text-align:right;
}

</style>
<script src="<egov:url path='js/works.js'/>"></script>
<script>
var hint='<a href="#" class="hintanchor" onMouseover="showhint(\'@fulldescription@\', this, event, \'300px\')"><img src="${pageContext.request.contextPath}/image/help.png" alt="Help" width="16" height="16" border="0" align="absmiddle"/></a>'

function createReadOnlyTextBoxFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+sorDataTable.getRecordSet().getLength();
    var fieldName="sorRcDetails[" + oRecord.getCount() + "]." + oColumn.getKey();
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
    var fieldName="sorRcDetails[" + oRecord.getCount() + "]." + oColumn.getKey();
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
    var fieldName = "sorRcDetails[" + oRecord.getCount() + "]." + oColumn.getKey();
    var fieldValue=value;
    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+fieldValue+"' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
}
return hiddenFormatter;
}
var hiddenFormatter = createHiddenFormatter(10,10);

var sorDataTable;  
var makeSORDataTable = function() {
        var cellEditor=new YAHOO.widget.TextboxCellEditor()
        var sorColumnDefs = [
        	{key:"sorNumber", hidden:true,formatter:hiddenFormatter,sortable:false, resizeable:false},
        	{key:"SlNo",label:'Sl No', width:80, sortable:false, resizeable:false},
            {key:"Item", formatter:descriptionFormatter, width:830, sortable:false, resizeable:false},
            {key:"QuotedRate",label:'Quoted Rate', width:200, sortable:false, resizeable:false}, 
            {key:"rcRate", hidden:true,formatter:hiddenFormatter,sortable:false, resizeable:false},          
            {key:"FullDescription",hidden:true,sortable:false, resizeable:false}
        ];

        var sorDataSource = new YAHOO.util.DataSource();
        sorDataTable = new YAHOO.widget.DataTable("sorTable",
                sorColumnDefs, sorDataSource,{MSG_EMPTY:"<s:text name='indent.sor.initial.table.message'/>"});
        return {
            oDS: sorDataSource,
            oDT: sorDataTable
        };
      }
</script>
<div class="errorstyle" id="sor_error" style="display:none;"></div>
<table id="sorHeaderTable" width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td colspan="11" class="headingwk" align="left"><div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" /></div><div class="headplacer">Item Rates</div></td>
              </tr>
              <tr>
                	<td colspan="11">&nbsp;</td>
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

<s:if test="%{id!=null || hasErrors()}">
<s:iterator id="soriterator" value="SorRcDetails" status="row_status">
var sorId = '<s:property value="indentDetail.scheduleOfRate.id"/>';
  if(sorId){
     sorDataTable.addRow({sorNumber:'<s:property value="indentDetail.scheduleOfRate.category.code"/>'+'\^'+'<s:property value="indentDetail.scheduleOfRate.code"/>',
                         SlNo:'<s:property value="#row_status.count"/>',
                         Item:'<s:property value="indentDetail.scheduleOfRate.summaryJS"/>',
                         QuotedRate:Math.abs('<s:property value="rcRate"/>'),
                         rcRate:'<s:property value="rcRate"/>',
         				 FullDescription:'<s:property value="indentDetail.scheduleOfRate.descriptionJS"/>'
                        });
       }
       else
       {
      		sorDataTable.addRow({sorNumber:'<s:property value="indentDetail.nonSor.id"/>',
                         SlNo:'<s:property value="#row_status.count"/>',
                         Item:'<s:property value="indentDetail.nonSor.descriptionJS" escape="false"/>',
                         QuotedRate:Math.abs('<s:property value="rcRate"/>'),
                         rcRate:'<s:property value="rcRate"/>',
                         FullDescription:'<s:property value="indentDetail.nonSor.descriptionJS"/>'
                        });
       }
	var record = sorDataTable.getRecord(parseInt('<s:property value="#row_status.index"/>'));
</s:iterator>   
</s:if>
<s:else> 

<s:iterator id="soriterator" value="responseLine" status="row_status">
    sorDataTable.addRow({sorNumber:'<s:property value="tenderableEntity.number"/>',
                        SlNo:'<s:property value="#row_status.count"/>',
                        Item:'<s:property value="tenderableEntity.nameJS"/>',
                        QuotedRate:Math.abs('<s:property value="bidRateByUom"/>'),
                        rcRate:'<s:property value="bidRateByUom"/>',
        				FullDescription:'<s:property value="tenderableEntity.descriptionJS"/>'
                        });
 </s:iterator>           
</s:else>    
</script>
</td>
</tr>
</table>