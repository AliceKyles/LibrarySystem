package beans;

import dictionaries.Genres;
import dictionaries.Role;
import hibernate.Book;
import hibernate.HibernateUtils;
import hibernate.User;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@ManagedBean(name = "availableBooks", eager = true)
@SessionScoped
public class AvailableBooks extends BasicBeanWithPermissions {

    private String author;
    private String title;
    private Integer genre;
    private Integer checkedOut;
    private Integer checkedOutUser;

    public List<Book> getAvailableBooksList() {
        return HibernateUtils.getAvailableBooks(getUser(), getTitle(), getAuthor(), getGenre(), isCheckedOut(), getCheckedOutUser());
    }

    private Boolean isCheckedOut() {
        if (Objects.equals(getCheckedOut(), 1)) {
            return true;
        }
        if (Objects.equals(getCheckedOut(), 2)) {
            return false;
        }
        return null;
    }

    public String getGenreName(Integer genre) {
        return Genres.GENRE_NAMES.get(genre);
    }

    public Map<Integer, String> getGenreNames() {
        Map<Integer, String> genreNames = new HashMap<>();
        genreNames.put(-1, "");
        genreNames.putAll(Genres.GENRE_NAMES);
        return genreNames;
    }

    public Map<Integer, String> getIsCheckedOut() {
        Map<Integer, String> isCheckedOut = new HashMap<>();
        isCheckedOut.put(0, "");
        isCheckedOut.put(1, "Yes");
        isCheckedOut.put(2, "No");
        return isCheckedOut;
    }

    public Map<Integer, String> getUserCheckedOut() {
        List<User> users = HibernateUtils.getUsers(null, null, Role.READER);
        Map<Integer, String> map = new HashMap<>();
        map.put(-1, "");
        users.forEach(u -> map.put(u.getId(), u.getName()));
        return map;
    }

    public void checkOutBook(Book book) {
        book.setUser(getUser());
        HibernateUtils.updateObject(book);
    }

    public void returnBook(Book book) {
        book.setUser(null);
        HibernateUtils.updateObject(book);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getGenre() {
        return Objects.equals(genre, -1) ? null : genre;
    }

    public void setGenre(Integer genre) {
        this.genre = genre;
    }

    public Integer getCheckedOut() {
        return checkedOut;
    }

    public void setCheckedOut(Integer checkedOut) {
        this.checkedOut = checkedOut;
    }

    public Integer getCheckedOutUser() {
        return Objects.equals(checkedOutUser, -1) ? null : checkedOutUser;
    }

    public void setCheckedOutUser(Integer checkedOutUser) {
        this.checkedOutUser = checkedOutUser;
    }
}
