package com.backend.daoImpl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.backend.dao.UserDao;
import com.backend.model.User;

@Repository("userDao")
public class UserDaoImpl implements UserDao{

	@Autowired 
	SessionFactory sessionFactory;
	
	@Autowired
	User user;
	
	private static final Logger log=LoggerFactory.getLogger(UserDaoImpl.class);
	
	public UserDaoImpl(SessionFactory sessionFactory)
	{
		log.debug("Starting of Constructor UserDaoImpl ");
		this.sessionFactory=sessionFactory;
		log.debug("Ending of Constructor UserDaoImpl ");
	}
	
	
	
	@Transactional
	public boolean save(User user) {
		
		log.debug("Starting of the method save ");
		try {
			sessionFactory.getCurrentSession().save(user);
			return true;
		} catch (HibernateException e) {
			
			e.printStackTrace();
			return false;
		}
		
		
	}

	@Transactional
	public boolean update(User user) {
		
		log.debug("Starting of the method update ");
		try {
			sessionFactory.getCurrentSession().update(user);
			return true;
		} catch (HibernateException e) {
			
			e.printStackTrace();
			return false;
		}
		
	}

	@Transactional
	public User get(String id) {
		
		log.debug("Starting of the method get id="+id);
	
		user= (User)sessionFactory.getCurrentSession().get(User.class,id);
		
		//log.debug("ending of the method get with name=  "+user.getName());
		return user;
	}
 
	@Transactional
	public List<User> list() {
		
		log.debug("Starting of the method list ");
		String q="from User";
		Query query=sessionFactory.getCurrentSession().createQuery(q);
		log.debug("Ending of the method list ");
		return query.list();
		
	}

	@Transactional
	public User validate(String id, String password) {
		
		log.debug("Starting of the method validate ");
		String q="from User where id='"+id+"' and password='"+password+"'";
		Query query=sessionFactory.getCurrentSession().createQuery(q);
		log.debug("Ending of the method validate ");
		return (User)query.uniqueResult();
	
	}

}
