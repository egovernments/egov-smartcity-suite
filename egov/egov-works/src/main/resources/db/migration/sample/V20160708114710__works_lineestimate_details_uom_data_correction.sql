--------- Update scripts to update uom data------------
update egw_lineestimate_details set uom = 'Each' where uom = 'each';
update egw_lineestimate_details set uom = 'Each' where uom = 'EACH';
update egw_lineestimate_details set uom = 'Cubic Metres' where uom = 'cum';
update egw_lineestimate_details set uom = 'Others' where uom = 'sum';