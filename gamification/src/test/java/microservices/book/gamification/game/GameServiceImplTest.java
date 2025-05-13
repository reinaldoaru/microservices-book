package microservices.book.gamification.game;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import microservices.book.gamification.challenge.ChallengeSolvedEvent;
import microservices.book.gamification.game.GameService.GameResult;
import microservices.book.gamification.game.badgeprocessors.BadgeProcessor;
import microservices.book.gamification.game.domain.BadgeCard;
import microservices.book.gamification.game.domain.BadgeType;
import microservices.book.gamification.game.domain.ScoreCard;

@ExtendWith(MockitoExtension.class)
public class GameServiceImplTest {

    private GameServiceImpl gameService;

    @Mock
    private ScoreRepository scoreRepository;

    @Mock
    private BadgeRepository badgeRepository;

    @Mock
    private BadgeProcessor badgeProcessor;

    @BeforeEach
    public void setUp() {
        gameService = new GameServiceImpl(
            scoreRepository,
            badgeRepository,
            List.of(badgeProcessor));
    }

    @Test
    public void processCorrectAttemptLuckyNumberTest() {
        // given
        long userId = 1L, attemptId = 10L;
        var attempt = new ChallengeSolvedEvent(attemptId, true, 42, 70, userId);
        ScoreCard scoreCard = new ScoreCard(userId, attemptId);

        given(scoreRepository.getTotalScoreForUser(userId)).willReturn(Optional.of(10));

        given(badgeRepository.findByUserIdOrderByBadgeTimestampDesc(userId)).willReturn(List.of(new BadgeCard(userId, BadgeType.FIRST_WON)));

        given(badgeProcessor.badgeType()).willReturn(BadgeType.LUCKY_NUMBER);
        given(badgeProcessor.processForOptionalBadge(10, List.of(scoreCard), attempt)).willReturn(Optional.of(BadgeType.LUCKY_NUMBER));

        // when
        final GameResult gameResult = gameService.newAttemptForUser(attempt);

        // then - should score one card and win the badge LUCKY_NUMBER.
        then(gameResult).isEqualTo(new GameResult(10, List.of(BadgeType.LUCKY_NUMBER)));

        verify(scoreRepository).save(scoreCard);

        verify(badgeRepository).saveAll(List.of(new BadgeCard(userId, BadgeType.LUCKY_NUMBER)));
    }

    @Test
    public void processCorrectAttemptFirstWonTest() {
        // given
        long userId = 1L, attemptId = 10L;
        var attempt = new ChallengeSolvedEvent(attemptId, true, 42, 70, userId);
        ScoreCard scoreCard = new ScoreCard(userId, attemptId);

        given(scoreRepository.getTotalScoreForUser(userId)).willReturn(Optional.of(10));

        given(badgeRepository.findByUserIdOrderByBadgeTimestampDesc(userId)).willReturn(List.of());

        given(badgeProcessor.badgeType()).willReturn(BadgeType.FIRST_WON);
        given(badgeProcessor.processForOptionalBadge(10, List.of(scoreCard), attempt)).willReturn(Optional.of(BadgeType.FIRST_WON));

        // when
        final GameResult gameResult = gameService.newAttemptForUser(attempt);

        // then - should score one card and win the badge FIRST_WON.
        then(gameResult).isEqualTo(new GameResult(10, List.of(BadgeType.FIRST_WON)));

        verify(scoreRepository).save(scoreCard);

        verify(badgeRepository).saveAll(List.of(new BadgeCard(userId, BadgeType.FIRST_WON)));
    }

