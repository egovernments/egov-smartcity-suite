CREATE TABLE egpt_tax_exemption_docs
(
  id_property bigint,
  document bigint
);

alter table egpt_property_detail rename column extra_field1 to exemption_details;
