package kz.softrack.quercat.controllers;


import kz.softrack.quercat.controllers.dto.CommandRequestDTO;
import kz.softrack.quercat.controllers.dto.CommandResponseDTO;
import kz.softrack.quercat.model.OutputType;
import kz.softrack.quercat.model.Query;
import kz.softrack.quercat.services.AuthorizerService;
import kz.softrack.quercat.services.HelpService;
import kz.softrack.quercat.services.QueryExecutorService;
import kz.softrack.quercat.services.QueryReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("commands")
public class CommandExecutorController {

    private static final Logger logger = Logger.getLogger(CommandExecutorController.class.getName());

    @Autowired
    private QueryExecutorService queryExecutorService;

    @Autowired
    private QueryReaderService queryReaderService;

    @Autowired
    private AuthorizerService authorizerService;

    @Autowired
    private HelpService helpService;

    @PostMapping(path = "/execute", consumes = "application/json", produces = "application/json")
    public CommandResponseDTO addMember(@RequestBody CommandRequestDTO commandRequestDTO) {
        try {
            List<String> commandAndArgs = commandRequestDTO.getCommandAndArgs();
            Query query = queryReaderService.readQuery(commandAndArgs.get(0));
            if (query == null) {
                return helpService.createHelpMessage();
            }
            authorizerService.authorize(query, commandRequestDTO);
            return queryExecutorService.query(query, commandAndArgs.subList(1, commandAndArgs.size()), OutputType.LOCAL_XLS_FILE);
        } catch (Exception e) {
            logger.log(Level.INFO,"Failed to execute request", e);
            return new CommandResponseDTO(e.getMessage(), Collections.emptyList(), Collections.emptyList());
        }
    }
}
