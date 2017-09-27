alter table eg_citypreferences 
add column googleApiKey varchar(50), 
add column recaptchaPK varchar(64),
add column recaptchaPub varchar(64);

update eg_citypreferences set recaptchaPK=citi.recaptchaPK, recaptchaPub = citi.recaptchaPub from eg_city citi where citi.preferences=eg_citypreferences.id;

alter table eg_city drop column recaptchaPK, drop column recaptchaPub;
