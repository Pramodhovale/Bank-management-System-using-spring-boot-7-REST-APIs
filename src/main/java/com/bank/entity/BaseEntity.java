package com.bank.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
abstract public class BaseEntity {
    @Column(updatable = false)
    private LocalDateTime updatedOn;
    private LocalDateTime createdOn;

    @Column(updatable = false)
    private String updatedBy;
    private String createdBy;

   @PrePersist
    protected void onCreate() {
            LocalDateTime now = LocalDateTime.now();
            this.createdOn = now;
            this.updatedOn = now;

       if(this.createdBy == null) {
            this.createdBy = "SYSTEM";
       }
       this.updatedBy = this.createdBy;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedOn = LocalDateTime.now();

        if (this.updatedBy == null) {
            this.updatedBy = this.createdBy;
        }
    }
}

