package com.sau.med.db;

import com.sau.med.dto.Appointment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class AppointmentCrudOperations {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/med_management";
    static final String USER = "postgres";
    static final String PASS = "Obadasm1234";

    // Get an appointment by id
    public Optional<Appointment> getAppointmentById(int id) {
        System.out.println("[Appointment][GET] id=" + id);
        Appointment appointment = null;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM appointments WHERE id = " + id;
            System.out.println("[Appointment][SQL] " + query);
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                appointment = new Appointment();
                appointment.setId(resultSet.getInt("id"));
                appointment.setPatientId(resultSet.getInt("patient_id"));
                appointment.setDoctorId(resultSet.getInt("doctor_id"));
                appointment.setAppointmentDate(resultSet.getDate("appointment_date").toString());
                appointment.setAppointmentTime(resultSet.getTime("appointment_time").toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (appointment != null) {
            System.out.println("[Appointment][GET] FOUND id=" + id);
            return Optional.of(appointment);
        }
        System.out.println("[Appointment][GET] NOT FOUND id=" + id);
        return Optional.empty();
    }

    // Insert an appointment by id
    public int insertAppointmentById(Appointment appointment) {
        System.out.println("[Appointment][INSERT] id=" + appointment.getId());
        int result;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            Statement statement = connection.createStatement();

            if (getAppointmentById(appointment.getId()).isPresent()) {
                System.out.println("[Appointment][INSERT] DUPLICATE id=" + appointment.getId());
                return -1;
            }

            String params = appointment.getId() + ", " +
                    appointment.getPatientId() + ", " +
                    appointment.getDoctorId() + ", '" +
                    appointment.getAppointmentDate() + "', '" +
                    appointment.getAppointmentTime() + "'";

            String query = "INSERT INTO appointments (id, patient_id, doctor_id, appointment_date, appointment_time) VALUES (" + params + ");";
            System.out.println("[Appointment][SQL] " + query);
            result = statement.executeUpdate(query);
            System.out.println("[Appointment][INSERT] RESULT=" + result);
        } catch (Exception e) {
            System.out.println("[Appointment][INSERT] ERROR: " + e.getMessage());
            return -2;
        }
        return result;
    }

    // Delete an appointment by id
    public int deleteAppointmentById(int id) {
        System.out.println("[Appointment][DELETE] id=" + id);
        int result;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            Statement statement = connection.createStatement();
            String query = "DELETE FROM appointments WHERE id = " + id;
            System.out.println("[Appointment][SQL] " + query);
            result = statement.executeUpdate(query);
            System.out.println("[Appointment][DELETE] RESULT=" + result);
        } catch (Exception e) {
            System.out.println("[Appointment][DELETE] ERROR: " + e.getMessage());
            return -2;
        }
        return result;
    }

    // Update an appointment by id
    public int updateAppointmentById(Appointment appointment) {
        System.out.println("[Appointment][UPDATE] id=" + appointment.getId());
        int result = 0;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            Statement statement = connection.createStatement();

            if (getAppointmentById(appointment.getId()).isPresent()) {
                String query = "UPDATE appointments SET " +
                        "patient_id = " + appointment.getPatientId() + ", " +
                        "doctor_id = " + appointment.getDoctorId() + ", " +
                        "appointment_date = '" + appointment.getAppointmentDate() + "', " +
                        "appointment_time = '" + appointment.getAppointmentTime() + "' WHERE id = " + appointment.getId() + ";";
                System.out.println("[Appointment][SQL] " + query);
                result = statement.executeUpdate(query);
                System.out.println("[Appointment][UPDATE] RESULT=" + result);
            } else {
                System.out.println("[Appointment][UPDATE] NOT FOUND id=" + appointment.getId());
            }
        } catch (Exception e) {
            System.out.println("[Appointment][UPDATE] ERROR: " + e.getMessage());
            return -2;
        }
        return result;
    }
}
