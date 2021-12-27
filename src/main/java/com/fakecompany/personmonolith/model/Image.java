package com.fakecompany.personmonolith.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "image")
@JsonPropertyOrder({"id","description","name"})
@AllArgsConstructor @NoArgsConstructor
@Builder @Getter
@Setter @Generated
@ToString
public class Image {

    @Id
    private String id;
    private String imageUrl;
    private String imageName;
    private Integer personId;
}
