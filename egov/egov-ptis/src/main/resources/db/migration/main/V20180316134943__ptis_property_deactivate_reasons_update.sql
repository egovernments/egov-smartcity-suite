update EGPT_MUTATION_MASTER set mutation_name='DOUBLE ASSESSMENT', mutation_desc='Double Assessment', code='DOUBLE'
where mutation_name='DUPLICATE PROPERTY' and type='DEACTIVATE';
update EGPT_MUTATION_MASTER set mutation_name='UNTRACED PROPERTY', mutation_desc='Untraced Property', code='UNTRACED'
where mutation_name='BOGUS PROPERTY' and type='DEACTIVATE';
update EGPT_MUTATION_MASTER set mutation_name='CONVERTED TO NEW PROPERTY', mutation_desc='Converted To New Property', code='CONV_TO_NEW'
where mutation_name='CONVERTED TO HOUSE' and type='DEACTIVATE';
