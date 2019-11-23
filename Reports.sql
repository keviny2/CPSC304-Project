-- Daily Rentals: This report contains information on all the vehicles rented out during the day. 
-- The entries are grouped by branch, and within each branch, the entries are grouped by vehicle category.
-- The report also displays the number of vehicles rented per category (e.g., 5 sedan rentals, 2 SUV rentals, etc.),
-- the number of rentals at each branch, and the total number of new rentals across the whole company

-- vehicles per category
SELECT v.vtname, CAST(COUNT(v.vlicense) AS VARCHAR(100)) AS NumVehiclesCategory
FROM Rent r
INNER JOIN Vehicle v ON v.vlicense = r.vlicense
WHERE r.fromdateTime LIKE '2019-08-02%'
GROUP BY v.vtname

-- rentals at each branch
SELECT v.location, v.city, v.VTNAME, CAST(COUNT(v.vlicense) AS VARCHAR(100)) AS NumVehiclesBranch
FROM Rent r
         INNER JOIN Vehicle v ON v.vlicense = r.vlicense
WHERE r.DATETIME LIKE '%inputDate%'
GROUP BY v.location, v.city, v.VTNAME

-- total number of new rentals across whole company
SELECT CAST(COUNT(*) AS VARCHAR(100)) AS TotalRentalsAcrossCompany
FROM Rent r
WHERE r.dateTime LIKE '%inputDateTime%'

-- The above works

-- information on all vehicles rented during the day.... @Kohl I dont think we need this
SELECT *
FROM Rent r
INNER JOIN Vehicle v ON v.vlicense = r.vlicense
WHERE r.DATETIME LIKE '%inputDate%'
GROUP BY v.location, v.city, v.vtname                  


-- Daily Rentals for Branch: This is the same as the Daily Rental report but it is 
-- for one specified branch

-- vehicles per category
SELECT COUNT(v.vlicense)
FROM Rent r
INNER JOIN Vehicle v ON v.vlicense = r.vlicense
WHERE EXTRACT(DAY FROM r.date) = inputDate AND v.location = inputLocation AND v.city = inputCity
GROUP BY v.vtname

-- vehicles per branch
SELECT COUNT(v.vlicense)
FROM Rent r
INNER JOIN Vehicle v ON v.vlicense = r.vlicense
WHERE EXTRACT(DAY FROM r.date) = inputDate and v.location = inputLocation and v.city = inputCity
                 
-- information on all vehicles rented during the day
SELECT *
FROM Rent r
INNER JOIN Vehicle v ON v.vlicense = r.vlicense
WHERE EXTRACT(DAY FROM r.date) = inputDate
GROUP BY v.vtname

-- Daily Returns: The report contains information on all the vehicles returned during the day. 
-- The entries are grouped by branch, and within each branch, the entries are grouped by vehicle category. 
-- The report also shows the number of vehicles returned per category, the revenue per category, subtotals 
-- for the number of vehicles and revenue per branch, and the grand totals for the day.

-- number vehicles and revenue per category
SELECT v.vtname, COUNT(v.vlicense) AS NumVehiclesCategory, SUM(re.value) AS CategoryRevenue
FROM [Return] re
INNER JOIN Rent r ON r.rid = re.rid
INNER JOIN Vehicle v ON v.vlicense = r.vlicense
WHERE EXTRACT(DAY FROM re.date) = inputDate
GROUP BY v.vtname

-- subtotals for number of vehicles and revenue per branch
SELECT COUNT(v.vlicense) AS NumVehiclesCategory, SUM(re.value) AS BranchRevenue
FROM [Return] re
INNER JOIN Rent r ON r.rid = re.rid
INNER JOIN Vehicle v ON v.vlicense = r.vlicense
WHERE EXTRACT(DAY FROM re.date) = inputDate
GROUP BY v.location, v.city

-- grand totals for the day
SELECT COUNT(v.vlicense) AS NumVehiclesCategory, SUM(re.value) AS BranchRevenue
FROM [Return] re
INNER JOIN Rent r ON r.rid = re.rid
INNER JOIN Vehicle v ON v.vlicense = r.vlicense
WHERE EXTRACT(DAY FROM re.date) = inputDate

-- information on all vehicles returned during the day
SELECT *
FROM [Return] re
INNER JOIN Rent r ON r.rid = re.rid
INNER JOIN Vehicle v ON v.vlicense = r.vlicense
WHERE EXTRACT(DAY FROM re.date) = inputDate
GROUP BY v.location, v.city, v.vtname  

-- Daily Returns for Branch: This is the same as the Daily Returns report, 
-- but it is for one specified branch.

-- number vehicles and revenue per category
SELECT COUNT(v.vlicense) AS NumVehiclesCategory, SUM(re.value) AS CategoryRevenue
FROM [Return] re
INNER JOIN Rent r ON r.rid = re.rid
INNER JOIN Vehicle v ON v.vlicense = r.vlicense
WHERE EXTRACT(DAY FROM re.date) = inputDate AND v.location = inputLocation AND v.city = inputCity
GROUP BY v.vtname

-- subtotals for number of vehicles and revenue in the branch
SELECT COUNT(v.vlicense) AS NumVehiclesCategory, SUM(re.value) AS BranchRevenue
FROM [Return] re
INNER JOIN Rent r ON r.rid = re.rid
INNER JOIN Vehicle v ON v.vlicense = r.vlicense
WHERE EXTRACT(DAY FROM re.date) = inputDate
GROUP BY v.location, v.city

-- information on all vehicles returned during the day
SELECT *
FROM [Return] re
INNER JOIN Rent r ON r.rid = re.rid
INNER JOIN Vehicle v ON v.vlicense = r.vlicense
WHERE EXTRACT(DAY FROM re.date) = inputDate AND v.location = inputLocation AND v.city = inputCity
GROUP BY v.vtname  
