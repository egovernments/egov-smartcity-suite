update eg_wf_matrix set objecttype='RevisionPetition'  where objecttype='Objection';
update eg_wf_matrix set currentstate='Revision Petition:'||currentstate where objecttype='RevisionPetition';
update eg_wf_matrix set nextstate='Revision Petition:'||nextstate where objecttype='RevisionPetition' ;
update eg_wf_types set type='RevisionPetition' , typefqn='org.egov.ptis.domain.entity.objection.RevisionPetition' where type='Objection';
