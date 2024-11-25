package com.sparrowwallet.larkapp.args;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.sparrowwallet.lark.Lark;

import java.util.List;

@Parameters(commandDescription = "Send the numeric positions for your PIN to the device")
public class SendPinCommand extends AbstractCommand {
    @Parameter(description = "pin", required = true)
    public List<String> pin;

    @Override
    public String getName() {
        return "sendpin";
    }

    @Override
    public void run(JCommander jCommander, Lark lark, Args args) throws Exception {
        super.run(jCommander, lark, args);

        boolean success;
        if(args.devicePath != null) {
            success = lark.sendPin(args.deviceType, args.devicePath, pin.getFirst());
        } else if(args.fingerprint != null) {
            success = lark.sendPin(args.fingerprint, pin.getFirst());
        } else {
            success = lark.sendPin(args.deviceType, pin.getFirst());
        }

        success(success);
    }
}
