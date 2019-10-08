package kz.softrack.quercat.services;

import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

@Service
public class ParameterCasterService {

    public interface CasterHandler {
        Object parse(String paramName, String paramValue);
        String getTypeDescription();
        String getTypeName();

        default boolean isHandlerFor(String paramName) {
            return paramName.startsWith(getTypeName() + "_");
        }
    }

    private static class DateCasterHandler implements CasterHandler {
        public static final String FORMAT = "yyyy-MM-dd";
        private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT);

        @Override
        public Object parse(String paramName, String paramValue) {
            try {
                return simpleDateFormat.parse(paramValue);
            } catch (ParseException e) {
                throw new RuntimeException("could not parse param " + paramName + " with value " + paramValue);
            }
        }

        @Override
        public String getTypeDescription() {
            return "a date in a format " + FORMAT;
        }

        @Override
        public String getTypeName() {
            return "date";
        }
    }

    private static class LongCasterHandler implements CasterHandler {

        @Override
        public Object parse(String paramName, String paramValue) {
            try {
                return Long.parseLong(paramValue);
            } catch (Exception e) {
                throw new RuntimeException("could not parse param " + paramName + " with value " + paramValue);
            }
        }

        @Override
        public String getTypeDescription() {
            return "a long value";
        }

        @Override
        public String getTypeName() {
            return "long";
        }
    }

    private static class StringCasterHandler implements CasterHandler {

        @Override
        public Object parse(String paramName, String paramValue) {
            return paramValue;
        }

        @Override
        public String getTypeDescription() {
            return "a string value";
        }

        @Override
        public String getTypeName() {
            return "string";
        }
    }

    private static class DateTimeCasterHandler implements CasterHandler {

        public static final String FORMAT = "yyyy-MM-dd_HH:mm:ss";
        private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT);

        @Override
        public Object parse(String paramName, String paramValue) {
            try {
                return simpleDateFormat.parse(paramValue);
            } catch (ParseException e) {
                throw new RuntimeException("could not parse param " + paramName + " with value " + paramValue);
            }
        }

        @Override
        public String getTypeDescription() {
            return "a datetime in a format " + FORMAT;
        }

        @Override
        public String getTypeName() {
            return "datetime";
        }
    }

    private final List<CasterHandler> handlers = Arrays.asList(
            new DateCasterHandler(),
            new LongCasterHandler(),
            new StringCasterHandler(),
            new DateTimeCasterHandler()
    );

    private CasterHandler findHandler(String paramName) {
        return handlers.stream().filter(handler -> handler.isHandlerFor(paramName)).findAny().get();
    }

    public Object castParameter(String paramName, String paramValue) {
        return findHandler(paramName).parse(paramName, paramValue);
    }

    public String getTypeDescription(String paramName) {
        return findHandler(paramName).getTypeDescription();
    }

    public List<CasterHandler> getHandlers() {
        return handlers;
    }
}
