package com.ubc.atlassian.plugin.atlassian.bamboo.maven;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.task.CommonTaskContext;
import org.apache.commons.lang.StringUtils;

import java.io.File;

public class TaskConfiguration {

    public static final String EXTRACT_MODE = "gavOrCustom";
    public static final String EXTRACT_MODE_GAV = "0";
    public static final String EXTRACT_MODE_CUSTOM = "1";

    public static final String STRIP_SNAPSHOT = "stripSnapshot";

    public static final String VARIABLE_TYPE = "variableType";
    public static final String VARIABLE_TYPE_JOB = "0";
    public static final String VARIABLE_TYPE_PLAN = "1";
    public static final String VARIABLE_TYPE_RESULT = "2";

    public static final String PREFIX_OPTION = "prefixOption";
    public static final String PREFIX_OPTION_DEFAULT = "1";
    public static final String PREFIX_OPTION_CUSTOM = "0";
    public static final String PREFIX_OPTION_CUSTOM_VALUE = "customPrefix";

    public static final String PROJECT_FILE = "projectFile";

    public static final String CUSTOM_VARIABLE_NAME = "customVariableName";
    public static final String CUSTOM_ELEMENT = "customElement";

    private final String projectFile;
    private final String customPrefix;

    private boolean customExtract = false;
    private String customVariableName;
    private String customElement;

    private boolean stripSnaphost = false;

    private final VariableType variableType;

    private final CommonTaskContext taskContext;

    public TaskConfiguration(CommonTaskContext taskContext) {
        this.taskContext = taskContext;
        ConfigurationMap configurationMap = taskContext.getConfigurationMap();
        projectFile = configurationMap.get(PROJECT_FILE);
        if (PREFIX_OPTION_CUSTOM.equals(configurationMap.get(PREFIX_OPTION))) {
            customPrefix = configurationMap.get(PREFIX_OPTION_CUSTOM_VALUE);
        } else {
            customPrefix = null;
        }
        if (EXTRACT_MODE_CUSTOM.equals(configurationMap.get(EXTRACT_MODE))) {
            customExtract = true;
            customVariableName = configurationMap.get(CUSTOM_VARIABLE_NAME);
            customElement = configurationMap.get(CUSTOM_ELEMENT);
        }

        String selectedType = configurationMap.get(VARIABLE_TYPE);
        if (VARIABLE_TYPE_PLAN.equals(selectedType)) {
            variableType = VariableType.PLAN;
        } else if (VARIABLE_TYPE_RESULT.equals(selectedType)) {
            variableType = VariableType.RESULT;
        } else if (VARIABLE_TYPE_JOB.equals(selectedType)) {
            variableType = VariableType.JOB;
        } else {
            // To support tasks that were configured prior to version 1.3 of the
            // plugin where VARIABLE_TYPE didn't exist
            variableType = VariableType.JOB;
        }

        if (Boolean.valueOf(configurationMap.get(STRIP_SNAPSHOT))) {
            stripSnaphost = true;
        }
    }

    public CommonTaskContext getTaskContext() {
        return taskContext;
    }

    public BuildLogger getBuildLogger() {
        return taskContext.getBuildLogger();
    }

    public boolean isCustomProjectFile() {
        return !StringUtils.isEmpty(projectFile);
    }

    public String getProjectFile() {
        return projectFile;
    }

    public File getBaseDir() {
        return taskContext.getRootDirectory();
    }

    public VariableType getVariableType() {
        return variableType;
    }

    public boolean areVariablesOfType(VariableType otherVariableType) {
        return variableType.equals(otherVariableType);
    }

    public boolean isCustomPrefix() {
        return !StringUtils.isEmpty(customPrefix);
    }

    public String getCustomPrefix() {
        return customPrefix;
    }

    public boolean isCustomExtract() {
        return customExtract;
    }

    public String getCustomVariableName() {
        return customVariableName;
    }

    public String getCustomElement() {
        return customElement;
    }

    public boolean isStripSnaphost() {
        return stripSnaphost;
    }

}
