package microservices.book.gamification.game;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpServletResponse;

import microservices.book.gamification.challenge.ChallengeSolvedDTO;
import microservices.book.gamification.game.GameService.GameResult;
import microservices.book.gamification.game.domain.BadgeType;

@AutoConfigureJsonTesters
@WebMvcTest(GameController.class)
public class GameControllerTest {

    @MockitoBean
    private GameService gameService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<ChallengeSolvedDTO> jsonRequestChallengeSolved;
    
    @Test
    void postResultTest() throws Exception {
        // given
        long attemptId = 10L;
        long userId = 1L;

        ChallengeSolvedDTO challengeSolvedDTO = new ChallengeSolvedDTO(attemptId, true, 20, 70, userId, "john");
        GameResult expectedResponse = new GameResult(10, List.of(BadgeType.GOLD));

        given(gameService.newAttemptForUser(eq(challengeSolvedDTO))).willReturn(expectedResponse);

        // when
        MockHttpServletResponse response = mvc.perform(
                post("/attempts").contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequestChallengeSolved.write(challengeSolvedDTO).getJson()))
            .andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        verify(gameService).newAttemptForUser(challengeSolvedDTO);
    }
}
