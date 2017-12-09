ALTER TABLE eg_loginaudit DROP COLUMN version;
ALTER TABLE eg_loginaudit ALTER COLUMN useragentinfo TYPE VARCHAR(500) ;
ALTER TABLE eg_loginaudit RENAME COLUMN useragentinfo TO useragent;
