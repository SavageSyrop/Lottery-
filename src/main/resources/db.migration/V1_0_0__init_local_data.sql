-- Таблица типов лотерей
CREATE TABLE lottery_type (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE,
    description TEXT,
    is_autogenerated BOOLEAN,
    price NUMERIC(10,2) NOT NULL,
    winning_amount NUMERIC(10,2) NOT NULL
);

-- Таблица пользователей
CREATE TABLE "users" (
    id BIGSERIAL PRIMARY KEY,
    email TEXT NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    balance NUMERIC(10,2),
    "role" TEXT NOT NULL
);

-- Таблица тиражей
CREATE TABLE draw (
    id BIGSERIAL PRIMARY KEY,
    lottery_type_id BIGINT NOT NULL REFERENCES lottery_type(id),
    start_time TIMESTAMPTZ NOT NULL,
    status TEXT NOT NULL DEFAULT 'PLANNED' CHECK (status IN ('PLANNED', 'ACTIVE', 'COMPLETED', 'CANCELLED'))
);

-- Результаты тиражей
CREATE TABLE draw_result (
    id BIGSERIAL PRIMARY KEY,
    draw_id BIGINT NOT NULL REFERENCES draw(id) ON DELETE CASCADE,
    winning_combination TEXT NOT NULL,
    result_time TIMESTAMPTZ NOT NULL
);

-- Таблица билетов
CREATE TABLE ticket (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES "users"(id) ON DELETE CASCADE,
    draw_id BIGINT NOT NULL REFERENCES draw(id) ON DELETE CASCADE,
    picked_numbers TEXT NOT NULL,
    status TEXT NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'WIN', 'LOSE'))
);

-- Таблица инвойсов
CREATE TABLE invoice (
    id BIGSERIAL PRIMARY KEY,
    draw_id BIGINT NOT NULL REFERENCES draw(id) ON DELETE CASCADE,
    picked_numbers TEXT NOT NULL,
    register_time TIMESTAMPTZ NOT NULL,
    status TEXT NOT NULL CHECK (status IN ('REGISTERED', 'CANCELLED'))
);

-- Таблица платежей
CREATE TABLE payment (
    id BIGSERIAL PRIMARY KEY,
    invoice_id BIGINT NOT NULL REFERENCES invoice(id) ON DELETE CASCADE,
    amount NUMERIC(10,2) NOT NULL,
    status TEXT NOT NULL CHECK (status IN ('SUCCESS', 'FAILED')),
    payment_time TIMESTAMPTZ NOT NULL
);

-- Индексы по draw_id
CREATE INDEX idx_ticket_draw_id ON ticket(draw_id);
CREATE INDEX idx_draw_result_draw_id ON draw_result(draw_id);

-- Индексы по user_id
CREATE INDEX idx_ticket_user_id ON ticket(user_id);

-- Индексы по status
CREATE INDEX idx_draw_status ON draw(status);
CREATE INDEX idx_ticket_status ON ticket(status);
CREATE INDEX idx_invoice_status ON invoice(status);
CREATE INDEX idx_payment_status ON payment(status);

INSERT INTO "users" (email, password_hash, balance, "role") values
('vrcard@mail.ru', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 0.0, 'ADMIN');

INSERT INTO lottery_type (id, name, description, is_autogenerated, price, winning_amount) VALUES
(1, 'FIVE_OUT_OF_36', '5 numbers from 36', FALSE, 100.00, 1000000.00),
(2, 'SIX_OUT_OF_45', '6 numbers from 45', FALSE, 200.00, 2000000.00);


