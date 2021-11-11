package golan.attack.surface.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;


@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ApiController {
    private final AttackResolver attackResolver;

    @GetMapping("/attack")
    public Collection<String> attack(@RequestParam("vm_id") String vmId) {
        return attackResolver.resolve(vmId);
    }
}
