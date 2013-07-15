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
import org.unitedid.yhsm.utility.ModHex;

import static org.testng.Assert.*;

public class KeyStorageUnlockTest extends SetupCommon {

    @BeforeTest
    public void setUp() throws Exception {
        super.setUp();
    }

    @AfterTest
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test(priority = 1)
    public void failedUnlockHsm() throws YubiHSMCommandFailedException, YubiHSMErrorException, YubiHSMInputException {
        assertFalse(hsm.unlock("1111"));
    }

    @Test(priority = 2)
    public void unlockHsm() throws Exception {
        assertTrue(hsm.unlock(hsmPassPhrase));
    }

    @Test(priority = 3)
    public void otpUnlockHsm() throws Exception {
        /* order is crucial here, that's why these are not made into separate tests */
        String yubiKeyPublicId = ModHex.decode(adminYubikey);

        if (hsm.getInfo().getMajorVersion() > 0) {
            /* Incorrect public id */
            try {
                hsm.unlockOtp("010000000000", "ffaaffaaffaaffaaffaaffaaffaaffaa");
                fail("unlockOtp not expected to work");
            } catch (YubiHSMCommandFailedException e) {
                assertEquals("Command YSM_HSM_UNLOCK failed: YSM_INVALID_PARAMETER", e.getMessage());
            }

            /* Right public id, wrong OTP */
            assertFalse(hsm.unlockOtp(yubiKeyPublicId, "ffaaffaaffaaffaaffaaffaaffaaffaa"));

            /* Right public id, right OTP (for counter values 00002/001) */
            assertTrue(hsm.unlockOtp(yubiKeyPublicId, "b2eabea788e74b233e4c847a619c874a"));

            /* Replay, will lock the HSM again */
            try {
                hsm.unlockOtp(yubiKeyPublicId, "b2eabea788e74b233e4c847a619c874a");
                fail("unlockOtp with same OTP not expected to work");
            } catch (YubiHSMCommandFailedException e) {
                assertEquals("Command YSM_HSM_UNLOCK failed: YSM_OTP_REPLAY", e.getMessage());
            }

            /* Right public id, new OTP (counter values 00002/002) */
            assertTrue(hsm.unlockOtp(yubiKeyPublicId, "5cd779b6892bf84d683252c67d875244"));
        }
    }
}
