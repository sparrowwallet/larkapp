package com.sparrowwallet.larkapp.args;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameters;
import com.sparrowwallet.lark.Lark;

@Parameters(commandDescription = "Have the device prompt for your PIN")
public class PromptPinCommand extends AbstractCommand {
    @Override
    public String getName() {
        return "promptpin";
    }

    @Override
    public void run(JCommander jCommander, Lark lark, Args args) throws Exception {
        super.run(jCommander, lark, args);

        boolean success;
        if(args.devicePath != null) {
            success = lark.promptPin(args.deviceType, args.devicePath);
        } else if(args.fingerprint != null) {
            success = lark.promptPin(args.fingerprint);
        } else {
            success = lark.promptPin(args.deviceType);
        }

        success(success);
    }
}
