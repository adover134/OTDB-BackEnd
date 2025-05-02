package org.duckdns.OTDB_backend.repo;

import java.util.Collection;

import org.duckdns.OTDB_backend.models.QuizResult;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizResultRepo extends JpaRepository<QuizResult, Long>{
	Long countByUserId (String userId);
	Collection<QuizResult> findByUserId (String userId, PageRequest pR);
}
