package kz.softrack.quercat.services;

import kz.softrack.quercat.controllers.dto.CommandResponseDTO;
import kz.softrack.quercat.model.OutputType;
import kz.softrack.quercat.model.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class QueryExecutorService {

    private static final Logger logger = Logger.getLogger(QueryExecutorService.class.getName());

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final QueryResultProcessor queryResultProcessor;

    private final HelpService helpService;
    private final ParameterCasterService parameterCasterService;

    @Autowired
    public QueryExecutorService(JdbcTemplate jdbcTemplate, QueryResultProcessor queryResultProcessor, HelpService helpService, ParameterCasterService parameterCasterService) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.queryResultProcessor = queryResultProcessor;
        this.helpService = helpService;
        this.parameterCasterService = parameterCasterService;
    }

    public CommandResponseDTO query(Query query, List<String> parameters, OutputType outputType) {
        List<Map<String, Object>> result = query(query, parameters);
        return queryResultProcessor.process(query, result, outputType);
    }

    private List<Map<String, Object>> query(Query query, List<String> parameters) {
        List<String> queryParameterNames = query.getQueryParameters();

        if (queryParameterNames.size() != parameters.size()) {
            throw new RuntimeException("Error: Query parameters does not correspond to expected, found "
                    + parameters.size() + " expected = " + queryParameterNames.size() + System.lineSeparator()
                    + helpService.createHelpMessage(query));
        }

        MapSqlParameterSource in = new MapSqlParameterSource();
        logger.info("registering values for query " + query.getQueryName());
        for (int i = 0; i < parameters.size(); i++) {
            String paramName = queryParameterNames.get(i);
            try {
                Object paramValue = parameterCasterService.castParameter(paramName, parameters.get(i));
                logger.info("registering " + paramName + " = " + paramValue);
                in.addValue(paramName, paramValue);
            } catch (Exception e) {
                logger.log(Level.INFO, "Could not cast parameter", e);
                throw new RuntimeException("Error: could not parse parameter " + paramName
                        + ", expected '" + parameterCasterService.getTypeDescription(paramName)
                        + "', found value = '" + parameters.get(i) + "' " + helpService.createHelpMessage(query));
            }
        }

        return namedParameterJdbcTemplate.queryForList(query.getQuery(), in);
    }
}
