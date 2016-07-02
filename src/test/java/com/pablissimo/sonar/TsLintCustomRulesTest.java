package com.pablissimo.sonar;

import com.pablissimo.sonar.model.TsLintRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.Settings;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TsLintCustomRulesTest {
    private Settings settings;
    private TsRulesDefinition rulesDef;

    @Before
    public void setUp() throws Exception {
        this.settings = mock(Settings.class);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void parsesSingleCustomRules() throws IOException {
        when(this.settings.getString(TypeScriptPlugin.SETTING_TS_LINT_CUSTOM_RULES_CONFIG))
            .thenReturn(
                "custom-rule-1=true\n" +
                "custom-rule-1.name=test rule #1\n" +
                "custom-rule-1.severity=MAJOR\n" +
                "custom-rule-1.description=#1 description\n" +
                "\n" +
                "custom-rule-2=true\n" +
                "custom-rule-2.name=test rule #2\n" +
                "custom-rule-2.severity=MINOR\n" +
                "custom-rule-2.description=#2 description\n" +
                "\n");

        this.rulesDef = new TsRulesDefinition(this.settings);

        final int numCustomRules = 2;

        assertEquals(this.rulesDef.getCustomRules().size(), numCustomRules);

        List<TsLintRule> customRules = this.rulesDef.getCustomRules();

        if (customRules.size() == numCustomRules) {
            TsLintRule ruleNo1 = customRules.get(0);
            TsLintRule ruleNo2 = customRules.get(1);

            assertEquals(ruleNo1.key, "custom-rule-1");
            assertEquals(ruleNo1.name, "test rule #1");
            assertEquals(ruleNo1.severity, "MAJOR");
            assertEquals(ruleNo1.htmlDescription, "#1 description");

            assertEquals(ruleNo2.key, "custom-rule-2");
            assertEquals(ruleNo2.name, "test rule #2");
            assertEquals(ruleNo2.severity, "MINOR");
            assertEquals(ruleNo2.htmlDescription, "#2 description");
        }
    }

    @Test
    public void disabledCustomRule() throws IOException {
        when(this.settings.getString(TypeScriptPlugin.SETTING_TS_LINT_CUSTOM_RULES_CONFIG))
            .thenReturn(
                "custom-rule-1=false\n" +
                "custom-rule-1.name=test rule #1\n" +
                "custom-rule-1.severity=MAJOR\n" +
                "custom-rule-1.description=#1 description\n" +
                "\n" +
                "custom-rule-2=true\n" +
                "custom-rule-2.name=test rule #2\n" +
                "custom-rule-2.severity=MINOR\n" +
                "custom-rule-2.description=#2 description\n" +
                "\n");

        this.rulesDef = new TsRulesDefinition(this.settings);

        final int numCustomRules = 1;

        assertEquals(this.rulesDef.getCustomRules().size(), numCustomRules);

        List<TsLintRule> customRules = this.rulesDef.getCustomRules();

        if (customRules.size() == numCustomRules) {
            TsLintRule ruleNo2 = customRules.get(0);

            assertEquals(ruleNo2.key, "custom-rule-2");
            assertEquals(ruleNo2.name, "test rule #2");
            assertEquals(ruleNo2.severity, "MINOR");
            assertEquals(ruleNo2.htmlDescription, "#2 description");
        }
    }

    @Test
    public void parsesEmptyCustomRules() throws IOException {
        when(this.settings.getString(TypeScriptPlugin.SETTING_TS_LINT_CUSTOM_RULES_CONFIG))
            .thenReturn("#empty config\n");

        this.rulesDef = new TsRulesDefinition(this.settings);

        final int numCustomRules = 0;

        assertEquals(this.rulesDef.getCustomRules().size(), numCustomRules);
    }
}
