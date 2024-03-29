package com.ubc.atlassian.plugin.atlassian.bamboo.maven;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.plan.Plan;
import com.atlassian.bamboo.plan.PlanKeys;
import com.atlassian.bamboo.plan.PlanManager;
import com.atlassian.bamboo.variable.VariableDefinition;
import com.atlassian.bamboo.variable.VariableDefinitionImpl;
import com.atlassian.bamboo.variable.VariableDefinitionManager;
import com.atlassian.bamboo.variable.VariableType;

import java.util.List;


public class BambooVariableManager {

    private final PlanManager planManager;
    private final VariableDefinitionManager variableDefinitionManager;
    private final BuildLogger buildLogger;

    public BambooVariableManager(PlanManager planManager,
                                 VariableDefinitionManager variableDefinitionManager,
                                 BuildLogger buildLogger) {
        this.planManager = planManager;
        this.variableDefinitionManager = variableDefinitionManager;
        this.buildLogger = buildLogger;
    }

    public void addOrUpdateVariables(String topLevelPlanKey,
                                     List<Variable> variables) {
        Plan plan = planManager.getPlanByKey(PlanKeys
                .getPlanKey(topLevelPlanKey));

        List<VariableDefinition> planVariables = getVariablesForPlan(topLevelPlanKey);
        for (Variable variable : variables) {
            VariableDefinition variableDefinition = null;
            String name = variable.getName();
            String value = variable.getValue();
            for (VariableDefinition v : planVariables) {
                if (v.getKey().equals(name)) {
                    variableDefinition = v;
                }
            }

            if (variableDefinition == null) {
                variableDefinition = new VariableDefinitionImpl();
                buildLogger.addBuildLogEntry("Adding Plan variable " + name
                        + ":" + value);
            } else {
                buildLogger.addBuildLogEntry("Updaing Plan variable from "
                        + name + ":" + variableDefinition.getValue() + " to "
                        + name + ":" + value);
            }
            variableDefinition.setPlan(plan);
            variableDefinition.setVariableType(VariableType.PLAN);
            variableDefinition.setKey(name);
            variableDefinition.setValue(value);

            variableDefinitionManager
                    .saveVariableDefinition(variableDefinition);
        }
    }

    private List<VariableDefinition> getVariablesForPlan(final String planKey) {
        Plan planByKey = planManager.getPlanByKey(PlanKeys.getPlanKey(planKey));
        return variableDefinitionManager.getPlanVariables(planByKey);
    }
}
