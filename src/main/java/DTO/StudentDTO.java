package DTO;

public class StudentDTO {
    private String name;
    private String surname;
    private Integer id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {

        this.id = id;
    }

    @Override
    public String toString() {
        String s = "StudentDTO: " +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", id=" + id +
                '}';
        return s;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentDTO that = (StudentDTO) o;
        return this.id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return this.id == null ? 0 : this.id;
    }
}
