package br.com.rdalloglio.scf.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rdalloglio.scf.dtos.AccountRequest;
import br.com.rdalloglio.scf.dtos.AccountResponse;
import br.com.rdalloglio.scf.services.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
public ResponseEntity<AccountResponse> create(@RequestBody @Valid AccountRequest request, Principal principal) {
    var response = accountService.create(request, principal.getName());
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
}

    @GetMapping
    public ResponseEntity<List<AccountResponse>> list(Principal principal) {
        var list = accountService.list(principal.getName());
        return ResponseEntity.ok(list);
    }
}