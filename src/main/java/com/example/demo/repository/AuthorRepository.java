package com.example.demo.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.springframework.stereotype.Service;

import com.example.demo.model.Author;
import com.example.demo.model.Author_;

@Service
public class AuthorRepository extends AbstractRepository<Author, Long> {
	public Builder createQuery() {
		return new Builder(em);
	}

	public static final class Builder {
		private EntityManager entityManager;
		private CriteriaBuilder criteriaBuilder;
		private CriteriaQuery<Author> query;
		private Root<Author> root;
//		private final String ROOT_ALIAS = "authorRootAlias";

		List<Predicate> predicates = new ArrayList<>();

		Builder(EntityManager entityManager) {
			this.entityManager = entityManager;
			criteriaBuilder = entityManager.getCriteriaBuilder();
			query = criteriaBuilder.createQuery(Author.class);
			root = query.from(Author.class);
//			root.alias(ROOT_ALIAS);
		}

		public Builder withId(Long id) {
			predicates.add(criteriaBuilder.equal(root.get(Author_.ID), id));
			return this;
		}

		public Builder withFirstName(String firstName) {
			predicates.add(criteriaBuilder.equal(root.get(Author_.FIRST_NAME), firstName));
			return this;
		}

		public Builder withLastName(String lastName) {
			predicates.add(criteriaBuilder.equal(root.get(Author_.LAST_NAME), lastName));
			return this;
		}

		public Builder withEmail(String email) {
			predicates.add(criteriaBuilder.equal(root.get(Author_.EMAIL), email));
			return this;
		}

		public Builder withBirhtdate(Date birthdate) {
			predicates.add(criteriaBuilder.equal(root.get(Author_.BIRTHDATE), birthdate));
			return this;
		}

		public List<Author> getResultList() {
			query.select(root);

			if (!predicates.isEmpty()) {
				query.where(predicates.toArray(new Predicate[] {}));
			}

			return entityManager.createQuery(query).getResultList();
		}

		public Long count() {
//			CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
//			Root<Author> countRoot = countQuery.from(query.getResultType());
//			countRoot.alias(getOrCreateAlias(root)); // use the same alias in order to match the restrictions part and
//														// the selection part
//			countQuery.select(criteriaBuilder.count(countRoot));
//			if (!predicates.isEmpty()) {
//				countQuery.where(predicates.toArray(new Predicate[] {}));
//			}
			
			
			
			CriteriaQuery<Long> countQuery = new CountQueryHelper<Author>(Author.class).getCountQuery(query, entityManager);
			
			if (!predicates.isEmpty()) {
				countQuery.where(predicates.toArray(new Predicate[] {}));
			}

			return entityManager.createQuery(countQuery).getSingleResult();
		}

		public Author getSingleResult() {
			query.select(root);

			if (!predicates.isEmpty()) {
				query.where(predicates.toArray(new Predicate[] {}));
			}

			return entityManager.createQuery(query).getSingleResult();
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

	}
}
