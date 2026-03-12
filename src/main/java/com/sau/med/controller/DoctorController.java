package com.sau.med.controller;

import com.sau.med.db.DoctorCrudOperations;
import com.sau.med.dto.Doctor;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.util.Optional;

public class DoctorController {

    @FXML
    private TextField doctorId;

    @FXML
    private TextField name;

    @FXML
    private TextField clinique;

    @FXML
    void clearDoctor(ActionEvent event) {
        doctorId.setText("");
        name.setText("");
        clinique.setText("");
    }

    @FXML
    public void close(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void getDoctor(ActionEvent event) {
        Integer id = parsePositiveId(doctorId.getText());
        if (id == null) {
            showError("Id is wrong!");
            return;
        }

        DoctorCrudOperations crudOperations = new DoctorCrudOperations();
        Optional<Doctor> doctor = crudOperations.getDoctorById(id);
        if (doctor.isPresent()) {
            doctorId.setText(Integer.toString(doctor.get().getId()));
            name.setText(doctor.get().getName());
            clinique.setText(doctor.get().getClinique());
        } else {
            showError("Doctor with id " + id + " not found");
        }
    }

    @FXML
    void saveDoctor(ActionEvent event) {
        Integer id = parsePositiveId(doctorId.getText());
        if (id == null) {
            showError("Id is wrong!");
            return;
        }

        Doctor doctor = new Doctor();
        doctor.setId(id);
        doctor.setName(name.getText());
        doctor.setClinique(clinique.getText());

        DoctorCrudOperations crudOperations = new DoctorCrudOperations();
        int result = crudOperations.insertDoctorById(doctor);

        if (result > 0) {
            showInfo("Doctor with id " + doctorId.getText() + " saved");
        } else if (result == -1) {
            showError("There is another doctor with id: " + doctorId.getText());
        } else if (result == -2) {
            showError("Database error while saving doctor");
        } else {
            showError("Error on save doctor!");
        }
    }

    @FXML
    void updateDoctor(ActionEvent event) {
        Integer id = parsePositiveId(doctorId.getText());
        if (id == null) {
            showError("Id is wrong!");
            return;
        }

        Doctor doctor = new Doctor();
        doctor.setId(id);
        doctor.setName(name.getText());
        doctor.setClinique(clinique.getText());

        DoctorCrudOperations crudOperations = new DoctorCrudOperations();
        int result = crudOperations.updateDoctorById(doctor);
        if (result > 0) {
            showInfo("Doctor with id " + doctorId.getText() + " updated");
        } else if (result == -2) {
            showError("Database error while updating doctor");
        } else {
            showError("Error on update doctor!");
        }
    }

    @FXML
    void deleteDoctor(ActionEvent event) {
        Integer id = parsePositiveId(doctorId.getText());
        if (id == null) {
            showError("Id is wrong!");
            return;
        }

        DoctorCrudOperations crudOperations = new DoctorCrudOperations();
        int result = crudOperations.deleteDoctorById(id);
        if (result == -2) {
            showError("Database error while deleting doctor");
        } else if (result > 0) {
            showInfo("Doctor with id " + doctorId.getText() + " deleted");
            clearDoctor(event);
        } else {
            showError("Doctor with id " + doctorId.getText() + " not found");
        }
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
