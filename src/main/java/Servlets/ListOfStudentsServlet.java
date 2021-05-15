package Servlets;

import DAO.DAOException;
import DAO.PoolConnectionBuilder;
import DAO.StudentDatabaseDAO;
import DTO.DTOException;
import DTO.StudentDTO;


import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

@WebServlet(name = "ListOfStudentsServlet", value = "/ListOfStudentsServlet")
public class ListOfStudentsServlet extends HttpServlet {
    private static final String ACTION_DELETE = "delete";
    private static final String ACTION_LIST_OF_STUDENTS = "list";
    private static final String ACTION_UPDATE = "update";
    private static final String ACTION_ADD = "add";
    private static final String ACTION_GET_NEW_PARAMETERS_TO_UPDATE_STUDENT = "newParameters";
    private static final String ACTION_GET_PARAMETERS_TO_ADD_STUDENT = "newStudent";
    private static final String PARAMETER_STUDENTS_PER_PAGE = "studentsPerPage";
    private static final String PARAMETER_STUDENT_ID = "studentId";
    private static final String PARAMETER_STUDENT_NAME = "name";
    private static final String PARAMETER_STUDENT_SURNAME = "surname";
    private static final String PARAMETER_PAGE = "page";
    private static final String PARAMETER_ACTION = "action";
    private static final String LINK_SERVLET = "/ListOfStudentsServlet";
    private static final Integer STUDENTS_PER_PAGE_DEFAULT = 10;
    private String link = "";
    HttpSession session;

    @Override

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //    session = request.getSession();
        String action = "";
        if (request.getParameter(PARAMETER_ACTION) != null) {
            action = request.getParameter(PARAMETER_ACTION);
        }
        String studentsHtml = "";
        String mainInform = getPageAndStudentsPerPage(request);

        switch (action) {
            case (ACTION_DELETE):
                studentsHtml = delete(request);
                studentsHtml += "<h2> Students:</h2>" + listOfStudent(request);
                break;
            case (ACTION_LIST_OF_STUDENTS):
                studentsHtml += "<h2> Students:</h2>" + listOfStudent(request);
                break;
            case (ACTION_GET_NEW_PARAMETERS_TO_UPDATE_STUDENT):
                mainInform = getNewParametersToUpdateStudent(request);
                break;
            case (ACTION_UPDATE):
                update(request);
                studentsHtml += "<h2> Students:</h2>" + listOfStudent(request);
                break;
            case (ACTION_GET_PARAMETERS_TO_ADD_STUDENT):
                mainInform = getNewStudent(LINK_SERVLET + "?" + PARAMETER_ACTION + "=" + ACTION_ADD);
                break;
            case (ACTION_ADD):
                add(request);
                studentsHtml += "<h2> Students:</h2>" + listOfStudent(request);
                break;
            default:
                studentsHtml += "<h2> Students:</h2>" + listOfStudent(request);
        }
        PrintWriter pw = response.getWriter();
        pw.write("<!DOCTYPE html>" +
                "<html lang=eng>" +
                "<head>" +
                "<title>University</title>" +
                "<meta name='author' content='Volha Suprun'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<link rel='stylesheet' type='text/css' href='${request.contextPath}../../resources/css/styles.css'>" +
                "</head>" +
                "<body>" +
                "<a href='/' target='_blank'>Main</a><br>" +
                studentsHtml +
                mainInform +
                "</body>" +
                "</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private String getPageAndStudentsPerPage(HttpServletRequest request) {
        Integer studentsPerPage = STUDENTS_PER_PAGE_DEFAULT;
        Integer page = 0;
        try {
            if (request.getParameter(PARAMETER_STUDENTS_PER_PAGE) != null) {
                studentsPerPage = Integer.parseInt(request.getParameter(PARAMETER_STUDENTS_PER_PAGE));
            }

            if (request.getParameter(PARAMETER_PAGE) != null) {
                page = Integer.parseInt(request.getParameter(PARAMETER_PAGE));
            }
            return "<form action='" + LINK_SERVLET + "?" + PARAMETER_ACTION + "=" + ACTION_LIST_OF_STUDENTS + "' method='get'> " +
                    "<label>Page</label>" +
                    "<input name='" + PARAMETER_PAGE + "' value='" + (page + 1) + "' pattern='^[1-9]\\d*$' required/>" +
                    "<br><label>Students per page</label>" +
                    "<br><input name='" + PARAMETER_STUDENTS_PER_PAGE + "' value='" + studentsPerPage + "' pattern='^[1-9]\\d*$' required/> " +
                    "<button type='submit'> next</button> </form>" +
                    "<br>" +
                    "<br><a href='" + LINK_SERVLET + "?" + PARAMETER_ACTION + "=" + ACTION_GET_PARAMETERS_TO_ADD_STUDENT + "' >Add new student</a><br>";
        } catch (NumberFormatException exception) {
            exception.printStackTrace();
            return "<!-- " + exception.getMessage() + " -->" + "<p>Please enter only numeric parameters</p>" +
                    "<form action='" + LINK_SERVLET + "?" + PARAMETER_ACTION + "=" + ACTION_LIST_OF_STUDENTS + "' method='get'> " +
                    "<input name='" + PARAMETER_PAGE + "' value='" + (page + 1) + "' pattern='^[1-9]\\d*$' required/>" +
                    "<br><label>Students per page</label>" +
                    "<br><input name='" + PARAMETER_STUDENTS_PER_PAGE + "' value='" + studentsPerPage + "' pattern='^[1-9]\\d*$' required/> " +
                    "<button type='submit'> next</button> </form>" +
                    "<br>" +
                    "<br><a href='" + LINK_SERVLET + "?" + PARAMETER_ACTION + "=" + ACTION_GET_PARAMETERS_TO_ADD_STUDENT + "' >Add new student</a><br>";
        }

    }

