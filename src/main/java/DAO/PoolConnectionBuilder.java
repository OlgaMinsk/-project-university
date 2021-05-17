package DAO;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class PoolConnectionBuilder implements ConnectionBuilder {
    //  private static DataSource dataSource;
    private DataSource dataSource;
    public PoolConnectionBuilder() throws DAOException {
        try {
            InitialContext initContext = new InitialContext();
            dataSource = (DataSource) initContext.lookup("java:comp/env/jdbc/university");
        } catch (NamingException e) {
            e.printStackTrace();
            throw new DAOException("Unable to find datasource: " + e.getMessage(), e);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
      //  System.out.println(dataSource.toString());
        Connection con = dataSource.getConnection();
       // System.out.println();
     //   System.out.println("connection: " + con.toString());
       // System.out.println();
        return con;
    }
}