package kz.softrack.quercat.controllers;


import kz.softrack.quercat.controllers.dto.QueryDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("books-rest")
public class QueryExecutorController {

    @PostMapping(path = "/members", consumes = "application/json", produces = "application/json")
    public void addMember(@RequestBody QueryDTO queryDTO) {

    }
}
