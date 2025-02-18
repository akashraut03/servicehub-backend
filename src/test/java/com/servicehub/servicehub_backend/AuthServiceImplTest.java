//package com.servicehub.servicehub_backend;
//
//import com.servicehub.servicehub_backend.model.UserInfo;
//import com.servicehub.servicehub_backend.repository.UserRepository;
//import com.servicehub.servicehub_backend.service.AuthServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNull;
//
//public class AuthServiceImplTest {
//
//    @InjectMocks
//    private AuthServiceImpl authService;
//    @Mock
//    private UserRepository userRepository;
//    private UserInfo user;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        user = new UserInfo("Test User","t" +
//                "est@example.com", "password", "Resident");
//    }
//
//    @Test
//    void shouldRegister() {
//        Mockito.when(userRepository.save(user)).thenReturn(user);
//        String response = authService.createUser(user);
//        assertEquals("User registered successfully", response);
//        Mockito.verify(userRepository, Mockito.times(1)).save(user);
//    }
//
//    @Test
//    void shouldLogin() {
//        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
//        UserInfo response = authService.loginUser(user);
//        assertEquals(user,response);
//    }
//
//    @Test
//    void shouldNotLogInWithIncorrectData() {
//        Mockito.when(userRepository.findById("test@example.com")).thenReturn(Optional.of(user));
//        UserInfo userWithIncorrectPassword = new UserInfo("test@example.com", "Test User", "WrongPassword", "Resident");
//        UserInfo response = authService.loginUser(userWithIncorrectPassword);
//        assertNull(response);
//    }
//}
