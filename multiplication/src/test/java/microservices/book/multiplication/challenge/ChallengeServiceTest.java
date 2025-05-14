package microservices.book.multiplication.challenge;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import microservices.book.multiplication.user.User;
import microservices.book.multiplication.user.UserRepository;

@ExtendWith(MockitoExtension.class)
public class ChallengeServiceTest {

    private ChallengeService challengeService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChallengeAttemptRepository challengeAttemptRepository;

    @Mock
    private ChallengeEventPub challengeEventPub;

    @BeforeEach
    public void setUp() {
        challengeService = new ChallengeServiceImpl(
            userRepository,
            challengeAttemptRepository,
            challengeEventPub
        );
    }

    @Test
    public void checkCorrectAttemptTest() {
        // given
        ChallengeAttemptDTO attemptDTO = new ChallengeAttemptDTO(50, 60, "john_doe", 3000);
        
        given(challengeAttemptRepository.save(any())).will(returnsFirstArg());
        willDoNothing().given(challengeEventPub).challengeSolved(any());

        // when
        ChallengeAttempt resultAttempt = challengeService.verifyAttempt(attemptDTO);

        // then
        then(resultAttempt.isCorrect()).isTrue();
        // newly added lines
        verify(userRepository).save(new User("john_doe"));
        verify(challengeAttemptRepository).save(resultAttempt);
        verify(challengeEventPub).challengeSolved(resultAttempt);
    }

    @Test
    public void checkWrongAttemptTest() {
        // given
        ChallengeAttemptDTO attemptDTO = new ChallengeAttemptDTO(50, 60, "john_doe", 5000);
        given(challengeAttemptRepository.save(any())).will(returnsFirstArg());

        // when
        ChallengeAttempt resultAttempt = challengeService.verifyAttempt(attemptDTO);

        // then
        then(resultAttempt.isCorrect()).isFalse();
        // newly added lines
        verify(userRepository).save(new User("john_doe"));
        verify(challengeAttemptRepository).save(resultAttempt);
    }

    @Test
    public void checkAttemptGamificationFailTest() {
        // given
        ChallengeAttemptDTO attemptDTO = new ChallengeAttemptDTO(50, 60, "john_doe", 3000);
        
        given(challengeAttemptRepository.save(any())).will(returnsFirstArg());
        willDoNothing().given(challengeEventPub).challengeSolved(any());

        // when
        ChallengeAttempt resultAttempt = challengeService.verifyAttempt(attemptDTO);

        // then
        then(resultAttempt.isCorrect()).isTrue();
        // newly added lines
        verify(userRepository).save(new User("john_doe"));
        verify(challengeAttemptRepository).save(resultAttempt);
        verify(challengeAttemptRepository).save(resultAttempt);
    }

    @Test
    public void checkExistingUserTest(){
        // given
        User existingUser = new User(1L, "john_doe");
        
        given(userRepository.findByAlias("john_doe"))
            .willReturn(Optional.of(existingUser));

        given(challengeAttemptRepository.save(any())).will(returnsFirstArg());
        
        ChallengeAttemptDTO attemptDTO = new ChallengeAttemptDTO(50, 60, "john_doe", 5000);

        // when
        ChallengeAttempt resultAttempt = challengeService.verifyAttempt(attemptDTO);

        // then
        then(resultAttempt.isCorrect()).isFalse();
        then(resultAttempt.getUser()).isEqualTo(existingUser);
        verify(userRepository, never()).save(any());
        verify(challengeAttemptRepository).save(resultAttempt);
    }

    @Test
    public void retrieveStatsTest() {
        // given
        User user = new User("john_doe");
        ChallengeAttempt attempt1 = new ChallengeAttempt(1L, user, 50, 60, 3010, false);
        ChallengeAttempt attempt2 = new ChallengeAttempt(2L, user, 50, 60, 3051, false);
        List<ChallengeAttempt> lastAttempts = List.of(attempt1, attempt2);

        given(challengeAttemptRepository.findTop10ByUserAliasOrderByIdDesc("john_doe"))
            .willReturn(lastAttempts);

        // when
        List<ChallengeAttempt> lastAttemptsResult = challengeService.getStatsForUser("john_doe");

        // then
        then(lastAttemptsResult).isEqualTo(lastAttempts);
    }
}
