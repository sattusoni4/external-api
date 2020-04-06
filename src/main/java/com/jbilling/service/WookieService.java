package com.jbilling.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

@Service
public class WookieService {
    private static final STGroup stGroup = new STGroupFile("email.stg");

    public String usagesPoolTemplate(JsonNode jsonNode) {
        ST emailTemplate = stGroup.getInstanceOf("usagesPool");

        jsonNode.fieldNames().forEachRemaining(fieldName ->
                emailTemplate.getAttributes().forEach((attr, value) -> {
                    if (fieldName.equalsIgnoreCase(attr)) {
                        emailTemplate.add(attr, jsonNode.get(fieldName).asText());
                    }
                }));

        if (jsonNode.get("percentageConsumption").asInt() < 100)
            emailTemplate.add("usagesPoolLeft", "usagesPoolLeft");
        System.out.println("emailTemplate.render() = " + emailTemplate.render());
        return emailTemplate.render();
    }

    public String mobileDataTemplate(JsonNode jsonNode) {
        ST emailTemplate = stGroup.getInstanceOf("mobileData");

        jsonNode.fieldNames().forEachRemaining(fieldName ->
                emailTemplate.getAttributes().forEach((attr, value) -> {
                    if (fieldName.equalsIgnoreCase(attr)) {
                        emailTemplate.add(attr, jsonNode.get(fieldName).asText());
                    }
                }));

        System.out.println("emailTemplate.render() = " + emailTemplate.render());
        return emailTemplate.render();
    }

    public String dataBoostTemplate(JsonNode jsonNode) {
        String fullUseForDB3 = "at an overage rate";
        StringBuilder number;

        char[] chars = jsonNode.get("type").asText().toCharArray();
        number = new StringBuilder();
        for (char c : chars) {
            if (Character.isDigit(c)) {
                number.append(c);
            }
        }
        ST emailTemplate = stGroup.getInstanceOf("dataBoost");

        jsonNode.fieldNames().forEachRemaining(fieldName ->
                emailTemplate.getAttributes().forEach((attr, value) -> {
                    if (fieldName.equalsIgnoreCase(attr)) {
                        emailTemplate.add(attr, jsonNode.get(fieldName).asText());
                    }
                }));
        int dataBoostValue = Integer.parseInt(String.valueOf(number));
        emailTemplate.add("dataBoostValue", dataBoostValue);
        if (dataBoostValue < 3)
            emailTemplate.add("nextDataBoost", dataBoostValue + 1);
        else emailTemplate.add("fullUse", fullUseForDB3);
        System.out.println("emailTemplate.render() = " + emailTemplate.render());
        return emailTemplate.render();
    }

    public String creditPoolTemplate(JsonNode jsonNode) {
        String fullUseForCreditPool = "as per the call rates";
        ST emailTemplate = stGroup.getInstanceOf("creditPool");

        jsonNode.fieldNames().forEachRemaining(fieldName ->
                emailTemplate.getAttributes().forEach((attr, value) -> {
                    if (fieldName.equalsIgnoreCase(attr)) {
                        emailTemplate.add(attr, jsonNode.get(fieldName).asText());
                    }
                }));
        if (jsonNode.get("percentageConsumption").asInt() < 100)
            emailTemplate.add("creditPoolLeft", "creditPoolLeft");
        else emailTemplate.add("fullUse", fullUseForCreditPool);
        System.out.println("emailTemplate.render() = " + emailTemplate.render());
        return emailTemplate.render();
    }
}
