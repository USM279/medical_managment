CREATE DATABASE med_management;
\connect med_management

CREATE TABLE IF NOT EXISTS patients (
    id INTEGER PRIMARY KEY,
    name VARCHAR(16) NOT NULL,
    address VARCHAR(32),
    telephone VARCHAR(16)
);

CREATE TABLE IF NOT EXISTS doctors (
    id INTEGER PRIMARY KEY,
    name VARCHAR(16) NOT NULL,
    clinique VARCHAR(16)
);

CREATE TABLE IF NOT EXISTS appointments (
    id INTEGER PRIMARY KEY,
    patient_id INTEGER NOT NULL REFERENCES patients(id) ON DELETE RESTRICT,
    doctor_id INTEGER NOT NULL REFERENCES doctors(id) ON DELETE RESTRICT,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    UNIQUE (doctor_id, appointment_date, appointment_time)
);
