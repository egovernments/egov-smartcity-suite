------------------START------------------
update egw_abstractestimate as ae set locality = (select le.location from egw_lineestimate as le, egw_lineestimate_details as led where led.lineestimate = le.id and led.id = ae.lineestimatedetails);
update egw_abstractestimate as ae set beneficiary = (select le.beneficiary from egw_lineestimate as le, egw_lineestimate_details as led where led.lineestimate = le.id and led.id = ae.lineestimatedetails);
update egw_abstractestimate as ae set modeofallotment = (select le.modeofallotment from egw_lineestimate as le, egw_lineestimate_details as led where led.lineestimate = le.id and led.id = ae.lineestimatedetails);
-------------------END-------------------