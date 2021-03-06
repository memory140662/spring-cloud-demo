package com.demo;

import com.demo.entities.Module;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    private List<Module> list = Arrays.asList(
            new Module("name1", "d1"),
            new Module("name2", "d2"),
            new Module("name3", "d3")
    );

    @GetMapping
    public List<Module> getAll(HttpServletRequest request, HttpServletResponse response) {
        return moduleOneController.getAll(request, response);
    }


}
