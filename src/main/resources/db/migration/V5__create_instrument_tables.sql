-- Instrument Categories Table
CREATE TABLE IF NOT EXISTS instrument_categories (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- Instruments Table
CREATE TABLE IF NOT EXISTS instruments (
    id VARCHAR(255) PRIMARY KEY,
    slug VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    short_description VARCHAR(500) NOT NULL,
    description TEXT NOT NULL,
    category_id VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    image_url TEXT NOT NULL,
    manufacturer VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    serial_number VARCHAR(255),
    location VARCHAR(500) NOT NULL,
    responsible_admin_id VARCHAR(255) NOT NULL,
    responsible_admin_name VARCHAR(255) NOT NULL,
    responsible_admin_email VARCHAR(255) NOT NULL,
    opening_hour INTEGER NOT NULL,
    closing_hour INTEGER NOT NULL,
    slot_interval_minutes INTEGER NOT NULL,
    min_duration_minutes INTEGER NOT NULL,
    max_duration_minutes INTEGER NOT NULL,
    advance_booking_days INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Instrument Features (Collection Table)
CREATE TABLE IF NOT EXISTS instrument_features (
    instrument_id VARCHAR(255) NOT NULL,
    feature VARCHAR(500) NOT NULL,
    FOREIGN KEY (instrument_id) REFERENCES instruments(id) ON DELETE CASCADE
);

-- Instrument Usage Requirements (Collection Table)
CREATE TABLE IF NOT EXISTS instrument_usage_requirements (
    instrument_id VARCHAR(255) NOT NULL,
    requirement VARCHAR(500) NOT NULL,
    FOREIGN KEY (instrument_id) REFERENCES instruments(id) ON DELETE CASCADE
);

-- Instrument Specifications Table
CREATE TABLE IF NOT EXISTS instrument_specifications (
    id VARCHAR(255) PRIMARY KEY,
    instrument_id VARCHAR(255) NOT NULL,
    label VARCHAR(255) NOT NULL,
    value VARCHAR(500) NOT NULL,
    sort_order INTEGER NOT NULL,
    FOREIGN KEY (instrument_id) REFERENCES instruments(id) ON DELETE CASCADE
);

-- Instrument Bookings Table
CREATE TABLE IF NOT EXISTS instrument_bookings (
    id VARCHAR(255) PRIMARY KEY,
    instrument_id VARCHAR(255) NOT NULL,
    requester_id VARCHAR(255),
    requester_name VARCHAR(255) NOT NULL,
    requester_email VARCHAR(255) NOT NULL,
    requester_phone VARCHAR(50),
    affiliation VARCHAR(500) NOT NULL,
    supervisor_name VARCHAR(255),
    purpose_title VARCHAR(500) NOT NULL,
    purpose_description TEXT NOT NULL,
    sample_details TEXT,
    start_at TIMESTAMP NOT NULL,
    end_at TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    admin_note TEXT,
    reviewed_by VARCHAR(255),
    reviewed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (instrument_id) REFERENCES instruments(id) ON DELETE CASCADE
);

-- Instrument Reviews Table
CREATE TABLE IF NOT EXISTS instrument_reviews (
    id VARCHAR(255) PRIMARY KEY,
    instrument_id VARCHAR(255) NOT NULL,
    booking_id VARCHAR(255),
    reviewer_name VARCHAR(255) NOT NULL,
    reviewer_affiliation VARCHAR(255),
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (instrument_id) REFERENCES instruments(id) ON DELETE CASCADE
);

-- Instrument Unavailable Periods Table
CREATE TABLE IF NOT EXISTS instrument_unavailable_periods (
    id VARCHAR(255) PRIMARY KEY,
    instrument_id VARCHAR(255) NOT NULL,
    start_at TIMESTAMP NOT NULL,
    end_at TIMESTAMP NOT NULL,
    reason VARCHAR(500) NOT NULL,
    type VARCHAR(50) NOT NULL,
    FOREIGN KEY (instrument_id) REFERENCES instruments(id) ON DELETE CASCADE
);

-- Indexes for better performance
CREATE INDEX IF NOT EXISTS idx_instruments_category_id ON instruments(category_id);
CREATE INDEX IF NOT EXISTS idx_instruments_status ON instruments(status);
CREATE INDEX IF NOT EXISTS idx_instrument_specifications_instrument_id ON instrument_specifications(instrument_id);
CREATE INDEX IF NOT EXISTS idx_instrument_bookings_instrument_id ON instrument_bookings(instrument_id);
CREATE INDEX IF NOT EXISTS idx_instrument_bookings_status ON instrument_bookings(status);
CREATE INDEX IF NOT EXISTS idx_instrument_bookings_start_at ON instrument_bookings(start_at);
CREATE INDEX IF NOT EXISTS idx_instrument_bookings_end_at ON instrument_bookings(end_at);
CREATE INDEX IF NOT EXISTS idx_instrument_reviews_instrument_id ON instrument_reviews(instrument_id);
CREATE INDEX IF NOT EXISTS idx_instrument_unavailable_periods_instrument_id ON instrument_unavailable_periods(instrument_id);

-- Insert default categories
INSERT INTO instrument_categories (id, name) VALUES
    ('cat-spectroscopy', 'Spectroscopy'),
    ('cat-microscopy', 'Microscopy'),
    ('cat-separation', 'Separation & Chromatography'),
    ('cat-sample-prep', 'Sample Preparation'),
    ('cat-molecular', 'Molecular Biology')
ON CONFLICT (name) DO NOTHING;
