package org.duckdns.OTDB_backend.implementation;

import java.util.Collection;

import org.duckdns.OTDB_backend.models.QuizResult;
import org.duckdns.OTDB_backend.repo.QuizResultRepo;
import org.duckdns.OTDB_backend.service.QuizReultService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class QuizResultServiceImplementation implements QuizReultService{
    private final QuizResultRepo quizResultRepo;

    @Override
	public QuizResult create(QuizResult qR) {
		log.info("Saving new solving result of user: {}", qR.getUserId());
		return quizResultRepo.save(qR);
	}
	
	@Override
	public Collection<QuizResult> list(String uI, Integer page) {
		log.info("Retrieving solving results of user: {}", uI);
		return quizResultRepo.findByUserId(uI, PageRequest.of(page-1, 10));
	}

	public Long maxPage(String uI) {
		return (Long)((quizResultRepo.countByUserId(uI)-1)/10) + 1;
	}
}
