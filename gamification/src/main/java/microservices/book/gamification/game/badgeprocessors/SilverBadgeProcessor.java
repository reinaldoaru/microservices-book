package microservices.book.gamification.game.badgeprocessors;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import microservices.book.gamification.challenge.ChallengeSolvedDTO;
import microservices.book.gamification.game.domain.BadgeType;
import microservices.book.gamification.game.domain.ScoreCard;

@Component
public class SilverBadgeProcessor implements BadgeProcessor {

    @Override
    public Optional<BadgeType> processForOptionalBadge(int currentScore, List<ScoreCard> scoreCards,
            ChallengeSolvedDTO solved) {
        return currentScore > 150 ?
                Optional.of(BadgeType.SILVER) :
                Optional.empty();
    }

    @Override
    public BadgeType badgeType() {
        return BadgeType.SILVER;
    }

}
