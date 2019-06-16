package com.example.demo.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Service;

import com.example.demo.model.Employee;

@Deprecated
@Service
public class EmployeeRepository extends AbstractRepository<Employee, Long> {
	 
	 public Builder createQuery() {
	        return new Builder(em);
	    }
		
		public static final class Builder {
	        private EntityManager entityManager;
	        private CriteriaBuilder criteriaBuilder;
	        private CriteriaQuery<Employee> query;
	        private Root<Employee> root;
	        
	        List<Predicate> predicates = new ArrayList<>();
	        
			Builder(EntityManager entityManager) {
				this.entityManager = entityManager;
	            criteriaBuilder = entityManager.getCriteriaBuilder();
	            query = criteriaBuilder.createQuery(Employee.class);
	            root = query.from(Employee.class);
			}
			
	        public Builder withFirstName(String firstName) {
	            predicates.add(criteriaBuilder.equal(root.get("firstName"), firstName));
	            return this;
	        }
	        
	        public Builder withLastName(String lastName) {
	            predicates.add(criteriaBuilder.equal(root.get("lastName"), lastName));
	            return this;
	        }
	        
	        public Builder withPhoneNumber(String phoneNumber) {
	            predicates.add(criteriaBuilder.equal(root.get("phoneNumber"), phoneNumber));
	            return this;
	        }
	        
	        public List<Employee> getResultList() {
	            query.select(root);

	            if (!predicates.isEmpty()) {
	                query.where(predicates.toArray(new Predicate[]{}));
	            }

	            return entityManager.createQuery(query).getResultList();
	        }

	        public Employee getSingleResult() {
	            query.select(root);

	            if (!predicates.isEmpty()) {
	                query.where(predicates.toArray(new Predicate[]{}));
	            }

	            return entityManager.createQuery(query).getSingleResult();
	        }
		}
}
