package kz.softrack.quercat.services;

import kz.softrack.quercat.controllers.dto.CommandRequestDTO;
import kz.softrack.quercat.model.Query;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class AuthorizerService {

    private static final Logger logger = Logger.getLogger(AuthorizerService.class.getName());

    public static final String ADMIN_PHONES_FILE_NAME = "adminPhones.txt";

    @Value("${quercat.data.folder.path}")
    private String path;

    @Value("${quercat.application.auth.token}")
    private String applicationAuthToken;

    public void authorize(Query query, CommandRequestDTO commandRequestDTO) {
        if (applicationAuthToken == null || applicationAuthToken.isEmpty()) {
            throw new RuntimeException("applicationAuthToken must be set");
        }

        if (!applicationAuthToken.equals(commandRequestDTO.getApplicationAuthToken())) {
            throw new RuntimeException("applicationAuthToken is not correct");
        }

        String requester = commandRequestDTO.getRequesterPhoneNumber();
        boolean isInAllowed = query.getAllowed().contains(requester);
        boolean isInAdmins = checkIfAdmin(requester);
        if (!isInAllowed && !isInAdmins) {
            throw new RuntimeException("requester " + requester + " is not in allowed list");
        }
    }

    private boolean checkIfAdmin(String requester) {
        String adminPhonesFile = getConfDir() + File.separator + ADMIN_PHONES_FILE_NAME;
        try {
            String content = new String(Files.readAllBytes(Paths.get(adminPhonesFile)));
            List<String> allowedPhones = Arrays.asList(content.split("\\r?\\n"));
            return allowedPhones.contains(requester);
        } catch (IOException e) {
            throw new RuntimeException("Could not read config file to check if requester is admin", e);
        }
    }

    private String getConfDir() {
        return path + File.separator + "conf";
    }

}
