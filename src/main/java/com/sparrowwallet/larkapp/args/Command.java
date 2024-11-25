package com.sparrowwallet.larkapp.args;

import com.beust.jcommander.JCommander;
import com.sparrowwallet.lark.Lark;

public interface Command {
    String getName();
    void run(JCommander jCommander, Lark lark, Args args) throws Exception;
}
