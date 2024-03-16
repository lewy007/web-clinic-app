package pl.szczecin.business;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.szczecin.business.dao.UserDAO;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserService userService;


    @BeforeEach
    public void setUp() {
        Assertions.assertNotNull(userDAO);
    }


    @Test
    @DisplayName("That method should correctly assign role to user")
    void shouldCorrectlyAssignRoleToUser() {
        //given
        int userId = 123;

        //when
        userService.assignRoleToUser(userId);

        //then
        Mockito.verify(userDAO, Mockito.times(1)).assignRoleToUser(userId);
    }

}