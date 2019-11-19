-- Renting a Vehicle: The system will display a receipt with the necessary details 
-- (e.g., confirmation number, date of reservation, type of car, location, how long 
-- the rental period lasts for, etc.) for the customer.  
 
-- Note: It is not necessary for a user to have made a reservation prior to renting a vehicle.
--  If this is the case, then you will need to determine how to appropriately handle this situation. 

-- confNo only
INSERT INTO Rent(vlicense, dlicense, date, fromDateTime, toDateTime, cardName, cardNo, expDate, confNo)
OUTPUT Inserted.rid, inputConfNo, vt.vtname, v.location, r.fromDateTime, r.toDateTime, DATETIME.NOW
SELECT v.vlicense, inputDlicense, DATETIME.NOW, r.fromDateTime, r.toDateTime, inputCardName, inputCardNo, inputExpDate, inputConfNo
FROM Reservation r
INNER JOIN VehicleType vt ON vt.vtname = r.vtname
INNER JOIN Vehicle v ON v.vtname = vt.vtname 
INNER JOIN Customer c ON c.dlicense = r.dlicense
WHERE r.confNo = inputConfNo OR c.cellphone = inputCellPhone

-- No reservation
INSERT INTO Rent(vlicense, dlicense, fromDateTime, toDateTime, cardName, cardNo, expDate)
SELECT v.vlicense, inputDlicense, inputFromDateTime, inputToDateTime, inputCardName, inputCardNo, inputExpDate
FROM VehicleType vt
INNER JOIN Vehicle v ON v.vtname = vt.vtname 
INNER JOIN Customer c ON c.dlicense = r.dlicense
WHERE vt.vtname = inputVtname

-- canRent
-- Use query in CustomerQueries.sql


-- Returning a Vehicle: Only a rented vehicle can be returned. Trying to return a vehicle that has
--  not been rented should generate some type of error message for the clerk.   
 
-- When returning a vehicle, the system will display a receipt with the necessary details 
-- (e.g., reservation confirmation number, date of return, how the total was calculated etc.) for the customer.  

-- canReturn
SELECT COUNT(*) 
FROM Rent r
WHERE r.rid = inputRid 

-- returning vehicle updating Return table
DECLARE @howCalculatep VARCHAR(250), @valuep INT
DECLARE @vtnamep CHAR(32), @fromDatep DATETIME, @toDatep DATETIME
SELECT @vtnamep = vtname, @fromDatep = fromDateTime, @toDatep = toDateTime
FROM Rent
WHERE rid = inputRid

-- EXECUTE CalculateValue @vtname = @vtnamep, @fromDate = @fromDatep, @toDate = @toDatep, @value out, @howCalculate out
-- SELECT @valuep = @value, @howCalculatep = @howCaluclate;
-- Just use CalculateValueSP.sql

INSERT INTO [Return](dateTime, odometer, fullTank, value)  
OUTPUT (
    SELECT r.confNo, DATETIME.NOW, @howCalculate
    FROM Rent r
    INNER JOIN Vehicle v ON v.vlicense = r.vlicense
    INNER JOIN VehicleType vt ON v.vtname = vt.vtname
    WHERE rid = inputRid)
SELECT DATETIME.NOW, inputOdometer, inputFullTank, value