package com.sau.med.controller;

import com.sau.med.db.AppointmentCrudOperations;
import com.sau.med.dto.Appointment;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class AppointmentController {

    @FXML
    private TextField appointmentId;

    @FXML
    private TextField patientId;

    @FXML
    private TextField doctorId;

    @FXML
    private TextField appointmentDate;

    @FXML
    private TextField appointmentTime;

    @FXML
    void clearAppointment(ActionEvent event) {
        appointmentId.setText("");
        patientId.setText("");
        doctorId.setText("");
        appointmentDate.setText("");
        appointmentTime.setText("");
    }

    @FXML
    public void close(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void getAppointment(ActionEvent event) {
        Integer id = parsePositiveId(appointmentId.getText());
        if (id == null) {
            showError("Id is wrong!");
            return;
        }

        AppointmentCrudOperations crudOperations = new AppointmentCrudOperations();
        Optional<Appointment> appointment = crudOperations.getAppointmentById(id);

        if (appointment.isPresent()) {
            appointmentId.setText(Integer.toString(appointment.get().getId()));
            patientId.setText(Integer.toString(appointment.get().getPatientId()));
            doctorId.setText(Integer.toString(appointment.get().getDoctorId()));
            appointmentDate.setText(appointment.get().getAppointmentDate());
            appointmentTime.setText(appointment.get().getAppointmentTime());
        } else {
            showError("Appointment with id " + id + " not found");
        }
    }

    @FXML
    void saveAppointment(ActionEvent event) {
        Appointment appointment = buildAppointmentFromForm();
        if (appointment == null) {
            return;
        }

        AppointmentCrudOperations crudOperations = new AppointmentCrudOperations();
        int result = crudOperations.insertAppointmentById(appointment);

        if (result > 0) {
            showInfo("Appointment with id " + appointmentId.getText() + " saved");
        } else if (result == -1) {
            showError("There is another appointment with id: " + appointmentId.getText());
        } else if (result == -2) {
            showError("Database error while saving appointment");
        } else {
            showError("Error on save appointment!");
        }
    }

    @FXML
    void updateAppointment(ActionEvent event) {
        Appointment appointment = buildAppointmentFromForm();
        if (appointment == null) {
            return;
        }

        AppointmentCrudOperations crudOperations = new AppointmentCrudOperations();
        int result = crudOperations.updateAppointmentById(appointment);
        if (result > 0) {
            showInfo("Appointment with id " + appointmentId.getText() + " updated");
        } else if (result == -2) {
            showError("Database error while updating appointment");
        } else {
            showError("Error on update appointment!");
        }
    }

    @FXML
    void deleteAppointment(ActionEvent event) {
        Integer id = parsePositiveId(appointmentId.getText());
        if (id == null) {
            showError("Id is wrong!");
            return;
        }

        AppointmentCrudOperations crudOperations = new AppointmentCrudOperations();
        int result = crudOperations.deleteAppointmentById(id);
        if (result == -2) {
            showError("Database error while deleting appointment");
        } else if (result > 0) {
            showInfo("Appointment with id " + appointmentId.getText() + " deleted");
            clearAppointment(event);
        } else {
            showError("Appointment with id " + appointmentId.getText() + " not found");
        }
    }

    private Appointment buildAppointmentFromForm() {
        Integer id = parsePositiveId(appointmentId.getText());
        Integer pId = parsePositiveId(patientId.getText());
        Integer dId = parsePositiveId(doctorId.getText());

        if (id == null || pId == null || dId == null) {
            showError("Ids must be positive integers");
            return null;
        }

        if (appointmentDate.getText().isBlank() || appointmentTime.getText().isBlank()) {
            showError("Appointment date/time are required. Use YYYY-MM-DD and HH:MM:SS");
            return null;
        }

        try {
            LocalDate.parse(appointmentDate.getText().trim());
            LocalTime.parse(appointmentTime.getText().trim());
        } catch (DateTimeParseException e) {
            showError("Wrong format. Date: YYYY-MM-DD, Time: HH:MM:SS");
            return null;
        }

        Appointment appointment = new Appointment();
        appointment.setId(id);
        appointment.setPatientId(pId);
        appointment.setDoctorId(dId);
        appointment.setAppointmentDate(appointmentDate.getText().trim());
        appointment.setAppointmentTime(appointmentTime.getText().trim());

        return appointment;
    }

    private Integer parsePositiveId(String idText) {
        try {
            int id = Integer.parseInt(idText);
            if (id <= 0) {
                return null;
            }
            return id;
        } catch (Exception e) {
            return null;
        }
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}
