/*
 * Copyright 2016 Red Hat Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.apiman.test.integration.ui.tests.apis.policies;

import static io.apiman.test.integration.ui.support.assertion.BeanAssert.assertNoPolicies;

import static com.codeborne.selenide.CollectionCondition.empty;

import io.apiman.test.integration.ui.support.selenide.pages.apis.detail.ApiPoliciesDetailPage;
import io.apiman.test.integration.ui.support.selenide.pages.policies.AddIPlistPolicyPage;

import org.junit.Test;

/**
 * @author jcechace
 */
public abstract class AbstractIPListPolicyIT extends AbstractApiPolicyIT {

    public static final String IP_1 = "128.0.0.1";
    public static final String IP_2 = "129.0.0.1";

    protected AddIPlistPolicyPage addPolicyPage;

    protected void addStartingConfiguration() {
        addPolicyPage
            .addAddress(IP_1)
            .addAddress(IP_2);
    }

    @Test
    public void canAddMultipleAddresses() {
        addStartingConfiguration();
        addPolicyPage
            .listedOptions().shouldHaveSize(2);
        addPolicyPage
            .addPolicy(ApiPoliciesDetailPage.class);
        assertPolicyPresent();
    }

    @Test
    public void canClearAddresses() {
        addStartingConfiguration();
        addPolicyPage.clearSelectList()
            .listedOptions().shouldBe(empty);
        addPolicyPage
            .addPolicy(ApiPoliciesDetailPage.class);
        assertPolicyPresent();
    }

    @Test
    public void canRemoveOneAddress() {
        addStartingConfiguration();
        addPolicyPage
            .removeOption(IP_1)
            .listedOptions().shouldHaveSize(1);
        addPolicyPage
            .addPolicy(ApiPoliciesDetailPage.class);
        assertPolicyPresent();
    }

    @Test
    public void canCancelConfiguration() {
        addPolicyPage
            .cancel(ApiPoliciesDetailPage.class);
        assertNoPolicies(apiVersions);
    }
}
