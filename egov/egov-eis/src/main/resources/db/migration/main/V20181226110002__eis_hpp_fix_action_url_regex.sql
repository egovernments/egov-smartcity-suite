UPDATE eg_action SET url='/position/by-dept-and-desig', queryparamregex='^department=[1-9]\d*&designation=[1-9]\d*$' WHERE name='EIS_POSITIONS_BY_DEPT_DESIG';
UPDATE eg_action SET queryparamregex='^name=([a-zA-Z0-9]+([ _-])?[a-zA-Z0-9])+$' WHERE name='EIS_POSITIONS_BY_CODE_NAME_POSITION_NAME';
UPDATE eg_action SET queryparamregex='^designationName=([a-zA-Z0-9]+([ _-])?[a-zA-Z0-9])+$' WHERE name='EIS_DESIGNATION_BY_NAME';
UPDATE eg_action SET queryparamregex='^department=[1-9]\d*$' WHERE name='EIS_DESIGNATION_BY_DEPARTMENT';
