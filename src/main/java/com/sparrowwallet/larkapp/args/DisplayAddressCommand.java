package com.sparrowwallet.larkapp.args;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparrowwallet.drongo.KeyDerivation;
import com.sparrowwallet.drongo.OutputDescriptor;
import com.sparrowwallet.lark.Lark;

import java.util.HashMap;
import java.util.Map;

@Parameters(commandDescription = "Display an address")
public class DisplayAddressCommand extends AbstractCommand {
    @Parameter(names = { "--desc" }, description = "Output descriptor e.g. wpkh([00000000/84h/0h/0h]xpub.../0/0), where 00000000 must match --fingerprint and xpub can be obtained with getxpub")
    public String desc;

    @Parameter(names = { "--path" }, description = "The BIP 32 derivation path of the key embedded in the address")
    public String path;

    @Parameter(names = { "--addr-type" }, description = "The address type to display")
    public AddrType addrType = AddrType.wit;

    @Override
    public String getName() {
        return "displayaddress";
    }

    @Override
    public void run(JCommander jCommander, Lark lark, Args args) throws Exception {
        super.run(jCommander, lark, args);

        if(desc == null && path == null) {
            error("Either --desc or --path must be specified");
        }

        String address;
        Map<OutputDescriptor, byte[]> existingRegistrations = new HashMap<>(lark.getWalletRegistrations());
        if(desc != null) {
            OutputDescriptor outputDescriptor;
            try {
                outputDescriptor = OutputDescriptor.getOutputDescriptor(desc);
            } catch(Exception e) {
                error(e.getMessage());
                return;
            }

            if(args.devicePath != null) {
                address = lark.displayAddress(args.deviceType, args.devicePath, outputDescriptor);
            } else if(args.fingerprint != null) {
                address = lark.displayAddress(args.fingerprint, outputDescriptor);
            } else {
                address = lark.displayAddress(args.deviceType, outputDescriptor);
            }
        } else {
            try {
                path = KeyDerivation.writePath(KeyDerivation.parsePath(path));
            } catch(Exception e) {
                error("Invalid BIP32 path: " + path);
                return;
            }

            if(args.devicePath != null) {
                address = lark.displaySinglesigAddress(args.deviceType, args.devicePath, path, addrType.getScriptType());
            } else if(args.fingerprint != null) {
                address = lark.displaySinglesigAddress(args.fingerprint, path, addrType.getScriptType());
            } else {
                address = lark.displaySinglesigAddress(args.deviceType, path, addrType.getScriptType());
            }
        }

        Map<String, WalletRegistration> newRegistrations = getNewRegistrations(lark, existingRegistrations);
        value(new AddressValue(address, newRegistrations));
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private record AddressValue(String address, Map<String, WalletRegistration> registrations) {}
}
