package com.jbilling.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

@Service
public class WookieService {
    private static final STGroup stGroup = new STGroupFile("email.stg");

    public String usagesPoolTemplate(JsonNode jsonNode) {
        final ST emailTemplate = stGroup.getInstanceOf("usagesPool");

        setTemplateParameters(jsonNode, emailTemplate);

        if (jsonNode.get("percentageConsumption").asInt() < 100)
            emailTemplate.add("usagesPoolLeft", "usagesPoolLeft");
        System.out.println("emailTemplate.render() = " + emailTemplate.render());
        return emailTemplate.render();
    }

    public String mobileDataTemplate(JsonNode jsonNode) {
        final ST emailTemplate = stGroup.getInstanceOf("mobileData");
        setTemplateParameters(jsonNode, emailTemplate);
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
        final ST emailTemplate = stGroup.getInstanceOf("dataBoost");

        setTemplateParameters(jsonNode, emailTemplate);
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
        final ST emailTemplate = stGroup.getInstanceOf("creditPool");

        setTemplateParameters(jsonNode, emailTemplate);
        if (jsonNode.get("percentageConsumption").asInt() < 100)
            emailTemplate.add("creditPoolLeft", "creditPoolLeft");
        else emailTemplate.add("fullUse", fullUseForCreditPool);
        System.out.println("emailTemplate.render() = " + emailTemplate.render());
        return emailTemplate.render();
    }

    private void setTemplateParameters(JsonNode jsonNode, ST emailTemplate) {
        jsonNode.fieldNames().forEachRemaining(fieldName ->
                emailTemplate.getAttributes().forEach((attr, value) -> {
                    if (fieldName.equalsIgnoreCase(attr)) {
                        emailTemplate.add(attr, jsonNode.get(fieldName).asText());
                    }
                }));
    }
}
