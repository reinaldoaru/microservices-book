package microservices.book.multiplication.challenge;

import java.util.List;

public interface ChallengeService {

    /**
     * Verifies if an attempt coming from the presentation layer is correct or not.
     * 
     * @return the resulting {@link ChallengeAttempt} object.
     */
    ChallengeAttempt verifyAttempt(ChallengeAttemptDTO resultAttempt);

    /**
     * Gets the statistics for a given user.
     */
    List<ChallengeAttempt> getStatsForUser(String userAlias);
    
}
