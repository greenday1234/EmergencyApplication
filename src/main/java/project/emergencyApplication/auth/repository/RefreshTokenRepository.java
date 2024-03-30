package project.emergencyApplication.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.emergencyApplication.auth.entity.RefreshToken;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    // AccessToken 만료 시 사용
    Optional<RefreshToken> findByKey(String key);
}
