SELECT setval('seq_egf_budget',(select max(id)+1 from EGF_BUDGET));
