$(document).ready(function(){

	patternvalidation(); 
	
});

//Add class to input field "patternvalidation" and add custom data attribute(eg: data-pattern="alphabetwithspace") 

//This will allow you to enter only alphabets with or without space. (eg: data-pattern="alphabetwithspace")
var regexp_alphabet = /[^a-zA-Z ]/g ; 

//This will allow you to enter only alphabets with specified special characters like dot(.), slash(/), hash(#), ampersand(&), plus(+), minus(-). If you need some additional special characters, add those characters to the corresponding regular expression. 
//(eg: data-pattern="alphabetwithspecialcharacters")
var regexp_alphabetspecialcharacters = /[^a-zA-Z_@./#&+-]*$/ ;

//This will allow you to enter only numbers. (eg: data-pattern="number")
var regexp_number = /[^0-9]/g ;

//This will allow you to enter alphabets and numbers with or without space. (eg: data-pattern="alphanumericwithspace")
var regexp_alphanumeric = /[^a-zA-Z0-9 ]/g ;

//This will allow you to enter alphabets and numbers with specified special characters like dot(.), slash(/), hash(#), ampersand(&), plus(+), minus(-). If you need some additional special characters, add those characters to the corresponding regular expression. (eg: data-pattern="alphanumericwithspecialcharacters")
var regexp_alphanumericspecialcharacters = /[^a-zA-Z0-9_@./#&+-]*$/ ;

//This will allow you to enter alphabets and numbers with space, hyphen(-) and underscore(_). (eg: data-pattern="alphanumericwithspace-hyphen-underscore")
var regexp_alphanumerichyphenunderscore = /[^a-zA-Z0-9 _-]/g ;

//This will allow you to enter numbers and dot. (eg: data-pattern="decimalvalue")
var regexp_decimalvalue = /[^0-9.]/g ;

//This will allow you to enter alphabets and numbers with specified special characters like  slash(/), hyphen(-). (eg: data-pattern="alphanumerichyphenbackslash")
var regexp_alphanumerichyphenbackslash = /[^a-zA-Z0-9/-]*$/ ;

function patternvalidation(){
	
	$('.patternvalidation').on("input", function(){

		if($(this).data('pattern') === "alphabetwithspace"){ //alphabetwithspaces
			if($(this).val().match(regexp_alphabet)){
				$(this).val( $(this).val().replace(regexp_alphabet,'') );
			}
		}else if($(this).data('pattern') === "alphabetwithspecialcharacters"){ //alphabetwithspecialcharacters
			if($(this).val().match(regexp_alphabetspecialcharacters)){
				$(this).val( $(this).val().replace(regexp_alphabetspecialcharacters,'') );
			}
		}else if($(this).data('pattern') === "number"){ //number
			if($(this).val().match(regexp_number)){
				$(this).val( $(this).val().replace(regexp_number,'') );
			}
		}else if($(this).data('pattern') === "alphanumericwithspace"){ //alphanumericwithspaces
			if($(this).val().match(regexp_alphanumeric)){
				$(this).val( $(this).val().replace(regexp_alphanumeric,'') );
			}
		}else if($(this).data('pattern') === "alphanumericwithspecialcharacters"){ //alphanumericwithspecialcharacters
			if($(this).val().match(regexp_alphanumericspecialcharacters)){
				$(this).val( $(this).val().replace(regexp_alphanumericspecialcharacters,'') );
			}
		}else if($(this).data('pattern') === "alphanumericwithspace-hyphen-underscore"){ //alphanumericwith-hyphen-underscore
			if($(this).val().match(regexp_alphanumerichyphenunderscore)){
				$(this).val( $(this).val().replace(regexp_alphanumerichyphenunderscore,'') );
			}
		}else if($(this).data('pattern') === "decimalvalue"){ //decimal value
			if($(this).val().match(regexp_decimalvalue)){
				$(this).val( $(this).val().replace(regexp_decimalvalue,'') );
			}
		}else if($(this).data('pattern') === "alphanumerichyphenbackslash"){ //decimal value
			if($(this).val().match(regexp_alphanumerichyphenbackslash)){
				$(this).val( $(this).val().replace(regexp_alphanumerichyphenbackslash,'') );
			}
		}

	});
	
}
