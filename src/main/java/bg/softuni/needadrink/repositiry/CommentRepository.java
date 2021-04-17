package bg.softuni.needadrink.repositiry;

import bg.softuni.needadrink.domain.entities.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, String> {


    void deleteAllByAuthor_Id(String id);
}
