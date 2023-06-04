package org.squidmin.bigquery.dto.other;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OtherResponse {

    private List<OtherResponseItem> body;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> errors;

}
