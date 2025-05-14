package microservices.book.gamification.game;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservices.book.gamification.challenge.ChallengeSolvedEvent;

@RequiredArgsConstructor
@Slf4j
@Service
public class GameEventHandler {

    final GameService gameService;

    @RabbitListener(queues = "${amqp.queue.gamification}")
    void handleMultiplicationSolved(final ChallengeSolvedEvent event) {
        log.info("Challenge Solved event received: {}", event.getAttemptId());

        try {
            gameService.newAttemptForUser(event);
            
        } catch (final Exception e) {
            log.error("Error when trying to process ChallengeSolved event", e);
            
            // Avoid the event to be requeued and reprocessed
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }
}
