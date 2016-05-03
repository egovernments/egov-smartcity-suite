/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
var SORT_COLUMN_INDEX;
function LrTrim(str)
{
	//Left trim the passed string
	while ((str.substring(0,1) == ' ') || (str.substring(0,1) == '\r'))
         str = str.substring(1,str.length);
  	//right trim the passed string
    while ((str.substring(str.length-1,str.length) == ' ') || (str.substring(str.length-1,str.length) == '\r'))
         str = str.substring(0,str.length-1);
    return str;
}
function  makeSortable(tblId) 
{
	table=document.getElementById(tblId);
    if (table.rows && table.rows.length > 0) {
        var firstRow = table.rows[0];
    }
    if (!firstRow) return;
    
    // We have a first row: assume it's the header, and make its contents clickable links
    for (var i=0;i<firstRow.cells.length;i++) {
        var cell = firstRow.cells[i];
        var txt = LrTrim(cell.innerHTML);
        cell.innerHTML = '<div class="sortheader" onclick="resortTable(this);return false;">'+txt+'</div>';
    }
}

function getInnerText(el) {
	if (typeof el == "string") return el;
	if (typeof el == "undefined") { return el };
	if (el.innerText) return el.innerText;	//Not needed but it is faster
	var str = "";
	
	var cs = el.childNodes;
	var l = cs.length;
	for (var i = 0; i < l; i++) {
		switch (cs[i].nodeType) {
			case 1: //ELEMENT_NODE
				str += getInnerText(cs[i]);
				break;
			case 3:	//TEXT_NODE
				str += cs[i].nodeValue;
				break;
		}
	}
	return str;
}
function getTextNode(tdObj)
{
	//returns first text node in the div of the sort link
	try{
		if(tdObj.childNodes[0].nodeType==3)  return tdObj;
	}catch(e){}
	var cellChild;
	for(var i=0;i<tdObj.childNodes.length;i++)
	{
		cellChild=getTextNode(tdObj.childNodes[i]);
		if(cellChild)
		{
			try{
				  if(cellChild.childNodes[0].nodeType==3) return cellChild;
			}catch(e){}
			
		}
	}

	return null;
}
function resortTable(lnk) {
    // get the div
    var span=lnk;
	//get first textnode link
	var rootNode=getTextNode(lnk);
	//get first textnode text
	var spantext = getInnerText(rootNode);
	//if arrow is there remove
	if(spantext.charCodeAt(0)==8595 || spantext.charCodeAt(0)==8593 )
	{
			spantext=spantext.substring(1,spantext.length);
	}
    var td = lnk.parentNode;
	while(td.tagName.toUpperCase()!="TD" && td.tagName.toUpperCase()!="TH")
	  td=td.parentNode;
    
    var column = td.cellIndex;
    var table = getParent(td,'TABLE');
    // Work out a type for the column
    if (table.rows.length <= 1) return;
    var itm = getInnerText(table.rows[1].cells[column]);
    sortfn = sortCaseInsensitive;
    if (itm.match(/^\d\d[\/-]\d\d[\/-]\d\d\d\d$/)) sortfn = sortDate;
    if (itm.match(/^\d\d[\/-]\d\d\d[\/-]\d\d\d\d$/)) sortfn = sortDate;
    if (itm.match(/^\d\d[\/-]\d\d[\/-]\d\d$/)) sortfn = sortDate;
    if (itm.match(/^\d[\/-]\d\d\d[\/-]\d\d\d\d$/)) sortfn = sortDate;
    if (itm.match(/^\d[\/-]\d\d[\/-]\d\d\d\d$/)) sortfn = sortDate;
    if (itm.match(/^[�$]/)) sortfn = sortCurrency;
    if (itm.match(/^[\d\.]+$/)) sortfn = sortNumeric;
	var gComma= new String(itm);
    if (gComma.match(',')==",")
	{
		sortfn = sortGeneric;
	}
    SORT_COLUMN_INDEX = column;
    var newRows = new Array();
    //=====================
    var newNode;
    var oldRowCount=table.rows.length;
	var oldColumnCount=table.rows[0].cells.length;
	var newCols;
	
	//insert column
    for (var j=0;j<table.rows.length;j++) 
    {
		table.rows[j].insertCell(oldColumnCount);
		//assign last column data = row number
		table.rows[j].cells[oldColumnCount].innerHTML=j; 
    }
	//copy all rows to newRows
    for (var j=1;j<table.rows.length;j++) { newRows[j-1] = table.rows[j]; }	
        
    newRows.sort(sortfn);

    if (span.getAttribute("sortdir") == 'down') {
		//set first text node text to arrow and oldtext
		rootNode.innerHTML='&uarr;'+spantext;
        newRows.reverse();
        span.setAttribute('sortdir','up');
    } else {
		//set first text node text to arrow and oldtext
		rootNode.innerHTML='&darr;'+spantext;
        span.setAttribute('sortdir','down');
    }
	
	for(var k=0;k<oldRowCount-1;k++)
	{
		//get lastcolumn data.thatis old row number
		newCols=newRows[k].cells[oldColumnCount].innerHTML;
		//clone that node and append
		newNode=table.rows[parseInt(newCols)].cloneNode(true);
		table.tBodies[0].appendChild(newNode);
    }
	
    
    
	var sortIndexArr = new Array();
    var element;
    var newIndex=oldRowCount;

	for(var i=0;i<newRows.length;i++)
	{
		sortIndexArr[i]=newRows[i].cells[oldColumnCount].innerHTML;
	}
	//fill properties and control data
	PageManager.setControlData(table,sortIndexArr);
    //delete rows
    for(var k=0;k<oldRowCount-1;k++)
	{
		table.deleteRow(1);
	}
	
    for(var k=0;k<oldRowCount;k++)
	{
		table.rows[k].deleteCell(oldColumnCount);
	}
 
}

