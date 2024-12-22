package beans;

import dictionaries.Genres;
import hibernate.Book;
import hibernate.HibernateUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.Map;
import java.util.Objects;

@ManagedBean(name = "bookEdit", eager = true)
@SessionScoped
public class BookEdit extends BasicBeanWithPermissions {

    public BookEdit() {
    }

    private Book bookToEdit = new Book();

    public void setBookId(Integer bookId) {
        if (Objects.equals(bookId, -1)) {
            bookToEdit = new Book();
        } else {
            bookToEdit = HibernateUtils.getBook(bookId);
            if (bookToEdit == null) {
                error = "Something went wrong";
            }
        }
    }

    public String saveBook() {
        if (error != null) {
            return "";
        }
        if (bookToEdit.getId() == null) {
            HibernateUtils.saveObject(bookToEdit);
        } else {
            HibernateUtils.updateObject(bookToEdit);
        }
        return "availableBooks";
    }

    public String deleteBook() {
        if (error != null) {
            return "";
        }
        if (bookToEdit.getId() != null) {
            HibernateUtils.deleteObject(bookToEdit);
        }
        return "availableBooks";
    }

    public void setBookToEdit(Book bookToEdit) {
        this.bookToEdit = bookToEdit;
    }

    public Book getBookToEdit() {
        return bookToEdit;
    }

    public Integer getBookId() {
        return bookToEdit != null ? bookToEdit.getId() : null;
    }

    public Map<Integer, String> getGenres() {
        return Genres.GENRE_NAMES;
    }
}
