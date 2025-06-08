package com.luopc.learn.batch.service;

import com.luopc.learn.batch.model.entity.Person;
import com.luopc.learn.batch.model.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class PersonServiceTest {

    @Autowired
    private PersonService personService;

    @Test
    public void contextLoads() {
        // 保存
        Person personEntity = new Person();
        personEntity.setName("张三");
        personService.save(personEntity);

        // 查询
        List<Person> personEntityList = personService.findAll();
        for (Person person : personEntityList) {
            System.out.println(person);
        }

        if (!personEntityList.isEmpty()) {
            // 更新
            Person person = personEntityList.getFirst();
            person.setName("张三-update");
            personService.save(person);
        }

        // 查询
        personEntityList = personService.findAll();
        for (Person person : personEntityList) {
            System.out.println(person);
        }

        // 删除
        for (Person person : personEntityList) {
            personService.delete(person);
        }
    }

}