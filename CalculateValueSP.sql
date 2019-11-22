-- CalculateValue SP
CREATE PROCEDURE CalculateValue(
    IN @vlicense VARCHAR(32),
    IN @toDate VARCHAR(32))
AS

DECLARE @fromDate TIMESTAMP := (SELECT fromDateTime FROM Rent WHERE vlicense = @vlicense);
@fromDate := TO_TIMESTAMP(@fromDate, 'YYYY-MM-DD');
DECLARE @toDate TIMESTAMP := TO_TIMESTAMP(@toDate, 'YYYY-MM-DD');
DECLARE @weeks := CAST(EXTRACT(WEEK FROM @toDate - @fromDate) AS INT);
DECLARE @days := CAST(EXTRACT(DAY FROM @toDate - @fromDate) AS INT);
DECLARE @hours := CAST(EXTRACT(HOUR FROM @toDate - @fromDate) AS INT);
DECLARE @wrate := (SELECT wrate FROM VehicleType WHERE vlicense = @vlicense);
DECLARE @drate := (SELECT drate FROM VehicleType WHERE vlicense = @vlicense);
DECLARE @hrate := (SELECT hrate FROM VehicleType WHERE vlicense = @vlicense);
DECLARE @swrate := CAST(@wrate AS VARCHAR(10));
DECLARE @sdrate := CAST(@wrate AS VARCHAR(10));
DECLARE @shrate := CAST(@wrate AS VARCHAR(10));
DECLARE @sweeks := CAST(@weeks AS VARCHAR(10));
DECLARE @sdays := CAST(@days AS VARCHAR(10));
DECLARE @shours := CAST(@hours AS VARCHAR(10));
DECLARE @value := @weeks*@wrate + @days*@drate + @hours*@hrate;
DECLARE @howCalculate := "Calculated using: " + @sweeks + "*" + @swrate + " + " + @sdays + "*" + @sdrate + " + " + @shours + "*" + @shrate;
SELECT @value as Value, @howCaluclate as HowCalculate;

SELECT r.fromDateTime, vt.wrate, vt.drate, vt.hrate
FROM Rent r, VehicleType vt
WHERE r.vlicense = vt.vlicense and vlicense = ?