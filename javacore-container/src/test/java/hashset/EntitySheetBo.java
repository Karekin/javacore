package hashset;

import lombok.Data;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
public class EntitySheetBo {
    String nodeId;
    String entity;
    Set<String> sheetId;

    public EntitySheetBo(String nodeId, String entity) {
        this.nodeId = nodeId;
        this.entity = entity;
        this.sheetId = new HashSet<>();
    }

    public EntitySheetBo(String nodeId, String entity, Set<String> sheetId) {
        this.nodeId = nodeId;
        this.entity = entity;
        this.sheetId = sheetId;
    }

    public EntitySheetBo copy() {
        Set<String> copiedSheetId = (this.sheetId != null) ? new HashSet<>(this.sheetId) : null;
        return new EntitySheetBo(this.nodeId, this.entity, copiedSheetId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntitySheetBo)) return false;
        EntitySheetBo that = (EntitySheetBo) o;
        return Objects.equals(nodeId, that.nodeId) && Objects.equals(entity, that.entity) && Objects.equals(sheetId, that.sheetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeId, entity, sheetId);
    }
}
