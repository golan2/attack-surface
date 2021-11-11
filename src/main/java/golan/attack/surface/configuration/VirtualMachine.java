package golan.attack.surface.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"vm_id", "name", "tags"})
@Getter
@Setter
@ToString
public class VirtualMachine {
    @JsonProperty("vm_id")
    private String vmId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("tags")
    private List<String> tags = null;
}

