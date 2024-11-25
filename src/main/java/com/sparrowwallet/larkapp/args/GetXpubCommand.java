package com.sparrowwallet.larkapp.args;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.sparrowwallet.drongo.ExtendedKey;
import com.sparrowwallet.drongo.KeyDerivation;
import com.sparrowwallet.lark.Lark;

import java.util.List;

@Parameters(commandDescription = "Get an extended public key derived at a BIP 32 derivation path")
public class GetXpubCommand extends AbstractCommand {
    @Parameter(description = "path", required = true)
    public List<String> path;

    @Override
    public String getName() {
        return "getxpub";
    }

    @Override
    public void run(JCommander jCommander, Lark lark, Args args) throws Exception {
        super.run(jCommander, lark, args);

        String xpubPath;
        try {
            xpubPath = KeyDerivation.writePath(KeyDerivation.parsePath(path.getFirst()));
        } catch(Exception e) {
            error("Invalid BIP32 path: " + path.getFirst());
            return;
        }

        ExtendedKey xpub;
        if(args.devicePath != null) {
            xpub = lark.getPubKeyAtPath(args.deviceType, args.devicePath, xpubPath);
        } else if(args.fingerprint != null) {
            xpub = lark.getPubKeyAtPath(args.fingerprint, xpubPath);
        } else {
            xpub = lark.getPubKeyAtPath(args.deviceType, xpubPath);
        }

        value(new XpubValue(xpub.toString()));
    }
}
