package pl.szczecin.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.szczecin.business.dao.UserDAO;

@Service
@AllArgsConstructor
public class UserService {

    private final UserDAO userDAO;

    @Transactional(
            propagation = Propagation.REQUIRED,
            isolation = Isolation.DEFAULT
    )
    public void assignRoleToUser(int userId) {
        userDAO.assignRoleToUser(userId);
    }

}
