package pl.szczecin.business;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService {

    private EntityManager entityManager;

    @Transactional(
            propagation = Propagation.REQUIRED,
            isolation = Isolation.DEFAULT
    )
    public void assignRoleToUser(int userId) {
        String sqlQuery = "INSERT INTO web_clinic_user_role (user_id, role_id) VALUES (?, 2)";
        Query query = entityManager.createNativeQuery(sqlQuery);
        query.setParameter(1, userId);
        query.executeUpdate();
    }

}
