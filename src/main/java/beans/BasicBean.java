package beans;

import javax.faces.context.FacesContext;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

//Basic functions, that any page might need
public class BasicBean {
    protected String error;

    public String getError() {
        return error;
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "login.xhtml";
    }

    LocalDate getLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    Date getDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

}
