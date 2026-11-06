package com.yunus.research.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "instrument_specifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstrumentSpecification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String instrumentId;

    @Column(nullable = false)
    private String label;

    @Column(nullable = false)
    private String value;

    @Column(nullable = false)
    private Integer sortOrder;
}
