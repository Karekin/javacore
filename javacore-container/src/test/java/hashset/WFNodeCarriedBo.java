package hashset;

import lombok.Data;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * WFNodeCarriedBo：被节点携带的对象、挂载，表示一个与任务流节点相关联并被节点携带的对象。
 * WFNodeFlowDimBo 与 WFNodeCarriedBo 的区别：前者决定任务的流转，后者参与或影响任务的流转
 * 这里的对象指：表单、规则、附件等一切可以参与流转的、非流程维的配置项。
 * 后续可根据 type 字段区分不同的对象类型
 */
@Data
class WFNodeCarriedBo {
    // 为了jackson序列化使用，标识唯一一个对象
    private String identity;
    // 节点所携带对象的编码，例如表单编码、规则编码、附件编码等
    private String carriedCode;
    // 对象所属节点的id
    private String nodeId;
    // 流程维的成员编码
    private String dimCode;

    // 下一个节点中，符合流程维走向、对象流转关系的节点-配置项
    private Set<WFNodeCarriedBo> nextNodeCarriedBo = new HashSet<>();
    // 前一个节点中，符合流程维走向、对象流转关系的节点-配置项
    private Set<WFNodeCarriedBo> prevNodeCarriedBo = new HashSet<>();
    // 节点在子图中所处的层级（子图级的属性）

    public WFNodeCarriedBo(String carriedCode, String nodeId, String dimCode) {
        this.carriedCode = carriedCode;
        this.nodeId = nodeId;
        this.dimCode = dimCode;
        this.identity = carriedCode + "_" + nodeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WFNodeCarriedBo that = (WFNodeCarriedBo) o;
        return carriedCode.equals(that.carriedCode)
                && nodeId.equals(that.nodeId)
                && dimCode.equals(that.dimCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(carriedCode, nodeId, dimCode);
    }
}
