package com.sparrowwallet.larkapp;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparrowwallet.drongo.*;
import com.sparrowwallet.lark.DeviceException;
import com.sparrowwallet.lark.Lark;
import com.sparrowwallet.larkapp.args.*;
import org.slf4j.Logger;
import org.slf4j.event.Level;

import java.util.List;
import java.util.Locale;

public class LarkCli {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(LarkCli.class);

    public static final String APP_NAME = "Lark";
    public static final Version APP_VERSION = new Version("0.9");

    public static void main(String[] argv) throws Exception {
        Lark.setConsoleOutput(true);

        List<Command> commands = List.of(
                new EnumerateCommand(),
                new PromptPinCommand(),
                new SendPinCommand(),
                new GetXpubCommand(),
                new GetMasterXpubCommand(),
                new SignTxCommand(),
                new SignMessageCommand(),
                new DisplayAddressCommand(),
                new TogglePassphraseCommand());

        Args args = new Args();
        JCommander.Builder jCommanderBuilder = JCommander.newBuilder()
                .addObject(args)
                .programName(OsType.getCurrent() == OsType.MACOS ? APP_NAME : APP_NAME.toLowerCase(Locale.ROOT));
        for(Command command : commands) {
            jCommanderBuilder.addCommand(command.getName(), command);
        }
        JCommander jCommander = jCommanderBuilder.build();

        try {
            jCommander.parse(argv);
        } catch(ParameterException e) {
            showErrorAndExit(e.getMessage());
        }

        if(args.help) {
            jCommander.usage();
            System.exit(0);
        }

        if(args.version) {
            System.out.println(APP_NAME + " " + APP_VERSION);
            System.exit(0);
        }

        if(args.level != null) {
            Drongo.setRootLogLevel(args.level);
        } else if(args.debug) {
            Drongo.setRootLogLevel(Level.DEBUG);
        }

        if(args.network != null) {
            Network.set(args.network);
        } else if(args.chain != null) {
            Network.set(args.chain.getNetwork());
        }

        Lark lark = new Lark();
        if(args.passphrase != null) {
            lark.setPassphrase(args.passphrase);
        }

        if(args.walletRegistration != null) {
            if(args.walletDescriptor == null || args.walletName == null) {
                System.err.println("If `--wallet-registration` is provided, `--wallet-descriptor` and `--wallet-name` must also be provided");
                System.exit(1);
            }
            OutputDescriptor walletDescriptor = getWalletDescriptor(args);
            byte[] walletRegistration = getWalletRegistration(args);
            lark.addWalletRegistration(walletDescriptor, args.walletName, walletRegistration);
        } else if(args.walletName != null) {
            if(args.walletDescriptor == null) {
                System.err.println("If `--wallet-name` is provided, `--wallet-descriptor` must also be provided");
                System.exit(1);
            }
            OutputDescriptor walletDescriptor = getWalletDescriptor(args);
            lark.addWalletName(walletDescriptor, args.walletName);
        }

        if(jCommander.getParsedCommand() == null) {
            jCommander.usage();
        } else {
            try {
                for(Command command : commands) {
                    if(command.getName().equals(jCommander.getParsedCommand())) {
                        command.run(jCommander, lark, args);
                    }
                }
            } catch(DeviceException e) {
                showErrorAndExit(e.getMessage());
            }
        }
    }

    private static OutputDescriptor getWalletDescriptor(Args args) {
        try {
            return OutputDescriptor.getOutputDescriptor(args.walletDescriptor);
        } catch(Exception e) {
            System.err.println("Invalid wallet descriptor: " + e.getMessage());
            System.exit(1);
            return null;
        }
    }

    private static byte[] getWalletRegistration(Args args) {
        try {
            return Utils.hexToBytes(args.walletRegistration);
        } catch(Exception e) {
            System.err.println("Invalid wallet registration: " + e.getMessage());
            System.exit(1);
            return null;
        }
    }

    public static void showSuccess(boolean success) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            System.out.println(objectMapper.writeValueAsString(new Success(success)));
        } catch(JsonProcessingException e) {
            log.error("Failed to serialize error", e);
        }
    }

    public static void showValue(Object value) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            System.out.println(objectMapper.writeValueAsString(value));
        } catch(JsonProcessingException e) {
            log.error("Failed to serialize error", e);
        }
    }

    public static void showErrorAndExit(String errorMessage) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            System.err.println(objectMapper.writeValueAsString(new Error(errorMessage)));
            System.exit(1);
        } catch(JsonProcessingException e) {
            log.error("Failed to serialize error", e);
        }
    }

    private record Success(boolean success) {
    }

    private record Error(String error) {
    }
}
