package br.com.feedback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.feedback.to.Assessment;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Integer> {

}
