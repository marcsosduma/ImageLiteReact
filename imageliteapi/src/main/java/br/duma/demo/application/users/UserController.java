package br.duma.demo.application.users;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.duma.demo.domain.entity.User;
import br.duma.demo.domain.exception.DuplicatedTupleException;
import br.duma.demo.domain.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity save(@RequestBody UserDTO dto){
        try{
            User user = userMapper.mapToUser(dto);
            userService.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch(DuplicatedTupleException e){
            Map<String, String> jasonResult = Map.of("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(jasonResult);
        }
    } 

    @PostMapping("/auth")
    public ResponseEntity authenticate(@RequestBody CredentialsDTO credentials) {
        var token = userService.authenticate(credentials.getEmail(), credentials.getPassword());
        if(token == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED) .build();
        }
        return ResponseEntity.ok(token);
    }   
    
}
