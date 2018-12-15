UPDATE egp_portalservice SET url='/pgr/grievance/register/by-citizen',
helpdoclink='/pgr/grievance/register/by-citizen' WHERE code='Register Grievance';

UPDATE egp_portalservice SET url='/pgr/grievance/search',
helpdoclink='/pgr/grievance/search' WHERE code='View Grievance';

UPDATE egp_inbox SET link=replace(link,'complaint','grievance') WHERE servicetype='Complaint';