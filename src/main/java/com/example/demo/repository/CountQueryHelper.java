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
		CriteriaBuilder cb = em.getCriteriaBuilder();

		// create count query
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);

		// start copying root/joins/restrictions from the original query

		// copy roots
		for (Root<?> fromRoot : originalQuery.getRoots()) {
			Root<?> toRoot = countQuery.from(fromRoot.getJavaType());
			toRoot.alias(getOrCreateAlias(fromRoot));
			copyJoins(fromRoot, toRoot);
		}

		countQuery.select(cb.count(countQuery.from(this.typeParameterClass)));

		countQuery.groupBy(originalQuery.getGroupList());
		countQuery.distinct(originalQuery.isDistinct());
		
		// copy restrictions
		if (originalQuery.getGroupRestriction() != null) {
			countQuery.having(originalQuery.getGroupRestriction());
		}
		
		if (originalQuery.getRestriction() != null) {
			countQuery.where(originalQuery.getRestriction());
		}

		return countQuery;
	}

	public static void copyJoins(From<?, ?> from, From<?, ?> to) {
		for (Join<?, ?> j : from.getJoins()) {
			Join<?, ?> toJoin = to.join(j.getAttribute().getName(), j.getJoinType());
			toJoin.alias(getOrCreateAlias(j));
			copyJoins(j, toJoin);
		}
	}
	
	private static volatile int aliasCount = 0;

	public static synchronized <T> String getOrCreateAlias(Selection<T> selection) {
		// reset alias count
		if (aliasCount > 1000)
			aliasCount = 0;

		String alias = selection.getAlias();
		if (alias == null) {
			alias = "JDAL_generatedAlias" + aliasCount++;
			selection.alias(alias);
		}
		return alias;

	}
	
//	/https://github.com/chelu/jdal/blob/master/core/src/main/java/org/jdal/dao/jpa/JpaUtils.java
}
