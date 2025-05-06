package microservices.book.gamification.game;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import microservices.book.gamification.game.domain.BadgeType;
import microservices.book.gamification.game.domain.LeaderBoardRow;

@AutoConfigureJsonTesters
@WebMvcTest(LeaderBoardController.class)
public class LeaderBoardControllerTest {

    @MockitoBean
    private LeaderBoardService leaderBoardService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<List<LeaderBoardRow>> jsonResultLeaderBoard;
    
    @Test
    void getLeaderBoard() throws Exception {
        // given
        List<LeaderBoardRow> expectedLeaderBoard = List.of(
                new LeaderBoardRow(3L, 170L)
                    .withBadges(List.of(
                            BadgeType.FIRST_WON.getDescription(),
                            BadgeType.BRONZE.getDescription(),
                            BadgeType.SILVER.getDescription()
                        )),
                new LeaderBoardRow(2L, 70L)
                    .withBadges(List.of(
                            BadgeType.FIRST_WON.getDescription(),
                            BadgeType.BRONZE.getDescription()
                        )),
                new LeaderBoardRow(1L, 30L)
                    .withBadges(List.of(
                            BadgeType.FIRST_WON.getDescription()
                        )));

        given(leaderBoardService.gerCurrentLeaderBoard()).willReturn(expectedLeaderBoard);

        //when
        MockHttpServletResponse response = mvc.perform(
                get("/leaders").contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString()).isEqualTo(jsonResultLeaderBoard.write(expectedLeaderBoard).getJson());

        verify(leaderBoardService).gerCurrentLeaderBoard();
    }
}
