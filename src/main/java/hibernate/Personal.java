package hibernate;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;

//Table with users' personal info, one record for each record in User
@Entity
@Table(name = "PERSONAL")
public class Personal {

    public Personal() {
    }

    public Personal(User user, Integer role) {
        this.user = user;
        this.role = role;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    @JoinColumn(name = "USER_ID")
    @OneToOne
    private User user;
    @Column(name = "NAME")
    private String name;
    @Column(name = "DATE_OF_BIRTH")
    private LocalDate dateOfBirth;
    @Column(name = "ROLE")
    private Integer role;
    @Column(name = "PERMISSIONS")
    private Integer permissions;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getPermissions() {
        return permissions;
    }

    public void setPermissions(Integer permissions) {
        this.permissions = permissions;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAge() {
        if (getDateOfBirth() == null) {
            return 0;
        }
        return Period.between(getDateOfBirth(), LocalDate.now()).getYears();
    }
}