function getParent(el, pTagName) {
	if (el == null) return null;
	else if (el.nodeType == 1 && el.tagName.toLowerCase() == pTagName.toLowerCase())	// Gecko bug, supposed to be uppercase
		return el;
	else
		return getParent(el.parentNode, pTagName);
}
function sortDate(a,b) {
    // y2k notes: two digit years less than 50 are treated as 20XX, greater than 50 are treated as 19XX
    aa = getInnerText(a.cells[SORT_COLUMN_INDEX]);
    bb = getInnerText(b.cells[SORT_COLUMN_INDEX]);
    if (aa.length == 10) {
        dt1 = aa.substr(6,4)+aa.substr(3,2)+aa.substr(0,2);
    } else {
        yr = aa.substr(6,2);
        if (parseInt(yr) < 50) { yr = '20'+yr; } else { yr = '19'+yr; }
        dt1 = yr+aa.substr(3,2)+aa.substr(0,2);
    }
    if (bb.length == 10) {
        dt2 = bb.substr(6,4)+bb.substr(3,2)+bb.substr(0,2);
    } else {
        yr = bb.substr(6,2);
        if (parseInt(yr) < 50) { yr = '20'+yr; } else { yr = '19'+yr; }
        dt2 = yr+bb.substr(3,2)+bb.substr(0,2);
    }
    if (dt1==dt2) return 0;
    if (dt1<dt2) return -1;
    return 1;
}

function sortCurrency(a,b) { 
    aa = getInnerText(a.cells[SORT_COLUMN_INDEX]).replace(/[^0-9.]/g,'');
    bb = getInnerText(b.cells[SORT_COLUMN_INDEX]).replace(/[^0-9.]/g,'');
    return parseFloat(aa) - parseFloat(bb);
}

function sortNumeric(a,b) { 

    aa = parseFloat(getInnerText(a.cells[SORT_COLUMN_INDEX]));
    if (isNaN(aa)) aa = 0;
    bb = parseFloat(getInnerText(b.cells[SORT_COLUMN_INDEX])); 
    if (isNaN(bb)) bb = 0;
    return aa-bb;
}

function sortCaseInsensitive(a,b) {
    aa = getInnerText(a.cells[SORT_COLUMN_INDEX]).toLowerCase();
    bb = getInnerText(b.cells[SORT_COLUMN_INDEX]).toLowerCase();
    if (aa==bb) return 0;
    if (aa<bb) return -1;
    return 1;
}

function sortDefault(a,b) {
    aa = getInnerText(a.cells[SORT_COLUMN_INDEX]);
    bb = getInnerText(b.cells[SORT_COLUMN_INDEX]);
    if (aa==bb) return 0;
    if (aa<bb) return -1;
    return 1;
}

function sortGeneric(a,b) 
{
    var alpha=0;

    aa = parseFloat(getInnerText(a.cells[SORT_COLUMN_INDEX]).replace(',',''));
    if (isNaN(aa))
	{ 
	  alpha=1;
	  aa = getInnerText(a.cells[SORT_COLUMN_INDEX]);
	}


    bb = parseFloat(getInnerText(b.cells[SORT_COLUMN_INDEX]).replace(',',''));
    if (isNaN(bb)) 
	{
	  alpha=1;
	  bb=getInnerText(b.cells[SORT_COLUMN_INDEX]);
	}


    if(alpha==0)  return aa-bb;//means number
	else 
	{
		if (aa==bb) return 0;
	    if (aa<bb) return -1;
    	return 1;
	}



}

 
