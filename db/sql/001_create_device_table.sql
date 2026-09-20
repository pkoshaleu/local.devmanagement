CREATE TABLE IF NOT EXISTS device (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    device_name TEXT NOT NULL,
    device_brand TEXT NOT NULL,
    device_state TEXT  NOT NULL,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT now(),
    version BIGINT NOT NULL DEFAULT 0

    CONSTRAINT device_name_len CHECK (char_length(device_name) <= 255),
    CONSTRAINT device_brand_len CHECK (char_length(device_brand) <= 255),
    CONSTRAINT device_state_enum CHECK (device_state IN ('AVAILABLE', 'IN_USE', 'INACTIVE'))
);

CREATE INDEX IF NOT EXISTS idx_devices_brand ON device (device_brand);
CREATE INDEX IF NOT EXISTS idx_devices_state ON device (device_state);
