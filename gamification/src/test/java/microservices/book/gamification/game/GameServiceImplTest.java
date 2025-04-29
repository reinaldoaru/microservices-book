package microservices.book.gamification.game;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import microservices.book.gamification.challenge.ChallengeSolvedDTO;
import microservices.book.gamification.game.GameService.GameResult;
import microservices.book.gamification.game.badgeprocessors.BadgeProcessor;
import microservices.book.gamification.game.domain.BadgeType;
import microservices.book.gamification.game.domain.ScoreCard;

@ExtendWith(MockitoExtension.class)
public class GameServiceImplTest {

    private GameServiceImpl gameService;

    @Mock
    private BadgeProcessor badgeProcessor;

    @BeforeEach
    public void setUp() {
        gameService = new GameServiceImpl(List.of(badgeProcessor));
    }

    @Test
    public void processCorrectAttemptTest() {
        // given
        long userId = 1L, attemptId = 10L;
        var attempt = new ChallengeSolvedDTO(attemptId, true, 42, 70, userId, "john");
        ScoreCard scoreCard = new ScoreCard(userId, attemptId);

        given(badgeProcessor.badgeType()).willReturn(BadgeType.LUCKY_NUMBER);
        given(badgeProcessor.processForOptionalBadge(10, List.of(scoreCard), attempt)).willReturn(Optional.of(BadgeType.LUCKY_NUMBER));

        // when
        final GameResult gameResult = gameService.newAttemptForUser(attempt);

        // then - should score one card and win the badge LUCKY_NUMBER.
        then(gameResult).isEqualTo(new GameResult(10, List.of(BadgeType.LUCKY_NUMBER)));
    }

    @Test
    public void processWrongAttemptTest() {
        // when
        GameResult gameResult = gameService.newAttemptForUser(new ChallengeSolvedDTO(10L, false, 10, 10, 1L, "john"));

        // then - shouldn't score anything.
        then(gameResult).isEqualTo(new GameResult(0, List.of()));
    }
}
