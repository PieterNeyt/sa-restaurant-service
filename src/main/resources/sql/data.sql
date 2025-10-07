-- Restaurants
INSERT INTO restaurant (id, owner_id, type, name, email, logo, is_open, price_category)
VALUES
    ('a1b2c3d4-e5f6-4a7b-8c9d-000000000001', '11111111-1111-1111-1111-111111111111', 'PIZZERIA', 'La Dolce Vita', 'contact@ladolcevita.it', 'logo_dolce_vita.png', false, 'NORMAL'),
    ('a1b2c3d4-e5f6-4a7b-8c9d-000000000002', '22222222-2222-2222-2222-222222222222', 'SUSHI_BAR', 'Sakura Sushi', 'info@sakurasushi.jp', 'logo_sakura.png', false, 'EXPENSIVE');

-- Dishes for La Dolce Vita
INSERT INTO dish (id, restaurant_id, name, description, price, state)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'a1b2c3d4-e5f6-4a7b-8c9d-000000000001', 'Margherita', 'Classic cheese pizza', 8.50, 'PUBLISHED'),
    ('22222222-2222-2222-2222-222222222222', 'a1b2c3d4-e5f6-4a7b-8c9d-000000000001', 'Pepperoni', 'Spicy pepperoni pizza', 10.50, 'PUBLISHED'),
    ('33333333-3333-3333-3333-333333333333', 'a1b2c3d4-e5f6-4a7b-8c9d-000000000001', 'Quattro Formaggi', 'Four cheese pizza', 12.00, 'NOT_PUBLISHED'),
    ('44444444-4444-4444-4444-444444444444', 'a1b2c3d4-e5f6-4a7b-8c9d-000000000001', 'Veggie', 'Vegetable pizza', 9.50, 'TEMP_NOT_AVAILABLE');

-- Dishes for Sakura Sushi
INSERT INTO dish (id, restaurant_id, name, description, price, state)
VALUES
    ('55555555-5555-5555-5555-555555555555', 'a1b2c3d4-e5f6-4a7b-8c9d-000000000001', 'Hawaiian', 'Ham and pineapple pizza', 11.00, 'PUBLISHED'),
    ('66666666-6666-6666-6666-666666666666', 'a1b2c3d4-e5f6-4a7b-8c9d-000000000001', 'BBQ Chicken', 'Grilled chicken with BBQ sauce', 13.50, 'PUBLISHED'),
    ('77777777-7777-7777-7777-777777777777', 'a1b2c3d4-e5f6-4a7b-8c9d-000000000001', 'Four Seasons', 'Four different toppings', 14.00, 'NOT_PUBLISHED'),
    ('88888888-8888-8888-8888-888888888888', 'a1b2c3d4-e5f6-4a7b-8c9d-000000000001', 'Mushroom', 'Mushroom and cheese pizza', 10.00, 'TEMP_NOT_AVAILABLE');

