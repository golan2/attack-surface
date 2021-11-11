package golan.attack.surface.service;

import golan.attack.surface.configuration.ForwardRule;
import golan.attack.surface.configuration.Input;
import golan.attack.surface.configuration.InputReader;
import golan.attack.surface.configuration.VirtualMachine;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
@Service
public class AttackResolver {
    private final Map<String, List<String>> tagsByVmId;      //VmId -> Tag[]
    private final Map<String, Set<String>> vmIdsByTag;      //Tag -> VmId[]  -->  reverse tagging
    private final Map<String, Set<String>> sourceTagsByDestTag;      //destTag -> sourceTag[]  -->  reverse rules

    public AttackResolver(InputReader inputReader) throws IOException {
        final Input input = inputReader.read();
        log.info("Configuration loaded successfully! (vms: {}) (rules: {})", input.getVms().size(), input.getFwRules().size());
        if (log.isDebugEnabled()) {
            log.debug("VMs:");
            input.getVms().forEach(vm -> log.debug(" - {} ({})", vm.getVmId(), vm.getName()));
            log.debug("Rules:");
            input.getFwRules().forEach(rule -> log.debug(" - {}", rule.getFwId()));
        }

        this.tagsByVmId = resolveTagsByVm(input);
        this.vmIdsByTag = resolveVmsByTag(input);
        this.sourceTagsByDestTag = resolveAllowedTags(input);
    }

    private Map<String, List<String>> resolveTagsByVm(Input input) {
        return input
                .getVms()
                .stream()
                .collect(Collectors.toMap(VirtualMachine::getVmId, VirtualMachine::getTags));
    }

    private Map<String, Set<String>> resolveVmsByTag(Input input) {
        return input
                .getVms()
                .stream()
                .flatMap(this::flattenToTags)
                .collect(groupByTag());
    }

    private Map<String, Set<String>> resolveAllowedTags(Input input) {
        return input
                .getFwRules()
                .stream()
                .collect(groupByDestTag());
    }

    private Collector<ForwardRule, ?, Map<String, Set<String>>> groupByDestTag() {
        return Collectors.groupingBy(
                ForwardRule::getDestTag,
                Collectors.mapping(ForwardRule::getSourceTag, Collectors.toSet())
        );
    }

    private Stream<TagVm> flattenToTags(VirtualMachine vm) {
        return vm.getTags()
                .stream()
                .map(tag -> new TagVm(vm.getVmId(), tag));
    }

    private Collector<TagVm, ?, Map<String, Set<String>>> groupByTag() {
        return Collectors.groupingBy(
                TagVm::getTag,
                Collectors.mapping(TagVm::getVmId, Collectors.toSet())
        );
    }

    /**
     * Given a Server, use tagsByVmId to resolve its Tag[]
     * For each Tag
     *     Use sourceTagsByDestTag to resolve the SourceTag[]
     *     For each SourceTag
     *         Use vmIdsByTag to get Server[]
     */
    public Set<String> resolve(String vmId) {
        log.debug("resolving attack on {}", vmId);
        final HashSet<String> result = new HashSet<>();
        final List<String> vmTags = tagsByVmId.getOrDefault(vmId, Collections.emptyList());
        log.debug("tags of {} are: {}", vmId, String.join(",", vmTags));
        for (String vmTag : vmTags) {
            final Set<String> sourceTags = sourceTagsByDestTag.get(vmTag);
            log.debug("Rules relevant for tag {}: ", vmTag);
            sourceTags.forEach(st -> log.debug("  - {} -> {}", st, vmTag));
            for (String sourceTag : sourceTags) {
                final Set<String> tags = vmIdsByTag.get(sourceTag);
                result.addAll(tags);
            }
        }
        return result;
    }

    @RequiredArgsConstructor
    @Getter
    private static class TagVm {
        private final String vmId;
        private final String tag;

    }
}
