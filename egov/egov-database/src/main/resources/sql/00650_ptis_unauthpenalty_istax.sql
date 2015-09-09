update eg_demand_reason_master set category = (select id from eg_reason_category where code='TAX') where code='UNAUTH_PENALTY';

--rollback update eg_demand_reason_master set category = (select id from eg_reason_category where code='FINES') where code='UNAUTH_PENALTY';
