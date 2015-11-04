jQuery(document).ready(function(){

	patternvalidation(); 
	
});

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

//username pattern (eg: data-pattern="username")
var regexp_username = /[^a-zA-Z0-9_.]/g ;

//This will allow you to enter alphabets and numbers with hyphen(-). (eg: data-pattern="alphanumericwithhyphen")
var regexp_alphanumerichyphen = /[^a-zA-Z0-9-]/g ;

//This will allow you to enter numbers with hyphen(-) and (/). (eg: data-pattern="numericslashhyphen")
var regexp_numericslashhyphen = /[^0-9/-]/g ;

function patternvalidation(){
	
	jQuery('.patternvalidation').on("input", function(){
		
		var fn = window[jQuery(this).data('pattern')];
		if(typeof fn === "function") {	
			fn(this);
		}

	});
	
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

