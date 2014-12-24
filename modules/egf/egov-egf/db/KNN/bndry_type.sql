Insert into eg_boundary_type
   (ID_BNDRY_TYPE, HIERARCHY, NAME, UPDATEDTIME, ID_HEIRARCHY_TYPE)
 Values
   (4, 2, 'Zone', TO_DATE('09/06/2005 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 2);
   
   
   Insert into eg_boundary_type
      (ID_BNDRY_TYPE, HIERARCHY, NAME, UPDATEDTIME, ID_HEIRARCHY_TYPE)
    Values
      (5, 3, 'Ward', TO_DATE('09/06/2005 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 3);


COMMIT;


