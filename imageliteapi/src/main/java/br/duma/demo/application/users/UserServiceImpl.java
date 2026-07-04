package br.duma.demo.application.users;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.duma.demo.application.jwt.JwtService;
import br.duma.demo.domain.AccessToken;
import br.duma.demo.domain.entity.User;
import br.duma.demo.domain.exception.DuplicatedTupleException;
import br.duma.demo.domain.service.UserService;
import br.duma.demo.infra.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public User getByEmail(String email) {
        
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public User save(User user) {
        var possibleUser = getByEmail(user.getEmail());
        if(possibleUser!=null){
            throw new DuplicatedTupleException("User already exists!");
        }   
        encodePassord(user);
        return userRepository.save(user);
    }

    private void encodePassord(User user){
        String rawPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
    }

    @Override
    public AccessToken authenticate(String email, String password) {
        var user = getByEmail(email);
        if(user == null){
            return null;
        }   
        boolean matches = passwordEncoder.matches(password, user.getPassword());
        if(matches){
            return jwtService.generateToken(user);
        }
        return null;
    }
   
}
