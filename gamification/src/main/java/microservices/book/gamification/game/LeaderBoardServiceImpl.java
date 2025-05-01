package microservices.book.gamification.game;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservices.book.gamification.game.domain.LeaderBoardRow;

@Service
@Slf4j
@RequiredArgsConstructor
public class LeaderBoardServiceImpl implements LeaderBoardService {

    private final ScoreRepository scoreRepository;
    private final BadgeRepository badgeRepository;

    @Override
    public List<LeaderBoardRow> gerCurrentLeaderBoard() {
        // Get score only.
        List<LeaderBoardRow> scoreOnly = scoreRepository.findFirst10();

        // Combine with badges.
        return scoreOnly.stream().map(row -> {
                List<String> badges = badgeRepository.findByUserIdOrderByBadgeTimestampDesc(
                        row.getUserId()).stream()
                                .map(b -> b.getBadgeType().getDescription())
                                .collect(Collectors.toList());
                
                return row.withBadges(badges);
            }).collect(Collectors.toList());
    }

}
