package beans;

import com.mysql.cj.util.StringUtils;
import hibernate.HibernateUtils;
import hibernate.Personal;
import security.PasswordUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@ManagedBean(name = "info", eager = true)
@SessionScoped
public class Info extends BasicBeanWithPermissions {
    private String name;
    private Date dateOfBirth;
    private String oldPassword;
    private String newPassword;

    public Info() {
        super();
        if (getPersonal() != null) {
            fillFromPersonal(getPersonal());
        }
    }

    private void fillFromPersonal(Personal personal) {
        setName(personal.getName());
        setDateOfBirth(getDate(personal.getDateOfBirth()));
    }

    public void nameChange() {
        getPersonal().setName(getName());
        HibernateUtils.updateObject(getPersonal());
        error = null;
    }

    public void birthDayChange() {
        getPersonal().setDateOfBirth(getLocalDate(getDateOfBirth()));
        HibernateUtils.updateObject(getPersonal());
        error = null;
    }

    public void passwordChange() throws NoSuchAlgorithmException {
        if (StringUtils.isNullOrEmpty(getNewPassword()) || StringUtils.isNullOrEmpty(getOldPassword())) {
            error = "Please enter both the current and the new password";
            return;
        }
        if (PasswordUtils.checkPassword(getUser(), getOldPassword())) {
            getUser().setPassword(PasswordUtils.getHashWithSalt(getNewPassword(), getUser().getSalt()));
            HibernateUtils.updateObject(getUser());
            error = null;
        } else {
            error = "Password is not correct";
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
