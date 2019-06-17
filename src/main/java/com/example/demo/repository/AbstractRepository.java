package com.example.demo.repository;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.hibernate.LockMode;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

/**
 * Inspired by https://developer.jboss.org/wiki/GenericDataAccessObjects?_sscc=t
 */

@Service
public abstract class AbstractRepository<T, ID extends Serializable> {
	@PersistenceContext
	protected EntityManager em;

	private Class<T> persistentClass;

	@SuppressWarnings("unchecked")
	public AbstractRepository() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	// TODO this query generates lots of queries
	public List<T> findAll() {
		TypedQuery<T> query = em.createQuery("FROM " + getPersistentClass().getName(), getPersistentClass());
		return query.getResultList();
	}

	public T findById(ID id) {
		return (T) em.find(getPersistentClass(), id);
	}

	public T findById(ID id, boolean lock) {
		T entity;
		if (lock) {
			entity = (T) getSession().load(getPersistentClass(), id, LockMode.PESSIMISTIC_WRITE);
		} else {
			entity = (T) getSession().load(getPersistentClass(), id);
		}

		return entity;
	}

	public T makePersistent(T entity) {
		getSession().saveOrUpdate(entity);
		return entity;
	}

	public void makeTransient(T entity) {
		getSession().delete(entity);
	}

	public void flush() {
		getSession().flush();
	}

	protected Session getSession() {
		return em.unwrap(Session.class);
	}

	private Class<T> getPersistentClass() {
		return persistentClass;
	}
}
