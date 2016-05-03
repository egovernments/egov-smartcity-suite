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
var currentDateForCalendar = null; // variable used to pass date to calendar html
if (typeof(window.exilWindow) == 'undefined')window.exilWindow = window;

function PageValidatorObject(){
	this.regex = new Object();
	this.regex['exilAnyChar'] = /^[\w\~\!\@\#\$\%\^\*\(\)\-\+\|\{\}\:\"\<\>\?\`\_\\\[\]\;\,\.\s\\/\=]*$/; 
	//this.regex['exilAnyChar'] =/^[a-zA-Z0-9\s\/\-\(\)\%\,\.\']*$/; 
	this.regex['exilAlpha'] = /^[a-zA-Z]*$/; 
	this.regex['exilAlphaNumeric'] = /^[\w\/\-\:\(\)\_]*$/;
	this.regex['exilUnsignedInt'] = /^\d*$/;
	this.regex['exilSignedInt'] = /^[+-]?\d*$/;
	this.regex['exilUnsignedDecimal'] = /^\d*\.?\d*$/;
	this.regex['exilSignedDecimal'] = /^[+-]?\d*\.?\d*$/;
	this.regex['exilAnyDate'] = /^\d\d?\/\w\w\/\d\d\d\d$/;  
	this.regex['exilPastDate'] = this.regex['exilAnyDate'];
	this.regex['exilFutureDate'] = this.regex['exilAnyDate'];
	this.regex['exilEmail'] =/^\w+(\.?\w+)?@[\w-]+(\.[\w-]+)*$/ ;

	this.err = new Object();
	this.err['exilAlpha'] = 'alphabetical value'; 
	this.err['exilAlphaNumeric'] = 'alpha-numeric value';
	this.err['exilUnsignedInt'] = 'positive integer';
	this.err['exilSignedInt'] = 'integer';
	this.err['exilUnsignedDecimal'] = 'positive number';
	this.err['exilSignedDecimal'] = 'number';
	this.err['exilAnyDate'] = 'date';
	this.err['exilPastDate'] = 'date in the past';
	this.err['exilFutureDate'] = 'date in the future';
	this.err['exilEmail'] ='email address';
	this.months  =["jan","feb","mar","apr","may","jun","jul","aug","sep","oct","nov","dec"];
	
	this.validateForm = function (){
		// elements marked for validations are picked up and validated
		var frm = exilWindow.document.forms[0];
		var n = frm.elements.length;

		//parse 1 for field level validation
		for(var i=0; i<n; i++)if (!this.validateElement(frm.elements[i])) return false;
			 
		//parse 2 to check from & to fields references
		for(var i=0; i<n; i++){   
			var ele = frm.elements[i]; 
			if(ele.getAttribute('exilFromField') && !this.validateFromTo(ele)) return false;
		}
		return this.validateUniqueColumns();
	}
	
	this.validateElement = function (ele){
		var val = this.getElementValue(ele);
		var typ = ele.getAttribute('exilDataType');
		if ("" == val){
			if (ele.getAttribute('exilMustEnter'))return this.showError(ele,'You must enter a valid value in this field');
			else return true;
		}			
		if (typ) {
			var regex = this.regex[typ];
			if (!regex) regex = new RegExp(typ); //use type as regex
			if (!val.match(regex))return this.showError(ele, 'Please enter a valid ' + (this.err[typ]? this.err[typ] : typ));

			// That took care of regex. look at date fields
			if (this.isDateType(typ)){
				var dat = this.getDate(val);
				if (!dat) return this.showError(ele, 'Invalid date format');

				var today = this.getToday();
				
				var minDate = this.getDate(ele.getAttribute('exilMin'));
				if (!minDate) minDate = exilParms.minDate;
				if (typ == 'exilFutureDate' && minDate < today) minDate = today;
				
				var maxDate = this.getDate(ele.getAttribute('exilMax'));
				if (!maxDate) maxDate = exilParms.maxDate;
				if (typ == 'exilPastDate' && maxDate > today) maxDate = today;
				if (dat > maxDate || dat < minDate)return this.showError(ele, ("Enter a date between " + this.showDate(minDate) + " and " + this.showDate(maxDate)));
				return true;//date type is fully taken care of, so return, 
			}else if (this.isNumericType(typ)){ //
				var valnum = parseFloat(val);
				if (0 == valnum && ele.getAttribute('exilMustEnter')) return this.showError(ele, "Please enter a non zero value");
			}
		}
		//min/max 
		var min = ele.getAttribute('exilMin'); 
		var max = ele.getAttribute('exilMax');
		if (this.isNumericType(typ)){
			min = parseFloat(min);
			max = parseFloat(max);
			val = parseFloat(val);
		}
		if (min && val < min) return this.showError(ele,("Value should be greater than or equal to " + min));
		if (max && val > max) return this.showError(ele,("Value should be less than or equal to " + max));
		return true;
	}
	
	this.validateFromTo = function (toele){
		var fromele = toele.getAttribute('exilFromField');
		if(!fromele) return true; 
		
		var typ = toele.getAttribute('exilDataType');
		if (this.objectIsInGrid(toele))fromele = this.getElementInGrid(toele, fromele);
		else fromele = exilWindow.document.getElementById(fromele);
		if(!fromele) return this.showError (toele, "This to-field has a wrong from-field reference to " );

		var fromval = this.getElementValue(fromele);
		var	toval = this.getElementValue(toele);
		var valid = true;
		if (fromval  || toval ){
			
			if (this.isDateType(typ)) {
				if ( this.getDate(fromval) > this.getDate(toval)) valid = false;
			}else if (this.isNumericType(typ)) {
				if ( ParseFloat(fromval) > ParseFloat(toval)) valid = false;
			}else{
				if (fromval > toval)valid = false; 
			}
			if (!valid) this.showError(toele, 'To-field value should be gretaer than or equal to the from-field');
		}
		return valid;
	}

	this.getElementValue = function (ele){

		if (ele.type.toLowerCase() == 'checkbox'){
			if (ele.checked)return ele.value;
			else return "";
		}
		
		if(ele.type.toLowerCase()=='radio'){//get the value of the button that is checked
			var eles = exilWindow.document.getElementsByName(ele.name);
			for (var i=0; i<eles.length; i++){
				if (eles[i].checked)return ele.value;
			}
			return ""
		}
		
		if(ele.tagName.toLowerCase() == 'select'){
			if (ele.selectedIndex >= 0)return ele.options[ele.selectedIndex].value;
			else return "";
		}
		ele.value  = this.trim(ele.value);
		return ele.value;
	}

	this.getElementInGrid = function (ele, nam){
		//returns the object with name=nam in the same row as that of toele
		if(!ele)return null;
		while(ele.nodeName.toUpperCase()!="TR")ele=ele.parentNode;

		return this.getControlInBranch(ele,nam);
	}
	
	this.getControlInBranch = function (obj,controlName){
	
		if (!obj) return null;
		// check if the object itself has the name
		if (obj.name == controlName) return obj;

		// try its children
		var children = obj.childNodes;
		var child;
		if (children && children.length > 0){ 
			for(var i=0; i<children.length; i++){
				child=this.getControlInBranch(children[i],controlName);
				if(child) return child;
			}
		}
		return null;
	}

	this.validateUniqueColumns = function (){
		var eles = exilWindow.document.getElementsByTagName('TABLE');
		var cols, val, vals;
		for (var i=0; i<eles.length; i++){
			if (!eles[i].getAttribute('exilUniqueColumn'))continue;
			cols = new String(eles[i].getAttribute('exilUniqueColumn'));
			cols = cols.split(","); //cotrol names that have to be together unique across rows

			var controls = new Array();
			for(var j=0; j<cols.length; j++){
				controls[j] = exilWindow.document.getElementsByName(cols[j]); //get controls from all columns
			}
			vals = new Array();
			for(var j=0; j<controls[0].length; j++){ //for each row
				val = "";
				for(var k=0; k<cols.length; k++){ //concatenate values from columns
					val += this.getElementValue(controls[k][j]);
				}
				if (val == "") continue; //no unique checking if nothing is entered
				for(var k=0; k<j; k++){ //check whether this value clashes with values for ealrier rows
					if (val == vals[k]){
						this.showError(controls[0][k], "Duplicate values in row " + (k+1) + " and row " +(j+1));
						return false;
					}
				}
				vals[j] = val;
			}
		}
		return true;
	}
	
	this.showError = function (ele, err){
		var color = ele.style.backgroundColor;
		ele.style.backgroundColor = 'red';
		if (ele.getAttribute('exilErrorMessage')) err = ele.getAttribute('exilErrorMessage');
		bootbox.alert(err);
		ele.style.backgroundColor = color;
		if (typeof ele.select != 'undefined') ele.select();
		return false;
	}

	this.trim =	function (str){
		str = new String(str);
		var c, l;
		while (true){// left trim
			c = str.substr(0,1);
			if (c == ' ' || c== '\r' || c == '\t' || c == '\n')str = str.substr(1);
			else break;
		}
		
		while(true){//right trim
			l = str.length;
			c = str.substr(l-1,1);
			if (c == ' ' || c== '\r' || c == '\t' || c == '\n')str = str.substr(0,l-1);
			else break;
		}
		return str;
	}

	this.showCalendar = function (targetName){
		var x = 0;
		var y = 0;
		var targetObj = exilWindow.document.getElementById(targetName);
		if (!targetObj)return false;
		var obj = targetObj;
		if( obj.offsetParent ) {
			while(obj.offsetParent){
				x += obj.offsetLeft;
				y += obj.offsetTop;
				obj = obj.offsetParent;
			}
		}else{
			x = obj.x;
			y = obj.y;
		}
		x -= 205;
		y +=165;
		//set the starting date for teh calendar
		currentDateForCalendar = this.getDate(targetObj.value);
		if (typeof showModalDialog != 'undefined'){//IE
			var dateReturned = showModalDialog("calendar.htm",currentDateForCalendar,"dialogLeft=" + x + ";dialogTop=" + y + ";dialogWidth=180pt;dialogHeight=170pt;status=no;");
			if (dateReturned) targetObj.value = dateReturned;
		} else{
			var win = exilWindow.open("calendar.htm", "calendar", "directories=no, location=no, menubar=no, resizable=no, scrollbars=no, status=no, titlebar=no, toolbar=no, left="+x+", top="+y+", height=200, width=250", true);
			exilWindow.targetForCalendar = targetObj;
			exilWindow.onfocus = function () {if (!win.closed) win.focus();}
		}
	}

//add calendar icons infront of controls tagged with exilCalendar="true"
	this.addCalendars =	function (){
		var frm =  exilWindow.document.forms[0];
		var ele;
		var htm1 = '<a onclick="PageValidator.showCalendar(\'';
		var htm2 = '\');" tabindex="-1" href="#"><img tabindex="-1" src="/egi/resources/erp2/images/calendar.gif" ></a>';
		//netscape 6.2 acts crazy when we change some elements inside form.
		//store all controls before adding calendars;
		var eles = new Array();
		for(var i=0; i<frm.elements.length; i++)
			if (frm.elements[i].getAttribute("exilCalendar")) 
				eles[eles.length] = frm.elements[i];
		for (var i=0; i<eles.length; i++) 
			eles[i].parentNode.innerHTML += htm1 + eles[i].id + htm2;
	}
	
	this.isDateType = function (typ){
		return (typ == 'exilAnyDate' || typ == 'exilPastDate' || typ == 'exilFutureDate');
	}
	
	this.isNumericType = function (typ){
		return (typ == 'exilSignedInt' || typ == 'exilUnsignedInt' || typ == 'exilSignedDecimal' || typ == 'exilUnignedDecimal' );
	}
	
	this.getDate = function (val){
	
		if (!val) return null;
		var parts = val.split("/");
		if (parts.length != 3) return null;
		dd = parseInt(parts[0],10);
		if(isNaN(dd)) return null;

		
		var mm=parseInt(parts[1]-1);
		if(isNaN(mm)) return null;
		if (mm < 0 && mm > 11) return null;
	
		
		var yyyy = parseInt(parts[2]);
		if (isNaN(yyyy)) return null;
		
		var maxdd = 0;
		if((yyyy % 4 == 0) && ((!(yyyy % 100 == 0)) || (yyyy % 400 == 0)))
			{
					switch(mm+1)
					{
						case 1: maxdd = 31; break;  
						case 2: maxdd = 29; break;
						case 3: maxdd = 31; break;
						case 4: maxdd = 30; break;
						case 5: maxdd = 31; break;
						case 6: maxdd = 30; break;
						case 7: maxdd = 31; break;
						case 8: maxdd = 31; break;
						case 9: maxdd = 30; break;
						case 10: maxdd = 31; break;
						case 11: maxdd = 30; break;
						case 12: maxdd = 31; break;
								 
					 }
					 if(dd > maxdd) return null;					 
			 		var dat = this.getToday();
			 		dat.setFullYear(yyyy);
			 		dat.setMonth(mm);
			 		dat.setDate(dd);
			 		return dat; 
			}
			else 
			{
				switch(mm+1)
				{
					case 1: maxdd = 31; break;  
					case 2: maxdd = 28; break;
					case 3: maxdd = 31; break;
					case 4: maxdd = 30; break;
					case 5: maxdd = 31; break;
					case 6: maxdd = 30; break;
					case 7: maxdd = 31; break;
					case 8: maxdd = 31; break;
					case 9: maxdd = 30; break;
					case 10: maxdd = 31; break;
					case 11: maxdd = 30; break;
					case 12: maxdd = 31; break;
							 
				 }
				 	if(dd > maxdd) return null;
			 		var dat = this.getToday();
			 		dat.setFullYear(yyyy);
			 		dat.setMonth(mm);
			 		dat.setDate(dd);
			 		return dat;
			}

			return null;
	}
	this.getToday = function (){
		var dat = new Date();
		dat.setHours(0);
		dat.setMinutes(0);
		dat.setSeconds(0);
		dat.setMilliseconds(0);
		return dat;
	}

	this.showDate = function(dat){
		if (!dat) return "";
		if (dat.constructor != Date) return "";
		return dat.getDate() +"/" +(dat.getMonth()+1) + "/" + dat.getFullYear(); 
	}
	
	this.onblur = function (e){
		var evt = (exilWindow.event)? event : e;
		var ele = (evt.srcElement) ? evt.srcElement : evt.target;
		if (ele.getAttribute('exilImmidiateValidation')){
			return(this.validateElement(ele));
		}
		if (ele.getAttribute('exilDataSource')){
			if (typeof(PageManager)!= 'undefined') PageManager.DescService.getDesc(ele);
		}
	}
	
	this.objectIsInGrid = function(obj){
		if (null == obj || !obj.tagName) return false;
		var tagName = obj.tagName.toUpperCase();
		if (tagName == "BODY") return false;
		if (tagName == "TABLE" && obj.getAttribute("exilDataSource")) return true;
		return (this.objectIsInGrid(obj.parentNode));
	}
}
var PageValidator = new PageValidatorObject();
