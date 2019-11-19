-- View the number of available vehicles for a specific car type, location, and time interval. 
-- The user should be able to provide any subset of {car type, location, time interval} to view 
-- the available vehicles. If the user provides no information, your application should automatically
-- return a list of all vehicles (at that branch) sorted in some reasonable way for the user to peruse.  
 
-- The actual number of available vehicles should be displayed. After seeing the number of vehicles, 
-- there should be a way for the user to see the details of the available vehicles if the user desires
--  to do so (e.g., if the user clicks on the number of available vehicles, a list with the vehicles’ 
--  details should be displayed).  
SELECT *, COUNT(DISTINCT vlicense)
FROM Vehicle v
WHERE inputCarType = v.vtname AND v.location = inputLocation AND v.reserved = 0 AND v.vlicense IN  
--Assuming carType is passed in by user and contains things like make, model, year of car
(SELECT vlicense
FROM Vehicle
EXCEPT
(SELECT vlicense
FROM Rent
WHERE timeInterval BETWEEN fromDateTime AND toDateTime))
ORDER BY v.vtname

-- Make a reservation. If a customer is new, add the customer’s details to the database. 
-- (You may choose to have a different interface for this).    
 
-- Upon successful completion, a confirmation number for the reservation should be shown 
-- along with the details entered during the reservation. Refer to the project description 
-- for details on what kind of information the user needs to provide when making a reservation. 
 
-- If the customer’s desired vehicle is not available, an appropriate error message should be shown.

-- Add new customer
INSERT INTO Customer(dlicense, cellphone, name, address)
VALUES (inputDlicense, inputCellPhone, inputName, inputAddress)

-- Make reservation
DECLARE @hybridOrElectric BIT
DECLARE @reservedVlicense CHAR(32)
UPDATE TOP (1) Vehicle 
SET reserved = 1,
    @hybridOrElectric = (SELECT CASE WHEN inserted.gasTypeID = 1 THEN 0 ELSE 1 END),
    @reservedVlicense = inserted.vlicense
WHERE vtname = inputVtname AND reserved = 0

DECLARE @CostEstimation INT
SET @CostEstimation = 5 -- Use CalculateValueSP
IF @hybridOrElectric = 1 
    SET @CostEstimation = @CostEstimation + 30
INSERT INTO Reservations(vtname, vlicense, fromDateTime, toDateTime)
OUTPUT Inserted.confNo, Inserted.vtname, Inserted.vlicense, Inserted.fromDateTime, Inserted.toDateTime, @CostEstimation
VALUES (inputVtname, @reservedVlicense, inputFromDateTime, inputToDateTime)

-- IsVehicleAvailable
-- Can use query above