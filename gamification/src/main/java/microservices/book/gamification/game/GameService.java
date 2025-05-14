package microservices.book.gamification.game;

import java.util.List;

import lombok.Value;
import microservices.book.gamification.challenge.ChallengeSolvedEvent;
import microservices.book.gamification.game.domain.BadgeType;

public interface GameService {

    /**
     * Process a new attempt froma given user.
     * 
     * @param challenge the challenge data woth user details, factors, etc.
     * @return a {@link GameResult} object containing the new score and badge cards obtained.
     * @throws Exception 
     */
    GameResult newAttemptForUser(ChallengeSolvedEvent challenge);

    @Value
    class GameResult {
        int score;
        List<BadgeType> badges;
    }
}
