package DAO;

import DTO.SubjectDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class SubjectDatabaseDAO implements SubjectDAO {
    private static final String INSERT = "insert into subject(subject) values (?)";
    private static final String UPDATE = "update subject set subject = ? where id_subject = ?";
    private static final String DELETE = "delete from subject where id_subject = ?";
    private static final String GET_PAGE_OF_SUBJECTS = "select id_subject, subject from subject limit ? offset ?";
    private static final String GET_SUBJECT_BY_ID = "select subject from subject where id_subject = ?";
    private PreparedStatement preparedStatementToCreate = null;
    private PreparedStatement preparedStatementToUpdate = null;
    private PreparedStatement preparedStatementToDelete = null;
    private PreparedStatement preparedStatementToGetPageOfSubjects = null;
    private PreparedStatement preparedStatementToGetSubjectById = null;
    private ConnectionBuilder connectionBuilder;
    private Connection connection = null;

    public void setConnectionBuilder(ConnectionBuilder connectionBuilder) {
        if(connectionBuilder!= null) {
            this.connectionBuilder = connectionBuilder;
        }
    }

    protected Connection getConnection() throws SQLException, DAOException {
        if (connection == null) {
            return connectionBuilder.getConnection();
        }else{
            return connection;
        }
    }

    private void createPreparedStatementToCreate() throws DAOException {
        if (preparedStatementToCreate == null) {
            try {
                connection = getConnection();
                preparedStatementToCreate = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DAOException("We have problems with create PreparedStatement to create Subject", e);
            }
        }
    }

    public void closePreparedStatementToCreate() throws DAOException {
        if (preparedStatementToCreate != null) {
            try {
                preparedStatementToCreate.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw new DAOException("We have problems with close PreparedStatement to create Subject", e);
            }
        }
    }

    public void create(SubjectDTO subject) throws DAOException {
        ResultSet resultSet = null;
        try {
            createPreparedStatementToCreate();
            preparedStatementToCreate.setString(1, subject.getSubject());
            preparedStatementToCreate.executeUpdate();
            resultSet = preparedStatementToCreate.getGeneratedKeys();
            if (resultSet.next()) {
                subject.setId(resultSet.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DAOException("We have problems with create Subject", e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new DAOException("We have problems with close resultSet (create Subject)", e);
                }
            }
        }
    }

    private void createPreparedStatementToUpdate() throws DAOException {
        try {
            if (preparedStatementToUpdate == null) {
                connection = getConnection();
                preparedStatementToUpdate = connection.prepareStatement(UPDATE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DAOException("We have problems with create PreparedStatement to update Subject", e);
        }
    }

    public void closePreparedStatementToUpdate() throws DAOException {
        if (preparedStatementToUpdate != null) {
            try {
                preparedStatementToUpdate.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw new DAOException("We have problems with close PreparedStatement to update Subject", e);
            }
        }
    }

    public void update(SubjectDTO newSubject) throws DAOException {
        try {
            createPreparedStatementToUpdate();
            preparedStatementToUpdate.setString(1, newSubject.getSubject());
            preparedStatementToUpdate.setInt(2, newSubject.getId());
            preparedStatementToUpdate.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DAOException("We have problems with update Subject", e);
        }
    }

    private void createPreparedStatementToDelete() throws DAOException {
        if (preparedStatementToDelete == null) {
            try {
                connection = getConnection();
                preparedStatementToDelete = connection.prepareStatement(DELETE);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DAOException("We have problems with create PreparedStatement to delete Subject", e);
            }
        }
    }

    public void closePreparedStatementToDelete() throws DAOException {
        if (preparedStatementToDelete != null) {
            try {
                preparedStatementToDelete.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw new DAOException("We have problems with close PreparedStatement to delete Subject", e);
            }
        }
    }

    public void delete(Integer id) throws DAOException {
        try {
            createPreparedStatementToDelete();
            preparedStatementToDelete.setInt(1, id);
            preparedStatementToDelete.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DAOException("We have problems with delete Subject", e);
        }
    }

    private void createPreparedStatementToGetPageOfSubjects() throws DAOException {
        if (preparedStatementToGetPageOfSubjects == null) {
            try {
                connection = getConnection();
                preparedStatementToGetPageOfSubjects = connection.prepareStatement(GET_PAGE_OF_SUBJECTS);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DAOException("We have problems with create PreparedStatement to get page of Subjects", e);
            }
        }
    }

    public void closePreparedStatementToGetPageOfSubjects() throws DAOException {
        if (preparedStatementToGetPageOfSubjects != null) {
            try {
                preparedStatementToGetPageOfSubjects.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw new DAOException("We have problems with close PreparedStatement to get page of Subjects", e);
            }
        }
    }


    public Collection<SubjectDTO> getPageOfSubjects(Integer numberOfSubjectsPerPage, Integer pageNumber) throws DAOException {
        ArrayList<SubjectDTO> subjects = new ArrayList<SubjectDTO>();
        ResultSet resultSet = null;
        try {
            createPreparedStatementToGetPageOfSubjects();
            preparedStatementToGetPageOfSubjects.setInt(1, numberOfSubjectsPerPage);
            preparedStatementToGetPageOfSubjects.setInt(2, numberOfSubjectsPerPage * (pageNumber - 1));
            resultSet = preparedStatementToGetPageOfSubjects.executeQuery();

            while (resultSet.next()) {
                SubjectDTO subject = new SubjectDTO();

                subject.setId(resultSet.getInt(1));

                subject.setSubject(resultSet.getString(2));

                subjects.add(subject);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new DAOException("We have problems with get page of Subjects", e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new DAOException("We have problems with close resultSet (get page of Subjects)", e);
                }
            }
        }
        if (subjects.size() == 0 && pageNumber != 1) {
            throw new DAONoSuchPageException("no such page");
        }
        return subjects;
    }

    private void createPreparedStatementToGetSubjectById() throws DAOException {
        if (preparedStatementToGetSubjectById == null) {
            try {
                connection = getConnection();
                preparedStatementToGetSubjectById = connection.prepareStatement(GET_SUBJECT_BY_ID);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DAOException("We have problems with create PreparedStatement to get Subject by ID", e);

            }
        }
    }

    public void closePreparedStatementToGetSubjectById() throws DAOException {
        if (preparedStatementToGetSubjectById != null) {
            try {
                preparedStatementToGetSubjectById.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw new DAOException("We have problems with close PreparedStatement to get Subject by ID", e);
            }
        }
    }


    public SubjectDTO getSubjectById(Integer id) throws DAOException {
        SubjectDTO subject = new SubjectDTO();
        ResultSet resultSet = null;
        try {
            createPreparedStatementToGetSubjectById();
            preparedStatementToGetSubjectById.setInt(1, id);
            resultSet = preparedStatementToGetSubjectById.executeQuery();

            if (resultSet.next()) {
                subject.setId(id);
                subject.setSubject(resultSet.getString(1));

                return subject;

            } else {
                throw new DAOException("subject with Id = " + id + " not found");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new DAOException("We have problems with get Subject by ID", e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new DAOException("We have problems with close resultSet (get Subject by ID)", e);
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
                throw new DAOException("cannot close the connection (return to Pool from Subject)", e);
            }
        }
    }

    public void closeAll() throws DAOException {
        try {
            closePreparedStatementToCreate();
        } catch (DAOException e) {
            throw new DAOException(e);
        } finally {
            closePreparedStatementToUpdate();
            closePreparedStatementToDelete();
            closePreparedStatementToGetPageOfSubjects();
            closePreparedStatementToGetSubjectById();
            closeConnection();
        }
    }

}
