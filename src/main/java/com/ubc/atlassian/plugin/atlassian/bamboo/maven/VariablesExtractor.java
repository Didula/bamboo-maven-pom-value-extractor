package com.ubc.atlassian.plugin.atlassian.bamboo.maven;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.ubc.atlassian.plugin.bamboo.maven.extractor.PomValueExtractor;

import java.util.ArrayList;
import java.util.List;

class VariablesExtractor {

    private static final String POM_ELEMENT_VERSION = "version";
    private static final String POM_ELEMENT_ARTIFACT_ID = "artifactId";
    private static final String POM_ELEMENT_GROUP_ID = "groupId";

    private static final String DEFAULT_VARIABLE_PREFIX = "maven.";

    private final PomValueExtractor extractor;

    public VariablesExtractor(PomValueExtractor extractor) {
        this.extractor = extractor;
    }

    public List<Variable> extractVariables(TaskConfiguration config) {
        List<Variable> variables = new ArrayList<Variable>();
        if (config.isCustomExtract()) {
            String variableName = config.getCustomVariableName();
            String element = config.getCustomElement();
            doExtract(element, variableName, extractor, variables, config);
        } else {
            doExtract(POM_ELEMENT_GROUP_ID, fullVariableName(POM_ELEMENT_GROUP_ID, config), extractor, variables,
                    config);
            doExtract(POM_ELEMENT_ARTIFACT_ID, fullVariableName(POM_ELEMENT_ARTIFACT_ID, config), extractor, variables,
                    config);
            extractVersion(config, variables);
        }
        return variables;
    }

    private void extractVersion(TaskConfiguration config, List<Variable> variables) {
        if (config.isStripSnaphost()) {
            String value = extractor.getValue(POM_ELEMENT_VERSION);
            boolean containsSnapshot = false;
            if (value.trim().endsWith("-SNAPSHOT")) {
                containsSnapshot = true;
                value = value.replace("-SNAPSHOT", "");
            }
            String variableName = fullVariableName(POM_ELEMENT_VERSION, config);
            variables.add(new Variable(variableName, value));

            BuildLogger logger = config.getBuildLogger();
            StringBuilder message = new StringBuilder();
            message.append("Extracted ");
            message.append(POM_ELEMENT_VERSION);
            if (containsSnapshot) {
                message.append(" from POM. Stripping '-SNAPSHOT' and setting ");
            } else {
                message.append(" from POM. Setting ");
            }
            message.append(config.getVariableType());
            message.append(" variable ");
            message.append(variableName);
            message.append(" to ");
            message.append(value);
            logger.addBuildLogEntry(message.toString());
        } else {
            doExtract(POM_ELEMENT_VERSION, fullVariableName(POM_ELEMENT_VERSION, config), extractor, variables, config);
        }
    }

    private void doExtract(String element, String variableName, PomValueExtractor extractor, List<Variable> variables,
                           TaskConfiguration config) {
        String value = extractor.getValue(element);
        variables.add(new Variable(variableName, value));

        BuildLogger logger = config.getBuildLogger();
        String message = "Extracted " +
                element +
                " from POM. Setting " +
                config.getVariableType() +
                " variable " +
                variableName +
                " to " +
                value;
        logger.addBuildLogEntry(message);
    }

    private String fullVariableName(String name, TaskConfiguration config) {
        String prefix = DEFAULT_VARIABLE_PREFIX;
        if (config.isCustomPrefix()) {
            prefix = config.getCustomPrefix();
        }
        return prefix + name;
    }
}