package com.sparrowwallet.larkapp.args;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.sparrowwallet.drongo.ExtendedKey;
import com.sparrowwallet.drongo.KeyDerivation;
import com.sparrowwallet.lark.Lark;

@Parameters(commandDescription = "Get the extended public key for BIP 44 standard derivation paths. Convenience function to get xpubs given the address type, account, and chain type.")
public class GetMasterXpubCommand extends AbstractCommand {
    @Parameter(names = { "--addr-type" }, description = "Get the master xpub used to derive addresses for this address type")
    public AddrType addrType = AddrType.wit;

    @Parameter(names = { "--account" }, description = "The account number")
    public int account = 0;

    @Override
    public String getName() {
        return "getmasterxpub";
    }

    @Override
    public void run(JCommander jCommander, Lark lark, Args args) throws Exception {
        super.run(jCommander, lark, args);

        String path = KeyDerivation.writePath(addrType.getScriptType().getDefaultDerivation(account));

        ExtendedKey xpub;
        if(args.devicePath != null) {
            xpub = lark.getPubKeyAtPath(args.deviceType, args.devicePath, path);
        } else if(args.fingerprint != null) {
            xpub = lark.getPubKeyAtPath(args.fingerprint, path);
        } else {
            xpub = lark.getPubKeyAtPath(args.deviceType, path);
        }

        value(new XpubValue(xpub.toString()));
    }
}
