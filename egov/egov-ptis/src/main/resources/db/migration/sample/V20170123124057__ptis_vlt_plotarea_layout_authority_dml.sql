-- Vacant land plot area inserts
INSERT INTO  egpt_vlt_plot_area(id, name, code, createddate, createdby, lastmodifiedby, lastmodifieddate) VALUES(nextval('seq_egpt_vlt_plot_area'), 'Old Area', 'OLD', now(), 1, 1, now());
INSERT INTO  egpt_vlt_plot_area(id, name, code, createddate, createdby, lastmodifiedby, lastmodifieddate) VALUES(nextval('seq_egpt_vlt_plot_area'), 'Existing Built-Up Area', 'EXBUILTUP', now(), 1, 1, now());
INSERT INTO  egpt_vlt_plot_area(id, name, code, createddate, createdby, lastmodifiedby, lastmodifieddate) VALUES(nextval('seq_egpt_vlt_plot_area'), 'Congested Area', 'CONGESTED', now(), 1, 1, now());
INSERT INTO  egpt_vlt_plot_area(id, name, code, createddate, createdby, lastmodifiedby, lastmodifieddate) VALUES(nextval('seq_egpt_vlt_plot_area'), 'Settlement Area', 'SETTLEMENT', now(), 1, 1, now());
INSERT INTO  egpt_vlt_plot_area(id, name, code, createddate, createdby, lastmodifiedby, lastmodifieddate) VALUES(nextval('seq_egpt_vlt_plot_area'), 'Newly Developed Area', 'NEWDEVELOP', now(), 1, 1, now());

-- Layout approval authority inserts
INSERT INTO  egpt_layout_approval_authority(id, name, code, createddate, createdby, lastmodifiedby, lastmodifieddate) VALUES(nextval('seq_egpt_layout_approval_authority'), 'DTCP', 'DTCP', now(), 1, 1, now());
INSERT INTO  egpt_layout_approval_authority(id, name, code, createddate, createdby, lastmodifiedby, lastmodifieddate) VALUES(nextval('seq_egpt_layout_approval_authority'), 'RDDTP', 'RDDTP', now(), 1, 1, now());
INSERT INTO  egpt_layout_approval_authority(id, name, code, createddate, createdby, lastmodifiedby, lastmodifieddate) VALUES(nextval('seq_egpt_layout_approval_authority'), 'UDA', 'UDA', now(), 1, 1, now());
INSERT INTO  egpt_layout_approval_authority(id, name, code, createddate, createdby, lastmodifiedby, lastmodifieddate) VALUES(nextval('seq_egpt_layout_approval_authority'), 'CRDA', 'CRDA', now(), 1, 1, now());
INSERT INTO  egpt_layout_approval_authority(id, name, code, createddate, createdby, lastmodifiedby, lastmodifieddate) VALUES(nextval('seq_egpt_layout_approval_authority'), 'Erstwhile Gram Panchayat', 'GRAMPANCHAYAT', now(), 1, 1, now());
INSERT INTO  egpt_layout_approval_authority(id, name, code, createddate, createdby, lastmodifiedby, lastmodifieddate) VALUES(nextval('seq_egpt_layout_approval_authority'), 'No Approval', 'NOAPPROVAL', now(), 1, 1, now());
