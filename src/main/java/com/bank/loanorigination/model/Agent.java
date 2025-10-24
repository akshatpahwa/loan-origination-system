package com.bank.loanorigination.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "agents")
public class Agent {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String phone;
    private boolean isAvailable = true;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    private LocalDateTime lastAssignedAt;

    private LocalDateTime createdAt;

    @Version
    private Long version;

    public Agent() {}
    public Agent(String name, String phone, Manager manager) {
        this.name = name;
        this.phone = phone;
        this.manager = manager;
    }

    // Getters & Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    public Manager getManager() { return manager; }
    public void setManager(Manager manager) { this.manager = manager; }
    public LocalDateTime getLastAssignedAt() { return lastAssignedAt; }
    public void setLastAssignedAt(LocalDateTime lastAssignedAt) { this.lastAssignedAt = lastAssignedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}
