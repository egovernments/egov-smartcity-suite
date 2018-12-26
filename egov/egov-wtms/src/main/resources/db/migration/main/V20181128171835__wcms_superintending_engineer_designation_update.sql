

update eg_wf_matrix 
set nextdesignation = REPLACE(nextdesignation, 'Superintendent Engineer', 'Superintending Engineer')
where objecttype='WaterConnectionDetails' and nextdesignation like '%Superintendent Engineer%';

update eg_wf_matrix 
set currentdesignation = REPLACE(currentdesignation, 'Superintendent Engineer', 'Superintending Engineer')
where objecttype='WaterConnectionDetails' and currentdesignation like '%Superintendent Engineer%';

update eg_wf_matrix 
set currentdesignation = REPLACE(currentdesignation, 'Superintendent engineer', 'Superintending Engineer')
where objecttype='WaterConnectionDetails' and currentdesignation like '%Superintendent engineer%';

update eg_wf_matrix set nextstate='Application Approval Pending', validactions='Approve,Forward,Preview,Sign'
where currentdesignation='Superintending Engineer' and currentstate='Application Approval Pending' and objecttype='WaterConnectionDetails';



