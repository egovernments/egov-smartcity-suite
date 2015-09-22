
alter table EGADTAX_RATES_DETAILS drop column class;
alter table egadtax_rates add column class bigint not null;
alter table egadtax_rates add CONSTRAINT fk_adtax_rates_class FOREIGN KEY (class) REFERENCES egadtax_rates_Class (id);
alter table egadtax_rates drop CONSTRAINT unq_adtax_rates_cat_subcat_uom;
alter table egadtax_rates add CONSTRAINT unq_adtax_rates_cat_subcat_uom_class UNIQUE (category,subcategory,unitofMeasure,class);
