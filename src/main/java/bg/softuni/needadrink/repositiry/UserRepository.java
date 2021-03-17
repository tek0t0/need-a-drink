package bg.softuni.needadrink.repositiry;


import bg.softuni.needadrink.domain.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<Object> findByEmail(String email);
}
