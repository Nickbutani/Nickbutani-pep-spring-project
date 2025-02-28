package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;

import com.example.entity.Account;
import com.example.exception.AccountNotFoundException;
import com.example.exception.PasswordNotMatchException;
import com.example.exception.UsernameTakenException;
import com.example.repository.AccountRepository;

import javassist.tools.web.BadHttpRequest;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Account login(Account account) throws AccountNotFoundException, PasswordNotMatchException {
        Optional<Account> foundAccount = accountRepository.findByUsername(account.getUsername());
        
        if (foundAccount.isEmpty()) {
            throw new AccountNotFoundException();
        } 

        if (!account.getPassword().equals(foundAccount.get().getPassword())) {
            throw new PasswordNotMatchException();
        }

        return foundAccount.get();
    }

    public Account register(Account account) throws BadHttpRequest, UsernameTakenException {
        if (account.getUsername().isEmpty() || 
            account.getPassword().length() < 4) 
        {
            throw new BadHttpRequest();
        }

        if (accountRepository.findByUsername(account.getUsername()).isPresent()) {
            throw new UsernameTakenException();
        }

        return accountRepository.save(account);
    }
}