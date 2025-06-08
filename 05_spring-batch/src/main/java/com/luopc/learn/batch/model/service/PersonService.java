package com.luopc.learn.batch.model.service;

import com.luopc.learn.batch.model.dao.PersonRepository;
import com.luopc.learn.batch.model.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public void save(Person person) {
        personRepository.save(person);
    }

    public void delete(Person person) {
        personRepository.delete(person);
    }

    public Person findById(Long id) {
        return personRepository.findById(id).orElse(null);
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }
}
