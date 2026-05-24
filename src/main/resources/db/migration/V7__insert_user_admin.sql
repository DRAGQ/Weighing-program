INSERT INTO partner (id, name, type, street, township, postcode, business_id, tax_id, vat)
VALUES (null, "Administrator", "USER", "prazdne", "prazdne", "prazdne", "prazdne", "prazdne", "prazdne");

INSERT INTO user (id, login, name, password, salt, is_active, is_admin, is_protected, partner_id)
VALUES (null, "admin", "Administrator", "Plc3tigVvlhKwTMtCTgMjIjl50tuPqf72SmUambZcaM=", "d+R028fDFKZRpWD87beKdg==", true, true, true, 1);