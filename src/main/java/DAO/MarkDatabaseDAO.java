package DAO;

import DTO.MarkDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

public class MarkDatabaseDAO extends UtilAbstractDAO implements MarkDAO {
    private static final String INSERT = "insert into mark(id_student, id_subject, mark) values (?,?, ?)";
    private static final String UPDATE = "update mark set mark = ? where id_mark = ?";
    private static final String DELETE = "delete from mark where id_mark = ?";
    // private static final String GET_PAGE_OF_MARKS = "select student.surname, subject.subject, mark.mark from student, subject, mark where mark.id_student = student.id_student and mark.id_subject = subject.id_subject limit ? offset ?";
    private static final String GET_PAGE_OF_MARKS = "select id_student, id_subject, mark, id_mark from mark  limit ? offset ?";
    private PreparedStatement preparedStatementToCreate = null;
    private PreparedStatement preparedStatementToUpdate = null;
    private PreparedStatement preparedStatementToDelete = null;
    private PreparedStatement preparedStatementToGetPageOfMarks = null;

    private void createPreparedStatementToCreate() throws DAOException {
        if (preparedStatementToCreate == null) {
            try {
                connection = new UtilAbstractDAO().getConnection();
                preparedStatementToCreate = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DAOException("We have problems with create PreparedStatement to create Mark", e);
            }
        }
    }

    public void closePreparedStatementToCreate() throws DAOException {
        if (preparedStatementToCreate != null) {
            try {
                preparedStatementToCreate.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw new DAOException("We have problems with close PreparedStatement to create Mark", e);
            }
        }
    }

    @Override
    public void create(MarkDTO mark) throws DAOException {
        ResultSet resultSet = null;
        try {
            createPreparedStatementToCreate();
            preparedStatementToCreate.setInt(1, mark.getIdStudent());
            preparedStatementToCreate.setInt(2, mark.getIdSubjeck());
            preparedStatementToCreate.setInt(3, mark.getMark());
            preparedStatementToCreate.executeUpdate();
            resultSet = preparedStatementToCreate.getGeneratedKeys();
            if (resultSet.next()) {
                mark.setIdMark(resultSet.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DAOException("We have problems with create Mark", e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new DAOException("We have problems with close resultSet (create Mark)", e);
                }
            }
        }
    }


    private void createPreparedStatementToUpdate() throws DAOException {
        try {
            if (preparedStatementToUpdate == null) {
                connection = new UtilAbstractDAO().getConnection();
                preparedStatementToUpdate = connection.prepareStatement(UPDATE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DAOException("We have problems with create PreparedStatement to update Mark", e);
        }
    }

    public void closePreparedStatementToUpdate() throws DAOException {
        if (preparedStatementToUpdate != null) {
            try {
                preparedStatementToUpdate.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw new DAOException("We have problems with close PreparedStatement to update Mark", e);
            }
        }
    }

    @Override
    public void update(MarkDTO newMark) throws DAOException {
        try {
            createPreparedStatementToUpdate();

            preparedStatementToUpdate.setInt(1, newMark.getMark());
            preparedStatementToUpdate.setInt(2, newMark.getIdMark());
            preparedStatementToUpdate.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DAOException("We have problems with update Mark", e);
        }
    }

    private void createPreparedStatementToDelete() throws DAOException {
        if (preparedStatementToDelete == null) {
            try {
                connection = new UtilAbstractDAO().getConnection();
                preparedStatementToDelete = connection.prepareStatement(DELETE);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DAOException("We have problems with create PreparedStatement to delete Mark", e);
            }
        }
    }

    public void closePreparedStatementToDelete() throws DAOException {
        if (preparedStatementToDelete != null) {
            try {
                preparedStatementToDelete.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw new DAOException("We have problems with close PreparedStatement to delete Mark", e);
            }
        }
    }

    @Override
    public void delete(Integer id) throws DAOException {
        try {
            createPreparedStatementToDelete();
            preparedStatementToDelete.setInt(1, id);
            preparedStatementToDelete.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DAOException("We have problems with delete Mark", e);
        }

    }

    private void createPreparedStatementToGetPageOfMarks() throws DAOException {
        if (preparedStatementToGetPageOfMarks == null) {
            try {
                connection = new UtilAbstractDAO().getConnection();
                preparedStatementToGetPageOfMarks = connection.prepareStatement(GET_PAGE_OF_MARKS);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DAOException("We have problems with create PreparedStatement to get page of Marks", e);
            }
        }
    }

    public void closePreparedStatementToGetPageOfMarks() throws DAOException {
        if (preparedStatementToGetPageOfMarks != null) {
            try {
                preparedStatementToGetPageOfMarks.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw new DAOException("We have problems with close PreparedStatement to get page of Marks", e);
            }
        }
    }


    @Override
    public Collection<MarkDTO> getPageOfMarks(Integer numberOfMarksPerPage, Integer pageNumber) throws DAOException {
        ArrayList<MarkDTO> marks = new ArrayList<MarkDTO>();
        ResultSet resultSet = null;
        try {
            createPreparedStatementToGetPageOfMarks();

            preparedStatementToGetPageOfMarks.setInt(1, numberOfMarksPerPage);
            preparedStatementToGetPageOfMarks.setInt(2, numberOfMarksPerPage * (pageNumber - 1));
            resultSet = preparedStatementToGetPageOfMarks.executeQuery();

            while (resultSet.next()) {
                MarkDTO mark = new MarkDTO();

                mark.setIdStudent(resultSet.getInt(1));
                mark.setIdSubjeck(resultSet.getInt(2));
                mark.setMark(resultSet.getInt(3));
                mark.setIdMark(resultSet.getInt(4));

                marks.add(mark);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new DAOException("We have problems with get page of Marks", e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new DAOException("We have problems with close resultSet (get page of Marks)", e);
                }
            }
        }
        if (marks.size() == 0 && pageNumber != 1) {
            throw new DAONoSuchPageException("no such page");
        }
        return marks;

    }


    public void closeAll() throws DAOException {
        try {
            closePreparedStatementToCreate();
        } catch (DAOException e) {
            throw new DAOException(e);
        } finally {
            closePreparedStatementToUpdate();
            closePreparedStatementToDelete();
            closePreparedStatementToGetPageOfMarks();
            closeConnection();
        }
    }

}
