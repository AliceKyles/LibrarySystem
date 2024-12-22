package hibernate;

import dictionaries.Role;
import jakarta.persistence.*;
import security.PasswordUtils;

import java.security.NoSuchAlgorithmException;
import java.util.List;

//Table with login credentials
@Entity
@Table(name = "USER")
public class User {

    User() {
    }

    public User(String name, String password) throws NoSuchAlgorithmException {
        this.name = name;
        this.salt = PasswordUtils.generateSalt();
        this.password = PasswordUtils.getHashWithSalt(password, salt);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME", unique = true)
    private String name;
    @Column(name = "SALT")
    private byte[] salt;
    @Column(name = "PASSWORD")
    private String password;
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Personal personal;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Book> books;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Personal getPersonal() {
        if (personal == null) {
            Personal personal = new Personal(this, Role.READER);
            setPersonal(personal);
            HibernateUtils.saveObject(personal);
        }
        return personal;
    }

    public void setPersonal(Personal personal) {
        this.personal = personal;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
