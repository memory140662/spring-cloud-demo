package com.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/module-one")
public class ModuleOneController {

    private List<ModuleOne> list = Arrays.asList(
            new ModuleOne("name1", "d1"),
            new ModuleOne("name2", "d2"),
            new ModuleOne("name3", "d3")
    );

    @GetMapping
    public List<ModuleOne> getAll(HttpServletRequest request) {
        System.out.println("************* module one port: " + request.getLocalPort());
        return list;
    }


    @GetMapping("/{name}")
    public ModuleOne getOne(@PathVariable String name) {
        return list.stream()
                .filter(moduleOne -> moduleOne.getName().equals(name))
                .findFirst()
                .orElse(new ModuleOne());
    }

}
