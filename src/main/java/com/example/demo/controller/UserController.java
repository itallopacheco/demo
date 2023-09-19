package com.example.demo.controller;

import com.example.demo.domain.user.dto.UserDTO;
import com.example.demo.domain.user.dto.UserUpdateDTO;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping()
    public ResponseEntity<List<UserDTO>> getAll(){
        return ResponseEntity.ok().body(userService.getAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable(value = "id") long id){
       UserDTO userDTO = userService.findById(id);
       return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
    }

    @PutMapping({"{id}"})
    public ResponseEntity<UserDTO> update(@PathVariable(value = "id")long id ,@RequestBody @Valid UserUpdateDTO userUpdateDTO){
        return ResponseEntity.ok(userService.updateUser(id,userUpdateDTO));
    }

    @DeleteMapping({"{id}"})
    public ResponseEntity<?> delete(@PathVariable(value = "id") long id){
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
