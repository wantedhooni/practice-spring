package com.revy.example.db_master_slave.service;


import com.revy.example.db_master_slave.domain.entity.person.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TestService {

    private final PersonRepository personRepository;

    @Transactional
    public void testQuery(){
        personRepository.findAll();
    }

    @Transactional(readOnly = true)
    public void testReadOnlyQuery(){
        personRepository.findAll();
    }
}
