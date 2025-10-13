INSERT INTO restaurant (id, owner_id, addres_id, type, name, email, logo, is_open, price_category)
VALUES
    ('a1b2c3d4-e5f6-4a7b-8c9d-000000000001', '11111111-1111-1111-1111-111111111111','22222222-2222-2222-2222-222222222223', 'PIZZERIA', 'La Dolce Vita', 'contact@ladolcevita.it', 'logo_dolce_vita.png', false, 'NORMAL'),
    ('a1b2c3d4-e5f6-4a7b-8c9d-000000000002', '22222222-2222-2222-2222-222222222222','22222222-2222-2222-2222-222222222224', 'SUSHI_BAR', 'Sakura Sushi', 'info@sakurasushi.jp', 'logo_sakura.png', true, 'EXPENSIVE');

-- Opening hours for La Dolce Vita (zonder id kolom)
INSERT INTO restaurant_opening_hours (restaurant_id, day_of_week, opening_time, closing_time)
VALUES
    ('a1b2c3d4-e5f6-4a7b-8c9d-000000000001', 'MONDAY', '11:00:00', '22:00:00'),
    ('a1b2c3d4-e5f6-4a7b-8c9d-000000000001', 'TUESDAY', '11:00:00', '22:00:00'),
    ('a1b2c3d4-e5f6-4a7b-8c9d-000000000001', 'WEDNESDAY', '11:00:00', '22:00:00'),
    ('a1b2c3d4-e5f6-4a7b-8c9d-000000000001', 'THURSDAY', '11:00:00', '22:00:00'),
    ('a1b2c3d4-e5f6-4a7b-8c9d-000000000001', 'FRIDAY', '11:00:00', '23:00:00'),
    ('a1b2c3d4-e5f6-4a7b-8c9d-000000000001', 'SATURDAY', '11:00:00', '23:00:00'),
    ('a1b2c3d4-e5f6-4a7b-8c9d-000000000001', 'SUNDAY', '12:00:00', '21:00:00');

-- Opening hours for Sakura Sushi (zonder id kolom)
INSERT INTO restaurant_opening_hours (restaurant_id, day_of_week, opening_time, closing_time)
VALUES
    ('a1b2c3d4-e5f6-4a7b-8c9d-000000000002', 'MONDAY', '17:00:00', '23:00:00'),
    ('a1b2c3d4-e5f6-4a7b-8c9d-000000000002', 'TUESDAY', '17:00:00', '23:00:00'),
    ('a1b2c3d4-e5f6-4a7b-8c9d-000000000002', 'WEDNESDAY', '17:00:00', '23:00:00'),
    ('a1b2c3d4-e5f6-4a7b-8c9d-000000000002', 'THURSDAY', '17:00:00', '23:00:00'),
    ('a1b2c3d4-e5f6-4a7b-8c9d-000000000002', 'FRIDAY', '17:00:00', '22:00:00'),
    ('a1b2c3d4-e5f6-4a7b-8c9d-000000000002', 'SATURDAY', '17:00:00', '22:00:00'),
    ('a1b2c3d4-e5f6-4a7b-8c9d-000000000002', 'SUNDAY', '17:00:00', '22:00:00');

-- Dishes for La Dolce Vita
INSERT INTO dish (id, restaurant_id, name, description, price, state,preparation_time)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'a1b2c3d4-e5f6-4a7b-8c9d-000000000001', 'Margherita', 'Classic cheese pizza', 8.50, 'PUBLISHED',30),
    ('22222222-2222-2222-2222-222222222222', 'a1b2c3d4-e5f6-4a7b-8c9d-000000000001', 'Pepperoni', 'Spicy pepperoni pizza', 10.50, 'PUBLISHED',19),
    ('33333333-3333-3333-3333-333333333333', 'a1b2c3d4-e5f6-4a7b-8c9d-000000000001', 'Quattro Formaggi', 'Four cheese pizza', 12.00, 'NOT_PUBLISHED',17),
    ('44444444-4444-4444-4444-444444444444', 'a1b2c3d4-e5f6-4a7b-8c9d-000000000001', 'Veggie', 'Vegetable pizza', 9.50, 'TEMP_NOT_AVAILABLE',10);

-- Dishes for Sakura Sushi
INSERT INTO dish (id, restaurant_id, name, description, price, state,preparation_time)
VALUES
    ('55555555-5555-5555-5555-555555555555', 'a1b2c3d4-e5f6-4a7b-8c9d-000000000002', 'California Roll', 'Crab, avocado, cucumber', 12.00, 'PUBLISHED',15),
    ('66666666-6666-6666-6666-666666666666', 'a1b2c3d4-e5f6-4a7b-8c9d-000000000002', 'Salmon Nigiri', 'Fresh salmon on rice', 8.50, 'PUBLISHED',5),
    ('77777777-7777-7777-7777-777777777777', 'a1b2c3d4-e5f6-4a7b-8c9d-000000000002', 'Dragon Roll', 'Eel and avocado', 18.00, 'NOT_PUBLISHED',25),
    ('88888888-8888-8888-8888-888888888888', 'a1b2c3d4-e5f6-4a7b-8c9d-000000000002', 'Tuna Sashimi', 'Fresh tuna slices', 22.00, 'TEMP_NOT_AVAILABLE',40);