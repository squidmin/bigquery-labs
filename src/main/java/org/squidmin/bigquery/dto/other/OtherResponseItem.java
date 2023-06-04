package org.squidmin.bigquery.dto.other;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.squidmin.bigquery.logger.Logger;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtherResponseItem {

    private String id;
    private String creationTimestamp;
    private String lastUpdateTimestamp;
    private String columnE;
    private String columnF;
    private String columnG;

    private Map<String, String> fields = new HashMap<>();

    public void setFromBigQueryResponse(String name, String value) {
        fields.put(name, value);
        if (name.equalsIgnoreCase("id")) {
            setId(value);
        } else if (name.equalsIgnoreCase("creation_timestamp")) {
            setCreationTimestamp(value);
        } else if (name.equalsIgnoreCase("last_update_timestamp")) {
            setLastUpdateTimestamp(value);
        } else if (name.equalsIgnoreCase("column_e")) {
            setColumnE(value);
        } else if (name.equalsIgnoreCase("column_f")) {
            setColumnF(value);
        } else if (name.equalsIgnoreCase("column_g")) {
            setColumnG(value);
        } else {
            Logger.log("Value associated with name is null.", Logger.LogType.ERROR);
        }
    }

    public String getValue(String name) {
        return fields.getOrDefault(name, "Not found: ".concat(name));
    }

}
