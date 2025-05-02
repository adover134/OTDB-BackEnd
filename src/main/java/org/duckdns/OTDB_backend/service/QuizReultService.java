package org.duckdns.OTDB_backend.service;

import java.util.Collection;

import org.duckdns.OTDB_backend.models.QuizResult;

public interface QuizReultService {
    QuizResult create(QuizResult qR);
    Collection<QuizResult> list(String uI, Integer page);
    Long maxPage(String uI);
}
