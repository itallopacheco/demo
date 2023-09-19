package com.example.demo.services.imp;

import com.example.demo.domain.user.User;
import com.example.demo.domain.user.dto.UserCreationDTO;
import com.example.demo.domain.user.dto.UserDTO;
import com.example.demo.domain.user.dto.UserUpdateDTO;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.UserService;
import com.example.demo.services.exceptions.EntityNotFoundException;
import com.example.demo.services.exceptions.UniqueFieldAlreadyExists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDTO save(UserCreationDTO userCreationDTO) {
        boolean emailExists = userRepository.existsByEmail(userCreationDTO.email());
        boolean usernameExists = userRepository.existsByUsername(userCreationDTO.username());

        if (emailExists && usernameExists) {
            throw new UniqueFieldAlreadyExists("Email: " + userCreationDTO.email() + " and Username: " + userCreationDTO.username() + " already in use");
        } else if (emailExists) {
            throw new UniqueFieldAlreadyExists("Email: " + userCreationDTO.email() + " already in use");
        } else if (usernameExists) {
            throw new UniqueFieldAlreadyExists("Username: " + userCreationDTO.username() + " already in use");
        }
        User user = userCreationDTO.userCreationDTOtoUser();
        userRepository.save(user);
        return new UserDTO(user);

    }

    @Override
    public List<UserDTO> getAll() {
        List<User> userList = userRepository.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();
        for (User u: userList) {
            UserDTO userDTO = new UserDTO(u);
            userDTOList.add(userDTO);
        }
        return userDTOList;
    }

    @Override
    public UserDTO findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return new UserDTO(userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User not found for id: "+id)));
    }

    @Override
    public UserDTO findByUsername(String username) {
        return null;
    }

    @Override
    public UserDTO updateUser(Long id,UserUpdateDTO userUpdateDTO) {
       if (userRepository.existsByEmail(userUpdateDTO.email())) throw new UniqueFieldAlreadyExists("Email: " + userUpdateDTO.email() + "already in use");
       Optional<User> oldUser = userRepository.findById(id);
       if (oldUser.isPresent()){
            User user = oldUser.get();

            if (userUpdateDTO.name() != null){
                user.setName(userUpdateDTO.name());

            }
            if (userUpdateDTO.email() != null){
                user.setEmail(userUpdateDTO.email());
            }

            userRepository.save(user);

           return new UserDTO(user);

       } else {
           throw new EntityNotFoundException("User not found for id: "+id);
       }

    }

    @Override
    public void deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()){
            userRepository.delete(user.get());
        } else {
            throw new EntityNotFoundException("User not found for id: "+id);
        }

    }
}
