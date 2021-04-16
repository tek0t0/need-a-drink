package bg.softuni.needadrink.repositiry;

import bg.softuni.needadrink.domain.entities.CommentEntity;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;


@Registered
public interface CommentRepository extends JpaRepository<CommentEntity, String> {


}
