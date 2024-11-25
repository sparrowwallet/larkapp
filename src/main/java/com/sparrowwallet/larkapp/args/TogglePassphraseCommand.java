package com.sparrowwallet.larkapp.args;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameters;
import com.sparrowwallet.lark.Lark;

@Parameters(commandDescription = "Toggle BIP39 passphrase")
public class TogglePassphraseCommand extends AbstractCommand {
    @Override
    public String getName() {
        return "togglepassphrase";
    }

    @Override
    public void run(JCommander jCommander, Lark lark, Args args) throws Exception {
        super.run(jCommander, lark, args);

        boolean success;
        if(args.devicePath != null) {
            success = lark.togglePassphrase(args.deviceType, args.devicePath);
        } else if(args.fingerprint != null) {
            success = lark.togglePassphrase(args.fingerprint);
        } else {
            success = lark.togglePassphrase(args.deviceType);
        }

        success(success);
    }
}
