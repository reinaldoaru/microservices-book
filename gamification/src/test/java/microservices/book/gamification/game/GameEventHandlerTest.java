package microservices.book.gamification.game;

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;

import microservices.book.gamification.challenge.ChallengeSolvedEvent;

@ExtendWith(MockitoExtension.class)
public class GameEventHandlerTest {

    private GameEventHandler gameEventHandler;

    @Mock
    private GameService gameService;

    @BeforeEach
    public void setUp() {
        gameEventHandler = new GameEventHandler(gameService);
    }

    @Test
    public void handleMultiplicationSolvedExceptionTest() throws Exception {
        // given
        long attemptId = 1L;
        long userId = 10L;
        ChallengeSolvedEvent attempt = new ChallengeSolvedEvent(attemptId, true, 42, 70, userId);
        Exception serviceException = new RuntimeException("Exception thrown in GameService");
        
        given(gameService.newAttemptForUser(attempt)).willThrow(serviceException);

        // when
        final Throwable thrown = catchThrowable(() -> gameEventHandler.handleMultiplicationSolved(attempt));

        // then
        assertThat(thrown).isInstanceOf(AmqpRejectAndDontRequeueException.class);
        verify(gameService).newAttemptForUser(attempt);        
    }
}
