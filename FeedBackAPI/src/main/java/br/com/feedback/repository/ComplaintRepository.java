package br.com.feedback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.feedback.to.Complaint;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Integer> {

}
