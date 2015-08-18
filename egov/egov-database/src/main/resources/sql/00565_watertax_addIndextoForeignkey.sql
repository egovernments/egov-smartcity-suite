update eg_action set url='/application/meterentry/' where name='meter Entry screen' and contextroot='wtms';

CREATE INDEX IDX_METERENTRYDETAILS_CONNECTIONDETAILSID ON egwtr_meter_connection_details USING BTREE (connectiondetailsid);