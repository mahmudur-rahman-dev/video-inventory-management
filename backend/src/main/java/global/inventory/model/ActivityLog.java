package global.inventory.model;

import global.inventory.enums.ActivityAction;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "activity_logs", indexes = {
        @Index(name = "idx_user_video", columnList = "user_id,video_id"),
        @Index(name = "idx_action_timestamp", columnList = "action,timestamp")
})
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", nullable = false)
    private Video video;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ActivityAction action;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(length = 500)
    private String details;
}