package com.pragma.personmonolith.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "person")
@AllArgsConstructor @NoArgsConstructor
@Builder @Getter
@Setter @Generated
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String lastName;

    @Column(unique = true)
    private String identification;

    @Column(name = "identification_type_id")
    private Integer identificationTypeId;

    private Integer age;
    private String cityBirth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="identification_type_id", insertable = false, updatable = false, nullable = false)
    private IdentificationType identificationType;
}
