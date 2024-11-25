package com.sparrowwallet.larkapp.args;

import com.beust.jcommander.Parameter;
import com.sparrowwallet.drongo.Network;
import com.sparrowwallet.lark.Chain;
import org.slf4j.event.Level;

public class Args {
    @Parameter(names = { "--device-path", "-d" }, description = "Specify the device path of the device to connect to", defaultValueDescription = "None")
    public String devicePath;

    @Parameter(names = { "--device-type", "-t" }, description = "Specify the type of device that will be connected. If `--device-path` not given, the first device of this type enumerated is used.", defaultValueDescription = "None")
    public String deviceType;

    @Parameter(names = { "--passphrase", "--password", "-p" }, description = "Device passphrase if it has one", defaultValueDescription = "None", password = true)
    public String passphrase;

    @Parameter(names = { "--network", "-n" }, description = "Select network to work with")
    public Network network;

    @Parameter(names = { "--chain" }, description = "Legacy alternative to select network to work with. If `--network` is provided it takes priority")
    public Chain chain;

    @Parameter(names = { "--level", "-l" }, description = "Set log level")
    public Level level;

    @Parameter(names = { "--debug" }, description = "Set log level to debug. If `--level` is provided it takes priority")
    public boolean debug;

    @Parameter(names = { "--fingerprint", "-f" }, description = "Specify the device to connect to using the first 4 bytes of the hash160 of the master public key. It will connect to the first device that matches this fingerprint.")
    public byte[] fingerprint;

    @Parameter(names = { "--version" }, description = "Show program's version number and exit")
    public boolean version;

    @Parameter(names = { "--emulators" }, description = "Enable enumeration and detection of device emulators")
    public boolean emulators;

    @Parameter(names = { "--wallet-desc", "-w" }, description = "Output descriptor of the wallet (used to set the wallet name)")
    public String walletDescriptor;

    @Parameter(names = { "--wallet-name" }, description = "Name of the wallet. `--wallet-desc` must also be provided")
    public String walletName;

    @Parameter(names = { "--wallet-registration" }, description = "Registration identifier of the wallet provided in hex. `--wallet-desc` and `--wallet-name` must also be provided. For Ledger, this is the wallet policy HMAC")
    public String walletRegistration;

    @Parameter(names = { "--help", "-h" }, description = "Show this help message and exit", help = true)
    public boolean help;
}
