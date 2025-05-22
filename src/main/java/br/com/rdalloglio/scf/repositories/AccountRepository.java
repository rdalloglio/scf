package br.com.rdalloglio.scf.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.rdalloglio.scf.entities.Account;
import br.com.rdalloglio.scf.entities.User;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAllByOwner(User owner);
}