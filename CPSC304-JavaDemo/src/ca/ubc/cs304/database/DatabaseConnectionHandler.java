package ca.ubc.cs304.database;
import ca.ubc.cs304.model.ColumnData;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Arrays;
import java.util.List;

/**
 * This class handles all database related transactions
 */
public class DatabaseConnectionHandler {
	private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
	private static final String EXCEPTION_TAG = "[EXCEPTION]";
	private static final String WARNING_TAG = "[WARNING]";
	private static final String pattern = "yyyy-MM-dd";
	private static final DateFormat df = new SimpleDateFormat(pattern);
	private static Integer confNo = 0;
    private static Integer rid = 0;
    private static final LogInCred logInCred = new LogInCred();

	private Connection connection = null;
	
	public DatabaseConnectionHandler() {
		try {
			// Load the Oracle JDBC driver
			// Note that the path could change for new drivers
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}

	public String getReservation(String dlNumber) {
		String ret = "";
		try {
			PreparedStatement stmt = connection.prepareStatement("SELECT confNo FROM Reservations WHERE dlicense = ?");
			stmt.setString(1, dlNumber);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) { // gets the last confNo
				ret = rs.getString(1);
			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			return ret;
		}
		return ret;
	}

	private Integer getConfNo() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(CAST(CONFNO AS INT)) FROM RESERVATIONS");
            rs.next();
            return rs.getInt(1) + 1;
        } catch (SQLException e) {
            return 123456789;
        }
    }

    private Integer getRid() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(RID) FROM RENT");
            rs.next();
            return rs.getInt(1) + 1;
        } catch (SQLException e) {
            return 123456789;
        }
    }

	public void doRentalWithReservation(String confirmation, String dlNumber, String cardNumber) throws SQLException {
		// 1. Get rid
		// 2. get vtname, vlicense, dlicense, fromDateTime, toDateTime from Reservations
		// 3. Generate now dateTime
		// 4. Get MAX(odometer) from Returns. If none, set odometer to 0 (because its a new car)
		// 5. Update to include cardNo
		try {
			rid = (rid == 0) ? getRid() : rid;
			String vtname = "", vlicense = "", dlicense = "", fromDateTime = "", toDateTime = "";
			PreparedStatement ps = connection.prepareStatement("SELECT vtname, vlicense, dlicense, fromDateTime, toDateTime FROM Reservations WHERE confNo = ?");
			ps.setString(1, confirmation);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				vtname = rs.getString(1);
				vlicense = rs.getString(2);
				dlicense = rs.getString(3);
				fromDateTime = rs.getString(4);
				toDateTime = rs.getString(5);
			}
			Date now = new Date(new java.util.Date().getTime());
			ps.close();
			rs.close();
			ps = connection.prepareStatement("SELECT ODOMETER FROM VEHICLE WHERE VLICENSE = ? AND VTNAME = ?");
			ps.setString(1, vlicense);
			ps.setString(2, vtname);
			rs = ps.executeQuery();
			int odometer = (rs.next() ? rs.getInt(1) : 0);
			ps.close();
			rs.close();

			addCardNumberToCards(cardNumber);

            ps = connection.prepareStatement("INSERT INTO Rent VALUES (?,?,?,?,?,?,?,?,?,1)");
			ps.setInt(1, rid);
			rid++;
			ps.setString(2, vlicense);
			ps.setString(3, dlicense);
			ps.setString(4, df.format(now));
			ps.setString(5, fromDateTime);
			ps.setString(6, toDateTime);
			ps.setInt(7, odometer);
			ps.setString(8, cardNumber);
			ps.setString(9, confirmation);
			ps.executeUpdate();
			connection.commit();
			ps.close();
		} catch (SQLException e) {
			rollbackConnection();
			throw e;
		}
	}

	public void registerCustomer(String fullName, String address, Long DLNumber) {
		try {
			PreparedStatement ps = connection.prepareStatement("INSERT INTO Customer VALUES (?,?,?,?)");
			ps.setString(1, DLNumber.toString());
			ps.setNull(2, Types.VARCHAR);
			ps.setString(3, fullName);
			ps.setString(4, address);
			ps.executeUpdate();
			connection.commit();
			ps.close();
		} catch (SQLException e) {
			rollbackConnection();
		}
	}

	public void isRented(String vlicense){
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM Rent r, Vehicle v WHERE " +
					"r.vlicense = v.vlicense and vlicense = ? and reserved = 1");
			ps.setString(1, vlicense);
			ResultSet rs = ps.executeQuery();

			if (!rs.next()) {
				throw new SQLException("Vehicle was not rented.");
			}
			ps.close();
			rs.close();
		} catch (SQLException e) {
			rollbackConnection();
		}
	}

	//retVal[0] = value, retVal[1] = howCalculate
	public ArrayList<String> getRevenue(String vlicense, String dateTimeReturned, String dLicense) throws SQLException, ParseException{
		ArrayList<String> retVal = new ArrayList<>();
		PreparedStatement ps = connection.prepareStatement("SELECT r.fromDateTime AS FromDateTime, vt.wrate AS Wrate" +
				", vt.drate AS Drate, vt.hrate AS Hrate " +
				"FROM Rent r, Vehicle v, VehicleTypes vt WHERE r.vlicense = v.vlicense and v.vtname = " +
				"vt.vtname and v.vlicense = ? and r.DLICENSE = ?");
		ps.setString(1, vlicense);
		ps.setString(2, dLicense);
		ResultSet rs = ps.executeQuery();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		LocalDate fromDate;
		LocalDate toDate;
		int wrate;
		int drate;
		int hrate;
		if (rs.next()) {
			String temp = rs.getString("FromDateTime");
			temp = dateFormat.format(dateFormat.parse(temp));
			formatter = formatter.withLocale( Locale.CANADA );  // Locale specifies human language for translating, and cultural norms for lowercase/uppercase and abbreviations and such. Example: Locale.US or Locale.CANADA_FRENCH
			fromDate = LocalDate.parse(temp, formatter);
			wrate = rs.getInt("Wrate");
			drate = rs.getInt("Drate");
			hrate = rs.getInt("Hrate");
		} else {
			throw new SQLException("Error in computing value");
		}
		ps.close();
		rs.close();

		toDate = LocalDate.parse(dateTimeReturned, formatter);
		if(fromDate.isAfter(toDate)){
			throw new SQLException("Invalid date interval");
		}

//		long weeks = ChronoUnit.WEEKS.between(fromDate, toDate);
//		long days = ChronoUnit.DAYS.between(fromDate,toDate);
//		long hours = days*24;

		long days = ChronoUnit.DAYS.between(fromDate,toDate);
		long weeks = days / 7;
		days = days % 7;
		long hours = 0;

		long value = weeks*wrate + days*drate + hours*hrate;
		String howCalculate = "Calculated using: " + weeks + "(weeks)*" + wrate +
				"(wrate) + " + days + "(days)*" + drate + "(drate) + " + hours + "(hours)*" +
				hrate + "(hrate)";

		retVal.add(Long.toString(value));
		retVal.add(howCalculate);
		return retVal;
	}

	public ArrayList<String> getEstimation(String vtname, String fromDatep, String toDatep) throws SQLException, ParseException{
		ArrayList<String> retVal = new ArrayList<>();
		PreparedStatement ps = connection.prepareStatement("SELECT wrate AS Wrate" +
				", drate AS Drate, hrate AS Hrate " +
				"FROM VehicleTypes WHERE " +
				"vtname = ?");
		ps.setString(1, vtname);
		ResultSet rs = ps.executeQuery();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate fromDate = LocalDate.parse(fromDatep, formatter);
		LocalDate toDate = LocalDate.parse(toDatep, formatter);
		int wrate;
		int drate;
		int hrate;
		if (rs.next()) {
			wrate = rs.getInt("Wrate");
			drate = rs.getInt("Drate");
			hrate = rs.getInt("Hrate");
		} else {
			throw new SQLException("Error in computing value");
		}
		ps.close();
		rs.close();

		if(fromDate.isAfter(toDate)){
			throw new SQLException("Invalid date interval");
		}

//		long weeks = ChronoUnit.WEEKS.between(fromDate, toDate);
//		long days = ChronoUnit.DAYS.between(fromDate,toDate);
//		long hours = days*24;

		long days = ChronoUnit.DAYS.between(fromDate,toDate);
		long weeks = days / 7;
		days = days % 7;
		long hours = 0;

		long value = weeks*wrate + days*drate + hours*hrate;
		String howCalculate = "Calculated using: " + weeks + "(weeks)*" + wrate +
				"(wrate) + " + days + "(days)*" + drate + "(drate) + " + hours + "(hours)*" +
				hrate + "(hrate)";

		retVal.add(Long.toString(value));
		retVal.add(howCalculate);
		return retVal;
	}

	public int getReturnId(String vlicense){
		int rid = -1;
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT rid FROM Rent WHERE vlicense = ?");
			ps.setString(1, vlicense);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				rid = rs.getInt("rid");
			} else {
				throw new SQLException("Error");
			}
			ps.close();
			rs.close();
		} catch (SQLException e) {
			rollbackConnection();
		}
		return rid;
	}

	public void returnVehicle(int rid, String dateTimeReturned, int odometerReading, boolean isTankFull, int value) {
		try {
			PreparedStatement ps = connection.prepareStatement("INSERT INTO \"RETURN\"(rid, dateTime, odometer, fullTank, value)" +
					" VALUES (?,?,?,?,?)");
			ps.setInt(1, rid);
			ps.setString(2, dateTimeReturned);
			ps.setInt(3, odometerReading);
			ps.setInt(4, isTankFull ? 1 : 0);
			ps.setInt(5, value);
			ps.executeUpdate();
			connection.commit();
			ps.close();
		} catch (SQLException e) {
			rollbackConnection();
		}
	}

	public void updateVehicle(String vlicense) {
		try {
			PreparedStatement ps = connection.prepareStatement("UPDATE Vehicle SET reserved = 0 WHERE vlicense = ?");
			ps.setString(1, vlicense);
			ps.executeUpdate();
			connection.commit();
			ps.close();
		} catch (SQLException e) {
			rollbackConnection();
		}
	}

	public ArrayList<ArrayList<String>> dailyReturns(String currDateTime) {
		ArrayList<ArrayList<String>> retObj = new ArrayList<>();
		try {
			//number vehicles and revenue per category
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
			Date now = format.parse(currDateTime);
			String dateString = "%" + df.format(now) + "%";
			PreparedStatement ps = connection.prepareStatement("SELECT v.vtname AS Vtname, COUNT(v.vlicense) AS " +
					"NumVehiclesCategory, SUM(re.value) AS CategoryRevenue FROM \"RETURN\" re INNER JOIN Rent r ON r.rid = re.rid INNER " +
					"JOIN Vehicle v ON v.vlicense = r.vlicense WHERE re.dateTime LIKE ? GROUP BY v.vtname");
			ps.setString(1, dateString);
			ResultSet rs = ps.executeQuery();

			ArrayList<String> numVehiclesAndRevenuePerCat = new ArrayList<>();
			while (rs.next()) {
				numVehiclesAndRevenuePerCat.add(rs.getString("Vtname"));
				numVehiclesAndRevenuePerCat.add(Integer.toString(rs.getInt("NumVehiclesCategory")));
				numVehiclesAndRevenuePerCat.add(Integer.toString(rs.getInt("CategoryRevenue")));
			}
			ps.close();
			rs.close();

			//subtotals for number of vehicles and revenue per branch
			PreparedStatement ps2 = connection.prepareStatement("SELECT v.location AS Location, v.city AS City, COUNT(v.vlicense) " +
					"AS NumVehiclesCategory, SUM(re.value) AS BranchRevenue" +
					"FROM \"RETURN\" re INNER JOIN Rent r ON r.rid = re.rid INNER JOIN Vehicle v ON v.vlicense = r.vlicense" +
					"WHERE re.dateTime = ? GROUP BY v.location, v.city");
			ps.setString(1, currDateTime);
			ResultSet rs2 = ps2.executeQuery();

			ArrayList<String> subNumVehicleAndRevPerBranch = new ArrayList<>();
			while (rs.next()) {
				subNumVehicleAndRevPerBranch.add(rs2.getString("Location"));
				subNumVehicleAndRevPerBranch.add(rs2.getString("City"));
				subNumVehicleAndRevPerBranch.add(Integer.toString(rs2.getInt("NumVehiclesCategory")));
				numVehiclesAndRevenuePerCat.add(Integer.toString(rs2.getInt("BranchRevenue")));
			}
			ps.close();
			rs.close();

			//grand totals for the day
			PreparedStatement ps3 = connection.prepareStatement("SELECT COUNT(rid) AS NumVehicles, SUM(value) AS TotalRevenue" +
					"FROM \"RETURN\" WHERE dateTime = ?");
			ps.setString(1, currDateTime);
			ResultSet rs3 = ps3.executeQuery();

			ArrayList<String> grandTotals = new ArrayList<>();
			while (rs.next()) {
				grandTotals.add(rs3.getString("NumVehicles"));
				grandTotals.add(rs3.getString("TotalRevenue"));
			}
			ps.close();
			rs.close();

			//info on all vehicles
			PreparedStatement ps4 = connection.prepareStatement("SELECT re.rid AS Rid, re.dateTime AS DateTime, re.odometer" +
					"AS Odometer, re.fullTank as FullTank, re.value AS Value FROM \"RETURN\" re INNER JOIN Rent r ON r.rid " +
					"= re.rid INNER JOIN Vehicle v ON v.vlicense = r.vlicense WHERE re.dateTime = ? GROUP BY v.location," +
					"v.city, v.vtname");
			ps.setString(1, currDateTime);
			ResultSet rs4 = ps4.executeQuery();

			ArrayList<String> allVehicles = new ArrayList<>();
			while (rs.next()) {
				allVehicles.add(Integer.toString(rs4.getInt("Rid")));
				allVehicles.add(rs4.getString("DateTime"));
				allVehicles.add(Float.toString(rs4.getFloat("Odometer")));
				allVehicles.add(Float.toString(rs4.getFloat("FullTank")));
				allVehicles.add(Integer.toString(rs4.getInt("Value")));
			}
			ps.close();
			rs.close();

			retObj.add(numVehiclesAndRevenuePerCat);
			retObj.add(subNumVehicleAndRevPerBranch);
			retObj.add(grandTotals);
			retObj.add(allVehicles);

		} catch (SQLException | ParseException e) {
			rollbackConnection();
		}
		return retObj;
	}

	public ArrayList<ArrayList<String>> dailyReturnsBranch(String currDateTime, String location, String city) {
		ArrayList<ArrayList<String>> retObj = new ArrayList<>();
		try {
			//number vehicles and revenue per category
			PreparedStatement ps = connection.prepareStatement("SELECT v.vtname AS Vtname, COUNT(v.vlicense) AS NumVehiclesCategory, SUM(re.value) FROM \"RETURN\" re INNER JOIN Rent r ON r.rid = re.rid INNER JOIN Vehicle v ON v.vlicense = r.vlicense WHERE re.dateTime = ? and v.location = ? and v.city = ? GROUP BY v.vtname");
			ps.setString(1, currDateTime);
			ps.setString(2, location);
			ps.setString(3, city);
			ResultSet rs = ps.executeQuery();

			ArrayList<String> numVehiclesAndRevenuePerCat = new ArrayList<>();
			while (rs.next()) {
				numVehiclesAndRevenuePerCat.add(rs.getString("Vtname"));
				numVehiclesAndRevenuePerCat.add(Integer.toString(rs.getInt("NumVehiclesCategory")));
				numVehiclesAndRevenuePerCat.add(Integer.toString(rs.getInt("CategoryRevenue")));
			}
			ps.close();
			rs.close();

			//subtotals for number of vehicles and revenue per branch
			PreparedStatement ps2 = connection.prepareStatement("SELECT v.location AS Location, v.city AS City, COUNT(v.vlicense) " +
					"AS NumVehiclesCategory, SUM(re.value) AS BranchRevenue" +
					"FROM \"RETURN\" re INNER JOIN Rent r ON r.rid = re.rid INNER JOIN Vehicle v ON v.vlicense = r.vlicense" +
					"WHERE re.dateTime = ? and v.location = ? and v.city = ? GROUP BY v.location, v.city");
			ps.setString(1, currDateTime);
			ps.setString(2, location);
			ps.setString(3, city);
			ResultSet rs2 = ps.executeQuery();

			ArrayList<String> subNumVehicleAndRevPerBranch = new ArrayList<>();
			while (rs.next()) {
				subNumVehicleAndRevPerBranch.add(rs.getString("Location"));
				subNumVehicleAndRevPerBranch.add(rs.getString("City"));
				subNumVehicleAndRevPerBranch.add(Integer.toString(rs.getInt("NumVehiclesCategory")));
				numVehiclesAndRevenuePerCat.add(Integer.toString(rs.getInt("BranchRevenue")));
			}
			ps.close();
			rs.close();

			//grand totals for the day
			PreparedStatement ps3 = connection.prepareStatement("SELECT COUNT(rid) AS NumVehicles, SUM(value) AS TotalRevenue" +
					"FROM \"RETURN\" WHERE dateTime = ?");
			ps.setString(1, currDateTime);
			ResultSet rs3 = ps.executeQuery();

			ArrayList<String> grandTotals = new ArrayList<>();
			while (rs.next()) {
				grandTotals.add(rs.getString("NumVehicles"));
				grandTotals.add(rs.getString("TotalRevenue"));
			}
			ps.close();
			rs.close();

			//info on all vehicles
			PreparedStatement ps4 = connection.prepareStatement("SELECT re.rid AS Rid, re.dateTime AS DateTime, re.odometer" +
					"AS Odometer, re.fullTank as FullTank, re.value AS Value FROM \"RETURN\" re INNER JOIN Rent r ON r.rid " +
					"= re.rid INNER JOIN Vehicle v ON v.vlicense = r.vlicense WHERE re.dateTime = ? GROUP BY v.location," +
					"v.city, v.vtname");
			ps.setString(1, currDateTime);
			ResultSet rs4 = ps.executeQuery();

			ArrayList<String> allVehicles = new ArrayList<>();
			while (rs.next()) {
				allVehicles.add(Integer.toString(rs.getInt("Rid")));
				allVehicles.add(rs.getString("DateTime"));
				allVehicles.add(Float.toString(rs.getFloat("Odometer")));
				allVehicles.add(Float.toString(rs.getFloat("FullTank")));
				allVehicles.add(Integer.toString(rs.getInt("Value")));
			}
			ps.close();
			rs.close();

			retObj.add(numVehiclesAndRevenuePerCat);
			retObj.add(subNumVehicleAndRevPerBranch);
			retObj.add(grandTotals);
			retObj.add(allVehicles);

		} catch (SQLException e) {
			rollbackConnection();
		}
		return retObj;
	}

	public void doRentalNoReservation(String location, String vehicleType, String fromDate,
									  String toDate, String fullName, String dlNumber, String cardNumber) throws SQLException {
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT vid, vlicense FROM Vehicle WHERE vtname = ? AND location = ? AND reserved = 0");
			ps.setString(1, vehicleType);
			ps.setString(2, location);
			ResultSet rs = ps.executeQuery();
			int vid;
			String vlicense;
			if (rs.next()) { // at least 1 row
				vid = rs.getInt(1);
				vlicense = rs.getString(2);
			} else {
				throw new SQLException("No vehicle able to rent.");
			}
			ps.close();
			rs.close();

            int odometer = getOdometerByVid(vid);
            addCardNumberToCards(cardNumber);
            rid = (rid == 0) ? getRid() : rid;
            Date now = new Date(new java.util.Date().getTime());

            ps = connection.prepareStatement("INSERT INTO Rent(RID,VLICENSE,DLICENSE,DATETIME,FROMDATETIME,TODATETIME,ODOMETER,CARDNO,CONFNO,RETURNED) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,0)");
            ps.setInt(1, rid);
            rid++;
            ps.setString(2, vlicense);
            ps.setString(3, dlNumber);
			ps.setString(4, df.format(now));
			ps.setString(5, fromDate);
			ps.setString(6, toDate);
			ps.setInt(7, odometer);
			ps.setString(8, cardNumber);
			ps.setNull(9, Types.VARCHAR);
			ps.executeUpdate();
			connection.commit();

            setVehicleToReserved(vid);

			ps.close();
			rs.close();
		} catch (SQLException e) {
			rollbackConnection();
			throw e;
		}
	}

    private void addCardNumberToCards(String cardNumber) throws SQLException {
        PreparedStatement ps;
        ResultSet rs;
        ps = connection.prepareStatement("SELECT * FROM CARDS WHERE CARDNO = ?");
        ps.setString(1, cardNumber);
        rs = ps.executeQuery();
        if (!rs.next()) {
            PreparedStatement ps2 = connection.prepareStatement("INSERT INTO CARDS VALUES (?,?,?)");
            ps2.setString(1, cardNumber);
            ps2.setNull(2, Types.VARCHAR);
            ps2.setNull(3, Types.VARCHAR);
            ps2.executeUpdate();
            connection.commit();
            ps2.close();
        }
        ps.close();
        rs.close();
    }

    private int getOdometerByVid(int vid) throws SQLException {
        PreparedStatement ps;
        ResultSet rs;
        ps = connection.prepareStatement("SELECT odometer FROM VEHICLE WHERE VID = ?");
        ps.setInt(1, vid);
        rs = ps.executeQuery();
        int odometer = (rs.next() ? rs.getInt(1) : 0);
        ps.close();
        rs.close();
        return odometer;
    }

    private void setVehicleToReserved(int vid) throws SQLException {
        PreparedStatement ps3 = connection.prepareStatement("UPDATE Vehicle SET reserved = 1 WHERE vid = ?");
        ps3.setInt(1, vid);
        ps3.executeUpdate();
        connection.commit();
    }

    public String findVehicles(ArrayList<String> criteria) {
		Integer count = 0;
		try {
			String sql = "SELECT COUNT(*) FROM Vehicle WHERE reserved = 0";
			for (int i = 0; i < criteria.size(); i++) {
				if (i == 0) {
					sql += " AND";
				}
				if (i == criteria.size() - 1) {
					sql += " " + criteria.get(i) + "";
				} else {
					sql += " " + criteria.get(i) + " AND";
				}
			}
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while(rs.next()) {
				count = Integer.parseInt(rs.getString(1));
			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			return "";
		}

		return count.toString();
	}

	public String[][] getVehicles(ArrayList<String> criteria) {
		ArrayList<String[]> toReturn = new ArrayList<>();
		try {
			String sql = "SELECT v.make, v.model, v.year, v.color, g.gasType, v.vtname, v.LOCATION, v.CITY " +
						"FROM Vehicle v NATURAL JOIN GasType g WHERE v.reserved = 0";
			for (int i = 0; i < criteria.size(); i++) {
				if (i == 0) {
					sql += " AND";
				}
				if (i == criteria.size() - 1) {
					sql += " " + criteria.get(i);
				} else {
					sql += " " + criteria.get(i) + " AND";
				}
			}
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			// get info on ResultSet
			ResultSetMetaData rsmd = rs.getMetaData();


			while(rs.next()) {
				String[] myResult = {rs.getString(1), rs.getString(2),
				rs.getString(3), rs.getString(4), rs.getString(5),
				rs.getString(6), rs.getString(7), rs.getString(8)};
				toReturn.add(myResult);
			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			String[] empty = {""};
			toReturn.set(0, empty);
			return arrayListToStringArray(toReturn);
		}

		return arrayListToStringArray(toReturn);
	}

	private String[][] arrayListToStringArray(ArrayList<String[]> list) {
		String[][] toReturn = new String[list.size()][];
		for (int i = 0; i < list.size(); i++) {
			toReturn[i] = list.get(i);
		}
		return toReturn;
	}

	public int reserveVehicle(String location, String vehicleType, String fromDateTime,
							  String toDateTime, String customerName, Long customerDL) {
		try {
			PreparedStatement ps = connection.prepareStatement("INSERT INTO Reservations VALUES (?,?,?,?,?,?)");
			PreparedStatement ps2 = connection.prepareStatement("SELECT vlicense FROM Vehicle WHERE reserved = 0 AND vtname = ?");
			ps2.setString(1, vehicleType);
			ResultSet rs = ps2.executeQuery();
            rs.next();
            confNo = (confNo == 0) ? getConfNo() : confNo;
            confNo++;
			ps.setString(1, confNo.toString());
			ps.setString(2, vehicleType);
			String vlicense = rs.getString(1);
			ps.setString(3, vlicense); // vlicense
			ps.setString(4, customerDL.toString());
			ps.setString(5, fromDateTime); // from
			ps.setString(6, toDateTime); // to
            ps2.close();

            ps2 = connection.prepareStatement("SELECT * FROM Customer WHERE dlicense = ?");
            ps2.setString(1, customerDL.toString());
			rs = ps2.executeQuery();
			if (!rs.next()) {
				PreparedStatement ps3 = connection.prepareStatement("INSERT INTO Customer VALUES (?,?,?,?)");
				ps3.setString(1, customerDL.toString());
				ps3.setNull(2, Types.VARCHAR);
				ps3.setString(3, customerName);
				ps3.setNull(4, Types.VARCHAR);
				ps3.executeUpdate();
				ps3.close();
			}
			// UPDATE CAR TO BE RESERVED, ASSUMES VLICENSE IS UNIQUE
            ps2.close();
			ps2 = connection.prepareStatement("UPDATE Vehicle SET reserved = 1 WHERE vlicense = ?");
			ps2.setString(1, vlicense);

			ps2.executeUpdate();
			ps.executeUpdate();
			connection.commit();

			ps.close();
			ps2.close();
			rs.close();
		} catch (SQLException e) {
			rollbackConnection();
			return -1;
		}
		return confNo;
	}
	
	public void updateBranch(int id, String name) {
		try {
		  PreparedStatement ps = connection.prepareStatement("UPDATE branch SET branch_name = ? WHERE branch_id = ?");
		  ps.setString(1, name);
		  ps.setInt(2, id);
		
		  int rowCount = ps.executeUpdate();
		  if (rowCount == 0) {
		      System.out.println(WARNING_TAG + " Branch " + id + " does not exist!");
		  }
	
		  connection.commit();
		  
		  ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}	
	}
	
	public boolean login() {
		try {
			if (connection != null) {
				connection.close();
			}

			JFrame loading = new JFrame();
			JLabel loadingLabel = new JLabel("Loading... Please wait.");
			loading.setTitle("SuperRent");
			JPanel contentPane = new JPanel();
			loading.setContentPane(contentPane);
			GridBagLayout gb = new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();
			contentPane.setLayout(gb);
			contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			c.gridwidth = GridBagConstraints.RELATIVE;
			c.insets = new Insets(25, 25, 25, 25);
			c.anchor = GridBagConstraints.CENTER;
			gb.setConstraints(loadingLabel, c);
			contentPane.add(loadingLabel);
			loading.pack();
			Dimension d = loading.getToolkit().getScreenSize();
			Rectangle r = loading.getBounds();
			loading.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );
			loading.setVisible(true);

			connection = DriverManager.getConnection(ORACLE_URL, logInCred.getUsername(), logInCred.getPassword());
			connection.setAutoCommit(false);

			loading.dispose();
			System.out.println("\nConnected to Oracle!");
			return true;
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			return false;
		}
	}

	private void rollbackConnection() {
		try  {
			connection.rollback();	
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}

	public ColumnData[] generateDailyRentalReport() throws SQLException {
		ColumnData[] toReturn = new ColumnData[3];
		java.util.Date now = new java.util.Date();
		String dateString = "%" + df.format(now) + "%";

		PreparedStatement ps1 = connection.prepareStatement("SELECT v.vtname AS Vehicle_Type, CAST(COUNT(v.vlicense) AS VARCHAR(100)) AS Number_Of_Vehicles " +
				"FROM Rent r " +
				"INNER JOIN Vehicle v ON v.vlicense = r.vlicense " +
				"WHERE r.FROMDATETIME LIKE ? " +
				"GROUP BY v.vtname");
		ps1.setString(1, dateString);
		ResultSet vehiclePerCat = ps1.executeQuery();
		ResultSetMetaData vehiclePerCatMetaData = vehiclePerCat.getMetaData();

		String sql = "SELECT v.location, v.city, v.VTNAME AS Vehicle_Type, CAST(COUNT(v.vlicense) AS VARCHAR(100)) AS Number_Of_Vehicles " +
				"FROM Rent r " +
				"INNER JOIN Vehicle v ON v.vlicense = r.vlicense " +
				"WHERE r.FROMDATETIME LIKE ? " +
				"GROUP BY v.location, v.city, v.VTNAME";
		PreparedStatement ps2 = connection.prepareStatement(sql);
		ps2.setString(1, dateString);
		ResultSet rentalBranch = ps2.executeQuery();
		ResultSetMetaData rentalBranchMetaData = rentalBranch.getMetaData();

		sql = "SELECT CAST(COUNT(*) AS VARCHAR(100)) AS Total_Rentals_Across_Company " +
				"FROM Rent r " +
				"WHERE r.FROMDATETIME LIKE ?";
		PreparedStatement ps3 = connection.prepareStatement(sql);
		ps3.setString(1, dateString);
		ResultSet newRentalTotal = ps3.executeQuery();
		ResultSetMetaData newRentalMetaData = newRentalTotal.getMetaData();

		ResultSetMetaData[] allMetaData = {vehiclePerCatMetaData, rentalBranchMetaData, newRentalMetaData};
		ResultSet[] allResults = {vehiclePerCat, rentalBranch, newRentalTotal};
		for (int i = 0; i < 3; i++) {
			int numColumns = allMetaData[i].getColumnCount();
			String[] columnNames = new String[numColumns];
			for (int j = 0; j < numColumns; j++) {
				columnNames[j] = allMetaData[i].getColumnName(j+1);
			}
			ResultSet rs = allResults[i];
			ArrayList<String[]> dataArr = new ArrayList<>();
			while (rs.next()) {
				String[] tuple = new String[numColumns];
				for (int j = 0; j < numColumns; j++) {
					tuple[j] = rs.getString(j+1);
				}
				dataArr.add(tuple);
			}
			ColumnData columnData = new ColumnData(columnNames, arrayListToStringArray(dataArr));
			toReturn[i] = columnData;
		}
		ps1.close();
		ps2.close();
		ps3.close();
		vehiclePerCat.close();
		rentalBranch.close();
		newRentalTotal.close();
		return toReturn;
	}

	public ColumnData[] generateDailyReturnsReport() throws SQLException {
		ColumnData[] toReturn = new ColumnData[3];
		java.util.Date now = new java.util.Date();
		String dateString = "%" + df.format(now) + "%";

		PreparedStatement ps1 = connection.prepareStatement("SELECT v.vtname AS Vehicle_Type, CAST(COUNT(v.vlicense) AS VARCHAR(100)) AS Number_Of_Vehicles, SUM(re.value) AS Revenue " +
				"FROM Return re " +
				"INNER JOIN Rent r ON r.rid = re.rid " +
				"INNER JOIN Vehicle v ON v.vlicense = r.vlicense " +
				"WHERE r.FROMDATETIME LIKE ? " +
				"GROUP BY v.vtname");
		ps1.setString(1, dateString);
		ResultSet vehiclePerCat = ps1.executeQuery();
		ResultSetMetaData vehiclePerCatMetaData = vehiclePerCat.getMetaData();

		String sql = "SELECT v.location, v.city, CAST(COUNT(v.vlicense) AS VARCHAR(100)) AS Number_Of_Vehicles, SUM(re.value) AS Revenue_Of_Branch " +
				"FROM Return re " +
				"INNER JOIN Rent r ON r.rid = re.rid " +
				"INNER JOIN Vehicle v ON v.vlicense = r.vlicense " +
				"WHERE r.FROMDATETIME LIKE ? " +
				"GROUP BY v.location, v.city";
		PreparedStatement ps2 = connection.prepareStatement(sql);
		ps2.setString(1, dateString);
		ResultSet rentalBranch = ps2.executeQuery();
		ResultSetMetaData rentalBranchMetaData = rentalBranch.getMetaData();

		sql = "SELECT CAST(COUNT(*) AS VARCHAR(100)) AS Number_Of_Vehicles, SUM(re.value) AS Grand_Total_Revenue " +
				"FROM Return re " +
				"INNER JOIN Rent r ON r.rid = re.rid " +
				"INNER JOIN Vehicle v ON v.vlicense = r.vlicense " +
				"WHERE r.FROMDATETIME LIKE ?";
		PreparedStatement ps3 = connection.prepareStatement(sql);
		ps3.setString(1, dateString);
		ResultSet newRentalTotal = ps3.executeQuery();
		ResultSetMetaData newRentalMetaData = newRentalTotal.getMetaData();

		ResultSetMetaData[] allMetaData = {vehiclePerCatMetaData, rentalBranchMetaData, newRentalMetaData};
		ResultSet[] allResults = {vehiclePerCat, rentalBranch, newRentalTotal};
		for (int i = 0; i < 3; i++) {
			int numColumns = allMetaData[i].getColumnCount();
			String[] columnNames = new String[numColumns];
			for (int j = 0; j < numColumns; j++) {
				columnNames[j] = allMetaData[i].getColumnName(j+1);
			}
			ResultSet rs = allResults[i];
			ArrayList<String[]> dataArr = new ArrayList<>();
			while (rs.next()) {
				String[] tuple = new String[numColumns];
				for (int j = 0; j < numColumns; j++) {
					tuple[j] = rs.getString(j+1);
				}
				dataArr.add(tuple);
			}
			ColumnData columnData = new ColumnData(columnNames, arrayListToStringArray(dataArr));
			toReturn[i] = columnData;
		}
		ps1.close();
		ps2.close();
		ps3.close();
		vehiclePerCat.close();
		rentalBranch.close();
		newRentalTotal.close();
		return toReturn;
	}

	public ColumnData[] generateDailyRentalReportByBranch(String city, String location) throws SQLException {
		ColumnData[] toReturn = new ColumnData[3];
		java.util.Date now = new java.util.Date();
		String dateString = "%" + df.format(now) + "%";

		PreparedStatement ps1 = connection.prepareStatement("SELECT v.vtname AS Vehicle_Type, CAST(COUNT(v.vlicense) AS VARCHAR(100)) AS Number_Of_Vehicle " +
				"FROM Rent r " +
				"INNER JOIN Vehicle v ON v.vlicense = r.vlicense " +
				"WHERE r.FROMDATETIME LIKE ? AND v.CITY = ? AND v.LOCATION = ? " +
				"GROUP BY v.vtname");
		ps1.setString(1, dateString);
		ps1.setString(2, city);
		ps1.setString(3, location);
		ResultSet vehiclePerCat = ps1.executeQuery();
		ResultSetMetaData vehiclePerCatMetaData = vehiclePerCat.getMetaData();

		PreparedStatement ps2 = connection.prepareStatement("SELECT v.VTNAME AS Vehicle_Type, CAST(COUNT(v.vlicense) AS VARCHAR(100)) AS Number_Of_Vehicle " +
				"FROM Rent r " +
				"INNER JOIN Vehicle v ON v.vlicense = r.vlicense " +
				"WHERE r.FROMDATETIME LIKE ? AND v.CITY = ? AND v.LOCATION = ? " +
				"GROUP BY v.location, v.city, v.VTNAME");
		ps2.setString(1, dateString);
		ps2.setString(2, city);
		ps2.setString(3, location);
		ResultSet rentalBranch = ps2.executeQuery();
		ResultSetMetaData rentalBranchMetaData = rentalBranch.getMetaData();

		PreparedStatement ps3 = connection.prepareStatement("SELECT CAST(COUNT(*) AS VARCHAR(100)) AS Total_Rentals_From_Branch " +
				"FROM Rent r " +
				"INNER JOIN Vehicle v ON v.vlicense = r.vlicense " +
				"WHERE r.FROMDATETIME LIKE ? AND v.CITY = ? AND v.LOCATION = ?");
		ps3.setString(1, dateString);
		ps3.setString(2, city);
		ps3.setString(3, location);
		ResultSet newRentalTotal = ps3.executeQuery();
		ResultSetMetaData newRentalMetaData = newRentalTotal.getMetaData();

		ResultSetMetaData[] allMetaData = {vehiclePerCatMetaData, rentalBranchMetaData, newRentalMetaData};
		ResultSet[] allResults = {vehiclePerCat, rentalBranch, newRentalTotal};
		for (int i = 0; i < 3; i++) {
			int numColumns = allMetaData[i].getColumnCount();
			String[] columnNames = new String[numColumns];
			for (int j = 0; j < numColumns; j++) {
				columnNames[j] = allMetaData[i].getColumnName(j+1);
			}
			ResultSet rs = allResults[i];
			ArrayList<String[]> dataArr = new ArrayList<>();
			while (rs.next()) {
				String[] tuple = new String[numColumns];
				for (int j = 0; j < numColumns; j++) {
					tuple[j] = rs.getString(j+1);
				}
				dataArr.add(tuple);
			}
			ColumnData columnData = new ColumnData(columnNames, arrayListToStringArray(dataArr));
			toReturn[i] = columnData;
		}
		return toReturn;
	}

	public ColumnData[] generateDailyReturnsReportByBranch(String city, String location) throws SQLException {
		ColumnData[] toReturn = new ColumnData[3];
		java.util.Date now = new java.util.Date();
		String dateString = "%" + df.format(now) + "%";

		PreparedStatement ps1 = connection.prepareStatement("SELECT v.vtname AS Vehicle_Type, CAST(COUNT(v.vlicense) AS VARCHAR(100)) AS Number_Of_Vehicles, SUM(re.value) AS Revenue " +
				"FROM Return re " +
				"INNER JOIN Rent r ON r.rid = re.rid " +
				"INNER JOIN Vehicle v ON v.vlicense = r.vlicense " +
				"WHERE r.FROMDATETIME LIKE ? AND v.location = ? AND v.city = ? " +
				"GROUP BY v.vtname");
		ps1.setString(1, "%2019%");
		ps1.setString(2, location);
		ps1.setString(3, city);
		ResultSet vehiclePerCat = ps1.executeQuery();
		ResultSetMetaData vehiclePerCatMetaData = vehiclePerCat.getMetaData();

		PreparedStatement ps2 = connection.prepareStatement("SELECT v.location, v.city, CAST(COUNT(v.vlicense) AS VARCHAR(100)) AS Number_Of_Vehicles, SUM(re.value) AS Revenue_Of_Branch " +
				"FROM Return re " +
				"INNER JOIN Rent r ON r.rid = re.rid " +
				"INNER JOIN Vehicle v ON v.vlicense = r.vlicense " +
				"WHERE r.FROMDATETIME LIKE ? AND v.location = ? AND v.city = ? " +
				"GROUP BY v.location, v.city");
		ps2.setString(1, "%2019%");
		ps2.setString(2, location);
		ps2.setString(3, city);
		ResultSet rentalBranch = ps2.executeQuery();
		ResultSetMetaData rentalBranchMetaData = rentalBranch.getMetaData();

		PreparedStatement ps3 = connection.prepareStatement("SELECT CAST(COUNT(*) AS VARCHAR(100)) AS Number_Of_Vehicles, SUM(re.value) AS Grand_Total_Revenue_From_Branch " +
				"FROM Return re " +
				"INNER JOIN Rent r ON r.rid = re.rid " +
				"INNER JOIN Vehicle v ON v.vlicense = r.vlicense " +
				"WHERE r.FROMDATETIME LIKE ? AND v.location = ? AND v.city = ?");
		ps3.setString(1, "%2019%");
		ps3.setString(2, location);
		ps3.setString(3, city);
		ResultSet newRentalTotal = ps3.executeQuery();
		ResultSetMetaData newRentalMetaData = newRentalTotal.getMetaData();

		ResultSetMetaData[] allMetaData = {vehiclePerCatMetaData, rentalBranchMetaData, newRentalMetaData};
		ResultSet[] allResults = {vehiclePerCat, rentalBranch, newRentalTotal};
		for (int i = 0; i < 3; i++) {
			int numColumns = allMetaData[i].getColumnCount();
			String[] columnNames = new String[numColumns];
			for (int j = 0; j < numColumns; j++) {
				columnNames[j] = allMetaData[i].getColumnName(j+1);
			}
			ResultSet rs = allResults[i];
			ArrayList<String[]> dataArr = new ArrayList<>();
			while (rs.next()) {
				String[] tuple = new String[numColumns];
				for (int j = 0; j < numColumns; j++) {
					tuple[j] = rs.getString(j+1);
				}
				dataArr.add(tuple);
			}
			ColumnData columnData = new ColumnData(columnNames, arrayListToStringArray(dataArr));
			toReturn[i] = columnData;
		}
		ps1.close();
		ps2.close();
		ps3.close();
		vehiclePerCat.close();
		rentalBranch.close();
		newRentalTotal.close();
		return toReturn;
	}

	// Returns and arraylist of all of the table names
	// TODO: @Ryan you can use this to get a list of the available tables for your view.
	public ArrayList<String> getTableNames() throws SQLException {
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT table_name FROM user_tables ORDER BY table_name");
		ArrayList<String> tableNames = new ArrayList<>();
		while (rs.next()) {
			tableNames.add(rs.getString(1));
		}
		return tableNames;
	}

	// TODO: @Ryan hook these up to UI where appropriate
	// ASSUMES: all items in args are formatted properly (i.e. String are like 'string', integer are like 0) AND
	// assumes tableName is NOT formatted (i.e. no quotes)
	// Returns true if successful, false otherwise
	public boolean insertDataIntoTable(String tableName, String values) throws SQLException {
		String sql = "INSERT INTO " + tableName + " VALUES (";
		sql += values + ")";
		Statement stmt = connection.createStatement();
		stmt.executeUpdate(sql);
		connection.commit();
		stmt.close();
		return true;
	}

	// ASSUMES: args are formatted like "vid = 123" or "name = 'Kohl'" as appropriate
	// assumes tableName is NOT formatted (i.e. No quotes)
	public boolean deleteDataFromTable(String tableName, String conditions) throws SQLException {
		String sql = "DELETE FROM " + tableName;
		List<String> conditionList = Arrays.asList(conditions.split(","));

		for (int i = 0; i < conditionList.size(); i++) {
			if (i == 0) {
				sql += " WHERE";
			} else {
				sql += " AND";
			}
			sql += " " + conditionList.get(i);
		}
		Statement stmt = connection.createStatement();
		stmt.executeUpdate(sql);
		connection.commit();
		stmt.close();
		return true;
	}

	// Returns a columnData object with columns set to column name and data set to the data of the table
	// if table not found throws SQLException
	public ColumnData viewTable(String tableName) throws SQLException {
		String sql = "SELECT * FROM " + tableName;
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		ResultSetMetaData md = rs.getMetaData();

		int numColumns = md.getColumnCount();
		String[] columnNames = new String[numColumns];
		for (int j = 0; j < numColumns; j++) {
			columnNames[j] = md.getColumnName(j+1);
		}
		ArrayList<String[]> dataArr = new ArrayList<>();
		while (rs.next()) {
			String[] tuple = new String[numColumns];
			for (int j = 0; j < numColumns; j++) {
				tuple[j] = rs.getString(j+1);
			}
			dataArr.add(tuple);
		}
		ColumnData columnData = new ColumnData(columnNames, arrayListToStringArray(dataArr));
		return columnData;
	}
}
