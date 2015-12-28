-----------------START--------------------
update eg_demand_reason_master set reasonmaster ='Water Connection Charges' where code='WTAXCONCHARGE';
update eg_demand_reason_master set reasonmaster ='Water Security Charges' where code='WTAXSECURITY';
update eg_demand_reason_master set reasonmaster ='Water Donation Charges' where code='WTAXDONATION';
update eg_demand_reason_master set reasonmaster ='Water Estimation Charges' where code='WTAXFIELDINSPEC';
update eg_demand_reason_master set reasonmaster ='Water Charges' where code='WTAXCHARGES';
------------------END---------------------
