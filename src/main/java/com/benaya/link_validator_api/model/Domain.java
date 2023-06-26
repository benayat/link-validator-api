package com.benaya.link_validator_api.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "domains")
@Data
@Builder
public class Domain {
    @Id
    @Indexed(unique = true)
    private String name;
    @Field("urls")
    private List<String> urls;
}
