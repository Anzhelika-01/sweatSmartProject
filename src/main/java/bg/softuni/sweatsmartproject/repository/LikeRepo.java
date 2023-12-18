package bg.softuni.sweatsmartproject.repository;

import bg.softuni.sweatsmartproject.domain.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LikeRepo extends JpaRepository<Like, UUID> {
}