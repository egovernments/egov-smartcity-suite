update egw_status set code='RECONNDIGSIGNPENDING',description='ReConnection Digital Signature Pending' 
where code='RECONNDIGSIGNUPDATED' AND moduletype='WATERTAXAPPLICATION';

update egw_status set code='CLOSUREDIGSIGNPENDING',description='Closure Conn Digital Signature Pending' 
where code='CLOSUREDIGSIGNUPDATED'AND moduletype='WATERTAXAPPLICATION';

update egw_status set code='DIGITALSIGNATUREPENDING',description='Digital Signature Pending' 
where code='DIGITALSIGNATUREUPDATED'AND moduletype='WATERTAXAPPLICATION';