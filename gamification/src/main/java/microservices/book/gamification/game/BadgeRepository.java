package microservices.book.gamification.game;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import microservices.book.gamification.game.domain.BadgeCard;

/**
 * Handles data operation with BadgeCards.
 */
public interface BadgeRepository extends CrudRepository<BadgeCard, Long> {

    /**
     * Retrieves all BadgeCards from a given user.
     * 
     * @param userId the id oof rhe user to look for BadgeCards
     * @return t
     */
    List<BadgeCard> findByUserIdOrderByBadgeTimestampDesc(Long userId);
    
}
