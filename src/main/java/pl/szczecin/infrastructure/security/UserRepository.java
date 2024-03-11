package pl.szczecin.infrastructure.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pl.szczecin.business.dao.UserDAO;

public interface UserRepository extends UserDAO, JpaRepository<UserEntity, Long> {

//    UserEntity findByUserName(String userName);

    UserEntity findByEmail(String email);

    @Modifying
    @Query(
            value = "INSERT INTO web_clinic_user_role (user_id, role_id) VALUES (?1, 2)",
            nativeQuery = true)
    void assignRoleToUser(int userId);
}

