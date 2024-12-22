package beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "mainPage", eager = true)
@SessionScoped
public class MainPage extends BasicBeanWithPermissions {

    public MainPage() {
    }
}
