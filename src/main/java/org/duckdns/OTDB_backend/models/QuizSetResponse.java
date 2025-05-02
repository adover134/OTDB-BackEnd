package org.duckdns.OTDB_backend.models;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@JsonInclude(Include.NON_NULL)
public class QuizSetResponse {
	protected LocalDateTime timeStamp;
	protected int statusCode;
	protected HttpStatus status;
	protected Map<?, ?> data;
}
