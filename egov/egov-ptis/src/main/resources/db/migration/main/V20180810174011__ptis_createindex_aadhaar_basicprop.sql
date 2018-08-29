create index idx_property_exempted on egpt_property(isexemptedfromtax);
create index idx_aadhar_basicprop on egpt_aadharseeding(basicproperty);
create index idx_aadhar_status on egpt_aadharseeding(status);
create index idx_seeding_aadharno on egpt_aadharseeding_details(aadharno);


