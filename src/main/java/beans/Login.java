package beans;

import com.mysql.cj.util.StringUtils;
import hibernate.HibernateUtils;
import hibernate.User;
import security.PasswordUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import java.security.NoSuchAlgorithmException;

@RequestScoped
@ManagedBean(name = "login", eager = true)
public class Login extends BasicBean {
    private String name;
    private String password;

    private boolean noDatabase = false;

    public Login() {
        try {
            HibernateUtils.createInstance();
        } catch (Exception e) {
            error = e.getMessage();
            noDatabase = true;
        }
    }

    public String login() {
        if (noDatabase) {
            return "";
        }
        if (StringUtils.isNullOrEmpty(getName()) || StringUtils.isNullOrEmpty(getPassword())) {
            error = "Please enter your username and password";
            return "";
        }
        User user = HibernateUtils.getUser(getName());
        try {
            if (user == null || !PasswordUtils.checkPassword(user, getPassword())) {
                error = "Username or password is incorrect";
            } else {
                FacesContext context = FacesContext.getCurrentInstance();
                context.getExternalContext().getSessionMap().put(BasicBeanWithPermissions.USER, user);
                return "mainPage.xhtml";
            }
        } catch (NoSuchAlgorithmException e) {
            error = "Login is impossible right now, please try again later";
        }
        return "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
