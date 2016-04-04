ALTER TABLE ONLY egadtax_rates
    drop CONSTRAINT unq_adtax_rates_cat_subcat_uom_class;

ALTER TABLE ONLY egadtax_rates
    ADD CONSTRAINT unq_adtax_rates_cat_subcat_uom_class_fy UNIQUE (category, subcategory, unitofmeasure, class,financialyear);    