package org.duckdns.OTDB_backend.resources;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.duckdns.OTDB_backend.models.QuizSetResponse;
import org.duckdns.OTDB_backend.models.Response;
import org.duckdns.OTDB_backend.models.TokenRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "https://adover134.github.io", allowCredentials = "true")
public class AuthResource {
    private static final String CLIENT_ID = "436391313932-h33hj7qjd236st6ejmp2s4vsn1k7mnkr.apps.googleusercontent.com";

    @PostConstruct
    public void init() {
        System.out.println("✅ AuthResource loaded");
    }

    @PostMapping("/login/{authProvider}")
    public ResponseEntity<Response> logIn(@PathVariable("authProvider") String authProvider,
        @RequestBody TokenRequest t, final HttpServletRequest request) {
            System.out.println("idTokenString: " + t.getCredential());
            String result =
            switch (authProvider) {
                case "google" -> this.GoogleAuth(t.getCredential(), request.getSession());
                default -> "";
            };
            return ResponseEntity.ok(
                Response.builder()
                    .timeStamp(LocalDateTime.now())
                    .data(Map.of("result", result))
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatus.OK.value())
                    .build()
            );
	}

    // get 요청을 받으며, 세션에서 로그인 관련 정보들을 제거한다.
    @GetMapping("/logout")
    public ResponseEntity<Response> logOut(final HttpServletRequest request) {
        String result;
        System.out.println("test");
        try {
            HttpSession session = request.getSession();
            session.removeAttribute("userId");
            session.removeAttribute("quizState");
            result = "Log Out success.";
        }
        catch (Exception e) {
            result = "Error: session was ended";
        }
        return ResponseEntity.ok(
            Response.builder()
                .timeStamp(LocalDateTime.now())
                .data(Map.of("result", result))
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build()
        );
    }

    @GetMapping("/loggedIn")
    public ResponseEntity<Response> LoggedIn(HttpSession session) {
        boolean k = false;
        System.out.println("UserInfo " + session.getAttribute("userId"));
        if (session.getAttribute("userId") != null)
            k = true;
        return ResponseEntity.ok(
            Response.builder()
                .timeStamp(LocalDateTime.now())
                .data(Map.of("result", k))
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build()
        );
    }

    private String GoogleAuth(String idTokenString, HttpSession session) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance()
            )
            .setAudience(Collections.singletonList(CLIENT_ID))
            .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                // 사용자 정보 가져오기
                String userId = payload.getSubject(); // Google user ID

                session.setAttribute("userId", userId);
                System.out.println("UserInfo " + session.getAttribute("userId"));

                return "Log In success.";
            } else {
                return "Invalid ID token.";
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
