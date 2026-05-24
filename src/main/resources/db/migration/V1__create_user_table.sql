CREATE TABLE user (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    login VARCHAR(100) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    salt VARCHAR(100) NOT NULL,
    is_active BIT NOT NULL,
    is_admin BIT NOT NULL,
    is_protected BIT NOT NULL,
    partner_id INTEGER NOT NULL REFERENCES partner(id)
);
