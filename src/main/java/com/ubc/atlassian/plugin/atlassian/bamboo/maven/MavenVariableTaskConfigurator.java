package com.ubc.atlassian.plugin.atlassian.bamboo.maven;


import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.task.AbstractTaskConfigurator;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

import static com.ubc.atlassian.plugin.atlassian.bamboo.maven.TaskConfiguration.*;

public class MavenVariableTaskConfigurator extends AbstractTaskConfigurator {

    //TODO Implement TextProvider @link https://struts.apache.org/maven/struts2-core/apidocs/index.html to get values from properties file.
    private static final Log LOG = LogFactory.getLog(MavenVariableTaskConfigurator.class);
    protected TaskConfiguratorHelperImpl taskConfiguratorHelper = new TaskConfiguratorHelperImpl();

    private static final List<String> FIELDS_TO_COPY = ImmutableList.of(PROJECT_FILE, EXTRACT_MODE, VARIABLE_TYPE,
            PREFIX_OPTION, PREFIX_OPTION_CUSTOM_VALUE, CUSTOM_VARIABLE_NAME, CUSTOM_ELEMENT, STRIP_SNAPSHOT);

    @NotNull
    @Override
    public Map<String, String> generateTaskConfigMap(@NotNull final ActionParametersMap params,
                                                     @Nullable final TaskDefinition previousTaskDefinition) {
        final Map<String, String> config = super.generateTaskConfigMap(params, previousTaskDefinition);
        taskConfiguratorHelper.populateTaskConfigMapWithActionParameters(config, params, FIELDS_TO_COPY);
        return config;
    }

    @Override
    public void populateContextForCreate(@NotNull final Map<String, Object> context) {
        super.populateContextForCreate(context);
        context.put(EXTRACT_MODE, EXTRACT_MODE_GAV);
        context.put(VARIABLE_TYPE, VARIABLE_TYPE_RESULT);
        context.put(PREFIX_OPTION, PREFIX_OPTION_DEFAULT);
        populateContextForAll(context);
    }

    @Override
    public void populateContextForEdit(@NotNull final Map<String, Object> context,
                                       @NotNull final TaskDefinition taskDefinition) {
        super.populateContextForEdit(context, taskDefinition);
        taskConfiguratorHelper.populateContextWithConfiguration(context, taskDefinition, FIELDS_TO_COPY);
        populateContextForAll(context);

    }

    private void populateContextForAll(@NotNull final Map<String, Object> context) {
        Map<String, String> servers = Maps.newHashMap();
        servers.put(EXTRACT_MODE_CUSTOM, "Specify specific elements");
        servers.put(EXTRACT_MODE_GAV, "Extract GAV values");
        context.put("options", servers);

        Map<String, String> prefixOptions = Maps.newHashMap();
        prefixOptions.put(PREFIX_OPTION_DEFAULT, "Prefix variables with \"maven.\"");
        prefixOptions.put(PREFIX_OPTION_CUSTOM, "Use a custom prefix");
        context.put("prefixOptions", prefixOptions);

        Map<String, String> variableTypeOptions = Maps.newHashMap();
        variableTypeOptions.put(VARIABLE_TYPE_JOB, "Job");
        variableTypeOptions.put(VARIABLE_TYPE_RESULT, "Result");
        variableTypeOptions.put(VARIABLE_TYPE_PLAN, "Plan");
        context.put("variableTypeOptions", variableTypeOptions);
    }

    @Override
    public void populateContextForView(@NotNull final Map<String, Object> context,
                                       @NotNull final TaskDefinition taskDefinition) {
        super.populateContextForView(context, taskDefinition);
        taskConfiguratorHelper.populateContextWithConfiguration(context, taskDefinition, FIELDS_TO_COPY);
    }

    @Override
    public void validate(@NotNull final ActionParametersMap params, @NotNull final ErrorCollection errorCollection) {
        super.validate(params, errorCollection);

        String gavOrCustom = params.getString(EXTRACT_MODE);
        if (EXTRACT_MODE_CUSTOM.equals(gavOrCustom)) {
            String variableName = params.getString(CUSTOM_VARIABLE_NAME);
            String element = params.getString(CUSTOM_ELEMENT);
            if (StringUtils.isEmpty(variableName)) {
                errorCollection.addError(CUSTOM_VARIABLE_NAME, "A name for the variable is required.");
            }
            if (StringUtils.isEmpty(element)) {
                errorCollection.addError(CUSTOM_ELEMENT, "An element is required");
            }
        }
        if (LOG.isDebugEnabled()) {
            if (errorCollection.hasAnyErrors()) {
                LOG.debug("Submitted configuration has validation errors.");
            }
        }
    }
}
