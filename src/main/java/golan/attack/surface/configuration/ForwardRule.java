package golan.attack.surface.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"fw_id", "source_tag", "tags"})
public class ForwardRule {
    @JsonProperty("fw_id")
    private String fwId;
    @JsonProperty("source_tag")
    private String sourceTag;
    @JsonProperty("dest_tag")
    private String destTag;
}

