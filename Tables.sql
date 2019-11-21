-- All vehicles are for rent 
-- Removed requirements associated with Equipment
CREATE TABLE Vehicle (
    vid INT UNIQUE,
    vlicense VARCHAR(32) PRIMARY KEY,
    make VARCHAR(32),
    model VARCHAR(32),
    year VARCHAR(4),
    color VARCHAR(20),
    odometer INT,
    gasTypeID INT REFERENCES GasType(gasTypeID),
    statusID INT REFERENCES Status(statusID), --FK to Status table
    vtname VARCHAR(32) REFERENCES VehicleTypes(vtname),
    location VARCHAR(50),
    city VARCHAR(32),
    reserved NUMBER(1)
);


CREATE TABLE Reservations(
    confNo VARCHAR(32) PRIMARY KEY,
    vtname VARCHAR(32) REFERENCES VehicleTypes(vtname),
    vlicense VARCHAR(32) REFERENCES Vehicle(vlicense),
    dlicense VARCHAR(20) REFERENCES Customer(dlicense),
    fromDateTime TIMESTAMP,
    toDateTime TIMESTAMP
);

CREATE TABLE Rent(
    rid INT PRIMARY KEY,
    vlicense VARCHAR(32) REFERENCES Vehicle(vlicense),
    dlicense VARCHAR(20) REFERENCES Customer(dlicense), 
    dateTime TIMESTAMP,
    fromdateTime TIMESTAMP,
    todateTime TIMESTAMP,
    odometer INT,
    cardNo VARCHAR(32) REFERENCES Cards(cardNo),
    confNo VARCHAR(32) REFERENCES Reservations(confNo)
);

CREATE TABLE "RETURN"(
    rid INT PRIMARY KEY REFERENCES Rent(rid),
    dateTime TIMESTAMP,
    odometer INT, 
    fulltank NUMBER(1),
    value INT
);