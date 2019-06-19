package com.example.demo.repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

public class CountQueryHelper<T> {

	final Class<T> typeParameterClass;

	public CountQueryHelper(Class<T> typeParameterClass) {
		this.typeParameterClass = typeParameterClass;
	}

	public CriteriaQuery<Long> getCountQuery(CriteriaQuery<T> originalQuery, EntityManager em) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// create count query
		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
		
		// start copying root/joins/restrictions from the original query

		// copy roots
		int count = 0;
		for (Root<?> fromRoot : originalQuery.getRoots()) {
			Root<?> toRoot = countQuery.from(fromRoot.getJavaType());
			toRoot.alias(getOrCreateAlias(fromRoot, count++));
			copyJoins(fromRoot, toRoot);
		}
		
		countQuery.groupBy(originalQuery.getGroupList());
		countQuery.distinct(originalQuery.isDistinct());

		// copy restrictions
		if (originalQuery.getGroupRestriction() != null) {
			countQuery.having(originalQuery.getGroupRestriction());
		}

		if (originalQuery.getRestriction() != null) {
			countQuery.where(originalQuery.getRestriction());
		}

	
		Root<T> countRoot = findRoot(countQuery);
		countQuery.select(criteriaBuilder.count(countRoot));
		return countQuery;
	}

	public Root<T> findRoot(CriteriaQuery<?> query) {
		for (Root<?> r : query.getRoots()) {
			if (typeParameterClass.equals(r.getJavaType())) {
				return (Root<T>) r.as(typeParameterClass);
			}
		}
		return null;
	}

	public void copyJoins(From<?, ?> from, From<?, ?> to) {
		int count = 0;
		for (Join<?, ?> j : from.getJoins()) {
			Join<?, ?> toJoin = to.join(j.getAttribute().getName(), j.getJoinType());
			//should create alias with prefix JOIN
			toJoin.alias(getOrCreateAlias(j, count++));
			copyJoins(j, toJoin);
		}
	}

	public static  <T> String getOrCreateAlias(Selection<T> selection, int count) {
		String alias = selection.getAlias();
		if (alias == null) {
			alias = "JDAL_generatedAlias" + count;
			selection.alias(alias);
		}
		return alias;

	}

//	/https://github.com/chelu/jdal/blob/master/core/src/main/java/org/jdal/dao/jpa/JpaUtils.java
}