    private String listOfStudent(HttpServletRequest request) {
        Integer page = 0;
        Integer studentsPerPage = STUDENTS_PER_PAGE_DEFAULT;
        StudentDatabaseDAO studentDatabaseDAO = null;
        String studentsHtml = "";
        try {
            if (request.getParameter(PARAMETER_PAGE) != null) {
                page = Integer.parseInt(request.getParameter(PARAMETER_PAGE));
            }
            if (request.getParameter(PARAMETER_STUDENTS_PER_PAGE) != null) {
                studentsPerPage = Integer.parseInt(request.getParameter(PARAMETER_STUDENTS_PER_PAGE));
            }
            studentDatabaseDAO = new StudentDatabaseDAO();
            Collection<StudentDTO> students = null;
            String link = LINK_SERVLET + "?" + PARAMETER_PAGE + "=" + page +
                    "&" + PARAMETER_STUDENTS_PER_PAGE + "=" + studentsPerPage;
            if (page > 0 && studentsPerPage > 0) {
                studentDatabaseDAO.setConnectionBuilder(new PoolConnectionBuilder());
                students = studentDatabaseDAO.getPageOfStudents(studentsPerPage, page);
                studentsHtml = "<p>Page = " + page + "</p>" +
                        "<table border=1><thead><th>Name</th><th>Surname</th><th></th><th></th></thead><tbody>";
                for (StudentDTO student : students) {

                    studentsHtml += " <tr><td>" + student.getName() +
                            "</td><td>" + student.getSurname() + "</td>" +
                            "<td>" + buttonDeleteStudent(link + "&" +
                            PARAMETER_ACTION + "=" + ACTION_DELETE, student.getId()) + " &nbsp " +
                            buttonUpdateStudent(link + "&" + PARAMETER_ACTION + "=" + ACTION_GET_NEW_PARAMETERS_TO_UPDATE_STUDENT,
                                    student.getId()) + "</td></tr>";
                }
                return studentsHtml += "</tbody></table><br>";
            }
        } catch (DAOException e) {
            e.printStackTrace();
            return "<!-- " + e.getMessage() + " -->" + "<p>Page = " + page + "</p><p>No such of page</p><br>";
        } catch (NumberFormatException exception) {
            exception.printStackTrace();
            return "<!-- " + exception.getMessage() + " -->" + "<p>Please enter only numeric parameters</p>";
        } finally {
            try {
                studentDatabaseDAO.closeAll();
            } catch (DAOException e) {
                e.printStackTrace();
                return "<!-- " + e.getMessage() + " -->";
            }
        }

        return studentsHtml;
    }

