// Table Delete Row with Calendar
// mredkj.com
// based off tabledeleterow.js version 1.2 2006-02-21
// tabledeleterow-calendar.js version 1.2.1 2006-05-04
// tabledeleterow-calendar.js version 1.2.2 2006-10-10

// CONFIG notes. Below are some comments that point to where this script can be customized.
// Note: Make sure to include a <tbody></tbody> in your table's HTML
// Note: Cannot use this with any calendar script that references object ids
//		 The way the rows are added and deleted, ids aren't reset.

var INPUT_NAME_PREFIX = 'inputName'; // this is being set via script
var RADIO_NAME = 'totallyrad'; // this is being set via script
var TABLE_NAME = 'tblSample'; // this should be named in the HTML
var ROW_BASE = 1; // first number (for display)
var hasLoaded = false;

window.onload=fillInRows;


function debug(msg)
{
    var debug = document.getElementById('txtdebug')
    var d = new Date();
    var tempTimestamp =
    	(d.getHours() < 10 ? '0' + d.getHours() : d.getHours())
    	+ ':' +
    	(d.getMinutes() < 10 ? '0' + d.getMinutes() : d.getMinutes())
    	+ ':' +
    	(d.getSeconds() < 10 ? '0' + d.getSeconds() : d.getSeconds())
    debug.value = tempTimestamp + ':' + msg + '\n' + debug.value ;
}

function fillInRows()
{
	hasLoaded = true;
	addRowToTable();
	addRowToTable();
}

// CONFIG:
// myRowObject is an object for storing information about the table rows
function myRowObject(one, two)
{
	this.one = one; // text object
	this.two = two; // input text object
}

/*
 * addRowToTable
 * Inserts at row 'num', or appends to the end if no arguments are passed in. Don't pass in empty strings.
 */
function addRowToTable(num)
{
	if (hasLoaded) {
		var tbl = document.getElementById(TABLE_NAME);
		var nextRow = tbl.tBodies[0].rows.length;
		var iteration = nextRow + ROW_BASE;
		if (num == null) { 
			num = nextRow;
		} else {
			iteration = num + ROW_BASE;
		}
		
		// add the row
		var row = tbl.tBodies[0].insertRow(num);
		
		// CONFIG: requires classes named classy0 and classy1
		row.className = 'classy' + (iteration % 2);
	
		// CONFIG: This whole section can be configured
		
		// cell 0 - text
		var cell0 = row.insertCell(0);
		var textNode = document.createTextNode(iteration);
		cell0.appendChild(textNode);
		
		// cell 1 - input text
		var cell1 = row.insertCell(1);
		var txtInp = document.createElement('input');
		txtInp.type = 'text';
		txtInp.name = INPUT_NAME_PREFIX + iteration;
		// Don't refer to ids
		//txtInp.id = INPUT_NAME_PREFIX + iteration;
		txtInp.size = 40;
		txtInp.value = '';
		cell1.appendChild(txtInp);
		
		// cell 2 - calendar button
		var cell2 = row.insertCell(2);
		var btnCal = document.createElement('input');
		btnCal.type = 'button';
		btnCal.value = 'Calendar';
		btnCal.onclick = function () {toggleCalendar(txtInp);};
		cell2.appendChild(btnCal);
		
		// cell 3 - delete button
		var cell3 = row.insertCell(3);
		var btnEl = document.createElement('input');
		btnEl.type = 'button';
		btnEl.value = 'Delete';
		btnEl.onclick = function () {deleteCurrentRow(this)};
		cell3.appendChild(btnEl);
				
		// Pass in the elements you want to reference later
		// Store the myRow object in each row
		row.myRow = new myRowObject(textNode, txtInp);
	}
}

// If there isn't an element with an onclick event in your row, then this function can't be used.
function deleteCurrentRow(obj)
{
	if (hasLoaded) {
		var delRow = obj.parentNode.parentNode;
		var tbl = delRow.parentNode.parentNode;
		var rIndex = delRow.sectionRowIndex;
		var rowArray = new Array(delRow);
		deleteRows(rowArray);
		reorderRows(tbl, rIndex);
	}
}

function reorderRows(tbl, startingIndex)
{
	if (hasLoaded) {
		if (tbl.tBodies[0].rows[startingIndex]) {
			var count = startingIndex + ROW_BASE;
			for (var i=startingIndex; i<tbl.tBodies[0].rows.length; i++) {
			
				// CONFIG: next line is affected by myRowObject settings
				tbl.tBodies[0].rows[i].myRow.one.data = count; // text
				
				// CONFIG: next line is affected by myRowObject settings
				tbl.tBodies[0].rows[i].myRow.two.name = INPUT_NAME_PREFIX + count; // input text
				
				// CONFIG: requires class named classy0 and classy1
				tbl.tBodies[0].rows[i].className = 'classy' + (count % 2);
				
				count++;
			}
		}
	}
}

function deleteRows(rowObjArray)
{
	if (hasLoaded) {
		for (var i=0; i<rowObjArray.length; i++) {
			var rIndex = rowObjArray[i].sectionRowIndex;
			rowObjArray[i].parentNode.deleteRow(rIndex);
		}
	}
}

function openInNewWindow(frm)
{
	// open a blank window
	var aWindow = window.open('', 'TableAddRow2NewWindow',
	'scrollbars=yes,menubar=yes,resizable=yes,toolbar=no,width=400,height=400');
	
	// set the target to the blank window
	frm.target = 'TableAddRow2NewWindow';
	
	// submit
	frm.submit();
}
