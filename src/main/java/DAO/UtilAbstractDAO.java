package DAO;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UtilAbstractDAO {
    private static final String USER_NAME = "root";
    private static final String PASS = "123ewq123";
    private static final String URL = "jdbc:mysql://localhost:3306/University?useUnicode=true&serverTimezone=UTC&useSSL=true&verifyServerCertificate=false";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    protected Connection connection = null;

    private void setupConnection() throws DAOException {

        if (this.connection == null) {
            try {
                //  Class.forName(DRIVER);
                //      Driver driver = new FabricMySQLDriver();

                Driver driver = new com.mysql.cj.jdbc.Driver();
                DriverManager.deregisterDriver(driver);
                this.connection = DriverManager.getConnection(URL, USER_NAME, PASS);


            } catch (SQLException e) {
                e.printStackTrace();
                throw new DAOException("connection not established ", e);
            }
        }
    }



    protected Connection getConnection() throws DAOException, SQLException {
        setupConnection();
        return this.connection;
    }

    public void closeConnection() throws DAOException {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw new DAOException("cannot close the connection", e);
            }
        }
    }


}
