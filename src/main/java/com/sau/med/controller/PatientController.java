package com.sau.med.controller;

import com.sau.med.db.PatientCrudOperations;
import com.sau.med.dto.Patient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.util.Optional;

public class PatientController {

    @FXML
    private TextField patientId;

    @FXML
    private TextField name;

    @FXML
    private TextField address;

    @FXML
    private TextField telephone;

    @FXML
    void clearPatient(ActionEvent event) {
        patientId.setText("");
        name.setText("");
        address.setText("");
        telephone.setText("");
    }

    @FXML
    public void close(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void getPatient(ActionEvent event) {
        Integer id = parsePositiveId(patientId.getText());
        if (id == null) {
            showError("Id is wrong!");
            return;
        }

        PatientCrudOperations crudOperations = new PatientCrudOperations();
        Optional<Patient> patient = crudOperations.getPatientById(id);
        if (patient.isPresent()) {
            patientId.setText(Integer.toString(patient.get().getId()));
            name.setText(patient.get().getName());
            address.setText(patient.get().getAddress());
            telephone.setText(patient.get().getTelephone());
        } else {
            showError("Patient with id " + id + " not found");
        }
    }

    @FXML
    void savePatient(ActionEvent event) {
        Integer id = parsePositiveId(patientId.getText());
        if (id == null) {
            showError("Id is wrong!");
            return;
        }

        Patient patient = new Patient();
        patient.setId(id);
        patient.setName(name.getText());
        patient.setAddress(address.getText());
        patient.setTelephone(telephone.getText());

        PatientCrudOperations crudOperations = new PatientCrudOperations();
        int result = crudOperations.insertPatientById(patient);

        if (result > 0) {
            showInfo("Patient with id " + patientId.getText() + " saved");
        } else if (result == -1) {
            showError("There is another patient with id: " + patientId.getText());
        } else if (result == -2) {
            showError("Database error while saving patient");
        } else {
            showError("Error on save patient!");
        }
    }

    @FXML
    void updatePatient(ActionEvent event) {
        Integer id = parsePositiveId(patientId.getText());
        if (id == null) {
            showError("Id is wrong!");
            return;
        }

        Patient patient = new Patient();
        patient.setId(id);
        patient.setName(name.getText());
        patient.setAddress(address.getText());
        patient.setTelephone(telephone.getText());

        PatientCrudOperations crudOperations = new PatientCrudOperations();
        int result = crudOperations.updatePatientById(patient);
        if (result > 0) {
            showInfo("Patient with id " + patientId.getText() + " updated");
        } else if (result == -2) {
            showError("Database error while updating patient");
        } else {
            showError("Error on update patient!");
        }
    }

    @FXML
    void deletePatient(ActionEvent event) {
        Integer id = parsePositiveId(patientId.getText());
        if (id == null) {
            showError("Id is wrong!");
            return;
        }

        PatientCrudOperations crudOperations = new PatientCrudOperations();
        int result = crudOperations.deletePatientById(id);
        if (result == -2) {
            showError("Database error while deleting patient");
        } else if (result > 0) {
            showInfo("Patient with id " + patientId.getText() + " deleted");
            clearPatient(event);
        } else {
            showError("Patient with id " + patientId.getText() + " not found");
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
