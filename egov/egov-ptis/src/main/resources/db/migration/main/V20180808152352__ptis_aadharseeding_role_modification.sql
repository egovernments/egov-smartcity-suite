
update eg_role set internal=false where name in ('Aadhar Initiator','Aadhar Approver');


update eg_action set displayname='Search Aadhar Seeding' where name='AadharSeedingSearch';
