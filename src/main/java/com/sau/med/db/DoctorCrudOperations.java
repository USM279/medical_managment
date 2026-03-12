package com.sau.med.db;

import com.sau.med.dto.Doctor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class DoctorCrudOperations {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/med_management";
    static final String USER = "postgres";
    static final String PASS = "Obadasm1234";

    // Get a doctor by id
    public Optional<Doctor> getDoctorById(int id) {
        System.out.println("[Doctor][GET] id=" + id);
        Doctor doctor = null;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM doctors WHERE id = " + id;
            System.out.println("[Doctor][SQL] " + query);
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                doctor = new Doctor();
                doctor.setId(resultSet.getInt("id"));
                doctor.setName(resultSet.getString("name"));
                doctor.setClinique(resultSet.getString("clinique"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (doctor != null) {
            System.out.println("[Doctor][GET] FOUND id=" + id);
            return Optional.of(doctor);
        }
        System.out.println("[Doctor][GET] NOT FOUND id=" + id);
        return Optional.empty();
    }

    // Insert a doctor by id
    public int insertDoctorById(Doctor doctor) {
        System.out.println("[Doctor][INSERT] id=" + doctor.getId());
        int result;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            Statement statement = connection.createStatement();

            if (getDoctorById(doctor.getId()).isPresent()) {
                System.out.println("[Doctor][INSERT] DUPLICATE id=" + doctor.getId());
                return -1;
            }

            String params = doctor.getId() + ", '" + doctor.getName() + "','" + doctor.getClinique() + "'";
            String query = "INSERT INTO doctors (id, name, clinique) VALUES (" + params + ");";
            System.out.println("[Doctor][SQL] " + query);
            result = statement.executeUpdate(query);
            System.out.println("[Doctor][INSERT] RESULT=" + result);
        } catch (Exception e) {
            System.out.println("[Doctor][INSERT] ERROR: " + e.getMessage());
            return -2;
        }
        return result;
    }

    // Delete a doctor by id
    public int deleteDoctorById(int id) {
        System.out.println("[Doctor][DELETE] id=" + id);
        int result;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            Statement statement = connection.createStatement();
            String query = "DELETE FROM doctors WHERE id = " + id;
            System.out.println("[Doctor][SQL] " + query);
            result = statement.executeUpdate(query);
            System.out.println("[Doctor][DELETE] RESULT=" + result);
        } catch (Exception e) {
            System.out.println("[Doctor][DELETE] ERROR: " + e.getMessage());
            return -2;
        }
        return result;
    }

    // Update a doctor by id
    public int updateDoctorById(Doctor doctor) {
        System.out.println("[Doctor][UPDATE] id=" + doctor.getId());
        int result = 0;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            Statement statement = connection.createStatement();

            if (getDoctorById(doctor.getId()).isPresent()) {
                String query = "UPDATE doctors SET " +
                        "name = '" + doctor.getName() + "', " +
                        "clinique = '" + doctor.getClinique() + "' WHERE id = " + doctor.getId() + ";";
                System.out.println("[Doctor][SQL] " + query);
                result = statement.executeUpdate(query);
                System.out.println("[Doctor][UPDATE] RESULT=" + result);
            } else {
                System.out.println("[Doctor][UPDATE] NOT FOUND id=" + doctor.getId());
            }
        } catch (Exception e) {
            System.out.println("[Doctor][UPDATE] ERROR: " + e.getMessage());
            return -2;
        }
        return result;
    }
}
