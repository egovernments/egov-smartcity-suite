-----------------Updated Role action mappings to search Estimate Templates----------------
update eg_action set url = '/estimatetemplate/searchestimatetemplateform' where name = 'SearchEstimateTemplateForm';

--rollback update eg_action set url = '/abstractestimate/searchestimatetemplateform' where name = 'SearchEstimateTemplateForm';