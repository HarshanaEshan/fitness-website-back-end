package com.example.demo.service;


import com.example.demo.dto.userDTO;
import com.example.demo.entity.User;
import com.example.demo.repo.UserRepo;
import com.example.demo.config.Encode;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;


    Encode encode=new Encode();

    public userDTO saveUSer(userDTO userdto){
        String password = userdto.getPassword();
        String hashedPassword = encode.hashPassword(password);
        userdto.setPassword(hashedPassword);
        userRepo.save(modelMapper.map(userdto, User.class));
        return userdto;

    }

    public List<userDTO> getAllusers()
    {
        List<User>userList=userRepo.findAll();
        return modelMapper.map(userList,new TypeToken<List<userDTO>>(){}.getType());
    }

    public userDTO updateUser(userDTO userdto){
        userRepo.save(modelMapper.map(userdto,User.class));
        return  userdto;
    }

    public boolean deleteUser(userDTO userdto){
        userRepo.delete(modelMapper.map(userdto,User.class));
        return true;
    }


    public Optional<String> getUserById(String id) {
        Optional<User> userDetail=userRepo.findById(id);
        if (userDetail.isPresent()) {
            User user = userDetail.get();
            return Optional.ofNullable(user.getPassword());
        } else {
            return Optional.empty();
        }
    }
    public Optional<String> logIn(String id, String password) {
        // Retrieve user details by ID
        Optional<User> userOptional = userRepo.findById(id);

        // Check if user exists and if password matches
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(password)) {
                // Return user details if password matches
                return Optional.of("User logged in: " + user.getUserName());
            } else {
                // Return error message if password does not match
                return Optional.of("Invalid password for user");
            }
        } else {
            // Return error message if user does not exist
            return Optional.of("User not found for ID");
        }
    }


}
