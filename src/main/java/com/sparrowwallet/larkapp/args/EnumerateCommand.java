package com.sparrowwallet.larkapp.args;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparrowwallet.lark.HardwareClient;
import com.sparrowwallet.lark.Lark;

import java.util.List;

@Parameters(commandDescription = "List all available devices")
public class EnumerateCommand extends AbstractCommand {
    public static final String NAME = "enumerate";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void run(JCommander jCommander, Lark lark, Args args) throws Exception {
        super.run(jCommander, lark, args);

        List<HardwareClient> clients = lark.enumerate();
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(clients.stream().map(EnumeratedDevice::new).toList()));
    }
}
