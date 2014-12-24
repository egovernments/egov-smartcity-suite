#UP
update egpay_payscale_header set pay_fixed_id=(select pay_fixed_in_id from egeis_pay_fixed_in_mstr where pay_fixed_in_value like'fifthPay');
#DOWN