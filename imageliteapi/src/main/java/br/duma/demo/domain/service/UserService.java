package br.duma.demo.domain.service;

import br.duma.demo.domain.AccessToken;
import br.duma.demo.domain.entity.User;

public interface UserService {
    User getByEmail(String email);
    User save(User user);
    AccessToken authenticate(String email, String password);
}
