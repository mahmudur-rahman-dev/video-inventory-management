package global.inventory.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "videos")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Video extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotBlank
    @Column(name = "video_url", nullable = false)
    private String videoUrl;

    @OneToMany(mappedBy = "video", fetch = FetchType.LAZY)
    private Set<VideoAssignment> videoAssignments = new HashSet<>();

    private boolean deleted = false;
}