package com.example.lab3;

public class Car {
    private String carMark;
    private String carModel;
    private String carYear;
    private String carRun;
    private String dtpCity;
    private String dtpCharacter;
    private String horsePower;
    private String engineMark;
    private String numOfEngine;
    private String transmissionType;
    private String phoneNumber;
    private String email;
    private String instLink;
    private String imagePath;

    public Car(String carMark, String carModel, String carYear, String carRun, String dtpCity, String dtpCharacter, String horsePower, String engineMark, String numOfEngine, String transmissionType, String email, String phoneNumber, String instLink, String imagePath) {
        this.carMark = carMark;
        this.carModel = carModel;
        this.carYear = carYear;
        this.carRun = carRun;
        this.dtpCity = dtpCity;
        this.dtpCharacter = dtpCharacter;
        this.horsePower = horsePower;
        this.engineMark = engineMark;
        this.numOfEngine = numOfEngine;
        this.transmissionType = transmissionType;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.instLink = instLink;
        this.imagePath = imagePath;
    }

    public String getCarMark() {
        return carMark;
    }

    public String getCarModel() {
        return carModel;
    }

    public String getCarYear() {
        return carYear;
    }

    public String getCarRun() {
        return carRun;
    }

    public String getDtpCity() {
        return dtpCity;
    }

    public String getDtpCharacter() {
        return dtpCharacter;
    }

    public String getHorsePower() {
        return horsePower;
    }

    public String getEngineMark() {
        return engineMark;
    }

    public String getNumOfEngine() {
        return numOfEngine;
    }

    public String getTransmissionType() {
        return transmissionType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getDetails() {
        return "Car Details: " + carMark + " " + carModel + ", Year: " + carYear + ", Run: " + carRun + ", City: " + dtpCity + ", Character: " + dtpCharacter + ", Horsepower: " + horsePower + ", Engine Mark: " + engineMark + ", Engine Number: " + numOfEngine + ", Transmission: " + transmissionType;
    }

    public String getInstLink() {
        return instLink;
    }

    public String getImagePath() {
        return imagePath;
    }
}
