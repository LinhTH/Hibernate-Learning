package com.example.demo.repository;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.springframework.stereotype.Service;

/**
 * Insprired by
 * https://developer.jboss.org/wiki/GenericDataAccessObjects?_sscc=t
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

	public List<T> findAll() {
		TypedQuery<T> query = em.createQuery("FROM " + getPersistentClass().getName(), getPersistentClass());
		return query.getResultList();
	}

	public T getOne(ID id) {
		return (T) getSession().load(getPersistentClass(), id);
	}

	@SuppressWarnings("unchecked")
	public ID save(T entity) {
		return (ID) getSession().save(entity);
	}

	private Session getSession() {
		return em.unwrap(Session.class);
	}

	private Class<T> getPersistentClass() {
		return persistentClass;
	}
}
