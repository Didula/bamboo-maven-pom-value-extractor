package com.ubc.atlassian.plugin.atlassian.bamboo.maven;

import com.atlassian.bamboo.build.BuildLoggerManager;
import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.plan.PlanKeys;
import com.atlassian.bamboo.plan.PlanManager;
import com.atlassian.bamboo.plan.PlanResultKey;
import com.atlassian.bamboo.v2.build.agent.messages.AbstractBambooAgentMessage;
import com.atlassian.bamboo.variable.VariableDefinitionManager;

import java.util.List;

public class CreateOrUpdateVariableMessage extends AbstractBambooAgentMessage {

    private final String topLevelPlanKey;
    private final String buildResultKey;
    private final List<Variable> variables;

    public CreateOrUpdateVariableMessage(String topLevelPlanKey,
                                         String buildResultKey, List<Variable> variables) {
        this.topLevelPlanKey = topLevelPlanKey;
        this.buildResultKey = buildResultKey;
        this.variables = variables;
    }

    @Override
    public Object deliver() {
        PlanManager planManager = getComponent(PlanManager.class, "planManager");
        VariableDefinitionManager variableDefinitionManager = getComponent(
                VariableDefinitionManager.class, "variableDefinitionManager");
        BuildLoggerManager buildLoggerManager = getComponent(
                BuildLoggerManager.class, "buildLoggerManager");

        PlanResultKey planResultKey = PlanKeys.getPlanResultKey(buildResultKey);

        BuildLogger buildLogger = buildLoggerManager
                .getLogger(planResultKey);

        BambooVariableManager manager = new BambooVariableManager(planManager,
                variableDefinitionManager, buildLogger);
        manager.addOrUpdateVariables(topLevelPlanKey, variables);

        return null;
    }

}
