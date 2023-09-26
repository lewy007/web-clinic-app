package pl.szczecin.business;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private EntityManager entityManager;

    @Transactional
    public void assignRoleToUser(int userId) {
        String sqlQuery = "INSERT INTO web_clinic_user_role (user_id, role_id) VALUES (?, 2)";
        Query query = entityManager.createNativeQuery(sqlQuery);
        query.setParameter(1, userId);
        query.executeUpdate();
    }

}
