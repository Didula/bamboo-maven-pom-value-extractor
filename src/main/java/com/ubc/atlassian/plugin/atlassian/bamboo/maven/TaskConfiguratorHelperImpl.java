package com.ubc.atlassian.plugin.atlassian.bamboo.maven;

import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.task.TaskDefinition;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class TaskConfiguratorHelperImpl {
    public void populateTaskConfigMapWithActionParameters(@NotNull Map<String, String> config, @NotNull ActionParametersMap params, @NotNull Iterable<String> fieldsToCopy) {
        for (String key : fieldsToCopy) {
            config.put(key, params.getString(key));
        }
    }

    public void populateContextWithConfiguration(@NotNull Map<String, Object> context, @NotNull TaskDefinition taskDefinition, @NotNull Iterable<String> fieldsToCopy) {
        for (String key : fieldsToCopy) {
            context.put(key, taskDefinition.getConfiguration().get(key));
        }
    }
}
