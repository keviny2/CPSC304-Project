-- CalculateValue SP
CREATE PROCEDURE CalculateValue(
    IN @vlicense VARCHAR(32),
    IN @toDate VARCHAR(32)
AS

DECLARE @value INT,
DECLARE @howCalculate VARCHAR(250))
DECLARE @weeks INT
DECLARE @days INT
DECLARE @hours INT
DECLARE @wrate INT
DECLARE @drate INT
DECLARE @hrate INT
DECLARE @value INT
DECLARE @howCalculate VARCHAR(250) 
DECLARE @swrate VARCHAR(10)
DECLARE @sdrate VARCHAR(10)
DECLARE @shrate VARCHAR(10)
DECLARE @sweeks VARCHAR(10)
DECLARE @sdays VARCHAR(10)
DECLARE @shours VARCHAR(10)
DECLARE @fromDate TIMESTAMP

SET @fromDate = (SELECT fromDateTime FROM Rent WHERE vlicense = @vlicense)
SET @fromDate = TO_TIMESTAMP(@fromDate, 'YYYY-MM-DD')
SET @toDate = TO_TIMESTAMP(@toDate, 'YYYY-MM-DD')
SET @weeks = CAST(EXTRACT(WEEK FROM @toDate - @fromDate) AS INT)
SET @days = CAST(EXTRACT(DAY FROM @toDate - @fromDate) AS INT)
SET @hours = CAST(EXTRACT(HOUR FROM @toDate - @fromDate) AS INT)
SET @wrate = (SELECT wrate FROM VehicleType WHERE vlicense = @vlicense)
SET @drate = (SELECT drate FROM VehicleType WHERE vlicense = @vlicense)
SET @hrate = (SELECT hrate FROM VehicleType WHERE vlicense = @vlicense)
SET @swrate = CAST(@wrate AS VARCHAR(10))
SET @sdrate = CAST(@wrate AS VARCHAR(10))
SET @shrate = CAST(@wrate AS VARCHAR(10))
SET @sweeks = CAST(@weeks AS VARCHAR(10))
SET @sdays = CAST(@days AS VARCHAR(10))
SET @shours = CAST(@hours AS VARCHAR(10))
SET @value = @weeks*@wrate + @days*@drate + @hours*@hrate 
SET @howCalculate = "Calculated using: " + @sweeks + "*" + @swrate + " + " + @sdays + "*" + @sdrate + " + " + @shours + "*" + @shrate 
SELECT @value as Value, @howCaluclate as HowCalculate