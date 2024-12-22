package beans;

import dictionaries.Permission;
import dictionaries.Role;
import hibernate.Personal;
import hibernate.User;

import javax.faces.context.FacesContext;
import java.util.Objects;

//Functions for pages, that check user permissions
public class BasicBeanWithPermissions extends BasicBean {

    private final User user;
    private final Personal personal;

    static String USER = "user";

    public BasicBeanWithPermissions() {
        this.user = getUserFromSession();
        this.personal = user == null ? null : user.getPersonal();
    }

    protected User getUserFromSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        return (User) context.getExternalContext().getSessionMap().get(USER);
    }

    public User getUser() {
        return user;
    }

    public Personal getPersonal() {
        return personal;
    }

    public boolean getCanViewBooks() {
        return getCanCheckOutBooks() || getCanEditUsers() || Objects.equals(personal.getPermissions(), Permission.VIEW_BOOKS);
    }

    public boolean getCanCheckOutBooks() {
        return !getCanEditUsers() && Objects.equals(personal.getPermissions(), Permission.CHECK_OUT_BOOKS);
    }

    public boolean getCanEditUsers() {
        return Role.CAN_EDIT_USERS.contains(personal.getRole());
    }

    public boolean getCanEditLibrarians() {
        return Objects.equals(Role.ADMIN, personal.getRole());
    }

    public boolean getCanEditBooks() {
        return getCanEditUsers();
    }
}
