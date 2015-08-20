update eg_demand_reason_master set reasonmaster ='Water tax charges' where code='WTAXCHARGES';
update eg_demand_reason_master set reasonmaster ='Water tax estimation charges' where code='WTAXFIELDINSPEC';
update eg_demand_reason_master set reasonmaster ='Water tax donation charges' where code='WTAXDONATION';

--rollback update eg_demand_reason_master set reasonmaster ='WT_CHARGES' where code='WTAXCHARGES';
--rollback update eg_demand_reason_master set reasonmaster ='WT_ESTIMATE_CHARGES' where code='WTAXFIELDINSPEC';
--rollback update eg_demand_reason_master set reasonmaster ='WT_DONATION_CHARGES' where code='WTAXDONATION';
