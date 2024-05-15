package com.brisage.authentication.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="role")
public class Role {
    @Id
    @Column(name = "nom_role")
    private String nomRole;

    @Column(name = "priority")
    private int priority;
}
