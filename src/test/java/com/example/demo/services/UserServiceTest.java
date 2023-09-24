package com.example.demo.services;

import com.example.demo.domain.enums.UserRole;
import com.example.demo.domain.project.dto.ProjectDTO;
import com.example.demo.domain.user.User;
import com.example.demo.domain.user.dto.UserCreationDTO;
import com.example.demo.domain.user.dto.UserDTO;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.exceptions.EntityNotFoundException;
import com.example.demo.services.exceptions.UniqueFieldAlreadyExists;
import com.example.demo.services.imp.UserServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    private static final Long ID = 1L;
    private static final String NAME = "Itallo";
    private static final Date BIRTHDATE = new Date(2000, 5, 20);
    private static final String EMAIL = "itallo0@hotmail.com";
    private static final String USERNAME = "itallo";
    private static final String PASSWORD = "senha123";
    private static final UserRole USER_ROLE = UserRole.USER;


    @InjectMocks
    private UserServiceImp userService;
    @Mock
    private UserRepository userRepository;

    private User user;
    private UserCreationDTO userCreationDTO;
    private UserDTO userDTO;

    private Optional<User> optionalUser;
    private Page<UserDTO> pageUsers;
    private Page<ProjectDTO> projectDTOS;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startUser();
    }

    @Test
    void whenSaveThenReturnSuccess() {
        when(userRepository.save(any())).thenReturn(user);

        UserDTO response = userService.save(userCreationDTO);

        assertNotNull(response);
        assertEquals(UserDTO.class,response.getClass());
        assertEquals(NAME,response.getName());
        assertEquals(BIRTHDATE,response.getBirthDate());
        assertEquals(EMAIL,response.getEmail());
        assertEquals(USERNAME,response.getUsername());

    }
    @Test
    void whenSaveThenReturnEmailAlreadyExistException() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        try{
            userService.save(userCreationDTO);
        } catch (Exception ex){
            assertEquals(UniqueFieldAlreadyExists.class, ex.getClass());
            assertEquals("Email: " +EMAIL+ " already in use",ex.getMessage());
        }

    }
    @Test
    void whenSaveThenReturnUsernameAlreadyExistException() {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        try{
            userService.save(userCreationDTO);
        } catch (Exception ex){
            assertEquals(UniqueFieldAlreadyExists.class, ex.getClass());
            assertEquals("Username: " +USERNAME+ " already in use",ex.getMessage());
        }

    }
    @Test
    void whenSaveThenReturnEmailAndUsernameAlreadyExistException() {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        try{
            userService.save(userCreationDTO);
        } catch (Exception ex){
            assertEquals(UniqueFieldAlreadyExists.class, ex.getClass());
            assertEquals("Email: " + EMAIL + " and Username: " + USERNAME + " already in use",ex.getMessage());
        }

    }

    @Test
    void whenFindAllThenReturnAnListOfUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        Page<UserDTO> response = userService.getAll();

        assertNotNull(response);

        assertEquals(1,response.getTotalElements());
        assertEquals(UserDTO.class,response.getContent().get(0).getClass());
        assertEquals(ID,response.getContent().get(0).getId());

    }

    @Test
    void getProjects() {
    }

    @Test
    void whenFindByIdThenReturnAnUserInstance() {
        when(userRepository.findById(anyLong())).thenReturn(optionalUser);

        UserDTO response = userService.findById(ID);

        assertNotNull(response);
        assertEquals(UserDTO.class, response.getClass());
        assertEquals(ID,response.getId());

    }

    @Test
    void whenFindByIdThenReturnAnObjectNotFoundException(){
        when(userRepository.findById(anyLong())).thenThrow(new EntityNotFoundException("User not found for id: "));
        try{
            userService.findById(ID);
        }catch (Exception ex){
            assertEquals(EntityNotFoundException.class,ex.getClass());
            assertEquals("User not found for id: ", ex.getMessage());
        }
    }

    @Test
    void search() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }


    private void startUser(){
        List<UserDTO> userDTOList = new ArrayList<>();
        user = new User(ID,NAME,BIRTHDATE,EMAIL,USERNAME,PASSWORD,USER_ROLE);
        userDTO = new UserDTO(user);
        pageUsers = new PageImpl<>(userDTOList, PageRequest.of(0, 10), userDTOList.size());
        optionalUser = Optional.of(user);
        userCreationDTO = new UserCreationDTO(NAME,BIRTHDATE,EMAIL,USERNAME,PASSWORD,PASSWORD);

    }


}