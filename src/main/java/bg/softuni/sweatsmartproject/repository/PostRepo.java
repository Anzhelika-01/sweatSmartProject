package bg.softuni.sweatsmartproject.repository;

import bg.softuni.sweatsmartproject.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostRepo extends JpaRepository<Post, UUID> {

    Post getPostById (UUID id);
}