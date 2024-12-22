package hibernate;

import com.mysql.cj.util.StringUtils;
import dictionaries.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.SQLGrammarException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;

public class HibernateUtils {

    private final SessionFactory factory;
    private static HibernateUtils INSTANCE;

    private HibernateUtils() throws Exception {
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Personal.class);
        configuration.addAnnotatedClass(Book.class);
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        factory = configuration.buildSessionFactory(builder.build());
        if (!isDatabaseSetUp()) {
            try {
                setUpDatabase();
            } catch (IOException e) {
                throw new Exception("Database can't be set up");
            }
        }
    }

    //If user table with admin user doesn't exist, assume none of the tables do
    private boolean isDatabaseSetUp() {
        try (Session session = factory.openSession()) {
            return session.get(User.class, 1) != null;
        } catch (SQLGrammarException e) {
            return false;
        }
    }

    //Creates database and add one admin user
    private void setUpDatabase() throws IOException {
        EntityManager manager = factory.createEntityManager();
        String script = Files.readString(new File(getClass().getResource("/create.sql").getPath()).toPath(), StandardCharsets.UTF_8);
        for (String q : script.split(";")) {
            Query query = manager.createNativeQuery(q);
            manager.getTransaction().begin();
            query.executeUpdate();
            manager.getTransaction().commit();
        }
        User user;
        try (Session session = factory.openSession()) {
            user = new User("admin", "admin");
            saveObject(session, user);
            saveObject(session, new Personal(user, Role.ADMIN));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    //Hibernate configuration happens ones on startup
    public synchronized static void createInstance() throws Exception {
        if (INSTANCE == null) {
            INSTANCE = new HibernateUtils();
        }
    }

    private static void saveObject(Session session, Object object) {
        Transaction transaction = session.beginTransaction();
        session.persist(object);
        transaction.commit();
    }

    public static void saveObject(Object object) {
        try (Session session = getFactory().openSession()) {
            saveObject(session, object);
        }
    }

    public static void updateObject(Object object) {
        try (Session session = getFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(object);
            transaction.commit();
        }
    }

    public static void deleteObject(Object object) {
        try (Session session = getFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(object);
            transaction.commit();
        }
    }

    private static SessionFactory getFactory() {
        return INSTANCE.factory;
    }

    public static User getUser(Integer id) {
        try (Session session = getFactory().openSession()) {
            return session.get(User.class, id);
        }
    }

    public static Book getBook(Integer id) {
        try (Session session = getFactory().openSession()) {
            return session.get(Book.class, id);
        }
    }

    public static User getUser(String name) {
        try (Session session = getFactory().openSession()) {
            List<User> users = session.createQuery("from User where name = '" + name + "'", User.class).getResultList();
            return users.isEmpty() ? null : users.get(0);
        }
    }

    public static List<User> getUsers(String userName, String name, int... roles) {
        if (roles.length == 0) {
            return Collections.emptyList();
        }
        try (Session session = getFactory().openSession()) {
            StringBuilder builder = new StringBuilder("from User where (");
            for (int role : roles) {
                builder.append(" personal.role = ").append(role).append(" or");
            }
            builder.delete(builder.length() - 3, builder.length()).append(")");
            addLikeCondition(builder, "name", userName, true);
            addLikeCondition(builder, "personal.name", name, true);
            return session.createQuery(builder.toString(), User.class).getResultList();
        }
    }

    public static List<Book> getAvailableBooks(User user, String title, String author, Integer genre, Boolean checkedOut, Integer checkedOutUserId) {
        if (user == null) {
            return Collections.emptyList();
        }
        try (Session session = getFactory().openSession()) {
            StringBuilder builder = new StringBuilder("from Book");
            if (!Role.CAN_EDIT_USERS.contains(user.getPersonal().getRole())) { // Readers can't view books they are too young to read
                builder.append(" where  minAge <= ").append(user.getPersonal().getAge()).append(" and (user is null or user.id = ").append(user.getId()).append(")");
            } else if (!StringUtils.isNullOrEmpty(title) || !StringUtils.isNullOrEmpty(author) || genre != null || checkedOut != null || checkedOutUserId != null) {
                builder.append(" where");
                if (checkedOutUserId != null) {
                    builder.append(" user.id = ").append(checkedOutUserId);
                }
            }
            boolean addAnd = addLikeCondition(builder, "title", title, !Role.CAN_EDIT_USERS.contains(user.getPersonal().getRole()) || checkedOutUserId != null);
            addAnd = addLikeCondition(builder, "author", author, addAnd);
            if (genre != null) {
                if (addAnd) {
                    builder.append(" and");
                }
                builder.append(" genre = ").append(genre);
                addAnd = true;
            }
            if (checkedOut != null) {
                if (addAnd) {
                    builder.append(" and");
                }
                builder.append(" user ").append(checkedOut ? "is not null" : "is null");
            }
            return session.createQuery(builder.toString(), Book.class).getResultList();
        }
    }

    //Adds like condition, returns whether the condition has been added and therefor if and before the next condition is required
    private static boolean addLikeCondition(StringBuilder builder, String name, String value, boolean addAnd) {
        if (!StringUtils.isNullOrEmpty(value)) {
            if (addAnd) {
                builder.append(" and");
            }
            builder.append(" ").append(name).append(" like '%").append(value).append("%'");
            return true;
        }
        return addAnd;
    }
}
