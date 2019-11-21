-- Codeset for status 1=Rented, 2=Maintenance, 3=Available
CREATE TABLE Status(
    statusID INT PRIMARY KEY,
    status VARCHAR(50)
);

-- Codeset for gasType 1=Gasoline, 2=Hybrid, 3=Electric
CREATE TABLE GasType(
    gasTypeID INT PRIMARY KEY,
    gasType VARCHAR(50)
);

CREATE TABLE Cards(
    cardNo VARCHAR(50) PRIMARY KEY,
    expDate VARCHAR(50),
    cardName VARCHAR(50)
);

CREATE TABLE VehicleTypes(
    vtname VARCHAR(50) PRIMARY KEY,
    features VARCHAR(50),
    wrate INT,
    drate INT,
    hrate INT,
    wirate INT,
    dirate INT,
    hirate INT,
    krate INT
);

-- Removed Club Member
CREATE TABLE Customer(
    dlicense VARCHAR(50) PRIMARY KEY,
    cellphone VARCHAR(50),
    name VARCHAR(50),
    address VARCHAR(50)
);

-- All vehicles are for rent 
-- Removed requirements associated with Equipment
CREATE TABLE Vehicle (
    vid INT UNIQUE,
    vlicense VARCHAR(50) PRIMARY KEY,
    make VARCHAR(50),
    model VARCHAR(50),
    year VARCHAR(4),
    color VARCHAR(50),
    odometer NUMBER,
    gasTypeID INT REFERENCES GasType(gasTypeID),
    statusID INT REFERENCES Status(statusID), --FK to Status table
    vtname VARCHAR(50) REFERENCES VehicleTypes(vtname),
    location VARCHAR(50),
    city VARCHAR(50),
    reserved NUMBER(1)
);


CREATE TABLE Reservations(
    confNo VARCHAR(50) PRIMARY KEY,
    vtname VARCHAR(50) REFERENCES VehicleTypes(vtname),
    vlicense VARCHAR(50) REFERENCES Vehicle(vlicense),
    dlicense VARCHAR(50) REFERENCES Customer(dlicense),
    fromDateTime VARCHAR(50),
    toDateTime VARCHAR(50)
);

CREATE TABLE Rent(
    rid INT PRIMARY KEY,
    vlicense VARCHAR(50) REFERENCES Vehicle(vlicense),
    dlicense VARCHAR(50) REFERENCES Customer(dlicense), 
    dateTime VARCHAR(50),
    fromDateTime VARCHAR(50),
    toDateTime VARCHAR(50),
    odometer NUMBER,
    cardNo VARCHAR(50) REFERENCES Cards(cardNo),
    confNo VARCHAR(50) REFERENCES Reservations(confNo)
);

CREATE TABLE "RETURN"(
    rid INT PRIMARY KEY REFERENCES Rent(rid),
    dateTime VARCHAR(50),
    odometer NUMBER, 
    fullTank NUMBER(1),
    value INT
);