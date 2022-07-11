package lt.codeacademy.provider;

import lt.codeacademy.entity.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import static org.hibernate.cfg.Environment.*;

import java.util.Properties;

public class SessionFactoryProvider {

    private static SessionFactoryProvider instance;
    private final SessionFactory sessionFactory;

    private SessionFactoryProvider() {
        Configuration configuration = new Configuration();
        configuration.setProperties(createHibernateProperties());

        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Result.class);
        configuration.addAnnotatedClass(Exam.class);
        configuration.addAnnotatedClass(ExamStats.class);
        configuration.addAnnotatedClass(Questions.class);


        sessionFactory = configuration.buildSessionFactory();
    }

    public static SessionFactoryProvider getInstance() {
        if (instance == null) {
            instance = new SessionFactoryProvider();
        }

        return instance;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private Properties createHibernateProperties() {
        Properties p = new Properties();

        p.put(DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        p.put(DRIVER, "org.postgresql.Driver");
        p.put(URL, "jdbc:postgresql://localhost/exam_db");
        p.put(USER, "postgres");
        p.put(PASS, "postgres");
        p.put(SHOW_SQL, "true");
        p.put(HBM2DDL_AUTO, "update");

        return p;
    }
}
