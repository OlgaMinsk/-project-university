package DTO;

public class MarkDTO {
    private Integer mark;
    private Integer idStudent;
    private Integer idSubjeck;
    private Integer idMark;

    public Integer getIdMark() {
        return idMark;
    }

    public void setIdMark(Integer idMark) {
        this.idMark = idMark;
    }

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) throws DTOException {
        if (mark > 0 && mark <= 10) {
            this.mark = mark;
        } else {
            throw new DTOException("MarkDTO: incorrect validation (more than 10 or less than 0)");
        }
    }

    public Integer getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(Integer idStudent) {
        this.idStudent = idStudent;
    }

    public Integer getIdSubjeck() {
        return idSubjeck;
    }

    public void setIdSubjeck(Integer idSubjeck) {
        this.idSubjeck = idSubjeck;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarkDTO markDTO = (MarkDTO) o;
        return markDTO.getIdMark() == this.getIdMark();
    }

    @Override
    public int hashCode() {
        return idMark;
    }

    @Override
    public String toString() {
        return "MarkDTO{" +
                "idMark=" + idMark +
                ", idStudent=" + idStudent +
                ", idSubjeck=" + idSubjeck +
                ", mark=" + mark +
                '}';
    }
}
