package com.demo;

import com.demo.entities.Module;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequestMapping("module-two")
@RestController
public class ModuleTwoController {

    private List<Module> list = Arrays.asList(
            new Module("ModuleTwo", "d1"),
            new Module("ModuleTwo", "d2"),
            new Module("ModuleTwo", "d3")
    );

    @GetMapping
    public List<Module> getAll(HttpServletRequest request, HttpServletResponse response) {
        response.addHeader("Cache-Control", "no-cache");
        String l = request.getParameter("l");
        return Collections.singletonList(
                new Module(l, "d2")
        );
    }


    @GetMapping("/{name}")
    public Module getOne(@PathVariable String name) {
        return list.stream()
                .filter(moduleOne -> moduleOne.getName().equals(name))
                .findFirst()
                .orElse(new Module());
    }
}
