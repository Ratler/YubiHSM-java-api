/*
 * Copyright (c) 2011 United ID. All rights reserved.
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
 *
 * @author Stefan Wold <stefan.wold@unitedid.org>
 */

package org.unitedid.yhsm.internal;

import org.unitedid.yhsm.utility.Utils;

public class KeyStorageUnlockCmd {

    /** Constructor */
    private KeyStorageUnlockCmd() {}

    /**
     * Unlock the YubiHSM key storage using the HSM password.
     *
     * @param device the YubiHSM device
     * @param password the password in hex format (see output of automatic password generation during HSM configuration)
     * @return true if unlock was successful
     * @throws YubiHSMErrorException error exceptions
     * @throws YubiHSMInputException argument exceptions
     * @throws YubiHSMCommandFailedException command failed exception
     */
    public static boolean execute(DeviceHandler device, String password) throws YubiHSMErrorException, YubiHSMInputException, YubiHSMCommandFailedException {
        byte[] passwordBA = Utils.validateByteArray("password", Utils.hexToByteArray(password), Defines.YSM_BLOCK_SIZE, 0, Defines.YSM_BLOCK_SIZE);
        return parseResult(CommandHandler.execute(device, Defines.YSM_KEY_STORAGE_UNLOCK, passwordBA, true));
    }

    /**
     *  Parse the response from the YubiHSM for a previous command.
     *
     * @param result the result from the last command
     * @return boolean indicating success
     * @throws YubiHSMCommandFailedException command failed exception
     */
    private static boolean parseResult(byte[] result) throws YubiHSMCommandFailedException {
        if (result[0] == Defines.YSM_STATUS_OK) {
            return true;
        } else {
            throw new YubiHSMCommandFailedException("Command " + Defines.getCommandString(Defines.YSM_KEY_STORAGE_UNLOCK) + " failed: " + Defines.getCommandStatus(result[0]));
        }
    }
}
