package DTO;

public class SubjectDTO {
    private Integer id;
    private String subject;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) throws DTOException {
        if (id > 0) {
            this.id = id;
        } else {
            throw new DTOException("id must be positive");
        }
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        SubjectDTO that = (SubjectDTO) o;
        return that.getId()==this.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "SubjectDTO{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                '}';
    }
}
