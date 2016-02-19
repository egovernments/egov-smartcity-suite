update egw_status set description='Collection Pending',code='COLLECTIONPENDING' where moduletype='TRADELICENSE' and code='COLLECTIONAMOUNTPAID';

update egw_status set description='Digital Signature Pending' ,code='DIGITALSIGNPENDING' where moduletype='TRADELICENSE' and code='DIGITALSIGNUPDATED';
