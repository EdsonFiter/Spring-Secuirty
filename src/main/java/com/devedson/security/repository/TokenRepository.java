package com.devedson.security.repository;

import com.devedson.security.domain.token.Token;
import com.devedson.security.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query("""
            select t from Token t inner join User u on t.user.id = u.id
            Where u.id = :userId and (t.revoked = false or t.expired = false)
            """)
    List<Token> findAllValidTokensByUser(Long userId);

    Optional<Token> findByToken(String token);
}
