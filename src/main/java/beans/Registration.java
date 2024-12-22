package beans;

import com.mysql.cj.util.StringUtils;
import hibernate.HibernateUtils;
import hibernate.User;

import javax.faces.bean.ManagedBean;
import java.security.NoSuchAlgorithmException;

@ManagedBean(name = "registration", eager = true)
public class Registration extends BasicBean {

    private String name;
    private String password;

    public String register() throws NoSuchAlgorithmException {
        if (StringUtils.isNullOrEmpty(getName()) || StringUtils.isNullOrEmpty(getPassword())) {
            error = "Please enter your username and password";
            return "";
        }
        User user = HibernateUtils.getUser(getName());
        if (user != null) {
            error = "User with this name already exists";
            return "";
        }
        HibernateUtils.saveObject(new User(getName(), getPassword()));
        return "login.xhtml";
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
