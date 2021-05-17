package DAO;

import DTO.StudentDTO;
import DAO.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class StudentDatabaseDAO implements DAO.StudentDAO {
    private static final String INSERT = "insert into student(name, surname) values (?, ?)";
    private static final String UPDATE = "update student set name = ?, surname =? where id_student = ?";
    private static final String DELETE = "delete from student where id_student = ?";
    private static final String GET_PAGE_OF_STUDENTS = "select id_student, name, surname from student limit ? offset ?";
    //private static final String GET_NUMBER_OF_STUDENTS = "select count(*) from student";
    private static final String GET_STUDENT_BY_ID = "select name, surname from student where id_student = ?";
    private PreparedStatement preparedStatementToCreate = null;
    private PreparedStatement preparedStatementToUpdate = null;
    private PreparedStatement preparedStatementToDelete = null;
    private PreparedStatement preparedStatementToGetPageOfStudents = null;
    private PreparedStatement preparedStatementToGetStudentById = null;
    private ConnectionBuilder connectionBuilder=null;
    private Connection connection = null;

    public void setConnectionBuilder(ConnectionBuilder connectionBuilder) {
        if(connectionBuilder!= null) {
            this.connectionBuilder = connectionBuilder;
        }
    }

    protected Connection getConnection() throws SQLException, DAOException {
        if (connection == null) {
            return connectionBuilder.getConnection();
        } else {
            return connection;
        }
    }

    private void createPreparedStatementToCreate() throws DAO.DAOException {
        if (preparedStatementToCreate == null) {
            try {
                connection = getConnection();
                preparedStatementToCreate = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DAO.DAOException("We have problems with create PreparedStatement to create Student", e);
            }
        }
    }

    public void closePreparedStatementToCreate() throws DAO.DAOException {
        if (preparedStatementToCreate != null) {
            try {
                preparedStatementToCreate.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw new DAO.DAOException("We have problems with close PreparedStatement to create Student", e);
            }
        }
    }

    public void create(StudentDTO student) throws DAO.DAOException {
        ResultSet resultSet = null;
        try {
            createPreparedStatementToCreate();
            preparedStatementToCreate.setString(1, student.getName());
            preparedStatementToCreate.setString(2, student.getSurname());
            preparedStatementToCreate.executeUpdate();
            resultSet = preparedStatementToCreate.getGeneratedKeys();
            if (resultSet.next()) {
                student.setId(resultSet.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DAO.DAOException("We have problems with create Student " + student.toString(), e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new DAO.DAOException("We have problems with close resultSet (create Student: " + student.toString(), e);
                }
            }
        }
    }

    private void createPreparedStatementToUpdate() throws DAO.DAOException {
        try {
            if (preparedStatementToUpdate == null) {
                connection = getConnection();
                preparedStatementToUpdate = connection.prepareStatement(UPDATE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DAO.DAOException("We have problems with create PreparedStatement to update Student", e);
        }
    }

    public void closePreparedStatementToUpdate() throws DAO.DAOException {
        if (preparedStatementToUpdate != null) {
            try {
                preparedStatementToUpdate.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw new DAO.DAOException("We have problems with close PreparedStatement to update Student", e);
            }
        }
    }

    public void update(StudentDTO newStudent) throws DAO.DAOException {
        try {
            createPreparedStatementToUpdate();
            preparedStatementToUpdate.setString(1, newStudent.getName());
            preparedStatementToUpdate.setString(2, newStudent.getSurname());
            preparedStatementToUpdate.setInt(3, newStudent.getId());
            preparedStatementToUpdate.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DAO.DAOException("We have problems with update Student", e);
        }
    }

    private void createPreparedStatementToDelete() throws DAO.DAOException {
        if (preparedStatementToDelete == null) {
            try {
                connection = getConnection();
                preparedStatementToDelete = connection.prepareStatement(DELETE);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DAO.DAOException("We have problems with create PreparedStatement to delete Student", e);
            }
        }
    }

    public void closePreraredStatementToDelete() throws DAO.DAOException {
        if (preparedStatementToDelete != null) {
            try {
                preparedStatementToDelete.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw new DAO.DAOException("We have problems with close PreparedStatement to delete Student", e);
            }
        }
    }

    public void delete(Integer id) throws DAO.DAOException {
        try {
            createPreparedStatementToDelete();
            preparedStatementToDelete.setInt(1, id);
            preparedStatementToDelete.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DAO.DAOException("We have problems with delete Student (id = " + id + ")", e);
        }
    }

    private void createPreparedStatementToGetPageOfStudents() throws DAO.DAOException {
        if (preparedStatementToGetPageOfStudents == null) {
            try {
                connection = getConnection();
                preparedStatementToGetPageOfStudents = connection.prepareStatement(GET_PAGE_OF_STUDENTS);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DAO.DAOException("We have problems with create PreparedStatement to get page of Students", e);
            }
        }
    }

    public void closePreparedStatementToGetPageOfStudents() throws DAO.DAOException {
        if (preparedStatementToGetPageOfStudents != null) {
            try {
                preparedStatementToGetPageOfStudents.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw new DAO.DAOException("We have problems with close PreparedStatement to get page of Students", e);
            }
        }
    }


    public Collection<StudentDTO> getPageOfStudents(Integer numberOfStudentsPerPage, Integer pageNumber) throws DAO.DAOException {
        ArrayList<StudentDTO> students = new ArrayList<StudentDTO>();
        ResultSet resultSet = null;
        try {
            createPreparedStatementToGetPageOfStudents();
            preparedStatementToGetPageOfStudents.setInt(1, numberOfStudentsPerPage);
            preparedStatementToGetPageOfStudents.setInt(2, numberOfStudentsPerPage * (pageNumber - 1));
            resultSet = preparedStatementToGetPageOfStudents.executeQuery();

            while (resultSet.next()) {
                StudentDTO student = new StudentDTO();

                student.setId(resultSet.getInt(1));

                student.setName(resultSet.getString(2));

                student.setSurname(resultSet.getString(3));

                students.add(student);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new DAO.DAOException("We have problems with get page of Students", e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new DAO.DAOException("We have problems with close resultSet (get page of Students)", e);
                }
            }
        }
        if (students.size() == 0 && pageNumber != 1) {
            throw new DAO.DAONoSuchPageException("no such page");
        }
        return students;
    }

    private void createPreparedStatementToGetStudentById() throws DAO.DAOException {
        if (preparedStatementToGetStudentById == null) {
            try {
                connection = getConnection();
                preparedStatementToGetStudentById = connection.prepareStatement(GET_STUDENT_BY_ID);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DAO.DAOException("We have problems with create PreparedStatement to get Student by ID", e);

            }
        }
    }

    public void closePreparedStatementToGetStudentById() throws DAO.DAOException {
        if (preparedStatementToGetStudentById != null) {
            try {
                preparedStatementToGetStudentById.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw new DAO.DAOException("We have problems with close PreparedStatement to get Student by ID", e);
            }
        }
    }


    public StudentDTO getStudentById(Integer id) throws DAO.DAOException {
        StudentDTO student = new StudentDTO();
        ResultSet resultSet = null;
        try {
            createPreparedStatementToGetStudentById();
            preparedStatementToGetStudentById.setInt(1, id);
            resultSet = preparedStatementToGetStudentById.executeQuery();

            if (resultSet.next()) {
                student.setId(id);
                student.setName(resultSet.getString(1));
                student.setSurname(resultSet.getString(2));
                return student;

            } else {
                throw new DAO.DAOException("student with Id = " + id + " not found");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new DAO.DAOException("We have problems with get Student by ID", e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new DAO.DAOException("We have problems with close resultSet (get Student by ID)", e);
                }
            }
        }

    }
    public void closeConnection() throws DAOException {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw new DAOException("cannot close the connection (return to Pool from Student)", e);
            }
        }
    }
    public void closeAll() throws DAO.DAOException {
        try {
            closePreparedStatementToCreate();
        } catch (DAO.DAOException e) {
            throw new DAO.DAOException(e);
        } finally {
            closePreparedStatementToUpdate();
            closePreraredStatementToDelete();
            closePreparedStatementToGetPageOfStudents();
            closePreparedStatementToGetStudentById();
            closeConnection();
        }
    }
}


/*
private static void createResultSetToGetNumberOfStudents() throws DAOException {
        try{
            connection = UtilAbstractDAO.getConnection();
            statementToGetNumberOfStudents = connection.createStatement();
            resultSetToGetNumberOfStudents = statementToGetNumberOfStudents.executeQuery(GET_NUMBER_OF_STUDENTS);
        }catch(Exception e){
            e.printStackTrace();
            throw new DAOException(e);
        }
}

public static ResultSet getResultSetToGetNumberOfStudents() throws DAOException {
    createResultSetToGetNumberOfStudents();
    return resultSetToGetNumberOfStudents;
}

public static void closeResultSetToGetNumberOfStudents() throws DAOException{
    if(resultSetToGetNumberOfStudents!=null){
        try{
            resultSetToGetNumberOfStudents.close();
        }catch(Exception e){
            e.printStackTrace();
            throw new DAOException(e);
        }
    }
}

public static void closeStatementToGetNumberOfStudents() throws DAOException{
    if(statementToGetNumberOfStudents!=null){
        try{
            statementToGetNumberOfStudents.close();
        }catch(Exception e){
            e.printStackTrace();
            throw new DAOException(e);
        }
    }
}

public static Collection <StudentDTO> get(){
Statement statement = null;
ResultSet resultSetGetAll=null;

ArrayList<StudentDTO> students = new ArrayList<>();
try{
    Connection connection = UtilStudentDAO.getConnection();
    statement = connection.createStatement();
    resultSetGetAll= statement.executeQuery(GET_ALL);
    while(resultSetGetAll.next()){
        StudentDTO student = new StudentDTO();
        student.setId(resultSetGetAll.getInt(1));
        student.setName(resultSetGetAll.getString(2));
        student.setSurname(resultSetGetAll.getString(3));
        students.add(student);
    }
}catch(Exception e){
  e.printStackTrace();
}
finally{
    if(resultSetGetAll!=null){
        try{
            resultSetGetAll.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    if(statement!=null){
        try{
            statement.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
return students;
}

public static Integer getNumberOfStudents() throws DAOException{
ResultSet resultSet =null;
try{
    resultSet =UtilStudentDAO.getResultSetToGetNumberOfStudents();
    if(resultSet.next()){
        return resultSet.getInt(1);
    }else{
        return null;
        }
}catch(Exception e){
    e.printStackTrace();
    throw new DAOException(e);
}finally{
    if(resultSet!=null){
        try{
            closeResultSetToGetNumberOfStudents();
        }catch(Exception e){
            e.printStackTrace();
            throw new DAOException(e);
        }
    }
        try{
            closeStatementToGetNumberOfStudents();
        }catch(Exception e){
            e.printStackTrace();
            throw new DAOException(e);
        }

}

}

**/


