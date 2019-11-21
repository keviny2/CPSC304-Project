package ca.ubc.cs304.database;

import ca.ubc.cs304.model.BranchModel;
import oracle.jdbc.proxy.annotation.Pre;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This class handles all database related transactions
 */
public class DatabaseConnectionHandler {
	private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
	private static final String EXCEPTION_TAG = "[EXCEPTION]";
	private static final String WARNING_TAG = "[WARNING]";
	
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

	public int getReservation(String dlNumber) {
		int ret = 0;
		try {
			PreparedStatement stmt = connection.prepareStatement("SELECT confNo FROM Reservations WHERE dlicense = ?");
			stmt.setString(1, dlNumber);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) { // gets the last confNo
				ret = rs.getInt(1);
			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			return ret;
		}
		return ret;
	}

	public void doRentalWithReservation(Integer confirmation, String dlNumber, Date fromDate, Date toDate) {
		// 1. Get rid (hash dlicense)
		// 2. get confNo, vtname, vlicense, dlicense from Reservations
		// 3. Generate now dateTime
		// 4. Get MAX(odometer) from Returns. If none, set odometer to 0 (because its a new car)
		// 5. Update to include cardNo
		int rid = Objects.hash(dlNumber);
		String vtname, vlicense, dlicense;
		PreparedStatement ps = connection.prepareStatement("SELECT vtname, vlicense, dlicense FROM Reservations WHERE confNo = ?");
		ps.setInt(1, confirmation);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			vtname = rs.getString(1);
			vlicense = rs.getString(2);
			dlicense = rs.getString(3);
		}
		Date now = new Date(new java.util.Date().getTime());
		ps.close();
		rs.close();
		// NATURAL JOIN r re doesnt work (odometer!)
		// TODO: Finish
	}

	public void doRentalNoReservation(String location, String vehicleType, Date fromDate,
									  Date toDate, String fullName, String dlNumber) throws SQLException {
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
			ps = connection.prepareStatement("UPDATE Vehicle SET reserved = 1 WHERE vid = ?");
			ps.setInt(1, vid);
			ps.executeUpdate();
			connection.commit();
			ps.close();

			ps = connection.prepareStatement("SELECT MAX(r.odometer) FROM Return r NATURAL JOIN Rent re WHERE re.vlicense = ?");
			ps.setString(1, vlicense);
			rs = ps.executeQuery();
			int odometer = (rs.next() ? rs.getInt(1) : 0);
			ps.close();

			ps = connection.prepareStatement("INSERT INTO Rent VALUES (?,?,?,?,?,?,?,?,?)");
			ps.setInt(1, Objects.hash(dlNumber));
			ps.setString(2, vlicense);
			Date now = new Date(new java.util.Date().getTime());
			ps.setDate(3, now);
			ps.setDate(4, fromDate);
			ps.setDate(5, toDate);
			ps.setInt(6, odometer);
			ps.setString(7, "CARD NUMBER PLS"); // TODO: Card number.
			ps.executeUpdate();
			connection.commit();

			ps.close();
			rs.close();

		} catch (SQLException e) {
			rollbackConnection();
			throw e;
		}
	}

	public String findVehicles(ArrayList<String> criteria) {
		Integer count = 0;
		try {
			String sql = "SELECT COUNT(*) FROM Vehicle";
			for (int i = 0; i < criteria.size(); i++) {
				if (i == 0) {
					sql += " WHERE";
				}
				if (i == criteria.size() - 1) {
					sql += " " + criteria;
				} else {
					sql += " " + criteria + " AND";
				}
			}
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			// get info on ResultSet
			ResultSetMetaData rsmd = rs.getMetaData();

			while(rs.next()) {
				count++;
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
			String sql = "SELECT v.make, v.model, v.year, v.color, g.gasType, v.vtname" +
						"FROM Vehicle NATURAL JOIN GasType";
			for (int i = 0; i < criteria.size(); i++) {
				if (i == 0) {
					sql += " WHERE";
				}
				if (i == criteria.size() - 1) {
					sql += " " + criteria;
				} else {
					sql += " " + criteria + " AND";
				}
			}
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			// get info on ResultSet
			ResultSetMetaData rsmd = rs.getMetaData();


			while(rs.next()) {
				String[] myResult = {rs.getString(1), rs.getString(2),
				rs.getString(3), rs.getString(4), rs.getString(5),
				rs.getString(6), rs.getString(7)};
				toReturn.add(myResult);
			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			String[] empty = {""};
			toReturn.set(0, empty);
			return (String[][]) toReturn.toArray();
		}

		return (String[][]) toReturn.toArray();
	}

	public int reserveVehicle(Integer confNo, String location, String vehicleType, Date fromDateTime,
							  Date toDateTime, String customerName, Long customerDL) {
		try {
			PreparedStatement ps = connection.prepareStatement("INSERT INTO Reservations VALUES (?,?,?,?,?)");
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT vlicense FROM Vehicle WHERE reserved = 0 AND vtname = " + vehicleType);

			ps.setString(1, confNo.toString());
			ps.setString(2, vehicleType);
			ps.setString(3, rs.getString(1)); // vlicense
			ps.setString(4, customerDL.toString());
			ps.setDate(5, fromDateTime); // from
			ps.setDate(6, toDateTime); // to

			rs = stmt.executeQuery("SELECT * FROM Customer WHERE dlicense = " + customerDL.toString());
			if (!rs.next()) {
				PreparedStatement ps2 = connection.prepareStatement("INSERT INTO Customer VALUES (?,?,?,?)");
				ps2.setString(1, customerDL.toString());
				ps2.setNull(2, Types.VARCHAR);
				ps2.setString(3, customerName);
				ps2.setNull(4, Types.VARCHAR);
				ps2.executeUpdate();
			}

			ps.executeUpdate();
			connection.commit();

			ps.close();
			stmt.close();
			rs.close();
		} catch (SQLException e) {
			rollbackConnection();
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
}
