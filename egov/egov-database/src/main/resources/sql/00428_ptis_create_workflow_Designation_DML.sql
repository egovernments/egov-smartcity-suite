--DESIGNATION
INSERT INTO eg_designation(id,name,description,chartofaccounts,version,createddate,lastmodifieddate,createdby,lastmodifiedby) VALUES
(nextval('seq_eg_designation'),'Bill Collector','Bill Collector',null,0,now(),now(),1,1);

--INSERT INTO eg_designation(id,name,description,chartofaccounts,version,createddate,lastmodifieddate,createdby,lastmodifiedby) VALUES
--(nextval('seq_eg_designation'),'Operator','Operator',null,0,now(),now(),1,1);
-- coment since operator is there as sample data 

--rollback delete from eg_designation where name='Bill Collector';
--rollback delete from eg_designation where name='Operator';
