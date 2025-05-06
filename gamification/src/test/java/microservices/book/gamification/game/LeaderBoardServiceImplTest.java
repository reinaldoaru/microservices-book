package microservices.book.gamification.game;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import microservices.book.gamification.game.domain.BadgeCard;
import microservices.book.gamification.game.domain.BadgeType;
import microservices.book.gamification.game.domain.LeaderBoardRow;

@ExtendWith(MockitoExtension.class)
public class LeaderBoardServiceImplTest {

    private LeaderBoardService leaderBoardService;

    @Mock
    private ScoreRepository scoreRepository;

    @Mock
    private BadgeRepository badgeRepository;

    @BeforeEach
    public void setUp() {
        leaderBoardService = new LeaderBoardServiceImpl(
            scoreRepository,
            badgeRepository
        );
    }

    @Test
    public void getCurrentLeaderBoardTest() {
        // given
        long userId1 = 1L;
        long userId2 = 2L;
        long userId3 = 3L;
        long userId4 = 4L;

        given(scoreRepository.findFirst10()).willReturn(List.of(
                new LeaderBoardRow(userId1, 500L),
                new LeaderBoardRow(userId2, 250L),
                new LeaderBoardRow(userId3, 230L),
                new LeaderBoardRow(userId4, 100L)
            ));

        given(badgeRepository.findByUserIdOrderByBadgeTimestampDesc(userId1)).willReturn(List.of(
                new BadgeCard(userId1, BadgeType.FIRST_WON),
                new BadgeCard(userId1, BadgeType.BRONZE),
                new BadgeCard(userId1, BadgeType.SILVER),
                new BadgeCard(userId1, BadgeType.GOLD)
            ));

        given(badgeRepository.findByUserIdOrderByBadgeTimestampDesc(userId2)).willReturn(List.of(
                new BadgeCard(userId2, BadgeType.FIRST_WON),
                new BadgeCard(userId2, BadgeType.BRONZE),
                new BadgeCard(userId2, BadgeType.SILVER)
            ));

        given(badgeRepository.findByUserIdOrderByBadgeTimestampDesc(userId3)).willReturn(List.of(
                new BadgeCard(userId3, BadgeType.FIRST_WON),
                new BadgeCard(userId3, BadgeType.BRONZE),
                new BadgeCard(userId2, BadgeType.SILVER)
            ));

        given(badgeRepository.findByUserIdOrderByBadgeTimestampDesc(userId4)).willReturn(List.of(
                new BadgeCard(userId4, BadgeType.FIRST_WON),
                new BadgeCard(userId4, BadgeType.BRONZE)
            ));

        // when 
        final List<LeaderBoardRow> leaderBoard = leaderBoardService.gerCurrentLeaderBoard();

        // then
        List<LeaderBoardRow> expectedLeaderBoard = List.of(
                new LeaderBoardRow(userId1, 500L, List.of(
                        BadgeType.FIRST_WON.getDescription(),
                        BadgeType.BRONZE.getDescription(),
                        BadgeType.SILVER.getDescription(),
                        BadgeType.GOLD.getDescription())),
                new LeaderBoardRow(userId2, 250L, List.of(
                        BadgeType.FIRST_WON.getDescription(),
                        BadgeType.BRONZE.getDescription(),
                        BadgeType.SILVER.getDescription())),
                new LeaderBoardRow(userId3, 230L, List.of(
                        BadgeType.FIRST_WON.getDescription(),
                        BadgeType.BRONZE.getDescription(),
                        BadgeType.SILVER.getDescription())),
                new LeaderBoardRow(userId4, 100L, List.of(
                    BadgeType.FIRST_WON.getDescription(),
                    BadgeType.BRONZE.getDescription())));

        then(leaderBoard).isEqualTo(expectedLeaderBoard);
    }
    
}
