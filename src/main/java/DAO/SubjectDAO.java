package DAO;

import DTO.SubjectDTO;

import java.util.Collection;

public interface SubjectDAO {
    public void create(SubjectDTO subject) throws DAOException;

    public void update(SubjectDTO newSubject) throws DAOException;

    public void delete(Integer id) throws DAOException;

    public Collection<SubjectDTO> getPageOfSubjects(Integer numberOfSubjectsPerPage, Integer pageNumber) throws DAOException;

}
