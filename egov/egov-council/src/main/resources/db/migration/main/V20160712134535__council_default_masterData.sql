update eg_module set enabled=false where name='Council Management' and contextroot='council';

ALTER TABLE egcncl_members ALTER COLUMN filestoreid DROP NOT NULL;


 insert into egcncl_designation(id,name,isactive,createddate,createdby,lastmodifieddate,lastmodifiedby,version,code) values(
    nextval('seq_egcncl_designation'),'Ward Member',true,now(),1,now(),1,0,'WardMember');
insert into egcncl_designation(id,name,isactive,createddate,createdby,lastmodifieddate,lastmodifiedby,version,code) values(
    nextval('seq_egcncl_designation'),'MLC',true,now(),1,now(),1,0,'MLC');
insert into egcncl_designation(id,name,isactive,createddate,createdby,lastmodifieddate,lastmodifiedby,version,code) values(
nextval('seq_egcncl_designation'),'MLA',true,now(),1,now(),1,0,'MLA');
insert into egcncl_designation(id,name,isactive,createddate,createdby,lastmodifieddate,lastmodifiedby,version,code) values(
nextval('seq_egcncl_designation'),'MP',true,now(),1,now(),1,0,'MP');


  insert into egcncl_caste(id,name,isactive,createddate,createdby,lastmodifieddate,lastmodifiedby,version,code) values(
    nextval('seq_egcncl_caste'),'OC',true,now(),1,now(),1,0,'OC');
  insert into egcncl_caste(id,name,isactive,createddate,createdby,lastmodifieddate,lastmodifiedby,version,code) values(
    nextval('seq_egcncl_caste'),'BC',true,now(),1,now(),1,0,'BC');
     insert into egcncl_caste(id,name,isactive,createddate,createdby,lastmodifieddate,lastmodifiedby,version,code) values(
    nextval('seq_egcncl_caste'),'SC',true,now(),1,now(),1,0,'SC');
     insert into egcncl_caste(id,name,isactive,createddate,createdby,lastmodifieddate,lastmodifiedby,version,code) values(
    nextval('seq_egcncl_caste'),'ST',true,now(),1,now(),1,0,'ST');
     insert into egcncl_caste(id,name,isactive,createddate,createdby,lastmodifieddate,lastmodifiedby,version,code) values(
    nextval('seq_egcncl_caste'),'Minorities',true,now(),1,now(),1,0,'Minorities');

    
     insert into egcncl_qualification(id,name,isactive,createddate,createdby,lastmodifieddate,lastmodifiedby,version,code,description) values(
    nextval('seq_egcncl_qualification'),'Less Than SSC',true,now(),1,now(),1,0,'LSSC','Less Than SSC');
       insert into egcncl_qualification(id,name,isactive,createddate,createdby,lastmodifieddate,lastmodifiedby,version,code,description) values(
    nextval('seq_egcncl_qualification'),'SSC',true,now(),1,now(),1,0,'SSC','SSC');
        insert into egcncl_qualification(id,name,isactive,createddate,createdby,lastmodifieddate,lastmodifiedby,version,code,description) values(
    nextval('seq_egcncl_qualification'),'Intermediate',true,now(),1,now(),1,0,'Intermediate','Intermediate');
        insert into egcncl_qualification(id,name,isactive,createddate,createdby,lastmodifieddate,lastmodifiedby,version,code,description) values(
    nextval('seq_egcncl_qualification'),'Graduation',true,now(),1,now(),1,0,'Graduation','Graduation');
        insert into egcncl_qualification(id,name,isactive,createddate,createdby,lastmodifieddate,lastmodifiedby,version,code,description) values(
    nextval('seq_egcncl_qualification'),'Post-Graduation',true,now(),1,now(),1,0,'Post-Graduation','Post-Graduation');
        insert into egcncl_qualification(id,name,isactive,createddate,createdby,lastmodifieddate,lastmodifiedby,version,code,description) values(
    nextval('seq_egcncl_qualification'),'PHD',true,now(),1,now(),1,0,'PHD','PHD');

        insert into egcncl_party(id,name,isactive,createddate,createdby,lastmodifieddate,lastmodifiedby,version,code) values(
    nextval('seq_egcncl_party'),'BJP',true,now(),1,now(),1,0,'BJP');
    insert into egcncl_party(id,name,isactive,createddate,createdby,lastmodifieddate,lastmodifiedby,version,code) values(
    nextval('seq_egcncl_party'),'CPI',true,now(),1,now(),1,0,'CPI');
   insert into egcncl_party(id,name,isactive,createddate,createdby,lastmodifieddate,lastmodifiedby,version,code) values(
    nextval('seq_egcncl_party'),'Congress',true,now(),1,now(),1,0,'Congress');
 
    
update eg_action set displayname='View Council Member' where name='Search and View Result-CouncilMember';
update eg_action set displayname='Update Council Member' where name='Search and Edit-CouncilMember';
update eg_action set displayname='Create Council Member' where name='New-CouncilMember';

update eg_module set displayname='Political Party' where name='Council Party'; 
update eg_module set displayname='Educational Qualification' where name='Council Qualification';
update eg_module set displayname='Caste' where name='Council Caste';

