package com.demo;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/module-one-copy")
@Profile("copy")
public class ModuleOneCopyController {

    private ModuleOneController moduleOneController;

    public ModuleOneCopyController(ModuleOneController moduleOneController) {
        this.moduleOneController = moduleOneController;
    }

    private List<ModuleOne> list = Arrays.asList(
            new ModuleOne("name1", "d1"),
            new ModuleOne("name2", "d2"),
            new ModuleOne("name3", "d3")
    );

    @GetMapping
    public List<ModuleOne> getAll(HttpServletRequest request) {
        return moduleOneController.getAll(request);
    }


}
