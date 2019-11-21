package ca.ubc.cs304.database;

import ca.ubc.cs304.model.BranchModel;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This class handles all database related transactions
 */
public class DatabaseConnectionHandler {
	private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
	private static final String EXCEPTION_TAG = "[EXCEPTION]";
	private static final String WARNING_TAG = "[WARNING]";
	private static final String pattern = "YYYY-MM-DD";
	private static final DateFormat df = new SimpleDateFormat(pattern);
	private static Integer confNo = 0;
    private static Integer rid = 0;
	
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
	
	public void close() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}

	public void deleteBranch(int branchId) {
		try {
			PreparedStatement ps = connection.prepareStatement("DELETE FROM branch WHERE branch_id = ?");

			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				System.out.println(WARNING_TAG + " Branch " + branchId + " does not exist!");
			}
			
			connection.commit();
	
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
	}
	
	public void insertBranch(BranchModel model) {
		try {
			PreparedStatement ps = connection.prepareStatement("INSERT INTO branch VALUES (?,?,?,?,?)");
			ps.setInt(1, model.getId());
			ps.setString(2, model.getName());
			ps.setString(3, model.getAddress());
			ps.setString(4, model.getCity());
			if (model.getPhoneNumber() == 0) {
				ps.setNull(5, java.sql.Types.INTEGER);
			} else {
				ps.setInt(5, model.getPhoneNumber());
			}

			ps.executeUpdate();
			connection.commit();

			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
	}
	
	public BranchModel[] getBranchInfo() {
		ArrayList<BranchModel> result = new ArrayList<BranchModel>();
		
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM branch");
		
    		// get info on ResultSet
    		ResultSetMetaData rsmd = rs.getMetaData();

    		System.out.println(" ");

    		// display column names;
    		for (int i = 0; i < rsmd.getColumnCount(); i++) {
    			// get column name and print it
    			System.out.printf("%-15s", rsmd.getColumnName(i + 1));
    		}
			
			while(rs.next()) {
				BranchModel model = new BranchModel(rs.getString("address"),
													rs.getString("city"),
													rs.getInt("id"),
													rs.getString("name"),
													rs.getInt("phonenumber"));
				result.add(model);
			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}	
		
		return result.toArray(new BranchModel[result.size()]);
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

            ps = connection.prepareStatement("INSERT INTO Rent VALUES (?,?,?,?,?,?,?,?,?)");
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

            ps = connection.prepareStatement("INSERT INTO Rent(RID,VLICENSE,DLICENSE,DATETIME,FROMDATETIME,TODATETIME,ODOMETER,CARDNO,CONFNO) " +
                    "VALUES (?,?,?,?,?,?,?,?,?)");
            ps.setInt(1, rid);
            rid++;
            ps.setString(2, vlicense);
            ps.setString(3, dlNumber);
			ps.setString(4, now.toString());
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
				count = rs.getInt(1);
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
			String sql = "SELECT v.make, v.model, v.year, v.color, g.gasType, v.vtname " +
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
				rs.getString(6)};
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
	
	public boolean login(String username, String password) {
		try {
			if (connection != null) {
				connection.close();
			}
	
			connection = DriverManager.getConnection(ORACLE_URL, username, password);
			connection.setAutoCommit(false);
	
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

	public String[] generateDailyRentalReport() {
		try {
			Statement stmt1 = connection.createStatement();
			Statement stmt2 = connection.createStatement();
			Statement stmt3 = connection.createStatement();
			Statement stmt4 = connection.createStatement();
			ResultSet vehiclePerCat = stmt1.executeQuery("SELECT COUNT(v.vlicense) AS NumVehiclesCategory\n" +
													"FROM Rent r\n" +
													"INNER JOIN Vehicle v ON v.vlicense = r.vlicense\n" +
													"WHERE EXTRACT(DAY FROM r.date) = inputDate\n" +
													"GROUP BY v.vtname");
			ResultSetMetaData vehiclePerCatMetaData = vehiclePerCat.getMetaData();
			ResultSet resAtEachBranch = stmt2.executeQuery("SELECT COUNT(v.vlicense) AS NumVehiclesBranch\n" +
													"FROM Rent r\n" +
													"INNER JOIN Vehicle v ON v.vlicense = r.vlicense\n" +
													"WHERE EXTRACT(DAY FROM r.date) = inputDate\n" +
													"GROUP BY v.location, v.city");
			ResultSetMetaData resultSetMetaData = resAtEachBranch.getMetaData();
			ResultSet newRentals = stmt3.executeQuery("SELECT COUNT(*)\n" +
															"FROM Rent r\n" +
															"WHERE r.date = inputDateTime");
			ResultSetMetaData newRentalsMetaData = newRentals.getMetaData();
			ResultSet dailyRental = stmt4.executeQuery("SELECT *\n" +
															"FROM Rent r\n" +
															"INNER JOIN Vehicle v ON v.vlicense = r.vlicense\n" +
															"WHERE EXTRACT(DAY FROM r.date) = inputDate\n" +
															"GROUP BY v.location, v.city, v.vtname");
			ResultSetMetaData dailyRentalMd = dailyRental.getMetaData();
			// TODO
		} catch (SQLException e) {
			// TODO
		}
		String myStr = "";
		String[] arr = {myStr};
		return arr;
	}
}
