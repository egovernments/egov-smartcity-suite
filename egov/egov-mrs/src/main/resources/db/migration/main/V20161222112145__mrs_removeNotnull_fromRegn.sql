alter table egmrs_registration  alter column marriageact DROP  NOT NULL;

update eg_action  set enabled=false where contextroot='mrs' and name='RegistrationactwiseReport';
