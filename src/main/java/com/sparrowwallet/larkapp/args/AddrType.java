package com.sparrowwallet.larkapp.args;

import com.sparrowwallet.drongo.protocol.ScriptType;

public enum AddrType {
    legacy(ScriptType.P2PKH), wit(ScriptType.P2WPKH), sh_wit(ScriptType.P2SH_P2WPKH), tap(ScriptType.P2TR);

    private final ScriptType scriptType;

    AddrType(ScriptType scriptType) {
        this.scriptType = scriptType;
    }

    public ScriptType getScriptType() {
        return scriptType;
    }
}
