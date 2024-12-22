package beans;

import dictionaries.Permission;
import dictionaries.Role;
import hibernate.HibernateUtils;
import hibernate.Personal;
import hibernate.User;
import security.PasswordUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@ManagedBean(name = "editUser", eager = true)
@SessionScoped
public class EditUser extends BasicBeanWithPermissions {

    public EditUser() {
    }

    private Personal editedPersonal;
    private Date dateOfBirth;
    private Integer role;
    private Integer permissions;

    public void setUserId(Integer userId) {
        User editedUser = HibernateUtils.getUser(userId);
        if (editedUser == null) {
            error = "Something went wrong";
            editedPersonal = null;
        } else {
            editedPersonal = editedUser.getPersonal();
        }
        fillFromPersonal();
    }

    private void fillFromPersonal() {
        if (editedPersonal == null) {
            return;
        }
        setDateOfBirth(getDate(editedPersonal.getDateOfBirth()));
        setRole(editedPersonal.getRole());
        setPermissions(editedPersonal.getPermissions());
    }

    public void birthDayChange() {
        editedPersonal.setDateOfBirth(getLocalDate(getDateOfBirth()));
        HibernateUtils.updateObject(editedPersonal);
    }

    public void permissionChange() {
        editedPersonal.setPermissions(getPermissions());
        HibernateUtils.updateObject(editedPersonal);
    }

    public void roleChange() {
        editedPersonal.setRole(getRole());
        HibernateUtils.updateObject(editedPersonal);
    }

    public void resetPass() {
        User user = editedPersonal.getUser();
        try {
            user.setPassword(PasswordUtils.getHashWithSalt("abcdefg", user.getSalt()));
            HibernateUtils.updateObject(user);
        } catch (NoSuchAlgorithmException e) {
            error = e.getMessage();
        }
    }

    public Integer getUserId() {
        return editedPersonal != null ? editedPersonal.getUser().getId() : null;
    }

    public boolean getCanEditThisUser() {
        return editedPersonal != null && getCanEditUsers() && (Objects.equals(editedPersonal.getRole(), Role.READER) || (Objects.equals(editedPersonal.getRole(), Role.LIBRARIAN) && getCanEditLibrarians()));
    }

    public boolean isLibrarian() {
        return editedPersonal != null && (Objects.equals(editedPersonal.getRole(), Role.LIBRARIAN));
    }

    public String getUserName() {
        return editedPersonal.getUser().getName();
    }

    public String getName() {
        return editedPersonal.getName();
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Map<Integer, String> getUserPermissions() {
        return Permission.PERMISSIONS;
    }

    public Map<Integer, String> getRoles() {
        return Role.ROLE_NAMES;
    }

    public Integer getPermissions() {
        return permissions;
    }

    public void setPermissions(Integer permissions) {
        this.permissions = permissions;
    }
}
