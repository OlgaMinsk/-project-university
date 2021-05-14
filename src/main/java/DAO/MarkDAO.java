package DAO;

import DTO.MarkDTO;

import java.util.Collection;

public interface MarkDAO {
    public void create(MarkDTO mark) throws DAOException;

    public void update(MarkDTO newMark) throws DAOException;

    public void delete(Integer id) throws DAOException;

    public Collection<MarkDTO> getPageOfMarks(Integer numberOfMarksPerPage, Integer pageNumber) throws DAOException;

}
