INSERT INTO users (user_id, password, name, id_type, id_value)
VALUES
    ('test@example.com', 'hashedpassword1', '홍길동', 'REG_NO', '123456-7890123'),
    ('business@example.com', 'hashedpassword2', '모인주식회사', 'BUSINESS_NO', '987654-3210987');

INSERT INTO quotes (quote_id, user_id, send_amount, target_currency, exchange_rate, fee, target_amount, usd_exchange_rate, target_usd_amount, expires_date, used)
VALUES
    ('test-quote-id', 'test@example.com', 100000, 'USD', 1100, 2000, 90909, 1300, 76.78, TIMESTAMPADD(MINUTE, 10, NOW()), FALSE),
    ('used-quote-id', 'test@example.com', 100000, 'JPY', 9.0565, 2000, 11039, 145.23, 76.01, TIMESTAMPADD(MINUTE, 10, NOW()), TRUE),
    ('expired-quote-id', 'test@example.com', 100000, 'USD', 1100, 2000, 90909, 1300, 76.78, TIMESTAMPADD(MINUTE, -20, NOW()), FALSE),
    ('individual-quote-id', 'test@example.com', 100000, 'JPY', 9.0565, 2000, 11039, 145.23, 1000, TIMESTAMPADD(MINUTE, 10, NOW()), TRUE),
    ('business-quote-id', 'test@example.com', 100000, 'JPY', 9.0565, 2000, 11039, 145.23, 5000, TIMESTAMPADD(MINUTE, 10, NOW()), TRUE);