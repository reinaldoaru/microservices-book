package microservices.book.gamification.game;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservices.book.gamification.challenge.ChallengeSolvedDTO;
import microservices.book.gamification.game.badgeprocessors.BadgeProcessor;
import microservices.book.gamification.game.domain.BadgeCard;
import microservices.book.gamification.game.domain.BadgeType;
import microservices.book.gamification.game.domain.ScoreCard;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    // Spring injects all the @Component beans in this list.
    private final List<BadgeProcessor> badgeProcessors;

    @Override
    public GameResult newAttemptForUser(ChallengeSolvedDTO challenge) {
        // We give points only if it's correct
        if (challenge.isCorrect()) {
            ScoreCard scoreCard = new ScoreCard(challenge.getUserId(), challenge.getAttemptId());
            // save
            log.info("User {} scored {} points for attempt id {}", challenge.getUserAlias(), scoreCard.getScore(), challenge.getAttemptId());

            List<BadgeCard> badgeCards = processForBadges(challenge);
            
            return new GameResult(scoreCard.getScore(), badgeCards.stream()
                    .map(BadgeCard::getBadgeType)
                    .collect(Collectors.toList()));
        } else {
            log.info("Attempt id {} is not correct. User {} does not get score.", challenge.getAttemptId(), challenge.getUserAlias());

            return new GameResult(0, List.of());
        }
    }

    /**
     * Checks the total score and the different score cards obtained
     * to give new badges incase their conditions are met.
     */
    private List<BadgeCard> processForBadges(final ChallengeSolvedDTO solvedChallenge) {
        Optional<Integer> optTotalScore = Optional.of(10); // get from BD.

        if (optTotalScore.isEmpty()) return Collections.emptyList();
        
        int totalScore = optTotalScore.get();

        // Gets the total score and existing badges for that user.
        List<ScoreCard> scoreCards = List.of(new ScoreCard(1L, 10L));

        Set<BadgeType> alreadyGotBadges = Collections.emptySet();

        // Calls the badge processors for badges thet the user doesn't have yet.
        List<BadgeCard> newBadgeCards = badgeProcessors.stream()
                .filter(bp -> !alreadyGotBadges.contains(bp.badgeType()))
                .map(bp -> bp.processForOptionalBadge(totalScore, scoreCards, solvedChallenge))
                .flatMap(Optional::stream) // returns an empty stream if empty
                .map(badgeType -> new BadgeCard(solvedChallenge.getUserId(), badgeType))
                .collect(Collectors.toList());

        // saveAll

        return newBadgeCards;
    }

}
