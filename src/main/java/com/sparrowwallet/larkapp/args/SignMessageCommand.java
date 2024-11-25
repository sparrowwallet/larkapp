package com.sparrowwallet.larkapp.args;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.sparrowwallet.drongo.KeyDerivation;
import com.sparrowwallet.lark.Lark;

import java.util.List;

@Parameters(commandDescription = "Sign a message")
public class SignMessageCommand extends AbstractCommand {
    @Parameter(description = "message path", arity = 2)
    public List<String> params;

    @Override
    public String getName() {
        return "signmessage";
    }

    @Override
    public void run(JCommander jCommander, Lark lark, Args args) throws Exception {
        super.run(jCommander, lark, args);

        String message = params.getFirst();
        String path = params.getLast();
        try {
            path = KeyDerivation.writePath(KeyDerivation.parsePath(path));
        } catch(Exception e) {
            error("Invalid BIP32 path: " + path);
            return;
        }

        String signature;
        if(args.devicePath != null) {
            signature = lark.signMessage(args.deviceType, args.devicePath, message, path);
        } else if(args.fingerprint != null) {
            signature = lark.signMessage(args.fingerprint, message, path);
        } else {
            signature = lark.signMessage(args.deviceType, message, path);
        }

        value(new SignatureValue(signature));
    }

    private record SignatureValue(String signature) {}
}
