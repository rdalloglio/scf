package br.com.rdalloglio.scf.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.rdalloglio.scf.dtos.AuthResponse;
import br.com.rdalloglio.scf.dtos.LoginRequest;
import br.com.rdalloglio.scf.dtos.RegisterRequest;
import br.com.rdalloglio.scf.entities.User;
import br.com.rdalloglio.scf.exceptions.InvalidCredentialsException;
import br.com.rdalloglio.scf.exceptions.UserAlreadyExistsException;
import br.com.rdalloglio.scf.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new UserAlreadyExistsException(request.username());
        }

        var user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(request.role());

        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {
        var authToken = new UsernamePasswordAuthenticationToken(
                request.username(),
                request.password()
        );

        try {
            authenticationManager.authenticate(authToken);
        } catch (Exception e) {
            throw new InvalidCredentialsException();
        }

        var user = userRepository.findByUsername(request.username())
            .orElseThrow(InvalidCredentialsException::new);

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}
