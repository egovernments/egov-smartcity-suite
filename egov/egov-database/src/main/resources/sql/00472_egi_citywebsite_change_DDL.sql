ALTER TABLE eg_city_website RENAME TO eg_city;
CREATE SEQUENCE seq_eg_city;
ALTER TABLE eg_city RENAME cityName to name;
ALTER TABLE eg_city RENAME cityNameLocal to localName;
ALTER TABLE eg_city RENAME cityBaseURL to domainURL;
ALTER TABLE eg_city ADD COLUMN longitude double precision;
ALTER TABLE eg_city ADD COLUMN latitude double precision;