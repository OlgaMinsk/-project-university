package DAO;

import DTO.StudentDTO;

import java.util.Collection;

public interface StudentDAO {
    public void create(StudentDTO student) throws DAOException;

    public void update(StudentDTO newStudent) throws DAOException;

    public void delete(Integer id) throws DAOException;

    public Collection<StudentDTO> getPageOfStudents(Integer numberOfStudentsPerPage, Integer pageNumber) throws DAOException;

}
