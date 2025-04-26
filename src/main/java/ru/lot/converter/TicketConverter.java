package ru.lot.converter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.lang.Collections;

@Component
public class TicketConverter {
    private static final String DELIMITER = ",";

    public List<Integer> convertToNumbers(String pickedNumbers) {
        if (pickedNumbers == null || pickedNumbers.isEmpty()) {
            return Collections.emptyList();
        }
        return Stream.of(pickedNumbers.split(DELIMITER))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        
    }

    public String convertToString(List<Integer> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            return "";
        }
        return numbers.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(DELIMITER));
    }
}