package com.sparrowwallet.larkapp.args;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.sparrowwallet.drongo.OutputDescriptor;
import com.sparrowwallet.drongo.Utils;
import com.sparrowwallet.lark.Lark;
import com.sparrowwallet.larkapp.LarkCli;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractCommand implements Command {
    @Parameter(names = { "--help", "-h" }, description = "Show this help message and exit", help = true)
    public boolean help;

    @Override
    public void run(JCommander jCommander, Lark lark, Args args) throws Exception {
        if(help) {
            jCommander.usage(getName());
            System.exit(0);
        }
        if(!EnumerateCommand.NAME.equals(getName())) {
            if(args.deviceType == null && args.fingerprint == null) {
                error("You must specify a device type or fingerprint for all commands except enumerate");
            }
        }
    }

    protected Map<String, WalletRegistration> getNewRegistrations(Lark lark, Map<OutputDescriptor, byte[]> existingRegistrations) {
        Map<OutputDescriptor, byte[]> newRegistrations = new HashMap<>(lark.getWalletRegistrations());
        newRegistrations.keySet().removeAll(existingRegistrations.keySet());
        return newRegistrations.entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().toString(), entry -> new WalletRegistration(lark.getWalletName(entry.getKey()), Utils.bytesToHex(entry.getValue()))));
    }

    protected void success(boolean success) {
        LarkCli.showSuccess(success);
    }

    protected void value(Object value) {
        LarkCli.showValue(value);
    }

    protected void error(String errorMessage) {
        LarkCli.showErrorAndExit(errorMessage);
    }

    protected record XpubValue(String xpub) {}

    protected record WalletRegistration(String name, String registration) {}
}
