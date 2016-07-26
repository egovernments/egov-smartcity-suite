
delete from eg_roleaction where actionid =(select id from eg_action where name='Fiscal periods-SetUp');
delete from eg_action where name='Fiscal periods-SetUp';



drop table closedperiods;


CREATE TABLE closedperiods
(
id bigint NOT NULL,
startingdate timestamp without time zone,
endingdate timestamp without time zone,
isclosed boolean,
financialyearid bigint,
version numeric default 0,
PRIMARY KEY (id),
FOREIGN KEY (financialyearid) REFERENCES financialyear(id)
);



CREATE SEQUENCE seq_closedperiods
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;






