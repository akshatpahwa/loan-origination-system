package com.bank.loanorigination.model;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "managers")
public class Manager {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String phone;

    public Manager() {}
    public Manager(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}