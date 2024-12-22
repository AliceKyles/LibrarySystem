package beans;

import dictionaries.Role;
import hibernate.HibernateUtils;
import hibernate.User;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean(name = "usersForEdit", eager = true)
@SessionScoped
public class UsersForEdit extends BasicBeanWithPermissions {

    private String userName;
    private String name;
    private Integer role;

    public List<User> getUsersToEdit() {
        int[] roles = getRole() != null && getRole() != 0 ? new int[]{getRole()} : getCanEditLibrarians() ? new int[]{Role.LIBRARIAN, Role.READER} : new int[]{Role.READER};
        return HibernateUtils.getUsers(getUserName(), getName(), roles);
    }

    public String getRoleName(Integer role) {
        return Role.ROLE_NAMES.get(role);
    }

    public Map<Integer, String> getRolesForSearch() {
        Map<Integer, String> rolesForSearch = new HashMap<>(Role.ROLE_NAMES);
        rolesForSearch.put(0, "");
        rolesForSearch.remove(Role.ADMIN);
        return rolesForSearch;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }
}
