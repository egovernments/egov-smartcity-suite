ALTER TABLE eg_user RENAME COLUMN id_user TO id;
ALTER TABLE eg_user RENAME COLUMN user_name TO username;
ALTER TABLE eg_user RENAME COLUMN pwd TO password;
ALTER TABLE eg_user RENAME COLUMN pwd_updated_date TO pwdexpirydate;
ALTER TABLE eg_user RENAME COLUMN email TO emailid;
ALTER TABLE eg_user RENAME COLUMN alternatenumber TO altContactNumber;

ALTER TABLE eg_user DROP COLUMN id_department;
ALTER TABLE eg_user DROP COLUMN pwd_reminder;
ALTER TABLE eg_user DROP COLUMN updatetime;
ALTER TABLE eg_user DROP COLUMN updateuserid;
ALTER TABLE eg_user DROP COLUMN extrafield1;
ALTER TABLE eg_user DROP COLUMN extrafield2;
ALTER TABLE eg_user DROP COLUMN extrafield3;
ALTER TABLE eg_user DROP COLUMN extrafield4;
ALTER TABLE eg_user DROP COLUMN is_suspended;
ALTER TABLE eg_user DROP COLUMN id_top_bndry;
ALTER TABLE eg_user DROP COLUMN isactive CASCADE;
ALTER TABLE eg_user DROP COLUMN reportsto;
ALTER TABLE eg_user DROP COLUMN fromdate;
ALTER TABLE eg_user DROP COLUMN todate;
ALTER TABLE eg_user DROP COLUMN user_sign;
ALTER TABLE eg_user DROP COLUMN organizationid;
ALTER TABLE eg_user DROP COLUMN isportaluser;
ALTER TABLE eg_user DROP COLUMN addressid;
ALTER TABLE eg_user DROP COLUMN first_name;
ALTER TABLE eg_user DROP COLUMN middle_name;
ALTER TABLE eg_user DROP COLUMN last_name;

ALTER TABLE eg_user ADD COLUMN createddate timestamp without time zone;
ALTER TABLE eg_user ADD COLUMN lastmodifieddate timestamp without time zone;
ALTER TABLE eg_user ADD COLUMN createdby BIGINT;
ALTER TABLE eg_user ADD COLUMN lastmodifiedby BIGINT;
ALTER TABLE eg_user ADD COLUMN isactive BOOLEAN;
ALTER TABLE eg_user ADD COLUMN name VARCHAR(100);
ALTER TABLE eg_user ADD COLUMN gender SMALLINT;
ALTER TABLE eg_user ADD COLUMN pan VARCHAR(10);
ALTER TABLE eg_user ADD COLUMN aadhaarnumber VARCHAR(20);
ALTER TABLE eg_user ADD COLUMN "type" VARCHAR(20);

ALTER TABLE eg_address RENAME COLUMN addressid TO id;
ALTER TABLE eg_address RENAME COLUMN  houseno TO houseNoBldgApt;

ALTER TABLE eg_address DROP COLUMN streetaddress1 CASCADE;
ALTER TABLE eg_address DROP COLUMN streetaddress2;
ALTER TABLE eg_address DROP COLUMN streetaddress1local;
ALTER TABLE eg_address DROP COLUMN streeraddress2local;
ALTER TABLE eg_address DROP COLUMN block;
ALTER TABLE eg_address DROP COLUMN blocklocal;
ALTER TABLE eg_address DROP COLUMN citytownvillage;
ALTER TABLE eg_address DROP COLUMN citytownvillagelocal;
ALTER TABLE eg_address DROP COLUMN district;
ALTER TABLE eg_address DROP COLUMN districtlocal;
ALTER TABLE eg_address DROP COLUMN state;
ALTER TABLE eg_address DROP COLUMN statelocal;
ALTER TABLE eg_address DROP COLUMN pincode;
ALTER TABLE eg_address DROP COLUMN locality;
ALTER TABLE eg_address DROP COLUMN localitylocal;
ALTER TABLE eg_address DROP COLUMN lastupdatedtimestamp;
ALTER TABLE eg_address DROP COLUMN id_addresstypemaster;
ALTER TABLE eg_address DROP COLUMN talukname;
ALTER TABLE eg_address DROP COLUMN taluklocal;

ALTER TABLE eg_address ADD COLUMN subdistrict VARCHAR(100);
ALTER TABLE eg_address ADD COLUMN identityby VARCHAR(100);
ALTER TABLE eg_address ADD COLUMN identitytype VARCHAR(3);
ALTER TABLE eg_address ADD COLUMN postOffice VARCHAR(100);
ALTER TABLE eg_address ADD COLUMN landmark VARCHAR(256);
ALTER TABLE eg_address ADD COLUMN country VARCHAR(50);
ALTER TABLE eg_address ADD COLUMN "user" BIGINT;
ALTER TABLE eg_address ADD COLUMN "type" SMALLINT;
ALTER TABLE eg_address ADD COLUMN streetRoadLine VARCHAR(256);
ALTER TABLE eg_address ADD COLUMN citytownvillage VARCHAR(256);
ALTER TABLE eg_address ADD COLUMN areaLocalitySector VARCHAR(256);
ALTER TABLE eg_address ADD COLUMN district VARCHAR(100);
ALTER TABLE eg_address ADD COLUMN state VARCHAR(100);
ALTER TABLE eg_address ADD COLUMN pincode VARCHAR(10);


ALTER TABLE eg_userrole RENAME COLUMN id_role TO role;
ALTER TABLE eg_userrole RENAME COLUMN id_user TO "user";

ALTER TABLE eg_userrole DROP COLUMN id;
ALTER TABLE eg_userrole DROP COLUMN fromdate;
ALTER TABLE eg_userrole DROP COLUMN todate;
ALTER TABLE eg_userrole DROP COLUMN is_history;