    @Test
    public void processCorrectAttemptGoldTest() {
        // given
        long userId = 1L, attemptId = 10L;
        var attempt = new ChallengeSolvedEvent(attemptId, true, 20, 70, userId);
        ScoreCard scoreCard = new ScoreCard(userId, attemptId);

        given(scoreRepository.getTotalScoreForUser(userId)).willReturn(Optional.of(410));

        given(badgeRepository.findByUserIdOrderByBadgeTimestampDesc(userId)).willReturn(List.of(
                new BadgeCard(userId, BadgeType.FIRST_WON),
                new BadgeCard(userId, BadgeType.BRONZE),
                new BadgeCard(userId, BadgeType.SILVER)
            ));

        given(badgeProcessor.badgeType()).willReturn(BadgeType.GOLD);
        given(badgeProcessor.processForOptionalBadge(410, List.of(scoreCard), attempt)).willReturn(Optional.of(BadgeType.GOLD));

        // when
        final GameResult gameResult = gameService.newAttemptForUser(attempt);

        // then - should score one card and win the badge GOLD.
        then(gameResult).isEqualTo(new GameResult(10, List.of(BadgeType.GOLD)));

        verify(scoreRepository).save(scoreCard);

        verify(badgeRepository).saveAll(List.of(new BadgeCard(userId, BadgeType.GOLD)));
    }

    @Test
    public void processCorrectAttemptSilverTest() {
        // given
        long userId = 1L, attemptId = 10L;
        var attempt = new ChallengeSolvedEvent(attemptId, true, 20, 70, userId);
        ScoreCard scoreCard = new ScoreCard(userId, attemptId);

        given(scoreRepository.getTotalScoreForUser(userId)).willReturn(Optional.of(160));

        given(badgeRepository.findByUserIdOrderByBadgeTimestampDesc(userId)).willReturn(List.of(
                new BadgeCard(userId, BadgeType.FIRST_WON),
                new BadgeCard(userId, BadgeType.BRONZE)
            ));

        given(badgeProcessor.badgeType()).willReturn(BadgeType.SILVER);
        given(badgeProcessor.processForOptionalBadge(160, List.of(scoreCard), attempt)).willReturn(Optional.of(BadgeType.SILVER));

        // when
        final GameResult gameResult = gameService.newAttemptForUser(attempt);

        // then - should score one card and win the badge LUCKY_NUMBER.
        then(gameResult).isEqualTo(new GameResult(10, List.of(BadgeType.SILVER)));

        verify(scoreRepository).save(scoreCard);

        verify(badgeRepository).saveAll(List.of(new BadgeCard(userId, BadgeType.SILVER)));
    }

    @Test
    public void processCorrectAttemptBronzeTest() {
        // given
        long userId = 1L, attemptId = 10L;
        var attempt = new ChallengeSolvedEvent(attemptId, true, 20, 70, userId);
        ScoreCard scoreCard = new ScoreCard(userId, attemptId);

        given(scoreRepository.getTotalScoreForUser(userId)).willReturn(Optional.of(60));

        given(badgeRepository.findByUserIdOrderByBadgeTimestampDesc(userId)).willReturn(List.of(
                new BadgeCard(userId, BadgeType.FIRST_WON)
            ));

        given(badgeProcessor.badgeType()).willReturn(BadgeType.BRONZE);
        given(badgeProcessor.processForOptionalBadge(60, List.of(scoreCard), attempt)).willReturn(Optional.of(BadgeType.BRONZE));

        // when
        final GameResult gameResult = gameService.newAttemptForUser(attempt);

        // then - should score one card and win the badge BRONZE.
        then(gameResult).isEqualTo(new GameResult(10, List.of(BadgeType.BRONZE)));

        verify(scoreRepository).save(scoreCard);

        verify(badgeRepository).saveAll(List.of(new BadgeCard(userId, BadgeType.BRONZE)));
    }

    @Test
    public void processWrongAttemptTest() {
        // when
        GameResult gameResult = gameService.newAttemptForUser(new ChallengeSolvedEvent(10L, false, 10, 10, 1L));

        // then - shouldn't score anything.
        then(gameResult).isEqualTo(new GameResult(0, List.of()));
    }
}