    private String buttonDeleteStudent(String link, Integer studentId) {
        String buttonDeleteStudentHml =
                "<div id='delete" + studentId + "' class='blackout'>" +
                        "<div class='window'>" +
                        "Do you want to delete the student?<br>" +
                        "<a href='#' class='close'>No</a>" +
                        "<form action='" + link + "' method='post'>" +
                        "<input type='hidden' name='" + PARAMETER_STUDENT_ID + "' value='" + studentId + "'/>" +
                        "<input value='Yes' type='submit'/>" +
                        "</form>" +
                        "</div>" +
                        "</div>" +
                        "<a href='#delete" + studentId + "'>Delete</a>";
        return buttonDeleteStudentHml;
    }

    private String buttonUpdateStudent(String link, Integer studentId) {
        String buttonUpdateStudentHml =
                "<div id='update" + studentId + "' class='blackout'>" +
                        "<div class='window'>" +
                        "Do you want to update the student?<br>" +
                        "<a href='#' class='close'>No</a>" +
                        "<form action='" + link + "' method='post'>" +
                        "<input type='hidden' name='" + PARAMETER_STUDENT_ID + "' value='" + studentId + "'/>" +
                        "<input value='Yes' type='submit'/>" +
                        "</form>" +
                        "</div>" +
                        "</div>" +
                        "<a href='#update" + studentId + "'>Update</a>";
        return buttonUpdateStudentHml;
    }

    private String getNewParametersToUpdateStudent(HttpServletRequest request) {
        Integer page = 0;
        Integer studentsPerPage = STUDENTS_PER_PAGE_DEFAULT;
        Integer studentId = 0;
        String updateStudentHml = "";
        StudentDatabaseDAO studentDatabaseDAO = null;
        try {
            if (request.getParameter(PARAMETER_STUDENT_ID) != null) {
                studentId = Integer.parseInt(request.getParameter(PARAMETER_STUDENT_ID));
            }
            if (request.getParameter(PARAMETER_PAGE) != null) {
                page = Integer.parseInt(request.getParameter(PARAMETER_PAGE));
            }
            if (request.getParameter(PARAMETER_STUDENTS_PER_PAGE) != null) {
                studentsPerPage = Integer.parseInt(request.getParameter(PARAMETER_STUDENTS_PER_PAGE));
            }
            link = LINK_SERVLET + "?" + PARAMETER_PAGE + "=" + page + "&" + PARAMETER_STUDENTS_PER_PAGE + "=" + studentsPerPage;


            studentDatabaseDAO = new StudentDatabaseDAO();

            StudentDTO studentDTO;
            studentDatabaseDAO.setConnectionBuilder(new PoolConnectionBuilder());
            studentDTO = studentDatabaseDAO.getStudentById(studentId);
            updateStudentHml =
                    "<br> Please make changes (all fields must be filled in)" +
                            "<form action='" + link + "&" + PARAMETER_ACTION + "=" + ACTION_UPDATE + "' method='post'> " +
                            "<input type='hidden' name='" + PARAMETER_STUDENT_ID + "' value='" + studentId + "'/>" +
                            "<br><label>Student name</label><br>" +
                            "<input name='" + PARAMETER_STUDENT_NAME + "' value='" + studentDTO.getName() + "' pattern='[A-Za-z]{1,}' required/>" +
                            "<br><label>Student surname</label>" +
                            "<br><input name='" + PARAMETER_STUDENT_SURNAME + "' value='" + studentDTO.getSurname() + "' pattern='[A-Za-z]{1,}' required/>" +
                            "<br><br><button type='submit'> Update student </button> </form>";
            return updateStudentHml;
        } catch (Exception e) {
            e.printStackTrace();
            return updateStudentHml = "<!-- " + e.getMessage() + " -->" + "Sorry, something went wrong. We are already correcting this situation. Please try again later<br>" +
                    "<a  href='" + link + "&" + PARAMETER_ACTION + "=" + ACTION_LIST_OF_STUDENTS + "'  type='submit'>Ok</a>" +
                    "</div>";
        } finally {
            try {
                studentDatabaseDAO.closeAll();
            } catch (DAOException e) {
                e.printStackTrace();
                return "<!-- " + e.getMessage() + " -->";
            }
        }

    }

