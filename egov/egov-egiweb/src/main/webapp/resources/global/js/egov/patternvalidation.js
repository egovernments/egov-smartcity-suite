$(document).ready(function(){

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

//This will allow you to enter numbers and dot. (eg: data-pattern="decimalvalue")
var regexp_decimalvalue = /[^0-9.]/g ;

//This will allow you to enter alphabets and numbers with specified special characters like  slash(/), hyphen(-). (eg: data-pattern="alphanumerichyphenbackslash")
var regexp_alphanumerichyphenbackslash = /[^a-zA-Z0-9/-]/g ;

//username pattern (eg: data-pattern="username")
var regexp_username = /[^a-zA-Z0-9_.]/g ;

function patternvalidation(){
	
	$('.patternvalidation').on("input", function(){
		
		var fn = window[$(this).data('pattern')];
		if(typeof fn === "function") {	
			fn(this);
		}

	});
	
}

function alphabetwithspace(obj){
	if($(obj).val().match(regexp_alphabet)){
		$(obj).val( $(obj).val().replace(regexp_alphabet,'') );
	}
}

function alphabetwithspecialcharacters(obj){
	if($(obj).val().match(regexp_alphabetspecialcharacters)){
		$(obj).val( $(obj).val().replace(regexp_alphabetspecialcharacters,'') );
	}
}

function number(obj){
	if($(obj).val().match(regexp_number)){
		$(obj).val( $(obj).val().replace(regexp_number,'') );
	}
}

function alphanumericwithspace(obj){
	if($(obj).val().match(regexp_alphanumeric)){
		$(obj).val( $(obj).val().replace(regexp_alphanumeric,'') );
	}
}

function alphanumericwithspecialcharacters(obj){
	if($(obj).val().match(regexp_alphanumericspecialcharacters)){
		$(obj).val( $(obj).val().replace(regexp_alphanumericspecialcharacters,'') );
	}
}

function alphanumericwithspacehyphenunderscore(obj){
	if($(obj).val().match(regexp_alphanumerichyphenunderscore)){
		$(obj).val( $(obj).val().replace(regexp_alphanumerichyphenunderscore,'') );
	}
}

function decimalvalue(obj){
	if($(obj).val().match(regexp_decimalvalue)){
		$(obj).val( $(obj).val().replace(regexp_decimalvalue,'') );
	}
}

function alphanumerichyphenbackslash(obj){
	if($(obj).val().match(regexp_alphanumerichyphenbackslash)){
		$(obj).val( $(obj).val().replace(regexp_alphanumerichyphenbackslash,'') );
	}
}

function username(obj){
	if($(obj).val().match(regexp_username)){
		$(obj).val( $(obj).val().replace(regexp_username,'') );
	}
}

