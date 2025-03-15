INSERT INTO users (user_id, password, name, id_type, id_value)
VALUES
    ('test@example.com', 'encodedpassword1', 'test', 'REG_NO', '123456-7890123'),
    ('business@example.com', 'encodedpassword2', 'company', 'BUSINESS_NO', '987654-3210987');

INSERT INTO quotes (quote_id, user_id, send_amount, target_currency, exchange_rate, fee, target_amount, usd_exchange_rate, target_usd_amount, expires_date, used)
VALUES
    ('quote-1', 'test@example.com', 500000, 'USD', 1300.50, 3000, 380.35, 1300.00, 380.35, TIMESTAMPADD(MINUTE, 10, NOW()), TRUE),
    ('quote-2', 'test@example.com', 250000, 'JPY', 9.05, 2000, 27500, 1300.00, 192.30, TIMESTAMPADD(MINUTE, 10, NOW()), TRUE),
    ('quote-3', 'business@example.com', 1000000, 'USD', 1295.00, 5000, 771.35, 1295.00, 771.35, TIMESTAMPADD(MINUTE, 10, NOW()), TRUE);

INSERT INTO transfers (transfer_id, user_id, quote_id, requested_date)
VALUES
    ('transfer-1', 'test@example.com', 'quote-1', TIMESTAMPADD(DAY, -1, NOW())),
    ('transfer-2', 'test@example.com', 'quote-2', TIMESTAMPADD(HOUR, -5, NOW())),
    ('transfer-3', 'business@example.com', 'quote-3', TIMESTAMPADD(MINUTE, -30, NOW()));

INSERT INTO histories (history_id, user_id, source_amount, fee, usd_exchange_rate, usd_amount, target_currency, exchange_rate, target_amount, requested_date)
VALUES
    ('history-1', 'test@example.com', 500000, 3000, 1300.00, 380.35, 'USD', 1300.50, 380.35, TIMESTAMPADD(DAY, -1, NOW())),
    ('history-2', 'test@example.com', 250000, 2000, 1300.00, 192.30, 'JPY', 9.05, 27500, TIMESTAMPADD(HOUR, -5, NOW())),
    ('history-3', 'business@example.com', 1000000, 5000, 1295.00, 771.35, 'USD', 1295.00, 771.35, TIMESTAMPADD(MINUTE, -30, NOW()));
