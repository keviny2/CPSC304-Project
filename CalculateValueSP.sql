-- CalculateValue SP
CREATE PROCEDURE CalculateValue
    @vtname CHAR(32),
    @fromDate DATETIME,
    @toDate DATETIME,
    @value INT OUTPUT,
    @howCalculate VARCHAR(250) OUTPUT
AS

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

SET @weeks = CAST(EXTRACT(WEEK FROM @toDate - @fromDate) AS INT)
SET @days = CAST(EXTRACT(DAY FROM @toDate - @fromDate) AS INT)
SET @hours = CAST(EXTRACT(HOUR FROM @toDate - @fromDate) AS INT)
SET @vtname = (SELECT vt.vtname FROM Rent r 
                                INNER JOIN Vehicle v ON v.vlicense = r.vlicense
                                INNER JOIN VehicleType vt ON vt.vtname = v.vtname
                                WHERE r.rid = inputRid)
SET @wrate = (SELECT wrate FROM VehicleType WHERE vtname = @vtname)
SET @drate = (SELECT drate FROM VehicleType WHERE vtname = @vtname)
SET @hrate = (SELECT hrate FROM VehicleType WHERE vtname = @vtname)
SET @swrate = CAST(@wrate AS VARCHAR(10))
SET @sdrate = CAST(@wrate AS VARCHAR(10))
SET @shrate = CAST(@wrate AS VARCHAR(10))
SET @sweeks = CAST(@weeks AS VARCHAR(10))
SET @sdays = CAST(@days AS VARCHAR(10))
SET @shours = CAST(@hours AS VARCHAR(10))
SET @value = @weeks*@wrate + @days*@drate + @hours*@hrate 
SET @howCalculate = "Calculated using: " + @sweeks + "*" + @swrate + " + " + @sdays + "*" + @sdrate + " + " + @shours + "*" + @shrate 
SELECT @value, @howCaluclate