-- All vehicles are for rent 
-- Removed requirements associated with Equipment
CREATE TABLE Vehicle (
    vid INT UNIQUE,
    vlicense CHAR(32) IDENTITY(1,1) PRIMARY KEY,
    make VARCHAR(32),
    model VARCHAR(32),
    year VARCHAR(4),
    color VARCHAR(20),
    odometer INT,
    gasTypeID INT,
    statusID INT, --FK to Status table
    vtname VARCHAR(32),
    location VARCHAR(50),
    city VARCHAR(32),
    reserved BIT,

    FOREIGN KEY (vtname) REFERENCES VehicleType(vtname),
    FOREIGN KEY (status) REFERENCES Status(statusID),
    FOREIGN KEY (gasTypeID) REFERENCES GasType(gasTypeId)
)

-- Codeset for status 1=Rented, 2=Maintenance, 3=Available
CREATE TABLE Status(
    statusID INT PRIMARY KEY,
    status VARCHAR(20)
)

-- Codeset for gasType 1=Gasoline, 2=Hybrid, 3=Electric
CREATE TABLE GasType(
    gasTypeID INT PRIMARY KEY,
    gasType VARCHAR(32)
)

CREATE TABLE VehicleTypes(
    vtname VARCHAR(32) PRIMARY KEY,
    features VARCHAR(50),
    wrate INT,
    drate INT,
    hrate INT,
    wirate INT,
    dirate INT,
    hirate INT,
    krate INT
)

-- Removed Club Member
CREATE TABLE Customer(
    dlicense VARCHAR(20) IDENTITY(1,1) PRIMARY KEY,
    cellphone VARCHAR(20),
    name VARCHAR(32),
    address VARCHAR(32)
)

CREATE TABLE Reservations(
    confNo INT IDENTITY(1,1) PRIMARY KEY,
    vtname VARCHAR(32),
    vlicense VARCHAR(32),
    dlicense VARCHAR(20),
    fromDateTime DATETIME,
    toDateTime DATETIME,

    FOREIGN KEY (vtname) REFERENCES VehicleType(vtname),
    FOREIGN KEY (dlicense) REFERENCES Customer(dlicense),
    FOREIGN KEY (vlicense) REFERENCES Vehicle(vlicense)
)

CREATE TABLE Rent(
    rid INT IDENTITY(1,1) PRIMARY KEY,
    vlicense VARCHAR(32),
    dlicense VARCHAR(20), 
    date DATETIME,
    fromDateTime DATETIME,
    toDateTime DATETIME,
    odometer INT,
    cardNo VARCHAR(32),
    confNo INT NULL,

    FOREIGN KEY (vlicense) REFERENCES Vehicle(vlicense),
    FOREIGN KEY (dlicense) REFERENCES Customer(dlicense),
    FOREIGN KEY (confNo) REFERENCES Reservation(confNo),
    FOREIGN KEY (cardNo) REFERENCES Cards
)

CREATE TABLE Cards(
    cardNo VARCHAR(32) PRIMARY KEY,
    expDate DATE,
    cardName CHAR(32)
)

CREATE TABLE [Return](
    rid INT IDENTITY(1,1) PRIMARY KEY,
    dateTime DATETIME,
    odometer INT, 
    fulltank BIT,
    value INT,

    FOREIGN KEY (rid) REFERENCES Rent(rid)
)