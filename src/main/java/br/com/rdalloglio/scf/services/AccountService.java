package br.com.rdalloglio.scf.services;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.rdalloglio.scf.dtos.AccountRequest;
import br.com.rdalloglio.scf.dtos.AccountResponse;
import br.com.rdalloglio.scf.entities.Account;
import br.com.rdalloglio.scf.repositories.AccountRepository;
import br.com.rdalloglio.scf.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountResponse create(AccountRequest request, String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        var account = Account.builder()
                .name(request.name())
                .balance(request.balance())
                .owner(user)
                .build();

        var saved = accountRepository.save(account);
        return new AccountResponse(saved.getId(), saved.getName(), saved.getBalance());
    }

    public List<AccountResponse> list(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        return accountRepository.findAllByOwner(user)
                .stream()
                .map(a -> new AccountResponse(a.getId(), a.getName(), a.getBalance()))
                .toList();
    }
}