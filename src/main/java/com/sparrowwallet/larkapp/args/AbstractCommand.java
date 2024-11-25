package com.sparrowwallet.larkapp.args;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.sparrowwallet.lark.Lark;
import com.sparrowwallet.larkapp.LarkCli;

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
}
