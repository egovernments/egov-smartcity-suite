/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

jQuery(document).ready(function(){

	patternvalidation(); 
	
});

//Add class to input field "patternvalidation" and add custom data attribute(eg: data-pattern="alphabets")

//This will allow you to enter only alphabets with or without space. (eg: data-pattern="alphabets")
var regexp_alphabets = /[^a-zA-Z]/g;

//Add class to input field "patternvalidation" and add custom data attribute(eg: data-pattern="alphabetwithspace") 

//This will allow you to enter only alphabets with or without space. (eg: data-pattern="alphabetwithspace")
var regexp_alphabet = /[^a-zA-Z ]/g ; 

//This will allow you to enter only alphabets with specified special characters like dot(.), slash(/), hash(#), ampersand(&), plus(+), minus(-). If you need some additional special characters, add those characters to the corresponding regular expression. 
//(eg: data-pattern="alphabetwithspecialcharacters")
var regexp_alphabetspecialcharacters = /[^a-zA-Z_@./#&+-]/g ;

//This will allow you to enter only numbers. (eg: data-pattern="number")
var regexp_number = /[^0-9]/g ;

//This will allow you to enter alphabets and numbers with or without space. (eg: data-pattern="alphanumericwithspace")
var regexp_alphanumeric = /[^a-zA-Z0-9 ]/g ;

//This will allow you to enter alphabets and numbers with or without space and dot(.). (eg: data-pattern="alphanumericwithspaceanddot")
var regexp_alphanumericdot = /[^a-zA-Z0-9 .]/g ;

//This will allow you to enter alphabets and numbers with specified special characters like dot(.), slash(/), hash(#), ampersand(&), plus(+), minus(-). If you need some additional special characters, add those characters to the corresponding regular expression. (eg: data-pattern="alphanumericwithspecialcharacters")
var regexp_alphanumericspecialcharacters = /[^a-zA-Z0-9_@./#&+-]/g ;

//This will allow you to enter alphabets and numbers with space, hyphen(-) and underscore(_). (eg: data-pattern="alphanumericwithspacehyphenunderscore")
var regexp_alphanumerichyphenunderscore = /[^a-zA-Z0-9 _-]/g ;

//This will allow you to enter alphabets with space, hyphen(-) and underscore(_). (eg: data-pattern="alphabetwithspacehyphenunderscore")
var regexp_alphabethyphenunderscore = /[^a-zA-Z _-]/g ;

//This will allow you to enter numbers and dot. (eg: data-pattern="decimalvalue")
var regexp_decimalvalue = /[^0-9.]/g ;

//This will allow you to enter alphabets and numbers with specified special characters like  slash(/), hyphen(-). (eg: data-pattern="alphanumerichyphenbackslash")
var regexp_alphanumerichyphenbackslash = /[^a-zA-Z0-9/-]/g ;

//This will allow you to enter alphabets and numbers with specified special characters like  slash(/), hyphen(-),brackets(),comma(,). (eg: data-pattern="address")
var regexp_address = /[^a-zA-Z0-9/(),-]/g ;

//username pattern (eg: data-pattern="username")
var regexp_username = /[^a-zA-Z0-9_.]/g ;

//This will allow you to enter alphabets and numbers with hyphen(-). (eg: data-pattern="alphanumericwithhyphen")
var regexp_alphanumerichyphen = /[^a-zA-Z0-9-]/g ;

//This will allow you to enter numbers with hyphen(-) and (/). (eg: data-pattern="numericslashhyphen")
var regexp_numericslashhyphen = /[^0-9/-]/g ;

//This will allow you to enter numbers with hyphen(-). (eg: data-pattern="numerichyphen")
var regexp_numerichyphen = /[^0-9-]/g ;


function patternvalidation(){
	
	jQuery('.patternvalidation').on("input", function(){
		
		var fn = window[jQuery(this).data('pattern')];
		if(typeof fn === "function") {	
			fn(this);
		}

	});
	
}

function alphabets(obj){
    if(jQuery(obj).val().match(regexp_alphabets)){
        jQuery(obj).val( jQuery(obj).val().replace(regexp_alphabets,'') );
    }
}

function alphabetwithspace(obj){
	if(jQuery(obj).val().match(regexp_alphabet)){
		jQuery(obj).val( jQuery(obj).val().replace(regexp_alphabet,'') );
	}
}

function alphabetwithspecialcharacters(obj){
	if(jQuery(obj).val().match(regexp_alphabetspecialcharacters)){
		jQuery(obj).val( jQuery(obj).val().replace(regexp_alphabetspecialcharacters,'') );
	}
}

function number(obj){
	if(jQuery(obj).val().match(regexp_number)){
		jQuery(obj).val( jQuery(obj).val().replace(regexp_number,'') );
	}
}

function alphanumericwithspace(obj){
	if(jQuery(obj).val().match(regexp_alphanumeric)){
		jQuery(obj).val( jQuery(obj).val().replace(regexp_alphanumeric,'') );
	}
}

function alphanumericwithspecialcharacters(obj){
	if(jQuery(obj).val().match(regexp_alphanumericspecialcharacters)){
		jQuery(obj).val( jQuery(obj).val().replace(regexp_alphanumericspecialcharacters,'') );
	}
}

function alphanumericwithspacehyphenunderscore(obj){
	if(jQuery(obj).val().match(regexp_alphanumerichyphenunderscore)){
		jQuery(obj).val( jQuery(obj).val().replace(regexp_alphanumerichyphenunderscore,'') );
	}
}

function alphabetwithspacehyphenunderscore(obj){
	if(jQuery(obj).val().match(regexp_alphabethyphenunderscore)){
		jQuery(obj).val( jQuery(obj).val().replace(regexp_alphabethyphenunderscore,'') );
	}
}

function decimalvalue(obj){
	if(jQuery(obj).val().match(regexp_decimalvalue)){
		jQuery(obj).val( jQuery(obj).val().replace(regexp_decimalvalue,'') );
	}
}

function alphanumerichyphenbackslash(obj){
	if(jQuery(obj).val().match(regexp_alphanumerichyphenbackslash)){
		jQuery(obj).val( jQuery(obj).val().replace(regexp_alphanumerichyphenbackslash,'') );
	}
}


function address(obj){
	if(jQuery(obj).val().match(regexp_address)){
		jQuery(obj).val( jQuery(obj).val().replace(regexp_address,'') );
	}	
}
function username(obj){
	if(jQuery(obj).val().match(regexp_username)){
		jQuery(obj).val( jQuery(obj).val().replace(regexp_username,'') );
	}
}

function alphanumericwithhyphen(obj){
	if(jQuery(obj).val().match(regexp_alphanumerichyphen)){
		jQuery(obj).val( jQuery(obj).val().replace(regexp_alphanumerichyphen,'') );
	}
}

function numericslashhyphen(obj){
	if(jQuery(obj).val().match(regexp_numericslashhyphen)){
		jQuery(obj).val( jQuery(obj).val().replace(regexp_numericslashhyphen,'') );
	}
}

function alphanumericwithspaceanddot(obj){
	if(jQuery(obj).val().match(regexp_alphanumericdot)){
		jQuery(obj).val( jQuery(obj).val().replace(regexp_alphanumericdot,'') );
	}
}

function numerichyphen(obj){
	if(jQuery(obj).val().match(regexp_numerichyphen)){
		jQuery(obj).val( jQuery(obj).val().replace(regexp_numerichyphen,'') );
	}
}