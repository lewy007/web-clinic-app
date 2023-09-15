package pl.szczecin.infrastructure.security;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUserName(String userName);

//    UserEntity findByEmail(String email);
}
