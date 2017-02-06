------------------START------------------
update egw_abstractestimate as ae set spilloverflag = (select le.abstractestimatecreated from egw_lineestimate as le, egw_lineestimate_details as led where led.lineestimate = le.id and led.id = ae.lineestimatedetails) where ae.lineestimatedetails is not null;
-------------------END-------------------