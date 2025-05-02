package org.duckdns.OTDB_backend.resources;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

import org.duckdns.OTDB_backend.implementation.QuizResultServiceImplementation;
import org.duckdns.OTDB_backend.models.QuizRequirements;
import org.duckdns.OTDB_backend.models.QuizResult;
import org.duckdns.OTDB_backend.models.QuizSetRequest;
import org.duckdns.OTDB_backend.models.QuizSetResponse;
import org.duckdns.OTDB_backend.models.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/quiz")
@RequiredArgsConstructor
@CrossOrigin(origins = "https://adover134.github.io", allowCredentials = "true")
public class QuizResource {
	private final QuizResultServiceImplementation quizResultService;

    @PostMapping("/save")
    public ResponseEntity<Response> save(@RequestBody QuizSetRequest q, final HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("quizState", q);
        System.out.println("yo!!!! "+session.getId());
        return ResponseEntity.ok(
            Response.builder()
                .timeStamp(LocalDateTime.now())
                .data(Map.of("result", "done!"))
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build()
        );
	}

    @PostMapping("/condition")
    public ResponseEntity<Response> saveCondition(@RequestBody QuizRequirements q, final HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("quizRequirements", q);
        return ResponseEntity.ok(
            Response.builder()
                .timeStamp(LocalDateTime.now())
                .data(Map.of("result", "done!"))
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build()
        );
	}

    @GetMapping("/current")
    public ResponseEntity<QuizSetResponse> quizSet(final HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object k = session.getAttribute("quizState");
        System.out.println("HeyYo!!!! "+session.getId());
        System.out.println(k);
        return ResponseEntity.ok(
            QuizSetResponse.builder()
                .timeStamp(LocalDateTime.now())
                .data(Map.of("result", k))
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build()
        );
    }

    @PostMapping("/save_result")
    public ResponseEntity<QuizSetResponse> quizResultSave(final HttpServletRequest request) {
        HttpSession session = request.getSession();
        // 여기서 우선 퀴즈 상태를 받는다.
        QuizSetRequest q = (QuizSetRequest) session.getAttribute("quizState");
        QuizRequirements qR = (QuizRequirements) session.getAttribute("quizRequirements");
        Integer correctNum = q.getQuizSet().stream()
                .filter(qr -> qr.getCorrect())
                .collect(Collectors.toList()).size();
        
        session.setAttribute("correctNum", correctNum);
        session.removeAttribute("quizState");

        QuizResult QR = new QuizResult();
        QR.setAmount(qR.getAmount());
        QR.setCategory(qR.getCategory());
        QR.setCorrects(correctNum);
        QR.setDifficulty(qR.getDifficulty());
        QR.setType(qR.getType());
        QR.setUserId((String)session.getAttribute("userId"));
        return ResponseEntity.ok(
            QuizSetResponse.builder()
                .timeStamp(LocalDateTime.now())
                // 로그인이 되어 있다면 DB에 저장한다.
                .data(Map.of("result", session.getAttribute("userId")!=null?this.quizResultService.create(QR):"Yay"))
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build()
        );
    }

    @GetMapping("/reset")
    public ResponseEntity<QuizSetResponse> quizReset(final HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer cN = 0;
        Integer tN = 0;
        if (session.getAttribute("correctNum")!=null)
        {
            cN = (Integer)session.getAttribute("correctNum");
            session.removeAttribute("correctNum");
        }
        if (session.getAttribute("quizRequirements")!=null)
        {
            tN = ((QuizRequirements)session.getAttribute("quizRequirements")).getAmount();
            session.removeAttribute("quizRequirements");
        }
        if (session.getAttribute("quizState") != null)
            session.removeAttribute("quizState");
        return ResponseEntity.ok(
            QuizSetResponse.builder()
                .timeStamp(LocalDateTime.now())
                .data(Map.of("corrects", cN, "total", tN))
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build()
        );
    }

    @GetMapping("/historyPages")
    public ResponseEntity<QuizSetResponse> solvingHistorySize(final HttpServletRequest request){
        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");
        return ResponseEntity.ok(
            QuizSetResponse.builder()
                .timeStamp(LocalDateTime.now())
                .data(Map.of("result", quizResultService.maxPage(userId)))
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build()
        );
    }

    @GetMapping("/history/{page}")
    public ResponseEntity<QuizSetResponse> solvingHistory(final HttpServletRequest request, @PathVariable("page") Integer page) {
        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");
        System.out.println(page);
        return ResponseEntity.ok(
            QuizSetResponse.builder()
                .timeStamp(LocalDateTime.now())
                .data(Map.of("result", quizResultService.list(userId, page)))
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build()
        );
    }
}
