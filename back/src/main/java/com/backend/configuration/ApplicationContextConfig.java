package com.backend.configuration;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.backend.dao.UserDao;
import com.backend.daoImpl.UserDaoImpl;
import com.backend.model.User;

@Configuration
@ComponentScan("com.backend")
@EnableTransactionManagement
public class ApplicationContextConfig {
	private static final Logger log = 
			LoggerFactory.getLogger(ApplicationContextConfig.class);
	@Bean(name = "dataSource")
	public DataSource getOracleDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();

		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setUrl("jdbc:h2:tcp://localhost/~/backendtest");

		dataSource.setUsername("sa"); // Schema name
		dataSource.setPassword("");

		Properties connectionProperties = new Properties();

		connectionProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialectt");
		connectionProperties.setProperty("hibernate.hbm2ddl.auto", "update");
		connectionProperties.setProperty("hibernate.show_sql", "true");
		connectionProperties.setProperty("hibernate.format_sql", "true");
	
		dataSource.setConnectionProperties(connectionProperties);
		return dataSource;
	}

	@Autowired
	@Bean(name = "sessionFactory")
	public SessionFactory getSessionFactory(DataSource dataSource) {
		LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
		// sessionBuilder.addProperties(getHibernateProperties());
		sessionBuilder.addAnnotatedClass(User.class);
    	//sessionBuilder.addAnnotatedClass(Blog.class);
		//sessionBuilder.addAnnotatedClass(Job.class);
		//sessionBuilder.addAnnotatedClass(Forum.class);
		// sessionBuilder.addAnnotatedClass(Chat.class);
		// sessionBuilder.addAnnotatedClass(Event.class);
		// sessionBuilder.addAnnotatedClass(Friend.class);

		// sessionBuilder.addAnnotatedClass(JobApplication.class);

		// sessionBuilder.addAnnotatedClass(ChatForum.class);
		// sessionBuilder.addAnnotatedClass(ChatForumComment.class);*/
		// sessionBuilder.addAnnotatedClass(User.class);

		return sessionBuilder.buildSessionFactory();
	}

	@Autowired
	@Bean(name = "transactionManager")
	public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory) {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);

		return transactionManager;
	}

	@Autowired
	@Bean(name = "userDetailsDAO")
	public UserDao getUserDao(SessionFactory sessionFactory) {
		return new UserDaoImpl(sessionFactory);
	}
}
