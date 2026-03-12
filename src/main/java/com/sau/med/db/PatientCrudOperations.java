package com.sau.med.db;

import com.sau.med.dto.Patient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class PatientCrudOperations {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/med_management";
    static final String USER = "postgres";
    static final String PASS = "Obadasm1234";

    // Get a patient by id
    public Optional<Patient> getPatientById(int id) {
        System.out.println("[Patient][GET] id=" + id);
        Patient patient = null;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM patients WHERE id = " + id;
            System.out.println("[Patient][SQL] " + query);
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                patient = new Patient();
                patient.setId(resultSet.getInt("id"));
                patient.setName(resultSet.getString("name"));
                patient.setAddress(resultSet.getString("address"));
                patient.setTelephone(resultSet.getString("telephone"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (patient != null) {
            System.out.println("[Patient][GET] FOUND id=" + id);
            return Optional.of(patient);
        }
        System.out.println("[Patient][GET] NOT FOUND id=" + id);
        return Optional.empty();
    }

    // Insert a patient by id
    public int insertPatientById(Patient patient) {
        System.out.println("[Patient][INSERT] id=" + patient.getId());
        int result;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            Statement statement = connection.createStatement();

            if (getPatientById(patient.getId()).isPresent()) {
                System.out.println("[Patient][INSERT] DUPLICATE id=" + patient.getId());
                return -1;
            }

            String params = patient.getId() + ", '" + patient.getName() + "','" + patient.getAddress() + "','" + patient.getTelephone() + "'";
            String query = "INSERT INTO patients (id, name, address, telephone) VALUES (" + params + ");";
            System.out.println("[Patient][SQL] " + query);
            result = statement.executeUpdate(query);
            System.out.println("[Patient][INSERT] RESULT=" + result);
        } catch (Exception e) {
            System.out.println("[Patient][INSERT] ERROR: " + e.getMessage());
            return -2;
        }
        return result;
    }

    // Delete a patient by id
    public int deletePatientById(int id) {
        System.out.println("[Patient][DELETE] id=" + id);
        int result;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            Statement statement = connection.createStatement();
            String query = "DELETE FROM patients WHERE id = " + id;
            System.out.println("[Patient][SQL] " + query);
            result = statement.executeUpdate(query);
            System.out.println("[Patient][DELETE] RESULT=" + result);
        } catch (Exception e) {
            System.out.println("[Patient][DELETE] ERROR: " + e.getMessage());
            return -2;
        }
        return result;
    }

    // Update a patient by id
    public int updatePatientById(Patient patient) {
        System.out.println("[Patient][UPDATE] id=" + patient.getId());
        int result = 0;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            Statement statement = connection.createStatement();

            if (getPatientById(patient.getId()).isPresent()) {
                String query = "UPDATE patients SET " +
                        "name = '" + patient.getName() + "', " +
                        "address = '" + patient.getAddress() + "', " +
                        "telephone = '" + patient.getTelephone() + "' WHERE id = " + patient.getId() + ";";
                System.out.println("[Patient][SQL] " + query);
                result = statement.executeUpdate(query);
                System.out.println("[Patient][UPDATE] RESULT=" + result);
            } else {
                System.out.println("[Patient][UPDATE] NOT FOUND id=" + patient.getId());
            }
        } catch (Exception e) {
            System.out.println("[Patient][UPDATE] ERROR: " + e.getMessage());
            return -2;
        }
        return result;
    }
}
