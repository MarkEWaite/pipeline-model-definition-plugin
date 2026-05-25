package org.jenkinsci.plugins.pipeline.modeldefinition.actions;

import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsSessionRule;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RestartDeclarativePipelineActionRestartTest {

    @Rule public JenkinsSessionRule jsr = new JenkinsSessionRule();

    @Test
    public void restartEnabledAfterJenkinsRestart() throws Throwable {
        jsr.then(r -> {
            WorkflowJob p = r.jenkins.createProject(WorkflowJob.class, "p");
            p.setDefinition(new CpsFlowDefinition(
                    "pipeline { agent any; stages { stage('s') { steps { echo 'hi' } } } }", true));
            r.buildAndAssertSuccess(p);
        });
        jsr.then(r -> {
            WorkflowJob p = r.jenkins.getItemByFullName("p", WorkflowJob.class);
            WorkflowRun b = p.getBuildByNumber(1);
            RestartDeclarativePipelineAction action = b.getAction(RestartDeclarativePipelineAction.class);
            assertNotNull("action should be present", action);
            assertTrue("restart should be enabled after Jenkins restart", action.isRestartEnabled());
        });
    }
}
