/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
// Calendar Display
// www.mredkj.com
// June 08, 2002 - version 1.0.0
// November 14, 2002 - version 1.1.0
// March 07, 2006 - version 2.BETA.20060307
// May 08, 2006 - version 2.BETA.20060508
// July 25, 2006 - version 2.BETA.20060725
// October 10, 2006 - version 2.BETA.20061010
//
// to do: If click away from the calendar, then hide it. This will help avoid the orphan calendar when deleting text boxes.
//
function toggleCalendar(txtObj)
{	
	cObj = txtObj.myCalendar;
	if (!cObj) {
		cObj = new CalendarDisplay(txtObj);
		document.body.appendChild(cObj.cDiv);
		txtObj.myCalendar = cObj;
	}
	
	cObj.toggle();
}

CalendarDisplay = function(txtObj) {
	this.txtObj = txtObj;
	this.tBox = this.txtObj;
	this.cDiv = document.createElement('div');
	this.cDiv.style.position = 'absolute';
	this.cDiv.style.display = 'none';
}

CalendarDisplay.prototype.MONTHS_CALENDAR = new Array("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
CalendarDisplay.prototype.DAYS_1_CALENDAR = new Array("S", "M", "T", "W", "T", "F", "S");
CalendarDisplay.prototype.DAYS_2_CALENDAR = new Array("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa");

CalendarDisplay.prototype.toggle = function() {
	if (this.cDiv.style.display == 'none') {
		this.adjustPosition();
		this.fillCalendar(this.grabDate());
		this.cDiv.style.display = 'block';
	} else {
		this.cDiv.style.display = 'none';
	}
}

CalendarDisplay.prototype.grabDate = function() {
	var tempDate = new Date(this.tBox.value);
	if (!tempDate.getYear()) {
		tempDate = new Date();
	}
	return tempDate;
}

CalendarDisplay.prototype.fillCalendar = function(theDate) {
	if (this.cDiv.firstChild) {
		this.cDiv.removeChild(this.cDiv.firstChild);
	}
	this.adjustPosition();
	this.cDiv.appendChild(this.getCalendar(theDate));
}

CalendarDisplay.prototype.adjustPosition = function() {
	this.cDiv.style.top = this.tBox.offsetHeight + this.findPosY(this.tBox) + 'px';
// !! IE, FF, and Opera positions can be slightly different depending on the page layout.
// !! I think it has to do with the page padding
	this.cDiv.style.left = this.findPosX(this.tBox) + 'px';
}

CalendarDisplay.prototype.getCalendar = function(theDate) {
	var theYear = theDate.getFullYear();
	var theMonth = theDate.getMonth();
	var theDay = theDate.getDate();

	var theTable = document.createElement('table');
	theTable.id = 'calendartable';
	var theTHead = theTable.createTHead();
	var theTBody = document.createElement('tbody');
	theTable.appendChild(theTBody);
	
	var monthRow = theTHead.insertRow(0);
	var navLeftCell = monthRow.insertCell(0);
	var monthCell = monthRow.insertCell(1);
	var navRightCell = monthRow.insertCell(2);
	monthCell.colSpan = 5;
	monthCell.appendChild(document.createTextNode(this.MONTHS_CALENDAR[theMonth] + ', ' + theYear));
	var leftLink = document.createElement('a');
	leftLink.href = '#';
	this.setCalendarPrevious(leftLink, this.txtObj, theYear, theMonth, theDay);
	leftLink.appendChild(document.createTextNode('-'));
	navLeftCell.appendChild(leftLink);
	var rightLink = document.createElement('a');
	rightLink.href = '#';
	this.setCalendarNext(rightLink, this.txtObj, theYear, theMonth, theDay);
	rightLink.appendChild(document.createTextNode('+'));
	navRightCell.appendChild(rightLink);
	
	var weeksRow = theTHead.insertRow(1);
	for (var i=0; i<7; i++) {
		var tempWeeksCell = weeksRow.insertCell(i);
		tempWeeksCell.appendChild(document.createTextNode(this.DAYS_2_CALENDAR[i]));
	}
	
	var temporaryDate1 = new Date(theYear, theMonth, 1);
	var startDayOfWeek = temporaryDate1.getDay();
	var temporaryDate2 = new Date(theYear, theMonth + 1, 0);
	var lastDateOfMonth = temporaryDate2.getDate();
	var dayCount = 1;
		
	for (var r=0; r<6; r++) {
		var tempDaysRow = theTable.tBodies[0].insertRow(r);
		tempDaysRow.className = 'dayrow';
		for (var c=0; c<7; c++) {
			var tempDaysCell = tempDaysRow.insertCell(c);
			var mysteryNode;
			if ((r > 0 || c >= startDayOfWeek) && dayCount <= lastDateOfMonth) {
				tempDaysCell.className = 'yestext';
				var mysteryNode = document.createElement('a');
				mysteryNode.href = '#';
				this.setCalendarClick(mysteryNode, this.txtObj, theYear, theMonth, dayCount);
				mysteryNode.appendChild(document.createTextNode(dayCount));
				dayCount++;
			} else {
				tempDaysCell.className = 'notext';
				mysteryNode = document.createTextNode('');
			}
			tempDaysCell.appendChild(mysteryNode);
		}
	}
	
	return theTable;
}
CalendarDisplay.prototype.setCalendarClick = function (node, theObj, theYear, theMonth, theDay) {
	node.onclick = function() {fillInFields(theObj, theYear, (theMonth + 1), theDay); return false;}
}
CalendarDisplay.prototype.setCalendarPrevious = function (node, theObj, theYear, theMonth, theDay) {
	node.onclick = function() {showPrevious(theObj, theYear, theMonth, theDay); return false;}
}
CalendarDisplay.prototype.setCalendarNext = function (node, theObj, theYear, theMonth, theDay) {
	node.onclick = function() {showNext(theObj, theYear, theMonth, theDay); return false;}
}
	

// http://www.quirksmode.org/js/findpos.html
CalendarDisplay.prototype.findPosX = function(obj) {
	var curleft = 0;
	if (obj.offsetParent) {
		while (obj.offsetParent) {
			curleft += obj.offsetLeft;
			obj = obj.offsetParent;
		}
	}
	else if (obj.x) {
		curleft += obj.x;
	}
	return curleft;
}

// http://www.quirksmode.org/js/findpos.html
CalendarDisplay.prototype.findPosY = function(obj) {
	var curtop = 0;
	if (obj.offsetParent)	{
		while (obj.offsetParent) {
			curtop += obj.offsetTop;
			obj = obj.offsetParent;
		}
	}
	else if (obj.y) {
		curtop += obj.y;
	}
	return curtop;
}

function fillInFields(obj, year, month, day)
{
	obj.value = (month < 10 ? '0'+month : month) + '/' + (day < 10 ? '0'+day : day) + '/' + year;
	cObj = obj.myCalendar;
	cObj.toggle();
}

function showPrevious(obj, year, month, day)
{
	cObj = obj.myCalendar;
	var lastMonth = new Date(year, month - 1, day)
	cObj.fillCalendar(lastMonth);
}
function showNext(obj, year, month, day)
{
	cObj = obj.myCalendar;
	var nextMonth = new Date(year, month + 1, day)
	cObj.fillCalendar(nextMonth);
}
