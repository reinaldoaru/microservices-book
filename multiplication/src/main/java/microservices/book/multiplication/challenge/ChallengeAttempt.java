package microservices.book.multiplication.challenge;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservices.book.multiplication.user.User;

/**
 * Identifies the attempt from a {@link User} to solve a {@link Challenge}.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeAttempt {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;
    
    private int factorA;
    private int factorB;
    private int resultAttempt;
    private boolean correct;
    
}
