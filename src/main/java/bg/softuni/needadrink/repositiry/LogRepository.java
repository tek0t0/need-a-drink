package bg.softuni.needadrink.repositiry;


import bg.softuni.needadrink.domain.entities.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, String> {

    Optional<LogEntity> findLogByUsername(String username);

}
