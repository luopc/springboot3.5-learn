package com.luopc.learn.batch.model.dao;

import com.luopc.learn.batch.model.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
    //
}
