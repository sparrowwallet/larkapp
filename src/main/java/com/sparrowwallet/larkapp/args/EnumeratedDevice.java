package com.sparrowwallet.larkapp.args;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sparrowwallet.lark.HardwareClient;

import java.util.Locale;

@JsonPropertyOrder({"type", "path", "model", "label", "fingerprint", "needs_pin_sent", "needs_passphrase_sent", "error"})
class EnumeratedDevice {
    private final HardwareClient hardwareClient;

    public EnumeratedDevice(HardwareClient hardwareClient) {
        this.hardwareClient = hardwareClient;
    }

    public String getType() {
        return hardwareClient.getType().toLowerCase(Locale.ROOT);
    }

    public String getModel() {
        return hardwareClient.getProductModel().toLowerCase(Locale.ROOT);
    }

    public String getLabel() {
        return hardwareClient.getLabel();
    }

    public String getPath() {
        return hardwareClient.getPath();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getFingerprint() {
        return hardwareClient.fingerprint();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("needs_pin_sent")
    public Boolean isNeedsPinSent() {
        return hardwareClient.needsPinSent();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("needs_passphrase_sent")
    public Boolean isNeedsPassphraseSent() {
        return hardwareClient.needsPassphraseSent();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getError() {
        return hardwareClient.error();
    }
}
