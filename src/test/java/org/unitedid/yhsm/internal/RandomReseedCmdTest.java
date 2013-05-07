/*
 * Copyright (c) 2011 - 2013 United ID.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.unitedid.yhsm.internal;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.unitedid.yhsm.SetupCommon;

import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertTrue;

public class RandomReseedCmdTest extends SetupCommon {

    @BeforeTest
    public void setUp() throws Exception {
        super.setUp();
    }

    @AfterTest
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testRandomReseed() throws YubiHSMCommandFailedException, YubiHSMErrorException, YubiHSMInputException {
        assertTrue(hsm.randomReseed("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
        assertNotSame(hsm.getRandom(10), hsm.getRandom(10));
    }

    @Test(expectedExceptions = YubiHSMInputException.class)
    public void testRandomReseedInputException() throws YubiHSMCommandFailedException, YubiHSMErrorException, YubiHSMInputException {
        hsm.randomReseed("AAAA");
    }
}
