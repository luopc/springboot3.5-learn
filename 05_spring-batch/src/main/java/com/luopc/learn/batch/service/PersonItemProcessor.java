package com.luopc.learn.batch.service;

import com.luopc.learn.batch.model.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PersonItemProcessor implements ItemProcessor<Person, Person> {

    @Override
    public Person process(final Person person) throws Exception {
        String name = person.getName().toUpperCase();
        int age = person.getAge();
        String email = person.getEmail().toUpperCase();

        Person transformedCoffee = new Person(name, age, email);
        log.info("Converting ( {} ) into ( {} )", person, transformedCoffee);

        return transformedCoffee;
    }
}
