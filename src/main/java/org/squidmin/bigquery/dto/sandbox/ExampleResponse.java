package org.squidmin.bigquery.dto.sandbox;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class ExampleResponse {

    private List<ExampleResponseItem> body;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> errors;

}
