-- All vehicles are for rent 
-- Removed requirements associated with Equipment
CREATE TABLE Vehicle (
    vid INT UNIQUE,
    vlicense CHAR(32) IDENTITY(1,1) PRIMARY KEY,
    make CHAR(32),
    model CHAR(32),
    year CHAR(4),
    color CHAR(20),
    odometer INT,
    statusID INT, --FK to Status table
    vtname CHAR(32),
    location CHAR(50),
    city CHAR(32),

    FOREIGN KEY (vtname) REFERENCES VehicleType(vtname),
    FOREIGN KEY (status) REFERENCES Status(statusID)
)

-- Codeset for status 1=Rented, 2=Maintenance, 3=Available
CREATE TABLE Status(
    statusID INT PRIMARY KEY,
    status CHAR(20)
)

CREATE TABLE VehicleTypes(
    vtname CHAR(32) PRIMARY KEY,
    features CHAR(50),
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
    dlicense CHAR(20) IDENTITY(1,1) PRIMARY KEY,
    cellphone CHAR(20),
    name CHAR(32),
    address CHAR(32)
)

CREATE TABLE Reservations(
    confNo INT IDENTITY(1,1) PRIMARY KEY,
    vtname CHAR(32),
    dlicense CHAR(20),
    fromDateTime DATETIME,
    toDateTime DATETIME,

    FOREIGN KEY (vtname) REFERENCES VehicleType(vtname),
    FOREIGN KEY (dlicense) REFERENCES Customer(dlicense)
)

CREATE TABLE Rent(
    rid INT IDENTITY(1,1) PRIMARY KEY,
    vlicense CHAR(32),
    dlicense CHAR(20), 
    date DATETIME,
    fromDateTime DATETIME,
    toDateTime DATETIME,
    odometer INT,
    cardNo CHAR(32),
    confNo INT NULL,

    FOREIGN KEY (vlicense) REFERENCES Vehicle(vlicense),
    FOREIGN KEY (dlicense) REFERENCES Customer(dlicense),
    FOREIGN KEY (confNo) REFERENCES Reservation(confNo),
    FOREIGN KEY (cardNo) REFERENCES Cards
)

CREATE TABLE Cards(
    cardNo CHAR(32) PRIMARY KEY,
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