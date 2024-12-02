package com.sparrowwallet.larkapp.args;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparrowwallet.drongo.OutputDescriptor;
import com.sparrowwallet.drongo.psbt.PSBT;
import com.sparrowwallet.lark.Lark;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Parameters(commandDescription = "Sign a PSBT")
public class SignTxCommand extends AbstractCommand{
    @Parameter(description = "psbt", required = true, arity = 1)
    public List<String> psbt;

    @Override
    public String getName() {
        return "signtx";
    }

    @Override
    public void run(JCommander jCommander, Lark lark, Args args) throws Exception {
        super.run(jCommander, lark, args);

        String strUnsignedPsbt = psbt.getFirst();
        PSBT unsignedPsbt;
        try {
            unsignedPsbt = PSBT.fromString(strUnsignedPsbt);
        } catch(Exception e) {
            error(e.getMessage());
            return;
        }

        Map<OutputDescriptor, byte[]> existingRegistrations = new HashMap<>(lark.getWalletRegistrations());
        PSBT signedPsbt;
        if(args.devicePath != null) {
            signedPsbt = lark.signTransaction(args.deviceType, args.devicePath, unsignedPsbt);
        } else if(args.fingerprint != null) {
            signedPsbt = lark.signTransaction(args.fingerprint, unsignedPsbt);
        } else {
            signedPsbt = lark.signTransaction(args.deviceType, unsignedPsbt);
        }

        String strSignedPSBT = signedPsbt.toBase64String();
        Map<String, WalletRegistration> newRegistrations = getNewRegistrations(lark, existingRegistrations);
        value(new PSBTValue(strSignedPSBT, !strSignedPSBT.equals(strUnsignedPsbt), newRegistrations));
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private record PSBTValue(String psbt, boolean signed, Map<String, WalletRegistration> registrations) {}
}
