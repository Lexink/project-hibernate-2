package com.javarush.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table (name = "store", schema = "movie")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Byte id;

    @OneToOne
    @JoinColumn(name = "manager_staff_id")
    private Staff managerStaff;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;


    @Column(name = "last_update")
    @UpdateTimestamp
    private LocalDateTime lastUpdate;
}
