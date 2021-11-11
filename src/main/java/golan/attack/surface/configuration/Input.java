package golan.attack.surface.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonPropertyOrder({"vms", "fw_rules"})
public class Input {
    @JsonProperty("vms")
    private List<VirtualMachine> vms;
    @JsonProperty("fw_rules")
    private List<ForwardRule> fwRules;
}


