package ch.settlex.notification.domain;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String type;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(nullable = false, length = 120)
    private String recipient;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private boolean read = false;

    protected Notification() {
    }

    public Notification(String type, String message, String recipient) {
        this.type = type;
        this.message = message;
        this.recipient = recipient;
        this.createdAt = Instant.now();
        this.read = false;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getRecipient() {
        return recipient;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public boolean isRead() {
        return read;
    }

    public void markRead() {
        this.read = true;
    }
}
