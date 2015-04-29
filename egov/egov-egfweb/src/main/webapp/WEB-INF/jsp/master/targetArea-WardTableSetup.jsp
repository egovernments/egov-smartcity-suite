#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#      accountability and the service delivery of the government  organizations.
#   
#       Copyright (C) <2015>  eGovernments Foundation
#   
#       The updated version of eGov suite of products as by eGovernments Foundation 
#       is available at http://www.egovernments.org
#   
#       This program is free software: you can redistribute it and/or modify
#       it under the terms of the GNU General Public License as published by
#       the Free Software Foundation, either version 3 of the License, or
#       any later version.
#   
#       This program is distributed in the hope that it will be useful,
#       but WITHOUT ANY WARRANTY; without even the implied warranty of
#       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#       GNU General Public License for more details.
#   
#       You should have received a copy of the GNU General Public License
#       along with this program. If not, see http://www.gnu.org/licenses/ or 
#       http://www.gnu.org/licenses/gpl.html .
#   
#       In addition to the terms of the GPL license to be adhered to in using this
#       program, the following additional terms are to be complied with:
#   
#   	1) All versions of this program, verbatim or modified must carry this 
#   	   Legal Notice.
#   
#   	2) Any misrepresentation of the origin of the material is prohibited. It 
#   	   is required that all modified versions of this material be marked in 
#   	   reasonable ways as different from the original version.
#   
#   	3) This license does not grant any rights to any user of the program 
#   	   with regards to rights under trademark law for use of the trade names 
#   	   or trademarks of eGovernments Foundation.
#   
#     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>

<script type="text/javascript">
var makeWardDetailTable = function() {    
	var wardTableColumns = [{key:"SlNo",label:'SlNo'},      
	                   {key:"boundary.id",label:'Ward Name',width:300,resizeable:true,formatter:createWardDropdownFormatter(TARGETWARDLIST),dropdownOptions:wardDropdownOptions},  
	                   {key:"Add",label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
	                   {key:"Delete",label:"Delete",formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")
		                   }  
	                   ];

	var wardTableDS = new YAHOO.util.DataSource();   
	wardDataTable = new YAHOO.widget.DataTable("wardDetailTable",wardTableColumns, wardTableDS);
	wardDataTable.on('cellClickEvent',function (oArgs) {
		var target = oArgs.target;
		var record = this.getRecord(target);
		var column = this.getColumn(target);
		if (column.key == 'Add') { 
			wardDataTable.addRow({SlNo:wardDataTable.getRecordSet().getLength()+1});
		}
		if (column.key == 'Delete') { 			 
			if(this.getRecordSet().getLength()>1){			
				this.deleteRow(record);
				allRecords=this.getRecordSet();
				for(var i=0;i<allRecords.getLength();i++){
					this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
				}
			}
			else{
				alert("This row can not be deleted");
			}
		}        
	});   
}                 

function onWardChange(obj)
{
   var scripts = new Array();
   var enteredWardName = obj.value;
   var wardName;
   var records= wardDataTable.getRecordSet(); 
   var row=getRow(obj);
   var rowIndex = row.rowIndex;
   for(i=0;i<records.getLength();i++)
   	{
	   var dropdwnId = "targetAreaMappingsResultList["+ i +"].boundary.id";
	   if (dom.get(dropdwnId) != null)
	   	wardName=dom.get(dropdwnId).value;
	   if((i != (rowIndex-2)) && wardName == enteredWardName)
		{
			alert('This ward is already mapped to this target area');
			obj.selectedIndex = 0;
			return false;
		}
	}
}

function createWardDropdownFormatter(prefix){ 
	return function(el, oRecord, oColumn, oData) {
        var selectedValue = (lang.isValue(oData)) ? oData : oRecord.getData(oColumn.field),
            options = (lang.isArray(oColumn.dropdownOptions)) ?
                oColumn.dropdownOptions : null,
            selectEl,
            collection = el.getElementsByTagName("select");
        if(collection.length === 0) {
            selectEl = document.createElement("select");
            selectEl.className = YAHOO.widget.DataTable.CLASS_DROPDOWN;
            	selectEl.name = prefix+'['+oRecord.getCount()+'].'+oColumn.getKey();
			selectEl.id = prefix+'['+oRecord.getCount()+'].'+oColumn.getKey();
            selectEl = el.appendChild(selectEl);
            selectEl.setAttribute("onChange", "onWardChange(this)");
			
        }

        selectEl = collection[0];

        if(selectEl) {
            selectEl.innerHTML = "";
            if(options) {
                for(var i=0; i<options.length; i++) {
                    var option = options[i];
                    var optionEl = document.createElement("option");
                    optionEl.value = (lang.isValue(option.value)) ?
                            option.value : option;
                    optionEl.innerHTML = (lang.isValue(option.text)) ?
                            option.text : (lang.isValue(option.label)) ? option.label : option;
                    optionEl = selectEl.appendChild(optionEl);
                    if (optionEl.value == selectedValue) {
                        optionEl.selected = true;
                    }
                }
            }
            else {
                selectEl.innerHTML = "<option selected value=\"" + selectedValue + "\">" + selectedValue + "</option>";
            }
        }
        else {
            el.innerHTML = lang.isValue(oData) ? oData : "";
        }
    }
}

var wardDropdownOptions=[{label:"--- Select ---", value:"0"}
<s:if test="dropdownData.wardList!=null && !dropdownData.wardList.isEmpty()">
,
<s:iterator var="s" value="dropdownData.wardList" status="status">  
{"label":"<s:property value="%{name}"/>" ,
	"value":"<s:property value="%{id}" />"  
}<s:if test="!#status.last">,</s:if>
</s:iterator>   
</s:if>      
]

</script>
