CREATE TABLE weighing (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    number INTEGER UNIQUE NOT NULL,
    type BIT NOT NULL,
    local_date TEXT NOT NULL,
    local_time_entry VARCHAR(50) NOT NULL,
    local_time_departure VARCHAR(50) NOT NULL,
    gross FLOAT NOT NULL,
    tara FLOAT NOT NULL,
    nett FLOAT NOT NULL,
    description VARCHAR(200) NOT NULL,
    user_id INTEGER NOT NULL REFERENCES user(id),
    partner_id INTEGER NOT NULL REFERENCES partner(id),
    vehicle_id INTEGER NOT NULL REFERENCES vehicle(id),
    material_id INTEGER NOT NULL REFERENCES material(id)
);