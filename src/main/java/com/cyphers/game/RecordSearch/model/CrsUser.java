package com.cyphers.game.RecordSearch.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "crs_user")
public class CrsUser {
    @Id
    private String userId;

    private String nickname;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