    private String delete(HttpServletRequest request) {
        Integer studentId = 0;
        Integer page = 0;
        Integer studentsPerPage = STUDENTS_PER_PAGE_DEFAULT;
        StudentDatabaseDAO studentDatabaseDAO = null;
        try {
            if (request.getParameter(PARAMETER_STUDENT_ID) != null) {
                studentId = Integer.parseInt(request.getParameter(PARAMETER_STUDENT_ID));
            }
            if (request.getParameter(PARAMETER_PAGE) != null) {
                page = Integer.parseInt(request.getParameter(PARAMETER_PAGE));
            }
            if (request.getParameter(PARAMETER_STUDENTS_PER_PAGE) != null) {
                studentsPerPage = Integer.parseInt(request.getParameter(PARAMETER_STUDENTS_PER_PAGE));
            }
            link = LINK_SERVLET + "?" + PARAMETER_PAGE + "=" + page +
                    "&" + PARAMETER_STUDENTS_PER_PAGE + "=" + studentsPerPage +
                    "&" + PARAMETER_ACTION + "=" + ACTION_LIST_OF_STUDENTS;

            studentDatabaseDAO = new StudentDatabaseDAO();
            if (studentId > 0) {

                studentDatabaseDAO.setConnectionBuilder(new PoolConnectionBuilder());
                studentDatabaseDAO.delete(studentId);
            }
        } catch (DAOException e) {
            e.printStackTrace();
            String st = "<!-- " + e.getMessage() + " -->" + "<div class='window'>" +
                    "We will not delete this student. And don't ask<br>" +
                    "<a  href='" + link + "'  type='submit'>Ok</a>" +
                    "</div>";
            return st;
        } finally {
            try {
                studentDatabaseDAO.closeAll();
            } catch (DAOException e) {
                e.printStackTrace();
                return "<!-- " + e.getMessage() + " -->";
            }
        }

        return "";
    }

    private String update(HttpServletRequest request) {
        System.out.println("update");
        String name = "";
        String surname = "";
        Integer studentId = 0;
        Integer page = 0;
        Integer studentsPerPage = STUDENTS_PER_PAGE_DEFAULT;
        StudentDatabaseDAO studentDatabaseDAO = null;
        try {
            if (request.getParameter(PARAMETER_STUDENT_NAME) != null) {
                name = request.getParameter(PARAMETER_STUDENT_NAME);
            }
            if (request.getParameter(PARAMETER_STUDENT_SURNAME) != null) {
                surname = request.getParameter(PARAMETER_STUDENT_SURNAME);
            }
            if (request.getParameter(PARAMETER_STUDENT_ID) != null) {
                studentId = Integer.parseInt(request.getParameter(PARAMETER_STUDENT_ID));
            }
            if (request.getParameter(PARAMETER_PAGE) != null) {
                page = Integer.parseInt(request.getParameter(PARAMETER_PAGE));
            }
            if (request.getParameter(PARAMETER_STUDENTS_PER_PAGE) != null) {
                studentsPerPage = Integer.parseInt(request.getParameter(PARAMETER_STUDENTS_PER_PAGE));
            }
            StudentDTO studentDTO = new StudentDTO();
            studentDTO.setName(name);
            studentDTO.setSurname(surname);
            studentDTO.setId(studentId);
            System.out.println(studentDTO.toString());
            link = LINK_SERVLET + "?" + PARAMETER_PAGE + "=" + page +
                    "&" + PARAMETER_STUDENTS_PER_PAGE + "=" + studentsPerPage +
                    "&" + PARAMETER_ACTION + "=" + ACTION_LIST_OF_STUDENTS;
            studentDatabaseDAO = new StudentDatabaseDAO();

            studentDatabaseDAO.setConnectionBuilder(new PoolConnectionBuilder());
            studentDatabaseDAO.update(studentDTO);
        } catch (Exception e) {
            e.printStackTrace();
            String st = "<!-- " + e.getMessage() + " -->" + "<div class='window'>" +
                    "We will not update this student. Sorry<br>" +
                    "<a  href='" + link + "'  type='submit'>Ok</a>" +
                    "</div>";
            return st;
        } finally {
            try {
                studentDatabaseDAO.closeAll();
            } catch (DAOException e) {
                e.printStackTrace();
                return "<!-- " + e.getMessage() + " -->";
            }
        }
        return "";
    }


    private String getNewStudent(String link) {
        return "<br> Please fill out the following form (all fields must be filled in)<br>" +
                "<form action='" + link + "' method='post'> " +
                "<label>Student name</label>" +
                "<br><input name='" + PARAMETER_STUDENT_NAME + "' pattern='[A-Za-z]{1,}' required/>" +
                "<br><label>Student surname</label>" +
                "<br><input name='" + PARAMETER_STUDENT_SURNAME + "' pattern='[A-Za-z]{1,}' required/>" +
                "<br><button type='submit'> Add student </button> </form>";

    }

    private String add(HttpServletRequest request) {
        String name = "";
        String surname = "";
        Integer page = 0;
        Integer studentsPerPage = 10;
        StudentDatabaseDAO studentDatabaseDAO = null;

        try {
            if (request.getParameter(PARAMETER_STUDENT_NAME) != null) {
                name = request.getParameter(PARAMETER_STUDENT_NAME);
            }
            if (request.getParameter(PARAMETER_STUDENT_SURNAME) != null) {
                surname = request.getParameter(PARAMETER_STUDENT_SURNAME);
            }
            StudentDTO studentDTO = new StudentDTO();
            studentDTO.setName(name);
            studentDTO.setSurname(surname);
            studentDatabaseDAO = new StudentDatabaseDAO();

            studentDatabaseDAO.setConnectionBuilder(new PoolConnectionBuilder());
            studentDatabaseDAO.create(studentDTO);

            if (request.getParameter(PARAMETER_PAGE) != null) {
                page = Integer.parseInt(request.getParameter(PARAMETER_PAGE));
            }
            if (request.getParameter(PARAMETER_STUDENTS_PER_PAGE) != null) {
                studentsPerPage = Integer.parseInt(request.getParameter(PARAMETER_STUDENTS_PER_PAGE));
            }
            link = LINK_SERVLET + "?" + PARAMETER_PAGE + "=" + page + "&" + PARAMETER_STUDENTS_PER_PAGE + "=" + studentsPerPage + "&" + PARAMETER_ACTION + "=" + ACTION_LIST_OF_STUDENTS;

        } catch (DAOException e) {
            e.printStackTrace();
            String st = "<!-- " + e.getMessage() + " -->" + "<div class='window'>" +
                    "We can't add this student. Sorry<br>" +
                    "<a  href='" + link + "'  type='submit'>Ok</a>" +
                    "</div>";
            return st;
        } catch (NumberFormatException exception) {
            exception.printStackTrace();
            return "<!-- " + exception.getMessage() + " -->" + "<p>Please enter only numeric parameters</p>";
        } finally {
            try {
                studentDatabaseDAO.closeAll();
            } catch (DAOException e) {
                e.printStackTrace();
                return "<!-- " + e.getMessage() + " -->";
            }
        }
        return "";
    }
}
